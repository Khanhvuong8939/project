CREATE OR REPLACE PROCEDURE FMOB000101
/********************************************************************/
/*  関数名：FMOB000101.sql                                          */
/*  機能名：AOBA転送群次(AOBA転送郵ﾋﾞﾙ一時ﾃｰﾌﾞﾙ挿入)                */
/*  作成者：TIS二宮                                                 */
/*  作成日：2007/12/13                                              */
/*  修正者：TIS二宮                                                 */
/*  修正日：2008/01/30                                              */
/*           15,25 各々登録対応                                     */
/*  修正者：TIS大原                                                 */
/*  修正日：2010/08/19                                              */
/*           請求レス対応                                           */
/*------------------------------------------------------------------*/
/*  引数：                                                          */
/*  戻値：                                                          */
/********************************************************************/
IS
BEGIN

EXECUTE IMMEDIATE 'TRUNCATE TABLE WK_AOBA_TNS_YBILL';


-- AOBA転送用対象データ作成


INSERT INTO WK_AOBA_TNS_YBILL
        (--請求先 顧客ｺｰﾄﾞ
        FIT_KKYK_CD,
        --郵BILLフラグ_15群
        YBILL_FLG_15G,
        --郵BILLフラグ_25群
        YBILL_FLG_25G,
        --相手先番号表示区分_15群
        AITE_BNG_HYJI_KBN_15G,
        --相手先番号表示区分_25群
        AITE_BNG_HYJI_KBN_25G,
        --ebillフラグ_15群
        EBILL_FLG_15G,
        --ebillフラグ_25群
        EBILL_FLG_25G)
SELECT  DISTINCT
        A.KKYK_CD,
        ' ',
        ' ',
        ' ',
        ' ',
        ' ',
        ' '
FROM    --請求先
        TB_SEIQ A
WHERE   --請求先．削除ﾌﾗｸﾞ = 0 (未削除)
        A.SAKJO_FLG = '0'
        --請求先．適用ｽﾃｰﾀｽ = 1(運用中)
AND     A.TEKIYO_STAT = '1'
        ;



-- 郵BILLフラグ情報(15群)をセット

UPDATE  WK_AOBA_TNS_YBILL A
    SET (
        --郵BILLフラグ_15群
        YBILL_FLG_15G,
        --相手先番号表示区分_15群
        AITE_BNG_HYJI_KBN_15G
        )=(
        SELECT  --請求先ｵﾌﾟｼｮﾝﾏｽﾀ.郵BILLフラグ_15群
                C.YBILL_FLG_15G,
                --請求先ｵﾌﾟｼｮﾝﾏｽﾀ.相手先番号表示区分_15群
                C.AITE_BNG_HYJI_KBN_15G
        FROM    --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ,請求先ｵﾌﾟｼｮﾝﾏｽﾀ
                TB_SEIQ_OPT_R B,TB_SEIQ_OPT_MST C
        WHERE   --AOBA転送郵ﾋﾞﾙ一時TBL．顧客ｺｰﾄﾞ
                --  = 請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ．顧客ｺｰﾄﾞ
                A.FIT_KKYK_CD = B.KKYK_CD
                --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ．請求先ｵﾌﾟｼｮﾝｺｰﾄﾞ
                --  = 請求先ｵﾌﾟｼｮﾝﾏｽﾀ．請求先ｵﾌﾟｼｮﾝｺｰﾄﾞ
        AND     B.SEIQ_OPT_CD = C.SEIQ_OPT_CD
                --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ．削除ﾌﾗｸﾞ = 0(未削除)
        AND     B.SAKJO_FLG = '0'
                --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ．適用ｽﾃｰﾀｽ = 1(運用中)
        AND     B.TEKIYO_STAT = '1'
                --請求先ｵﾌﾟｼｮﾝﾏｽﾀ．反映順序ﾌﾗｸﾞ = 1
        AND     C.HANEI_JNJ_FLG = '1'
                --請求先ｵﾌﾟｼｮﾝﾏｽﾀ.郵BILLフラグ_15群 = ' '
        AND     C.YBILL_FLG_15G != ' '
        )
