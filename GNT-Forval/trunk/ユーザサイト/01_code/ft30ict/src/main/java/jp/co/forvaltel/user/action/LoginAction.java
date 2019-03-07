/**
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.action;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.forvaltel.common.annotation.InitDisp;
import jp.co.forvaltel.common.constant.CodeMstConst;
import jp.co.forvaltel.common.constant.CommonConst;
import jp.co.forvaltel.common.dto.BasicWebApiResDto;
import jp.co.forvaltel.common.dto.SessionInfoDto;
import jp.co.forvaltel.common.dto.UserDetailDto;
import jp.co.forvaltel.common.entity.HcJuchMeisai;
import jp.co.forvaltel.common.entity.HcKakuchouData;
import jp.co.forvaltel.common.entity.HcKyakuMst;
import jp.co.forvaltel.common.entity.HcSeiqMst;
import jp.co.forvaltel.common.exception.FtApplicationException;
import jp.co.forvaltel.common.exception.FtSystemException;
import jp.co.forvaltel.common.util.CheckUtils;
import jp.co.forvaltel.common.util.FtResourcesUtil;
import jp.co.forvaltel.common.util.JsonUtils;
import jp.co.forvaltel.common.util.KakuchouUtils;
import jp.co.forvaltel.common.util.SessionUtils;
import jp.co.forvaltel.common.util.StringUtils;
import jp.co.forvaltel.user.annotation.TokenNoCheck;
import jp.co.forvaltel.user.constant.UserConst;
import jp.co.forvaltel.user.dto.FirstLoginInfoDto;
import jp.co.forvaltel.user.dto.FirstLoginViewInfoDto;
import jp.co.forvaltel.user.form.LoginForm;
import jp.co.forvaltel.user.logic.KakuchouLogic;
import jp.co.forvaltel.user.logic.KyakuMstLogic;
import jp.co.forvaltel.user.logic.LogRegisterLogic;
import jp.co.forvaltel.user.logic.LoginFlgUpdateLogic;
import jp.co.forvaltel.user.logic.OrderDetailDataLogic;
import jp.co.forvaltel.user.logic.OrderNoLogic;
import jp.co.forvaltel.user.logic.SeiqMstLogic;
import jp.co.forvaltel.user.util.LogUtils;

import org.apache.log4j.MDC;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ActionMessagesUtil;
import org.seasar.struts.util.MessageResourcesUtil;
import org.seasar.struts.util.RequestUtil;

/**
 * ログインアクション
 *
 * @author smis
 */
public class LoginAction extends AbstractUserAction {

	/** JSPファイル名 */
	private static final String JSP_FILE = "login.jsp";

	/** JSPファイル名（初回ログイン） */
	private static final String JSP_FIRST_FILE = "firstLogin.jsp";

	/** アクションパス 初回ログイン表示 */
	private static final String ACTION_PATH_LOGIN_FIRST = "first";

	/** アクションパス 契約情報照会画面 */
	private static final String ACTION_PATH_CONTRACT_INFO_QUERY = "/contractInfoQuery";

	/** アクションパス 支払先情報登録(クレジット) */
	private static final String ACTION_PATH_PAYMENT_CREDIT = "/paymentCreditRegister";

	/** アクションパス 支払先情報登録(NTT合算)画面 */
	private static final String ACTION_PATH_PAYMENT_ADDING_UP = "/paymentAddingUpRegister";

	/** キー テナントID */
	private static final String ITEM_KEY_TENANT_ID = "system.tenantid";

	/** 支払方法確定フラグ：確定後 */
	private static final String HARAI_HOUHOU_KAKUTEI_FLAG_UNDECISION = "1";

	/** アクションフォーム */
	@Resource
	@ActionForm
	public LoginForm loginForm;

	/** 初回ログイン表示情報 */
	@Resource
	public FirstLoginViewInfoDto firstLoginViewInfoDto;

	/**
	 * 請求先マスタ取得ロジック
	 */
	@Resource
	SeiqMstLogic seiqMstLogic;
	// TTM_DEV USER_SITE_FIRST_MODULE BEGIN ADD
	/**
	 *kyakuMstLogic
	 */
	@Resource
	KyakuMstLogic kyakuMstLogic;
	// TTM_DEV USER_SITE_FIRST_MODULE END ADD
	/**
	 * 受注番号取得ロジック
	 */
	@Resource
	OrderNoLogic orderNoLogic;

