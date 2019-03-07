/**
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.logic;

import java.util.HashMap;
import java.util.Map;

import jp.co.forvaltel.common.dto.BasicWebApiResDto;

/**
 * 初回ログインフラグ更新ロジック
 * 
 * @author smis
 */
public class LoginFlgUpdateLogic extends AbstractWebApiLogic {

	/** WebAPI実行用のパス */
	private static final String ACTION_PATH = "loginFlgUpdate/updateFlg";

	/**
	 * フラグ更新
	 * 
	 * @param version バージョン
	 * @return BasicWebApiResDto 基本WebAPIResponseDTO
	 */
	public BasicWebApiResDto updateFlg(Integer version) {

		// 引数用のmapを作成
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("version", version);

		// WebAPI実行
		BasicWebApiResDto resDto = super.executeWebApi(ACTION_PATH, map);

		// 戻り値設定
		return resDto;
	}

}

