#!/bin/bash 
######################################################################
#�ե�����̾��FAIB000001.sh
#��ǽ      ������IN���ޥ��������
#����      ���ʤ�
#�����    ��0������ 1���ٹ� 
#������    ������ ��§
#������    ��2006/03/18
#VER       ��1.00
#��������  ��YYYY/MM/DD
#               �ѹ����Ƥ򵭽�	
######################################################################

################################################################################
# ����̾    :�׻����������ޥ��������
# ����      :�ʤ�
# ����      :�ʤ�
################################################################################
. ${AOBA_HOME}/batch/exec/env/aoba_env.env
#. ${AOBA_HOME}/batch/exec/env/aoba_rate_env.env

################################################################################
JOBID=FAIB000001
JOBNAME=����IN���ޥ��������

################################################################################
INFILE=${SQL}/init_truc_in.txt
EXEFILE=${SQL}/init_truc_in.sql
SPOOLFILE=${SPOOL}/${JOBID}.lst

################################################################################
# �ե�����¸�ߥ����å�
# �ե����뤬¸�ߤ��ʤ���硧touch
################################################################################
LOG_FILE=${BATCH_LOG}
if [[ ! -a ${LOG_FILE} ]]
then
	touch ${LOG_FILE}
fi

if [[ ! -a ${SPOOLFILE} ]]
then
	touch ${SPOOLFILE}
fi

################################################################################
touch $EXEFILE
rm $EXEFILE
while read line; do
	echo "TRUNCATE TABLE "$line";" >> $EXEFILE
done < "$INFILE"

################################################################################
sqlplus ${ORALOGIN_BT}/${ORAPSWD_BT}@${ORACLE_SID_BT} <<EOF > /dev/null
SET NEWPAGE 0
SET PAGESIZE 0
SET FEEDBACK OFF
SET HEADING OFF
SET SPACE 0
SET TRIMS ON
SET LINESIZE 2000
SET ECHO OFF
SET TERMOUT OFF
SET VERIFY OFF
--
SPOOL ${SPOOLFILE}
@$EXEFILE
SPOOL off
--
EXIT
EOF

################################################################################
RET=$?
if [ ${RET_OK} -ne ${RET} ];
then
	exit ${RET_NG}
fi

#SPOOL�����å�
${COM}/put_oracle_error.sh ${SPOOLFILE} ${JOBID} ${PINT002}
RET=$?
if [ ${RET_OK} -ne ${RET} ];
then
	exit ${RET_NG}
fi

# ������
${COM}/put_message.sh ${JOBID} ${PINT003} ${MSG002} ${JOBNAME}

exit ${RET_OK}
