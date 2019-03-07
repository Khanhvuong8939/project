CREATE OR REPLACE PROCEDURE        FASB010106
/********************************************************************/
/*	関数名：FASB010106												*/
/*	機能名：口座振替入金処理	スケジュールマスタステータス更新処理*/
/*	作成者：宮下　克徳												*/
/*	作成日：2005/12/01												*/
/*	VER   ：1.00													*/
/*------------------------------------------------------------------*/
/*	引数：		無し												*/
/*	戻値：		無し												*/
/********************************************************************/
IS
	/* 変数 */
	nRet					NUMBER;										--リターンコード
	dShori_date				DATE;										--処理日付
	vShori_user				VARCHAR2(256);								--処理ﾕｰｻﾞ

	/* カーソル定義 */
	CURSOR cur_hurikae_rec
	IS
		SELECT		sche_kakin_nengetu,
					seikyu_gun,
					oem_cd
		FROM		wk_shuno_hurikae
		GROUP BY 	sche_kakin_nengetu,
					seikyu_gun,
					oem_cd;

BEGIN

	/*	処理日付取得	*/
	nRet := aoba_common.get_date(dShori_date);

	/*	処理ﾕｰｻﾞ取得	*/
	nRet := aoba_common.get_user(vShori_user);

	/* 収納情報口座振替ワークテーブル書込み */
	FOR rec_hurikae_rec IN cur_hurikae_rec LOOP
		/* スケジュールマスタ更新 */
		UPDATE	tb_schedule_mst
		SET		hurikae_status = DECODE(hurikae_status,
										aoba_syuno_const.CST_KOUHURI_MI_CD,aoba_syuno_const.CST_KOUHURI_ZUMI_CD,
										aoba_syuno_const.CST_KOUHURI_ZUMI_CD,aoba_syuno_const.CST_KOUHURI_REZUMI_CD,
										hurikae_status),
				update_date = dShori_date,
				update_user = vShori_user
		WHERE	kakin_nengetu = rec_hurikae_rec.sche_kakin_nengetu
		AND		seikyu_gun = rec_hurikae_rec.seikyu_gun
		AND		oem_cd = rec_hurikae_rec.oem_cd;

	END LOOP;

END FASB010106;
/
