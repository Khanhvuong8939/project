/**
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.logic;

import java.util.HashMap;
import java.util.Map;

import jp.co.forvaltel.common.constant.CommonConst;
import jp.co.forvaltel.common.dto.BasicWebApiResDto;
import jp.co.forvaltel.common.exception.FtApplicationException;
import jp.co.forvaltel.user.constant.UserConst;

/**
 * ホームページ情報登録処理（WebAPI実行）
 *
 * @author smis
 */
public class HpInfoRegistLogic extends AbstractWebApiLogic {

	/** WebAPI実行用のパス */
	private static final String ACTION_PATH = "hpInfoRegist/regist";

	/**
	 * ホームページ情報登録処理
	 *
	 * @param kkykBng 顧客番号
	 * @param webAccount WEBアカウント
	 * @param webAccountPassword WEBアカウントパスワード
	 * @return 基本WebAPIResponseDTO
	 */
	//	TTM_DEV  20170530 BEGIN UPDATE
	//	public BasicWebApiResDto regist(String kkykBng, String webAccount, String webAccountPassword) {
	public BasicWebApiResDto regist(String kkykBng, String webAccount, String webAccountPassword,
			String internetConnectionId,String accountClassification) {
	//		TTM_DEV  20170530 END UPDATE
		Map<String, String> map = new HashMap<String, String>();
		// 顧客番号 = 設定した顧客番号
		map.put(UserConst.PRM_KEY_KKYK_BNG, kkykBng);

		// WEBアカウント = アクションフォーム.WEBアカウント
		map.put(UserConst.PRM_KEY_WEBACCOUNT, webAccount);
		// WEBアカウントパスワード = アクションフォーム.変更後WEBアカウントパスワード
		map.put(UserConst.PRM_KEY_WEB_ACCOUNT_PASSWORD, webAccountPassword);
		
		//TTM_DEV  20170530 BEGIN ADD
		map.put(UserConst.PRM_KEY_INTERNET_CONNECTTION_ID, internetConnectionId);
		map.put(UserConst.PRM_KEY_ACCOUNT_CLASSIFICATION, accountClassification);
		//TTM_DEV  20170530 END ADD
		
		// WebAPI実行
		BasicWebApiResDto resDto = super.execute(ACTION_PATH, map);
		if (!resDto.isResult() && CommonConst.WEBAPI_WEB_EXIST_ERR_CD.equals(resDto.getErrorCode())) {
			// アカウント重複エラーの場合
			throw new FtApplicationException(UserConst.MSG_ID_EXIST_WEB_ACCOUNT);
		}

		// 戻り値設定
		return resDto;
	}
}
