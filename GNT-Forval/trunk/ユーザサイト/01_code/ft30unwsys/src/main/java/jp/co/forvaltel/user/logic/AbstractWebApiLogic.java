/**
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.logic;

import java.util.Map;

import javax.annotation.Resource;

import jp.co.forvaltel.common.dto.BasicWebApiResDto;
import jp.co.forvaltel.common.logic.AbstractLogic;
import jp.co.forvaltel.common.util.JsonUtils;

/**
 * WebAPIロジック抽象クラス
 * 
 * @author smis
 */
public abstract class AbstractWebApiLogic extends AbstractLogic {

	/**
	 * ユーザサイトセッション情報
	 */
	@Resource
	private WebApiCoreLogic webApiCoreLogic;

	/**
	 * WebAPI実行
	 * 
	 * @param actionPath アクションパス
	 * @param map パラメータマップ
	 * @return String JSON文字列
	 */
	protected BasicWebApiResDto execute(String actionPath, Map<String, String> map) {
		// WebAPI実行
		return this.webApiCoreLogic.execute(actionPath, map);
	}

	/**
	 * WebAPI実行
	 * 
	 * @param actionPath アクションパス
	 * @param map パラメータマップ
	 * @return String JSON文字列
	 */
	protected BasicWebApiResDto executeWebApi(String actionPath, Map<String, Object> map) {
		// WebAPI実行
		return this.webApiCoreLogic.executeWebApi(actionPath, map);
	}

	/**
	 * 一度JSONオブジェクト化されたMapを該当Classのマップにデコード
	 * 
	 * @param obj 変換元オブジェクト
	 * @param calzz 返還後のクラス
	 * @param <T> 返還後のクラス
	 * @return 返還後のオブジェクト
	 */
	protected <T> T decode(Object obj, Class<? extends T> calzz) {
		return JsonUtils.decode(obj, calzz);
	}
}
