/**
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.forvaltel.common.dto.BasicWebApiResDto;
import jp.co.forvaltel.common.entity.HcCodeMst;

import org.seasar.framework.beans.util.BeanUtil;

/**
 * コードマスタ取得（WebAPI実行）
 * 
 * @author smis
 */
public class CodeMstLogic extends AbstractWebApiLogic {

	/** WebAPI実行用のパス */
	private static final String ACTION_PATH = "codeMst/getCodeMstList";
	/** WebAPI実行用のパス */
	private static final String ACTION_PATH2 = "codeMst/get";
	/** パラメータ カテゴリID */
	private static final String PARAM_KATEGORI_ID = "KategoriId";
	/** パラメータ コード */
	private static final String PARAM_CODE = "code";

	/**
	 * コードマスタ取得
	 * 
	 * @param kategoriId カテゴリID
	 * @return List<HcCodeMst> コードマスタエンティティのリスト
	 */
	@SuppressWarnings("unchecked")
	public List<HcCodeMst> getList(String kategoriId) {

		// 引数用のmapを作成
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(PARAM_KATEGORI_ID, kategoriId);

		// WebAPI実行
		BasicWebApiResDto resDto = super.executeWebApi(ACTION_PATH, map);

		// 戻り値用オブジェクトに結果を複写
		List<HcCodeMst> list = new ArrayList<HcCodeMst>();
		if (resDto.isResult()) {
			for (Object obj : resDto.getReturnValueList()) {
				HcCodeMst entity = new HcCodeMst();
				BeanUtil.copyProperties((Map<String, String>) obj, entity);
				list.add(entity);
			}
		}

		// 戻り値設定
		return list;
	}

	/**
	 * コードマスタ取得
	 * 
	 * @param kategoriId カテゴリID
	 * @param code コード
	 * @return List<HcCodeMst> コードマスタエンティティのリスト
	 */
	public HcCodeMst get(String kategoriId, String code) {

		// 引数用のmapを作成
		Map<String, String> map = new HashMap<String, String>();
		map.put(PARAM_KATEGORI_ID, kategoriId);
		map.put(PARAM_CODE, code);

		// WebAPI実行
		BasicWebApiResDto resDto = super.execute(ACTION_PATH2, map);

		// 戻り値用オブジェクトに結果を複写
		HcCodeMst entity = new HcCodeMst();
		if (resDto.isResult()) {
			entity = super.decode(resDto.getReturnValue(), HcCodeMst.class);
		}

		// 戻り値設定
		return entity;
	}

}
