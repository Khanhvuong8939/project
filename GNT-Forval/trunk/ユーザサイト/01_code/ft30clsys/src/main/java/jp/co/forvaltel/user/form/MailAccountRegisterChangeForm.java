/**
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.form;

import java.lang.reflect.Field;

import jp.co.forvaltel.common.annotation.FtMask;
import jp.co.forvaltel.common.annotation.FtRequired;
import jp.co.forvaltel.common.form.AbstractForm;
import jp.co.forvaltel.common.util.CheckUtils;
import jp.co.forvaltel.user.constant.UserConst;

import org.seasar.framework.util.StringUtil;

/**
 * メールアカウント登録・変更機能アクションフォーム
 *
 * @author smis
 */
public class MailAccountRegisterChangeForm extends AbstractForm {

	/**
	 * シリアルバージョンID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * メールアドレス
	 */
	@FtRequired(itemKey = "mail_account_register_change.label.07", target = "register")
	@FtMask(regexp = "^(?![\\_\\.\\-].*)(?!.*[\\_\\.\\-]$)(?!(.*[\\_\\.\\-]+[\\_\\.\\-].*))[a-z0-9\\_\\.\\-]{4,20}$", itemKey = "mail_account_register_change.label.07", 
			target = "register")
	private String mailAddress;
	/**
	 * POP3サーバ名
	 */
	private String pop3ServerName;
	/**
	 * SMTPサーバ名
	 */
	private String smtpServerName;
	/**
	 * メールアドレスパスワード
	 */
	@FtRequired(itemKey = "mail_account_register_change.label.15")
	// TTM_DEV 20170725 BEGIN UPDATE
//	@FtMask(regexp = "^[a-zA-Z0-9][\\w\\>\\<\\(\\)\\[\\]\\#\\.\\*\\+\\-\\/\\:\\;\\=\\?\\@]{6,14}[a-zA-Z0-9]$",
//	@FtMask(regexp = "^[a-zA-Z0-9]{8,16}$",	
	@FtMask(regexp = "^[a-zA-Z0-9\\w\\>\\<\\(\\)\\[\\]\\#\\.\\*\\+\\-\\/\\:\\;\\=\\?\\@]{8,16}$",
		itemKey = "mail_account_register_change.label.15")
	// TTM_DEV 20170725 END UPDATE
	private String mailAddressPassword;
	/**
	 * メールアドレスパスワード確認
	 */
	private String mailAddressPasswordConfirm;
	/**
	 * 登録・変更判定フラグ
	 */
	private String judgeFlg;
	
	//TTM_DEV  20170529 BEGIN ADD
	/**
	 * Internet Connection Id
	 */
	private String internetConnectionId;
	/**
	 * Account Classification
	 */
	private String accountClassification;
	//TTM_DEV  20170529 END ADD
	/**
	 * @return mailAddress
	 */
	public String getMailAddress() {
		return this.mailAddress;
	}
	
	/**
	 * @return mailAccount
	 */
	public String getMailAccount() {
		return StringUtil.substringFromLast(this.mailAddress, UserConst.MAIL_ADDRESS_MARK);
	}

	/**
	 * @param mailAddress 設定する mailAddress
	 */
	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	/**
	 * @return pop3ServerName
	 */
	public String getPop3ServerName() {
		return this.pop3ServerName;
	}

	/**
	 * @param pop3ServerName 設定する pop3ServerName
	 */
	public void setPop3ServerName(String pop3ServerName) {
		this.pop3ServerName = pop3ServerName;
	}

	/**
	 * @return smtpServerName
	 */
	public String getSmtpServerName() {
		return this.smtpServerName;
	}

	/**
	 * @param smtpServerName 設定する smtpServerName
	 */
	public void setSmtpServerName(String smtpServerName) {
		this.smtpServerName = smtpServerName;
	}

	/**
	 * @return mailAddressPassword
	 */
	public String getMailAddressPassword() {
		return this.mailAddressPassword;
	}

	/**
	 * @param mailAddressPassword 設定する mailAddressPassword
	 */
	public void setMailAddressPassword(String mailAddressPassword) {
		this.mailAddressPassword = mailAddressPassword;
	}

	/**
	 * @return mailAddressPasswordConfirm
	 */
	public String getMailAddressPasswordConfirm() {
		return this.mailAddressPasswordConfirm;
	}

	/**
	 * @param mailAddressPasswordConfirm 設定する mailAddressPasswordConfirm
	 */
	public void setMailAddressPasswordConfirm(String mailAddressPasswordConfirm) {
		this.mailAddressPasswordConfirm = mailAddressPasswordConfirm;
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
