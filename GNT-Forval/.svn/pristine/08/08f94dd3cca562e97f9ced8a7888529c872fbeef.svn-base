CREATE OR REPLACE PROCEDURE        FAOB010106
/********************************************************************/
/*  関数名：FAOB0101												*/
/*  機能名：請求書データ作成：請求書出荷明細編集					*/
/*  作成者：山田													*/
/*  作成日：2005/11/01												*/
/*  更新日：2006/03/09	田中	データレコード備考のマッピング変更	*/
/*        ：2006/03/22	田中	注文番号→納品番号に変更			*/
/*        ：2006/04/10	田中	支払区分(12→02)のものは出力しない	*/
/*        ：2006/04/21	田中	ID計でのグループから、印字用IDを削除*/
/*        ：2006/05/09	田中	郵ビル対応							*/
/*	VER   ：1.04													*/
/*------------------------------------------------------------------*/
/*	引数：		無し												*/
/*	戻値：		無し												*/
/********************************************************************/
IS
	/* ローカル定数 */
	CONST_MEISAI_SHUKKA	CONSTANT wk_bl_shukka_meisai.meisai_type%TYPE := '23';
	CONST_PATTERN_HD1 		CONSTANT wk_bl_shukka_meisai.ptn_sk%TYPE := '01';
	CONST_PATTERN_HD2		CONSTANT wk_bl_shukka_meisai.ptn_sk%TYPE := '02';
	CONST_PATTERN_MEI 		CONSTANT wk_bl_shukka_meisai.ptn_sk%TYPE := '05';
	CONST_PATTERN_END 		CONSTANT wk_bl_shukka_meisai.ptn_sk%TYPE := '09';
	CONST_DATA_TYPE_01	 	CONSTANT wk_bl_seikyu_sum.data_type%TYPE := '01';
	CONST_DATA_KBN_00 		CONSTANT wk_bl_shukka_meisai.data_kbn_sk%TYPE := '00';
	CONST_DATA_KBN_01 		CONSTANT wk_bl_shukka_meisai.data_kbn_sk%TYPE := '01';
	CONST_DATA_KBN_02 		CONSTANT wk_bl_shukka_meisai.data_kbn_sk%TYPE := '02';
	CONST_DATA_KBN_03 		CONSTANT wk_bl_shukka_meisai.data_kbn_sk%TYPE := '03';
	CONST_DATA_KBN_04 		CONSTANT wk_bl_shukka_meisai.data_kbn_sk%TYPE := '04';
	CONST_DATA_KBN_05 		CONSTANT wk_bl_shukka_meisai.data_kbn_sk%TYPE := '05';
	CONST_DATA_KBN_06 		CONSTANT wk_bl_shukka_meisai.data_kbn_sk%TYPE := '06';
	CONST_DATA_KBN_07 		CONSTANT wk_bl_shukka_meisai.data_kbn_sk%TYPE := '07';
	CONST_DATA_KBN_08 		CONSTANT wk_bl_shukka_meisai.data_kbn_sk%TYPE := '08';
	CONST_DATA_KBN_09 		CONSTANT wk_bl_shukka_meisai.data_kbn_sk%TYPE := '09';
	CONST_DATA_KBN_0A 		CONSTANT wk_bl_shukka_meisai.data_kbn_sk%TYPE := '0A';
	CONST_DATA_KBN_ZZ 		CONSTANT wk_bl_shukka_meisai.data_kbn_sk%TYPE := 'ZZ';

	CONST_DATA_KBN_1 CONSTANT 	CHAR(1) := '1';
	CONST_DATA_KBN_2 CONSTANT 	CHAR(1) := '2';
	CONST_DATA_KBN_3 CONSTANT 	CHAR(1) := '3';
	CONST_DATA_KBN_4 CONSTANT 	CHAR(1) := '4';
	CONST_DATA_KBN_5 CONSTANT 	CHAR(1) := '5';
	CONST_DATA_KBN_6 CONSTANT 	CHAR(1) := '6';
	CONST_DATA_KBN_7 CONSTANT 	CHAR(1) := '7';
	CONST_DATA_KBN_8 CONSTANT 	CHAR(1) := '8';
	CONST_DATA_KBN_9 CONSTANT 	CHAR(1) := '9';
	CONST_DATA_KBN_A CONSTANT 	CHAR(1) := 'A';

	CONST_PATTERN_1 CONSTANT 	CHAR(1) := '1';
	CONST_PATTERN_2 CONSTANT 	CHAR(1) := '2';
	CONST_PATTERN_5 CONSTANT 	CHAR(1) := '5';
	CONST_PATTERN_9 CONSTANT 	CHAR(1) := '9';

	CST_DEL_TABLE	CONSTANT VARCHAR2(20) := 'WK_BL_SHUKKA_MEISAI';	--削除対象テーブル名

	/* ローカル変数 */
	nRet					NUMBER;								--リターンコード
	dSysDate				wk_bl_shukka_meisai.insert_date%TYPE;	--システム日付
	vUser					wk_bl_shukka_meisai.insert_user%TYPE;	--ユーザ
	/* UPDATE修正用変数 */
	nSeqno					NUMBER;								--シーケンスカウンタ

	/* 1/25UPDATE変更 */
	/* カーソル定義 */
	CURSOR cur_shukka_meisai
	IS
		SELECT	SUBSTRB(kaisen_meisai_data,1,14) meisai_a,
		 		SUBSTRB(kaisen_meisai_data,24) meisai_b
		FROM	wk_bl_shukka_meisai
		WHERE	meisai_type = CONST_MEISAI_SHUKKA
		/* 200060204 ソート変更 */
		ORDER BY	kokyaku_cd_sk,
					kakin_nengetu_sk,
					ptn_sk,
					id_sk,
					order_bng_sk,
					data_kbn_sk,
					shukka_date_sk,
					/* 20060216 商品コードソート追加 */
					SUBSTRB(kaisen_meisai_data,104,10)
					--kokyaku_cd_sk,
--					id_sk,
		FOR UPDATE;

	rec_shukka_meisai		cur_shukka_meisai%ROWTYPE;
	/* 20060202 金額を絶対値で入れるように変更 */
