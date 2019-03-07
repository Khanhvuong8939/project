/**
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.action;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import jp.co.forvaltel.common.annotation.InitDisp;
import jp.co.forvaltel.common.constant.CodeMstConst;
import jp.co.forvaltel.common.constant.CommonConst;
import jp.co.forvaltel.common.dto.BasicWebApiResDto;
import jp.co.forvaltel.common.dto.EbillSsoParameterDto;
import jp.co.forvaltel.common.dto.MailAccountDto;
import jp.co.forvaltel.common.dto.MailUserDto;
import jp.co.forvaltel.common.dto.UserDetailDto;
import jp.co.forvaltel.common.dto.WebAccountDto;
import jp.co.forvaltel.common.entity.HcCodeMst;
import jp.co.forvaltel.common.entity.HcDairitenShiharaiHimoMst;
import jp.co.forvaltel.common.entity.HcJuchMeisai;
import jp.co.forvaltel.common.entity.HcKakuchouData;
import jp.co.forvaltel.common.entity.HcKyakuMst;
import jp.co.forvaltel.common.entity.HcSeiqMst;
import jp.co.forvaltel.common.entity.HcShohinMst;
import jp.co.forvaltel.common.exception.FtApplicationException;
import jp.co.forvaltel.common.exception.FtSystemException;
import jp.co.forvaltel.common.util.CheckUtils;
import jp.co.forvaltel.common.util.DateUtils;
import jp.co.forvaltel.common.util.FtResourcesUtil;
import jp.co.forvaltel.common.util.JsonUtils;
import jp.co.forvaltel.common.util.KakuchouUtils;
import jp.co.forvaltel.user.constant.UserConst;
import jp.co.forvaltel.user.dto.HpInfoDto;
import jp.co.forvaltel.user.dto.ItemInfoDto;
import jp.co.forvaltel.user.dto.JuchKihonInfoDto;
import jp.co.forvaltel.user.dto.MailInfoDto;
import jp.co.forvaltel.user.form.ContractInfoQueryForm;
import jp.co.forvaltel.user.logic.CodeMstLogic;
import jp.co.forvaltel.user.logic.HpInfoDeleteLogic;
import jp.co.forvaltel.user.logic.HpInfoReferenceLogic;
import jp.co.forvaltel.user.logic.LogRegisterLogic;
import jp.co.forvaltel.user.logic.MailAccountDeleteLogic;
import jp.co.forvaltel.user.logic.MailAccountReferenceLogic;
import jp.co.forvaltel.user.logic.OptionCancelCancelLogic;
import jp.co.forvaltel.user.logic.OptionCancelLogic;
import jp.co.forvaltel.user.logic.OptionDeleteLogic;
import jp.co.forvaltel.user.logic.OrderDetailDataLogic;
import jp.co.forvaltel.user.logic.OrderNoLogic;
import jp.co.forvaltel.user.logic.ShohinMstLogic;
import jp.co.forvaltel.user.util.LogUtils;

import org.apache.struts.action.ActionMessage;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

/**
 * 契約情報照会アクション
 *
 * @author smis
 */
public class ContractInfoQueryAction extends AbstractUserAction {

	/** JSPファイル名 */
	private static final String JSP_FILE = "contractInfoQuery.jsp";
	/** JSPファイル名（完了） */
	private static final String JSP_FILE_FINISH = "finish.jsp";
	/** 拡張項目：表示区分 */
	private static final String HYOUJI_KBN = "3";
	/** 実行メソッド名：口座振替 */
	private static final String ACCOUNT = "accountTransfer";
	/** 実行メソッド名：クレジット */
	private static final String CREDIT = "paymentCreditRegister";
	/** 実行メソッド名：NTT合算 */
	private static final String NTT = "paymentAddingUpRegister";
	/** SP社APIエラーメッセージ */
	private static final String SP_API_ERROR = "errors.sp.api";
	//	/** NTT東西 */
	//	private static final String NTTEW = "contract_info_query.label.142";
	/**	shohin parrent */
	//private static final String SHOHIN_PARRENT = "0023";

	//TTM_DEV 20170622 ADD BEGIN
	/**SCREEN*/
	private static final String SCREEN = "USS";
	//TTM_DEV 20170622 ADD END
	/** アクションフォーム */
	@Resource
	@ActionForm
	public ContractInfoQueryForm contractInfoQueryForm;

	/**
	 * コードマスタ取得APIロジック
	 */
	@Resource
	CodeMstLogic codeMstLogic;

	/**
	 * 受注番号取得ロジック
	 */
	@Resource
	OrderNoLogic orderNoLogic;

	/**
	 * 受注明細情報取得ロジック
	 */
	@Resource
	OrderDetailDataLogic orderDetailDataLogic;

	/**
	 * オプションキャンセルAPIロジック
	 */
	@Resource
	OptionDeleteLogic optionDeleteLogic;

	/**
	 * オプション解約APIロジック
	 */
	@Resource
	OptionCancelLogic optionCancelLogic;

	/**
	 * オプション解約取消APIロジック
	 */
	@Resource
	OptionCancelCancelLogic OptionCancelCancelLogic;

	/**
	 * ホームページ削除APIロジック  
	 */
	@Resource
	HpInfoDeleteLogic hpInfoDeleteLogic;

	/**
	 * ホームページ情報照会APIロジック
	 */
	@Resource
	HpInfoReferenceLogic hpInfoReferenceLogic;

	/**
	 * メールアカウント削除APIロジック
	 */
	@Resource
	MailAccountDeleteLogic mailAccountDeleteLogic;

	/**
	 * メールアカウント情報照会APIロジック
	 */
	@Resource
	MailAccountReferenceLogic mailAccountReferenceLogic;
	
	// TTM_DEV_11 APPLICATION_LOG BEGIN ADD
	@Resource
	LogRegisterLogic logRegisterLogic;

	//
	@Resource
	ShohinMstLogic shohinMstLogic;

	// TTM_DEV_11 APPLICATION_LOG END ADD