WHERE   EXISTS
        (
        SELECT  1
        FROM    --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ,請求先ｵﾌﾟｼｮﾝﾏｽﾀ
                TB_SEIQ_OPT_R B,TB_SEIQ_OPT_MST C
        WHERE   --AOBA転送郵ﾋﾞﾙ一時TBL．顧客ｺｰﾄﾞ
                --  = 請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ．顧客ｺｰﾄﾞ
                A.FIT_KKYK_CD = B.KKYK_CD
                --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ．請求先ｵﾌﾟｼｮﾝｺｰﾄﾞ
                --  = 請求先ｵﾌﾟｼｮﾝﾏｽﾀ．請求先ｵﾌﾟｼｮﾝｺｰﾄﾞ
        AND     B.SEIQ_OPT_CD = C.SEIQ_OPT_CD
                --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ．削除ﾌﾗｸﾞ = 0(未削除)
        AND     B.SAKJO_FLG = '0'
                --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ．適用ｽﾃｰﾀｽ = 1(運用中)
        AND     B.TEKIYO_STAT = '1'
                --請求先ｵﾌﾟｼｮﾝﾏｽﾀ．反映順序ﾌﾗｸﾞ = 1
        AND     C.HANEI_JNJ_FLG = '1'
                --請求先ｵﾌﾟｼｮﾝﾏｽﾀ.郵BILLフラグ_15群 = ' '
        AND     C.YBILL_FLG_15G != ' '
        );


-- 郵BILLフラグ情報(25群)をセット
UPDATE  WK_AOBA_TNS_YBILL A
    SET (
        --郵BILLフラグ_25群
        YBILL_FLG_25G,
        --相手先番号表示区分_25群
        AITE_BNG_HYJI_KBN_25G
        )=(
        SELECT
                --請求先ｵﾌﾟｼｮﾝﾏｽﾀ.郵BILLフラグ_25群
                C.YBILL_FLG_25G,
                --請求先ｵﾌﾟｼｮﾝﾏｽﾀ.相手先番号表示区分_25群
                C.AITE_BNG_HYJI_KBN_25G
        FROM    --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ,請求先ｵﾌﾟｼｮﾝﾏｽﾀ
                TB_SEIQ_OPT_R B,TB_SEIQ_OPT_MST C
        WHERE   --AOBA転送郵ﾋﾞﾙ一時TBL．顧客ｺｰﾄﾞ
                --  = 請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ．顧客ｺｰﾄﾞ
                A.FIT_KKYK_CD = B.KKYK_CD
                --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ．請求先ｵﾌﾟｼｮﾝｺｰﾄﾞ
                --  = 請求先ｵﾌﾟｼｮﾝﾏｽﾀ．請求先ｵﾌﾟｼｮﾝｺｰﾄﾞ
        AND     B.SEIQ_OPT_CD = C.SEIQ_OPT_CD
                --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ．削除ﾌﾗｸﾞ = 0(未削除)
        AND     B.SAKJO_FLG = '0'
                --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ．適用ｽﾃｰﾀｽ = 1(運用中)
        AND     B.TEKIYO_STAT = '1'
                --請求先ｵﾌﾟｼｮﾝﾏｽﾀ．反映順序ﾌﾗｸﾞ = 1
        AND     C.HANEI_JNJ_FLG = '1'
                --請求先ｵﾌﾟｼｮﾝﾏｽﾀ.郵BILLフラグ_25群 = ' '
        AND     C.YBILL_FLG_25G != ' '
        )
WHERE   EXISTS
        (
        SELECT  1
        FROM    --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ,請求先ｵﾌﾟｼｮﾝﾏｽﾀ
                TB_SEIQ_OPT_R B,TB_SEIQ_OPT_MST C
        WHERE   --AOBA転送郵ﾋﾞﾙ一時TBL．顧客ｺｰﾄﾞ
                --  = 請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ．顧客ｺｰﾄﾞ
                A.FIT_KKYK_CD = B.KKYK_CD
                --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ．請求先ｵﾌﾟｼｮﾝｺｰﾄﾞ
                --  = 請求先ｵﾌﾟｼｮﾝﾏｽﾀ．請求先ｵﾌﾟｼｮﾝｺｰﾄﾞ
        AND     B.SEIQ_OPT_CD = C.SEIQ_OPT_CD
                --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ．削除ﾌﾗｸﾞ = 0(未削除)
        AND     B.SAKJO_FLG = '0'
                --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ．適用ｽﾃｰﾀｽ = 1(運用中)
        AND     B.TEKIYO_STAT = '1'
                --請求先ｵﾌﾟｼｮﾝﾏｽﾀ．反映順序ﾌﾗｸﾞ = 1
        AND     C.HANEI_JNJ_FLG = '1'
                --請求先ｵﾌﾟｼｮﾝﾏｽﾀ.郵BILLフラグ_25群 = ' '
        AND     C.YBILL_FLG_25G != ' '
        );


