CREATE OR REPLACE PROCEDURE        FAGB010005(in_gun IN CHAR)
/********************************************************************/
/* 関数名：FARB010005                                               */
/* 機能名：請求サマリ作成 支払区分洗替                              */
/* 作成者：会沢　隆昭                                               */
/* 作成日：2005/11/23                                               */
/* 更新日：2006/02/17 TIS成毛 支払区分洗替を請求年月で行うよう修正  */
/* 更新日：2006/02/17 TIS成毛 半角スペースの郵ビルフラグをNULLと同  */
/*       ：                   様に扱うように修正                    */
/*       ：           TIS成毛 更新のキーに群を追加                  */
/*       ：2006/02/25 TIS成毛 顧客情報の支払区分で判定に修正        */
/*       ：2006/02/25 TIS成毛 5ヶ月経過分も洗替に修正               */
/*       ：2006/03/13 TIS成毛 洗替なしで前月を顧客情報の支払区分で  */
/*       ：                   洗替している個所を修正(洗替なしが正)  */
/*       ：2006/03/30 TIS成毛 用紙フラグの設定を変更                */
/*       ：                   郵ビルフラグなしのCVS系               */
/*       ：2006/07/13 TIS成毛 支払区分洗替マスタのOEMコード付加対応 */
/*       ：                   既存は支払区分だけで判断していた方式  */
/*       ：                   を支払区分＋OEMコードに変更           */
/* 		 ：2010/10/15 根来　　請求書ペーパレス対応          */
/* VER   ：1.00                                                     */
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
	CST_NYUKIN_NASHI                CONSTANT tb_seikyu_sum.nyukin_flg%TYPE  :=  '0';  -- 入金なし
	CST_SHIHARAI_KBN_KOUZA          CONSTANT CHAR(1) := '1';
	CST_SHIHARAI_KBN_YUBIN          CONSTANT CHAR(1) := '2';
	CST_SHIHARAI_KBN_YUBIN_SHONIN   CONSTANT CHAR(1) := '3';
	CST_SHIHARAI_KBN_YUBIN_BANK     CONSTANT CHAR(1) := '4';
	CST_SHIHARAI_KBN_YUBIN_CARD     CONSTANT CHAR(1) := '5';
	CST_SHIHARAI_KBN_YUBIN_MITUI    CONSTANT CHAR(1) := '9';
	CST_ARAIGAE_NASI			    CONSTANT CHAR(1) := '0';
	/****************/
	/* ローカル変数 */
	/****************/
	dSysDate			DATE;                                       	-- システム日時
	cKakinNengetu		wk_kokyaku_oem.kakin_nengetu%TYPE;   	       	-- 課金年月
	vUser				tb_seikyu_sum.insert_user%TYPE;         	    -- ユーザー
	cPrintTypeCd		tb_seikyu_sum.print_type_cd%TYPE;
	nDiffMonths			number;
	cShiharai_kbn_cd	tb_shiharai_araigae_mst.shiharai_kbn_cd%TYPE;	-- 支払区分コード
	nRet           NUMBER(1)                             := 0; -- 戻り値
	/* カーソル     */
	/* 顧客契約情報取得用カーソル */
	CURSOR cur_seikyu_sum ( in_kakin_nengetu  tb_seikyu_sum.kakin_nengetu%TYPE,
	                        in_seikyu_gun     tb_seikyu_sum.seikyu_gun%TYPE )
	IS
		SELECT B.kokyaku_cd                	B_kokyaku_cd,
		       B.kakin_nengetu             	B_kakin_nengetu,
		       B.seikyu_nengetu            	B_seikyu_nengetu,
		       B.seikyu_gun                 B_seikyu_gun,       --2006/02/17
		       A.shiharai_kbn_cd            A_shiharai_kbn_cd , --2006/02/27
		       B.shiharai_kbn_cd2           B_shiharai_kbn_cd,  --2006/03/13
		       C.shiharai_kbn_cd1          	C_shiharai_kbn_cd1,
		       C.shiharai_kbn_cd2          	C_shiharai_kbn_cd2,
		       C.shiharai_kbn_cd3          	C_shiharai_kbn_cd3,
		       C.shiharai_kbn_cd4          	C_shiharai_kbn_cd4,
		       C.shiharai_kbn_cd5          	C_shiharai_kbn_cd5,
		       DECODE(A.yubill_flg15,' ',null, A.yubill_flg15)  A_yubill_flg15,  --2006/02/17
		       DECODE(A.yubill_flg25,' ',null, A.yubill_flg25)  A_yubill_flg25   --2006/02/17
		FROM   tb_kokyaku_joho          A,    -- 顧客基本情報
		       tb_seikyu_sum@RF     B,    -- 請求サマリ
		       tb_shiharai_araigae_mst  C     -- 支払区分洗替マスタ
		WHERE  B.seikyu_nengetu     <  in_kakin_nengetu    --2006/02/17
