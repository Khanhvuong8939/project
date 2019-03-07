CREATE OR REPLACE PROCEDURE        FASB010101_UTI
/********************************************************************/
/*	関数名：FASB010101_UTI											*/
/*	機能名：口座振替結果（ENO単位）作成								*/
/*	作成者：根来　博明												*/
/*	作成日：2010/03/04												*/
/*	更新日：														*/
/*	VER   ：1.00													*/
/*------------------------------------------------------------------*/
/*	引数：		無し												*/
/*	戻値：		無し												*/
/********************************************************************/
IS
	/* ローカル変数 */
	nRet					NUMBER;										--リターンコード
	vDate					VARCHAR2(8):='';							--処理対象年月日(YYYYMMDD)
	cGun					VARCHAR2(2):='';							--処理対象群
	vCode					VARCHAR2(8):='';							--コード
	vEdaban					VARCHAR2(5):='';							--枝番
	vFile_id				tb_output_ctrl.file_id%TYPE;				--ファイルID
	vLocation				tb_output_ctrl.location%TYPE;				--ロケーション
	vMax_lines				tb_output_ctrl.max_linesize%TYPE;			--最大文字数
	cOpen_mode				CHAR(1);									--ファイルオープンモード
	file_handle				UTL_FILE.FILE_TYPE;							--ファイルハンドル定義
	BufOemCd				CHAR(4);									--処理OEMｺｰﾄﾞ【OEM毎出力対応】

	CST_OEM_CD				VARCHAR2(4):='0016';						--OEMｺｰﾄﾞ（固定「0016」）


	vNyukinDate				VARCHAR2(8):='';							--最小入金日(YYYYMMDD)

	/* カーソル定義 */
	CURSOR cur_ome_eno_data IS
	SELECT	A.KOKYAKU_CD AS 顧客ｺｰﾄﾞ,
			B.SEIKYUSAKI_KAISHA_NAME AS 顧客名,
			C.OYA_ID AS 親ID,
			C.ID AS ENO,
			C.PRINT_ID AS 表示用ENO,
			C.SRV_TYPE_CD AS サービスコード,
			C.SEIKYUGAKU AS 請求額,
			A.KAKIN_NENGETU AS 課金年月,
			DECODE(D.HURIKAE_KEKKA_CD,'0',A.NYUKIN_DATE,NULL) AS 入金日,
			A.HURIKAE_KEKKA_CD AS 振替結果ｺｰﾄﾞ,
			D.HURIKAE_KEKKA_NAME AS 振替結果名,
			A.OEM_CD AS OEMｺｰﾄﾞ
	FROM	WK_SHUNO_HURIKAE A,															-- 収納情報口座振替ﾜｰｸﾃｰﾌﾞﾙ
			TB_KOKYAKU_JOHO_CAT B,														-- 顧客ﾏｽﾀ
--			TB_KOKYAKU_JOHO B,															-- 顧客ﾏｽﾀ
			TB_SEIKYU_UTIWAKE_SUM C,														-- 請求内訳ｻﾏﾘ
			TB_HURIKAE_KEKKA_MST D														-- 振替結果ﾏｽﾀ
	WHERE	A.KOKYAKU_CD		=	B.KOKYAKU_CD										-- 顧客コードの一致
	AND		A.KOKYAKU_CD		=	C.KOKYAKU_CD										-- 顧客コードの一致
	AND		A.KAKIN_NENGETU		=	C.KAKIN_NENGETU										-- 課金年月の一致
	AND		A.SEIKYU_GUN		=	C.SEIKYU_GUN										-- 請求群の一致
	AND		A.OEM_CD			=	C.OEM_CD											-- OEMコードの一致
	AND		A.OEM_CD			in	(CST_OEM_CD)										-- OEMコードの一致 ※OEM先が増えた場合、この条件にOEMｺｰﾄﾞを加えて下さい。
	AND		A.HURIKAE_KEKKA_CD	=	D.HURIKAE_KEKKA_CD									-- 振替結果ｺｰﾄﾞの一致
	ORDER BY A.OEM_CD,B.KOKYAKU_CD,A.KAKIN_NENGETU,C.ID;

	rec_ome_eno_data cur_ome_eno_data%ROWTYPE;

