/**
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.form;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

import jp.co.forvaltel.common.constant.CommonConst;
import jp.co.forvaltel.common.dto.EbillSsoParameterDto;
import jp.co.forvaltel.common.form.AbstractForm;
import jp.co.forvaltel.user.dto.HpInfoDto;
import jp.co.forvaltel.user.dto.ItemInfoDto;
import jp.co.forvaltel.user.dto.JuchKihonInfoDto;
import jp.co.forvaltel.user.dto.MailInfoDto;

/**
 * 契約情報照会アクションフォーム
 * 
 * @author smis
 */
@Component(instance = InstanceType.SESSION)
public class ContractInfoQueryForm extends AbstractForm {

	/**
	 * シリアルバージョンID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * バナー情報：画像リンク
	 */
	private String bannerImgLink;
	/**
	 * バナー情報：URLリンク
	 */
	private String bannerUrlLink;

	/**
	 * お客様名：契約者名
	 */
	private String kkykMei;
	/**
	 * お客様番号：顧客番号
	 */
	private String kkykBng;
	/**
	 * サービス利用規約URL
	 */
	private String termsOfServiceURL;
	/**
	 * 解約についてURL
	 */
	private String aboutCancellationURL;

	/**
	 * 請求先コード：支払先情報画面パラメータ
	 */
	private String seiqCd;
	/**
	 * 支払方法：ラベル名
	 */
	private String haraiKbnSetName;
	/**
	 * 支払方法：実行メソッド
	 */
	private String haraiKbnSetMethod;
	/**
	 * 支払方法：エラー（NTT合算時のみ）
	 */
	private String haraiKbnSetError;
	/**
	 * 支払方法変更：ラベル名
	 */
	private List<String> haraiKbnChengeName;
	/**
	 * 支払方法変更：実行メソッド
	 */
	private List<String> haraiKbnChengeMethod;
	/**
	 * 解約希望日
	 */
	private String kaiyakuDate;

	/**
	 * 請求情報リンク(eBill)
	 */
	private EbillSsoParameterDto ebillSsoParameterDto = new EbillSsoParameterDto();
	
	// TTM_DEV USER_SITE_CONTRACT_INFO_MODULE BEGIN ADD
	/**
	 * juchKihonInfoDto
	 */
	private List<JuchKihonInfoDto> juchKihonInfoDtoList;
	//TTM_DEV 20170606 BEGIN ADD
	/**
	 * RadID
	 */
	private String internetConnectionId;
	/**
	 * accountClassification
	 */
	private String accountClassification;
	/**
	 * screen
	 */
	private String screen;
	//TTM_DEV 20170606 END ADD
	/**
	 * @return the juchKihonInfoDtoList
	 */
	public List<JuchKihonInfoDto> getJuchKihonInfoDtoList() {
		return juchKihonInfoDtoList;
	}
	/**
	 * @param juchKihonInfoDtoList the juchKihonInfoDtoList to set
	 */
	public void setJuchKihonInfoDtoList(List<JuchKihonInfoDto> juchKihonInfoDtoList) {
		this.juchKihonInfoDtoList = juchKihonInfoDtoList;
	}
	
	// TTM_DEV USER_SITE_CONTRACT_INFO_MODULE END ADD

	/**
	 * 拡張項目
	 * 受注番号：juchNo
	 */
	private String juchNo;
	/**
	 * 拡張項目
	 * 表示区分：hyoujiKbn
	 */
	private String hyoujiKbn;

	/**
	 * 商品情報リスト
	 */
	private List<ItemInfoDto> itemInfoDtoList = new LinkedList<ItemInfoDto>();

	/**
	 * ホームページ情報
	 */
	private HpInfoDto hpInfoDto;

	/**
	 * メール情報リスト
	 */
	private List<MailInfoDto> mailInfoDtoList = new LinkedList<MailInfoDto>();
	/**
	 * メール情報選択行番号
	 */
	private String selectMailAddr;
	/**
	 * メール登録最大件数
	 */
	private String mailCountNum;

