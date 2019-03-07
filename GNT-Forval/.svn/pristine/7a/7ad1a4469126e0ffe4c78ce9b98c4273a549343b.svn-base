CREATE OR REPLACE PROCEDURE        FAOB010103
/********************************************************************/
/*  関数名：FAOB0101												*/
/*  機能名：請求書データ作成：請求書顧客マスタ編集					*/
/*  作成者：山田													*/
/*  作成日：2005/11/07												*/
/*  更新日：2006/02/28  田中  まとめ以外は、年月1,空白,金額1,0埋め	*/
/*        ：2006/03/22  田中  変更ﾌﾗｸﾞのINSERT処理追加				*/
/*        ：2006/06/08  田中  同封チラシコードの取得先変更			*/
/*        ：2006/06/30  田中  口座番号表示フラグ値増加対応			*/
/*        ：2006/07/05  田中  口座番号表示フラグ値修正				*/
/*        ：2006/07/07  田中  領収書用紙タイプ洗い換え処理対応		*/
/*        ：2006/09/19  田中  請求額負数対応						*/
/*  VER   ：1.07													*/
/*------------------------------------------------------------------*/
/*	引数：		無し												*/
/*	戻値：		無し												*/
/********************************************************************/
IS
	/* 1/27 全般的にnvl関数を仕様するように変更 */
	/* ローカル定数 */
	CONST_DATA_TYPE_01	CONSTANT wk_bl_seikyu_sum.data_type%TYPE 	:= '01';
	CONST_DATA_TYPE_02	CONSTANT wk_bl_seikyu_sum.data_type%TYPE 	:= '02';
	CONST_DATA_TYPE_03	CONSTANT wk_bl_seikyu_sum.data_type%TYPE 	:= '03';
	CST_DEL_TABLE_1		CONSTANT VARCHAR2(30) := 'WK_BL_KOKYAKU';		--削除対象テーブル名
	CST_DEL_TABLE_2		CONSTANT VARCHAR2(30) := 'WK_BL_SEIKYU_INFO';	--削除対象テーブル名
	CST_DEL_TABLE_3		CONSTANT VARCHAR2(30) := 'WK_BL_KOKYAKU_INFO';	--削除対象テーブル名
	CST_FT_SRV_CD		CONSTANT VARCHAR2(10) := '1011';				--FTフォンサービスコード

	/* ローカル変数 */
	nRet					NUMBER;										--リターンコード
	dSysDate				wk_bl_kokyaku.insert_date%TYPE;				--システム日付
	vUser					wk_bl_kokyaku.insert_user%TYPE;				--ユーザ
	nRec 					NUMBER;
	nCount 					NUMBER;
	nWk_kingaku 			NUMBER;
	/* 20060919 add */
	nWk_sign				NUMBER;
	cWk_seikyu 				CHAR(6);
	cKokyaku 				CHAR(8);
	cSeikyu 				CHAR(6);
	cWk_kokyaku 			CHAR(8);
	cWk_Kakin 				CHAR(6);

	/* カーソル定義 */
	CURSOR cur_1
	IS
		SELECT	a.kakin_nengetu		AS kakin_nengetu,
					a.seikyu_gun 		AS seikyu_gun,
					a.kokyaku_cd 		AS kokyaku_cd,
					a.matome_flg 		AS matome_flg,
					b.seikyugaku 		AS seikyugaku,
					b.nyukingaku 		AS nyukingaku,
					b.kazei_taishogaku	AS kazei_taishogaku,
					b.hikazei_taishogaku	AS hikazei_taishogaku,
					b.daiko_seikyugaku	AS daiko_seikyugaku,
					b.tax 				AS tax
		FROM		wk_bl_seikyu_sum a,
					wk_matome_seikyu_sum b
		WHERE		a.matome_flg 		= aoba_out_const.CST_OUT_MATOME_FLG_Y
		AND		a.kakin_nengetu		= b.kakin_nengetu(+)
		AND		a.seikyu_gun			= b.seikyu_gun(+)
		AND		a.kokyaku_cd		= b.kokyaku_cd(+)
		ORDER BY 	a.kokyaku_cd,
					a.seikyu_nengetu,
					a.kakin_nengetu;

	rec_1 cur_1%ROWTYPE;

	CURSOR cur_wk1
	IS
		SELECT	kokyaku_cd,
					seikyu_nengetu, 							-- 請求年月
					zip, 										-- 郵便番号
					addr1, 										-- 住所１
					addr2, 										-- 住所２
					kokyaku_name, 								-- 顧客名
					kokyaku_tanto,							 	-- 顧客担当者
					kokyaku_tel, 								-- 顧客電話番号
					ryoshusho_no, 								-- 領収書発行No
					nyukingaku_sign, 							-- 入金額サイン
					nyukingaku, 								-- 入金額
					nyukin_date, 								-- 入金年月日
					bank_name, 									-- 銀行名
					shiten_name, 								-- 支店名
					koza_type_cd, 								-- 口座種別
					koza_bng, 									-- 口座No
					koza_meigi, 								-- 口座名義
					nengetu1, 									-- 年月１
					nyukingaku_sign1, 							-- 入金額１サイン
					nyukingaku1, 								-- 入金額１
					nengetu2, 									-- 年月２
					nyukingaku_sign2, 							-- 入金額２サイン
					nyukingaku2, 								-- 入金額２
					nengetu3, 									-- 年月３
					nyukingaku_sign3, 							-- 入金額３サイン
					nyukingaku3, 								-- 入金額３
					print_type_cd								-- 用紙タイプ	-- 20060707
		FROM		wk_bl_ryoshu_info
		ORDER BY 	kokyaku_cd,
					seikyu_nengetu
					DESC;

	rec_wk1 cur_wk1%ROWTYPE;

