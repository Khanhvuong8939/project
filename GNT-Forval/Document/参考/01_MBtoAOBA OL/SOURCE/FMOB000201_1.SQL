CREATE OR REPLACE PROCEDURE FMOB000201_1
/********************************************************************/
/*  関数名：FMOB000201_1.sql                                        */
/*  機能名：AOBA転送日次(FITMST一時ﾃｰﾌﾞﾙ挿入)                       */
/*  作成者：TIS杉本                                                 */
/*  作成日：2007/11/                                                */
/*  修正者：TIS杉本                                                 */
/*  修正日：2007/12/26                                              */
/*  修正箇所：                                                      */
/*    43(OEM･支払種別･請求先 ﾘﾚｰｼｮﾝ 口座名義人(カナ))30byte切取     */
/*  修正者：TIS杉本                                                 */
/*  修正日：2008/01/21                                              */
/*  修正箇所：挿入先のテーブルのカラムを記入(コーディング規約 )     */
/*  修正者：TIS杉本                                                 */
/*  修正日：2008/02/05                                              */
/*  修正箇所：適用ｽﾃｰﾀｽ='1'のデータのみ抽出できるよう修正する。     */
/*  修正者：TIS二宮                                                 */
/*  修正日：2008/02/09                                              */
/*  修正箇所：OEM_CDを申込者から取るよう変更                        */
/*            申込者・OEM・申込者オプションのJOIN条件変更           */
/*                  適用ｽﾃｰﾀｽ追加                                   */
/*                  カラム明示化                                    */
/*                  適用開始群→適用終了群                          */
/*  修正者：TIS杉本                                                 */
/*  修正日：2008/03/24                                              */
/*  修正箇所：OA顧客設置先リレーションテーブルを外部結合に修正する。*/
/*  修正者：TIS杉本                                                 */
/*  修正日：2008/03/25                                              */
/*  修正箇所：削除フラグが'0'のものが対象となるよう修正する。       */
/*  修正者：TIS杉本                                                 */
/*  修正日：2008/04/04                                              */
/*  修正箇所：請求先に関しても                                      */
/*            削除フラグが'0'のものが対象となるよう修正する。       */
/*  修正箇所：54カラム目は口座名義人ではなく、 ｶｰﾄﾞ名義人           */
/*  修正箇所：削除済み申込者ｵﾌﾟｼｮﾝと未削除申込者ｵﾌﾟｼｮﾝがある場合    */
/*            ある顧客に対して１行のみ出力されるよう修正。          */
/*  修正者：TIS杉本                                                 */
/*  修正日：2008/04/07                                              */
/*  修正箇所：申込者建物名、請求先建物名のカラムのＦを全角に修正    */
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
BEGIN

EXECUTE IMMEDIATE 'TRUNCATE TABLE WK_FITMST';

            --FITMST一時
INSERT INTO WK_FITMST
    --削除区分              --法人/個人区分         --顧客№
    (SAKJO_KBN,             HOJIN_KOJIN_KBN,        KKYK_NO,
    --FIT顧客№             --FIT管理№             --FIT親顧客№
    FIT_KKYK_NO,            FIT_KNR_NO,             FIT_OYAKKYK_NO,
    --ｻｰﾋﾞｽｺｰﾄﾞ             --予備１                --予備２
    SCD,                    YOBI1,                  YOBI2,
    --予備３                --予備４                --備考
    YOBI3,                  YOBI4,                  BIKO,
    --申込　カナ会社名      --申込　漢字会社名      --申込　郵便番号
    MSKM_KN_SHAMEI,         MSKM_KANJI_SHAMEI,      MSKM_YUBIN_BNG,
    --申込　県名            --申込　市区郡名        --申込　町村丁字名
    MSKM_KEN_MEI,           MSKM_SIKUGUN_MEI,       MSKM_CHOSON_CHOZ,
    --申込　建物名          --部署/担当者名         --申込　電話番号
    MSKM_TTMNMEI,           BSH_TANTOSHAMEI,        MSKM_TEL,
    --FAX番号               --請求　漢字会社名      --請求先会社名カナ
    FAX_BNG,                SEIQ_KANJI_SHAMEI,      SEIQ_SHAMEI_KN,
    --請求先部署担当者名    --請求　郵便番号        --請求　県名
    SEIQ_BSH_TANTOSHAMEI,   SEIQ_YUBIN_BNG,         SEIQ_KEN_MEI,
    --請求　市区郡名        --請求　町村丁字名      --請求　建物名
    SEIQ_SIKUGUN_MEI,       CHOSON_CHOZ,            SEIQ_TTMNMEI,
    --請求　電話番号        --料金明細有無区分      --設置　漢字会社名
    SEIQ_TEL,               RYOKIN_MESAI_UM_KBN,    SETI_KANJI_SHAMEI,
    --設置　郵便番号        --設置　県名            --設置　市区郡名
    SETI_YUBIN_BNG,         SETI_KEN_MEI,           SETI_SIKUGUN_MEI,
    --設置　町村丁字名      --設置　建物名          --担当者名
    SETI_CHOSON_CHOZ,       SETI_TTMNMEI,           TANTOSHAMEI,
    --設置　電話番号        --オプションサービス    --振替種別
    SETI_TEL,               OPT_SER,                FRKE_SHBET,
    --預金者名              --金融機関ｺｰﾄﾞ          --金融機関支店ｺｰﾄﾞ
    YOKINSHA_MEI,           KINNYUKIKAN_CD,         KINNYUKIKAN_SITEN_CD,
    --預金種別              --口座番号              --ﾚｺｰﾄﾞ登録日/時刻
    YOKIN_SHBET,            KOZA_BNG,               RECD_INSERT_DATE,
    --ﾚｺｰﾄﾞ更新日/時刻      --FITスーパーライト     --カード会社ｺｰﾄﾞ
    RECD_UPDATE_DATE,       FIT_SPR_LIGHT,          CADGISH_CD,
    --カード№              --カード有効期限        --カード名義
    CAD_NO,                 CAD_YUKO_KIGEN,         CAD_MEGI,
    --勤務先名              --勤務先電話番号        --勤務先圧縮電話番号
    KINM_MEI,               KINM_TEL,               KINM_ASHK_TEL,
    --生年月日　和暦区分    --生年月日              --性別
    BIRTHDAY_WAREKI_KBN,    BIRTHDAY,               SEX,
    --ＯＡ顧客№            --OEMｺｰﾄﾞ               --免税フラグ
    OAKOKYK_NO,             OEM_CD,                 MENZEI_FLG,
    --口座番号表示区分      --まとめ除外フラグ
    KOZA_BNG_HYJI_KBN,      MATOME_JYOGAI_FLG)

