CREATE OR REPLACE PROCEDURE           FASB010103
/********************************************************************/
/*	関数名：FASB010103												*/
/*	機能名：口座振替入金処理	請求サマリ振替結果更新処理			*/
/*	作成者：宮下　克徳												*/
/*	作成日：2005/12/01												*/
/*	更新日：2006/02/16 TIS成毛 請求サマリに存在しないデータを除外	*/
/*	      ：2006/02/16 TIS成毛 請求サマリに存在しないデータをエラー	*/
/*	      ：                   に出力する処理を追加					*/
/*		  ：2006/07/05 木原　　実入金日対応							*/
/*	VER   ：1.01													*/
/*------------------------------------------------------------------*/
/*	引数：		無し												*/
/*	戻値：		無し												*/
/********************************************************************/
IS
	/* 変数 */
	nRet					NUMBER;										--リターンコード
	dShori_date				DATE;										--処理日付
	vShori_user				VARCHAR2(256);								--処理ﾕｰｻﾞ
	cHurikae_kekka_cd		tb_seikyu_sum.hurikae_kekka_cd%TYPE;		--振替結果コード

	/* カーソル定義 */
	CURSOR cur_hurikae_rec
	IS
		SELECT	a.kokyaku_cd,
				a.kakin_nengetu,
				a.seikyu_gun,
				a.hurikae_kekka_cd
		FROM	wk_shuno_hurikae a
		WHERE	a.hurikae_kekka_cd <> aoba_syuno_const.CST_KOUHURI_MI_CD
		AND		EXISTS (
					SELECT (1)
					FROM   tb_seikyu_sum b
					WHERE  b.kokyaku_cd = a.kokyaku_cd
					AND    b.seikyu_nengetu = a.kakin_nengetu
					AND    b.seikyu_gun = a.seikyu_gun
				);

BEGIN

	/*	処理日付取得	*/
	nRet := aoba_common.get_date(dShori_date);

	/*	処理ﾕｰｻﾞ取得	*/
	nRet := aoba_common.get_user(vShori_user);

	/* 収納情報口座振替ワークテーブル書込み */
	FOR rec_hurikae_rec IN cur_hurikae_rec LOOP
		/* 請求サマリーデータ検索 */
		SELECT		hurikae_kekka_cd
		INTO		cHurikae_kekka_cd
		FROM		tb_seikyu_sum
		WHERE		kokyaku_cd = rec_hurikae_rec.kokyaku_cd
		AND			seikyu_nengetu = rec_hurikae_rec.kakin_nengetu
		AND			seikyu_gun = rec_hurikae_rec.seikyu_gun
		GROUP BY	hurikae_kekka_cd;

		/* 振替結果コード更新 */
		IF rec_hurikae_rec.seikyu_gun = aoba_syuno_const.CST_SEIKYU_GUN_25
		AND cHurikae_kekka_cd = aoba_syuno_const.CST_KOUHURI_DEF_CD
		AND	rec_hurikae_rec.hurikae_kekka_cd = aoba_syuno_const.CST_KOUHURI_ZUMI_CD THEN
			cHurikae_kekka_cd := aoba_syuno_const.CST_KOUHURI_SAISEIKYU_FUYO;
		ELSE
			cHurikae_kekka_cd := rec_hurikae_rec.hurikae_kekka_cd;
		END IF;

		/* 請求サマリ更新 */
		UPDATE	tb_seikyu_sum
		SET		hurikae_kekka_cd = cHurikae_kekka_cd,
				update_date = dShori_date,
				update_user = vShori_user
		WHERE	kokyaku_cd = rec_hurikae_rec.kokyaku_cd
		AND		seikyu_nengetu = rec_hurikae_rec.kakin_nengetu
		AND		seikyu_gun = rec_hurikae_rec.seikyu_gun;

	END LOOP;

	/* 収納情報口座振替エラーテーブルへの追加 2006/02/16 */
	/*   消込対象が請求サマリに存在しない場合に追加      */
	INSERT INTO wk_shuno_hurikae_err(
			kokyaku_cd,				kakin_nengetu,			seikyu_gun,
			hurikae_itaku_cd,		oem_cd,					nyukingaku,
			nyukin_date,			hurikae_kekka_cd,		data_kbn,
			torihiki_bank_cd,		torihiki_bank_name,		torihiki_shiten_cd,
			torihiki_shiten_name,	yokin_shumoku,			kouza_bng,
			yokinsha_name,			shinki_cd,				mfs_shiyoran,
			sche_kakin_nengetu,		insert_date,			insert_user,
/* 2006/07/05 木原 実入金対応 */
			jitu_nyukin_date
/* end of 2006/07/05 木原 実入金対応 */
			)
	SELECT	a.kokyaku_cd,			a.kakin_nengetu,		a.seikyu_gun,
			a.hurikae_itaku_cd,		a.oem_cd,				a.nyukingaku,
			a.nyukin_date,			a.hurikae_kekka_cd,		a.data_kbn,
			a.torihiki_bank_cd,		a.torihiki_bank_name,	a.torihiki_shiten_cd,
			a.torihiki_shiten_name,	a.yokin_shumoku,		a.kouza_bng,
			a.yokinsha_name,		a.shinki_cd,			a.mfs_shiyoran,
			a.sche_kakin_nengetu,	dShori_date,			vShori_user,
/* 2006/07/05 木原 実入金対応 */
			a.jitu_nyukin_date
/* end of 2006/07/05 木原 実入金対応 */
	FROM	wk_shuno_hurikae a
	WHERE	a.hurikae_kekka_cd <> aoba_syuno_const.CST_KOUHURI_MI_CD
	AND		NOT EXISTS (
				SELECT (1)
				FROM   tb_seikyu_sum b
				WHERE  b.kokyaku_cd = a.kokyaku_cd
				AND    b.seikyu_nengetu = a.kakin_nengetu
				AND    b.seikyu_gun = a.seikyu_gun
			);

END FASB010103;
/
