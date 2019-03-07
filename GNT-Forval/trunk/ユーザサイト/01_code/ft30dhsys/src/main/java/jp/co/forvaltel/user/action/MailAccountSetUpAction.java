/**
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.action;

import javax.annotation.Resource;

import jp.co.forvaltel.common.constant.CommonConst;
import jp.co.forvaltel.common.dto.BasicWebApiResDto;
import jp.co.forvaltel.common.dto.MailAccountDto;
import jp.co.forvaltel.common.util.JsonUtils;
import jp.co.forvaltel.user.constant.UserConst;
import jp.co.forvaltel.user.form.MailAccountSetUpForm;
import jp.co.forvaltel.user.logic.LogRegisterLogic;
import jp.co.forvaltel.user.logic.MailAddressDetailReferenceLogic;
import jp.co.forvaltel.user.logic.MailAddressDetailRegistLogic;
import jp.co.forvaltel.user.util.LogUtils;

import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.util.StringUtil;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

/**
 * メールアカウント詳細設定機能アクション処理
 *
 * @author smis
 */
public class MailAccountSetUpAction extends AbstractUserAction {

	/** JSPファイル名（登録） */
	private static final String JSP_FILE_MAIL_ACCOUNT_SETUP = "mailAccountSetUp.jsp";

	/** JSPファイル名（契約情報照会画面) */
	private static final String JSP_FILE_CONTRACT_INFO_QUERY = "/contractInfoQuery";

	/** 転送設定：転送設定する */
	private static final String TRANSFER_CONFIGRATION_TRANSFER_MAIL = "1";

	/** 自動返信設定：自動返信 */
	private static final String AUTO_REPLY_FLAG_AUTO_REPLY = "1";

	/** メッセージ埋め込み文字：メールアドレス */
	private static final String MESSAGE_STRING_TRANSFER_MAIL_ADDRESS = "転送先メールアドレス";

	/** メッセージ埋め込み文字：件名 */
	private static final String MESSAGE_STRING_SUBJECT = "自動返信設定の件名";

	/** メッセージ埋め込み文字：本文 */
	private static final String MESSAGE_STRING_TEXT = "自動返信設定の本文";

	/** アクションフォーム */
	@Resource
	@ActionForm
	public MailAccountSetUpForm mailAccountSetUpForm;

	/**
	 * メールアドレス詳細設定情報参照処理
	 */
	@Resource
	MailAddressDetailReferenceLogic mailAddressDetailReferenceLogic;

	/**
	 * メールアドレス詳細設定情報登録処理
	 */
	@Resource
	MailAddressDetailRegistLogic mailAddressDetailRegistLogic;

	// TTM_DEV_11 APPLICATION_LOG BEGIN ADD
	@Resource
	LogRegisterLogic logRegisterLogic;
	// TTM_DEV_11 APPLICATION_LOG END ADD

	/**
	 * 初期表示
	 *
	 * @return 戻り先
	 */
	@Execute(validator = false, input = JSP_FILE_CONTRACT_INFO_QUERY)
	public String index() {
		//TTM_DEV  20170530 BEGIN ADD
		String internetConnectionId = mailAccountSetUpForm.getInternetConnectionId();
		String accountClassification = mailAccountSetUpForm.getAccountClassification();
		//TTM_DEV  20170530 END ADD

		// メールアドレス詳細設定参照API呼出処理
		//TTM_DEV  20170530 BEGIN UPDATE
		//BasicWebApiResDto resDto = this.mailAddressDetailReferenceLogic.getMailAddressDetailInfo(
		//this.userSessionDto.getKkykBng(), this.mailAccountSetUpForm.getMailAccount());
		BasicWebApiResDto resDto = this.mailAddressDetailReferenceLogic.getMailAddressDetailInfo(
				this.userSessionDto.getKkykBng(), this.mailAccountSetUpForm.getMailAccount(),internetConnectionId,accountClassification);
		//TTM_DEV  20170530 END UPDATE
		MailAccountDto mailDto = JsonUtils.decode(resDto.getReturnValue(), MailAccountDto.class);
		
		// 画面出力項目設定処理
		// 転送設定 = 取得した転送区分
		this.mailAccountSetUpForm.setTransferConfigration(mailDto.getAutoTransfer());
		// 転送先メールアドレス = 取得した転送先メールアドレス
		this.mailAccountSetUpForm.setTransferMailAddress(mailDto.getTransferToEmail());
		// メールを残す = 取得したサーバ保持フラグ
		this.mailAccountSetUpForm.setLeaveMail(mailDto.getMessageDelete());
		// 自動返信設定 = 取得した自動返信区分
		this.mailAccountSetUpForm.setAutoReplyConfigration(mailDto.getAutoReply());
		// 件名 = 取得した件名
		this.mailAccountSetUpForm.setSubject(mailDto.getAutoReplySubject());
		// 本文 = 取得した自動応答の本文
		this.mailAccountSetUpForm.setText(mailDto.getAutoReplyBody());

		// TTM_DEV_11 APPLICATION_LOG BEGIN ADD
		BasicWebApiResDto logResDto = this.logRegisterLogic.registerLog(UserConst.LOG_SCREEN_CONTRACT_INFO, UserConst.LOG_BUTTON_MAIL_SETTING, CommonConst.EMPTY_STRING, ipAddress);
		if(!logResDto.isResult()){
			LogUtils log = new LogUtils(this.getClass());
			log.error(UserConst.ERROR_MESSAGE_WRITE_LOG);
		}
		// TTM_DEV_11 APPLICATION_LOG END ADD

		// メール詳細設定画面へ
		return JSP_FILE_MAIL_ACCOUNT_SETUP;
	}

