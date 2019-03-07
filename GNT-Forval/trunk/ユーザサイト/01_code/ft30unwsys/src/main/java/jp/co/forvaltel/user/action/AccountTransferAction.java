/**
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.action;

import javax.annotation.Resource;

import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import jp.co.forvaltel.common.annotation.InitDisp;
import jp.co.forvaltel.common.constant.CodeMstConst;
import jp.co.forvaltel.common.constant.CommonConst;
import jp.co.forvaltel.common.dto.BasicWebApiResDto;
import jp.co.forvaltel.common.dto.PayeeReferenceResDto;
import jp.co.forvaltel.common.entity.HcDairitenShiharaiHimoMst;
import jp.co.forvaltel.user.constant.UserConst;
import jp.co.forvaltel.user.form.AccountTransferForm;
import jp.co.forvaltel.user.logic.LogRegisterLogic;
import jp.co.forvaltel.user.logic.PayeeReferenceLogic;
import jp.co.forvaltel.user.util.LogUtils;

/**
 * 支払先情報登録（口座振替）アクション
 *
 * @author smis
 */
public class AccountTransferAction extends AbstractUserAction {

	/** JSPファイル名（入力） */
	private static final String JSP_FILE = "accountTransfer.jsp";

	/** アクションフォーム */
	@Resource
	@ActionForm
	public AccountTransferForm accountTransferForm;

	/**
	 * 支払先情報取得
	 */
	@Resource
	PayeeReferenceLogic payeeReferenceLogic;

	// TTM_DEV_11 APPLICATION_LOG BEGIN ADD
	@Resource
	LogRegisterLogic logRegisterLogic;
	// TTM_DEV_11 APPLICATION_LOG END ADD
	
	/**
	 * 初期表示（入力）
	 *
	 * @return 戻り先
	 */
	@InitDisp
	@Execute(validator = false)
	public String index() {
		String seiqCd = super.userSessionDto.getSeiqCd();
		String kkykBng = super.userSessionDto.getKkykBng();
		BasicWebApiResDto dto = this.payeeReferenceLogic.getPayee(seiqCd, kkykBng);
		PayeeReferenceResDto resDto = (PayeeReferenceResDto) dto.getReturnValue();
		// TTM_DEV-96 20170807 BEGIN UPDATE
//		this.accountTransferForm.setPaymentMethodFlg(
//				UserConst.HARAI_HOUHOU_KAKUTEI_FLAG_DEFINED.equals(resDto.getHaraiHouhouKakuteiFlag()));
		this.accountTransferForm.setPaymentMethodFlg(true);
		if(!UserConst.HARAI_HOUHOU_KAKUTEI_FLAG_DEFINED.equals(resDto.getHaraiHouhouKakuteiFlag())
				&& (CodeMstConst.HARAI_KBN_CREDIT.equals(resDto.getHaraiKbn()) 
						|| CodeMstConst.HARAI_KBN_NTTADDUP.equals(resDto.getHaraiKbn()))){
			this.accountTransferForm.setPaymentMethodFlg(false);
		}
		// TTM_DEV-96 20170807 END UPDATE

		// TTM_DEV-542 20170830 BEGIN ADD
		this.accountTransferForm.setPaymentCreditFlg(false);
		this.accountTransferForm.setPaymentAddingUpFlg(false);
		for (HcDairitenShiharaiHimoMst entity : this.userSessionDto.getHcDairitenShiharaiHimoMstList()){
			if (CodeMstConst.KOKYAKU_KBN_KOJIN.equals(this.userSessionDto.getKokyakuKbn()) 
					&& CodeMstConst.HARAI_KBN_CREDIT.equals(entity.shiaraiKbn)){
				this.accountTransferForm.setPaymentCreditFlg(true);
			}
			if (CodeMstConst.HARAI_KBN_FILE_NTT_ADDING_UP.equals(entity.shiaraiKbn)){
				this.accountTransferForm.setPaymentAddingUpFlg(true);
			}
		}
		// TTM_DEV-542 20170830 END ADD
		
		// TTM_DEV-542 20170830 BEGIN DELETE
//		this.accountTransferForm.setPaymentCreditFlg(
//				!CodeMstConst.KOKYAKU_KBN_CORPOTATION.equals(userSessionDto.getKokyakuKbn()));
		// TTM_DEV-542 20170830 END DELETE
		
		// TTM_DEV_11 APPLICATION_LOG BEGIN ADD
		BasicWebApiResDto logResDto = this.logRegisterLogic.registerLog(UserConst.LOG_SCREEN_CONTRACT_INFO, UserConst.LOG_BUTTON_CREDIT_CARD, CommonConst.EMPTY_STRING, ipAddress);
		if(!logResDto.isResult()){
			LogUtils log = new LogUtils(this.getClass());
			log.error(UserConst.ERROR_MESSAGE_WRITE_LOG);
		}
		// TTM_DEV_11 APPLICATION_LOG END ADD
				
		return JSP_FILE;
	}
}
