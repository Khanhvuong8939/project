CREATE OR REPLACE PROCEDURE        FAOB010105
/********************************************************************/
/*	関数名：FAOB010105												*/
/*	機能名：請求書データ作成：請求書通話明細１，３編集				*/
/*	作成者：田中　昭啓												*/
/*	作成日：2005/11/10												*/
/*	更新日：2006/02/24	田中	通話開始時刻対応					*/
/*								金額切り捨て対応					*/
/*		  ：2006/02/28	田中	着信系自由適用欄にID裏番設定		*/
/*		  ：2006/03/01	田中	自由適用欄のID裏番の前に			*/
/*                              固定値【課金電話番号：】を追加		*/
/*		  ：2006/04/10	田中	支払区分(12→02)のものは出力しない	*/
/*        ：2006/04/21	田中	電話番号計のグループから、			*/
/*                              印字用IDを削除						*/
/*	VER   ：1.04													*/
/*------------------------------------------------------------------*/
/*	引数：		無し												*/
/*	戻値：		無し												*/
/********************************************************************/
IS

	/* ローカル定数 */
	CST_MEISAI_TYPE1		CONSTANT tb_srv_type_mst.meisai_type%TYPE := '21';			--請求書明細種別(発信系)
	CST_MEISAI_TYPE2		CONSTANT tb_srv_type_mst.meisai_type%TYPE := '22';			--請求書明細種別(着信系)
	CST_DATA_TYPE1			CONSTANT wk_bl_seikyu_sum.data_type%TYPE := '01';			--データ種別(当月分請求書)
	CST_PTN_SK1				CONSTANT wk_bl_tuwa_meisai.ptn_sk%TYPE := '01';				--パターンソートキー(ヘッダー１)
	CST_PTN_SK2				CONSTANT wk_bl_tuwa_meisai.ptn_sk%TYPE := '02';				--パターンソートキー(ヘッダー２)
	CST_PTN_SK5				CONSTANT wk_bl_tuwa_meisai.ptn_sk%TYPE := '05';				--パターンソートキー(データ)
	CST_PTN_SK9				CONSTANT wk_bl_tuwa_meisai.ptn_sk%TYPE := '09';				--パターンソートキー(エンド)
	CST_DATA_KBN_01			CONSTANT CHAR(1) := '1';									--データ区分(明細)
	CST_DATA_KBN_06			CONSTANT CHAR(1) := '6';									--データ区分(ID計)
	CST_DATA_KBN_07			CONSTANT CHAR(1) := '7';									--データ区分(顧客部署コード計)
	CST_DATA_KBN_08			CONSTANT CHAR(1) := '8';									--データ区分(課金年月計)
	CST_DATA_KBN_09			CONSTANT CHAR(1) := '9';									--データ区分(総合計)
	CST_DATA_KBN_SK00		CONSTANT wk_bl_tuwa_meisai.data_kbn_sk%TYPE := '00';		--データ区分ソートキー(ヘッダー)
	CST_DATA_KBN_SK01		CONSTANT wk_bl_tuwa_meisai.data_kbn_sk%TYPE := '01';		--データ区分ソートキー(明細)
	CST_DATA_KBN_SK06		CONSTANT wk_bl_tuwa_meisai.data_kbn_sk%TYPE := '06';		--データ区分ソートキー(ID計)
	CST_DATA_KBN_SK07		CONSTANT wk_bl_tuwa_meisai.data_kbn_sk%TYPE := '07';		--データ区分ソートキー(顧客部署コード計)
	CST_DATA_KBN_SK08		CONSTANT wk_bl_tuwa_meisai.data_kbn_sk%TYPE := '08';		--データ区分ソートキー(課金年月計)
	CST_DATA_KBN_SK09		CONSTANT wk_bl_tuwa_meisai.data_kbn_sk%TYPE := '09';		--データ区分ソートキー(総合計)
	CST_DATA_KBN_SKZZ		CONSTANT wk_bl_tuwa_meisai.data_kbn_sk%TYPE := 'ZZ';		--データ区分ソートキー(エンド)
	CST_MEISAI_PTN1			CONSTANT CHAR(1) := '1';									--明細データパターン１
	CST_MEISAI_PTN2			CONSTANT CHAR(1) := '2';									--明細データパターン２
	CST_MEISAI_PTN5			CONSTANT CHAR(1) := '5';									--明細データパターン５
	CST_MEISAI_PTN9			CONSTANT CHAR(1) := '9';									--エンドデータパターン
	CST_DEL_TABLE			CONSTANT VARCHAR2(20) := 'WK_BL_TUWA_MEISAI';				--削除対象テーブル名
	CST_DEL_TABLE2			CONSTANT VARCHAR2(20) := 'WK_TUWA_MEISAI';					--削除対象テーブル名２
	CST_KINGAKU_YUKO_NO		CONSTANT NUMBER := 7;										--金額有効桁数
	/* ローカル変数 */
	nRet					NUMBER;														--リターンコード
	dDate					wk_bl_seikyu_sum.update_date%TYPE;							--システム日付
	vUser					wk_bl_seikyu_sum.update_user%TYPE;							--バッチユーザ
	nCnt1					NUMBER;														--データ件数カウント用
	cMeisai					tb_srv_type_mst.meisai_type%TYPE;							--請求書明細種別
	nSeqno					NUMBER;															--連番取得用

	/* 2006/01/05 パフォーマンス対応 */
	/* カーソル定義 */
	CURSOR cur_tuwa_meisai(in_meisai_type tb_srv_type_mst.meisai_type%TYPE)
	IS
		SELECT	SUBSTRB(kaisen_meisai_data,1,14) meisai_a,
		 		SUBSTRB(kaisen_meisai_data,24) meisai_b
		FROM	wk_bl_tuwa_meisai
		WHERE	meisai_type = in_meisai_type
		/* 20060204 ソート変更 */
		/* 20060224 ソート変更 */
		ORDER BY	kokyaku_cd_sk,
					kakin_nengetu_sk,
					ptn_sk,
					id_sk,
					tuwa_kbn_sk,
					tuwa_start_date_sk,
					tuwa_start_time_sk,		--20060224
					data_kbn_sk
					--kokyaku_cd_sk,
--					tuwa_kbn_sk,
--					tuwa_time_sk
		FOR UPDATE;

	rec_tuwa_meisai		cur_tuwa_meisai%ROWTYPE;

