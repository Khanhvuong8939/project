CREATE OR REPLACE PROCEDURE        FAGB010003(in_gun IN CHAR)
/********************************************************************/
/* 関数名：FARB000003                                               */
/* 機能名：請求サマリ作成 各種属性付与                              */
/* 作成者：会沢　隆昭                                               */
/* 作成日：2005/11/23                                               */
/* 更新日：2006/01/25 TIS成毛 メインカーソルにヒント句追加(TUNING)  */
/* 更新日：2006/01/26 TIS成毛 EANバーコードに引数(消費税)追加       */
/* 更新日：2006/02/04 TIS成毛 ID数、親ID数を内訳から取得に修正      */
/* 更新日：2006/02/17 TIS成毛 半角スペースの郵ビルフラグをNULLと同  */
/*       ：                   様に扱うように修正                    */
/* 更新日：2006/03/06 TIS成毛 請求書発行フラグを下記に修正          */
/*       ：                   顧客情報の発行フラグが'N'以外の場合は */
/*       ：                   既に設定されている値('N')を引継ぐ     */
/* 更新日：2006/03/27 TIS成毛 FTフォンのID数からGNO除外             */
/* 更新日：2006/03/30 TIS成毛 用紙フラグの設定を変更                */
/*       ：                   郵ビルフラグなしのCVS系               */
/* 更新日：2006/06/26 TIS成毛 領収書発行ﾌﾗｸﾞを顧客基本情報の下記の  */
/*       ：                   項目より設定する方式に変更            */
/*       ：                   ・KOZA_BNG_HYOJI_FLG                  */
/*       ：                     "1" :設定あり(全表示)               */
/*       ：                     "2" :設定あり(＊埋め)               */
/*       ：                     上記以外 :設定なし                  */
/* 		 ：2007/03/14 木原　　請求額上限値:定数→DB取得値に切替 	*/
/* 		 ：2010/10/08 根来　　請求書ペーパレス対応				 	*/
/* VER   ：1.02                                                     */
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
	CST_SHIHARAI_KBN_BANK           CONSTANT CHAR(1) := '4';
	CST_SHIHARAI_KBN_CARD           CONSTANT CHAR(1) := '5';
	CST_SHIHARAI_KBN_YUBIN_MITUI    CONSTANT CHAR(1) := '9';
	CST_SEIKYUSHO_HUYO              CONSTANT CHAR(1) := 'X';
	CST_KOZA_HYOJI_ARI              CONSTANT tb_seikyu_sum.koza_bng_hyoji_flg%TYPE  :=  '1';
	CST_KOZA_HYOJI_NASHI            CONSTANT tb_seikyu_sum.koza_bng_hyoji_flg%TYPE  :=  '0';

	/****************/
	/* ローカル変数 */
	/****************/
	dSysDate       DATE;                                       -- システム日時
	cKakinNengetu  wk_kokyaku_oem.kakin_nengetu%TYPE;          -- 課金年月
	vUser          tb_seikyu_sum.insert_user%TYPE;             -- ユーザー
	cPrintTypeCd   tb_seikyu_sum.print_type_cd%TYPE;
	cItakuCd       CHAR(5);
	vBarJAN1       VARCHAR2(20);
	vBarJAN2       VARCHAR2(20);
	vBarJAN3       VARCHAR2(20);
	vBarJAN4       VARCHAR2(20);
	vBarEAN        VARCHAR2(44);
	cSaveHakkoFlg  tb_seikyu_sum.seikyusho_hakko_flg%TYPE;

	nRet           NUMBER(1)                             := 0; -- 戻り値
	nSeikyugaku	   NUMBER;									   -- 請求額ワーク

	/* カーソル     */
	/* 顧客契約情報取得用カーソル */
	CURSOR cur_seikyu_sum ( in_kakin_nengetu  tb_seikyu_sum.kakin_nengetu%TYPE,
							in_seikyu_gun     tb_seikyu_sum.seikyu_gun%TYPE )
	IS
		SELECT /*+ FULL(A) FULL(B) FULL(C) FULL(D) FULL(E) FULL(VW1) FULL(VW2) USE_MERGE(A) USE_MERGE(B) USE_MERGE(C) USE_MERGE(D) USE_MERGE(E) USE_MERGE(VW1) USE_MERGE(VW2) */
		       C.oem_cd                    	C_oem_cd,                    
		       C.seikyu_nengetu            	C_seikyu_nengetu,            
		       C.seikyugaku                	C_seikyugaku,                
		       C.tax                       	C_tax,                       
		       B.kokyaku_cd                	B_kokyaku_cd,                
		       B.seikyusaki_kaisha_name    	B_seikyusaki_kaisha_name,    
		       B.seikyusaki_addr1          	B_seikyusaki_addr1,          
		       B.seikyusaki_addr2          	B_seikyusaki_addr2,          
		       B.seikyusaki_addr3          	B_seikyusaki_addr3,          
		       B.seikyusaki_addr4          	B_seikyusaki_addr4,          
		       B.seikyusaki_zip            	B_seikyusaki_zip,            
		       B.seikyusaki_busho_name     	B_seikyusaki_busho_name,     
		       B.seikyusaki_tel            	B_seikyusaki_tel,            
		       B.shiharai_kbn_cd           	B_shiharai_kbn_cd,           
		       B.bank_cd                   	B_bank_cd,                   
		       E.bank_name	            	E_bank_name,				 
		       B.shiten_cd                 	B_shiten_cd,                 
		       E.shiten_name	          	E_shiten_name, 				 
		       B.koza_type_cd              	B_koza_type_cd,              
		       B.koza_bng                  	B_koza_bng,                  
		       B.koza_meigi                	B_koza_meigi,                
		       B.card_kaisha_cd            	B_card_kaisha_cd,            
		       B.card_bng                  	B_card_bng,                  
		       B.yuko_kigen                	B_yuko_kigen,                
		       DECODE(B.yubill_flg15,' ',null,B.yubill_flg15)   B_yubill_flg15, -- 2006/02/17
		       DECODE(B.yubill_flg25,' ',null,B.yubill_flg25)   B_yubill_flg25, -- 2006/02/17
		       C.seikyusho_hakko_flg        C_seikyusho_hakko_flg,       -- 2006/03/06
		       B.seikyusho_hakko_flg       	B_seikyusho_hakko_flg,       
		       B.koza_bng_hyoji_flg        	B_koza_bng_hyoji_flg,        
		       B.dohu_tirashi_cd           	B_dohu_tirashi_cd,           
		       D.seikyusho_hakko_date      	D_seikyusho_hakko_date,      
		       D.hurikae_nyukin_date       	D_hurikae_nyukin_date,       
		       D.cvs_nyukin_date           	D_cvs_nyukin_date,           
		       D.other_nyukin_date         	D_other_nyukin_date,         
		       NVL(VW1.VW1_count,0)              	VW1_count,
		       NVL(VW2.VW2_count,0)               	VW2_count,
		       DECODE(B.koza_bng_hyoji_flg,
		            '1', aoba_const.CST_N_FLG,
		            '2', aoba_const.CST_N_FLG,
		            aoba_rate_const.CST_RYOSHUSHO_FLG_INIT)  B_ryoshusho_flg -- 2006/06/26
		FROM   wk_kokyaku_oem   A,    -- 顧客OEM対照表
		       tb_kokyaku_joho  B,    -- 顧客基本情報
		       tb_seikyu_sum    C,    -- 請求サマリ
		       tb_schedule_mst  D,    -- スケジュールマスタ
		       tb_bank_mst      E,    -- 金融機関マスタ
		       (
		           SELECT    A.kokyaku_cd    VW1_kokyaku_cd, 
		                     COUNT(A.id)     VW1_count
		           FROM      (
		                     SELECT    kokyaku_cd,
		                               id
		                     FROM      tb_seikyu_utiwake
		                     WHERE     id != 'ZZZZZZZZZZ'
		                     AND       (  srv_type_cd != '1011'	--2006/03/27
		                                  OR
		                                  (srv_type_cd = '1011' and id like '0%')
		                               )
		                     GROUP BY  kokyaku_cd,
		                               id
		                     ) A
		           GROUP BY  A.kokyaku_cd
		       )                VW1,  -- 顧客別ID情報ビュー1
		       (
		           SELECT    A.kokyaku_cd    VW2_kokyaku_cd, 
		                     COUNT(A.oya_id) VW2_count
		           FROM      (
		                     SELECT    kokyaku_cd,
		                               oya_id
		                     FROM      tb_seikyu_utiwake
		                     WHERE     oya_id != 'ZZZZZZZZZZ'
		                     GROUP BY  kokyaku_cd,
		                               oya_id
		                     ) A
		           GROUP BY  A.kokyaku_cd
		       )                VW2   -- 顧客別ID情報ビュー2
		WHERE  c.kakin_nengetu  =  in_kakin_nengetu
		AND    c.seikyu_gun     =  in_seikyu_gun
		AND    c.kokyaku_cd     =  a.kokyaku_cd
		AND    c.oem_cd         =  a.oem_cd
		AND    c.kakin_nengetu  =  a.kakin_nengetu
		AND    c.seikyu_gun     =  a.seikyu_gun
