CREATE OR REPLACE PROCEDURE FAOB010119_06
/********************************************************************/
/*	関数名：FAOB010119_06											*/
/*	機能名：請求書ペーパレス対応？ 請求書備考情報一時TBL更新		*/
/*	作成者：根来													*/
/*	作成日：2010/08/10												*/
/*	更新日：														*/
/*	VER   ：1.00													*/
/*------------------------------------------------------------------*/
/*	引数：		無し												*/
/*	戻値：		無し												*/
/********************************************************************/
IS
	/* ローカル定数 */

	/* ローカル変数 */
	nRet					NUMBER; 										--リターンコード
	dSysDate				WK_BL_BIKO_COMMENT.insert_date%TYPE;			--システム日付
	vUser					WK_BL_BIKO_COMMENT.insert_user%TYPE;			--ユーザ

	vSeikyu_nengetu			TB_BATCH_CTRL.SHORI_TAISHO_NENGETU%type;		--処理対象課金年月
	vSeikyu_gun				TB_BATCH_CTRL.SHORI_TAISHO_GUN%type;			--処理対象請求群
	
	nCnt					number;


	/****************************/
	/* カーソル定義				*/
	/****************************/
	/* 当月分データ抽出 カーソル */
	CURSOR cur_tougetu
	IS
		SELECT	C.KOKYAKU_CD,
				B.KAKIN_NENGETU_SK KAKIN_NENGETU,
				CASE WHEN vSeikyu_gun = '15' THEN
					C.YUBILL_FLG15
				ELSE
					C.YUBILL_FLG25	
				END AS YUBILL_FLG,
				A.OEM_CD,
				A.PRINT_TYPE_CD,
				A.TOGETU_FLG
		FROM	TB_YEC_PNT_OEM_MST A,
				(
				SELECT	AA.*,
						AB.DATA_TYPE
				from	WK_BL_KOKYAKU AA,
		  				(SELECT DATA_TYPE,
		    					KOKYAKU_CD,
		    					MAX(SEIKYU_NENGETU) as SEIKYU_NENGETU
		  				 FROM	WK_BL_SEIKYU_SUM
		  				 WHERE	KAKIN_NENGETU != SEIKYU_NENGETU
		  				 GROUP BY	DATA_TYPE,
		    						KOKYAKU_CD
		  				 UNION
		  				 SELECT	DATA_TYPE,
		    					KOKYAKU_CD,
		    					SEIKYU_NENGETU
		  				 FROM	WK_BL_SEIKYU_SUM
		  				 WHERE 	KAKIN_NENGETU = SEIKYU_NENGETU) AB
				WHERE	AA.KOKYAKU_CD_SK		=	AB.KOKYAKU_CD
				AND		AA.KAKIN_NENGETU_SK		=	AB.SEIKYU_NENGETU
				AND		(AB.DATA_TYPE			=	'01'
				OR		 (AB.DATA_TYPE			=	'03'
				AND		  AA.KAKIN_NENGETU_SK	=	vSeikyu_nengetu))
				ORDER BY	KOKYAKU_CD_SK,
							KAKIN_NENGETU_SK,
							PRINT_TYPE_CD_SK,
							DOHU_TIRASHI_CD_SK
				) B,
				TB_KOKYAKU_JOHO C
		WHERE	A.PRINT_TYPE_CD					<>	'XX'
		AND		(B.DATA_TYPE					=	'01'
		OR		 (B.DATA_TYPE					=	'03'
		AND		  B.KAKIN_NENGETU_SK			=	vSeikyu_nengetu))
		AND		A.OEM_CD						=	B.OEM_CD_SK
		AND		A.PRINT_TYPE_CD					=	B.PRINT_TYPE_CD_SK
		AND		B.KOKYAKU_CD_SK					=	C.KOKYAKU_CD;

	rec_tougetu cur_tougetu%ROWTYPE;

	/* 再請求データ抽出 カーソル */
	CURSOR cur_saiseikyu
	IS
		SELECT	C.KOKYAKU_CD,
				B.KAKIN_NENGETU_SK KAKIN_NENGETU,
				B.SEIKYU_NENGETU,
				CASE WHEN vSeikyu_gun = '15' THEN
					C.YUBILL_FLG15
				ELSE
					C.YUBILL_FLG25	
				END AS YUBILL_FLG,
				A.OEM_CD,
				A.PRINT_TYPE_CD,
				A.SAISEIKYU_FLG
		FROM	TB_YEC_PNT_OEM_MST A,
				(
				SELECT	AA.*,
						AB.SEIKYU_NENGETU,
						AB.DATA_TYPE
				from	WK_BL_KOKYAKU AA,
		  				(SELECT DATA_TYPE,
		    					KOKYAKU_CD,
		    					MAX(SEIKYU_NENGETU) as SEIKYU_NENGETU
		  				 FROM	WK_BL_SEIKYU_SUM
		  				 WHERE	KAKIN_NENGETU != SEIKYU_NENGETU
		  				 GROUP BY	DATA_TYPE,
		    						KOKYAKU_CD
		  				 UNION
		  				 SELECT	DATA_TYPE,
		    					KOKYAKU_CD,
		    					SEIKYU_NENGETU
		  				 FROM	WK_BL_SEIKYU_SUM
		  				 WHERE 	KAKIN_NENGETU = SEIKYU_NENGETU) AB
				WHERE	AA.KOKYAKU_CD_SK		=	AB.KOKYAKU_CD
				AND		AA.KAKIN_NENGETU_SK		=	AB.SEIKYU_NENGETU
				AND		(AB.DATA_TYPE			=	'02'
				OR		 (AB.DATA_TYPE			=	'03'
				AND		  AA.KAKIN_NENGETU_SK	<	vSeikyu_nengetu))
				ORDER BY	KOKYAKU_CD_SK,
							KAKIN_NENGETU_SK,
							PRINT_TYPE_CD_SK,
							DOHU_TIRASHI_CD_SK
				) B,
				TB_KOKYAKU_JOHO C
		WHERE	A.PRINT_TYPE_CD					<>	'XX'
		AND		(B.DATA_TYPE					=	'02'
		OR		 (B.DATA_TYPE					=	'03'
		AND		  B.KAKIN_NENGETU_SK			<	vSeikyu_nengetu))
		AND		A.OEM_CD						=	B.OEM_CD_SK
		AND		A.PRINT_TYPE_CD					=	B.PRINT_TYPE_CD_SK
		AND		B.KOKYAKU_CD_SK					=	C.KOKYAKU_CD;

	rec_saiseikyu cur_saiseikyu%ROWTYPE;