BEGIN

	/* 請求書通話明細一時TBL削除 */
	EXECUTE IMMEDIATE 'TRUNCATE TABLE ' || CST_DEL_TABLE;

	/* 2006/1/31 ワークテーブル追加 */
	/* 請求書通話明細作成ﾜｰｸ削除 */
	EXECUTE IMMEDIATE 'TRUNCATE TABLE ' || CST_DEL_TABLE2;

	/* システム日付取得 */
	nRet := aoba_common.get_date(dDate);

	/* バッチユーザ取得 */
	nRet := aoba_common.get_user(vUser);

	/* 2006/1/31 ワークテーブル登録処理追加 */
	/* 請求書通話明細作成ﾜｰｸtbl登録 */
	INSERT INTO wk_tuwa_meisai(
			meisai_type,
			kakin_nengetu,
			seikyu_gun,
			kokyaku_cd,
			oem_cd,
			srv_type_cd,
			id,
			print_id,
			tuwa_kbn_cd,
			tuwa_kbn_name,
			ryokin_cd1,
			ryokin_cd2,
			waribiki_tuwaryo,
			tuwa_start_date,
			tuwa_start_time,
			tuwa_time,
			tuwa_unit,
			aitesaki_tel,
			from_areaname,
			to_areaname,
			from_jigyosha,
			id_ura,
			print_id_ura,
			meisai_tel_hyoji_flg
			)
	SELECT	c.meisai_type,
			b.kakin_nengetu,
			b.seikyu_gun,
			b.kokyaku_cd,
			b.oem_cd,
			b.srv_type_cd,
			b.id,
			b.print_id,
			b.tuwa_kbn_cd,
			b.tuwa_kbn_name,
			b.ryokin_cd1,
			b.ryokin_cd2,
			b.waribiki_tuwaryo,
			b.tuwa_start_date,
			b.tuwa_start_time,
			b.tuwa_time,
			b.tuwa_unit,
			b.aitesaki_tel,
			b.from_areaname,
			b.to_areaname,
			b.from_jigyosha,
			b.id_ura,
			b.print_id_ura,
			b.meisai_tel_hyoji_flg
	FROM	wk_bl_seikyu_sum a,
			tb_tuwa_meisai b,
			tb_srv_type_mst c,
			wk_bl_kokyaku_info d					-- 20060410
	WHERE	b.srv_type_cd = c.srv_type_cd
	AND		a.data_type = CST_DATA_TYPE1
	AND		a.kokyaku_cd = d.kokyaku_cd				-- 20060410
	AND		a.seikyu_nengetu = d.seikyu_nengetu		-- 20060410
	AND		d.modify_flg = 'N'						-- 20060410
	AND		a.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_11
	AND		a.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_13
	AND		a.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_14
	AND		a.kakin_nengetu = b.kakin_nengetu
	AND		a.seikyu_gun = b.seikyu_gun
	AND		a.kokyaku_cd = b.kokyaku_cd
	AND		b.meisai_tel_hyoji_flg IS NOT NULL;

	/* ループ開始 */
	FOR i IN 0..1 LOOP
		/* i = 0 発信系 */
		/* i = 1 着信系 */

		/* 明細種別選定 */
		IF i = 0 THEN
			cMeisai := CST_MEISAI_TYPE1;
		ELSIF i = 1 THEN
			cMeisai := CST_MEISAI_TYPE2;
		END IF;

		/* 件数カウント */
		/* 2006/01/31 ワークテーブル追加に伴う変更 */
		SELECT	COUNT(*)
		INTO	nCnt1
		FROM	wk_tuwa_meisai
		WHERE	meisai_type = cMeisai;
/*		FROM	wk_bl_seikyu_sum a,
				tb_tuwa_meisai b,
				tb_srv_type_mst c
		WHERE	b.srv_type_cd = c.srv_type_cd
		AND		c.meisai_type = cMeisai
		AND		a.data_type = CST_DATA_TYPE1
		AND		a.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_11
		AND		a.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_13
		AND		a.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_14
		AND		a.kakin_nengetu = b.kakin_nengetu
		AND		a.seikyu_gun = b.seikyu_gun
		AND		a.kokyaku_cd = b.kokyaku_cd
		AND		b.meisai_tel_hyoji_flg IS NOT NULL;*/

		IF nCnt1 > 0 THEN
			/* ヘッダーレコード１作成 */
			INSERT INTO wk_bl_tuwa_meisai(
						meisai_type,				--請求書明細種別
						ptn_sk,						--パターンソートキー
						data_kbn_sk,				--データ区分ソートキー
						kokyaku_cd_sk,				--顧客コードソートキー
						kakin_nengetu_sk,			--課金年月ソートキー
						id_sk,						--IDソートキー
						tuwa_kbn_sk,				--通話区分コードソートキー
						tuwa_start_date_sk,			--通話開始日ソートキー
						/* 20060224 通話開始時間対応 */
						--tuwa_time_sk,				--通話時間ソートキー
						tuwa_start_time_sk,			--通話開始時刻ソートキー
						sk_yobi,					--ソートキー予備
						kaisen_meisai_data,			--請求書回線明細データ
						insert_date,				--登録日時
						insert_user,				--登録ユーザ
						update_date,				--更新日
						update_user					--更新ユーザ
						)
			SELECT	cMeisai,
						CST_PTN_SK1,
						CST_DATA_KBN_SK00,
						a.kokyaku_cd,
						a.kakin_nengetu,
						'00000000000000000000',
						'0000',
						'00000000',
--						'0000000',
						'000000',		--20060224
						'00',
						/* 請求書通話明細データ 開始 */
						a.kokyaku_cd	||											--顧客コード
						a.kakin_nengetu	||											--課金年月
						'000000000'		||											--連番
						CST_MEISAI_PTN1 ||											--パターン
						a.oem_cd		||											--グループ
						DECODE(i,0,
								'ワンビリングサービス／通話明細　　　　　' ||
								'　　　　　　　　　　　　　　　　　　　　' ||
								'　　　　　　　　　　　　　　　　　　　　',
								1,
								'ワンビリングサービス／通話明細（着電）　' ||
								'　　　　　　　　　　　　　　　　　　　　' ||
								'　　　　　　　　　　　　　　　　　　　　' ) ||		--サービス名
							RPAD(' ',108),											--予備
						/* 請求書通話明細データ 終了 */
						dDate,
						vUser,
						NULL,
						NULL
			FROM		(
			/* 2005/01/05 パフォーマンス対応 */
/*						SELECT	DISTINCT	b.kokyaku_cd,
											b.kakin_nengetu,
											b.oem_cd
						FROM	wk_bl_seikyu_sum a,
								tb_tuwa_meisai b,
								tb_srv_type_mst c
						WHERE	b.srv_type_cd = c.srv_type_cd
						AND		c.meisai_type = cMeisai
						AND		a.data_type = CST_DATA_TYPE1
						AND		a.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_11
						AND		a.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_13
						AND		a.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_14
						AND		a.kakin_nengetu = b.kakin_nengetu
						AND		a.seikyu_gun = b.seikyu_gun
						AND		a.kokyaku_cd = b.kokyaku_cd
						AND		b.meisai_tel_hyoji_flg IS NOT NULL*/
						/* 2006/01/31 ワークテーブル追加に伴う変更 */
						SELECT	a.kokyaku_cd,
								a.kakin_nengetu,
								a.oem_cd
						FROM	wk_bl_seikyu_sum a
						/*WHERE	EXISTS (select 'x' from tb_tuwa_meisai b,tb_srv_type_mst c
										where	a.kokyaku_cd = b.kokyaku_cd
										and		a.kakin_nengetu = b.kakin_nengetu
										and		a.seikyu_gun = b.seikyu_gun
										and		b.srv_type_cd = c.srv_type_cd
										AND		b.meisai_tel_hyoji_flg IS NOT NULL
										AND		c.meisai_type = cMeisai
										)
										*/
						WHERE	EXISTS (SELECT	'x'
										FROM	wk_tuwa_meisai b
										WHERE	a.kokyaku_cd = b.kokyaku_cd
										AND		a.kakin_nengetu = b.kakin_nengetu
										AND		a.seikyu_gun = b.seikyu_gun
										AND		b.meisai_type = cMeisai
										)
--						AND		a.data_type = CST_DATA_TYPE1
--						AND		a.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_11
--						AND		a.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_13
--						AND		a.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_14
						) a;

			/* ヘッダーレコード２作成 */
			INSERT INTO wk_bl_tuwa_meisai(
						meisai_type,				--請求書明細種別
						ptn_sk,						--パターンソートキー
						data_kbn_sk,				--データ区分ソートキー
						kokyaku_cd_sk,				--顧客コードソートキー
						kakin_nengetu_sk,			--課金年月ソートキー
						id_sk,						--IDソートキー
						tuwa_kbn_sk,				--通話区分コードソートキー
						tuwa_start_date_sk,			--通話開始日ソートキー
						/* 20060224 通話開始時刻ソートキー */
--						tuwa_time_sk,				--通話時間ソートキー
						tuwa_start_time_sk,			--通話開始時刻ソートキー
						sk_yobi,					--ソートキー予備
						kaisen_meisai_data,			--請求書回線明細データ
						insert_date,				--登録日時
						insert_user,				--登録ユーザ
						update_date,				--更新日
						update_user					--更新ユーザ
						)
			SELECT		cMeisai,
						CST_PTN_SK2,
						CST_DATA_KBN_SK00,
						a.kokyaku_cd,
						a.kakin_nengetu,
						'00000000000000000000',
						'0000',
						'00000000',
