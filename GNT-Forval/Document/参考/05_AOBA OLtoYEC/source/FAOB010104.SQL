CREATE OR REPLACE PROCEDURE        FAOB010104
/********************************************************************/
/*	関数名：FAOB010104												*/
/*	機能名：請求書データ作成：請求書回線明細１，２編集				*/
/*	作成者：田中　昭啓												*/
/*	作成日：2005/11/07												*/
/*	更新日：2006/02/23  田中  利用期間対策							*/
/*	      ：2006/02/24  田中  総合計対応							*/
/*                            部門ソートキー対策					*/
/*        ：2006/03/09  田中  部門ソートキー対策					*/
/*        ：2006/04/10  田中  支払区分変換(12→02)のものは出力しない*/
/*        ：2006/04/21  田中  電話番号系のグループから、			*/
/*                            印字用IDを削除						*/
/*	VER   ：1.05													*/
/*------------------------------------------------------------------*/
/*	引数：		無し												*/
/*	戻値：		無し												*/
/********************************************************************/
IS

	/* ローカル定数 */
	CST_UTIWAKE_TYPE1		CONSTANT tb_srv_type_mst.utiwake_type%TYPE := '11';				--請求書内訳種別(割引有り)
	CST_UTIWAKE_TYPE2		CONSTANT tb_srv_type_mst.utiwake_type%TYPE := '12';				--請求書内訳種別(割引無し)
	CST_DATA_TYPE1			CONSTANT wk_bl_seikyu_sum.data_type%TYPE := '01';				--データ種別(当月分請求書)
	CST_REC_TYPE			CONSTANT tb_seikyu_utiwake.rec_type_cd%TYPE := '10';		--レコード種別コード(請求対象)
	CST_PTN_SK1				CONSTANT wk_bl_kaisen_meisai.ptn_sk%TYPE := '01';				--パターンソートキー(ヘッダー１)
	CST_PTN_SK2				CONSTANT wk_bl_kaisen_meisai.ptn_sk%TYPE := '02';				--パターンソートキー(ヘッダー２)
	CST_PTN_SK5				CONSTANT wk_bl_kaisen_meisai.ptn_sk%TYPE := '05';				--パターンソートキー(データ)
	CST_PTN_SK9				CONSTANT wk_bl_kaisen_meisai.ptn_sk%TYPE := '09';				--パターンソートキー(エンド)
	CST_DATA_KBN_01			CONSTANT CHAR(1) := '1';										--データ区分(明細)
	CST_DATA_KBN_06			CONSTANT CHAR(1) := '6';										--データ区分(ID計)
	CST_DATA_KBN_07			CONSTANT CHAR(1) := '7';										--データ区分(顧客部署コード計)
	CST_DATA_KBN_08			CONSTANT CHAR(1) := '8';										--データ区分(課金年月計)
	CST_DATA_KBN_09			CONSTANT CHAR(1) := '9';										--データ区分(総合計)
	CST_DATA_KBN_SK00		CONSTANT wk_bl_kaisen_meisai.data_kbn_sk%TYPE := '00';			--データ区分ソートキー(ヘッダー)
	CST_DATA_KBN_SK01		CONSTANT wk_bl_kaisen_meisai.data_kbn_sk%TYPE := '01';			--データ区分ソートキー(明細)
	CST_DATA_KBN_SK06		CONSTANT wk_bl_kaisen_meisai.data_kbn_sk%TYPE := '06';			--データ区分ソートキー(ID計)
	CST_DATA_KBN_SK07		CONSTANT wk_bl_kaisen_meisai.data_kbn_sk%TYPE := '07';			--データ区分ソートキー(顧客部署コード計)
	CST_DATA_KBN_SK08		CONSTANT wk_bl_kaisen_meisai.data_kbn_sk%TYPE := '08';			--データ区分ソートキー(課金年月計)
	CST_DATA_KBN_SK09		CONSTANT wk_bl_kaisen_meisai.data_kbn_sk%TYPE := '09';			--データ区分ソートキー(総合計)
	CST_DATA_KBN_SKZZ		CONSTANT wk_bl_kaisen_meisai.data_kbn_sk%TYPE := 'ZZ';			--データ区分ソートキー(エンド)
	CST_MEISAI_PTN1			CONSTANT CHAR(1) := '1';										--明細データパターン１
	CST_MEISAI_PTN2			CONSTANT CHAR(1) := '2';										--明細データパターン２
	CST_MEISAI_PTN5			CONSTANT CHAR(1) := '5';										--明細データパターン５
	CST_MEISAI_PTN9			CONSTANT CHAR(1) := '9';										--エンドデータパターン
	CST_DEL_TABLE			CONSTANT VARCHAR2(20) := 'WK_BL_KAISEN_MEISAI';					--削除対象テーブル名
	/* ローカル変数 */
	nRet					NUMBER;															--リターンコード
	dDate					wk_bl_kaisen_meisai.insert_date%TYPE;							--システム日付
	vUser					wk_bl_kaisen_meisai.insert_user%TYPE;							--バッチユーザ
	nCnt1					NUMBER;															--データ件数カウント用
	cUtiwake				tb_srv_type_mst.utiwake_type%TYPE;								--請求書内訳種別
	nSeqno					NUMBER;															--連番取得用

	/* 2006/01/04 パフォーマンス対応 */
	/* カーソル定義 */
	CURSOR cur_kaisen_meisai(in_utiwake_type wk_bl_kaisen_meisai.utiwake_type%TYPE)
	IS
		/* 20060206 ソート順変更 */
		SELECT	utiwake_type,
				SUBSTRB(kaisen_meisai_data,1,14) meisai_a,
		 		SUBSTRB(kaisen_meisai_data,24) meisai_b,
				kokyaku_cd_sk,
				ptn_sk,
				kakin_nengetu_sk,
				id_sk,
				data_kbn_sk,
				kokyaku_busho_cd_sk,