BEGIN

	/********************************************/
	/* システム日時の取得                       */
	/********************************************/
	nRet := aoba_common.get_date( dSysDate );

	/********************************************/
	/* ユーザの取得                             */
	/********************************************/
	nRet := aoba_common.get_user( vUser );

	/********************************************/
	/* 処理対象課金年月、群の取得               */
	/********************************************/
	SELECT	SHORI_TAISHO_NENGETU,
			SHORI_TAISHO_GUN
	  INTO	vSeikyu_nengetu,
			vSeikyu_gun
	  FROM	TB_BATCH_CTRL
	 WHERE	JOBNET_ID = aoba_out_const.CST_OUT_FAOB010100;


	/********************************************/
	/* OEM単位でﾍﾟｰﾊﾟﾚｽのﾃﾞｰﾀの更新				*/
	/********************************************/
	UPDATE	WK_BL_BIKO_COMMENT A	SET
			A.YEC_PNT_FLG	=	
			(SELECT 'N'
			 FROM	TB_YEC_PNT_OEM_MST B,
				(
				SELECT	AA.KOKYAKU_CD_SK,
						AA.KAKIN_NENGETU_SK,
						AA.OEM_CD_SK
				from	WK_BL_KOKYAKU AA,
		  				(SELECT DATA_TYPE,
		    					KOKYAKU_CD,
		    					MAX(SEIKYU_NENGETU) as SEIKYU_NENGETU
		  				 FROM	WK_BL_SEIKYU_SUM
		  				 WHERE	KAKIN_NENGETU != SEIKYU_NENGETU
		  				 GROUP BY	DATA_TYPE,
		    						KOKYAKU_CD
		  				 UNION
		  				 SELECT	DATA_TYPE,
		    					KOKYAKU_CD,
		    					SEIKYU_NENGETU
		  				 FROM	WK_BL_SEIKYU_SUM
		  				 WHERE 	KAKIN_NENGETU = SEIKYU_NENGETU) AB
				WHERE	AA.KOKYAKU_CD_SK	=	AB.KOKYAKU_CD
				AND		AA.KAKIN_NENGETU_SK	=	AB.SEIKYU_NENGETU
				ORDER BY	KOKYAKU_CD_SK,
							KAKIN_NENGETU_SK,
							PRINT_TYPE_CD_SK,
							DOHU_TIRASHI_CD_SK
				) C
			 WHERE	A.KOKYAKU_CD_SK		=	C.KOKYAKU_CD_SK
			 AND	A.KAKIN_NENGETU_SK	=	C.KAKIN_NENGETU_SK
			 AND	C.OEM_CD_SK			=	B.OEM_CD
			 AND	B.PRINT_TYPE_CD		=	'XX');
			 
	commit;

	/********************************************/
	/* 当月ﾃﾞｰﾀの更新							*/
	/********************************************/

	nCnt := 0;
	
	FOR rec_tougetu IN cur_tougetu LOOP

		UPDATE	WK_BL_BIKO_COMMENT	SET
				YEC_PNT_FLG	=
				DECODE(rec_tougetu.TOGETU_FLG,'Y','Y',DECODE(trim(rec_tougetu.YUBILL_FLG),NULL,'N','Y'))
		WHERE	KOKYAKU_CD_SK				=	rec_tougetu.KOKYAKU_CD
		AND		KAKIN_NENGETU_SK			=	rec_tougetu.KAKIN_NENGETU;

		nCnt := nCnt + 1;
		
		if nCnt = 1000 then
			commit;
			nCnt := 0;
		end if;
	END LOOP;

	commit;

	/********************************************/
	/* 再請求ﾃﾞｰﾀの更新							*/
	/********************************************/

	nCnt := 0;

	FOR rec_saiseikyu IN cur_saiseikyu LOOP

		UPDATE	WK_BL_BIKO_COMMENT	SET
				YEC_PNT_FLG	=
				DECODE(rec_saiseikyu.SAISEIKYU_FLG,'Y','Y',DECODE(trim(rec_saiseikyu.YUBILL_FLG),NULL,'N','Y'))
		WHERE	KOKYAKU_CD_SK				=	rec_saiseikyu.KOKYAKU_CD
		AND		(KAKIN_NENGETU_SK			=	rec_saiseikyu.KAKIN_NENGETU
		OR		 (rec_saiseikyu.SEIKYU_NENGETU is not null
		AND		  KAKIN_NENGETU_SK			=	rec_saiseikyu.SEIKYU_NENGETU));

		nCnt := nCnt + 1;
		
		if nCnt = 1000 then
			commit;
			nCnt := 0;
		end if;
	END LOOP;

	commit;

END FAOB010119_06;
/
