/** 既に入力されている拡張項目の連想配列 */
inputKakuchoHash = {};

/**
 * フォームSubmit
 */
function submitTargetForm(target, url){
	// Submit
	var thisForm = $(target).parents('form');
	$(thisForm).attr('action', $('base').attr('href') + url);
	$(thisForm).submit();	
}

/**
 * 8桁のランダム文字列を生成
 */
function createRandomId(){
	// 生成する文字列の長さ
	var l = 8;

	// 生成する文字列に含める文字セット
	var c = "abcdefghijklmnopqrstuvwxyz0123456789";

	var cl = c.length;
	var r = "";
	for(var i=0; i<l; i++){
	  r += c[Math.floor(Math.random()*cl)];
	}
	return r;
}
	
/**
 * 拡張項目アクションURL取得
 */
function getKakuchoUrl(target){
	return $('base').attr('href') + 'kakuchou/' + target;
}

/**
 * 拡張項目表示区分取得
 */
function getKakuchoHoujiKbn(){
	var ret = $('#ft-kakcho-kbn').html();
	if (isReadOnly()) {
		ret = 3;
	}
	return ret;
}

/**
 * 商品拡張項目設定処理
 */
function settingApplyKakuchou(target, isView)　{
	var data = {
			'token': $('input[name=token]').val(),
			'moshikomiNo': $('input[name=moshikomi_no]').val(),
			'mode': '1'		
	};
	
	$.ajax({
		type: 'POST',
		url: getKakuchoUrl('getkakuchouList'),
		data: data,
	}).success(function(data){
		// 成功の場合、拡張項目設定
		setteingKakuchou(data, target);
		if (isView) {
			$('#agree-button').attr('disabled', true);
			$('.ft-kakuchou-check').each(function() {
				$(this).prop('checked', true);
				$(this).attr('disabled', true);
			});
		}
		ajLoaderOff(true);
	}).error(function(data){
		// 通信失敗のメッセージ表示
		ajLoaderOff(false,LIST_TXT.REQUEST_ERROR);
	});	
}

/**
 * 拡張項目設定処理
 */
function setteingKakuchou(data, target) {
	// 自身の親のTR取得
	var nowTr = $(target).parents('tr:first');
	$('[data-parent=' + $(target).attr('id') + ']').remove();

	// 取得した拡張項目分処理を繰り返し
	$.each(data, function(i, obj) {	
		// 既に入力されている情報を再設定
		var key = obj.srvShohinKbn + '-' + obj.srvShohinCd + '-' + obj.koumokuCd;
		if (inputKakuchoHash[key]) {
			obj.hyoujiText = inputKakuchoHash[key];
		}
	
		// ----共通の処理----
		// テンプレート取得
		var template = getTemplate(obj, target);
		// タイトル設定
		setteingTitle(obj, template);
		// コメント設定
		setteingComment(obj, template);

		// ----各タグ向けの処理----
		// ラベル向け設定
		setteingLabel(obj, template);
		// テキスト向け設定
		setteingText(obj, template);
		// 日付向け設定
		var dateId = setteingDate(obj, template);
		// ボタン向け設定
		setteingButton(obj, template);
		// リンク向け設定
		setteingLink(obj, template);
		// SELECTタグ向け設定
		setteingSelect(obj, template);
		// Check向け設定
		setteingCheck(obj, template);

		// タグ追加
		$(nowTr).after(template);
		
		// 日付選択のイベント設定
		if (dateId != '') {
			$('#' + dateId).datepicker({"dayNamesMin":["日","月","火","水","木","金","土"],
										"monthNamesShort":["1","2","3","4","5","6","7","8","9","10","11","12"],
										"nextText":"来月",
										"dateFormat":"yy/mm/dd",
										"closeText":"閉じる",
										"altField":'#' + $(this).id,
										"currentText":"現在",
										"firstDay":0,
										"dayNamesShort":["日","月","火","水","木","金","土"],
										"monthNames":["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"],
										"dayNames":["日曜日","月曜日","火曜日","水曜日","木曜日","金曜日","土曜日"],"prevText":"先月"});
		}
		
		// 非活性処理実行
		execDisabled();
	});
}

