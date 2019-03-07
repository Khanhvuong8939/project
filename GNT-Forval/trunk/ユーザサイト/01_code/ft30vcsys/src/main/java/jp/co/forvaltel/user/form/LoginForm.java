/**
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.form;

import java.lang.reflect.Field;

import jp.co.forvaltel.common.annotation.FtRequired;
import jp.co.forvaltel.common.form.AbstractForm;
import jp.co.forvaltel.common.util.CheckUtils;

/**
 * ログインアクションフォーム
 * 
 * @author smis
 */
public class LoginForm extends AbstractForm {

	/**
	 * シリアルバージョンID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * ログインID
	 */
	@FtRequired(itemKey = "login.item.30")
	private String loginId;
	/**
	 * パスワード
	 */
	@FtRequired(itemKey = "login.item.33")
	private String password;
	/**
	 * version
	 */
	private String version;

	/**
	 * @return loginId
	 */
	public String getLoginId() {
		return this.loginId;
	}
	/**
	 * @param loginId 設定する loginId
	 */
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	/**
	 * @return password
	 */
	public String getPassword() {
		return this.password;
	}
	/**
	 * @param password 設定する password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return version
	 */
	public String getVersion() {
		return this.version;
	}
	/**
	 * @param version 設定する version
	 */
	public void setVersion(String version) {
		this.version = version;
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
