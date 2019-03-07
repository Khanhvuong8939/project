#!/bin/ksh
######################################################################
#ファイル名：FAIB000002.sh
#機能      ：外部IN・マスタコピー
#引数      ：なし
#戻り値    ：0：正常 1：警告 
#作成者    ：成毛 正則
#作成日    ：2006/03/18
#VER       ：1.00
#改訂履歴  ：YYYY/MM/DD
#               変更内容を記述	
######################################################################

################################################################################
# 処理名    :計算前処理・マスタコピー
# 入力      :なし
# 引数 1    :なし
################################################################################
. ${AOBA_HOME}/batch/exec/env/aoba_env.env
#. ${AOBA_HOME}/batch/exec/env/aoba_rate_env.env

################################################################################
JOBID=FAIB000002
JOBNAME=外部IN・マスタコピー

################################################################################
INFILE=${SQL}/init_copy_in.txt
EXEFILE=${SQL}/init_copy_in.sql
SPOOLFILE=${SPOOL}/${JOBID}.lst

################################################################################
# ファイル存在チェック
# ファイルが存在しない場合：touch
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
echo "BEGIN" >> $EXEFILE
while read line; do
	TARGET="`echo "${line}" | cut -d\| -f1`"
	SOURCE="`echo "${line}" | cut -d\| -f2`"
	COND="`echo "${line}" | cut -d\| -f3`"
	if [ "${COND:=null}" = "null" ]; then
		echo "    INSERT INTO ${TARGET} SELECT * FROM ${SOURCE};" >> $EXEFILE
	else
		echo "    INSERT INTO ${TARGET} SELECT * FROM ${SOURCE} WHERE ${COND};" >> $EXEFILE
	fi
done < "$INFILE"
echo "COMMIT;"                                                >> $EXEFILE
echo "EXCEPTION"                                              >> $EXEFILE
echo "    WHEN OTHERS THEN"                                   >> $EXEFILE
echo "        ROLLBACK;"                                      >> $EXEFILE
echo "        DBMS_OUTPUT.PUT_LINE(substr(sqlerrm, 1, 100));" >> $EXEFILE
echo "END;"                                                   >> $EXEFILE
echo "/"                                                      >> $EXEFILE

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

#SPOOLチェック
${COM}/put_oracle_error.sh ${SPOOLFILE} ${JOBID} ${PINT002}
RET=$?
if [ ${RET_OK} -ne ${RET} ];
then
	exit ${RET_NG}
fi

# ログ出力
${COM}/put_message.sh ${JOBID} ${PINT003} ${MSG002} ${JOBNAME}

exit ${RET_OK}
