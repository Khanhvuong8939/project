/**
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.dto;

import jp.co.forvaltel.common.dto.AbstractDto;

/**
 * 
 * @author smis
 */
public class ItemInfoDto extends AbstractDto {
	/**
	 * シリアルバージョンID
	 */
	private static final long serialVersionUID = -4837273640220674775L;
	/**
	 * 受注番号
	 */
	private String juchNo;
	/**
	 * 受注明細番号
	 */
	private String juchMeisaiNo;
	/**
	 * 表示区分
	 */
	private String hyoujiKbn;
	/**
	 * サービスコード
	 */
	private String srvCd;
	/**
	 * サービス名
	 */
	private String srvName;
	/**
	 * 商品コード
	 */
	private String shohinCd;
	/**
	 * 商品名
	 */
	private String shohinName;
	/**
	 * 解約日
	 */
	private String kaiyakuDate;
	/**
	 * kaiyakuKubun Option
	 */
	private String kaiyakuKubun;

	// 判定用
	/**
	 * 子明細（オプション）
	 */
	private boolean optionFlag;
	/**
	 * 基幹連携受信日
	 */
	private String kikanJusinDate;
	/**
	 * 基幹連携送信日
	 */
	private String kikanSousinDate;
	/**
	 * オプション解約申請中
	 */
	private boolean opTerminationApplied;
	/**
	 * オプション解約後
	 */
	private boolean opTermination;
	/**
	 * サービス解約中
	 */
	private boolean serviceTerminationApplied;

	/**
	 * @return juchNo
	 */
	public String getJuchNo() {
		return this.juchNo;
	}
	/**
	 * @param juchNo 設定する juchNo
	 */
	public void setJuchNo(String juchNo) {
		this.juchNo = juchNo;
	}
	/**
	 * @return juchMeisaiNo
	 */
	public String getJuchMeisaiNo() {
		return this.juchMeisaiNo;
	}
	/**
	 * @param juchMeisaiNo 設定する juchMeisaiNo
	 */
	public void setJuchMeisaiNo(String juchMeisaiNo) {
		this.juchMeisaiNo = juchMeisaiNo;
	}
	/**
	 * @return hyoujiKbn
	 */
	public String getHyoujiKbn() {
		return this.hyoujiKbn;
	}
	/**
	 * @param hyoujiKbn 設定する hyoujiKbn
	 */
	public void setHyoujiKbn(String hyoujiKbn) {
		this.hyoujiKbn = hyoujiKbn;
	}
	/**
	 * @return srvCd
	 */
	public String getSrvCd() {
		return this.srvCd;
	}
	/**
	 * @param srvCd 設定する srvCd
	 */
	public void setSrvCd(String srvCd) {
		this.srvCd = srvCd;
	}
	/**
	 * @return srvName
	 */
	public String getSrvName() {
		return this.srvName;
	}
	/**
	 * @param srvName 設定する srvName
	 */
	public void setSrvName(String srvName) {
		this.srvName = srvName;
	}
	/**
	 * @return shohinCd
	 */
	public String getShohinCd() {
		return this.shohinCd;
	}
	/**
	 * @param shohinCd 設定する shohinCd
	 */
	public void setShohinCd(String shohinCd) {
		this.shohinCd = shohinCd;
	}
	/**
	 * @return shohinName
	 */
	public String getShohinName() {
		return this.shohinName;
	}
	/**
	 * @param shohinName 設定する shohinName
	 */
	public void setShohinName(String shohinName) {
		this.shohinName = shohinName;
	}
	/**
	 * @return kaiyakuDate
	 */
	public String getKaiyakuDate() {
		return this.kaiyakuDate;
	}
	/**
	 * @param kaiyakuDate 設定する kaiyakuDate
	 */
	public void setKaiyakuDate(String kaiyakuDate) {
		this.kaiyakuDate = kaiyakuDate;
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
	/**
	 * @return optionFlag
	 */
	public boolean isOptionFlag() {
		return this.optionFlag;
	}
	/**
	 * @param optionFlag 設定する optionFlag
	 */
	public void setOptionFlag(boolean optionFlag) {
		this.optionFlag = optionFlag;
	}
	/**
	 * @return kikanJusinDate
	 */
	public String getKikanJusinDate() {
		return this.kikanJusinDate;
	}
	/**
	 * @param kikanJusinDate 設定する kikanJusinDate
	 */
	public void setKikanJusinDate(String kikanJusinDate) {
		this.kikanJusinDate = kikanJusinDate;
	}
	/**
	 * @return kikanSousinDate
	 */
	public String getKikanSousinDate() {
		return this.kikanSousinDate;
	}
	/**
	 * @param kikanSousinDate 設定する kikanSousinDate
	 */
	public void setKikanSousinDate(String kikanSousinDate) {
		this.kikanSousinDate = kikanSousinDate;
	}
	/**
	 * @return opTerminationApplied
	 */
	public boolean isOpTerminationApplied() {
		return this.opTerminationApplied;
	}
	/**
	 * @param opTerminationApplied 設定する opTerminationApplied
	 */
	public void setOpTerminationApplied(boolean opTerminationApplied) {
		this.opTerminationApplied = opTerminationApplied;
	}
	/**
	 * @return opTermination
	 */
	public boolean isOpTermination() {
		return this.opTermination;
	}
	/**
	 * @param opTermination 設定する opTermination
	 */
	public void setOpTermination(boolean opTermination) {
		this.opTermination = opTermination;
	}
	/**
	 * @return serviceTerminationApplied
	 */
	public boolean isServiceTerminationApplied() {
		return this.serviceTerminationApplied;
	}
	/**
	 * @param serviceTerminationApplied 設定する serviceTerminationApplied
	 */
	public void setServiceTerminationApplied(boolean serviceTerminationApplied) {
		this.serviceTerminationApplied = serviceTerminationApplied;
	}

}
