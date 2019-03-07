/**
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.form;

import java.lang.reflect.Field;

import jp.co.forvaltel.common.annotation.FtAlphaNumeric;
import jp.co.forvaltel.common.annotation.FtFullKana;
import jp.co.forvaltel.common.annotation.FtFullString;
import jp.co.forvaltel.common.annotation.FtMaxLength;
import jp.co.forvaltel.common.annotation.FtRequired;
import jp.co.forvaltel.common.constant.CommonConst;
import jp.co.forvaltel.common.dto.OrderRegisterKakuchouDto;
import jp.co.forvaltel.common.form.AbstractForm;
import jp.co.forvaltel.common.util.CheckUtils;

/**
 * 支払先情報登録（NTT合算）アクションフォーム
 *
 * @author smis
 */
public class PaymentAddingUpRegisterForm extends AbstractForm {

	/**
	 * シリアルバージョンID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 請求まとめ先ID
	 */
	@FtRequired(itemKey = "payment_adding_upregiste.label.100", target = "confirmationInit")
	@FtAlphaNumeric(itemKey = "payment_adding_upregiste.label.100", target = "confirmationInit")
	@FtMaxLength(itemKey = "payment_adding_upregiste.label.100", max = 11, target = "confirmationInit")
	private String agencyCd;
	/**
	 * 回線名義人名
	 */
	@FtRequired(itemKey = "payment_adding_upregiste.label.170", target = "confirmationInit")
	@FtFullString(itemKey = "payment_adding_upregiste.label.170", target = "confirmationInit")
	@FtMaxLength(itemKey = "payment_adding_upregiste.label.170", max = 40, target = "confirmationInit")
	private String holderName;
	/**
	 * 回線名義人名カナ
	 */
	@FtRequired(itemKey = "payment_adding_upregiste.label.220", target = "confirmationInit")
	@FtFullKana(itemKey = "payment_adding_upregiste.label.220", target = "confirmationInit")
	@FtMaxLength(itemKey = "payment_adding_upregiste.label.220", max = 80, target = "confirmationInit")
	private String holderNameKana;
	/**
	 * 利用規約同意チェック
	 */
	@FtRequired(itemKey = "payment_adding_upregiste.label.600", target = "confirmationInit")
	private boolean doneFlg;
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
	 * 銀行口座振替(引落し)へ変更可否チェック
	 */
	private boolean paymentTransferFlg;
	// TTM_DEV-542 20170830 END ADD
	//TTM_DEV 20170630 ADD BEGIN
	/**
	 * disPlayFlgBtnBack
	 */
	private boolean disPlayFlgBtnBack;
	
	//TTM_DEV 20170630 ADD END
	/**
	 * @return agencyCd
	 */
	public String getAgencyCd() {
		return this.agencyCd;
	}

	/**
	 * @param agencyCd 設定する agencyCd
	 */
	public void setAgencyCd(String agencyCd) {
		this.agencyCd = agencyCd;
	}

	/**
	 * @return holderName
	 */
	public String getHolderName() {
		return this.holderName;
	}

	/**
	 * @param holderName 設定する holderName
	 */
	public void setHolderName(String holderName) {
		this.holderName = holderName;
	}

	/**
	 * @return holderNameKana
	 */
	public String getHolderNameKana() {
		return this.holderNameKana;
	}

	/**
	 * @param holderNameKana 設定する holderNameKana
	 */
	public void setHolderNameKana(String holderNameKana) {
		this.holderNameKana = holderNameKana;
	}

	/**
	 * @return doneFlg
	 */
	public boolean isDoneFlg() {
		return this.doneFlg;
	}

	/**
	 * @param doneFlg 設定する doneFlg
	 */
	public void setDoneFlg(boolean doneFlg) {
		this.doneFlg = doneFlg;
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

	// TTM_DEV-542 20170830 BEGIN ADD
	/**
	 * @return the paymentTransferFlg
	 */
	public boolean isPaymentTransferFlg() {
		return paymentTransferFlg;
	}

	/**
	 * @param paymentTransferFlg the paymentTransferFlg to set
	 */
	public void setPaymentTransferFlg(boolean paymentTransferFlg) {
		this.paymentTransferFlg = paymentTransferFlg;
	}
	// TTM_DEV-542 20170830 END ADD

	/*
	 * (非 Javadoc)
	 *
	 * @see jp.co.forvaltel.common.form.AbstractForm#reset()
	 */
	@Override
	public void reset() {
		// 各処理の初期化実行
		this.agencyCd = CommonConst.EMPTY_STRING;
		this.holderName = CommonConst.EMPTY_STRING;
		this.holderNameKana = CommonConst.EMPTY_STRING;
		this.doneFlg = false;
		this.paymentMethodFlg = false;
		this.setDisPlayFlgBtnBack(false);
	}
	//TTM_DEV 20170706 BEGIN ADD
	/** 
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
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
	
	/**
	 * @return the disPlayFlgBtnBack
	 */
	public boolean isDisPlayFlgBtnBack() {
		return disPlayFlgBtnBack;
	}

	/**
	 * @param disPlayFlgBtnBack the disPlayFlgBtnBack to set
	 */
	public void setDisPlayFlgBtnBack(boolean disPlayFlgBtnBack) {
		this.disPlayFlgBtnBack = disPlayFlgBtnBack;
	}
	//TTM_DEV 20170706 END ADD
}
