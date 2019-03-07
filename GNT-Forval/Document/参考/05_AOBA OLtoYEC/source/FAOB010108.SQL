CREATE OR REPLACE PROCEDURE	          FAOB010108
/********************************************************************/
/*  関数名：FAOB0101												*/
/*  機能名：請求書データ作成：備考コメントデータ抽出				*/
/*  作成者：山田													*/
/*  作成日：2005/10/31												*/
/*  更新日：2006/02/28	田中  対照データの取得方法を変更			*/
/*  更新日：2006/04/14 服部【OEM対応】                              */
/*					   備考コメントIDマスタ抽出条件の変更           */
/*  更新日：2008/09/16 齊藤											*/
/*                     備考コメント 54→60に修正                	*/
/*  更新日：2012/02/10 内海											*/
/*                     入金期日が不明な顧客(前回請求書発行を実施    */
/*					   していない顧客)のコメントを空白に変更        */
/*  更新日：2013/09/02 内海											*/
/*                     新ISPの請求書上の課金年月が前払いの場合ずれる*/
/*					   ため備考コメント上に修正を加える				*/
/*	VER   ：1.40													*/
/*------------------------------------------------------------------*/
/*	引数：		無し												*/
/*	戻値：		無し												*/
/********************************************************************/
IS
	/* 20120210  入金期日が不明な顧客のコメントを空白変換するデータを取得*/
	cursor upd_cur is
		select a.kokyaku_cd_sk, a.kakin_nengetu_sk
		  from wk_bl_biko_comment a
		 where comment_inf like '%＃ＫＥＹＷＯＲＤ４＃%';

	/* ローカル定数 */
	CST_DEL_TABLE		CONSTANT VARCHAR2(30) := 'WK_BL_BIKO_COMMENT';				--削除対象テーブル名
	/* 2005/12/15 領収書除外条件に使用 */
	CST_RYOSHUSHO_FLG	CONSTANT wk_seikyu_hakko_kokyaku.ryoshusho_flg%TYPE := 'N';	--領収書ﾌﾗｸﾞ

	/* ローカル変数 */
	nRet					NUMBER;													-- リターンコード
	dSysDate				wk_bl_biko_comment.insert_date%TYPE;					-- システム日付
	vUser					wk_bl_biko_comment.insert_user%TYPE;					-- ユーザ


	-- 2013/09/03 新ISP　前払いのデータを取得
	CURSOR iSmart_CUR IS
		SELECT A.KOKYAKU_CD_SK, A.KAKIN_NENGETU_SK, B.BIKO_COMMENT_ID
		  FROM WK_BL_BIKO_COMMENT      A,
			   WK_SEIKYU_HAKKO_KOKYAKU B
		 WHERE A.KOKYAKU_CD_SK    = B.KOKYAKU_CD
		   AND A.KAKIN_NENGETU_SK = B.KAKIN_NENGETU
		   AND EXISTS(SELECT 1
						FROM TB_SEIKYU_UTIWAKE C
					   WHERE C.KOKYAKU_CD    = A.KOKYAKU_CD_SK
						 AND C.KAKIN_NENGETU = A.KAKIN_NENGETU_SK
						 AND C.OEM_CD        = '9010'
						 AND C.REC_TYPE_CD   = '10'
						 AND C.RYOKIN_CD2    = '50'
						 AND to_date(C.RIYO_START_DATE,'YYYYMMDD') = ADD_MONTHS(to_date(A.KAKIN_NENGETU_SK,'YYYYMM'), 1)
					 );
	nComment1				TB_BIKO_COMMENT_HENKAN_MST.COMMENT1%TYPE;
	nComment2				TB_BIKO_COMMENT_HENKAN_MST.COMMENT2%TYPE;
	nComment3				TB_BIKO_COMMENT_HENKAN_MST.COMMENT3%TYPE;
	nComment4				TB_BIKO_COMMENT_HENKAN_MST.COMMENT4%TYPE;
	nComment5				TB_BIKO_COMMENT_HENKAN_MST.COMMENT5%TYPE;
BEGIN