BEGIN

	/* 請求書出荷明細一時TBLの削除 */
	EXECUTE IMMEDIATE 'TRUNCATE TABLE ' || CST_DEL_TABLE;

	/* システム日時の取得 */
	nRet := aoba_common.get_date( dSysDate );

	/* ユーザの取得 */
	nRet := aoba_common.get_user( vUser );

	/* 請求書出荷明細一時TBL挿入（ヘッダレコード２） */
	INSERT INTO	wk_bl_shukka_meisai(
			meisai_type, 								-- 請求書明細種別
			ptn_sk, 									-- パターンソートキー
			data_kbn_sk, 								-- データ区分ソートキー
			kokyaku_cd_sk,								-- 顧客コードソートキー
			kakin_nengetu_sk,							-- 課金年月ソートキー
			id_sk,										-- IDソートキー
			order_bng_sk,								-- 注文番号ソートキー
			shukka_date_sk,								-- 注文日ソートキー
			sk_yobi,									-- ソートキー予備
			kaisen_meisai_data,							-- 請求書回線明細データ
			insert_date,								-- 登録日時
			insert_user,								-- 登録ユーザ
			update_date,								-- 更新日時
			update_user)								-- 更新ユーザ
	SELECT	CONST_MEISAI_SHUKKA,
			CONST_PATTERN_HD2,
			CONST_DATA_KBN_00,
			a.kokyaku_cd,
			a.kakin_nengetu,
			'00000000000000000000',
			'0000000000',
			'0000000000',
			'00',
			a.kokyaku_cd || a.kakin_nengetu ||
			'000000000' || CONST_PATTERN_2 || a.oem_cd ||
			'注文日　　　' ||
			'出荷日　　　' ||
			'カタログ商品コード　' ||
			'商品名　　　' ||
			'納品数　　　' ||
			'単価　　　　' ||
			'御請求額　　' ||
			'備考　　　　' ||
			RPAD(' ',124),
			dSysDate,
			vUser,
			aoba_const.CST_NULL,
			aoba_const.CST_NULL
	FROM 	(SELECT DISTINCT	a2.kokyaku_cd AS kokyaku_cd,
								a2.kakin_nengetu AS kakin_nengetu,
								a2.oem_cd AS oem_cd
			FROM	wk_bl_seikyu_sum a1,
					tb_shukka_meisai a2,
					tb_srv_type_mst a3,
					tb_data_kbn_mst a4,
					wk_bl_kokyaku_info a5,							-- 20060410
					tb_kokyaku_id_joho a6							-- 20060509
			WHERE	a1.data_type = CONST_DATA_TYPE_01
			AND		a1.kokyaku_cd = a5.kokyaku_cd					-- 20060410
			AND		a1.seikyu_nengetu = a5.seikyu_nengetu			-- 20060410
			AND		a5.modify_flg = 'N'								-- 20060410
			AND		a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_11
			AND		a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_13
			AND		a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_14
			AND		a1.kakin_nengetu = a2.kakin_nengetu
			AND		a1.seikyu_gun = a2.seikyu_gun
			AND		a1.kokyaku_cd = a2.kokyaku_cd
			AND		a2.srv_type_cd = a3.srv_type_cd
			AND		a3.meisai_type = CONST_MEISAI_SHUKKA
			AND		a2.data_kbn_cd = a4.data_kbn_cd
			/* 顧客別ID情報JOIN */		-- 20060509 ADD
			AND		a2.srv_type_cd = a6.srv_type_cd
			AND		a2.id = a6.id
			AND		a6.meisai_tel_hyoji_flg is not null) a;

	/* 請求書出荷明細一時TBL挿入（ヘッダレコード１） */
	INSERT INTO	wk_bl_shukka_meisai(
			meisai_type, 								-- 請求書明細種別
			ptn_sk, 									-- パターンソートキー
			data_kbn_sk, 								-- データ区分ソートキー
			kokyaku_cd_sk,								-- 顧客コードソートキー
			kakin_nengetu_sk,							-- 課金年月ソートキー
			id_sk,										-- IDソートキー
			order_bng_sk,								-- 注文番号ソートキー
			shukka_date_sk,								-- 注文日ソートキー
			sk_yobi,									-- ソートキー予備
			kaisen_meisai_data,							-- 請求書回線明細データ
			insert_date,								-- 登録日時
			insert_user,								-- 登録ユーザ
			update_date,								-- 更新日時
			update_user)								-- 更新ユーザ
	SELECT	CONST_MEISAI_SHUKKA,
			CONST_PATTERN_HD1,
			CONST_DATA_KBN_00,
			a.kokyaku_cd,
			a.kakin_nengetu,
			'00000000000000000000',
			'0000000000',
			'0000000000',
			'00',
			a.kokyaku_cd || a.kakin_nengetu ||
			'000000000' || CONST_PATTERN_1 || a.oem_cd ||
			'ワンビリングサービス／納品明細　　　　　' ||
			'　　　　　　　　　　　　　　　　　　　　' ||
			'　　　　　　　　　　　　　　　　　　　　' ||
			RPAD(' ',108),
			dSysDate,
			vUser,
			aoba_const.CST_NULL,
			aoba_const.CST_NULL
	FROM 	(SELECT DISTINCT	a2.kokyaku_cd AS kokyaku_cd,
								a2.kakin_nengetu AS kakin_nengetu,
								a2.oem_cd AS oem_cd
			FROM	wk_bl_seikyu_sum a1,
					tb_shukka_meisai a2,
					tb_srv_type_mst a3,
					tb_data_kbn_mst a4,
					wk_bl_kokyaku_info a5,							-- 20060410
					tb_kokyaku_id_joho a6							-- 20060509
			WHERE	a1.data_type = CONST_DATA_TYPE_01
			AND		a1.kokyaku_cd = a5.kokyaku_cd					-- 20060410
			AND		a1.seikyu_nengetu = a5.seikyu_nengetu			-- 20060410
			AND		a5.modify_flg = 'N'								-- 20060410
			AND		a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_11
			AND		a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_13
			AND		a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_14
			AND		a1.kakin_nengetu = a2.kakin_nengetu
			AND		a1.seikyu_gun = a2.seikyu_gun
			AND		a1.kokyaku_cd = a2.kokyaku_cd
			AND		a2.srv_type_cd = a3.srv_type_cd
			AND		a3.meisai_type = CONST_MEISAI_SHUKKA
			AND		a2.data_kbn_cd = a4.data_kbn_cd
			/* 顧客別ID情報JOIN */		-- 20060509 ADD
			AND		a2.srv_type_cd = a6.srv_type_cd
			AND		a2.id = a6.id
			AND		a6.meisai_tel_hyoji_flg is not null) a;

	/* 請求書出荷明細一時TBL挿入（データレコード） */
	INSERT INTO	wk_bl_shukka_meisai(
			meisai_type, 								-- 請求書明細種別
			ptn_sk, 									-- パターンソートキー
			data_kbn_sk, 								-- データ区分ソートキー
			kokyaku_cd_sk,								-- 顧客コードソートキー
			kakin_nengetu_sk,							-- 課金年月ソートキー
			id_sk,										-- IDソートキー
			order_bng_sk,								-- 注文番号ソートキー
			shukka_date_sk,								-- 注文日ソートキー
			sk_yobi,									-- ソートキー予備
			kaisen_meisai_data,							-- 請求書回線明細データ
			insert_date,								-- 登録日時
			insert_user,								-- 登録ユーザ
			update_date,								-- 更新日時
			update_user)								-- 更新ユーザ
	SELECT	CONST_MEISAI_SHUKKA,
			CONST_PATTERN_MEI,
			CONST_DATA_KBN_01,
			b.kokyaku_cd,
			b.kakin_nengetu,
			b.id,
			b.uriage_bng,
			b.order_date,
			'00',
			b.kokyaku_cd ||										-- 顧客コード
			b.kakin_nengetu ||									-- 課金年月
			'000000000' ||										-- 通番
			CONST_PATTERN_5 ||									-- パターン
			b.oem_cd ||											-- グループ
			CONST_DATA_KBN_1 ||									-- データ区分
			RPAD(SUBSTR(b.print_id,1,20),20,' ') ||				-- 電話番号
--			'注文番号：　　' ||
			'納品番号：　　' ||									-- 20060322
			b.uriage_bng || RPAD(' ',10) ||						-- 自由使用欄2
			b.order_date ||										-- 発注日
			b.shukka_date ||									-- 出荷日
			b.shohin_cd ||										-- 商品コード
			RPAD(SUBSTRB(b.shohin_name1,1,80),80,'　') ||		-- 商品名
			aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,
				b.shukka_num) ||
			TRIM(TO_CHAR(ABS(b.shukka_num),'000000009')) ||		-- 金額1
			aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,
				b.user_tanka) ||
			TRIM(TO_CHAR(ABS(b.user_tanka),'000000009')) ||		-- 金額2
			aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,
				b.meisai_shokei) ||
			TRIM(TO_CHAR(ABS(b.meisai_shokei),'000000009')) ||	-- 金額3
