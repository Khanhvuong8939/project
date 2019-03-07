/**
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.logic;

import java.util.HashMap;
import java.util.Map;

import jp.co.forvaltel.common.dto.BasicWebApiResDto;
import jp.co.forvaltel.user.constant.UserConst;

/**
 * メールパスワード変更（WebAPI実行）
 * 
 * @author smis
 */
public class MailPwRegistLogic extends AbstractWebApiLogic {

	/** WebAPI実行用のパス */
	private static final String ACTION_PATH = "mailPwRegist/regist";

	/**
	 * メールパスワード変更処理
	 * 
	 * @param kkykBng 顧客番号
	 * @param mailAccount メールアドレス
	 * @param mailPassword パスワード
	 * @return BasicWebApiResDto 基本WebAPIResponseDTO
	 */
	//	TTM_DEV  20170530 BEGIN UPDATE
	//	public BasicWebApiResDto regist(String kkykBng, String mailAccount, String mailPassword){
	public BasicWebApiResDto regist(String kkykBng, String mailAccount, String mailPassword,
			String internetConnectionId,String accountClassification) {
	//	TTM_DEV  20170530 END UPDATE

		Map<String, String> map = new HashMap<String, String>();
		// 顧客番号 = アクションフォーム.顧客番号リスト
		map.put(UserConst.PRM_KEY_KKYK_BNG, kkykBng);
		// メールアドレス = アクションフォーム.メールアドレス
		map.put(UserConst.PRM_KEY_MAIL_ACCOUNT, mailAccount);
		// メールパスワード = アクションフォーム.メールパスワード
		map.put(UserConst.PRM_KEY_MAIL_PASS, mailPassword);
		//	TTM_DEV  20170530 BEGIN ADD
		map.put(UserConst.PRM_KEY_INTERNET_CONNECTTION_ID, internetConnectionId);
		map.put(UserConst.PRM_KEY_ACCOUNT_CLASSIFICATION, accountClassification);
		//	TTM_DEV  20170530 END ADD
		// WebAPI実行
		BasicWebApiResDto resDto = super.execute(ACTION_PATH, map);

		// 戻り値設定
		return resDto;
	}
}
