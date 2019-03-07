/**
 * 初期処理
 */
$(function() {

	// ---- 各種イベント設定 ----
	// 支払方法
	$('#paymentMode').on('click', function() {
		clickPaymentMode(this);
	});
	// 支払方法変更１
	$('#paymentModeChenge1').on('click', function() {
		clickPaymentModeChenge1(this);
	});
	// 支払方法変更２
	$('#paymentModeChenge2').on('click', function() {
		clickPaymentModeChenge2(this);
	});
	//TTM_DEV 20170703 ADD BEGIN
	$('#paymentModeChenge3').on('click', function() {
		clickPaymentModeChenge3(this);
	});
	$('#paymentModeChenge4').on('click', function() {
		clickPaymentModeChenge4(this);
	});
	//TTM_DEV 20170703 ADD END
	// 請求情報リンク
	$('#eBillSsoLink').on('click', function() {
		onClickEbillSso();
	});
	//TTM_DEV  20170529 BEGIN UPDATE
//	// 商品情報：新規追加
//	$('#addOption').on('click', function() {
//		clickAddOption();
//	});
	$(".add-option-button").on('click', function() {
		clickAddOption($(this).attr('id'));
	});
	
//	TTM-DEV 20180130 BEGIN ADD
	// Transfer to KaiyakuInfo Screen
	$('#btnAboutCancellationURL').on('click', function() {
	clickKaiyakuInfo($(this).attr('id'));
	});	
//	TTM-DEV 20180130 END ADD
	
//	TTM_DEV  20170529 END UPDATE
	// 商品情報：キャンセル（deleteOption）
	$('#deleteOption').live('click', function() {
		clickDeleteOption(this);
	});

	// ホームページ情報：新規追加
	$('#addHp').live('click', function() {
		clickAddHp(this);
	});
	// ホームページ情報：パスワード変更
	$('#hpPassWdChenge').live('click', function() {
		clickHpPassWdChenge(this);
	});
	// ホームページ情報：削除
	$('#hpDelete').live('click', function() {
		clickHpDelete(this);
	});

	// メール情報：新規追加
	$('#addMail').live('click', function() {
		clickAddMail(this);
	});
	// メール情報：メール詳細設定
	$('#mailDetailSetting').live('click', function() {
		clickMailDetailSetting(this);
	});
	// メール情報：削除
	$('#mailDelete').live('click', function() {
		clickMailDelete(this);
	});
	// メール情報：パスワード変更
	$('#mailPassWdChenge').live('click', function() {
		clickMailPassWdChenge(this);
	});

	// 完了画面（メインメニューへ）
	$('#ft-btn-finish').on('click', function() {
		clickMainMenu();
	});
});

//TTM_DEV-593 20171213 BEGIN UPDATE
//ボタン押下後、JSでgotoAction(haraiKbnSetValue);する。
/** 支払方法 */
function clickPaymentMode(obj) {
	// var gotoMethod = $(obj).val();
	var haraiKbn = $(obj).val();
	var gotoMethod = getMethod(haraiKbn);
	if (haraiKbn == '2' || haraiKbn == '6') {
		gotoTransferAction(gotoMethod, haraiKbn);
	} else {
		gotoAction(gotoMethod);
	}
}

/** 支払方法変更１ */
function clickPaymentModeChenge1(obj) {
	// var gotoMethod = $(obj).val();
	var haraiKbn = $(obj).val();
	var gotoMethod = getMethod(haraiKbn);
	if (haraiKbn == '2' || haraiKbn == '6') {
		gotoTransferAction(gotoMethod, haraiKbn);
	} else {
		gotoAction(gotoMethod);
	}
}
/** 支払方法変更２ */
function clickPaymentModeChenge2(obj) {
	// var gotoMethod = $(obj).val();
	var haraiKbn = $(obj).val();
	var gotoMethod = getMethod(haraiKbn);
	if (haraiKbn == '2' || haraiKbn == '6') {
		gotoTransferAction(gotoMethod, haraiKbn);
	} else {
		gotoAction(gotoMethod);
	}
}
//TTM_DEV 201700703 ADD BEGIN
/** 支払方法変更3 */
function clickPaymentModeChenge3(obj) {
	// var gotoMethod = $(obj).val();
	var haraiKbn = $(obj).val();
	var gotoMethod = getMethod(haraiKbn);
	if (haraiKbn == '2' || haraiKbn == '6') {
		gotoTransferAction(gotoMethod, haraiKbn);
	} else {
		gotoAction(gotoMethod);
	}
}
/** 支払方法変更4 */
function clickPaymentModeChenge4(obj) {
	// var gotoMethod = $(obj).val();
	var haraiKbn = $(obj).val();
	var gotoMethod = getMethod(haraiKbn);
	if (haraiKbn == '2' || haraiKbn == '6') {
		gotoTransferAction(gotoMethod, haraiKbn);
	} else {
		gotoAction(gotoMethod);
	}
}
//TTM_DEV-593 20171213 END UPDATE

