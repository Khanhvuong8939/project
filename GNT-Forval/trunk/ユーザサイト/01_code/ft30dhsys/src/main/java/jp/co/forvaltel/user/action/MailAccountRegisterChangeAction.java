/**
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.action;

import java.util.List;

import javax.annotation.Resource;

import jp.co.forvaltel.common.constant.CommonConst;
import jp.co.forvaltel.common.dto.BasicWebApiResDto;
import jp.co.forvaltel.common.dto.MailAccountDto;
import jp.co.forvaltel.common.dto.MailUserDto;
import jp.co.forvaltel.common.exception.FtSystemException;
import jp.co.forvaltel.common.util.CheckUtils;
import jp.co.forvaltel.common.util.FtResourcesUtil;
import jp.co.forvaltel.common.util.JsonUtils;
import jp.co.forvaltel.user.constant.UserConst;
import jp.co.forvaltel.user.form.MailAccountRegisterChangeForm;
import jp.co.forvaltel.user.logic.LogRegisterLogic;
import jp.co.forvaltel.user.logic.MailAccountReferenceLogic;
import jp.co.forvaltel.user.logic.MailAccountRegistLogic;
import jp.co.forvaltel.user.logic.MailPwRegistLogic;
import jp.co.forvaltel.user.util.LogUtils;

import org.apache.struts.action.ActionMessages;
import org.seasar.framework.util.StringUtil;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

/**
 * メールアカウント登録・変更機能アクション処理
 *
 * @author smis
 */
public class MailAccountRegisterChangeAction extends AbstractUserAction {

	/** メールアカウント登録画面のURL */
	private static final String JSP_FILE_MAIL_ACCOUNT_REGISTER_CHANGE = "mailAccountRegisterChange.jsp";

	/** JSPファイル名（契約情報照会画面) */
	private static final String JSP_FILE_CONTRACT_INFO_QUERY = "/contractInfoQuery";

	/** 登録画面初期表示 */
	private static final String JUDGE_FLG_REGISTER_INIT = "1";

	/** 登録画面 */
	private static final String JUDGE_FLG_REGISTER = "2";

	/** 変更画面初期表示 */
	private static final String JUDGE_FLG_CHANGE_INIT = "3";

	/** 変更画面 */
	private static final String JUDGE_FLG_CHANGE = "4";

	/** メッセージ埋め込み文字：入力されたパスワード */
	private static final String REPLACE_STR_INPUT_PASSWORD = "入力されたパスワード";

	/** メッセージ埋め込み文字：確認用パスワード */
	private static final String REPLACE_STR_CONFIRM_PASSWORD = "確認用パスワード";

	/** メールアドレスドメイン部 */
	private static final String KEY_MAIL_ADDRESS_DOMAIN = "mail_account_register_change.label.10";
	
	/** アクションフォーム */
	@Resource
	@ActionForm
	public MailAccountRegisterChangeForm mailAccountRegisterChangeForm;

	/**
	 * メールアカウント情報参照処理
	 */
	@Resource
	MailAccountReferenceLogic mailAccountReferenceLogic;

	/**
	 * メールアカウント情報登録
	 */
	@Resource
	MailAccountRegistLogic mailAccountRegistLogic;

	/**
	 * メールパスワード変更
	 */
	@Resource
	MailPwRegistLogic mailPwRegistLogic;

	// TTM_DEV_11 APPLICATION_LOG BEGIN ADD
	@Resource
	LogRegisterLogic logRegisterLogic;
	// TTM_DEV_11 APPLICATION_LOG END ADD