--						'0000000',
						'000000',
						'00',
						/* 請求書通話明細データ 開始 */
						DECODE(i,0,
								/* 発信系 */
								a.kokyaku_cd ||					--顧客コード
								a.kakin_nengetu ||				--課金年月
								'000000000' ||					--連番
								CST_MEISAI_PTN2 ||				--パターン
								a.oem_cd ||						--グループ
								'通話開始日　　　　　' ||		--通話開始日
								'通話開始時刻　　　　' ||		--通話開始時刻
								'通話区分　　　　　　' ||		--通話区分
								'通話時間　　　　　　' ||		--通話時間
								'度数　　　' ||					--度数
								'金額　　　' ||					--金額
								'対話地　　' ||					--対話地
								'相手先電話番号　　　' ||		--相手先電話番号
								RPAD(' ',98),					--予備
								1,
								/* 着信系 */
								a.kokyaku_cd ||					--顧客コード
								a.kakin_nengetu ||				--課金年月
								'000000000' ||					--連番
								CST_MEISAI_PTN2 ||				--パターン
								a.oem_cd ||						--グループ
								'通話開始日　　　　　' ||		--通話開始日
								'通話開始時刻　　　　' ||		--通話開始時刻
								'通話区分　　　　　　' ||		--通話区分
								'通話時間　　　　　　' ||		--通話時間
								'度数　　　' ||					--度数
								'金額　　　' ||					--金額
								'発地域　　' ||					--発地域
								'発地域／発事業者　　' ||		--発地域／発事業者
								RPAD(' ',98)					--予備
								),
						/* 請求書通話明細データ 終了 */
						dDate,
						vUser,
						NULL,
						NULL
			FROM		(
						/* 2005/01/05 パフォーマンス対応 */
						/*SELECT	DISTINCT	b.kokyaku_cd,
											b.kakin_nengetu,
											b.oem_cd
						FROM	wk_bl_seikyu_sum a,
								tb_tuwa_meisai b,
								tb_srv_type_mst c
						WHERE	b.srv_type_cd = c.srv_type_cd
						AND		c.meisai_type = cMeisai
						AND		a.data_type = CST_DATA_TYPE1
						AND		a.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_11
						AND		a.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_13
						AND		a.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_14
						AND		a.kakin_nengetu = b.kakin_nengetu
						AND		a.seikyu_gun = b.seikyu_gun
						AND		a.kokyaku_cd = b.kokyaku_cd
						AND		b.meisai_tel_hyoji_flg IS NOT NULL*/
						/* 2006/01/31 ワークテーブル追加に伴う変更 */
						SELECT	a.kokyaku_cd,
								a.kakin_nengetu,
								a.oem_cd
						FROM	wk_bl_seikyu_sum a
						/*WHERE	EXISTS (select 'x' from tb_tuwa_meisai b,tb_srv_type_mst c
										where	a.kokyaku_cd = b.kokyaku_cd
										and		a.kakin_nengetu = b.kakin_nengetu
										and		a.seikyu_gun = b.seikyu_gun
										and		b.srv_type_cd = c.srv_type_cd
										AND		b.meisai_tel_hyoji_flg IS NOT NULL
										AND		c.meisai_type = cMeisai
										)
										*/
						WHERE	EXISTS (SELECT	'x'
										FROM	wk_tuwa_meisai b
										WHERE	a.kokyaku_cd = b.kokyaku_cd
										AND		a.kakin_nengetu = b.kakin_nengetu
										AND		a.seikyu_gun = b.seikyu_gun
										AND		b.meisai_type = cMeisai
										)
--						AND		a.data_type = CST_DATA_TYPE1
--						AND		a.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_11
--						AND		a.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_13
--						AND		a.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_14
						) a;

			/* データレコード作成 */
			/* 明細 */
			INSERT INTO wk_bl_tuwa_meisai(
						meisai_type,				--請求書明細種別
						ptn_sk,						--パターンソートキー
						data_kbn_sk,				--データ区分ソートキー
						kokyaku_cd_sk,				--顧客コードソートキー
						kakin_nengetu_sk,			--課金年月ソートキー
						id_sk,						--IDソートキー
						tuwa_kbn_sk,				--通話区分コードソートキー
						tuwa_start_date_sk,			--通話開始日ソートキー
						/* 20060224 通話開始時刻対応 */
--						tuwa_time_sk,				--通話時間ソートキー
						tuwa_start_time_sk,			--通話開始時刻ソートキー
						sk_yobi,					--ソートキー予備
						kaisen_meisai_data,			--請求書回線明細データ
						insert_date,				--登録日時
						insert_user,				--登録ユーザ
						update_date,				--更新日
						update_user					--更新ユーザ
						)
			/* 2006/01/31 ワークテーブル追加に伴う変更 */
			/* 2006/02/15 全体的に自由摘要欄にnull対策 */
			/* 2006/02/24 通話開始時刻対応 */
			SELECT		cMeisai,
						CST_PTN_SK5,
						CST_DATA_KBN_SK01,
						kokyaku_cd,
						kakin_nengetu,
						id,
						tuwa_kbn_cd,
						tuwa_start_date,
