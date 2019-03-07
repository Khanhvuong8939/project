CREATE OR REPLACE PROCEDURE		FAOB010110
/********************************************************************/
/*  関数名：FAOB0101												*/
/*  機能名：請求書データ作成：領収書発行フラグ更新					*/
/*  作成者：山田													*/
/*  作成日：2005/10/27												*/
/*  更新日：2006/05/15	田中	OEM取引形態コード対応				*/
/*	VER   ：1.01													*/
/*------------------------------------------------------------------*/
/*	引数：		無し												*/
/*	戻値：		無し												*/
/********************************************************************/
IS

	/* ローカル定数 */
	/* 20060515 ADD Start */
	CST_OEM_CODE2			CONSTANT wk_seikyu_hakko_kokyaku.oem_keitai_cd%TYPE := '02';	--OEM取引形態コード(データと印刷)
	CST_OEM_CODE3			CONSTANT wk_seikyu_hakko_kokyaku.oem_keitai_cd%TYPE := '03';	--OEM取引形態コード(印刷のみ)
	/* 20060515 ADD End */

	/* ローカル変数 */
	nRet					NUMBER;								--リターンコード
	dDate					tb_seikyu_sum.update_date%TYPE;		--システム日付
	vUser					tb_seikyu_sum.update_user%TYPE;		--ユーザ

BEGIN

	/* システム日時の取得 */
	nRet := aoba_common.get_date( dDate );

	/* ユーザの取得 */
	nRet := aoba_common.get_user( vUser );

	/* 領収書発行フラグ更新 */
	UPDATE	tb_seikyu_sum a
	SET	(ryoshusho_flg,										-- 領収書フラグ
		update_date,										-- 更新日時
		update_user)										-- 更新ユーザー
		=
		(SELECT aoba_const.CST_Y_FLG,
			dDate,
			vUser
		FROM	wk_seikyu_hakko_kokyaku b
		WHERE	b.kokyaku_cd = a.kokyaku_cd
		AND	b.kakin_nengetu = a.kakin_nengetu
		AND	b.seikyu_gun = a.seikyu_gun
		AND	b.ryoshusho_flg = aoba_const.CST_Y_FLG
		AND	b.oem_keitai_cd IN (CST_OEM_CODE2,CST_OEM_CODE3)		-- 20060515 ADD
		)
	WHERE 	EXISTS
			(
			SELECT 1
			FROM	wk_seikyu_hakko_kokyaku b1
			WHERE	b1.kokyaku_cd = a.kokyaku_cd
			AND	b1.kakin_nengetu = a.kakin_nengetu
			AND	b1.seikyu_gun = a.seikyu_gun
			AND	b1.ryoshusho_flg = aoba_const.CST_Y_FLG
			AND	b1.oem_keitai_cd IN (CST_OEM_CODE2,CST_OEM_CODE3)		-- 20060515 ADD
			);

END FAOB010110;
/