--			SUBSTR(d.data_kbn_name,1,20) ||						-- 備考
			/* 20060309 start */
			DECODE(d.data_kbn_cd,'0',
					RPAD('　',20,'　'),
					RPAD(SUBSTR(d.data_kbn_name,1,20),20,'　')) ||-- 備考
			/* 20060309 end */
			RPAD(' ',13), 										-- 予備
			dSysDate,
			vUser,
			aoba_const.CST_NULL,
			aoba_const.CST_NULL
	FROM	wk_bl_seikyu_sum a,
			tb_shukka_meisai b,
			tb_srv_type_mst c,
			tb_data_kbn_mst d,
			wk_bl_kokyaku_info e,						-- 20060410
			tb_kokyaku_id_joho f						-- 20060509
	WHERE	a.data_type = CONST_DATA_TYPE_01
	AND		a.kokyaku_cd = e.kokyaku_cd					-- 20060410
	AND		a.seikyu_nengetu = e.seikyu_nengetu			-- 20060410
	AND		e.modify_flg = 'N'							-- 20060410
	AND		a.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_11
	AND		a.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_13
	AND		a.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_14
	/* 2005/12/13 不要条件により削除 */
--	AND 	a.koza_bng_hyoji_flg IS NOT NULL
	AND		a.kakin_nengetu = b.kakin_nengetu
	AND		a.seikyu_gun = b.seikyu_gun
	AND		a.kokyaku_cd = b.kokyaku_cd
	AND		b.srv_type_cd = c.srv_type_cd
	AND		c.meisai_type = CONST_MEISAI_SHUKKA
	AND		b.data_kbn_cd = d.data_kbn_cd
	/* 顧客別ID情報JOIN */		-- 20060509 ADD
	AND		b.srv_type_cd = f.srv_type_cd
	AND		b.id = f.id
	AND		f.meisai_tel_hyoji_flg is not null
	UNION
	/* 注文小計 */
	/* 売上番号ごとに一意になるようなﾃﾞｰﾀを取得するよう変更 */
	SELECT	CONST_MEISAI_SHUKKA,
			CONST_PATTERN_MEI,
			CONST_DATA_KBN_02,
			a.kokyaku_cd,
			a.kakin_nengetu,
			a.id,
			a.uriage_bng,
--			a.order_date,
			RPAD(' ',10),
			'00',
			a.kokyaku_cd ||														-- 顧客コード
			a.kakin_nengetu ||													-- 課金年月
			'000000000' ||														-- 通番
			CONST_PATTERN_5 ||													-- パターン
			a.oem_cd ||															-- グループ
			CONST_DATA_KBN_2 ||													-- データ区分
			RPAD(SUBSTR(a.print_id,1,20),20,' ') ||								-- 電話番号
--			'注文番号：　　' ||
			'納品番号：　　' ||													-- 20060322
			a.uriage_bng || RPAD(' ',10) ||										-- 自由使用欄2
--			a.order_date ||														-- 発注日
			RPAD(' ',10) ||														-- 発注日
--			a.shukka_date ||													-- 出荷日
			RPAD(' ',10) ||														-- 出荷日
--			a.shohin_cd ||														-- 商品コード
			RPAD(' ',10) ||														-- 商品コード
--			RPAD(SUBSTRB(a.shohin_name1,1,80),80,'　') ||						-- 商品名
			RPAD('　',80,'　') ||												-- 商品名
			aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,0) ||
			'000000000' ||														-- 金額1
			aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,0) ||
			'000000000' ||														-- 金額2
			aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,
				a.user_shokei) ||
			TRIM(TO_CHAR(ABS(a.user_shokei),'000000009')) ||					-- 金額3
			/* 20060206 項目名にそれぞれの集計の名称を固定値で設定するよう変更 */
--			SUBSTR(a.data_kbn_name,1,20) ||										-- 備考
			RPAD('伝票小計',20,'　') ||											-- 備考
			RPAD(' ',13),														-- 予備
			dSysDate,
			vUser,
			aoba_const.CST_NULL,
			aoba_const.CST_NULL
	FROM 	(SELECT DISTINCT	a2.kokyaku_cd AS kokyaku_cd,
								a2.kakin_nengetu AS kakin_nengetu,
								a2.oem_cd AS oem_cd,
								a2.id AS id,
								a2.uriage_bng AS uriage_bng,
--								a2.order_date AS order_date,
--								a2.shukka_date AS shukka_date,
--								a2.shohin_cd AS shohin_cd,
--								a2.shohin_name1 AS shohin_name1,
								a2.print_id AS print_id,
								a2.user_shokei AS user_shokei,
								a1.koza_bng_hyoji_flg AS koza_bng_hyoji_flg
--								a4.data_kbn_name AS data_kbn_name
			FROM	wk_bl_seikyu_sum a1,
					tb_shukka_meisai a2,
					tb_srv_type_mst a3,
					tb_data_kbn_mst a4,
					wk_bl_kokyaku_info a5,							-- 20060410
					tb_kokyaku_id_joho a6							-- 20060509
			WHERE	a1.data_type = CONST_DATA_TYPE_01
			AND		a1.kokyaku_cd = a5.kokyaku_cd					-- 20060410
			AND		a1.seikyu_nengetu = a5.seikyu_nengetu			-- 20060410
			AND		a5.modify_flg = 'N'								-- 20060410
			AND		a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_11
			AND		a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_13
			AND		a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_14
			/* 2005/12/13 不要条件により削除 */
--			AND		a1.koza_bng_hyoji_flg IS NOT NULL
			AND		a1.kakin_nengetu = a2.kakin_nengetu
			AND		a1.seikyu_gun = a2.seikyu_gun
			AND		a1.kokyaku_cd = a2.kokyaku_cd
			AND		a2.srv_type_cd = a3.srv_type_cd
			AND		a3.meisai_type = CONST_MEISAI_SHUKKA
			AND		a2.data_kbn_cd = a4.data_kbn_cd
			/* 顧客別ID情報JOIN */		-- 20060509 ADD
			AND		a2.srv_type_cd = a6.srv_type_cd
			AND		a2.id = a6.id
			AND		a6.meisai_tel_hyoji_flg is not null) a
	UNION
	/* 注文消費税 */
	SELECT	CONST_MEISAI_SHUKKA,
			CONST_PATTERN_MEI,
			CONST_DATA_KBN_03,
			a.kokyaku_cd,
			a.kakin_nengetu,
			a.id,
			a.uriage_bng,
--			a.order_date,
			RPAD(' ',10),
			'00',
			a.kokyaku_cd ||											-- 顧客コード
			a.kakin_nengetu ||										-- 課金年月
			'000000000' ||											-- 通番
			CONST_PATTERN_5 ||										-- パターン
			a.oem_cd ||												-- グループ
			CONST_DATA_KBN_3 ||										-- データ区分
			RPAD(SUBSTR(a.print_id,1,20),20,' ') || 				-- 電話番号
--			'注文番号：　　' ||
			'納品番号：　　' ||										-- 20060322
			a.uriage_bng || RPAD(' ',10) ||							-- 自由使用欄2
--			a.order_date ||											-- 発注日
			RPAD(' ',10) ||											-- 発注日
--			a.shukka_date || 										-- 出荷日
			RPAD(' ',10) || 										-- 出荷日
--			a.shohin_cd ||											-- 商品コード
			RPAD(' ',10) ||											-- 商品コード
