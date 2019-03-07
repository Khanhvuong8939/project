/*
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.action;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.forvaltel.common.annotation.Ajax;
import jp.co.forvaltel.common.dto.BasicWebApiResDto;
import jp.co.forvaltel.common.dto.KakuchouDataDto;
import jp.co.forvaltel.user.form.KakuchouForm;
import jp.co.forvaltel.user.logic.KakuchouLogic;

import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

/**
 * 拡張項目アクション
 * 
 * @author smis
 */
public class KakuchouAction extends AbstractUserAction {

	/** アクションフォーム */
	@Resource
	@ActionForm
	public KakuchouForm kakuchouForm;

	/** 拡張項目ロジック */
	@Resource
	private KakuchouLogic kakuchouLogic;

	/**
	 * 　コンストラクタ
	 */
	public KakuchouAction() {
	}

	/**
	 * 拡張項目マスタ取得
	 * 
	 * @return jspパス
	 */
	@Ajax
	@SuppressWarnings("unchecked")
	@Execute(validator = false, input = "ajaxOutputError")
	public String getkakuchouList() {
		// 戻り値用のDTOリストを生成
		BasicWebApiResDto dto = this.kakuchouLogic.getkakuchouList(this.kakuchouForm.getJuchNo(),
				this.kakuchouForm.getJuchMeisaiNo(), this.kakuchouForm.getSrvCd(), this.kakuchouForm.getShohinCd(),
				this.kakuchouForm.getHyoujiKbn(),
				//TTM_DEV 20170615 ADD BEGIN
				this.kakuchouForm.getScreen());
				//TTM_DEV 20170615 ADD END

		// 戻り値が正常の場合、
		List<KakuchouDataDto> list = new ArrayList<KakuchouDataDto>();
		if (dto.isResult()) {
			list = (List<KakuchouDataDto>) dto.getReturnValueList();
		}

		// 取得結果をJSONでResponseに設定
		super.writeJson(list);
		return null;
	}
}
