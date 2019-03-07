/**
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
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
import jp.co.forvaltel.user.form.PaymentAddingUpRegisterForm;
import jp.co.forvaltel.user.logic.CodeMstLogic;
import jp.co.forvaltel.user.logic.LogRegisterLogic;
import jp.co.forvaltel.user.logic.PayeeReferenceLogic;
import jp.co.forvaltel.user.logic.PayeeUpdateLogic;
import jp.co.forvaltel.user.util.LogUtils;

/**
 * 支払先情報登録（NTT合算）アクション
 *
 * @author smis
 */
public class PaymentAddingUpRegisterAction extends AbstractUserAction {

	/** JSPファイル名（入力） */
	private static final String JSP_FILE_INPUT = "input.jsp";
	/** JSPファイル名（確認） */
	private static final String JSP_FILE_CONFIRM = "confirm.jsp";
	/** JSPファイル名（完了） */
	private static final String JSP_FILE_FINISH = "finish.jsp";
	/** アクションパス 契約情報照会画面 */
	private static final String ACTION_PATH_CONTRACT_INFO_QUERY = "/contractInfoQuery";
	/** プロパティキー 電話料金合算請求 */
	private static final String PROP_KEY_PAYMENT_ADDING = "payment_adding_upregiste.label.999";

	/** アクションフォーム */
	@Resource
	@ActionForm
	public PaymentAddingUpRegisterForm paymentAddingUpRegisterForm;

	/**
	 * 支払先情報取得
	 */
	@Resource
	PayeeReferenceLogic payeeReferenceLogic;

	/**
	 * 支払先情報更新
	 */
	@Resource
	PayeeUpdateLogic payeeUpdateLogic;

	/**
	 * コードマスタロジック
	 */
	@Resource
	CodeMstLogic codeMstLogic;

	// TTM_DEV_11 APPLICATION_LOG BEGIN ADD
	@Resource
	LogRegisterLogic logRegisterLogic;
	// TTM_DEV_11 APPLICATION_LOG END ADD
	/**
	 * 入力チェック処理
	 *
	 * @return ActionMessages　エラーメッセージオブジェクト
	 */
	public ActionMessages initcheck() {
		// 戻り値生成
		ActionMessages errors = new ActionMessages();

		// 支払状況取得
		String seiqCd = super.userSessionDto.getSeiqCd();
		String kkykBng = super.userSessionDto.getKkykBng();
		BasicWebApiResDto dto = this.payeeReferenceLogic.getPayee(seiqCd, kkykBng);
		PayeeReferenceResDto resDto = (PayeeReferenceResDto) dto.getReturnValue();
//		TTM_DEV ORDER_MODULE 20170524 BEGIN UPDATE
		// NTT登録区分="0"デフォルトかチェック
//		if (!UserConst.NTT_TOUROKU_KUBUN_DEFAULT.equals(resDto.getNttTourokuKbn())) {
//			// 画面にメッセージ表示
//			super.addErrors(errors, UserConst.MSG_ID_ERRORS_HARAI_NOT_BE_CHANGED,
//					FtResourcesUtil.getItemValue(PROP_KEY_PAYMENT_ADDING));
//		}

		if(CodeMstConst.HARAI_KBN_FILE_NTTTOUZAI.equals(resDto.getHaraiKbn()) 
				&& CodeMstConst.KIKAN_STATUS_RECEIVED.equals(resDto.getKikanStatus()) ){
			super.addErrors(errors, UserConst.MSG_ID_ERRORS_HARAI_NOT_BE_CHANGED,
					FtResourcesUtil.getItemValue(PROP_KEY_PAYMENT_ADDING));
		}	
//		TTM_DEV ORDER_MODULE 20170524 END UPDATE 
		return errors;
	}

