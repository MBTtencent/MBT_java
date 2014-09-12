#!/bin/bash
#
# Database handler
#

. config.sh

#
# Insert,update,delete
# @param sql
#
function querydb()
{	
	sql=$1

	${MYSQL} -A -u${USER} -p${PASSWD} -h${HOST} ${DB} -s -e\
	"
		${sql}
	"
	
	if [ $? != 0 ]
	then
		return 1
	else
		return 0
	fi
}

#
# Select 
# @param sql
#
function selectdb()
{
	sql=$1
	
	num=`${MYSQL} -A -u${USER} -p${PASSWD} -h${HOST} ${DB} -s -e\
	"
		${sql}
	"`
	echo "${num}"
	return ${num}
}
