package jp.co.forvaltel.user.form;

import jp.co.forvaltel.common.form.AbstractForm;

/**
 * KaiyakuInfo Form
 *
 * @author TTM-DEV
 */
public class KaiyakuInfoForm extends AbstractForm {

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
	
	/**
	 * 電話料金合算へ変更可否チェック
	 */
	private boolean paymentAddingUpFlg;
	
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
	 * @param haraiKbnChange 設定する haraiKbnChange
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
	 * @param paymentAddingUpFlg 設定する paymentAddingUpFlg
	 */
	public void setPaymentAddingUpFlg(boolean paymentAddingUpFlg) {
		this.paymentAddingUpFlg = paymentAddingUpFlg;
	}


}
