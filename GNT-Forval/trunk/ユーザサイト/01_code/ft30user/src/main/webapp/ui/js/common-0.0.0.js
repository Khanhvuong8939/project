/**
 * 初期処理
 */
$(function() {
	// コールバックメッセージがあれば表示
	var callbackMsg = $('#ft-callback-message');
	if ($(callbackMsg).length != 0){
		// メッセージを表示し、OKボタン押下後指定のActionに遷移
		showInfo(callbackMsg.html(), function(){
			// 対象のFormを取得
			var callbackForm = $('[name=callbackForm]');
			if ($(callbackForm).length != 0) {
				// 存在した場合、Submit実行
				$(callbackForm).submit();
			}
		});
	}

	// ログアウトのイベント追加
	$('#ft-logout').on('click', function(){
		// 確認ダイアログを出力
		var result = confirm("ログアウトします。\n入力中の内容は破棄されます。よろしいですか？" );
		if (result) {
			gotoAction('login/logout');
		}
	});
});

/**
 * ReadOnly判定
 */
function isReadOnly(){
	var ret = false;
	if ($('#ft-readOnlyFlg').html() == '1'){
		ret = true;
	}
	return ret
}

//TTM_DEV-838 20180906 BEGIN UPDATE
///**
// * 対象のアクションへ遷移
// */
//function gotoAction(target) {
//	// トークンオブジェクトを取得
//	var token = $('[name="org.apache.struts.taglib.html.TOKEN"]');
//
//	// トークンオブジェクトから値を取得
//	var tokenAddString = '';
//	if ($(token).length == 1) {
//		tokenAddString = '?org.apache.struts.taglib.html.TOKEN=' + $(token).val();
//	} else if ($(token).length > 1) {
//		tokenAddString = '?org.apache.struts.taglib.html.TOKEN=' + $(token)[0].value;
//	}
//
//	// URL設定
//	var url = $('base').attr('href') + target;
//	window.location.href = url + tokenAddString;
//}

/**
 * 対象のアクションへ遷移
 */
function gotoAction(target) {
	// トークンオブジェクトを取得
	var token = $('[name="org.apache.struts.taglib.html.TOKEN"]');

	// トークンオブジェクトから値を取得
	var tokenAddString = '';
	if ($(token).length == 1) {
		tokenAddString = $(token).val();
	} else if ($(token).length > 1) {
		tokenAddString = $(token)[0].value;
	}
	
	// formを生成
	var form = $('<form/>', {action: $('base').attr('href') + target + '/', method: 'post'});
	$(form).append($('<input>', { type:'hidden', name:'org.apache.struts.taglib.html.TOKEN', value:tokenAddString}));
	$(form).appendTo(document.body);
	
	$(form).submit();
	
	// formをbodyに追加して、サブミットする。その後、formを削除
	$(form).remove()
}
//TTM_DEV-838 20180906 END UPDATE

// TTM_DEV-593 20171212 BEGIN ADD
/**
 * 対象のアクションへ遷移
 */
function gotoTransferAction(target, haraiKbnChange) {
	// // トークンオブジェクトを取得
//	var token = $('[name="org.apache.struts.taglib.html.TOKEN"]');
    $('[name=haraiKbnChange]').val(haraiKbnChange);

	// トークンオブジェクトから値を取得
//	var tokenAddString = '';
//	if ($(token).length == 1) {
//		tokenAddString = '?org.apache.struts.taglib.html.TOKEN=' + $(token).val();
//	} else if ($(token).length > 1) {
//		tokenAddString = '?org.apache.struts.taglib.html.TOKEN=' + $(token)[0].value;
//	}
	
	var form = $('[name=contractInfoQueryActionForm]');
	$(form).attr('action',  $('base').attr('href') + target + '/');
	$(form).submit();
}
//TTM_DEV-593 20171212 END ADD

/**
 * 確認画面表示
 *
 * @param {String} msg - 表示メッセージ
 * @param {function} okCallBack - OKボタン押下後の処理
 */
function showConfirm(msg, okCallBack) {
    var msg = '<p>' + msg + '</p>';
    var dialogDiv = $('#ft-dialog-confirm');
    $(dialogDiv).html(msg);
	$(dialogDiv).dialog({
		resizable : true,
		height : 300,
		width : 400,
		modal : true,
		buttons : {
			"OK" : function() {
				if (okCallBack != null) {
					okCallBack();
				}
				$(this).dialog("close");
			},
			"Cancel" : function() {
				$(this).dialog("close");
			}
		}
	});
}

/**
 * Information画面表示
 *
 * @param {String} msg - 表示メッセージ
 * @param {function} okCallBack - OKボタン押下後の処理
 */
function showInfo(msg, okCallBack) {
    var msg = '<div>' + msg + '</div>';
    $(msg).imuiMessageDialog({
        iconType : 'info',
        modal : true,
        title : $('#ft-system-name').html(),
        buttons: [
          {
            'id': 'ft-msg-ok-button',
            'text': 'OK',
            'click': function(){
        	  if (okCallBack != null) {
            	  okCallBack();
        	  }
        	  $(this).imuiMessageDialog('close');
            }
          }
        ]
    });
}