BEGIN

	/* 請求書顧客マスタ一時TBLの削除 */
	EXECUTE IMMEDIATE 'TRUNCATE TABLE ' || CST_DEL_TABLE_1;

	/* 請求情報格納一時TBLの削除 */
	EXECUTE IMMEDIATE 'TRUNCATE TABLE ' || CST_DEL_TABLE_2;

	/* 2005/12/19 INSERTテーブル追加により、削除処理追加 */
	/* 請求書顧客マスタ格納一時TBLの削除 */
	EXECUTE IMMEDIATE 'TRUNCATE TABLE ' || CST_DEL_TABLE_3;

	/* システム日時の取得 */
	nRet := aoba_common.get_date( dSysDate );

	/* ユーザの取得 */
	nRet := aoba_common.get_user( vUser );

	/* 請求情報格納一時TBL挿入（当月請求まとめなし） */
	INSERT INTO	wk_bl_seikyu_info(
			kokyaku_cd,											-- 顧客コード
			seikyu_nengetu, 									-- 請求年月
			zip,												-- 郵便番号
			addr1,												-- 住所１
			addr2,												-- 住所２
			kokyaku_name,										-- 顧客名
			kokyaku_tanto,										-- 顧客担当者
			kokyaku_tel,										-- 顧客電話番号
			seikyusho_no,										-- 請求書発行No
			seikyusho_hakko_date,								-- 請求書発行日
			nyukin_date,										-- 入金期日
			seikyugaku_sign,									-- 請求金額サイン
			seikyugaku,											-- 請求金額
			kazei_taishogaku_sign,								-- 課税対象額サイン
			kazei_taishogaku,									-- 課税対象額
			hikazei_taishogaku_sign, 							-- 非課税対象額サイン
			hikazei_taishogaku,									-- 非課税対象額
			daiko_seikyugaku_sign,								-- 代行請求額サイン
			daiko_seikyugaku, 									-- 代行請求額
			tax_sign, 											-- 消費税サイン
			tax, 												-- 消費税
			nengetu1, 											-- 年月１
			seikyugaku_sign1, 									-- 請求額１サイン
			seikyugaku1, 										-- 請求額１
			nengetu2, 											-- 年月２
			seikyugaku_sign2, 									-- 請求額２サイン
			seikyugaku2, 										-- 請求額２
			nengetu3, 											-- 年月３
			seikyugaku_sign3, 									-- 請求額３サイン
			seikyugaku3, 										-- 請求額３
			bar_cd_jan1, 										-- バーコード1
			bar_cd_jan2, 										-- バーコード2
			bar_cd_jan3, 										-- バーコード3
			bar_cd_jan4, 										-- バーコード4
			bar_cd_ean128, 										-- バーコードEAN128
			card_bng, 											-- カードNo
			saihakko_flg, 										-- 再発行フラグ
			/* 20060210 登録項目追加 */
			ftphone_flg)
	SELECT	a.kokyaku_cd,
				a.seikyu_nengetu, 								-- 請求年月
				a.zip, 											-- 郵便番号
				RPAD(SUBSTRB(a.seikyu_saki_addr1 || a.seikyu_saki_addr2 ||
					a.seikyu_saki_addr3 || a.seikyu_saki_addr4,1,50),50,'　'),
				CASE WHEN LENGTHB(a.seikyu_saki_addr1 || a.seikyu_saki_addr2 ||
							a.seikyu_saki_addr3 || a.seikyu_saki_addr4)
							> 50 THEN
					RPAD(SUBSTRB(a.seikyu_saki_addr1 || a.seikyu_saki_addr2 ||
						a.seikyu_saki_addr3 || a.seikyu_saki_addr4,51,50),50,'　')
				ELSE
					RPAD('　',50,'　')
				END , 																-- 住所１
				RPAD(a.seikyu_saki_name,80,'　'), 									-- 顧客名
				RPAD(a.kokyaku_tantosha,80,'　'),									-- 顧客担当者
				SUBSTR(a.kokyaku_tel,1,13), 										-- 顧客電話番号
				SUBSTR(a.seikyu_nengetu,3,4) || a.seikyu_gun ||
					'-' || a.kokyaku_cd, 											-- 請求書発行No
				SUBSTR(RPAD(nvl(b.seikyusho_hakko_date,' '),8),1,4) || '.' ||
					SUBSTR(RPAD(nvl(b.seikyusho_hakko_date,' '),8),5,2) || '.' ||
					SUBSTR(RPAD(nvl(b.seikyusho_hakko_date,' '),8),7,2) , 			-- 請求書発行日
				SUBSTR(RPAD(nvl(b.shuno_kigen,'    '),8),1,4) || '.' ||
					SUBSTR(RPAD(nvl(b.shuno_kigen,'    '),8),5,2) || '.' ||
					SUBSTR(RPAD(nvl(b.shuno_kigen,'    '),8),7,2), 					-- 入金期日
				aoba_out_common.get_mark(	aoba_out_const.CST_OUT_FAOB010100,
											a.seikyugaku),							-- 請求金額サイン
				TO_NUMBER(SUBSTR(TO_CHAR(a.seikyugaku,'0000000000'),3,9)), 			-- 請求金額
					aoba_out_common.get_mark(	aoba_out_const.CST_OUT_FAOB010100,
												a.kazei_taishogaku),				-- 課税対象額サイン
				TO_NUMBER(SUBSTR(TO_CHAR(a.kazei_taishogaku,'0000000000'),3,9)),
					aoba_out_common.get_mark(	aoba_out_const.CST_OUT_FAOB010100,
												a.hikazei_taishogaku),				-- 非課税対象額サイン
				TO_NUMBER(SUBSTR(TO_CHAR(a.hikazei_taishogaku,'0000000000'),3,9)),
					aoba_out_common.get_mark(	aoba_out_const.CST_OUT_FAOB010100,
												a.daiko_seikyugaku), 				-- 代行請求額サイン
				TO_NUMBER(SUBSTR(TO_CHAR(a.daiko_seikyugaku,'0000000000'),3,9)),
					aoba_out_common.get_mark(	aoba_out_const.CST_OUT_FAOB010100,
												a.tax), 							-- 消費税サイン
				TO_NUMBER(SUBSTR(TO_CHAR(a.tax,'0000000000'),3,9)),
				RPAD(' ',7), 														-- 年月１
				aoba_out_common.get_mark(	aoba_out_const.CST_OUT_FAOB010100,0),	-- 請求額１サイン
				aoba_const.CST_NUM_ZERO,											-- 請求額１
				RPAD(' ',7),														-- 年月２
				aoba_out_common.get_mark(	aoba_out_const.CST_OUT_FAOB010100,0), 	-- 請求額2サイン
				aoba_const.CST_NUM_ZERO,											-- 請求額２
				RPAD(' ',7),														-- 年月３
				aoba_out_common.get_mark(	aoba_out_const.CST_OUT_FAOB010100,0), 	-- 請求額3サイン
				aoba_const.CST_NUM_ZERO,											-- 請求額３
				SUBSTR(a.bar_cd_jan1,1,8),											-- バーコード1
				SUBSTR(a.bar_cd_jan2,1,13),											-- バーコード2
				SUBSTR(a.bar_cd_jan3,1,13),											-- バーコード3
				SUBSTR(a.bar_cd_jan4,1,13),											-- バーコード4
				a.bar_cd_ean128,													-- バーコードEAN128
				a.card_bng, 														-- カードNo
				' ', 																-- 再発行フラグ
				DECODE(c.kokyaku_cd,'',' ','Y')										-- FTPHONEフラグ
	FROM 	wk_bl_seikyu_sum a,
			wk_seikyu_hakko_kokyaku b,
			/* 20060210 FTPHONEフラグ対応 */
			(SELECT kokyaku_cd,
					kakin_nengetu,
					seikyu_gun
			FROM	tb_seikyu_sum_srv
			WHERE	srv_type_cd = CST_FT_SRV_CD
			) c
	WHERE 	a.matome_flg	<> aoba_out_const.CST_OUT_MATOME_FLG_Y
	AND 	a.kokyaku_cd	= b.kokyaku_cd
	AND 	a.kakin_nengetu = b.kakin_nengetu
	AND 	a.seikyu_gun	= b.seikyu_gun
	AND		a.kokyaku_cd = c.kokyaku_cd (+)
	AND		a.kakin_nengetu = c.kakin_nengetu(+)
	AND		a.seikyu_gun = c.seikyu_gun (+);

	/* 請求情報格納一時TBL挿入（当月請求まとめあり） */
	INSERT INTO	wk_bl_seikyu_info(
			kokyaku_cd,								-- 顧客コード
			seikyu_nengetu, 						-- 請求年月
			zip, 									-- 郵便番号
			addr1, 									-- 住所１
			addr2, 									-- 住所２
			kokyaku_name, 							-- 顧客名
			kokyaku_tanto, 							-- 顧客担当者
			kokyaku_tel, 							-- 顧客電話番号
			seikyusho_no, 							-- 請求書発行No
			seikyusho_hakko_date, 					-- 請求書発行日
			nyukin_date, 							-- 入金期日
			seikyugaku_sign, 						-- 請求金額サイン
			seikyugaku, 							-- 請求金額
			kazei_taishogaku_sign, 					-- 課税対象額サイン
			kazei_taishogaku, 						-- 課税対象額
			hikazei_taishogaku_sign, 				-- 非課税対象額サイン
			hikazei_taishogaku, 					-- 非課税対象額
			daiko_seikyugaku_sign, 					-- 代行請求額サイン
			daiko_seikyugaku, 						-- 代行請求額
			tax_sign, 								-- 消費税サイン
			tax, 									-- 消費税
			nengetu1, 								-- 年月１
			seikyugaku_sign1, 						-- 請求額１サイン
			seikyugaku1, 							-- 請求額１
			nengetu2, 								-- 年月２
			seikyugaku_sign2, 						-- 請求額２サイン
			seikyugaku2, 							-- 請求額２
			nengetu3, 								-- 年月３
			seikyugaku_sign3, 						-- 請求額３サイン
			seikyugaku3, 							-- 請求額３
			bar_cd_jan1, 							-- バーコード1
			bar_cd_jan2, 							-- バーコード2
			bar_cd_jan3, 							-- バーコード3
			bar_cd_jan4, 							-- バーコード4
			bar_cd_ean128, 							-- バーコードEAN128
			card_bng, 								-- カードNo
			saihakko_flg, 							-- 再発行フラグ
			ftphone_flg)
	SELECT	a_kokyaku_cd,
				a_seikyu_nengetu, 						-- 請求年月
				a_zip, 									-- 郵便番号
				RPAD(SUBSTRB(a_seikyu_saki_addr1 || a_seikyu_saki_addr2 ||
					a_seikyu_saki_addr3 || a_seikyu_saki_addr4,1,50),50,'　'), -- 住所１
				CASE WHEN LENGTHB(a_seikyu_saki_addr1 || a_seikyu_saki_addr2 ||
					a_seikyu_saki_addr3 || a_seikyu_saki_addr4)
							> 50 THEN
						RPAD(SUBSTRB(a_seikyu_saki_addr1 || a_seikyu_saki_addr2 ||
							a_seikyu_saki_addr3 || a_seikyu_saki_addr4,51,50),50,'　')
				ELSE
					RPAD('　',50,'　')
				END , -- 住所１
				RPAD(a_seikyu_saki_name,80,'　'), 										-- 顧客名
				RPAD(a_kokyaku_tantosha,80,'　'), 										-- 顧客担当者
				SUBSTR(a_kokyaku_tel,1,13), 											-- 顧客電話番号
				SUBSTR(a_seikyu_nengetu,3,4) || a_seikyu_gun ||
				'-' || a_kokyaku_cd, 													-- 請求書発行No
				SUBSTR(d_seikyusho_hakko_date,1,4) || '.' ||
				SUBSTR(d_seikyusho_hakko_date,5,2) || '.' ||
				SUBSTR(d_seikyusho_hakko_date,7,2) , 									-- 請求書発行日
				SUBSTR(d_shuno_kigen,1,4) || '.' ||
				SUBSTR(d_shuno_kigen,5,2) || '.' ||
				SUBSTR(d_shuno_kigen,7,2), 												-- 入金期日
				aoba_out_common.get_mark(	aoba_out_const.CST_OUT_FAOB010100,
											b_seikyugaku), 								-- 請求金額サイン
				TO_NUMBER(SUBSTR(TO_CHAR(b_seikyugaku,'0000000000'),3,9)),
					aoba_out_common.get_mark(	aoba_out_const.CST_OUT_FAOB010100,
												b_kazei_taishogaku), 					-- 課税対象額サイン
				TO_NUMBER(SUBSTR(TO_CHAR(b_kazei_taishogaku,'0000000000'),3,9)),
					aoba_out_common.get_mark(	aoba_out_const.CST_OUT_FAOB010100,
												b_hikazei_taishogaku), 					-- 非課税対象額サイン
				TO_NUMBER(SUBSTR(TO_CHAR(b_hikazei_taishogaku,'0000000000'),3,9)),
					aoba_out_common.get_mark(	aoba_out_const.CST_OUT_FAOB010100,
												b_daiko_seikyugaku), 					-- 代行請求額サイン
				TO_NUMBER(SUBSTR(TO_CHAR(b_daiko_seikyugaku,'0000000000'),3,9)),
					aoba_out_common.get_mark(	aoba_out_const.CST_OUT_FAOB010100,
												b_tax), 								-- 消費税サイン
				TO_NUMBER(SUBSTR(TO_CHAR(b_tax,'0000000000'),3,9)),
				SUBSTR(a_seikyu_nengetu,1,4) || '.' || SUBSTR(a_seikyu_nengetu,5,2),	-- 年月１
				aoba_out_common.get_mark(	aoba_out_const.CST_OUT_FAOB010100,
											b_seikyugaku), 								-- 請求金額サイン
				TO_NUMBER(SUBSTR(TO_CHAR(b_seikyugaku,'0000000000'),3,9)),				-- 請求額
				RPAD(' ',7),-- 年月２
				aoba_out_common.get_mark(	aoba_out_const.CST_OUT_FAOB010100,0),		-- 請求額2サイン
				aoba_const.CST_NUM_ZERO, 												-- 請求額２
				RPAD(' ',7), 															-- 年月３
				aoba_out_common.get_mark(	aoba_out_const.CST_OUT_FAOB010100,0),	 	-- 請求額3サイン
				aoba_const.CST_NUM_ZERO, 												-- 請求額３
				SUBSTR(a_bar_cd_jan1,1,8), 												-- バーコード1
				SUBSTR(a_bar_cd_jan2,1,13), 											-- バーコード2
				SUBSTR(a_bar_cd_jan3,1,13), 											-- バーコード3
				SUBSTR(a_bar_cd_jan4,1,13), 											-- バーコード4
				a_bar_cd_ean128, 														-- バーコードEAN128
				a_card_bng, 															-- カードNo
				' ', 																	-- 再発行フラグ
				ftphone_flg																-- FTPHONEフラグ
	FROM
		(SELECT 	a.data_type 		AS a_data_type,
					a.kakin_nengetu 	AS a_kakin_nengetu,
					a.seikyu_gun 		AS a_seikyu_gun,
					a.kokyaku_cd 		AS a_kokyaku_cd,
					a.oem_cd 			AS a_oem_cd,
					a.matome_flg 		AS a_matome_flg,
					a.seikyu_nengetu 	AS a_seikyu_nengetu,
					a.seikyugaku 		AS a_seikyugaku,
					a.nyukingaku 		AS a_nyukingaku,
					a.nyukin_date 		AS a_nyukin_date,
					a.kazei_taishogaku 	AS a_kazei_taishogaku,
					a.hikazei_taishogaku AS a_hikazei_taishogaku,
					a.daiko_seikyugaku 	AS a_daiko_seikyugaku,
					a.tax 				AS a_tax,
					a.seikyu_saki_name 	AS a_seikyu_saki_name,
					a.seikyu_saki_addr1 AS a_seikyu_saki_addr1,
					a.seikyu_saki_addr2 AS a_seikyu_saki_addr2,
					a.seikyu_saki_addr3 AS a_seikyu_saki_addr3,
					a.seikyu_saki_addr4 AS a_seikyu_saki_addr4,
					a.zip 				AS a_zip,
					a.kokyaku_tantosha 	AS a_kokyaku_tantosha,
					a.kokyaku_tel 		AS a_kokyaku_tel,
					a.shuno_kigen 		AS a_shuno_kigen,
					a.bank_cd 			AS a_bank_cd,
					a.bank_name 		AS a_bank_name,
					a.shiten_name 		AS a_shiten_name,
					a.koza_type_cd 		AS a_koza_type_cd,
					a.koza_bng 			AS a_koza_bng,
					a.koza_meigi 		AS a_koza_meigi,
					a.bar_cd_jan1 		AS a_bar_cd_jan1,
					a.bar_cd_jan2 		AS a_bar_cd_jan2,
					a.bar_cd_jan3 		AS a_bar_cd_jan3,
					a.bar_cd_jan4 		AS a_bar_cd_jan4,
					a.bar_cd_ean128 	AS a_bar_cd_ean128,
					a.card_bng 			AS a_card_bng,
					a.print_type_cd 	AS a_print_type_cd,
					a.dohu_tirashi_cd 	AS a_dohu_tirashi_cd,
					a.seikyusho_hakko_date AS a_seikyusho_hakko_date,
					b.kakin_nengetu 	AS b_kakin_nengetu,
					b.seikyu_gun 		AS b_seikyu_gun,
					b.kokyaku_cd 		AS b_kokyaku_cd,
					b.matome_flg 		AS b_matome_flg,
					b.seikyugaku 		AS b_seikyugaku,
					b.nyukingaku 		AS b_nyukingaku,
					b.kazei_taishogaku 	AS b_kazei_taishogaku,
					b.hikazei_taishogaku AS b_hikazei_taishogaku,
					b.daiko_seikyugaku 	AS b_daiko_seikyugaku,
					b.tax 				AS b_tax,
					nvl(c.seikyusaki_kaisha_name,'　') AS c_seikyusaki_kaisha_name,
					nvl(c.seikyusaki_zip,' ') 	AS c_seikyusaki_zip,
					nvl(c.seikyusaki_addr1,'　') 	AS c_seikyusaki_addr1,
					nvl(c.seikyusaki_addr2,'　') 	AS c_seikyusaki_addr2,
					nvl(c.seikyusaki_addr3,'　') 	AS c_seikyusaki_addr3,
					nvl(c.seikyusaki_addr4,'　') 	AS c_seikyusaki_addr4,
					nvl(c.seikyusaki_busho_name,'　') AS c_seikyusaki_busho_name,
					nvl(c.seikyusaki_tel,' ') 	AS c_seikyusaki_tel,
					c.koza_bng_hyoji_flg AS c_koza_bng_hyoji_flg,
					nvl(d.seikyusho_hakko_date,' ') AS d_seikyusho_hakko_date,
					nvl(d.shuno_kigen,' ') 		AS d_shuno_kigen,
					b.ftphone_flg AS ftphone_flg
		FROM 	wk_bl_seikyu_sum a,
			/* 定義１ */
			(SELECT	a1.kakin_nengetu 	AS kakin_nengetu,
					a1.seikyu_gun 		AS seikyu_gun,
					a1.kokyaku_cd 		AS kokyaku_cd,
					a1.matome_flg 		AS matome_flg,
					b1.seikyu_nengetu 	AS seikyu_nengetu,
					b1.seikyugaku 		AS seikyugaku,
					b1.nyukingaku 		AS nyukingaku,
					b1.kazei_taishogaku AS kazei_taishogaku,
					b1.hikazei_taishogaku AS hikazei_taishogaku,
					b1.daiko_seikyugaku AS daiko_seikyugaku,
					b1.tax 				AS tax,
					DECODE(c1.kokyaku_cd,'',' ','Y') AS ftphone_flg		-- FTPHONEフラグ
			FROM	wk_bl_seikyu_sum a1,
					wk_matome_seikyu_sum b1,
					/* 20060210 FTPHONEフラグ対応 */
					(SELECT 	kokyaku_cd,
								kakin_nengetu,
								seikyu_gun
					FROM	tb_seikyu_sum_srv
					WHERE	srv_type_cd = CST_FT_SRV_CD
					) c1
			WHERE	a1.matome_flg 		= aoba_out_const.CST_OUT_MATOME_FLG_Y
			AND		a1.kakin_nengetu     = b1.kakin_nengetu(+)
			AND		a1.seikyu_gun        = b1.seikyu_gun(+)
			AND		a1.kokyaku_cd        = b1.kokyaku_cd(+)
			AND		a1.kokyaku_cd = c1.kokyaku_cd (+)
			AND		a1.kakin_nengetu = c1.kakin_nengetu(+)
			AND		a1.seikyu_gun = c1.seikyu_gun (+)
			/* 1/26　インサート時にソートは不要 */
			--ORDER BY 	a1.kokyaku_cd,b1.seikyu_nengetu,a1.kakin_nengetu
			) b,
			tb_kokyaku_joho c,
			wk_seikyu_hakko_kokyaku d
		WHERE 	a.data_type 	= CONST_DATA_TYPE_01
		AND 	a.kokyaku_cd 	= b.kokyaku_cd
		AND 	a.kakin_nengetu = b.kakin_nengetu
		AND 	a.kokyaku_cd 	= c.kokyaku_cd(+)
		AND 	a.oem_cd 		= c.oem_cd(+)
		AND 	a.kakin_nengetu = d.kakin_nengetu
		AND 	a.seikyu_gun = d.seikyu_gun
		AND 	a.kokyaku_cd = d.kokyaku_cd
		AND 	b.seikyu_nengetu IS NOT NULL
--		ORDER BY 	a.kokyaku_cd,
--					a.seikyu_nengetu DESC,
--					a.kakin_nengetu
		) w;

	/* まとめ要素のデータ抽出（当月請求） */
	nRec 		:= 0;
	cKokyaku 	:= ' ';
	cSeikyu 	:= ' ';
	FOR rec_1 IN cur_1() LOOP
		IF 	rec_1.matome_flg = aoba_out_const.CST_OUT_MATOME_FLG_Y THEN
			SELECT 	COUNT(*) INTO nCount
			FROM wk_bl_seikyu_sum
			WHERE 	kokyaku_cd 		= rec_1.kokyaku_cd
			AND 	kakin_nengetu 	= rec_1.kakin_nengetu;
			IF nCount > 0 THEN
				SELECT 	kokyaku_cd,
						kakin_nengetu,
						/* 20060919 add */
						seikyugaku,
						TO_NUMBER(SUBSTR(TO_CHAR(seikyugaku,'0000000000'),3,9)), 		seikyu_nengetu
				INTO 	cWk_kokyaku,cWk_kakin,nWk_sign,nWk_kingaku,cWk_seikyu
				FROM 	wk_bl_seikyu_sum
				WHERE 	kokyaku_cd 		= rec_1.kokyaku_cd
				AND 	kakin_nengetu 	= rec_1.kakin_nengetu;
				IF cKokyaku <> cWk_kokyaku OR cSeikyu <> cWk_seikyu THEN
					nRec := 1;
				END IF;
				SELECT 	COUNT(*) INTO nCount
				FROM 	wk_bl_seikyu_info
				WHERE 	kokyaku_cd 		= cWk_kokyaku
				AND 	seikyu_nengetu 	= cWk_seikyu;
				IF 	nCount > 0 THEN
					CASE 	nRec
					WHEN 	1 	THEN
						UPDATE 	wk_bl_seikyu_info
						SET 	nengetu1 	=
								SUBSTR(cWk_kakin,1,4) || '.' ||
								SUBSTR(cWk_kakin,5,2),
								seikyugaku_sign1 =
								aoba_out_common.get_mark(
									aoba_out_const.CST_OUT_FAOB010100,
									/* 20060919 modify */
									--nWk_kingaku),
									nWk_sign),
								seikyugaku1 = nWk_kingaku
						WHERE 	kokyaku_cd 		= cWk_kokyaku
						AND 	seikyu_nengetu 	= cWk_seikyu;
					WHEN 	2 	THEN
						UPDATE 	wk_bl_seikyu_info
						SET 	nengetu2 	=
								SUBSTR(cWk_kakin,1,4) || '.' ||
								SUBSTR(cWk_kakin,5,2),
								seikyugaku_sign2 =
								aoba_out_common.get_mark(
									aoba_out_const.CST_OUT_FAOB010100,
									/* 20060919 modify */
									--nWk_kingaku),
									nWk_sign),
								seikyugaku2 = nWk_kingaku
						WHERE 	kokyaku_cd 		= cWk_kokyaku
						AND 	seikyu_nengetu 	= cWk_seikyu;
					WHEN 	3 	THEN
						UPDATE 	wk_bl_seikyu_info
						SET 	nengetu3 	=
								SUBSTR(cWk_kakin,1,4) || '.' ||
								SUBSTR(cWk_kakin,5,2),
								seikyugaku_sign3 =
								aoba_out_common.get_mark(
									aoba_out_const.CST_OUT_FAOB010100,
									/* 20060919 modify */
									--nWk_kingaku),
									nWk_sign),
								seikyugaku3 = nWk_kingaku
						WHERE 	kokyaku_cd 		= cWk_kokyaku
						AND 	seikyu_nengetu 	= cWk_seikyu;
					END CASE;
					nRec := nRec + 1;
					cKokyaku := cWk_kokyaku;
					cSeikyu 	 := cWk_seikyu;
					IF nRec > 3 THEN
						nRec := 0;
					END IF;
				END IF;
			END IF;
		END IF;
	END LOOP;

	/* 請求書顧客マスタ一時TBL挿入（当月請求） */
	INSERT INTO	wk_bl_kokyaku(
			kokyaku_cd_sk,								-- 顧客コードソートキー
			kakin_nengetu_sk,							-- 課金年月ソートキー
			print_type_cd_sk,							-- 用紙タイプソートキー
			dohu_tirashi_cd_sk,							-- 同封物コードソートキー
			oem_cd_sk,									-- OEMコードソートキー
			sk_yobi,									-- ソートキー予備
			seikyu_data,								-- 請求データ
			insert_date,								-- 登録日時
			insert_user)								-- 登録ユーザ
	SELECT	a.kokyaku_cd,
			a.seikyu_nengetu,
			CASE WHEN b.print_type_cd = '12' THEN
					'02'
				 ELSE
					b.print_type_cd
			END,
			b.dohu_tirashi_cd,
			b.oem_cd,
			'00',
			/* 請求データ */
			/* キー情報 */
			a.kokyaku_cd || a.seikyu_nengetu ||
			CASE WHEN b.print_type_cd = '12' THEN
					'02'
				 ELSE
					b.print_type_cd
			END ||
			b.dohu_tirashi_cd ||
			b.oem_cd ||
			/* 顧客基本情報 */
			a.zip ||
			a.addr1 ||
			a.addr2 ||
			a.kokyaku_name ||
			a.kokyaku_tanto ||
			a.kokyaku_tel ||
			/* 請求書発行情報 */
			a.seikyusho_no ||
			a.seikyusho_hakko_date ||
			a.nyukin_date ||
			a.seikyugaku_sign ||
			SUBSTR(TO_CHAR(a.seikyugaku,'000000009'),2,9) ||
			a.kazei_taishogaku_sign ||
			SUBSTR(TO_CHAR(a.kazei_taishogaku,'000000009'),2,9) ||
			a.hikazei_taishogaku_sign ||
			SUBSTR(TO_CHAR(a.hikazei_taishogaku,'000000009'),2,9) ||
			a.daiko_seikyugaku_sign ||
			SUBSTR(TO_CHAR(a.daiko_seikyugaku,'000000009'),2,9) ||
			a.tax_sign ||
			SUBSTR(TO_CHAR(a.tax,'000000009'),2,9) ||
			a.nengetu1 ||
			a.seikyugaku_sign1 ||
			SUBSTR(TO_CHAR(a.seikyugaku1,'000000009'),2,9) ||
			a.nengetu2 ||
			a.seikyugaku_sign2 ||
			SUBSTR(TO_CHAR(a.seikyugaku2,'000000009'),2,9) ||
			a.nengetu3 ||
			a.seikyugaku_sign3 ||
			SUBSTR(TO_CHAR(a.seikyugaku3,'000000009'),2,9) ||
			/* 口座振替時情報 */
			RPAD(' ',15) ||
			aoba_out_common.get_mark(
				aoba_out_const.CST_OUT_FAOB010100,0) || 	-- 入金額サイン
			'000000000' ||
			RPAD(' ',10) ||
			RPAD('　',40,'　') ||
			RPAD('　',40,'　') ||
			RPAD(' ',11) ||
			RPAD('　',70,'　') ||
			RPAD(' ',7)  ||
			aoba_out_common.get_mark(
				aoba_out_const.CST_OUT_FAOB010100,0) || 	-- 入金額1サイン
			'000000000' ||
			RPAD(' ',7)  ||
			aoba_out_common.get_mark(
				aoba_out_const.CST_OUT_FAOB010100,0) || 	-- 入金額2サイン
			'000000000' ||
			RPAD(' ',7)  ||
			aoba_out_common.get_mark(
				aoba_out_const.CST_OUT_FAOB010100,0) || 	-- 入金額3サイン
			'000000000' ||

			/* 郵便＋コンビニ情報 */
			CASE WHEN 	b.print_type_cd = '02' OR
				b.print_type_cd = '12' THEN
				RPAD(SUBSTR(a.bar_cd_jan1,1,8),8) ||
				RPAD(SUBSTR(a.bar_cd_jan2,1,13),13) ||
				RPAD(SUBSTR(a.bar_cd_jan3,1,13),13) ||
				RPAD(SUBSTR(a.bar_cd_jan4,1,13),13) ||
				RPAD(SUBSTR(a.bar_cd_ean128,1,44),44)
			ELSE
				RPAD('0',8,'0') ||
				RPAD('0',13,'0') ||
				RPAD('0',13,'0') ||
				RPAD('0',13,'0') ||
				RPAD('0',44,'0')
			END ||
			/* カード情報 */
			CASE WHEN 	b.print_type_cd = '04' OR
				b.print_type_cd = '14' THEN
				SUBSTR(TRIM(TO_CHAR(a.card_bng,'0000000000000000')),1,8) ||
				RPAD('*',8,'*')
			ELSE
				RPAD(' ',16)
			END ||
			a.saihakko_flg ||
			/* 20060210 FTPHONEフラグ対応 */
			a.ftphone_flg ||
