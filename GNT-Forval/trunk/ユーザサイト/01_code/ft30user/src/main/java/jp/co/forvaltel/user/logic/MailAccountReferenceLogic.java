/**
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.logic;

import java.util.HashMap;
import java.util.Map;

import jp.co.forvaltel.common.dto.BasicWebApiResDto;
import jp.co.forvaltel.user.constant.UserConst;

/**
 * メールアカウント情報参照処理（WebAPI実行）
 * 
 * @author smis
 */
public class MailAccountReferenceLogic extends AbstractWebApiLogic {

	/** WebAPI実行用のパス */
	private static final String ACTION_PATH = "mailAccountReference/getMailAccountInfo";
	
	/**
	 * メールアカウント情報参照処理
	 * 
	 * @param kkykBng 顧客番号
	 * @return BasicWebApiResDto 基本WebAPIResponseDTO
	 */
//	TTM_DEV  20170530 BEGIN UPDATE
//	public BasicWebApiResDto getMailAccountInfo(String kkykBng) {
	public BasicWebApiResDto getMailAccountInfo(String kkykBng,String internetConnectionId,String accountClassification) {
//	TTM_DEV  20170530 END UPDATE
		Map<String, Object> map = new HashMap<String, Object>();
		// 顧客番号 = なし（顧客番号）
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