	/**
	 * 初期表示（入力）
	 *
	 * @return 戻り先
	 */
	@Execute(validator = true, validate = "initcheck", input = ACTION_PATH_CONTRACT_INFO_QUERY)
	public String index() {
		// 既存Form取得
		Object sessionObject = super.session.getAttribute(UserConst.S_KEY_PAYMENT_ADDING_UP_REGIST);
		PaymentAddingUpRegisterForm sessionForm = (PaymentAddingUpRegisterForm) sessionObject;

		// 既存Formの存在チェック
		if (sessionForm == null) {
			// 存在しない場合、初期表示処理
			String seiqCd = super.userSessionDto.getSeiqCd();
			String kkykBng = super.userSessionDto.getKkykBng();
			BasicWebApiResDto dto = this.payeeReferenceLogic.getPayee(seiqCd, kkykBng);
			PayeeReferenceResDto resDto = (PayeeReferenceResDto) dto.getReturnValue();

			// 支払区分ステータスをチェック
			if (CodeMstConst.HARAI_KBN_STATUS_NG.compareTo(resDto.getHaraiKbnStatus()) <= 0
					&& !CodeMstConst.HARAI_KBN_STATUS_PENDING.equals(resDto.getHaraiKbnStatus())) {
					// 支払区分ステータスが[未確定/引落し可/申請中以外]の時
					// NTTF申請結果ステータスの値でコードマスタを参照し、エラーメッセージを取得する。
				// コードマスタから取得
				HcCodeMst codeMst = this.codeMstLogic.get(CodeMstConst.ID_NTT_FINANCE_KEKKA_STATUS,
						resDto.getNttFinanceKekkaStatus());
				// 画面にメッセージ表示
				super.addWarns(new ActionMessage("warns.nttf.ng", resDto.getNttFinanceKekkaStatus(), codeMst.codeMei));

			} 
			// TTM_DEV-489 20170807 BEGIN DELETE
//			else {
			// TTM_DEV-489 20170807 END DELETE
				// NGではない場合、Formに値を設定
				this.paymentAddingUpRegisterForm.setAgencyCd(resDto.getSeiqMatomeNttf());
				this.paymentAddingUpRegisterForm.setHolderName(resDto.getNttKsnMegiNttf());
				this.paymentAddingUpRegisterForm.setHolderNameKana(resDto.getNttKsnMegiKnNttf());
				this.paymentAddingUpRegisterForm.setDoneFlg(false);
				// TTM_DEV-96 20170807 BEGIN UPDATE
//				this.paymentAddingUpRegisterForm.setPaymentMethodFlg(
//						UserConst.HARAI_HOUHOU_KAKUTEI_FLAG_DEFINED.equals(resDto.getHaraiHouhouKakuteiFlag()));
				this.paymentAddingUpRegisterForm.setPaymentMethodFlg(true);
				if(!UserConst.HARAI_HOUHOU_KAKUTEI_FLAG_DEFINED.equals(resDto.getHaraiHouhouKakuteiFlag())
						&& (CodeMstConst.HARAI_KBN_CREDIT.equals(resDto.getHaraiKbn()) 
								|| CodeMstConst.HARAI_KBN_NTTADDUP.equals(resDto.getHaraiKbn()))){
					this.paymentAddingUpRegisterForm.setPaymentMethodFlg(false);
				}
				// TTM_DEV-96 20170807 END UPDATE
				
				// TTM_DEV-542 20170830 BEGIN ADD
				this.paymentAddingUpRegisterForm.setPaymentCreditFlg(false);
				this.paymentAddingUpRegisterForm.setPaymentTransferFlg(false);
				for (HcDairitenShiharaiHimoMst entity : this.userSessionDto.getHcDairitenShiharaiHimoMstList()){
					if (CodeMstConst.KOKYAKU_KBN_KOJIN.equals(this.userSessionDto.getKokyakuKbn()) 
							&& CodeMstConst.HARAI_KBN_CREDIT.equals(entity.shiaraiKbn)){
						this.paymentAddingUpRegisterForm.setPaymentCreditFlg(true);
					}
					if (CodeMstConst.HARAI_KBN_ACOUNT.equals(entity.shiaraiKbn)){
						this.paymentAddingUpRegisterForm.setPaymentTransferFlg(true);
					}
				}
				// TTM_DEV-542 20170830 END ADD
				
				// TTM_DEV-542 20170830 BEGIN DELETE
//				this.paymentAddingUpRegisterForm.setPaymentCreditFlg(
//						!CodeMstConst.KOKYAKU_KBN_CORPOTATION.equals(userSessionDto.getKokyakuKbn()));
				// TTM_DEV-542 20170830 END DELETE
				
				//TTM_DEV 201706310	ADD BEGIN
				this.paymentAddingUpRegisterForm.setDisPlayFlgBtnBack(resDto.isDisPlayFlgBtnBack());
				//TTM_DEV 201706310	ADD END
			// TTM_DEV-489 20170807 BEGIN DELETE
//			{
			// TTM_DEV-489 20170807 END DELETE

		} else {
			// 存在する場合、セッション上のForm、アクションフォームにコピー
			BeanUtil.copyProperties(sessionForm, this.paymentAddingUpRegisterForm);
			super.session.removeAttribute(UserConst.S_KEY_PAYMENT_ADDING_UP_REGIST);
		}

		// TTM_DEV_11 APPLICATION_LOG BEGIN ADD
		BasicWebApiResDto logResDto = this.logRegisterLogic.registerLog(UserConst.LOG_SCREEN_CONTRACT_INFO, UserConst.LOG_BUTTON_TABAL, CommonConst.EMPTY_STRING, ipAddress);
		if(!logResDto.isResult()){
			LogUtils log = new LogUtils(this.getClass());
			log.error(UserConst.ERROR_MESSAGE_WRITE_LOG);
		}
		// TTM_DEV_11 APPLICATION_LOG END ADD
		
		return JSP_FILE_INPUT;
	}

