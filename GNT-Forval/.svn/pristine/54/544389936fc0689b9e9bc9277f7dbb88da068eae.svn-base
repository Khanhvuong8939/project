CREATE OR REPLACE PROCEDURE FASB010101
/********************************************************************/
/*	関数名：FASB010101												*/
/*	機能名：口座振替入金処理	入金データ取込						*/
/*	作成者：宮下　克徳												*/
/*	作成日：2005/12/01												*/
/*	更新日：2006/03/31 TIS成毛 顧客情報をAOBACATのものを参照        */
/*        ：2006/07/05 木原    実入金日対応							*/
/*	VER   ：1.00													*/
/*------------------------------------------------------------------*/
/*	引数：		無し												*/
/*	戻値：		無し												*/
/********************************************************************/
IS
	/* 変数 */
	nRet					NUMBER;										--リターンコード
	cHikiotoshi_date		CHAR(8);									--引落日(YYYY付加後)
	cOem_cd					tb_oem_joho.oem_cd%TYPE;					--ＯＥＭコード
	cSeikyu_gun				wk_shuno_hurikae.seikyu_gun%TYPE;			--請求群
	cKakin_nengetu			tb_schedule_mst.kakin_nengetu%TYPE;			--スケジュールマスタ課金年月
	cSmbc_kakin_nengetu		CHAR(6);									--ＳＭＢＣデータ課金年月(YY付加後)
	cHurikae_date			tb_schedule_mst.hurikae_date1%TYPE;			--入金日(営業日 or リトライ営業日)
	cHurikae_status			tb_schedule_mst.hurikae_status%TYPE;		--口座振替結果ステータス
	cNormal_status			tb_schedule_mst.hurikae_status%TYPE;		--口座振替結果警告無しステータス
	cKoufuri_shori_cd		CHAR(1);									--口座振替結果処理ｺｰﾄﾞ(0:約定日,1;リトライ約定日)
/* 2006/07/05 TIS木原 実入金対応 */
	cJituHurikae_date	    CHAR(8);									--実入金日(営業日 or リトライ営業日)
/* end of 2006/07/05 TIS木原 実入金対応 */

	/* カーソル定義 */
	CURSOR cur_smbc_rec
	IS
		SELECT	a.itaku_cd AS itaku_cd,
				a.hikiotoshi_date AS hikiotoshi_date,
				b.kokyaku_cd AS kokyaku_cd,
				b.kakin_nengetu AS kakin_nengetu,
				b.seikyugaku AS seikyugaku,
				b.hurikae_kekka_cd AS hurikae_kekka_cd,
				b.data_kbn AS data_kbn,
				b.torihiki_bank_cd AS torihiki_bank_cd,
				b.torihiki_bank_name AS torihiki_bank_name,
				b.torihiki_shiten_cd AS torihiki_shiten_cd,
				b.torihiki_shiten_name AS torihiki_shiten_name,
				b.yokin_shumoku AS yokin_shumoku,
				b.kouza_bng AS kouza_bng,
				b.yokinsha_name AS yokinsha_name,
				b.shinki_cd AS shinki_cd,
				b.mfs_shiyoran AS mfs_shiyoran
		FROM	wk_smbc_h a,
				wk_smbc_d b;

BEGIN

	/* 収納情報口座振替ワークテーブル書込み */
	FOR rec_smbc_rec IN cur_smbc_rec LOOP
		/* 引落日に西暦を付加する */
		nRet := aoba_syuno_common.get_seireki(rec_smbc_rec.hikiotoshi_date,aoba_syuno_const.CST_SEIREKI_YYYY_FLG,cHikiotoshi_date);
		/* OEM基本情報検索 */
		SELECT	a.oem_cd,
				DECODE(
						rec_smbc_rec.itaku_cd,
						a.hurikae_itaku_cd15,
						AOBA_SYUNO_CONST.CST_SEIKYU_GUN_15,
						AOBA_SYUNO_CONST.CST_SEIKYU_GUN_25
						)
		INTO	cOem_cd,
				cSeikyu_gun
		FROM	tb_oem_joho a,
				tb_kokyaku_joho_cat b
--				tb_kokyaku_joho b
		WHERE	a.oem_cd = b.oem_cd
		AND		b.kokyaku_cd = rec_smbc_rec.kokyaku_cd
		AND		(
				a.hurikae_itaku_cd15 = rec_smbc_rec.itaku_cd OR
				a.hurikae_itaku_cd25 = rec_smbc_rec.itaku_cd
				);

		/* スケジュールマスタ検索 */
		BEGIN
			/* 約定日のデータを検索 */
			SELECT	kakin_nengetu,
					hurikae_date1,
					hurikae_status,
					aoba_syuno_const.CST_KOUHURI_MI_CD,
					aoba_syuno_const.CST_YAKUJYO_SHORI_CD,
