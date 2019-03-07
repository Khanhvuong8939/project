/*
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.constant;

/**
 * ユーザサイト 共通定数インタフェース.
 *
 * @author smis
 */
public interface UserConst {

	/** NTT登録区分：デフォルト */
	String NTT_TOUROKU_KUBUN_DEFAULT = "0";

	/** アクションパス ログイン */
	String ACTION_PATH_LOGIN = "/login?redirect=true";

	// セッションキ－
	/** セッションキー 支払先情報登録（NTT合算） */
	String S_KEY_PAYMENT_ADDING_UP_REGIST = "Const_SESSION_KEY_PAYMENT_ADDING_UP_REGIST";
	/** セッションキー 支払先情報登録（クレジット） */
	String S_KEY_PAYMENT_CREDIT_REGISTER = "Const_SESSION_KEY_PAYMENT_CREDIT_REGISTER";
	/** セッションキー メールアカウント登録・変更機能アクション処理 */
	String S_KEY_MAIL_ACCOUNT_REGISTER_CHANGE = "Const_SESSION_KEY_MAIL_ACCOUNT_REGISTER_CHANGE";
	/** セッションキー ホームページ登録・パスワード変更機能 */
	String S_KEY_HOME_PAGE_SETTING_PASSWORD_CHANGE = "Const_SESSION_KEY_HOME_PAGE_SETTING_PASSWORD_CHANGE";
	/** セッションキー支払先情報登録（口座振替） **/
	String S_KEY_ACOUNT_TRANSFER = "Const_SESSION_KEY_ACCOUNT_TRANSFER";
	/** セッションキー Sessionタイムアウトエラー表示 */
	String S_KEY_SESSION_ERROR_MSG = "Const_SESSION_KEY_SESSION_ERROR_MSG";

	// エラーメッセージID
	/** メッセージID：{0}が{1}と一致していません。 */
	String MSG_ID_ERRORS_MATCH = "errors.match";
	/** メッセージID：予期せぬエラーが発生しました。管理者にお問い合わせください。 */
	String MSG_ID_ERRORS_SYSTEM = "errors.system";
	/** メッセージID：{0}にはｅメールアドレスの形式で入力してください. */
	String MSG_ID_ERRORS_EMAIL = "errors.email";
	/** メッセージID：{0}は入力必須項目です. */
	String MSG_ID_ERRORS_REQUIRED = "errors.required";
	/** メッセージID　認証エラー */
	String MSG_ID_ERRORS_FAILURE_AUTH = "errors.failure.auth";
	/** メッセージID　WebAPIシステム例外発生 */
	String MSG_ID_ERRORS_WEBAPI_ERROR = "errors.webapi.error";
	/** メッセージID　支払方法変更不可 */
	String MSG_ID_ERRORS_HARAI_NOT_BE_CHANGED = "errors.harai.not.be.changed";
	/** メッセージID　セッションエラー */
	String MSG_ID_ERRORS_SESSION = "errors.session";
//	TTM_DEV  20170606 BEGIN ADD
	/** check exists internetConnectionId*/
	String MESSAGE_ID_ERRORS_RADID = "errors.radid";
	String MESSAGE_ERRORS_TOKEN = "errors.checktoken";
//	TTM_DEV  20170606 END ADD
	
	/** 既に発行されているアカウントです。<br>メールアドレスを変更してください。 **/
	String MSG_ID_EXIST_MAIL_ADDRESS = "errors.mail.dupe";
	/** 既に発行されているアカウントです。<br>WEBアカウントを変更してください。 **/
	String MSG_ID_EXIST_WEB_ACCOUNT = "errors.web.dupe";

	// メールアカウント登録・変更機能アクション処理
	/** パラメータキー 顧客番号 */
	String PRM_KEY_KKYK_BNG = "kkykBng";
	//TTM_DEV ORDER_MODULE 20170530 BEGIN ADD
	/**Internet Connection  ID*/
	String PRM_KEY_INTERNET_CONNECTTION_ID = "koumokuAtai";
	/**account Classification*/
	String PRM_KEY_ACCOUNT_CLASSIFICATION ="accountClassification";
	//TTM_DEV ORDER_MODULE 20170530 END ADD
	/** パラメータキー メールパスワード */
	String PRM_KEY_MAIL_PASS = "mailPassword";

	// メールアカウント詳細設定機能アクション処理
	/** パラメータキー メールアカウント */
	String PRM_KEY_MAIL_ACCOUNT = "mailAccount";
	/** パラメータキー 転送設定 */
	String PRM_KEY_AUTO_TRANSFER = "autoTransfer";
	/** パラメータキー 転送先メールアドレス */
	String PRM_KEY_TRANSFER_TO_EMAIL = "transferToEmail";
	/** パラメータキー メールを残す */
	String PRM_KEY_MESSAGE_DELETE = "messageDelete";
	/** パラメータキー 自動返信設定 */
	String PRM_KEY_AUTO_REPLY = "autoReply";
	/** パラメータキー 件名 */
	String PRM_KEY_AUTO_REPLY_SUBJECT = "autoReplySubject";
	/** パラメータキー 本文 */
	String PRM_KEY_AUTO_REPLY_BODY = "autoReplyBody";