--				id_sk,
				print_order,
				ryokin_cd1
		FROM	wk_bl_kaisen_meisai
		WHERE	utiwake_type = in_utiwake_type
		/* 20060204 ソート変更 */
		/* 20060215 部門計ソート対応 */
		ORDER BY	kokyaku_cd_sk,
					kakin_nengetu_sk,
					ptn_sk,
					/* 20060224 部署ソート不要 */
					kokyaku_busho_cd_sk,
					id_sk,
					data_kbn_sk,
					--kokyaku_cd_sk,
					--id_sk,
					ryokin_cd1
		FOR UPDATE;

	rec_kaisen_meisai	cur_kaisen_meisai%ROWTYPE;

BEGIN

	/* 請求書回線明細一時TBL削除 */
	EXECUTE IMMEDIATE 'TRUNCATE TABLE ' || CST_DEL_TABLE;

	/* システム日付取得 */
	nRet := aoba_common.get_date(dDate);

	/* バッチユーザ取得 */
	nRet := aoba_common.get_user(vUser);

	/* ループ開始 */
	FOR i IN 0..1 LOOP
		/* i = 0 割引有り */
		/* i = 1 割引無し */

		/* 割引有無選定 */
		IF i = 0 THEN
			cUtiwake := CST_UTIWAKE_TYPE1;
		ELSIF i = 1 THEN
			cUtiwake := CST_UTIWAKE_TYPE2;
		END IF;

		/* 件数カウント */
		SELECT	COUNT(*)
		INTO	nCnt1
		FROM	wk_bl_seikyu_sum a,
				tb_seikyu_utiwake b,
				tb_srv_type_mst c,
				wk_bl_kokyaku_info d
		WHERE	b.srv_type_cd = c.srv_type_cd
		AND		c.utiwake_type = cUtiwake
		AND		a.seikyu_nengetu = d.seikyu_nengetu		-- 20060410
		AND		a.kokyaku_cd = d.kokyaku_cd				-- 20060410
		AND		d.modify_flg = 'N'						-- 20060410
		AND		a.data_type = CST_DATA_TYPE1
		AND		a.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_11
		AND		a.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_13
		AND		a.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_14
		AND		a.kakin_nengetu = b.kakin_nengetu
		AND		a.seikyu_gun = b.seikyu_gun
		AND		a.kokyaku_cd = b.kokyaku_cd
		AND		b.rec_type_cd = CST_REC_TYPE;

		IF nCnt1 > 0 THEN
			/* ヘッダーレコード１作成 */
			INSERT INTO wk_bl_kaisen_meisai(
						utiwake_type,				--請求書内訳種別
						ptn_sk,						--パターンソートキー
						data_kbn_sk,					--データ区分ソートキー
						kokyaku_cd_sk,				--顧客コードソートキー
						kakin_nengetu_sk,			--課金年月ソートキー
						kokyaku_busho_cd_sk,		--顧客部署コードソートキー
						id_sk,						--IDソートキー
						print_order,					--請求書印字順ソートキー
						ryokin_cd1,					--料金項目コード１ソートキー
						sk_yobi,						--ソートキー予備
						kaisen_meisai_data,			--請求書回線明細データ
						insert_date,					--登録日時
						insert_user)					--登録ユーザ
			SELECT		cUtiwake,
						CST_PTN_SK1,
						CST_DATA_KBN_SK00,
						a.kokyaku_cd,
						a.kakin_nengetu,
						'0000',
						'00000000000000000000',
						aoba_const.CST_NUM_ZERO,
						'0000000000',
						'00',
						a.kokyaku_cd || a.kakin_nengetu ||
						'000000000' || CST_MEISAI_PTN1 || a.oem_cd ||
						'ワンビリングサービス／請求内訳　　　　　' ||
			 			'　　　　　　　　　　　　　　　　　　　　' ||
			 			'　　　　　　　　　　　　　　　　　　　　' ||
			 			RPAD(' ',108),
			 			dDate,
			 			vUser
			 			--NULL,
			 			--NULL
			FROM		(
						SELECT	DISTINCT	b1.kokyaku_cd,
											b1.kakin_nengetu,
											b1.oem_cd
						FROM	wk_bl_seikyu_sum a1,
								tb_seikyu_utiwake b1,
								tb_srv_type_mst c1,
								wk_bl_kokyaku_info d1
						WHERE	b1.srv_type_cd = c1.srv_type_cd
						AND		c1.utiwake_type = cUtiwake
						AND		a1.data_type = CST_DATA_TYPE1
						AND		a1.seikyu_nengetu = d1.seikyu_nengetu		-- 20060410
						AND		a1.kokyaku_cd = d1.kokyaku_cd				-- 20060410
						AND		d1.modify_flg = 'N'							-- 20060410
						AND		a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_11
						AND		a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_13
						AND		a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_14
						AND		a1.kakin_nengetu = b1.kakin_nengetu
						AND		a1.seikyu_gun = b1.seikyu_gun
						AND		a1.kokyaku_cd = b1.kokyaku_cd
						AND		b1.rec_type_cd = CST_REC_TYPE
						) a;

			/* ヘッダーレコード２作成 */
			INSERT INTO wk_bl_kaisen_meisai(
						utiwake_type,				--請求書内訳種別
						ptn_sk,						--パターンソートキー
						data_kbn_sk,				--データ区分ソートキー
						kokyaku_cd_sk,				--顧客コードソートキー
						kakin_nengetu_sk,			--課金年月ソートキー
						kokyaku_busho_cd_sk,		--顧客部署コードソートキー
						id_sk,						--IDソートキー
						print_order,				--請求書印字順ソートキー
						ryokin_cd1,					--料金項目コード１ソートキー
						sk_yobi,					--ソートキー予備
						kaisen_meisai_data,			--請求書回線明細データ
						insert_date,				--登録日時
						insert_user)				--登録ユーザ
			SELECT		cUtiwake,
						CST_PTN_SK2,
						CST_DATA_KBN_SK00,
						a.kokyaku_cd,
						a.kakin_nengetu,
						'0000',
						'00000000000000000000',
						aoba_const.CST_NUM_ZERO,
						'0000000000',
						'00',
						DECODE(i,0,
								a.kokyaku_cd ||
								a.kakin_nengetu ||
								'000000000' ||
								CST_MEISAI_PTN2 ||
								a.oem_cd ||
								'キャリア価格　　　　' ||
								' ' ||
								'割引額　　　　　　　' ||
								' ' ||
								'小計　　　　　　　　' ||
								' ' ||
								'消費税　　　　　　　' ||
								' ' ||
								'御請求額　　　　　　' ||
								' ' ||
								'税区' ||
								'料金項目名　　　　　' ||
								RPAD(' ',99),
								1,
								a.kokyaku_cd ||
								a.kakin_nengetu ||
								'000000000' ||
								CST_MEISAI_PTN2 ||
								a.oem_cd ||
								'金額　　　　　　　　' ||
								' ' ||
								'消費税　　　　　　　' ||
								' ' ||
								'御請求額　　　　　　' ||
								' ' ||
								'税区' ||
								'料金項目名　　　　　' ||
								RPAD(' ',141)
								),
						dDate,
						vUser
						--NULL
						--NULL
			FROM		(
						SELECT	DISTINCT	b1.kokyaku_cd,
											b1.kakin_nengetu,
											b1.oem_cd
						FROM	wk_bl_seikyu_sum a1,
								tb_seikyu_utiwake b1,
								tb_srv_type_mst c1,
								wk_bl_kokyaku_info d1
						WHERE	b1.srv_type_cd = c1.srv_type_cd
						AND		c1.utiwake_type = cUtiwake
						AND		a1.data_type = CST_DATA_TYPE1
						AND		a1.seikyu_nengetu = d1.seikyu_nengetu		-- 20060410
						AND		a1.kokyaku_cd = d1.kokyaku_cd				-- 20060410
						AND		d1.modify_flg = 'N'							-- 20060410
						AND		a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_11
						AND		a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_13
						AND		a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_14
						AND		a1.kakin_nengetu = b1.kakin_nengetu
						AND		a1.seikyu_gun = b1.seikyu_gun
						AND		a1.kokyaku_cd = b1.kokyaku_cd
						AND		b1.rec_type_cd = CST_REC_TYPE
						) a;

			/* データレコード作成 */
			INSERT INTO wk_bl_kaisen_meisai(
						utiwake_type,				--請求書内訳種別
						ptn_sk,						--パターンソートキー
						data_kbn_sk,					--データ区分ソートキー
						kokyaku_cd_sk,				--顧客コードソートキー
						kakin_nengetu_sk,			--課金年月ソートキー
						kokyaku_busho_cd_sk,		--顧客部署コードソートキー
						id_sk,						--IDソートキー
						print_order,					--請求書印字順ソートキー
						ryokin_cd1,					--料金項目コード１ソートキー
						sk_yobi,						--ソートキー予備
						kaisen_meisai_data,			--請求書回線明細データ
						insert_date,					--登録日時
						insert_user)					--登録ユーザ
			/* 明細データ */
			SELECT	cUtiwake,
						CST_PTN_SK5,
						CST_DATA_KBN_SK01,
						b.kokyaku_cd,
						b.kakin_nengetu,