--						tuwa_time,
						tuwa_start_time,		--20060224
						'00',
						/* 請求書通話明細データ 開始 */
						DECODE(i,0,
								/* 発信系 */
								kokyaku_cd ||																			--顧客コード
								kakin_nengetu ||																		--課金年月
								'000000000' ||																			--連番
								CST_MEISAI_PTN5 ||																		--パターン
								oem_cd ||																				--グループ
								CST_DATA_KBN_01 ||																		--データ区分
								RPAD(print_id,20,' ') ||																--電話番号
								/* 20060217 自由摘要欄は全部空白埋め */
--								RPAD(nvl(aoba_out_common.get_riyou_kikan(kakin_nengetu, seikyu_gun, srv_type_cd,
--																		kokyaku_cd, id, ryokin_cd1, ryokin_cd2),'　'),80,'　'
--									) ||																				--自由使用欄
								RPAD('　',80,'　') ||																	--自由使用欄
								TO_CHAR(TO_DATE(tuwa_start_date,'YYYY/MM/DD'),'YYYY/MM/DD') ||							--通話開始日
								RPAD(TO_CHAR(TO_DATE(tuwa_start_time,'HH24:MI:SS'),'HH24:MI:SS'),9,' ') ||				--通話開始時刻
								RPAD(tuwa_kbn_name,16,'　') ||															--通話区分
								SUBSTR(tuwa_time,2,2) || ':' || SUBSTR(tuwa_time,4,2) || ':' || SUBSTR(tuwa_time,6,2) || ' '
								 ||																						--通話時間
								SUBSTRB(LPAD(tuwa_unit,10,'0'),4,7) ||													--度数
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,waribiki_tuwaryo) ||			--金額サイン
								/* 20060215 端数切捨て */
								/* 20060224 明細は切捨て不要 */
								CASE	WHEN	LENGTHB(waribiki_tuwaryo) > CST_KINGAKU_YUKO_NO THEN
													TO_CHAR(ABS(waribiki_tuwaryo)*100)
										WHEN	LENGTHB(waribiki_tuwaryo) <= CST_KINGAKU_YUKO_NO THEN
													LPAD(ABS(waribiki_tuwaryo)*100,9,'0')
								END ||																					--金額
								/* 20060207 null対策 */
--								SUBSTRB(RPAD(to_areaname,20,'　'),1,20) ||												--対話地
								SUBSTRB(RPAD(nvl(to_areaname,'　'),20,'　'),1,20) ||									--対話地
								DECODE(meisai_tel_hyoji_flg,1,RPAD('*',20,'*'),aitesaki_tel) ||							--相手先電話番号
								RPAD(' ',26),																			--予備
								1,
								/* 着信系 */
								kokyaku_cd ||																			--顧客コード
								kakin_nengetu ||																		--課金年月
								'000000000' ||																			--連番
								CST_MEISAI_PTN5 ||																		--パターン
								oem_cd ||																				--グループ
								CST_DATA_KBN_01 ||																		--データ区分
								RPAD(print_id,20,' ') ||																--電話番号
								RPAD('課金電話番号：' || nvl(aoba_out_common.conv_out_mbc(print_id_ura),'　'),80,'　') ||	--自由使用欄
--								RPAD('　',80,'　') ||
								TO_CHAR(TO_DATE(tuwa_start_date,'YYYY.MM.DD'),'YYYY.MM.DD') ||							--通話開始日
								RPAD(TO_CHAR(TO_DATE(tuwa_start_time,'HH24:MI:SS'),'HH24:MI:SS'),9,' ') ||				--通話開始時刻
								RPAD(tuwa_kbn_name,16,'　') ||															--通話区分
								SUBSTR(tuwa_time,2,2) || ':' || SUBSTR(tuwa_time,4,2) || ':' || SUBSTR(tuwa_time,6,2) || ' '
								 ||																						--通話時間
								SUBSTRB(LPAD(tuwa_unit,10,'0'),4,7) ||													--度数
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,waribiki_tuwaryo) ||			--金額サイン
								CASE	WHEN	LENGTHB(waribiki_tuwaryo) > CST_KINGAKU_YUKO_NO THEN
													TO_CHAR(ABS(waribiki_tuwaryo)*100)
										WHEN	LENGTHB(waribiki_tuwaryo) <= CST_KINGAKU_YUKO_NO THEN
													LPAD(ABS(waribiki_tuwaryo)*100,9,'0')
								END ||																					--金額
								/* 20060207 null対策 */
--								SUBSTRB(RPAD(from_areaname,20,'　'),1,20) ||											--発地域
								SUBSTRB(RPAD(nvl(from_areaname,'　'),20,'　'),1,20) ||											--発地域
								SUBSTRB(RPAD(aoba_out_common.conv_out_mbc(from_jigyosha),20,'　'),1,20) ||			--発地域／発事業者
								RPAD(' ',26)																			--予備
								),
						/* 請求書通話明細データ 終了 */
						dDate,
						vUser,
						NULL,
						NULL
			FROM	wk_tuwa_meisai
			WHERE	meisai_type = cMeisai;
			/* 2006/01/05 パフォーマンス対応 */
			/* UNION削除 */
			--UNION

			/* 小計１(通話区分単位) */
			INSERT INTO wk_bl_tuwa_meisai(
						meisai_type,				--請求書明細種別
						ptn_sk,						--パターンソートキー
						data_kbn_sk,				--データ区分ソートキー
						kokyaku_cd_sk,				--顧客コードソートキー
						kakin_nengetu_sk,			--課金年月ソートキー
						id_sk,						--IDソートキー
						tuwa_kbn_sk,				--通話区分コードソートキー
						tuwa_start_date_sk,			--通話開始日ソートキー
						/* 20060224 通話開始時刻対応 */
--						tuwa_time_sk,				--通話時間ソートキー
						tuwa_start_time_sk,			--通話開始時刻ソートキー
						sk_yobi,					--ソートキー予備
						kaisen_meisai_data,			--請求書回線明細データ
						insert_date,				--登録日時
						insert_user,				--登録ユーザ
						update_date,				--更新日
						update_user					--更新ユーザ
						)
			/* 2006/01/31 ワークテーブル追加による */
			SELECT		cMeisai,
						CST_PTN_SK5,
						CST_DATA_KBN_SK06,
						a.kokyaku_cd,
						a.kakin_nengetu,
						a.id,
						a.tuwa_kbn_cd,
						'ZZZZZZZZ',
--						'ZZZZZZZ',
						'ZZZZZZ',		--20060224
						'00',
						/* 請求書通話明細データ 開始 */
						DECODE(i,0,
								/* 発信系 */
								a.kokyaku_cd ||																			--顧客コード
								a.kakin_nengetu ||																		--課金年月
								'000000000' ||																			--連番
								CST_MEISAI_PTN5 ||																		--パターン
								a.oem_cd ||																				--グループ
								CST_DATA_KBN_06 ||																		--データ区分
								RPAD(a.print_id,20,' ') ||																--電話番号
								RPAD('　',80,'　')||																	--自由使用欄
								RPAD(' ',10) ||																			--通話開始日
								RPAD(' ',9) ||																			--通話開始時刻
								RPAD('　',16,'　') ||																	--通話区分
								RPAD(' ',9) ||																			--通話時間
								RPAD(' ',7) ||																			--度数
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,a.waribiki_tuwaryo) ||		--金額サイン
								CASE	WHEN	LENGTHB(a.waribiki_tuwaryo) > CST_KINGAKU_YUKO_NO THEN
													TO_CHAR(ABS(a.waribiki_tuwaryo)*100)
										WHEN	LENGTHB(a.waribiki_tuwaryo) <= CST_KINGAKU_YUKO_NO THEN
													LPAD(ABS(a.waribiki_tuwaryo)*100,9,'0')
								END ||																					--金額
								/* 20060206 項目名にそれぞれの集計の名称を固定値で設定するよう変更 */
