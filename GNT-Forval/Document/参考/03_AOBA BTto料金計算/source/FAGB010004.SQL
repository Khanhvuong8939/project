CREATE OR REPLACE PROCEDURE           FAGB010004(in_gun IN CHAR)
/********************************************************************/
/* 関数名：FARB000004                                               */
/* 機能名：請求サマリ作成 まとめ処理                                */
/* 作成者：会沢　隆昭                                               */
/* 作成日：2005/11/23                                               */
/* 更新日：2006/01/26 TIS成毛 EANバーコードに引数(消費税)追加       */
/*       ：2006/01/26 TIS成毛 まとめ除外フラグの値を変更            */
/*       ：2006/01/27 TIS成毛 収納期限、請求書発効日の取得を修正    */
/*       ：2006/01/30 TIS成毛 翌月以降まとめの場合の、収納期限、請  */
/*       ：              求書発効日ををnullに設定するように変更     */
/*       ：2006/01/30 TIS成毛 25群のみまとめ判定をするように修正    */
/*       ：2006/01/31 TIS成毛 初回まとめ除外、未入金まとめ除外対応  */
/*       ：2006/04/13 服部【OEM対応】                               */
/*					  まとめ対象ﾃﾞｰﾀ判定にOEM基本情報.まとめ有無ﾌﾗｸﾞ*/
/*					  　の条件を追加						        */
/*					  請求下限額(Const)⇒OEM基本情報.閾値に変更     */
/*       ：2006/04/26 服部【OEM対応】                               */
/*					  25群のみまとめ判定を解除						*/
/* 		 ：2007/03/14 木原　　請求額上限値:定数→DB取得値に切替 	*/
/* VER   ：1.3                                                     	*/
/*------------------------------------------------------------------*/
/* 引数  ：請求群                                                   */
/********************************************************************/
IS
	/****************/
	/* ローカル定数 */
	/****************/
	CST_DATE_FORMAT                 CONSTANT CHAR(8) := 'YYYYMMDD';
	CST_DATE_FORMAT_YM              CONSTANT CHAR(6) := 'YYYYMM';
	CST_GUN15                       CONSTANT CHAR(2) := '15';
	CST_GUN25                       CONSTANT CHAR(2) := '25';
	CST_SHIHARAI_KBN_KOUZA          CONSTANT CHAR(1) := '1';
	CST_SHIHARAI_KBN_YUBIN          CONSTANT CHAR(1) := '2';
	CST_SHIHARAI_KBN_YUBIN_SHONIN   CONSTANT CHAR(1) := '3';
	CST_SHIHARAI_KBN_YUBIN_BANK     CONSTANT CHAR(1) := '4';
	CST_SHIHARAI_KBN_YUBIN_CARD     CONSTANT CHAR(1) := '5';
	CST_SHIHARAI_KBN_YUBIN_MITUI    CONSTANT CHAR(1) := '9';
	CST_TANGETU_SEIKYU_CD    		CONSTANT CHAR(1) := '1';--まとめ無しの単月請求
	CST_MATOME_KINGAKU_CD    		CONSTANT CHAR(1) := '2';--まとめ有り（金額によるまとめ）
	CST_MATOME_TUKI_CD				CONSTANT CHAR(1) := '3';--まとめ有り（最長月によるまとめ）
	CST_RAIGETU_MATOME_CD    		CONSTANT CHAR(1) := '4';--まとめ有り（来月以降にまとめ）
	CST_SEIKYUNASI_MATOME_CD 		CONSTANT CHAR(1) := '5';--まとめ有り（当月請求無しによるまとめ）
	
	CST_MATOME_UMU_FLG				CONSTANT CHAR(1) := 'Y';--まとめ有	--【OEM対応】

	/****************/
	/* ローカル変数 */
	/****************/
	dSysDate       DATE;                                       -- システム日時
	cKakinNengetu  wk_kokyaku_oem.kakin_nengetu%TYPE;          -- 課金年月
	vUser          tb_seikyu_sum.insert_user%TYPE;             -- ユーザー
	cPrintTypeCd   tb_seikyu_sum.print_type_cd%TYPE;
	nDiffMonths    NUMBER;

	nRet           NUMBER(1)                             := 0; -- 戻り値
	nSeikyugaku	   NUMBER;									   -- 請求額ワーク

	/* カーソル     */
	/* まとめ対象取得用カーソル */
	CURSOR cur_seikyu_sum ( in_kakin_nengetu  tb_seikyu_sum.kakin_nengetu%TYPE,
	                        in_seikyu_gun     tb_seikyu_sum.seikyu_gun%TYPE )
	IS
		SELECT  AA1.kokyaku_cd,
		        AA1.kakin_nengetu,
		        AA1.seikyu_nengetu,
		        AA1.seikyu_gun,
		        AA1.oem_cd,
		        AA1.shiharai_kbn_cd1,
		        AA2.g_kokyaku_cd,
		        AA2.g_seikyu_nengetu,
		        AA2.g_seikyu_gun,
		        AA2.g_seikyugaku_sum,
		        AA2.g_tax_sum,
		        AA2.g_kakin_nengetu_min,
		        AA2.g_kakin_nengetu_max,
				DECODE(AA1.seikyu_gun,CST_GUN15,AA3.matomegaku_limit15,AA3.matomegaku_limit25) AS matomegaku_limit	--【OEM対応】
		FROM (
				/* 請求サマリ当月 */
				SELECT A1.kokyaku_cd,
				       A1.kakin_nengetu,
				       A1.seikyu_nengetu,
				       A1.seikyu_gun,
			           A1.oem_cd,
			           A1.shiharai_kbn_cd1
				FROM   tb_seikyu_sum         A1,    -- 請求サマリ
				       tb_kokyaku_joho       B1     -- 顧客基本情報
				WHERE  A1.kokyaku_cd        =  B1.kokyaku_cd
				AND    NVL(B1.matome_jogai_flg,0)  =  '0'
				UNION
				/* 請求サマリ履歴 */
				SELECT A2.kokyaku_cd,
				       A2.kakin_nengetu,
				       A2.seikyu_nengetu,
				       A2.seikyu_gun,
			           A2.oem_cd,
			           A2.shiharai_kbn_cd1
				FROM   tb_seikyu_sum@RF  A2
				WHERE  A2.matome_flg        =  aoba_const.CST_Y_FLG
				AND    A2.seikyu_nengetu    =  in_kakin_nengetu
				AND    A2.seikyu_gun        =  in_seikyu_gun
			  ) AA1,
			 (
				SELECT A2.kokyaku_cd           g_kokyaku_cd,
					   A2.seikyu_nengetu       g_seikyu_nengetu,
					   A2.seikyu_gun           g_seikyu_gun,
					   SUM(A2.seikyugaku)      g_seikyugaku_sum,
					   SUM(A2.tax)             g_tax_sum,
					   SUBSTRB(MIN(A2.kakin_nengetu),1,6)   g_kakin_nengetu_min,
					   SUBSTRB(MAX(A2.kakin_nengetu),1,6)   g_kakin_nengetu_max
				FROM (
						/* 請求サマリ当月 */
						SELECT A.kokyaku_cd,
						       A.kakin_nengetu,
						       A.seikyu_nengetu,
						       A.seikyu_gun,
						       A.seikyugaku,
						       A.tax
						FROM   tb_seikyu_sum         A,    -- 請求サマリ
						       tb_kokyaku_joho       B     -- 顧客基本情報
						WHERE  A.kokyaku_cd        =  B.kokyaku_cd
						AND    NVL(B.matome_jogai_flg,0)  =  '0'
						UNION
						/* 請求サマリ履歴 */
						SELECT A.kokyaku_cd,
						       A.kakin_nengetu,
						       A.seikyu_nengetu,
						       A.seikyu_gun,
						       A.seikyugaku,
						       A.tax
						FROM   tb_seikyu_sum@RF  A
						WHERE  A.matome_flg        =  aoba_const.CST_Y_FLG
						AND    A.seikyu_nengetu    =  in_kakin_nengetu
						AND    A.seikyu_gun        =  in_seikyu_gun
					) A2
				GROUP BY   A2.kokyaku_cd,
					       A2.seikyu_nengetu,
					       A2.seikyu_gun
			) AA2,
			tb_oem_joho			AA3		-- OEM基本情報 										  【OEM対応】
		WHERE  AA1.kokyaku_cd      =  AA2.g_kokyaku_cd
		AND    AA1.seikyu_nengetu  =  AA2.g_seikyu_nengetu
		AND    AA1.seikyu_gun      =  AA2.g_seikyu_gun
		AND	   AA1.oem_cd		   =  AA3.oem_cd											--【OEM対応】
		AND	   DECODE(AA1.seikyu_gun,CST_GUN15,AA3.matome_umu_flg15,AA3.matome_umu_flg25)	--【OEM対応】
								   =  CST_MATOME_UMU_FLG;
