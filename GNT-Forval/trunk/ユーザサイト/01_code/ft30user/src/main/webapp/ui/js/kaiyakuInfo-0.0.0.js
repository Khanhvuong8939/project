/**
 * 初期処理
 */
$(function() {

	// ---- 各種イベント設定 ----
	// 「クレジットカード支払いへ」ボタン
	$('#ft-btn-card').on('click', function() {
		// 支払先情報登録(クレジット) 初期表示へ
		gotoAction('paymentCreditRegister');
	});
	// 「電話料金合算へ」ボタン
	$('#ft-btn-tel').on('click', function() {
		// 支払先情報登録(NTT合算) 初期表示へ
		gotoAction('paymentAddingUpRegister');
	});
	// 「メインメニューへ」ボタン
	$('#ft-btn-mainmenu').on('click', function() {
		// 契約状況照会 初期表示へ
		gotoAction('contractInfoQuery');
	});
});

/**
 * 別画面でWindowを開く
 */
function openWindow(urlDivId){
	window.open($(urlDivId).html(), null, 'location=no,menubar=no,toolbar=no,resizable=yes,fullscreen=yes');	
}
	
/**
 * 利用規約同意チェック onchange処理
 */
function changeFtDone(target) {
	if ($(target).prop('checked')) {
		// チェックがオンの場合、確認を活性化
		$('#ft-btn-confirm').prop('disabled', false);
	} else {
		// チェックがオフの場合、確認を非活性
		$('#ft-btn-confirm').prop('disabled', true);
	}
}

/**
 * 確認ボタン onclick処理
 */
function clickConfirm() {
	// 重複押下制御
	if (!mhDisableOnSubmit(1000)) {
		return;
	}

	var form = $('[name=paymentAddingUpRegisterActionForm]');
	$(form).attr('action', $(form).attr('action') + 'confirmationInit');
	$(form).submit();	
}