// ================================================
// 名称：onSeparateDate
// 概要：入力フォーマット固定(YYYY-MM-DD,YYYY/MM/DD,YYYYMMDD)で
// 入力値を規定のフォーマット(YYYY/MM/DD)に変換して返す。
// 引数：obj
// 戻り値：YYYY/MM/DD
// 作成：2007/06/19
// 更新：
// ================================================
function onSeparateDate(obj) {
	if (obj.value == "") {
		return;
	}

	// 数値四桁＋（区切り文字）＋数値二桁＋（区切り文字）＋数値二桁
	// 区切り文字はあってもなくてもいい
	var sts = /^(\d{4})[-/]?(\d{2})[-/]?(\d{2})$/.exec(obj.value);

	if (sts == null) {
		return;
	} else {
		// 結果が四桁、二桁、二桁でArrayに格納される
		var wk_y = sts[1];
		var wk_m = sts[2];
		var wk_d = sts[3];

		if (parseFloat(wk_y) < 1 || parseFloat(wk_y) > 9999) {
			return;
		}


		if (parseFloat(wk_m) < 1 || parseFloat(wk_m) > 12) {
			return;
		}

		if (parseFloat(wk_d) <1) {
			return;
		}
		if (wk_m.match(/02/)) {
			// ＊閏年チェックがなにげにうまく行かないので分割
			if ((eval(parseFloat(wk_y) % 4) == 0)
					&& (eval(parseFloat(wk_y) % 100) != 0)) {
				if (parseFloat(wk_d) > 29) {
					return;
				}
			} else {
				if (eval(parseInt(wk_y) % 400) == 0) {
					if (parseFloat(wk_d) > 29) {
						return;
					}
				} else {
					if (parseFloat(wk_d) > 28) {
						return;
					}
				}
			}
		} else if (wk_m.match(/04|06|09|11/)) {
			if (parseFloat(wk_d) > 30) {
				return;
			}
		} else {
			if (parseFloat(wk_d) > 31) {
				return;
			}
		}

		obj.value = wk_y + "/" + wk_m + "/" + wk_d;
	}

}

// ================================================
// 名称：onNoSeparateDate
// 概要：入力フォーマット固定(YYYY/MM/DD)で
// 入力値を規定のフォーマット(YYYYMMDD)に変換して返す。
// 引数：obj
// 戻り値：YYYYMMDD
// 作成：2007/06/19
// 更新：
// ================================================
function onNoSeparateDate(obj) {
	if (obj.value == "") {
		return;
	}

	// 数値四桁＋（区切り文字）＋数値二桁＋（区切り文字）＋数値二桁
	// 区切り文字はあってもなくてもいい
	var sts = /^(\d{4})[-/]?(\d{2})[-/]?(\d{2})$/.exec(obj.value);

	if (sts == null) {
		return;
	} else {
		// 結果が四桁、二桁、二桁でArrayに格納される
		var wk_y = sts[1];
		var wk_m = sts[2];
		var wk_d = sts[3];

		obj.value = wk_y + wk_m + wk_d;
		obj.select();
	}

}

/**
 * ボタン重複押下制御関数
 */
var isMhDisabledOnSubmit = false;
function mhDisableOnSubmit(timeout){
    if(!timeout || isNaN(timeout) || timeout < 1) {
        timeout = 10000;
    }
	if(!isMhDisabledOnSubmit){
		isMhDisabledOnSubmit = true;
        setTimeout(function(){
        	isMhDisabledOnSubmit = false;
        },timeout);
		return true;
	}
	return false;
}

//ブラウザの戻るボタン、BackSpace禁止
//IEでのアンカー対応用変数
var bsctrlOrg ;

// onload時の実行関数
function location_setTimeOut()
{
        setTimeout('location_change()',1);
}

// location.hashの初期化関数
function location_change()
{
        // IEでのアンカー対応
        bsctrlOrg = location.hash;
        location.hash = bsctrlOrg;

        // location.hashの初期化
        location.hash = "#bsctrl";
}


// onhashchange時の処理の設定
window.onhashchange = function()
{
        if( location.hash != "#bsctrl" )
        {
                // IEでのアンカー対応
                if( bsctrlOrg != location.hash )
                {
                        bsctrlOrg = location.hash;
                }
                location.hash = bsctrlOrg  ;

                // location.hashの再初期化
                location.hash = "bsctrl"  ;
        }
};

// onload時の処理の追加
if( window.addEventListener )
{
        // IE9以降,FX,CH,SF用
        window.addEventListener( 'load', location_setTimeOut, false );
}
else if( window.attachEvent )
{
        // IE8以前用
        window.attachEvent( 'onload', location_setTimeOut );
}
else
{
        // その他
        window.onload = location_setTimeOut;
}