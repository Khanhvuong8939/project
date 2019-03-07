CREATE OR REPLACE PROCEDURE           FASB010102
/********************************************************************/
/*	関数名：FASB010102												*/
/*	機能名：口座振替入金処理	入金消込処理						*/
/*	作成者：宮下　克徳												*/
/*	作成日：2005/12/01												*/
/*	更新日：2006/02/16 TIS成毛 請求サマリに存在しないデータを除外	*/
/*	      ：2006/02/16 TIS成毛 請求サマリに存在しないデータをエラー	*/
/*	      ：                   に出力する処理を追加					*/
/*  更新日：2006/06/26 TIS成毛 領収書発行ﾌﾗｸﾞが"N"のレコードのみ、  */
/*        ：                   印紙税計算をするように変更           */
/*		  ：2006/07/05 木原　　実入金日対応							*/
/*		　：2006/07/25 木原	   預り金情報の実入金日対応				*/
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
	cMatome_flg				tb_seikyu_sum.matome_flg%TYPE;				--まとめフラグ
	cNyukin_flg				tb_seikyu_sum.nyukin_flg%TYPE;				--入金フラグ
	nSeikyugaku				tb_seikyu_sum.seikyugaku%TYPE;				--請求額
	nTax					tb_seikyu_sum.tax%TYPE;						--消費税
	cKakin_nengetu			tb_seikyu_sum.kakin_nengetu%TYPE;			--課金年月
	nInshizei				NUMBER;										--印紙税
	cNyukin_status_cd		tb_deposit_joho.nyukin_status_cd%TYPE;		--入金状態コード
	cNiju_status_cd			CHAR(1);									--二重入金ステータスコード
	cRyoshusho_flg			tb_seikyu_sum.ryoshusho_flg%TYPE;			--領収書フラグ -- 2006/06/26

	/* 定数 */
	CST_JOB_ID				CONSTANT CHAR(10) := 'FASB010102';			--ジョブＩＤ

	/* カーソル定義 */
	CURSOR cur_hurikae_rec
	IS
		SELECT	a.kokyaku_cd,
				a.kakin_nengetu,
				a.seikyu_gun,
				a.nyukin_date,
				a.nyukingaku,
				a.hurikae_kekka_cd,
/* 2006/07/05 木原 実入金対応 */
				a.jitu_nyukin_date
/* end of 2006/07/05 木原 実入金対応 */
		FROM	wk_shuno_hurikae a
		WHERE	a.hurikae_kekka_cd = aoba_syuno_const.CST_KOUHURI_MI_CD
		AND		EXISTS (
					SELECT (1)
					FROM   tb_seikyu_sum b
					WHERE  b.kokyaku_cd = a.kokyaku_cd
					AND    b.seikyu_nengetu = a.kakin_nengetu
					AND    b.seikyu_gun = a.seikyu_gun
				)
		ORDER BY
				a.kokyaku_cd,
				a.kakin_nengetu,
				a.seikyu_gun;