	/**
	 * 初期表示（入力）
	 *
	 * @return 戻り先
	 */
	@InitDisp
	@Execute(validator = false, input = JSP_FILE)
	public String index() {

		// 保持しているForm情報を削除
		this.contractInfoQueryForm.resetForm();
	
		// バナー情報取得
		this.contractInfoQueryForm.setBannerImgLink(
				this.codeMstLogic.get(CodeMstConst.ID_BANNER_INFO, CodeMstConst.BANNER_INFO_IMG).biko);
		this.contractInfoQueryForm.setBannerUrlLink(
				this.codeMstLogic.get(CodeMstConst.ID_BANNER_INFO, CodeMstConst.BANNER_INFO_URL).biko);
		this.contractInfoQueryForm.setScreen(SCREEN);
		// 受注番号取得処理
		List<String> resDtoOrderNoList = this.orderNoLogic.getOrderNoList();

		// TTM_DEV USER_SITE_CONTRACT_INFO_MODULE BEGIN UPDATE
		List<JuchKihonInfoDto> juchKihonInfoDtoList = new ArrayList<JuchKihonInfoDto>();
		boolean isSetContractInfo = false;
		
		for(String juchNo : resDtoOrderNoList){
			
			// ホームページ情報、メール情報で使用する。
			UserDetailDto userDetailDto = new UserDetailDto();

			List<HcKakuchouData> hcKakuchouDataList = new LinkedList<HcKakuchouData>();

			// 契約情報
			// 受注詳細取得
			BasicWebApiResDto resDto = this.orderDetailDataLogic.referDetail(juchNo);

			JuchKihonInfoDto juchKihonInfoDto = new JuchKihonInfoDto();

			// 戻り値用オブジェクトに結果を複写
			if (resDto.isResult()) {

				// ユーザ詳細情報DTO
				//List<String> a = JsonUtils.decode(resDto.getReturnValue(), String.class);
				userDetailDto = JsonUtils.decode(resDto.getReturnValue(), UserDetailDto.class);

				// 契約情報設定
				//TTM_DEV 20170307 UPDATE BEGIN
				if(!isSetContractInfo){
					this.setContractInfo(userDetailDto, juchKihonInfoDto, isSetContractInfo);
				}
				//TTM_DEV 20170307 UPDATE BEGIN
//				this.setContractInfo(userDetailDto, coopDto);

				// eBill設定
				this.setEbillSsoParam(userDetailDto);

				//set Disp Flag
				this.setDispFlag(userDetailDto, juchKihonInfoDto);

				// 受注詳細取得で取得した拡張項目リストを生成
				for (Object obj : resDto.getReturnValueList()) {
					HcKakuchouData hcKakuchouData = JsonUtils.decode(obj, HcKakuchouData.class);
					hcKakuchouDataList.add(hcKakuchouData);
				}
				// ホームページ情報設定
				juchKihonInfoDto.setInternetConnectionId(KakuchouUtils.getKoumokuAtai(hcKakuchouDataList, KakuchouUtils.CodeType.AuthAccount));
				//				juchKihonInfoDto.setInternetConnectionId("FA0012BE@fm.testdomain.ne.jp");

				juchKihonInfoDto.setAccountClass(UserConst.ACCOUNT_CLASS_RAD_ID);
					
				List<MailInfoDto> mailInfoDtoList = this.setMailInfo(hcKakuchouDataList,userDetailDto);
				// メール情報取得
				juchKihonInfoDto.setMailInfoDtoList(mailInfoDtoList);
				HpInfoDto hpInfoDto = this.setHpInfo(hcKakuchouDataList, userDetailDto);
				juchKihonInfoDto.setHpInfoDto(hpInfoDto);
					
				// 拡張項目設定
				List<ItemInfoDto> itemInfoDtoList = this.setExtension(userDetailDto, juchKihonInfoDto);

				juchKihonInfoDto.setItemInfoDtoList(itemInfoDtoList);
				// TTM_DEV BEGIN ADD 20180327
				juchKihonInfoDto.setKaiyakuKubun(userDetailDto.getHcJuchKihon().kaiyakuKubun);
				// TTM_DEV END ADD 20180327
				juchKihonInfoDtoList.add(juchKihonInfoDto);

			} //end if result
		} // end for()

		
		this.contractInfoQueryForm.setJuchKihonInfoDtoList(juchKihonInfoDtoList);

		// TTM_DEV USER_SITE_CONTRACT_INFO_MODULE BEGIN UPDATE

		return JSP_FILE;
	}

