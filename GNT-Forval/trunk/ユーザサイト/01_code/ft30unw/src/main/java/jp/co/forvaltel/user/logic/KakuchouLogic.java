/**
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.logic;

import java.util.HashMap;
import java.util.Map;

import jp.co.forvaltel.common.dto.BasicWebApiResDto;

/**
 * 拡張項目ロジック
 * 
 * @author smis
 */
public class KakuchouLogic extends AbstractWebApiLogic {

	/** WebAPI実行用のパス */
	private static final String ACTION_PATH = "kakuchouMst/getShohinList";
	/** WebAPI実行用のパス */
	private static final String ACTION_PATH_GET_KAKUCHOU_LIST = "kakuchouMst/getkakuchouList";
	/** WebAPI実行用のパス */
	private static final String ACTION_PATH_GET_KAKUCHOU_DATA_LIST = "kakuchouMst/getkakuchouDataList";

	/** パラメータ 代理店コード */
	private static final String PARAM_DAIRITEN_CD = "dairitenCd";
	/** パラメータ サービスコード */
	private static final String PARAM_SRV_CD = "srvCd";
	/** パラメータ 商品コード */
	private static final String PARAM_SHOHIN_CD = "shohinCd";
	/** パラメータ 親商品コード */
	private static final String PARAM_OYA_SHOHIN_CD = "oyaShohinCd";
	/** パラメータ 受注番号 */
	private static final String PARAM_JUCH_NO = "juchNo";
	/** パラメータ 受注番号 */
	private static final String PARAM_JUCH_MEISAI_NO = "juchMeisaiNo";
	/** パラメータ 表示区分 */
	private static final String PARAM_HYOUJI_KBN = "hyoujiKbn";
//	TMM_DEV 20170615 ADD BEGIN
	/** gamen shikibetsu*/
	private static final String PARAM_SCREEN = "screen";
//	TMM_DEV 20170615 ADD END
	/**
	 * 商品リスト取得
	 * 
	 * @param dairitenCd サービスコード
	 * @param srvCd サービスコード
	 * @param oyaShohinCd サービスコード
	 * @return BasicWebApiResDto 処理結果DTO
	 */
	public BasicWebApiResDto getShohinList(String dairitenCd, String srvCd, String oyaShohinCd) {

		// 引数用のmapを作成
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(PARAM_DAIRITEN_CD, dairitenCd);
		map.put(PARAM_SRV_CD, srvCd);
		map.put(PARAM_OYA_SHOHIN_CD, oyaShohinCd);

		// WebAPI実行
		BasicWebApiResDto resDto = super.executeWebApi(ACTION_PATH, map);

		// 戻り値設定
		return resDto;
	}

	/**
	 * 拡張項目リストを取得
	 * 
	 * @param juchNo 受注番号
	 * @param juchMeisai 受注明細
	 * @param srvCd サービスコード
	 * @param shohinCd 商品コード
	 * @param hyoujiKbn 表示区分
	 * @return BasicWebApiResDto 処理結果DTO
	 */
	public BasicWebApiResDto getkakuchouList(String juchNo, String juchMeisai, String srvCd, String shohinCd,
			String hyoujiKbn,
			//TTM_DEV 20170615 ADD BEGIN
			String screen) {
			//TTM_DEV 20170615 ADD END

		// 引数用のmapを作成
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(PARAM_JUCH_NO, juchNo);
		map.put(PARAM_JUCH_MEISAI_NO, juchMeisai);
		map.put(PARAM_SRV_CD, srvCd);
		map.put(PARAM_SHOHIN_CD, shohinCd);
		map.put(PARAM_HYOUJI_KBN, hyoujiKbn);
		//TTM_DEV 20170615 ADD BEGIN
		map.put(PARAM_SCREEN, screen);
		//TTM_DEV 20170615 ADD END
		// WebAPI実行
		BasicWebApiResDto resDto = super.executeWebApi(ACTION_PATH_GET_KAKUCHOU_LIST, map);

		// 戻り値設定
		return resDto;
	}
	
	/**
	 * 拡張項目リストを取得
	 * 
	 * @param juchNo 受注番号
	 * @param juchMeisai 受注明細
	 * @return BasicWebApiResDto 処理結果DTO
	 */
	public BasicWebApiResDto getkakuchouDataList(String juchNo, String juchMeisai) {

		// 引数用のmapを作成
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(PARAM_JUCH_NO, juchNo);
		map.put(PARAM_JUCH_MEISAI_NO, juchMeisai);

		// WebAPI実行
		BasicWebApiResDto resDto = super.executeWebApi(ACTION_PATH_GET_KAKUCHOU_DATA_LIST, map);

		// 戻り値設定
		return resDto;
	}


}
