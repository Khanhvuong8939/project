CREATE OR REPLACE PROCEDURE           FAOB010116
/********************************************************************/
/*  関数名：FAOB0116												*/
/*  機能名：請求書データ作成：請求書請求金額内訳編集				*/
/*  作成者：伊東　佐江子											*/
/*  作成日：2007/05/17												*/
/*  更新日：														*/
/*  VER   ：1.07													*/
/*------------------------------------------------------------------*/
/*	引数：		無し												*/
/*	戻値：		無し												*/
/********************************************************************/
IS

	/* 用紙タイプ */
	CST_PRINT_TYPE_11		CONSTANT CHAR(2) := '11';	--圧着ハガキ（口座振替＋領収書なし）

	/* 削除対象テーブル名 */
	CST_DEL_TABLE_1			CONSTANT VARCHAR2(30) := 'WK_BL_SEIKYU_SUM_SRV';
	CST_DEL_TABLE_2			CONSTANT VARCHAR2(30) := 'WK_BL_SEIKYU_SUM_SRV_INFO';
	
	/* その他フラグ */
	CST_SONOTA_FLG_1		CONSTANT CHAR(1) := '1';	-- サービス名=その他として集計する項目
	CST_SONOTA_FLG_0		CONSTANT CHAR(1) := '0';

	/* ローカル変数 */
	cSONOTA_FLG 			CHAR(1)	:= '0';
	cSONOTA_FLG_SK			CHAR(1)	:= '0';
	cKOKYAKU_CD 			CHAR(8)	:= NULL;
	cSEIKYU_NENGETU			CHAR(6)	:= NULL;

	nSeqno					NUMBER;								--シーケンスカウンタ
	-- 共通項目
	nRet					NUMBER;								--リターンコード
	dSysDate				WK_BL_SEIKYU_SUM_SRV_INFO.insert_date%TYPE;		--処理日付
	vUser					WK_BL_SEIKYU_SUM_SRV_INFO.insert_user%TYPE;		--処理ユーザ
	cKakin_nengetu			CHAR(6)	;	-- 課金年月
	cSeikyu_gun				CHAR(2)	;	-- 請求群

	/* カーソル定義 */
	CURSOR cur_srv_seikyu_sum
	IS
		SELECT
			b.KOKYAKU_CD AS KOKYAKU_CD,
			b.SEIKYU_NENGETU AS SEIKYU_NENGETU,
			c.SRV_TYPE_CD AS SRV_TYPE_CD,
			d.OEM_SRV_NAME AS OEM_SRV_NAME,
			sum(c.SEIKYUGAKU) AS SEIKYUGAKU,
			sum(c.TAX) AS TAX
		FROM
			WK_BL_KOKYAKU a,
			TB_SEIKYU_SUM  b,
			TB_SEIKYU_SUM_SRV c,
			TB_OEMSRV_NM_MST d,
			tb_batch_ctrl e
		WHERE
			a.KOKYAKU_CD_SK = b.KOKYAKU_CD 
			and  a.PRINT_TYPE_CD_SK = CST_PRINT_TYPE_11  -- 用紙タイプ【11】で抽出
			and  a.KAKIN_NENGETU_SK = b.SEIKYU_NENGETU
			and  a.OEM_CD_SK = b.OEM_CD
			AND	 b.seikyu_gun = e.shori_taisho_gun       -- 請求群
			and  b.KOKYAKU_CD = c.KOKYAKU_CD
			and  b.KAKIN_NENGETU = c.KAKIN_NENGETU
			and  b.SEIKYU_GUN = c.SEIKYU_GUN
			and  b.OEM_CD = c.OEM_CD
			and  c.SRV_TYPE_CD = d.SRV_TYPE_CD
			and  c.OEM_CD = d.OEM_CD
			and	 e.jobnet_id = aoba_out_const.CST_OUT_FAOB010100
		GROUP BY 
			b.KOKYAKU_CD,
			b.SEIKYU_NENGETU,
			c.SRV_TYPE_CD,
			d.OEM_SRV_NAME
		ORDER BY
			b.KOKYAKU_CD,
			b.SEIKYU_NENGETU,
			sum(c.SEIKYUGAKU) desc ,
			c.SRV_TYPE_CD;
	
	rec_srv_seikyu_sum		cur_srv_seikyu_sum%ROWTYPE;