--			RPAD(' ',4),
			RPAD(' ',3),
			dSysdate,
			vUser
	FROM 	wk_bl_seikyu_info a,
			wk_bl_seikyu_sum b
	WHERE 	a.kokyaku_cd = b.kokyaku_cd
	AND 	a.seikyu_nengetu = b.seikyu_nengetu
	AND 	b.kakin_nengetu = (
	/* 2005/12/12 桁あふれ対策 */
	--		SELECT MAX(kakin_nengetu)
			SELECT TRIM(MAX(kakin_nengetu))
			FROM 	wk_bl_seikyu_sum
			WHERE 	kokyaku_cd = a.kokyaku_cd
			AND 	seikyu_nengetu = a.seikyu_nengetu
			AND 	data_type = CONST_DATA_TYPE_01)
	AND 	b.data_type = CONST_DATA_TYPE_01;

	/* 2005/12/19 格納一時TBLにINSERTする処理を追加 */
	/* 請求書顧客マスタ格納一時TBL挿入（当月請求） */
	INSERT INTO	wk_bl_kokyaku_info(
			KOKYAKU_CD,				-- 顧客ｺｰﾄﾞ
			SEIKYU_NENGETU,			-- 請求年月
			PRINT_TYPE_CD,			-- 用紙ﾀｲﾌﾟ
			DOHU_TIRASHI_CD,		-- 同封物
			OEM_CD,					-- ｸﾞﾙｰﾌﾟ
			ZIP,					-- 郵便番号
			ADDR1,					-- 住所1
			ADDR2,					-- 住所2
			KOKYAKU_NAME,			-- 顧客名
			KOKYAKU_TANTO,			-- 顧客担当者名
			KOKYAKU_TEL,			-- 顧客電話番号
			SEIKYUSHO_NO,			-- 請求書発行No
			SEIKYUSHO_HAKKO_DATE,	-- 請求書発行日
			SHUNO_KIGEN,			-- 収納期限
			SEIKYUGAKU_SIGN,		-- 請求書金額ｻｲﾝ
			SEIKYUGAKU,				-- 請求金額
			KAZEI_TAISHOGAKU_SIGN,	-- 課税対象額ｻｲﾝ
			KAZEI_TAISHOGAKU,		-- 課税対象額
			HIKAZEI_TAISHOGAKU_SIGN,-- 非課税対象額ｻｲﾝ
			HIKAZEI_TAISHOGAKU,		-- 非課税対象額
			DAIKO_SEIKYUGAKU_SIGN,	-- 代行請求額ｻｲﾝ
			DAIKO_SEIKYUGAKU,		-- 代行請求額
			TAX_SIGN,				-- 消費税ｻｲﾝ
			TAX,					-- 消費税
			SEI_NENGETU1,			-- 請求/年月1
			SEIKYUGAKU_SIGN1,		-- 請求額1ｻｲﾝ
			SEIKYUGAKU1,			-- 請求額1
			SEI_NENGETU2,			-- 請求/年月2
			SEIKYUGAKU_SIGN2,		-- 請求額2ｻｲﾝ
			SEIKYUGAKU2,			-- 請求額2
			SEI_NENGETU3,			-- 請求/年月3
			SEIKYUGAKU_SIGN3,		-- 請求額3ｻｲﾝ
			SEIKYUGAKU3,			-- 請求額3
			RYOSHUSHO_NO,			-- 領収書発行No
			NYUKINGAKU_SIGN,		-- 入金額ｻｲﾝ
			NYUKINGAKU,				-- 入金額
			NYUKIN_DATE,			-- 入金年月日
			BANK_NAME,				-- 銀行名
			SHITEN_NAME,			-- 支店名
			KOZA_TYPE_CD,			-- 口座種別
			KOZA_BNG,				-- 口座No
			KOZA_MEIGI,				-- 口座名義
			NYU_NENGETU1,			-- 入金/年月1
			NYUKINGAKU_SIGN1,		-- 入金額1ｻｲﾝ
			NYUKINGAKU1,			-- 入金額1
			NYU_NENGETU2,			-- 入金/年月2
			NYUKINGAKU_SIGN2,		-- 入金額2ｻｲﾝ
			NYUKINGAKU2,			-- 入金額2
			NYU_NENGETU3,			-- 入金/年月3
			NYUKINGAKU_SIGN3,		-- 入金額3ｻｲﾝ
			NYUKINGAKU3,			-- 入金額3
			BAR_CD_JAN1,			-- ﾊﾞｰｺｰﾄﾞ1
			BAR_CD_JAN2,			-- ﾊﾞｰｺｰﾄﾞ2
			BAR_CD_JAN3,			-- ﾊﾞｰｺｰﾄﾞ3
			BAR_CD_JAN4,			-- ﾊﾞｰｺｰﾄﾞ4
			BAR_CD_EAN128,			-- ﾊﾞｰｺｰﾄﾞEAN128
			CARD_BNG,				-- ｶｰﾄﾞNo
			SAIHAKKO_FLG,			-- 再発行ﾌﾗｸﾞ
			/* 20060210 FTPHONEフラグ対応 */
			FTPHONE_FLG,			-- FTPHONEフラグ
			/* 20060322 変更ﾌﾗｸﾞ対応 */
			MODIFY_FLG				-- 変更ﾌﾗｸﾞ
			)
	SELECT	a.kokyaku_cd,								-- 顧客ｺｰﾄﾞ
			a.seikyu_nengetu,							-- 請求年月
			CASE WHEN b.print_type_cd = '12' THEN
					'02'
				 ELSE
					b.print_type_cd						-- 用紙タイプ
			END,
			b.dohu_tirashi_cd,							-- 同封物
			b.oem_cd,									-- グループ
			/* 顧客基本情報 */
			a.zip,										-- 郵便番号
			a.addr1,									-- 住所1
			a.addr2,									-- 住所2
			a.kokyaku_name,								-- 顧客名
			a.kokyaku_tanto,							-- 顧客担当者名
			a.kokyaku_tel,								-- 顧客電話番号
			/* 請求書発行情報 */
			a.seikyusho_no,								-- 請求書発行No
			a.seikyusho_hakko_date,						-- 請求書発行日
			a.nyukin_date,								-- 収納期限
			a.seikyugaku_sign,							-- 請求書金額ｻｲﾝ
			a.seikyugaku,								-- 請求金額
			a.kazei_taishogaku_sign,					-- 課税対象額ｻｲﾝ
			a.kazei_taishogaku,							-- 課税対象額
			a.hikazei_taishogaku_sign,					-- 非課税対象額ｻｲﾝ
			a.hikazei_taishogaku,						-- 非課税対象額
			a.daiko_seikyugaku_sign,					-- 代行請求額ｻｲﾝ
			a.daiko_seikyugaku,							-- 代行請求額
			a.tax_sign,									-- 消費税ｻｲﾝ
			a.tax,										-- 消費税
			TRIM(a.nengetu1),							-- 請求/年月1
			a.seikyugaku_sign1,							-- 請求額1ｻｲﾝ
			a.seikyugaku1,								-- 請求額1
			TRIM(a.nengetu2),							-- 請求/年月2
			a.seikyugaku_sign2,							-- 請求額2ｻｲﾝ
			a.seikyugaku2,								-- 請求額2
			TRIM(a.nengetu3),							-- 請求/年月3
			a.seikyugaku_sign3,							-- 請求額3ｻｲﾝ
			a.seikyugaku3,								-- 請求額3
			/* 口座振替時情報 */
			NULL,										-- 領収書No
			aoba_out_common.get_mark(
				aoba_out_const.CST_OUT_FAOB010100,0), 	-- 入金額サイン
			0,											-- 入金額
			NULL,										-- 入金年月日
			NULL,										-- 銀行名
			NULL,										-- 支店名
			NULL,										-- 口座種別
			NULL,										-- 口座No
			NULL,										-- 口座名義
			NULL,										-- 年月1
			aoba_out_common.get_mark(
				aoba_out_const.CST_OUT_FAOB010100,0), 	-- 入金額1サイン
			0,											-- 入金額1
			NULL,										-- 年月2
			aoba_out_common.get_mark(
				aoba_out_const.CST_OUT_FAOB010100,0),	-- 入金額2サイン
			0,											-- 入金額2
			NULL,										-- 年月3
			aoba_out_common.get_mark(
				aoba_out_const.CST_OUT_FAOB010100,0),	-- 入金額3サイン
			0,											-- 入金額3
			/* 郵便＋コンビニ情報 */
			CASE WHEN 	b.print_type_cd = '02' OR
				b.print_type_cd = '12' THEN
				RPAD(SUBSTR(a.bar_cd_jan1,1,8),8)		-- バーコード1段目
			ELSE
				NULL
			END,
			CASE WHEN 	b.print_type_cd = '02' OR
				b.print_type_cd = '12' THEN
				RPAD(SUBSTR(a.bar_cd_jan2,1,13),13)		-- バーコード2段目
			ELSE
				NULL
			END,
			CASE WHEN 	b.print_type_cd = '02' OR
				b.print_type_cd = '12' THEN
				RPAD(SUBSTR(a.bar_cd_jan3,1,13),13)		-- バーコード3段目
			ELSE
				NULL
			END,
			CASE WHEN 	b.print_type_cd = '02' OR
				b.print_type_cd = '12' THEN
				RPAD(SUBSTR(a.bar_cd_jan4,1,13),13)		-- バーコード4段目
			ELSE
				NULL
			END,
			CASE WHEN 	b.print_type_cd = '02' OR
				b.print_type_cd = '12' THEN
				RPAD(SUBSTR(a.bar_cd_ean128,1,44),44)	-- バーコードEAN128
			ELSE
				NULL
			END,
			/* カード情報 */
			CASE WHEN 	b.print_type_cd = '04' OR
				b.print_type_cd = '14' THEN
				TRIM(SUBSTR(TRIM(TO_CHAR(a.card_bng,'0000000000000000')),1,8) ||
					RPAD('*',8,'*'))					-- カードNo
			ELSE
				NULL
			END,
			/* 20060210 FTPHONEフラグ */
			TRIM(a.saihakko_flg),								-- 再発行ﾌﾗｸﾞ
			a.ftphone_flg,
			/* 20060322 変更ﾌﾗｸﾞ対応 */
			CASE WHEN b.print_type_cd = '12' THEN
					'Y'
				 ELSE
					'N'									-- 変更ﾌﾗｸﾞ
			END
	FROM 	wk_bl_seikyu_info a,
			wk_bl_seikyu_sum b
	WHERE 	a.kokyaku_cd = b.kokyaku_cd
	AND 	a.seikyu_nengetu = b.seikyu_nengetu
	AND 	b.kakin_nengetu = (
			SELECT TRIM(MAX(kakin_nengetu))
			FROM 	wk_bl_seikyu_sum
			WHERE 	kokyaku_cd = a.kokyaku_cd
			AND 	seikyu_nengetu = a.seikyu_nengetu
			AND 	data_type = CONST_DATA_TYPE_01)
	AND 	b.data_type = CONST_DATA_TYPE_01;


	/* 請求情報格納一時TBLの削除 */
	EXECUTE IMMEDIATE 'TRUNCATE TABLE wk_bl_seikyu_info';

	/* 請求情報格納一時TBL挿入（再請求まとめなし） */
	INSERT INTO	wk_bl_seikyu_info(
			kokyaku_cd,								-- 顧客コード
			seikyu_nengetu, 						-- 請求年月
			zip, 									-- 郵便番号
			addr1, 									-- 住所１
			addr2, 									-- 住所２
			kokyaku_name, 							-- 顧客名
			kokyaku_tanto, 							-- 顧客担当者
			kokyaku_tel, 							-- 顧客電話番号
			seikyusho_no, 							-- 請求書発行No
			seikyusho_hakko_date, 					-- 請求書発行日
			nyukin_date, 							-- 入金期日
			seikyugaku_sign, 						-- 請求金額サイン
			seikyugaku, 							-- 請求金額
			kazei_taishogaku_sign, 					-- 課税対象額サイン
			kazei_taishogaku, 						-- 課税対象額
			hikazei_taishogaku_sign, 				-- 非課税対象額サイン
			hikazei_taishogaku, 					-- 非課税対象額
			daiko_seikyugaku_sign, 					-- 代行請求額サイン
			daiko_seikyugaku, 						-- 代行請求額
			tax_sign, 								-- 消費税サイン
			tax, 									-- 消費税
			nengetu1, 								-- 年月１
			seikyugaku_sign1, 						-- 請求額１サイン
			seikyugaku1, 							-- 請求額１
			nengetu2, 								-- 年月２
			seikyugaku_sign2, 						-- 請求額２サイン
			seikyugaku2, 							-- 請求額２
			nengetu3, 								-- 年月３
			seikyugaku_sign3, 						-- 請求額３サイン
			seikyugaku3, 							-- 請求額３
			bar_cd_jan1, 							-- バーコード1
			bar_cd_jan2, 							-- バーコード2
			bar_cd_jan3, 							-- バーコード3
			bar_cd_jan4, 							-- バーコード4
			bar_cd_ean128, 							-- バーコードEAN128
			card_bng, 								-- カードNo
			saihakko_flg, 							-- 再発行フラグ
			/* 20060210 登録項目追加 */
			ftphone_flg)
	SELECT	a.kokyaku_cd,
			a.seikyu_nengetu, 													-- 請求年月
			nvl(b.seikyusaki_zip,' '), 											-- 郵便番号
			RPAD(SUBSTRB(nvl(b.seikyusaki_addr1,'　') || b.seikyusaki_addr2 ||
			b.seikyusaki_addr3 || b.seikyusaki_addr4,1,50),50,'　'), 			-- 住所１
			CASE WHEN LENGTHB(b.seikyusaki_addr1 || b.seikyusaki_addr2 ||
							b.seikyusaki_addr3 || b.seikyusaki_addr4)
							> 50 THEN
				RPAD(SUBSTRB(b.seikyusaki_addr1 || b.seikyusaki_addr2 ||
				b.seikyusaki_addr3 || b.seikyusaki_addr4,51,50),50,'　')
			ELSE
				RPAD('　',50,'　')
			END ,																-- 住所１
			RPAD(nvl(b.seikyusaki_kaisha_name,'　'),80,'　'), 					-- 顧客名
			/* 1/27 null対応 */
			RPAD(nvl(b.seikyusaki_busho_name,'　'),80,'　'), 					-- 顧客担当者
			SUBSTR(nvl(b.seikyusaki_tel,' '),1,13), 							-- 顧客電話番号
			SUBSTR(a.seikyu_nengetu,3,4) || a.seikyu_gun ||
			'-' || a.kokyaku_cd, 												-- 請求書発行No
			SUBSTR(RPAD(nvl(c.seikyusho_hakko_date,' '),8),1,4) || '.' ||
			SUBSTR(RPAD(nvl(c.seikyusho_hakko_date,' '),8),5,2) || '.' ||
			SUBSTR(RPAD(nvl(c.seikyusho_hakko_date,' '),8),7,2) ,				-- 請求書発行日
			SUBSTR(RPAD(nvl(c.shuno_kigen,' '),8),1,4) || '.' ||
			SUBSTR(RPAD(nvl(c.shuno_kigen,' '),8),5,2) || '.' ||
			SUBSTR(RPAD(nvl(c.shuno_kigen,' '),8),7,2), 						-- 入金期日
			aoba_out_common.get_mark(
				aoba_out_const.CST_OUT_FAOB010100,a.seikyugaku), 				-- 請求金額サイン
			TO_NUMBER(SUBSTR(TO_CHAR(a.seikyugaku,'0000000000'),3,9)),
				aoba_out_common.get_mark(	aoba_out_const.CST_OUT_FAOB010100,
				a.kazei_taishogaku), 											-- 課税対象額サイン
			TO_NUMBER(SUBSTR(TO_CHAR(a.kazei_taishogaku,'0000000000'),3,9)),
				aoba_out_common.get_mark(	aoba_out_const.CST_OUT_FAOB010100,
				a.hikazei_taishogaku), 											-- 非課税対象額サイン
			TO_NUMBER(SUBSTR(TO_CHAR(a.hikazei_taishogaku,'0000000000'),3,9)),
				aoba_out_common.get_mark(	aoba_out_const.CST_OUT_FAOB010100,
				a.daiko_seikyugaku), 											-- 代行請求額サイン
			TO_NUMBER(SUBSTR(TO_CHAR(a.daiko_seikyugaku,'0000000000'),3,9)),
				aoba_out_common.get_mark(	aoba_out_const.CST_OUT_FAOB010100,
				a.tax), 														-- 消費税サイン
			TO_NUMBER(SUBSTR(TO_CHAR(a.tax,'0000000000'),3,9)),
			/* 20060216 */
			--	SUBSTR(a.seikyu_nengetu,1,4) || '.' || SUBSTR(a.seikyu_nengetu,5,2),	-- 年月１
			RPAD(' ',7),														-- 年月１
