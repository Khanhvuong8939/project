CREATE OR REPLACE PROCEDURE        FAOB010120
/********************************************************************/
/*	関数名：FAOB010120												*/
/*	機能名：請求書ペーパレス対応 請求データ作成履歴作成				*/
/*	作成者：根来													*/
/*	作成日：2010/10/25												*/
/*	更新日：2011/06/28 サーバ移設対応 SQL文変更						*/
/*	      ：2011/10/04 不具合対応 SQL文変更						*/
/*	      ：2011/10/12 不具合対応 SQL文変更						*/
/*	VER   ：1.00													*/
/*------------------------------------------------------------------*/
/*	引数：		無し												*/
/*	戻値：		無し												*/
/********************************************************************/
IS
	/* ローカル定数 */
    CST_SEND_MAIL_FLG_9		CONSTANT VARCHAR2(1) :='9';							--発行なし
    CST_SEND_MAIL_MEI_NASHI	CONSTANT VARCHAR2(20) :='発行なし';					--発行なし
    CST_SEND_MAIL_MEI_IRAI	CONSTANT VARCHAR2(20) :='送信依頼中';				--送信依頼中
    CST_SEND_MAIL_MEI_HGK	CONSTANT VARCHAR2(20) :='葉書　標準';				--葉書　標準

   	CST_YUBILL_FLG_1		CONSTANT VARCHAR2(1) :='1';	
   	CST_YUBILL_FLG_2		CONSTANT VARCHAR2(1) :='2';	
   	CST_YUBILL_FLG_3		CONSTANT VARCHAR2(1) :='3';	
   	CST_YUBILL_FLG_4		CONSTANT VARCHAR2(1) :='4';	
   	CST_YUBILL_FLG_5		CONSTANT VARCHAR2(1) :='5';	
   	CST_YUBILL_FLG_6		CONSTANT VARCHAR2(1) :='6';	
   	CST_YUBILL_FLG_7		CONSTANT VARCHAR2(1) :='7';	
   	CST_YUBILL_FLG_8		CONSTANT VARCHAR2(1) :='8';	

   	CST_YUBILL_MEI_1		CONSTANT VARCHAR2(20) :='封筒２００';	
   	CST_YUBILL_MEI_2		CONSTANT VARCHAR2(20) :='封筒５５０';	
   	CST_YUBILL_MEI_3		CONSTANT VARCHAR2(20) :='封筒２００';	
   	CST_YUBILL_MEI_4		CONSTANT VARCHAR2(20) :='封筒３５０';	
   	CST_YUBILL_MEI_5		CONSTANT VARCHAR2(20) :='封筒０';	
   	CST_YUBILL_MEI_6		CONSTANT VARCHAR2(20) :='封筒０';	
   	CST_YUBILL_MEI_7		CONSTANT VARCHAR2(20) :='葉書１００';	
   	CST_YUBILL_MEI_8		CONSTANT VARCHAR2(20) :='葉書０';	

   	CST_Y_FLG				CONSTANT VARCHAR2(1) :='Y';	
   	CST_N_FLG				CONSTANT VARCHAR2(1) :='N';	

   	CST_15_FLG				CONSTANT VARCHAR2(2) :='15';	
   	CST_25_FLG				CONSTANT VARCHAR2(2) :='25';	
	
	/* ローカル変数 */
	nRet					NUMBER; 										--リターンコード
	dSysDate				TB_SEIKYU_DATA_HIST.insert_date%TYPE;			--システム日付
	vUser					TB_SEIKYU_DATA_HIST.insert_user%TYPE;			--ユーザ

	vSeikyu_nengetu			TB_BATCH_CTRL.SHORI_TAISHO_NENGETU%type;		--処理対象課金年月
	vSeikyu_gun				TB_BATCH_CTRL.SHORI_TAISHO_GUN%type;			--処理対象請求群

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
	/* ﾘﾗﾝ対応処理				                */
	/********************************************/
	DELETE
	FROM	TB_SEIKYU_DATA_HIST
	WHERE	KAKIN_NENGETU	=	vSeikyu_nengetu
	AND		SEIKYU_GUN		=	vSeikyu_gun;


	/********************************************/
	/* 請求データ作成履歴登録					*/
	/********************************************/
	
	insert into TB_SEIKYU_DATA_HIST
	(
	    KOKYAKU_CD,
	    KAKIN_NENGETU,
	    SEIKYU_GUN,
	    OEM_CD,
	    PRINT_TYPE_CD,
	    SHOKAI_SEIKYUSHO_HAKKO_FLG,
	    SHOKAI_SEIKYUSHO_HAKKO_MEI,
	    SEND_MAIL1_FLG,
	    SEND_MAIL1_FLG_MEI,
	    SEND_MAIL2_FLG,
	    SEND_MAIL2_FLG_MEI,
	    INSERT_DATE,
	    INSERT_USER
	)
	select	a.kokyaku_cd_sk,
			a.kakin_nengetu_sk,
			vSeikyu_gun,
			a.oem_cd_sk,
			a.print_type_cd_sk,
			CASE WHEN vSeikyu_gun = CST_15_FLG THEN
				b.yubill_flg15
			ELSE
				b.yubill_flg25
			END AS yubill_flg,
			CST_SEND_MAIL_MEI_NASHI ryokin_name,