--
	rec_seikyu_sum  cur_seikyu_sum%ROWTYPE;

	/* 初回請求判定カーソル */
	CURSOR cur_shokai_hantei ( in_kokyaku_cd     tb_seikyu_sum.kokyaku_cd%TYPE,
	                           in_seikyu_gun     tb_seikyu_sum.seikyu_gun%TYPE )
	IS
		SELECT  COUNT(*) CNT
		FROM    TB_SEIKYU_SUM@RF
		WHERE   kokyaku_cd = in_kokyaku_cd
		AND     seikyu_gun = in_seikyu_gun;

	rec_shokai_hantei  cur_shokai_hantei%ROWTYPE;

	/* 未入金判定カーソル */
	CURSOR cur_minyukin_hantei ( in_kokyaku_cd     tb_seikyu_sum.kokyaku_cd%TYPE,
	                             in_seikyu_gun     tb_seikyu_sum.seikyu_gun%TYPE,
	                             in_seikyu_nengetu tb_seikyu_sum.seikyu_nengetu%TYPE)
	IS
		SELECT  COUNT(*) CNT
		FROM    TB_SEIKYU_SUM@RF
		WHERE   kokyaku_cd = in_kokyaku_cd
		AND     seikyu_gun = in_seikyu_gun
		AND     seikyu_nengetu < in_seikyu_nengetu
		AND     nyukin_flg = 0;

	rec_minyukin_hantei  cur_minyukin_hantei%ROWTYPE;


