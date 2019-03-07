CREATE OR REPLACE PROCEDURE    FMOB000201_5
/********************************************************************/
/*  関数名：FMOB000201_5.sql                                        */
/*  機能名：tabal契約者情報連携				                        */
/*  作成者：FT内海	                                                */
/*  作成日：2013/5/30                                               */
/*------------------------------------------------------------------*/
/*  引数：                                                          */
/*  戻値：                                                          */
/********************************************************************/
IS
	-- 連携データ取得
	cursor tabal_cur is
		select *
		  from TB_TABAL_INFO a
		 where AOBA_SEND_DATE IS NULL;

	-- 変数
	n_user			varchar2(10);
	n_date			date;
	n_cnt  			integer;
	n_kokyaku_cd	TB_TABAL_INFO.KKYK_CD%type;
BEGIN
	-- 変数格納
	n_user := 'Manbow';
	n_date := sysdate;

	for tabal_rec in tabal_cur loop
		/*****************************************
		-- Aobaへ　tabal契約者情報連携
		-- 対象データ　Aoba連携日付がNULLのデータ
		-- データが存在する場合はUPDATE、存在しない場合はINSERT
		*****************************************/
		select count(1)
		  into n_cnt
		  from AOBACAT.TB_TABAL_INFO@RF a
		 where a.oya_id    = tabal_rec.ENO
		   and a.SERIAL_NO = tabal_rec.SERIAL_NO;

		n_kokyaku_cd := manbow_common.GET_KKYK_CD(tabal_rec.ENO);
		if n_cnt > 0 then
			-- データ存在時は、更新
			UPDATE AOBACAT.TB_TABAL_INFO@RF ao
			   SET
