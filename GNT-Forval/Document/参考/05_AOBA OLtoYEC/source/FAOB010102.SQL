CREATE OR REPLACE PROCEDURE        FAOB010102
/********************************************************************/
/*  関数名：FAOB010102												*/
/*  機能名：請求書データ作成：請求書データ抽出処理					*/
/*  作成者：田中　昭啓												*/
/*  作成日：2005/11/07												*/
/*  更新日：2006/05/15	田中	OEM取引形態コード対応				*/
/*        ：2006/06/30	田中	用紙タイプコード洗換処理追加		*/
/*	VER   ：1.01													*/
/*------------------------------------------------------------------*/
/*	引数：		無し												*/
/*	戻値：		無し												*/
/********************************************************************/
IS
	/* 全般的にNVL関数を使うよう変更 */
	/* ローカル定数 */
	CST_SAISEIKYU_FLG_Y		CONSTANT wk_seikyu_hakko_kokyaku.saiseikyu_flg%TYPE := 'Y';		--再請求フラグ(再請求)
	CST_SAISEIKYU_FLG_N		CONSTANT wk_seikyu_hakko_kokyaku.saiseikyu_flg%TYPE := 'N';		--再請求フラグ(当月請求)
	CST_RYOSHUSHO_FLG_Y		CONSTANT wk_seikyu_hakko_kokyaku.ryoshusho_flg%TYPE := 'Y';		--領収書フラグ(領収書)
	CST_RYOSHUSHO_FLG_N		CONSTANT wk_seikyu_hakko_kokyaku.ryoshusho_flg%TYPE := 'N';		--領収書フラグ(領収書以外)
--	CST_OEM_CODE			CONSTANT wk_seikyu_hakko_kokyaku.oem_keitai_cd%TYPE := '01';	--OEM取引形態コード
	CST_OEM_CODE2			CONSTANT wk_seikyu_hakko_kokyaku.oem_keitai_cd%TYPE := '02';	--OEM取引形態コード(データと印刷)
	CST_OEM_CODE3			CONSTANT wk_seikyu_hakko_kokyaku.oem_keitai_cd%TYPE := '03';	--OEM取引形態コード(印刷のみ)
	CST_DATA_TYPE1			CONSTANT wk_bl_seikyu_sum.data_type%TYPE := '01';				--データ種別(当月分請求書)
	CST_DATA_TYPE2			CONSTANT wk_bl_seikyu_sum.data_type%TYPE := '02';				--データ種別(再請求分請求書)
	CST_DATA_TYPE3			CONSTANT wk_bl_seikyu_sum.data_type%TYPE := '03';				--データ種別(領収書)
	CST_DEL_TABLE			CONSTANT VARCHAR2(20) := 'WK_BL_SEIKYU_SUM';					--削除対象テーブル名
	CST_PRINT_TYPE_CD_01	CONSTANT tb_seikyu_sum.print_type_cd%TYPE := '01';				--郵便口振の用紙タイプ
	CST_PRINT_TYPE_CD_05	CONSTANT tb_seikyu_sum.print_type_cd%TYPE := '05';				--郵便口振の用紙タイプ(領収書)
	CST_PRINT_TYPE_CD_11	CONSTANT tb_seikyu_sum.print_type_cd%TYPE := '11';				--はがき口振の用紙タイプ
	CST_PRINT_TYPE_CD_15	CONSTANT tb_seikyu_sum.print_type_cd%TYPE := '15';				--はがき口振の用紙タイプ(領収書)
	/* ローカル変数 */
	nRet					NUMBER;										--リターンコード
	dDate					wk_bl_seikyu_sum.update_date%TYPE;			--システム日付
	vUser					wk_bl_seikyu_sum.update_user%TYPE;			--バッチユーザ

	/* カーソル定義 */