--			aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,
--										a.seikyugaku),							-- 請求金額サイン
			aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,0),		-- 請求金額サイン	--20060228
--			TO_NUMBER(SUBSTR(TO_CHAR(a.seikyugaku,'0000000000'),3,9)), 			-- 請求額１
			aoba_const.CST_NUM_ZERO, 											-- 請求額１			--20060228
			RPAD(' ',7), 														-- 年月２
			aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,0),		-- 請求額2サイン
			aoba_const.CST_NUM_ZERO,											-- 請求額２
			RPAD(' ',7), 														-- 年月３
			aoba_out_common.get_mark(
				aoba_out_const.CST_OUT_FAOB010100,0),							-- 請求額3サイン
			aoba_const.CST_NUM_ZERO, 											-- 請求額３
			SUBSTR(a.bar_cd_jan1,1,8), 											-- バーコード1
			SUBSTR(a.bar_cd_jan2,1,13), 										-- バーコード2
			SUBSTR(a.bar_cd_jan3,1,13), 										-- バーコード3
			SUBSTR(a.bar_cd_jan4,1,13), 										-- バーコード4
			a.bar_cd_ean128, 													-- バーコードEAN128
			a.card_bng, 														-- カードNo
			aoba_const.CST_Y_FLG,												-- 再発行フラグ
			DECODE(d.kokyaku_cd,'',' ','Y')										-- FTPHONEフラグ
	FROM 	wk_bl_seikyu_sum 	a,
			tb_kokyaku_joho 	b,
			wk_seikyu_hakko_kokyaku 	c,
			/* 20060210 FTPHONEフラグ対応 */
			(SELECT 	kokyaku_cd,
						kakin_nengetu,
						seikyu_gun
			FROM	tb_seikyu_sum_srv
			WHERE	srv_type_cd = CST_FT_SRV_CD
			) d
	WHERE 	a.data_type 	= CONST_DATA_TYPE_02
	AND 	a.matome_flg 	<> aoba_out_const.CST_OUT_MATOME_FLG_Y
	AND 	a.kokyaku_cd 	= c.kokyaku_cd
	AND 	a.kakin_nengetu = c.kakin_nengetu
	AND 	a.seikyu_gun 	= c.seikyu_gun
	AND 	a.kokyaku_cd 	= b.kokyaku_cd(+)
	AND 	a.oem_cd 		= b.oem_cd(+)
	/* 20060210 FTPHONEフラグ対応 */
	AND		a.kokyaku_cd = d.kokyaku_cd (+)
	AND		a.kakin_nengetu = d.kakin_nengetu(+)
	AND		a.seikyu_gun = d.seikyu_gun (+);

	/* 請求情報格納一時TBL挿入（再請求まとめあり） */
	INSERT INTO	wk_bl_seikyu_info(
			kokyaku_cd,								-- 顧客コード
			seikyu_nengetu, 						-- 請求年月
			zip, 									-- 郵便番号
			addr1, 									-- 住所１
			addr2, 									-- 住所２
			kokyaku_name, 							-- 顧客名
			kokyaku_tanto, 							-- 顧客担当者
			kokyaku_tel, 							-- 顧客電話番号
			seikyusho_no, 							-- 請求書発行No
			seikyusho_hakko_date, 					-- 請求書発行日
			nyukin_date, 							-- 入金期日
			seikyugaku_sign, 						-- 請求金額サイン
			seikyugaku, 							-- 請求金額
			kazei_taishogaku_sign, 					-- 課税対象額サイン
			kazei_taishogaku, 						-- 課税対象額
			hikazei_taishogaku_sign, 				-- 非課税対象額サイン
			hikazei_taishogaku, 					-- 非課税対象額
			daiko_seikyugaku_sign, 					-- 代行請求額サイン
			daiko_seikyugaku, 						-- 代行請求額
			tax_sign, 								-- 消費税サイン
			tax, 									-- 消費税
			nengetu1, 								-- 年月１
			seikyugaku_sign1, 						-- 請求額１サイン
			seikyugaku1, 							-- 請求額１
			nengetu2, 								-- 年月２
			seikyugaku_sign2, 						-- 請求額２サイン
			seikyugaku2, 							-- 請求額２
			nengetu3, 								-- 年月３
			seikyugaku_sign3, 						-- 請求額３サイン
			seikyugaku3, 							-- 請求額３
			bar_cd_jan1, 							-- バーコード1
			bar_cd_jan2, 							-- バーコード2
			bar_cd_jan3, 							-- バーコード3
			bar_cd_jan4, 							-- バーコード4
			bar_cd_ean128, 							-- バーコードEAN128
			card_bng, 								-- カードNo
			saihakko_flg, 							-- 再発行フラグ
			ftphone_flg)
	SELECT	a_kokyaku_cd,
			a_seikyu_nengetu, 						-- 請求年月
			c_seikyusaki_zip, 						-- 郵便番号
			RPAD(SUBSTRB(c_seikyusaki_addr1 || c_seikyusaki_addr2 ||
			c_seikyusaki_addr3 || c_seikyusaki_addr4,1,50),50,'　'), -- 住所１
			CASE WHEN LENGTHB(c_seikyusaki_addr1 || c_seikyusaki_addr2 ||
							c_seikyusaki_addr3 || c_seikyusaki_addr4)
							> 50 THEN
				RPAD(SUBSTRB(c_seikyusaki_addr1 || c_seikyusaki_addr2 ||
				c_seikyusaki_addr3 || c_seikyusaki_addr4,51,50),50,'　')
			ELSE
				RPAD('　',50,'　')
			END , -- 住所１
			RPAD(c_seikyusaki_kaisha_name,80,'　'), 	-- 顧客名
			/* 1/27 null対応 */
			RPAD(nvl(c_seikyusaki_busho_name,'　'),80,'　'), -- 顧客担当者
			SUBSTR(c_seikyusaki_tel,1,13), 				-- 顧客電話番号
			SUBSTR(a_seikyu_nengetu,3,4) || a_seikyu_gun ||
			'-' || a_kokyaku_cd, 						-- 請求書発行No
			SUBSTR(d_seikyusho_hakko_date,1,4) || '.' ||
			SUBSTR(d_seikyusho_hakko_date,5,2) || '.' ||
			SUBSTR(d_seikyusho_hakko_date,7,2) , 		-- 請求書発行日
			SUBSTR(d_shuno_kigen,1,4) || '.' ||
			SUBSTR(d_shuno_kigen,5,2) || '.' ||
			SUBSTR(d_shuno_kigen,7,2), 					-- 入金期日
			aoba_out_common.get_mark(
				aoba_out_const.CST_OUT_FAOB010100,
				b_seikyugaku), 						-- 請求金額サイン
			TO_NUMBER(SUBSTR(TO_CHAR(b_seikyugaku,'0000000000'),3,9)),
				aoba_out_common.get_mark(	aoba_out_const.CST_OUT_FAOB010100,
											b_kazei_taishogaku), 					-- 課税対象額サイン
			TO_NUMBER(SUBSTR(TO_CHAR(b_kazei_taishogaku,'0000000000'),3,9)),
				aoba_out_common.get_mark(	aoba_out_const.CST_OUT_FAOB010100,
											b_hikazei_taishogaku), 				-- 非課税対象額サイン
			TO_NUMBER(SUBSTR(TO_CHAR(b_hikazei_taishogaku,'0000000000'),3,9)),
				aoba_out_common.get_mark(	aoba_out_const.CST_OUT_FAOB010100,
											b_daiko_seikyugaku), 					-- 代行請求額サイン
			TO_NUMBER(SUBSTR(TO_CHAR(b_daiko_seikyugaku,'0000000000'),3,9)),
				aoba_out_common.get_mark(	aoba_out_const.CST_OUT_FAOB010100,
											b_tax), 								-- 消費税サイン
			TO_NUMBER(SUBSTR(TO_CHAR(b_tax,'0000000000'),3,9)),
			RPAD(' ',7), 								-- 年月１
			aoba_out_common.get_mark(	aoba_out_const.CST_OUT_FAOB010100,0), 	-- 請求額１サイン
			aoba_const.CST_NUM_ZERO,				-- 請求額１
			RPAD(' ',7), 								-- 年月２
			aoba_out_common.get_mark(
				aoba_out_const.CST_OUT_FAOB010100,0), 	-- 請求額2サイン
			aoba_const.CST_NUM_ZERO, 							-- 請求額２
			RPAD(' ',7), 								-- 年月３
			aoba_out_common.get_mark(
				aoba_out_const.CST_OUT_FAOB010100,0), 	-- 請求額3サイン
			aoba_const.CST_NUM_ZERO,						-- 請求額３
			SUBSTR(a_bar_cd_jan1,1,8), 							-- バーコード1
			SUBSTR(a_bar_cd_jan2,1,13), 							-- バーコード2
			SUBSTR(a_bar_cd_jan3,1,13), 							-- バーコード3
			SUBSTR(a_bar_cd_jan4,1,13), 							-- バーコード4
			a_bar_cd_ean128, 							-- バーコードEAN128
			a_card_bng, 								-- カードNo
			aoba_const.CST_Y_FLG,						-- 再発行フラグ
			ftphone_flg									-- FTPHONEフラグ
	FROM
		(SELECT 	a.data_type 		AS a_data_type,
					a.kakin_nengetu 	AS a_kakin_nengetu,
					a.seikyu_gun 		AS a_seikyu_gun,
					a.kokyaku_cd 		AS a_kokyaku_cd,
					a.oem_cd 			AS a_oem_cd,
					a.matome_flg 		AS a_matome_flg,
					a.seikyu_nengetu 	AS a_seikyu_nengetu,
					a.seikyugaku 		AS a_seikyugaku,
					a.nyukingaku 		AS a_nyukingaku,
					a.nyukin_date 		AS a_nyukin_date,
					a.kazei_taishogaku 	AS a_kazei_taishogaku,
					a.hikazei_taishogaku AS a_hikazei_taishogaku,
					a.daiko_seikyugaku 	AS a_daiko_seikyugaku,
					a.tax 				AS a_tax,
					a.seikyu_saki_name 	AS a_seikyu_saki_name,
					a.seikyu_saki_addr1 AS a_seikyu_saki_addr1,
					a.seikyu_saki_addr2 AS a_seikyu_saki_addr2,
					a.seikyu_saki_addr3 AS a_seikyu_saki_addr3,
					a.seikyu_saki_addr4 AS a_seikyu_saki_addr4,
					a.zip 				AS a_zip,
					a.kokyaku_tantosha 	AS a_kokyaku_tantosha,
					a.kokyaku_tel 		AS a_kokyaku_tel,
					a.shuno_kigen 		AS a_shuno_kigen,
					a.bank_cd 			AS a_bank_cd,
					a.bank_name 		AS a_bank_name,
					a.shiten_name 		AS a_shiten_name,
					a.koza_type_cd 		AS a_koza_type_cd,
					a.koza_bng 			AS a_koza_bng,
					a.koza_meigi 		AS a_koza_meigi,
					a.bar_cd_jan1 		AS a_bar_cd_jan1,
					a.bar_cd_jan2 		AS a_bar_cd_jan2,
					a.bar_cd_jan3 		AS a_bar_cd_jan3,
					a.bar_cd_jan4 		AS a_bar_cd_jan4,
					a.bar_cd_ean128 	AS a_bar_cd_ean128,
					a.card_bng 			AS a_card_bng,
					a.print_type_cd 	AS a_print_type_cd,
					a.dohu_tirashi_cd 	AS a_dohu_tirashi_cd,
					a.seikyusho_hakko_date AS a_seikyusho_hakko_date,
					b.kakin_nengetu 	AS b_kakin_nengetu,
					b.seikyu_gun 		AS b_seikyu_gun,
					b.kokyaku_cd 		AS b_kokyaku_cd,
					b.matome_flg 		AS b_matome_flg,
					b.seikyugaku 		AS b_seikyugaku,
					b.nyukingaku 		AS b_nyukingaku,
					b.kazei_taishogaku 	AS b_kazei_taishogaku,
					b.hikazei_taishogaku AS b_hikazei_taishogaku,
					b.daiko_seikyugaku 	AS b_daiko_seikyugaku,
					b.tax 				AS b_tax,
					nvl(c.seikyusaki_kaisha_name,'　') AS c_seikyusaki_kaisha_name,
					nvl(c.seikyusaki_zip,' ') 	AS c_seikyusaki_zip,
					nvl(c.seikyusaki_addr1,'　') 	AS c_seikyusaki_addr1,
					nvl(c.seikyusaki_addr2,'　') 	AS c_seikyusaki_addr2,
					nvl(c.seikyusaki_addr3,'　') 	AS c_seikyusaki_addr3,
					nvl(c.seikyusaki_addr4,'　') 	AS c_seikyusaki_addr4,
					nvl(c.seikyusaki_busho_name,'　') AS c_seikyusaki_busho_name,
					nvl(c.seikyusaki_tel,' ') 	AS c_seikyusaki_tel,
					c.koza_bng_hyoji_flg AS c_koza_bng_hyoji_flg,
					nvl(d.seikyusho_hakko_date,' ') AS d_seikyusho_hakko_date,
					nvl(d.shuno_kigen,' ') 		AS d_shuno_kigen,
					b.ftphone_flg AS ftphone_flg
		FROM 	wk_bl_seikyu_sum a,
			/* 定義１ */
			(SELECT	a1.kakin_nengetu 	AS kakin_nengetu,
					a1.seikyu_gun 		AS seikyu_gun,
					a1.kokyaku_cd 		AS kokyaku_cd,
					a1.matome_flg 		AS matome_flg,
					b1.seikyu_nengetu 	AS seikyu_nengetu,
					b1.seikyugaku 		AS seikyugaku,
					b1.nyukingaku 		AS nyukingaku,
					b1.kazei_taishogaku AS kazei_taishogaku,
					b1.hikazei_taishogaku AS hikazei_taishogaku,
					b1.daiko_seikyugaku AS daiko_seikyugaku,
					b1.tax 				AS tax,
					DECODE(c1.kokyaku_cd,'',' ','Y') AS ftphone_flg		-- FTPHONEフラグ
			FROM	wk_bl_seikyu_sum a1,
					wk_matome_seikyu_sum b1,
					/* 20060210 FTPHONEフラグ対応 */
					(SELECT 	kokyaku_cd,
								kakin_nengetu,
								seikyu_gun
					FROM	tb_seikyu_sum_srv
					WHERE	srv_type_cd = CST_FT_SRV_CD
					) c1
			WHERE	a1.matome_flg 		= aoba_out_const.CST_OUT_MATOME_FLG_Y
			AND		a1.kakin_nengetu	= b1.kakin_nengetu(+)
			AND		a1.seikyu_gun		= b1.seikyu_gun(+)
			AND		a1.kokyaku_cd		= b1.kokyaku_cd(+)
			/* 20060210 FTPHONEフラグ対応 */
			AND		a1.kokyaku_cd		= c1.kokyaku_cd (+)
			AND		a1.kakin_nengetu	= c1.kakin_nengetu(+)
			AND		a1.seikyu_gun		= c1.seikyu_gun (+)
			/* インサート時にソートは不要 */
			--ORDER BY 	a1.kokyaku_cd,b1.seikyu_nengetu,a1.kakin_nengetu
			) b,
			tb_kokyaku_joho c,
			wk_seikyu_hakko_kokyaku d
		WHERE 	a.data_type 	= CONST_DATA_TYPE_02
		AND 	a.kokyaku_cd 	= b.kokyaku_cd
		AND 	a.kakin_nengetu = b.kakin_nengetu
		AND 	b.seikyu_nengetu IS NOT NULL
		AND 	a.kokyaku_cd 	= c.kokyaku_cd(+)
		AND 	a.oem_cd 		= c.oem_cd(+)
		AND 	a.kakin_nengetu = d.kakin_nengetu
		AND 	a.seikyu_gun = d.seikyu_gun
		AND 	a.kokyaku_cd = d.kokyaku_cd
		/* インサート時にソートは不要 */
--		ORDER BY 	a.kokyaku_cd,
--					a.seikyu_nengetu DESC,
--					a.kakin_nengetu
		) w;

	/* まとめ要素のデータ抽出（再請求） */
	nRec 		:= 0;
	cKokyaku 	:= ' ';
	cSeikyu 	:= ' ';
	FOR rec_1 IN cur_1() LOOP
		IF 	rec_1.matome_flg = aoba_out_const.CST_OUT_MATOME_FLG_Y THEN
			SELECT 	COUNT(*) INTO nCount
			FROM wk_bl_seikyu_sum
			WHERE 	kokyaku_cd 		= rec_1.kokyaku_cd
			AND 	kakin_nengetu 	= rec_1.kakin_nengetu;
			IF nCount > 0 THEN

				SELECT 	kokyaku_cd,
						kakin_nengetu,
						/*20060919 add*/
						seikyugaku,
						TO_NUMBER(SUBSTR(TO_CHAR(seikyugaku,'0000000000'),3,9)), 						seikyu_nengetu
				INTO 	cWk_kokyaku,cWk_kakin,nWk_sign,nWk_kingaku,cWk_seikyu
				FROM 	wk_bl_seikyu_sum
				WHERE 	kokyaku_cd 		= rec_1.kokyaku_cd
				AND 	kakin_nengetu 	= rec_1.kakin_nengetu;
				IF cKokyaku <> cWk_kokyaku OR cSeikyu <> cWk_seikyu THEN
					nRec := 1;
				END IF;
				SELECT 	COUNT(*) INTO nCount
				FROM 	wk_bl_seikyu_info
				WHERE 	kokyaku_cd 		= cWk_kokyaku
				AND 	seikyu_nengetu 	= cWk_seikyu;
				IF 	nCount > 0 THEN
					CASE 	nRec
					WHEN 	1 	THEN
						UPDATE 	wk_bl_seikyu_info
						SET 	nengetu1 	=
								SUBSTR(cWk_kakin,1,4) || '.' ||
								SUBSTR(cWk_kakin,5,2),
								seikyugaku_sign1 =
								aoba_out_common.get_mark(
									aoba_out_const.CST_OUT_FAOB010100,
									/* 20060919 modify */
									--nWk_kingaku),
									nWk_sign),
								seikyugaku1 = nWk_kingaku
						WHERE 	kokyaku_cd 		= cWk_kokyaku
						AND 	seikyu_nengetu 	= cWk_seikyu;
					WHEN 	2 	THEN
						UPDATE 	wk_bl_seikyu_info
						SET 	nengetu2 	=
								SUBSTR(cWk_kakin,1,4) || '.' ||
								SUBSTR(cWk_kakin,5,2),
								seikyugaku_sign2 =
								aoba_out_common.get_mark(
									aoba_out_const.CST_OUT_FAOB010100,
									/* 20060919 modify */
									--nWk_kingaku),
									nWk_sign),
								seikyugaku2 = nWk_kingaku
						WHERE 	kokyaku_cd 		= cWk_kokyaku
						AND 	seikyu_nengetu 	= cWk_seikyu;
					WHEN 	3 	THEN
						UPDATE 	wk_bl_seikyu_info
						SET 	nengetu3 	=
								SUBSTR(cWk_kakin,1,4) || '.' ||
								SUBSTR(cWk_kakin,5,2),
								seikyugaku_sign3 =
								aoba_out_common.get_mark(
									aoba_out_const.CST_OUT_FAOB010100,
									/* 20060919 modify */
									--nWk_kingaku),
									nWk_sign),
								seikyugaku3 = nWk_kingaku
						WHERE 	kokyaku_cd 		= cWk_kokyaku
						AND 	seikyu_nengetu 	= cWk_seikyu;
					END CASE;
					nRec := nRec + 1;
					cKokyaku := cWk_kokyaku;
					cSeikyu 	 := cWk_seikyu;
					IF nRec > 3 THEN
						nRec := 0;
					END IF;
				END IF;
			END IF;
		END IF;
	END LOOP;

	/* 請求書顧客マスタ一時TBL挿入（再請求） */
	INSERT INTO	wk_bl_kokyaku(
			kokyaku_cd_sk,								-- 顧客コードソートキー
			kakin_nengetu_sk,							-- 課金年月ソートキー
			print_type_cd_sk,							-- 用紙タイプソートキー
			dohu_tirashi_cd_sk,							-- 同封物コードソートキー
			oem_cd_sk,									-- OEMコードソートキー
			sk_yobi,									-- ソートキー予備
			seikyu_data,								-- 請求データ
			insert_date,								-- 登録日時
			insert_user)								-- 登録ユーザ
	SELECT	a.kokyaku_cd,
			a.seikyu_nengetu,
			CASE WHEN b.print_type_cd = '12' THEN
					'02'
				 ELSE
					b.print_type_cd
			END,
			/* 20060608 modify */
			--b.dohu_tirashi_cd,
			c.dohu_tirashi_cd,
			b.oem_cd,
			'00',
			/* 請求データ */
			/* キー情報 */
			a.kokyaku_cd || a.seikyu_nengetu ||
			CASE WHEN b.print_type_cd = '12' THEN
					'02'
				 ELSE
					b.print_type_cd
			END ||
			/* 20060608 modify */
			--b.dohu_tirashi_cd ||
			c.dohu_tirashi_cd ||
			b.oem_cd ||
			/* 顧客基本情報 */
			a.zip ||
			a.addr1 ||
			a.addr2 ||
			a.kokyaku_name ||
			a.kokyaku_tanto ||
			a.kokyaku_tel ||
			/* 請求書発行情報 */
			a.seikyusho_no ||
			a.seikyusho_hakko_date ||
			a.nyukin_date ||
			a.seikyugaku_sign ||
			SUBSTR(TO_CHAR(a.seikyugaku,'000000009'),2,9) ||
			a.kazei_taishogaku_sign ||
			SUBSTR(TO_CHAR(a.kazei_taishogaku,'000000009'),2,9) ||
			a.hikazei_taishogaku_sign ||
			SUBSTR(TO_CHAR(a.hikazei_taishogaku,'000000009'),2,9) ||
			a.daiko_seikyugaku_sign ||
			SUBSTR(TO_CHAR(a.daiko_seikyugaku,'000000009'),2,9) ||
			a.tax_sign ||
			SUBSTR(TO_CHAR(a.tax,'000000009'),2,9) ||
			a.nengetu1 ||
			a.seikyugaku_sign1 ||
			SUBSTR(TO_CHAR(a.seikyugaku1,'000000009'),2,9) ||
			a.nengetu2 ||
			a.seikyugaku_sign2 ||
			SUBSTR(TO_CHAR(a.seikyugaku2,'000000009'),2,9) ||
			a.nengetu3 ||
			a.seikyugaku_sign3 ||
			SUBSTR(TO_CHAR(a.seikyugaku3,'000000009'),2,9) ||
			/* 口座振替時情報 */
			RPAD(' ',15) ||
			aoba_out_common.get_mark(
				aoba_out_const.CST_OUT_FAOB010100,0) || 	-- 入金額サイン
			'000000000' ||
			RPAD(' ',10) ||
			RPAD('　',40,'　') ||
			RPAD('　',40,'　') ||
			RPAD(' ',11) ||
			RPAD('　',70,'　') ||
			RPAD(' ',7)  ||
			aoba_out_common.get_mark(
				aoba_out_const.CST_OUT_FAOB010100,0) || 	-- 入金額1サイン
			'000000000' ||
			RPAD(' ',7)  ||
			aoba_out_common.get_mark(
				aoba_out_const.CST_OUT_FAOB010100,0) || 	-- 入金額2サイン
			'000000000' ||
			RPAD(' ',7)  ||
			aoba_out_common.get_mark(
				aoba_out_const.CST_OUT_FAOB010100,0) || 	-- 入金額3サイン
			'000000000' ||
			/* 郵便＋コンビニ情報 */
			CASE WHEN 	b.print_type_cd = '02' OR
				b.print_type_cd = '12' THEN
				RPAD(SUBSTR(a.bar_cd_jan1,1,8),8) ||
				RPAD(SUBSTR(a.bar_cd_jan2,1,13),13) ||
				RPAD(SUBSTR(a.bar_cd_jan3,1,13),13) ||
				RPAD(SUBSTR(a.bar_cd_jan4,1,13),13) ||
				RPAD(SUBSTR(a.bar_cd_ean128,1,44),44)
			ELSE
				RPAD('0',8,'0') ||
				RPAD('0',13,'0') ||
				RPAD('0',13,'0') ||
				RPAD('0',13,'0') ||
				RPAD('0',44,'0')
			END ||
			/* カード情報 */
			CASE WHEN 	b.print_type_cd = '04' OR
				b.print_type_cd = '14' THEN
				SUBSTR(TRIM(TO_CHAR(a.card_bng,'0000000000000000')),1,8) ||
				RPAD('*',8,'*')
			ELSE
				RPAD(' ',16)
			END ||
			a.saihakko_flg ||
			/* 20060210 FTPHONEフラグ対応 */
			ftphone_flg ||