	/**
	 * 確認
	 *
	 * @return 戻り先
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	@Execute(validator = true, input = JSP_FILE_INPUT)
	public String confirmationInit() throws IllegalArgumentException, IllegalAccessException {
		// セッションにForm設定
		super.session.setAttribute(UserConst.S_KEY_PAYMENT_ADDING_UP_REGIST, this.paymentAddingUpRegisterForm);
		this.paymentAddingUpRegisterForm.trim();
		// TTM_DEV_11 APPLICATION_LOG BEGIN ADD
		BasicWebApiResDto logResDto = this.logRegisterLogic.registerLog(UserConst.LOG_SCREEN_FINANCE_APPLICATION_INFO, UserConst.LOG_BUTTON_CONFIRM, CommonConst.EMPTY_STRING, ipAddress);
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
	@Execute(validator = false, input = JSP_FILE_INPUT)
	public String regist() {

		// セッションからFormを取得
		PaymentAddingUpRegisterForm form = (PaymentAddingUpRegisterForm) super.session
				.getAttribute(UserConst.S_KEY_PAYMENT_ADDING_UP_REGIST);

		// 更新API用パラメータ作成
		Map<String, String> map = new HashMap<String, String>();
		map.put("seiqCd", super.userSessionDto.getSeiqCd());
		map.put("kkykBng", super.userSessionDto.getKkykBng());
		map.put("seiqMatomeNttf", form.getAgencyCd());
		map.put("nttKsnMegiNttf", form.getHolderName());
		map.put("nttKsnMegiKnNttf", form.getHolderNameKana());
		map.put("haraiKbn", CodeMstConst.HARAI_KBN_NTTADDUP);

		// 更新API実行
		BasicWebApiResDto resDto = this.payeeUpdateLogic.updatePayee(map);

		String retPath;
		if (resDto.isResult()) {
			// 正常終了時
			// SessionからForm情報を削除
			super.session.removeAttribute(UserConst.S_KEY_PAYMENT_ADDING_UP_REGIST);

			// TTM_DEV_11 APPLICATION_LOG BEGIN ADD
			BasicWebApiResDto logResDto = this.logRegisterLogic.registerLog(UserConst.LOG_SCREEN_FINANCE_APPLICATION_CONFIRM, UserConst.LOG_BUTTON_REGISTER, CommonConst.EMPTY_STRING, ipAddress);
			if(!logResDto.isResult()){
				LogUtils log = new LogUtils(this.getClass());
				log.error(UserConst.ERROR_MESSAGE_WRITE_LOG);
			}
			// TTM_DEV_11 APPLICATION_LOG END ADD
			
			// 完了JSPに戻り値を指定
			retPath = JSP_FILE_FINISH;
		} else {
			// 異常終了時
			// エラーメッセージ表示を追加
			ActionMessages errors = new ActionMessages();
			this.addErrors(errors, resDto.getErrorCode());
			ActionMessagesUtil.addErrors(super.request, errors);

			// 入力JresDto.getErrorCode()SPに戻り値を指定
			retPath = JSP_FILE_INPUT;
		}

		// 完了画面へ
		return retPath;
	}

}
