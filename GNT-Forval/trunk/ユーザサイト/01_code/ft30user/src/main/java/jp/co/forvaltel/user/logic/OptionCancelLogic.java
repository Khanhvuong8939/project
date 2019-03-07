/**
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.logic;

import java.util.HashMap;
import java.util.Map;

import jp.co.forvaltel.common.dto.BasicWebApiResDto;

/**
 * オプション解約処理（WebAPI実行）
 * 
 * @author smis
 */
public class OptionCancelLogic extends AbstractWebApiLogic {

	/** WebAPI実行用のパス */
	private static final String ACTION_PATH = "optionCancel/cancelOption";
	/** パラメータ 受注番号 */
	private static final String JUCH_NO = "juchNo";
	/** パラメータ 受注明細番号 */
	private static final String JUCH_MEISAI_NO = "juchMeisaiNo";

	/**
	 * オプション解約処理を行う。
	 * 
	 * @param juchNo 受注番号
	 * @param juchMeisaiNo 受注明細番号
	 * @return boolean true:成功/false:失敗
	 */
	public BasicWebApiResDto cancelOption(String juchNo, String juchMeisaiNo) {

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

