/**
 * 
 */
package jp.co.forvaltel.user.action;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import jp.co.forvaltel.common.action.AbstractAction;
import jp.co.forvaltel.common.constant.CommonConst;
import jp.co.forvaltel.common.dto.BasicWebApiResDto;
import jp.co.forvaltel.common.util.CheckUtils;
import jp.co.forvaltel.common.util.FtResourcesUtil;
import jp.co.forvaltel.user.constant.DenryokuConst;
import jp.co.forvaltel.user.dto.TokenDto;
import jp.co.forvaltel.user.logic.DenryokuLogic;
import jp.co.forvaltel.user.util.LogUtils;

import org.seasar.struts.util.ServletContextUtil;

/**
 * @author hungtp
 *
 */
public abstract class AbstractDenryokuAction extends AbstractAction {

	private static final String ITEM_KEY_TENANT_ID = "system.tenantid";
	
	@Resource
	protected TokenDto tokenDto;
	
	@Resource
	protected DenryokuLogic denryokuLogic;
	
	protected LogUtils logUtils;
	
	private String logoName;
	
	public AbstractDenryokuAction() {
		super();
		logUtils = new LogUtils(this.getClass());
	}

	protected void initWebApi(String token, String bpmId) {
		tokenDto.setTenantId(FtResourcesUtil.getItemValue(ITEM_KEY_TENANT_ID));
		tokenDto.setToken(token);
		tokenDto.setBpmId(bpmId);
		this.setLogoName(this.getLogo());
	}
	
	protected Boolean isTokenError(BasicWebApiResDto resDto) {
		if (CommonConst.MSG_ID_ERRORS_COLLATE.equals(resDto.getErrorCode()) || 
				DenryokuConst.ERRORS_TOKEN_EXPIRE_CD.equals(resDto.getErrorCode())) {
			super.session.setAttribute(DenryokuConst.SESSION_ATTR_WEBAPI_ERROR_CD, resDto.getErrorCode());
			super.session.setAttribute(DenryokuConst.SESSION_ATTR_WEBAPI_ERROR_MSG, resDto.getErrorMessage());
			return true;
		} 
		return false;
	}
	
	protected void removeSession() {
		super.session.removeAttribute(DenryokuConst.SESSION_ATTR_WEBAPI_ERROR_CD);
		super.session.removeAttribute(DenryokuConst.SESSION_ATTR_WEBAPI_ERROR_MSG);
	}
	
	protected void writeAjResult(BasicWebApiResDto resDto) {
		
		Map<String,Object> result = new HashMap<String,Object>();
		
		if (resDto.isResult()) {
			result.put("success", true);
			result.put("data", resDto.getReturnValue());
		} else {
			result.put("success", false);
			result.put("is_redirect", isTokenError(resDto));
			result.put("msg", resDto.getErrorMessage());
		}
		
		super.writeJson(result);
	}
	
	private String getLogo() {
		String logo = FtResourcesUtil.getItemValue("system.logo");
		BasicWebApiResDto dto = this.denryokuLogic.getLogo();
		if (dto.isResult() && !CheckUtils.isEmpty(dto.getReturnValue())) {
			String logoName = (String) dto.getReturnValue();
			
			ServletContext sc = ServletContextUtil.getServletContext();
			String path = sc.getRealPath("/ui/img/logo/" + logoName);
			File file = new File(path);
			
			if (file.exists()) {
				logo = logoName;
			}
		}
		
		return logo;
	}

	/**
	 * @return the logoName
	 */
	public String getLogoName() {
		return logoName;
	}

	/**
	 * @param logoName the logoName to set
	 */
	public void setLogoName(String logoName) {
		this.logoName = logoName;
	}
}