-- 2011/06/28 start
--			CASE WHEN mail_add1 is not null THEN
--				CST_SEND_MAIL_FLG_9
--			ELSE
--				null
--			END AS send_mail_flg1,
-- 2011/10/12 start
--			NVL2 (mail_add1,CST_SEND_MAIL_FLG_9,null) send_mail_flg1,
			NVL2 (d.oem_cd, NVL2 (mail_add1,CST_SEND_MAIL_FLG_9,null),null) send_mail_flg1,
-- 2011/10/12 end
--			CASE WHEN mail_add1 is not null THEN
--				CST_SEND_MAIL_MEI_IRAI
--			ELSE
--				''
--			END AS send_mail_mei1,
-- 2011/10/12 start
--			NVL2 (mail_add1,CST_SEND_MAIL_MEI_IRAI,'') send_mail_mei1,
			NVL2 (d.oem_cd,NVL2 (mail_add1,CST_SEND_MAIL_MEI_IRAI,''),'') send_mail_mei1,
-- 2011/10/12 end
--			CASE WHEN mail_add2 is not null THEN
--				CST_SEND_MAIL_FLG_9
--			ELSE
--				null
--			END AS send_mail_flg2,
-- 2011/10/12 start
--			NVL2 (mail_add2,CST_SEND_MAIL_FLG_9,null) send_mail_flg2,
			NVL2 (d.oem_cd,NVL2 (mail_add2,CST_SEND_MAIL_FLG_9,null),null) send_mail_flg2,
-- 2011/10/12 end
--			CASE WHEN mail_add2 is not null THEN
--				CST_SEND_MAIL_MEI_IRAI
--			ELSE
--				null
--			END AS send_mail_mei2,
-- 2011/10/12 start
--			NVL2 (mail_add2,CST_SEND_MAIL_MEI_IRAI,'') send_mail_mei2,
			NVL2 (d.oem_cd,NVL2 (mail_add2,CST_SEND_MAIL_MEI_IRAI,''),'') send_mail_mei2,
-- 2011/10/12 end
-- 2011/06/28 end
			dSysDate insert_date,
			vUser insert_user
	from	wk_bl_kokyaku a,
	  		tb_kokyaku_joho b,
			(select	a.kokyaku_cd,
			       	a.mail_add1 mail_add1,
			       	b.mail_add2 mail_add2
			from	(select	aa.kokyaku_cd,
			       			aa.EMAIL1 mail_add1
					 from	TB_KOKYAKU_EMAIL_MST@BT aa
					 where	EMAIL1 is not null
					) a,
					(select	bb.kokyaku_cd,
			       			bb.EMAIL2 mail_add2
					 from	TB_KOKYAKU_EMAIL_MST@BT bb
					 where	EMAIL2 is not null
					) b
			where a.kokyaku_cd				=	b.kokyaku_cd(+)
			union
			select	aa.kokyaku_cd,
			       			aa.EMAIL1 mail_add1,
			       			aa.EMAIL2 mail_add2
					 from	TB_KOKYAKU_EMAIL_MST@BT aa
					 where	EMAIL1 is  null
					 and EMAIL2 is not null) c,
			TB_OEM_EMAIL_MST d
	where	a.kokyaku_cd_sk					=	b.kokyaku_cd
	and		a.YEC_PNT_FLG					=	CST_N_FLG
	and		a.kakin_nengetu_sk				=	vSeikyu_nengetu
	AND		SUBSTRB(a.SEIKYU_DATA,796,1)	<>	CST_Y_FLG
	and     a.kokyaku_cd_sk					=	c.kokyaku_cd(+)
