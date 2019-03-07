<%--
   - Copyright Notice   ： NTTDATA INTRAMART , INC. All Rights Reserved.
   - ファイル名                             ：homepageSettingRegister.jsp
   - 作者                                    ：Y.Wang
   - バージョン                              ：1.00
   - 作成日                                 ：2016/06/08
   - 履歴                                     ：2016/06/08 新規作成
   - 説明                                     ：ホームページ登録・パスワード変更機能
--%>

<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="tiles"
	uri="http://jakarta.apache.org/struts/tags-tiles"%>
<%@taglib prefix="ft" uri="/WEB-INF/tld/ft.tld"%>

<tiles:insert template="/WEB-INF/view/common/layout.jsp" flush="true">

	<!-- タイトル設定 -->
	<tiles:put name="title"><ft:label key="system.title.9000" /></tiles:put>
	
	<!-- 表示部分実装 -->
	<tiles:put name="content" type="string">
		<div class="ft-chapter-title">
			<c:if test="${homepageSettingRegisterForm.judgeFlg == '1'}">
				<h2>
					<ft:label key="homepage_setting_register.label.10" />
				</h2>
			</c:if>
			<c:if test="${homepageSettingRegisterForm.judgeFlg == '2'}">
				<h2>
					<ft:label key="homepage_setting_register.label.11" />
				</h2>
			</c:if>
			<c:if test="${homepageSettingRegisterForm.judgeFlg == '3'}">
				<h2>
					<ft:label key="homepage_setting_register.label.12" />
				</h2>
			</c:if>
			<c:if test="${homepageSettingRegisterForm.judgeFlg == '4'}">
				<h2>
					<ft:label key="homepage_setting_register.label.13" />
				</h2>
			</c:if>
		</div>
		<s:form>
			<div class="container">
				<div class="row">
					<div class="col-xs-12 text-center">
						<c:if
							test="${homepageSettingRegisterForm.judgeFlg == '1' or homepageSettingRegisterForm.judgeFlg == '3'}">
							<label class="imui-required"><font color="#ff0000">*</font></label>
							<ft:label key="homepage_setting_register.label.21" />
						</c:if>
						<c:if
							test="${homepageSettingRegisterForm.judgeFlg == '2' or homepageSettingRegisterForm.judgeFlg == '4'}">
							<ft:label key="homepage_setting_register.label.22" />
						</c:if>
					</div>
				</div>
				<c:if
					test="${homepageSettingRegisterForm.judgeFlg == '1' or homepageSettingRegisterForm.judgeFlg == '3'}">
					<div class="row col-xs-12" style="margin-top: 7px;">
				</c:if>
				<c:if
					test="${homepageSettingRegisterForm.judgeFlg == '2' or homepageSettingRegisterForm.judgeFlg == '4'}">
					<div class="row col-xs-12" style="margin-top: 20px;">
				</c:if>
				<div class="col-xs-7" style="left: 20%">
					<table class="table table-bordered">
						<colgroup>
							<col class="wd-40" style="background-color: #F0F0F0">
							<col>
						</colgroup>
						<tbody>