--相手先番号表示区分情報(15群)をセット
UPDATE  WK_AOBA_TNS_YBILL A
    SET --相手先番号表示区分_15群
        AITE_BNG_HYJI_KBN_15G
        =(
        SELECT  --請求先ｵﾌﾟｼｮﾝﾏｽﾀ.相手先番号表示区分_15群
                C.AITE_BNG_HYJI_KBN_15G
        FROM    --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ,請求先ｵﾌﾟｼｮﾝﾏｽﾀ
                TB_SEIQ_OPT_R B,TB_SEIQ_OPT_MST C
        WHERE   --AOBA転送郵ﾋﾞﾙ一時TBL．顧客ｺｰﾄﾞ
                --  = 請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ．顧客ｺｰﾄﾞ
                A.FIT_KKYK_CD = B.KKYK_CD
                --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ．請求先ｵﾌﾟｼｮﾝｺｰﾄﾞ
                --  = 請求先ｵﾌﾟｼｮﾝﾏｽﾀ．請求先ｵﾌﾟｼｮﾝｺｰﾄﾞ
        AND     B.SEIQ_OPT_CD = C.SEIQ_OPT_CD
                --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ．削除ﾌﾗｸﾞ = 0(未削除)
        AND     B.SAKJO_FLG = '0'
                --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ．適用ｽﾃｰﾀｽ = 1(運用中)
        AND     B.TEKIYO_STAT = '1'
                --請求先ｵﾌﾟｼｮﾝﾏｽﾀ．反映順序ﾌﾗｸﾞ = 1
        AND     C.HANEI_JNJ_FLG = '2'
                --請求先ｵﾌﾟｼｮﾝﾏｽﾀ.相手先番号表示区分_15群 = ' '
        AND     C.AITE_BNG_HYJI_KBN_15G != ' '
        )
WHERE   EXISTS
        (
        SELECT  1
        FROM    --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ,請求先ｵﾌﾟｼｮﾝﾏｽﾀ
                TB_SEIQ_OPT_R B,TB_SEIQ_OPT_MST C
        WHERE   --AOBA転送郵ﾋﾞﾙ一時TBL．顧客ｺｰﾄﾞ
                --  = 請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ．顧客ｺｰﾄﾞ
                A.FIT_KKYK_CD = B.KKYK_CD
                --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ．請求先ｵﾌﾟｼｮﾝｺｰﾄﾞ
                --  = 請求先ｵﾌﾟｼｮﾝﾏｽﾀ．請求先ｵﾌﾟｼｮﾝｺｰﾄﾞ
        AND     B.SEIQ_OPT_CD = C.SEIQ_OPT_CD
                --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ．削除ﾌﾗｸﾞ = 0(未削除)
        AND     B.SAKJO_FLG = '0'
                --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ．適用ｽﾃｰﾀｽ = 1(運用中)
        AND     B.TEKIYO_STAT = '1'
                --請求先ｵﾌﾟｼｮﾝﾏｽﾀ．反映順序ﾌﾗｸﾞ = 2
        AND     C.HANEI_JNJ_FLG = '2'
                --請求先ｵﾌﾟｼｮﾝﾏｽﾀ.相手先番号表示区分_15群 = ' '
        AND     C.AITE_BNG_HYJI_KBN_15G != ' '
        );

