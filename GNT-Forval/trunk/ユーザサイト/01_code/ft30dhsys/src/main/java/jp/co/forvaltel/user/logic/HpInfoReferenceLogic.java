/**
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.logic;

import java.util.HashMap;
import java.util.Map;

import jp.co.forvaltel.common.dto.BasicWebApiResDto;
import jp.co.forvaltel.user.constant.UserConst;

/**
 * ホームページ情報取得（WebAPI実行）
 * 
 * @author smis
 */
public class HpInfoReferenceLogic extends AbstractWebApiLogic {

	/** WebAPI実行用のパス */
	private static final String ACTION_PATH = "hpInfoReference/getHpInfo";

	/**
	 * SP社の「ホームページ情報参照API」から情報を取得する。
	 * 
	 * @param kkykBng 顧客番号
	 * @return BasicWebApiResDto ホームページ情報レスポンス
	 */
//	TTM_DEV  20170530 BEGIN UPDATE
//	public BasicWebApiResDto getHpInfo(String kkykBng) {
	public BasicWebApiResDto getHpInfo(String kkykBng,String internetConnectionId,String accountClassification) {
//		TTM_DEV  20170530 END UPDATE
		// 引数用のmapを作成
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(UserConst.PRM_KEY_KKYK_BNG, kkykBng);
//		TTM_DEV  20170530 BEGIN ADD
		map.put(UserConst.PRM_KEY_INTERNET_CONNECTTION_ID, internetConnectionId);
		map.put(UserConst.PRM_KEY_ACCOUNT_CLASSIFICATION, accountClassification);
//		TTM_DEV  20170530 END ADD
		
		// WebAPI実行
		BasicWebApiResDto resDto = super.executeWebApi(ACTION_PATH, map);

		// 戻り値設定
		return resDto;
	}

}