--								RPAD('　',20,'　') ||																	--対話地
								RPAD('通話区分計',20,'　') ||																	--対話地
								RPAD(' ',20) ||																			--相手先電話番号
								RPAD(' ',26),																			--予備
								1,
								/* 着信系 */
								a.kokyaku_cd ||																			--顧客コード
								a.kakin_nengetu ||																		--課金年月
								'000000000' ||																			--連番
								CST_MEISAI_PTN5 ||																		--パターン
								a.oem_cd ||																				--グループ
								CST_DATA_KBN_06 ||																		--データ区分
								RPAD(a.print_id,20,' ') ||																--電話番号
								RPAD('　',80,'　') ||																	--自由使用欄
								RPAD(' ',10) ||																			--通話開始日
								RPAD(' ',9) ||																			--通話開始時刻
								RPAD('　',16,'　') ||																	--通話区分
								RPAD(' ',9) ||																			--通話時間
								RPAD(' ',7) ||																			--度数
								/* 20060215 金額サインに値をいれるよう変更 */
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,a.waribiki_tuwaryo) ||		--金額サイン
								CASE	WHEN	LENGTHB(a.waribiki_tuwaryo) > CST_KINGAKU_YUKO_NO THEN
													TO_CHAR(ABS(TRUNC(a.waribiki_tuwaryo))*100)
										WHEN	LENGTHB(a.waribiki_tuwaryo) <= CST_KINGAKU_YUKO_NO THEN
													LPAD(ABS(TRUNC(a.waribiki_tuwaryo))*100,9,'0')
								END ||																					--金額
								/* 20060206 項目名にそれぞれの集計の名称を固定値で設定するよう変更 */
--								RPAD('　',20,'　') ||																	--発地域
								RPAD('通話区分計',20,'　') ||															--発地域
								RPAD('　',20,'　') ||																	--発地域／発事業者
								RPAD(' ',26)																			--予備
								),
						/* 請求書通話明細データ 終了 */
						dDate,
						vUser,
						NULL,
						NULL
			FROM		(
						SELECT	kokyaku_cd,
								kakin_nengetu,
								id,
								oem_cd,
								MIN(print_id) as print_id,	--20060421 成毛
								tuwa_kbn_cd,
								/* 20060224 合計値で切捨てするよう変更 */
--								SUM(TRUNC(waribiki_tuwaryo)) waribiki_tuwaryo
								TRUNC(SUM(waribiki_tuwaryo)) waribiki_tuwaryo
						/*
						FROM	wk_bl_seikyu_sum a,
								tb_tuwa_meisai b,
								tb_srv_type_mst c
						WHERE	b.srv_type_cd = c.srv_type_cd
						AND		c.meisai_type = cMeisai
						AND		a.data_type = CST_DATA_TYPE1
						AND		a.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_11
						AND		a.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_13
						AND		a.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_14
						AND		a.kakin_nengetu = b.kakin_nengetu
						AND		a.seikyu_gun = b.seikyu_gun
						AND		a.kokyaku_cd = b.kokyaku_cd
						AND		b.meisai_tel_hyoji_flg IS NOT NULL*/
						FROM	wk_tuwa_meisai
						WHERE	meisai_type = cMeisai
						GROUP BY	kokyaku_cd,
									kakin_nengetu,
									id,
									oem_cd,
--									print_id,
									tuwa_kbn_cd
						) a;
			/* 2006/01/05 パフォーマンス対応 */
			/* UNION削除 */
			--UNION

			/* 小計２(ID単位) */
			INSERT INTO wk_bl_tuwa_meisai(
						meisai_type,				--請求書明細種別
						ptn_sk,						--パターンソートキー
						data_kbn_sk,				--データ区分ソートキー
						kokyaku_cd_sk,				--顧客コードソートキー
						kakin_nengetu_sk,			--課金年月ソートキー
						id_sk,						--IDソートキー
						tuwa_kbn_sk,				--通話区分コードソートキー
						tuwa_start_date_sk,			--通話開始日ソートキー
						/* 20060224 通話開始時刻対応 */
--						tuwa_time_sk,				--通話時間ソートキー
						tuwa_start_time_sk,			--通話開始時刻ソートキー
						sk_yobi,					--ソートキー予備
						kaisen_meisai_data,			--請求書回線明細データ
						insert_date,				--登録日時
						insert_user,				--登録ユーザ
						update_date,				--更新日
						update_user					--更新ユーザ
						)
			SELECT		cMeisai,
						CST_PTN_SK5,
						CST_DATA_KBN_SK07,
						a.kokyaku_cd,
						a.kakin_nengetu,
						a.id,
						'ZZZZ',
						'ZZZZZZZZ',
--						'ZZZZZZZ',
						'ZZZZZZ',		--20060224
						'00',
						/* 請求書通話明細データ 開始 */
						DECODE(i,0,
								/* 発信系 */
								a.kokyaku_cd ||																			--顧客コード
								a.kakin_nengetu ||																		--課金年月
								'000000000' ||																			--連番
								CST_MEISAI_PTN5 ||																		--パターン
								a.oem_cd ||																				--グループ
								CST_DATA_KBN_07 ||																		--データ区分
								RPAD(a.print_id,20,' ') ||																--電話番号
								RPAD('　',80,'　')||																	--自由使用欄
								RPAD(' ',10) ||																			--通話開始日
								RPAD(' ',9) ||																			--通話開始時刻
								RPAD('　',16,'　') ||																	--通話区分
								RPAD(' ',9) ||																			--通話時間
								RPAD(' ',7) ||																			--度数
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,a.waribiki_tuwaryo) ||		--金額サイン
								CASE	WHEN	LENGTHB(a.waribiki_tuwaryo) > CST_KINGAKU_YUKO_NO THEN
													TO_CHAR(ABS(a.waribiki_tuwaryo)*100)
										WHEN	LENGTHB(a.waribiki_tuwaryo) <= CST_KINGAKU_YUKO_NO THEN
													LPAD(ABS(a.waribiki_tuwaryo)*100,9,'0')
								END ||																					--金額
								/* 20060206 項目名にそれぞれの集計の名称を固定値で設定するよう変更 */
--								RPAD('　',20,'　') ||																	--対話地
								RPAD('電話番号計',20,'　') ||																	--対話地
								RPAD(' ',20) ||																			--相手先電話番号
								RPAD(' ',26),																			--予備
								1,
								/* 着信系 */
								a.kokyaku_cd ||																			--顧客コード
								a.kakin_nengetu ||																		--課金年月
								'000000000' ||																			--連番
								CST_MEISAI_PTN5 ||																		--パターン
								a.oem_cd ||																				--グループ
								CST_DATA_KBN_07 ||																		--データ区分
								RPAD(a.print_id,20,' ') ||																--電話番号
								RPAD('　',80,'　') ||																	--自由使用欄
								RPAD(' ',10) ||																			--通話開始日
								RPAD(' ',9) ||																			--通話開始時刻
								RPAD('　',16,'　') ||																	--通話区分
								RPAD(' ',9) ||																			--通話時間
								RPAD(' ',7) ||																			--度数
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,a.waribiki_tuwaryo) ||		--金額サイン
								CASE	WHEN	LENGTHB(a.waribiki_tuwaryo) > CST_KINGAKU_YUKO_NO THEN
													TO_CHAR(ABS(a.waribiki_tuwaryo)*100)
										WHEN	LENGTHB(a.waribiki_tuwaryo) <= CST_KINGAKU_YUKO_NO THEN
													LPAD(ABS(a.waribiki_tuwaryo)*100,9,'0')
								END ||																					--金額
								/* 20060206 項目名にそれぞれの集計の名称を固定値で設定するよう変更 */
