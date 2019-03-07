/**
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.action;

import javax.annotation.Resource;

import jp.co.forvaltel.common.action.AbstractAction;
import jp.co.forvaltel.user.annotation.TokenNoCheck;
import jp.co.forvaltel.user.constant.UserConst;
import jp.co.forvaltel.user.dto.UserSessionDto;

import org.seasar.framework.aop.annotation.RemoveSession;
import org.seasar.struts.annotation.Execute;

/**
 * ユーザサイト 抽象アクション
 * 
 * @author smis
 */
public abstract class AbstractUserAction extends AbstractAction {

	/**
	 * ユーザサイトセッション情報
	 */
	@Resource
	public UserSessionDto userSessionDto;

	public String ipAddress = request.getRemoteAddr();
	
	/**
	 * ログアウト処理
	 * 
	 * @return 戻り先(ログイン画面)
	 */
	@TokenNoCheck
	@Execute(validator = false)
	@RemoveSession(name = "userSessionDto")
	public String logout() {
		// ユーザサイトセッション情報を破棄してログイン画面へ
		return UserConst.ACTION_PATH_LOGIN;
	}
}