--	DBMS_OUTPUT.PUT_LINE('処理開始01：' || TO_CHAR(SYSDATE, 'YYYY/MM/DD HH24:MI:SS'));

	/* 請求書備考情報一時TBLの削除 */
	EXECUTE IMMEDIATE 'TRUNCATE TABLE ' || CST_DEL_TABLE;

	/* システム日時の取得 */
	nRet := aoba_common.get_date( dSysDate );

	/* ユーザの取得 */
	nRet := aoba_common.get_user( vUser );

	/* 請求書備考情報一時TBL挿入 */
	INSERT INTO	/*+ APPEND */wk_bl_biko_comment(
			kokyaku_cd_sk,														-- 顧客コードソートキー
			kakin_nengetu_sk,													-- 課金年月ソートキー
			comment_inf,														-- コメント情報
			biko_comment1,														-- 備考コメント１
			biko_comment2,														-- 備考コメント２
			biko_comment3,														-- 備考コメント３
			biko_comment4,														-- 備考コメント４
			biko_comment5,														-- 備考コメント５
			insert_date,														-- 登録日時
			insert_user)														-- 登録ユーザ
	SELECT	DISTINCT a.kokyaku_cd,
			a.seikyu_nengetu,
			RPAD(
				a.kokyaku_cd		||
				a.seikyu_nengetu	||
				RPAD(NVL(aoba_out_common.conv_out_mbc(
						aoba_out_common.conv_keyword(
						a.comment1, a.seikyu_nengetu, a.seikyu_gun, a.kokyaku_cd))	||
				aoba_out_common.conv_out_mbc(
						aoba_out_common.conv_keyword(
						a.comment2, a.seikyu_nengetu, a.seikyu_gun, a.kokyaku_cd))	||
				aoba_out_common.conv_out_mbc(
						aoba_out_common.conv_keyword(
						a.comment3, a.seikyu_nengetu, a.seikyu_gun, a.kokyaku_cd))	||
				aoba_out_common.conv_out_mbc(
						aoba_out_common.conv_keyword(
						a.comment4, a.seikyu_nengetu, a.seikyu_gun, a.kokyaku_cd))	||
				aoba_out_common.conv_out_mbc(
						aoba_out_common.conv_keyword(
						a.comment5, a.seikyu_nengetu, a.seikyu_gun, a.kokyaku_cd)),'　'), 270, '　')
				, 300, ' ') AS comment_inf,

			/* 20080916 start */
			SUBSTRB(aoba_out_common.conv_out_mbc(
				aoba_out_common.conv_keyword(
				a.comment1, a.seikyu_nengetu, a.seikyu_gun, a.kokyaku_cd)), 1, 60),
			SUBSTRB(aoba_out_common.conv_out_mbc(
				aoba_out_common.conv_keyword(
				a.comment2, a.seikyu_nengetu, a.seikyu_gun, a.kokyaku_cd)), 1, 60),
			SUBSTRB(aoba_out_common.conv_out_mbc(
				aoba_out_common.conv_keyword(
				a.comment3, a.seikyu_nengetu, a.seikyu_gun, a.kokyaku_cd)), 1, 60),
			SUBSTRB(aoba_out_common.conv_out_mbc(
				aoba_out_common.conv_keyword(
				a.comment4, a.seikyu_nengetu, a.seikyu_gun, a.kokyaku_cd)), 1, 60),
			SUBSTRB(aoba_out_common.conv_out_mbc(
				aoba_out_common.conv_keyword(
				a.comment5, a.seikyu_nengetu, a.seikyu_gun, a.kokyaku_cd)), 1, 60),
			dSysDate,
			vUser
	FROM	(
			SELECT	a.kokyaku_cd AS kokyaku_cd,
					a.seikyu_nengetu AS seikyu_nengetu,
					a.seikyu_gun AS seikyu_gun,
					b.comment1 AS comment1,
					b.comment2 AS comment2,
					b.comment3 AS comment3,
					b.comment4 AS comment4,
					b.comment5 AS comment5
			FROM	(
					SELECT	a1.kokyaku_cd,
							a1.seikyu_nengetu,
							b1.oem_cd,
							b1.seikyu_gun,
							b1.biko_comment_id
					FROM	wk_bl_kokyaku_info a1,
							(
							SELECT	a11.kokyaku_cd,
									a11.seikyu_nengetu,
									a11.biko_comment_id,
									a11.seikyu_gun,
									a11.oem_cd
							FROM	wk_seikyu_hakko_kokyaku a11
							GROUP BY
									kokyaku_cd,
									seikyu_nengetu,
									biko_comment_id,
									seikyu_gun,
									oem_cd
							) b1
					WHERE a1.kokyaku_cd		=	b1.kokyaku_cd
					AND   a1.seikyu_nengetu	=	b1.seikyu_nengetu
					) a,
					tb_biko_comment_mst b
			WHERE	a.biko_comment_id = b.biko_comment_id (+)
			AND		a.oem_cd = b.oem_cd (+)
			) a;
	commit;

	/* 20120210  入金期日が不明な顧客のコメントデータを空白変換する*/
	for upd_rec in upd_cur loop
		-- 対象データを空白で更新
		update wk_bl_biko_comment a
		   set a.comment_inf      = RPAD(a.kokyaku_cd_sk || a.kakin_nengetu_sk, 300, ' '),
			   a.biko_comment1    = null,
			   a.biko_comment2    = null,
			   a.biko_comment3    = null,
			   a.biko_comment4    = null,
			   a.biko_comment5    = null
		 where a.kokyaku_cd_sk    = upd_rec.kokyaku_cd_sk
		   and a.kakin_nengetu_sk = upd_rec.kakin_nengetu_sk;
	end loop;
	/* 20120210  入金期日が不明な顧客のコメントデータを空白変換する*/

	commit;