	/**
	 * 受注詳細情報取得ロジック
	 */
	@Resource
	OrderDetailDataLogic orderDetailDataLogic;

	/**
	 * 拡張項目取得ロジック
	 */
	@Resource
	KakuchouLogic kakuchouLogic;

	/**
	 * 初回ログインフラグ更新ロジック
	 */
	@Resource
	LoginFlgUpdateLogic loginFlgUpdateLogic;
	
	// TTM_DEV_11 APPLICATION_LOG BEGIN ADD
	@Resource
	LogRegisterLogic logRegisterLogic;
	// TTM_DEV_11 APPLICATION_LOG END ADD
	
	
	/**
	 * 初期表示
	 *
	 * @return 戻り先
	 */
	@TokenNoCheck
	@InitDisp
	@Execute(validator = false, input = JSP_FILE)
	public String index() {
		// ドメイン名取得
		String domain = super.request.getServerName();

		// エラーメッセージを表示するか判定
		if (super.session.getAttribute(UserConst.S_KEY_SESSION_ERROR_MSG) != null) {
			// セッションエラーメッセージ表示
			ActionMessages errors = new ActionMessages();
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(UserConst.MSG_ID_ERRORS_SESSION));
			ActionMessagesUtil.addErrors(RequestUtil.getRequest(), errors);
			// SessionからSessionタイムアウトエラー表示を削除
			super.session.removeAttribute(UserConst.S_KEY_SESSION_ERROR_MSG);
		}

		// セッション情報にドメイン名を設定
		SessionInfoDto dto = SessionUtils.getFtSessionInfo();
		dto.setLabelViewType(domain);
		SessionUtils.setFtSessionInfo(dto);

		// テナントIDを取得し、ユーザサイト用のSession情報に設定
		String tenantId = FtResourcesUtil.getItemValue(ITEM_KEY_TENANT_ID);
		userSessionDto.setTenantId(tenantId);