	/**
	 * メールアカウント登録画面初期表示処理
	 *
	 * @return 戻り先
	 */
	@Execute(validator = false, validate = "checkRadid" ,  input = JSP_FILE_CONTRACT_INFO_QUERY)
	public String registerInit() {
		
		// メールアカウント情報参照API呼出処理を実行する。
		// 顧客番号 = なし（顧客番号）
		// TTM_DEV 20170726 BEGIN UPDATE
		String internetConnectionId = this.mailAccountRegisterChangeForm.getInternetConnectionId();
//			BasicWebApiResDto resDto = this.mailAccountReferenceLogic.getMailAccountInfo(CommonConst.EMPTY_STRING);
//			BasicWebApiResDto resDto = this.mailAccountReferenceLogic.getMailAccountInfo(CommonConst.EMPTY_STRING, CommonConst.EMPTY_STRING, CommonConst.EMPTY_STRING);
		BasicWebApiResDto resDto = this.mailAccountReferenceLogic.getMailAccountInfo(CommonConst.EMPTY_STRING, 
				internetConnectionId, UserConst.ACCOUNT_CLASS_RAD_ID);
		// TTM_DEV 20170726 END UPDATE
		
		MailAccountDto mailDto = JsonUtils.decode(resDto.getReturnValue(), MailAccountDto.class);
		
		// 画面出力項目設定処理
		// POP3サーバ名 = 取得したメール情報リストレスポンス.POP3
		this.mailAccountRegisterChangeForm.setPop3ServerName(mailDto.getPopServerName());
		// SMTPサーバ名 = 取得したメール情報リストレスポンス.SMTP
		this.mailAccountRegisterChangeForm.setSmtpServerName(mailDto.getSmtpServerName());
		// 登録・変更フラグ = 「1(登録画面）」
		this.mailAccountRegisterChangeForm.setJudgeFlg(JUDGE_FLG_REGISTER_INIT);

		// TTM_DEV_11 APPLICATION_LOG BEGIN ADD
		BasicWebApiResDto logResDto = this.logRegisterLogic.registerLog(UserConst.LOG_SCREEN_CONTRACT_INFO, UserConst.LOG_BUTTON_MAIL_REGISTER, CommonConst.EMPTY_STRING, ipAddress);
		if(!logResDto.isResult()){
			LogUtils log = new LogUtils(this.getClass());
			log.error(UserConst.ERROR_MESSAGE_WRITE_LOG);
		}
		// TTM_DEV_11 APPLICATION_LOG END ADD
		
		// メールアカウント登録画面のURLを返却する。
		return JSP_FILE_MAIL_ACCOUNT_REGISTER_CHANGE;
	}

	/**
	 * メールパスワード変更画面初期表示処理
	 *
	 * @return 戻り先
	 */
	@Execute(validator = false, validate = "checkRadid" , input = JSP_FILE_CONTRACT_INFO_QUERY)
	public String changeInit() {

		// 前画面より取得した以下のフォーム情報を取得する。・メールアドレス
		String mailAddress = this.mailAccountRegisterChangeForm.getMailAddress();

		//TTM_DEV 20170606 BEGIN UPDATE
		String internetConnectionId = mailAccountRegisterChangeForm.getInternetConnectionId();
		String accountClassification = mailAccountRegisterChangeForm.getAccountClassification();
		
		// メールアカウント情報参照API呼出処理を実行する。
		// 顧客番号 = セッション.顧客番号
//		BasicWebApiResDto resDto = this.mailAccountReferenceLogic.getMailAccountInfo(this.userSessionDto.getKkykBng());
		BasicWebApiResDto resDto = this.mailAccountReferenceLogic.getMailAccountInfo(this.userSessionDto.getKkykBng(),internetConnectionId,accountClassification);
		MailAccountDto mailDto = JsonUtils.decode(resDto.getReturnValue(), MailAccountDto.class);

		// 取得しメールアドレスとメールアカウント情報参照APIから取得したメールアカウント情報取得結果.メールアドレスと
		// 一致するメールアドレスが存在するか確認を行う。
		List<MailUserDto> mailUserList = mailDto.getMailUserList();
		boolean isMatched = false;
		for (MailUserDto mailUserDto : mailUserList) {
			if (StringUtil.equals(mailAddress, mailUserDto.getMailAddr())) {
				isMatched = true;
				break;
			}
		}

		// 一致するメールアドレスが存在しない場合、「3.エラー処理」へ。
		if (!isMatched) {
			throw new FtSystemException(CommonConst.EMPTY_STRING);
		}

		// 画面出力項目設定処理
		// メールアドレス = 取得したメールアドレス　※前画面より取得
		this.mailAccountRegisterChangeForm.setMailAddress(mailAddress);
		// POP3サーバ名 = 取得したメール情報リストレスポンス.POP3
		this.mailAccountRegisterChangeForm.setPop3ServerName(mailDto.getPopServerName());
		// SMTPサーバ名 = 取得したメール情報リストレスポンス.SMTP
		this.mailAccountRegisterChangeForm.setSmtpServerName(mailDto.getSmtpServerName());

		// 登録・変更フラグ = 「3(変更初期画面）」
		this.mailAccountRegisterChangeForm.setJudgeFlg(JUDGE_FLG_CHANGE_INIT);

		// TTM_DEV_11 APPLICATION_LOG BEGIN ADD
		BasicWebApiResDto logResDto = this.logRegisterLogic.registerLog(UserConst.LOG_SCREEN_CONTRACT_INFO, UserConst.LOG_BUTTON_MAIL_PASSWORD_CHANGE, CommonConst.EMPTY_STRING, ipAddress);
		if(!logResDto.isResult()){
			LogUtils log = new LogUtils(this.getClass());
			log.error(UserConst.ERROR_MESSAGE_WRITE_LOG);
		}
		// TTM_DEV_11 APPLICATION_LOG END ADD
		
		// 登録画面へ
		return JSP_FILE_MAIL_ACCOUNT_REGISTER_CHANGE;
	}