/* 2006/07/05 TIS木原 実入金対応 */
					jitu_hurikae_date1
/* end of 2006/07/05 TIS木原 実入金対応 */
			INTO	cKakin_nengetu,
					cHurikae_date,
					cHurikae_status,
					cNormal_status,
					cKoufuri_shori_cd,
/* 2006/07/05 TIS木原 実入金対応 */
					cJituHurikae_date
/* end of 2006/07/05 TIS木原 実入金対応 */
			FROM	tb_schedule_mst
			WHERE	seikyu_gun = cSeikyu_gun
			AND		oem_cd = cOem_cd
			AND		hurikae_date2 = cHikiotoshi_date;
		EXCEPTION
			WHEN NO_DATA_FOUND THEN
				/* 約定日のデータが存在しない場合は、リトライ約定日のデータを検索する */
				SELECT	kakin_nengetu,
						hurikae_date_retry1,
						hurikae_status,
						AOBA_SYUNO_CONST.CST_KOUHURI_ZUMI_CD,
						AOBA_SYUNO_CONST.CST_REYAKUJYO_SHORI_CD,
/* 2006/07/05 TIS木原 実入金対応 */
						jitu_hurikae_date_retry1
/* end of 2006/07/05 TIS木原 実入金対応 */
				INTO	cKakin_nengetu,
						cHurikae_date,
						cHurikae_status,
						cNormal_status,
						cKoufuri_shori_cd,
/* 2006/07/05 TIS木原 実入金対応 */
						cJituHurikae_date
/* end of 2006/07/05 TIS木原 実入金対応 */
				FROM	tb_schedule_mst
				WHERE	seikyu_gun = cSeikyu_gun
				AND		oem_cd = cOem_cd
				AND		hurikae_date_retry2 = cHikiotoshi_date;
		END;

		/* 口座振替結果確認ログ出力判定 */
		IF cHurikae_status <> cNormal_status THEN
			nRet := aoba_syuno_common.koufuri_war_list(	rec_smbc_rec.itaku_cd,
														cSeikyu_gun,
														cOem_cd,
														cKoufuri_shori_cd,
														cHikiotoshi_date,
														cHurikae_status);
		END IF;

		/* 課金年月に西暦を付加する */
		nRet := aoba_syuno_common.get_seireki(rec_smbc_rec.kakin_nengetu,aoba_syuno_const.CST_SEIREKI_YY_FLG,cSmbc_kakin_nengetu);

		/* 収納情報口座振替ワーク */
		INSERT INTO wk_shuno_hurikae(
			kokyaku_cd,
			kakin_nengetu,
			seikyu_gun,
			hurikae_itaku_cd,
			oem_cd,
			nyukingaku,
			nyukin_date,
			hurikae_kekka_cd,
			data_kbn,
			torihiki_bank_cd,
			torihiki_bank_name,
			torihiki_shiten_cd,
			torihiki_shiten_name,
			yokin_shumoku,
			kouza_bng,
			yokinsha_name,
			shinki_cd,
			mfs_shiyoran,
			sche_kakin_nengetu,
/* 2006/07/05 TIS木原 実入金対応 */
			jitu_nyukin_date
/* end of 2006/07/05 TIS木原 実入金対応 */
			)
			VALUES
			(
			rec_smbc_rec.kokyaku_cd,
			cSmbc_kakin_nengetu,
			cSeikyu_gun,
			rec_smbc_rec.itaku_cd,
			cOem_cd,
			rec_smbc_rec.seikyugaku,
			cHurikae_date,
			rec_smbc_rec.hurikae_kekka_cd,
			rec_smbc_rec.data_kbn,
			rec_smbc_rec.torihiki_bank_cd,
			rec_smbc_rec.torihiki_bank_name,
			rec_smbc_rec.torihiki_shiten_cd,
			rec_smbc_rec.torihiki_shiten_name,
			rec_smbc_rec.yokin_shumoku,
			rec_smbc_rec.kouza_bng,
			rec_smbc_rec.yokinsha_name,
			rec_smbc_rec.shinki_cd,
			rec_smbc_rec.mfs_shiyoran,
			cKakin_nengetu,
/* 2006/07/05 TIS木原 実入金対応 */
			cJituHurikae_date
/* end of 2006/07/05 TIS木原 実入金対応 */
			);

	END LOOP;

END FASB010101;
/
