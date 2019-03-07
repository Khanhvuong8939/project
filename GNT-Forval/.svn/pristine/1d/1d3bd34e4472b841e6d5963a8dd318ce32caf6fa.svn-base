CREATE OR REPLACE PROCEDURE        FAGB010002(in_gun IN CHAR)      -- 請求群
/********************************************************************/
/* 関数名：FAGB010002                                               */
/* 機能名：合算（請求サマリ作成） 請求金額算出                      */
/* 作成者：岡田 成夫                                                */
/* 作成日：2005/11/24                                               */
/* 更新日：2006/01/25 TIS 成毛 課税額/非課税額/代行額を料金で設定   */
/*       ：           に修正                                        */
/* 更新日：2006/06/26 TIS 成毛 領収書発行ﾌﾗｸﾞの初期値を"X"に変更    */
/*       ：           "X":初期値(領収書発行なし)                    */
/*       ：             ※発行する場合は後続JOBで"N"(未発行)となる  */
/* 更新日：2006/10/09 木原　請求額=0の時の実入金日設定対応          */
/*                          実売掛確定ﾌﾗｸﾞの初期設定（N)対応        */
/*       ：2007/5/24 木原 請求額=0の時の実入金美設定値を修正        */
/*                  TB_SCHEDULE_MST.jitu_other_nyukin_date          */
/*                  →												*/
/*                  TB_SCHEDULE_MST.other_nyukin_date               */
/* VER   ：1.01                                                     */
/*------------------------------------------------------------------*/
/*      引数：      in_gun   請求群                                 */
/*      戻値：      なし                                            */
/*                                                                  */
/********************************************************************/
IS

	/* ローカル定数 */
	CST_NYUKIN_KBN_CD_41			CONSTANT tb_seikyu_sum.nyukin_kbn_cd%TYPE     := '41';	-- 入金区分コード　その他
	CST_SHIHARAI_KBN_KOUZA			CONSTANT tb_kokyaku_joho.shiharai_kbn_cd%TYPE := '1';	-- 支払区分コード　口座振替
	CST_SHIHARAI_KBN_YUBIN			CONSTANT tb_kokyaku_joho.shiharai_kbn_cd%TYPE := '2';	-- 支払区分コード　郵便振込
	CST_SHIHARAI_KBN_YUBIN_SHONIN	CONSTANT tb_kokyaku_joho.shiharai_kbn_cd%TYPE := '3';	-- 支払区分コード　郵便振込(承認あり)
	CST_SHIHARAI_KBN_YUBIN_MITUI	CONSTANT tb_kokyaku_joho.shiharai_kbn_cd%TYPE := '9';	-- 支払区分コード　郵便振込(三井予定)
	/* ローカル変数 */
	cKakinNengetu	tb_seikyu_sum.kakin_nengetu%TYPE;		-- 課金年月
	dSysDate		tb_seikyu_sum.insert_date%TYPE;			-- システム日時
	vUser			tb_seikyu_sum.insert_user%TYPE;			-- ユーザー
	nRet			NUMBER(1) := 0;							-- 戻り値

