/**
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.form;

import java.lang.reflect.Field;

import org.seasar.framework.util.StringUtil;

import jp.co.forvaltel.common.annotation.FtMaxLength;
import jp.co.forvaltel.common.form.AbstractForm;
import jp.co.forvaltel.common.util.CheckUtils;
import jp.co.forvaltel.user.constant.UserConst;

/**
 * メールアカウント詳細設定機能アクションフォーム
 *
 * @author smis
 */
public class MailAccountSetUpForm extends AbstractForm {

	/**
	 * シリアルバージョンID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * メールアドレス
	 */
	private String mailAddress;
	/**
	 * 転送設定
	 */
	private String transferConfigration;
	/**
	 * 転送先メールアドレス
	 */
	@FtMaxLength(itemKey = "mail_account_set_up.label.33", max = 128)
	private String transferMailAddress;
	/**
	 * メールを残す
	 */
	private String leaveMail;
	/**
	 * 自動返信設定
	 */
	private String autoReplyConfigration;
	/**
	 * 件名
	 */
	@FtMaxLength(itemKey = "mail_account_set_up.label.42", max = 85)
	private String subject;
	/**
	 * 本文
	 */
	@FtMaxLength(itemKey = "mail_account_set_up.label.44", max = 333)
	private String text;
	/**
	 * エラーメッセージ
	 */
	private String errorMessage;
	
	/**
	 * internetConnectionId
	 */
	private String internetConnectionId;
	/**
	 * accountClassification
	 */
	private String accountClassification;
	//TTM_DEV 20170606 BEGIN END
	/**
	 * @return mailAddress
	 */
	public String getMailAddress() {
		return this.mailAddress;
	}

	/**
	 * @param mailAddress 設定する mailAddress
	 */
	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	/**
	 * @return mailAccount
	 */
	public String getMailAccount() {
		return StringUtil.substringFromLast(this.mailAddress, UserConst.MAIL_ADDRESS_MARK);
	}

	/**
	 * @return transferConfigration
	 */
	public String getTransferConfigration() {
		return this.transferConfigration;
	}

	/**
	 * @param transferConfigration 設定する transferConfigration
	 */
	public void setTransferConfigration(String transferConfigration) {
		this.transferConfigration = transferConfigration;
	}

	/**
	 * @return transferMailAddress
	 */
	public String getTransferMailAddress() {
		return this.transferMailAddress;
	}

	/**
	 * @param transferMailAddress 設定する transferMailAddress
	 */
	public void setTransferMailAddress(String transferMailAddress) {
		this.transferMailAddress = transferMailAddress;
	}

	/**
	 * @return leaveMail
	 */
	public String getLeaveMail() {
		return this.leaveMail;
	}

	/**
	 * @param leaveMail 設定する leaveMail
	 */
	public void setLeaveMail(String leaveMail) {
		this.leaveMail = leaveMail;
	}

	/**
	 * @return autoReplyConfigration
	 */
	public String getAutoReplyConfigration() {
		return this.autoReplyConfigration;
	}

	/**
	 * @param autoReplyConfigration 設定する autoReplyConfigration
	 */
	public void setAutoReplyConfigration(String autoReplyConfigration) {
		this.autoReplyConfigration = autoReplyConfigration;
	}

	/**
	 * @return subject
	 */
	public String getSubject() {
		return this.subject;
	}

	/**
	 * @param subject 設定する subject
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return text
	 */
	public String getText() {
		return this.text;
	}

	/**
	 * @param text 設定する text
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return errorMessage
	 */
	public String getErrorMessage() {
		return this.errorMessage;
	}

	/**
	 * @param errorMessage 設定する errorMessage
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
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