		return JSP_FILE;
	}

	/**
	 * ログイン処理
	 *
	 * @return 戻り先
	 */
	@TokenNoCheck
	@Execute(validator = true, input = JSP_FILE)
	public String login() {
		// ユーザ名とパスワードをセッション情報に設定
		//TTM_DEV 20170706 BEGIN UPDATE
//		userSessionDto.setUserId(this.loginForm.getLoginId());
//		userSessionDto.setPassword(this.loginForm.getPassword());
		userSessionDto.setUserId(this.loginForm.getLoginId().trim());
		userSessionDto.setPassword(this.loginForm.getPassword().trim());
		//TTM_DEV 20170706 END UPDATE

		// 遷移先パス取得
		String actionPath = this.getActionPath();

		// 戻り値設定
		return actionPath;
	}

	/**
	 * 初回ログイン画面表示
	 *
	 * @return 戻り先
	 */
	@InitDisp
	@Execute(validator = false, input = JSP_FILE)
	public String first() {
		// 受注番号取得処理
		List<String> juchNoList = this.orderNoLogic.getOrderNoList();

		// 受注が存在しない場合、アプリケーションエラーとする。
		if (CheckUtils.isEmpty(juchNoList)) {
			// ユーザIDとパスワードクリア
			this.clearPassAndUser();
			// 　存在しないはずはないのでシステム例外
			throw new FtSystemException(new IllegalArgumentException());
		}

		// TTM_DEV USER_SITE_FIRST_MODULE BEGIN ADD
		List<FirstLoginInfoDto> dtoList = new ArrayList<FirstLoginInfoDto>();
		for(String juchNo : juchNoList){
			// 明細取得
			BasicWebApiResDto dto = this.orderDetailDataLogic.referDetail(juchNo);
			
			// 処理結果が異常の場合
			if (!dto.isResult()) {
				// ユーザIDとパスワードクリア
				this.clearPassAndUser();
				// 業務例外をスロー
				throw new FtApplicationException(dto.getErrorCode());
			}

			// ユーザ詳細情報DTO取得
			UserDetailDto userDetailDto = JsonUtils.decode(dto.getReturnValue(), UserDetailDto.class);
		
			// 契約者マスタの顧客区分をセッションに保存
			if (!CheckUtils.isEmpty(userDetailDto.getHcKyakuMst())) {
				userSessionDto.setKokyakuKbn(userDetailDto.getHcKyakuMst().kokyakuKbn);
			}

			// 請求先マスタのVersion保存
			if (!CheckUtils.isEmpty(userDetailDto.getHcSeiqMst())) {
				this.loginForm.setVersion(userDetailDto.getHcSeiqMst().version.toString());
			}
			
			FirstLoginInfoDto firstLoginDto = new FirstLoginInfoDto();
			
			// 取得した受注明細から親明細の拡張項目情報設定
			for (HcJuchMeisai meisai : userDetailDto.getHcJuchMeisai()) {
				// 親受注明細番号が設定されていた場合、次のレコードへ
				if (!CheckUtils.isEmpty(meisai.oyaJuchMeisaiNo)) {
					continue;
				}

				// サービス名設定
				firstLoginDto.setSrvName(meisai.srvName);

				// 拡張項目関連情報設定
				this.settingFirstLoginKakuchouData(meisai, firstLoginDto);

				// 存在した場合、ループ終了
				break;
			}
			
			dtoList.add(firstLoginDto);
		}
		this.firstLoginViewInfoDto.firstLoginDtoList = dtoList;
		// TTM_DEV USER_SITE_FIRST_MODULE END ADD
		
		// TTM_DEV USER_SITE_FIRST_MODULE BEGIN DELETE
		// 受注番号の先頭を表示
//		String juchNo = juchNoList.get(0);
//		// 明細取得
//		BasicWebApiResDto dto = this.orderDetailDataLogic.referDetail(juchNo);
//		
//		// 処理結果が異常の場合
//		if (!dto.isResult()) {
//			// ユーザIDとパスワードクリア
//			this.clearPassAndUser();
//			// 業務例外をスロー
//			throw new FtApplicationException(dto.getErrorCode());
//		}
//
//		// ユーザ詳細情報DTO取得
//		UserDetailDto userDetailDto = JsonUtils.decode(dto.getReturnValue(), UserDetailDto.class);
//
//		// 契約者マスタの顧客区分をセッションに保存
//		if (!CheckUtils.isEmpty(userDetailDto.getHcKyakuMst())) {
//			userSessionDto.setKokyakuKbn(userDetailDto.getHcKyakuMst().kokyakuKbn);
//		}
//
//		// 請求先マスタのVersion保存
//		if (!CheckUtils.isEmpty(userDetailDto.getHcSeiqMst())) {
//			this.loginForm.setVersion(userDetailDto.getHcSeiqMst().version.toString());
//		}
//
//		// 取得した受注明細から親明細の拡張項目情報設定
//		for (HcJuchMeisai meisai : userDetailDto.getHcJuchMeisai()) {
//			// 親受注明細番号が設定されていた場合、次のレコードへ
//			if (!CheckUtils.isEmpty(meisai.oyaJuchMeisaiNo)) {
//				continue;
//			}
//
//			// サービス名設定
//			this.firstLoginViewInfoDto.srvName = meisai.srvName;
//
//			// 拡張項目関連情報設定
//			this.settingFirstLoginKakuchouData(meisai);
//
//			// 存在した場合、ループ終了
//			break;
//		}
		// TTM_DEV USER_SITE_FIRST_MODULE END DELETE
		return JSP_FIRST_FILE;
	}

	/**
	 * 画面遷移処理
	 *
	 * @return 戻り先
	 */
	@Execute(validator = false, input = JSP_FILE)
	public String movePage() {

		// フラグ更新
		Integer version = Integer.parseInt(this.loginForm.getVersion());
		BasicWebApiResDto dto = this.loginFlgUpdateLogic.updateFlg(version);

		// 処理結果確認
		if (!dto.isResult()) {
			// ユーザIDとパスワードクリア
			this.clearPassAndUser();
		}
		// 遷移先パス取得及び戻り値設定
		return this.getActionPath();
	}

	/**
	 * 遷移先ActionPath取得
	 *
	 * @return 遷移先のActionPath
	 */
	private String getActionPath() {
		// TTM_DEV USER_SITE_FIRST_MODULE BEGIN UPDATE
		// 請求先情報取得
		List<HcSeiqMst> list = null;
		List<HcKyakuMst> kyakuList = null;
		
		try {
			list = this.seiqMstLogic.getSeiqMstList();
			kyakuList = this.kyakuMstLogic.getKyakuList();
		} catch (FtSystemException ftEx) {
			// 認証エラー以外の場合、そのまま上位にスロー
			if (!MessageResourcesUtil.getMessage(CommonConst.MSG_ID_ERRORS_COLLATE).equals(ftEx.getMessage())) {
				throw ftEx;
			}
		}
	
		// 請求先が取得できなかった場合、認証エラー
		if (CheckUtils.isEmpty(list) || CheckUtils.isEmpty(kyakuList)) {
			// ユーザIDとパスワードクリア
			this.clearPassAndUser();
			// 認証エラーのメッセージ表示
			throw new FtApplicationException(UserConst.MSG_ID_ERRORS_FAILURE_AUTH);
		}
		// TTM_DEV USER_SITE_FIRST_MODULE END UPDATE
		
		// TTM_DEV_11 APPLICATION_LOG BEGIN ADD
		if(!StringUtils.nullToEmptyString(this.loginForm.getLoginId()).equals(CommonConst.EMPTY_STRING)){
			BasicWebApiResDto logResDto = this.logRegisterLogic.registerLog(UserConst.LOG_SCREEN_TITLE_LOGIN, UserConst.LOG_BUTTON_TITLE_LOGIN, UserConst.LOG_TITLE_USER.concat(this.loginForm.getLoginId()), ipAddress);
			if(!logResDto.isResult()){
				LogUtils log = new LogUtils(this.getClass());
				log.error(UserConst.ERROR_MESSAGE_WRITE_LOG);
			}
		}
		// TTM_DEV_11 APPLICATION_LOG END ADD
		
		// リストから最初のIndexのオブジェクト取得
		HcSeiqMst hcSeiqMst = list.get(0);
		// TTM_DEV USER_SITE_FIRST_MODULE BEGIN ADD
		HcKyakuMst hcKyakuMst = kyakuList.get(0);
		// TTM_DEV USER_SITE_FIRST_MODULE END ADD

		// セッションに付加情報設定
		userSessionDto.setKkykBng(hcSeiqMst.kkykBng);
		userSessionDto.setSeiqCd(hcSeiqMst.seiqCd);
		userSessionDto.setSeiqMei(hcSeiqMst.seiqMei);

		// TTM_DEV USER_SITE_FIRST_MODULE BEGIN UPDATE
		// 遷移先判定
		String actionPath;
		if (CheckUtils.isNotEmpty(hcKyakuMst.kaiyakuDate)){
			actionPath = ACTION_PATH_CONTRACT_INFO_QUERY;
		} else {
			if (CommonConst.FLG_TRUE.equals(hcSeiqMst.firstLoginFlag)) {
				// 初回ログインフラグがTrueの場合、初回ログイン画面　初期表示画面へ
				actionPath = ACTION_PATH_LOGIN_FIRST;

				// 取得した請求マスタ中の支払方法確定フラグを判定する。
				// 支払方法確定フラグが確定後
			} else if (HARAI_HOUHOU_KAKUTEI_FLAG_UNDECISION.equals(hcSeiqMst.haraiHouhouKakuteiFlag)) {

				// 契約情報照会画面に遷移
				actionPath = ACTION_PATH_CONTRACT_INFO_QUERY;
			} else if (this.isGoToContractInfoQuery(hcSeiqMst)) {
				// 契約情報照会画面に遷移
				actionPath = ACTION_PATH_CONTRACT_INFO_QUERY;
			} else if (CodeMstConst.HARAI_KBN_CREDIT.equals(hcSeiqMst.haraiKbn)) {
				this.setShiarai();
				// 支払先情報登録(クレジット)　に遷移
				actionPath = ACTION_PATH_PAYMENT_CREDIT;
			} else if (CodeMstConst.HARAI_KBN_NTTADDUP.equals(hcSeiqMst.haraiKbn)) {
				this.setShiarai();
				// 支払先情報登録(NTT合算)画面に遷移
				actionPath = ACTION_PATH_PAYMENT_ADDING_UP;
			} else {
				//TTM_DEV 20170724 BEGIN UPDATE
				// ユーザIDとパスワードクリア
//				this.clearPassAndUser();
				// 上記以外の場合、想定外のデータのためシステムエラーとする。
//				throw new FtSystemException(new IllegalArgumentException());
				// 契約情報照会画面に遷移
				actionPath = ACTION_PATH_CONTRACT_INFO_QUERY;
				//TTM_DEV 20170724 END UPDATE
			}
		}
		// TTM_DEV USER_SITE_FIRST_MODULE END UPDATE
		
		// 戻り値設定
		return actionPath;
	}

	/**
	 * 初回ログイン画面拡張項目情報設定処理
	 *
	 * @param meisai 受注明細
	 */
	private void settingFirstLoginKakuchouData(HcJuchMeisai meisai) {
		// 親明細の拡張項目情報取得
		BasicWebApiResDto kakuchouReDto = this.kakuchouLogic.getkakuchouDataList(meisai.juchNo, meisai.juchMeisaiNo);

		// 取得できない場合処理を行わない。
		if (!kakuchouReDto.isResult()) {
			return;
		}

		// ---表示する拡張項目データ情報設定---
		// 取得したリストから表示する対象の項目コード以外除外する
		for (Object object : kakuchouReDto.getReturnValueList()) {
			// 対象のオブジェクトをdecode(キャスト)
			HcKakuchouData hcKakuchouData = JsonUtils.decode(object, HcKakuchouData.class);

			// インタネット接続ＩＤ、パスワード、初回請求起算日、ご利用料金以外は表示対象外
			if (KakuchouUtils.isCode(hcKakuchouData.koumokuCd, KakuchouUtils.CodeType.AuthAccount)) {
				// インタネット接続ＩＤ
				this.firstLoginViewInfoDto.authAccountLabel = hcKakuchouData.koumokuMei;
				this.firstLoginViewInfoDto.authAccountValue = hcKakuchouData.koumokuAtai;

			} else if (KakuchouUtils.isCode(hcKakuchouData.koumokuCd, KakuchouUtils.CodeType.AuthPwd)) {
				// パスワード
				this.firstLoginViewInfoDto.authPwLabel = hcKakuchouData.koumokuMei;
				this.firstLoginViewInfoDto.authPwValue = hcKakuchouData.koumokuAtai;

			} else if (KakuchouUtils.isCode(hcKakuchouData.koumokuCd, KakuchouUtils.CodeType.InitialClaimValueDate)) {
				// 初回請求起算日
				this.firstLoginViewInfoDto.initialClaimValueDateLabel = hcKakuchouData.koumokuMei;
				this.firstLoginViewInfoDto.initialClaimValueDateValue = hcKakuchouData.koumokuAtai;

			} else if (KakuchouUtils.isCode(hcKakuchouData.koumokuCd, KakuchouUtils.CodeType.UsageFee)) {
				// ご利用料金
				this.firstLoginViewInfoDto.usageFeeLabel = hcKakuchouData.koumokuMei;
				this.firstLoginViewInfoDto.usageFeeValue = hcKakuchouData.koumokuAtai;
			}
		}

	}
	// TTM_DEV USER_SITE_FIRST_MODULE BEGIN ADD
	/**
	 * 初回ログイン画面拡張項目情報設定処理
	 *
	 * @param meisai 受注明細
	 */
	private void settingFirstLoginKakuchouData(HcJuchMeisai meisai, FirstLoginInfoDto firstLoginInfoDto) {
		// 親明細の拡張項目情報取得
		BasicWebApiResDto kakuchouReDto = this.kakuchouLogic.getkakuchouDataList(meisai.juchNo, meisai.juchMeisaiNo);

		// 取得できない場合処理を行わない。
		if (!kakuchouReDto.isResult()) {
			return;
		}

		// ---表示する拡張項目データ情報設定---
		// 取得したリストから表示する対象の項目コード以外除外する
		for (Object object : kakuchouReDto.getReturnValueList()) {
			// 対象のオブジェクトをdecode(キャスト)
			HcKakuchouData hcKakuchouData = JsonUtils.decode(object, HcKakuchouData.class);

			// インタネット接続ＩＤ、パスワード、初回請求起算日、ご利用料金以外は表示対象外
			if (KakuchouUtils.isCode(hcKakuchouData.koumokuCd, KakuchouUtils.CodeType.AuthAccount)) {
				// インタネット接続ＩＤ
				firstLoginInfoDto.setAuthAccountLabel(hcKakuchouData.koumokuMei);
				firstLoginInfoDto.setAuthAccountValue(hcKakuchouData.koumokuAtai);
				
			} else if (KakuchouUtils.isCode(hcKakuchouData.koumokuCd, KakuchouUtils.CodeType.AuthPwd)) {
				// パスワード
				firstLoginInfoDto.setAuthPwLabel(hcKakuchouData.koumokuMei);
				firstLoginInfoDto.setAuthPwValue(hcKakuchouData.koumokuAtai);
				
			} else if (KakuchouUtils.isCode(hcKakuchouData.koumokuCd, KakuchouUtils.CodeType.InitialClaimValueDate)) {
				// 初回請求起算日
				firstLoginInfoDto.setInitialClaimValueDateLabel(hcKakuchouData.koumokuMei);
				firstLoginInfoDto.setInitialClaimValueDateValue(hcKakuchouData.koumokuAtai);

			} else if (KakuchouUtils.isCode(hcKakuchouData.koumokuCd, KakuchouUtils.CodeType.UsageFee)) {
				// ご利用料金
				firstLoginInfoDto.setUsageFeeLabel(hcKakuchouData.koumokuMei);
				firstLoginInfoDto.setUsageFeeValue(hcKakuchouData.koumokuAtai);
			}
		}
	}
	// TTM_DEV USER_SITE_FIRST_MODULE END ADD
	/**
	 * セッション情報クリア(テナントID以外)
	 */
	private void clearPassAndUser() {
		userSessionDto.setUserId(null);
		userSessionDto.setPassword(null);
		userSessionDto.setSeiqCd(null);
		userSessionDto.setSeiqMei(null);
		userSessionDto.setKkykBng(null);
	}

	/**
	 * 契約情報照会画面遷移条件判定
	 *
	 * @param hcSeiqMst 契約マスタエンティティ
	 * @return true:契約情報照会へ遷移/false:契約情報照会へ遷移しない
	 */
	private boolean isGoToContractInfoQuery(HcSeiqMst hcSeiqMst) {
		// 戻り値初期化
		boolean ret = false;
		// TTM_DEV USER_SITE_FIRST_MODULE BEGIN UPDATE
		// 支払区分ステータスが申請中 or 支払区分ステータスが引落し可 or 支払区分が口座振替
//		if (CodeMstConst.HARAI_KBN_ACOUNT.equals(hcSeiqMst.haraiKbn)
		if ((!CodeMstConst.HARAI_KBN_CREDIT.equals(hcSeiqMst.haraiKbn) && !CodeMstConst.HARAI_KBN_NTTADDUP.equals(hcSeiqMst.haraiKbn))
				|| CodeMstConst.HARAI_KBN_STATUS_OK.equals(hcSeiqMst.haraiKbnStatus)
				|| CodeMstConst.HARAI_KBN_STATUS_PENDING.equals(hcSeiqMst.haraiKbnStatus)){
//				|| CodeMstConst.HARAI_KBN_NEW.equals(hcSeiqMst.haraiKbn)) {
			ret = true;
		}
		// TTM_DEV USER_SITE_FIRST_MODULE END UPDATE
		return ret;
	}
	
	// TTM_DEV 20171123 BEGIN ADD
	private void setShiarai() {

		// 受注番号取得処理
		List<String> resDtoOrderNoList = this.orderNoLogic.getOrderNoList();
		// 受注詳細取得
		BasicWebApiResDto resDto = this.orderDetailDataLogic.referDetail(resDtoOrderNoList.get(0));
		// ホームページ情報、メール情報で使用する。
		UserDetailDto userDetailDto = JsonUtils.decode(resDto.getReturnValue(), UserDetailDto.class);
		
		this.userSessionDto.setHcDairitenShiharaiHimoMstList(userDetailDto.getHcDairitenShiharaiHimoMst());
	}
	// TTM_DEV 20171123 END ADD
}