--						b.bumon_cd,
						NVL(b.bumon_cd,'    '),		--20060309
						b.id,
						NVL(b.print_order,aoba_const.CST_NUM_ZERO),
						/* 20060210 サービスタイプのソート追加 */
--						b.ryokin_cd1,
						b.srv_type_cd || b.ryokin_cd1,
						'00',
						DECODE(i,0,
								b.kokyaku_cd ||																					--顧客コード
								b.kakin_nengetu ||																				--課金年月
								'000000000' ||																					--連番
								CST_MEISAI_PTN5 ||																				--パターン
								b.oem_cd ||																						--グループ
								CST_DATA_KBN_01 ||																				--データ区分
								RPAD(b.print_id,20,' ') ||																		--電話番号
--								RPAD('（利用期間：' ||
								RPAD('（課金年月：' ||		--20060223
--								aoba_out_common.conv_out_mbc(SUBSTR(riyo_start_date,5,2) || '/' ||
--									SUBSTR(riyo_start_date,7,2) || '～' || SUBSTR(riyo_end_date,5,2) || '/' ||
--									SUBSTR(riyo_end_date,7,2) || '）'),80,'　') ||												--自由使用欄
								aoba_out_common.conv_out_mbc(SUBSTRB(b.kakin_nengetu,1,4) || '年' ||
								SUBSTRB(b.kakin_nengetu,5,2) || '月分）'),80,'　') ||											--自由使用欄
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,b.teika) ||							--金額１サイン
								LPAD(ABS(b.teika),9,'0') ||																		--金額１
								' ' ||																							--フラグ１
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,b.waribikigaku) ||					--金額２サイン
								LPAD(ABS(b.waribikigaku),9,'0') ||																--金額２
								' ' ||																							--フラグ２
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,b.ryokin) ||							--金額３サイン
								LPAD(ABS(b.ryokin),9,'0') ||																	--金額３
								' ' ||																							--フラグ３
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,b.tax) ||							--金額４サイン
								LPAD(ABS(b.tax),9,'0') ||																		--金額４
								' ' ||																							--フラグ４
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,b.zeikomi) ||						--金額５サイン
								LPAD(ABS(b.zeikomi),9,'0') ||																	--金額５
								' ' ||																							--フラグ５
								b.tax_flg || ' ' ||																				--区分
								RPAD(aoba_out_common.conv_out_mbc(b.ryokin_name),60,'　') ||									--項目名
								RPAD(' ',10),																					--予備
								1,
								b.kokyaku_cd ||																					--顧客コード
								b.kakin_nengetu ||																				--課金年月
								'000000000' ||																					--連番
								CST_MEISAI_PTN5 ||																				--パターン
								b.oem_cd ||																						--グループ
								CST_DATA_KBN_01 ||																				--データ区分
								RPAD(b.print_id,20,' ') ||																		--電話番号