--			RPAD(' ',4),
			RPAD(' ',3),
			dSysdate,
			vUser
	FROM 	wk_bl_seikyu_info a,
			wk_bl_seikyu_sum b,
			/* 20060608 顧客基本情報JOIN追加 */
			tb_kokyaku_joho c
	WHERE 	a.kokyaku_cd = b.kokyaku_cd
	AND		a.kokyaku_cd = c.kokyaku_cd		-- 20060608 ADD
	AND 	a.seikyu_nengetu = b.seikyu_nengetu
	AND 	b.kakin_nengetu = (
	/* 2005/12/12 桁あふれ対策 */
--			SELECT MAX(kakin_nengetu)
			SELECT TRIM(MAX(kakin_nengetu))
			FROM 	wk_bl_seikyu_sum
			WHERE 	kokyaku_cd = a.kokyaku_cd
			AND 	seikyu_nengetu = a.seikyu_nengetu
			AND 	data_type = CONST_DATA_TYPE_02)
	AND 	b.data_type = CONST_DATA_TYPE_02;

	/* 2005/12/19 格納一時TBLにINSERTする処理を追加 */
	/* 請求書顧客マスタ格納一時TBL挿入（再請求） */
	INSERT INTO	wk_bl_kokyaku_info(
			KOKYAKU_CD,				-- 顧客ｺｰﾄﾞ
			SEIKYU_NENGETU,			-- 請求年月
			PRINT_TYPE_CD,			-- 用紙ﾀｲﾌﾟ
			DOHU_TIRASHI_CD,		-- 同封物
			OEM_CD,					-- ｸﾞﾙｰﾌﾟ
			ZIP,					-- 郵便番号
			ADDR1,					-- 住所1
			ADDR2,					-- 住所2
			KOKYAKU_NAME,			-- 顧客名
			KOKYAKU_TANTO,			-- 顧客担当者名
			KOKYAKU_TEL,			-- 顧客電話番号
			SEIKYUSHO_NO,			-- 請求書発行No
			SEIKYUSHO_HAKKO_DATE,	-- 請求書発行日
			SHUNO_KIGEN,			-- 収納期限
			SEIKYUGAKU_SIGN,		-- 請求書金額ｻｲﾝ
			SEIKYUGAKU,				-- 請求金額
			KAZEI_TAISHOGAKU_SIGN,	-- 課税対象額ｻｲﾝ
			KAZEI_TAISHOGAKU,		-- 課税対象額
			HIKAZEI_TAISHOGAKU_SIGN,-- 非課税対象額ｻｲﾝ
			HIKAZEI_TAISHOGAKU,		-- 非課税対象額
			DAIKO_SEIKYUGAKU_SIGN,	-- 代行請求額ｻｲﾝ
			DAIKO_SEIKYUGAKU,		-- 代行請求額
			TAX_SIGN,				-- 消費税ｻｲﾝ
			TAX,					-- 消費税
			SEI_NENGETU1,			-- 請求/年月1
			SEIKYUGAKU_SIGN1,		-- 請求額1ｻｲﾝ
			SEIKYUGAKU1,			-- 請求額1
			SEI_NENGETU2,			-- 請求/年月2
			SEIKYUGAKU_SIGN2,		-- 請求額2ｻｲﾝ
			SEIKYUGAKU2,			-- 請求額2
			SEI_NENGETU3,			-- 請求/年月3
			SEIKYUGAKU_SIGN3,		-- 請求額3ｻｲﾝ
			SEIKYUGAKU3,			-- 請求額3
			RYOSHUSHO_NO,			-- 領収書発行No
			NYUKINGAKU_SIGN,		-- 入金額ｻｲﾝ
			NYUKINGAKU,				-- 入金額
			NYUKIN_DATE,			-- 入金年月日
			BANK_NAME,				-- 銀行名
			SHITEN_NAME,			-- 支店名
			KOZA_TYPE_CD,			-- 口座種別
			KOZA_BNG,				-- 口座No
			KOZA_MEIGI,				-- 口座名義
			NYU_NENGETU1,			-- 入金/年月1
			NYUKINGAKU_SIGN1,		-- 入金額1ｻｲﾝ
			NYUKINGAKU1,			-- 入金額1
			NYU_NENGETU2,			-- 入金/年月2
			NYUKINGAKU_SIGN2,		-- 入金額2ｻｲﾝ
			NYUKINGAKU2,			-- 入金額2
			NYU_NENGETU3,			-- 入金/年月3
			NYUKINGAKU_SIGN3,		-- 入金額3ｻｲﾝ
			NYUKINGAKU3,			-- 入金額3
			BAR_CD_JAN1,			-- ﾊﾞｰｺｰﾄﾞ1
			BAR_CD_JAN2,			-- ﾊﾞｰｺｰﾄﾞ2
			BAR_CD_JAN3,			-- ﾊﾞｰｺｰﾄﾞ3
			BAR_CD_JAN4,			-- ﾊﾞｰｺｰﾄﾞ4
			BAR_CD_EAN128,			-- ﾊﾞｰｺｰﾄﾞEAN128
			CARD_BNG,				-- ｶｰﾄﾞNo
			SAIHAKKO_FLG,			-- 再発行ﾌﾗｸﾞ
			FTPHONE_FLG,
			/* 20060322 変更ﾌﾗｸﾞ対応 */
			MODIFY_FLG				-- 変更ﾌﾗｸﾞ
			)
	SELECT	a.kokyaku_cd,
			a.seikyu_nengetu,
			CASE WHEN b.print_type_cd = '12' THEN
					'02'
				 ELSE
					b.print_type_cd
			END,
			/* 20060608 modify */
			--b.dohu_tirashi_cd,
			c.dohu_tirashi_cd,
			b.oem_cd,
			/* 顧客基本情報 */
			a.zip,
			a.addr1,
			a.addr2,
			a.kokyaku_name,
			a.kokyaku_tanto,
			a.kokyaku_tel,
			/* 請求書発行情報 */
			a.seikyusho_no,
			a.seikyusho_hakko_date,
			a.nyukin_date,
			a.seikyugaku_sign,
			a.seikyugaku,
			a.kazei_taishogaku_sign,
			a.kazei_taishogaku,
			a.hikazei_taishogaku_sign,
			a.hikazei_taishogaku,
			a.daiko_seikyugaku_sign,
			a.daiko_seikyugaku,
			a.tax_sign,
			a.tax,
			TRIM(a.nengetu1),
			a.seikyugaku_sign1,
			a.seikyugaku1,
			TRIM(a.nengetu2),
			a.seikyugaku_sign2,
			a.seikyugaku2,
			TRIM(a.nengetu3),
			a.seikyugaku_sign3,
			a.seikyugaku3,
			/* 口座振替時情報 */
			NULL,
			aoba_out_common.get_mark(
				aoba_out_const.CST_OUT_FAOB010100,0),	-- 入金額サイン
			0,
			NULL,
			NULL,
			NULL,
			NULL,
			NULL,
			NULL,
			NULL,
			aoba_out_common.get_mark(
				aoba_out_const.CST_OUT_FAOB010100,0),	-- 入金額1サイン
			0,
			NULL,
			aoba_out_common.get_mark(
				aoba_out_const.CST_OUT_FAOB010100,0),	-- 入金額2サイン
			0,
			NULL,
			aoba_out_common.get_mark(
				aoba_out_const.CST_OUT_FAOB010100,0),	-- 入金額3サイン
			0,
			/* 郵便＋コンビニ情報 */
			CASE WHEN 	b.print_type_cd = '02' OR
				b.print_type_cd = '12' THEN
				RPAD(SUBSTR(a.bar_cd_jan1,1,8),8)		-- バーコード1段目
			ELSE
				NULL
			END,
			CASE WHEN 	b.print_type_cd = '02' OR
				b.print_type_cd = '12' THEN
				RPAD(SUBSTR(a.bar_cd_jan2,1,13),13)	-- バーコード2段目
			ELSE
				NULL
			END,
			CASE WHEN 	b.print_type_cd = '02' OR
				b.print_type_cd = '12' THEN
				RPAD(SUBSTR(a.bar_cd_jan3,1,13),13)	-- バーコード3段目
			ELSE
				NULL
			END,
			CASE WHEN 	b.print_type_cd = '02' OR
				b.print_type_cd = '12' THEN
				RPAD(SUBSTR(a.bar_cd_jan4,1,13),13)	-- バーコード4段目
			ELSE
				NULL
			END,
			CASE WHEN 	b.print_type_cd = '02' OR
				b.print_type_cd = '12' THEN
				RPAD(SUBSTR(a.bar_cd_ean128,1,44),44)	-- バーコードEAN128
			ELSE
				NULL
			END,
			/* カード情報 */
			CASE WHEN 	b.print_type_cd = '04' OR
				b.print_type_cd = '14' THEN
				TRIM(SUBSTR(TRIM(TO_CHAR(a.card_bng,'0000000000000000')),1,8) ||
					RPAD('*',8,'*'))
			ELSE
				NULL
			END,
			TRIM(a.saihakko_flg),
			a.ftphone_flg,
			/* 20060322 変更ﾌﾗｸﾞ対応 */
			CASE WHEN b.print_type_cd = '12' THEN
					'Y'
				 ELSE
					'N'
			END
	FROM 	wk_bl_seikyu_info a,
			wk_bl_seikyu_sum b,
			/* 20060608 顧客基本情報JOIN */
			tb_kokyaku_joho c
	WHERE 	a.kokyaku_cd = b.kokyaku_cd
	AND		a.kokyaku_cd = c.kokyaku_cd		-- 20060608 ADD
	AND 	a.seikyu_nengetu = b.seikyu_nengetu
	AND 	b.kakin_nengetu = (
			SELECT TRIM(MAX(kakin_nengetu))
			FROM 	wk_bl_seikyu_sum
			WHERE 	kokyaku_cd = a.kokyaku_cd
			AND 	seikyu_nengetu = a.seikyu_nengetu
			AND 	data_type = CONST_DATA_TYPE_02)
	AND 	b.data_type = CONST_DATA_TYPE_02;


	/* 領収情報格納一時TBLの削除 */
	EXECUTE IMMEDIATE 'TRUNCATE TABLE wk_bl_ryoshu_info';

	/* 領収情報格納一時TBL挿入（口座振替情報まとめなし） */
	INSERT INTO	wk_bl_ryoshu_info(
			kokyaku_cd,								-- 顧客コード
			seikyu_nengetu, 						-- 請求年月
			zip, 									-- 郵便番号
			addr1, 									-- 住所１
			addr2, 									-- 住所２
			kokyaku_name, 							-- 顧客名
			kokyaku_tanto, 							-- 顧客担当者
			kokyaku_tel, 							-- 顧客電話番号
			ryoshusho_no, 							-- 領収書発行No
			nyukingaku_sign, 						-- 入金額サイン
			nyukingaku, 							-- 入金額
			nyukin_date, 							-- 入金年月日
			bank_name, 								-- 銀行名
			shiten_name, 							-- 支店名
			koza_type_cd, 							-- 口座種別
			koza_bng, 								-- 口座No
			koza_meigi, 							-- 口座名義
			nengetu1, 								-- 年月１
			nyukingaku_sign1, 						-- 入金額１サイン
			nyukingaku1, 							-- 入金額１
			nengetu2, 								-- 年月２
			nyukingaku_sign2, 						-- 入金額２サイン
			nyukingaku2, 							-- 入金額２
			nengetu3, 								-- 年月３
			nyukingaku_sign3, 						-- 入金額３サイン
			nyukingaku3, 							-- 入金額３
			print_type_cd)							-- 用紙タイプ	-- 20060707 ADD
	SELECT	a.kokyaku_cd,
			a.seikyu_nengetu, 						-- 請求年月
			nvl(b.seikyusaki_zip,' '), 						-- 郵便番号
			RPAD(SUBSTRB(nvl(b.seikyusaki_addr1,'　') || b.seikyusaki_addr2 ||
			b.seikyusaki_addr3 || b.seikyusaki_addr4,1,50),50,'　'), -- 住所１
			CASE WHEN LENGTHB(b.seikyusaki_addr1 || b.seikyusaki_addr2 ||
							b.seikyusaki_addr3 || b.seikyusaki_addr4)
							> 50 THEN
				RPAD(SUBSTRB(b.seikyusaki_addr1 || b.seikyusaki_addr2 ||
				b.seikyusaki_addr3 || b.seikyusaki_addr4,51,50),50,'　')
			ELSE
				RPAD('　',50,'　')
			END , -- 住所１
			RPAD(nvl(b.seikyusaki_kaisha_name,'　'),80,'　'),			-- 顧客名
			/* 1/27 null対応 */
			RPAD(nvl(b.seikyusaki_busho_name,'　'),80,'　'),			-- 顧客担当者
			SUBSTR(nvl(b.seikyusaki_tel,' '),1,13), 					-- 顧客電話番号
			SUBSTR(a.seikyu_nengetu,3,4) || a.seikyu_gun ||
			'-' || a.kokyaku_cd, 										-- 領収書発行No
			aoba_out_common.get_mark(
				aoba_out_const.CST_OUT_FAOB010100,
			a.nyukingaku), 												-- 入金額サイン
			TO_NUMBER(SUBSTR(TO_CHAR(a.nyukingaku,'0000000000'),3,9)), 
			SUBSTR(a.nyukin_date,1,4) || '.' ||
			SUBSTR(a.nyukin_date,5,2) || '.' ||
			SUBSTR(a.nyukin_date,7,2) , 								-- 入金日
			RPAD(a.bank_name,40,'　'), 									-- 銀行名
			RPAD(a.shiten_name,40,'　'), 								-- 支店名
			/* 口座番号表示フラグの値増加対応 */
			CASE WHEN a.koza_bng_hyoji_flg = '1' THEN
			--CASE WHEN a.koza_bng_hyoji_flg IN( '1' , '3' ) THEN
				a.koza_type_cd
			ELSE
				'*'
			END , 														-- 口座種別
			CASE WHEN a.koza_bng_hyoji_flg = '1' THEN
			--CASE WHEN a.koza_bng_hyoji_flg IN( '1' , '3' ) THEN
				a.koza_bng
			ELSE
				RPAD('*',10,'*')
			END , 														-- 口座番号
			CASE WHEN a.koza_bng_hyoji_flg = '1' THEN
			--CASE WHEN a.koza_bng_hyoji_flg IN( '1' , '3' ) THEN
			RPAD(aoba_out_common.conv_out_mbc
						(a.koza_meigi),70,'　')
			ELSE
				RPAD('＊',70,'＊')
			END , 						-- 口座名義
			/* 20060216 まとめ無の場合年月１，２，３は空白埋め */