--								RPAD('　',20,'　') ||																	--発地域
								RPAD('電話番号計',20,'　') ||															--発地域
								RPAD('　',20,'　') ||																	--発地域／発事業者
								RPAD(' ',26)																			--予備
								),
						/* 請求書通話明細データ 終了 */
						dDate,
						vUser,
						NULL,
						NULL
			FROM		(
						SELECT		a1.kokyaku_cd,
									a1.kakin_nengetu,
									a1.id,
									a1.oem_cd,
--									a1.print_id,
									MIN(a1.print_id) AS print_id,		-- 20060421 成毛
									SUM(a1.waribiki_tuwaryo) AS waribiki_tuwaryo

						FROM	(
								/* 2006/01/31 ワークテーブル追加に伴う変更 */
								SELECT	kokyaku_cd,
										kakin_nengetu,
										id,
										oem_cd,
--										print_id,
										MIN(print_id) AS print_id,		-- 20060421 成毛
										tuwa_kbn_cd,
										/* 20060224 通話区分計ごとに切捨て */
--										SUM(TRUNC(waribiki_tuwaryo)) AS waribiki_tuwaryo
										TRUNC(SUM(waribiki_tuwaryo)) AS waribiki_tuwaryo
								/*
								FROM	wk_bl_seikyu_sum a,
										tb_tuwa_meisai b,
										tb_srv_type_mst c
								WHERE	b.srv_type_cd = c.srv_type_cd
								AND		c.meisai_type = cMeisai
								AND		a.data_type = CST_DATA_TYPE1
								AND		a.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_11
								AND		a.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_13
								AND		a.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_14
								AND		a.kakin_nengetu = b.kakin_nengetu
								AND		a.seikyu_gun = b.seikyu_gun
								AND		a.kokyaku_cd = b.kokyaku_cd
								AND		b.meisai_tel_hyoji_flg IS NOT NULL
								*/
								FROM	wk_tuwa_meisai
								WHERE	meisai_type = cMeisai
								GROUP BY	kokyaku_cd,
											kakin_nengetu,
											id,
											oem_cd,
--											print_id
											tuwa_kbn_cd
								) a1
						GROUP BY	a1.kokyaku_cd,
									a1.kakin_nengetu,
									a1.id,
									a1.oem_cd
--									a1.print_id
						) a;
			/* 2006/01/05 パフォーマンス対応 */
			/* UNION削除 */
			--UNION

			/* 小計３(課金年月単位) */
			INSERT INTO wk_bl_tuwa_meisai(
						meisai_type,				--請求書明細種別
						ptn_sk,						--パターンソートキー
						data_kbn_sk,				--データ区分ソートキー
						kokyaku_cd_sk,				--顧客コードソートキー
						kakin_nengetu_sk,			--課金年月ソートキー
						id_sk,						--IDソートキー
						tuwa_kbn_sk,				--通話区分コードソートキー
						tuwa_start_date_sk,			--通話開始日ソートキー
						/* 20060224 通話開始時刻対応 */
--						tuwa_time_sk,				--通話時間ソートキー
						tuwa_start_time_sk,			--通話開始時刻ソートキー
						sk_yobi,					--ソートキー予備
						kaisen_meisai_data,			--請求書回線明細データ
						insert_date,				--登録日時
						insert_user,				--登録ユーザ
						update_date,				--更新日
						update_user					--更新ユーザ
						)
			SELECT		cMeisai,
						CST_PTN_SK5,
						CST_DATA_KBN_SK08,
						a.kokyaku_cd,
						a.kakin_nengetu,
						'ZZZZZZZZZZZZZZZZZZZZ',
						'ZZZZ',
						'ZZZZZZZZ',
--						'ZZZZZZZ',
						'ZZZZZZ',		--20060224
						'00',
						/* 請求書通話明細データ 開始 */
						DECODE(i,0,
								/* 発信系 */
								a.kokyaku_cd ||																			--顧客コード
								a.kakin_nengetu ||																		--課金年月
								'000000000' ||																			--連番
								CST_MEISAI_PTN5 ||																		--パターン
								a.oem_cd ||																				--グループ
								CST_DATA_KBN_08 ||																		--データ区分
								RPAD(' ',20,' ') ||																		--電話番号
								RPAD('　',80,'　')||																	--自由使用欄
								RPAD(' ',10) ||																			--通話開始日
								RPAD(' ',9) ||																			--通話開始時刻
								RPAD('　',16,'　') ||																	--通話区分
								RPAD(' ',9) ||																			--通話時間
								RPAD(' ',7) ||																			--度数
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,a.waribiki_tuwaryo) ||		--金額サイン
								CASE	WHEN	LENGTHB(a.waribiki_tuwaryo) > CST_KINGAKU_YUKO_NO THEN
													TO_CHAR(ABS(a.waribiki_tuwaryo)*100)
										WHEN	LENGTHB(a.waribiki_tuwaryo) <= CST_KINGAKU_YUKO_NO THEN
													LPAD(ABS(a.waribiki_tuwaryo)*100,9,'0')
								END ||																					--金額
								/* 20060206 項目名にそれぞれの集計の名称を固定値で設定するよう変更 */
--								RPAD('　',20,'　') ||																	--対話地
								RPAD('課金年月計',20,'　') ||																	--対話地
								RPAD(' ',20) ||																			--相手先電話番号
								RPAD(' ',26),																			--予備
								1,
								/* 着信系 */
								a.kokyaku_cd ||																			--顧客コード
								a.kakin_nengetu ||																		--課金年月
								'000000000' ||																			--連番
								CST_MEISAI_PTN5 ||																		--パターン
								a.oem_cd ||																				--グループ
								CST_DATA_KBN_08 ||																		--データ区分
								RPAD(' ',20,' ') ||																		--電話番号
								RPAD('　',80,'　') ||																	--自由使用欄
								RPAD(' ',10) ||																			--通話開始日
								RPAD(' ',9) ||																			--通話開始時刻
								RPAD('　',16,'　') ||																	--通話区分
								RPAD(' ',9) ||																			--通話時間
								RPAD(' ',7) ||																			--度数
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,a.waribiki_tuwaryo) ||		--金額サイン
								CASE	WHEN	LENGTHB(a.waribiki_tuwaryo) > CST_KINGAKU_YUKO_NO THEN
													TO_CHAR(ABS(a.waribiki_tuwaryo)*100)
										WHEN	LENGTHB(a.waribiki_tuwaryo) <= CST_KINGAKU_YUKO_NO THEN
													LPAD(ABS(a.waribiki_tuwaryo)*100,9,'0')
								END ||																					--金額
								/* 20060206 項目名にそれぞれの集計の名称を固定値で設定するよう変更 */