--		WHERE  B.kakin_nengetu     <  in_kakin_nengetu
		AND    B.seikyu_gun        =  in_seikyu_gun
		AND    B.nyukin_flg        =  CST_NYUKIN_NASHI
		AND    B.kokyaku_cd        =  A.kokyaku_cd
		AND    A.shiharai_kbn_cd  =  C.shiharai_kbn_cd     --2006/02/27
		AND    A.oem_cd           =  C.oem_cd;             --2006/07/08
--
	rec_seikyu_sum  cur_seikyu_sum%ROWTYPE;
	/**********************/
	/* ローカルプロシジャ */
	/**********************/
	/* 超過日数計算 */
	FUNCTION GetPrintTypeCd(in_yubill_flg       IN  CHAR,
		                    in_shiharai_kbn_cd  IN  CHAR)
		                    RETURN CHAR
	IS
		out_prin_type_cd    CHAR(2) := '  ';
	BEGIN
-- NULL：申込なし、1：郵ビル(200円)、2：郵ビル+明細(550円)、3：郵ビル+明細(200円)、4：郵ビル+明細(350円)、5：郵ビル(0円)、6：郵ビル+明細(0円)
		IF in_yubill_flg IS NOT NULL THEN
-- 2010/10/08 start
			if in_yubill_flg in ('7','8') then
				CASE in_shiharai_kbn_cd
					WHEN '1' THEN
						out_prin_type_cd := '11';
					WHEN '2' THEN
						out_prin_type_cd := '12';
					WHEN '3' THEN
						out_prin_type_cd := '12';
					WHEN '9' THEN
						out_prin_type_cd := '12';
					WHEN '4' THEN
						out_prin_type_cd := '13';
					WHEN '5' THEN
						out_prin_type_cd := '14';
				END CASE;
			elsif in_yubill_flg not in ('7','8') then
-- 2010/10/08 end
				CASE in_shiharai_kbn_cd
					WHEN '1' THEN
						out_prin_type_cd := '01';
					WHEN '2' THEN
						out_prin_type_cd := '02';
					WHEN '3' THEN
						out_prin_type_cd := '02';
					WHEN '9' THEN
						out_prin_type_cd := '02';
					WHEN '4' THEN
						out_prin_type_cd := '03';
					WHEN '5' THEN
						out_prin_type_cd := '04';
				END CASE;
			end if;
		ELSE
			CASE in_shiharai_kbn_cd
				WHEN '1' THEN
					out_prin_type_cd := '11';
				WHEN '2' THEN
					out_prin_type_cd := '12'; --2006/03/30
					--out_prin_type_cd := '02';
				WHEN '3' THEN
					out_prin_type_cd := '12'; --2006/03/30
					--out_prin_type_cd := '02';
				WHEN '9' THEN
					out_prin_type_cd := '12'; --2006/03/30
					--out_prin_type_cd := '02';
				WHEN '4' THEN
					out_prin_type_cd := '13';
				WHEN '5' THEN
					out_prin_type_cd := '14';
			END CASE;
		END IF;
		RETURN out_prin_type_cd;
	END;
--
BEGIN
	/* システム日時取得 */
	nRet := aoba_common.get_date( dSysDate );

	/* ユーザー取得 */
	nRet := aoba_common.get_user( vUser );

	/* 課金年月取得 */
	nRet := aoba_common.get_kakin_nengetu( in_gun,
	                                       cKakinNengetu );
	/* 顧客契約情報の取得 */
	<<label_seikyu_sum>>
	FOR rec_seikyu_sum IN cur_seikyu_sum( cKakinNengetu, in_gun ) LOOP