--
		AND    c.kokyaku_cd     =  b.kokyaku_cd
		AND    c.oem_cd         =  b.oem_cd
--
		AND    c.kakin_nengetu  =  d.kakin_nengetu
		AND    c.seikyu_gun     =  d.seikyu_gun
		AND    c.oem_cd         =  d.oem_cd
--
		AND    b.bank_cd        =  e.bank_cd(+)
		AND    b.shiten_cd      =  e.shiten_cd(+)
--
		AND    c.kokyaku_cd     =  VW1.vw1_kokyaku_cd(+)
		AND    c.kokyaku_cd     =  VW2.vw2_kokyaku_cd(+);
--
	rec_seikyu_sum  cur_seikyu_sum%ROWTYPE;
--	/**********************/
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
					out_prin_type_cd := '12'; -- 2006/03/30
					--out_prin_type_cd := '02';
				WHEN '3' THEN
					out_prin_type_cd := '12'; -- 2006/03/30
					--out_prin_type_cd := '02';
				WHEN '9' THEN
					out_prin_type_cd := '12'; -- 2006/03/30
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
/*2007/02/16 修正 木原 */
EXECUTE IMMEDIATE 'ANALYZE TABLE tb_seikyu_sum compute statistics';
/*end of 2007/02/16 修正 木原 */

	/* 顧客契約情報の取得 */
	<<label_seikyu_sum>>
	FOR rec_seikyu_sum IN cur_seikyu_sum( cKakinNengetu, in_gun ) LOOP

		IF  in_gun  =  CST_GUN15  THEN
			cPrintTypeCd  :=  GetPrintTypeCd(rec_seikyu_sum.B_yubill_flg15, rec_seikyu_sum.B_shiharai_kbn_cd);
		ELSE
			cPrintTypeCd  :=  GetPrintTypeCd(rec_seikyu_sum.B_yubill_flg25, rec_seikyu_sum.B_shiharai_kbn_cd);
		END IF;