--								RPAD('　',20,'　') ||																	--発地域
								RPAD('課金年月計',20,'　') ||																	--発地域
								RPAD('　',20,'　') ||																	--発地域／発事業者
								RPAD(' ',26)																			--予備
								),
						/* 請求書通話明細データ 終了 */
						dDate,
						vUser,
						NULL,
						NULL
			FROM		(
						SELECT		a1.kokyaku_cd,
									a1.kakin_nengetu,
									a1.oem_cd,
									SUM(a1.waribiki_tuwaryo) AS waribiki_tuwaryo

						FROM	(
								/* 2006/01/31 ワークテーブル追加に伴う変更 */
								SELECT	kokyaku_cd,
										kakin_nengetu,
										id,
										oem_cd,
										tuwa_kbn_cd,
										/* 20060224 通話区分ごとに切捨て */
--										SUM(TRUNC(waribiki_tuwaryo)) AS waribiki_tuwaryo
										TRUNC(SUM(waribiki_tuwaryo)) AS waribiki_tuwaryo
								/*
								FROM	wk_bl_seikyu_sum a,
										tb_tuwa_meisai b,
										tb_srv_type_mst c
								WHERE	b.srv_type_cd = c.srv_type_cd
								AND		c.meisai_type = cMeisai
								AND		a.data_type = CST_DATA_TYPE1
								AND		a.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_11
								AND		a.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_13
								AND		a.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_14
								AND		a.kakin_nengetu = b.kakin_nengetu
								AND		a.seikyu_gun = b.seikyu_gun
								AND		a.kokyaku_cd = b.kokyaku_cd
								AND		b.meisai_tel_hyoji_flg IS NOT NULL
								*/
								FROM	wk_tuwa_meisai
								WHERE	meisai_type = cMeisai
								GROUP BY	kokyaku_cd,
											kakin_nengetu,
											id,
											oem_cd,
											tuwa_kbn_cd
								) a1
						GROUP BY	a1.kokyaku_cd,
									a1.kakin_nengetu,
									a1.oem_cd
						) a;
			/* 2006/01/05 パフォーマンス対応 */
			/* UNION削除 */
			--UNION

			/* 総合計(顧客単位) */
			INSERT INTO wk_bl_tuwa_meisai(
						meisai_type,				--請求書明細種別
						ptn_sk,						--パターンソートキー
						data_kbn_sk,				--データ区分ソートキー
						kokyaku_cd_sk,				--顧客コードソートキー
						kakin_nengetu_sk,			--課金年月ソートキー
						id_sk,						--IDソートキー
						tuwa_kbn_sk,				--通話区分コードソートキー
						tuwa_start_date_sk,			--通話開始日ソートキー
						/* 20060224 通話開始時刻対応 */
--						tuwa_time_sk,				--通話時間ソートキー
						tuwa_start_time_sk,			--通話開始時刻ソートキー
						sk_yobi,					--ソートキー予備
						kaisen_meisai_data,			--請求書回線明細データ
						insert_date,				--登録日時
						insert_user,				--登録ユーザ
						update_date,				--更新日
						update_user					--更新ユーザ
						)
			SELECT		cMeisai,
						CST_PTN_SK5,
						CST_DATA_KBN_SK09,
						a.kokyaku_cd,
						/* 20060224 最大課金年月セット */
--						'999999',
						a.kakin_nengetu,		--20060224
						'ZZZZZZZZZZZZZZZZZZZZ',
						'ZZZZ',
						'ZZZZZZZZ',
--						'ZZZZZZZ',
						'ZZZZZZ',		--20060224
						'00',
						/* 請求書通話明細データ 開始 */
						DECODE(i,0,
								/* 発信系 */
								a.kokyaku_cd ||																			--顧客コード
								/* 20060224 最大課金年月をセット */
--								RPAD(' ',6) ||																			--課金年月
								a.kakin_nengetu ||																			--課金年月
								'000000000' ||																			--連番
								CST_MEISAI_PTN5 ||																		--パターン
								/* 20060206 OEMコードをマッピングするように変更 */
--								RPAD(' ',4) ||																			--グループ
								nvl(a.oem_cd,'    ') ||																	--グループ
								CST_DATA_KBN_09 ||																		--データ区分
								RPAD(' ',20,' ') ||																		--電話番号
								RPAD('　',80,'　')||																	--自由使用欄
								RPAD(' ',10) ||																			--通話開始日
								RPAD(' ',9) ||																			--通話開始時刻
								RPAD('　',16,'　') ||																	--通話区分
								RPAD(' ',9) ||																			--通話時間
								RPAD(' ',7) ||																			--度数
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,a.waribiki_tuwaryo) ||		--金額サイン
								CASE	WHEN	LENGTHB(a.waribiki_tuwaryo) > CST_KINGAKU_YUKO_NO THEN
													TO_CHAR(ABS(a.waribiki_tuwaryo)*100)
										WHEN	LENGTHB(a.waribiki_tuwaryo) <= CST_KINGAKU_YUKO_NO THEN
													LPAD(ABS(a.waribiki_tuwaryo)*100,9,'0')
								END ||																					--金額
								/* 20060206 項目名にそれぞれの集計の名称を固定値で設定するよう変更 */
--								RPAD('　',20,'　') ||																	--対話地
								RPAD('総合計',20,'　') ||																--対話地
								RPAD(' ',20) ||																			--相手先電話番号
								RPAD(' ',26),																			--予備
								1,
								/* 着信系 */
								a.kokyaku_cd ||																			--顧客コード
--								RPAD(' ',6) ||																			--課金年月
								a.kakin_nengetu ||																		--課金年月
								'000000000' ||																			--連番
								CST_MEISAI_PTN5 ||																		--パターン
								/* 20060206 OEMコードをマッピングするように変更 */
--								RPAD(' ',4) ||																			--グループ
								nvl(a.oem_cd,'    ') ||																			--グループ
								CST_DATA_KBN_09 ||																		--データ区分
								RPAD(' ',20,' ') ||																		--電話番号
								RPAD('　',80,'　') ||																	--自由使用欄
								RPAD(' ',10) ||																			--通話開始日
								RPAD(' ',9) ||																			--通話開始時刻
								RPAD('　',16,'　') ||																	--通話区分
								RPAD(' ',9) ||																			--通話時間
								RPAD(' ',7) ||																			--度数
								aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,a.waribiki_tuwaryo) ||		--金額サイン
								CASE	WHEN	LENGTHB(a.waribiki_tuwaryo) > CST_KINGAKU_YUKO_NO THEN
													TO_CHAR(ABS(a.waribiki_tuwaryo)*100)
										WHEN	LENGTHB(a.waribiki_tuwaryo) <= CST_KINGAKU_YUKO_NO THEN
													LPAD(ABS(a.waribiki_tuwaryo)*100,9,'0')
								END ||																					--金額
								/* 20060206 項目名にそれぞれの集計の名称を固定値で設定するよう変更 */
