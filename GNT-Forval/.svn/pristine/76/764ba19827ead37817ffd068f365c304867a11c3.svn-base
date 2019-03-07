CREATE OR REPLACE PROCEDURE FAOB010118
/********************************************************************/
/*	関数名：FAOB010118												*/
/*	機能名：請求書データ作成：SimpleNamingRights対応処理			*/
/*	作成者：根来													*/
/*	作成日：2009/4/6												*/
/*	更新日：														*/
/*	VER   ：1.00													*/
/*------------------------------------------------------------------*/
/*	引数：		無し												*/
/*	戻値：		無し												*/
/********************************************************************/
IS
	/* ローカル定数 */
	CST_JUCHU_DATE			CONSTANT TB_KOKYAKU_ID_JOHO.JUCHU_DATE%TYPE := '20090331';	--対象判別受注日
--	CST_JUCHU_DATE			CONSTANT TB_KOKYAKU_ID_JOHO.JUCHU_DATE%TYPE := '20080131';	--対象判別受注日(テスト用)

	/* ローカル変数 */
	nRet					NUMBER; 										--リターンコード
	dSysDate				wk_bl_kokyaku.insert_date%TYPE;					--システム日付
	vUser					wk_bl_kokyaku.insert_user%TYPE;					--ユーザ

	vSeikyu_nengetu			WK_SEIKYU_HAKKO_KOKYAKU.KAKIN_NENGETU%type;		--処理対象課金年月
	vSeikyu_gun				WK_SEIKYU_HAKKO_KOKYAKU.SEIKYU_GUN%type;		--処理対象請求群

	vHenkan_Oem_Cd			WK_SNR_TAISHO_DAIRITEN_LST.HENKAN_OEM_CD%TYPE;	--処理中代理店ｺｰﾄﾞ格納変数
	BufKokyakucd			TB_KOKYAKU_ID_JOHO.KOKYAKU_CD%TYPE;				--処理中顧客ｺｰﾄﾞ格納変数
	nInsflg					NUMBER(1);

	/* カーソル定義 */
	CURSOR cur_snr_oem_henkan
	IS
		SELECT	A.KOKYAKU_CD		KOKYAKU_CD,
				MIN(A.JUCHU_DATE)	MIN_JUCHU_DATE,
				MIN(A.JUCHU_BNG)	MIN_JUCHU_BNG,
				C.OEM_CD			OEM_CD,
				A.DAIRITEN_CD		DAIRITEN_CD
		FROM	TB_KOKYAKU_ID_JOHO A,
				(SELECT		KOKYAKU_CD,
							MIN(JUCHU_DATE)
				FROM		TB_KOKYAKU_ID_JOHO
				GROUP BY	KOKYAKU_CD
				HAVING		MIN(JUCHU_DATE) > TO_DATE(CST_JUCHU_DATE,'YYYYMMDD')
				ORDER BY	KOKYAKU_CD,
							MIN(JUCHU_DATE)
				) B,
				TB_KOKYAKU_JOHO C
		WHERE	A.KOKYAKU_CD	=	B.KOKYAKU_CD
		AND		A.KOKYAKU_CD	=	C.KOKYAKU_CD
		GROUP BY A.KOKYAKU_CD,
				 A.DAIRITEN_CD,
				 C.OEM_CD
		ORDER BY A.KOKYAKU_CD,
		 		 MIN(A.JUCHU_DATE),
				 MIN(A.JUCHU_BNG),
				 A.DAIRITEN_CD,
				 C.OEM_CD;

	rec_snr_oem_henkan	cur_snr_oem_henkan%ROWTYPE;

