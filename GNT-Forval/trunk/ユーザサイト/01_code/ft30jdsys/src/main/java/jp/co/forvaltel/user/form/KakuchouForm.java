/*
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.form;

import jp.co.forvaltel.common.form.AbstractForm;

/**
 * 受注商品アクションフォーム
 * 
 * @author smis
 */
public class KakuchouForm extends AbstractForm {

	/**
	 * シリアルバージョンID
	 */
	private static final long serialVersionUID = 2905780877115860752L;
	/**
	 * 受注番号
	 */
	private String juchNo;
	/**
	 * 受注明細番号
	 */
	private String juchMeisaiNo;
	/**
	 * サービスコード
	 */
	private String srvCd;
	/**
	 * 商品コード
	 */
	private String shohinCd;
	/**
	 * 表示区分
	 */
	private String hyoujiKbn;
	//TTM_DEV 20171506 ADD BEGIN
	/**
	 * gamen subetsu
	 */
	private String screen;
	//TTM_DEV 20171506 ADD END
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

	
	/*
	 * (非 Javadoc)
	 * 
	 * @see jp.co.forvaltel.common.form.AbstractForm#reset()
	 */
	@Override
	public void reset() {
		// 各項目の値を初期化
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
