CREATE OR REPLACE PROCEDURE        FAOB010117
/********************************************************************/
/*  関数名：FAOB010117                                              */
/*  機能名：請求書データ作成：YEC OEMｺｰﾄﾞ変換処理                   */
/*  作成者：斎藤                                                    */
/*  作成日：2008/8/7                                                */
/*  更新日：                                                        */
/*  VER   ：1.00                                                    */
/*------------------------------------------------------------------*/
/*  引数：      無し                                                */
/*  戻値：      無し                                                */
/********************************************************************/
IS
    /* ローカル定数 */

    /* ローカル変数 */
    nRet                    NUMBER;                         --リターンコード
    dSysDate                wk_bl_kokyaku.insert_date%TYPE; --システム日付
    vUser                   wk_bl_kokyaku.insert_user%TYPE; --ユーザ
    cGun                    CHAR(2);
    nOemcd                  CHAR(4);

BEGIN
    /* システム日時の取得 */
    nRet := aoba_common.get_date( dSysDate );
    /* ユーザの取得 */
    nRet := aoba_common.get_user( vUser );

    /* OEMコード変換ワーク一時TBLのレコード削除 */
    execute immediate 'TRUNCATE TABLE wk_oem_henkan_temp';

    /* OEMコード変換ワークTBLのレコード削除 */
    execute immediate 'TRUNCATE TABLE wk_oem_henkan';

    /* OEMコード変換ワーク一時TBLのレコード作成 */
    INSERT INTO wk_oem_henkan_temp
    (   kokyaku_cd,
        srv_type_cd,
        id,--
        dairiten_cd,
        juchu_moshikomi_date,
        moto_oem_cd,
        henkan_oem_cd
    )
        SELECT  A.kokyaku_cd,
                A.srv_type_cd,
                A.id,--
                A.dairiten_cd,
                A.juchu_date,
                B.moto_oem_cd,
                B.henkan_oem_cd
        FROM    tb_kokyaku_id_joho A,
                tb_oem_henkan_taisho_mst B
        WHERE   A.dairiten_cd = B.dairiten_cd;

    /* OEMコード変換ワークTBLのレコード作成 */
    INSERT INTO wk_oem_henkan
    (   kokyaku_cd,
        srv_type_cd,
        dairiten_cd,
        juchu_moshikomi_date,
        moto_oem_cd,
        henkan_oem_cd
    )
        SELECT  A.kokyaku_cd,
                A.srv_type_cd,
                A.dairiten_cd,
                A.juchu_moshikomi_date,
                A.moto_oem_cd,
                A.henkan_oem_cd
        FROM    wk_oem_henkan_temp A,

                (SELECT  F.kokyaku_cd, F.juchu_moshikomi_date, F.id, MAX(F.srv_type_cd) srv_type_cd --
                 FROM   wk_oem_henkan_temp F,
                    (SELECT  C.kokyaku_cd, C.juchu_moshikomi_date, MAX(C.id) id --
                     FROM   wk_oem_henkan_temp C,
                        (SELECT  kokyaku_cd, MAX(juchu_moshikomi_date) juchu_moshikomi_date --
                         FROM    wk_oem_henkan_temp
                         GROUP BY kokyaku_cd) D
                     WHERE C.kokyaku_cd = D.kokyaku_cd
                     AND   C.juchu_moshikomi_date = D.juchu_moshikomi_date
                     GROUP BY C.kokyaku_cd, C.juchu_moshikomi_date) E
                 WHERE E.kokyaku_cd = F.kokyaku_cd
                 AND   E.juchu_moshikomi_date = F.juchu_moshikomi_date
                 AND   E.id = F.id
                 GROUP BY F.kokyaku_cd, F.juchu_moshikomi_date, F.id
                 ) B

        WHERE   A.kokyaku_cd = B.kokyaku_cd
        AND     A.juchu_moshikomi_date = B.juchu_moshikomi_date
        AND     A.ID = B.ID
        AND     A.SRV_TYPE_CD = B.SRV_TYPE_CD;

    /* 請求書顧客マスタ一時TBLの擬似OEMコード変換 */
    UPDATE wk_bl_kokyaku A SET (A.seikyu_data,A.oem_cd_sk)
    =(
      SELECT SUBSTRB(A.seikyu_data,1,18)||TRIM(TO_CHAR(b.henkan_oem_cd,'0009'))||SUBSTRB(A.seikyu_data,23),
             b.henkan_oem_cd
      FROM ( SELECT * FROM wk_oem_henkan WHERE henkan_oem_cd is not NULL ) b
      WHERE
          A.kokyaku_cd_sk = b.kokyaku_cd
     )
    WHERE
      EXISTS
      (
      SELECT 1
      FROM ( SELECT * FROM wk_oem_henkan WHERE henkan_oem_cd is not NULL ) b
      WHERE
          A.kokyaku_cd_sk = b.kokyaku_cd
     );

    /* 請求書回線明細一時TBLの擬似OEMコード変換 */
    UPDATE wk_bl_kaisen_meisai A SET A.kaisen_meisai_data
    =(
      SELECT SUBSTRB(A.KAISEN_MEISAI_DATA,1,24)||TRIM(TO_CHAR(b.henkan_oem_cd,'0009'))||SUBSTRB(A.KAISEN_MEISAI_DATA,29)
      FROM ( SELECT * FROM wk_oem_henkan WHERE henkan_oem_cd is not NULL ) b
      WHERE
          A.kokyaku_cd_sk = B.kokyaku_cd
    )
    WHERE
      EXISTS
      (
      SELECT 1
      FROM ( SELECT * FROM wk_oem_henkan WHERE henkan_oem_cd is not NULL ) b
      WHERE
          A.kokyaku_cd_sk = b.kokyaku_cd
      );

    /* 請求書通話明細一時TBLの擬似OEMコード変換 */
    UPDATE wk_bl_tuwa_meisai A SET A.kaisen_meisai_data
    =(
      SELECT SUBSTRB(A.KAISEN_MEISAI_DATA,1,24)||TRIM(TO_CHAR(b.henkan_oem_cd,'0009'))||SUBSTRB(A.KAISEN_MEISAI_DATA,29)
      FROM ( SELECT * FROM wk_oem_henkan WHERE henkan_oem_cd is not NULL ) b
      WHERE
          A.kokyaku_cd_sk = B.kokyaku_cd
    )
    WHERE
      EXISTS
      (
      SELECT 1
      FROM ( SELECT * FROM wk_oem_henkan WHERE henkan_oem_cd is not NULL ) b
      WHERE
          A.kokyaku_cd_sk = b.kokyaku_cd
      );

    /* 請求書出荷明細一時TBLの擬似OEMコード変換 */
    UPDATE wk_bl_shukka_meisai A SET A.kaisen_meisai_data
    =(
      SELECT  SUBSTRB(A.KAISEN_MEISAI_DATA,1,24)||TRIM(TO_CHAR(b.henkan_oem_cd,'0009'))||SUBSTRB(A.KAISEN_MEISAI_DATA,29)
      FROM ( SELECT * FROM wk_oem_henkan WHERE henkan_oem_cd is not NULL ) b
      WHERE
          A.kokyaku_cd_sk = B.kokyaku_cd
    )
    WHERE
      EXISTS
      (
      select 1
      FROM ( SELECT * FROM wk_oem_henkan WHERE henkan_oem_cd is not NULL ) b
      WHERE
          A.kokyaku_cd_sk = b.kokyaku_cd
      );

END FAOB010117;
/
