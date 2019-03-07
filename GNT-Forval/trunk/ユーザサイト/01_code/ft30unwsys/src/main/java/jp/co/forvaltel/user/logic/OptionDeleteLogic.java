/**
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.logic;

import java.util.HashMap;
import java.util.Map;

import jp.co.forvaltel.common.dto.BasicWebApiResDto;

/**
 * キャンセルオプション（WebAPI実行）
 * 
 * @author smis
 */
public class OptionDeleteLogic extends AbstractWebApiLogic {

	/** WebAPI実行用のパス */
	private static final String ACTION_PATH = "optionDelete/deleteOption";
	/** パラメータ 受注番号 */
	private static final String JUCH_NO = "juchNo";
	/** パラメータ 受注明細番号 */
	private static final String JUCH_MEISAI_NO = "juchMeisaiNo";

	/**
	 * 内部APIのオプションキャンセル処理を実行する。
	 * 
	 * @param juchNo 受注番号
	 * @param juchMeisaiNo 受注明細番号
	 * @return boolean true:成功/false:失敗
	 */
	public BasicWebApiResDto deleteOption(String juchNo, String juchMeisaiNo) {

		// 引数用のmapを作成
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(JUCH_NO, juchNo);
		map.put(JUCH_MEISAI_NO, juchMeisaiNo);

		// WebAPI実行
		BasicWebApiResDto resDto = super.executeWebApi(ACTION_PATH, map);

		// 戻り値設定
		return resDto;
	}

}

