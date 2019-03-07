/**
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.dto;

import jp.co.forvaltel.common.dto.AbstractDto;

/**
 * 
 * @author smis
 */
public class HpInfoDto extends AbstractDto {
	/**
	 * シリアルバージョンID
	 */
	private static final long serialVersionUID = 1960432220980301869L;
	/**
	 * URL
	 */
	private String url;
	/**
	 * FTPサーバ
	 */
	private String ftpServerName;
	/**
	 * WEBアカウント
	 */
	private String webAccount;
	/**
	 * FTP接続ID
	 */
	private String ftpConnectId;
	/**
	 * パスワード
	 */
	private String webPasswd;
	/**
	 * インターネット接続ID
	 */
	private String connectAccount;
	/**
	 * @return url
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
	 * @return ftpServerName
	 */
	public String getFtpServerName() {
		return this.ftpServerName;
	}
	/**
	 * @param ftpServerName 設定する ftpServerName
	 */
	public void setFtpServerName(String ftpServerName) {
		this.ftpServerName = ftpServerName;
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
	 * @return ftpConnectId
	 */
	public String getFtpConnectId() {
		return this.ftpConnectId;
	}
	/**
	 * @param ftpConnectId 設定する ftpConnectId
	 */
	public void setFtpConnectId(String ftpConnectId) {
		this.ftpConnectId = ftpConnectId;
	}
	/**
	 * @return webPasswd
	 */
	public String getWebPasswd() {
		return this.webPasswd;
	}
	/**
	 * @param webPasswd 設定する webPasswd
	 */
	public void setWebPasswd(String webPasswd) {
		this.webPasswd = webPasswd;
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

}