	// パラメータ
	/**
	 * オプション追加用パラメータ（オプション追加用）
	 * 受注番号：pjuchNo
	 */
	private String pjuchNo;
	/**
	 * オプション追加用パラメータ（オプション追加用）
	 * 受注明細番号：pJuchMeisaiNoList
	 */
	private List<String> pJuchMeisaiNoList;
	/**
	 * オプション追加用パラメータ（オプション追加用）
	 * 受注明細番号：pJuchMeisaiNo
	 */
	private String pJuchMeisaiNo;

	/**
	 * オプション申請キャンセル用パラメータ（拡張項目）
	 * 受注番号：kjuchNo
	 */
	private String kjuchNo;
	/**
	 * オプション申請キャンセル用パラメータ（拡張項目）
	 * 受注明細番号：kJuchMeisaiNo
	 */
	private String kJuchMeisaiNo;
	/**
	 * WEBアカウント
	 */
	private String webAccount;
	/**
	 * メールアドレス
	 */
	private String mailAddress;
	/**
	 * メールアカウント
	 */
	private String mailAccount;

	// 画面項目表示非表示
	/**
	 * 電話料金合算請求手続き中
	 * 請求先マスタ.支払区分=電話料金合算&請求先マスタ.NTTF結果ステータス=""
	 */
	private boolean consolidateBill;
	/**
	 * サービス解約中
	 * 親明細.解約日isNotNull & 親明細.解約日>=システム日付
	 */
	private boolean serviceTerminationApplied;
	/**
	 * サービス解約後
	 * 親明細.解約日isNotNull & 親明細.解約日< システム日付
	 */
	private boolean serviceTermination;

// 完了画面
	/**
	 * ホームページ削除完了フラグ
	 */
	private boolean completeOfHPFlag;

	/**
	 * @return bannerImgLink
	 */
	public String getBannerImgLink() {
		return this.bannerImgLink;
	}

	/**
	 * @param bannerImgLink 設定する bannerImgLink
	 */
	public void setBannerImgLink(String bannerImgLink) {
		this.bannerImgLink = bannerImgLink;
	}

	/**
	 * @return bannerUrlLink
	 */
	public String getBannerUrlLink() {
		return this.bannerUrlLink;
	}

	/**
	 * @param bannerUrlLink 設定する bannerUrlLink
	 */
	public void setBannerUrlLink(String bannerUrlLink) {
		this.bannerUrlLink = bannerUrlLink;
	}

	/**
	 * @return kkykMei
	 */
	public String getKkykMei() {
		return this.kkykMei;
	}

	/**
	 * @param kkykMei 設定する kkykMei
	 */
	public void setKkykMei(String kkykMei) {
		this.kkykMei = kkykMei;
	}

	/**
	 * @return kkykBng
	 */
	public String getKkykBng() {
		return this.kkykBng;
	}

	/**
	 * @param kkykBng 設定する kkykBng
	 */
	public void setKkykBng(String kkykBng) {
		this.kkykBng = kkykBng;
	}

	/**
	 * @return termsOfServiceURL
	 */
	public String getTermsOfServiceURL() {
		return this.termsOfServiceURL;
	}

	/**
	 * @param termsOfServiceURL 設定する termsOfServiceURL
	 */
	public void setTermsOfServiceURL(String termsOfServiceURL) {
		this.termsOfServiceURL = termsOfServiceURL;
	}

	/**
	 * @return aboutCancellationURL
	 */
	public String getAboutCancellationURL() {
		return this.aboutCancellationURL;
	}

	/**
	 * @param aboutCancellationURL 設定する aboutCancellationURL
	 */
	public void setAboutCancellationURL(String aboutCancellationURL) {
		this.aboutCancellationURL = aboutCancellationURL;
	}

	/**
	 * @return seiqCd
	 */
	public String getSeiqCd() {
		return this.seiqCd;
	}

	/**
	 * @param seiqCd 設定する seiqCd
	 */
	public void setSeiqCd(String seiqCd) {
		this.seiqCd = seiqCd;
	}

	/**
	 * @return haraiKbnSetName
	 */
	public String getHaraiKbnSetName() {
		return this.haraiKbnSetName;
	}