BEGIN

	/* 請求書サービス別請求情報一時TBLの削除 */
	EXECUTE IMMEDIATE 'TRUNCATE TABLE ' || CST_DEL_TABLE_1;

	/* 請求書サービス別請求情報一時格納TBLの削除 */
	EXECUTE IMMEDIATE 'TRUNCATE TABLE ' || CST_DEL_TABLE_2;

	/* システム日時の取得 */
	nRet := aoba_common.get_date( dSysDate );

	/* ユーザの取得 */
	nRet := aoba_common.get_user( vUser );

	/* ワーク項目の初期化 */
 	nSeqno := 0;

 	FOR rec_srv_seikyu_sum IN cur_srv_seikyu_sum LOOP

		/* ブレイクキーの判定 */
		IF	rec_srv_seikyu_sum.KOKYAKU_CD = cKOKYAKU_CD AND
			rec_srv_seikyu_sum.SEIKYU_NENGETU = cSEIKYU_NENGETU THEN

	 		/* カウンタの加算 */
 			nSeqno := nSeqno + 1;

			/* カウンタの判定 */
			IF nSeqno > 5 THEN

	 			/* その他フラグの編集 */
				cSONOTA_FLG := CST_SONOTA_FLG_1;
				cSONOTA_FLG_SK := CST_SONOTA_FLG_1;

			END IF;

		ELSE
	 		/* カウンタの初期化 */
 			nSeqno := 1;

	 		/* その他フラグの初期化 */
			cSONOTA_FLG := CST_SONOTA_FLG_0;
			cSONOTA_FLG_SK := CST_SONOTA_FLG_0;
			
			/* ブレイクキーの設定 */
			cKOKYAKU_CD := rec_srv_seikyu_sum.KOKYAKU_CD;
			cSEIKYU_NENGETU := rec_srv_seikyu_sum.SEIKYU_NENGETU;

		END IF;

		/*********************************************/
		/* 請求書サービス別請求情報一時格納TBLの作成 */
		/*********************************************/

		INSERT INTO WK_BL_SEIKYU_SUM_SRV_INFO
		(
		KOKYAKU_CD_SK,
		KAKIN_NENGETU_SK,
		SRV_TYPE_CD,
		SRV_NAME,
		SEIKYUGAKU,
		TAX,
		SONOTA_FLG,
		SONOTA_FLG_SK,
		INSERT_DATE,
		INSERT_USER
		)
		VALUES(
		rec_srv_seikyu_sum.KOKYAKU_CD,
		rec_srv_seikyu_sum.SEIKYU_NENGETU,
		rec_srv_seikyu_sum.SRV_TYPE_CD,
		rec_srv_seikyu_sum.OEM_SRV_NAME,
		rec_srv_seikyu_sum.SEIKYUGAKU,
		rec_srv_seikyu_sum.TAX,
		cSONOTA_FLG	,
		cSONOTA_FLG_SK,
		dSysDate,
		vUser
		);

 	END LOOP;

	/*****************************************/
	/* 請求書サービス別請求情報一時TBLの作成 */
	/*****************************************/
	/* その他フラグ="0"のデータ */

	INSERT INTO WK_BL_SEIKYU_SUM_SRV(
			KOKYAKU_CD_SK,
			KAKIN_NENGETU_SK,
			SRV_TYPE_CD,
			SRV_NAME,
			SEIKYUGAKU_SIGN,
			SEIKYUGAKU,
			TAX_SIGN,
			TAX,
			SONOTA_FLG_SK,
			INSERT_DATE,
			INSERT_USER
			)
	SELECT	KOKYAKU_CD_SK,
			KAKIN_NENGETU_SK,
			SRV_TYPE_CD,
			SRV_NAME,
			aoba_out_common.get_mark(	aoba_out_const.CST_OUT_FAOB010100,SEIKYUGAKU),	
			SEIKYUGAKU,
			aoba_out_common.get_mark(	aoba_out_const.CST_OUT_FAOB010100,TAX),	
			TAX,
			SONOTA_FLG_SK,
			dSysDate,
			vUser
	FROM	WK_BL_SEIKYU_SUM_SRV_INFO 
	WHERE	SONOTA_FLG = CST_SONOTA_FLG_0;


	/* その他フラグ="1"のデータ */

	INSERT INTO WK_BL_SEIKYU_SUM_SRV(
			KOKYAKU_CD_SK,
			KAKIN_NENGETU_SK,
			SRV_TYPE_CD,
			SRV_NAME,
			SEIKYUGAKU_SIGN,
			SEIKYUGAKU,
			TAX_SIGN,
			TAX,
			SONOTA_FLG_SK,
			INSERT_DATE,
			INSERT_USER
			)
	SELECT	KOKYAKU_CD_SK,
			KAKIN_NENGETU_SK,
			'XXXX',
			'その他',
			aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,SUM(SEIKYUGAKU)),	
			SUM(SEIKYUGAKU),
			aoba_out_common.get_mark(aoba_out_const.CST_OUT_FAOB010100,SUM(TAX)),	
			SUM(TAX),
			SONOTA_FLG_SK,
      		dSysDate,
			vUser
	FROM	WK_BL_SEIKYU_SUM_SRV_INFO 
	WHERE	SONOTA_FLG = CST_SONOTA_FLG_1
    GROUP BY KOKYAKU_CD_SK,KAKIN_NENGETU_SK,SONOTA_FLG_SK;

END FAOB010116;
/