--
		SELECT	DECODE(in_gun, CST_GUN15, cvs_itaku_cd15,
							  CST_GUN25, cvs_itaku_cd25)
		INTO	cItakuCd
		FROM	tb_oem_joho      A     -- OEM基本情報
		WHERE	A.oem_cd         =  rec_seikyu_sum.C_oem_cd;

--		請求額がバーコード最大金額以上の場合は０円でバーコードを作成する
/* 2007/03/14 修正 木原　定数→DB取得値に切替 */
		--IF NVL(rec_seikyu_sum.C_seikyugaku,0) < aoba_rate_const.CST_BAR_MAX_KINGAKU THEN
		IF NVL(rec_seikyu_sum.C_seikyugaku,0) < aoba_rate_common.get_seikyugaku_max THEN
/* end of 2007/03/14 修正 木原　定数→DB取得値に切替 */
			nSeikyugaku := NVL(rec_seikyu_sum.C_seikyugaku,0);
		else
			nSeikyugaku := 0;
		END IF;

		/* バーコード取得(EAN) */
		nRet  :=  FUNC_BARCODE_EAN(
				rec_seikyu_sum.B_kokyaku_cd,
				rec_seikyu_sum.C_seikyu_nengetu,
				nSeikyugaku,
				rec_seikyu_sum.C_tax,
				cItakuCd,
				in_gun,
				vBarEAN
				);
		/* バーコード取得(JAN) */
		nRet  :=  FUNC_BARCODE_JAN(
				rec_seikyu_sum.B_kokyaku_cd,
				rec_seikyu_sum.C_seikyu_nengetu,
				nSeikyugaku,
				cItakuCd,
				vBarJAN1,
				vBarJAN2,
				vBarJAN3,
				vBarJAN4
				);

		cSaveHakkoFlg  :=  rec_seikyu_sum.B_seikyusho_hakko_flg;

		UPDATE tb_seikyu_sum
		SET
			  seikyu_saki_name      =   rec_seikyu_sum.B_seikyusaki_kaisha_name,    -- 請求先名称
			  seikyu_saki_addr1     =   rec_seikyu_sum.B_seikyusaki_addr1,          -- 請求先住所1
			  seikyu_saki_addr2     =   rec_seikyu_sum.B_seikyusaki_addr2,          -- 請求先住所2
			  seikyu_saki_addr3     =   rec_seikyu_sum.B_seikyusaki_addr3,          -- 請求先住所3
			  seikyu_saki_addr4     =   rec_seikyu_sum.B_seikyusaki_addr4,          -- 請求先住所4
			  zip                   =   rec_seikyu_sum.B_seikyusaki_zip,            -- 郵便番号
			  kokyaku_tantosha      =   rec_seikyu_sum.B_seikyusaki_busho_name,     -- 顧客担当者名
			  kokyaku_tel           =   rec_seikyu_sum.B_seikyusaki_tel,            -- 顧客代表電話番号
			  shiharai_kbn_cd1      =   rec_seikyu_sum.B_shiharai_kbn_cd,           -- 支払区分コード1(初回)
			  shiharai_kbn_cd2      =   rec_seikyu_sum.B_shiharai_kbn_cd,           -- 支払区分コード2(直近)
			  shuno_kigen           =   DECODE(rec_seikyu_sum.B_shiharai_kbn_cd, CST_SHIHARAI_KBN_KOUZA,        rec_seikyu_sum.D_hurikae_nyukin_date,
			                                                                     CST_SHIHARAI_KBN_YUBIN,        rec_seikyu_sum.D_cvs_nyukin_date,
			                                                                     CST_SHIHARAI_KBN_YUBIN_SHONIN, rec_seikyu_sum.D_cvs_nyukin_date,
			                                                                     CST_SHIHARAI_KBN_YUBIN_MITUI,  rec_seikyu_sum.D_cvs_nyukin_date,
			                                                                                                    rec_seikyu_sum.D_OTHER_NYUKIN_DATE),
			                                                                        -- 収納期限
			  bank_cd               =   rec_seikyu_sum.B_bank_cd,                   -- 金融機関コード
			  bank_name             =   rec_seikyu_sum.E_bank_name,	                -- 金融機関名称
			  shiten_cd             =   rec_seikyu_sum.B_shiten_cd,                 -- 支店コード
			  shiten_name           =   rec_seikyu_sum.E_shiten_name,	            -- 支店名称
			  koza_type_cd          =   rec_seikyu_sum.B_koza_type_cd,              -- 口座種別コード
			  koza_bng              =   rec_seikyu_sum.B_koza_bng,                  -- 口座番号
			  koza_meigi            =   rec_seikyu_sum.B_koza_meigi,                -- 口座名義
			  bar_cd_jan1            =   vBarJAN1,                                  -- バーコードJAN1
			  bar_cd_jan2            =   vBarJAN2,                                  -- バーコードJAN1
			  bar_cd_jan3            =   vBarJAN3,                                  -- バーコードJAN1
			  bar_cd_jan4            =   vBarJAN4,                                  -- バーコードJAN1
			  bar_cd_ean128          =   vBarEAN,                                   -- バーコードEAN128
			  card_kaisha_cd        =   rec_seikyu_sum.B_card_kaisha_cd,            -- カード会社コード
			  card_bng              =   rec_seikyu_sum.B_card_bng,                  -- カード番号
			  yuko_kigen            =   rec_seikyu_sum.B_yuko_kigen,                -- カード有効期限
			  print_type_cd         =   cPrintTypeCd,                               -- 請求書用紙種別コード
			  seikyusho_hakko_flg   =   DECODE(cSaveHakkoFlg, aoba_const.CST_N_FLG, CST_SEIKYUSHO_HUYO,
			                                                  rec_seikyu_sum.C_seikyusho_hakko_flg), -- 2006/03/06
			                                                                        -- 請求書発行フラグ
			  yubill_flg            =   DECODE(in_gun, CST_GUN15, rec_seikyu_sum.B_yubill_flg15,
			                                           CST_GUN25, rec_seikyu_sum.B_yubill_flg25),
			                                                                        -- 郵ビルフラグ
			  koza_bng_hyoji_flg    =   rec_seikyu_sum.B_koza_bng_hyoji_flg,        -- 口座番号表示属性フラグ
			  dohu_tirashi_cd       =   rec_seikyu_sum.B_dohu_tirashi_cd,           -- 同封ちらしコード
			  seikyusho_hakko_date  =   rec_seikyu_sum.D_seikyusho_hakko_date,      -- 請求書発行日
			  id_num                =   rec_seikyu_sum.VW1_count,                   -- ID数
			  oya_id_num            =   rec_seikyu_sum.VW2_count,                   -- 親ID数
			  ryoshusho_flg         =   rec_seikyu_sum.B_ryoshusho_flg,             -- 領収書フラグ -- 2006/06/26
			  update_date           =   dSysDate,                                   -- 更新日時
			  update_user           =   vUser                                       -- 更新ユーザ
		WHERE kokyaku_cd            =   rec_seikyu_sum.B_kokyaku_cd
		AND   kakin_nengetu         =   cKakinNengetu
		AND   seikyu_gun            =   in_gun;
--
	END LOOP label_seikyu_sum;
--
END;
/