--								RPAD('（利用期間：' || aoba_out_common.conv_out_mbc(SUBSTR(riyo_start_date,5,2) || '/' ||
--									SUBSTR(riyo_start_date,7,2) || '～' || SUBSTR(riyo_end_date,5,2) || '/' ||
--									SUBSTR(riyo_end_date,7,2) || '）'),80,'　') ||												--自由使用欄
								RPAD('（課金年月：' ||
								aoba_out_common.conv_out_mbc(SUBSTRB(b.kakin_nengetu,1,4) || '年' ||
								SUBSTRB(b.kakin_nengetu,5,2) || '月分）'),80,'　') ||											--自由使用欄
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,b.ryokin) ||							--金額１サイン
								LPAD(ABS(b.ryokin),9,'0') ||																	--金額１
								' ' ||																							--フラグ１
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,b.tax) ||							--金額２サイン
								LPAD(ABS(b.tax),9,'0') ||																		--金額２
								' ' ||																							--フラグ２
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,b.zeikomi) ||						--金額３サイン
								LPAD(ABS(b.zeikomi),9,'0') ||																	--金額３
								' ' ||																							--フラグ３
								b.tax_flg || ' ' ||																				--区分
								RPAD(aoba_out_common.conv_out_mbc(b.ryokin_name),80,'　') ||									--項目名
								RPAD(' ',12)																					--予備
								),
						dDate,
						vUser
						--NULL
						--NULL
			FROM	wk_bl_seikyu_sum a,
					tb_seikyu_utiwake b,
					tb_srv_type_mst c,
					wk_bl_kokyaku_info d
			WHERE	b.srv_type_cd = c.srv_type_cd
			AND		c.utiwake_type = cUtiwake
			AND		a.data_type = CST_DATA_TYPE1
			AND		a.seikyu_nengetu = d.seikyu_nengetu		-- 20060410
			AND		a.kokyaku_cd = d.kokyaku_cd				-- 20060410
			AND		d.modify_flg = 'N'						-- 20060410
			AND		a.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_11
			AND		a.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_13
			AND		a.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_14
			AND		a.kakin_nengetu = b.kakin_nengetu
			AND		a.seikyu_gun = b.seikyu_gun
			AND		a.kokyaku_cd = b.kokyaku_cd
			AND		b.rec_type_cd = CST_REC_TYPE;
			/* 2006/01/05 UNION解除 */
			--UNION

			/* 小計１(ID計) */
			INSERT INTO wk_bl_kaisen_meisai(
						utiwake_type,				--請求書内訳種別
						ptn_sk,						--パターンソートキー
						data_kbn_sk,				--データ区分ソートキー
						kokyaku_cd_sk,				--顧客コードソートキー
						kakin_nengetu_sk,			--課金年月ソートキー
						kokyaku_busho_cd_sk,		--顧客部署コードソートキー
						id_sk,						--IDソートキー
						print_order,				--請求書印字順ソートキー
						ryokin_cd1,					--料金項目コード１ソートキー
						sk_yobi,					--ソートキー予備
						kaisen_meisai_data,			--請求書回線明細データ
						insert_date,				--登録日時
						insert_user)				--登録ユーザ
			SELECT		cUtiwake,
						CST_PTN_SK5,
						CST_DATA_KBN_SK06,
						a.kokyaku_cd,
						a.kakin_nengetu,
--						a.bumon_cd,
						NVL(a.bumon_cd,'    '),		--20060309
						a.id,
						999,
						'ZZZZZZZZZZ',
						'00',
						DECODE(i,0,
								a.kokyaku_cd ||																					--顧客コード
								a.kakin_nengetu ||																				--課金年月
								'000000000' ||																					--連番
								CST_MEISAI_PTN5 ||																				--パターン
								a.oem_cd ||																						--グループ
								CST_DATA_KBN_06 ||																				--データ区分
								RPAD(a.print_id,20,' ') ||																					--電話番号
								RPAD('　',80,'　') ||																			--自由使用欄
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,a.teika) ||							--金額１サイン
								LPAD(ABS(a.teika),9,'0') ||																		--金額１
								' ' ||																							--フラグ１
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,a.waribikigaku) ||					--金額２サイン
								LPAD(ABS(a.waribikigaku),9,'0') ||																--金額２
								' ' ||																							--フラグ２
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,a.ryokin) ||							--金額３サイン
								LPAD(ABS(a.ryokin),9,'0') ||																	--金額３
								' ' ||																							--フラグ３
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,a.tax) ||							--金額４サイン
								LPAD(ABS(a.tax),9,'0') ||																		--金額４
								' ' ||																							--フラグ４
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,a.zeikomi) ||						--金額５サイン
								LPAD(ABS(a.zeikomi),9,'0') ||																	--金額５
								' ' ||																							--フラグ５
								'  ' ||																							--区分
								/* 20060206 項目名にそれぞれの集計の名称を固定値で設定するよう変更 */
--								RPAD('　',60,'　') ||																			--項目名
								RPAD('電話番号計',60,'　') ||																	--項目名
								RPAD(' ',10),																					--予備
								1,
								a.kokyaku_cd ||																					--顧客コード
								a.kakin_nengetu ||																				--課金年月
								'000000000' ||																					--連番
								CST_MEISAI_PTN5 ||																				--パターン
								a.oem_cd ||																						--グループ
								CST_DATA_KBN_06 ||																				--データ区分
								RPAD(a.print_id,20,' ') ||																					--電話番号
								RPAD('　',80,'　') ||																			--自由使用欄
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,a.ryokin) ||							--金額１サイン
								LPAD(ABS(a.ryokin),9,'0') ||																	--金額１
								' ' ||																							--フラグ１
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,a.tax) ||							--金額２サイン
								LPAD(ABS(a.tax),9,'0') ||																		--金額２
								' ' ||																							--フラグ２
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,a.zeikomi) ||						--金額３サイン
								LPAD(ABS(a.zeikomi),9,'0') ||																	--金額３
								' ' ||																							--フラグ３
								'  ' ||																							--区分
								/* 20060206 項目名にそれぞれの集計の名称を固定値で設定するよう変更 */
