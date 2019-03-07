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
import jp.co.forvaltel.user.form.KaiyakuInfoForm;
import jp.co.forvaltel.user.logic.LogRegisterLogic;
import jp.co.forvaltel.user.logic.PayeeReferenceLogic;
import jp.co.forvaltel.user.util.LogUtils;

/**
 * KaiyakuInfo Action
 * 
 * @author TTM-DEV
 */
public class KaiyakuInfoAction extends AbstractUserAction {

	/** JSPファイル名（入力） */
	private static final String JSP_FILE = "kaiyakuInfo.jsp";

	/** アクションフォーム */
	@Resource
	@ActionForm
	public KaiyakuInfoForm kaiyakuInfoForm;

	/**
	 * 支払先情報取得
	 */
	@Resource
	PayeeReferenceLogic payeeReferenceLogic;

	@Resource
	LogRegisterLogic logRegisterLogic;
	
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
		this.kaiyakuInfoForm.setPaymentMethodFlg(true);
		if(!UserConst.HARAI_HOUHOU_KAKUTEI_FLAG_DEFINED.equals(resDto.getHaraiHouhouKakuteiFlag())
				&& (CodeMstConst.HARAI_KBN_CREDIT.equals(resDto.getHaraiKbn()) 
						|| CodeMstConst.HARAI_KBN_NTTADDUP.equals(resDto.getHaraiKbn()))){
			this.kaiyakuInfoForm.setPaymentMethodFlg(false);
		}
		
		this.kaiyakuInfoForm.setPaymentCreditFlg(false);
		this.kaiyakuInfoForm.setPaymentAddingUpFlg(false);
		for (HcDairitenShiharaiHimoMst entity : this.userSessionDto.getHcDairitenShiharaiHimoMstList()){
			if (CodeMstConst.KOKYAKU_KBN_KOJIN.equals(this.userSessionDto.getKokyakuKbn()) 
					&& CodeMstConst.HARAI_KBN_CREDIT.equals(entity.shiaraiKbn)){
				this.kaiyakuInfoForm.setPaymentCreditFlg(true);
			}
			if (CodeMstConst.HARAI_KBN_FILE_NTT_ADDING_UP.equals(entity.shiaraiKbn)){
				this.kaiyakuInfoForm.setPaymentAddingUpFlg(true);
			}
		}
		
		BasicWebApiResDto logResDto = this.logRegisterLogic.registerLog(UserConst.LOG_SCREEN_CONTRACT_INFO, UserConst.LOG_BUTTON_CREDIT_CARD, CommonConst.EMPTY_STRING, ipAddress);
		if(!logResDto.isResult()){
			LogUtils log = new LogUtils(this.getClass());
			log.error(UserConst.ERROR_MESSAGE_WRITE_LOG);
		}
		
		return JSP_FILE;
	}
}
