/**
 * Copyright 2015 NTTDATA INTRAMART, INC. All Rights Reserved.
 */
package jp.co.forvaltel.user.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jp.co.forvaltel.common.dto.BasicWebApiResDto;
import jp.co.forvaltel.common.dto.FtpUserDto;
import jp.co.forvaltel.common.dto.MailAccountDto;
import jp.co.forvaltel.common.dto.MailUserDto;
import jp.co.forvaltel.common.dto.WebAccountDto;
import jp.co.forvaltel.common.exception.FtSystemException;

/**
 * WebApiCoreLogicのスタブ
 * 
 * @author smis
 *
 */
public class WebApiCoreLogicStub extends WebApiCoreLogic {

	/*
	 * (非 Javadoc)
	 * 
	 * @see jp.co.forvaltel.user.logic.WebApiCoreLogic#executeWebApi(java.lang.String, java.util.Map)
	 */
	@Override
	public BasicWebApiResDto executeWebApi(String actionPath, Map<String, Object> map) {
		// 戻り値初期化
		BasicWebApiResDto ret = new BasicWebApiResDto();

		// アクションのパスによって設定する値を変更
		if ("seiqMst/getSeiqMstList".equals(actionPath)) {
			// 請求先情報取得 WebAPIの戻り値設定処理
			ret = this.seiqMstGetSeiqMstList(map);

		} else if ("orderNo/getOrderNoList".equals(actionPath)) {
			// 受注番号取得WebAPI
			ret = this.orderNoGetOrderNoList(map);
		} else if ("mailAccountReference/getMailAccountInfo".equals(actionPath)) {
			// メールアカウント情報参照処理
			ret = this.getMailAccountInfo(map);
		} else if ("mailAccountRegist/regist".equals(actionPath)) {
			// メールアカウント情報登録
			ret = this.registMailAccount(map);
		} else if ("mailAddressDetailReference/getMailAddressDetailInfo".equals(actionPath)) {
			// メールアドレス詳細設定情報参照
			ret = this.getMailAddressDetailInfo(map);
		} else if ("mailAddressDetailRegist/regist".equals(actionPath)) {
			// メールアドレス詳細設定情報登録
			ret = this.registMailAddressDetail(map);
		} else if ("mailPwRegist/regist".equals(actionPath)) {
			// メールパスワード変更
			ret = this.registMailPw(map);
		} else if ("hpInfoReference/getHpInfo".equals(actionPath)) {
			// メールパスワード変更
			ret = this.getHpInfo(map);
		} else if ("hpInfoRegist/regist".equals(actionPath)) {
			// メールパスワード変更
			ret = this.registHpInfo(map);
		}

		// 戻り値設定
		return ret;
	}

	/**
	 * mailAddressDetailRegist/regist　スタブ処理
	 * 
	 * @param map パラメータ
	 * @return BasicWebApiResDto　WebAPIの戻り値
	 */
	private BasicWebApiResDto registHpInfo(Map<String, Object> map) {

		BasicWebApiResDto resDto = new BasicWebApiResDto();
		resDto.setResult(true);
		if (!resDto.isResult()) {
			throw new FtSystemException(new IllegalArgumentException());
		}
		// 戻り値設定
		return resDto;
	}

	/**
	 * mailAddressDetailRegist/regist　スタブ処理
	 * 
	 * @param map パラメータ
	 * @return BasicWebApiResDto　WebAPIの戻り値
	 */
	private BasicWebApiResDto getHpInfo(Map<String, Object> map) {
		// 戻り値用のオブジェクト生成
		BasicWebApiResDto resDto = new BasicWebApiResDto();
		WebAccountDto dto = new WebAccountDto();
		List<FtpUserDto> l = new ArrayList<>();
		FtpUserDto fd = new FtpUserDto();
		fd.setUrl("url");
		fd.setFtpUserName("FTPアカウント");
		fd.setWebAccount("webAccount");
		fd.setWebPass("webPass");
		l.add(fd);
		dto.setFtpServerName("FTPサーバー名");
		dto.setFtpUserDtoList(l);
		resDto.setReturnValue(dto);
		resDto.setResult(true);
		return resDto;
	}

	/**
	 * mailAddressDetailRegist/regist　スタブ処理
	 * 
	 * @param map パラメータ
	 * @return BasicWebApiResDto　WebAPIの戻り値
	 */
	private BasicWebApiResDto registMailAddressDetail(Map<String, Object> map) {

		BasicWebApiResDto resDto = new BasicWebApiResDto();
		resDto.setResult(true);
		if (!resDto.isResult()) {
			throw new FtSystemException(new IllegalArgumentException());
		}
		// 戻り値設定
		return resDto;
	}

