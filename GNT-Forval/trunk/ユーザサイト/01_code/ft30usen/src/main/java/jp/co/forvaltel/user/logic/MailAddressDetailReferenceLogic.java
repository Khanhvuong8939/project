/**
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.logic;

import java.util.HashMap;
import java.util.Map;

import jp.co.forvaltel.common.dto.BasicWebApiResDto;
import jp.co.forvaltel.user.constant.UserConst;

/**
 * メールアドレス詳細設定情報参照処理（WebAPI実行）
 * 
 * @author smis
 */
public class MailAddressDetailReferenceLogic extends AbstractWebApiLogic {

	/** WebAPI実行用のパス */
	private static final String ACTION_PATH = "mailAddressDetailReference/getMailAddressDetailInfo";
	
	/**
	 * メールアドレス詳細設定情報参照処理
	 * 
	 * @param kkykBng 顧客番号
	 * @param mailAccount メールアカウント
	 * @return BasicWebApiResDto 基本WebAPIResponseDTO
	 */
//	TTM_DEV  20170530 BEGIN UPDATE
//	public BasicWebApiResDto getMailAddressDetailInfo(String kkykBng, String mailAccount) {
	public BasicWebApiResDto getMailAddressDetailInfo(String kkykBng, String mailAccount,
			String internetConnectionId,String accountClassification) {
//	TTM_DEV  20170530 END UPDATE
		Map<String, String> map = new HashMap<String, String>();
		// 顧客番号 = 設定した顧客番号
		map.put(UserConst.PRM_KEY_KKYK_BNG, kkykBng);
		// メールアカウント = 設定したメールアドレス
		map.put(UserConst.PRM_KEY_MAIL_ACCOUNT, mailAccount);
//		TTM_DEV  20170530 BEGIN ADD
		map.put(UserConst.PRM_KEY_INTERNET_CONNECTTION_ID, internetConnectionId);
		map.put(UserConst.PRM_KEY_ACCOUNT_CLASSIFICATION, accountClassification);
//		TTM_DEV  20170530 END ADD
		// WebAPI実行
		BasicWebApiResDto resDto = super.execute(ACTION_PATH, map);

		// 戻り値設定
		return resDto;
	}
}