--相手先番号表示区分情報(25群)をセット
UPDATE  WK_AOBA_TNS_YBILL A
    SET --相手先番号表示区分_25群
        AITE_BNG_HYJI_KBN_25G
        =(
        SELECT  --請求先ｵﾌﾟｼｮﾝﾏｽﾀ.相手先番号表示区分_25群
                C.AITE_BNG_HYJI_KBN_25G
        FROM    --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ,請求先ｵﾌﾟｼｮﾝﾏｽﾀ
                TB_SEIQ_OPT_R B,TB_SEIQ_OPT_MST C
        WHERE   --AOBA転送郵ﾋﾞﾙ一時TBL．顧客ｺｰﾄﾞ
                --  = 請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ．顧客ｺｰﾄﾞ
                A.FIT_KKYK_CD = B.KKYK_CD
                --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ．請求先ｵﾌﾟｼｮﾝｺｰﾄﾞ
                --  = 請求先ｵﾌﾟｼｮﾝﾏｽﾀ．請求先ｵﾌﾟｼｮﾝｺｰﾄﾞ
        AND     B.SEIQ_OPT_CD = C.SEIQ_OPT_CD
                --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ．削除ﾌﾗｸﾞ = 0(未削除)
        AND     B.SAKJO_FLG = '0'
                --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ．適用ｽﾃｰﾀｽ = 1(運用中)
        AND     B.TEKIYO_STAT = '1'
                --請求先ｵﾌﾟｼｮﾝﾏｽﾀ．反映順序ﾌﾗｸﾞ = 1
        AND     C.HANEI_JNJ_FLG = '2'
                --請求先ｵﾌﾟｼｮﾝﾏｽﾀ.相手先番号表示区分_25群 = ' '
        AND     C.AITE_BNG_HYJI_KBN_25G != ' '
        )
WHERE   EXISTS
        (
        SELECT  1
        FROM    --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ,請求先ｵﾌﾟｼｮﾝﾏｽﾀ
                TB_SEIQ_OPT_R B,TB_SEIQ_OPT_MST C
        WHERE   --AOBA転送郵ﾋﾞﾙ一時TBL．顧客ｺｰﾄﾞ
                --  = 請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ．顧客ｺｰﾄﾞ
                A.FIT_KKYK_CD = B.KKYK_CD
                --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ．請求先ｵﾌﾟｼｮﾝｺｰﾄﾞ
                --  = 請求先ｵﾌﾟｼｮﾝﾏｽﾀ．請求先ｵﾌﾟｼｮﾝｺｰﾄﾞ
        AND     B.SEIQ_OPT_CD = C.SEIQ_OPT_CD
                --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ．削除ﾌﾗｸﾞ = 0(未削除)
        AND     B.SAKJO_FLG = '0'
                --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ．適用ｽﾃｰﾀｽ = 1(運用中)
        AND     B.TEKIYO_STAT = '1'
                --請求先ｵﾌﾟｼｮﾝﾏｽﾀ．反映順序ﾌﾗｸﾞ = 2
        AND     C.HANEI_JNJ_FLG = '2'
                --請求先ｵﾌﾟｼｮﾝﾏｽﾀ.相手先番号表示区分_25群 = ' '
        AND     C.AITE_BNG_HYJI_KBN_25G != ' '
        );




--e-billフラグ情報(15群)をセット
UPDATE  WK_AOBA_TNS_YBILL A
    SET
        --ebillフラグ_15群
        EBILL_FLG_15G
        =(
        SELECT  --請求先ｵﾌﾟｼｮﾝﾏｽﾀ.相手先番号表示区分_15群
                C.EBILL_FLG_15G
        FROM    --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ,請求先ｵﾌﾟｼｮﾝﾏｽﾀ
                TB_SEIQ_OPT_R B,TB_SEIQ_OPT_MST C
        WHERE   --AOBA転送郵ﾋﾞﾙ一時TBL．顧客ｺｰﾄﾞ
                --  = 請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ．顧客ｺｰﾄﾞ
                A.FIT_KKYK_CD = B.KKYK_CD
                --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ．請求先ｵﾌﾟｼｮﾝｺｰﾄﾞ
                --  = 請求先ｵﾌﾟｼｮﾝﾏｽﾀ．請求先ｵﾌﾟｼｮﾝｺｰﾄﾞ
        AND     B.SEIQ_OPT_CD = C.SEIQ_OPT_CD
                --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ．削除ﾌﾗｸﾞ = 0(未削除)
        AND     B.SAKJO_FLG = '0'
                --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ．適用ｽﾃｰﾀｽ = 1(運用中)
        AND     B.TEKIYO_STAT = '1'
                --請求先ｵﾌﾟｼｮﾝﾏｽﾀ．反映順序ﾌﾗｸﾞ = 1
        AND     C.HANEI_JNJ_FLG = '3'
                --請求先ｵﾌﾟｼｮﾝﾏｽﾀ.ebillフラグ_15群 = ' '
        AND     C.EBILL_FLG_15G != ' '

        )
