/*
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.taglib;

import javax.servlet.jsp.JspException;

import jp.co.forvaltel.common.util.FtResourcesUtil;

import org.apache.struts.taglib.html.ButtonTag;

/**
 * ボタン拡張タグライブラリ
 * 
 * @author smis
 */
public class FtButtonTag extends ButtonTag {

	/** デフォルトシリアルバージョンID */
	private static final long serialVersionUID = 1L;
	
	/* (非 Javadoc)
	 * @see jp.co.intra_mart.foundation.ui.tags.controls.UIInput#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
		// Valueに設定されたキーを変換して、Valueに再設定
		super.setValue(FtResourcesUtil.getItemValue(super.getValue()));
		return super.doStartTag();
	}

}
