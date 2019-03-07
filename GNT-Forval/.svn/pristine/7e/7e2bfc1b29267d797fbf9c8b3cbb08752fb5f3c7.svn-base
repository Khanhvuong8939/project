$(function() {
	var LIST_TXT = {
			REQUEST_ERROR: '通信に失敗しました。',
			VALIDATE_CHECKBOX: '電気需給約款と個人情報方針の内容をご確認の上、チェックしてください。'
	};
	
	getScreenInfo();

	$('#agree-button').on('click', function() {
		agreeBtnEvent();
	});
	
	function getScreenInfo() {
		var data = {
				'token': $('input[name=token]').val(),
				'bpmId': $('input[name=bpm_id]').val()
		};
		$.ajax({
			url: getUrl('agree/ajScreenInfo'),
			data: data,
			type: 'post',
			success: function(result) {
				if (result.success) {
					var dt = JSON.parse(result.data);
					$('#moshikomi_no').val(dt.moshikomi_no);
					$('#kkyk_bng').text(dt.kkyk_bng);
					$('#kkyk_mei').text(dt.kkyk_mei);
					$('#kkyk_kana').text(dt.kkyk_kana);
					$('#yubin_bng').text(dt.yubin_bng);
					$('#todofken').text(dt.todofken);
					$('#sichoku').text(dt.sichoku);
					$('#chousonbanchi').text(dt.chousonbanchi);
					$('#biru_tatemono').text(dt.biru_tatemono);
					$('#tel').text(dt.tel);
					$('#email').text(dt.email);
					setAgreeKakuchou(dt.is_view_mode, dt.view_mode_msg, dt.consumer_flg);
					settingAgreeBtn(dt.consumer_flg);
				} else {
					if (result.is_redirect) {
						location.reload(true);
					} else {
						ajLoaderOff(false, result.msg);
					}
				}
			},
			error: function() {
				ajLoaderOff(false, LIST_TXT.REQUEST_ERROR);
			}
		});		
	}
	
	function agreeBtnEvent() {
		var checkBtns = $('.ft-kakuchou-check');
		
		//validate checkbox status
		if (checkBtns.length != 3 || !$(checkBtns[1]).is(':checked') || !$(checkBtns[2]).is(':checked')) {
			alert(LIST_TXT.VALIDATE_CHECKBOX);	
			return;
		} 
		
		ajLoaderOn(false);

		$.ajax({
			url: getUrl('agree/ajUpdateInfo'),
			data:  {
				'token': $('input[name=token]').val(),
				'bpmId': $('input[name=bpm_id]').val(),
				'moshikomiNo': $('input[name=moshikomi_no]').val()
			},
			type: 'post',
			success: function(result) {
				if (result.success) {
					var dt = JSON.parse(result.data);
					$('#agree-button').attr('disabled', true);
					$('.ft-kakuchou-check').each(function() {
						$(this).attr('disabled', true);
					});
					ajLoaderOff(true, dt.msg);					
				} else {
					if (result.is_redirect) {
						location.reload(true);
					} else {
						ajLoaderOff(false, result.msg);
					}
				}
			},
			error: function() {
				ajLoaderOff(false, LIST_TXT.REQUEST_ERROR);
			}
		});
	}
	
	function setAgreeKakuchou(isViewMode, viewMsg, consumerFlg)　{
		var target = $('.ft-apply-kakuchou');
		var data = {
				'token': $('input[name=token]').val(),
				'moshikomiNo': $('input[name=moshikomi_no]').val(),
				'mode': '1'	
		};
		
		$.ajax({
			type: 'POST',
			url: getKakuchoUrl('getkakuchouList'),
			data: data,
		}).success(function(result){
			if (result.success) {
				// 成功の場合、拡張項目設定
				setteingKakuchou(result.data, target);
				if (isViewMode) {
					$('#agree-button').attr('disabled', true);
					$('.ft-kakuchou-check').each(function() {
						$(this).prop('checked', true);
						$(this).attr('disabled', true);
					});
					$('.font-caution').remove();
				}
				if (consumerFlg == '1') {
					$('.ft-kakuchou-check').each(function() {
						$(this).hide();
						$(this).next('label').hide();
						$(this).closest('tr').find('br').hide();
					});
				}
				ajLoaderOff(true, viewMsg);
			} else {
				if (result.is_redirect) {
					location.reload(true);
				} else {
					ajLoaderOff(false, result.msg);
				}
			}
		}).error(function(data){
			// 通信失敗のメッセージ表示
			ajLoaderOff(false,LIST_TXT.REQUEST_ERROR);
		});	
	}
	
	function settingAgreeBtn (consumerFlg) {
		if (consumerFlg == '1') {
			$('#agree-btn-dev').hide();
			$('#agree-text-dev').hide();
		}
	}
	
	function getUrl(target) {
		return $('base').attr('href') + '/' + target;
	}

	function ajLoaderOff(isSuccess, msg) {
		if (isSuccess) {
			if (msg != undefined && msg != '')
				addToConfirmBox('info', msg)
				
			$('.denki-layer-loader').css('display', 'none');
			$('.denki-layer-blocker').css('display', 'none');			
		} else {
			if (msg != 'undefined' && msg != '')
				addToConfirmBox('error', msg)
			$('.denki-layer-blocker').css('display', 'none');
			$('.denki-layer-loader').css('display', 'none');		
		}
		$('html,body').animate({scrollTop:0},0);
	}

	function ajLoaderOn(isBlocked) {
		$('.denki-layer-loader').css('display', 'block');
		if (isBlocked)
			$('.denki-layer-blocker').css('display', 'block');
		else
			$('.denki-layer-blocker').css('display', 'none');
	}
	
	function setFormError(cd, msg) {
		$('.form-to-error').attr('action', location.pathname);
		$('input[name="webapiErrCd"]').val(cd);
		$('input[name="webapiMsg"]').val(msg);
		$('.form-to-error').submit();
	}
	
	function addToConfirmBox(type, msg) {
		if (type == 'error') {
			$('#ft-dialog-confirm').html(errorHtml(msg));
		}
		if (type == 'info') {
			$('#ft-dialog-confirm').html(infoHtml(msg));
		}
		if (type == 'warn') {
			$('#ft-dialog-confirm').html(warnHtml(msg));
		}
	}
	
	function infoHtml(msg) {
		var parentHtml = $('<div class="row padding-top15" style="margin: 0;"></div>');
		var childHtml = $('<div class="alert alert-info col-xs-8 col-xs-offset-2" role="alert"></div>');
		childHtml.text(msg);
		parentHtml.html(childHtml)
		return parentHtml;
	}
	
	function errorHtml(msg) {
		var parentHtml = $('<div class="row padding-top15" style="margin: 0;"></div>');
		var childHtml = $('<div class="alert alert-danger col-xs-8 col-xs-offset-2" role="alert"></div>');
		childHtml.text(msg);
		parentHtml.html(childHtml)
		return parentHtml;
	}
	
	function warnHtml(msg) {
		var parentHtml = $('<div class="row padding-top15" style="margin: 0;"></div>');
		var childHtml = $('<div class="alert alert-warning col-xs-8 col-xs-offset-2" role="alert"></div>');
		childHtml.text(msg);
		parentHtml.html(childHtml)
		return parentHtml;
	}
	
});