/**
 * CHECKボックス向け設定
 */
function setteingCheck(obj, template){
	// テンプレートからCHECKボックスのClassを保持するタグを取得
	var　check = $(template).find('.ft-template-item-check');
	// 存在しない場合、処理終了
	if ($(check).length == '0') {
		return;
	}
	
	// Checkされている値を保持するHiddenを定義
	var checkHidden = $(template).find('.ft-template-item-check-hidden');
	var index = $('.ft-order-hidden').length;
	$(checkHidden).attr('name', 'kakuchouDtoList[' + index + '].inputValue');
	$(checkHidden).val(obj.hyoujiText);
	$(checkHidden).removeClass('ft-template-item-check-hidden');

	// チェックボックス用の設定を取得
	var checkParent = $(check).parent();
	var insertPoint = $(checkParent).children('br');
	var checkLabel = $(template).find('.ft-template-item-check-label');
	var checkBase = $(check).clone(true);
	var checkBaseLabel = $(checkLabel).clone(true);
	
	// 既に入力されている値をSplitして配列を作成
	if (obj.hyoujiText == null) {
		obj.hyoujiText = '';
	}
	var inputedValue = obj.hyoujiText.split(',');

	// 戻り値のJSON分オプション追加
	if (obj.selectOptionList != null) {
		$.each(obj.selectOptionList, function(i, optionMap) {
			// 追加用のタグ情報取得
			var checkAdd = $(check).clone(true);
			var checkAddLabel = $(checkBaseLabel).clone(true);
			
			// 属性の設定
			var checkId = createRandomId(); 
			$(checkAdd).val(optionMap.value);
			$(checkAdd).attr('id', checkId);
			for(var i = 0; i < inputedValue.length; i++) {
				if (inputedValue[i] == optionMap.value) {
					$(checkAdd).prop('checked', true);
				}
			}
			if (obj.disabledFlg == true || (isReadOnly() && obj.readOnlySkipFlg == false)) {
				$(checkAdd).prop('disabled', true);
			}
			$(checkAddLabel).html(optionMap.label);
			$(checkAddLabel).attr('for', checkId);

			// 不要なClassを削除
			$(checkAdd).removeClass('ft-template-item-check');
			$(checkAddLabel).removeClass('ft-template-item-check-label');

			// 対象のタグを追加
			$(insertPoint).before(checkAdd);
			$(insertPoint).before(checkAddLabel);
		});		
	}
	
	// テンプレートオブジェクトを削除
	$(check).remove();
	$(checkLabel).remove();
}

/**
 * SELECTボックス向け設定
 */
function setteingSelect(obj, template){
	// テンプレートからSELECTボックスのClassを保持するタグを取得
	var　select = $(template).find('.ft-template-item-select');
	// 存在しない場合、処理終了
	if ($(select).length == '0') {
		return;
	}
	
	// 値を設定
	$(select).val(obj.hyoujiText);
	// Nameの設定
	var index = $('.ft-order-hidden').length;
	$(select).attr('name', 'kakuchouDtoList[' + index + '].inputValue');
	// 戻り値のJSON分オプション追加
	$.each(obj.selectOptionList, function(i, optionMap) {
		var option = '';
		if (obj.hyoujiText == optionMap.value) {
			option = $('<option>', { value: optionMap.value, text: optionMap.label, selected: 'selected' });
		} else {
			option = $('<option>', { value: optionMap.value, text: optionMap.label});
		}
		$(select).append(option);
	});
	// リストに未選択時のオプション追加
	$emptyOption = $('<option>', { value: '', text: '' });
	$(select).prepend($emptyOption);
	
	// 不要なClassを削除
	$(select).removeClass('ft-template-item-select');
}

/**
 * 日付向け設定
 */
function setteingDate(obj, template){
	// テンプレートから日付のClassを保持するタグを取得
	var date = $(template).find('.ft-template-item-date');
	// 存在しない場合、処理終了
	if ($(date).length == '0') {
		return '';
	}
	
	// ID採番
	var dateId = createRandomId(); 
	
	// 値、IDを設定
	$(date).attr('id', dateId);
	$(date).val(obj.hyoujiText);
	// Nameの設定
	var index = $('.ft-order-hidden').length;
	$(date).attr('name', 'kakuchouDtoList[' + index + '].inputValue');
	// 不要なClassを削除
	$(date).removeClass('ft-template-item-date');
	
	return dateId;
}

