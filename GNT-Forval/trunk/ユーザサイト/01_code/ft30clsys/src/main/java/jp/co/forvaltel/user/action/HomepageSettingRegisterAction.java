/**
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.action;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.util.StringUtil;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import jp.co.forvaltel.common.constant.CommonConst;
import jp.co.forvaltel.common.dto.BasicWebApiResDto;
import jp.co.forvaltel.common.dto.FtpUserDto;
import jp.co.forvaltel.common.dto.WebAccountDto;
import jp.co.forvaltel.common.util.CheckUtils;
import jp.co.forvaltel.common.util.JsonUtils;
import jp.co.forvaltel.user.constant.UserConst;
import jp.co.forvaltel.user.form.HomepageSettingRegisterForm;
import jp.co.forvaltel.user.logic.HpInfoReferenceLogic;
import jp.co.forvaltel.user.logic.HpInfoRegistLogic;
import jp.co.forvaltel.user.logic.HpPwRegistLogic;
import jp.co.forvaltel.user.logic.LogRegisterLogic;
import jp.co.forvaltel.user.util.LogUtils;

/**
 * ホームページ登録・パスワード変更機能アクション処理
 *
 * @author smis
 */
public class HomepageSettingRegisterAction extends AbstractUserAction {

	/** ホームページ設定（登録） 入力画面のURL */
	private static final String JSP_FILE_HOMEPAGE_SETTING_REGISTER = "homepageSettingRegister.jsp";

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
	/** 変更画面初期表示 */

	/** メッセージ埋め込み文字：入力されたパスワード */
	private static final String REPLACE_STR_INPUT_PASSWORD = "入力されたパスワード";

	/** メッセージ埋め込み文字：確認用パスワード */
	private static final String REPLACE_STR_CONFIRM_PASSWORD = "確認用パスワード";

	/** メッセージID：一致していません */
	private static final String MESSAGE_ID_ERRORS_MATCH = "errors.match";
	
	/** URLのヘッダ部分：http:// */
	private static final String URL_PREFIX = "http://";

	/** URLの最後部分：/Webアカウント名/ */
	private static final String URL_POSTFIX = "/Webアカウント名/";

	/** アクションフォーム */
	@Resource
	@ActionForm
	public HomepageSettingRegisterForm homepageSettingRegisterForm;

	/**
	 * ホームページ情報参照処理
	 */
	@Resource
	HpInfoReferenceLogic hpInfoReferenceLogic;

	/**
	 * ホームページ情報登録処理
	 */
	@Resource
	HpInfoRegistLogic hpInfoRegistLogic;

	/**
	 * ホームページパスワード変更処理
	 */
	@Resource
	HpPwRegistLogic hpPwRegistLogic;

	// TTM_DEV_11 APPLICATION_LOG BEGIN ADD
	@Resource
	LogRegisterLogic logRegisterLogic;
	// TTM_DEV_11 APPLICATION_LOG END ADD
	
