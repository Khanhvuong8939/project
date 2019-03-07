/**
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionMessages;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ActionMessagesUtil;

import jp.co.forvaltel.common.constant.CommonConst;
import jp.co.forvaltel.common.dto.BasicWebApiResDto;
import jp.co.forvaltel.common.dto.OrderRegisterKakuchouDto;
import jp.co.forvaltel.common.entity.HcKakuchouData;
import jp.co.forvaltel.common.entity.HcShohinMst;
import jp.co.forvaltel.user.constant.UserConst;
import jp.co.forvaltel.user.dto.UserSessionDto;
import jp.co.forvaltel.user.form.AddOptionForm;
import jp.co.forvaltel.user.logic.LogRegisterLogic;
import jp.co.forvaltel.user.logic.OptionLogic;
import jp.co.forvaltel.user.logic.ShohinMstLogic;
import jp.co.forvaltel.user.util.LogUtils;

/**
 * オプション追加アクション
 *
 * @author smis
 */
public class AddOptionAction extends AbstractUserAction {

	/** JSPファイル名（入力） */
	private static final String JSP_FILE = "addOption.jsp";
	/** 契約情報紹介（遷移元画面） **/
	private static final String JSP_FILE_CONTRACT_INFO_QUERY = "/contractInfoQuery/";
	/** 拡張項目：表示区分 */
	private static final String HYOUJI_KBN = "2";
	/** 画面表示フラグ（入力画面） **/
	private static final String DISPLAY_FLG_INPUT = "input";
	/** 画面表示フラグ（完了画面） **/
	private static final String DISPLAY_FLG_FINISH = "finish";
//	TTM_DEV 20170615 ADD BEGIN
	/** gamen shikibetsu**/
	private static final String SCREEN = "USO";
//	TTM_DEV 20170615 ADD END
	/** アクションフォーム */
	@Resource
	@ActionForm
	public AddOptionForm addOptionForm;

	/** セッション情報 **/
	@Resource
	private UserSessionDto userSessionDto;
	/** 商品マスタロジック **/
	@Resource
	private ShohinMstLogic shohinMstLogic;
	/** オプションロジック **/
	@Resource
	private OptionLogic optionLogic;

	// TTM_DEV_11 APPLICATION_LOG BEGIN ADD
	@Resource
	LogRegisterLogic logRegisterLogic;
	// TTM_DEV_11 APPLICATION_LOG END ADD
	
	/**
	 * 初期表示（入力）
	 *
	 * @return 戻り先
	 */
	@Execute(validator = false, input = JSP_FILE_CONTRACT_INFO_QUERY)
	public String index() {

		// 商品セレクトボックス情報を取得
		BasicWebApiResDto resDto = this.shohinMstLogic.getChildShohinList(
										this.addOptionForm.getkJuchNo(), this.addOptionForm.getkJuchMeisaiNo());

		// JSONを変換
		List<HcShohinMst> resList = this.shohinMstLogic.changeJson(resDto);
		// Formに「商品リスト」を設定
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (HcShohinMst tmpMst:resList) {
			if ( CommonConst.SHOUHIN_HYOUJI_KBN_USER.equals(tmpMst.hyoujiKbn) ||
					CommonConst.SHOUHIN_HYOUJI_KBN_AGENT_USER.equals(tmpMst.hyoujiKbn)) {
				Object[] lineObj = {
						"itemCd", tmpMst.shohinCd,
				"itemDisplay", tmpMst.shohinMeiGamenHyji,
				};
				list.add(this.createValueMap(lineObj));
			}
		}
		// オプション追加画面
		this.addOptionForm.setDisplayFlg(DISPLAY_FLG_INPUT);
		this.addOptionForm.setItemCodeList(list);
		this.addOptionForm.setHyoujiKbn(HYOUJI_KBN);
		//TTM_DEV 20170615 ADD BEGIN
		this.addOptionForm.setScreen(SCREEN);
		//TTM_DEV 20170615 ADD END
		// TTM_DEV_11 APPLICATION_LOG BEGIN ADD
		BasicWebApiResDto logResDto = this.logRegisterLogic.registerLog(UserConst.LOG_SCREEN_CONTRACT_INFO, UserConst.LOG_BUTTON_OPTION_NEW, CommonConst.EMPTY_STRING, ipAddress);
		if(!logResDto.isResult()){
			LogUtils log = new LogUtils(this.getClass());
			log.error(UserConst.ERROR_MESSAGE_WRITE_LOG);
		}
		// TTM_DEV_11 APPLICATION_LOG END ADD
		
		// セッションに格納
		super.session.setAttribute("shohinList", this.addOptionForm.getItemCodeList());

		return JSP_FILE;
	}

