/**
 * 初期処理
 */
$(function() {
	// ---- 各種イベント設定 ----
	// 登録ボタン
	$('#ft-btn-regist').on('click', function() {
		clickRegist();
	});
	// 変更ボタン
	$('#ft-btn-change').on('click', function() {
		clickChange();
	});
	// メニューボタン
	$('#ft-btn-menu').on('click', function() {
		clickMenu();
	});
});

/**
 * 登録ボタン onclick処理
 */
function clickRegist() {
	// 重複押下制御
	if (!mhDisableOnSubmit(1000)) {
		return;
	}

	var form = $('[name=mailAccountRegisterChangeActionForm]');
	$(form).attr('action', $(form).attr('action') + 'register');
	$(form).submit();
};

/**
 * 変更ボタン onclick処理
 */
function clickChange() {
	// 重複押下制御
	if (!mhDisableOnSubmit(1000)) {
		return;
	}

	var form = $('[name=mailAccountRegisterChangeActionForm]');
	$(form).attr('action', $(form).attr('action') + 'change');
	$(form).submit();
};

/**
 * メインメニューヘボタン onclick処理
 */
function clickMenu() {
	// 契約状況照会 初期表示へ
	gotoAction('contractInfoQuery');
};