--			RPAD(SUBSTRB(a.shohin_name1,1,80),80,'　') ||			-- 商品名
			RPAD('　',80,'　') ||									-- 商品名
			aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,0) ||
			'000000000' ||											-- 金額1
			aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,0) ||
			'000000000' ||											-- 金額2
			aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,
				a.user_tax) ||
			TRIM(TO_CHAR(ABS(a.user_tax),'000000009')) ||			-- 金額3
			/* 20060206 項目名にそれぞれの集計の名称を固定値で設定するよう変更 */
--			SUBSTR(a.data_kbn_name,1,20) ||							-- 備考
			RPAD('伝票消費税',20,'　') ||							-- 備考
			RPAD(' ',13),											-- 予備
			dSysDate,
			vUser,
			aoba_const.CST_NULL,
			aoba_const.CST_NULL
	FROM 	(SELECT DISTINCT	a2.kokyaku_cd AS kokyaku_cd,
								a2.kakin_nengetu AS kakin_nengetu,
								a2.oem_cd AS oem_cd,
								a2.id AS id,
								a2.uriage_bng AS uriage_bng,
--								a2.order_date AS order_date,
--								a2.shukka_date AS shukka_date,
--								a2.shohin_cd AS shohin_cd,
--								a2.shohin_name1 AS shohin_name1,
								a2.print_id AS print_id,
								a2.user_tax AS user_tax,
								a1.koza_bng_hyoji_flg AS koza_bng_hyoji_flg
--								a4.data_kbn_name AS data_kbn_name
			FROM	wk_bl_seikyu_sum a1,
					tb_shukka_meisai a2,
					tb_srv_type_mst a3,
					tb_data_kbn_mst a4,
					wk_bl_kokyaku_info a5,							-- 20060410
					tb_kokyaku_id_joho a6							-- 20060509
			WHERE	a1.data_type = CONST_DATA_TYPE_01
			AND		a1.kokyaku_cd = a5.kokyaku_cd					-- 20060410
			AND		a1.seikyu_nengetu = a5.seikyu_nengetu			-- 20060410
			AND		a5.modify_flg = 'N'								-- 20060410
			AND		a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_11
			AND		a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_13
			AND		a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_14
			/* 2005/12/13 不要条件により削除 */
--			AND 	a1.koza_bng_hyoji_flg IS NOT NULL
			AND		a1.kakin_nengetu = a2.kakin_nengetu
			AND		a1.seikyu_gun = a2.seikyu_gun
			AND		a1.kokyaku_cd = a2.kokyaku_cd
			AND		a2.srv_type_cd = a3.srv_type_cd
			AND     a3.meisai_type = CONST_MEISAI_SHUKKA
			AND     a2.data_kbn_cd = a4.data_kbn_cd
			/* 顧客別ID情報JOIN */		-- 20060509 ADD
			AND		a2.srv_type_cd = a6.srv_type_cd
			AND		a2.id = a6.id
			AND		a6.meisai_tel_hyoji_flg is not null) a
	UNION
	/* 注文合計 */
	SELECT	CONST_MEISAI_SHUKKA,
			CONST_PATTERN_MEI,
			CONST_DATA_KBN_04,
			a.kokyaku_cd,
			a.kakin_nengetu,
			a.id,
			a.uriage_bng,
--			a.order_date,
			RPAD(' ',10),
			'00',
			a.kokyaku_cd ||											-- 顧客コード
			a.kakin_nengetu ||										-- 課金年月
			'000000000' ||											-- 通番
			CONST_PATTERN_5 ||										-- パターン
			a.oem_cd ||												-- グループ
			CONST_DATA_KBN_4 ||										-- データ区分
			RPAD(SUBSTR(a.print_id,1,20),20,' ') ||					-- 電話番号
--			'注文番号：　　' ||
			'納品番号：　　' ||										-- 20060322
			a.uriage_bng || RPAD(' ',10) ||							-- 自由使用欄2
--			a.order_date ||											-- 発注日
			RPAD(' ',10) ||											-- 発注日
--			a.shukka_date ||										-- 出荷日
			RPAD(' ',10) ||											-- 出荷日
--			a.shohin_cd ||											-- 商品コード
			RPAD(' ',10) ||											-- 商品コード
--			RPAD(SUBSTRB(a.shohin_name1,1,80),80,'　') ||			-- 商品名
			RPAD('　',80,'　') ||									-- 商品名
			aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,0) ||
			'000000000' ||											-- 金額1
			aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,0) ||
			'000000000' ||											-- 金額2
			aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,
				a.user_gokei) ||
			TRIM(TO_CHAR(ABS(a.user_gokei),'000000009')) ||			-- 金額3
			/* 20060206 項目名にそれぞれの集計の名称を固定値で設定するよう変更 */
--			SUBSTR(a.data_kbn_name,1,20) || 						-- 備考
			RPAD('伝票合計',20,'　') ||								-- 備考
			RPAD(' ',13), 											-- 予備
			dSysDate,
			vUser,
			aoba_const.CST_NULL,
			aoba_const.CST_NULL
	FROM 	(SELECT DISTINCT	a2.kokyaku_cd AS kokyaku_cd,
								a2.kakin_nengetu AS kakin_nengetu,
								a2.oem_cd AS oem_cd,
								a2.id AS id,
								a2.uriage_bng AS uriage_bng,
--								a2.order_date AS order_date,
--								a2.shukka_date AS shukka_date,
--								a2.shohin_cd AS shohin_cd,
--								a2.shohin_name1 AS shohin_name1,
								a2.print_id AS print_id,
								a2.user_gokei AS user_gokei,
								a1.koza_bng_hyoji_flg AS koza_bng_hyoji_flg
--								a4.data_kbn_name AS data_kbn_name
			FROM	wk_bl_seikyu_sum a1,
					tb_shukka_meisai a2,
					tb_srv_type_mst a3,
					tb_data_kbn_mst a4,
					wk_bl_kokyaku_info a5,							-- 20060410
					tb_kokyaku_id_joho a6							-- 20060509
			WHERE	a1.data_type = CONST_DATA_TYPE_01
			AND		a1.kokyaku_cd = a5.kokyaku_cd					-- 20060410
			AND		a1.seikyu_nengetu = a5.seikyu_nengetu			-- 20060410
			AND		a5.modify_flg = 'N'								-- 20060410
			AND		a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_11
			AND		a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_13
			AND		a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_14
			/* 2005/12/13 不要条件により削除 */
--			AND 	a1.koza_bng_hyoji_flg IS NOT NULL
			AND		a1.kakin_nengetu = a2.kakin_nengetu
			AND		a1.seikyu_gun = a2.seikyu_gun
			AND		a1.kokyaku_cd = a2.kokyaku_cd
			AND		a2.srv_type_cd = a3.srv_type_cd
			AND     a3.meisai_type = CONST_MEISAI_SHUKKA
			AND     a2.data_kbn_cd = a4.data_kbn_cd
			/* 顧客別ID情報JOIN */		-- 20060509 ADD
			AND		a2.srv_type_cd = a6.srv_type_cd
			AND		a2.id = a6.id
			AND		a6.meisai_tel_hyoji_flg is not null) a
	UNION
	/* BOXNO小計 */
	SELECT	CONST_MEISAI_SHUKKA,
			CONST_PATTERN_MEI,
			CONST_DATA_KBN_05,
			a.kokyaku_cd,
			a.kakin_nengetu,
			a.id,
			'ZZZZZZZZZZ',
			'ZZZZZZZZZZ',
			'00',
			a.kokyaku_cd || 										-- 顧客コード
			a.kakin_nengetu ||										-- 課金年月
			'000000000' || 											-- 通番
			CONST_PATTERN_5 || 										-- パターン
			a.oem_cd || 											-- グループ
			CONST_DATA_KBN_5 || 									-- データ区分
			RPAD(SUBSTR(a.print_id,1,20),20,' ') || 				-- 電話番号
			RPAD('　',14,'　') ||									-- 自由使用欄1
			RPAD(' ',20) || 										-- 自由使用欄2
			RPAD(' ',10) || 										-- 発注日
			RPAD(' ',10) || 										-- 出荷日
			RPAD(' ',10) || 										-- 商品コード
			RPAD('　',80,'　') ||									-- 商品名
			aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,0) ||
			'000000000' || 											-- 金額1
			aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,0) ||
			'000000000' || 											-- 金額2
			aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,
			a.user_shokei) ||
			TRIM(TO_CHAR(ABS(a.user_shokei),'000000009')) ||		-- 金額3
			/* 20060206 項目名にそれぞれの集計の名称を固定値で設定するよう変更 */