--
	/**********************/
	/* ローカルプロシジャ */
	/**********************/
	/* 請求サマリ更新 */
	/* 発行年月日、収納期限、バーコードの更新あり */
	PROCEDURE UpdateSeikyuSum(in_shori_cd	      IN  CHAR,
	                          in_kakin_nengetu    IN  tb_seikyu_sum.kakin_nengetu%TYPE,
	                          in_cur_rec          IN  cur_seikyu_sum%ROWTYPE)
	IS
		cHurikaeNyukinDate  tb_schedule_mst.hurikae_nyukin_date%TYPE;
		cCvsNyukinDate      tb_schedule_mst.cvs_nyukin_date%TYPE;
		cOtherNyukinDate    tb_schedule_mst.other_nyukin_date%TYPE;
		cItakuCd            CHAR(5);
		vBarJAN1            VARCHAR2(20);
		vBarJAN2            VARCHAR2(20);
		vBarJAN3            VARCHAR2(20);
		vBarJAN4            VARCHAR2(20);
		vBarEAN             VARCHAR2(44);
		nRet                NUMBER  := 0;
	BEGIN
		SELECT A.hurikae_nyukin_date,
		       A.cvs_nyukin_date,
		       A.other_nyukin_date
		INTO   cHurikaeNyukinDate,
		       cCvsNyukinDate,
		       cOtherNyukinDate
		FROM   tb_schedule_mst A     -- スケジュールマスタ
		WHERE  A.kakin_nengetu = in_cur_rec.seikyu_nengetu
		AND    A.seikyu_gun = in_cur_rec.seikyu_gun
		AND    A.oem_cd = in_cur_rec.oem_cd;
