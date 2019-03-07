/**
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.dto;

import java.io.Serializable;
import java.util.List;

import jp.co.forvaltel.common.entity.HcDairitenShiharaiHimoMst;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * ユーザサイト　セッション管理DTO
 *
 * @author smis
 */
@Component(instance = InstanceType.SESSION)
public class UserSessionDto implements Serializable {

	/**
	 * シリアルバージョンID
	 */
	private static final long serialVersionUID = -403932637402825475L;
	/**
	 * ユーザID
	 */
	private String userId;
	/**
	 * パスワード
	 */
	private String password;
	/**
	 * テナントＩＤ
	 */
	private String tenantId;
	/**
	 * 請求先コード
	 */
	private String seiqCd;
	/**
	 * 顧客番号
	 */
	private String kkykBng;
	/**
	 * 請求先名
	 */
	private String seiqMei;
	/**
	 * 顧客区分
	 */
	private String kokyakuKbn;
	
	// TTM_DEV-542 20170830 BEGIN ADD
	/**
	 * HcDairitenShiharaiHimoMst List
	 */
	private List<HcDairitenShiharaiHimoMst> hcDairitenShiharaiHimoMstList;
	// TTM_DEV-542 20170830 END ADD
	
	/**
	 * kaiyakuKubun Contract
	 */
	private String kaiyakuKubun;
	
	/**
	 * @return userId
	 */
	public String getUserId() {
		return this.userId;
	}

	/**
	 * @param userId 設定する userId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
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
	 * @return seiqCd
	 */
	public String getSeiqCd() {
		return this.seiqCd;
	}

	/**
	 * @param seiqCd 設定する seiqCd
	 */
	public void setSeiqCd(String seiqCd) {
		this.seiqCd = seiqCd;
	}

	/**
	 * @return kkykBng
	 */
	public String getKkykBng() {
		return this.kkykBng;
	}

	/**
	 * @param kkykBng 設定する kkykBng
	 */
	public void setKkykBng(String kkykBng) {
		this.kkykBng = kkykBng;
	}

	/**
	 * @return seiqMei
	 */
	public String getSeiqMei() {
		return this.seiqMei;
	}

	/**
	 * @param seiqMei 設定する seiqMei
	 */
	public void setSeiqMei(String seiqMei) {
		this.seiqMei = seiqMei;
	}

	/**
	 * @param kokyakuKbn 設定する kokyakuKbn
	 */
	public void setKokyakuKbn(String kokyakuKbn) {
		this.kokyakuKbn = kokyakuKbn;
	}

	/**
	 * @return kokyakuKbn
	 */
	public String getKokyakuKbn() {
		return this.kokyakuKbn;
	}

	// TTM_DEV-542 20170830 BEGIN ADD
	/**
	 * @return the hcDairitenShiharaiHimoMstList
	 */
	public List<HcDairitenShiharaiHimoMst> getHcDairitenShiharaiHimoMstList() {
		return hcDairitenShiharaiHimoMstList;
	}

	/**
	 * @param hcDairitenShiharaiHimoMstList the hcDairitenShiharaiHimoMstList to set
	 */
	public void setHcDairitenShiharaiHimoMstList(
			List<HcDairitenShiharaiHimoMst> hcDairitenShiharaiHimoMstList) {
		this.hcDairitenShiharaiHimoMstList = hcDairitenShiharaiHimoMstList;
	}
	// TTM_DEV-542 20170830 END ADD

	/**
	 * @return the kaiyakuKubun
	 */
	public String getKaiyakuKubun() {
		return kaiyakuKubun;
	}

	/**
	 * @param kaiyakuKubun the kaiyakuKubun to set
	 */
	public void setKaiyakuKubun(String kaiyakuKubun) {
		this.kaiyakuKubun = kaiyakuKubun;
	}
}