/**
 * リンク向け設定
 */
function setteingLink(obj, template){
	// テンプレートからリンクのClassを保持するタグを取得
	var link = $(template).find('.ft-template-item-link');
	// 存在しない場合、処理終了
	if ($(link).length == '0') {
		return;
	}
	
	// 表示設定
	$(link).html(obj.inputHojyoKomento);
	// リンク先設定
	$(link).attr('href',　obj.hyoujiText);
	// 不要なClassを削除
	$(link).removeClass('ft-template-item-link');
}

/**
 * ボタン向け設定
 */
function setteingButton(obj, template){
	// テンプレートからボタンのClassを保持するタグを取得
	var button = $(template).find('.ft-template-item-button');
	// 存在しない場合、処理終了
	if ($(button).length == '0') {
		return;
	}
	
	// IDの設定
	var id = createRandomId();
	$(button).attr('id', id);
	// buttonのラベル設定
	$(button).val(obj.hyoujiText);
	// 実行するjavascriptの指定
	var execString = 'return ' + obj.jsFunction + '("' + id + '")';
	$(button).on('click', function(){
		  myFunction = new Function(execString);
		  myFunction();
	});
	// disabled属性の設定チェック
	if (obj.disabledFlg == true) {
		$(button).prop('disabled', true);
	}
	
	// 不要なClassを削除
	$(button).removeClass('ft-template-item-button');
}

/**
 * テキスト向け設定
 */
function setteingText(obj, template){
	// テンプレートからテキストボックスのClassを保持するタグを取得
	var textBox = $(template).find('.ft-template-item-text');
	// 存在しない場合、処理終了
	if ($(textBox).length == '0') {
		return;
	}

	// 値を設定
	$(textBox).val(obj.hyoujiText);

	// 入力桁数チェック（min）設定
	$(textBox).attr('minlength', obj.textKetaChkMin);
	// 入力桁数チェック（max）設定
	$(textBox).attr('maxlength', obj.textKetaChkMax);
	// Nameの設定
	var index = $('.ft-order-hidden').length;
	$(textBox).attr('name', 'kakuchouDtoList[' + index + '].inputValue');

	// 不要なClassを削除
	$(textBox).removeClass('ft-template-item-text');
}

/**
 * ラベル向け設定
 */
function setteingLabel(obj, template){
	// テンプレートからラベルのClassを保持するタグを取得
	var label =	$(template).find('.ft-template-item-label');
	// 存在しない場合、処理終了
	if ($(label).length == '0') {
		return;
	}
	// 表示テキストを設定
	$(label).html(obj.hyoujiText);

	if (obj.koumokuMei == '契約容量') {
		$(label).after("<span style='margin-left:10px'>kVA</span>");
	}
	
	// 不要なClassを削除
	$(label).removeClass('ft-template-item-label');
}
	
/**
 * コメント設定
 */
function setteingComment(obj, template){
	// テンプレートからコメントのClassを保持するタグを取得
	var comment = $(template).find('.ft-template-item-commnet');
	
	if(obj.inputHojyoKomento != null && 'DISP' == obj.koumokuKbn){
		obj.inputHojyoKomento = '';
	}
	
	// 表示補助コメントを設定
	$(comment).html(obj.inputHojyoKomento);
	// 不要なClassを削除
	$(comment).removeClass('ft-template-item-commnet');
}
	
/**
 * タイトル設定
 */
function setteingTitle(obj, template){
	// テンプレートからタイトルのClassを保持するタグを取得
	var title = $(template).find('.ft-template-item-title');
	// 項目名を設定
	if ('1' == obj.inputHisuFlg　&& $('#ft-kakuchou-hyoujikbn').html()　!= '3'){
		// 必須あり
		$(title).html(obj.koumokuMei + '<span class="font-caution"><small>*</small></span>');
	} else {
		// 必須なし
		$(title).html(obj.koumokuMei);
	}
	// 不要なClassを削除
	$(title).removeClass('ft-template-item-title');
}