--								RPAD('　',80,'　') ||																			--項目名
								RPAD('電話番号計',80,'　') ||																	--項目名
								RPAD(' ',12)																					--予備
								),
						dDate,
						vUser
						--NULL
						--NULL
			FROM		(
						SELECT		b1.kokyaku_cd AS kokyaku_cd,
									b1.kakin_nengetu AS kakin_nengetu,
									b1.oem_cd AS oem_cd,
									b1.bumon_cd AS bumon_cd,
									b1.id AS id,
									MIN(b1.print_id) AS print_id,		-- 20060421 成毛
									SUM(b1.teika) AS teika,
									SUM(b1.waribikigaku) AS waribikigaku,
									SUM(b1.ryokin) AS ryokin,
									SUM(b1.tax) AS tax,
									SUM(b1.zeikomi) AS zeikomi
						FROM		wk_bl_seikyu_sum a1,
						            tb_seikyu_utiwake b1,
						            tb_srv_type_mst c1,
						            wk_bl_kokyaku_info d1
						WHERE		b1.srv_type_cd = c1.srv_type_cd
						AND			c1.utiwake_type = cUtiwake
						AND			a1.data_type = CST_DATA_TYPE1
						AND			a1.seikyu_nengetu = d1.seikyu_nengetu		-- 20060410
						AND			a1.kokyaku_cd = d1.kokyaku_cd				-- 20060410
						AND			d1.modify_flg = 'N'							-- 20060410
						AND			a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_11
						AND			a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_13
						AND			a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_14
						AND			a1.kakin_nengetu = b1.kakin_nengetu
						AND			a1.seikyu_gun = b1.seikyu_gun
						AND			a1.kokyaku_cd = b1.kokyaku_cd
						AND			b1.rec_type_cd = CST_REC_TYPE
						GROUP BY	b1.kokyaku_cd,
									b1.kakin_nengetu,
									b1.oem_cd,
									b1.bumon_cd,
									b1.id
--									b1.print_id				-- 20060421
						) a;
			--UNION

			/* 小計２(部門単位) */
			INSERT INTO wk_bl_kaisen_meisai(
						utiwake_type,				--請求書内訳種別
						ptn_sk,						--パターンソートキー
						data_kbn_sk,				--データ区分ソートキー
						kokyaku_cd_sk,				--顧客コードソートキー
						kakin_nengetu_sk,			--課金年月ソートキー
						kokyaku_busho_cd_sk,		--顧客部署コードソートキー
						id_sk,						--IDソートキー
						print_order,				--請求書印字順ソートキー
						ryokin_cd1,					--料金項目コード１ソートキー
						sk_yobi,					--ソートキー予備
						kaisen_meisai_data,			--請求書回線明細データ
						insert_date,				--登録日時
						insert_user)				--登録ユーザ
			SELECT		cUtiwake,
						CST_PTN_SK5,
						CST_DATA_KBN_SK07,
						a.kokyaku_cd,
						a.kakin_nengetu,
--						a.bumon_cd,
						NVL(a.bumon_cd,'    '),		--20060309
						'ZZZZZZZZZZZZZZZZZZZZ',
						999,
						'ZZZZZZZZZZ',
						'00',
						DECODE(i,0,
								a.kokyaku_cd ||																					--顧客コード
								a.kakin_nengetu ||																				--課金年月
								'000000000' ||																					--連番
								CST_MEISAI_PTN5 ||																				--パターン
								a.oem_cd ||																						--グループ
								CST_DATA_KBN_07 ||																				--データ区分
								RPAD(' ',20) ||																					--電話番号
								RPAD('　',80,'　') ||																			--自由使用欄
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,a.teika) ||							--金額１サイン
								LPAD(ABS(a.teika),9,'0') ||																		--金額１
								' ' ||																							--フラグ１
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,a.waribikigaku) ||					--金額２サイン
								LPAD(ABS(a.waribikigaku),9,'0') ||																--金額２
								' ' ||																							--フラグ２
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,a.ryokin) ||							--金額３サイン
								LPAD(ABS(a.ryokin),9,'0') ||																	--金額３
								' ' ||																							--フラグ３
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,a.tax) ||							--金額４サイン
								LPAD(ABS(a.tax),9,'0') ||																		--金額４
								' ' ||																							--フラグ４
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,a.zeikomi) ||						--金額５サイン
								LPAD(ABS(a.zeikomi),9,'0') ||																	--金額５
								' ' ||																							--フラグ５
								'  ' ||																							--区分
								/* 20060206 項目名にそれぞれの集計の名称固定値と部門コードを連結した値を設定 */
--								RPAD('　',60,'　') ||																			--項目名
								RPAD('部門計（' || nvl(aoba_out_common.conv_out_mbc(a.bumon_cd),'　　　　') || '）',60,'　') ||	--項目名
								RPAD(' ',10),																					--予備
								1,
								a.kokyaku_cd ||																					--顧客コード
								a.kakin_nengetu ||																				--課金年月
								'000000000' ||																					--連番
								CST_MEISAI_PTN5 ||																				--パターン
								a.oem_cd ||																						--グループ
								CST_DATA_KBN_07 ||																				--データ区分
								RPAD(' ',20) ||																					--電話番号
								RPAD('　',80,'　') ||																			--自由使用欄
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,a.ryokin) ||							--金額１サイン
								LPAD(ABS(a.ryokin),9,'0') ||																	--金額１
								' ' ||																							--フラグ１
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,a.tax) ||							--金額２サイン
								LPAD(ABS(a.tax),9,'0') ||																		--金額２
								' ' ||																							--フラグ２
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,a.zeikomi) ||						--金額３サイン
								LPAD(ABS(a.zeikomi),9,'0') ||																	--金額３
								' ' ||																							--フラグ３
								'  ' ||																							--区分
								/* 20060206 項目名にそれぞれの集計の名称固定値と部門コードを連結した値を設定 */
