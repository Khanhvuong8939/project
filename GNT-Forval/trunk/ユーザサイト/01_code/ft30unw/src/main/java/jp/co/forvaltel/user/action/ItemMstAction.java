/**
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import jp.co.forvaltel.common.annotation.Ajax;
import jp.co.forvaltel.common.dto.BasicWebApiResDto;
import jp.co.forvaltel.common.entity.HcShohinMst;
import jp.co.forvaltel.common.util.JsonUtils;
import jp.co.forvaltel.user.form.ItemMstForm;
import jp.co.forvaltel.user.logic.ShohinMstLogic;

import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

/**
 * オプション追加アクション
 * 
 * @author smis
 */
public class ItemMstAction extends AbstractUserAction {

	/** アクションフォーム */
	@Resource
	@ActionForm
	public ItemMstForm itemMstForm;

	/** 商品マスタロジック **/
	@Resource
	private ShohinMstLogic shohinMstLogic;
	/**
	 * Ajax 表示文言取得処理
	 * @return String
	 */
	@Ajax
	@Execute(validator = false)
	public String getItemData() {

		// 送信値生成
		Map<String, String> map = new HashMap<String, String>();
		map.put("shohinCd", this.itemMstForm.getItemCd());

		// 商品データ取得
		BasicWebApiResDto resDto = this.shohinMstLogic.getShohinDate(map);
		// 戻り値が正常の場合
		HcShohinMst itemMst = new HcShohinMst();
		if (resDto.isResult()) {

			itemMst = JsonUtils.decode(resDto.getReturnValue(), HcShohinMst.class);
		}

		// 取得結果をJSONでResponseに設定
		super.writeJson(itemMst);
		return null;
	}
}
