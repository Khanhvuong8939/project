/**
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.dto;

import java.util.LinkedList;
import java.util.List;

import jp.co.forvaltel.common.dto.AbstractDto;

/**
 * 
 * @author TTM
 */
//TTM_DEV USER_SITE_CONTRACT_INFO_MODULE BEGIN ADD
public class JuchKihonInfoDto extends AbstractDto {
	
	/**
	 * サービス解約中
	 * 親明細.解約日isNotNull & 親明細.解約日>=システム日付
	 */
	private boolean serviceTerminationApplied;
	/**
	 * サービス解約後
	 * 親明細.解約日isNotNull & 親明細.解約日< システム日付
	 */
	private boolean serviceTermination;
	
	/**
	 * 拡張項目
	 * 受注番号：juchNo
	 */
	private String juchNo;
	/**
	 * 拡張項目
	 * 表示区分：hyoujiKbn
	 */
	private String hyoujiKbn;
	
	/**
	 * hpInfoDto
	 */
	private HpInfoDto hpInfoDto;
	
	/**
	 * メール情報リスト
	 */
	private List<MailInfoDto> mailInfoDtoList;
	/**
	 * シリアルバージョンID
	 */
	private static final long serialVersionUID = -4837273640220674775L;
	private List<ItemInfoDto> itemInfoDtoList;
	
	/**
	 * オプション追加用パラメータ（オプション追加用）
	 * 受注番号：pjuchNo
	 */
	private String pjuchNo;
	/**
	 * オプション追加用パラメータ（オプション追加用）
	 * 受注明細番号：pJuchMeisaiNoList
	 */
	private List<String> pJuchMeisaiNoList;
	
//	TTM_DEV  20170515 BEGIN ADD
	/**
	 * オプション追加用パラメータ（オプション追加用）
	 * メール表示フラグ：mailDispFlag
	 */
	private String mailDispFlag;
	/**
	 * オプション追加用パラメータ（オプション追加用）
	 * ホームページ表示フラグ：hpDispFlag
	 */
	private String hpDispFlag;
	/**
	 * オプション追加用パラメータ（オプション追加用）
	 * インターネット接続ID：internet Connection Id
	 */
	private String internetConnectionId;
	/**
	 * オプション追加用パラメータ（オプション追加用）
	 * アカウントの分類：account Class
	 */
	private String accountClass;
//	TTM_DEV  20170515 END ADD
	
	/**
	 * TTM_DEV  20170608 Begin ADD
	 * 連携先
	 */
	private String cooperation;
	//TTM_DEV  20170608 End ADD
	//TTM_DEV BEGIN ADD 20180327
	/**
	 * kaiyakuKubun Order
	 */
	private String kaiyakuKubun;
	//TTM_DEV END ADD 20180327
	public String getMailDispFlag() {
		return mailDispFlag;
	}
	public void setMailDispFlag(String mailDispFlag) {
		this.mailDispFlag = mailDispFlag;
	}
	public String getHpDispFlag() {
		return hpDispFlag;
	}
	public void setHpDispFlag(String hpDispFlag) {
		this.hpDispFlag = hpDispFlag;
	}
	/**
	 * @return the itemInfoDtoList
	 */
	public List<ItemInfoDto> getItemInfoDtoList() {
		return itemInfoDtoList;
	}
	/**
	 * @param itemInfoDtoList the itemInfoDtoList to set
	 */
	public void setItemInfoDtoList(List<ItemInfoDto> itemInfoDtoList) {
		
		this.itemInfoDtoList = itemInfoDtoList;
	}
	/**
	 * @return the serviceTerminationApplied
	 */
	public boolean isServiceTerminationApplied() {
		return serviceTerminationApplied;
	}
	/**
	 * @param serviceTerminationApplied the serviceTerminationApplied to set
	 */
	public void setServiceTerminationApplied(boolean serviceTerminationApplied) {
		this.serviceTerminationApplied = serviceTerminationApplied;
	}
	/**
	 * @return the serviceTermination
	 */
	public boolean isServiceTermination() {
		return serviceTermination;
	}
	/**
	 * @param serviceTermination the serviceTermination to set
	 */
	public void setServiceTermination(boolean serviceTermination) {
		this.serviceTermination = serviceTermination;
	}
	/**
	 * @return the juchNo
	 */
	public String getJuchNo() {
		return juchNo;
	}
	/**
	 * @param juchNo the juchNo to set
	 */
	public void setJuchNo(String juchNo) {
		this.juchNo = juchNo;
	}
	/**
	 * @return the hyoujiKbn
	 */
	public String getHyoujiKbn() {
		return hyoujiKbn;
	}
	/**
	 * @param hyoujiKbn the hyoujiKbn to set
	 */
	public void setHyoujiKbn(String hyoujiKbn) {
		this.hyoujiKbn = hyoujiKbn;
	}
	/**
	 * @return the pjuchNo
	 */
	public String getPjuchNo() {
		return pjuchNo;
	}
	/**
	 * @param pjuchNo the pjuchNo to set
	 */
	public void setPjuchNo(String pjuchNo) {
		this.pjuchNo = pjuchNo;
	}
	/**
	 * @return the pJuchMeisaiNoList
	 */
	public List<String> getpJuchMeisaiNoList() {
		return pJuchMeisaiNoList;
	}
	/**
	 * @param pJuchMeisaiNoList the pJuchMeisaiNoList to set
	 */
	public void setpJuchMeisaiNoList(List<String> pJuchMeisaiNoList) {
		this.pJuchMeisaiNoList = pJuchMeisaiNoList;
	}
	/**
	 * @return the hpInfoDto
	 */
	public HpInfoDto getHpInfoDto() {
		return hpInfoDto;
	}
	/**
	 * @param hpInfoDto the hpInfoDto to set
	 */
	public void setHpInfoDto(HpInfoDto hpInfoDto) {
		this.hpInfoDto = hpInfoDto;
	}
	/**
	 * @return the mailInfoDtoList
	 */
	public List<MailInfoDto> getMailInfoDtoList() {
		return mailInfoDtoList;
	}
	/**
	 * @param mailInfoDtoList the mailInfoDtoList to set
	 */
	public void setMailInfoDtoList(List<MailInfoDto> mailInfoDtoList) {
		this.mailInfoDtoList = mailInfoDtoList;
	}
	public String getInternetConnectionId() {
		return internetConnectionId;
	}
	public void setInternetConnectionId(String internetConnectionId) {
		this.internetConnectionId = internetConnectionId;
	}
	public String getAccountClass() {
		return accountClass;
	}
	public void setAccountClass(String accountClass) {
		this.accountClass = accountClass;
	}
	/**
	 * @return the cooperation
	 */
	public String getCooperation() {
		return cooperation;
	}
	/**
	 * @param cooperation the cooperation to set
	 */
	public void setCooperation(String cooperation) {
		this.cooperation = cooperation;
	}
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
//TTM_DEV USER_SITE_CONTRACT_INFO_MODULE END ADD