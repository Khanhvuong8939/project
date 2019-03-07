CREATE OR REPLACE PROCEDURE           FAOB010115
/********************************************************************/
/*  関数名：FAOB010115												*/
/*  機能名：請求書データ作成：擬似OEMｺｰﾄﾞ変換処理					*/
/*  作成者：木原													*/
/*  作成日：2006/7/7												*/
/*  更新日：2006/8/7 木原 群特定ﾛｼﾞｯｸを追加							*/
/*                   木原 顧客ﾏｽﾀ結合条件を請求年月に修正           */
/*	VER   ：1.04													*/
/*------------------------------------------------------------------*/
/*	引数：		無し												*/
/*	戻値：		無し												*/
/********************************************************************/
IS
	/* ローカル定数 */

	/* ローカル変数 */
	nRet					NUMBER;							--リターンコード
	dSysDate				wk_bl_kokyaku.insert_date%TYPE;	--システム日付
	vUser					wk_bl_kokyaku.insert_user%TYPE;	--ユーザ
	cGun  					CHAR(2);
	nOemcd 					CHAR(4);

/*
	CURSOR cur_seikyu_sum --カーソル定義(請求書顧客ﾏｽﾀ一時TBL)
 	IS
		SELECT	A.OEM_CD_SK oem_cd,
				SUBSTRB(A.seikyu_data,1,18) AS data_a,
		 		SUBSTRB(A.seikyu_data,23) AS data_b,
				B.giji_oem_cd AS giji_oem
		FROM	wk_bl_kokyaku A,
				( SELECT * FROM tb_seikyu_sum WHERE giji_oem_cd is not NULL ) B
		WHERE
				A.KOKYAKU_CD_SK = B.kokyaku_cd
				AND
				A.KAKIN_NENGETU_SK = B.kakin_nengetu
		FOR UPDATE;
	rec_seikyu_sum		cur_seikyu_sum%ROWTYPE;
	CURSOR cur_kaisen_meisai --カーソル定義(請求書回線明細一時TBL)
	IS
		SELECT	SUBSTRB(A.KAISEN_MEISAI_DATA,1,24) AS data_a,
		 		SUBSTRB(A.KAISEN_MEISAI_DATA,29) AS data_b,
				B.giji_oem_cd AS giji_oem
		FROM	wk_bl_kaisen_meisai A,
				( SELECT * FROM tb_seikyu_sum WHERE giji_oem_cd is not NULL	) B
		WHERE
				A.KOKYAKU_CD_SK = B.kokyaku_cd
				AND
				A.KAKIN_NENGETU_SK = B.kakin_nengetu
		FOR UPDATE;
	rec_kaisen_meisai		cur_kaisen_meisai%ROWTYPE;
	CURSOR cur_tuwa_meisai --カーソル定義(請求書通話明細一時TBL)
	IS
		SELECT	SUBSTRB(A.KAISEN_MEISAI_DATA,1,24) AS data_a,
		 		SUBSTRB(A.KAISEN_MEISAI_DATA,29) AS data_b,
				B.giji_oem_cd AS giji_oem
		FROM	wk_bl_tuwa_meisai A,
				( SELECT * FROM tb_seikyu_sum WHERE giji_oem_cd is not NULL	) B
		WHERE
				A.KOKYAKU_CD_SK = B.kokyaku_cd
				AND
				A.KAKIN_NENGETU_SK = B.kakin_nengetu
		FOR UPDATE;
	rec_tuwa_meisai		cur_tuwa_meisai%ROWTYPE;
	CURSOR cur_shukka_meisai  --カーソル定義(請求書出荷明細一時TBL)
	IS
		SELECT	SUBSTRB(A.KAISEN_MEISAI_DATA,1,24) AS data_a,
		 		SUBSTRB(A.KAISEN_MEISAI_DATA,29) AS data_b,
				B.giji_oem_cd AS giji_oem
		FROM	wk_bl_shukka_meisai A,
				( SELECT * FROM tb_seikyu_sum WHERE giji_oem_cd is not NULL	) B
		WHERE
				A.KOKYAKU_CD_SK = B.kokyaku_cd
				AND
				A.KAKIN_NENGETU_SK = B.kakin_nengetu
		FOR UPDATE;
	rec_shukka_meisai		cur_shukka_meisai%ROWTYPE;
*/
BEGIN
	/* システム日時の取得 */
	nRet := aoba_common.get_date( dSysDate );
	/* ユーザの取得 */
	nRet := aoba_common.get_user( vUser );

/* 2006/08/07 追加 群特定 */
	/* 処理対象群, 処理対象年月取得 */
	SELECT	shori_taisho_gun
	INTO	cGun
	FROM	TB_BATCH_CTRL
	WHERE	JOBNET_ID = aoba_out_const.CST_OUT_FAOB010200;
