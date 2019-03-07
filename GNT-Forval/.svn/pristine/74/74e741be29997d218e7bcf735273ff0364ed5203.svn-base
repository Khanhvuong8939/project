CREATE OR REPLACE PROCEDURE FMOB000201_3
/********************************************************************/
/*  関数名：FMOB000201_3.sql                                        */
/*  機能名：AOBA転送日次(1P一時ﾃｰﾌﾞﾙ挿入)                           */
/*  作成者：TIS杉本                                                 */
/*  作成日：2007/11/                                                */
/*  修正者：TIS杉本                                                 */
/*  修正日：2007/12/13                                              */
/*  修正日：2008/01/21                                              */
/*  修正箇所：挿入先のテーブルのカラムを記入(コーディング規約 )     */
/*  修正者：TIS杉本                                                 */
/*  修正日：2008/02/20                                              */
/*  修正箇所：EXCEPTION 発生時のエラーに対応                        */
/*------------------------------------------------------------------*/
/*  引数：                                                          */
/*  戻値：                                                          */
/********************************************************************/
IS
BEGIN

EXECUTE IMMEDIATE 'TRUNCATE TABLE WK_1P';

			--1P一時
INSERT INTO WK_1P
	--サービス種別ｺｰﾄﾞ		--受注№				--書類不備区分
	(SER_SHBET_CD,           JUCH_NO,                SHORUI_FBI_KBN,
	--FIT顧客№             --FIT管理№             --管理№枝番
	FIT_KKYK_NO,            FIT_KNR_NO,             KNR_NO_EBN,
	--担当営業事業部ｺｰﾄﾞ    --担当営業地区ｺｰﾄﾞ      --担当営業部ｺｰﾄﾞ
	TANT_EIGYO_JGYO_CD,     TANT_EIGYO_TIKU_CD,     TANT_EIGYO_BU_CD,
	--担当営業課ｺｰﾄﾞ        --担当営業係ｺｰﾄﾞ        --担当営業者ｺｰﾄﾞ
	TANT_EIGYO_KA_CD,       TANT_EIGYO_KAKARI_CD,   TANT_EIGYOSHA_CD,
	--FIT代理店ｺｰﾄﾞ         --申込み日              --備考
	FIT_DAIRITEN_CD,        MSKM_DATE,              BIKO,
	--レｺｰﾄﾞ登録日/時刻     --レｺｰﾄﾞ更新日/時刻     --削除区分
	RECD_INSERT_DATE,       RECD_UPDATE_DATE,       SAKJO_KBN,
	--代理店適用欄
	DAIRITEN_TEKIYO_RAN)

SELECT	DISTINCT
		--1P2P一時 サービス種別ｺｰﾄﾞ
		A.SER_SHBET_CD,
		--1P2P一時 受注№
		A.JUCH_NO,
		--1P2P一時 書類不備区分
		A.SHORUI_FBI_KBN,
		--1P2P一時 FIT顧客№
		A.FIT_KKYK_NO,
		--1P2P一時 FIT管理№
		A.FIT_KNR_NO,
		--1P2P一時 管理№枝番
		A.KNR_NO_EBN,
		--1P2P一時 担当営業事業部ｺｰﾄﾞ
		A.TANT_EIGYO_JGYO_CD,
		--1P2P一時 担当営業地区ｺｰﾄﾞ
		A.TANT_EIGYO_TIKU_CD,
		--1P2P一時 担当営業部ｺｰﾄﾞ
		A.TANT_EIGYO_BU_CD,
		--1P2P一時 担当営業課ｺｰﾄﾞ
		A.TANT_EIGYO_KA_CD,
		--1P2P一時 担当営業係ｺｰﾄﾞ
		A.TANT_EIGYO_KAKARI_CD,
		--1P2P一時 担当営業者ｺｰﾄﾞ
		A.TANT_EIGYOSHA,
		--1P2P一時 FIT代理店ｺｰﾄﾞ
		A.FIT_DAIRITEN_CD,
		--1P2P一時 申込み日
		A.MSKM_DATE,
		--1P2P一時 備考
		A.BIKO,
		--1P2P一時 ﾚｺｰﾄﾞ登録日/時刻1P
		A.RECD_INSERT_DATE_1P,
		--1P2P一時 ﾚｺｰﾄﾞ更新日/時刻1P
		A.RECD_UPDATE_DATE_1P,
		--1P2P一時 削除区分1P
		A.SAKJO_KBN_1P,
		--1P2P一時 代理店適用欄
		A.DAIRITEN_TEKIYO_RAN
FROM	--1P2P一時
		WK_1P2P A;
COMMIT;
EXCEPTION
WHEN OTHERS THEN
ROLLBACK;
RAISE_APPLICATION_ERROR(-20500, '[ユーザエラー]AOBA転送日次(1P一時テーブル挿入)でエラーが発生しました。');
END;
 
/