--								RPAD('　',80,'　') ||
								RPAD('部門計（' || nvl(aoba_out_common.conv_out_mbc(a.bumon_cd),'　　　　') || '）',80,'　') ||	--項目名
								RPAD(' ',12)																					--予備
								),
						dDate,
						vUser
						--NULL
						--NULL
			FROM		(
						SELECT		b1.kokyaku_cd AS kokyaku_cd,
									b1.kakin_nengetu AS kakin_nengetu,
									b1.bumon_cd AS bumon_cd,
									b1.oem_cd AS oem_cd,
									SUM(b1.teika) AS teika,
									SUM(b1.waribikigaku) AS waribikigaku,
									SUM(b1.ryokin) AS ryokin,
									SUM(b1.tax) AS tax,
									SUM(b1.zeikomi) AS zeikomi
						FROM		wk_bl_seikyu_sum a1,
						            tb_seikyu_utiwake b1,
						            tb_srv_type_mst c1,
						            wk_bl_kokyaku_info d1
						WHERE	b1.srv_type_cd = c1.srv_type_cd
						AND		c1.utiwake_type = cUtiwake
						AND		a1.data_type = CST_DATA_TYPE1
						AND		a1.seikyu_nengetu = d1.seikyu_nengetu		-- 20060410
						AND		a1.kokyaku_cd = d1.kokyaku_cd				-- 20060410
						AND		d1.modify_flg = 'N'							-- 20060410
						AND		a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_11
						AND		a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_13
						AND		a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_14
						AND		a1.kakin_nengetu = b1.kakin_nengetu
						AND		a1.seikyu_gun = b1.seikyu_gun
						AND		a1.kokyaku_cd = b1.kokyaku_cd
						AND		b1.rec_type_cd = CST_REC_TYPE
						GROUP BY	b1.kokyaku_cd,
									b1.kakin_nengetu,
									b1.bumon_cd,
									b1.oem_cd
						) a;
--			UNION

			/* 小計３(課金年月単位) */
			INSERT INTO wk_bl_kaisen_meisai(
						utiwake_type,				--請求書内訳種別
						ptn_sk,						--パターンソートキー
						data_kbn_sk,					--データ区分ソートキー
						kokyaku_cd_sk,				--顧客コードソートキー
						kakin_nengetu_sk,			--課金年月ソートキー
						kokyaku_busho_cd_sk,		--顧客部署コードソートキー
						id_sk,						--IDソートキー
						print_order,					--請求書印字順ソートキー
						ryokin_cd1,					--料金項目コード１ソートキー
						sk_yobi,						--ソートキー予備
						kaisen_meisai_data,			--請求書回線明細データ
						insert_date,					--登録日時
						insert_user)					--登録ユーザ
			SELECT		cUtiwake,
						CST_PTN_SK5,
						CST_DATA_KBN_SK08,
						a.kokyaku_cd,
						a.kakin_nengetu,
						'ZZZZ',
						'ZZZZZZZZZZZZZZZZZZZZ',
						999,
						'ZZZZZZZZZZ',
						'00',
						DECODE(i,0,
								a.kokyaku_cd ||																					--顧客コード
								a.kakin_nengetu ||																				--課金年月
								'000000000' ||																					--連番
								CST_MEISAI_PTN5 ||																				--パターン
								a.oem_cd ||																						--グループ
								CST_DATA_KBN_08 ||																				--データ区分
								RPAD(' ',20) ||																					--電話番号
								RPAD('　',80,'　') ||																			--自由使用欄
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,a.teika) ||							--金額１サイン
								LPAD(ABS(a.teika),9,'0') ||																		--金額１
								' ' ||																							--フラグ１
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,a.waribikigaku) ||					--金額２サイン
								LPAD(ABS(a.waribikigaku),9,'0') ||																--金額２
								' ' ||																							--フラグ２
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,a.ryokin) ||							--金額３サイン
								LPAD(ABS(a.ryokin),9,'0') ||																	--金額３
								' ' ||																							--フラグ３
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,a.tax) ||							--金額４サイン
								LPAD(ABS(a.tax),9,'0') ||																		--金額４
								' ' ||																							--フラグ４
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,a.zeikomi) ||						--金額５サイン
								LPAD(ABS(a.zeikomi),9,'0') ||																	--金額５
								' ' ||																							--フラグ５
								'  ' ||																							--区分
								/* 20060206 項目名にそれぞれの集計の名称を固定値で設定するよう変更 */
--								RPAD('　',60,'　') ||																			--項目名
								RPAD('課金年月計',60,'　') ||																	--項目名
								RPAD(' ',10),																					--予備
								1,
								a.kokyaku_cd ||																					--顧客コード
								a.kakin_nengetu ||																				--課金年月
								'000000000' ||																					--連番
								CST_MEISAI_PTN5 ||																				--パターン
								a.oem_cd ||																						--グループ
								CST_DATA_KBN_08 ||																				--データ区分
								RPAD(' ',20) ||																					--電話番号
								RPAD('　',80,'　') ||																			--自由使用欄
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,a.ryokin) ||							--金額１サイン
								LPAD(ABS(a.ryokin),9,'0') ||																	--金額１
								' ' ||																							--フラグ１
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,a.tax) ||							--金額２サイン
								LPAD(ABS(a.tax),9,'0') ||																		--金額２
								' ' ||																							--フラグ２
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,a.zeikomi) ||						--金額３サイン
								LPAD(ABS(a.zeikomi),9,'0') ||																	--金額３
								' ' ||																							--フラグ３
								'  ' ||																							--区分
								/* 20060206 項目名にそれぞれの集計の名称を固定値で設定するよう変更 */