--			RPAD(SUBSTR(a.kakin_nengetu,1,4) || '.' || SUBSTR(a.kakin_nengetu,5,2),7), 	-- 年月１
			RPAD(' ',7), 	-- 年月１
--			aoba_out_common.get_mark(
--				aoba_out_const.CST_OUT_FAOB010100,a.nyukingaku),					-- 入金額１サイン
			aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,0),			-- 入金額１サイン
--			TO_NUMBER(SUBSTR(TO_CHAR(a.nyukingaku,'0000000000'),3,9)),				-- 入金額１
			0,																		-- 入金額１
			RPAD(' ',7), 															-- 年月２
			aoba_out_common.get_mark(
				aoba_out_const.CST_OUT_FAOB010100,0),								-- 入金額2サイン
			0, 																		-- 入金額２
			RPAD(' ',7), 															-- 年月３
			aoba_out_common.get_mark(
				aoba_out_const.CST_OUT_FAOB010100,0),								-- 入金額3サイン
			0, 																		-- 入金額３
			a.print_type_cd															-- 用紙タイプ	-- 20060707 ADD
	FROM 	wk_bl_seikyu_sum 	a,
			tb_kokyaku_joho 	b
	WHERE 	a.data_type 	= CONST_DATA_TYPE_03
	AND 	a.matome_flg 	<> aoba_out_const.CST_OUT_MATOME_FLG_Y
	AND 	a.kokyaku_cd 	= b.kokyaku_cd(+)
	AND 	a.oem_cd 		= b.oem_cd(+);

	/* 領収情報格納一時TBL挿入（口座振替まとめあり） */
	INSERT INTO	wk_bl_ryoshu_info(
			kokyaku_cd,								-- 顧客コード
			seikyu_nengetu, 						-- 請求年月
			zip, 									-- 郵便番号
			addr1, 									-- 住所１
			addr2, 									-- 住所２
			kokyaku_name, 							-- 顧客名
			kokyaku_tanto, 							-- 顧客担当者
			kokyaku_tel, 							-- 顧客電話番号
			ryoshusho_no, 							-- 領収書発行No
			nyukingaku_sign, 						-- 入金額サイン
			nyukingaku, 							-- 入金額
			nyukin_date, 							-- 入金年月日
			bank_name, 								-- 銀行名
			shiten_name, 							-- 支店名
			koza_type_cd, 							-- 口座種別
			koza_bng, 								-- 口座No
			koza_meigi, 							-- 口座名義
			nengetu1, 								-- 年月１
			nyukingaku_sign1, 						-- 入金額１サイン
			nyukingaku1, 							-- 入金額１
			nengetu2, 								-- 年月２
			nyukingaku_sign2, 						-- 入金額２サイン
			nyukingaku2, 							-- 入金額２
			nengetu3, 								-- 年月３
			nyukingaku_sign3, 						-- 入金額３サイン
			nyukingaku3, 							-- 入金額３
			print_type_cd)							-- 用紙タイプ	-- 20060707 ADD
	SELECT	a_kokyaku_cd,
			a_seikyu_nengetu, 						-- 請求年月
			c_seikyusaki_zip, 						-- 郵便番号
			RPAD(SUBSTRB(c_seikyusaki_addr1 || c_seikyusaki_addr2 ||
			c_seikyusaki_addr3 || c_seikyusaki_addr4,1,50),50,'　'), -- 住所１
			CASE WHEN LENGTHB(c_seikyusaki_addr1 || c_seikyusaki_addr2 ||
							c_seikyusaki_addr3 || c_seikyusaki_addr4)
							> 50 THEN
				RPAD(SUBSTRB(c_seikyusaki_addr1 || c_seikyusaki_addr2 ||
				c_seikyusaki_addr3 || c_seikyusaki_addr4,51,50),50,'　')
			ELSE
				RPAD('　',50,'　')
			END , -- 住所１
			RPAD(c_seikyusaki_kaisha_name,80,'　'), 	-- 顧客名
			RPAD(c_seikyusaki_busho_name,80,'　'),		-- 顧客担当者
			SUBSTR(c_seikyusaki_tel,1,13), 				-- 顧客電話番号
			SUBSTR(a_seikyu_nengetu,3,4) || a_seikyu_gun ||
			'-' || a_kokyaku_cd, 						-- 領収書発行No
			aoba_out_common.get_mark(
				aoba_out_const.CST_OUT_FAOB010100,
				b_nyukingaku), 						-- 入金額サイン
			TO_NUMBER(SUBSTR(TO_CHAR(b_nyukingaku,'0000000000'),3,9)), 						SUBSTR(a_nyukin_date,1,4) || '.' ||
			SUBSTR(a_nyukin_date,5,2) || '.' ||
			SUBSTR(a_nyukin_date,7,2) , 			-- 入金日
			RPAD(a_bank_name,40,'　'), 				-- 銀行名
			RPAD(a_shiten_name,40,'　'), 			-- 支店名
			/* 口座番号表示フラグの値増加対応 */
			CASE WHEN a_koza_bng_hyoji_flg = '1' THEN
			--CASE WHEN a_koza_bng_hyoji_flg IN( '1' , '3' ) THEN
				a_koza_type_cd
			ELSE
				'*'
			END , 						-- 口座種別
			CASE WHEN a_koza_bng_hyoji_flg = '1' THEN
			--CASE WHEN a_koza_bng_hyoji_flg IN( '1' , '3' ) THEN
				a_koza_bng
			ELSE
				RPAD('*',10,'*')
			END , 							-- 口座番号
			CASE WHEN a_koza_bng_hyoji_flg = '1' THEN
			--CASE WHEN a_koza_bng_hyoji_flg IN( '1' , '3' ) THEN
			RPAD(aoba_out_common.conv_out_mbc
						(a_koza_meigi),70,'　')
			ELSE
				RPAD('＊',70,'＊')
			END , 						-- 口座名義
			RPAD(' ',7), 								-- 年月１
			aoba_out_common.get_mark(
				aoba_out_const.CST_OUT_FAOB010100,0), 	-- 入金額1サイン
			aoba_const.CST_NUM_ZERO, 							-- 入金額１
			RPAD(' ',7), 								-- 年月２
			aoba_out_common.get_mark(
				aoba_out_const.CST_OUT_FAOB010100,0), 	-- 入金額2サイン
			aoba_const.CST_NUM_ZERO, 							-- 入金額２
			RPAD(' ',7), 								-- 年月３
			aoba_out_common.get_mark(
				aoba_out_const.CST_OUT_FAOB010100,0), 	-- 入金額3サイン
			aoba_const.CST_NUM_ZERO, 							-- 入金額３
			a_print_type_cd										-- 用紙タイプ	-- 20060707
	FROM
		(SELECT 	a.data_type 		AS a_data_type,
					a.kakin_nengetu 	AS a_kakin_nengetu,
					a.seikyu_gun 		AS a_seikyu_gun,
					a.kokyaku_cd 		AS a_kokyaku_cd,
					a.oem_cd 			AS a_oem_cd,
					a.matome_flg 		AS a_matome_flg,
					a.seikyu_nengetu 	AS a_seikyu_nengetu,
					a.seikyugaku 		AS a_seikyugaku,
					a.nyukingaku 		AS a_nyukingaku,
					a.nyukin_date 		AS a_nyukin_date,
					a.kazei_taishogaku 	AS a_kazei_taishogaku,
					a.hikazei_taishogaku AS a_hikazei_taishogaku,
					a.daiko_seikyugaku 	AS a_daiko_seikyugaku,
					a.tax 				AS a_tax,
					a.seikyu_saki_name 	AS a_seikyu_saki_name,
					a.seikyu_saki_addr1 AS a_seikyu_saki_addr1,
					a.seikyu_saki_addr2 AS a_seikyu_saki_addr2,
					a.seikyu_saki_addr3 AS a_seikyu_saki_addr3,
					a.seikyu_saki_addr4 AS a_seikyu_saki_addr4,
					a.zip 				AS a_zip,
					a.kokyaku_tantosha 	AS a_kokyaku_tantosha,
					a.kokyaku_tel 		AS a_kokyaku_tel,
					a.shuno_kigen 		AS a_shuno_kigen,
					a.bank_cd 			AS a_bank_cd,
					a.bank_name 		AS a_bank_name,
					a.shiten_name 		AS a_shiten_name,
					a.koza_type_cd 		AS a_koza_type_cd,
					a.koza_bng 			AS a_koza_bng,
					a.koza_meigi 		AS a_koza_meigi,
					a.bar_cd_jan1 		AS a_bar_cd_jan1,
					a.bar_cd_jan2 		AS a_bar_cd_jan2,
					a.bar_cd_jan3 		AS a_bar_cd_jan3,
					a.bar_cd_jan4 		AS a_bar_cd_jan4,
					a.bar_cd_ean128 	AS a_bar_cd_ean128,
					a.card_bng 			AS a_card_bng,
					a.print_type_cd 	AS a_print_type_cd,
					a.dohu_tirashi_cd 	AS a_dohu_tirashi_cd,
					a.seikyusho_hakko_date AS a_seikyusho_hakko_date,
					a.koza_bng_hyoji_flg AS a_koza_bng_hyoji_flg,
					b.kakin_nengetu 	AS b_kakin_nengetu,
					b.seikyu_gun 		AS b_seikyu_gun,
					b.kokyaku_cd 		AS b_kokyaku_cd,
					b.matome_flg 		AS b_matome_flg,
					b.seikyugaku 		AS b_seikyugaku,
					b.nyukingaku 		AS b_nyukingaku,
					b.kazei_taishogaku 	AS b_kazei_taishogaku,
					b.hikazei_taishogaku AS b_hikazei_taishogaku,
					b.daiko_seikyugaku 	AS b_daiko_seikyugaku,
					b.tax 				AS b_tax,
					nvl(c.seikyusaki_kaisha_name,'　') AS c_seikyusaki_kaisha_name,
					nvl(c.seikyusaki_zip,' ') 	AS c_seikyusaki_zip,
					nvl(c.seikyusaki_addr1,'　') 	AS c_seikyusaki_addr1,
					nvl(c.seikyusaki_addr2,'　') 	AS c_seikyusaki_addr2,
					nvl(c.seikyusaki_addr3,'　') 	AS c_seikyusaki_addr3,
					nvl(c.seikyusaki_addr4,'　') 	AS c_seikyusaki_addr4,
					nvl(c.seikyusaki_busho_name,'　') AS c_seikyusaki_busho_name,
					nvl(c.seikyusaki_tel,' ') 	AS c_seikyusaki_tel,
					c.koza_bng_hyoji_flg AS c_koza_bng_hyoji_flg
		FROM 	wk_bl_seikyu_sum a,
			/* 定義１ */
			(SELECT	a1.kakin_nengetu 	AS kakin_nengetu,
					a1.seikyu_gun 		AS seikyu_gun,
					a1.kokyaku_cd 		AS kokyaku_cd,
					a1.matome_flg 		AS matome_flg,
					b1.seikyu_nengetu 	AS seikyu_nengetu,
					b1.seikyugaku 		AS seikyugaku,
					b1.nyukingaku 		AS nyukingaku,
					b1.kazei_taishogaku AS kazei_taishogaku,
					b1.hikazei_taishogaku AS hikazei_taishogaku,
					b1.daiko_seikyugaku AS daiko_seikyugaku,
					b1.tax 				AS tax
			FROM	wk_bl_seikyu_sum a1,
					wk_matome_seikyu_sum b1
			WHERE	a1.matome_flg 		= aoba_out_const.CST_OUT_MATOME_FLG_Y
			AND		a1.kakin_nengetu     = b1.kakin_nengetu(+)
			AND		a1.seikyu_gun        = b1.seikyu_gun(+)
			AND		a1.kokyaku_cd        = b1.kokyaku_cd(+)
			/* インサート時にソートは不要 */
			--ORDER BY 	a1.kokyaku_cd,b1.seikyu_nengetu,a1.kakin_nengetu
			) b,
			tb_kokyaku_joho c
		WHERE 	a.data_type 	= CONST_DATA_TYPE_03
		AND 	a.matome_flg 	= aoba_out_const.CST_OUT_MATOME_FLG_Y
		AND 	b.seikyu_nengetu IS NOT NULL
		AND 	a.kokyaku_cd 	= b.kokyaku_cd
		AND 	a.kakin_nengetu = b.kakin_nengetu
		AND 	a.kokyaku_cd 	= c.kokyaku_cd(+)
		AND 	a.oem_cd 		= c.oem_cd(+)
		/* インサート時にソートは不要 */
--		ORDER BY 	a.kokyaku_cd,
--					a.seikyu_nengetu DESC,
--					a.kakin_nengetu
		) w;

	/* まとめ要素のデータ抽出（領収書） */
	nRec 		:= 0;
	cKokyaku 	:= ' ';
	cSeikyu 	:= ' ';
	FOR rec_1 IN cur_1() LOOP
		IF 	rec_1.matome_flg = aoba_out_const.CST_OUT_MATOME_FLG_Y THEN
			SELECT 	COUNT(*) INTO nCount
			FROM wk_bl_seikyu_sum
			WHERE 	kokyaku_cd 		= rec_1.kokyaku_cd
			AND 	kakin_nengetu 	= rec_1.kakin_nengetu;
			IF nCount > 0 THEN

				SELECT 	kokyaku_cd,
						kakin_nengetu,
						/* 20060919 add */
						nyukingaku,
						TO_NUMBER(SUBSTR(TO_CHAR(nyukingaku,'0000000000'),3,9)), 						seikyu_nengetu
				INTO 	cWk_kokyaku,cWk_kakin,nWk_sign,nWk_kingaku,cWk_seikyu
				FROM 	wk_bl_seikyu_sum
				WHERE 	kokyaku_cd 		= rec_1.kokyaku_cd
				AND 	kakin_nengetu 	= rec_1.kakin_nengetu;
				IF cKokyaku <> cWk_kokyaku OR cSeikyu <> cWk_seikyu THEN
					nRec := 1;
				END IF;
				SELECT 	COUNT(*) INTO nCount
				FROM 	wk_bl_ryoshu_info
				WHERE 	kokyaku_cd 		= cWk_kokyaku
				AND 	seikyu_nengetu 	= cWk_seikyu;
				IF 	nCount > 0 THEN
					CASE 	nRec
					WHEN 	1 	THEN
						UPDATE 	wk_bl_ryoshu_info
						SET 	nengetu1 	=
								SUBSTR(cWk_kakin,1,4) || '.' ||
								SUBSTR(cWk_kakin,5,2),
								nyukingaku_sign1 =
								aoba_out_common.get_mark(
									aoba_out_const.CST_OUT_FAOB010100,
									/* 20060919 modify */
									--nWk_kingaku),
									nWk_sign),
								nyukingaku1 = nWk_kingaku
						WHERE 	kokyaku_cd 		= cWk_kokyaku
						AND 	seikyu_nengetu 	= cWk_seikyu;
					WHEN 	2 	THEN
						UPDATE 	wk_bl_ryoshu_info
						SET 	nengetu2 	=
								SUBSTR(cWk_kakin,1,4) || '.' ||
								SUBSTR(cWk_kakin,5,2),
								nyukingaku_sign2 =
								aoba_out_common.get_mark(
									aoba_out_const.CST_OUT_FAOB010100,
									/* 20060919 modify */
									--nWk_kingaku),
									nWk_sign),
								nyukingaku2 = nWk_kingaku
						WHERE 	kokyaku_cd 		= cWk_kokyaku
						AND 	seikyu_nengetu 	= cWk_seikyu;
					WHEN 	3 	THEN
						UPDATE 	wk_bl_ryoshu_info
						SET 	nengetu3 	=
								SUBSTR(cWk_kakin,1,4) || '.' ||
								SUBSTR(cWk_kakin,5,2),
								nyukingaku_sign3 =
								aoba_out_common.get_mark(
									aoba_out_const.CST_OUT_FAOB010100,
									/* 20060919 modify */
									--nWk_kingaku),
									nWk_sign),
								nyukingaku3 = nWk_kingaku
						WHERE 	kokyaku_cd 		= cWk_kokyaku
						AND 	seikyu_nengetu 	= cWk_seikyu;
					END CASE;
					nRec := nRec + 1;
					cKokyaku := cWk_kokyaku;
					cSeikyu 	 := cWk_seikyu;
					IF nRec > 3 THEN
						nRec := 0;
					END IF;
				END IF;
			END IF;
		END IF;
	END LOOP;

	/* 請求書顧客マスタ一時TBL挿入（領収書） */
	FOR rec_wk1 IN cur_wk1() LOOP
		SELECT 	COUNT(*) INTO nCount FROM wk_bl_kokyaku
		WHERE 	kokyaku_cd_sk = rec_wk1.kokyaku_cd
		AND 	print_type_cd_sk IN ('01','11')
		AND 	SUBSTRB(seikyu_data,442,15) = '               ';
		IF 	nCount > 0 THEN
		/* 桁あふれ対策 */