--2011/10/04 start
--	and		a.oem_cd_sk						=	d.oem_cd
	and		a.oem_cd_sk						=	d.oem_cd(+)
--2011/10/04 end 
	union all
	select	a.kokyaku_cd_sk,
			a.kakin_nengetu_sk,
			vSeikyu_gun,
			a.oem_cd_sk,
			a.print_type_cd_sk,
			CASE WHEN vSeikyu_gun = CST_15_FLG THEN
				b.yubill_flg15
			ELSE
				b.yubill_flg25
			END AS yubill_flg,
			CASE WHEN vSeikyu_gun = CST_15_FLG THEN
				decode(b.yubill_flg15,CST_YUBILL_FLG_1,CST_YUBILL_MEI_1,CST_YUBILL_FLG_2,CST_YUBILL_MEI_2,CST_YUBILL_FLG_3,CST_YUBILL_MEI_3,CST_YUBILL_FLG_4,CST_YUBILL_MEI_4,CST_YUBILL_FLG_5,CST_YUBILL_MEI_5,CST_YUBILL_FLG_6,CST_YUBILL_MEI_6,CST_YUBILL_FLG_7,CST_YUBILL_MEI_7,CST_YUBILL_FLG_8,CST_YUBILL_MEI_8)
			ELSE
				decode(b.yubill_flg25,CST_YUBILL_FLG_1,CST_YUBILL_MEI_1,CST_YUBILL_FLG_2,CST_YUBILL_MEI_2,CST_YUBILL_FLG_3,CST_YUBILL_MEI_3,CST_YUBILL_FLG_4,CST_YUBILL_MEI_4,CST_YUBILL_FLG_5,CST_YUBILL_MEI_5,CST_YUBILL_FLG_6,CST_YUBILL_MEI_6,CST_YUBILL_FLG_7,CST_YUBILL_MEI_7,CST_YUBILL_FLG_8,CST_YUBILL_MEI_8)
			END AS  ryokin_name,
			null send_mail_flg1,
			null send_mail_mei1,
			null send_mail_flg2,
			null send_mail_mei2,
			dSysDate insert_date,
			vUser insert_user
	from	wk_bl_kokyaku a,
	  		tb_kokyaku_joho b
	where	a.kokyaku_cd_sk					=	b.kokyaku_cd
	and		a.YEC_PNT_FLG					=	CST_Y_FLG
	and		a.kakin_nengetu_sk				=	vSeikyu_nengetu
	and		((vSeikyu_gun					=	CST_15_FLG
	and		  b.yubill_flg15 				is not null)
	or		 (vSeikyu_gun					=	CST_25_FLG
	and		  b.yubill_flg25				is not null))
	AND		SUBSTRB(a.SEIKYU_DATA,796,1)	<>	CST_Y_FLG
	union all
	select	a.kokyaku_cd_sk,
			a.kakin_nengetu_sk,
			vSeikyu_gun,
			a.oem_cd_sk,
			a.print_type_cd_sk,
			CASE WHEN vSeikyu_gun = CST_15_FLG THEN
				b.yubill_flg15
			ELSE
				b.yubill_flg25
			END AS yubill_flg,
			CST_SEND_MAIL_MEI_HGK  ryokin_name,
			null send_mail_flg1,
			null send_mail_mei1,
			null send_mail_flg2,
			null send_mail_mei2,
			dSysDate insert_date,
			vUser insert_user
	from	wk_bl_kokyaku a,
			tb_kokyaku_joho b
	where	a.kokyaku_cd_sk					=	b.kokyaku_cd
	and		a.YEC_PNT_FLG					=	CST_Y_FLG
	and		a.kakin_nengetu_sk				=	 vSeikyu_nengetu
	and		((vSeikyu_gun					=	CST_15_FLG
	and		  b.yubill_flg15 				is null)
	or		 (vSeikyu_gun					=	CST_25_FLG
	and		  b.yubill_flg25				is null))
	AND		SUBSTRB(a.SEIKYU_DATA,796,1)	<>	CST_Y_FLG;

END FAOB010120;
/
