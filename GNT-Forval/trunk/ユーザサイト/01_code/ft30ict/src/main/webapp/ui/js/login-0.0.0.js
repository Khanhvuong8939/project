/**
 * 初期処理
 */
$(function() {
	// SessionId等のパラメータ初期化
	var actionValue = $('form').attr('action');
	$('form').attr('action', actionValue.split(';')[0]);	

	// 登録ボタンイベントバインド
	$('#login-button').on('click', function() {		
		// 重複押下制御
		if (!mhDisableOnSubmit(1000)) {
			return;
		}

		var form = $('[name=loginActionForm]');
		$(form).attr('action', $(form).attr('action') + "login");
		$(form).submit();
	});
	// 同意ボタンイベントバインド
	$('#agree-button').on('click', function() {
		// 重複押下制御
		if (!mhDisableOnSubmit(1000)) {
			return;
		}

		var form = $('[name=loginActionForm]');
		$(form).attr('action', $(form).attr('action') + "movePage");
		$(form).submit();
	});
});