WHERE   EXISTS
        (
        SELECT  1
        FROM    --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ,請求先ｵﾌﾟｼｮﾝﾏｽﾀ
                TB_SEIQ_OPT_R B,TB_SEIQ_OPT_MST C
        WHERE   --AOBA転送郵ﾋﾞﾙ一時TBL．顧客ｺｰﾄﾞ
                --  = 請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ．顧客ｺｰﾄﾞ
                A.FIT_KKYK_CD = B.KKYK_CD
                --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ．請求先ｵﾌﾟｼｮﾝｺｰﾄﾞ
                --  = 請求先ｵﾌﾟｼｮﾝﾏｽﾀ．請求先ｵﾌﾟｼｮﾝｺｰﾄﾞ
        AND     B.SEIQ_OPT_CD = C.SEIQ_OPT_CD
                --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ．削除ﾌﾗｸﾞ = 0(未削除)
        AND     B.SAKJO_FLG = '0'
                --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ．適用ｽﾃｰﾀｽ = 1(運用中)
        AND     B.TEKIYO_STAT = '1'
                --請求先ｵﾌﾟｼｮﾝﾏｽﾀ．反映順序ﾌﾗｸﾞ = 3
        AND     C.HANEI_JNJ_FLG = '3'
                --請求先ｵﾌﾟｼｮﾝﾏｽﾀ.ebillフラグ_15群 = ' '
        AND     C.EBILL_FLG_15G != ' '
        );


--e-billフラグ情報(25群)をセット
UPDATE  WK_AOBA_TNS_YBILL A
    SET
        --ebillフラグ_25群
        EBILL_FLG_25G
        =(
        SELECT  --請求先ｵﾌﾟｼｮﾝﾏｽﾀ.相手先番号表示区分_25群
                C.EBILL_FLG_25G
        FROM    --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ,請求先ｵﾌﾟｼｮﾝﾏｽﾀ
                TB_SEIQ_OPT_R B,TB_SEIQ_OPT_MST C
        WHERE   --AOBA転送郵ﾋﾞﾙ一時TBL．顧客ｺｰﾄﾞ
                --  = 請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ．顧客ｺｰﾄﾞ
                A.FIT_KKYK_CD = B.KKYK_CD
                --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ．請求先ｵﾌﾟｼｮﾝｺｰﾄﾞ
                --  = 請求先ｵﾌﾟｼｮﾝﾏｽﾀ．請求先ｵﾌﾟｼｮﾝｺｰﾄﾞ
        AND     B.SEIQ_OPT_CD = C.SEIQ_OPT_CD
                --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ．削除ﾌﾗｸﾞ = 0(未削除)
        AND     B.SAKJO_FLG = '0'
                --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ．適用ｽﾃｰﾀｽ = 1(運用中)
        AND     B.TEKIYO_STAT = '1'
                --請求先ｵﾌﾟｼｮﾝﾏｽﾀ．反映順序ﾌﾗｸﾞ = 1
        AND     C.HANEI_JNJ_FLG = '3'
                --請求先ｵﾌﾟｼｮﾝﾏｽﾀ.ebillフラグ_25群 = ' '
        AND     C.EBILL_FLG_25G != ' '

        )
WHERE   EXISTS
        (
        SELECT  1
        FROM    --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ,請求先ｵﾌﾟｼｮﾝﾏｽﾀ
                TB_SEIQ_OPT_R B,TB_SEIQ_OPT_MST C
        WHERE   --AOBA転送郵ﾋﾞﾙ一時TBL．顧客ｺｰﾄﾞ
                --  = 請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ．顧客ｺｰﾄﾞ
                A.FIT_KKYK_CD = B.KKYK_CD
                --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ．請求先ｵﾌﾟｼｮﾝｺｰﾄﾞ
                --  = 請求先ｵﾌﾟｼｮﾝﾏｽﾀ．請求先ｵﾌﾟｼｮﾝｺｰﾄﾞ
        AND     B.SEIQ_OPT_CD = C.SEIQ_OPT_CD
                --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ．削除ﾌﾗｸﾞ = 0(未削除)
        AND     B.SAKJO_FLG = '0'
                --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ．適用ｽﾃｰﾀｽ = 1(運用中)
        AND     B.TEKIYO_STAT = '1'
                --請求先ｵﾌﾟｼｮﾝﾏｽﾀ．反映順序ﾌﾗｸﾞ = 3
        AND     C.HANEI_JNJ_FLG = '3'
                --請求先ｵﾌﾟｼｮﾝﾏｽﾀ.ebillフラグ_25群 = ' '
        AND     C.EBILL_FLG_25G != ' '
        );


