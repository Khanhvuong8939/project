/**
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.logic;

import java.util.HashMap;
import java.util.Map;

import jp.co.forvaltel.common.dto.BasicWebApiResDto;

/**
 * 受注詳細情報取得（WebAPI実行）
 * 
 * @author smis
 */
public class OrderDetailDataLogic extends AbstractWebApiLogic {

	/** WebAPI実行用のパス */
	private static final String ACTION_PATH = "orderDetailData/referDetail";
	/** パラメータ 受注番号 */
	private static final String JUCH_NO = "juchNo";

	/**
	 * 指定された受注番号で申込情報、請求先情報、送付先情報、設置先情報、受注情報を取得する。
	 * 
	 * @param juchNo 受注番号
	 * @return List<String> 受注番号リスト
	 */
	public BasicWebApiResDto referDetail(String juchNo) {

		// 引数用のmapを作成
		Map<String, String> map = new HashMap<String, String>();
		map.put(JUCH_NO, juchNo);

		// WebAPI実行
		BasicWebApiResDto resDto = super.execute(ACTION_PATH, map);

		// 戻り値設定
		return resDto;
	}

}