--			RPAD(' ',20) || 										-- 備考
			RPAD('ＩＤ小計',20,'　') || 							-- 備考
			RPAD(' ',13), 											-- 予備
			dSysDate,
			vUser,
			aoba_const.CST_NULL,
			aoba_const.CST_NULL
	FROM 	(SELECT 	a2.kokyaku_cd,
						a2.kakin_nengetu,
						a2.oem_cd,
						a2.id,
--						a2.print_id,
						MIN(a2.print_id) AS print_id,			-- 20060421 成毛
						a1.koza_bng_hyoji_flg,
						SUM(a2.user_shokei) AS user_shokei
			FROM		wk_bl_seikyu_sum a1,
						/* 20060216 伝票単位のレコードに絞込み */
--						tb_shukka_meisai a2,
						(
						SELECT DISTINCT	kokyaku_cd,
										kakin_nengetu,
										oem_cd,
										id,
										print_id,
										user_shokei,
										seikyu_gun,
										srv_type_cd,
										data_kbn_cd,
										uriage_bng
						FROM	tb_shukka_meisai
						) a2,
						tb_srv_type_mst  a3,
						tb_data_kbn_mst  a4,
						wk_bl_kokyaku_info a5,						-- 20060410
						tb_kokyaku_id_joho a6						-- 20060509
			WHERE		a1.data_type = CONST_DATA_TYPE_01
			AND			a1.kokyaku_cd = a5.kokyaku_cd				-- 20060410
			AND			a1.seikyu_nengetu = a5.seikyu_nengetu		-- 20060410
			AND			a5.modify_flg = 'N'							-- 20060410
			AND			a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_11
			AND			a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_13
			AND			a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_14
			/* 2005/12/13 不要条件により削除 */
--			AND			a1.koza_bng_hyoji_flg IS NOT NULL
			AND			a1.kakin_nengetu = a2.kakin_nengetu
			AND			a1.seikyu_gun = a2.seikyu_gun
			AND			a1.kokyaku_cd = a2.kokyaku_cd
			AND			a2.srv_type_cd = a3.srv_type_cd
			AND			a3.meisai_type = CONST_MEISAI_SHUKKA
			AND			a2.data_kbn_cd = a4.data_kbn_cd
			/* 顧客別ID情報JOIN */		-- 20060509 ADD
			AND			a2.srv_type_cd = a6.srv_type_cd
			AND			a2.id = a6.id
			AND			a6.meisai_tel_hyoji_flg is not null
			GROUP BY	a2.kokyaku_cd,
						a2.kakin_nengetu,
						a2.oem_cd,
						a2.id,
--						a2.print_id,		-- 20060421
						a1.koza_bng_hyoji_flg) a
	UNION
	/* BOXNO消費税 */
	SELECT	CONST_MEISAI_SHUKKA,
			CONST_PATTERN_MEI,
			CONST_DATA_KBN_06,
			a.kokyaku_cd,
			a.kakin_nengetu,
			a.id,
			'ZZZZZZZZZZ',
			'ZZZZZZZZZZ',
			'00',
			a.kokyaku_cd || 										-- 顧客コード
			a.kakin_nengetu ||										-- 課金年月
			'000000000' || 											-- 通番
			CONST_PATTERN_5 || 										-- パターン
			a.oem_cd || 											-- グループ
			CONST_DATA_KBN_6 || 									-- データ区分
			RPAD(SUBSTR(a.print_id,1,20),20,' ') || 				-- 電話番号
			RPAD('　',14,'　') ||									-- 自由使用欄1
			RPAD(' ',20) || 										-- 自由使用欄2
			RPAD(' ',10) || 										-- 発注日
			RPAD(' ',10) || 										-- 出荷日
			RPAD(' ',10) || 										-- 商品コード
			RPAD('　',80,'　') ||									-- 商品名
			aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,0) ||
			'000000000' || 											-- 金額1
			aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,0) ||
			'000000000' || 											-- 金額2
			aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,
			a.user_tax) ||
			TRIM(TO_CHAR(ABS(a.user_tax),'000000009')) ||			-- 金額3
			/* 20060206 項目名にそれぞれの集計の名称を固定値で設定するよう変更 */
--			RPAD(' ',20) || 										-- 備考
			RPAD('ＩＤ消費税',20,'　') || 							-- 備考
			RPAD(' ',13), 											-- 予備
			dSysDate,
			vUser,
			aoba_const.CST_NULL,
			aoba_const.CST_NULL
	FROM 	(SELECT 	a2.kokyaku_cd,
						a2.kakin_nengetu,
						a2.oem_cd,
						a2.id,
--						a2.print_id,
						MIN(a2.print_id) AS print_id,	--20060421 成毛
						a1.koza_bng_hyoji_flg,
						SUM(a2.user_tax) AS user_tax
			FROM		wk_bl_seikyu_sum a1,
						/* 20060216 伝票単位のレコードに絞込み */
--						tb_shukka_meisai a2,
						(
						SELECT DISTINCT	kokyaku_cd,
										kakin_nengetu,
										oem_cd,
										id,
										print_id,
										user_tax,
										seikyu_gun,
										srv_type_cd,
										data_kbn_cd,
										uriage_bng
						FROM	tb_shukka_meisai
						) a2,
						tb_srv_type_mst a3,
						tb_data_kbn_mst a4,
						wk_bl_kokyaku_info a5,						-- 20060410
						tb_kokyaku_id_joho a6						-- 20060509
			WHERE		a1.data_type = CONST_DATA_TYPE_01
			AND			a1.kokyaku_cd = a5.kokyaku_cd				-- 20060410
			AND			a1.seikyu_nengetu = a5.seikyu_nengetu		-- 20060410
			AND			a5.modify_flg = 'N'							-- 20060410
			AND			a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_11
			AND			a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_13
			AND			a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_14
			/* 2005/12/13 不要条件により削除 */
--			AND			a1.koza_bng_hyoji_flg IS NOT NULL
			AND			a1.kakin_nengetu = a2.kakin_nengetu
			AND			a1.seikyu_gun = a2.seikyu_gun
			AND			a1.kokyaku_cd = a2.kokyaku_cd
			AND			a2.srv_type_cd = a3.srv_type_cd
			AND			a3.meisai_type = CONST_MEISAI_SHUKKA
			AND			a2.data_kbn_cd = a4.data_kbn_cd
			/* 顧客別ID情報JOIN */		-- 20060509 ADD
			AND		a2.srv_type_cd = a6.srv_type_cd
			AND		a2.id = a6.id
			AND		a6.meisai_tel_hyoji_flg is not null
			GROUP BY	a2.kokyaku_cd,
						a2.kakin_nengetu,
						a2.oem_cd,
						a2.id,
--						a2.print_id,		-- 20060421
						a1.koza_bng_hyoji_flg) a
	UNION
	/* BOXNO合計 */
	SELECT	CONST_MEISAI_SHUKKA,
			CONST_PATTERN_MEI,
			CONST_DATA_KBN_07,
			a.kokyaku_cd,
			a.kakin_nengetu,
			a.id,
			'ZZZZZZZZZZ',
			'ZZZZZZZZZZ',
			'00',
			a.kokyaku_cd || 										-- 顧客コード
			a.kakin_nengetu ||										-- 課金年月
			'000000000' || 											-- 通番
			CONST_PATTERN_5 || 										-- パターン
			a.oem_cd || 											-- グループ
			CONST_DATA_KBN_7 || 									-- データ区分
			RPAD(SUBSTR(a.print_id,1,20),20,' ') || 				-- 電話番号
			RPAD('　',14,'　') ||									-- 自由使用欄1
			RPAD(' ',20) || 										-- 自由使用欄2
			RPAD(' ',10) || 										-- 発注日
			RPAD(' ',10) || 										-- 出荷日
			RPAD(' ',10) || 										-- 商品コード
			RPAD('　',80,'　') ||									-- 商品名
			aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,0) ||
			'000000000' || 											-- 金額1
			aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,0) ||
			'000000000' || 											-- 金額2
			aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,
			a.user_gokei) ||
			TRIM(TO_CHAR(ABS(a.user_gokei),'000000009')) ||			-- 金額3
			/* 20060206 項目名にそれぞれの集計の名称を固定値で設定するよう変更 */