	/**
	 * オプション登録処理
	 *
	 * @return 戻り先
	 */
	@SuppressWarnings("unchecked")
	@Execute(validator = true, input = JSP_FILE)
	public String addOption() {
		List<HcKakuchouData> hcKakuchouData = new ArrayList<HcKakuchouData>();
		// 拡張項目設定処理
		if (this.addOptionForm.getKakuchouDtoList() != null) {
			hcKakuchouData = this.setKakuchouList(this.addOptionForm.getKakuchouDtoList());
		}

		// TTM_DEV 20170719 BEGIN UPDATE
		// オプション追加処理
//		BasicWebApiResDto resDto = this.optionLogic.addOption(this.addOptionForm.getkJuchNo(),
//										this.addOptionForm.getShohinCd(), hcKakuchouData);
		BasicWebApiResDto resDto = this.optionLogic.addOption(this.userSessionDto.getUserId(), this.addOptionForm.getkJuchNo(),
										this.addOptionForm.getShohinCd(), hcKakuchouData);
		// TTM_DEV 20170719 BEGIN UPDATE
		
		// エラー判定
		if (resDto.getErrorCode() != null) {
			if (CommonConst.MSG_ID_ERRORS_VALIDATE.equals(resDto.getErrorCode())) {
				ActionMessages errors = new ActionMessages();
				List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
				list = (List<HashMap<String, Object>>) resDto.getReturnValue();
				// ActionMessageに整形
				for (HashMap<String, Object> wrkList:list) {
					String tmpKey = (String) wrkList.get("key");
					ArrayList<String> wrkValue = (ArrayList<String>) wrkList.get("values");
					String tmpValue = "";
					for (String wrkValueList:wrkValue) {
						tmpValue = tmpValue + wrkValueList + CommonConst.H_COMMA;
					}
					tmpValue = tmpValue.substring(0, tmpValue.length() - 1);
					this.addErrors(errors, tmpKey, tmpValue);
				}
				ActionMessagesUtil.addErrors(super.request, errors);
			} else {
				// エラーメッセージ表示を追加
				ActionMessages errors = new ActionMessages();
				this.addErrors(errors, resDto.getErrorCode());
				ActionMessagesUtil.addErrors(super.request, errors);
			}
			// オプション追加完了画面
			this.addOptionForm.setDisplayFlg(DISPLAY_FLG_INPUT);
			// オプション追加画面のURLを返却し、処理を終了する。
			return JSP_FILE;
		}

		// TTM_DEV_11 APPLICATION_LOG BEGIN ADD
		String shohinMei = CommonConst.EMPTY_STRING;
		// 商品セレクトボックス情報を取得
		BasicWebApiResDto resShohinDto = this.shohinMstLogic.getChildShohinList(
												this.addOptionForm.getkJuchNo(), this.addOptionForm.getkJuchMeisaiNo());

		// JSONを変換
		List<HcShohinMst> resShohinList = this.shohinMstLogic.changeJson(resShohinDto);
		for (HcShohinMst tmpMst:resShohinList) {
			if(tmpMst.shohinCd.equals(this.addOptionForm.getShohinCd())){
				shohinMei = tmpMst.shohinMei;
				break;
			}
		}
		String detail = CommonConst.EMPTY_STRING;
		detail = detail.concat(UserConst.LOG_TITLE_SHOHIN_CODE).concat(this.addOptionForm.getShohinCd());
		if(!StringUtils.isEmpty(shohinMei)){
			detail = detail.concat(CommonConst.LINE_SEPARATOR).concat(UserConst.LOG_TITLE_SHOHIN_NAME).concat(shohinMei);
		}
		
		BasicWebApiResDto logResDto = this.logRegisterLogic.registerLog(UserConst.LOG_SCREEN_OPTIONAL_INFO, UserConst.LOG_BUTTON_SHOHIN_NEW, detail, ipAddress);
		if(!logResDto.isResult()){
			LogUtils log = new LogUtils(this.getClass());
			log.error(UserConst.ERROR_MESSAGE_WRITE_LOG);
		}
		// TTM_DEV_11 APPLICATION_LOG END ADD
		
		// オプション追加完了画面
		this.addOptionForm.setDisplayFlg(DISPLAY_FLG_FINISH);

		// オプション追加完了画面へ遷移する。
		return JSP_FILE;
	}

	/**
	 * 入力チェック処理
	 *
	 * @return ActionMessages　エラーメッセージオブジェクト
	 */
	public ActionMessages check() {
		// 戻り値生成
		ActionMessages errors = new ActionMessages();



		return errors;
	}
	/**
	 * 検索結果を画面表示用のマップに変換します。
	 * @param objArray オブジェクト
	 * @return map
	 */
	private Map<String, Object> createValueMap(Object... objArray) {
		Map<String, Object> map = new HashMap<String, Object>();
		int size = objArray.length;

		for (int i = 0; i < size; i++) {
			if ((i + 1) % 2 != 0) {
				map.put(objArray[i].toString(), objArray[i + 1]);
			}
		}
		return map;
	}
	/**
	 * 拡張項目設定処理
	 * @param kakuchouList 拡張項目リスト（入力値）
	 * @return List<HcKakuchouData>
	 */
	private List<HcKakuchouData> setKakuchouList(List<OrderRegisterKakuchouDto> kakuchouList) {

		List<HcKakuchouData> retKakuchoList = new ArrayList<HcKakuchouData>();
		for (OrderRegisterKakuchouDto wrkList : kakuchouList) {
			HcKakuchouData wrkKakucho = new HcKakuchouData();
			// 項目コード
			wrkKakucho.koumokuCd = wrkList.getKoumokuCd();
			// 項目値
			wrkKakucho.koumokuAtai = wrkList.getInputValue();
			// 商品コード
			wrkKakucho.shohinCd = wrkList.getSrvShohinCd();
			// 基幹連携ステータス
			wrkKakucho.kikanStatus = CommonConst.KIKAN_STATUS_NONE;
			retKakuchoList.add(wrkKakucho);

		}

		return retKakuchoList;
	}
}