--			SELECT 	MAX(kakin_nengetu_sk) INTO cWk_kakin FROM wk_bl_kokyaku
			SELECT 	TRIM(MAX(kakin_nengetu_sk)) INTO cWk_kakin FROM wk_bl_kokyaku
			WHERE 	kokyaku_cd_sk = rec_wk1.kokyaku_cd
			AND 	print_type_cd_sk IN ('01','11')
			AND 	SUBSTRB(seikyu_data,442,15) = '               ';
			/* 請求書顧客マスタ一時TBL更新（領収書） */
			UPDATE 	wk_bl_kokyaku
			SET 	seikyu_data =
				--SUBSTRB(seikyu_data,1,441) ||  20060707 modify
				SUBSTRB(seikyu_data,1,14) ||	-- 20060707 ADD
				/* 20060707 用紙タイプ洗い換え処理追加 */
				DECODE(SUBSTRB(seikyu_data,15,2),'01','05',
				                                 '11','15',SUBSTRB(seikyu_data,15,2)) ||
				SUBSTRB(SEIKYU_DATA,17,425) ||		-- 20060707 ADD
				rec_wk1.ryoshusho_no ||
				rec_wk1.nyukingaku_sign ||
				SUBSTR(TO_CHAR(rec_wk1.nyukingaku,'000000009'),2,9) ||
				rec_wk1.nyukin_date ||
				rec_wk1.bank_name ||
				rec_wk1.shiten_name ||
				rec_wk1.koza_type_cd ||
				rec_wk1.koza_bng ||
				rec_wk1.koza_meigi ||
				rec_wk1.nengetu1 ||
				rec_wk1.nyukingaku_sign1 ||
				SUBSTR(TO_CHAR(rec_wk1.nyukingaku1,'000000009'),2,9) ||
				rec_wk1.nengetu2 ||
				rec_wk1.nyukingaku_sign2 ||
				SUBSTR(TO_CHAR(rec_wk1.nyukingaku2,'000000009'),2,9) ||
				rec_wk1.nengetu3 ||
				rec_wk1.nyukingaku_sign3 ||
				SUBSTR(TO_CHAR(rec_wk1.nyukingaku3,'000000009'),2,9) ||
				SUBSTRB(seikyu_data,689,112),
				print_type_cd_sk = rec_wk1.print_type_cd		-- 20060707 ADD
			WHERE 	kokyaku_cd_sk 	= rec_wk1.kokyaku_cd
			AND 	kakin_nengetu_sk = cWk_kakin
			AND 	print_type_cd_sk IN ('01','11');

			/* 2005/12/19 格納一時TBLをUPDATEする処理を追加 */
			/* 請求書顧客マスタ格納一時TBL更新（領収書） */
			UPDATE 	wk_bl_kokyaku_info
			SET	ryoshusho_no = rec_wk1.ryoshusho_no,
				nyukingaku_sign = rec_wk1.nyukingaku_sign,
				nyukingaku = TO_NUMBER(SUBSTR(TO_CHAR(rec_wk1.nyukingaku,'000000009'),2,9)),
				nyukin_date = rec_wk1.nyukin_date,
				bank_name = rec_wk1.bank_name,
				shiten_name = rec_wk1.shiten_name,
				koza_type_cd = rec_wk1.koza_type_cd,
				koza_bng = rec_wk1.koza_bng,
				koza_meigi = rec_wk1.koza_meigi,
				nyu_nengetu1 = TRIM(rec_wk1.nengetu1),
				nyukingaku_sign1 = rec_wk1.nyukingaku_sign1,
				nyukingaku1 = TO_NUMBER(SUBSTR(TO_CHAR(rec_wk1.nyukingaku1,'000000009'),2,9)),
				nyu_nengetu2 = TRIM(rec_wk1.nengetu2),
				nyukingaku_sign2 = rec_wk1.nyukingaku_sign2,
				nyukingaku2 = TO_NUMBER(SUBSTR(TO_CHAR(rec_wk1.nyukingaku2,'000000009'),2,9)),
				nyu_nengetu3 = TRIM(rec_wk1.nengetu3),
				nyukingaku_sign3 = rec_wk1.nyukingaku_sign3,
				nyukingaku3 = TO_NUMBER(SUBSTR(TO_CHAR(rec_wk1.nyukingaku3,'000000009'),2,9)),
				print_type_cd = rec_wk1.print_type_cd						-- 20060707 ADD
			WHERE 	kokyaku_cd 	= rec_wk1.kokyaku_cd
			AND 	seikyu_nengetu = cWk_kakin
			AND 	print_type_cd IN ('01','11');
		ELSE
			/* 請求書顧客マスタ一時TBL挿入（領収書） */
			/* 20060221 請求書発行対象顧客とJOINし、請求書発行日を取得するよう変更 */
			INSERT INTO	wk_bl_kokyaku(
					kokyaku_cd_sk,						-- 顧客コードソートキー
					kakin_nengetu_sk,					-- 課金年月ソートキー
					print_type_cd_sk,					-- 用紙タイプソートキー
					dohu_tirashi_cd_sk,					-- 同封物コードソートキー
					oem_cd_sk,							-- OEMコードソートキー
					sk_yobi,							-- ソートキー予備
					seikyu_data,						-- 請求データ
					insert_date,						-- 登録日時
					insert_user)						-- 登録ユーザ
			SELECT	rec_wk1.kokyaku_cd,
					rec_wk1.seikyu_nengetu,
					CASE WHEN b.print_type_cd = '12' THEN
							'02'
						ELSE
							b.print_type_cd
					END,
					b.dohu_tirashi_cd,
					b.oem_cd,
					'00',
					/* 請求データ */
					/* キー情報 */
					rec_wk1.kokyaku_cd || rec_wk1.seikyu_nengetu ||
					CASE WHEN b.print_type_cd = '12' THEN
							'02'
						ELSE
							b.print_type_cd
					END ||
					b.dohu_tirashi_cd ||
					b.oem_cd ||
					/* 顧客基本情報 */
					rec_wk1.zip ||
					rec_wk1.addr1 ||
					rec_wk1.addr2 ||
					rec_wk1.kokyaku_name ||
					rec_wk1.kokyaku_tanto ||
					rec_wk1.kokyaku_tel ||
					/* 請求書発行情報 */
					RPAD(' ',15) ||
