/**
 * 初期処理
 */
$(function() {
	// チェックボックスの状態により活性・非活性を設定
	changeFtDone($('#ft-done'));
	
	// ---- 各種イベント設定 ----
	// 利用規約同意チェックボックス
	$('#ft-done').on('change', function() {
		changeFtDone(this);
	});
	// 確認ボタン
	$('#ft-btn-confirm').on('click', function() {
		clickConfirm();
	});
	// 登録ボタン
	$('#ft-btn-regist').on('click', function() {
		// 支払先情報登録(NTT合算)　登録処理へ
		gotoAction('paymentAddingUpRegister/regist');
	});
	// 入力画面 戻るボタン
	$('#ft-btn-back-input').on('click', function() {
		// 確認ダイアログを出力
		var result = confirm("画面を移動します。\n入力中の内容は破棄されます。よろしいですか？");
		if (result) {
			// 契約情報照会 初期表示へ
			gotoAction('contractInfoQuery');
		}
	});
	// 確認画面 戻るボタン
	$('#ft-btn-back-confirm').on('click', function() {
		// 契約状況照会 初期表示へ
		gotoAction('paymentAddingUpRegister');
	});
	// 完了ボタン
	$('#ft-btn-finish').on('click', function() {
		// 契約状況照会 初期表示へ
		gotoAction('contractInfoQuery');
	});
	// クレジットカード決済ボタン
	$('#ft-btn-credit').on('click', function() {
		// 支払先情報登録(クレジット) 初期表示へ
		gotoAction('paymentCreditRegister');
	});
	// 銀行口座振替(引落し)ボタン
	$('#ft-btn-account').on('click', function() {
		// 支払先情報登録(口座振替) 初期表示へ
		gotoAction('accountTransfer');
	});
	
	// 請求めとめ先IDとは？リンク
	$('#ft-link-reference').on('click', function() {
		// まとめ先ID説明画像を開く
		openWindow('#ft-link-reference-url');
	});
	
	// 利用規約はこちらリンク
	$('#ft-link-terms').on('click', function() {
		// 利用規約PDFを開く
		openWindow('#ft-link-terms-url');
	});

	// 登録ボタンイベントバインド
	$('#login-button').on('click', function() {
		// 重複押下制御
		if (!mhDisableOnSubmit(1000)) {
			return;
		}
	
		var form = $('[name=loginActionForm]');
		$(form).attr('action', $(form).attr('action') + "go");
		$(form).submit();
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