/* end of 2006/08/07 追加 群特定 */

 	/* 請求書顧客ﾏｽﾀ一時TBLの擬似OEMｺｰﾄﾞ変換 */
	UPDATE wk_bl_kokyaku A Set (A.seikyu_data,A.oem_cd_sk)
    =(
      select SUBSTRB(A.seikyu_data,1,18)||TRIM(TO_CHAR(b.giji_oem_cd,'0009'))||SUBSTRB(A.seikyu_data,23),
			 b.giji_oem_cd
/* 2006/08/07 追加 群特定 */
--      FROM ( SELECT * FROM tb_seikyu_sum WHERE giji_oem_cd is not NULL ) b
      FROM (
			SELECT distinct kokyaku_cd,seikyu_nengetu,giji_oem_cd FROM tb_seikyu_sum
			WHERE giji_oem_cd is not NULL
				　AND
				  seikyu_gun = cGun
		    ) b
      WHERE
		  A.kokyaku_cd_sk = b.kokyaku_cd
		  AND
/* 2006/08/07 修正 請求年月 に修正 */
--		  A.kakin_nengetu_sk = b.kakin_nengetu
		　A.kakin_nengetu_sk = b.seikyu_nengetu
     )
    WHERE
	  EXISTS
	  (
      select 1
/* 2006/08/07 追加 群特定 */
--      FROM ( SELECT * FROM tb_seikyu_sum WHERE giji_oem_cd is not NULL ) b
      FROM (
			SELECT * FROM tb_seikyu_sum
			WHERE giji_oem_cd is not NULL
				　AND
				  seikyu_gun = cGun
		    ) b
      WHERE
		  A.kokyaku_cd_sk = b.kokyaku_cd
		  AND
/* 2006/08/07 修正 請求年月 に修正 */
--		  A.kakin_nengetu_sk = b.kakin_nengetu
		　A.kakin_nengetu_sk = b.seikyu_nengetu
     );

 	/* 請求書回線明細一時TBLの擬似OEMｺｰﾄﾞ変換 */
    UPDATE wk_bl_kaisen_meisai A Set A.kaisen_meisai_data
	=(
	    SELECT	SUBSTRB(A.KAISEN_MEISAI_DATA,1,24)||TRIM(TO_CHAR(b.giji_oem_cd,'0009'))||SUBSTRB(A.KAISEN_MEISAI_DATA,29)
/* 2006/08/07 追加 群特定 */
--      FROM ( SELECT * FROM tb_seikyu_sum WHERE giji_oem_cd is not NULL ) b
      FROM (
			SELECT * FROM tb_seikyu_sum
			WHERE giji_oem_cd is not NULL
				　AND
				  seikyu_gun = cGun
		    ) b
		WHERE
				A.KOKYAKU_CD_SK = B.kokyaku_cd
				AND
				A.KAKIN_NENGETU_SK = B.kakin_nengetu
    )
	WHERE
	  EXISTS
	  (
      select 1
/* 2006/08/07 追加 群特定 */
--      FROM ( SELECT * FROM tb_seikyu_sum WHERE giji_oem_cd is not NULL ) b
      FROM (
			SELECT * FROM tb_seikyu_sum
			WHERE giji_oem_cd is not NULL
				　AND
				  seikyu_gun = cGun
		    ) b
      WHERE
		  A.kokyaku_cd_sk = b.kokyaku_cd
		  AND
		  A.kakin_nengetu_sk = b.kakin_nengetu
      );

 	/* 請求書通話明細一時TBLの擬似OEMｺｰﾄﾞ変換 */
    UPDATE wk_bl_tuwa_meisai A Set A.kaisen_meisai_data
	=(
	    SELECT	SUBSTRB(A.KAISEN_MEISAI_DATA,1,24)||TRIM(TO_CHAR(b.giji_oem_cd,'0009'))||SUBSTRB(A.KAISEN_MEISAI_DATA,29)
/* 2006/08/07 追加 群特定 */
--      FROM ( SELECT * FROM tb_seikyu_sum WHERE giji_oem_cd is not NULL ) b
      FROM (
			SELECT * FROM tb_seikyu_sum
			WHERE giji_oem_cd is not NULL
				　AND
				  seikyu_gun = cGun
		    ) b
		WHERE
				A.KOKYAKU_CD_SK = B.kokyaku_cd
				AND
				A.KAKIN_NENGETU_SK = B.kakin_nengetu
    )
	WHERE
	  EXISTS
	  (
      select 1
/* 2006/08/07 追加 群特定 */
--      FROM ( SELECT * FROM tb_seikyu_sum WHERE giji_oem_cd is not NULL ) b
      FROM (
			SELECT * FROM tb_seikyu_sum
			WHERE giji_oem_cd is not NULL
				　AND
				  seikyu_gun = cGun
		    ) b
      WHERE
		  A.kokyaku_cd_sk = b.kokyaku_cd
		  AND
		  A.kakin_nengetu_sk = b.kakin_nengetu
      );

 	/* 請求書出荷明細一時TBLの擬似OEMｺｰﾄﾞ変換 */
	UPDATE wk_bl_shukka_meisai A Set A.kaisen_meisai_data
	=(
	    SELECT	SUBSTRB(A.KAISEN_MEISAI_DATA,1,24)||TRIM(TO_CHAR(b.giji_oem_cd,'0009'))||SUBSTRB(A.KAISEN_MEISAI_DATA,29)
/* 2006/08/07 追加 群特定 */
--      FROM ( SELECT * FROM tb_seikyu_sum WHERE giji_oem_cd is not NULL ) b
      FROM (
			SELECT * FROM tb_seikyu_sum
			WHERE giji_oem_cd is not NULL
				　AND
				  seikyu_gun = cGun
		    ) b
		WHERE
				A.KOKYAKU_CD_SK = B.kokyaku_cd
				AND
				A.KAKIN_NENGETU_SK = B.kakin_nengetu
    )
	WHERE
	  EXISTS
	  (
      select 1
/* 2006/08/07 追加 群特定 */
--      FROM ( SELECT * FROM tb_seikyu_sum WHERE giji_oem_cd is not NULL ) b
      FROM (
			SELECT * FROM tb_seikyu_sum
			WHERE giji_oem_cd is not NULL
				　AND
				  seikyu_gun = cGun
		    ) b
      WHERE
		  A.kokyaku_cd_sk = b.kokyaku_cd
		  AND
		  A.kakin_nengetu_sk = b.kakin_nengetu
      );

END FAOB010115;
/
