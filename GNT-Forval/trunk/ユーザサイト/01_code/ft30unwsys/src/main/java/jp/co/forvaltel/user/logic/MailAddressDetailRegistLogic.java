/**
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.logic;

import java.util.HashMap;
import java.util.Map;

import jp.co.forvaltel.common.constant.CommonConst;
import jp.co.forvaltel.common.dto.BasicWebApiResDto;
import jp.co.forvaltel.user.constant.UserConst;

import org.seasar.framework.util.StringUtil;

/**
 * メールアドレス詳細設定情報登録処理（WebAPI実行）
 * 
 * @author smis
 */
public class MailAddressDetailRegistLogic extends AbstractWebApiLogic {

	/** WebAPI実行用のパス */
	private static final String ACTION_PATH = "mailAddressDetailRegist/regist";

	/** 転送設定：転送設定する */
	private static final String TRANSFER_CONFIGRATION_TRANSFER_MAIL = "1";

	/** 自動返信設定：自動返信 */
	private static final String AUTO_REPLY_FLAG_AUTO_REPLY = "1";

	/**
	 * メールアドレス詳細設定情報登録処理
	 * 
	 * @param kkykBng 顧客番号
	 * @param mailAccount メールアドレス
	 * @param autoTransfer 転送設定
	 * @param transferToEmail 転送先メールアドレス
	 * @param messageDelete メールを残す
	 * @param autoReply 自動応答設定
	 * @param autoReplySubject 件名
	 * @param autoReplyBody 本文
	 * @return BasicWebApiResDto 基本WebAPIResponseDTO
	 */
	public BasicWebApiResDto regist(String kkykBng, String mailAccount, String autoTransfer, String transferToEmail,
			String messageDelete, String autoReply, String autoReplySubject, String autoReplyBody, String radId, String accountClass) {

		Map<String, String> map = new HashMap<String, String>();
		// 顧客番号 = アクションフォーム.顧客番号
		map.put(UserConst.PRM_KEY_KKYK_BNG, kkykBng);
		// メールアドレス = アクションフォーム.メールアドレス
		map.put(UserConst.PRM_KEY_MAIL_ACCOUNT, mailAccount);
		// アクションフォーム.転送設定
		// 転送設定 = アクションフォーム.転送設定
		map.put(UserConst.PRM_KEY_AUTO_TRANSFER, autoTransfer);
		// 転送設定＝1の場合のみ
		if (StringUtil.equals(TRANSFER_CONFIGRATION_TRANSFER_MAIL, autoTransfer)) {
			// 転送先メールアドレス = アクションフォーム.転送先メールアドレス
			map.put(UserConst.PRM_KEY_TRANSFER_TO_EMAIL, transferToEmail);
			// メールを残す = アクションフォーム.メールを残す
			map.put(UserConst.PRM_KEY_MESSAGE_DELETE, messageDelete);
			// 上記以外
		} else {
			// 転送先メールアドレス = 設定しない
			map.put(UserConst.PRM_KEY_TRANSFER_TO_EMAIL, CommonConst.EMPTY_STRING);
			// メールを残す = 設定しない
			map.put(UserConst.PRM_KEY_MESSAGE_DELETE, CommonConst.EMPTY_STRING);
		}
		// アクションフォーム.自動応答設定
		// 自動応答設定 = アクションフォーム.自動応答設定
		map.put(UserConst.PRM_KEY_AUTO_REPLY, autoReply);
		// 自動応答設定＝１の場合のみ
		if (StringUtil.equals(AUTO_REPLY_FLAG_AUTO_REPLY, autoReply)) {
			// 件名 = アクションフォーム.件名
			map.put(UserConst.PRM_KEY_AUTO_REPLY_SUBJECT, autoReplySubject);
			// 本文 = アクションフォーム.本文
			map.put(UserConst.PRM_KEY_AUTO_REPLY_BODY, autoReplyBody);
			// 上記以外
		} else {
			// 件名 = アクションフォーム.件名
			map.put(UserConst.PRM_KEY_AUTO_REPLY_SUBJECT, CommonConst.EMPTY_STRING);
			// 本文 = アクションフォーム.本文
			map.put(UserConst.PRM_KEY_AUTO_REPLY_BODY, CommonConst.EMPTY_STRING);
		}
		
		// TTM_DEV 20170810 BEGIN ADD
		map.put(UserConst.PRM_KEY_INTERNET_CONNECTTION_ID, radId);
		map.put(UserConst.PRM_KEY_ACCOUNT_CLASSIFICATION, accountClass);
		// TTM_DEV 20170810 END ADD

		// WebAPI実行
		BasicWebApiResDto resDto = super.execute(ACTION_PATH, map);

		// 戻り値設定
		return resDto;
	}
}
