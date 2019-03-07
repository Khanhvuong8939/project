/**
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.form;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import jp.co.forvaltel.common.annotation.FtByteLength;
import jp.co.forvaltel.common.annotation.FtDateType;
import jp.co.forvaltel.common.annotation.FtHolderName;
import jp.co.forvaltel.common.annotation.FtMaxLength;
import jp.co.forvaltel.common.annotation.FtNumeric;
import jp.co.forvaltel.common.annotation.FtRequired;
import jp.co.forvaltel.common.constant.CommonConst;
import jp.co.forvaltel.common.form.AbstractForm;
import jp.co.forvaltel.common.util.CheckUtils;

/**
 * 支払先情報登録（NTT合算）アクションフォーム
 *
 * @author tanaka
 */

public class PaymentCreditRegisterForm extends AbstractForm  {

	/**
	 * シリアルバージョンID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * テナントID
	 */
	private String tenantId;

	/**
	 * ユーザコード
	 */
	private String userCd;

	/**
	 * パスワード
	 */
	private String password;

	/**
	 * 顧客番号
	 */
	private String customerNumber;

	/**
	 * 請求先コード
	 */
	private String billingCode;

	/**
	 *  カード会社リスト
	 */
	public List<LabelValueBean> cardNameList = new ArrayList<LabelValueBean>();

	/**
	 *  カード会社名
	 */
	private String cardLabel;

	/**
	 *  カード会社(登録用)
	 */
	@FtRequired(itemKey = "payment_credit_register.label.77")
	private String cardName;

	/**
	 *  カード番号(入力内容)
	 */
	private String cardNo1;
	private String cardNo2;
	private String cardNo3;
	private String cardNo4;

	/**
	 *  カード番号(登録用)
	 */
	@FtRequired(itemKey = "payment_credit_register.label.81")
	@FtNumeric(itemKey = "payment_credit_register.label.81")
	@FtByteLength(itemKey = "payment_credit_register.label.81", length = 16)
	private String cardNo;

	/**
	 *  有効期限(入力内容)
	 */
	private String expire1;
	private String expire2;

	/**
	 *  有効期限(登録用)
	 */
	@FtRequired(itemKey = "payment_credit_register.label.91")
	@FtNumeric(itemKey = "payment_credit_register.label.91")
	// トークン方式対応 20170302 BEGIN UPDATE
	@FtDateType(itemKey = "payment_credit_register.label.91", datePatternStrict = "yyMM")
	// トークン方式対応 20170302 END UPDATE
	private String expire;

	/**
	 *  名義人名
	 */
	@FtRequired(itemKey = "payment_credit_register.label.101")
	@FtHolderName(itemKey = "payment_credit_register.label.101")
	@FtMaxLength(itemKey = "payment_credit_register.label.101", max = 30)
	private String holder;

	/**
	 *  名義人名(登録用)
	 */
	private String holderName;

	/**
	 * 支払方法確定チェック
	 */
	private boolean paymentMethodFlg;
	
	// TTM_DEV-542 20170830 BEGIN ADD
	/**
	 * 電話料金合算へ変更可否チェック
	 */
	private boolean paymentAddingUpFlg;
	/**
	 * 銀行口座振替(引落し)へ変更可否チェック
	 */
	private boolean paymentTransferFlg;
	// TTM_DEV-542 20170830 END ADD
	
//TTM_DEV 20170621 ADD BEGIN
	/**
	 * checkToken
	 */
	private String checkToken;
	/**
	 * disPlayFlgBtnBack
	 */
	private boolean disPlayFlgBtnBack;
//TTM_DEV 20170621 ADD END
	/**
	 * @return cardName
	 */
	public String getCardLabel() {
		return cardLabel;
	}

	/**
	 * @param cardLabel
	 */
	public void setCardLabel(String cardLabel) {
		this.cardLabel = cardLabel;
	}

	/**
	 * @return cardName
	 */
	public String getCardName() {
		return cardName;
	}

	/**
	 * @param cardName
	 */
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	/**
	 * @return cardNo
	 */
	public String getCardNo() {
		return cardNo;
	}

	/**
	 * @param cardNo
	 */
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	/**
	 * @return cardNo1
	 */
	public String getCardNo1() {
		return cardNo1;
	}

	/**
	 * @param cardNo1
	 */
	public void setCardNo1(String cardNo1) {
		this.cardNo1 = cardNo1;
	}

	/**
	 * @return cardNo2
	 */
	public String getCardNo2() {
		return cardNo2;
	}

