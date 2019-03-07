/**
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.logic;

import java.util.HashMap;
import java.util.Map;

import jp.co.forvaltel.common.dto.BasicWebApiResDto;
import jp.co.forvaltel.user.constant.UserConst;

/**
 * ホームページ削除（WebAPI実行）
 * 
 * @author smis
 */
public class HpInfoDeleteLogic extends AbstractWebApiLogic {

	/** WebAPI実行用のパス */
	private static final String ACTION_PATH = "hpInfoDelete/delete";
	/** パラメータ 顧客番号 */
	private static final String KKYK_BNG = "kkykBng";
	/** パラメータ WEBアカウント */
	private static final String WEB_ACCOUNT = "webAccount";
	/** パラメータ 削除日 */
	private static final String DEL_DATE = "delDate";
	/**
	 * SP社の「ホームページ削除API」から情報を取得する。
	 * 
	 * @param kkykBng 顧客番号
	 * @param webAccount WEBアカウント
	 * @param delDate 削除日（システム日付）
	 * @return boolean true:成功/false:失敗
	 */
//	TTM_DEV  20170530 BEGIN UPDATE
//	public BasicWebApiResDto delete(String kkykBng, String webAccount, String delDate) {
	public BasicWebApiResDto delete(String kkykBng, String webAccount, String delDate,String internetConnectionId,String accountClassification) {
//	TTM_DEV  20170530 BEGIN UPDATE
		// 引数用のmapを作成
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(KKYK_BNG, kkykBng);
		map.put(WEB_ACCOUNT, webAccount);
		map.put(DEL_DATE, delDate);
//		TTM_DEV  20170530 BEGIN ADD
		map.put(UserConst.PRM_KEY_INTERNET_CONNECTTION_ID, internetConnectionId);
		map.put(UserConst.PRM_KEY_ACCOUNT_CLASSIFICATION,accountClassification);
//		TTM_DEV  20170530 END ADD
		
		// WebAPI実行
		BasicWebApiResDto resDto = super.executeWebApi(ACTION_PATH, map);

		// 戻り値設定
		return resDto;
	}

}

