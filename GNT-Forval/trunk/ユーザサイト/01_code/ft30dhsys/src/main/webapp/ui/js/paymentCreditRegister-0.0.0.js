/**
 * 初期処理
 */
$(function() {
	// ---- 各種イベント設定 ----
	});
	// 確認ボタン
	$('#ft-btn-confirm').on('click', function() {

		// トークン方式対応 20170302 BEGIN UPDATE
		doPurchase();
		// トークン方式対応 20170302 END UPDATE	
		//	    TTM_DEV ORDER_MODULE 20170524 BEGIN DELETE
//		clickConfirm();
		//	    TTM_DEV ORDER_MODULE 20170524 BEGIN DELETE
	});
	// 登録ボタン
	$('#ft-btn-regist').on('click', function() {
		gotoAction('PaymentCreditRegister/register');
	});
	// 確認画面 戻るボタン
	$('#ft-btn-back').on('click', function() {
		// 登録画面に遷移
		gotoAction('paymentCreditRegister/index');
	});
	// 戻る(契約情報照会画面)ボタン
	$('#ft-btn-menu').on('click', function() {
		// 確認ダイアログを出力
		var result = confirm("画面を移動します。\n入力中の内容は破棄されます。よろしいですか？");
		if (result) {
			// 契約状況照会 初期表示へ遷移
			gotoAction('contractInfoQuery');
		}
	});
	// 完了ボタン
	$('#ft-btn-finish').on('click', function() {
		// 契約状況照会 初期表示へ
		gotoAction('contractInfoQuery');
	});
	// 銀行口座振替(引落し)ボタン
	$('#ft-btn-account').on('click', function() {
		// 支払先情報登録(口座振替) 初期表示へ遷移
		gotoAction('accountTransfer');
	});
	// 電話料金合算ボタン
	$('#ft-btn-adding').on('click', function() {
		// 電話料金合算 初期表示へ遷移
		gotoAction('paymentAddingUpRegister');
	});

	// 登録ボタンイベントバインド
	$('#ft-btn-register').on('click', function() {
		
		var form = $('[name=paymentCreditRegisterActionForm]');
		$(form).attr('action', $(form).attr('action') + "register");
		$(form).submit();
		
	});

/**
 * 確認ボタン onclick処理
 */
function clickConfirm() {
	var form = $('[name=paymentCreditRegisterActionForm]');

	// 確認フォーム用カード会社名
	var cardLabel = paymentCreditRegisterActionForm.cardName.options[paymentCreditRegisterActionForm.cardName.selectedIndex].text;
	paymentCreditRegisterActionForm.cardLabel.value = "" + cardLabel;

	// 登録用カード番号生成
	var card1 = paymentCreditRegisterActionForm.cardNo1.value;
	var card2 = paymentCreditRegisterActionForm.cardNo2.value;
	var card3 = paymentCreditRegisterActionForm.cardNo3.value;
	var card4 = paymentCreditRegisterActionForm.cardNo4.value;
	paymentCreditRegisterActionForm.cardNo.value = "" + card1 + card2 + card3 + card4;
	
	// 登録用有効期限生成
	var expire1 = paymentCreditRegisterActionForm.expire1.value;
	var expire2 = paymentCreditRegisterActionForm.expire2.value;
	paymentCreditRegisterActionForm.expire.value = "" + expire2 + expire1;

	// トークン方式対応 20170302 BEGIN UPDATE
	var securityCd = paymentCreditRegisterActionForm.securityCd.value;
	paymentCreditRegisterActionForm.securityCd.value = "" + securityCd;
	
	var tokenValue = paymentCreditRegisterActionForm.token.value;
	paymentCreditRegisterActionForm.token.value = "" + tokenValue;
	// トークン方式対応 20170302 END UPDATE	

	// 登録用名義人名生成
	var holderName = paymentCreditRegisterActionForm.holder.value;
	paymentCreditRegisterActionForm.holderName.value = holderName;

	$(form).attr('action', $(form).attr('action') + 'confirmationInit');
	$(form).submit();
}

// トークン方式対応 20170302 BEGIN ADD
/**
 * トークン取得後に、購入フォームを加盟店様に送信。 
 */
function execPurchase (response) { 
	if(response.resultCode != 000){	
	    //alert('購入処理中にエラーが発生しました')	;
	    $('[name=checktoken]').val("0");
	    
	}else{	
	    // 取得したトークンを隠し項目に設定	
	    $('[name=token]').val(response.tokenObject.token);
	    $('[name=checktoken]').val("1");
	}	
	//TTM_DEV ORDER_MODULE 20170524 BEGIN ADD
	    clickConfirm();
	 //TTM_DEV ORDER_MODULE 20170524 BEGIN ADD
}

/**
 * マルチペイメントオブジェクトを利用してトークン化。
 */
function doPurchase(){ 
	
	// 登録用カード番号生成
	var card1 = $('[name=cardNo1]').val();
	var card2 = $('[name=cardNo2]').val();
	var card3 = $('[name=cardNo3]').val();
	var card4 = $('[name=cardNo4]').val();
	var cardNo = "" + card1 + card2 + card3 + card4;
	
	// 登録用有効期限生成
	var expire1 = $('[name=expire1]').val();
	var expire2 = $('[name=expire2]').val();
	var expire = "" + expire2 + expire1;

	var securityCd = $('[name=securityCd]').val();

	// 登録用名義人名生成
	var holderName = $('[name=holder]').val();

	// トークンを利用するためのAPIキー	
//TTM-DEV 20180409 BEGIN EDIT
//			Multipayment.init('tshop00027385');	
	var shopId= $('#shopId').val();
	Multipayment.init(shopId);
//TTM-DEV 20180409 END EDIT
	Multipayment.getToken({	
		cardno      : cardNo,
		expire      : expire,
		securitycode: securityCd,
		holdername  : holderName
	},execPurchase);	
}
// トークン方式対応 20170302 END ADD