--	DBMS_OUTPUT.PUT_LINE('処理開始02：' || TO_CHAR(SYSDATE, 'YYYY/MM/DD HH24:MI:SS'));
	FOR iSmart_rec IN iSmart_CUR LOOP
		-- 変換対象データ取得
		BEGIN
			SELECT A.COMMENT1,  A.COMMENT2,  A.COMMENT3,  A.COMMENT4,  A.COMMENT5
			  INTO nComment1,   nComment2,   nComment3,   nComment4,   nComment5
			  FROM TB_BIKO_COMMENT_HENKAN_MST A
			 WHERE A.OEM_CD          = '9010'
			   AND A.BIKO_COMMENT_ID = iSmart_rec.BIKO_COMMENT_ID;
		EXCEPTION
			WHEN NO_DATA_FOUND THEN
				nComment1 := NULL;
				nComment2 := NULL;
				nComment3 := NULL;
				nComment4 := NULL;
				nComment5 := NULL;
		END;

		-- 変換データがある場合は対象データを更新
		IF nComment1 IS NOT NULL THEN
			-- 更新
			update wk_bl_biko_comment a
			   set a.comment_inf      = RPAD(
												iSmart_rec.kokyaku_cd_sk		||
												iSmart_rec.kakin_nengetu_sk		||
												RPAD(NVL(aoba_out_common.conv_out_mbc(
														aoba_out_common.conv_keyword(
														nComment1, iSmart_rec.kakin_nengetu_sk, '15', iSmart_rec.kokyaku_cd_sk))	||
												aoba_out_common.conv_out_mbc(
														aoba_out_common.conv_keyword(
														nComment2, iSmart_rec.kakin_nengetu_sk, '15', iSmart_rec.kokyaku_cd_sk))	||
												aoba_out_common.conv_out_mbc(
														aoba_out_common.conv_keyword(
														nComment3, iSmart_rec.kakin_nengetu_sk, '15', iSmart_rec.kokyaku_cd_sk))	||
												aoba_out_common.conv_out_mbc(
														aoba_out_common.conv_keyword(
														nComment4, iSmart_rec.kakin_nengetu_sk, '15', iSmart_rec.kokyaku_cd_sk))	||
												aoba_out_common.conv_out_mbc(
														aoba_out_common.conv_keyword(
														nComment5, iSmart_rec.kakin_nengetu_sk, '15', iSmart_rec.kokyaku_cd_sk)),'　'), 270, '　')
												, 300, ' '
											),
				   a.biko_comment1    = SUBSTRB(aoba_out_common.conv_out_mbc(
												aoba_out_common.conv_keyword(
												nComment1, iSmart_rec.kakin_nengetu_sk, '15', iSmart_rec.kokyaku_cd_sk)), 1, 60),
				   a.biko_comment2    = SUBSTRB(aoba_out_common.conv_out_mbc(
												aoba_out_common.conv_keyword(
												nComment2, iSmart_rec.kakin_nengetu_sk, '15', iSmart_rec.kokyaku_cd_sk)), 1, 60),
				   a.biko_comment3    = SUBSTRB(aoba_out_common.conv_out_mbc(
												aoba_out_common.conv_keyword(
												nComment3, iSmart_rec.kakin_nengetu_sk, '15', iSmart_rec.kokyaku_cd_sk)), 1, 60),
				   a.biko_comment4    = SUBSTRB(aoba_out_common.conv_out_mbc(
												aoba_out_common.conv_keyword(
												nComment4, iSmart_rec.kakin_nengetu_sk, '15', iSmart_rec.kokyaku_cd_sk)), 1, 60),
				   a.biko_comment5    = SUBSTRB(aoba_out_common.conv_out_mbc(
												aoba_out_common.conv_keyword(
												nComment5, iSmart_rec.kakin_nengetu_sk, '15', iSmart_rec.kokyaku_cd_sk)), 1, 60)
			 where a.kokyaku_cd_sk    = iSmart_rec.kokyaku_cd_sk
			   and a.kakin_nengetu_sk = iSmart_rec.kakin_nengetu_sk;
		END IF;
	END LOOP;
--	DBMS_OUTPUT.PUT_LINE('処理終了03：' || TO_CHAR(SYSDATE, 'YYYY/MM/DD HH24:MI:SS'));
END FAOB010108;
/
