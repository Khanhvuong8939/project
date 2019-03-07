/**
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.forvaltel.common.dto.BasicWebApiResDto;
import jp.co.forvaltel.common.entity.HcKakuchouData;

/**
 * 商品マスタ取得（WebAPI実行）
 * 
 * @author smis
 */
public class OptionLogic extends AbstractWebApiLogic {

	/** WebAPI実行用のパス（オプション追加） */
	private static final String ADD_ACTION_PATH = "optionRegister/addOption";

	/**
	 * オプション入力チェック
	 * @param juchNo 受注番号
	 * @param shohinCd 商品コード
	 * @param orderRegisterKakuchouDto 拡張項目
	 * @return BasicWebApiResDto 実行結果
	 */
	// TTM_DEV 20170719 BEGIN UPDATE
//	public BasicWebApiResDto addOption(String juchNo, String shohinCd, 
//										List<HcKakuchouData> orderRegisterKakuchouDto) {
	public BasicWebApiResDto addOption(String userId, String juchNo, String shohinCd, 
										List<HcKakuchouData> orderRegisterKakuchouDto) {
	// TTM_DEV 20170719 END UPDATE

		// 送信値を生成
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("juchNo", juchNo);
		map.put("shohinCd", shohinCd);
		map.put("hcKakuchouDataEx", orderRegisterKakuchouDto);
		// TTM_DEV 20170719 BEGIN ADD
		map.put("userCd", userId);
		// TTM_DEV 20170719 END ADD

		// WebAPI実行
		BasicWebApiResDto resDto = super.executeWebApi(ADD_ACTION_PATH, map);

		// 戻り値設定
		return resDto;
	}
}