--					RPAD(' ',10) ||
					SUBSTR(c.seikyusho_hakko_date,1,4) || '.' ||
					SUBSTR(c.seikyusho_hakko_date,5,2) || '.' ||
					SUBSTR(c.seikyusho_hakko_date,7,2) ||
					RPAD(' ',10) ||
					aoba_out_common.get_mark(
						aoba_out_const.CST_OUT_FAOB010100,0) || 	-- 請求金額サイン
					'000000000' ||
					aoba_out_common.get_mark(
						aoba_out_const.CST_OUT_FAOB010100,0) || 	-- 課税対象額サイン
					'000000000' ||
					aoba_out_common.get_mark(
						aoba_out_const.CST_OUT_FAOB010100,0) || 	-- 非課税対象額サイン
					'000000000' ||
					aoba_out_common.get_mark(
						aoba_out_const.CST_OUT_FAOB010100,0) || 	-- 代行請求額サイン
					'000000000' ||
					aoba_out_common.get_mark(
						aoba_out_const.CST_OUT_FAOB010100,0) || 	-- 消費税サイン
					'000000000' ||
					RPAD(' ',7) ||
					aoba_out_common.get_mark(
						aoba_out_const.CST_OUT_FAOB010100,0) || 	-- 請求額1サイン
					'000000000' ||
					RPAD(' ',7) ||
					aoba_out_common.get_mark(
						aoba_out_const.CST_OUT_FAOB010100,0) || 	-- 請求額2サイン
					'000000000' ||
					RPAD(' ',7) ||
					aoba_out_common.get_mark(
						aoba_out_const.CST_OUT_FAOB010100,0) || 	-- 請求額3サイン
					'000000000' ||
					/* 口座振替時情報 */
					rec_wk1.ryoshusho_no ||
					rec_wk1.nyukingaku_sign ||
					SUBSTR(TO_CHAR(rec_wk1.nyukingaku,'000000009'),2,9) ||
					rec_wk1.nyukin_date ||
					rec_wk1.bank_name ||
					rec_wk1.shiten_name ||
					rec_wk1.koza_type_cd ||
					rec_wk1.koza_bng ||
					rec_wk1.koza_meigi ||
					rec_wk1.nengetu1 ||
					rec_wk1.nyukingaku_sign1 ||
					SUBSTR(TO_CHAR(rec_wk1.nyukingaku1,'000000009'),2,9) ||
					rec_wk1.nengetu2 ||
					rec_wk1.nyukingaku_sign2 ||
					SUBSTR(TO_CHAR(rec_wk1.nyukingaku2,'000000009'),2,9) ||
					rec_wk1.nengetu3 ||
					rec_wk1.nyukingaku_sign3 ||
					SUBSTR(TO_CHAR(rec_wk1.nyukingaku3,'000000009'),2,9) ||
					/* 郵便＋コンビニ情報 */
					RPAD('0',8,'0') ||
					RPAD('0',13,'0') ||
					RPAD('0',13,'0') ||
					RPAD('0',13,'0') ||
					RPAD('0',44,'0') ||
					/* カード情報 */
					RPAD(' ',16) ||
					' ' ||
					RPAD(' ',4),
					dSysdate,
					vUser
			FROM 	wk_bl_seikyu_sum b,
					wk_seikyu_hakko_kokyaku c
			WHERE 	b.kokyaku_cd = rec_wk1.kokyaku_cd
			AND		b.kokyaku_cd = c.kokyaku_cd
			AND		b.kakin_nengetu = c.kakin_nengetu
			AND		b.seikyu_gun = c.seikyu_gun
			AND 	rownum = 1
			AND 	b.data_type = CONST_DATA_TYPE_03;
			/* インサート時にソートは不要 */
--			ORDER BY b.seikyu_nengetu DESC;

			/* 請求書顧客マスタ格納一時TBL挿入（領収書） */
			/* 20060221 請求書発行対象顧客とJOINし、請求書発行日を取得するよう変更 */
			INSERT INTO	wk_bl_kokyaku_info(
			KOKYAKU_CD,				-- 顧客ｺｰﾄﾞ
			SEIKYU_NENGETU,			-- 請求年月
			PRINT_TYPE_CD,			-- 用紙ﾀｲﾌﾟ
			DOHU_TIRASHI_CD,		-- 同封物
			OEM_CD,					-- ｸﾞﾙｰﾌﾟ
			ZIP,					-- 郵便番号
			ADDR1,					-- 住所1
			ADDR2,					-- 住所2
			KOKYAKU_NAME,			-- 顧客名
			KOKYAKU_TANTO,			-- 顧客担当者名
			KOKYAKU_TEL,			-- 顧客電話番号
			SEIKYUSHO_NO,			-- 請求書発行No
			SEIKYUSHO_HAKKO_DATE,	-- 請求書発行日
			SHUNO_KIGEN,			-- 収納期限
			SEIKYUGAKU_SIGN,		-- 請求書金額ｻｲﾝ
			SEIKYUGAKU,				-- 請求金額
			KAZEI_TAISHOGAKU_SIGN,	-- 課税対象額ｻｲﾝ
			KAZEI_TAISHOGAKU,		-- 課税対象額
			HIKAZEI_TAISHOGAKU_SIGN,-- 非課税対象額ｻｲﾝ
			HIKAZEI_TAISHOGAKU,		-- 非課税対象額
			DAIKO_SEIKYUGAKU_SIGN,	-- 代行請求額ｻｲﾝ
			DAIKO_SEIKYUGAKU,		-- 代行請求額
			TAX_SIGN,				-- 消費税ｻｲﾝ
			TAX,					-- 消費税
			SEI_NENGETU1,			-- 請求/年月1
			SEIKYUGAKU_SIGN1,		-- 請求額1ｻｲﾝ
			SEIKYUGAKU1,			-- 請求額1
			SEI_NENGETU2,			-- 請求/年月2
			SEIKYUGAKU_SIGN2,		-- 請求額2ｻｲﾝ
			SEIKYUGAKU2,			-- 請求額2
			SEI_NENGETU3,			-- 請求/年月3
			SEIKYUGAKU_SIGN3,		-- 請求額3ｻｲﾝ
			SEIKYUGAKU3,			-- 請求額3
			RYOSHUSHO_NO,			-- 領収書発行No
			NYUKINGAKU_SIGN,		-- 入金額ｻｲﾝ
			NYUKINGAKU,				-- 入金額
			NYUKIN_DATE,			-- 入金年月日
			BANK_NAME,				-- 銀行名
			SHITEN_NAME,			-- 支店名
			KOZA_TYPE_CD,			-- 口座種別
			KOZA_BNG,				-- 口座No
			KOZA_MEIGI,				-- 口座名義
			NYU_NENGETU1,			-- 入金/年月1
			NYUKINGAKU_SIGN1,		-- 入金額1ｻｲﾝ
			NYUKINGAKU1,			-- 入金額1
			NYU_NENGETU2,			-- 入金/年月2
			NYUKINGAKU_SIGN2,		-- 入金額2ｻｲﾝ
			NYUKINGAKU2,			-- 入金額2
			NYU_NENGETU3,			-- 入金/年月3
			NYUKINGAKU_SIGN3,		-- 入金額3ｻｲﾝ
			NYUKINGAKU3,			-- 入金額3
			BAR_CD_JAN1,			-- ﾊﾞｰｺｰﾄﾞ1
			BAR_CD_JAN2,			-- ﾊﾞｰｺｰﾄﾞ2
			BAR_CD_JAN3,			-- ﾊﾞｰｺｰﾄﾞ3
			BAR_CD_JAN4,			-- ﾊﾞｰｺｰﾄﾞ4
			BAR_CD_EAN128,			-- ﾊﾞｰｺｰﾄﾞEAN128
			CARD_BNG,				-- ｶｰﾄﾞNo
			SAIHAKKO_FLG,			-- 再発行ﾌﾗｸﾞ
			FTPHONE_FLG,			-- FTﾌｫﾝﾌﾗｸﾞ
			/* 20060322 変更ﾌﾗｸﾞ対応 */
			MODIFY_FLG				-- 変更ﾌﾗｸﾞ
			)
			SELECT	rec_wk1.kokyaku_cd,
					rec_wk1.seikyu_nengetu,
					CASE WHEN b.print_type_cd = '12' THEN
							'02'
						ELSE
							b.print_type_cd
					END,
					b.dohu_tirashi_cd,
					b.oem_cd,
					/* 顧客基本情報 */
					rec_wk1.zip,
					rec_wk1.addr1,
					rec_wk1.addr2,
					rec_wk1.kokyaku_name,
					rec_wk1.kokyaku_tanto,
					rec_wk1.kokyaku_tel,
					/* 請求書発行情報 */
					NULL,
--					NULL,
					SUBSTR(c.seikyusho_hakko_date,1,4) || '.' ||
					SUBSTR(c.seikyusho_hakko_date,5,2) || '.' ||
					SUBSTR(c.seikyusho_hakko_date,7,2),
					NULL,
					aoba_out_common.get_mark(
						aoba_out_const.CST_OUT_FAOB010100,0),	-- 請求金額サイン
					0,
					aoba_out_common.get_mark(
						aoba_out_const.CST_OUT_FAOB010100,0),	-- 課税対象額サイン
					0,
					aoba_out_common.get_mark(
						aoba_out_const.CST_OUT_FAOB010100,0),	-- 非課税対象額サイン
					0,
					aoba_out_common.get_mark(
						aoba_out_const.CST_OUT_FAOB010100,0),	-- 代行請求額サイン
					0,
					aoba_out_common.get_mark(
						aoba_out_const.CST_OUT_FAOB010100,0),	-- 消費税サイン
					0,
					NULL,
					aoba_out_common.get_mark(
						aoba_out_const.CST_OUT_FAOB010100,0),	-- 請求額1サイン
					0,
					NULL,
					aoba_out_common.get_mark(
						aoba_out_const.CST_OUT_FAOB010100,0),	-- 請求額2サイン
					0,
					NULL,
					aoba_out_common.get_mark(
						aoba_out_const.CST_OUT_FAOB010100,0),	-- 請求額3サイン
					0,
					/* 口座振替時情報 */
					rec_wk1.ryoshusho_no,
					rec_wk1.nyukingaku_sign,
					TO_NUMBER(SUBSTR(TO_CHAR(rec_wk1.nyukingaku,'000000009'),2,9)),
					rec_wk1.nyukin_date,
					rec_wk1.bank_name,
					rec_wk1.shiten_name,
					rec_wk1.koza_type_cd,
					rec_wk1.koza_bng,
					rec_wk1.koza_meigi,
					TRIM(rec_wk1.nengetu1),
					rec_wk1.nyukingaku_sign1,
					TO_NUMBER(SUBSTR(TO_CHAR(rec_wk1.nyukingaku1,'000000009'),2,9)),
					TRIM(rec_wk1.nengetu2),
					rec_wk1.nyukingaku_sign2,
					TO_NUMBER(SUBSTR(TO_CHAR(rec_wk1.nyukingaku2,'000000009'),2,9)),
					TRIM(rec_wk1.nengetu3),
					rec_wk1.nyukingaku_sign3,
					TO_NUMBER(SUBSTR(TO_CHAR(rec_wk1.nyukingaku3,'000000009'),2,9)),
					/* 郵便＋コンビニ情報 */
					NULL,
					NULL,
					NULL,
					NULL,
					NULL,
					/* カード情報 */
					NULL,
					NULL,
					NULL,
					/* 20060322 変更ﾌﾗｸﾞ対応 */
					CASE WHEN b.print_type_cd = '12' THEN
							'Y'
						ELSE
							'N'
					END
--			FROM 	wk_bl_seikyu_sum b,
			FROM	(SELECT	b1.kokyaku_cd,
							b2.kakin_nengetu,
							b1.seikyu_gun,
							b1.print_type_cd,
							b1.dohu_tirashi_cd,
							b1.oem_cd
					FROM	wk_bl_seikyu_sum b1,
							(
							SELECT	kokyaku_cd,
									MAX(kakin_nengetu) kakin_nengetu,
									seikyu_gun
							FROM	wk_bl_seikyu_sum
							WHERE	kokyaku_cd = rec_wk1.kokyaku_cd
							AND		data_type = CONST_DATA_TYPE_03
							GROUP BY	kokyaku_cd,
										seikyu_gun
							) b2
					WHERE	b1.kokyaku_cd = b2.kokyaku_cd
					AND		b1.kakin_nengetu = b2.kakin_nengetu) b,
					wk_seikyu_hakko_kokyaku c
--			WHERE 	b.kokyaku_cd = rec_wk1.kokyaku_cd
			WHERE	b.kokyaku_cd = c.kokyaku_cd
			AND		b.kakin_nengetu = c.kakin_nengetu
			AND		b.seikyu_gun = c.seikyu_gun
			--AND 	rownum = 1
			;
			/* インサート時にソートは不要 */
--			ORDER BY b.seikyu_nengetu DESC;
		END IF;
	END LOOP;

END FAOB010103;
/