BEGIN
	/* システム日時の取得 */
	nRet := aoba_common.get_date( dSysDate );
	/* ユーザの取得 */
	nRet := aoba_common.get_user( vUser );

	/* 処理対象課金年月、群の取得 */
	SELECT	KAKIN_NENGETU,
			SEIKYU_GUN
	  INTO	vSeikyu_nengetu,
			vSeikyu_gun
	  FROM	WK_SEIKYU_HAKKO_KOKYAKU A,
			(SELECT MAX(KAKIN_NENGETU) MAX_KAKIN_NENGETU
			   FROM WK_SEIKYU_HAKKO_KOKYAKU) B
	 WHERE	A.KAKIN_NENGETU = B.MAX_KAKIN_NENGETU
	GROUP BY KAKIN_NENGETU,SEIKYU_GUN;

	/* NR_OEMｺｰﾄﾞ変換履歴ﾃｰﾌﾞﾙTBLのレコード削除 */
	DELETE
	  FROM	WK_SNR_OEM_HENKAN_HIST
	 WHERE	KAKIN_NENGETU	=	vSeikyu_nengetu
	   AND	SEIKYU_GUN		=	vSeikyu_gun;

	/* NR_対象代理店一覧TBLの初期化 */
	execute immediate 'TRUNCATE TABLE WK_SNR_TAISHO_DAIRITEN_LST';

	/* NR_OEMｺｰﾄﾞ変換ﾃｰﾌﾞﾙTBLの初期化 */
	execute immediate 'TRUNCATE TABLE WK_SNR_OEM_HENKAN';

	/* NR_対象代理店一覧TBLのレコード作成 */
	INSERT INTO WK_SNR_TAISHO_DAIRITEN_LST
	(	DAIRITEN_CD,
		HENKAN_OEM_CD,
		INSERT_DATE,
		INSERT_USER
	)
		SELECT	B.DAIRITEN_CD,
				A.OEM_CD,
				dSysDate,
				vUser
		FROM	TB_SNR_TAISHO_DAIRITEN_MST A,
				TB_DAIRITEN_JOHO B
		WHERE	(LENGTH(TRIM(A.DAIRITEN_CD))	=		'9'
		  AND	 A.DAIRITEN_CD			=		trim(B.DAIRITEN_CD))
		   OR	(LENGTH(TRIM(A.DAIRITEN_CD))	=		'6'
		  AND	 B.DAIRITEN_CD			LIKE	trim(A.DAIRITEN_CD)||'%');

	/* 明示的コミット */
	commit;

	/* NR_OEMｺｰﾄﾞ変換ﾃｰﾌﾞﾙTBLのレコード作成 */

		-- 変数初期値設定
	BufKokyakucd	:=	NULL;

	-- カーソルオープン
	FOR rec_snr_oem_henkan IN cur_snr_oem_henkan LOOP

		-- 顧客コード判定
		if	BufKokyakucd	is	null
		or	BufKokyakucd	<>	rec_snr_oem_henkan.KOKYAKU_CD	then

			-- カーソルの代理店コードをKEYにNR_対象代理店一覧TBLにレコードが存在するかチェック
			-- NR_対象代理店一覧TBLにレコードが存在する場合NR_OEMｺｰﾄﾞ変換ﾃｰﾌﾞﾙTBLへのレコード登録する
			-- 対象データが存在しない時は、カーソルの次のレコードを読む
			BEGIN
				SELECT	HENKAN_OEM_CD,
						'1'
				  INTO	vHenkan_Oem_Cd,
						nInsflg
				  FROM	WK_SNR_TAISHO_DAIRITEN_LST
				 WHERE	trim(DAIRITEN_CD)	=	trim(rec_snr_oem_henkan.DAIRITEN_CD);
			EXCEPTION
				when	NO_DATA_FOUND	then
						vHenkan_Oem_Cd	:=	null;
						nInsflg			:=	0;
			END;

			if	nInsflg	=	1	then
				-- NR_OEMｺｰﾄﾞ変換ﾃｰﾌﾞﾙTBLへのレコード登録
				INSERT	INTO	WK_SNR_OEM_HENKAN
						(
						KOKYAKU_CD,
						DAIRITEN_CD,
						MOTO_OEM_CD,
						HENKAN_OEM_CD,
						JUCHU_BNG,
						INSERT_DATE,
						INSERT_USER
						)
						VALUES
						(
						trim(rec_snr_oem_henkan.KOKYAKU_CD),
						trim(rec_snr_oem_henkan.DAIRITEN_CD),
						trim(rec_snr_oem_henkan.OEM_CD),
						vHenkan_Oem_Cd,
						trim(rec_snr_oem_henkan.MIN_JUCHU_BNG),
						dSysDate,
						vUser
						);

				-- NR_OEMｺｰﾄﾞ変換履歴ﾃｰﾌﾞﾙTBLへのレコード登録
				INSERT	INTO	WK_SNR_OEM_HENKAN_HIST
						(
						KOKYAKU_CD,
						DAIRITEN_CD,
						MOTO_OEM_CD,
						HENKAN_OEM_CD,
						JUCHU_BNG,
						KAKIN_NENGETU,
						SEIKYU_GUN,
						INSERT_DATE,
						INSERT_USER
						)
						VALUES
						(
						trim(rec_snr_oem_henkan.KOKYAKU_CD),
						trim(rec_snr_oem_henkan.DAIRITEN_CD),
						trim(rec_snr_oem_henkan.OEM_CD),
						vHenkan_Oem_Cd,
						trim(rec_snr_oem_henkan.MIN_JUCHU_BNG),
						vSeikyu_nengetu,
						vSeikyu_gun,
						dSysDate,
						vUser
						);
			end if;

			-- 顧客コード保存
			BufKokyakucd	:=	rec_snr_oem_henkan.KOKYAKU_CD;
		end if;

	END LOOP;

	/* 明示的コミット */
	commit;

	/* 請求書顧客マスタ一時TBLの擬似OEMコード変換 */
	UPDATE wk_bl_kokyaku A SET (A.seikyu_data,A.oem_cd_sk)
	=(
	  SELECT	SUBSTRB(A.seikyu_data,1,18)||TRIM(TO_CHAR(b.henkan_oem_cd,'0009'))||SUBSTRB(A.seikyu_data,23),
				b.henkan_oem_cd
	  FROM		WK_SNR_OEM_HENKAN b
	  WHERE		A.kokyaku_cd_sk = b.kokyaku_cd
	 )
	WHERE
	  EXISTS
	  (
	  SELECT 1
	  FROM WK_SNR_OEM_HENKAN b
	  WHERE
		  A.kokyaku_cd_sk = b.kokyaku_cd
	 );

	/* 請求書回線明細一時TBLの擬似OEMコード変換 */
	UPDATE wk_bl_kaisen_meisai A SET A.kaisen_meisai_data
	=(
	  SELECT SUBSTRB(A.KAISEN_MEISAI_DATA,1,24)||TRIM(TO_CHAR(b.henkan_oem_cd,'0009'))||SUBSTRB(A.KAISEN_MEISAI_DATA,29)
	  FROM WK_SNR_OEM_HENKAN b
	  WHERE
		  A.kokyaku_cd_sk = B.kokyaku_cd
	)
	WHERE
	  EXISTS
	  (
	  SELECT 1
	  FROM WK_SNR_OEM_HENKAN b
	  WHERE
		  A.kokyaku_cd_sk = b.kokyaku_cd
	  );

	/* 請求書通話明細一時TBLの擬似OEMコード変換 */
	UPDATE wk_bl_tuwa_meisai A SET A.kaisen_meisai_data
	=(
	  SELECT SUBSTRB(A.KAISEN_MEISAI_DATA,1,24)||TRIM(TO_CHAR(b.henkan_oem_cd,'0009'))||SUBSTRB(A.KAISEN_MEISAI_DATA,29)
	  FROM WK_SNR_OEM_HENKAN b
	  WHERE
		  A.kokyaku_cd_sk = B.kokyaku_cd
	)
	WHERE
	  EXISTS
	  (
	  SELECT 1
	  FROM WK_SNR_OEM_HENKAN b
	  WHERE
		  A.kokyaku_cd_sk = b.kokyaku_cd
	  );

	/* 請求書出荷明細一時TBLの擬似OEMコード変換 */
	UPDATE wk_bl_shukka_meisai A SET A.kaisen_meisai_data
	=(
	  SELECT  SUBSTRB(A.KAISEN_MEISAI_DATA,1,24)||TRIM(TO_CHAR(b.henkan_oem_cd,'0009'))||SUBSTRB(A.KAISEN_MEISAI_DATA,29)
	  FROM WK_SNR_OEM_HENKAN b
	  WHERE
		  A.kokyaku_cd_sk = B.kokyaku_cd
	)
	WHERE
	  EXISTS
	  (
	  select 1
	  FROM WK_SNR_OEM_HENKAN b
	  WHERE
		  A.kokyaku_cd_sk = b.kokyaku_cd
	  );

END FAOB010118;
/
