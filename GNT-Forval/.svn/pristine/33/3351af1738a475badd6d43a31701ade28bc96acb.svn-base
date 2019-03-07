CREATE OR REPLACE PROCEDURE FMOB000201_2
/********************************************************************/
/*  関数名：FMOB000201_2.sql                                        */
/*  機能名：AOBA転送日次(1P2P一時ﾃｰﾌﾞﾙ挿入)                         */
/*  作成者：TIS杉本                                                 */
/*  作成日：2007/11/                                                */
/*  修正者：TIS杉本                                                 */
/*  修正日：2007/12/13                                              */
/*  修正日：2008/01/21                                              */
/*  修正箇所：挿入先のテーブルのカラムを記入(コーディング規約 )     */
/*  修正日：2008/02/05                                              */
/*  修正箇所：適用ｽﾃｰﾀｽ='1'のデータのみ抽出できるよう修正する。     */
/*  修正者：TIS杉本                                                 */
/*  修正日：2008/02/20                                              */
/*  修正箇所：EXCEPTION 発生時のエラーに対応                        */
/*  修正者：TIS杉本                                                 */
/*  修正日：2008/03/24                                              */
/*  修正箇所：社員部署リレーションテーブルを外部結合に修正する。    */
/*------------------------------------------------------------------*/
/*  引数：                                                          */
/*  戻値：                                                          */
/********************************************************************/
IS
BEGIN

EXECUTE IMMEDIATE 'TRUNCATE TABLE WK_1P2P';

            --1P2P一時
INSERT INTO WK_1P2P
    --サービス種別ｺｰﾄﾞ      --受注№                --書類不備区分
    (SER_SHBET_CD,           JUCH_NO,                SHORUI_FBI_KBN,
    --FIT顧客№             --FIT管理№             --管理№枝番
    FIT_KKYK_NO,            FIT_KNR_NO,             KNR_NO_EBN,
    --担当営業事業部ｺｰﾄﾞ    --担当営業地区ｺｰﾄﾞ      --担当営業部ｺｰﾄﾞ
    TANT_EIGYO_JGYO_CD,     TANT_EIGYO_TIKU_CD,     TANT_EIGYO_BU_CD,
    --担当営業課ｺｰﾄﾞ        --担当営業係ｺｰﾄﾞ        --担当営業者ｺｰﾄﾞ
    TANT_EIGYO_KA_CD,       TANT_EIGYO_KAKARI_CD,   TANT_EIGYOSHA,
    --FIT代理店ｺｰﾄﾞ         --申込み日              --備考
    FIT_DAIRITEN_CD,        MSKM_DATE,              BIKO,
    --ﾚｺｰﾄﾞ登録日/時刻1P    --ﾚｺｰﾄﾞ更新日/時刻1P    --削除区分1P
    RECD_INSERT_DATE_1P,    RECD_UPDATE_DATE_1P,    SAKJO_KBN_1P,
    --代理店適用欄          --受注明細番号          --電話番号
    DAIRITEN_TEKIYO_RAN,    JUCH_MESAI_BNG,         TEL,
    --圧縮電話番号          --電話種別              --受注種別
    ASHK_TEL,               DNWA_SHBET,             JUCH_SHBET,
    --ISDN種別              --申込NO                --設置先NO
    ISDN_SHBET,             MSKM_NO,                SETI_NO,
    --顧客部署ｺｰﾄﾞ          --ｷｬﾝｾﾙ受付日           --ｷｬﾝｾﾙ対象区分
    KKYK_BSH_CD,            C_UKTK_DATE,            C_TAISH_KBN,
    --ｷｬﾝｾﾙ理由             --レｺｰﾄﾞ登録日/時刻2P   --レｺｰﾄﾞ更新日/時刻2P
    C_RIYU,                 RECD_INSERT_DATE_2P,    RECD_UPDATE_DATE_2P,
    --削除区分2P
    SAKJO_KBN_2P)