BEGIN

	/* 請求サマリ一時TBL削除 */
	EXECUTE IMMEDIATE 'TRUNCATE TABLE ' || CST_DEL_TABLE;

	/* システム日付取得 */
	nRet := aoba_common.get_date(dDate);

	/* バッチユーザ取得 */
	nRet := aoba_common.get_user(vUser);

	/* 当月および、再請求分の請求書情報の作成 */
	INSERT INTO wk_bl_seikyu_sum
					(
					data_type,
					kokyaku_cd,
					oya_kokyaku_cd,
					oem_cd,
					oem_keisan_type_cd,
					kakin_nengetu,
					keijo_nengetu,
					seikyu_gun,
					kakingaku1,
					kakingaku2,
					kakingaku3,
					kakingaku4,
					kakingaku5,
					waribikigaku1,
					waribikigaku2,
					waribikigaku3,
					waribikigaku4,
					waribikigaku5,
					kazei_taishogaku,
					hikazei_taishogaku,
					daiko_seikyugaku,
					choseigaku,
					tax,
					menzeigaku,
					seikyugaku,
					inshizei,
					nyukingaku,
					nyukin_date,
					urikake_zandaka,
					urikake_teiseigaku,
					seikyu_saki_name,
					seikyu_saki_addr1,
					seikyu_saki_addr2,
					seikyu_saki_addr3,
					seikyu_saki_addr4,
					zip,
					kokyaku_tantosha,
					kokyaku_tel,
					shiharai_kbn_cd1,
					shiharai_kbn_cd2,
					nyukin_kbn_cd,
					shuno_kigen,
					bank_cd,
					bank_name,
					shiten_cd,
					shiten_name,
					koza_type_cd,
					koza_bng,
					koza_meigi,
					bar_cd_jan1,
					bar_cd_jan2,
					bar_cd_jan3,
					bar_cd_jan4,
					bar_cd_ean128,
					card_kaisha_cd,
					card_bng,
					yuko_kigen,
					card_shonin_bng,
					ryoshusho_flg,
					matome_flg,
					seikyu_nengetu,
					urikake_kakutei_flg,
					print_type_cd,
					seikyusho_hakko_flg,
					hurikae_kekka_cd,
					yubill_flg,
					koza_bng_hyoji_flg,
					dohu_tirashi_cd,
					seikyusho_hakko_date,
					id_num,
					oya_id_num,
					nyukin_flg,
					status_cd,
					insert_date,
					insert_user,
					update_date,
					update_user
					)
	SELECT	DECODE(b.saiseikyu_flg, CST_SAISEIKYU_FLG_N, CST_DATA_TYPE1, CST_SAISEIKYU_FLG_Y, CST_DATA_TYPE2, ''),
				a.kokyaku_cd,
				nvl(a.oya_kokyaku_cd,' '),
				nvl(a.oem_cd,' '),
				nvl(a.oem_keisan_type_cd,' '),
				nvl(a.kakin_nengetu,' '),
				nvl(a.keijo_nengetu,' '),
				nvl(a.seikyu_gun,' '),
				a.kakingaku1,
				a.kakingaku2,
				a.kakingaku3,
				a.kakingaku4,
				a.kakingaku5,
				a.waribikigaku1,
				a.waribikigaku2,
				a.waribikigaku3,
				a.waribikigaku4,
				a.waribikigaku5,
				a.kazei_taishogaku,
				a.hikazei_taishogaku,
				a.daiko_seikyugaku,
				a.choseigaku,
				a.tax,
				a.menzeigaku,
				a.seikyugaku,
				a.inshizei,
				a.nyukingaku,
				nvl(a.nyukin_date,' '),
				a.urikake_zandaka,
				a.urikake_teiseigaku,
				nvl(a.seikyu_saki_name,'　'),
				nvl(a.seikyu_saki_addr1,'　'),
				nvl(a.seikyu_saki_addr2,'　'),
				nvl(a.seikyu_saki_addr3,'　'),
				nvl(a.seikyu_saki_addr4,'　'),
				nvl(a.zip,' '),
				/* 1/26 NULL対策 */
				nvl(a.kokyaku_tantosha,'　'),
				nvl(a.kokyaku_tel,' '),
				nvl(a.shiharai_kbn_cd1,' '),
				nvl(a.shiharai_kbn_cd2,' '),
				nvl(a.nyukin_kbn_cd,' '),
				nvl(a.shuno_kigen,' '),
				nvl(a.bank_cd,' '),
				nvl(a.bank_name,'　'),
				nvl(a.shiten_cd,' '),
				nvl(a.shiten_name,'　'),
				nvl(a.koza_type_cd,' '),
				nvl(a.koza_bng,' '),
				nvl(a.koza_meigi,'　'),
				nvl(a.bar_cd_jan1,' '),
				nvl(a.bar_cd_jan2,' '),
				nvl(a.bar_cd_jan3,' '),
				nvl(a.bar_cd_jan4,' '),
				nvl(a.bar_cd_ean128,' '),
				nvl(a.card_kaisha_cd,' '),
				nvl(a.card_bng,' '),
				nvl(a.yuko_kigen,' '),
				nvl(a.card_shonin_bng,' '),
				a.ryoshusho_flg,
				a.matome_flg,
				a.seikyu_nengetu,
				a.urikake_kakutei_flg,
				nvl(a.print_type_cd,' '),
				a.seikyusho_hakko_flg,
				nvl(a.hurikae_kekka_cd,' '),
				nvl(a.yubill_flg,' '),
				nvl(a.koza_bng_hyoji_flg,' '),
				nvl(a.dohu_tirashi_cd,' '),
				nvl(a.seikyusho_hakko_date,' '),
				a.id_num,
				a.oya_id_num,
				a.nyukin_flg,
				a.status_cd,
				a.insert_date,
				a.insert_user,
				dDate,
				vUser
	FROM		tb_seikyu_sum a,
				wk_seikyu_hakko_kokyaku b
	WHERE		b.ryoshusho_flg = CST_RYOSHUSHO_FLG_N
