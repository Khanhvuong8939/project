package jp.co.forvaltel.user.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;
import org.seasar.framework.beans.util.BeanUtil;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ActionMessagesUtil;

import jp.co.forvaltel.common.constant.CodeMstConst;
import jp.co.forvaltel.common.constant.CommonConst;
import jp.co.forvaltel.common.dto.BasicWebApiResDto;
import jp.co.forvaltel.common.dto.PayeeReferenceResDto;
import jp.co.forvaltel.common.entity.HcCodeMst;
import jp.co.forvaltel.common.entity.HcDairitenShiharaiHimoMst;
import jp.co.forvaltel.common.util.FtResourcesUtil;
import jp.co.forvaltel.user.constant.UserConst;
import jp.co.forvaltel.user.form.PaymentCreditRegisterForm;
import jp.co.forvaltel.user.logic.CodeMstLogic;
import jp.co.forvaltel.user.logic.LogRegisterLogic;
import jp.co.forvaltel.user.logic.PayeeReferenceLogic;
import jp.co.forvaltel.user.logic.PayeeUpdateLogic;
import jp.co.forvaltel.user.util.LogUtils;


public class PaymentCreditRegisterAction extends AbstractUserAction {


	/** アクションフォーム */
	@Resource
	@ActionForm
	public PaymentCreditRegisterForm paymentCreditRegisterForm;

	/**
	 * 支払先情報取得
	 */
	@Resource
	PayeeReferenceLogic payeeReferenceLogic;

	/**
	 * カード会社取得
	 */
	@Resource
	CodeMstLogic codeMstLogic;

	/**
	 * 支払先情報更新
	 */
	@Resource
	PayeeUpdateLogic payeeUpdateLogic;

	// TTM_DEV_11 APPLICATION_LOG BEGIN ADD
	@Resource
	LogRegisterLogic logRegisterLogic;
	// TTM_DEV_11 APPLICATION_LOG END ADD
	
	/** JSPファイル名（入力） */
	private static final String JSP_FILE_INPUT = "input.jsp";
	/** JSPファイル名（確認） */
	private static final String JSP_FILE_CONFIRM = "confirm.jsp";
	/** JSPファイル名（完了） */
	private static final String JSP_FILE_FINISH = "finish.jsp";
	/** アクションパス 契約情報照会画面 */
	private static final String ACTION_PATH_CONTRACT_INFO_QUERY = "/contractInfoQuery";

	/** カード会社リスト */
	public List<LabelValueBean> cardNameList;

	/** エラー時のセッション制御フラグ */
	private boolean errFlag;

