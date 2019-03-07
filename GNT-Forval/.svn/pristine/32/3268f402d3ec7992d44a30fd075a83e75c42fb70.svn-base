/**
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.forvaltel.common.dto.BasicWebApiResDto;
import jp.co.forvaltel.common.entity.HcKyakuMst;
import jp.co.forvaltel.common.entity.HcSeiqMst;

/**
 * 請求先マスタ取得（WebAPI実行）
 * 
 * @author TTM
 */
//TTM_DEV USER_SITE_FIRST_MODULE BEGIN ADD
public class KyakuMstLogic extends AbstractWebApiLogic {

	/** WebAPI実行用のパス */
	private static final String ACTION_PATH = "kyakuMst/getKyakuMstList";

	/**
	 * 請求先マスタ取得
	 * 
	 * @return List<HcCodeMst> コードマスタエンティティのリスト
	 */
	public List<HcKyakuMst> getKyakuList() {

		// 引数用のmapを作成（基本はセッション情報で設定されるため、個別設定無し）
		Map<String, String> map = new HashMap<String, String>();

		// WebAPI実行
		BasicWebApiResDto resDto = super.execute(ACTION_PATH, map);

		// 処理結果がFalseの場合
		List<HcKyakuMst> list = null;
		if (!resDto.isResult()) {
			// 処理結果がFalseの場合、Nullを返却
			return list;
		}

		// 戻り値用オブジェクトに結果を複写
		list = new ArrayList<HcKyakuMst>();
		for (Object obj : resDto.getReturnValueList()) {
			list.add(super.decode(obj, HcKyakuMst.class));
		}

		// 戻り値設定
		return list;
	}
}
//TTM_DEV USER_SITE_FIRST_MODULE END ADD