--	AND			b.oem_keitai_cd <> CST_OEM_CODE
	AND			b.oem_keitai_cd IN (CST_OEM_CODE2,CST_OEM_CODE3)		-- 20060515 ADD
	/* 2005/12/08 条件を課金年月に変更 */
--	AND			b.seikyu_nengetu = a.seikyu_nengetu
	AND			a.kakin_nengetu = b.kakin_nengetu
	AND			a.seikyu_gun = b.seikyu_gun
	AND			a.kokyaku_cd = b.kokyaku_cd;
	/* 2006/01/26 インサート時にソートは不要 */
--	ORDER BY	a.kokyaku_cd,
--				a.kakin_nengetu;

	/* 領収書データ抽出 */
	INSERT INTO wk_bl_seikyu_sum
					(
					data_type,
					kokyaku_cd,
					oya_kokyaku_cd,
					oem_cd,
					oem_keisan_type_cd,
					kakin_nengetu,
					keijo_nengetu,
					seikyu_gun,
					kakingaku1,
					kakingaku2,
					kakingaku3,
					kakingaku4,
					kakingaku5,
					waribikigaku1,
					waribikigaku2,
					waribikigaku3,
					waribikigaku4,
					waribikigaku5,
					kazei_taishogaku,
					hikazei_taishogaku,
					daiko_seikyugaku,
					choseigaku,
					tax,
					menzeigaku,
					seikyugaku,
					inshizei,
					nyukingaku,
					nyukin_date,
					urikake_zandaka,
					urikake_teiseigaku,
					seikyu_saki_name,
					seikyu_saki_addr1,
					seikyu_saki_addr2,
					seikyu_saki_addr3,
					seikyu_saki_addr4,
					zip,
					kokyaku_tantosha,
					kokyaku_tel,
					shiharai_kbn_cd1,
					shiharai_kbn_cd2,
					nyukin_kbn_cd,
					shuno_kigen,
					bank_cd,
					bank_name,
					shiten_cd,
					shiten_name,
					koza_type_cd,
					koza_bng,
					koza_meigi,
					bar_cd_jan1,
					bar_cd_jan2,
					bar_cd_jan3,
					bar_cd_jan4,
					bar_cd_ean128,
					card_kaisha_cd,
					card_bng,
					yuko_kigen,
					card_shonin_bng,
					ryoshusho_flg,
					matome_flg,
					seikyu_nengetu,
					urikake_kakutei_flg,
					print_type_cd,
					seikyusho_hakko_flg,
					hurikae_kekka_cd,
					yubill_flg,
					koza_bng_hyoji_flg,
					dohu_tirashi_cd,
					seikyusho_hakko_date,
					id_num,
					oya_id_num,
					nyukin_flg,
					status_cd,
					insert_date,
					insert_user,
					update_date,
					update_user
					)
	SELECT	CST_DATA_TYPE3,
				a.kokyaku_cd,
				nvl(a.oya_kokyaku_cd,' '),
				nvl(a.oem_cd,' '),
				nvl(a.oem_keisan_type_cd,' '),
				a.kakin_nengetu,
				nvl(a.keijo_nengetu,' '),
				nvl(a.seikyu_gun,' '),
				a.kakingaku1,
				a.kakingaku2,
				a.kakingaku3,
				a.kakingaku4,
				a.kakingaku5,
				a.waribikigaku1,
				a.waribikigaku2,
				a.waribikigaku3,
				a.waribikigaku4,
				a.waribikigaku5,
				a.kazei_taishogaku,
				a.hikazei_taishogaku,
				a.daiko_seikyugaku,
				a.choseigaku,
				a.tax,
				a.menzeigaku,
				a.seikyugaku,
				a.inshizei,
				a.nyukingaku,
				nvl(a.nyukin_date,' '),
				a.urikake_zandaka,
				a.urikake_teiseigaku,
				nvl(a.seikyu_saki_name,'　'),
				nvl(a.seikyu_saki_addr1,'　'),
				nvl(a.seikyu_saki_addr2,'　'),
				nvl(a.seikyu_saki_addr3,'　'),
				nvl(a.seikyu_saki_addr4,'　'),
				nvl(a.zip,' '),
				/* 1/26 NULL対策 */
				nvl(a.kokyaku_tantosha,'　'),
				nvl(a.kokyaku_tel,' '),
				nvl(a.shiharai_kbn_cd1,' '),
				nvl(a.shiharai_kbn_cd2,' '),
				nvl(a.nyukin_kbn_cd,' '),
				nvl(a.shuno_kigen,' '),
				nvl(a.bank_cd,' '),
				nvl(a.bank_name,'　'),
				nvl(a.shiten_cd,' '),
				nvl(a.shiten_name,'　'),
				nvl(a.koza_type_cd,' '),
				nvl(a.koza_bng,' '),
				nvl(a.koza_meigi,'　'),
				nvl(a.bar_cd_jan1,' '),
				nvl(a.bar_cd_jan2,' '),
				nvl(a.bar_cd_jan3,' '),
				nvl(a.bar_cd_jan4,' '),
				nvl(a.bar_cd_ean128,' '),
				nvl(a.card_kaisha_cd,' '),
				nvl(a.card_bng,' '),
				nvl(a.yuko_kigen,' '),
				nvl(a.card_shonin_bng,' '),
				a.ryoshusho_flg,
				a.matome_flg,
				a.seikyu_nengetu,
				a.urikake_kakutei_flg,
				/* 20060630 用紙タイプコード洗換処理 */
				--nvl(a.print_type_cd,' '),
				DECODE(a.print_type_cd,
							CST_PRINT_TYPE_CD_11,CST_PRINT_TYPE_CD_15,
							CST_PRINT_TYPE_CD_01,CST_PRINT_TYPE_CD_05),
				a.seikyusho_hakko_flg,
				nvl(a.hurikae_kekka_cd,' '),
				nvl(a.yubill_flg,' '),
				nvl(a.koza_bng_hyoji_flg,' '),
				nvl(a.dohu_tirashi_cd,' '),
				nvl(a.seikyusho_hakko_date,' '),
				a.id_num,
				a.oya_id_num,
				a.nyukin_flg,
				a.status_cd,
				a.insert_date,
				a.insert_user,
				dDate,
				vUser
	FROM		tb_seikyu_sum a,
				wk_seikyu_hakko_kokyaku b
	WHERE		b.ryoshusho_flg = CST_RYOSHUSHO_FLG_Y
--	AND		b.oem_keitai_cd <> CST_OEM_CODE
	AND		b.oem_keitai_cd IN (CST_OEM_CODE2,CST_OEM_CODE3)	-- 20060515 ADD
	/* 2005/12/08 条件を課金年月に変更 */
--	AND		b.seikyu_nengetu = a.seikyu_nengetu
	AND		a.kakin_nengetu = b.kakin_nengetu
	AND		a.seikyu_gun = b.seikyu_gun
	AND		a.kokyaku_cd = b.kokyaku_cd;
	/* 2006/01/26 インサート時にソートは不要 */
--	ORDER BY	a.kokyaku_cd,
--				a.kakin_nengetu;

END FAOB010102;
/