SELECT DISTINCT
        --AOBAｻｰﾋﾞｽｺｰﾄﾞ変換ﾏｽﾀ 出力ｻｰﾋﾞｽｺｰﾄﾞ
        NVL((SELECT K.SHTRYOK_SCD
             FROM TB_AOBA_SCD_HNKN_MST K
             WHERE K.ASHK_TEL = D.ASHK_TEL
             AND K.AOBA_SCD = F.AOBA_SCD),
             --通信ｻｰﾋﾞｽ商品ﾏｽﾀ AOBAｻｰﾋﾞｽｺｰﾄﾞ
             F.AOBA_SCD),
        --受注 受注NO
        B.JUCH_NO,
        --受注 書類不備区分
        B.SHORUI_FBI_KBN,
        --受注ﾘｿｰｽ管理 顧客ｺｰﾄﾞ
        A.KKYK_CD,
        --受注 FIT管理番号
        B.FIT_KANRIBNG,
        --受注 FIT管理番号枝番
        B.FIT_KANRIBNG_EBN,
        --社員・部署 ﾘﾚｰｼｮﾝ 事業部ｺｰﾄﾞ
        C.JGYO_CD,
        --社員・部署 ﾘﾚｰｼｮﾝ 地区ｺｰﾄﾞ
        C.TIKU_CD,
        --社員・部署 ﾘﾚｰｼｮﾝ 部署ｺｰﾄﾞ
        C.BSH_CD,
        --社員・部署 ﾘﾚｰｼｮﾝ 課ｺｰﾄﾞ
        C.KA_CD,
        --社員・部署 ﾘﾚｰｼｮﾝ 係ｺｰﾄﾞ
        C.KAKARI_CD,
        --受注 FVL社員ｺｰﾄﾞ
        B.FVL_SHAIN_CD,
        --受注 代理店ｺｰﾄﾞ
        B.DAIRITEN_CD,
        --受注 FIT登録日
        TO_CHAR(B.FIT_TROK_DATE,'YYYYMMDD'),
        --受注 受注備考
        SUBSTRB(REPLACE(B.JUCH_BIKO,',','，'),1,64),
        --受注 登録日時
        TO_CHAR(B.INSERT_DATE,'YYMMDDHH24MISS'),
        --受注 更新日時
        TO_CHAR(B.UPDATE_DATE,'YYMMDDHH24MISS'),
        --受注 削除ﾌﾗｸﾞ
        CASE B.SAKJO_FLG WHEN '1' THEN 'X' ELSE ' ' END,
        --受注 代理店使用欄
        B.DAIRITEN_SIYO_RAN,
        --受注明細 受注明細NO
        D.JUCH_MESAI_NO,
        --受注明細 電話番号
        D.TEL,
        --受注明細 圧縮電話番号
        D.ASHK_TEL,
        --固定電話受注明細内容 端末種別
        CASE E.TMAT_SHBET WHEN '2' THEN 'F' ELSE 'T' END,
        --固定電話受注明細内容 付属品ﾀｲﾌﾟｺｰﾄﾞ
        CASE E.FZKHINTYPE_CD WHEN '99' THEN '2' ELSE '1' END,
        --固定電話受注明細内容 ISDN種別区分
        E.ISDN_SHBET_KBN,
        --受注ﾘｿｰｽ管理 申込者NO
        A.MSKM_NO,
        --申込者・設置先 ﾘﾚｰｼｮﾝ 申込者設置先SEQ
        G.MSKM_SETI_SEQ,
        --受注明細 部署別仕訳ｺｰﾄﾞ
        D.BSH_BETSIWAKE_CD,
        --受注明細 ｷｬﾝｾﾙ受付日
        TO_CHAR(D.C_UKTK_DATE,'YYYYMMDD'),
        --受注明細 ｷｬﾝｾﾙ対象区分
        D.C_TAISH_KBN,
        --受注明細 ｷｬﾝｾﾙ理由ｺｰﾄﾞ
        D.C_RIYU_CD,
        --受注明細 登録日時
        TO_CHAR(D.INSERT_DATE,'YYMMDDHH24MISS'),
        --受注明細 更新日時
        TO_CHAR(D.UPDATE_DATE,'YYMMDDHH24MISS'),
        --受注明細 削除ﾌﾗｸﾞ
        CASE D.SAKJO_FLG WHEN '1' THEN 'X' ELSE ' ' END

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
COMMIT;
EXCEPTION
WHEN OTHERS THEN
ROLLBACK;
RAISE_APPLICATION_ERROR(-20500, '[ユーザエラー]AOBA転送日次(1P2P一時テーブル挿入)でエラーが発生しました。');
END;
 
/