BEGIN

	/* 処理対象群, 処理対象年月取得 */
	SELECT	null,
			to_char(sysdate,'yyyymmdd')
	INTO	cGun,
			vDate
	FROM	dual;

	/* 最小入金日取得 */
	SELECT	nvl(min(NYUKIN_DATE),'99999999')
	INTO	vNyukinDate
	FROM	WK_SHUNO_HURIKAE;

	/* 出力ファイル情報取得 */
	vEdaban := aoba_out_const.CST_OUT_EDABAN_0;

/*****************************/
/*** OEM毎のファイルを作成 ***/
/*****************************/
	BufOemCd := '0000';						-- OEMｺｰﾄﾞ初期化

	FOR rec_ome_eno_data IN cur_ome_eno_data LOOP

		/*当該OEMｺｰﾄﾞ出力するにあたって、ﾌｧｲﾙがOPENされているかを判定する*/
		IF (BufOemCd = '0000') OR (BufOemCd <> rec_ome_eno_data.OEMｺｰﾄﾞ) THEN

			/*前OEMｺｰﾄﾞ出力用ﾌｧｲﾙのClose*/
			IF BufOemCd <> '0000' THEN
				UTL_FILE.FCLOSE(FILE_HANDLE);
			END IF;

			/* ﾌｧｲﾙ名の取得 */
			nRet := aoba_out_common.get_file_info(
				aoba_out_const.CST_OUT_FASB010101_UTI,		-- JOBNETID
				vDate,									-- 年月
				cGun,									-- 群
				rec_ome_eno_data.OEMｺｰﾄﾞ,				-- コード
				vEdaban,								-- 枝番
				vFile_id,								-- ファイルID
				vLocation,								-- ロケーション
				vMax_lines,								-- 最大文字数
				cOpen_mode);							-- ファイルオープンモード

			vFile_id := vFile_id||'_'||vNyukinDate||'.txt';

			/* ﾌｧｲﾙOPEN */
	 		file_handle := UTL_FILE.FOPEN(vLocation, vFile_id, cOpen_mode, vMax_lines);	-- ファイルを作成

			/* ﾍｯﾀﾞ行の出力 */
			UTL_FILE.PUT_LINE(file_handle,
				'"' ||
				'顧客コード' || '","' ||
				'顧客名' || '","' ||
				'親ID' || '","' ||
				'ENO' || '","' ||
				'ENO表示用' || '","' ||
				'予備' || '","' ||
				'サービスコード' || '","' ||
				'請求額' || '","' ||
				'課金年月' || '","' ||
				'入金日' || '","' ||
				'振替結果フラグ' || '","' ||
				'振替結果名' || '"' ||
				aoba_out_const.CST_OUT_CR_CODE);

			/* 処理中OEMｺｰﾄﾞの取得 */
			BufOemCd := rec_ome_eno_data.OEMｺｰﾄﾞ;
		END IF;

		IF BufOemCd = rec_ome_eno_data.OEMｺｰﾄﾞ THEN

			/* 行出力 */
			UTL_FILE.PUT_LINE(file_handle,
				'"' || rec_ome_eno_data.顧客ｺｰﾄﾞ || '","' ||
				rec_ome_eno_data.顧客名 || '","' ||
				rec_ome_eno_data.親ID || '","' ||
				rec_ome_eno_data.ENO || '","' ||
				rec_ome_eno_data.表示用ENO || '","","'||
				rec_ome_eno_data.サービスコード || '",' ||
				rec_ome_eno_data.請求額 || ',"' ||
				rec_ome_eno_data.課金年月 || '","' ||
				rec_ome_eno_data.入金日 || '","' ||
				rec_ome_eno_data.振替結果ｺｰﾄﾞ || '","' ||
				rec_ome_eno_data.振替結果名 || '"' ||
				aoba_out_const.CST_OUT_CR_CODE);

				/* 処理中OEMｺｰﾄﾞの取得 */
				BufOemCd := rec_ome_eno_data.OEMｺｰﾄﾞ;
		END IF;
	END LOOP;

	/* 最後のOEM出力ﾌｧｲﾙをCLOSE */
	UTL_FILE.FCLOSE(file_handle);						--書き込み終了

END FASB010101_UTI;
/
