/**
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.form;

import jp.co.forvaltel.common.form.AbstractForm;

/**
 * ログインアクションフォーム
 * 
 * @author smis
 */
public class ItemMstForm extends AbstractForm {

	/**
	 * シリアルバージョンID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 商品コード
	 */
	private String itemCd;
	/*
	 * (非 Javadoc)
	 * 
	 * @see jp.co.forvaltel.common.form.AbstractForm#reset()
	 */
	@Override
	public void reset() {
	}
	/**
	 * @return itemCd
	 */
	public String getItemCd() {
		return this.itemCd;
	}
	/**
	 * @param itemCd 設定する itemCd
	 */
	public void setItemCd(String itemCd) {
		this.itemCd = itemCd;
	}
}