	// ホームページ設定(登録)アクション処理
	/** WEBアカウント */
	String PRM_KEY_WEBACCOUNT = "webAccount";
	/** WEBアカウントパスワード */
	String PRM_KEY_WEB_ACCOUNT_PASSWORD = "webAccountPassword";
	/** パラメータキー FTPアカウント */
	String PRM_KEY_FTP_USER_NAME = "ftpUserName";
	/** メールアドレスの記号:@ */
	String MAIL_ADDRESS_MARK = "@";

	/** 支払方法確定フラグ：確定前 */
	String HARAI_HOUHOU_KAKUTEI_FLAG_UNDEFINED = "0";
	/** 支払方法確定フラグ：確定後 */
	String HARAI_HOUHOU_KAKUTEI_FLAG_DEFINED = "1";

	// TTM_DEV_11 APPLICATION_LOG BEGIN ADD
	String LOG_TITLE_USER = "ユーザID:　";
	String LOG_TITLE_SHOHIN_CODE= "商品コード： ";
	String LOG_TITLE_SHOHIN_NAME = "商品名：　";
	
	String LOG_SCREEN_TITLE_LOGIN = "ログイン";
	String LOG_SCREEN_CONTRACT_INFO = "契約情報照会";
	String LOG_SCREEN_CARD_REGISTER = "カード情報登録入力";
	String LOG_SCREEN_CARD_CONFIRM = "カード情報登録確認";
	String LOG_SCREEN_OPTIONAL_INFO = "オプション情報";
	String LOG_SCREEN_HOME_REGISTER_INFO = "ホームページ情報登録入力";
	String LOG_SCREEN_HOME_CHANGE_INFO = "ホームページ情報変更入力";
	String LOG_SCREEN_MAIL_REGISTER_INFO = "メール情報登録入力";
	String LOG_SCREEN_MAIL_CHANGE_INFO = "メール情報変更入力";
	String LOG_SCREEN_FINANCE_APPLICATION_INFO = "NTTファイナンス申し込み情報入力";
	String LOG_SCREEN_FINANCE_APPLICATION_CONFIRM = "NTTファイナンス申し込み情報確認";
	
	String LOG_BUTTON_TITLE_LOGIN = "ログイン";
	String LOG_BUTTON_CREDIT_CARD = "支払方法登録（クレジットカード）";
	String LOG_BUTTON_TABAL = "支払方法登録（TABAL）";
	String LOG_BUTTON_HOME_PAGE_NEW = "新規追加（ホームページ）";
	String LOG_BUTTON_HOMEPAGE_PASSWORD_CHANGE = "パスワード変更（ホームページ有り）";
	String LOG_BUTTON_HOMEPAGE_DELETE = "削除（ホームページ有り）";
	String LOG_BUTTON_MAIL_REGISTER = "新規追加（メールアカウント）";
	String LOG_BUTTON_MAIL_PASSWORD_CHANGE = "パスワード変更（メールアカウント有り）";
	String LOG_BUTTON_MAIL_SETTING = "メール詳細設定（メールアカウント有り）";
	String LOG_BUTTON_MAIL_DELETE = "削除（メールアカウント有り）";
	String LOG_BUTTON_OPTION_NEW = "新規追加（オプション）";
	String LOG_BUTTON_OPTION_CANCEL = "キャンセル（オプション追加中）";
	String LOG_BUTTON_REQUEST_CANCEL = "解約申請（オプション有り）";
	String LOG_BUTTON_REGISTER = "登録";
	String LOG_BUTTON_CONFIRM = "確認";
	String LOG_BUTTON_EDIT = "変更";
	String LOG_BUTTON_SHOHIN_NEW = "同意して申込";
	
	String ERROR_MESSAGE_WRITE_LOG = "writing log occurs error";
	// TTM_DEV_11 APPLICATION_LOG END ADD
//	TTM_DEV  20170602 BEGIN ADD	
	String ACCOUNT_CLASS_USER_CODE = "1";
	String ACCOUNT_CLASS_RAD_ID = "2";
	// TTM_DEV-1280 20181210 BEGIN ADD
	String ACCOUNT_CLASS_ENO = "3";
	// TTM_DEV-1280 20181210 END ADD
	String CHECK_TOKEN_TRUE = "1";
	String CHECK_TOKEN_FALSE = "0";
//	TTM_DEV  20170602 END ADD
	
	/** フリービット対応 **/
	String MSG_ID_ERRORS_MAIL_BIG_STRING = "errors.freebit.mail.big.string";
	String MSG_ID_ERRORS_MAIL_START_STRING = "errors.freebit.mail.start.string";
	String MSG_ID_ERRORS_MAIL_CONTINUITY = "errors.freebit.mail.continuity";
}