--			RPAD(' ',20) || 										-- 備考
			RPAD('ＩＤ合計',20,'　') || 							-- 備考
			RPAD(' ',13), 											-- 予備
			dSysDate,
			vUser,
			aoba_const.CST_NULL,
			aoba_const.CST_NULL
	FROM 	(SELECT 	a2.kokyaku_cd,
						a2.kakin_nengetu,
						a2.oem_cd,
						a2.id,
--						a2.print_id,
						MIN(a2.print_id) AS print_id,			-- 20060421 成毛
						a1.koza_bng_hyoji_flg,
						SUM(a2.user_gokei) AS user_gokei
			FROM		wk_bl_seikyu_sum a1,
						/* 20060216 伝票単位のレコードに絞込み */
--						tb_shukka_meisai a2,
						(
						SELECT DISTINCT	kokyaku_cd,
										kakin_nengetu,
										oem_cd,
										id,
										print_id,
										user_gokei,
										seikyu_gun,
										srv_type_cd,
										data_kbn_cd,
										uriage_bng
						FROM	tb_shukka_meisai
						) a2,
						tb_srv_type_mst  a3,
						tb_data_kbn_mst  a4,
						wk_bl_kokyaku_info a5,						-- 20060410
						tb_kokyaku_id_joho a6						-- 20060509
			WHERE		a1.data_type = CONST_DATA_TYPE_01
			AND			a1.kokyaku_cd = a5.kokyaku_cd				-- 20060410
			AND			a1.seikyu_nengetu = a5.seikyu_nengetu		-- 20060410
			AND			a5.modify_flg = 'N'							-- 20060410
			AND			a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_11
			AND			a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_13
			AND			a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_14
			/* 2005/12/13 不要条件により削除 */
--			AND			a1.koza_bng_hyoji_flg IS NOT NULL
			AND			a1.kakin_nengetu = a2.kakin_nengetu
			AND			a1.seikyu_gun = a2.seikyu_gun
			AND			a1.kokyaku_cd = a2.kokyaku_cd
			AND			a2.srv_type_cd = a3.srv_type_cd
			AND			a3.meisai_type = CONST_MEISAI_SHUKKA
			AND			a2.data_kbn_cd = a4.data_kbn_cd
			/* 顧客別ID情報JOIN */		-- 20060509 ADD
			AND			a2.srv_type_cd = a6.srv_type_cd
			AND			a2.id = a6.id
			AND			a6.meisai_tel_hyoji_flg is not null
			GROUP BY	a2.kokyaku_cd,
						a2.kakin_nengetu,
						a2.oem_cd,
						a2.id,
--						a2.print_id,			-- 20060421
						a1.koza_bng_hyoji_flg) a
	UNION
	/* 顧客小計 */
	/* OEMコードをグループに追加 */
	SELECT	CONST_MEISAI_SHUKKA,
			CONST_PATTERN_MEI,
			CONST_DATA_KBN_08,
			a.kokyaku_cd,
--			'ZZZZZZ',
			a.seikyu_nengetu,
			'ZZZZZZZZZZZZZZZZZZZZ',
			'ZZZZZZZZZZ',
			'ZZZZZZZZZZ',
			'00',
			a.kokyaku_cd || 										-- 顧客コード
--			RPAD(' ',6) ||											-- 課金年月
			a.seikyu_nengetu ||										-- 請求年月
			'000000000' || 											-- 通番
			CONST_PATTERN_5 || 										-- パターン
			/* 20060206 OEMコードを設定するよう変更 */
--			RPAD(' ',4) || 											-- グループ
			nvl(a.oem_cd,'    ') || 								-- グループ
			CONST_DATA_KBN_8 ||										-- データ区分
			RPAD(' ',20) || 										-- 電話番号
			RPAD('　',14,'　') ||									-- 自由使用欄1
			RPAD(' ',20) || 										-- 自由使用欄2
			RPAD(' ',10) || 										-- 発注日
			RPAD(' ',10) || 										-- 出荷日
			RPAD(' ',10) || 										-- 商品コード
			RPAD('　',80,'　') ||									-- 商品名
			aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,0) ||
			'000000000' || 											-- 金額1
			aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,0) ||
			'000000000' || 											-- 金額2
			aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,
				a.user_shokei) ||
			TRIM(TO_CHAR(ABS(a.user_shokei),'000000009')) ||		-- 金額3
			/* 20060206 項目名にそれぞれの集計の名称を固定値で設定するよう変更 */
--			RPAD(' ',20) || 										-- 備考
			RPAD('小計',20,'　') || 								-- 備考
			RPAD(' ',13), 											-- 予備
			dSysDate,
			vUser,
			aoba_const.CST_NULL,
			aoba_const.CST_NULL
			/* 20060208 請求年月を取得するように変更 */
	FROM 	(SELECT 	a2.kokyaku_cd,
						a1.seikyu_nengetu,
						a2.oem_cd,
						SUM(a2.user_shokei) AS user_shokei
			FROM		wk_bl_seikyu_sum a1,
						/* 20060216 伝票単位のレコードに絞込み */
--						tb_shukka_meisai a2,
						(
						SELECT DISTINCT	kokyaku_cd,
										kakin_nengetu,
										oem_cd,
										id,
										print_id,
										user_shokei,
										seikyu_gun,
										srv_type_cd,
										data_kbn_cd,
										uriage_bng
						FROM	tb_shukka_meisai
						) a2,
						tb_srv_type_mst a3,
						tb_data_kbn_mst a4,
						wk_bl_kokyaku_info a5,						-- 20060410
						tb_kokyaku_id_joho a6						-- 20060509
			WHERE		a1.data_type = CONST_DATA_TYPE_01
			AND			a1.kokyaku_cd = a5.kokyaku_cd				-- 20060410
			AND			a1.seikyu_nengetu = a5.seikyu_nengetu		-- 20060410
			AND			a5.modify_flg = 'N'							-- 20060410
			AND			a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_11
			AND			a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_13
			AND			a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_14
			/* 2005/12/13 不要条件により削除 */
--			AND			a1.koza_bng_hyoji_flg IS NOT NULL
			AND			a1.kakin_nengetu = a2.kakin_nengetu
			AND			a1.seikyu_gun = a2.seikyu_gun
			AND			a1.kokyaku_cd = a2.kokyaku_cd
			AND			a2.srv_type_cd = a3.srv_type_cd
			AND			a3.meisai_type = CONST_MEISAI_SHUKKA
			AND			a2.data_kbn_cd = a4.data_kbn_cd
			/* 顧客別ID情報JOIN */		-- 20060509 ADD
			AND			a2.srv_type_cd = a6.srv_type_cd
			AND			a2.id = a6.id
			AND			a6.meisai_tel_hyoji_flg is not null
--			GROUP BY	a2.kokyaku_cd,a2.oem_cd) a
			GROUP BY	a2.kokyaku_cd,
						a1.seikyu_nengetu,
						a2.oem_cd) a
	UNION
	/* 顧客消費税 */
	/* 20060206 OEMコードをグループに追加 */
	/* 20060208 請求年月を取得するように変更 */
	SELECT	CONST_MEISAI_SHUKKA,
			CONST_PATTERN_MEI,
			CONST_DATA_KBN_09,
			a.kokyaku_cd,