	/**
	 * @param haraiKbnSetName 設定する haraiKbnSetName
	 */
	public void setHaraiKbnSetName(String haraiKbnSetName) {
		this.haraiKbnSetName = haraiKbnSetName;
	}

	/**
	 * @return haraiKbnSetMethod
	 */
	public String getHaraiKbnSetMethod() {
		return this.haraiKbnSetMethod;
	}

	/**
	 * @param haraiKbnSetMethod 設定する haraiKbnSetMethod
	 */
	public void setHaraiKbnSetMethod(String haraiKbnSetMethod) {
		this.haraiKbnSetMethod = haraiKbnSetMethod;
	}

	/**
	 * @return haraiKbnSetError
	 */
	public String getHaraiKbnSetError() {
		return this.haraiKbnSetError;
	}

	/**
	 * @param haraiKbnSetError 設定する haraiKbnSetError
	 */
	public void setHaraiKbnSetError(String haraiKbnSetError) {
		this.haraiKbnSetError = haraiKbnSetError;
	}

	/**
	 * @return haraiKbnChengeName
	 */
	public List<String> getHaraiKbnChengeName() {
		return this.haraiKbnChengeName;
	}

	/**
	 * @param haraiKbnChengeName 設定する haraiKbnChengeName
	 */
	public void setHaraiKbnChengeName(List<String> haraiKbnChengeName) {
		this.haraiKbnChengeName = haraiKbnChengeName;
	}

	/**
	 * @return haraiKbnChengeMethod
	 */
	public List<String> getHaraiKbnChengeMethod() {
		return this.haraiKbnChengeMethod;
	}

	/**
	 * @param haraiKbnChengeMethod 設定する haraiKbnChengeMethod
	 */
	public void setHaraiKbnChengeMethod(List<String> haraiKbnChengeMethod) {
		this.haraiKbnChengeMethod = haraiKbnChengeMethod;
	}

	/**
	 * @return kaiyakuDate
	 */
	public String getKaiyakuDate() {
		return this.kaiyakuDate;
	}

	/**
	 * @param kaiyakuDate 設定する kaiyakuDate
	 */
	public void setKaiyakuDate(String kaiyakuDate) {
		this.kaiyakuDate = kaiyakuDate;
	}

	/**
	 * @return ebillSsoParameterDto
	 */
	public EbillSsoParameterDto getEbillSsoParameterDto() {
		return this.ebillSsoParameterDto;
	}

	/**
	 * @param ebillSsoParameterDto 設定する ebillSsoParameterDto
	 */
	public void setEbillSsoParameterDto(EbillSsoParameterDto ebillSsoParameterDto) {
		this.ebillSsoParameterDto = ebillSsoParameterDto;
	}

	/**
	 * @return pjuchNo
	 */
	public String getPjuchNo() {
		return this.pjuchNo;
	}

	/**
	 * @param pjuchNo 設定する pjuchNo
	 */
	public void setPjuchNo(String pjuchNo) {
		this.pjuchNo = pjuchNo;
	}

	/**
	 * @return pJuchMeisaiNoList
	 */
	public List<String> getpJuchMeisaiNoList() {
		return this.pJuchMeisaiNoList;
	}

	/**
	 * @param pJuchMeisaiNoList 設定する pJuchMeisaiNoList
	 */
	public void setpJuchMeisaiNoList(List<String> pJuchMeisaiNoList) {
		this.pJuchMeisaiNoList = pJuchMeisaiNoList;
	}

	/**
	 * @return pJuchMeisaiNo
	 */
	public String getpJuchMeisaiNo() {
		return this.pJuchMeisaiNo;
	}

	/**
	 * @param pJuchMeisaiNo 設定する pJuchMeisaiNo
	 */
	public void setpJuchMeisaiNo(String pJuchMeisaiNo) {
		this.pJuchMeisaiNo = pJuchMeisaiNo;
	}

	/**
	 * @return kjuchNo
	 */
	public String getKjuchNo() {
		return this.kjuchNo;
	}

	/**
	 * @param kjuchNo 設定する kjuchNo
	 */
	public void setKjuchNo(String kjuchNo) {
		this.kjuchNo = kjuchNo;
	}