--
		SELECT DECODE(in_gun, CST_GUN15, A.cvs_itaku_cd15,
		                      CST_GUN25, A.cvs_itaku_cd25)
		INTO   cItakuCd
		FROM   tb_oem_joho A     -- OEM基本情報
		WHERE  A.oem_cd = in_cur_rec.oem_cd;

--		請求額がバーコード最大金額以上の場合は０円でバーコードを作成する
/* 2007/03/14 修正 木原　定数→DB取得値に切替 */
		--IF NVL(in_cur_rec.g_seikyugaku_sum,0) < aoba_rate_const.CST_BAR_MAX_KINGAKU THEN
		IF NVL(in_cur_rec.g_seikyugaku_sum,0) < aoba_rate_common.get_seikyugaku_max THEN
/* end of 2007/03/14 修正 木原　定数→DB取得値に切替 */
			nSeikyugaku := NVL(in_cur_rec.g_seikyugaku_sum,0);
		else
			nSeikyugaku := 0;
		END IF;

		/* バーコード取得(EAN) */
		nRet  :=  FUNC_BARCODE_EAN(
				in_cur_rec.kokyaku_cd,
				in_cur_rec.seikyu_nengetu,
				nSeikyugaku,
				in_cur_rec.g_tax_sum,
				cItakuCd,
				in_gun,
				vBarEAN
				);

		/* バーコード取得(JAN) */
		nRet  :=  FUNC_BARCODE_JAN(
				in_cur_rec.kokyaku_cd,
				in_cur_rec.seikyu_nengetu,
				nSeikyugaku,
				cItakuCd,
				vBarJAN1,
				vBarJAN2,
				vBarJAN3,
				vBarJAN4
				);

		IF in_shori_cd IN (CST_TANGETU_SEIKYU_CD,CST_MATOME_KINGAKU_CD,CST_MATOME_TUKI_CD,CST_RAIGETU_MATOME_CD) THEN
			/* 当月請求サマリ更新 */
			UPDATE	tb_seikyu_sum
			SET		matome_flg = DECODE(in_shori_cd,CST_TANGETU_SEIKYU_CD,aoba_const.CST_N_FLG,aoba_const.CST_Y_FLG),
			     	seikyu_nengetu = in_kakin_nengetu,
			     	shuno_kigen = (
									CASE
										WHEN in_shori_cd IN (CST_MATOME_KINGAKU_CD,CST_MATOME_TUKI_CD) THEN
											DECODE(in_cur_rec.shiharai_kbn_cd1, CST_SHIHARAI_KBN_KOUZA, cHurikaeNyukinDate,
									                                            CST_SHIHARAI_KBN_YUBIN, cCvsNyukinDate,
									       	                                                            cOtherNyukinDate)
										WHEN in_shori_cd IN (CST_RAIGETU_MATOME_CD) THEN
											null
										ELSE shuno_kigen
									END
								   ) ,
					bar_cd_jan1 = (
									CASE
										WHEN in_shori_cd IN (CST_MATOME_KINGAKU_CD,CST_MATOME_TUKI_CD) THEN vBarJAN1
										ELSE bar_cd_jan1
									END
								   ) ,												-- バーコードJAN1
					bar_cd_jan2 = (
									CASE
										WHEN in_shori_cd IN (CST_MATOME_KINGAKU_CD,CST_MATOME_TUKI_CD) THEN vBarJAN2
										ELSE bar_cd_jan2
									END
								   ) ,												-- バーコードJAN1
					bar_cd_jan3 = (
									CASE
										WHEN in_shori_cd IN (CST_MATOME_KINGAKU_CD,CST_MATOME_TUKI_CD) THEN vBarJAN3
										ELSE bar_cd_jan3
									END
								   ) ,												-- バーコードJAN1
					bar_cd_jan4 = (
									CASE
										WHEN in_shori_cd IN (CST_MATOME_KINGAKU_CD,CST_MATOME_TUKI_CD) THEN vBarJAN4
										ELSE bar_cd_jan4
									END
								   ) ,												-- バーコードJAN1
					bar_cd_ean128 = (
									CASE
										WHEN in_shori_cd IN (CST_MATOME_KINGAKU_CD,CST_MATOME_TUKI_CD) THEN vBarEAN
										ELSE bar_cd_ean128
									END
								   ) ,												-- バーコードEAN128
				 	seikyusho_hakko_date = (
											CASE
												WHEN in_shori_cd IN (CST_MATOME_KINGAKU_CD,CST_MATOME_TUKI_CD) THEN
													(	SELECT seikyusho_hakko_date
						                        		FROM   tb_schedule_mst A
					    	                    		WHERE  A.kakin_nengetu  =  in_cur_rec.seikyu_nengetu
					        	                		AND    A.seikyu_gun     =  in_cur_rec.seikyu_gun
					            	            		AND    A.oem_cd         =  in_cur_rec.oem_cd
					                	       		)
												WHEN in_shori_cd IN (CST_RAIGETU_MATOME_CD) THEN
													null
												ELSE seikyusho_hakko_date
											END
								   			),                                    	-- 請求書発効日
				  	update_date           =  dSysDate,                              -- 更新日時
				  	update_user           =  vUser                                  -- 更新ユーザ
			WHERE	kokyaku_cd            =  in_cur_rec.kokyaku_cd
			AND		kakin_nengetu         =  in_cur_rec.kakin_nengetu
			AND		seikyu_gun            =  in_cur_rec.seikyu_gun;
		END IF;
