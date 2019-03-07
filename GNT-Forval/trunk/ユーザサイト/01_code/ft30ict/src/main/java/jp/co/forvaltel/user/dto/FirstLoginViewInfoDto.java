/**
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 初回ログイン表示情報DTO
 * 
 * @author smis
 */
public class FirstLoginViewInfoDto implements Serializable {

	/**
	 * シリアルバージョンID
	 */
	private static final long serialVersionUID = -8046984815570247563L;

	/**
	 * サービス名
	 */
	public String srvName;
	/**
	 * インターネット接続ID ラベル
	 */
	public String authAccountLabel;
	/**
	 * インターネット接続ID 値
	 */
	public String authAccountValue;
	/**
	 * インターネット接続パスワード ラベル
	 */
	public String authPwLabel;
	/**
	 * インターネット接続パスワード 値
	 */
	public String authPwValue;
	/**
	 * 初回請求起算日 ラベル
	 */
	public String initialClaimValueDateLabel;
	/**
	 * 初回請求起算日 値
	 */
	public String initialClaimValueDateValue;
	/**
	 * ご利用料金 ラベル
	 */
	public String usageFeeLabel;
	/**
	 * ご利用料金 値
	 */
	public String usageFeeValue;
	// TTM_DEV USER_SITE_FIRST_MODULE BEGIN ADD
	/**
	 * firstLoginDtoList
	 */
	public List<FirstLoginInfoDto> firstLoginDtoList;
	// TTM_DEV USER_SITE_FIRST_MODULE END ADD
}


