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
public class AccountTransferForm extends AbstractForm {

	/**
	 * シリアルバージョンID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 請求先コード
	 */
	private String billingCode;
	/**
	 * haraiKbnChange
	 */
	private String haraiKbnChange;
	/**
	 * 支払方法確定チェック
	 */
	private boolean paymentMethodFlg;
	/**
	 * クレジット変更可否チェック
	 */
	private boolean paymentCreditFlg;
	// TTM_DEV-542 20170830 BEGIN ADD
	/**
	 * 電話料金合算へ変更可否チェック
	 */
	private boolean paymentAddingUpFlg;
	// TTM_DEV-542 20170830 END ADD

	/*
	 * (非 Javadoc)
	 *
	 * @see jp.co.forvaltel.common.form.AbstractForm#reset()
	 */
	@Override
	public void reset() {
	}
	/**
	 * @return billingCode
	 */
	public String getBillingCode() {
		return this.billingCode;
	}
	/**
	 * @param billingCode 設定する billingCode
	 */
	public void setBillingCode(String billingCode) {
		this.billingCode = billingCode;
	}
	/**
	 * @return the haraiKbnChange
	 */
	public String getHaraiKbnChange() {
		return haraiKbnChange;
	}
	/**
	 * @param haraiKbnChange the haraiKbnChange to set
	 */
	public void setHaraiKbnChange(String haraiKbnChange) {
		this.haraiKbnChange = haraiKbnChange;
	}
	/**
	 * @return paymentMethodFlg
	 */
	public boolean isPaymentMethodFlg() {
		return this.paymentMethodFlg;
	}

	/**
	 * @param paymentMethodFlg 設定する paymentMethodFlg
	 */
	public void setPaymentMethodFlg(boolean paymentMethodFlg) {
		this.paymentMethodFlg = paymentMethodFlg;
	}

	/**
	 * @return paymentCreditFlg
	 */
	public boolean isPaymentCreditFlg() {
		return this.paymentCreditFlg;
	}

	/**
	 * @param paymentCreditFlg 設定する paymentCreditFlg
	 */
	public void setPaymentCreditFlg(boolean paymentCreditFlg) {
		this.paymentCreditFlg = paymentCreditFlg;
	}
	/**
	 * @return the paymentAddingUpFlg
	 */
	public boolean isPaymentAddingUpFlg() {
		return paymentAddingUpFlg;
	}
	/**
	 * @param paymentAddingUpFlg the paymentAddingUpFlg to set
	 */
	public void setPaymentAddingUpFlg(boolean paymentAddingUpFlg) {
		this.paymentAddingUpFlg = paymentAddingUpFlg;
	}


}
