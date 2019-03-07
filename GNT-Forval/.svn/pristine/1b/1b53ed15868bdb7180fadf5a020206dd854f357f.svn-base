CREATE OR REPLACE PROCEDURE	        FAOB010107
/********************************************************************/
/*  関数名：FAOB0101												*/
/*  機能名：請求書データ作成：内訳コメントデータ抽出				*/
/*  作成者：山田													*/
/*  作成日：2005/10/31												*/
/*  更新日：2006/02/28	田中	対照データ取得法変更				*/
/*	VER   ：1.01													*/
/*------------------------------------------------------------------*/
/*	引数：		無し												*/
/*	戻値：		無し												*/
/********************************************************************/
IS

	/* ローカル定数 */
	CST_DEL_TABLE	CONSTANT VARCHAR2(30) := 'WK_BL_UTIWAKE_COMMENT';	--削除対象テーブル名

	/* ローカル変数 */
	nRet					NUMBER;											--リターンコード
	dSysDate				wk_bl_utiwake_comment.insert_date%TYPE;				--システム日付
	vUser					wk_bl_utiwake_comment.insert_user%TYPE;				--ユーザ
BEGIN

	/* 請求書内訳情報一時TBLの削除 */
	EXECUTE IMMEDIATE 'TRUNCATE TABLE ' || CST_DEL_TABLE;

	/* システム日時の取得 */
	nRet := aoba_common.get_date( dSysDate );

	/* ユーザの取得 */
	nRet := aoba_common.get_user( vUser );

	/* 請求書内訳情報一時TBL挿入 */
	INSERT INTO	wk_bl_utiwake_comment(
			kokyaku_cd_sk,														-- 顧客コードソートキー
			kakin_nengetu_sk,													-- 課金年月ソートキー
			comment_inf,														-- コメント情報
			utiwake_comment1,													-- 内訳コメント１
			utiwake_comment2,													-- 内訳コメント２
			utiwake_comment3,													-- 内訳コメント３
			utiwake_comment4,													-- 内訳コメント４
			utiwake_comment5,													-- 内訳コメント５
			insert_date,														-- 登録日時
			insert_user)														-- 登録ユーザ
	/* 2005/12/14 抽出条件変更
	SELECT	DISTINCT a.kokyaku_cd,
			a.seikyu_nengetu,
			RPAD(
				a.kokyaku_cd	||
				a.seikyu_nengetu	||
				RPAD(b.comment1, 50, '　')	||
				RPAD(b.comment2, 26, '　')	||
				RPAD(b.comment3, 24, ' ')	||
				RPAD(b.comment4, 26, '　')	||
				RPAD(b.comment5, 24, ' ')
				, 300, ' ') AS comment_inf,
			b.comment1,
			b.comment2,
			b.comment3,
			b.comment4,
			b.comment5,
			dSysDate,
			vUser
	FROM	wk_seikyu_hakko_kokyaku a,
			tb_seikyusho_utiwake_joho b,
			tb_batch_ctrl c
	WHERE	c.jobnet_id = aoba_out_const.CST_OUT_FAOB010100
	AND	a.seikyu_nengetu = c.shori_taisho_nengetu
	AND	a.seikyu_gun = c.shori_taisho_gun
	AND	a.kokyaku_cd = b.kokyaku_cd
	AND	a.seikyu_gun = b.seikyu_gun
	AND	a.seikyu_nengetu = b.kakin_nengetu;	*/
	/* 20060228 start */
/*	SELECT	a.kokyaku_cd,
			a.seikyu_nengetu,
			RPAD(
				a.kokyaku_cd	||
				a.seikyu_nengetu	||
				RPAD(b.comment1, 50, '　')	||
				RPAD(b.comment2, 26, '　')	||
				RPAD(b.comment3, 24, ' ')	||
				RPAD(b.comment4, 26, '　')	||
				RPAD(b.comment5, 24, ' ')
				, 300, ' ') AS comment_inf,
			b.comment1,
			b.comment2,
			b.comment3,
			b.comment4,
			b.comment5,
			dSysDate,
			vUser
	FROM	wk_seikyu_hakko_kokyaku a,
			(
			SELECT	b1.kokyaku_cd,
					b2.kakin_nengetu,
					b1.seikyu_gun,
					b1.comment1,
					b1.comment2,
					b1.comment3,
					b1.comment4,
					b1.comment5
			FROM	tb_seikyusho_utiwake_joho b1,
					(
					SELECT	kokyaku_cd,
							TRIM(MAX(kakin_nengetu)) kakin_nengetu
					FROM	tb_seikyusho_utiwake_joho
					GROUP BY	kokyaku_cd
					) b2
			WHERE	b1.kokyaku_cd = b2.kokyaku_cd
			AND		b1.kakin_nengetu = b2.kakin_nengetu
			) b,
			tb_batch_ctrl c
	WHERE	c.jobnet_id = aoba_out_const.CST_OUT_FAOB010100
	AND	a.seikyu_nengetu = c.shori_taisho_nengetu
	AND	a.seikyu_gun = c.shori_taisho_gun
	AND	a.kokyaku_cd = b.kokyaku_cd
	AND	a.seikyu_gun = b.seikyu_gun
	AND	a.kakin_nengetu = b.kakin_nengetu;*/
SELECT	b.kokyaku_cd,
			b.seikyu_nengetu,
			RPAD(
				b.kokyaku_cd	||
				b.seikyu_nengetu	||
				RPAD(a.comment1, 50, '　')	||
				RPAD(a.comment2, 26, '　')	||
				RPAD(a.comment3, 24, ' ')	||
				RPAD(a.comment4, 26, '　')	||
				RPAD(a.comment5, 24, ' ')
				, 300, ' ') AS comment_inf,
			a.comment1,
			a.comment2,
			a.comment3,
			a.comment4,
			a.comment5,
			dSysDate,
			vUser
FROM	tb_seikyusho_utiwake_joho a,
		(SELECT	a1.kokyaku_cd,
				a1.seikyu_nengetu,
				b1.kakin_nengetu,
				b1.seikyu_gun
		FROM	wk_bl_kokyaku_info a1,
				(SELECT	b11.kokyaku_cd,
						MAX(b11.kakin_nengetu) AS kakin_nengetu,
						b11.seikyu_nengetu,
						b11.seikyu_gun
				FROM	wk_seikyu_hakko_kokyaku b11
				GROUP BY	b11.kokyaku_cd,
							b11.seikyu_nengetu,
							b11.seikyu_gun
				) b1
		WHERE a1.kokyaku_cd = b1.kokyaku_cd
		AND  a1.seikyu_nengetu = b1.seikyu_nengetu
		) b
WHERE	b.kokyaku_cd = a.kokyaku_cd (+)
AND		b.kakin_nengetu = a.kakin_nengetu (+)
AND		b.seikyu_gun = a.seikyu_gun (+);
/* 20060228 end */

END FAOB010107;
/
