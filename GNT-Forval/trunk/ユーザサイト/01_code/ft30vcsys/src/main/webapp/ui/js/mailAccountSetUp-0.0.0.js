/**
 * 初期処理
 */
$(function() {
	// 「転送設定を無効にする」を選択した時
	$('#radioNoTransfer').on('click', function() {
		disableTransfer();
	});
	if($("input[name='transferConfigration']:checked").val() == "0") {
		disableTransfer();
	}
	// 「転送先メールアドレス」を選択した時
	$('#radioTransferMailAddress').on('click', function() {
		enableTransfer();
	});
	if($("input[name='transferConfigration']:checked").val() == "1") {
		enableTransfer();
	}
	// 「自動返信設定を有効にする」チェックボックスをチェックする時
	$('#checkboxAutoReplyConfigration').on('change', function() {
    	if($('#checkboxAutoReplyConfigration').attr("checked")) {
    		enableAutoReplyConfigration();
    	}
    	if(!$('#checkboxAutoReplyConfigration').attr("checked")) {
    		disableAutoReplyConfigration();
    	}
	});
	if($('#checkboxAutoReplyConfigration').attr("checked")) {
		enableAutoReplyConfigration();
	}
	if(!$('#checkboxAutoReplyConfigration').attr("checked")) {
		disableAutoReplyConfigration();
	}
	// 更新ボタン
	$('#ft-btn-update').on('click', function() {
		clickUpdate();
	});
	// 戻るボタン
	$('#ft-btn-back').on('click', function() {
		clickBack();
	});
});

/**
 * 更新ボタン onclick処理
 */
function clickUpdate() {
	// 重複押下制御
	if (!mhDisableOnSubmit(1000)) {
		return;
	}

	// 「転送時にこのアドレスにメールを残す」チェックボックス
	if($('#checkboxLeaveMail').attr("checked")) {
		$('#leaveMail').val("1");
	}
	if(!$('#checkboxLeaveMail').attr("checked")) {
		$('#leaveMail').val("0");
	}
	// 転送先メールアドレス
	$('#transferMailAddress').val($('#textTransferMailAddress').val());
	// 転送設定
	if($('#checkboxAutoReplyConfigration').attr("checked")) {
		$('#autoReplyConfigration').val("1");
	}
	if(!$('#checkboxAutoReplyConfigration').attr("checked")) {
		$('#autoReplyConfigration').val("0");
	}
	// 自動返信メールの件名
	$('#subject').val($('#textSubject').val());
	// 自動返信メールの本文
	$('#text').val($('#textareaText').val());
	
	var form = $('[name=mailAccountSetUpActionForm]');
	$(form).attr('action', $(form).attr('action') + 'change');
	$(form).submit();
};
/**
 * 自動返信設定無効化
 */
function disableAutoReplyConfigration() {
	// 自動返信設定
	$('#textSubject').attr('disabled', 'disabled');
	$('#textareaText').attr('disabled', 'disabled');
};
/**
 * 自動返信設定有効化
 */
function enableAutoReplyConfigration() {
	// 自動返信設定
	$('#textSubject').removeAttr('disabled');
	$('#textareaText').removeAttr('disabled');
};
/**
 * 転送設定有効化
 */
function enableTransfer() {
	// 転送先メールアドレス
	$('#textTransferMailAddress').removeAttr('disabled');
	$('#checkboxLeaveMail').removeAttr('disabled');
};
/**
 * 転送設定無効化
 */
function disableTransfer() {
	// 転送先メールアドレス
	$('#textTransferMailAddress').attr('disabled', 'disabled');
	$('#checkboxLeaveMail').attr('disabled', 'disabled');
};
/**
 * 戻るボタン onclick処理
 */
function clickBack() {
	// 契約状況照会 初期表示へ
	gotoAction('contractInfoQuery');
};