	/**
	 * @return kJuchMeisaiNo
	 */
	public String getkJuchMeisaiNo() {
		return this.kJuchMeisaiNo;
	}

	/**
	 * @param kJuchMeisaiNo 設定する kJuchMeisaiNo
	 */
	public void setkJuchMeisaiNo(String kJuchMeisaiNo) {
		this.kJuchMeisaiNo = kJuchMeisaiNo;
	}

	/**
	 * @return webAccount
	 */
	public String getWebAccount() {
		return this.webAccount;
	}

	/**
	 * @param webAccount 設定する webAccount
	 */
	public void setWebAccount(String webAccount) {
		this.webAccount = webAccount;
	}

	/**
	 * @return mailAddress
	 */
	public String getMailAddress() {
		return this.mailAddress;
	}

	/**
	 * @param mailAddress 設定する mailAddress
	 */
	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	/**
	 * @return mailAccount
	 */
	public String getMailAccount() {
		return this.mailAccount;
	}

	/**
	 * @param mailAccount 設定する mailAccount
	 */
	public void setMailAccount(String mailAccount) {
		this.mailAccount = mailAccount;
	}

	/**
	 * @return juchNo
	 */
	public String getJuchNo() {
		return this.juchNo;
	}

	/**
	 * @param juchNo 設定する juchNo
	 */
	public void setJuchNo(String juchNo) {
		this.juchNo = juchNo;
	}

	/**
	 * @return hyoujiKbn
	 */
	public String getHyoujiKbn() {
		return this.hyoujiKbn;
	}

	/**
	 * @param hyoujiKbn 設定する hyoujiKbn
	 */
	public void setHyoujiKbn(String hyoujiKbn) {
		this.hyoujiKbn = hyoujiKbn;
	}

	/**
	 * @return itemInfoDtoList
	 */
	public List<ItemInfoDto> getItemInfoDtoList() {
		return this.itemInfoDtoList;
	}

	/**
	 * @param itemInfoDtoList 設定する itemInfoDtoList
	 */
	public void setItemInfoDtoList(List<ItemInfoDto> itemInfoDtoList) {
		this.itemInfoDtoList = itemInfoDtoList;
	}

	/**
	 * @return hpInfoDto
	 */
	public HpInfoDto getHpInfoDto() {
		return this.hpInfoDto;
	}

	/**
	 * @param hpInfoDto 設定する hpInfoDto
	 */
	public void setHpInfoDto(HpInfoDto hpInfoDto) {
		this.hpInfoDto = hpInfoDto;
	}

	/**
	 * @return mailInfoDtoList
	 */
	public List<MailInfoDto> getMailInfoDtoList() {
		return this.mailInfoDtoList;
	}

	/**
	 * @param mailInfoDtoList 設定する mailInfoDtoList
	 */
	public void setMailInfoDtoList(List<MailInfoDto> mailInfoDtoList) {
		this.mailInfoDtoList = mailInfoDtoList;
	}

	/**
	 * @return selectMailAddr
	 */
	public String getSelectMailAddr() {
		return this.selectMailAddr;
	}

	/**
	 * @param selectMailAddr 設定する selectMailAddr
	 */
	public void setSelectMailAddr(String selectMailAddr) {
		this.selectMailAddr = selectMailAddr;
	}

	/**
	 * @return mailCountNum
	 */
	public String getMailCountNum() {
		return this.mailCountNum;
	}

	/**
	 * @param mailCountNum 設定する mailCountNum
	 */
	public void setMailCountNum(String mailCountNum) {
		this.mailCountNum = mailCountNum;
	}

	/**
	 * @return consolidateBill
	 */
	public boolean isConsolidateBill() {
		return this.consolidateBill;
	}

	/**
	 * @param consolidateBill 設定する consolidateBill
	 */
	public void setConsolidateBill(boolean consolidateBill) {
		this.consolidateBill = consolidateBill;
	}

	/**
	 * @return serviceTerminationApplied
	 */
	public boolean getServiceTerminationApplied() {
		return this.serviceTerminationApplied;
	}

	/**
	 * @param serviceTerminationApplied 設定する serviceTerminationApplied
	 */
	public void setServiceTerminationApplied(boolean serviceTerminationApplied) {
		this.serviceTerminationApplied = serviceTerminationApplied;
	}

