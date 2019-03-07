package jp.co.forvaltel.user.logic;

import java.util.HashMap;
import java.util.Map;

import jp.co.forvaltel.common.dto.BasicWebApiResDto;

/**
 * LogRegisterLogic
 * @author TTM-HaiND
 *
 */
public class LogRegisterLogic extends AbstractWebApiLogic {
	private static final String ACTION_PATH_REGISTER_LOG = "denkiLogRegist/registerLog";
	private static final String LOG_KEY_SCREEN_NAME = "screenName";
	private static final String LOG_KEY_DETAIL = "detail";
	private static final String LOG_KEY_ACTION = "action";
	private static final String LOG_KEY_IP_ADDRESS = "ipAddress";
	
	/**
	 * registerLog function
	 * @param screenName
	 * @param message
	 * @param detail
	 * @return BasicWebApiResDto
	 */
	public BasicWebApiResDto registerLog(String screenName, String action, String detail, String ipAddress) {

		// 引数用のmapを作成
		Map<String, String> map = new HashMap<String, String>();
		map.put(LOG_KEY_SCREEN_NAME, screenName);
		map.put(LOG_KEY_ACTION, action);
		map.put(LOG_KEY_DETAIL, detail);
		map.put(LOG_KEY_IP_ADDRESS, ipAddress);

		// WebAPI実行
		BasicWebApiResDto resDto = super.execute(ACTION_PATH_REGISTER_LOG, map);

		// 戻り値設定
		return resDto;
	}
}
