/**
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.logic;

import java.util.HashMap;
import java.util.Map;

import jp.co.forvaltel.common.dto.BasicWebApiResDto;
import jp.co.forvaltel.common.dto.PayeeReferenceResDto;

/**
 * 支払先情報取得（WebAPI実行）
 *
 * @author smis
 */
public class PayeeReferenceLogic extends AbstractWebApiLogic {

	/** WebAPI実行用のパス */
	private static final String ACTION_PATH = "payeeReference/getPayee";

	/**
	 * 支払先情報取得
	 *
	 * @param seiqCd 請求先コード
	 * @param kkykBng 顧客番号
	 * @return PayeeReferenceResDto 支払先情報取得結果
	 */
	public BasicWebApiResDto getPayee(String seiqCd, String kkykBng) {

		// 引数用のmapを作成
		Map<String, String> map = new HashMap<String, String>();
		map.put("seiqCd", seiqCd);
		map.put("kkykBng", kkykBng);

		// WebAPI実行
		BasicWebApiResDto resDto = super.execute(ACTION_PATH, map);

		// 戻り値のJSONを変換
		resDto.setReturnValue(super.decode(resDto.getReturnValue(), PayeeReferenceResDto.class));

		// 戻り値設定
		return resDto;
	}

}
