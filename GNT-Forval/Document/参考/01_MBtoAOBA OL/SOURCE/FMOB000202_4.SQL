CREATE OR REPLACE PROCEDURE FMOB000202_4
/********************************************************************/
/*  関数名：FMOB000202_4.sql                                        */
/*  機能名：AOBA転送日次(AOBA転送日更新)                            */
/*  作成者：TIS杉本                                                 */
/*  作成日：2007/11/                                                */
/*  修正者：TIS杉本                                                 */
/*  修正日：2007/12/13                                              */
/*  修正箇所：請求先ｵﾌﾟｼｮﾝﾘﾚｰｼｮﾝ(FITMST)の更新時に                  */
/*            適用ｽﾃｰﾀｽ='1'の条件が必要。                           */
/*  修正者：TIS二宮                                                 */
/*  修正日：2008/02/09                                              */
/*  修正箇所：申込者・OEM・申込者オプションのJOIN条件変更           */
/*                  適用ｽﾃｰﾀｽ追加                                   */
/*                  カラム明示化                                    */
/*                  適用開始群→適用終了群                          */
/*  修正者：TIS二宮                                                 */
/*  修正日：2008/02/14                                              */
/*  修正箇所：WK_AOBA_TNS_UPDATE を作成                             */
/*  修正者：TIS二宮                                                 */
/*  修正日：2008/03/25                                              */
/*  修正箇所：外部結合条件変更                                      */
/*  修正者：TIS杉本                                                 */
/*  修正日：2008/03/25                                              */
/*  修正箇所：削除フラグが'0'のものが対象となるよう修正する。       */
/*  修正日：2008/04/04                                              */
/*  修正箇所：請求先に関しても                                      */
/*            削除フラグが'0'のものが対象となるよう修正する。       *
/*  修正箇所：where句にOA顧客・設置先ﾘﾚｰｼｮﾝ．OA顧客番号 IS NOT NULL */
/*            を追加する。                                          */
/*  修正者：TIS杉本                                                 */
/*  修正日：2008/04/08                                              */
/*  修正箇所：申込者ｵﾌﾟｼｮﾝと請求先ｵﾌﾟｼｮﾝの適用ｽﾃｰﾀｽ IN ('1','2')    */
/*            の位置を変更し、そこに                                */
/*            MSKM_NO IS NOT NULL、KKYK_CD IS NOT NULLを追加        */
/*------------------------------------------------------------------*/
/*  引数：                                                          */
/*  戻値：                                                          */
/********************************************************************/
IS
    tSysTimestamp      TB_JUCH_RISOS_KNR.INSERT_DATE%TYPE;    -- システム日時
    tTruncTimestamp    TB_JUCH_RISOS_KNR.INSERT_DATE%TYPE;    -- システム日
    nRet    NUMBER(1) := 0;    -- 戻り値
BEGIN

    /* システム日時取得 */
    nRet := manbow_common.get_date(tSysTimestamp);    -- システム日時
    /* システム日取得 */
    tTruncTimestamp := TRUNC(tSysTimestamp);    -- システム日


EXECUTE IMMEDIATE 'TRUNCATE TABLE WK_AOBA_TNS_UPDATE';