	/**
	 * 初期表示（入力）
	 *
	 * @return 戻り先
	 */
	@Execute(validator = false, input = JSP_FILE_INPUT)
	public String index() {
		// 戻り先設定
		String retPath = JSP_FILE_INPUT;

		// セレクトボックスに表示するカード会社、カード会社コードを取得
		List<HcCodeMst> cardCompanyCdList = codeMstLogic.getList(CodeMstConst.ID_CARD_KAISYA);
		cardNameList = new ArrayList<LabelValueBean>();
		LabelValueBean bean;
		// 先頭に空白行を入れる
		bean = new LabelValueBean("","");
		cardNameList.add(bean);
		for(HcCodeMst codeMst : cardCompanyCdList){
			bean = new LabelValueBean(codeMst.codeMei,codeMst.code);
			cardNameList.add(bean);
		}

		// 既存Form取得
		Object sessionObject = super.session.getAttribute(UserConst.S_KEY_PAYMENT_CREDIT_REGISTER);
		PaymentCreditRegisterForm sessionForm = (PaymentCreditRegisterForm) sessionObject;

		// 既存Formの存在チェック
		if (sessionForm == null) {
			// // 存在しない場合、初期表示処理
			// 請求先コードは契約情報照会画面から持ってくる。
			String seiqCd = super.userSessionDto.getSeiqCd();
			String kkykBng = super.userSessionDto.getKkykBng();

			// 内部API呼び出し
			BasicWebApiResDto resDto = this.payeeReferenceLogic.getPayee(seiqCd, kkykBng);
			if (resDto.isResult()) {
				// 正常終了時
				PayeeReferenceResDto payeeReferenceResDto = (PayeeReferenceResDto)resDto.getReturnValue();
				// Formに値を設定
				this.paymentCreditRegisterForm.setBillingCode(seiqCd);
				this.paymentCreditRegisterForm.setCustomerNumber(kkykBng);
				String cardName = StringUtils.isEmpty(payeeReferenceResDto.getCardName())?
						paymentCreditRegisterForm.getCardName():payeeReferenceResDto.getCardName();
				this.paymentCreditRegisterForm.setCardName(cardName);
				this.paymentCreditRegisterForm.setCardNo(payeeReferenceResDto.getCardNo());
				this.paymentCreditRegisterForm.setExpire(payeeReferenceResDto.getExpire());
				this.paymentCreditRegisterForm.setHolderName(payeeReferenceResDto.getHolderName());
				// TTM_DEV-96 20170807 BEGIN UPDATE
//				this.paymentCreditRegisterForm.setPaymentMethodFlg(
//						UserConst.HARAI_HOUHOU_KAKUTEI_FLAG_DEFINED.equals(
//								payeeReferenceResDto.getHaraiHouhouKakuteiFlag()));
				this.paymentCreditRegisterForm.setPaymentMethodFlg(true);
				if(!UserConst.HARAI_HOUHOU_KAKUTEI_FLAG_DEFINED.equals(payeeReferenceResDto.getHaraiHouhouKakuteiFlag())
						&& (CodeMstConst.HARAI_KBN_CREDIT.equals(payeeReferenceResDto.getHaraiKbn()) 
								|| CodeMstConst.HARAI_KBN_NTTADDUP.equals(payeeReferenceResDto.getHaraiKbn()))){
					this.paymentCreditRegisterForm.setPaymentMethodFlg(false);
				}
				// TTM_DEV-96 20170807 END UPDATE
				
				// TTM_DEV-542 20170830 BEGIN ADD
				this.paymentCreditRegisterForm.setPaymentAddingUpFlg(false);
				this.paymentCreditRegisterForm.setPaymentTransferFlg(false);
				for (HcDairitenShiharaiHimoMst entity : this.userSessionDto.getHcDairitenShiharaiHimoMstList()){
					if (CodeMstConst.HARAI_KBN_FILE_NTT_ADDING_UP.equals(entity.shiaraiKbn)){
						this.paymentCreditRegisterForm.setPaymentAddingUpFlg(true);
					}
					if (CodeMstConst.HARAI_KBN_ACOUNT.equals(entity.shiaraiKbn)){
						this.paymentCreditRegisterForm.setPaymentTransferFlg(true);
					}
				}
				// TTM_DEV-542 20170830 END ADD
				
				//TTM_DEV 201706310	ADD BEGIN
				this.paymentCreditRegisterForm.setDisPlayFlgBtnBack(payeeReferenceResDto.isDisPlayFlgBtnBack());
				//TTM_DEV 201706310	ADD END
			} else {
				// 異常終了時 エラー処理
				retPath = ACTION_PATH_CONTRACT_INFO_QUERY;
				//TTM_DEV BEGIN EDIT 20180312
				//String tel = FtResourcesUtil.getItemValue("system.error.tel");
				//String mail = FtResourcesUtil.getItemValue("system.error.mail");
				ActionMessages errors = new ActionMessages();
				//this.addErrors(errors, "errors.system",tel,mail);
				this.addErrors(errors, "errors.system");
				//TTM_DEV END EDIT 20180312
				
				ActionMessagesUtil.addErrors(super.request, errors);
			}

		} else {
			// 存在する場合、セッション上のForm、アクションフォームにコピー
			BeanUtil.copyProperties(sessionForm, this.paymentCreditRegisterForm);
			// SessionからForm情報を削除
			super.session.removeAttribute(UserConst.S_KEY_PAYMENT_CREDIT_REGISTER);
		}

		// TTM_DEV_11 APPLICATION_LOG BEGIN ADD
		BasicWebApiResDto logResDto = this.logRegisterLogic.registerLog(UserConst.LOG_SCREEN_CONTRACT_INFO, UserConst.LOG_BUTTON_CREDIT_CARD, CommonConst.EMPTY_STRING, ipAddress);
		if(!logResDto.isResult()){
			LogUtils log = new LogUtils(this.getClass());
			log.error(UserConst.ERROR_MESSAGE_WRITE_LOG);
		}
		// TTM_DEV_11 APPLICATION_LOG END ADD
		
		return retPath;
	}