-- 郵BILLフラグ情報(15群)をセット(請求レス対象外OEM)

UPDATE  WK_AOBA_TNS_YBILL A
    SET --郵BILLフラグ_15群
        A.YBILL_FLG_15G = '8'
WHERE   EXISTS
        (
        SELECT  1
        FROM    --請求先,OEMﾏｽﾀ
                TB_SEIQ B,TB_OEM_MST C
        WHERE   --AOBA転送郵ﾋﾞﾙ一時TBL．郵BILLフラグ_15群 = ' '
                A.YBILL_FLG_15G = ' '
                --AOBA転送郵ﾋﾞﾙ一時TBL．顧客ｺｰﾄﾞ = 請求先．顧客ｺｰﾄﾞ
        AND     A.FIT_KKYK_CD = B.KKYK_CD
                --請求先．削除ﾌﾗｸﾞ = 0(未削除)
        AND     B.SAKJO_FLG = '0'
                --請求先．適用ｽﾃｰﾀｽ = 1(運用中)
        AND     B.TEKIYO_STAT = '1'
                --請求先．OEMｺｰﾄﾞ = OEMﾏｽﾀ．OEMｺｰﾄﾞ
        AND     B.OEM_CD = C.OEM_CD
                --OEMﾏｽﾀ．請求ﾚｽﾌﾗｸﾞ = 0(請求ﾚｽ対象外)
        AND     C.SEIQ_LESS_FLG = '0'
                --OEMﾏｽﾀ．有効ﾌﾗｸﾞ = 1(有効)
        AND     C.YUKO_FLG = '1'
        );

-- 郵BILLフラグ情報(25群)をセット(請求レス対象外OEM)
UPDATE  WK_AOBA_TNS_YBILL A
    SET
        --郵BILLフラグ_25群
        A.YBILL_FLG_25G = '8'
WHERE   EXISTS
        (
        SELECT  1
        FROM    --請求先,OEMﾏｽﾀ
                TB_SEIQ B,TB_OEM_MST C
        WHERE   --AOBA転送郵ﾋﾞﾙ一時TBL．郵BILLフラグ_25群 = ' '
                A.YBILL_FLG_25G = ' '
                --AOBA転送郵ﾋﾞﾙ一時TBL．顧客ｺｰﾄﾞ = 請求先．顧客ｺｰﾄﾞ
        AND     A.FIT_KKYK_CD = B.KKYK_CD
                --請求先．削除ﾌﾗｸﾞ = 0(未削除)
        AND     B.SAKJO_FLG = '0'
                --請求先．適用ｽﾃｰﾀｽ = 1(運用中)
        AND     B.TEKIYO_STAT = '1'
                --請求先．OEMｺｰﾄﾞ = OEMﾏｽﾀ．OEMｺｰﾄﾞ
        AND     B.OEM_CD = C.OEM_CD
                --OEMﾏｽﾀ．ｺｰﾄﾞ．請求ﾚｽﾌﾗｸﾞ = 0(請求ﾚｽ対象外)
        AND     C.SEIQ_LESS_FLG = '0'
                --OEMﾏｽﾀ．有効ﾌﾗｸﾞ = 1(有効)
        AND     C.YUKO_FLG = '1'
        );


-- 郵BILLフラグ情報(15群)をセット(支払種別が請求ﾚｽ対象外)

UPDATE  WK_AOBA_TNS_YBILL A
    SET
        --郵BILLフラグ_15群
        A.YBILL_FLG_15G = ' '