INSERT INTO WK_AOBA_TNS_UPDATE
        (--受注NO,受注明細NO,受注ﾘｿｰｽ履歴NO,
        JUCH_NO,        JUCH_MESAI_NO,  JUCH_RISOS_RRK_NO,
        --申込者,申込者履歴NO,
        MSKM_NO,        MSKM_RRK_NO,
        --顧客ｺｰﾄﾞ,請求先履歴NO,
        KKYK_CD,        SEIQ_RRK_NO,
        --OA顧客番号,設置先NO,
        OAKOKYK_BNG,    SETI_NO,
        --請求先・支払種別 OEM ｺｰﾄﾞ,請求先・支払種別 SEQ,
        HARAI_OEM_CD,   HARAI_SEQ,
        --申込者ｵﾌﾟｼｮﾝ ｵﾌﾟｼｮﾝｺｰﾄﾞ,申込者ｵﾌﾟｼｮﾝ OEM ｺｰﾄﾞ,申込者ｵﾌﾟｼｮﾝ SEQ,
        MOPT,           MOPT_OEM_CD,       MOPT_SEQ,
        --請求先ｵﾌﾟｼｮﾝ ｵﾌﾟｼｮﾝｺｰﾄﾞ,請求先ｵﾌﾟｼｮﾝ OEM ｺｰﾄﾞ,請求先ｵﾌﾟｼｮﾝ SEQ
        SEIQ_OPT_CD,    SEIQ_OPT_OEM_CD,SEIQ_OPT_SEQ)
SELECT       --受注ﾘｿｰｽ管理
        C.JUCH_NO, C.JUCH_MESAI_NO, C.RRK_NO,
             --申込者
        D.MSKM_NO, D.RRK_NO,
            --請求先
        E.KKYK_CD, E.RRK_NO,
             --OA顧客・設置先ﾘﾚｰｼｮﾝ
        H.OAKOKYK_BNG, H.SETI_NO,
            --OEM･支払種別･請求先 ﾘﾚｰｼｮﾝ
        F.OEM_CD, F.SEQ,
            --申込者･OEM･申込者ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ
        Q.MOPT, Q.OEM_CD, Q.SEQ,
             --請求先ｵﾌﾟｼｮﾝﾘﾚｰｼｮﾝ
        J.SEIQ_OPT_CD, J.OEM_CD, J.SEQ
FROM        --請求先
        TB_SEIQ E
            --OEM･支払種別･請求先 ﾘﾚｰｼｮﾝ
        JOIN TB_OEM_HARAI_SEIQ_R F
             --請求先/OEM･支払種別･請求先 ﾘﾚｰｼｮﾝ 顧客ｺｰﾄﾞ
         ON (E.KKYK_CD = F.KKYK_CD)
             --請求先ｵﾌﾟｼｮﾝﾘﾚｰｼｮﾝ
        LEFT JOIN TB_SEIQ_OPT_R J
             --請求先/請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ 顧客ｺｰﾄﾞ
         ON (E.KKYK_CD = J.KKYK_CD
             AND
             --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ 適用ｽﾃｰﾀｽ
             J.TEKIYO_STAT IN ('1','2'))
             --受注ﾘｿｰｽ管理
        JOIN TB_JUCH_RISOS_KNR C
            --請求先/受注ﾘｿｰｽ管理 顧客ｺｰﾄﾞ
         ON (E.KKYK_CD = C.KKYK_CD)
             --受注
        JOIN TB_JUCH A
             --受注ﾘｿｰｽ管理/受注 受注NO
         ON (C.JUCH_NO = A.JUCH_NO)
             --OA顧客・設置先ﾘﾚｰｼｮﾝ
        LEFT JOIN TB_OAKOKYK_SETI_R H
             --受注ﾘｿｰｽ管理/OA顧客・設置先ﾘﾚｰｼｮﾝ 設置先NO
         ON (H.SETI_NO = C.SETI_NO)
             --申込者
        JOIN TB_MSKM D
            --受注ﾘｿｰｽ管理/申込者 申込者NO
         ON (C.MSKM_NO = D.MSKM_NO)
            --申込者･OEM･申込者ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ
        LEFT JOIN TB_MSKM_OEM_MOPT_R Q
            --申込者･OEM･申込者ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ 申込者
         ON (Q.MSKM_NO = D.MSKM_NO
             AND
            --申込者･OEM･申込者ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ 適用ｽﾃｰﾀｽ
             Q.TEKIYO_STAT IN ('1','2'))
             --受注明細
        JOIN TB_JUCH_MESAI B
             --受注ﾘｿｰｽ管理/受注明細 受注NO
         ON (C.JUCH_NO = B.JUCH_NO
             AND
             --受注ﾘｿｰｽ管理/受注明細 受注明細NO
             C.JUCH_MESAI_NO = B.JUCH_MESAI_NO)
             --通信ｻｰﾋﾞｽ商品ﾏｽﾀ
        JOIN TB_TSINSER_MST I
             --受注明細/通信ｻｰﾋﾞｽ商品ﾏｽﾀ 通信ｻｰﾋﾞｽｵﾌﾟｼｮﾝｺｰﾄﾞ
         ON (B.TSINSER_OPT_CD = I.TSINSER_OPT_CD)
