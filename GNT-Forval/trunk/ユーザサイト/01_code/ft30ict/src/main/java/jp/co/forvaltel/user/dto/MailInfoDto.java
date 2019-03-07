/**
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.dto;

import jp.co.forvaltel.common.dto.AbstractDto;
import jp.co.forvaltel.user.constant.UserConst;

/**
 * 
 * @author smis
 */
public class MailInfoDto extends AbstractDto {
	/**
	 * シリアルバージョンID
	 */
	private static final long serialVersionUID = 8376107308810690572L;
	/**
	 * メールアドレス
	 */
	private String mailAddr;
	/**
	 * メールパスワード
	 */
	private String mailPass;
	/**
	 * POPサーバ名
	 */
	private String popServerName;
	/**
	 * SMTPサーバ名
	 */
	private String smtpServerName;
	/**
	 * @return mailAddr
	 */
	public String getMailAddr() {
		return this.mailAddr;
	}
	/**
	 * @param mailAddr 設定する mailAddr
	 */
	public void setMailAddr(String mailAddr) {
		this.mailAddr = mailAddr;
	}
	/**
	 * @return mailPass
	 */
	public String getMailPass() {
		return this.mailPass;
	}
	/**
	 * @param mailPass 設定する mailPass
	 */
	public void setMailPass(String mailPass) {
		this.mailPass = mailPass;
	}
	/**
	 * @return popServerName
	 */
	public String getPopServerName() {
		return this.popServerName;
	}
	/**
	 * @param popServerName 設定する popServerName
	 */
	public void setPopServerName(String popServerName) {
		this.popServerName = popServerName;
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
	 * メールアドレスからメールアカウントを生成します。<br>
	 * "@"マークより左を切り出します。
	 * @return mailAccount
	 */
	public String getMailAccount() {
		return this.mailAddr.substring(0, this.mailAddr.indexOf(UserConst.MAIL_ADDRESS_MARK));
	}

}
