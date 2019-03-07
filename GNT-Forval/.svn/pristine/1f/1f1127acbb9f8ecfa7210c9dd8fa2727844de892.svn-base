package jp.co.forvaltel.user.action;

import javax.annotation.Resource;

import jp.co.forvaltel.common.annotation.InitDisp;
import jp.co.forvaltel.common.constant.CommonConst;
import jp.co.forvaltel.common.dto.BasicWebApiResDto;
import jp.co.forvaltel.common.util.CheckUtils;
import jp.co.forvaltel.common.util.FtResourcesUtil;
import jp.co.forvaltel.user.constant.DenryokuConst;
import jp.co.forvaltel.user.form.AgreeForm;
import jp.co.forvaltel.user.logic.LogRegisterLogic;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.json.JSONObject;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ActionMessagesUtil;

/**
 * 
 * @author TTM-HaiND
 *
 */
public class AgreeAction extends AbstractDenryokuAction {

	/** JSPファイル名 */
	private static final String JSP_FILE = "agree.jsp";
	private static final String JSP_FILE_TOKEN_ERR = "/error/token_error.jsp";

	/** アクションフォーム */
	@Resource
	@ActionForm
	public AgreeForm agreeForm;
	
	@Resource
	LogRegisterLogic logRegisterLogic;

	@InitDisp
	@Execute(validator = true, input = DenryokuConst.JSP_FILE_404, urlPattern = "{bpmId}/{token}")
	public String index() {
		String url = JSP_FILE;
		
		this.initWebApi(agreeForm.getToken(), agreeForm.getBpmId());

		// check webapi error code
		if (CommonConst.MSG_ID_ERRORS_COLLATE
				.equals((String) super.session.getAttribute(DenryokuConst.SESSION_ATTR_WEBAPI_ERROR_CD))) {
			url = DenryokuConst.JSP_FILE_404;
		} else if (DenryokuConst.ERRORS_TOKEN_EXPIRE_CD
				.equals((String) super.session.getAttribute(DenryokuConst.SESSION_ATTR_WEBAPI_ERROR_CD))) {
			agreeForm.setWebapiMsg((String) super.session.getAttribute(DenryokuConst.SESSION_ATTR_WEBAPI_ERROR_MSG));
			url = JSP_FILE_TOKEN_ERR;
		}

		ActionMessages infos = new ActionMessages();
		infos.add("org.apache.struts.action.GLOBAL_MESSAGE",new ActionMessage("agree.completed.msg"));
		setInfos(infos);
		ActionMessagesUtil.addMessages(request, infos);
		
		this.removeSession();

		return url;
	}

	@Execute(validator = false)
	public String ajScreenInfo() throws InterruptedException {

		try {
			// 商品セレクトボックス情報を取得
			BasicWebApiResDto resDto = super.denryokuLogic.getCustomerInfo();

			writeAjResult(resDto);
			
			if (resDto.isResult()) {
				String json = resDto.getReturnValue().toString();
				JSONObject object = new JSONObject(json);
				String consumerFlg = object.getString("consumer_flg");
				
				if (CommonConst.FLG_TRUE.equals(consumerFlg)) {
					this.logReigster(false);
				}
			}
		} catch (Exception e) {
			super.response.setStatus(500);
		}

		return null;
	}

	@Execute(validator = false)
//	@RemoveSession(name = "tokenDto")
	public String ajUpdateInfo() throws InterruptedException {

		try {
			// 商品セレクトボックス情報を取得
			BasicWebApiResDto resDto = super.denryokuLogic.updateCustomerInfo(agreeForm.getMoshikomiNo());
			super.session.removeAttribute("tokenDto");
			writeAjResult(resDto);
			
			if (resDto.isResult()) {
				this.logReigster(true);
			}
		} catch (Exception e) {
			super.response.setStatus(500);
		}

		return null;
	}
	
	private void logReigster(boolean isAction) {

		String ipAdrress = CommonConst.EMPTY_STRING;
		String header = request.getHeader("X-Forwarded-For");
		if (!CheckUtils.isEmpty(header)) {
			ipAdrress = header.split(CommonConst.H_COMMA)[0];
		} else {
			ipAdrress = request.getRemoteAddr();
		}

		String action = CommonConst.EMPTY_STRING;
		if(isAction) {
			action = FtResourcesUtil.getItemValue("agree.button.10");
		}
		this.logRegisterLogic.registerLog(
				FtResourcesUtil.getItemValue("agree.title.00"),  action,  this.agreeForm.getBpmId(), ipAdrress);
	}
}