WHERE   --受注 AOBA転送日
        (A.AOBA_TNS_DATE IS NULL
         OR
        --受注明細 AOBA転送日
         B.AOBA_TNS_DATE IS NULL
         OR
        --受注ﾘｿｰｽ管理 AOBA転送日
         C.AOBA_TNS_DATE IS NULL
         OR
        --申込者 AOBA転送日
         D.AOBA_TNS_DATE IS NULL
         OR
        --請求先 AOBA転送日
         E.AOBA_TNS_DATE IS NULL
         OR
        --OEM･支払種別･請求先 ﾘﾚｰｼｮﾝ AOBA転送日
         F.AOBA_TNS_DATE IS NULL
         OR
        --OA顧客・設置先ﾘﾚｰｼｮﾝ AOBA転送日
         (H.AOBA_TNS_DATE IS NULL
          AND
        --OA顧客・設置先ﾘﾚｰｼｮﾝ OA顧客番号
          H.OAKOKYK_BNG IS NOT NULL)
         OR
        --申込者･OEM･申込者ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ AOBA転送日
         (Q.AOBA_TNS_DATE IS NULL
          AND
        --申込者･OEM･申込者ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ 申込者NO
          Q.MSKM_NO IS NOT NULL)
         OR
        --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ AOBA転送日
         (J.AOBA_TNS_DATE IS NULL
          AND
        --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ 顧客ｺｰﾄﾞ
          J.KKYK_CD IS NOT NULL
          AND
        --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ 請求先ｵﾌﾟｼｮﾝｺｰﾄﾞ
          J.SEIQ_OPT_CD IN ('012', '192', '191', '193' ,'190')))
         AND
        --受注ﾘｿｰｽ管理 適用ｽﾃｰﾀｽ
         C.TEKIYO_STAT = '1'
         AND
        --申込者 適用ｽﾃｰﾀｽ
         D.TEKIYO_STAT = '1'
         AND
        --請求先 適用ｽﾃｰﾀｽ
         E.TEKIYO_STAT = '1'
         AND
        --OEM･支払種別･請求先 ﾘﾚｰｼｮﾝ 適用ｽﾃｰﾀｽ
         F.TEKIYO_STAT = '1'
         AND
        --請求先 削除フラグ
         E.SAKJO_FLG = '0'
         AND
        --OEM･支払種別･請求先 ﾘﾚｰｼｮﾝ 削除フラグ
         F.SAKJO_FLG = '0'
         AND
        --受注ﾘｿｰｽ管理 削除フラグ
         C.SAKJO_FLG = '0'
         AND
        --受注 削除フラグ
         A.SAKJO_FLG = '0'
         AND
        --申込者 削除フラグ
         D.SAKJO_FLG = '0'
         AND
        --受注明細 削除フラグ
         B.SAKJO_FLG = '0';


INSERT INTO WK_AOBA_TNS_UPDATE
        (--受注NO,受注明細NO,受注ﾘｿｰｽ履歴NO,
        JUCH_NO,        JUCH_MESAI_NO,  JUCH_RISOS_RRK_NO)