SELECT DISTINCT
        --請求先 削除フラグ
        CASE E.SAKJO_FLG WHEN '1' THEN 'X' ELSE NULL END,
        --申込者 法人/個人ﾌﾗｸﾞ
         D.HOJIN_KOJIN_FLG,
        --ヌルをセットする
         NULL,
        --請求先 顧客ｺｰﾄﾞ
         E.KKYK_CD,
        --受注 FIT管理番号
         A.FIT_KANRIBNG,
        --請求先 親顧客ｺｰﾄﾞ
         E.OYAKKYK_CD,
        --AOBAｻｰﾋﾞｽｺｰﾄﾞ変換ﾏｽﾀ 出力ｻｰﾋﾞｽｺｰﾄﾞ
        NVL((SELECT K.SHTRYOK_SCD
             FROM TB_AOBA_SCD_HNKN_MST K
             WHERE K.ASHK_TEL = B.ASHK_TEL
             AND K.AOBA_SCD = I.AOBA_SCD),
             --通信ｻｰﾋﾞｽ商品ﾏｽﾀ AOBAｻｰﾋﾞｽｺｰﾄﾞ
             I.AOBA_SCD),
        --ヌルをセットする
        NULL,
        --ヌルをセットする
        NULL,
        --ヌルをセットする
        NULL,
        --ヌルをセットする
        NULL,
        --申込者 備考
        SUBSTRB(D.BIKO,1,64),
        --申込者 申込者 カナ会社名
        D.MSKM_KN_SHAMEI,
        --申込者 申込者 漢字会社名
        D.MSKM_KANJI_SHAMEI,
        --申込者 申込者 郵便番号
        SUBSTRB(D.MSKM_YUBIN_BNG,1,3) ||
         NVL2(SUBSTRB(D.MSKM_YUBIN_BNG,4,4),
         '-' || SUBSTRB(D.MSKM_YUBIN_BNG,4,4),NULL),
        --申込者 申込者 県名
        D.MSKM_KEN_MEI,

        --申込者 申込者 市区郡名--申込者 申込者 区名
        SUBSTRB(RTRIM(LTRIM(D.MSKM_SIKUGUN_MEI, '　 '), '　 ') ||
                RTRIM(LTRIM(D.MSKM_KMEI, '　 '), '　 '),1,20),

        --申込者 申込者 町村丁字名
        SUBSTRB((RTRIM(LTRIM(D.MSKM_CHOSON_CHOZ, '　 '), '　 ') ||
        --申込者 申込者 丁目
         NVL2(RTRIM(LTRIM(D.MSKM_CHOM, '　 '), '　 '),
              RTRIM(LTRIM(D.MSKM_CHOM, '　 '), '　 ') || '丁目',NULL) ||
        --申込者 申込者 番地
         NVL2(RTRIM(LTRIM(D.MSKM_BANTI, '　 '), '　 '),
              RTRIM(LTRIM(D.MSKM_BANTI, '　 '), '　 ') || '番地',NULL) ||
        --申込者 申込者 号
         NVL2(RTRIM(LTRIM(D.MSKM_GO, '　 '), '　 '),
              RTRIM(LTRIM(D.MSKM_GO, '　  '), '　 ') || '号',NULL)),1,40 ),

        --申込者 申込者 建物名
        SUBSTRB((RTRIM(LTRIM(D.MSKM_TTMNMEI, '　 '), '　 ') ||
        --申込者 申込者 階数
         NVL2(RTRIM(LTRIM(D.MSKM_KSU, '　 '), '　 '),
              RTRIM(LTRIM(D.MSKM_KSU, '　 '), '　 ') || 'Ｆ',NULL) ||
        --申込者 申込者 号室
         NVL2(RTRIM(LTRIM(D.MSKM_GOSIT, '　 '), '　 '),
              RTRIM(LTRIM(D.MSKM_GOSIT, '　 '), '　 ') || '号室',NULL)),1,52),

        --申込者 申込者 担当者部署
        D.MSKM_TANT_BSH,
        --申込者 申込者 電話番号
        D.MSKM_TEL,
        --申込者 申込者 FAX番号
        D.MSKM_FAX_BNG,
        --請求先 請求先 漢字会社名
        E.SEIQ_KANJI_SHAMEI,
        --請求先 請求先 カナ
        E.SEIQ_KN,
        --請求先 請求先 担当者部署
        E.SEIQ_TANT_BSH,
        --請求先 請求先 郵便番号
        SUBSTRB(E.SEIQ_YUBIN_BNG,1,3) ||
         NVL2(SUBSTRB(E.SEIQ_YUBIN_BNG,4,4),
         '-' || SUBSTRB(E.SEIQ_YUBIN_BNG,4,4),NULL),
        --請求先 請求先 県名
        E.SEIQ_KEN_MEI ,

        --請求先 請求先 市区郡名--請求先 請求先 区名
        SUBSTRB(RTRIM(LTRIM(E.SEIQ_SIKUGUN_MEI, '　 '), '　 ') ||
                RTRIM(LTRIM(E.SEIQ_KMEI, '　 '), '　 '),1,20),

        --請求先 請求先 町村丁字名
        SUBSTRB((RTRIM(LTRIM(E.SEIQ_CHOSON_CHOZ, '　 '), '　 ') ||
        --請求先 請求先 丁目
         NVL2(RTRIM(LTRIM(E.SEIQ_CHOM, '　 '), '　 '),
              RTRIM(LTRIM(E.SEIQ_CHOM, '　 '), '　 ') || '丁目',NULL) ||
        --請求先 請求先 番地
         NVL2(RTRIM(LTRIM(E.SEIQ_BANTI, '　 '), '　 '),
              RTRIM(LTRIM(E.SEIQ_BANTI, '　 '), '　 ') || '番地',NULL) ||
        --請求先 請求先 号
         NVL2(RTRIM(LTRIM(E.SEIQ_GO, '　 '), '　 '),
               RTRIM(LTRIM(E.SEIQ_GO, '　 '), '　 ') || '号',NULL)),1,40 ),

        --請求先 請求先 建物名
        SUBSTRB((RTRIM(LTRIM(E.SEIQ_TTMNMEI, '　 '), '　 ') ||
        --請求先 請求先 階数
         NVL2(RTRIM(LTRIM(E.SEIQ_KSU, '　 '), '　 '),
               RTRIM(LTRIM(E.SEIQ_KSU, '　 '), '　 ') || 'Ｆ',NULL) ||
        --請求先 請求先 号室
         NVL2(RTRIM(LTRIM(E.SEIQ_GOSIT, '　 '), '　 '),
              RTRIM(LTRIM(E.SEIQ_GOSIT, '　 '), '　 ') || '号室',NULL)),1,52),

        --請求先 請求先 電話番号
        E.SEIQ_TEL,
        --ヌルをセットする
        NULL,
        NULL,
        NULL,
        NULL,
        NULL,
        NULL,
        NULL,
        NULL,
        NULL,

       --申込者･OEM･申込者ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ 申込者ｵﾌﾟｼｮﾝ
       CASE (SELECT P.MOPT FROM TB_MSKM_OEM_MOPT_R P
             WHERE P.MSKM_NO = Q.MSKM_NO
               AND P.TEKIYO_STAT='1'
               AND P.SAKJO_FLG = '0')
        WHEN '001' THEN 'S'
        WHEN '002' THEN 'A'
        WHEN '003' THEN 'B'
        WHEN '004' THEN 'C'
        WHEN '005' THEN 'D'
            ELSE NULL END,

        --OEM･支払種別･請求先 ﾘﾚｰｼｮﾝ 支払種別
         F.HARAI,
        --OEM･支払種別･請求先 ﾘﾚｰｼｮﾝ 口座名義人(カナ)
         SUBSTRB(F.KOZA_MEGININ_KN,1,30),
        --OEM･支払種別･請求先 ﾘﾚｰｼｮﾝ 金融機関ｺｰﾄﾞ
         F.KINNYUKIKAN_CD,
        --OEM･支払種別･請求先 ﾘﾚｰｼｮﾝ 金融機関支店ｺｰﾄﾞ
         F.KINNYUKIKAN_SITEN_CD,
        --OEM･支払種別･請求先 ﾘﾚｰｼｮﾝ 預金種目ﾌﾗｸﾞ
         F.YOKIN_SHMOK_FLG,
        --OEM･支払種別･請求先 ﾘﾚｰｼｮﾝ 口座番号
         F.KOZA_BNG,
        --請求先 登録日
        TO_CHAR(E.INSERT_DATE,'YYMMDDHH24MISS'),
        --請求先 更新日時
        TO_CHAR(E.UPDATE_DATE,'YYMMDDHH24MISS'),

        --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ 請求先ｵﾌﾟｼｮﾝｺｰﾄﾞ
        NVL((SELECT 1 FROM TB_SEIQ_OPT_R J
             WHERE J.SEIQ_OPT_CD = '012'
             AND E.KKYK_CD = J.KKYK_CD
             AND J.TEKIYO_STAT = '1'
             AND J.SAKJO_FLG = '0'),0),

        --OEM･支払種別･請求先 ﾘﾚｰｼｮﾝ ｶｰﾄﾞ会社ｺｰﾄﾞ
        F.CADGISH_CD,
        --OEM･支払種別･請求先 ﾘﾚｰｼｮﾝ ｶｰﾄﾞ番号
        F.CAD_BNG,
        --OEM･支払種別･請求先 ﾘﾚｰｼｮﾝ 有効期限
        TO_CHAR(F.YUKO_KIGEN,'YYMM'),
        --OEM･支払種別･請求先 ﾘﾚｰｼｮﾝ ｶｰﾄﾞ名義人
        F.CAD_MEGININ,
        --申込者 勤務先名
        D.KINM_MEI,
        --申込者 勤務先電話番号
        D.KINM_TEL,
        --申込者 勤務先圧縮電話番号
        D.KINM_ASHK_TEL,
        --申込者 生年月日 和暦区分
        D.BIRTHDAY_WAREKI_KBN,
        --申込者 生年月日
        TO_CHAR(D.BIRTHDAY,'YYYYMMDD'),
        --申込者 性別ﾌﾗｸﾞ
        D.SEX_FLG,
        --OA顧客・設置先ﾘﾚｰｼｮﾝ OA顧客番号
        H.OAKOKYK_BNG,

        --申込者 OEMｺｰﾄﾞ
        D.OEM_CD,

        --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ 請求先ｵﾌﾟｼｮﾝｺｰﾄﾞ
        (SELECT 1 FROM TB_SEIQ_OPT_R J
         WHERE J.SEIQ_OPT_CD = '192'  --免税ﾌﾗｸﾞ
         AND J.KKYK_CD = E.KKYK_CD
         AND J.TEKIYO_STAT = '1'
         AND J.SAKJO_FLG = '0'),

        --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ 請求先ｵﾌﾟｼｮﾝｺｰﾄﾞ
        (SELECT
         NVL2 ( (SELECT 1 FROM TB_SEIQ_OPT_R J
                 WHERE J.SEIQ_OPT_CD = '191'  --口座番号表示区分
                 AND J.KKYK_CD = E.KKYK_CD
                 AND J.TEKIYO_STAT = '1'
                 AND J.SAKJO_FLG = '0') , '1', '2')
         FROM TB_SEIQ_OPT_R J
         WHERE J.SEIQ_OPT_CD = '190'
         AND J.KKYK_CD = E.KKYK_CD
         AND J.TEKIYO_STAT = '1'
         AND J.SAKJO_FLG = '0'),

        --請求先・ｵﾌﾟｼｮﾝ ﾘﾚｰｼｮﾝ 請求先ｵﾌﾟｼｮﾝｺｰﾄﾞ
        (SELECT 1 FROM TB_SEIQ_OPT_R J
         WHERE J.SEIQ_OPT_CD = '193'  --まとめ除外ﾌﾗｸﾞ
         AND J.KKYK_CD = E.KKYK_CD
         AND J.TEKIYO_STAT = '1'
         AND J.SAKJO_FLG = '0')

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
COMMIT;
EXCEPTION
WHEN OTHERS THEN
ROLLBACK;
RAISE_APPLICATION_ERROR(-20500, '[ユーザエラー]AOBA転送日次(FITMST一時テーブル挿入)でエラーが発生しました。');
END;

 
/
