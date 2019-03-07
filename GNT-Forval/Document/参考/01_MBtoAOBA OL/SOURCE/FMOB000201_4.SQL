CREATE OR REPLACE PROCEDURE FMOB000201_4
/********************************************************************/
/*  関数名：FMOB000201_4.sql                                        */
/*  機能名：AOBA転送日次(2P一時ﾃｰﾌﾞﾙ挿入)                           */
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

EXECUTE IMMEDIATE 'TRUNCATE TABLE WK_2P';

			--2P一時
INSERT INTO WK_2P
	--サービス種別ｺｰﾄﾞ		--受注番号				--受注明細番号
	(SER_SHBET_CD,           JUCH_BNG,               JUCH_MESAI_BNG,
	--電話番号              --圧縮電話番号          --電話種別
	TEL,                    ASHK_TEL,               DNWA_SHBET,
	--受注種別              --ISDN種別              --申込NO
	JUCH_SHBET,             ISDN_SHBET,             MSKM_NO,
	--設置先NO              --顧客部署ｺｰﾄﾞ          --ｷｬﾝｾﾙ受付日
	SETI_NO,                KKYK_BSH_CD,            C_UKTK_DATE,
	--ｷｬﾝｾﾙ対象区分         --ｷｬﾝｾﾙ理由             --レｺｰﾄﾞ登録日/時刻
	C_TAISH_KBN,            C_RIYU,                 RECD_INSERT_DATE,
	--レｺｰﾄﾞ更新日/時刻     --削除区分
	RECD_UPDATE_DATE,       SAKJO_KBN)

SELECT	--1P2P一時 サービス種別ｺｰﾄﾞ
		A.SER_SHBET_CD,
		--1P2P一時 受注№
		A.JUCH_NO,
		--1P2P一時 受注明細番号
		A.JUCH_MESAI_BNG,
		--1P2P一時 電話番号
		A.TEL,
		--1P2P一時 圧縮電話番号
		A.ASHK_TEL,
		--1P2P一時 電話種別
		A.DNWA_SHBET,
		--1P2P一時 受注種別
		A.JUCH_SHBET,
		--1P2P一時 ISDN種別
		A.ISDN_SHBET,
		--1P2P一時 申込NO
		A.MSKM_NO,
		--1P2P一時 設置先NO
		A.SETI_NO,
		--1P2P一時 顧客部署ｺｰﾄﾞ
		A.KKYK_BSH_CD,
		--1P2P一時 ｷｬﾝｾﾙ受付日
		A.C_UKTK_DATE,
		--1P2P一時 ｷｬﾝｾﾙ対象区分
		A.C_TAISH_KBN,
		--1P2P一時 ｷｬﾝｾﾙ理由
		A.C_RIYU,
		--1P2P一時 レｺｰﾄﾞ登録日/時刻2P
		A.RECD_INSERT_DATE_2P,
		--1P2P一時 レｺｰﾄﾞ更新日/時刻2P
		A.RECD_UPDATE_DATE_2P,
		--1P2P一時 削除区分2P
		A.SAKJO_KBN_2P
FROM	--1P2P一時
		WK_1P2P A;
COMMIT;
EXCEPTION
WHEN OTHERS THEN
ROLLBACK;
RAISE_APPLICATION_ERROR(-20500, '[ユーザエラー]AOBA転送日次(2P一時テーブル挿入)でエラーが発生しました。');
END;
 
/