--			'ZZZZZZ',
			a.seikyu_nengetu,
			'ZZZZZZZZZZZZZZZZZZZZ',
			'ZZZZZZZZZZ',
			'ZZZZZZZZZZ',
			'00',
			a.kokyaku_cd || 										-- 顧客コード
--			RPAD(' ',6) ||											-- 課金年月
			a.seikyu_nengetu ||										-- 請求年月
			'000000000' || 											-- 通番
			CONST_PATTERN_5 ||										-- パターン
--			RPAD(' ',4) || 											-- グループ
			nvl(a.oem_cd,'    ') || 								-- グループ
			CONST_DATA_KBN_9 ||										-- データ区分
			RPAD(' ',20) ||											-- 電話番号
			RPAD('　',14,'　') ||									-- 自由使用欄1
			RPAD(' ',20) ||											-- 自由使用欄2
			RPAD(' ',10) ||											-- 発注日
			RPAD(' ',10) ||											-- 出荷日
			RPAD(' ',10) ||											-- 商品コード
			RPAD('　',80,'　') ||									-- 商品名
			aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,0) ||
			'000000000' ||											-- 金額1
			aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,0) ||
			'000000000' ||											-- 金額2
			aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,
				a.user_tax) ||
			TRIM(TO_CHAR(ABS(a.user_tax),'000000009')) ||			-- 金額3
			/* 20060206 項目名にそれぞれの集計の名称を固定値で設定するよう変更 */
--			RPAD(' ',20) ||											-- 備考
			RPAD('消費税',20,'　') ||								-- 備考
			RPAD(' ',13), 											-- 予備
			dSysDate,
			vUser,
			aoba_const.CST_NULL,
			aoba_const.CST_NULL
			/* 20060208 請求年月を取得するように変更 */
	FROM 	(SELECT 	a2.kokyaku_cd,
						a1.seikyu_nengetu,
						a2.oem_cd,
						SUM(a2.user_tax) AS user_tax
			FROM		wk_bl_seikyu_sum a1,
						/* 20060216 伝票単位のレコードに絞込み */
--						tb_shukka_meisai a2,
						(
						SELECT DISTINCT	kokyaku_cd,
										kakin_nengetu,
										oem_cd,
										id,
										print_id,
										user_tax,
										seikyu_gun,
										srv_type_cd,
										data_kbn_cd,
										uriage_bng
						FROM	tb_shukka_meisai
						) a2,
						tb_srv_type_mst  a3,
						tb_data_kbn_mst  a4,
						wk_bl_kokyaku_info a5,						-- 20060410
						tb_kokyaku_id_joho a6						-- 20060509
			WHERE		a1.data_type = CONST_DATA_TYPE_01
			AND			a1.kokyaku_cd = a5.kokyaku_cd				-- 20060410
			AND			a1.seikyu_nengetu = a5.seikyu_nengetu		-- 20060410
			AND			a5.modify_flg = 'N'							-- 20060410
			AND			a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_11
			AND			a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_13
			AND			a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_14
			/* 2005/12/13 不要条件により削除 */
--			AND			a1.koza_bng_hyoji_flg IS NOT NULL
			AND			a1.kakin_nengetu = a2.kakin_nengetu
			AND			a1.seikyu_gun = a2.seikyu_gun
			AND			a1.kokyaku_cd = a2.kokyaku_cd
			AND			a2.srv_type_cd = a3.srv_type_cd
			AND			a3.meisai_type = CONST_MEISAI_SHUKKA
			AND			a2.data_kbn_cd = a4.data_kbn_cd
			/* 顧客別ID情報JOIN */		-- 20060509 ADD
			AND			a2.srv_type_cd = a6.srv_type_cd
			AND			a2.id = a6.id
			AND			a6.meisai_tel_hyoji_flg is not null
--			GROUP BY	a2.kokyaku_cd,a2.oem_cd) a
			GROUP BY	a2.kokyaku_cd,
						a1.seikyu_nengetu,
						a2.oem_cd) a
	UNION
	/* 顧客合計 */
	/* 20060206 OEMコードをグループに追加 */
	SELECT	CONST_MEISAI_SHUKKA,
			CONST_PATTERN_MEI,
			CONST_DATA_KBN_0A,
			a.kokyaku_cd,
			/* 20060208 請求年月をINSERTするように変更 */
--			'ZZZZZZ',
			a.seikyu_nengetu,
			'ZZZZZZZZZZZZZZZZZZZZ',
			'ZZZZZZZZZZ',
			'ZZZZZZZZZZ',
			'00',
			a.kokyaku_cd || 											-- 顧客コード
--			RPAD(' ',6) ||												-- 課金年月
			a.seikyu_nengetu ||											-- 請求年月
			'000000000' ||												-- 通番
			CONST_PATTERN_5 || 											-- パターン
			/* OEMコードを設定するように変更 */
--			RPAD(' ',4) ||												-- グループ
			nvl(a.oem_cd,'    ') ||										-- グループ
			CONST_DATA_KBN_A || 										-- データ区分
			RPAD(' ',20) ||												-- 電話番号
			RPAD('　',14,'　') ||										-- 自由使用欄1
			RPAD(' ',20) ||												-- 自由使用欄2
			RPAD(' ',10) ||												-- 発注日
			RPAD(' ',10) ||												-- 出荷日
			RPAD(' ',10) ||												-- 商品コード
			RPAD('　',80,'　') ||										-- 商品名
			aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,0) ||
			'000000000' ||												-- 金額1
			aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,0) ||
			'000000000' ||												-- 金額2
			aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,
				a.user_gokei) ||
			TRIM(TO_CHAR(ABS(a.user_gokei),'000000009')) ||				-- 金額3
			/* 20060206 項目名にそれぞれの集計の名称を固定値で設定するよう変更 */
