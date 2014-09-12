#!/bin/bash

if [ 2 -ne $# ]
then
    echo "USAGE: gendot.sh datafile dotfile"
    exit
fi

DATA_FILE=$1
DOT_FILE=$2
TMP_FILE=/tmp/gendot.tmp

function main()
{
    echo "" > ${TMP_FILE}

    awk -F '\t' '{print $1"@"$2"@"$4}' ${DATA_FILE} | while read LINE
    do
        PARENT_ID=`echo ${LINE} | cut -d '@' -f 1`
        PARENT_NAME=`echo ${LINE} | cut -d '@' -f 2`
        CHILD_ID=`echo ${LINE} | cut -d '@' -f 3`
        CHILD_NAME=`awk -F '\t' '$1=="'$CHILD_ID'" {print $2}' ${DATA_FILE} | tail -n 1` 

        if [ 0 -ne ${CHILD_ID} ]
        then
            echo "    \""${PARENT_NAME}"\" -> \""${CHILD_NAME}"\" [color=black,style=bold];" >> ${TMP_FILE}   
        fi
    done

    cat ${TMP_FILE} | sort | uniq > ${TMP_FILE}

    echo "digraph" > ${DOT_FILE}
    echo "{" >> ${DOT_FILE}
    cat ${TMP_FILE} >> ${DOT_FILE}
    echo "}" >> ${DOT_FILE}
}

main

if [ $? == 0 ]
then
    echo "TRUE"
else
    echo "FALSE"
fi