	/**
	 * 入力チェック
	 *
	 * @return jspパス
	 */
	public ActionMessages check() {

		// 戻り値生成
		ActionMessages errors = new ActionMessages();

		// パスワードと確認用パスワードが一致するチェック
		String password = this.mailAccountRegisterChangeForm.getMailAddressPassword().trim();
		String passwordConfirm = this.mailAccountRegisterChangeForm.getMailAddressPasswordConfirm().trim();
		if (!StringUtil.equals(password, passwordConfirm)) {

			String[] replaceStr = {REPLACE_STR_INPUT_PASSWORD, REPLACE_STR_CONFIRM_PASSWORD };
			super.addErrors(errors, UserConst.MSG_ID_ERRORS_MATCH, replaceStr);
		}
//		TTM_DEV  20170606 BEGIN ADD
		String radid = mailAccountRegisterChangeForm.getInternetConnectionId();
		if(CheckUtils.isEmpty(radid)){
			super.addErrors(errors, UserConst.MESSAGE_ID_ERRORS_RADID);
		}
//		TTM_DEV  20170606 END ADD
		
		return errors;
	}
//	TTM_DEV  20170606 BEGIN ADD	
	public ActionMessages checkRadid() {
		// 戻り値生成
		ActionMessages errors = new ActionMessages();

		// パスワードと確認用パスワードが一致するチェック
		String radid = mailAccountRegisterChangeForm.getInternetConnectionId();
		if(CheckUtils.isEmpty(radid)){
			super.addErrors(errors,  UserConst.MESSAGE_ID_ERRORS_RADID);
		}
		return errors;
	}
//	TTM_DEV  20170606 END ADD
	/**
	 * 登録処理
	 *
	 * @return 戻り先
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	@Execute(validator = true, validate = "check", input = JSP_FILE_MAIL_ACCOUNT_REGISTER_CHANGE)
	public String register() throws IllegalArgumentException, IllegalAccessException {
		//TTM_DEV 20170706 BEGIN ADD 
		this.mailAccountRegisterChangeForm.trim();
		//TTM_DEV 20170706 END ADD
		// 登録・変更フラグ = 「1(登録初期画面）」
		this.mailAccountRegisterChangeForm.setJudgeFlg(JUDGE_FLG_REGISTER_INIT);
		
		// 完了画面用変数 
		String mailAddress = CommonConst.EMPTY_STRING;
		
		// メールアカウント登録API呼出処理
		this.mailAccountRegistLogic.regist(this.userSessionDto.getKkykBng(),
				this.mailAccountRegisterChangeForm.getMailAccount(),
				this.mailAccountRegisterChangeForm.getMailAddressPassword(),
				this.mailAccountRegisterChangeForm.getInternetConnectionId(),
				this.mailAccountRegisterChangeForm.getAccountClassification());
		
			mailAddress = this.mailAccountRegisterChangeForm.getMailAddress()
					+ FtResourcesUtil.getItemValue(KEY_MAIL_ADDRESS_DOMAIN);
	
		// メールアカウント情報設定
		// メールアドレス = アクションフォーム.メールアドレス
//		this.mailAccountRegisterChangeForm.setMailAddress(this.mailAccountRegisterChangeForm.getMailAddress()
//				+ FtResourcesUtil.getItemValue(KEY_MAIL_ADDRESS_DOMAIN));
		this.mailAccountRegisterChangeForm.setMailAddress(mailAddress);
		// POP3サーバ名 = アクションフォーム.POP3
		this.mailAccountRegisterChangeForm.setPop3ServerName(this.mailAccountRegisterChangeForm.getPop3ServerName());
		// SMTPサーバ名 = アクションフォーム.SMTP
		this.mailAccountRegisterChangeForm.setSmtpServerName(this.mailAccountRegisterChangeForm.getSmtpServerName());
		// パスワード = アクションフォーム.パスワード
		this.mailAccountRegisterChangeForm.setMailAddressPassword(this.mailAccountRegisterChangeForm
				.getMailAddressPassword());

		// 登録・変更フラグ = 「2(登録画面）」
		this.mailAccountRegisterChangeForm.setJudgeFlg(JUDGE_FLG_REGISTER);
		
		// TTM_DEV_11 APPLICATION_LOG BEGIN ADD
		BasicWebApiResDto logResDto = this.logRegisterLogic.registerLog(UserConst.LOG_SCREEN_MAIL_REGISTER_INFO, UserConst.LOG_BUTTON_REGISTER, CommonConst.EMPTY_STRING, ipAddress);
		if(!logResDto.isResult()){
			LogUtils log = new LogUtils(this.getClass());
			log.error(UserConst.ERROR_MESSAGE_WRITE_LOG);
		}
		// TTM_DEV_11 APPLICATION_LOG END ADD

		// 登録完了画面へ
		return JSP_FILE_MAIL_ACCOUNT_REGISTER_CHANGE;
	}

	/**
	 * 更新処理
	 *
	 * @return 戻り先
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	@Execute(validator = true, validate = "check", input = JSP_FILE_MAIL_ACCOUNT_REGISTER_CHANGE)
	public String change() throws IllegalArgumentException, IllegalAccessException {
		//TTM_DEV 20170706 BEGIN ADD 
		this.mailAccountRegisterChangeForm.trim();
		//TTM_DEV 20170706 END ADD
		// 登録・変更フラグ = 「3(変更初期画面）」
		this.mailAccountRegisterChangeForm.setJudgeFlg(JUDGE_FLG_CHANGE_INIT);

//		TTM_DEV 20170530 BEGIN UPDATE
		// メールパスワード変更API呼出処理
//		this.mailPwRegistLogic.regist(this.userSessionDto.getKkykBng(),
//		this.mailAccountRegisterChangeForm.getMailAccount(),
//		this.mailAccountRegisterChangeForm.getMailAddressPassword());
		this.mailPwRegistLogic.regist(this.userSessionDto.getKkykBng(),
				this.mailAccountRegisterChangeForm.getMailAccount(),
				this.mailAccountRegisterChangeForm.getMailAddressPassword(),
				this.mailAccountRegisterChangeForm.getInternetConnectionId(),
				this.mailAccountRegisterChangeForm.getAccountClassification());
//		TTM_DEV 20170530 END UPDATE	
		
		// メールアカウント情報設定
		// メールアドレス = アクションフォーム.メールアドレス
		this.mailAccountRegisterChangeForm.setMailAddress(this.mailAccountRegisterChangeForm.getMailAddress());
		// POP3サーバ名 = アクションフォーム.POP3
		this.mailAccountRegisterChangeForm.setPop3ServerName(this.mailAccountRegisterChangeForm.getPop3ServerName());
		// SMTPサーバ名 = アクションフォーム.SMTP
		this.mailAccountRegisterChangeForm.setSmtpServerName(this.mailAccountRegisterChangeForm.getSmtpServerName());
		// パスワード = アクションフォーム.パスワード
		this.mailAccountRegisterChangeForm.setMailAddressPassword(this.mailAccountRegisterChangeForm
				.getMailAddressPassword());

		// 登録・変更フラグ = 「4(変更画面）」
		this.mailAccountRegisterChangeForm.setJudgeFlg(JUDGE_FLG_CHANGE);

		// TTM_DEV_11 APPLICATION_LOG BEGIN ADD
		BasicWebApiResDto logResDto = this.logRegisterLogic.registerLog(UserConst.LOG_SCREEN_MAIL_CHANGE_INFO, UserConst.LOG_BUTTON_EDIT, CommonConst.EMPTY_STRING, ipAddress);
		if(!logResDto.isResult()){
			LogUtils log = new LogUtils(this.getClass());
			log.error(UserConst.ERROR_MESSAGE_WRITE_LOG);
		}
		// TTM_DEV_11 APPLICATION_LOG END ADD
		
		// 登録完了画面へ
		return JSP_FILE_MAIL_ACCOUNT_REGISTER_CHANGE;
	}
}