	/**
	 * @param cardNo2
	 */
	public void setCardNo2(String cardNo2) {
		this.cardNo2 = cardNo2;
	}

	/**
	 * @return cardNo3
	 */
	public String getCardNo3() {
		return cardNo3;
	}

	/**
	 * @param cardNo3
	 */
	public void setCardNo3(String cardNo3) {
		this.cardNo3 = cardNo3;
	}

	/**
	 * @return cardNo4
	 */
	public String getCardNo4() {
		return cardNo4;
	}

	/**
	 * @param cardNo4
	 */
	public void setCardNo4(String cardNo4) {
		this.cardNo4 = cardNo4;
	}

	/**
	 * @return expire
	 */
	public String getExpire() {
		return expire;
	}

	/**
	 * @param expire
	 */
	public void setExpire(String expire) {
		this.expire = expire;
	}

	/**
	 * @return expire1
	 */
	public String getExpire1() {
		return expire1;
	}

	/**
	 * @param expire1
	 */
	public void setExpire1(String expire1) {
		this.expire1 = expire1;
	}

	/**
	 * @return expire2
	 */
	public String getExpire2() {
		return expire2;
	}

	/**
	 * @param expire2
	 */
	public void setExpire2(String expire2) {
		this.expire2 = expire2;
	}

	/**
	 * @return holderName
	 */
	public String getHolderName() {
		return holderName;
	}

	/**
	 * @param holderName
	 */
	public void setHolderName(String holderName) {
		this.holderName = holderName;
	}

	@Override
	public void reset() {
		// 各処理の初期化実行
		this.cardName = CommonConst.EMPTY_STRING;
		this.cardNo = CommonConst.EMPTY_STRING;
		this.expire = CommonConst.EMPTY_STRING;
		this.holderName = CommonConst.EMPTY_STRING;
		// トークン方式対応 20170302 BEGIN ADD
		this.securityCd = CommonConst.EMPTY_STRING;
		this.token = CommonConst.EMPTY_STRING;
		this.disPlayFlgBtnBack = false;
		// トークン方式対応 20170302 BEGIN ADD
	}

	/**
	 * return tenantId
	 */
	public String getTenantId() {
		return tenantId;
	}
	/**
	 * @param tenantId
	 */
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	/**
	 * @return userCd
	 */
	public String getUserCd() {
		return userCd;
	}
	/**
	 * @param userCd
	 */
	public void setUserCd(String userCd) {
		this.userCd = userCd;
	}
	/**
	 * @return password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return customerNumber
	 */
	public String getCustomerNumber() {
		return customerNumber;
	}
	/**
	 * @param customerNumber
	 */
	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	public String getBillingCode() {
		return billingCode;
	}

	public void setBillingCode(String billingCode) {
		this.billingCode = billingCode;
	}

	public List<LabelValueBean> getCardNameList() {
		return cardNameList;
	}

	public void setCardNameList(List<LabelValueBean> cardNameList) {
		this.cardNameList = cardNameList;
	}

	public String getHolder() {
		return holder;
	}

	public void setHolder(String holder) {
		this.holder = holder;
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

	// TTM_DEV-542 20170830 BEGIN ADD
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

	// トークン方式対応 20170302 BEGIN ADD
	/**
	  * トークン
	  */
	private String token;

	/**
	  * セキュリティコード
	  */
	@FtRequired(itemKey = "payment_credit_register.label.111")
	@FtNumeric(itemKey = "payment_credit_register.label.111")
	@FtMaxLength(itemKey = "payment_credit_register.label.111", max = 4)
	private String securityCd;

	/**
	 * @return token
	 */
	public String getToken() {
	    return token;
	}

	/**
	 * @param token
	 */
	public void setToken(String token) {
	    this.token = token;
	}

	/**
	 * @return securityCd
	 */
	public String getSecurityCd() {
	    return securityCd;
	}

	/**
	 * @param securityCd
	 */
	public void setSecurityCd(String securityCd) {
	    this.securityCd = securityCd;
	}
	// トークン方式対応 20170302 END ADD

	/**
	 * @return the checkToken
	 */
	public String getCheckToken() {
		return checkToken;
	}

	/**
	 * @param checkToken the checkToken to set
	 */
	public void setCheckToken(String checkToken) {
		this.checkToken = checkToken;
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
	/**
	 * 
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	//TTM_DEV 20170706 BEGIN ADD
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
	//TTM_DEV 20170706 END ADD
}