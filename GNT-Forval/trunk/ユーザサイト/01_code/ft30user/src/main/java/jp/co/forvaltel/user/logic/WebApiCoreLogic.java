/**
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.logic;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.persistence.OptimisticLockException;

import jp.co.forvaltel.common.constant.CommonConst;
import jp.co.forvaltel.common.dto.BasicWebApiResDto;
import jp.co.forvaltel.common.exception.FtSystemException;
import jp.co.forvaltel.common.logic.AbstractLogic;
import jp.co.forvaltel.common.util.FtResourcesUtil;
import jp.co.forvaltel.user.constant.UserConst;
import jp.co.forvaltel.user.dto.UserSessionDto;
import jp.co.forvaltel.user.util.LogUtils;
import net.arnx.jsonic.JSON;
import net.arnx.jsonic.JSONException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * WebAPIロジック抽象クラス
 * 
 * @author smis
 */
public class WebApiCoreLogic extends AbstractLogic {

	/** 共通パラメータ定数 テナントID */
	protected static final String PRM_TENANT_ID = "tenantId";
	/** 共通パラメータ定数 ユーザID */
	protected static final String PRM_USER_ID = "userCd";
	/** 共通パラメータ定数 パスワード */
	protected static final String PRM_PASSWORD = "password";
	/** プロパティキー WebサービスURL */
	private static final String KEY_URL_WEB_SERVICE = "system.websrv.url";

	/**
	 * ユーザサイトセッション情報
	 */
	@Resource
	protected UserSessionDto userSessionDto;

	/**
	 * WebAPI実行
	 * 
	 * @param actionPath アクションパス
	 * @param map パラメータマップ
	 * @return String JSON文字列
	 */
	public BasicWebApiResDto execute(String actionPath, Map<String, String> map) {
		// Mapオブジェクトの詰め替え
		Map<String, Object> paramMap = new LinkedHashMap<String, Object>();
		for (Entry<String, String> enrty : map.entrySet()) {
			paramMap.put(enrty.getKey(), enrty.getValue());
		}
		// WebAPI実行
		return this.executeWebApi(actionPath, paramMap);
	}

	/**
	 * WebAPI実行
	 * 
	 * @param actionPath アクションパス
	 * @param map パラメータマップ
	 * @return String JSON文字列
	 */
	public BasicWebApiResDto executeWebApi(String actionPath, Map<String, Object> map) {

		// ログ出力設定
		LogUtils logUtils = new LogUtils(this.getClass());

		// 共通項目の設定
		if (this.userSessionDto != null) {
			map.put(PRM_TENANT_ID, this.userSessionDto.getTenantId());
			map.put(PRM_USER_ID, this.userSessionDto.getUserId());
			map.put(PRM_PASSWORD, this.userSessionDto.getPassword());
		}

		// パラメータをJSONに変換
		String param = JSON.encode(map);

		// 通信処理実行
		BasicWebApiResDto ret = null;
		try {
			// HttpClient生成
			HttpClient httpClient = HttpClientBuilder.create().build();

			// HTTPPOST生成
			StringBuilder webSrvUrl = new StringBuilder();
			webSrvUrl.append(FtResourcesUtil.getItemValue(KEY_URL_WEB_SERVICE));
			webSrvUrl.append(CommonConst.H_SLASH);
			webSrvUrl.append(actionPath);
			webSrvUrl.append(CommonConst.H_QUESTION);
			webSrvUrl.append(CommonConst.QUERY_STRING_TENATID);
			webSrvUrl.append(CommonConst.H_EQUAL);
			webSrvUrl.append(this.userSessionDto.getTenantId());
			HttpPost request = new HttpPost(webSrvUrl.toString());

			// ヘッダーの設定
			request.addHeader(CommonConst.REQ_HEAD_KEY_CONTENT_TYPE, CommonConst.REQ_HEAD_VAL_JSON);

			// JSONパラメータ設定
			StringEntity body = new StringEntity(param, CommonConst.CHARSET_UTF8);
			request.setEntity(body);

			logUtils.debug("------------APIリクエストここから----------");
			logUtils.debug("URL：" + webSrvUrl.toString());
			logUtils.debug("Body：" + param);
			logUtils.debug("------------APIリクエストここまで----------");
			
			// 通信実行
			HttpResponse response = httpClient.execute(request);

			logUtils.debug("------------APIレスポンスここから----------");
			logUtils.debug("ステータス：" + response.getStatusLine().getStatusCode());
			
			// ステータスを確認
			//if (response.getStatusLine().getStatusCode() == 200) {
				if (true) {
				// 戻り値のJSONを取得
				String json = EntityUtils.toString(response.getEntity());
				logUtils.debug("Body：" + json);
				logUtils.debug("------------APIレスポンスここまで----------");
				// ResponseDTOに変換
				ret = JSON.decode(json, BasicWebApiResDto.class);
			} else {
				// 通信ステータスが200以外の場合、システムエラーとする。
				String message = MessageResourcesUtil.getMessage(CommonConst.MSG_ID_ERRORS_COMMUNICATION_ERROR,
						response.getStatusLine().getStatusCode());
				logUtils.error("内部API実行に失敗　パラメータ：" + param);
				throw new FtSystemException(message);
			}

			// 所定のエラーコードが設定されていた場合、その例外をスロ－
			if (!ret.isResult()) {
				if (CommonConst.MSG_ID_ERRORS_OPTIMISTIC_LOCK.equals(ret.getErrorCode())) {
					// 楽観排他エラーの場合
					logUtils.error("内部API実行に失敗　パラメータ：" + param);
					throw new OptimisticLockException();
				} else if (CommonConst.MSG_ID_ERRORS_COLLATE.equals(ret.getErrorCode())) {
					// 認証エラーの場合
					String message = MessageResourcesUtil.getMessage(CommonConst.MSG_ID_ERRORS_COLLATE);
					logUtils.error("内部API実行に失敗　パラメータ：" + param);
					throw new FtSystemException(message);
				} else if (CommonConst.MSG_ID_ERRORS_SYSTEM.equals(ret.getErrorCode())) {
					// システムエラーの場合
					String message = MessageResourcesUtil.getMessage(UserConst.MSG_ID_ERRORS_WEBAPI_ERROR);
					logUtils.error("内部API実行に失敗　パラメータ：" + param);
					throw new FtSystemException(message);
				}
			}

		} catch (IOException | JSONException e) {
			// システム例外をスロー
			logUtils.error("内部API実行に失敗　パラメータ：" + param);
			throw new FtSystemException(e);
		}

		return ret;
	}
}