SELECT  A.JUCH_NO, A.JUCH_MESAI_NO, A.RRK_NO
FROM    --受注ﾘｿｰｽ管理
        TB_JUCH_RISOS_KNR A,
        --受注
        TB_JUCH B,
        --社員・部署 ﾘﾚｰｼｮﾝ
        TB_SHAIN_BSH_R C,
        --受注明細
        TB_JUCH_MESAI D,
        --固定電話受注明細内容
        TB_KOTEI_JUCH_MESAI E,
        --通信ｻｰﾋﾞｽ商品ﾏｽﾀ
        TB_TSINSER_MST F,
        --申込者・設置先 ﾘﾚｰｼｮﾝ
        TB_MSKM_SETI_R G
WHERE   --受注 AOBA転送日
        (B.AOBA_TNS_DATE IS NULL
            OR
        --受注明細 AOBA転送日
            D.AOBA_TNS_DATE IS NULL
            OR
        --固定電話受注明細内容 AOBA転送日
            E.AOBA_TNS_DATE IS NULL
            OR
        --受注ﾘｿｰｽ管理 AOBA転送日
            A.AOBA_TNS_DATE IS NULL)
        AND
        --受注ﾘｿｰｽ管理 適用ｽﾃｰﾀｽ
            A.TEKIYO_STAT = '1'
        AND
        --受注ﾘｿｰｽ管理 受注NO--受注 受注NO
        A.JUCH_NO = B.JUCH_NO
        AND
        --受注 FVL社員ｺｰﾄﾞ--社員・部署 ﾘﾚｰｼｮﾝ 社員ｺｰﾄﾞ
        B.FVL_SHAIN_CD = C.SHAIN_CD(+)
        AND
        --受注明細 受注NO--受注ﾘｿｰｽ管理 受注NO
        D.JUCH_NO = A.JUCH_NO
        AND
        --受注明細 受注明細NO--受注ﾘｿｰｽ管理 受注明細NO
        D.JUCH_MESAI_NO = A.JUCH_MESAI_NO
        AND
        --固定電話受注明細内容 受注NO--受注明細 受注NO
        E.JUCH_NO = D.JUCH_NO
        AND
        --固定電話受注明細内容 受注明細NO--受注明細 受注明細NO
        E.JUCH_MESAI_NO = D.JUCH_MESAI_NO
        AND
        --受注明細 通信ｻｰﾋﾞｽｵﾌﾟｼｮﾝｺｰﾄﾞ--通信ｻｰﾋﾞｽ商品ﾏｽﾀ 通信ｻｰﾋﾞｽｵﾌﾟｼｮﾝｺｰﾄﾞ
        D.TSINSER_OPT_CD = F.TSINSER_OPT_CD
        AND
        --申込者・設置先 ﾘﾚｰｼｮﾝ 申込者NO--受注ﾘｿｰｽ管理 申込者NO
        G.MSKM_NO = A.MSKM_NO
        AND
        --申込者・設置先 ﾘﾚｰｼｮﾝ 設置先NO--受注ﾘｿｰｽ管理 設置先NO
        G.SETI_NO = A.SETI_NO;


--受注
UPDATE TB_JUCH A
SET A.AOBA_TNS_DATE = tTruncTimestamp
WHERE   A.JUCH_NO IN
        (SELECT JUCH_NO
         FROM WK_AOBA_TNS_UPDATE)
AND A.AOBA_TNS_DATE IS NULL
;


--受注明細
UPDATE TB_JUCH_MESAI A
SET A.AOBA_TNS_DATE = tTruncTimestamp
WHERE   (A.JUCH_NO, A.JUCH_MESAI_NO) IN
        (SELECT JUCH_NO, JUCH_MESAI_NO
         FROM WK_AOBA_TNS_UPDATE)
AND A.AOBA_TNS_DATE IS NULL
;


