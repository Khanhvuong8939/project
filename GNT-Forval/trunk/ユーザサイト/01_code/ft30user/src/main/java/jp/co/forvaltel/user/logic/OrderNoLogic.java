/**
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.logic;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jp.co.forvaltel.common.dto.BasicWebApiResDto;

/**
 * 受注番号取得（WebAPI実行）
 * 
 * @author smis
 */
public class OrderNoLogic extends AbstractWebApiLogic {

	/** WebAPI実行用のパス */
	private static final String ACTION_PATH = "orderNo/getOrderNoList";

	/**
	 * 指定されたユーザコードの契約者に関する受注番号を取得する。
	 * 
	 * @return List<String> 受注番号リスト
	 */
	@SuppressWarnings("unchecked")
	public List<String> getOrderNoList() {

		// 引数用のmapを作成
		Map<String, String> map = new HashMap<String, String>();

		// WebAPI実行
		BasicWebApiResDto resDto = super.execute(ACTION_PATH, map);

		// 戻り値用オブジェクトに結果を複写
		List<String> list = new LinkedList<String>();
		if (resDto.isResult()) {
			list.addAll((List<String>) resDto.getReturnValueList());
		}

		// 戻り値設定
		return list;
	}

}