	/**
	 * ホームページ登録・パスワード登録画面初期表示処理
	 *
	 * @return 戻り先
	 */
//	TTM_DEV 20170606 BEGIN UPDATE
//	@Execute(validator = false , input = JSP_FILE_CONTRACT_INFO_QUERY)
	@Execute(validator = false , validate = "checkRadid", input = JSP_FILE_CONTRACT_INFO_QUERY)
	public String init() {
//	TTM_DEV 20170606 BEGIN UPDATE	
		// webアカウント
		String webAccount = this.homepageSettingRegisterForm.getWebAccount();
		// get ID 
		String internetConnectionId = this.homepageSettingRegisterForm.getInternetConnectionId();
		String accountClassification = this.homepageSettingRegisterForm.getAccountClassification();
		// inyrnryVonnrvyionIF = homepageSetStringRegisterFomrm.get
		
		// 画面出力項目設定処理
		// 登録の場合
		if (StringUtils.isEmpty(webAccount)) {
			
			// 既存のホームページ設定情報を取得する。
			// 顧客番号 = アクションフォーム.顧客番号 （登録の場合、空白）
			// TTM_DEV 20170726 BEGIN UPDATE
//			BasicWebApiResDto resDto = this.hpInfoReferenceLogic.getHpInfo(CommonConst.EMPTY_STRING)
//			BasicWebApiResDto resDto = this.hpInfoReferenceLogic.getHpInfo(CommonConst.EMPTY_STRING, CommonConst.EMPTY_STRING, CommonConst.EMPTY_STRING);
			// TTM_DEV-1280 20181218 BEGIN UPDATE
			BasicWebApiResDto resDto = this.hpInfoReferenceLogic.getHpInfo(CommonConst.EMPTY_STRING, 
					internetConnectionId, accountClassification);
			// TTM_DEV-1280 20181218 END UPDATE
			// TTM_DEV 20170726 END UPDATE
			WebAccountDto returnValue = JsonUtils.decode(resDto.getReturnValue(), WebAccountDto.class);
			
			// FTPサーバー
			String ftpServer = returnValue.getFtpServerName();
			// 戻り値 ホームページ情報取得結果（0番目のレコード）を基に、以下のフォーマットでURLを生成する。
			// URL = "http://" ＋ ホームページ情報取得結果．FTPサーバ名 ＋ "/Webアカウント名/"
			String url = new StringBuffer().append(URL_PREFIX).append(ftpServer).append(URL_POSTFIX).toString();
			// アクションフォームの値を基に、以下のフォーマットでFTPユーザ名を生成する。
			// FTPアカウント = アクションフォーム．インターネット接続　の@以前の文字列を切り出し
			String ftpAccount = StringUtils.substringBefore(this.homepageSettingRegisterForm.getConnectAccount(),
					UserConst.MAIL_ADDRESS_MARK);

			// URL = 取得したホームページ設定情報のURL
			this.homepageSettingRegisterForm.setUrl(url);
			// FTPサーバー = 取得したホームページ設定情報のFTPサーバー
			this.homepageSettingRegisterForm.setFtpServer(ftpServer);
			// FTPアカウント = 取得したホームページ設定情報のFTPアカウント
			this.homepageSettingRegisterForm.setFtpAccount(ftpAccount);
			// WEBアカウント = 空文字
			this.homepageSettingRegisterForm.setWebAccount(CommonConst.EMPTY_STRING);
			// 登録・変更フラグ = 「1(登録画面）」
			this.homepageSettingRegisterForm.setJudgeFlg(JUDGE_FLG_REGISTER_INIT);
			
			// TTM_DEV_11 APPLICATION_LOG BEGIN ADD
			
			BasicWebApiResDto logResDto = this.logRegisterLogic.registerLog(UserConst.LOG_SCREEN_CONTRACT_INFO, UserConst.LOG_BUTTON_HOME_PAGE_NEW, CommonConst.EMPTY_STRING, ipAddress);
			if(!logResDto.isResult()){
				LogUtils log = new LogUtils(this.getClass());
				log.error(UserConst.ERROR_MESSAGE_WRITE_LOG);
			}
			// TTM_DEV_11 APPLICATION_LOG END ADD
			// 変更の場合
		} else {
			// 顧客番号
			String custmerNo = super.userSessionDto.getKkykBng();
			// 既存のホームページ設定情報を取得する。
			// 顧客番号 = アクションフォーム.顧客番号 （登録の場合、空白）
//			TTM_DEV HOMEPAGE 20170602 BEGIN UPDATE		
//			BasicWebApiResDto resDto = this.hpInfoReferenceLogic.getHpInfo(custmerNo)
			BasicWebApiResDto resDto = this.hpInfoReferenceLogic.getHpInfo(custmerNo,internetConnectionId,accountClassification);
//			TTM_DEV HOMEPAGE 20170602 END UPDATE
			
			WebAccountDto returnValue = JsonUtils.decode(resDto.getReturnValue(), WebAccountDto.class);

			// FTPユーザ情報
			FtpUserDto ftpUserDto = returnValue.getFtpUserDtoList().get(0);
			// URL = 取得したホームページ設定情報のURL
			this.homepageSettingRegisterForm.setUrl(ftpUserDto.getUrl());
			// FTPサーバー = 取得したホームページ設定情報のFTPサーバー
			this.homepageSettingRegisterForm.setFtpServer(returnValue.getFtpServerName());
			// FTPアカウント = 取得したホームページ設定情報のFTPアカウント
			this.homepageSettingRegisterForm.setFtpAccount(ftpUserDto.getFtpUserName());
			// WEBアカウント = アクションフォーム.WEBアカウント
			this.homepageSettingRegisterForm.setWebAccount(webAccount);
			// パスワード = 取得したホームページ設定情報のパスワード
			this.homepageSettingRegisterForm.setHomePagePassword(ftpUserDto.getWebPass());
			// 登録・変更フラグ = 「3(変更画面初期表示）」
			this.homepageSettingRegisterForm.setJudgeFlg(JUDGE_FLG_CHANGE_INIT);
			// TTM_DEV_11 APPLICATION_LOG BEGIN ADD
			BasicWebApiResDto logResDto = this.logRegisterLogic.registerLog(UserConst.LOG_SCREEN_CONTRACT_INFO, UserConst.LOG_BUTTON_HOMEPAGE_PASSWORD_CHANGE, CommonConst.EMPTY_STRING, ipAddress);
			if(!logResDto.isResult()){
				LogUtils log = new LogUtils(this.getClass());
				log.error(UserConst.ERROR_MESSAGE_WRITE_LOG);
			}
			// TTM_DEV_11 APPLICATION_LOG END ADD
		}

		// ホームページ設定（登録） 入力画面のURL
		return JSP_FILE_HOMEPAGE_SETTING_REGISTER;
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
		String password = this.homepageSettingRegisterForm.getHomePagePassword().trim();
		String passwordConfirm = this.homepageSettingRegisterForm.getHomePagePasswordConfirm().trim();
		
		
		if (!StringUtil.equals(password, passwordConfirm)) {

			String[] replaceStr = {REPLACE_STR_INPUT_PASSWORD, REPLACE_STR_CONFIRM_PASSWORD};
			super.addErrors(errors, MESSAGE_ID_ERRORS_MATCH, replaceStr);
		}
//		TTM_DEV  20170606 BEGIN ADD
		// TTM_DEV-1280 20181214 BEGIN DELETE
//		String radid = homepageSettingRegisterForm.getInternetConnectionId();
//		if(CheckUtils.isEmpty(radid)){
//			super.addErrors(errors,  UserConst.MESSAGE_ID_ERRORS_RADID);
//		}
		// TTM_DEV-1280 20181214 END DELETE
//		TTM_DEV  20170606 END ADD
		return errors;
	}
//	TTM_DEV  20170606 BEGIN ADD	
	public ActionMessages checkRadid() {

		// 戻り値生成
		ActionMessages errors = new ActionMessages();

		// パスワードと確認用パスワードが一致するチェック
		// TTM_DEV-1280 20181214 BEGIN DELETE
//		String radid = homepageSettingRegisterForm.getInternetConnectionId();
//		if(CheckUtils.isEmpty(radid)){
//			super.addErrors(errors,  UserConst.MESSAGE_ID_ERRORS_RADID);
//		}
		// TTM_DEV-1280 20181214 END DELETE
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
	@Execute(validator = true, validate = "check", input = JSP_FILE_HOMEPAGE_SETTING_REGISTER)
	public String register() throws IllegalArgumentException, IllegalAccessException {
		//TTM_DEV 20170706 BEGIN ADD
		this.homepageSettingRegisterForm.trim();
		//TTM_DEV 20170706 BEGIN ADD
		// 顧客番号
		String custmerNo = super.userSessionDto.getKkykBng();
		
		// WEBアカウント
		String webAccount = this.homepageSettingRegisterForm.getWebAccount();
		// 変更後WEBアカウントパスワード
		String webAccountPassword = this.homepageSettingRegisterForm.getHomePagePassword();

		// 登録・変更フラグ = 1(登録画面初期表示）
		this.homepageSettingRegisterForm.setJudgeFlg(JUDGE_FLG_REGISTER_INIT);
		// 内部APIのホームページパスワード変更処理を実行する。
//		TTM_DEV HOMEPAGE 20170602 BEGIN UPDATE
//		BasicWebApiResDto resDtoHp = this.hpInfoRegistLogic.regist(custmerNo, webAccount, webAccountPassword);
		String internetConnecttion = this.homepageSettingRegisterForm.getInternetConnectionId();
		String accountClassification = this.homepageSettingRegisterForm.getAccountClassification();
		BasicWebApiResDto resDtoHp = this.hpInfoRegistLogic.regist(custmerNo, webAccount, webAccountPassword,
				internetConnecttion,accountClassification);
//		TTM_DEV HOMEPAGE 20170602 END UPDATE	
		WebAccountDto webAccountDto = JsonUtils.decode(resDtoHp.getReturnValue(), WebAccountDto.class);
		this.homepageSettingRegisterForm.setFtpAccount(webAccountDto.getFtpUserDtoList().get(0).getFtpUserName());

		// 登録・変更フラグ = 「2(登録画面）」
		this.homepageSettingRegisterForm.setJudgeFlg(JUDGE_FLG_REGISTER);
		
		// TTM_DEV_11 APPLICATION_LOG BEGIN ADD
		BasicWebApiResDto logResDto = this.logRegisterLogic.registerLog(UserConst.LOG_SCREEN_HOME_REGISTER_INFO, UserConst.LOG_BUTTON_REGISTER, CommonConst.EMPTY_STRING, ipAddress);
		
		if(!logResDto.isResult()){
			LogUtils log = new LogUtils(this.getClass());
			log.error(UserConst.ERROR_MESSAGE_WRITE_LOG);
			
		}
		// TTM_DEV_11 APPLICATION_LOG END ADD
		
		// 登録完了画面へ
		return JSP_FILE_HOMEPAGE_SETTING_REGISTER;
	}

	/**
	 * 更新処理
	 *
	 * @return 戻り先
	 */
	@Execute(validator = true, validate = "check", input = JSP_FILE_HOMEPAGE_SETTING_REGISTER)
	public String changePassword() {

		// 顧客番号
		String custmerNo = super.userSessionDto.getKkykBng();
		// WEBアカウント
		String webAccount = this.homepageSettingRegisterForm.getWebAccount();
		// 変更後WEBアカウントパスワード
		String webAccountPassword = this.homepageSettingRegisterForm.getHomePagePassword().trim();

		// 登録・変更フラグ = 「2(変更画面）」
		this.homepageSettingRegisterForm.setJudgeFlg(JUDGE_FLG_CHANGE_INIT);
//		TTM_DEV HOMEPAGE 20170602 BEGIN ADD		
		String internetConnectionId = this.homepageSettingRegisterForm.getInternetConnectionId();
		String accountClassification = this.homepageSettingRegisterForm.getAccountClassification();
//		TTM_DEV HOMEPAGE 20170602 END ADD	
		// 内部APIのホームページパスワード変更処理を実行する。
//		TTM_DEV HOMEPAGE 20170602 END UPDATE
//		this.hpPwRegistLogic.regist(custmerNo, webAccount, webAccountPassword);
		this.hpPwRegistLogic.regist(custmerNo, webAccount, webAccountPassword,internetConnectionId,accountClassification);
//		TTM_DEV HOMEPAGE 20170602 END UPDATE
		// 登録・変更フラグ = 3(変更画面）
		this.homepageSettingRegisterForm.setJudgeFlg(JUDGE_FLG_CHANGE);
		// TTM_DEV_11 APPLICATION_LOG BEGIN ADD
		BasicWebApiResDto logResDto = this.logRegisterLogic.registerLog(UserConst.LOG_SCREEN_HOME_CHANGE_INFO, UserConst.LOG_BUTTON_EDIT, CommonConst.EMPTY_STRING, ipAddress);
		if(!logResDto.isResult()){
			LogUtils log = new LogUtils(this.getClass());
			log.error(UserConst.ERROR_MESSAGE_WRITE_LOG);
		}
		// TTM_DEV_11 APPLICATION_LOG END ADD
		
		// 登録完了画面へ
		return JSP_FILE_HOMEPAGE_SETTING_REGISTER;
	}
}
