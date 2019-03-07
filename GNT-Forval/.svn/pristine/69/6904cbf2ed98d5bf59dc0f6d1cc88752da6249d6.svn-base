/*
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import jp.co.forvaltel.common.dto.BasicWebApiResDto;
import jp.co.forvaltel.common.dto.KakuchouDataDto;
import jp.co.forvaltel.user.form.KakuchouForm;
import jp.co.forvaltel.user.logic.KakuchouLogic;

/**
 * 拡張項目アクション
 * 
 * @author TTM
 */
public class KakuchouAction extends AbstractDenryokuAction {
	
	/** アクションフォーム */
	@Resource
	@ActionForm
	public KakuchouForm kakuchouForm;

	/** 拡張項目ロジック */
	@Resource
	private KakuchouLogic kakuchouLogic;
	
	/**
	 * 拡張項目マスタ取得
	 * 
	 * @return jspパス
	 */
	@SuppressWarnings("unchecked")
	@Execute(validator = false)
	public String getkakuchouList() {
		
		// 戻り値用のDTOリストを生成
		BasicWebApiResDto dto = this.kakuchouLogic.getkakuchouList(this.kakuchouForm.getMoshikomiNo(), this.kakuchouForm.getMode());
	
		try {
			// 戻り値が正常の場合、
			List<KakuchouDataDto> list = new ArrayList<KakuchouDataDto>();
			Map<String,Object> result = new HashMap<String,Object>();
			if (dto.isResult()) {
				list = (List<KakuchouDataDto>) dto.getReturnValueList();
				result.put("success", true);
				result.put("data", list);
			} else {
				result.put("success", false);
				result.put("is_redirect", this.isTokenError(dto));
				result.put("msg", dto.getErrorMessage());
			}
	
			// 取得結果をJSONでResponseに設定
			super.writeJson(result);
		} catch (Exception e) {
			super.response.setStatus(500);
		}

		return null;
	}
}
