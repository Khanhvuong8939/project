/**
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.form;

import java.util.List;
import java.util.Map;

import jp.co.forvaltel.common.annotation.FtRequired;
import jp.co.forvaltel.common.dto.OrderRegisterKakuchouDto;
import jp.co.forvaltel.common.form.AbstractForm;

/**
 * ログインアクションフォーム
 * 
 * @author smis
 */
public class AddOptionForm extends AbstractForm {

	/**
	 * シリアルバージョンID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 商品コード
	 */
	private String pShohinCd;
	/**
	 * 確認チェックボックス
	 */
	private String confirm;
	/**
	 * 受注番号
	 */
	private String pJuchNo;
	/**
	 * 受注明細番号
	 */
	private String pJuchMeisaiNo;
	//TTM_DEV ORDER_MODULE 20170529 BEGIN ADD
	/**
	 * 受注番号
	 */
	private String kJuchNo;
	
	/**
	 * 受注明細番号
	 */
	private String kJuchMeisaiNo;
	//TTM_DEV ORDER_MODULE 20170529 END ADD
	/**
	 * 商品コード
	 */
	@FtRequired(itemKey = "add_option.label.40")
	private String shohinCd;
	/**
	 * 商品コードリスト
	 */
	private List<Map<String, Object>> itemCodeList;
	/**
	 * 表示区分
	 */
	private String hyoujiKbn;
	/**
	 * 拡張項目DTOリスト
	 */
	private List<OrderRegisterKakuchouDto> kakuchouDtoList;
	/**
	 * 表示制御フラグ
	 */
	private String displayFlg;
	//TTM_DEV 20171506 ADD BEGIN
	/**
	 * gamen shikibetsu
	 */
	private String screen;
	//TTM_DEV 20171506 ADD END
	/*
	 * (非 Javadoc)
	 * 
	 * @see jp.co.forvaltel.common.form.AbstractForm#reset()
	 */
	@Override
	public void reset() {
	}
	/**
	 * @return pShohinCd
	 */
	public String getPShohinCd() {
		return this.pShohinCd;
	}
	/**
	 * @param pShohinCd 設定する pShohinCd
	 */
	public void setpShohinCd(String pShohinCd) {
		this.pShohinCd = pShohinCd;
	}
	/**
	 * @return confirm
	 */
	public String getConfirm() {
		return this.confirm;
	}
	/**
	 * @param confirm 設定する confirm
	 */
	public void setConfirm(String confirm) {
		this.confirm = confirm;
	}
	/**
	 * @return pJuchNo
	 */
	public String getpJuchNo() {
		return this.pJuchNo;
	}

	/**
	 * @param pJuchNo 設定する pJuchNo
	 */
	public void setpJuchNo(String pJuchNo) {
		this.pJuchNo = pJuchNo;
	}
	/**
	 * @return itemCodeList
	 */
	public List<Map<String, Object>> getItemCodeList() {
		return this.itemCodeList;
	}
	/**
	 * @param itemCodeList 設定する itemCodeList
	 */
	public void setItemCodeList(List<Map<String, Object>> itemCodeList) {
		this.itemCodeList = itemCodeList;
	}
	/**
	 * @return pJuchMeisaiNo
	 */
	public String getpJuchMeisaiNo() {
		return this.pJuchMeisaiNo;
	}
	/**
	 * @param pJuchMeisaiNo 設定する pJuchMeisaiNo
	 */
	public void setpJuchMeisaiNo(String pJuchMeisaiNo) {
		this.pJuchMeisaiNo = pJuchMeisaiNo;
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
	 * @return kakuchouDtoList
	 */
	public List<OrderRegisterKakuchouDto> getKakuchouDtoList() {
		return this.kakuchouDtoList;
	}
	/**
	 * @param kakuchouDtoList 設定する kakuchouDtoList
	 */
	public void setKakuchouDtoList(List<OrderRegisterKakuchouDto> kakuchouDtoList) {
		this.kakuchouDtoList = kakuchouDtoList;
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
	 * @return displayFlg
	 */
	public String getDisplayFlg() {
		return this.displayFlg;
	}
	/**
	 * @param displayFlg 設定する displayFlg
	 */	
	public void setDisplayFlg(String displayFlg) {
		this.displayFlg = displayFlg;
	}
	public String getkJuchNo() {
		return kJuchNo;
	}
	public void setkJuchNo(String kJuchNo) {
		this.kJuchNo = kJuchNo;
	}
	public String getkJuchMeisaiNo() {
		return kJuchMeisaiNo;
	}
	public void setkJuchMeisaiNo(String kJuchMeisaiNo) {
		this.kJuchMeisaiNo = kJuchMeisaiNo;
	}
	/**
	 * @return the screen
	 */
	public String getScreen() {
		return screen;
	}
	/**
	 * @param screen the screen to set
	 */
	public void setScreen(String screen) {
		this.screen = screen;
	}
}