--								RPAD('　',80,'　') ||																			--項目名
								RPAD('課金年月計',80,'　') ||																	--項目名
								RPAD(' ',12)																					--予備
								),
						dDate,
						vUser
						--NULL
						--NULL
			FROM		(
						SELECT		b1.kokyaku_cd AS kokyaku_cd,
									b1.kakin_nengetu AS kakin_nengetu,
									b1.oem_cd AS oem_cd,
									SUM(b1.teika) AS teika,
									SUM(b1.waribikigaku) AS waribikigaku,
									SUM(b1.ryokin) AS ryokin,
									SUM(b1.tax) AS tax,
									SUM(b1.zeikomi) AS zeikomi
						FROM		wk_bl_seikyu_sum a1,
						            tb_seikyu_utiwake b1,
						            tb_srv_type_mst c1,
						            wk_bl_kokyaku_info d1
						WHERE		b1.srv_type_cd = c1.srv_type_cd
						AND			c1.utiwake_type = cUtiwake
						AND			a1.data_type = CST_DATA_TYPE1
						AND			a1.seikyu_nengetu = d1.seikyu_nengetu		-- 20060410
						AND			a1.kokyaku_cd = d1.kokyaku_cd				-- 20060410
						AND			d1.modify_flg = 'N'							-- 20060410
						AND			a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_11
						AND			a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_13
						AND			a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_14
						AND			a1.kakin_nengetu = b1.kakin_nengetu
						AND			a1.seikyu_gun = b1.seikyu_gun
						AND			a1.kokyaku_cd = b1.kokyaku_cd
						AND			b1.rec_type_cd = CST_REC_TYPE
						GROUP BY	b1.kokyaku_cd,
									b1.kakin_nengetu,
									b1.oem_cd
						) a;
--			UNION

			/* 総合計(顧客単位) */
			INSERT INTO wk_bl_kaisen_meisai(
						utiwake_type,				--請求書内訳種別
						ptn_sk,						--パターンソートキー
						data_kbn_sk,					--データ区分ソートキー
						kokyaku_cd_sk,				--顧客コードソートキー
						kakin_nengetu_sk,			--課金年月ソートキー
						kokyaku_busho_cd_sk,		--顧客部署コードソートキー
						id_sk,						--IDソートキー
						print_order,					--請求書印字順ソートキー
						ryokin_cd1,					--料金項目コード１ソートキー
						sk_yobi,						--ソートキー予備
						kaisen_meisai_data,			--請求書回線明細データ
						insert_date,					--登録日時
						insert_user)					--登録ユーザ
			SELECT		cUtiwake,
						CST_PTN_SK5,
						CST_DATA_KBN_SK09,
						a.kokyaku_cd,
						/* 20060224 最大課金年月セット */
--						'999999',
						a.kakin_nengetu,
						'ZZZZ',
						'ZZZZZZZZZZZZZZZZZZZZ',
						999,
						'ZZZZZZZZZZ',
						'00',
						DECODE(i,0,
								a.kokyaku_cd ||																					--顧客コード
								/* 20060224 最大課金年月セット */
--								RPAD(' ',6) ||																					--課金年月
								a.kakin_nengetu ||																					--課金年月
								'000000000' ||																					--連番
								CST_MEISAI_PTN5 ||																				--パターン
								/* 20060206 OEMコードをマッピングするように変更 */
--								RPAD(' ',4) ||																					--グループ
								nvl(a.oem_cd,'    ') ||																			--グループ
								CST_DATA_KBN_09 ||																				--データ区分
								RPAD(' ',20) ||																					--電話番号
								RPAD('　',80,'　') ||																			--自由使用欄
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,a.teika) ||							--金額１サイン
								LPAD(ABS(a.teika),9,'0') ||																		--金額１
								' ' ||																							--フラグ１
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,a.waribikigaku) ||					--金額２サイン
								LPAD(ABS(a.waribikigaku),9,'0') ||																--金額２
								' ' ||																							--フラグ２
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,a.ryokin) ||							--金額３サイン
								LPAD(ABS(a.ryokin),9,'0') ||																	--金額３
								' ' ||																							--フラグ３
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,a.tax) ||							--金額４サイン
								LPAD(ABS(a.tax),9,'0') ||																		--金額４
								' ' ||																							--フラグ４
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,a.zeikomi) ||						--金額５サイン
								LPAD(ABS(a.zeikomi),9,'0') ||																	--金額５
								' ' ||																							--フラグ５
								'  ' ||																							--区分
								/* 20060206 項目名にそれぞれの集計の名称を固定値で設定するよう変更 */
--								RPAD('　',60,'　') ||																			--項目名
								RPAD('総合計',60,'　') ||																			--項目名
								RPAD(' ',10),																					--予備
								1,
								a.kokyaku_cd ||																					--顧客コード
--								RPAD(' ',6) ||																					--課金年月
								a.kakin_nengetu	||																				--課金年月
								'000000000' ||																					--連番
								CST_MEISAI_PTN5 ||																				--パターン
								/* 20060206 OEMコードをマッピングするように変更 */
--								RPAD(' ',4) ||																					--グループ
								nvl(a.oem_cd,'    ') ||																			--グループ
								CST_DATA_KBN_09 ||																				--データ区分
								RPAD(' ',20) ||																					--電話番号
								RPAD('　',80,'　') ||																			--自由使用欄
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,a.ryokin) ||							--金額１サイン
								LPAD(ABS(a.ryokin),9,'0') ||																	--金額１
								' ' ||																							--フラグ１
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,a.tax) ||							--金額２サイン
								LPAD(ABS(a.tax),9,'0') ||																		--金額２
								' ' ||																							--フラグ２
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,a.zeikomi) ||						--金額３サイン
								LPAD(ABS(a.zeikomi),9,'0') ||																	--金額３
								' ' ||																							--フラグ３
								'  ' ||																							--区分
								/* 20060206 項目名にそれぞれの集計の名称を固定値で設定するよう変更 */
