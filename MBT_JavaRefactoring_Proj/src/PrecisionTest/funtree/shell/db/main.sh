#!/bin/sh

#
# Main function
# @author MICKCHEN
#

workdir="$(pwd)"
logdir="${workdir}/log"
date_str="`date +'%Y-%m-%d_%H-%M-%S'`"
logfile="${logdir}/${date_str}.log"

. config.sh
. dbhandler.sh

function handle_busi()
{
	# insert into database
	businessname=$1

	sql="SELECT id FROM ${BUSITABLE} WHERE busi_name='${businessname}'"
	busiid=$(selectdb "${sql}")

	if [ "${busiid}" == "" ]
	then
		sql="INSERT INTO ${BUSITABLE} (busi_name) VALUES ('${businessname}')"
		querydb "${sql}"
		sql="SELECT id FROM ${BUSITABLE} WHERE busi_name='${businessname}'"
		busiid=$(selectdb "${sql}")
	fi

	echo ${busiid}
	return ${busiid}
}

function handle_model()
{
	modelname=$1
	busiid=$2

	sql="SELECT id FROM ${MODELTABLE} WHERE module_name='${modelname}' and parent_busi_id='"${busiid}"'"
	modelid=$(selectdb "${sql}")

	if [ "${modelid}" == "" ]
	then
		sql="INSERT INTO ${MODELTABLE} (module_name, parent_busi_id) VALUES ('${modelname}', ${busiid})"
		querydb "${sql}"
		sql="SELECT id FROM ${MODELTABLE} WHERE module_name='${modelname}' and parent_busi_id='"${busiid}"'"
		modelid=$(selectdb "${sql}")
	else
		sql="DELETE FROM ${FUNTABLE} WHERE module_id=${modelid}"
		querydb "${sql}"
		sql="DELETE FROM ${FUNRELATIONTB} WHERE module_id=${modelid}"
		querydb "${sql}"
	fi

	echo ${modelid}
	return ${modelid}
}

function handle_fun()
{
	datapath=$1
	modelid=$2
	if [ -e "${datapath}" ]
	then
		# copy of the data file
		#
		awk -F '\t' '{print $1"@"$2"@"$4}' ${datapath} | sort | uniq > ${datapath}.tmp
	
		while read -r LINE
		do
			funid=`echo ${LINE} | cut -d'@' -f 1`
			funname=`echo ${LINE} | cut -d'@' -f 2`
			childid=`echo ${LINE} | cut -d'@' -f 3`
			
			sql="SELECT count(fun_id) FROM ${FUNTABLE} WHERE fun_id=${funid}"
			number=$(selectdb "${sql}")

			if [ ${number} == 0 ]
			then
				sql="INSERT INTO ${FUNTABLE} (fun_id, fun_name, module_id) VALUES (${funid}, '${funname}', ${modelid})"
				querydb "${sql}"
				
				if [ $? != 0 ]
				then
					#continue
					return 1
				fi
			fi

			if [ "${childid}" == "0" ]
			then
				continue
			else
				sql="INSERT INTO ${FUNRELATIONTB} (child_fun_id, parent_fun_id, module_id) VALUES (${childid}, ${funid}, ${modelid})"
				querydb "${sql}"
				
				if [ $? != 0 ]
				then
					#continue
					return 1
				fi
			fi

		done < ${datapath}.tmp

		rm ${datapath}.tmp
		
	else
		return 1
	fi
}

function main()
{
	#handle business
	businessname=$1
	busiid=$(handle_busi ${businessname})
	
	if [ "${busiid}" == "" ]
	then
		echo "Fail to get business id."
		return 1
	fi
	
	# get model name
	modelname=$2
	modelid=$(handle_model ${modelname} ${busiid})
	
	if [ "${modelid}" == "" ]
	then
		echo "Fail to get model id."
		return 1
	fi
	
	# get data file path
	datapath=$3
	handle_fun ${datapath} ${modelid}
	
	if [ $? != 0 ]
	then
		echo "Fail to handle function and it's relation."
		return 1
	fi
	
	return 0
}

if [ 3 != $# ]
then
    echo "USAGE: sh main.sh business_name module_name file_path"
    exit
fi

businessname=$1
modelname=$2
datapath=$3
main ${businessname} ${modelname} ${datapath} 1>${logfile} 2>${logfile}.erro
if [ $? == 0 ]
then
	echo "Finished..."
else
	echo "Some error exist..."
fi