WHERE   EXISTS
        (
        SELECT  1
        FROM    --請求先,OEM･支払種別･請求先 ﾘﾚｰｼｮﾝ,支払種別
                TB_SEIQ B, TB_OEM_HARAI_SEIQ_R C, TB_HARAI D
        WHERE   --AOBA転送郵ﾋﾞﾙ一時TBL．郵BILLフラグ_15群 = 7
                A.YBILL_FLG_15G = '7'
                --AOBA転送郵ﾋﾞﾙ一時TBL．顧客ｺｰﾄﾞ = 請求先．顧客ｺｰﾄﾞ
        AND     A.FIT_KKYK_CD = B.KKYK_CD
                --OEM･支払種別･請求先 ﾘﾚｰｼｮﾝ．顧客ｺｰﾄﾞ = 請求先．顧客ｺｰﾄﾞ
        AND     C.KKYK_CD = B.KKYK_CD
                --OEM･支払種別･請求先 ﾘﾚｰｼｮﾝ．OEMｺｰﾄﾞ = 請求先．OEMｺｰﾄﾞ
        AND     C.OEM_CD = B.OEM_CD
                --請求先．適用ｽﾃｰﾀｽ = 1(運用中)
        AND     B.TEKIYO_STAT = '1'
                --OEM･支払種別･請求先 ﾘﾚｰｼｮﾝ．適用ｽﾃｰﾀｽ = 1(運用中)
        AND     C.TEKIYO_STAT = '1'
                --請求先．削除ﾌﾗｸﾞ = 0(未削除)
        AND     B.SAKJO_FLG = '0'
                --OEM･支払種別･請求先 ﾘﾚｰｼｮﾝ 削除ﾌﾗｸﾞ = 0
        AND     C.SAKJO_FLG = '0'
                --OEM･支払種別･請求先 ﾘﾚｰｼｮﾝ．支払種別 = 支払種別．支払種別
        AND     C.HARAI = D.HARAI
                --支払種別．強制無効ﾌﾗｸﾞ = 0(請求ﾚｽ対象外)
        AND     D.KYOSEI_MUKO_FLG = '0'
        );

-- 郵BILLフラグ情報(25群)をセット(支払種別が請求ﾚｽ対象外)

UPDATE  WK_AOBA_TNS_YBILL A
    SET
        --郵BILLフラグ_25群
        A.YBILL_FLG_25G = ' '
WHERE   EXISTS
        (
        SELECT  1
        FROM    --請求先,OEM･支払種別･請求先 ﾘﾚｰｼｮﾝ,支払種別
                TB_SEIQ B, TB_OEM_HARAI_SEIQ_R C, TB_HARAI D
        WHERE   --AOBA転送郵ﾋﾞﾙ一時TBL．郵BILLフラグ_25群 = 7
                A.YBILL_FLG_25G = '7'
                --AOBA転送郵ﾋﾞﾙ一時TBL．顧客ｺｰﾄﾞ = 請求先．顧客ｺｰﾄﾞ
        AND     A.FIT_KKYK_CD = B.KKYK_CD
                --OEM･支払種別･請求先 ﾘﾚｰｼｮﾝ．顧客ｺｰﾄﾞ = 請求先．顧客ｺｰﾄﾞ
        AND     C.KKYK_CD = B.KKYK_CD
                --OEM･支払種別･請求先 ﾘﾚｰｼｮﾝ．OEMｺｰﾄﾞ = 請求先．OEMｺｰﾄﾞ
        AND     C.OEM_CD = B.OEM_CD
                --請求先．適用ｽﾃｰﾀｽ = 1(運用中)
        AND     B.TEKIYO_STAT = '1'
                --OEM･支払種別･請求先 ﾘﾚｰｼｮﾝ．適用ｽﾃｰﾀｽ = 1(運用中)
        AND     C.TEKIYO_STAT = '1'
                --請求先．削除ﾌﾗｸﾞ = 0(未削除)
        AND     B.SAKJO_FLG = '0'
                --OEM･支払種別･請求先 ﾘﾚｰｼｮﾝ 削除ﾌﾗｸﾞ = 0
        AND     C.SAKJO_FLG = '0'
                --OEM･支払種別･請求先 ﾘﾚｰｼｮﾝ．支払種別 = 支払種別．支払種別
        AND     C.HARAI = D.HARAI
                --支払種別．強制無効ﾌﾗｸﾞ = 0(請求ﾚｽ対象外)
        AND     D.KYOSEI_MUKO_FLG = '0'
        );


COMMIT;

EXCEPTION
    WHEN OTHERS THEN
    ROLLBACK;
    RAISE_APPLICATION_ERROR(-20500, '[ユーザエラー]AOBA転送群次(AOBA転送郵ビル一時テーブル挿入)でエラーが発生しました。');

END;
 
/
