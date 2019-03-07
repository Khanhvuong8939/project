/*
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.util;

import org.apache.log4j.Logger;
import org.seasar.framework.container.SingletonS2Container;

import jp.co.forvaltel.common.constant.CommonConst;
import jp.co.forvaltel.common.util.CheckUtils;
import jp.co.forvaltel.user.dto.TokenDto;

/**
 * ログユーティリティクラス
 * 
 * @author smis
 */
public class LogUtils {

	/**
	 * ロガー
	 */
	private Logger logger;

	/**
	 * コンストラクタ
	 * 
	 * @param clazz クラス
	 */
	public LogUtils(Class<?> clazz) {
		// ロガーのインスタンス生成
		this.logger = Logger.getLogger(clazz);
	}

	/**
	 * エラーログ出力
	 * 
	 * @param message 出力メッセージ
	 * @param exception 例外情報
	 */
	public void error(String message, Throwable exception) {
		// ログ出力
		this.logger.error(this.createLogMessage(message), exception);
	}

	/**
	 * エラーログ出力
	 * 
	 * @param message 出力メッセージ
	 */
	public void error(String message) {
		// ログ出力
		this.logger.error(this.createLogMessage(message));
	}

	/**
	 * 警告ログ出力
	 * 
	 * @param message 出力メッセージ
	 */
	public void warn(String message) {
		// ログ出力
		this.logger.warn(this.createLogMessage(message));
	}

	/**
	 * infoログ出力
	 * 
	 * @param message 出力メッセージ
	 */
	public void info(String message) {
		// ログ出力
		this.logger.info(this.createLogMessage(message));
	}

	/**
	 * デバックログ出力
	 * 
	 * @param message 出力メッセージ
	 */
	public void debug(String message) {
		// ログ出力
		this.logger.debug(this.createLogMessage(message));
	}

	/**
	 * ログ出力文字列生成
	 * 
	 * @param message 出力メッセージ
	 * @return ログ出力文字列
	 */
	public String createLogMessage(String message) {
		// セッション情報取得
		TokenDto dto = SingletonS2Container.getComponent(TokenDto.class);
		String tenantId = CommonConst.H_HYPHEN;
		if (dto != null) {
			if (!CheckUtils.isNullOrEmpty(dto.getTenantId())) {
				tenantId = dto.getTenantId();
			}
		}

		// ログ文字列生成
		StringBuffer sb = new StringBuffer();
		sb.append(tenantId);
		sb.append(CommonConst.H_SPACE);
		sb.append(message);
		return sb.toString();
	}
	
	public Boolean hasDebug() {
		return this.logger.isDebugEnabled();
	}
}