--								RPAD('　',20,'　') ||																	--発地域
								RPAD('総合計',20,'　') ||																--発地域
								RPAD('　',20,'　') ||																	--発地域／発事業者
								RPAD(' ',26)																			--予備
								),
						/* 請求書通話明細データ 終了 */
						dDate,
						vUser,
						NULL,
						NULL
			FROM		(
						/* 20060206 OEMコードをグループに追加 */
						/* 20060224 課金年月の最大値取得 */
						SELECT		a1.kokyaku_cd,
									MAX(a1.kakin_nengetu) AS kakin_nengetu,		--20060224
									a1.oem_cd,
									SUM(a1.waribiki_tuwaryo) AS waribiki_tuwaryo

						FROM	(
								/* 2006/01/31 ワークテーブル追加に伴う変更 */
								SELECT	kokyaku_cd,
										kakin_nengetu,
										id,
										oem_cd,
										tuwa_kbn_cd,
										/* 20060224 通話区分計単位で切捨て */
--										SUM(TRUNC(waribiki_tuwaryo)) AS waribiki_tuwaryo
										TRUNC(SUM(waribiki_tuwaryo)) AS waribiki_tuwaryo
								/*
								FROM	wk_bl_seikyu_sum a,
										tb_tuwa_meisai b,
										tb_srv_type_mst c
								WHERE	b.srv_type_cd = c.srv_type_cd
								AND		c.meisai_type = cMeisai
								AND		a.data_type = CST_DATA_TYPE1
								AND		a.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_11
								AND		a.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_13
								AND		a.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_14
								AND		a.kakin_nengetu = b.kakin_nengetu
								AND		a.seikyu_gun = b.seikyu_gun
								AND		a.kokyaku_cd = b.kokyaku_cd
								AND		b.meisai_tel_hyoji_flg IS NOT NULL
								*/
								FROM	wk_tuwa_meisai
								WHERE	meisai_type = cMeisai
								GROUP BY	kokyaku_cd,
											kakin_nengetu,
											id,
											oem_cd,
											tuwa_kbn_cd
								) a1
						GROUP BY	a1.kokyaku_cd,a1.oem_cd
						) a;
			/* エンドレコード作製 */
			INSERT INTO wk_bl_tuwa_meisai(
						meisai_type,				--請求書明細種別
						ptn_sk,						--パターンソートキー
						data_kbn_sk,				--データ区分ソートキー
						kokyaku_cd_sk,				--顧客コードソートキー
						kakin_nengetu_sk,			--課金年月ソートキー
						id_sk,						--IDソートキー
						tuwa_kbn_sk,				--通話区分コードソートキー
						tuwa_start_date_sk,			--通話開始日ソートキー
						/* 20060224 通話開始時刻対応 */
--						tuwa_time_sk,				--通話時間ソートキー
						tuwa_start_time_sk,			--通話開始時刻ソートキー
						sk_yobi,					--ソートキー予備
						kaisen_meisai_data,			--請求書回線明細データ
						insert_date,				--登録日時
						insert_user,				--登録ユーザ
						update_date,				--更新日
						update_user)				--更新ユーザ
			SELECT		cMeisai,
						CST_PTN_SK9,
						CST_DATA_KBN_SKZZ,
						a.kokyaku_cd,
						a.kakin_nengetu,
						'ZZZZZZZZZZZZZZZZZZZZ',
						'ZZZZ',
						'ZZZZZZZZ',
--						'ZZZZZZZ',
						'ZZZZZZ',		--20060224
						'00',
						/* 請求書通話明細データ 開始 */
						a.kokyaku_cd ||						--顧客コード
						a.kakin_nengetu ||					--課金年月
						'000000000' ||						--連番
						CST_MEISAI_PTN9 ||					--パターン
						a.oem_cd ||							--グループ
						RPAD('　',100,'　') ||				--コメント１
						RPAD('　',100,'　') ||				--コメント２
						RPAD(' ',28),						--予備
						/* 請求書通話明細データ 終了 */
						dDate,
						vUser,
						NULL,
						NULL
			FROM		(
						/*SELECT	DISTINCT	b.kokyaku_cd,
											b.kakin_nengetu,
											b.oem_cd
						FROM	wk_bl_seikyu_sum a,
								tb_tuwa_meisai b,
								tb_srv_type_mst c
						WHERE	b.srv_type_cd = c.srv_type_cd
						AND		c.meisai_type = cMeisai
						AND		a.data_type = CST_DATA_TYPE1
						AND		a.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_11
						AND		a.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_13
						AND		a.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_14
						AND		a.kakin_nengetu = b.kakin_nengetu
						AND		a.seikyu_gun = b.seikyu_gun
						AND		a.kokyaku_cd = b.kokyaku_cd
						AND		b.meisai_tel_hyoji_flg IS NOT NULL*/
						SELECT	a.kokyaku_cd,
								a.kakin_nengetu,
								a.oem_cd
						FROM	wk_bl_seikyu_sum a
						/* 2006/01/31 ワークテーブル追加に伴う変更 */
						/*
						WHERE	EXISTS (select 'x' from tb_tuwa_meisai b,tb_srv_type_mst c
										where	a.kokyaku_cd = b.kokyaku_cd
										and		a.kakin_nengetu = b.kakin_nengetu
										and		a.seikyu_gun = b.seikyu_gun
										and		b.srv_type_cd = c.srv_type_cd
										AND		b.meisai_tel_hyoji_flg IS NOT NULL
										AND		c.meisai_type = cMeisai
										)
						*/
						WHERE	EXISTS (SELECT	'x'
										FROM	wk_tuwa_meisai b
										WHERE	a.kokyaku_cd = b.kokyaku_cd
										AND		a.kakin_nengetu = b.kakin_nengetu
										AND		a.seikyu_gun = b.seikyu_gun
										AND		b.meisai_type = cMeisai
										)
--						AND		a.data_type = CST_DATA_TYPE1
--						AND		a.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_11
--						AND		a.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_13
--						AND		a.print_type_cd <> aoba_out_const.CST_YOUSHI_TYPE_14
						) a;

			/* 請求書回線明細一時TBL連番更新 */
			/* 2006/01/05 パフォーマンス対応 */
			/*UPDATE wk_bl_tuwa_meisai a
			SET kaisen_meisai_data
				=
				(
				SELECT SUBSTRB(a.kaisen_meisai_data,1,14) ||
						TRIM(TO_CHAR(rec_no,'000000009')) ||
						SUBSTRB(b.kaisen_meisai_data,24)
				FROM 	(SELECT	meisai_type,
								kaisen_meisai_data,
								row_number() over
								(order by ptn_sk,
								data_kbn_sk,
								kokyaku_cd_sk,
								kakin_nengetu_sk,
								id_sk,
								tuwa_kbn_sk,
								tuwa_start_date_sk,
								tuwa_time_sk) rec_no
						FROM	wk_bl_tuwa_meisai
						WHERE	meisai_type = cMeisai) b
				WHERE 	a.meisai_type = cMeisai
				AND		b.rowid = a.rowid)
			WHERE	a.meisai_type = cMeisai;*/
		 	/* シーケンス初期化 */
		 	nSeqno := 1;
		 	FOR rec_tuwa_meisai IN cur_tuwa_meisai(cMeisai) LOOP
		 		UPDATE	wk_bl_tuwa_meisai
		 		SET		kaisen_meisai_data = rec_tuwa_meisai.meisai_a ||
		 									TRIM(TO_CHAR(nSeqno,'000000009')) ||
		 									rec_tuwa_meisai.meisai_b
		 		WHERE CURRENT OF cur_tuwa_meisai;
		 		/* カウンタアップ */
		 		nSeqno := nSeqno + 1;
		 	END LOOP;
		END IF;
	END LOOP;
END FAOB010105;
/