//TTM_DEV 201700703 ADD END
/** Ebill SSO 処理 */
function onClickEbillSso() {
	// Ebill SSO用のサブミット
	$('[name=ebillSsoForm]').submit();
}
//TTM_DEV  20170529 BEGIN UPDATE
/** 商品情報：新規追加 */
//function clickAddOption() {
//	var form = $('[name=contractInfoQueryActionForm]');
//	$(form).attr('action',  $('#ft-contextpath').html() + '/addOption/');
//	$(form).submit();
//}
// Click addOption set kihon_cd
/** 商品情報：新規追加 */
function clickAddOption(targetId){
	var target = $('#' + targetId);
	var juchNo = $(target).parent('div').children("input[name='pJuchNo']").val();
	var juchMeisaiNo = $(target).parent('div').children("input[name='pJuchMeisaiNo']").val();
	$("#kJuchNo").val(juchNo);
	$("#kJuchMeisaiNo").val(juchMeisaiNo);
	var form = $('[name=contractInfoQueryActionForm]');
	$(form).attr('action',  $('#ft-contextpath').html() + '/addOption/');
	// Submit
	$(form).submit();
}
// TTM-DEV 20180130 BEGIN ADD
/** Transfer to KaiyakuInfo Screen */
function clickKaiyakuInfo(targetId){
	var target = $('#' + targetId);
	var form = $('[name=contractInfoQueryActionForm]');
	$(form).attr('action',  $('#ft-contextpath').html() + '/kaiyakuInfo/');
	$(form).submit();	}
// TTM-DEV 20180130 END ADD

//TTM_DEV  20170529 END ADD
/**
 * 商品情報：キャンセルボタン（オプション追加申請取消）
 */
function clickDeleteOption(obj) {
	// 重複押下制御
	if (!mhDisableOnSubmit(1000)) {
		return;
	}
	showConfirm('オプションキャンセルを行います。よろしいですか？', function(){
		var juchNo = $(obj).parent('td').children('.juchInfo').children("input[name='kakuJuchNo']").val();
		var juchMeisaiNo = $(obj).parent('td').children('.juchInfo').children("input[name='kakuJuchMeisaiNo']").val();
	
		// 該当カラム値をhiddenに設定
		$("#kJuchNo").val(juchNo);
		$("#kJuchMeisaiNo").val(juchMeisaiNo);
		// 画面遷移
		var form = $('[name=contractInfoQueryActionForm]');
		$(form).attr('action', $(form).attr('action') + 'deleteOption');
		$(form).submit();
	});
}

/** ホームページ情報：新規追加 */
function clickAddHp(ojb) {
	var form = $('[name=contractInfoQueryActionForm]');
//	TTM_DEV 20170606 BEGIN ADD
	var radId = $(ojb).parent("div").children("input[name='radId']").val();
	var accountClass = $(ojb).parent("div").children("input[name='accountClass']").val();
	$("#internetConnectionId").val(radId);
	$("#accountClassification").val(accountClass);
//	TTM_DEV 20170606 END ADD
	$(form).attr('action',  $('#ft-contextpath').html() + '/homepageSettingRegister/init/');
	$(form).submit();
}
/** ホームページ情報：パスワード変更 */
function clickHpPassWdChenge(ojb) {
	var webAccount = $(ojb).parent('td').children('.hpInfo').children("input[name='selectWebAccount']").val();
	// 該当カラム値をhiddenに設定
	$("#webAccount").val(webAccount);
	var form = $('[name=contractInfoQueryActionForm]');
//	TTM_DEV 20170606 BEGIN ADD
	var radId = $(ojb).parent("td").children('.hpInfo').children("input[name='selectInternetConnectionId']").val();
	var accountClass = $(ojb).parent("td").children('.hpInfo').children("input[name='selectaccountClass']").val();
	$("#internetConnectionId").val(radId);
	$("#accountClassification").val(accountClass);
//	TTM_DEV 20170606 BEGIN ADD
	$(form).attr('action', $('#ft-contextpath').html() + '/homepageSettingRegister/init/');
	$(form).submit();
}
/** ホームページ情報：削除 */
function clickHpDelete(ojb) {
	// 重複押下制御
	if (!mhDisableOnSubmit(1000)) {
		return;
	}

	showConfirm('ホームページ削除を行います。よろしいですか？', function(){
		// 画面遷移
		var form = $('[name=contractInfoQueryActionForm]');
//		TTM_DEV 20170606 BEGIN ADD
		var radId = $(ojb).parent("div").parent("div").parent("div").children("input[name='radId']").val();
		var accountClass =  $(ojb).parent("div").parent("div").parent("div").children("input[name='accountClass']").val();
		var getwebAccount = $(ojb).parent("div").parent("div").parent("div").children("input[name='webAc']").val();
		$("#internetConnectionId").val(radId);
		$("#accountClassification").val(accountClass);
		$("#webAccount").val(getwebAccount);
//		TTM_DEV 20170606 BEGIN ADD
		$(form).attr('action', $(form).attr('action') + 'deleteHp');
		$(form).submit();
	});
}