--
		-- 2006/02/17
		nDiffMonths := CEIL(MONTHS_BETWEEN(TO_DATE(cKakinNengetu, CST_DATE_FORMAT_YM), TO_DATE(rec_seikyu_sum.B_seikyu_nengetu, CST_DATE_FORMAT_YM)));
		--nDiffMonths := CEIL(MONTHS_BETWEEN(TO_DATE(cKakinNengetu, CST_DATE_FORMAT_YM), TO_DATE(rec_seikyu_sum.B_kakin_nengetu, CST_DATE_FORMAT_YM)));

		IF nDiffMonths = 1 AND NVL(rec_seikyu_sum.C_shiharai_kbn_cd1,0) <> CST_ARAIGAE_NASI THEN
			cShiharai_kbn_cd := NVL(rec_seikyu_sum.C_shiharai_kbn_cd1,0);
		ELSIF nDiffMonths = 2 AND NVL(rec_seikyu_sum.C_shiharai_kbn_cd2,0) <> CST_ARAIGAE_NASI THEN
			cShiharai_kbn_cd := NVL(rec_seikyu_sum.C_shiharai_kbn_cd2,0);
		ELSIF nDiffMonths = 3 AND NVL(rec_seikyu_sum.C_shiharai_kbn_cd3,0) <> CST_ARAIGAE_NASI THEN
			cShiharai_kbn_cd := NVL(rec_seikyu_sum.C_shiharai_kbn_cd3,0);
		ELSIF nDiffMonths = 4 AND NVL(rec_seikyu_sum.C_shiharai_kbn_cd4,0) <> CST_ARAIGAE_NASI THEN
			cShiharai_kbn_cd :=  NVL(rec_seikyu_sum.C_shiharai_kbn_cd4,0);
		ELSIF nDiffMonths >= 5 AND NVL(rec_seikyu_sum.C_shiharai_kbn_cd5,0) <> CST_ARAIGAE_NASI THEN
			cShiharai_kbn_cd :=  NVL(rec_seikyu_sum.C_shiharai_kbn_cd5,0);
		ELSE
			--cShiharai_kbn_cd := rec_seikyu_sum.A_shiharai_kbn_cd;
			/* 洗替なしの場合は、請求サマリ履歴の支払区分コード2を設定 2006/03/13 */
			cShiharai_kbn_cd := rec_seikyu_sum.B_shiharai_kbn_cd;
		END IF;

		IF  in_gun  =  CST_GUN15  THEN
			cPrintTypeCd  :=  GetPrintTypeCd(rec_seikyu_sum.A_yubill_flg15, cShiharai_kbn_cd);
		ELSE
			cPrintTypeCd  :=  GetPrintTypeCd(rec_seikyu_sum.A_yubill_flg25, cShiharai_kbn_cd);
		END IF;
--
		UPDATE tb_seikyu_sum@RF
		SET
			  --shiharai_kbn_cd2      =   DECODE(cShiharai_kbn_cd,CST_ARAIGAE_NASI,shiharai_kbn_cd2,cShiharai_kbn_cd),
			  shiharai_kbn_cd2      = cShiharai_kbn_cd,                             -- 支払区分コード2(直近) 2006/03/13
			  print_type_cd         =   cPrintTypeCd,                               -- 請求書用紙種別コード
			  yubill_flg            =   DECODE(in_gun, CST_GUN15, rec_seikyu_sum.A_yubill_flg15,
			                                           CST_GUN25, rec_seikyu_sum.A_yubill_flg25),
			                                                                        -- 郵ビルフラグ
			  update_date           =   dSysDate,                                   -- 更新日時
			  update_user           =   'S_KBN_A'                                   -- 更新ユーザ
		WHERE kokyaku_cd            =   rec_seikyu_sum.B_kokyaku_cd
		AND   kakin_nengetu         =   rec_seikyu_sum.B_kakin_nengetu
		AND   seikyu_nengetu        =   rec_seikyu_sum.B_seikyu_nengetu
		AND   seikyu_gun            =   rec_seikyu_sum.B_seikyu_gun;  -- 2006/02/17
--
	END LOOP label_seikyu_sum;
--
END;
/