--固定電話受注明細内容
UPDATE TB_KOTEI_JUCH_MESAI A
SET A.AOBA_TNS_DATE = tTruncTimestamp
WHERE   (A.JUCH_NO, A.JUCH_MESAI_NO) IN
        (SELECT JUCH_NO, JUCH_MESAI_NO
         FROM WK_AOBA_TNS_UPDATE)
AND A.AOBA_TNS_DATE IS NULL
;

--受注ﾘｿｰｽ管理
UPDATE TB_JUCH_RISOS_KNR A
SET A.AOBA_TNS_DATE = tTruncTimestamp
WHERE   (A.JUCH_NO, A.JUCH_MESAI_NO, A.RRK_NO) IN
        (SELECT JUCH_NO, JUCH_MESAI_NO, JUCH_RISOS_RRK_NO
         FROM WK_AOBA_TNS_UPDATE)
AND A.AOBA_TNS_DATE IS NULL
;

--申込者
UPDATE TB_MSKM A
SET A.AOBA_TNS_DATE = tTruncTimestamp
WHERE   (A.MSKM_NO, A.RRK_NO) IN
        (SELECT MSKM_NO, MSKM_RRK_NO
         FROM WK_AOBA_TNS_UPDATE)
AND A.AOBA_TNS_DATE IS NULL
;

--請求先
UPDATE TB_SEIQ A
SET A.AOBA_TNS_DATE = tTruncTimestamp
WHERE   (A.KKYK_CD, A.RRK_NO) IN
        (SELECT KKYK_CD, SEIQ_RRK_NO
         FROM WK_AOBA_TNS_UPDATE)
AND A.AOBA_TNS_DATE IS NULL
;

--OA顧客・設置先ﾘﾚｰｼｮﾝ
UPDATE TB_OAKOKYK_SETI_R A
SET A.AOBA_TNS_DATE = tTruncTimestamp
WHERE   (A.OAKOKYK_BNG, A.SETI_NO) IN
        (SELECT OAKOKYK_BNG, SETI_NO
         FROM WK_AOBA_TNS_UPDATE)
AND A.AOBA_TNS_DATE IS NULL
;

--OEM･支払種別･請求先 ﾘﾚｰｼｮﾝ
UPDATE TB_OEM_HARAI_SEIQ_R A
SET A.AOBA_TNS_DATE = tTruncTimestamp
WHERE   (A.KKYK_CD, A.OEM_CD, A.SEQ) IN
        (SELECT KKYK_CD, HARAI_OEM_CD, HARAI_SEQ
         FROM WK_AOBA_TNS_UPDATE)
AND A.AOBA_TNS_DATE IS NULL
;

--申込者･OEM･申込者ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ
UPDATE TB_MSKM_OEM_MOPT_R A
SET A.AOBA_TNS_DATE = tTruncTimestamp
WHERE   (A.MSKM_NO, A.MOPT, A.OEM_CD, A.SEQ) IN
        (SELECT MSKM_NO, MOPT, MOPT_OEM_CD, MOPT_SEQ
         FROM WK_AOBA_TNS_UPDATE)
AND A.AOBA_TNS_DATE IS NULL
;

--請求先ｵﾌﾟｼｮﾝﾘﾚｰｼｮﾝ
UPDATE TB_SEIQ_OPT_R A
SET A.AOBA_TNS_DATE = tTruncTimestamp
WHERE   (A.KKYK_CD, A.OEM_CD, A.SEIQ_OPT_CD, A.SEQ) IN
        (SELECT KKYK_CD, SEIQ_OPT_OEM_CD, SEIQ_OPT_CD, SEIQ_OPT_SEQ
         FROM WK_AOBA_TNS_UPDATE)
AND A.AOBA_TNS_DATE IS NULL
;

    COMMIT;

    EXCEPTION
        WHEN OTHERS THEN
        RAISE_APPLICATION_ERROR(-20500, '[ユーザエラー]AOBA転送日次(AOBA転送日更新)でエラーが発生しました。');
        ROLLBACK;

END;

 
/