BEGIN

	/* システム日時取得 */
	nRet := aoba_common.get_date( dSysDate );               -- システム日時

	/* ユーザー取得 */
	nRet := aoba_common.get_user( vUser );                  -- ユーザー

	/* 課金年月取得 */
	nRet := aoba_common.get_kakin_nengetu( in_gun,          -- 請求群
	                                       cKakinNengetu ); -- 課金年月

	/* 請求サマリ作成 */
	INSERT INTO tb_seikyu_sum(
				kokyaku_cd,				--顧客コード
				oya_kokyaku_cd,			--親顧客コード
				oem_cd,					--OEMコード
				oem_keisan_type_cd,		--OEM計算種別コード
				kakin_nengetu,			--課金年月
				keijo_nengetu,			--計上年月
				seikyu_gun,				--請求群
				kakingaku1,				--課金額1
				kakingaku2,				--課金額2
				kakingaku3,				--課金額3
				kakingaku4,				--課金額4
				kakingaku5,				--課金額5
				waribikigaku1,			--割引額1
				waribikigaku2,			--割引額2
				waribikigaku3,			--割引額3
				waribikigaku4,			--割引額4
				waribikigaku5,			--割引額5
				kazei_taishogaku,		--課税対象額
				hikazei_taishogaku,		--非課税対象額
				daiko_seikyugaku,		--代行請求額
				choseigaku,				--調整額
				tax,					--消費税
				menzeigaku,				--免税額
				seikyugaku,				--請求額
				inshizei,				--印紙税
				nyukingaku,				--入金額
				nyukin_date,			--入金日
				urikake_zandaka,		--売掛残高
				urikake_teiseigaku,		--売掛訂正額
				seikyu_saki_name,		--請求先名称
				seikyu_saki_addr1,		--請求先住所1
				seikyu_saki_addr2,		--請求先住所2
				seikyu_saki_addr3,		--請求先住所3
				seikyu_saki_addr4,		--請求先住所4
				zip,					--郵便番号
				kokyaku_tantosha,		--顧客担当者名
				kokyaku_tel,			--顧客代表電話番号
				shiharai_kbn_cd1,		--支払区分コード1(初回)
				shiharai_kbn_cd2,		--支払区分コード2(直近)
				nyukin_kbn_cd,			--入金区分コード
				shuno_kigen,			--収納期限
				bank_cd,				--金融機関コード
				bank_name,				--金融機関名称
				shiten_cd,				--支店コード
				shiten_name,			--支店名称
				koza_type_cd,			--口座種別コード
				koza_bng,				--口座番号
				koza_meigi,				--口座名義
				bar_cd_jan1,			--バーコードJAN1
				bar_cd_jan2,			--バーコードJAN2
				bar_cd_jan3,			--バーコードJAN3
				bar_cd_jan4,			--バーコードJAN4
				bar_cd_ean128,			--バーコードEAN128
				card_kaisha_cd,			--カード会社コード
				card_bng,				--カード番号
				yuko_kigen,				--カード有効期限
				card_shonin_bng,		--カード承認番号
				ryoshusho_flg,			--領収書発行フラグ
				matome_flg,				--まとめフラグ
				seikyu_nengetu,			--請求年月
				urikake_kakutei_flg,	--売掛確定フラグ
				print_type_cd,			--請求書用紙種別コード
				seikyusho_hakko_flg,	--請求書発行フラグ
				hurikae_kekka_cd,		--振替結果コード
				yubill_flg,				--郵ビルフラグ
				koza_bng_hyoji_flg,		--口座番号表示属性フラグ
				seikyusho_hakko_date,	--請求書発行日
				id_num,					--ID数
				oya_id_num,				--親ID数
				nyukin_flg,				--入金フラグ
				status_cd,				--ステータスコード
				insert_date,			--登録日時
				insert_user,			--登録ユーザ
				update_date,			--更新日時
				update_user				--更新ユーザ
				,jitu_nyukin_date,		--実入金日　--20061009追加
　　　　　　　　jitu_urikake_kakutei_flg --実売掛確定ﾌﾗｸﾞ --20061009追加
				)
		SELECT
				a.kokyaku_cd,						--顧客コード
				a.oya_kokyaku_cd,					--親顧客コード
				a.oem_cd,							--OEMコード
				a.oem_keisan_type_cd,				--OEM計算種別
				a.kakin_nengetu,					--課金年月
				aoba_const.CST_NULL,				--計上年月
				a.seikyu_gun,						--請求群
				a.kakingaku1,						--課金額1
				a.kakingaku2,						--課金額2
				a.kakingaku3,						--課金額3
				a.kakingaku4,						--課金額4
				a.kakingaku5,						--課金額5
				a.waribikigaku1,					--割引額1
				a.waribikigaku2,					--割引額2
				a.waribikigaku3,					--割引額3
				a.waribikigaku4,					--割引額4
				a.waribikigaku5,					--割引額5
				a.kazei_taishogaku,					--課税対象額
				a.hikazei_taishogaku,				--非課税対象額額
				a.daiko_seikyugaku,					--代行請求額額
				a.choseigaku,						--調整額
				a.tax,								--消費税
				aoba_const.CST_NUM_ZERO,			--免税額
				a.seikyugaku,						--請求額
				aoba_const.CST_NUM_ZERO,			--印紙税
				aoba_const.CST_NUM_ZERO,			--入金額
				DECODE(a.seikyugaku,0,
						DECODE(d.shiharai_kbn_cd,
								CST_SHIHARAI_KBN_KOUZA, e.hurikae_nyukin_date,
								CST_SHIHARAI_KBN_YUBIN, e.cvs_nyukin_date,
								CST_SHIHARAI_KBN_YUBIN_SHONIN,e.cvs_nyukin_date,
								CST_SHIHARAI_KBN_YUBIN_MITUI,e.cvs_nyukin_date,
								e.other_nyukin_date),
						aoba_const.CST_NULL),		--入金日
				a.urikake_zandaka,					--売掛残高
				aoba_const.CST_NUM_ZERO,			--売掛訂正額
				aoba_const.CST_NULL,				--請求先名称
				aoba_const.CST_NULL,				--請求先住所1
				aoba_const.CST_NULL,				--請求先住所2
				aoba_const.CST_NULL,				--請求先住所3
				aoba_const.CST_NULL,				--請求先住所4
				aoba_const.CST_NULL,				--郵便番号
				aoba_const.CST_NULL,				--顧客担当者名
				aoba_const.CST_NULL,				--顧客代表電話
				aoba_const.CST_NULL,				--支払区分コード1
				aoba_const.CST_NULL,				--支払区分コード2
				DECODE(a.seikyugaku,0,
						CST_NYUKIN_KBN_CD_41,
						aoba_const.CST_NULL),		--入金区分コード
				aoba_const.CST_NULL,				--収納期限
				aoba_const.CST_NULL,				--金融機関コード
				aoba_const.CST_NULL,				--金融機関名称
				aoba_const.CST_NULL,				--支店コード
				aoba_const.CST_NULL,				--支店名称
				aoba_const.CST_NULL,				--口座種別コード
				aoba_const.CST_NULL,				--口座番号
				aoba_const.CST_NULL,				--口座名義
				aoba_const.CST_NULL,				--バーコードJAN1
				aoba_const.CST_NULL,				--バーコードJAN2
				aoba_const.CST_NULL,				--バーコードJAN3
				aoba_const.CST_NULL,				--バーコードJAN4
				aoba_const.CST_NULL,				--バーコードEAN1
				aoba_const.CST_NULL,				--カード会社コード
				aoba_const.CST_NULL,				--カード番号
				aoba_const.CST_NULL,				--カード有効期限
				aoba_const.CST_NULL,				--カード承認番号
				aoba_rate_const.CST_RYOSHUSHO_FLG_INIT,	--領収書発行フラグ --2006/06/26
				aoba_const.CST_N_FLG,				--まとめフラグ
				a.kakin_nengetu,					--請求年月
				aoba_const.CST_N_FLG,				--売掛確定フラク
				aoba_const.CST_NULL,				--請求書用紙種別コード
				aoba_const.CST_N_FLG,				--請求書発行フラグ
				aoba_rate_const.CST_KOUHURI_DEF_CD,	--振替結果コード
				aoba_const.CST_NULL,				--郵ビルフラグ
				aoba_const.CST_NULL,				--口座番号表示属性フラグ
				aoba_const.CST_NULL,				--請求書発行日
				aoba_const.CST_NULL,				--ID数
				aoba_const.CST_NULL,				--親ID数
				DECODE(a.seikyugaku,0,
						aoba_const.CST_FLG_ON,
						aoba_const.CST_FLG_OFF),	--入金フラグ
				aoba_rate_const.CST_STATUS_CD_00,	--ステータスコード
				dSysDate,							--登録日時
				vUser,								--登録ユーザ
				aoba_const.CST_NULL,				--更新日時
				aoba_const.CST_NULL					--更新ユーザ
				,DECODE(a.seikyugaku,0,											    --実入金日 --20061009追加
						DECODE(d.shiharai_kbn_cd,								    --実入金日 --20061009追加
								CST_SHIHARAI_KBN_KOUZA, e.jitu_hurikae_nyukin_date,	--実入金日 --20061009追加
								CST_SHIHARAI_KBN_YUBIN, e.cvs_nyukin_date,			--実入金日 --20061009追加
								CST_SHIHARAI_KBN_YUBIN_SHONIN,e.cvs_nyukin_date,	--実入金日 --20061009追加
								CST_SHIHARAI_KBN_YUBIN_MITUI,e.cvs_nyukin_date,		--実入金日 --20061009追加
/* 2007/05/24 修正(設定値変更) */
--								e.jitu_other_nyukin_date),							--実入金日 --20061009追加
								e.other_nyukin_date),							