--				ao.OYA_ID                  = tabal_rec.ENO,
				ao.KOKYAKU_CD              = n_kokyaku_cd,
				ao.SERIAL_NO               = tabal_rec.SERIAL_NO,
				ao.TABAL_ID                = tabal_rec.TABAL_ID,
				ao.MSKM_KANJI_SHAMEI       = tabal_rec.MSKM_KANJI_SHAMEI,
				ao.MSKM_KN_SHAMEI          = tabal_rec.MSKM_KN_SHAMEI,
				ao.MSKM_TEL                = tabal_rec.MSKM_TEL,
				ao.BIRTHDAY                = tabal_rec.BIRTHDAY,
				ao.SEX_FLG                 = tabal_rec.SEX_FLG,
				ao.MSKM_YUBIN_BNG          = tabal_rec.MSKM_YUBIN_BNG,
				ao.MSKM_KEN_SIKUGUN_MEI    = tabal_rec.MSKM_KEN_SIKUGUN_MEI,
				ao.MSKM_CHOSON_CHOZ        = tabal_rec.MSKM_CHOSON_CHOZ,
				ao.MSKM_TTMNMEI            = tabal_rec.MSKM_TTMNMEI,
				ao.TABAL_SINSEI            = tabal_rec.TABAL_SINSEI,
				ao.TABAL_STATUS            = tabal_rec.TABAL_STATUS,
				ao.ARAIGAE_DATE            = tabal_rec.ARAIGAE_DATE,
				ao.ARAIGAE_STATUS          = tabal_rec.ARAIGAE_STATUS,
				ao.AOBA_SEND_DATE          = n_date,
				ao.FST_TABAL_ID            = tabal_rec.FST_TABAL_ID,
				ao.FST_MSKM_KANJI_SHAMEI   = tabal_rec.FST_MSKM_KANJI_SHAMEI,
				ao.FST_MSKM_KN_SHAMEI      = tabal_rec.FST_MSKM_KN_SHAMEI,
				ao.FST_MSKM_TEL            = tabal_rec.FST_MSKM_TEL,
				ao.FST_MSKM_YUBIN_BNG      = tabal_rec.FST_MSKM_YUBIN_BNG,
				ao.FST_MSKM_KEN_SIKUGUN_MEI= tabal_rec.FST_MSKM_KEN_SIKUGUN_MEI,
				ao.FST_MSKM_CHOSON_CHOZ    = tabal_rec.FST_MSKM_CHOSON_CHOZ,
				ao.FST_MSKM_TTMNMEI        = tabal_rec.FST_MSKM_TTMNMEI,
				ao.SOSIN_FILENAME          = tabal_rec.SOSIN_FILENAME,
				ao.FT_TABAL_STATUS         = tabal_rec.FT_TABAL_STATUS,
				ao.FT_ARAIGAE_STATUS       = tabal_rec.FT_ARAIGAE_STATUS,
				ao.NG_KBN                  = tabal_rec.NG_KBN,
				ao.SP_SEND_DATE            = tabal_rec.SP_SEND_DATE,
				ao.SAKUJO_FLG              = tabal_rec.SAKUJO_FLG,
				ao.INSERT_USER             = n_user,
				ao.INSERT_DATE             = n_date,
				ao.UPDATE_USER             = n_user,
				ao.UPDATE_DATE             = n_date
			where ao.OYA_ID				   = tabal_rec.ENO
			  and ao.SERIAL_NO			   = tabal_rec.SERIAL_NO;
		else
			-- データが存在しない場合は登録
			insert into AOBACAT.TB_TABAL_INFO@RF
			  values(
					tabal_rec.ENO,
					n_kokyaku_cd,
					tabal_rec.SERIAL_NO,
					tabal_rec.TABAL_ID,
					tabal_rec.MSKM_KANJI_SHAMEI,
					tabal_rec.MSKM_KN_SHAMEI,
					tabal_rec.MSKM_TEL,
					tabal_rec.BIRTHDAY,
					tabal_rec.SEX_FLG,
					tabal_rec.MSKM_YUBIN_BNG,
					tabal_rec.MSKM_KEN_SIKUGUN_MEI,
					tabal_rec.MSKM_CHOSON_CHOZ,
					tabal_rec.MSKM_TTMNMEI,
					tabal_rec.TABAL_SINSEI,
					tabal_rec.TABAL_STATUS,
					tabal_rec.ARAIGAE_DATE,
					tabal_rec.ARAIGAE_STATUS,
					n_date,
					tabal_rec.FST_TABAL_ID,
					tabal_rec.FST_MSKM_KANJI_SHAMEI,
					tabal_rec.FST_MSKM_KN_SHAMEI,
					tabal_rec.FST_MSKM_TEL,
					tabal_rec.FST_MSKM_YUBIN_BNG,
					tabal_rec.FST_MSKM_KEN_SIKUGUN_MEI,
					tabal_rec.FST_MSKM_CHOSON_CHOZ,
					tabal_rec.FST_MSKM_TTMNMEI,
					tabal_rec.SOSIN_FILENAME,
					tabal_rec.FT_TABAL_STATUS,
					tabal_rec.FT_ARAIGAE_STATUS,
					tabal_rec.NG_KBN,
					tabal_rec.SP_SEND_DATE,
					tabal_rec.SAKUJO_FLG,
					n_user,
					n_date,
					n_user,
					n_date
				);
		end if;
	end loop;

	/*****************************************
	-- 後処理
	-- Manbow tabal契約者情報 のデータ移動対象を更新
	*****************************************/
	UPDATE TB_TABAL_INFO
	   SET AOBA_SEND_DATE = SYSDATE
	 WHERE AOBA_SEND_DATE IS NULL;

	COMMIT;

EXCEPTION
	WHEN OTHERS THEN
		ROLLBACK;
		RAISE_APPLICATION_ERROR(-20500, '[ユーザエラー]AOBA転送日次(tabal契約者情報連携)でエラーが発生しました。');
--		dbms_output.put_line('SQL コード = ' || sqlcode);
--		dbms_output.put_line('エラー内容 = ' || sqlerrm(sqlcode));
END;
/