--								RPAD('　',80,'　') ||																			--項目名
								RPAD('総合計',80,'　') ||																			--項目名
								RPAD(' ',12)																					--予備
								),
						dDate,
						vUser
						--NULL
						--NULL
			FROM		(
						/* OEMコードをグループに追加 */
						SELECT		b1.kokyaku_cd AS kokyaku_cd,
									/* 20060224 最大課金年月取得 */
									MAX(b1.kakin_nengetu) AS kakin_nengetu,		--20060224
									b1.oem_cd AS oem_cd,
									SUM(b1.teika) AS teika,
									SUM(b1.waribikigaku) AS waribikigaku,
									SUM(b1.ryokin) AS ryokin,
									SUM(b1.tax) AS tax,
									SUM(b1.zeikomi) AS zeikomi
						FROM		wk_bl_seikyu_sum a1,
						            tb_seikyu_utiwake b1,
						            tb_srv_type_mst c1,
						            wk_bl_kokyaku_info d1
						WHERE		b1.srv_type_cd = c1.srv_type_cd
						AND			c1.utiwake_type = cUtiwake
						AND			a1.data_type = CST_DATA_TYPE1
						AND			a1.seikyu_nengetu = d1.seikyu_nengetu		-- 20060410
						AND			a1.kokyaku_cd = d1.kokyaku_cd				-- 20060410
						AND			d1.modify_flg = 'N'							-- 20060410
						AND			a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_11
						AND			a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_13
						AND			a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_14
						AND			a1.kakin_nengetu = b1.kakin_nengetu
						AND			a1.seikyu_gun = b1.seikyu_gun
						AND			a1.kokyaku_cd = b1.kokyaku_cd
						AND			b1.rec_type_cd = CST_REC_TYPE
						GROUP BY	b1.kokyaku_cd,b1.oem_cd
						) a;

			/* エンドレコード作成 */
			INSERT INTO wk_bl_kaisen_meisai(
						utiwake_type,				--請求書内訳種別
						ptn_sk,						--パターンソートキー
						data_kbn_sk,					--データ区分ソートキー
						kokyaku_cd_sk,				--顧客コードソートキー
						kakin_nengetu_sk,			--課金年月ソートキー
						kokyaku_busho_cd_sk,		--顧客部署コードソートキー
						id_sk,						--IDソートキー
						print_order,					--請求書印字順ソートキー
						ryokin_cd1,					--料金項目コード１ソートキー
						sk_yobi,						--ソートキー予備
						kaisen_meisai_data,			--請求書回線明細データ
						insert_date,					--登録日時
						insert_user)					--登録ユーザ
			SELECT		cUtiwake,
						CST_PTN_SK9,
						CST_DATA_KBN_SKZZ,
						a.kokyaku_cd,
						a.kakin_nengetu,
						'ZZZZ',
						'ZZZZZZZZZZZZZZZZZZZZ',
						999,
						'ZZZZZZZZZZ',
						'00',
						a.kokyaku_cd || a.kakin_nengetu ||
						'000000000' || CST_MEISAI_PTN9 || a.oem_cd ||
						'※税区（Ｙ＝課税、Ｎ＝非課税、Ｍ＝免税、Ｄ＝代行請求）　　　　　　　　　　　　　　　　　　　　　　　' ||
						RPAD('　',100,'　') ||
						RPAD(' ',28),
						dDate,
						vUser
						--NULL
						--NULL
			FROM		(
						SELECT	DISTINCT	b1.kokyaku_cd,
											b1.kakin_nengetu,
											b1.oem_cd
						FROM	wk_bl_seikyu_sum a1,
								tb_seikyu_utiwake b1,
								tb_srv_type_mst c1,
								wk_bl_kokyaku_info d1
						WHERE	b1.srv_type_cd = c1.srv_type_cd
						AND		c1.utiwake_type = cUtiwake
						AND		a1.data_type = CST_DATA_TYPE1
						AND		a1.seikyu_nengetu = d1.seikyu_nengetu		-- 20060410
						AND		a1.kokyaku_cd = d1.kokyaku_cd				-- 20060410
						AND		d1.modify_flg = 'N'							-- 20060410
						AND		a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_11
						AND		a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_13
						AND		a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_14
						AND		a1.kakin_nengetu = b1.kakin_nengetu
						AND		a1.seikyu_gun = b1.seikyu_gun
						AND		a1.kokyaku_cd = b1.kokyaku_cd
						AND		b1.rec_type_cd = CST_REC_TYPE
						) a;

			/* 請求書回線明細一時TBL連番更新 */
			/* 2006/01/05 パフォーマンス対応 */
/*		 	UPDATE wk_bl_kaisen_meisai a
		 	SET kaisen_meisai_data
		 		=
		 		(
		 		SELECT SUBSTRB(a.kaisen_meisai_data,1,14) ||
		 				TRIM(TO_CHAR(rec_no,'000000009')) ||
		 				SUBSTRB(b.kaisen_meisai_data,24)
		 		FROM 	(SELECT	utiwake_type,
		 						kaisen_meisai_data,
		 						ptn_sk,
		 						data_kbn_sk,
		 						kokyaku_cd_sk,
		 						kakin_nengetu_sk,
		 						kokyaku_busho_cd_sk,
		 						id_sk,
		 						print_order,
		 						ryokin_cd1,
		 						row_number() over
		 						(order by ptn_sk,
		 						data_kbn_sk,
		 						kokyaku_cd_sk,
		 						kakin_nengetu_sk,
		 						id_sk,
		 						ryokin_cd1) rec_no
		 				FROM	wk_bl_kaisen_meisai
		 				WHERE	utiwake_type = cUtiwake) b
		 		WHERE 	a.utiwake_type = cUtiwake
		 		AND		b.rowid = a.rowid)
		 	WHERE		a.utiwake_type = cUtiwake;
		 	*/
		 	/* シーケンス初期化 */
		 	nSeqno := 1;
		 	FOR rec_kaisen_meisai IN cur_kaisen_meisai(cUtiwake) LOOP
		 		UPDATE	wk_bl_kaisen_meisai
		 		SET		kaisen_meisai_data = rec_kaisen_meisai.meisai_a ||
		 									TRIM(TO_CHAR(nSeqno,'000000009')) ||
		 									rec_kaisen_meisai.meisai_b
		 		WHERE CURRENT OF cur_kaisen_meisai;
		 		/* カウンタアップ */
		 		nSeqno := nSeqno + 1;
		 	END LOOP;
		END IF;
	END LOOP;
END FAOB010104;
/