<!-- 						TTM_DEV ORDER_MODULE 20170524 BEGIN ADD -->
<!-- 							<tr> -->
<%-- 								<td class="text-right"><strong><ft:label --%>
<%-- 											key="homepage_setting_register.label.82" />：</strong><br /></td> --%>
<!-- 								<td> -->
<%-- 									${homepageSettingRegisterForm.internetConnectionId}</td> --%>
<!-- 							</tr> -->
<!-- 							<tr> -->
<%-- 								<td class="text-right"><strong><ft:label --%>
<%-- 											key="homepage_setting_register.label.82" />：</strong><br /></td> --%>
<!-- 								<td> -->
<%-- 									${homepageSettingRegisterForm.accountClassification}</td> --%>
<!-- 							</tr> -->
<!-- 						TTM_DEV ORDER_MODULE 20170524 BEGIN ADD -->
							<tr>
								<td class="text-right"><strong><ft:label
											key="homepage_setting_register.label.31" />：</strong><br /></td>
								<td>${f:h(homepageSettingRegisterForm.url)}</td>
							</tr>
							<tr>
								<td class="text-right"><strong><ft:label
											key="homepage_setting_register.label.41" />：</strong><br /></td>
								<td>${f:h(homepageSettingRegisterForm.ftpServer)}</td>
							</tr>
							<tr>
								<td class="text-right"><c:if
										test="${homepageSettingRegisterForm.judgeFlg == '1'}">
										<strong><label class="imui-required"><font
												color="#ff0000">*</font></label>
										<ft:label key="homepage_setting_register.label.52" />：</strong>
										<br />
										<strong><font color="#ff0000"><ft:label
													key="homepage_setting_register.label.53" /></font></strong>
									</c:if> <c:if
										test="${homepageSettingRegisterForm.judgeFlg == '2' or homepageSettingRegisterForm.judgeFlg == '3' or homepageSettingRegisterForm.judgeFlg == '4'}">
										<strong><ft:label
												key="homepage_setting_register.label.52" />：</strong>
									</c:if></td>
								<td style="vertical-align: middle;"><c:if
										test="${homepageSettingRegisterForm.judgeFlg == '1'}">
										<html:text styleId="textWebAccount"
											name="homepageSettingRegisterForm" property="webAccount"
											size="25"></html:text>
									</c:if> <c:if
										test="${homepageSettingRegisterForm.judgeFlg == '2' or homepageSettingRegisterForm.judgeFlg == '3' or homepageSettingRegisterForm.judgeFlg == '4'}">
                                            ${f:h(homepageSettingRegisterForm.webAccount)}
                                        </c:if></td>
							</tr>
							<tr>
								<td class="text-right"><strong><ft:label
											key="homepage_setting_register.label.61" />：</strong><br /></td>
								<td>${f:h(homepageSettingRegisterForm.ftpAccount)}</td>
							</tr>
							<tr>
								<td class="text-right"><c:if
										test="${homepageSettingRegisterForm.judgeFlg == '1' or homepageSettingRegisterForm.judgeFlg == '3'}">
										<strong><label class="imui-required"><font
												color="#ff0000">*</font></label>
										<ft:label key="homepage_setting_register.label.71" />：</strong>
										<br />
										<strong><font color="#ff0000"><ft:label
													key="homepage_setting_register.label.72" /></font></strong>
									</c:if> <c:if
										test="${homepageSettingRegisterForm.judgeFlg == '2' or homepageSettingRegisterForm.judgeFlg == '4'}">
										<strong><ft:label
												key="homepage_setting_register.label.71" />：</strong>
										<br />
									</c:if></td>
								<td><c:if
										test="${homepageSettingRegisterForm.judgeFlg == '1' or homepageSettingRegisterForm.judgeFlg == '3'}">
										<html:text name="homepageSettingRegisterForm"
											property="homePagePassword"></html:text>
										<br />
										<font color="#ff0000"><ft:label
												key="homepage_setting_register.label.74" /></font>
									</c:if> <c:if
										test="${homepageSettingRegisterForm.judgeFlg == '2' or homepageSettingRegisterForm.judgeFlg == '4'}">
                                            ${f:h(homepageSettingRegisterForm.homePagePassword)}
                                        </c:if></td>
							</tr>
							<c:if
								test="${homepageSettingRegisterForm.judgeFlg == '1' or homepageSettingRegisterForm.judgeFlg == '3'}">
								<tr>
									<td class="text-right"><strong><label
											class="imui-required"><font color="#ff0000">*</font></label>
										<ft:label key="homepage_setting_register.label.81" />：</strong><br />
									</td>
									<td><html:text name="homepageSettingRegisterForm"
											property="homePagePasswordConfirm"></html:text></td>
								</tr>
							</c:if>

						</tbody>
					</table>
				</div>
			</div>

			<div class="row padding-bottom10">
				<div class="col-xs-12 text-center">
					<c:if
						test="${homepageSettingRegisterForm.judgeFlg == '1' or homepageSettingRegisterForm.judgeFlg == '3'}">
						<span style="margin-right: 2%"> <ft:button
								styleId="ft-btn-menu"
								value="homepage_setting_register.button.111"
								styleClass="btn btn-default btn-w150"></ft:button>
						</span>
					</c:if>
					<c:if
						test="${homepageSettingRegisterForm.judgeFlg == '2' or homepageSettingRegisterForm.judgeFlg == '4'}">
						<ft:button styleId="ft-btn-menu"
							value="homepage_setting_register.button.114"
							styleClass="btn btn-default btn-w150"></ft:button>
					</c:if>
					<c:if test="${homepageSettingRegisterForm.judgeFlg == '1'}">
						<span style="margin-right: 2%"> <ft:button
								styleId="ft-btn-regist"
								value="homepage_setting_register.button.112"
								styleClass="btn btn-default btn-w150"></ft:button>
						</span>
					</c:if>
					<c:if test="${homepageSettingRegisterForm.judgeFlg == '3'}">
						<span style="margin-right: 2%"> <ft:button
								styleId="ft-btn-change"
								value="homepage_setting_register.button.113"
								styleClass="btn btn-default btn-w150"></ft:button>
						</span>
					</c:if>
				</div>
			</div>
			</div>
			<html:hidden name="homepageSettingRegisterForm" property="judgeFlg"
				value="${f:h(homepageSettingRegisterForm.judgeFlg)}" />
			<html:hidden name="homepageSettingRegisterForm" property="url"
				value="${f:h(homepageSettingRegisterForm.url)}" />
			<html:hidden name="homepageSettingRegisterForm" property="ftpServer"
				value="${f:h(homepageSettingRegisterForm.ftpServer)}" />
			<html:hidden name="homepageSettingRegisterForm" property="ftpAccount"
				value="${f:h(homepageSettingRegisterForm.ftpAccount)}" />
			<html:hidden name="homepageSettingRegisterForm" property="webAccount"
				value="${f:h(homepageSettingRegisterForm.webAccount)}" />
<!-- 				TTM_DEV 20170606 BEGIN END -->
			<html:hidden name="homepageSettingRegisterForm"
				property="internetConnectionID"
				value="${f:h(homepageSettingRegisterForm.internetConnectionID)}" />
			<html:hidden name="homepageSettingRegisterForm"
				property="accountClassification"
				value="${f:h(homepageSettingRegisterForm.accountClassification)}" />	
<!-- 				TTM_DEV 20170606 BEGIN END -->
		</s:form>
	</tiles:put>

	<!-- javascript実装 -->
	<tiles:put name="myscript" type="string">
		<script type="text/javascript"
			src="${f:url('/ui/js/homepageSettingRegister-0.0.0.js')}"></script>
	</tiles:put>
</tiles:insert>