BEGIN

	/*	処理日付取得	*/
	nRet := aoba_common.get_date(dShori_date);

	/*	処理ﾕｰｻﾞ取得	*/
	nRet := aoba_common.get_user(vShori_user);

	/* 収納情報口座振替ワークテーブル書込み */
	FOR rec_hurikae_rec IN cur_hurikae_rec LOOP
		/* 請求サマリーデータ検索 */
		SELECT		matome_flg,
					nyukin_flg,
					SUM(seikyugaku),
					SUM(tax),
					TRIM(MAX(kakin_nengetu)),
					ryoshusho_flg      -- 2006/06/26
		INTO		cMatome_flg,
					cNyukin_flg,
					nSeikyugaku,
					nTax,
					cKakin_nengetu,
					cRyoshusho_flg     -- 2006/06/26
		FROM		tb_seikyu_sum
		WHERE		kokyaku_cd = rec_hurikae_rec.kokyaku_cd
		AND			seikyu_nengetu = rec_hurikae_rec.kakin_nengetu
		AND			seikyu_gun = rec_hurikae_rec.seikyu_gun
		GROUP BY	matome_flg,
					nyukin_flg,
					ryoshusho_flg;     -- 2006/06/26

		/* 入金処理 or 預かり金処理 */
		IF rec_hurikae_rec.nyukingaku = nSeikyugaku
		AND	cNyukin_flg <> aoba_syuno_const.CST_NYUKIN_ZUMI_FLG THEN
			/* 入金処理 */
			/* 印紙税計算関数 */
			--　2006/06/26 領収書フラグが'N'のレコードのみ印紙税計算対象とする
			nInshizei := 0;	--初期化
			IF cRyoshusho_flg = aoba_const.CST_N_FLG
			THEN
				nRet := aoba_syuno_common.get_inshizei((nSeikyugaku - nTax),nInshizei);
			END IF;
			--nRet := aoba_syuno_common.get_inshizei((nSeikyugaku - nTax),nInshizei);
			/* 請求サマリ更新 */
			UPDATE	tb_seikyu_sum
			SET		nyukingaku = seikyugaku,
					nyukin_date = rec_hurikae_rec.nyukin_date,
					urikake_zandaka = (urikake_zandaka - seikyugaku),
					nyukin_kbn_cd = aoba_syuno_const.CST_NYUKIN_KBN_KOUHURI,
					inshizei = DECODE(kakin_nengetu,cKakin_nengetu,nInshizei,inshizei),
					hurikae_kekka_cd = rec_hurikae_rec.hurikae_kekka_cd,
					nyukin_flg = aoba_syuno_const.CST_NYUKIN_ZUMI_FLG,
					update_date = dShori_date,
					update_user = vShori_user,
/* 2006/07/05 木原 実入金対応 */
					jitu_nyukin_date = rec_hurikae_rec.jitu_nyukin_date