	/**
	 * 確認
	 *
	 * @return 戻り先
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	//TTM_DEV 20170621 UPDATE BEGIN
	@Execute(validator = true,validate = "check", input = "index")
	public String confirmationInit() throws IllegalArgumentException, IllegalAccessException {
	//TTM_DEV 20170621 UPDATE END
		// セッションにForm設定
		super.session.setAttribute(UserConst.S_KEY_PAYMENT_CREDIT_REGISTER, this.paymentCreditRegisterForm);
		this.paymentCreditRegisterForm.trim();
		// TTM_DEV_11 APPLICATION_LOG BEGIN ADD
		BasicWebApiResDto logResDto = this.logRegisterLogic.registerLog(UserConst.LOG_SCREEN_CARD_REGISTER, UserConst.LOG_BUTTON_CONFIRM, CommonConst.EMPTY_STRING, ipAddress);
		if(!logResDto.isResult()){
			LogUtils log = new LogUtils(this.getClass());
			log.error(UserConst.ERROR_MESSAGE_WRITE_LOG);
		}
		// TTM_DEV_11 APPLICATION_LOG END ADD
		
		// 確認画面へ
		return JSP_FILE_CONFIRM;
	}

	/**
	 * 登録
	 *
	 * @return 戻り先
	 */
	@Execute(validator = false, input = "index")
	public String register() {

		// 請求先コードは契約情報照会画面から持ってくる。
		String seiqCd = super.userSessionDto.getSeiqCd();
		String kkykBng = super.userSessionDto.getKkykBng();
				
		// セッションからFormを取得
		paymentCreditRegisterForm = (PaymentCreditRegisterForm) super.session
										.getAttribute(UserConst.S_KEY_PAYMENT_CREDIT_REGISTER);

		// トークン方式対応 20170302 BEGIN ADD
		// セッションから情報を取得する前に、formからトークンを取り出す。
		String token = paymentCreditRegisterForm.getToken();
		// トークン方式対応 20170302 BEGIN ADD
		
		// トークン方式対応 20170302 BEGIN UPDATE
		// 更新API用パラメータ作成
		Map<String, String> map = new HashMap<String, String>();
		map.put("seiqCd", seiqCd);	// 請求先コード
		map.put("kkykBng", kkykBng);	// 顧客番号
		map.put("cardName", paymentCreditRegisterForm.getCardName());
//		map.put("cardNo", paymentCreditRegisterForm.getCardNo());
//		map.put("expire", paymentCreditRegisterForm.getExpire());
		map.put("token", token);
		map.put("holderName", paymentCreditRegisterForm.getHolderName());
		map.put("haraiKbn", CodeMstConst.HARAI_KBN_CREDIT);
		// トークン方式対応 20170302 END UPDATE
		
		// 更新API実行
		BasicWebApiResDto resDto = this.payeeUpdateLogic.updatePayee(map);

		String retPath;
		if (resDto.isResult()) {
			// 正常終了時
			// SessionからForm情報を削除
			super.session.removeAttribute(UserConst.S_KEY_PAYMENT_CREDIT_REGISTER);

			// TTM_DEV_11 APPLICATION_LOG BEGIN ADD
			BasicWebApiResDto logResDto = this.logRegisterLogic.registerLog(UserConst.LOG_SCREEN_CARD_REGISTER, UserConst.LOG_BUTTON_REGISTER, CommonConst.EMPTY_STRING, ipAddress);
			if(!logResDto.isResult()){
				LogUtils log = new LogUtils(this.getClass());
				log.error(UserConst.ERROR_MESSAGE_WRITE_LOG);
			}
			// TTM_DEV_11 APPLICATION_LOG END ADD
			
			// 完了JSPに戻り値を指定
			retPath = JSP_FILE_FINISH;
		} else {
			// 異常終了時

			// 入力JresDto.getErrorCode()SPに戻り値を指定
			retPath = this.index();

			// エラーメッセージ表示を追加
			ActionMessages errors = new ActionMessages();
			this.addErrors(errors, resDto.getErrorCode());
			ActionMessagesUtil.addErrors(super.request, errors);

		}

		// 完了画面へ
		return retPath;
	}
//	TTM_DEV 20170621 ADD BEGIN
	public ActionMessages check() {
		ActionMessages errors = new ActionMessages();
		if(UserConst.CHECK_TOKEN_FALSE.equals(this.paymentCreditRegisterForm.getCheckToken())){
			super.addErrors(errors,  UserConst.MESSAGE_ERRORS_TOKEN);
		}
		return errors;
	}
//	TTM_DEV 20170621 ADD END
}
