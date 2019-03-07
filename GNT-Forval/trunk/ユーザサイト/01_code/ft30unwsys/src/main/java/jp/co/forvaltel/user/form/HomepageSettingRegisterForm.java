/**
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.form;

import java.lang.reflect.Field;

import jp.co.forvaltel.common.annotation.FtMask;
import jp.co.forvaltel.common.annotation.FtRequired;
import jp.co.forvaltel.common.form.AbstractForm;
import jp.co.forvaltel.common.util.CheckUtils;

/**
 * ホームページ登録・パスワード変更機能アクションフォーム
 *
 * @author smis
 */
public class HomepageSettingRegisterForm extends AbstractForm {

	/**
	 * シリアルバージョンID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * URL
	 */
	private String url;
	/**
	 * FTPサーバー
	 */
	private String ftpServer;
	/**
	 * WEBアカウント
	 */
	@FtRequired(itemKey = "homepage_setting_register.label.52", target = "register")
	@FtMask(regexp = "^[a-zA-Z0-9]{4,20}$", itemKey = "homepage_setting_register.label.52", target = "register")
	private String webAccount;
	/**
	 * FTPアカウント
	 */
	private String ftpAccount;
	/**
	 * ホームページパスワード
	 */
	@FtRequired(itemKey = "homepage_setting_register.label.71")
	// TTM_DEV 20170725 BEGIN UPDATE
//	@FtMask(regexp = "^[a-zA-Z0-9][\\w\\>\\<\\(\\)\\[\\]\\#\\.\\*\\+\\-\\/\\:\\;\\=\\?\\@]{6,14}[a-zA-Z0-9]$",
	@FtMask(regexp = "^[a-zA-Z0-9\\w\\>\\<\\(\\)\\[\\]\\#\\.\\*\\+\\-\\/\\:\\;\\=\\?\\@]{8,16}$",
			itemKey = "homepage_setting_register.label.71")
	// TTM_DEV 20170725 END UPDATE
	private String homePagePassword;
	/**
	 * ホームページパスワード(確認)
	 */
	private String homePagePasswordConfirm;
	/**
	 * 接続アカウント
	 */
	private String connectAccount;
	/**
	 * 顧客番号
	 */
	private String custmerNo;
	/**
	 * 登録・変更判定フラグ
	 */
	private String judgeFlg;
//	TTM_DEV  20170529 BEGIN ADD
	/**
	 * internetConnectionId
	 */
	private String internetConnectionId ;
	/**
	 * accountClassification
	 */
	private String accountClassification ;
//	TTM_DEV  20170529 END ADD
	/**
	 * @return URL
	 */
	public String getUrl() {
		return this.url;
	}

	/**
	 * @param url 設定する url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return ftpServer
	 */
	public String getFtpServer() {
		return this.ftpServer;
	}

	/**
	 * @param ftpServer 設定する ftpServer
	 */
	public void setFtpServer(String ftpServer) {
		this.ftpServer = ftpServer;
	}

	/**
	 * @return webAccount
	 */
	public String getWebAccount() {
		return this.webAccount;
	}

	/**
	 * @param webAccount 設定する webAccount
	 */
	public void setWebAccount(String webAccount) {
		this.webAccount = webAccount;
	}

	/**
	 * @return ftpAccount
	 */
	public String getFtpAccount() {
		return this.ftpAccount;
	}

	/**
	 * @param ftpAccount 設定する ftpAccount
	 */
	public void setFtpAccount(String ftpAccount) {
		this.ftpAccount = ftpAccount;
	}

	/**
	 * @return homePagePassword
	 */
	public String getHomePagePassword() {
		return this.homePagePassword;
	}

	/**
	 * @param homePagePassword 設定する homePagePassword
	 */
	public void setHomePagePassword(String homePagePassword) {
		this.homePagePassword = homePagePassword;
	}

	/**
	 * @return homePagePasswordConfirm
	 */
	public String getHomePagePasswordConfirm() {
		return this.homePagePasswordConfirm;
	}

	/**
	 * @param homePagePasswordConfirm 設定する homePagePasswordConfirm
	 */
	public void setHomePagePasswordConfirm(String homePagePasswordConfirm) {
		this.homePagePasswordConfirm = homePagePasswordConfirm;
	}

	/**
	 * @return connectAccount
	 */
	public String getConnectAccount() {
		return this.connectAccount;
	}

	/**
	 * @param connectAccount 設定する connectAccount
	 */
	public void setConnectAccount(String connectAccount) {
		this.connectAccount = connectAccount;
	}

	/**
	 * @return custmerNo
	 */
	public String getCustmerNo() {
		return this.custmerNo;
	}

	/**
	 * @param custmerNo 設定する custmerNo
	 */
	public void setCustmerNo(String custmerNo) {
		this.custmerNo = custmerNo;
	}

	/**
	 * @return judgeFlg
	 */
	public String getJudgeFlg() {
		return this.judgeFlg;
	}

	/**
	 * @param judgeFlg 設定する judgeFlg
	 */
	public void setJudgeFlg(String judgeFlg) {
		this.judgeFlg = judgeFlg;
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see jp.co.forvaltel.common.form.AbstractForm#reset()
	 */
	@Override
	public void reset() {
	}

	/**
	 * @return the internetConnectionId
	 */
	public String getInternetConnectionId() {
		return internetConnectionId;
	}

	/**
	 * @param internetConnectionId the internetConnectionId to set
	 */
	public void setInternetConnectionId(String internetConnectionId) {
		this.internetConnectionId = internetConnectionId;
	}

	/**
	 * @return the accountClassification
	 */
	public String getAccountClassification() {
		return accountClassification;
	}

	/**
	 * @param accountClassification the accountClassification to set
	 */
	public void setAccountClassification(String accountClassification) {
		this.accountClassification = accountClassification;
	}
	/**
	 * 
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	//TTM_DEV 20170706 BEGIN ADD
	public void trim() throws IllegalArgumentException, IllegalAccessException {
		Class<?> c = this.getClass();
		Field[] fields = c.getDeclaredFields();
		for (Field field : fields) {
			if(field.getType().isAssignableFrom(String.class)){
				boolean accessible = field.isAccessible();
				field.set(this, CheckUtils.trimSpace((String)field.get(this)));
				field.setAccessible(accessible);
			}
		}
	}
	//TTM_DEV 20170706 END ADD
}