/* end of 2006/07/05 木原 実入金対応 */
			WHERE	kokyaku_cd = rec_hurikae_rec.kokyaku_cd
			AND		seikyu_nengetu = rec_hurikae_rec.kakin_nengetu
			AND		seikyu_gun = rec_hurikae_rec.seikyu_gun;

			/* 入金情報登録 */
			INSERT INTO tb_nyukin_joho(
				kokyaku_cd,
				kakin_nengetu,
				seikyu_gun,
				nyukin_date,
				nyukin_status_cd,
				nyukin_kbn_cd,
				nyukingaku,
				insert_date,
				insert_user
				)
				SELECT	kokyaku_cd,
						kakin_nengetu,
						seikyu_gun,
						rec_hurikae_rec.nyukin_date,
						aoba_syuno_const.CST_NYUKIN_JYOTAI_KANSAI,
						aoba_syuno_const.CST_NYUKIN_KBN_KOUHURI,
						seikyugaku,
						dShori_date,
						vShori_user
				FROM	tb_seikyu_sum
				WHERE	kokyaku_cd = rec_hurikae_rec.kokyaku_cd
				AND		seikyu_nengetu = rec_hurikae_rec.kakin_nengetu
				AND		seikyu_gun = rec_hurikae_rec.seikyu_gun;

			/* サービス別請求サマリ更新 */
			UPDATE	tb_seikyu_sum_srv a
			SET		a.nyukingaku = a.seikyugaku,
					a.nyukin_date = rec_hurikae_rec.nyukin_date,
					a.urikake_zandaka = a.urikake_zandaka - a.seikyugaku,
					a.update_date = dShori_date,
					a.update_user = vShori_user
			WHERE	EXISTS(
							SELECT	1
							FROM	(
									SELECT	b1.kokyaku_cd,
											b1.kakin_nengetu,
											b1.seikyu_gun
									FROM	tb_seikyu_sum b1
									WHERE	b1.kokyaku_cd = rec_hurikae_rec.kokyaku_cd
									AND		b1.seikyu_nengetu = rec_hurikae_rec.kakin_nengetu
									AND		b1.seikyu_gun = rec_hurikae_rec.seikyu_gun
									) b
							WHERE	b.kokyaku_cd = a.kokyaku_cd
							AND		b.kakin_nengetu = a.kakin_nengetu
							AND		b.seikyu_gun = a.seikyu_gun
			);

			/* 督促情報更新 */
			UPDATE	tb_tokusoku_joho a
			SET		a.nyukin_flg = aoba_syuno_const.CST_NYUKIN_ZUMI_FLG,
					a.update_date = dShori_date,
					a.update_user = vShori_user
			WHERE	EXISTS(
							SELECT	1
							FROM	(
									SELECT	b1.kokyaku_cd,
											b1.kakin_nengetu,
											b1.seikyu_gun
									FROM	tb_seikyu_sum b1
									WHERE	b1.kokyaku_cd = rec_hurikae_rec.kokyaku_cd
									AND		b1.seikyu_nengetu = rec_hurikae_rec.kakin_nengetu
									AND		b1.seikyu_gun = rec_hurikae_rec.seikyu_gun
									) b
							WHERE	b.kokyaku_cd = a.kokyaku_cd
							AND		b.kakin_nengetu = a.kakin_nengetu
							AND		b.seikyu_gun = a.seikyu_gun
			);
			/* 二重入金リストステータス */
			cNiju_status_cd := aoba_syuno_const.CST_NIJU_NYUKIN_SEIJOU;
		ELSE
			/* 入金状態コード設定 */
			IF cNyukin_flg = aoba_syuno_const.CST_NYUKIN_ZUMI_FLG
			OR rec_hurikae_rec.nyukingaku > nSeikyugaku THEN
				/* 過剰コードを設定 */
				cNyukin_status_cd := aoba_syuno_const.CST_NYUKIN_KAJYO;
			ELSIF rec_hurikae_rec.nyukingaku < nSeikyugaku THEN
				/* 過少コードを設定 */
				cNyukin_status_cd := aoba_syuno_const.CST_NYUKIN_KASYO;
			END IF;

			/* 預かり金処理 */
			INSERT INTO tb_deposit_joho(
				deposit_kanri_bng,
				kokyaku_cd,
				kakin_nengetu,
				seikyu_gun,
				nyukin_date,
				nyukin_status_cd,
				deposit_kbn_cd,
				shoki_kingaku,
				deposit_zandaka,
				insert_date,
				insert_user,
/* 2006/07/25 木原 実入金対応 */
				jitu_nyukin_date
/* end of 2006/07/25 木原 実入金対応 */
				)
				VALUES
				(
				sq_deposit_joho.NEXTVAL,
				rec_hurikae_rec.kokyaku_cd,
				rec_hurikae_rec.kakin_nengetu,
				rec_hurikae_rec.seikyu_gun,
				rec_hurikae_rec.nyukin_date,
				cNyukin_status_cd,
				aoba_syuno_const.CST_NYUKIN_KBN_KOUHURI,
				rec_hurikae_rec.nyukingaku,
				rec_hurikae_rec.nyukingaku,
				dShori_date,
				vShori_user,
/* 2006/07/25 木原 実入金対応 */
 				rec_hurikae_rec.jitu_nyukin_date
/* end of 2006/07/25 木原 実入金対応 */
				);

			/* 預かり金登録ログ出力 */
			nRet := aoba_syuno_common.azukarikin_list(	rec_hurikae_rec.kokyaku_cd,
														rec_hurikae_rec.kakin_nengetu,
														rec_hurikae_rec.seikyu_gun,
														rec_hurikae_rec.nyukingaku,
														aoba_syuno_const.CST_NYUKIN_KBN_KOUHURI,
														rec_hurikae_rec.nyukin_date,
														cNyukin_status_cd,
														aoba_syuno_const.CST_KOUFURI_SHORI_CD );
			/* 二重入金リストステータス */
			cNiju_status_cd := aoba_syuno_const.CST_NIJU_NYUKIN_HUSEI;
		END IF;

		/* 二重入金リスト出力 */
		nRet := aoba_syuno_common.nijyu_nyukin_list(	CST_JOB_ID,
														cNiju_status_cd,
														rec_hurikae_rec.kokyaku_cd,
														rec_hurikae_rec.kakin_nengetu,
														rec_hurikae_rec.seikyu_gun);
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
	WHERE	a.hurikae_kekka_cd = aoba_syuno_const.CST_KOUHURI_MI_CD
	AND		NOT EXISTS (
				SELECT (1)
				FROM   tb_seikyu_sum b
				WHERE  b.kokyaku_cd = a.kokyaku_cd
				AND    b.seikyu_nengetu = a.kakin_nengetu
				AND    b.seikyu_gun = a.seikyu_gun
			);

END FASB010102;
/