	/**
	 * mailAddressDetailReference/getMailAddressDetailInfo　スタブ処理
	 * 
	 * @param map パラメータ
	 * @return BasicWebApiResDto　WebAPIの戻り値
	 */
	private BasicWebApiResDto getMailAddressDetailInfo(Map<String, Object> map) {

		// 戻り値用のオブジェクト生成
		BasicWebApiResDto resDto = new BasicWebApiResDto();
		// 戻り値のJSONを変換
		MailAccountDto dto = new MailAccountDto();

		// 伝送設定
		dto.setAutoTransfer("1");
		// メールを残す
		dto.setMessageDelete("1");
		// 転送先メールアドレス
		dto.setTransferToEmail("転送先メールアドレス@jdirect.jp");
		// 自動返信
		dto.setAutoReply("1");
		// 自動返信メールの件名
		dto.setAutoReplySubject("自動返信メールの件名");
		// 自動返信メールの本文
		dto.setAutoReplyBody("自動返信メールの本文");

		resDto.setResult(true);
		resDto.setReturnValue(dto);
		if (!resDto.isResult()) {
			throw new FtSystemException(new IllegalArgumentException());
		}
		// 戻り値設定
		return resDto;
	}

	/**
	 * mailPwRegist/regist　スタブ処理
	 * 
	 * @param map パラメータ
	 * @return BasicWebApiResDto　WebAPIの戻り値
	 */
	private BasicWebApiResDto registMailPw(Map<String, Object> map) {

		BasicWebApiResDto resDto = new BasicWebApiResDto();
		resDto.setResult(true);
		if (!resDto.isResult()) {
			throw new FtSystemException(new IllegalArgumentException());
		}
		// 戻り値設定
		return resDto;
	}

	/**
	 * mailAccountRegist/regist　スタブ処理
	 * 
	 * @param map パラメータ
	 * @return BasicWebApiResDto　WebAPIの戻り値
	 */
	private BasicWebApiResDto registMailAccount(Map<String, Object> map) {

		BasicWebApiResDto resDto = new BasicWebApiResDto();
		resDto.setResult(true);
		if (!resDto.isResult()) {
			throw new FtSystemException(new IllegalArgumentException());
		}
		// 戻り値設定
		return resDto;
	}

	/**
	 * mailAccountReference/getMailAccountInfo　スタブ処理
	 * 
	 * @param map パラメータ
	 * @return BasicWebApiResDto　WebAPIの戻り値
	 */
	private BasicWebApiResDto getMailAccountInfo(Map<String, Object> map) {

		MailAccountDto dto = new MailAccountDto();
		// メールユーザ情報リスト
		List<MailUserDto> mailUserList = new ArrayList<>();
		MailUserDto mailUser = new MailUserDto();
		mailUser.setMailAddr("test@mail.fit.call.ne.jp");
		mailUserList.add(mailUser);
		dto.setMailUserList(mailUserList);
		// POP3サーバ名
		dto.setPopServerName("pop3ServerName");
		// SMTPサーバ名
		dto.setSmtpServerName("smtpServerName");

		BasicWebApiResDto resDto = new BasicWebApiResDto();
		resDto.setResult(true);
		resDto.setReturnValue(dto);
		if (!resDto.isResult()) {
			throw new FtSystemException(new IllegalArgumentException());
		}
		// 戻り値設定
		return resDto;
	}

	/**
	 * seiqMst/getSeiqMstList　スタブ処理
	 * 
	 * @param map パラメータ
	 * @return BasicWebApiResDto　WebAPIの戻り値
	 */
	private BasicWebApiResDto seiqMstGetSeiqMstList(Map<String, Object> map) {
		// 戻り値初期化
		BasicWebApiResDto ret = new BasicWebApiResDto();

		// ここで引数を元に戻り値を設定
		if ("想定値".equals(map.get("パラメータ"))) {
			ret.setResult(true);
		}

		// 戻り値設定
		return ret;
	}

	/**
	 * orderNo/getOrderNoList　スタブ処理
	 * 
	 * @param map パラメータ
	 * @return BasicWebApiResDto　WebAPIの戻り値
	 */
	private BasicWebApiResDto orderNoGetOrderNoList(Map<String, Object> map) {
		// 戻り値初期化
		BasicWebApiResDto ret = new BasicWebApiResDto();

		// ここで引数を元に戻り値を設定
		if ("hoge".equals(map.get("piyo"))) {
			ret.setResult(true);
		}

		// 戻り値設定
		return ret;
	}

}