	/**
	 * set hpDispFlg and mailDispFlg 
	 * @param userDetailDto
	 * @param juchKihonInfoDto
	 */
	private void setDispFlag(UserDetailDto userDetailDto, JuchKihonInfoDto juchKihonInfoDto){
		//declare hcJuchMeisai Parrent
		HcJuchMeisai hcJuchMeisaiParrent = new HcJuchMeisai();
		//find hcJuchMeisai Parrent in list HcJuchMeisai in userDetailDto
		for (HcJuchMeisai hcJuchMeusai : userDetailDto.getHcJuchMeisai()){
			//TTM_DEV 20180423 BEGIN EDIT
//			if (hcJuchMeusai.srvCd.equals(SHOHIN_PARRENT) ){
//				hcJuchMeisaiParrent = hcJuchMeusai;
//				break;
//			}
			if(hcJuchMeusai.juchMeisaiNo.equals(CommonConst.FLG_TRUE)){
				hcJuchMeisaiParrent = hcJuchMeusai;
				break;
			}
			//TTM_DEV 20180423 END EDIT
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("shohinCd", hcJuchMeisaiParrent.shohinCd);

		// 商品データ取得
		BasicWebApiResDto resDto = this.shohinMstLogic.getShohinDate(map);

		// 戻り値用オブジェクトに結果を複写
		if (resDto.isResult()) {

			// JSONを変換
			HcShohinMst hcShohinMst = JsonUtils.decode(resDto.getReturnValue(), HcShohinMst.class);
			//set hpDispFlg
			if (hcShohinMst.hpDispFlg != null) 
				juchKihonInfoDto.setHpDispFlag(hcShohinMst.hpDispFlg);
			else 
				juchKihonInfoDto.setHpDispFlag("1");
			//set emailDispFlg
			if (hcShohinMst.mailDispFlg != null)
				juchKihonInfoDto.setMailDispFlag(hcShohinMst.mailDispFlg);
			else 
				//TTM_DEV 20180423 BEGIN EDIT
				//juchKihonInfoDto.setHpDispFlag("1");
				juchKihonInfoDto.setMailDispFlag("1");
				//TTM_DEV 20180423 END EDIT
		}else{
			//
		}

	}
	
	/**
	 * 契約情報設定
	 * @param userDetailDto 受注詳細レスポンス情報
	 * @param coopDto 連携先情報 
	 */
	//TTM_DEV 20170307 DELETE BEGIN 
//	private void setContractInfo(UserDetailDto userDetailDto, CooperationDto coopDto) {
//
//		// 親受注明細番号リスト取得
//		this.contractInfoQueryForm.setPjuchNo(userDetailDto.getHcJuchKihon().juchNo);
//		this.contractInfoQueryForm.setpJuchMeisaiNoList(this.extractParentOrderNoList(userDetailDto));
//		this.contractInfoQueryForm.setCooperation(coopDto.getCooperation());
//		
//		// プロパティに設定された支払方法コードからコードマスタを取得し、支払方法、支払先変更、支払方法変更の初期表示とする。
//		HcSeiqMst hcSeiqMst = userDetailDto.getHcSeiqMst();
//		//TTM_DEV 20170630 ADD BEGIN
//		HcKyakuMst hcKyakuMst = userDetailDto.getHcKyakuMst();
//		List<HcCodeMst> hcCodeMstList = this.codeMstLogic.getList(CodeMstConst.ID_HARAI_KBN);
//		List<HcCodeMst> haraiKbnList = new LinkedList<HcCodeMst>();
//		for(HcDairitenShiharaiHimoMst hcDairitenShiharaiHimoMst : userDetailDto.getHcDairitenShiharaiHimoMst()){
//			for(HcCodeMst hcCodeMst : hcCodeMstList ){
//				if(hcDairitenShiharaiHimoMst.dairitenCd.equals(hcCodeMst.code) && 
//						!(hcCodeMst.code.equals(CodeMstConst.HARAI_KBN_STATUS_PENDING) && hcKyakuMst.kokyakuKbn.equals(CodeMstConst.KOKYAKU_KBN_CORPOTATION) )){
//					haraiKbnList.add(hcCodeMst);
//					break;
//				}
//			}
//		}
//		
//		List<String> haraiKbnName = new LinkedList<String>();
//		List<String> haraiKbnMethod = new LinkedList<String>();
//
//		// 電話料金合算請求手続き中の判定
//		// 請求先マスタ.支払区分=電話料金合算&請求先マスタ.NTTF結果ステータス=""
//		if (CodeMstConst.HARAI_KBN_NTTADDUP.equals(hcSeiqMst.kakuteiHaraiKbn)
//				&& org.apache.commons.lang.StringUtils.isBlank(hcSeiqMst.nttFinanceKekkaStatus) || CodeMstConst.HARAI_KBN_STATUS_PENDING.equals(hcSeiqMst.haraiKbnStatus)) {
//			// 手続き中
//			this.contractInfoQueryForm.setConsolidateBill(true);
//		} else {
//			this.contractInfoQueryForm.setConsolidateBill(false);
//		}
//
//		String kokyakuKbn = userDetailDto.getHcKyakuMst().kokyakuKbn;
//		
//		userSessionDto.setKokyakuKbn(kokyakuKbn);
//		// 支払方法及び支払方法変更の画面制御
//		for (HcCodeMst hcCodeMst : haraiKbnList) {
//			if (CodeMstConst.KOKYAKU_KBN_CORPOTATION.equals(kokyakuKbn) &&
//					CodeMstConst.HARAI_KBN_CREDIT.equals(hcCodeMst.code)) {
//				// 法人顧客の場合、クレジットへの変更ボタン非表示
//				continue;
//			}
			
			//TTM_DEV  20170529 BEGIN DELETE
			//if ("1".equals(hcSeiqMst.nttTourokuKbn)) {
			//				// NTT登録区分が1 (NTT東西)の場合
			//				if (hcCodeMst.code.equals(hcSeiqMst.haraiKbn)) { 
			//					// 支払方法設定時
			//					if (CodeMstConst.HARAI_KBN_NTTADDUP.equals(hcCodeMst.code)) { 
			//						// NTT合算の場合
			//						// NTT東西を設定
			//						this.contractInfoQueryForm.setHaraiKbnSetName(
			//								FtResourcesUtil.getItemValue(NTTEW));
			//					} else { // neu ma thanh toan # 4
			//						// NTT合算以外
			//						this.contractInfoQueryForm.setHaraiKbnSetName(hcCodeMst.codeMei);
			//						this.contractInfoQueryForm.setHaraiKbnSetMethod(this.getMethod(hcCodeMst.code));
			//					}
			//				} else {
			//					// 支払方法未設定時（支払方法変更）
			//					if (CodeMstConst.HARAI_KBN_NTTADDUP.equals(hcCodeMst.code)) {
			//						// NTT合算の場合
			//						// NTT東西を設定(ボタン非表示)
			//						haraiKbnName.add(FtResourcesUtil.getItemValue(NTTEW));
			//						haraiKbnMethod.add(CommonConst.EMPTY_STRING);
			//					} else {
			//						// NTT合算以外
			//						haraiKbnName.add(hcCodeMst.codeMei);
			//						haraiKbnMethod.add(this.getMethod(hcCodeMst.code));
			//					}
			//				}
			//			} else {
			//TTM_DEV  20170529 END DELETE			
			// NTT登録区分が0(NTT合算)の場合
//			if (hcCodeMst.code.equals(hcSeiqMst.kakuteiHaraiKbn)) { 
//				// 支払方法設定時
//				if (CodeMstConst.HARAI_KBN_NTTADDUP.equals(hcCodeMst.code)) {
//					// NTT合算の場合
//					if (CodeMstConst.HARAI_KBN_STATUS_NG.compareTo(hcSeiqMst.haraiKbnStatus) <= 0
//							&& !CodeMstConst.HARAI_KBN_STATUS_PENDING.equals(hcSeiqMst.haraiKbnStatus)) {
//						// 支払区分ステータスが[未確定/引落し可/申請中以外]の時
//						// NTTF申請結果ステータスの値でコードマスタを参照し、エラーメッセージを取得する。
//						if(CheckUtils.isEmpty(hcSeiqMst.nttFinanceKekkaStatus.trim())){
//							HcCodeMst nttFinanceKekkaStatus = this.codeMstLogic.get(
//									CodeMstConst.ID_NTT_FINANCE_KEKKA_STATUS, hcSeiqMst.nttFinanceKekkaStatus);
//
//							if (CheckUtils.isEmpty(nttFinanceKekkaStatus)) {
//								// 業務上通過しないはず。 支払区分ステータスがNGの場合かつNTTF結果ステータスが空の場合
//								this.contractInfoQueryForm.setHaraiKbnSetError(CommonConst.EMPTY_STRING);
//							} else {
//								this.contractInfoQueryForm.setHaraiKbnSetError(nttFinanceKekkaStatus.codeMei);
//							}
//						}else
//						{
//							this.contractInfoQueryForm.setHaraiKbnSetError(CommonConst.EMPTY_STRING);
//						}
//
//					}
//					// 支払区分ステータスがNGでない場合
//					this.contractInfoQueryForm.setHaraiKbnSetName(hcCodeMst.codeMei);
//					this.contractInfoQueryForm.setHaraiKbnSetMethod(this.getMethod(hcCodeMst.code));
//				} else {
//					// NTT合算以外
//					this.contractInfoQueryForm.setHaraiKbnSetName(hcCodeMst.codeMei);
//					this.contractInfoQueryForm.setHaraiKbnSetMethod(this.getMethod(hcCodeMst.code));
//				}
//			} else {
//				// 支払方法未設定時（支払方法変更）
//				haraiKbnName.add(hcCodeMst.codeMei);
//				haraiKbnMethod.add(this.getMethod(hcCodeMst.code));
//			}
//		}
//		//		}
//
//		this.contractInfoQueryForm.setHaraiKbnChengeName(haraiKbnName);
//		this.contractInfoQueryForm.setHaraiKbnChengeMethod(haraiKbnMethod);
//
//		// 共通項目
//		this.contractInfoQueryForm.setSeiqCd(userDetailDto.getHcSeiqMst().seiqCd);
//		this.contractInfoQueryForm.setKkykMei(userDetailDto.getHcKyakuMst().kkykMei);
//		this.contractInfoQueryForm.setKkykBng(userDetailDto.getHcKyakuMst().kkykBng);
//		this.contractInfoQueryForm.setTermsOfServiceURL(
//				FtResourcesUtil.getItemValue("contract_info_query.button.171"));
//		this.contractInfoQueryForm.setAboutCancellationURL(
//				FtResourcesUtil.getItemValue("contract_info_query.button.181"));
//
//	}
	//TTM_DEV 20170307 DELETE END 
	/**
	 * 親受注明細番号リストを抽出します。
	 *
	 * @param userDetailDto 受注詳細レスポンス情報
	 * @return List<String> 親受注明細番号リスト
	 */
	List<String> extractParentOrderNoList(UserDetailDto userDetailDto) {
		List<String> resultList = new LinkedList<String>();
		for (HcJuchMeisai hcJuchMeisai : userDetailDto.getHcJuchMeisai()) {
			// 親受注明細番号がnullの場合、商品コードを親商品コードリストに追加する。
		//TTM_DEV 20170623 UPDATE BEGIN
		//hcJuchMeisai.oyaJuchMeisaiNo == null
			if (CheckUtils.isNullOrEmpty(hcJuchMeisai.oyaJuchMeisaiNo)) {
		//TTM_DEV 20170623 UPDATE END
				resultList.add(hcJuchMeisai.juchMeisaiNo);
			}
		}
		return resultList;
	}

	//TTM_DEV-593 20171213 BEGIN DELETE
//	/**
//	 * 支払区分コード値から、該当する実行メソッド名を返却する。
//	 *
//	 * @param code 支払区分コード値
//	 * @return String 実行メソッド名
//	 */
//	String getMethod(String code) {
//		String method;
//		if (CodeMstConst.HARAI_KBN_ACOUNT.equals(code)) {
//			// 口座振替
//			method = ACCOUNT;
//		} else if (CodeMstConst.HARAI_KBN_CREDIT.equals(code)) {
//			// クレジット
//			method = CREDIT;
//		// TTM_DEV-593 20171211 BEGIN UPDATE
////		} else {
//		} else if (CodeMstConst.HARAI_KBN_NTTADDUP.equals(code)) {
//			// NTT合算
//			method = NTT;
//		} else {
//			method = ACCOUNT;
//		}
//		// TTM_DEV-593 20171211 END UPDATE
//		return method;
//	}
	//TTM_DEV-593 20171213 END DELETE
	
	/**
	 * Ebill SSO パラメータDTO
	 *
	 * @param userDetailDto 受注詳細レスポンス情報
	 */
	void setEbillSsoParam(UserDetailDto userDetailDto) {
		EbillSsoParameterDto ebillSsoParameterDto = new EbillSsoParameterDto();
		ebillSsoParameterDto.setTenantCd(FtResourcesUtil.getItemValue("system.ebill.sso.tenantid"));
		ebillSsoParameterDto.setLoginId(userDetailDto.getHcKyakuMst().kkykBng);
		ebillSsoParameterDto.setChangeScreen(FtResourcesUtil.getItemValue("system.ebill.sso.change.screen"));
		this.contractInfoQueryForm.setEbillSsoParameterDto(ebillSsoParameterDto);
	}

	/**
	 * 拡張項目設定
	 *
	 * @param userDetailDto 契約者詳細情報
	 * @return List<ItemInfoDto> 商品情報リスト
	 */
	List<ItemInfoDto> setExtension(UserDetailDto userDetailDto) {
		Date sysDate = DateUtils.convUtilDate2SqlDate(DateUtils.getSysdate());
		List<ItemInfoDto> itemInfoDtoList = new LinkedList<ItemInfoDto>();
		for (HcJuchMeisai hcJuchMeisai : userDetailDto.getHcJuchMeisai()) {
			ItemInfoDto itemInfoDto = new ItemInfoDto();

			itemInfoDto.setOptionFlag(CheckUtils.isNotEmpty(hcJuchMeisai.oyaJuchMeisaiNo));
			itemInfoDto.setJuchMeisaiNo(hcJuchMeisai.juchMeisaiNo);
			itemInfoDto.setSrvCd(hcJuchMeisai.srvCd);
			itemInfoDto.setSrvName(hcJuchMeisai.srvName);
			itemInfoDto.setShohinCd(hcJuchMeisai.shohinCd);
			itemInfoDto.setShohinName(hcJuchMeisai.shohinName);
			itemInfoDto.setKaiyakuKubun(hcJuchMeisai.kaiyakuKubun);

			// 基幹連携受信日
			if (CheckUtils.isEmpty(hcJuchMeisai.kikanJusinDate)) {
				itemInfoDto.setKikanJusinDate("");
			} else {
				String str = new SimpleDateFormat(DateUtils.FORMAT_S_YYYYMMDD).format(hcJuchMeisai.kikanJusinDate);
				itemInfoDto.setKikanJusinDate(str);
			}

			// 基幹連携送信日
			if (CheckUtils.isEmpty(hcJuchMeisai.kikanSousinDate)) {
				itemInfoDto.setKikanSousinDate("");
			} else {
				String str = new SimpleDateFormat(DateUtils.FORMAT_S_YYYYMMDD).format(hcJuchMeisai.kikanSousinDate);
				itemInfoDto.setKikanSousinDate(str);
			}

			// 解約日
			if (CheckUtils.isNotEmpty(hcJuchMeisai.kaiyakuDate)) {
				itemInfoDto.setKaiyakuDate(
						DateUtils.convertDateToStr(hcJuchMeisai.kaiyakuDate, DateUtils.FORMAT_YYYYMMDD_KANJI_FULL));
			}

			// 画面制御ロジック
			this.screenControl(userDetailDto, itemInfoDto, hcJuchMeisai, sysDate);

			itemInfoDtoList.add(itemInfoDto);
		}
		this.contractInfoQueryForm.setJuchNo(userDetailDto.getHcJuchKihon().juchNo);
		this.contractInfoQueryForm.setHyoujiKbn(HYOUJI_KBN);
		return itemInfoDtoList;
	}

	// TTM_DEV USER_SITE_CONTRACT_INFO_MODULE BEGIN ADD
	/**
	 * 拡張項目設定
	 *
	 * @param userDetailDto 契約者詳細情報
	 * @return List<ItemInfoDto> 商品情報リスト
	 */
	List<ItemInfoDto> setExtension(UserDetailDto userDetailDto, JuchKihonInfoDto juchKihonInfoDto) {
		Date sysDate = DateUtils.convUtilDate2SqlDate(DateUtils.getSysdate());
		List<ItemInfoDto> itemInfoDtoList = new LinkedList<ItemInfoDto>();
		for (HcJuchMeisai hcJuchMeisai : userDetailDto.getHcJuchMeisai()) {
			ItemInfoDto itemInfoDto = new ItemInfoDto();

			itemInfoDto.setOptionFlag(CheckUtils.isNotEmpty(hcJuchMeisai.oyaJuchMeisaiNo));
			itemInfoDto.setJuchMeisaiNo(hcJuchMeisai.juchMeisaiNo);
			itemInfoDto.setSrvCd(hcJuchMeisai.srvCd);
			itemInfoDto.setSrvName(hcJuchMeisai.srvName);
			itemInfoDto.setShohinCd(hcJuchMeisai.shohinCd);
			itemInfoDto.setShohinName(hcJuchMeisai.shohinName);
			itemInfoDto.setKaiyakuKubun(hcJuchMeisai.kaiyakuKubun);

			// 基幹連携受信日
			if (CheckUtils.isEmpty(hcJuchMeisai.kikanJusinDate)) {
				itemInfoDto.setKikanJusinDate("");
			} else {
				String str = new SimpleDateFormat(DateUtils.FORMAT_S_YYYYMMDD).format(hcJuchMeisai.kikanJusinDate);
				itemInfoDto.setKikanJusinDate(str);
			}

			// 基幹連携送信日
			if (CheckUtils.isEmpty(hcJuchMeisai.kikanSousinDate)) {
				itemInfoDto.setKikanSousinDate("");
			} else {
				String str = new SimpleDateFormat(DateUtils.FORMAT_S_YYYYMMDD).format(hcJuchMeisai.kikanSousinDate);
				itemInfoDto.setKikanSousinDate(str);
			}

			// 解約日
			if (CheckUtils.isNotEmpty(hcJuchMeisai.kaiyakuDate)) {
				itemInfoDto.setKaiyakuDate(
						DateUtils.convertDateToStr(hcJuchMeisai.kaiyakuDate, DateUtils.FORMAT_YYYYMMDD_KANJI_FULL));
			}
			// 画面制御ロジック
			this.screenControl(userDetailDto, itemInfoDto, hcJuchMeisai, sysDate, juchKihonInfoDto);
			itemInfoDtoList.add(itemInfoDto);
		}
		juchKihonInfoDto.setJuchNo(userDetailDto.getHcJuchKihon().juchNo);
		juchKihonInfoDto.setHyoujiKbn(HYOUJI_KBN);
		return itemInfoDtoList;
	}

	/**
	 * 画面制御ロジック
	 * @param userDetailDto
	 * @param itemInfoDto
	 * @param hcJuchMeisai
	 * @param sysDate
	 * @return itemInfoDto
	 */
	ItemInfoDto screenControl(UserDetailDto userDetailDto, ItemInfoDto itemInfoDto,
			HcJuchMeisai hcJuchMeisai, Date sysDate, JuchKihonInfoDto juchKihonInfoDto) {


		if (CheckUtils.isNotEmpty(hcJuchMeisai.oyaJuchMeisaiNo)) {
			// 子明細（オプション）
			if (CheckUtils.isNotEmpty(hcJuchMeisai.kaiyakuDate)) {
				// 解約日が設定されている場合
				if (this.dateDecision(hcJuchMeisai.kaiyakuDate, sysDate)) {
					// オプション解約申請中(受注明細．解約日>=システム日付)
					itemInfoDto.setOpTerminationApplied(true);
				} else {
					// オプション解約後(受注明細.解約日<システム日付)
					itemInfoDto.setOpTermination(true);
				}
			} else {
				// 解約日が設定されていない場合
				itemInfoDto.setOpTerminationApplied(false);
				itemInfoDto.setOpTermination(false);
			}
		} else {
			// 親明細（サービス）
			if (CheckUtils.isNotEmpty(hcJuchMeisai.kaiyakuDate)) {
				// 解約日が設定されている場合
				if (this.dateDecision(hcJuchMeisai.kaiyakuDate, sysDate)) {
					// サービス解約申請中(受注明細．解約日>=システム日付)
					juchKihonInfoDto.setServiceTerminationApplied(true);
					itemInfoDto.setServiceTerminationApplied(true);
				} else {
					// サービス解約申請中(受注明細.解約日<システム日付)
					juchKihonInfoDto.setServiceTermination(true);
				}
			} else {
				// 解約日が設定されていない場合
				juchKihonInfoDto.setServiceTerminationApplied(false);
				juchKihonInfoDto.setServiceTermination(false);
				itemInfoDto.setServiceTerminationApplied(false);
			}
		}
		//		juchKihonInfoDto.setServiceTermination(true);
		return itemInfoDto;
	}
	//TTM_DEV 20170307 ADD BEGIN
	//TODO: cheat code CodeMstConst.HARAI_KBN_TEST.equals(kakuteiHaraiKbn)){
	private boolean returnHaraiDisplay(String kakuteiHaraiKbn){
		if(CodeMstConst.HARAI_KBN_ACOUNT.equals(kakuteiHaraiKbn) || 
				CodeMstConst.HARAI_KBN_CREDIT.equals(kakuteiHaraiKbn) || 
				CodeMstConst.HARAI_KBN_NTTADDUP.equals(kakuteiHaraiKbn) ||
				// TTM_DEV-593 20171211 BEGIN ADD
				"3".equals(kakuteiHaraiKbn)){
				// TTM_DEV-593 20171211 END ADD
			return true;
		}else{
			return false;
		}
	}
	//TTM_DEV 20170307 ADD END
	/**
	 * 契約情報設定
	 *
	 * @param userDetailDto 受注詳細レスポンス情報
	 */
	 //TTM_DEV 20170307 UPDATE BEGIN
//	 boolean isSetContractInfo
	void setContractInfo(UserDetailDto userDetailDto, JuchKihonInfoDto juchKihonInfoDto, boolean isSetContractInfo) {
	//TTM_DEV 20170307 UPDATE END	 
		// 親受注明細番号リスト取得
		juchKihonInfoDto.setPjuchNo(userDetailDto.getHcJuchKihon().juchNo);
		juchKihonInfoDto.setpJuchMeisaiNoList(this.extractParentOrderNoList(userDetailDto));

		// プロパティに設定された支払方法コードからコードマスタを取得し、支払方法、支払先変更、支払方法変更の初期表示とする。
		HcSeiqMst hcSeiqMst = userDetailDto.getHcSeiqMst();
		//TTM_DEV 201706310	ADD BEGIN
		HcKyakuMst hcKyakuMst = userDetailDto.getHcKyakuMst();
		List<HcCodeMst> hcCodeMstList = this.codeMstLogic.getList(CodeMstConst.ID_HARAI_KBN);
		List<HcCodeMst> haraiKbnList = new LinkedList<HcCodeMst>();
		for(HcCodeMst hcCodeMst : hcCodeMstList ){
			if(hcSeiqMst.kakuteiHaraiKbn.equals(hcCodeMst.code)){
				this.contractInfoQueryForm.setHaraiKbnSetName(hcCodeMst.codeMei);
				break;
			}
		}
		for(HcDairitenShiharaiHimoMst hcDairitenShiharaiHimoMst : userDetailDto.getHcDairitenShiharaiHimoMst()){
			for(HcCodeMst hcCodeMst : hcCodeMstList ){
				if(hcDairitenShiharaiHimoMst.shiaraiKbn.equals(hcCodeMst.code) && 
						!(hcCodeMst.code.equals(CodeMstConst.HARAI_KBN_STATUS_PENDING) && hcKyakuMst.kokyakuKbn.equals(CodeMstConst.KOKYAKU_KBN_CORPOTATION) )){
					haraiKbnList.add(hcCodeMst);
					break;
				}
			}
		}
		
		// TTM_DEV-542 20170830 BEGIN ADD
		this.userSessionDto.setHcDairitenShiharaiHimoMstList(userDetailDto.getHcDairitenShiharaiHimoMst());
		// TTM_DEV-542 20170830 END ADD
		// TTM_DEV BEGIN ADD 20180327
		this.userSessionDto.setKaiyakuKubun(hcKyakuMst.kaiyakuKubun);
		// TTM_DEV END ADD 20180327
		//TTM_DEV 201706310	ADD BEGIN
		Date sysDate = DateUtils.convUtilDate2SqlDate(DateUtils.getSysdate());
		List<String> haraiKbnName = new LinkedList<String>();
		List<String> haraiKbnMethod = new LinkedList<String>(); 

		// 電話料金合算請求手続き中の判定
		// 請求先マスタ.支払区分=電話料金合算&請求先マスタ.NTTF結果ステータス=""
		
		//TTM_DEV 20170307 UPDATE BEGIN
//				CodeMstConst.HARAI_KBN_NTTADDUP.equals(hcSeiqMst.kakuteiHaraiKbn)
//				&&
//			org.apache.commons.lang.StringUtils.isBlank(hcSeiqMst.nttFinanceKekkaStatus) 
		if ( CodeMstConst.HARAI_KBN_STATUS_PENDING.equals(hcSeiqMst.haraiKbnStatus) 
				||  (CheckUtils.isNotEmpty(hcKyakuMst.kaiyakuDate)&& !this.dateDecision(hcKyakuMst.kaiyakuDate, sysDate) )){
			
				//TTM_DEV 20170307 UPDATE END
			// 手続き中
			this.contractInfoQueryForm.setConsolidateBill(true);
		} else {
			this.contractInfoQueryForm.setConsolidateBill(false);
		}

		String kokyakuKbn = userDetailDto.getHcKyakuMst().kokyakuKbn;
		userSessionDto.setKokyakuKbn(kokyakuKbn);
		// 支払方法及び支払方法変更の画面制御
		for (HcCodeMst hcCodeMst : haraiKbnList) { 
			if (CodeMstConst.KOKYAKU_KBN_CORPOTATION.equals(kokyakuKbn) &&
					CodeMstConst.HARAI_KBN_CREDIT.equals(hcCodeMst.code)) {
				// 法人顧客の場合、クレジットへの変更ボタン非表示
				continue;
			}
			//TTM_DEV  20170529 BEGIN DELETE
			//			if ("1".equals(hcSeiqMst.nttTourokuKbn)) {
			//				
			//				// NTT登録区分が1 (NTT東西)の場合
			//				if (hcCodeMst.code.equals(hcSeiqMst.haraiKbn)) { 
			//					// 支払方法設定時
			//					if (CodeMstConst.HARAI_KBN_NTTADDUP.equals(hcCodeMst.code)) { 
			//						// NTT合算の場合
			//						// NTT東西を設定
			//						this.contractInfoQueryForm.setHaraiKbnSetName(
			//								FtResourcesUtil.getItemValue(NTTEW)); 
			//					} else {
			//						// NTT合算以外
			//						this.contractInfoQueryForm.setHaraiKbnSetName(hcCodeMst.codeMei);
			//						this.contractInfoQueryForm.setHaraiKbnSetMethod(this.getMethod(hcCodeMst.code)); 
			//					}
			//				} else { // neu ma thanh toan cua user # ma thanh toan
			//					// 支払方法未設定時（支払方法変更）
			//					if (CodeMstConst.HARAI_KBN_NTTADDUP.equals(hcCodeMst.code)) { 
			//						// NTT合算の場合
			//						// NTT東西を設定(ボタン非表示)
			//						haraiKbnName.add(FtResourcesUtil.getItemValue(NTTEW));
			//						haraiKbnMethod.add(CommonConst.EMPTY_STRING);
			//					} else { // ma thanh toan = 1,2,3
			//						// NTT合算以外
			//						haraiKbnName.add(hcCodeMst.codeMei);
			//						haraiKbnMethod.add(this.getMethod(hcCodeMst.code));
			//					}
			//				}
			//			} else { // neu co quan dang ky thanh toan la ntt sum
			//TTM_DEV  20170529 END DELETE
			//TTM_DEV 20170629 UPDATE BEGIN
			//HaraiKbn
			// NTT登録区分が0(NTT合算)の場合
			if (hcCodeMst.code.equals(hcSeiqMst.kakuteiHaraiKbn)) { 
			//TTM_DEV 20170629 UPDATE END	
				// 支払方法設定時
				if (CodeMstConst.HARAI_KBN_NTTADDUP.equals(hcCodeMst.code)) { 
					// NTT合算の場合

					if (CodeMstConst.HARAI_KBN_STATUS_NG.compareTo(hcSeiqMst.haraiKbnStatus) <= 0 
							&& !CodeMstConst.HARAI_KBN_STATUS_PENDING.equals(hcSeiqMst.haraiKbnStatus)) { 
						// 支払区分ステータスが[未確定/引落し可/申請中以外]の時
						// NTTF申請結果ステータスの値でコードマスタを参照し、エラーメッセージを取得する。
						if(!CheckUtils.isEmpty(hcSeiqMst.nttFinanceKekkaStatus.trim())){
							HcCodeMst nttFinanceKekkaStatus = this.codeMstLogic.get(CodeMstConst.ID_NTT_FINANCE_KEKKA_STATUS, hcSeiqMst.nttFinanceKekkaStatus); 

							if (CheckUtils.isEmpty(nttFinanceKekkaStatus)) { //neu trang thai loi bang null
								// 業務上通過しないはず。 支払区分ステータスがNGの場合かつNTTF結果ステータスが空の場合
								this.contractInfoQueryForm.setHaraiKbnSetError(CommonConst.EMPTY_STRING); 
							} else {
								this.contractInfoQueryForm.setHaraiKbnSetError(nttFinanceKekkaStatus.codeMei); 
							}
						}else
						{
							this.contractInfoQueryForm.setHaraiKbnSetError(CommonConst.EMPTY_STRING); 
						}

					}
					//TTM_DEV 20170307 UPDATE BEGIN
					// 支払区分ステータスがNGでない場合
//					this.contractInfoQueryForm.setHaraiKbnSetName(hcCodeMst.codeMei);
					if(returnHaraiDisplay(hcCodeMst.code)){
					//TTM_DEV 20170307 UPDATE END	
						//TTM_DEV-593 20171213 BEGIN UPDATE
//						this.contractInfoQueryForm.setHaraiKbnSetMethod(this.getMethod(hcCodeMst.code));
						this.contractInfoQueryForm.setHaraiKbnSetMethod(hcCodeMst.code);
						//TTM_DEV-593 20171213 END UPDATE
					}
				} else { 
					//TTM_DEV 20170307 UPDATE BEGIN
					// NTT合算以外
//					this.contractInfoQueryForm.setHaraiKbnSetName(hcCodeMst.codeMei);
					if(returnHaraiDisplay(hcCodeMst.code)){
					//TTM_DEV 20170307 UPDATE END		
						//TTM_DEV-593 20171213 BEGIN UPDATE
//						this.contractInfoQueryForm.setHaraiKbnSetMethod(this.getMethod(hcCodeMst.code));
						this.contractInfoQueryForm.setHaraiKbnSetMethod(hcCodeMst.code);
						//TTM_DEV-593 20171213 END UPDATE
					}
				}
			} else { // neu khac ma khach thanh toan khach hang
				// 支払方法未設定時（支払方法変更）
				//TTM_DEV 20170307 ADD BEGIN
				if(returnHaraiDisplay(hcCodeMst.code)){
				//TTM_DEV 20170307 ADD END
					haraiKbnName.add(hcCodeMst.codeMei);
					//TTM_DEV-593 20171213 BEGIN UPDATE
//					haraiKbnMethod.add(this.getMethod(hcCodeMst.code)); //cac phuong thuc con lai (credit card , ntt Sum , acount
					haraiKbnMethod.add(hcCodeMst.code); 
					//TTM_DEV-593 20171213 END UPDATE
				}
			}
		}
		//		}
		
		this.contractInfoQueryForm.setHaraiKbnChengeName(haraiKbnName);
		this.contractInfoQueryForm.setHaraiKbnChengeMethod(haraiKbnMethod);

		// 共通項目
		this.contractInfoQueryForm.setSeiqCd(userDetailDto.getHcSeiqMst().seiqCd);
		this.contractInfoQueryForm.setKkykMei(userDetailDto.getHcKyakuMst().kkykMei);
		this.contractInfoQueryForm.setKkykBng(userDetailDto.getHcKyakuMst().kkykBng);
		this.contractInfoQueryForm.setTermsOfServiceURL(
					FtResourcesUtil.getItemValue("contract_info_query.button.171"));
		this.contractInfoQueryForm.setAboutCancellationURL(
				FtResourcesUtil.getItemValue("contract_info_query.button.181"));
		//TTM_DEV 20170307 ADD BEGIN
		isSetContractInfo = true;
		//TTM_DEV 20170307 ADD END
	}
	// TTM_DEV USER_SITE_CONTRACT_INFO_MODULE END ADD

	/**
	 * 画面制御ロジック
	 * @param userDetailDto
	 * @param itemInfoDto
	 * @param hcJuchMeisai
	 * @param sysDate
	 * @return itemInfoDto
	 */
	ItemInfoDto screenControl(UserDetailDto userDetailDto, ItemInfoDto itemInfoDto,
			HcJuchMeisai hcJuchMeisai, Date sysDate) {

		if (CheckUtils.isNotEmpty(hcJuchMeisai.oyaJuchMeisaiNo)) {
			// 子明細（オプション）
			if (CheckUtils.isNotEmpty(hcJuchMeisai.kaiyakuDate)) {
				// 解約日が設定されている場合
				if (this.dateDecision(hcJuchMeisai.kaiyakuDate, sysDate)) {
					// オプション解約申請中(受注明細．解約日>=システム日付)
					itemInfoDto.setOpTerminationApplied(true);
				} else {
					// オプション解約後(受注明細.解約日<システム日付)
					itemInfoDto.setOpTermination(true);
				}
			} else {
				// 解約日が設定されていない場合
				itemInfoDto.setOpTerminationApplied(false);
				itemInfoDto.setOpTermination(false);
			}
		} else {
			// 親明細（サービス）
			if (CheckUtils.isNotEmpty(hcJuchMeisai.kaiyakuDate)) {
				// 解約日が設定されている場合
				if (this.dateDecision(hcJuchMeisai.kaiyakuDate, sysDate)) {
					// サービス解約申請中(受注明細．解約日>=システム日付)
					this.contractInfoQueryForm.setServiceTerminationApplied(true);
					itemInfoDto.setServiceTerminationApplied(true);
				} else {
					// サービス解約申請中(受注明細.解約日<システム日付)
					this.contractInfoQueryForm.setServiceTermination(true);
				}
			} else {
				// 解約日が設定されていない場合
				this.contractInfoQueryForm.setServiceTerminationApplied(false);
				this.contractInfoQueryForm.setServiceTermination(false);
				itemInfoDto.setServiceTerminationApplied(false);
			}
		}
		return itemInfoDto;
	}

	/**
	 * 日付判定ロジック
	 * @param date1
	 * @param date2
	 * @return ret
	 */
	boolean dateDecision(Date date1, Date date2) {
		boolean ret = false;
		int diff = date1.compareTo(date2);
		if (diff == 0) {
			// 解約日とシステム日付は同じ
			// 解約申請中
			ret = true;
		} else if (diff > 0) {
			// 解約日はシステム日付より未来の日付
			// 解約申請中
			ret = true;
		} else {
			// 解約日はシステム日付より過去の日付
			// 解約後
			ret = false;
		}
		return ret;
	}

	/**
	 * ホームページ情報設定
	 *
	 * @param hcKakuchouDataList 拡張データリスト
	 * @param userDetailDto 契約者詳細情報
	 * @return HpInfoDto ホームページ情報
	 */
	HpInfoDto setHpInfo(List<HcKakuchouData> hcKakuchouDataList, UserDetailDto userDetailDto) {
		BasicWebApiResDto resDtoHp;
		String connectAccount =	KakuchouUtils.getKoumokuAtai(hcKakuchouDataList, KakuchouUtils.CodeType.AuthAccount);

		try {
			//			resDtoHp = this.hpInfoReferenceLogic.getHpInfo(userDetailDto.getHcKyakuMst().kkykBng);
			//TTM_DEV  20170530 BEGIN UPDATE
			String internetConnectionId = connectAccount;
			String accountClassification = UserConst.ACCOUNT_CLASS_RAD_ID;
			if (CheckUtils.isEmpty(internetConnectionId)){
				return null;
			}
			resDtoHp = this.hpInfoReferenceLogic.getHpInfo(userDetailDto.getHcKyakuMst().kkykBng,
					internetConnectionId,accountClassification);
			//TTM_DEV  20170530 END UPDATE

		} catch (FtSystemException e) {
			throw new FtApplicationException(SP_API_ERROR, "");
		}
		if (!resDtoHp.isResult()) {
			throw new FtApplicationException(SP_API_ERROR, "");
		}

		// インターネット接続ID

		List<HpInfoDto> hpInfoDtoList = new LinkedList<HpInfoDto>();
		HpInfoDto hpInfoDto = new HpInfoDto();
		WebAccountDto webAccountDto = JsonUtils.decode(resDtoHp.getReturnValue(), WebAccountDto.class);
		if (CheckUtils.isNotEmpty(webAccountDto.getFtpServerName()) && !webAccountDto.getFtpUserDtoList().isEmpty()) {
			hpInfoDto.setFtpServerName(webAccountDto.getFtpServerName());
			hpInfoDto.setUrl(webAccountDto.getFtpUserDtoList().get(0).getUrl());
			hpInfoDto.setWebAccount(webAccountDto.getFtpUserDtoList().get(0).getWebAccount());
			hpInfoDto.setFtpConnectId(webAccountDto.getFtpUserDtoList().get(0).getFtpUserName());
			hpInfoDto.setWebPasswd(webAccountDto.getFtpUserDtoList().get(0).getWebPass());
			hpInfoDto.setConnectAccount(connectAccount);

			hpInfoDtoList.add(hpInfoDto);
			return hpInfoDtoList.get(0);
		} else {
			return null;
		}
	}
	
	/**
	 * メール情報設定
	 *
	 * @param userDetailDto 契約者詳細情報
	 * @return List<MailInfoDto> メール情報リスト
	 */
	List<MailInfoDto> setMailInfo(List<HcKakuchouData> hcKakuchouDataList,UserDetailDto userDetailDto) {

		// メール情報最大登録件数を設定
		this.contractInfoQueryForm.setMailCountNum(FtResourcesUtil.getItemValue("contract_info_query.label.591"));

		BasicWebApiResDto resDtoMail;
		try {
			//TTM_DEV HOMEPAGE_MODULE 20170602 BEGIN ADD
			String connectAccount =	KakuchouUtils.getKoumokuAtai(hcKakuchouDataList, KakuchouUtils.CodeType.AuthAccount);
			String internetConnectionId = connectAccount;
			String accountClassification = UserConst.ACCOUNT_CLASS_RAD_ID;
			if (CheckUtils.isEmpty(internetConnectionId)){
				return null;
			}
			//TTM_DEV HOMEPAGE_MODULE 20170602 END ADD
			//TTM_DEV HOMEPAGE_MODULE 20170602 BEGIN UPDATE	
			//			resDtoMail = this.mailAccountReferenceLogic.getMailAccountInfo(userDetailDto.getHcKyakuMst().kkykBng);
			resDtoMail = this.mailAccountReferenceLogic.getMailAccountInfo(userDetailDto.getHcKyakuMst().kkykBng,
					internetConnectionId,accountClassification);
			//TTM_DEV HOMEPAGE_MODULE 20170602 END UPDATE	
		} catch (FtSystemException e) {
			throw new FtApplicationException(SP_API_ERROR, "");
		}
		if (!resDtoMail.isResult()) {
			throw new FtApplicationException(SP_API_ERROR, "");
		}

		List<MailInfoDto> mailInfoDtoList = new LinkedList<MailInfoDto>();
		MailAccountDto mailAccountDto = JsonUtils.decode(resDtoMail.getReturnValue(), MailAccountDto.class);

		if (!(CheckUtils.isEmpty(mailAccountDto.getPopServerName()))
				&& (mailAccountDto.getMailUserList() != null && !mailAccountDto.getMailUserList().isEmpty())) {
			String popServerName = mailAccountDto.getPopServerName();
			String smtpServerName = mailAccountDto.getSmtpServerName();

			for (MailUserDto mailUserDto : mailAccountDto.getMailUserList()) {
				MailInfoDto mailInfoDto = new MailInfoDto();
				mailInfoDto.setMailAddr(mailUserDto.getMailAddr());
				mailInfoDto.setMailPass(mailUserDto.getMailPass());
				mailInfoDto.setPopServerName(popServerName);
				mailInfoDto.setSmtpServerName(smtpServerName);
				mailInfoDtoList.add(mailInfoDto);

			}
			return mailInfoDtoList;
		} else {
			return null;
		}
	}

	/**
	 * オプションキャンセル処理
	 * @return String JSPファイル名
	 */
	@Execute(validator = false, input = JSP_FILE)
	public String deleteOption() {

		try {
			String juchNo = this.contractInfoQueryForm.getKjuchNo();
			String juchMeisaiNo = this.contractInfoQueryForm.getkJuchMeisaiNo();

			BasicWebApiResDto resDto = this.optionDeleteLogic.deleteOption(juchNo, juchMeisaiNo);

			// 戻り値用オブジェクトに結果を複写
			if (resDto.isResult()) {
				ActionMessage infos = new ActionMessage("infos.delete.option");
				super.addInfos(infos);
				// 削除処理成功時、キャンセル対象のオブジェクトを削除
				this.contractInfoQueryForm = (ContractInfoQueryForm) session.getAttribute("contractInfoQueryForm");
				//TTM_DEV 20170620 UPDATE BEGIN
//					for (ItemInfoDto dto : this.contractInfoQueryForm.getItemInfoDtoList()) {
//					if (dto.getJuchMeisaiNo().equals(this.contractInfoQueryForm.getkJuchMeisaiNo())) {
//						this.contractInfoQueryForm.getItemInfoDtoList().remove(dto);
//					}
					for (JuchKihonInfoDto dtoJuchKihon : this.contractInfoQueryForm.getJuchKihonInfoDtoList()) {
					if(this.contractInfoQueryForm.getKjuchNo().equals(dtoJuchKihon.getJuchNo())){
						
						for (ItemInfoDto dtoItemInfo : dtoJuchKihon.getItemInfoDtoList()){
							if(dtoItemInfo.getJuchMeisaiNo().equals(this.contractInfoQueryForm.getkJuchMeisaiNo())) {
								
								dtoJuchKihon.getItemInfoDtoList().remove(dtoItemInfo);
							}
						}
					}
				}
					//TTM_DEV 20170620 UPDATE END
				// TTM_DEV_11 APPLICATION_LOG BEGIN ADD
				BasicWebApiResDto logResDto = this.logRegisterLogic.registerLog(UserConst.LOG_SCREEN_CONTRACT_INFO, UserConst.LOG_BUTTON_OPTION_CANCEL, CommonConst.EMPTY_STRING, ipAddress);
				if(!logResDto.isResult()){
					LogUtils log = new LogUtils(this.getClass());
					log.error(UserConst.ERROR_MESSAGE_WRITE_LOG);
				}
				// TTM_DEV_11 APPLICATION_LOG END ADD

			} else {
				throw new FtSystemException("");
			}

		} catch (FtSystemException e) {
			throw new FtSystemException(e);
		}

		// 初期表示処理
		return JSP_FILE;
	}

	/**
	 * オプション解約処理
	 * @return String JSPファイル名
	 */
	@Execute(validator = false, input = JSP_FILE)
	public String cancalOption() {

		try {
			String juchNo = this.contractInfoQueryForm.getKjuchNo();
			String juchMeisaiNo = this.contractInfoQueryForm.getkJuchMeisaiNo();

			BasicWebApiResDto resDto = this.optionCancelLogic.cancelOption(juchNo, juchMeisaiNo);

			// 戻り値用オブジェクトに結果を複写
			if (resDto.isResult()) {
				ActionMessage infos = new ActionMessage("infos.cancel.option");
				super.addInfos(infos);

				// TTM_DEV_11 APPLICATION_LOG BEGIN ADD
				BasicWebApiResDto logResDto = this.logRegisterLogic.registerLog(UserConst.LOG_SCREEN_CONTRACT_INFO, UserConst.LOG_BUTTON_REQUEST_CANCEL, CommonConst.EMPTY_STRING, ipAddress);
				if(!logResDto.isResult()){
					LogUtils log = new LogUtils(this.getClass());
					log.error(UserConst.ERROR_MESSAGE_WRITE_LOG);
				}
				// TTM_DEV_11 APPLICATION_LOG END ADD
			} else {
				throw new FtSystemException("");
			}
		} catch (FtSystemException e) {
			throw new FtSystemException(e);
		}

		// 初期表示処理
		return JSP_FILE;
	}

	/**
	 * オプション解約取消処理
	 * @return String JSPファイル名
	 */
	@Execute(validator = false, input = JSP_FILE)
	public String cancelOptionCancel() {

		try {
			String juchNo = this.contractInfoQueryForm.getKjuchNo();
			String juchMeisaiNo = this.contractInfoQueryForm.getkJuchMeisaiNo();

			BasicWebApiResDto resDto = this.OptionCancelCancelLogic.cancelOptionCancel(juchNo, juchMeisaiNo);

			// 戻り値用オブジェクトに結果を複写
			if (resDto.isResult()) {
				ActionMessage infos = new ActionMessage("infos.cancel.option.cancel");
				super.addInfos(infos);
			} else {
				throw new FtSystemException("");
			}

		} catch (FtSystemException e) {
			throw new FtSystemException(e);
		}

		// 初期表示処理
		return JSP_FILE;
	}



	/**
	 * ホームページ削除
	 * @return String JSPファイル名
	 */
	@Execute(validator = false, input = JSP_FILE)
	public String deleteHp() {

		try {
			String kkykBng = this.contractInfoQueryForm.getKkykBng();
			String webAccount = this.contractInfoQueryForm.getWebAccount();
			String delDate = DateUtils.getSysdayValiable(DateUtils.FORMAT_YYYYMMDD, -1);

			//BasicWebApiResDto resDto = this.hpInfoDeleteLogic.delete(kkykBng, webAccount, delDate);
			//TTM_DEV  20170529 BEGIN UPDATE
			String internetConnectionId = this.contractInfoQueryForm.getInternetConnectionId();
			String accountClassification = this.contractInfoQueryForm.getAccountClassification();
			BasicWebApiResDto resDto = this.hpInfoDeleteLogic.delete(kkykBng, webAccount, delDate,internetConnectionId,accountClassification);
			//TTM_DEV  20170529 END UPDATE
			// 戻り値用オブジェクトに結果を複写
			if (resDto.isResult()) {
				this.contractInfoQueryForm.setCompleteOfHPFlag(true);

				// TTM_DEV_11 APPLICATION_LOG BEGIN ADD
				BasicWebApiResDto logResDto = this.logRegisterLogic.registerLog(UserConst.LOG_SCREEN_CONTRACT_INFO, UserConst.LOG_BUTTON_HOMEPAGE_DELETE, CommonConst.EMPTY_STRING, ipAddress);
				if(!logResDto.isResult()){
					LogUtils log = new LogUtils(this.getClass());
					log.error(UserConst.ERROR_MESSAGE_WRITE_LOG);
				}
				// TTM_DEV_11 APPLICATION_LOG END ADD
			} else {
				throw new FtSystemException("");
			}

		} catch (FtSystemException e) {
			throw new FtSystemException(e);
		}

		return JSP_FILE_FINISH;
	}

	/**
	 * メール解除
	 * @return String JSPファイル名
	 */
	@Execute(validator = false, input = JSP_FILE)
	public String removeMail() {
		try {
			String kkykBng = this.contractInfoQueryForm.getKkykBng();
			String mailAccount = this.contractInfoQueryForm.getMailAddress();
			mailAccount = mailAccount.substring(0, mailAccount.indexOf("@"));
			String delDate = DateUtils.getSysdayValiable(DateUtils.FORMAT_YYYYMMDD, -1);

//			BasicWebApiResDto resDto = this.mailAccountDeleteLogic.delete(kkykBng, mailAccount, delDate);
//			TTM_DEV ORDER_MODULE 20170529 BEGIN UPDATE
			String internetConnectionId = contractInfoQueryForm.getInternetConnectionId();
			String accountClassification = contractInfoQueryForm.getAccountClassification();
			BasicWebApiResDto resDto = this.mailAccountDeleteLogic.delete(kkykBng, mailAccount, delDate,internetConnectionId,accountClassification);
//			TTM_DEV ORDER_MODULE 20170529 END UPDATE

			// 戻り値用オブジェクトに結果を複写
			if (resDto.isResult()) {
				this.contractInfoQueryForm.setCompleteOfHPFlag(false);

				// TTM_DEV_11 APPLICATION_LOG BEGIN ADD
				BasicWebApiResDto logResDto = this.logRegisterLogic.registerLog(UserConst.LOG_SCREEN_CONTRACT_INFO, UserConst.LOG_BUTTON_MAIL_DELETE, CommonConst.EMPTY_STRING, ipAddress);
				if(!logResDto.isResult()){
					LogUtils log = new LogUtils(this.getClass());
					log.error(UserConst.ERROR_MESSAGE_WRITE_LOG);
				}
				// TTM_DEV_11 APPLICATION_LOG END ADD

			} else {
				throw new FtSystemException("");
			}

		} catch (FtSystemException e) {
			throw new FtSystemException(e);
		}

		return JSP_FILE_FINISH;
	}
}