--
		IF in_shori_cd IN (CST_MATOME_KINGAKU_CD,CST_MATOME_TUKI_CD,CST_RAIGETU_MATOME_CD,CST_SEIKYUNASI_MATOME_CD) THEN
			/* 履歴請求サマリ更新 */
			UPDATE	tb_seikyu_sum@RF
			SET		matome_flg      =  aoba_const.CST_Y_FLG,
					seikyu_nengetu  =  in_kakin_nengetu,
			     	shuno_kigen = (
									CASE
										WHEN in_shori_cd IN (CST_MATOME_KINGAKU_CD,CST_MATOME_TUKI_CD,CST_SEIKYUNASI_MATOME_CD) THEN
											DECODE(in_cur_rec.shiharai_kbn_cd1, CST_SHIHARAI_KBN_KOUZA, cHurikaeNyukinDate,
									                                            CST_SHIHARAI_KBN_YUBIN, cCvsNyukinDate,
									       	                                                            cOtherNyukinDate)
										WHEN in_shori_cd IN (CST_RAIGETU_MATOME_CD) THEN
											null
										ELSE shuno_kigen
									END
								   ) ,
					bar_cd_jan1 = (
									CASE
										WHEN in_shori_cd IN (CST_MATOME_KINGAKU_CD,CST_MATOME_TUKI_CD,CST_SEIKYUNASI_MATOME_CD) THEN vBarJAN1
										ELSE bar_cd_jan1
									END
								   ) ,												-- バーコードJAN1
					bar_cd_jan2 = (
									CASE
										WHEN in_shori_cd IN (CST_MATOME_KINGAKU_CD,CST_MATOME_TUKI_CD,CST_SEIKYUNASI_MATOME_CD) THEN vBarJAN2
										ELSE bar_cd_jan2
									END
								   ) ,												-- バーコードJAN1
					bar_cd_jan3 = (
									CASE
										WHEN in_shori_cd IN (CST_MATOME_KINGAKU_CD,CST_MATOME_TUKI_CD,CST_SEIKYUNASI_MATOME_CD) THEN vBarJAN3
										ELSE bar_cd_jan3
									END
								   ) ,												-- バーコードJAN1
					bar_cd_jan4 = (
									CASE
										WHEN in_shori_cd IN (CST_MATOME_KINGAKU_CD,CST_MATOME_TUKI_CD,CST_SEIKYUNASI_MATOME_CD) THEN vBarJAN4
										ELSE bar_cd_jan4
									END
								   ) ,												-- バーコードJAN1
					bar_cd_ean128 = (
									CASE
										WHEN in_shori_cd IN (CST_MATOME_KINGAKU_CD,CST_MATOME_TUKI_CD,CST_SEIKYUNASI_MATOME_CD) THEN vBarEAN
										ELSE bar_cd_ean128
									END
								   ) ,												-- バーコードEAN128
				 	seikyusho_hakko_date = (
											CASE
												WHEN in_shori_cd IN (CST_MATOME_KINGAKU_CD,CST_MATOME_TUKI_CD,CST_SEIKYUNASI_MATOME_CD) THEN
													(	SELECT seikyusho_hakko_date
						                        		FROM   tb_schedule_mst A
					    	                    		WHERE  A.kakin_nengetu  =  in_cur_rec.seikyu_nengetu
					        	                		AND    A.seikyu_gun     =  in_cur_rec.seikyu_gun
					            	            		AND    A.oem_cd         =  in_cur_rec.oem_cd
					                	       		)
												WHEN in_shori_cd IN (CST_RAIGETU_MATOME_CD) THEN
													null
												ELSE seikyusho_hakko_date
											END
								   			),                                    	-- 請求書発効日
				  	update_date           =   dSysDate,                                 -- 更新日時
				  	update_user           =   vUser                                     -- 更新ユーザ
			WHERE	kokyaku_cd            =  in_cur_rec.kokyaku_cd
			AND		kakin_nengetu         =  in_cur_rec.kakin_nengetu
			AND		seikyu_gun            =  in_cur_rec.seikyu_gun;
		END IF;
	END;