	/**
	 * @return serviceTermination
	 */
	public boolean getServiceTermination() {
		return this.serviceTermination;
	}

	/**
	 * @param serviceTermination 設定する serviceTermination
	 */
	public void setServiceTermination(boolean serviceTermination) {
		this.serviceTermination = serviceTermination;
	}

	/**
	 * @return completeOfHPFlag
	 */
	public boolean isCompleteOfHPFlag() {
		return this.completeOfHPFlag;
	}

	/**
	 * @param completeOfHPFlag 設定する completeOfHPFlag
	 */
	public void setCompleteOfHPFlag(boolean completeOfHPFlag) {
		this.completeOfHPFlag = completeOfHPFlag;
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see jp.co.forvaltel.common.form.AbstractForm#reset()
	 */
	@Override
	public void reset() {
		// 各処理の初期化実行
	}

	/**
	 * Form情報の初期化
	 */
	public void resetForm() {
		this.bannerImgLink = CommonConst.EMPTY_STRING;
		this.bannerUrlLink = CommonConst.EMPTY_STRING;
		this.kkykMei = CommonConst.EMPTY_STRING;
		this.kkykBng = CommonConst.EMPTY_STRING;
		this.seiqCd = CommonConst.EMPTY_STRING;
		this.haraiKbnSetName = CommonConst.EMPTY_STRING;
		this.haraiKbnSetMethod = CommonConst.EMPTY_STRING;
		this.haraiKbnSetError = CommonConst.EMPTY_STRING;
		this.haraiKbnChengeName = new LinkedList<String>();
		this.haraiKbnChengeMethod = new LinkedList<String>();
		this.kaiyakuDate = CommonConst.EMPTY_STRING;
		this.ebillSsoParameterDto = new EbillSsoParameterDto();
		this.pjuchNo = CommonConst.EMPTY_STRING;
		this.pJuchMeisaiNoList = new LinkedList<String>();
		this.pJuchMeisaiNo = CommonConst.EMPTY_STRING;
		this.kjuchNo = CommonConst.EMPTY_STRING;
		this.kJuchMeisaiNo = CommonConst.EMPTY_STRING;
		this.webAccount = CommonConst.EMPTY_STRING;
		this.mailAddress = CommonConst.EMPTY_STRING;
		this.mailAccount = CommonConst.EMPTY_STRING;
		this.juchNo = CommonConst.EMPTY_STRING;
		this.hyoujiKbn = CommonConst.EMPTY_STRING;
		this.itemInfoDtoList = new LinkedList<ItemInfoDto>();
		this.hpInfoDto = null;
		this.mailInfoDtoList = null;
		this.selectMailAddr = CommonConst.EMPTY_STRING;
		this.mailCountNum = "0";
		this.consolidateBill = false;
		this.serviceTerminationApplied = false;
		this.serviceTermination = false;
		//TTM_DEV 20170710 ADD BEGIN
		this.juchKihonInfoDtoList = new ArrayList<JuchKihonInfoDto>();
		//TTM_DEV 20170710 ADD END
	}

	/**
	 * 完了画面用制御フラグをリセット
	 */
	public void resetCompleteFlag() {
		this.completeOfHPFlag = false;
	}
	
	/**
	 * @return the internetConnectionId
	 */
	/**
	 * @return the internetConnectionId
	 */
	public String getInternetConnectionId() {
		return internetConnectionId;
	}
	/**
	 * @param internetConnectionId the internetConnectionId to set
	 */
	public void setInternetConnectionId(String internetConnectionId) {
		this.internetConnectionId = internetConnectionId;
	}
	/**
	 * @return the accountClassification
	 */
	public String getAccountClassification() {
		return accountClassification;
	}
	/**
	 * @param accountClassification the accountClassification to set
	 */
	public void setAccountClassification(String accountClassification) {
		this.accountClassification = accountClassification;
	}
	/**
	 * @return the screen
	 */
	public String getScreen() {
		return screen;
	}
	/**
	 * @param screen the screen to set
	 */
	public void setScreen(String screen) {
		this.screen = screen;
	}

}
