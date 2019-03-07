/**
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.dto;

import java.io.Serializable;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * ユーザサイト　セッション管理DTO
 *
 * @author smis
 */
@Component(instance = InstanceType.SESSION)
public class TokenDto implements Serializable {

	/**
	 * シリアルバージョンID
	 */
	private static final long serialVersionUID = -403932637402825475L;
	
	/**
	 * Token
	 */
	private String token;
	
	/**
	 * テナントＩＤ
	 */
	private String tenantId;
	
	/**
	 * BPM Instance ID
	 */
	private String bpmId;

	/**
	 * @return the bpmId
	 */
	public String getBpmId() {
		return bpmId;
	}

	/**
	 * @param bpmId the bpmId to set
	 */
	public void setBpmId(String bpmId) {
		this.bpmId = bpmId;
	}

	/**
	 * @return tenantId
	 */
	public String getTenantId() {
		return this.tenantId;
	}

	/**
	 * @param tenantId 設定する tenantId
	 */
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

}