--			RPAD(' ',20) ||												-- 備考
			RPAD('総合計',20,'　') ||									-- 備考
			RPAD(' ',13),												-- 予備
			dSysDate,
			vUser,
			aoba_const.CST_NULL,
			aoba_const.CST_NULL
			/* 20060208 請求年月を取得するように変更 */
	FROM 	(SELECT 	a2.kokyaku_cd,
						a1.seikyu_nengetu,
						a2.oem_cd,
						SUM(a2.user_gokei) AS user_gokei
			FROM		wk_bl_seikyu_sum a1,
						/* 20060216 伝票単位のレコードに絞込み */
--						tb_shukka_meisai a2,
						(
						SELECT DISTINCT	kokyaku_cd,
										kakin_nengetu,
										oem_cd,
										id,
										print_id,
										user_gokei,
										seikyu_gun,
										srv_type_cd,
										data_kbn_cd,
										uriage_bng
						FROM	tb_shukka_meisai
						) a2,
						tb_srv_type_mst a3,
						tb_data_kbn_mst a4,
						wk_bl_kokyaku_info a5,						-- 20060410
						tb_kokyaku_id_joho a6						-- 20060509
			WHERE		a1.data_type = CONST_DATA_TYPE_01
			AND			a1.kokyaku_cd = a5.kokyaku_cd				-- 20060410
			AND			a1.seikyu_nengetu = a5.seikyu_nengetu		-- 20060410
			AND			a5.modify_flg = 'N'							-- 20060410
			AND			a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_11
			AND			a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_13
			AND			a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_14
			/* 2005/12/13 不要条件により削除 */
--			AND			a1.koza_bng_hyoji_flg IS NOT NULL
			AND			a1.kakin_nengetu = a2.kakin_nengetu
			AND			a1.seikyu_gun = a2.seikyu_gun
			AND			a1.kokyaku_cd = a2.kokyaku_cd
			AND			a2.srv_type_cd = a3.srv_type_cd
			AND			a3.meisai_type = CONST_MEISAI_SHUKKA
			AND			a2.data_kbn_cd = a4.data_kbn_cd
			/* 顧客別ID情報JOIN */		-- 20060509 ADD
			AND			a2.srv_type_cd = a6.srv_type_cd
			AND			a2.id = a6.id
			AND			a6.meisai_tel_hyoji_flg is not null
--			GROUP BY	a2.kokyaku_cd,a2.oem_cd) a
			GROUP BY	a2.kokyaku_cd,
						a1.seikyu_nengetu,
						a2.oem_cd) a
	;
	/* 請求書出荷明細一時TBL挿入（エンドレコード） */
	INSERT INTO	wk_bl_shukka_meisai(
			meisai_type, 								-- 請求書明細種別
			ptn_sk, 									-- パターンソートキー
			data_kbn_sk, 								-- データ区分ソートキー
			kokyaku_cd_sk,								-- 顧客コードソートキー
			kakin_nengetu_sk,							-- 課金年月ソートキー
			id_sk,										-- IDソートキー
			order_bng_sk,								-- 注文番号ソートキー
			shukka_date_sk,								-- 注文日ソートキー
			sk_yobi,									-- ソートキー予備
			kaisen_meisai_data,							-- 請求書回線明細データ
			insert_date,								-- 登録日時
			insert_user,								-- 登録ユーザ
			update_date,								-- 更新日時
			update_user)								-- 更新ユーザ
	SELECT	CONST_MEISAI_SHUKKA,
			CONST_PATTERN_END,
			CONST_DATA_KBN_ZZ,
			a.kokyaku_cd,
			/* 20060208 請求年月に変更 */
--			a.kakin_nengetu,
			a.seikyu_nengetu,
			'ZZZZZZZZZZZZZZZZZZZZ',
			'ZZZZZZZZZZ',
			'ZZZZZZZZZZ',
			'00',
--			a.kokyaku_cd || a.kakin_nengetu ||
			a.kokyaku_cd || a.seikyu_nengetu ||
			'000000000' ||
			CONST_PATTERN_9 || a.oem_cd ||
			RPAD('　',200,'　') ||
			RPAD(' ',28),
			dSysDate,
			vUser,
			aoba_const.CST_NULL,
			aoba_const.CST_NULL
	FROM 	(SELECT DISTINCT a2.kokyaku_cd 	AS kokyaku_cd,
							/* 20060208 請求年月に変更 */
--							a2.kakin_nengetu AS kakin_nengetu,
							a1.seikyu_nengetu AS seikyu_nengetu,
							a2.oem_cd 		AS oem_cd
			FROM	wk_bl_seikyu_sum a1,
					tb_shukka_meisai a2,
					tb_srv_type_mst  a3,
					tb_data_kbn_mst  a4,
					wk_bl_kokyaku_info a5,							-- 20060410
					tb_kokyaku_id_joho a6							-- 20060509
			WHERE	a1.data_type = CONST_DATA_TYPE_01
			AND		a1.kokyaku_cd = a5.kokyaku_cd				-- 20060410
			AND		a1.seikyu_nengetu = a5.seikyu_nengetu		-- 20060410
			AND		a5.modify_flg = 'N'							-- 20060410
			AND		a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_11
			AND		a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_13
			AND		a1.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_14
			AND		a1.kakin_nengetu = a2.kakin_nengetu
			AND		a1.seikyu_gun = a2.seikyu_gun
			AND		a1.kokyaku_cd = a2.kokyaku_cd
			AND		a2.srv_type_cd = a3.srv_type_cd
			AND     a3.meisai_type = CONST_MEISAI_SHUKKA
			AND     a2.data_kbn_cd = a4.data_kbn_cd
			/* 顧客別ID情報JOIN */		-- 20060509 ADD
			AND		a2.srv_type_cd = a6.srv_type_cd
			AND		a2.id = a6.id
			AND		a6.meisai_tel_hyoji_flg is not null) a;

	/* 請求書出荷明細一時TBL連番更新 */
	/* 1/25 UPDATE変更 */
	/*UPDATE wk_bl_shukka_meisai a
	SET kaisen_meisai_data
		=
		(
		SELECT SUBSTRB(b.kaisen_meisai_data,1,14) ||
				TRIM(TO_CHAR(rec_no,'000000009')) ||
				SUBSTRB(b.kaisen_meisai_data,24)
		FROM 	(SELECT kaisen_meisai_data kaisen_meisai_data,
						ptn_sk ptn_sk,
						data_kbn_sk data_kbn_sk,
						kokyaku_cd_sk kokyaku_cd_sk,
						kakin_nengetu_sk kakin_nengetu_sk,
						id_sk id_sk,
						order_bng_sk order_bng_sk,
						shukka_date_sk shukka_date_sk,
						row_number() over
						(order by ptn_sk,
						data_kbn_sk,
						kokyaku_cd_sk,
						kakin_nengetu_sk,
						id_sk,
						order_bng_sk,
						shukka_date_sk) rec_no
				FROM wk_bl_shukka_meisai
				WHERE meisai_type = CONST_MEISAI_SHUKKA) b

	WHERE
		a.meisai_type = CONST_MEISAI_SHUKKA
			AND 	a.ptn_sk = b.ptn_sk
			AND 	a.data_kbn_sk = b.data_kbn_sk
			AND 	a.kokyaku_cd_sk = b.kokyaku_cd_sk
			AND 	a.kakin_nengetu_sk = b.kakin_nengetu_sk
			AND 	a.id_sk = b.id_sk
			AND 	a.order_bng_sk = b.order_bng_sk
			AND 	a.shukka_date_sk = b.shukka_date_sk)
	;*/
 	/* シーケンス初期化 */
 	nSeqno := 1;
 	FOR rec_shukka_meisai IN cur_shukka_meisai LOOP
 		UPDATE	wk_bl_shukka_meisai
 		SET		kaisen_meisai_data = rec_shukka_meisai.meisai_a ||
 									TRIM(TO_CHAR(nSeqno,'000000009')) ||
 									rec_shukka_meisai.meisai_b
 		WHERE CURRENT OF cur_shukka_meisai;
 		/* カウンタアップ */
 		nSeqno := nSeqno + 1;
 	END LOOP;
END FAOB010106;
/
