/**
 * 初期処理
 */
$(function() {

	// 初期表示処理
	init();
	
	// チェックボックスの状態により活性・非活性を設定
	changeFtDone($('#ft-done'));
	
	// ---- 各種イベント設定 ----
	// 利用規約同意チェックボックス
	$('#ft-done').on('change', function() {
		changeFtDone(this);
	});
	// 同意して申込ボタン
	$('#ft-btn-register').on('click', function() {
		clickRegister();
	});
	// 戻るボタン
	$('#ft-btn-back').on('click', function() {
		// 契約状況照会 初期表示へ
		gotoAction('contractInfoQuery');
	});
	// メインメニューボタン
	$('#ft-btn-mein-menu').on('click', function() {
		// 契約状況照会 初期表示へ
		gotoAction('contractInfoQuery');
	});
	$('.ft-kakuchou-shohinlist').on('change', function() {
		// 商品拡張項目設定
		var selectValue = $(this).val();
		$('.ft-kakuchou-shohin').each(function(){
			$(this).find('.ft-kakuchou-shohincd:first').html(selectValue);
			// 既に入力されている情報をクリア
			inputKakuchoHash = {};
			settingShohinKakuchou(this);
		});

		// 商品表示文言設定
		settingItemDisplayString(this);

		if ($('#ft-done').prop('checked')) {
			// 利用規約同意チェックがオンの場合、オフに変更
			$('#ft-done').trigger('click');
		}
	});
});

/**
 * 利用規約同意チェック onchange処理
 */
function changeFtDone(target) {
	if ($(target).prop('checked')) {
		// チェックがオンの場合、確認を活性化
		$('#ft-btn-register').prop('disabled', false);
	} else {
		// チェックがオフの場合、確認を非活性
		$('#ft-btn-register').prop('disabled', true);
	}
}

/**
 * 初期処理
 */
function init() {
	// 商品コード設定
	var itemCd = $('[name=shohinCd]').val();
	if (itemCd != ''){
		// 商品拡張項目設定
		var selectValue = $('.ft-kakuchou-shohinlist').val();
		$('.ft-kakuchou-shohin').each(function(){
			$(this).find('.ft-kakuchou-shohincd:first').html(selectValue);
			settingShohinKakuchou(this);
		});

		settingItemDisplayString(itemCd);
	}
	
}


/**
 * 確認ボタン onclick処理
 */
function clickRegister() {
	// 重複押下制御
	if (!mhDisableOnSubmit(1000)) {
		return;
	}

	var form = $('[name=addOptionActionForm]');
	$(form).attr('action', $(form).attr('action') + 'addOption');
	$(form).submit();	
}
/**
 * 商品リスト変更時の「商品表示文言」取得処理
 */
function settingItemDisplayString(target) {
	// 商品コード設定
	var itemCd = $('[name=shohinCd]').val();
	if (itemCd != '') {
		$.ajax({
			type: 'POST',
			url: getDispItemStringUrl('getItemData'),
			data: {
				'itemCd':itemCd
			},
		}).success(function(data){
			// 追加しているtrタグを削除する。
			$(".system-add").parent().parent().remove();
			if (data.hyoujiMongon != null && data.hyoujiMongon != ''){
				// 特定のテーブルの最後の<tr>を取得する。
				$("#ft-option-list").append( getRowData(data.hyoujiMongon) );
			}
				var target = document.getElementById('concentCheck');
				target.style.visibility = "visible";
	
		}).error(function(data){
			// 通信失敗のメッセージ表示
		    alert('通信に失敗しました。');
		    //　通信失敗した場合、「同意して申し込み」チェックボックス、ボタンを非活性とする。
		    $('#ft-btn-register').prop('disabled', true);
		    $('#ft-done').prop('disabled', true);
		});
	} else {
		$(".system-add").parent().parent().remove();
		var target = document.getElementById('concentCheck');
		target.style.visibility = "hidden";
	}
	
}
/**
 * 表示文言取得アクションURL取得
 */
function getDispItemStringUrl(target){
	return $('base').attr('href') + 'itemMst/' + target;
}
/**
 * 最終行に備考欄を追加
 */	
function getRowData(obj) {
    return '<tr style="background-color:#FFFFFF"><td colspan="2"><table class="system-add"><tr><td>' + obj + '</td></tr></table></td></tr>';

}

