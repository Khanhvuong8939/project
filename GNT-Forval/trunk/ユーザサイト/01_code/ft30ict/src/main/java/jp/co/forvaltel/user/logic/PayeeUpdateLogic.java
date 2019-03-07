/**
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.logic;

import java.util.Map;

import jp.co.forvaltel.common.dto.BasicWebApiResDto;

/**
 * 支払先情報変更（WebAPI実行）
 * 
 * @author smis
 */
public class PayeeUpdateLogic extends AbstractWebApiLogic {

	/** WebAPI実行用のパス */
	private static final String ACTION_PATH = "payeeUpdate/updatePayee";
	
	/**
	 * 支払先情報変更
	 * 
	 * @param map 変更情報
	 * @return BasicWebApiResDto 支払先情報変更結果
	 */
	public BasicWebApiResDto updatePayee(Map<String, String> map) {

		// WebAPI実行
		BasicWebApiResDto resDto = super.execute(ACTION_PATH, map);

		// 戻り値設定
		return resDto;
	}

}