/* end of 2007/05/24 修正(設定値変更) */
						aoba_const.CST_NULL),										--実入金日 --20061009追加
				aoba_const.CST_N_FLG				--実売掛確定フラク --20061009追加
		FROM	(
				SELECT	kokyaku_cd,
						oya_kokyaku_cd,
						oem_cd,
						oem_keisan_type_cd,
						kakin_nengetu,
						seikyu_gun,
						SUM(NVL(kakingaku1,0))			kakingaku1,
						SUM(NVL(kakingaku2,0))			kakingaku2,
						SUM(NVL(kakingaku3,0))			kakingaku3,
						SUM(NVL(kakingaku4,0))			kakingaku4,
						SUM(NVL(kakingaku5,0))			kakingaku5,
						SUM(NVL(waribikigaku1,0))		waribikigaku1,
						SUM(NVL(waribikigaku2,0))		waribikigaku2,
						SUM(NVL(waribikigaku3,0))		waribikigaku3,
						SUM(NVL(waribikigaku4,0))		waribikigaku4,
						SUM(NVL(waribikigaku5,0))		waribikigaku5,
						SUM(NVL(kazei_taishogaku,0))	kazei_taishogaku,
						SUM(NVL(hikazei_taishogaku,0))	hikazei_taishogaku,
						SUM(NVL(daiko_seikyugaku,0))	daiko_seikyugaku,
						SUM(NVL(choseigaku,0))			choseigaku,
						SUM(NVL(tax,0)) 				tax,
						SUM(NVL(kakingaku1,0) + NVL(kakingaku2,0) + NVL(kakingaku3,0) +
							NVL(kakingaku4,0) + NVL(kakingaku5,0) +
							NVL(waribikigaku1,0) + NVL(waribikigaku2,0) + NVL(waribikigaku3,0) +
							NVL(waribikigaku4,0) + NVL(waribikigaku5,0) +
							NVL(choseigaku,0) + NVL(tax,0))	seikyugaku,
						SUM(NVL(kakingaku1,0) + NVL(kakingaku2,0) + NVL(kakingaku3,0) +
							NVL(kakingaku4,0) + NVL(kakingaku5,0) +
							NVL(waribikigaku1,0) + NVL(waribikigaku2,0) + NVL(waribikigaku3,0) +
							NVL(waribikigaku4,0) + NVL(waribikigaku5,0) +
							NVL(choseigaku,0) + NVL(tax,0))	urikake_zandaka
				FROM	(
						SELECT	b1.kokyaku_cd,
								b1.oya_kokyaku_cd,
								b1.oem_cd,
								b1.oem_keisan_type_cd,
								b1.kakin_nengetu,
								b1.seikyu_gun,
								DECODE(SUBSTRB(b1.ryokin_cd2,1,1),
										aoba_rate_const.CST_RYOKIN_CD2_1,b1.teika,
										aoba_rate_const.CST_RYOKIN_CD2_2,b1.teika,0)	kakingaku1,
								DECODE(SUBSTRB(b1.ryokin_cd2,1,1),aoba_rate_const.CST_RYOKIN_CD2_3,b1.teika,0)			kakingaku2,
								DECODE(SUBSTRB(b1.ryokin_cd2,1,1),aoba_rate_const.CST_RYOKIN_CD2_4,b1.teika,0)			kakingaku3,
								DECODE(SUBSTRB(b1.ryokin_cd2,1,1),aoba_rate_const.CST_RYOKIN_CD2_5,b1.teika,0)			kakingaku4,
								DECODE(SUBSTRB(b1.ryokin_cd2,1,1),aoba_rate_const.CST_RYOKIN_CD2_6,b1.teika,0)			kakingaku5,
								DECODE(SUBSTRB(b1.ryokin_cd2,1,1),aoba_rate_const.CST_RYOKIN_CD2_7,b1.teika,0)			waribikigaku1,
								DECODE(b1.waribikigaku,0,0,b1.waribikigaku)	+
									DECODE(SUBSTRB(b1.ryokin_cd2,1,1),aoba_rate_const.CST_RYOKIN_CD2_8,b1.teika,0)		waribikigaku2,
								DECODE(SUBSTRB(b1.ryokin_cd2,1,1),aoba_rate_const.CST_RYOKIN_CD2_9,b1.teika,0)			waribikigaku3,
								DECODE(SUBSTRB(b1.ryokin_cd2,1,1),aoba_rate_const.CST_RYOKIN_CD2_A,b1.teika,0)			waribikigaku4,
								DECODE(SUBSTRB(b1.ryokin_cd2,1,1),aoba_rate_const.CST_RYOKIN_CD2_B,b1.teika,0)			waribikigaku5,
								DECODE(b1.tax_flg,aoba_rate_const.CST_TAX_FLG_Y,b1.ryokin,0)							kazei_taishogaku,
								DECODE(b1.tax_flg,aoba_rate_const.CST_TAX_FLG_N,b1.ryokin,0)							hikazei_taishogaku,
								DECODE(b1.tax_flg,aoba_rate_const.CST_TAX_FLG_D,b1.ryokin,0)							daiko_seikyugaku,
								DECODE(SUBSTRB(b1.ryokin_cd2,1,1),aoba_rate_const.CST_RYOKIN_CD2_C,b1.teika,0)			choseigaku,
								b1.tax
						FROM 	tb_seikyu_utiwake	b1,		-- 請求内訳
								wk_kokyaku_oem		b2		-- 顧客OEM対照表
						WHERE	b1.kokyaku_cd = b2.kokyaku_cd
						AND		b1.oem_cd = b2.oem_cd
						AND		b1.srv_type_cd = b2.srv_type_cd
						AND		b1.kakin_nengetu = b2.kakin_nengetu
						AND		b1.seikyu_gun = in_gun
						AND		b1.kakin_nengetu = cKakinNengetu
						AND		b1.rec_type_cd = aoba_rate_const.CST_REC_TYPE_CD_10
						) c
						GROUP BY kokyaku_cd,oya_kokyaku_cd,oem_cd,oem_keisan_type_cd,kakin_nengetu,seikyu_gun
				) a,
				tb_kokyaku_joho d,		-- 顧客基本情報
				tb_schedule_mst e		-- スケジュールマスタ
		WHERE	a.kokyaku_cd = d.kokyaku_cd
		AND		a.kakin_nengetu = e.kakin_nengetu
		AND		a.seikyu_gun = e.seikyu_gun
		AND		a.oem_cd = e.oem_cd;

END FAGB010002;
/