/**
 * 拡張項目情報保存用Hiddenタグ追加
 */
function insertHiddenTag(template, index, hiddenName, hiddenValue, classValue){
	// Nameを形成
	var nameValue = 'kakuchouDtoList[' + index + '].' + hiddenName;
	// Hiddenタグ作成
	var hidden = $('<input>', { type:'hidden', name:nameValue, value:hiddenValue, 'class':classValue});
	// 対象テンプレートのtd内に追加
	$(template).find('td:first').append(hidden);
}
	
/**
 * テンプレート取得
 */
function getTemplate(obj, target){
	// 取得するテンプレートを識別するClassを取得
	var tempClass; 
	if ('DISP' == obj.koumokuKbn){
		tempClass = 'ft-template-label';
	}else if ('SELECT' == obj.koumokuKbn) {
		tempClass = 'ft-template-select';
	}else if ('DATE' == obj.koumokuKbn) {
		tempClass = 'ft-template-date';
	}else if ('URL' == obj.koumokuKbn) {
		tempClass = 'ft-template-url';
	}else if ('CHECK' == obj.koumokuKbn) {
		tempClass = 'ft-template-check';
	}else if ('BTN' == obj.koumokuKbn) {
		tempClass = 'ft-template-button';
	}else if ('TXT' == obj.koumokuKbn) {
		tempClass = 'ft-template-text';
	}else if ('TEL' == obj.koumokuKbn) {
		// 電話番号の場合はTEXTと同じ
		tempClass = 'ft-template-text';
	}else if ('MAIL' == obj.koumokuKbn) {
		// メールの場合はTEXTと同じ
		tempClass = 'ft-template-text';
	}
	//TTM_DEV 20170705 ADD BEGIN
	else if ('HIDDEN' == obj.koumokuKbn) {
		// メールの場合はTEXTと同じ
		tempClass = 'ft-template-hidden';
	}
	//TTM_DEV 20170705 ADD END
	// テンプレート取得
	var template = $('.' + tempClass).clone(true);
	// 不要なClassを除去
	$(template).removeClass(tempClass);
	// 紐付く親タグのIDを保存
	$(template).attr('data-parent', $(target).attr('id'));
	
	// 必要なHiddenタグ追加
	var index = $('.ft-order-hidden').length;
	insertHiddenTag(template, index, 'srvShohinKbn', obj.srvShohinKbn, 'ft-order-hidden')
	insertHiddenTag(template, index, 'srvShohinCd', obj.srvShohinCd, 'ft-order-srvShohinCd')
	insertHiddenTag(template, index, 'koumokuCd', obj.koumokuCd, 'ft-order-koumokuCd')
	insertHiddenTag(template, index, 'version', obj.version, 'ft-order-version')
	
	// 戻り値設定
	return template;
}

/**
 * 非活性実行処理
 */
function execDisabled(){
	$('#ft-kakuchou-disabled').find('input').prop('disabled', true);
	$('#ft-kakuchou-disabled').find('select').prop('disabled', true);
}

/**
 * 初期処理
 */
$(function() {
	
	// 拡張項目のチェックボックス変更時のイベント
	$('.ft-kakuchou-check').on('change', function(){
		var tdCheck = $(this).parents('td');
		var value = '';
		$(tdCheck).find('.ft-kakuchou-check').each(function(){
			if ($(this).prop('checked') == true) {
				if (value != '') {
					value = value + ',';
				}
				value = value + $(this).val();
			}
		});
		$(tdCheck).find('.ft-kakuchou-check-hidden').val(value);
	});

	// 既に入力されている拡張項目の連想配列作成
	$('.ft-inputed-kakucho').each(function(){
		inputKakuchoHash[$(this).attr('id')] = $(this).html(); 
	});
	
	// --- 拡張項目 商品表示
	$('.ft-apply-kakuchou').each(function() {
		// 商品拡張項目設定
//		settingApplyKakuchou(this);
	});

});