BEGIN
	/* システム日時取得 */
	nRet := aoba_common.get_date( dSysDate );

	/* ユーザー取得 */
	nRet := aoba_common.get_user( vUser );

	/* 課金年月取得 */
	nRet := aoba_common.get_kakin_nengetu( in_gun,
	                                       cKakinNengetu );

	/* 25群の場合のみまとめ判定 */
	/* 【OEM対応】
	IF in_gun = CST_GUN25
	THEN
	*/

		/* 請求サマリの取得 */
		<<label_seikyu_sum>>
		FOR rec_seikyu_sum IN cur_seikyu_sum( cKakinNengetu, in_gun ) LOOP
--
			nDiffMonths  :=  MONTHS_BETWEEN(TO_DATE(cKakinNengetu, CST_DATE_FORMAT_YM), TO_DATE(rec_seikyu_sum.g_kakin_nengetu_min, CST_DATE_FORMAT_YM)) + 1;
			/*【OEM対応】*/
			/*IFrec_seikyu_sum.g_seikyugaku_sum >= aoba_rate_const.CST_MATOME_SEIKYUGAKU_MIN*/
			IF	rec_seikyu_sum.g_seikyugaku_sum >= rec_seikyu_sum.matomegaku_limit
			AND rec_seikyu_sum.g_kakin_nengetu_min = cKakinNengetu THEN
				-- 単月請求
				UpdateSeikyuSum( CST_TANGETU_SEIKYU_CD,cKakinNengetu,rec_seikyu_sum );
			/*【OEM対応】*/
			/*ELSIF rec_seikyu_sum.g_seikyugaku_sum >= aoba_rate_const.CST_MATOME_SEIKYUGAKU_MIN*/
			ELSIF	rec_seikyu_sum.g_seikyugaku_sum >= rec_seikyu_sum.matomegaku_limit
			AND		rec_seikyu_sum.g_kakin_nengetu_min < cKakinNengetu
			AND		rec_seikyu_sum.g_kakin_nengetu_max = cKakinNengetu THEN
				-- まとめ有り（金額によるまとめ）
				UpdateSeikyuSum( CST_MATOME_KINGAKU_CD,cKakinNengetu, rec_seikyu_sum );
			/*【OEM対応】*/
			/*ELSIF	rec_seikyu_sum.g_seikyugaku_sum < aoba_rate_const.CST_MATOME_SEIKYUGAKU_MIN*/
			ELSIF	rec_seikyu_sum.g_seikyugaku_sum < rec_seikyu_sum.matomegaku_limit
			AND		nDiffMonths >= aoba_rate_const.CST_MATOME_SEIKYU_TUKI_MAX
			AND		rec_seikyu_sum.g_kakin_nengetu_max = cKakinNengetu THEN
				-- まとめ有り（最長月によるまとめ）
				UpdateSeikyuSum( CST_MATOME_TUKI_CD,cKakinNengetu, rec_seikyu_sum );
			/*【OEM対応】*/
			/*ELSIF	rec_seikyu_sum.g_seikyugaku_sum < aoba_rate_const.CST_MATOME_SEIKYUGAKU_MIN*/
			ELSIF	rec_seikyu_sum.g_seikyugaku_sum < rec_seikyu_sum.matomegaku_limit
			AND		nDiffMonths < aoba_rate_const.CST_MATOME_SEIKYU_TUKI_MAX
			AND		rec_seikyu_sum.g_kakin_nengetu_max = cKakinNengetu THEN

					/* 初回請求判定 */
					open cur_shokai_hantei(  rec_seikyu_sum.kokyaku_cd,
										     rec_seikyu_sum.seikyu_gun);
					fetch cur_shokai_hantei into rec_shokai_hantei;
					close cur_shokai_hantei;

					/* 未入金判定 */
					open cur_minyukin_hantei(  rec_seikyu_sum.kokyaku_cd,
											   rec_seikyu_sum.seikyu_gun,
											   rec_seikyu_sum.seikyu_nengetu
											);
					fetch cur_minyukin_hantei into rec_minyukin_hantei;
					close cur_minyukin_hantei;

					/* まとめ判定 */
					--初回の場合
					IF rec_shokai_hantei.CNT = 0
					THEN
						--まとめなし
						UpdateSeikyuSum( CST_TANGETU_SEIKYU_CD,cKakinNengetu,rec_seikyu_sum );
					--未入金がある場合
					ELSIF rec_minyukin_hantei.CNT > 0
					THEN
						--まとめなし
						UpdateSeikyuSum( CST_TANGETU_SEIKYU_CD,cKakinNengetu,rec_seikyu_sum );
					--上記以外
					ELSE
						-- まとめ有り（来月以降にまとめ）
						UpdateSeikyuSum( CST_RAIGETU_MATOME_CD,TO_CHAR(ADD_MONTHS(TO_DATE(cKakinNengetu, CST_DATE_FORMAT_YM), 1), CST_DATE_FORMAT_YM),
						                 rec_seikyu_sum );
					END IF;

			ELSIF	rec_seikyu_sum.g_kakin_nengetu_max < cKakinNengetu THEN
			--AND		nDiffMonths < aoba_rate_const.CST_MATOME_SEIKYU_TUKI_MAX THEN
				--まとめ有り（当月請求無しによるまとめ）
				UpdateSeikyuSum( CST_SEIKYUNASI_MATOME_CD,cKakinNengetu, rec_seikyu_sum );
			END IF;
--
		END LOOP label_seikyu_sum;

	/*【OEM対応】
	END IF;
	*/

END;
/
