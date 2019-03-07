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
public class DenryokuLogic extends AbstractWebApiLogic {

	private static final String ACTION_PATH_AUTH = "denryoku/customerAuth";
	/** WebAPI実行用のパス */
	private static final String ACTION_PATH = "denryoku/getCustomerInfo";
	/** WebAPI実行用のパス */
	private static final String ACTION_PATH_UPDATE = "denryoku/updateCustomerInfo";
	/** WebAPI実行用のパス */
	private static final String ACTION_PATH_GET_LOGO = "denryoku/getLogo";

	/** パラメータ 代理店コード */
	private static final String PARAM_MOSHIKOMI_NO = "moshikomiNo";
	/**
	 * Get info to display on agree screen
	 * @return
	 */
	public BasicWebApiResDto checkAuth() {
		Map<String, Object> map = new HashMap<String, Object>();
		// WebAPI実行
		BasicWebApiResDto resDto = super.executeWebApi(ACTION_PATH_AUTH, map);

		// 戻り値設定
		return resDto;
	}
	
	/**
	 * Get info to display on agree screen
	 * @return
	 */
	public BasicWebApiResDto getCustomerInfo() {
		Map<String, Object> map = new HashMap<String, Object>();
		// WebAPI実行
		BasicWebApiResDto resDto = super.executeWebApi(ACTION_PATH, map);

		// 戻り値設定
		return resDto;
	}

	/**
	 * Update info when customer click agree button
	 * @param moshikomiNo
	 * @return
	 */
	public BasicWebApiResDto updateCustomerInfo(String moshikomiNo) {

		// 引数用のmapを作成
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(PARAM_MOSHIKOMI_NO, moshikomiNo);
		
		// WebAPI実行
		BasicWebApiResDto resDto = super.executeWebApi(ACTION_PATH_UPDATE, map);

		// 戻り値設定
		return resDto;
	}
	
	public BasicWebApiResDto getLogo() {
		// 引数用のmapを作成
		Map<String, Object> map = new HashMap<String, Object>();
		// WebAPI実行
		BasicWebApiResDto resDto = super.executeWebApi(ACTION_PATH_GET_LOGO, map);
		// 戻り値設定
		return resDto;
	}
}
