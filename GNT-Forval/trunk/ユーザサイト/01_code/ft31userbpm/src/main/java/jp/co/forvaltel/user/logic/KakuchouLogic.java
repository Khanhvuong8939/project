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
	private static final String ACTION_PATH_GET_KAKUCHOU_LIST = "kakuchou/getkakuchouList";

	/** パラメータ moshikomiNo */
	private static final String PARAM_MOSHIKOMI_NO = "moshikomiNo";
	/** パラメータ mode */
	private static final String PARAM_MODE = "mode";

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
	public BasicWebApiResDto getkakuchouList(String moshikomiNo, String mode) {

		// 引数用のmapを作成
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(PARAM_MOSHIKOMI_NO, moshikomiNo);
		map.put(PARAM_MODE, mode);

		// WebAPI実行
		BasicWebApiResDto resDto = super.executeWebApi(ACTION_PATH_GET_KAKUCHOU_LIST, map);

		// 戻り値設定
		return resDto;
	}

}