/** メール情報：新規追加 */
function clickAddMail(ojb) {
//	TTM_DEV  20170529 BEGIN ADD
	var radId = $(ojb).parent("div").children("input[name='radId']").val();
	var accountClass =  $(ojb).parent("div").children("input[name='accountClass']").val();
	$("#internetConnectionId").val(radId);
	$("#accountClassification").val(accountClass);
//  TTM_DEV  20170529 END ADD
	var form = $('[name=contractInfoQueryActionForm]');
	$(form).attr('action',  $('#ft-contextpath').html() + '/mailAccountRegisterChange/registerInit/');
	$(form).submit();
}
/** メール情報：メール詳細設定 */
function clickMailDetailSetting(obj) {
	var mailAddress = $(obj).parent('td').children('.mailInfo').children("input[name='selectMailAddr']").val();
	var mailAccount = $(obj).parent('td').children('.mailInfo').children("input[name='selectMailAccount']").val();
	// 該当カラム値をhiddenに設定
//	TTM_DEV 20170606 BEGIN ADD
	$("#mailAddress").val(mailAddress);
	$("#mailAccount").val(mailAccount);
	var radId = 		$(obj).parent("td").parent("tr").parent("tbody").parent("table").parent("div").children("input[name='radId']").val();
	var accountClass =  $(obj).parent("td").parent("tr").parent("tbody").parent("table").parent("div").children("input[name='accountClass']").val();
	$("#internetConnectionId").val(radId);
	$("#accountClassification").val(accountClass);
//	TTM_DEV 20170606 END ADD
	var form = $('[name=contractInfoQueryActionForm]');
	$(form).attr('action',  $('#ft-contextpath').html() + '/mailAccountSetUp/');
	$(form).submit();
}
/** メール情報：削除 */
function clickMailDelete(obj) {
	// 重複押下制御
	if (!mhDisableOnSubmit(1000)) {
		return;
	}	

	showConfirm('メール解約を行います。よろしいですか？', function(){
		var mailAddr = $(obj).parent('td').children('.mailInfo').children("input[name='selectMailAddr']").val();
		// 該当カラム値をhiddenに設定
		$("#mailAddress").val(mailAddr);
		// 画面遷移
//		TTM_DEV 20170606 BEGIN ADD
		var radId = 		$(obj).parent("td").parent("tr").parent("tbody").parent("table").parent("div").children("input[name='radId']").val();
		var accountClass =  $(obj).parent("td").parent("tr").parent("tbody").parent("table").parent("div").children("input[name='accountClass']").val();
		$("#internetConnectionId").val(radId);
		$("#accountClassification").val(accountClass);
//		TTM_DEV 20170606 END ADD
		var form = $('[name=contractInfoQueryActionForm]');
		$(form).attr('action', $(form).attr('action') + 'removeMail');
		$(form).submit();
	});
}
/** メール情報：パスワード変更 */
function clickMailPassWdChenge(obj) {
	var mailAddress = $(obj).parent('td').children('.mailInfo').children("input[name='selectMailAddr']").val();
	var mailAccount = $(obj).parent('td').children('.mailInfo').children("input[name='selectMailAccount']").val();
	// 該当カラム値をhiddenに設定
//	TTM_DEV 20170606 BEGIN ADD
	$("#mailAddress").val(mailAddress);
	$("#mailAccount").val(mailAccount);
	var radId = 		$(obj).parent("td").parent("tr").parent("tbody").parent("table").parent("div").children("input[name='radId']").val();
	var accountClass =  $(obj).parent("td").parent("tr").parent("tbody").parent("table").parent("div").children("input[name='accountClass']").val();
	$("#internetConnectionId").val(radId);
	$("#accountClassification").val(accountClass);
//	TTM_DEV 20170606 END ADD
	var form = $('[name=contractInfoQueryActionForm]');
	$(form).attr('action',  $('#ft-contextpath').html() + '/mailAccountRegisterChange/changeInit/');
	$(form).submit();
}

/** 完了画面（メインメニューへ） */
function clickMainMenu() {
	// 重複押下制御
	if (!mhDisableOnSubmit(1000)) {
		return;
	}

	var form = $('[name=contractInfoQueryActionForm]');
	$(form).attr('action', $(form).attr('action') + 'index');
	$(form).submit();
}

//TTM_DEV-593 20171213 BEGIN ADD
/** 支払区分コード値から、該当する実行メソッド名を返却する */
function getMethod(code) {
	var method = '';
	if (code == '2' || code == '6') {
		// 口座振替
		method = 'accountTransfer';
	} else if (code == '3') {
		// クレジット
		method = 'paymentCreditRegister';
	} else if (code == '4') {
		// NTT合算
		method = 'paymentAddingUpRegister';
	}
	
	return method;
}
//TTM_DEV-593 20171213 END ADD
