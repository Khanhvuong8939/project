/*
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import jp.co.forvaltel.common.constant.CommonConst;
import jp.co.forvaltel.common.exception.FtAuthenticationException;
import jp.co.forvaltel.common.interceptor.AbstractActionInterceptor;
import jp.co.forvaltel.user.action.ContractInfoQueryAction;
import jp.co.forvaltel.user.action.LoginAction;
import jp.co.forvaltel.user.annotation.TokenNoCheck;
import jp.co.forvaltel.user.constant.UserConst;
import jp.co.forvaltel.user.form.ContractInfoQueryForm;
import jp.co.forvaltel.user.util.LogUtils;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.struts.util.TokenProcessor;
import org.seasar.struts.util.RequestUtil;

/**
 * @author smis
 *
 */
public class UserActionInterceptor extends AbstractActionInterceptor {

	/**
	 * デフォルトシリアルバージョンID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Action共通前処理
	 */
	@Override
	protected Object beforeExecute(MethodInvocation invocation) throws FtAuthenticationException {
		// デバックログ出力
		LogUtils logUtils = new LogUtils(invocation.getThis().getClass());
		StringBuffer sb = new StringBuffer();
		sb.append("[START]");
		sb.append(CommonConst.H_COMMA);
		sb.append(invocation.getThis().getClass().getName());
		sb.append(CommonConst.H_COMMA);
		sb.append(invocation.getMethod().getName());
		logUtils.debug(sb.toString());

		// Sessionチェック
		HttpServletRequest request = RequestUtil.getRequest();
		HttpSession session = request.getSession();
		Object sessionObj = session.getAttribute(CommonConst.S_KEY_SYSTEM_SESSION_INFO);
		if (sessionObj == null && !(invocation.getThis() instanceof LoginAction)) {
			session.setAttribute(UserConst.S_KEY_SESSION_ERROR_MSG, true);
			return UserConst.ACTION_PATH_LOGIN;
		}

		// トークンチェック
		if (super.isExecuteMethod(invocation) && !super.isAjaxMethod(invocation)
				&& !this.isTokenNoCheckMethod(invocation)) {
			// トークンチェック対象の場合、
			if (!TokenProcessor.getInstance().isTokenValid(RequestUtil.getRequest(), false)) {
				return "/error/authentication_error.jsp";
			}
		}

		// 契約情報照会以外であれば契約情報照会のActionForm上のバナー情報を消す
		if (!(invocation.getThis() instanceof ContractInfoQueryAction) || !super.isAjaxMethod(invocation)) {
			// Form取得
			ContractInfoQueryForm form = (ContractInfoQueryForm) session.getAttribute("contractInfoQueryForm");
			if (form != null) {
				// クリア
				form.setBannerUrlLink(CommonConst.EMPTY_STRING);
				form.setBannerImgLink(CommonConst.EMPTY_STRING);
			}
		}

		return null;
	}

	/**
	 * Action共通後処理
	 */
	@Override
	protected boolean afterExecute(MethodInvocation invocation, Object returnOblect) {
		// ExceuteかつAjax以外ならトークン更新
		if (super.isExecuteMethod(invocation) && !super.isAjaxMethod(invocation) && this.isJspPath(returnOblect)) {
			// トークンSave
			TokenProcessor.getInstance().saveToken(RequestUtil.getRequest());
		}

		// デバックログ出力
		LogUtils logUtils = new LogUtils(invocation.getThis().getClass());
		StringBuffer sb = new StringBuffer();
		sb.append("[END]");
		sb.append(CommonConst.H_COMMA);
		sb.append(invocation.getThis().getClass().getName());
		sb.append(CommonConst.H_COMMA);
		sb.append(invocation.getMethod().getName());
		logUtils.debug(sb.toString());
		return true;
	}

	/**
	 * エラー時 ログ出力処理
	 */
	@Override
	protected void outputLog(Class<?> targetClass, String message, Throwable exception) {
		// ログ出力
		LogUtils logUtils = new LogUtils(targetClass);
		logUtils.error(message, exception);
	}

	/**
	 * TokenNoCheckアノテーション付加メソッド判定
	 * 
	 * @param invocation 実行情報
	 * @return boolean True:TokenNoCheckアノテーションメソッド/false:TokenNoCheckアノテーション以外
	 */
	protected Boolean isTokenNoCheckMethod(MethodInvocation invocation) {
		// 戻り値初期化
		boolean ret = false;
		// ActionかつTokenNoCheckアノテーションを保持している場合、戻り値をTrueに設定
		if (invocation.getMethod().isAnnotationPresent(TokenNoCheck.class)) {
			ret = true;
		}
		return ret;
	}

	/**
	 * TokenNoCheckアノテーション付加メソッド判定
	 * 
	 * @param obj aaa
	 * @return boolean True:TokenNoCheckアノテーションメソッド/false:TokenNoCheckアノテーション以外
	 */
	protected Boolean isJspPath(Object obj) {
		// 戻り値初期化
		boolean ret = false;

		if (obj == null || !(obj instanceof String)) {
			return ret;
		}

		// 文字列に変換
		String objStr = obj.toString();
		// 最後の4文字取得
		String last4Str = "";
		if (objStr.length() > 3) {
			last4Str = objStr.substring(objStr.length() - 4, objStr.length());
		}

		if (".jsp".equals(last4Str)) {
			ret = true;
		}

		return ret;
	}

}