	/**
	 * 入力チェック
	 *
	 * @return jspパス
	 */
	public ActionMessages check() {

		// 戻り値生成
		ActionMessages errors = new ActionMessages();

		// 転送設定
		String transferConfigration = this.mailAccountSetUpForm.getTransferConfigration();
		// 転送先メールアドレス
		String transferMailAddress = this.mailAccountSetUpForm.getTransferMailAddress();
		// 自動返信設定
		String autoReplyConfigration = this.mailAccountSetUpForm.getAutoReplyConfigration();

		// 転送設定＝1の場合
		if (StringUtil.equals(TRANSFER_CONFIGRATION_TRANSFER_MAIL, transferConfigration)) {

			// 入力値が設定されていない、または、Eメールアドレスの形式ではない場合、エラーになる。
			if (GenericValidator.isBlankOrNull(transferMailAddress)
					|| !GenericValidator.isEmail(transferMailAddress)) {

				super.addErrors(errors, UserConst.MSG_ID_ERRORS_EMAIL, MESSAGE_STRING_TRANSFER_MAIL_ADDRESS);
			}
		}

		// 自動応答設定＝１の場合のみ
		if (StringUtil.equals(AUTO_REPLY_FLAG_AUTO_REPLY, autoReplyConfigration)) {

			// 件名が設定されていない場合、
			if (GenericValidator.isBlankOrNull(this.mailAccountSetUpForm.getSubject())) {
				super.addErrors(errors, UserConst.MSG_ID_ERRORS_REQUIRED, MESSAGE_STRING_SUBJECT);
			}

			// 本文が設定されていない場合、
			if (GenericValidator.isBlankOrNull(this.mailAccountSetUpForm.getText())) {
				super.addErrors(errors, UserConst.MSG_ID_ERRORS_REQUIRED, MESSAGE_STRING_TEXT);
			}
		}
		
		return errors;
	}

	/**
	 * 更新処理
	 *
	 * @return 戻り先
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	@Execute(validator = true, validate = "check", input = JSP_FILE_MAIL_ACCOUNT_SETUP)
	public String change() throws IllegalArgumentException, IllegalAccessException {
		//TTM_DEV 20170706 BEGIN ADD
		this.mailAccountSetUpForm.trim();
		
		//TTM_DEV 20170706 END ADD
		// メールアドレス詳細設定登録API呼出処理を実行する。
		this.mailAddressDetailRegistLogic.regist(this.userSessionDto.getKkykBng(),
				this.mailAccountSetUpForm.getMailAccount(), this.mailAccountSetUpForm.getTransferConfigration(),
				this.mailAccountSetUpForm.getTransferMailAddress(), this.mailAccountSetUpForm.getLeaveMail(),
				this.mailAccountSetUpForm.getAutoReplyConfigration(), this.mailAccountSetUpForm.getSubject(),
				this.mailAccountSetUpForm.getText(),
				// TTM_DEV 20170810 BEGIN ADD
				this.mailAccountSetUpForm.getInternetConnectionId(), this.mailAccountSetUpForm.getAccountClassification());
				// TTM_DEV 20170810 BEGIN ADD
		
		// 契約情報照会画面のURLを返却する
		return JSP_FILE_CONTRACT_INFO_QUERY;
	}
}
