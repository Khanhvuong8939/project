/**
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.forvaltel.common.dto.BasicWebApiResDto;
import jp.co.forvaltel.common.entity.HcShohinMst;

/**
 * 商品マスタ取得（WebAPI実行）
 * 
 * @author smis
 */
public class ShohinMstLogic extends AbstractWebApiLogic {

	/** WebAPI実行用のパス */
	private static final String ACTION_LIST_PATH = "shohinMst/getShohinMstList";
	/** WebAPI実行用のパス */
	private static final String ACTION_PATH = "shohinMst/getShohinMst";

	/**
	 * 子商品一覧取得
	 * @param juchNo 受注番号
	 * @param JuchMeisaiNo 受注明細番号
	 * @return List<HcShohinMst> 子商品コードの商品マスタエンティティのリスト
	 */
	public BasicWebApiResDto getChildShohinList(String juchNo, String JuchMeisaiNo) {

		// 送信値生成
		Map<String, String> map = new HashMap<String, String>();
		map.put("juchNo", juchNo);
		map.put("juchMeisaiNo", JuchMeisaiNo);

		// WebAPI実行
		BasicWebApiResDto resDto = super.execute(ACTION_LIST_PATH, map);

		// 戻り値設定
		return resDto;
	}

	/**
	 * JSONを変換
	 * @param resultList json形式処理結果
	 * @return List<HcShohinMst> 子商品コードの商品マスタエンティティのリスト
	 */
	public List<HcShohinMst> changeJson(BasicWebApiResDto resultList) {
		// 戻り値用オブジェクトに結果を複写
		List<HcShohinMst> list = new ArrayList<HcShohinMst>();
		for (Object obj : resultList.getReturnValueList()) {
			list.add(super.decode(obj, HcShohinMst.class));
		}

		// 戻り値設定
		return list;
	}

	/**
	 * @param map 商品コード
	 * @return resDto 処理結果
	 */
	public BasicWebApiResDto getShohinDate(Map<String, String> map) {

		// WebAPI実行
		BasicWebApiResDto resDto = super.execute(ACTION_PATH, map);


		// 戻り値設定
		return resDto; 
	}

}
