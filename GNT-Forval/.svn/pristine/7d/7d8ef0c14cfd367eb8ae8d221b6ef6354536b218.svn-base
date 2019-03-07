<%@page import="jp.co.forvaltel.common.constant.CommonConst"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="tiles" uri="http://jakarta.apache.org/struts/tags-tiles"%>
<%@ taglib prefix="ft" uri="/WEB-INF/tld/ft.tld"%>
<%@page import="jp.co.forvaltel.common.util.FtResourcesUtil" %>

<tiles:insert template="/WEB-INF/view/common/layout.jsp" flush="true">
	<!-- タイトル設定 -->
	<tiles:put name="title"><ft:label key="system.title.9000" /></tiles:put>
	
	<%
		String bgContract = "";
		String bgOrder = "";
		String bgOption = "";
	%>
	<% String display = FtResourcesUtil.getItemValue("flag_display_button").toString().trim(); %>
	<!-- 表示部分実装 -->
	<tiles:put name="content" type="string">
		<div class="container-fluid">
			<s:form>

				<%-- 契約情報 --%>
				<div class="ft-chapter-title">
					<h2><ft:label key="contract_info_query.label.20" /></h2>
				</div>
				
				<div class="container-fluid">  <!--kyaku info -->
					<%-- 右ボタンエリア --%>
					<div style="width: 15%; float:right;" >
						<%-- サービス利用規約 --%>
						<c:if test="<%=display.equals(CommonConst.STR_TRUE) %>" >
						<button type="button" id="caution" onclick="window.open('${contractInfoQueryForm.termsOfServiceURL}')" class="btn btn-default btn-width135">
							<%=FtResourcesUtil.getItemValue("contract_info_query.button.170")%>
						</button>
						</c:if>
						<p style="height: 10px;"></p>
						<%-- 解約について --%>
						
			<%--TTM-DEV 20180130 BEGIN EDIT --%>
						<!-- <button type="button" id="btnAboutCancellationURL" onclick="window.open('${contractInfoQueryForm.aboutCancellationURL}')" class="btn btn-default btn-width135"> -->
						<button type="button" id="btnAboutCancellationURL" class="btn btn-default btn-width135">
						<%=FtResourcesUtil.getItemValue("contract_info_query.button.180")%>
						</button>
		    <%--TTM-DEV 20180130 END EDIT --%>	
					</div>
					<div style="width: 83%; margin-right: 1%; margin-right: auto;" > <!--  -->
					<!-- TTM_DEV BEGIN ADD change bgColor Contract -->
								<c:choose>
									<c:when test="${userSessionDto.kaiyakuKubun == '1'}">
										<%
											bgContract = "background-color: #4682B4";
										%>
									</c:when>
									<c:when test="${userSessionDto.kaiyakuKubun == '9'}">
										<%
											bgContract = "background-color: #696969";
										%>
									</c:when>
									<c:otherwise>
										<%
											bgContract = "background-color: #F5F5F5";
										%>
									</c:otherwise>
								</c:choose>
					<!-- TTM_DEV END ADD change bgColor -->
						<table class="table table-bordered contract-table"  style="<%=bgContract%>">
							<colgroup>
								<col style="width: 300px; background-color:rgb(105, 105, 105);">
								<col>
							</colgroup>

							<tbody>
								<%-- お客様名 --%>
								<tr>
									<th>
										<label><ft:label key="contract_info_query.label.30" /></label>
									</th>
									<td>
										<small>${f:h(contractInfoQueryForm.kkykMei)}</small>
									</td>
								</tr>

								<%-- お客様番号 --%>
								<tr>
									<th>
										<label><ft:label key="contract_info_query.label.50" /></label>
									</th>
									<td>
										<small>${f:h(contractInfoQueryForm.kkykBng)}</small>
									</td>
								</tr>

								<%-- 支払方法 --%>
								<tr>
									<th>
										<label><ft:label key="contract_info_query.label.70" /></label>
									</th>
									<td>
										<small style="margin-right: 20px;">${f:h(contractInfoQueryForm.haraiKbnSetName)}</small>
										<c:if test="${!(contractInfoQueryForm.serviceTermination || contractInfoQueryForm.consolidateBill) }" >
<!-- 										サービス解約後または合算請求手続き中 判定 START -->
											<%-- 合算エラー時に表示する。 --%>
											<c:if test="${fn:length(contractInfoQueryForm.haraiKbnSetError) > 0 }" >
												<small style="margin-right: 20px;">
													<font color=#ff0000>${f:h(contractInfoQueryForm.haraiKbnSetError)}</font>
												</small>
											</c:if>
<!-- 											TTM_DEV 20160630 UPDATE BEGIN -->
											<c:if test="${fn:length(contractInfoQueryForm.haraiKbnSetMethod) > 0 }" ><%-- NTT東西対応 START --%>
<!-- 											TTM_DEV 20160630 UPDATE END	 -->
												<button type="button" id="paymentMode" value="${f:h(contractInfoQueryForm.haraiKbnSetMethod)}" class="btn btn-default btn-small">
													${f:h(contractInfoQueryForm.haraiKbnSetName)}
												</button>
											</c:if><%-- NTT東西対応 END --%>
										</c:if>
<!-- 										サービス解約後または合算請求手続き中 判定 END -->
									</td>
								</tr>

								<%-- 支払方法変更 --%>
									<tr>
										<th>
											<label><ft:label key="contract_info_query.label.100" /></label>
										</th>
										<td>
											<c:if test="${!(contractInfoQueryForm.serviceTermination || contractInfoQueryForm.consolidateBill) }" >
<!-- 											サービス解約後または合算請求手続き中 判定 START -->
												<c:if test="${fn:length(contractInfoQueryForm.haraiKbnChengeMethod[0]) > 0 }" ><%-- NTT東西対応 START --%>
													<button type="button" id="paymentModeChenge1" value="${f:h(contractInfoQueryForm.haraiKbnChengeMethod[0])}" class="btn btn-default btn-small">
														${f:h(contractInfoQueryForm.haraiKbnChengeName[0])}
													</button>
												</c:if>
												<%-- NTT東西対応 END --%>
												<c:if test="${fn:length(contractInfoQueryForm.haraiKbnChengeMethod[1]) > 0 }" ><%-- NTT東西対応 START --%>
													<button type="button" id="paymentModeChenge2" value="${f:h(contractInfoQueryForm.haraiKbnChengeMethod[1])}" class="btn btn-default btn-small">
														${f:h(contractInfoQueryForm.haraiKbnChengeName[1])}
													</button>
												</c:if>
<!-- 												NTT東西対応 END -->
												<c:if test="${fn:length(contractInfoQueryForm.haraiKbnChengeMethod[2]) > 0 }" ><%-- NTT東西対応 START --%>
													<button type="button" id="paymentModeChenge3" value="${f:h(contractInfoQueryForm.haraiKbnChengeMethod[2])}" class="btn btn-default btn-small">
														${f:h(contractInfoQueryForm.haraiKbnChengeName[2])}
													</button>
												</c:if>
												<c:if test="${fn:length(contractInfoQueryForm.haraiKbnChengeMethod[3]) > 0 }" ><%-- NTT東西対応 START --%>
													<button type="button" id="paymentModeChenge4" value="${f:h(contractInfoQueryForm.haraiKbnChengeMethod[3])}" class="btn btn-default btn-small">
														${f:h(contractInfoQueryForm.haraiKbnChengeName[3])}
													</button>
												</c:if>
											</c:if>
<!-- 											サービス解約後または合算請求手続き中 判定 END -->
										</td>
									</tr>

								<%-- 請求情報 --%>
								<tr>
									<th>
										<label><ft:label key="contract_info_query.label.130" /></label>
									</th>
									<td>
										<c:if test="${ebillSsoParameterDto != null}" ><%-- Ebill情報がない場合非表示にする（プログラム上のエラー回避） START --%>
											<small>
												<a id="eBillSsoLink" onclick="onClickEbillSso"><ft:label key="contract_info_query.label.140"/></a>
												<ft:label key="contract_info_query.label.141"/>
											</small>
										</c:if><%-- Ebill情報がない場合非表示にする（プログラム上のエラー回避） END --%>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>

				<c:forEach var="juchKihonInfoDto" varStatus="status" items="${contractInfoQueryForm.juchKihonInfoDtoList}"><%-- 拡張項目ループ START --%>
					
					<c:if test="${!juchKihonInfoDto.serviceTermination }" ><%-- サービス解約後 判定 START --%>
						
						<%-- 商品情報 --%>
						<div class="ft-chapter-title">
							<h2><ft:label key="contract_info_query.label.200" /></h2>
						</div>
						<!-- TTM_DEV BEGIN ADD change bgColor Order-->
						<c:choose>
							<c:when test="${juchKihonInfoDto.kaiyakuKubun == '1'}">
								<%
									bgOrder = "applycancel";
								%>
							</c:when>
							<c:when test="${juchKihonInfoDto.kaiyakuKubun == '9'}">
								<%
									bgOrder = "canceled";
								%>
							</c:when>
							<c:otherwise>
								<%
									bgOrder = "notcancel";
								%>
							</c:otherwise>
						</c:choose>
						<!-- TTM_DEV END ADD change bgColor -->
						<div class="container-fluid order_div <%=bgOrder%>">
							<%-- 新規追加 --%>
							<c:if test="${!juchKihonInfoDto.serviceTerminationApplied}" ><%-- サービス解約中 判定 START --%>
<%-- 							<ft:button styleId="addOption"  value="contract_info_query.button.210" styleClass="btn btn-default add-option-button"></ft:button> --%>
<!-- 							TTM_DEV ORDER_MODULE 20170530 BEGIN UPADATE -->
<!-- 							TTM_DEV 20180410 BEGIN UPDATE -->
								
								<c:if test="<%=display.equals(CommonConst.STR_TRUE) %>" >
									<ft:button styleId="addOption${status.index}"  value="contract_info_query.button.210" styleClass="btn btn-default add-option-button"></ft:button>
								</c:if>
<!-- 							TTM_DEV 20180410 END UPDATE -->
<!-- 							TTM_DEV ORDER_MODULE 20170530 END UPADATE -->
							</c:if><%-- サービス解約中 判定 END --%>
							<br/>
							<div style="margin: 5px 5px 20px 0px;">
								<ft:label key="contract_info_query.label.220"></ft:label>
							</div>
	
							<%-- hidden オプション追加画面用パラメータ --%>
							<input type="hidden" id="pJuchNo" name="pJuchNo" value="${f:h(juchKihonInfoDto.pJuchNo)}">
							<input type="hidden" id="pJuchMeisaiNo" name="pJuchMeisaiNo" value="${f:h(juchKihonInfoDto.pJuchMeisaiNoList[0])}">
	
							<%-- 拡張項目 --%>
							<span id="ft-kakuchou-juchno" class="ui-helper-hidden">${f:h(juchKihonInfoDto.juchNo)}</span>
							<span id="ft-kakuchou-hyoujikbn" class="ui-helper-hidden">${f:h(juchKihonInfoDto.hyoujiKbn)}</span>
							
							<c:forEach var="itemInfoDto" varStatus="status" items="${juchKihonInfoDto.itemInfoDtoList}"><%-- 拡張項目ループ START --%>
								<!-- TTM_DEV BEGIN ADD change bgColor Option-->
								<c:choose>
									<c:when test="${itemInfoDto.kaiyakuKubun == '1'}">
										<%
											bgOption = "background-color: #4682B4";
										%>
									</c:when>
									<c:when test="${itemInfoDto.kaiyakuKubun == '9'}">
										<%
											bgOption = "background-color: #696969";
										%>
									</c:when>
									<c:otherwise>
										<%
											bgOption = "background-color: #F5F5F5";
										%>
									</c:otherwise>
								</c:choose>
								<!-- TTM_DEV END ADD change bgColor -->
								<c:if test="${!itemInfoDto.opTermination}" ><%-- オプション解約後 判定 START --%>
									<table class="table table-bordered contract-table" style="<%=bgOption%>">
										<colgroup>
											<col style="width: 300px; background-color:rgb(105, 105, 105);">
											<col>
										</colgroup>
										<tbody>
											<tr>
												<th>
													<label><ft:label key="contract_info_query.label.230" /></label>
												</th>
												<td>
													${f:h(itemInfoDto.shohinName)}
	
													<%-- deleteOption キャンセルボタン --%>
													<c:if test="${!itemInfoDto.opTerminationApplied}" ><%-- オプション解約申請中 判定 START --%>
														<c:if test="${(fn:length(itemInfoDto.kikanSousinDate) == 0 && itemInfoDto.optionFlag)}" ><%-- オプション申請中 基幹連携前 判定 START --%>
															<ft:button styleId="deleteOption" value="contract_info_query.label.280" styleClass="btn btn-default btn-small"></ft:button>
															<div class="juchInfo">
																<input type="hidden" name="kakuJuchNo" value="${f:h(juchKihonInfoDto.juchNo)}" />
																<input type="hidden" name="kakuJuchMeisaiNo" value="${f:h(itemInfoDto.juchMeisaiNo)}" />
															</div>
														</c:if><%-- オプション申請中 基幹連携前 判定 END --%>
													</c:if><%-- オプション解約申請中 判定 END --%>
												</td>
											</tr>
	
											<%-- 申し込み手続き中 --%>
											<c:if test="${!itemInfoDto.serviceTerminationApplied}" ><%-- サービス解約申請中 判定 START --%>
												<c:if test="${!itemInfoDto.opTerminationApplied}" ><%-- オプション解約申請中 判定 START --%>
													<%-- (基幹送信日isNull && 子明細) || (基幹受信日isNull) --%>
													<c:if test="${((fn:length(itemInfoDto.kikanSousinDate) == 0) && itemInfoDto.optionFlag) || (fn:length(itemInfoDto.kikanJusinDate) == 0) }" >
														<tr>
															<td colspan="2" style="background-color: rgb(255,255,255);">
																<label><ft:label key="contract_info_query.label.290" /></label>
															</td>
														</tr>
													</c:if>
												</c:if><%-- オプション解約申請中 判定 END --%>
											</c:if><%-- サービス解約申請中 判定 END --%>
											<%-- 解約希望日 --%>
											<c:if test="${fn:length(itemInfoDto.kaiyakuDate) > 0}" ><%-- 解約希望日 判定 START --%>
												<c:if test="${!((fn:length(itemInfoDto.kikanSousinDate) == 0) || (fn:length(itemInfoDto.kikanJusinDate) == 0))}" ><%-- 基幹連携 判定 START --%>
													<tr>
														<th>
															<label><ft:label key="contract_info_query.label.160" /></label>
														</th>
														<td>
															<small style="margin-right: 20px;">${f:h(itemInfoDto.kaiyakuDate)}</small>
														</td>
													</tr>
												</c:if><%-- 基幹連携 判定 END --%>
											</c:if><%-- 解約希望日 判定 END --%>
											<c:if test="${!(fn:length(itemInfoDto.kikanJusinDate) == 0)}"><%-- 基幹連携 判定 --%>
												<%-- 拡張項目 --%>
												<tr class="ui-helper-hidden">
													<th>
													</th>
													<td class="ft-kakuchou-shohin">
														<span class="ui-helper-hidden ft-kakuchou-juchmeisaino">${f:h(itemInfoDto.juchMeisaiNo)}</span>
														<span class="ui-helper-hidden ft-kakuchou-shohincd">${f:h(itemInfoDto.shohinCd)}</span>
													</td>
												</tr>
											</c:if><%-- 基幹連携 判定 END --%>
										</tbody>
									</table>
								</c:if>
							</c:forEach>	
						</div>
						<c:if test="${juchKihonInfoDto.hpDispFlag == '0'}"> <!-- Bengin if HpDispFlag equa 0 -->
							<%-- ホームページ情報 --%>
							<div class="ft-chapter-title">
								<h2><ft:label key="contract_info_query.label.420" /></h2>
							</div>
							<div class="container-fluid">
								<c:if test="${juchKihonInfoDto.hpInfoDto == null || juchKihonInfoDto.hpInfoDto.url == null}" ><%-- 1件登録されていたら登録ボタンを削除する対応がある場合有効にする。 下の判定は消す --%>
									<%-- 新規追加 --%>
									<ft:button styleId="addHp" value="contract_info_query.button.430" styleClass="btn btn-default"></ft:button>
									<br/>
									<div style="margin: 5px 5px 20px 0px;">
										<%-- 登録されていません。 --%>
										<strong><ft:label key="contract_info_query.label.440"></ft:label></strong>
									</div>
								</c:if>
								<%-- ホームページ情報登録後 --%>
								<c:if test="${juchKihonInfoDto.hpInfoDto != null && juchKihonInfoDto.hpInfoDto.url != null}" ><%-- ホームページ情報登録後 判定 START --%>
									<div class="row-fluid">
										<div style="width: 83%; margin-right: 1%; float:left;">
											<table class="table table-bordered contract-table">
												<colgroup>
													<col style="width: 300px; background-color:rgb(105, 105, 105);">
													<col>
												</colgroup>
												<tbody>
		
													<%-- URL --%>
													<tr>
														<th>
															<label><ft:label key="contract_info_query.label.450" /></label>
														</th>
														<td>
															<small>${f:h(juchKihonInfoDto.hpInfoDto.url)}</small>
														</td>
													</tr>
		
													<%-- FTPサーバー --%>
													<tr>
														<th>
															<label><ft:label key="contract_info_query.label.470" /></label>
														</th>
														<td>
															<small>${f:h(juchKihonInfoDto.hpInfoDto.ftpServerName)}</small>
														</td>
													</tr>
		
													<%-- WEBアカウント --%>
													<tr>
														<th>
															<label><ft:label key="contract_info_query.label.490" /></label>
														</th>
														<td>
															<small>${f:h(juchKihonInfoDto.hpInfoDto.webAccount)}</small>
														</td>
													</tr>
		
													<%-- FTPアカウント --%>
													<tr>
														<th>
															<label><ft:label key="contract_info_query.label.510" /></label>
														</th>
														<td>
															<small>${f:h(juchKihonInfoDto.hpInfoDto.ftpConnectId)}</small>
														</td>
													</tr>
		
													<%-- パスワード --%>
													<tr>
														<th>
															<label><ft:label key="contract_info_query.label.530" /></label>
														</th>
														<td>
															<small style="margin-right: 20px;">${f:h(juchKihonInfoDto.hpInfoDto.webPasswd)}</small>
																<ft:button styleId="hpPassWdChenge" value="contract_info_query.button.550" styleClass="btn btn-default btn-small"></ft:button>
																<div class="hpInfo">
																	<input type="hidden" name="selectWebAccount" value="${f:h(juchKihonInfoDto.hpInfoDto.webAccount)}" />
<!-- 																	TTM_DEV 20170606 BEGIN ADD -->
																	<input type="hidden" name="selectInternetConnectionId" value="${f:h(juchKihonInfoDto.internetConnectionId)}" />
																	<input type="hidden" name = "selectaccountClass" value="${f:h(juchKihonInfoDto.accountClass)}">
<!-- 																	TTM_DEV 20170606 BEGIN ADD -->
																</div>
														</td>
													</tr>
<!-- 													TTM_DEV ORDER_MODULE 20170524 BEGIN ADD -->
<!-- 													<tr> -->
<!-- 														<th> -->
<%-- 															<label><ft:label key="contract_info_query.label.531" /></label> --%>
<!-- 														</th> -->
<!-- 														<td> -->
<%-- 															<small>${f:h(juchKihonInfoDto.internetConnectionId)}</small> --%>
<!-- 														</td> -->
<!-- 													</tr> -->
<!-- 													TTM_DEV ORDER_MODULE 20170524 END ADD -->													
												</tbody>
											</table>
										</div>
		
										<%-- 右ボタンエリア --%>
										<div style="width: 15%; margin-left: auto;">
											<%-- 削除 --%>
											<p style="height: 130px;"></p>
												<ft:button styleId="hpDelete" value="contract_info_query.button.560" styleClass="btn btn-default btn-small"></ft:button>
										</div>
									</div>
								</c:if><%-- ホームページ情報登録後 判定 END --%>
								<input type="hidden" name="webAc" value="${f:h(juchKihonInfoDto.hpInfoDto.webAccount)}" />
<!-- 								TTM_DEV 20170606 BEGIN ADD -->
								<input type="hidden"  name="radId" id="radId" value="${f:h(juchKihonInfoDto.internetConnectionId)}">
								<input type="hidden" name = "accountClass" id="accountClass" value="${f:h(juchKihonInfoDto.accountClass)}">
<!-- 								TTM_DEV 20170606 END ADD -->
							</div>
						</c:if> <!--  End if HpDispFlag equa 0 -->
						<c:if test="${juchKihonInfoDto.mailDispFlag == '0'}"> <!-- Bengin if MailDispFlag equa 0 -->
						<%-- メール情報 --%>
							<div class="ft-chapter-title">
								<h2><ft:label key="contract_info_query.label.570" /></h2>
							</div>
							<div class="container-fluid">
								
								<c:if test="${fn:length(juchKihonInfoDto.mailInfoDtoList) == 0}" ><%-- メール 未登録時 START --%>
									<%-- 新規追加 --%>
									<ft:button styleId="addMail" value="contract_info_query.button.580" styleClass="btn btn-default"></ft:button>
									<br/>
									<div style="margin: 5px 5px 20px 0px;">
										<ft:label key="contract_info_query.label.590"></ft:label><br/>
										<strong><ft:label key="contract_info_query.label.600"></ft:label></strong>
									</div>
								</c:if><%-- メール 未登録時 END --%>
								
								<c:if test="${fn:length(juchKihonInfoDto.mailInfoDtoList) > 0}" ><%-- メール 登録時 START --%>
									<%-- 新規追加 --%>
									<fmt:parseNumber var="numberData" value="${contractInfoQueryForm.mailCountNum}" integerOnly="true" />
									<c:if test="${fn:length(juchKihonInfoDto.mailInfoDtoList) < contractInfoQueryForm.mailCountNum}" ><%-- 10以上,非表示にする場合は有効にする--%>
										<ft:button styleId="addMail" value="contract_info_query.button.580" styleClass="btn btn-default"></ft:button>
									</c:if>
									<br/>
									<div style="margin: 5px 5px 20px 0px;">
										<ft:label key="contract_info_query.label.590"></ft:label>
									</div>
									
									<c:forEach var="mailInfoDto" varStatus="status" items="${juchKihonInfoDto.mailInfoDtoList}"><%-- メール情報ループ START --%>
										<%-- メール情報登録後 --%>
										<table class="table table-bordered contract-table">
											<colgroup>
												<col style="width: 300px; background-color:rgb(105, 105, 105);">
												<col>
												<col style="width: 150px; background-color:rgb(105, 105, 105);">
												<col>
											</colgroup>
											<tbody>
												<%-- メールアドレス --%>
												<tr>
													<th>
														<label><ft:label key="contract_info_query.label.610" /></label>
													</th>
													<td colspan="2">
														<small>${f:h(mailInfoDto.mailAddr)}</small>
													</td>
													<td align="center">
														<ft:button styleId="mailDetailSetting" value="contract_info_query.button.630" styleClass="btn btn-default btn-small"></ft:button>
														<ft:button styleId="mailDelete" value="contract_info_query.button.640" styleClass="btn btn-default btn-small"></ft:button>
														<div class="mailInfo">
															<input type="hidden" name="selectMailAddr" value="${f:h(mailInfoDto.mailAddr)}" />
															<input type="hidden" name="selectMailAccount" value="${f:h(mailInfoDto.mailAccount)}" />
															
														</div>
													</td>
												</tr>
		
												<%-- パスワード --%>
												<tr>
													<th rowspan="2">
														<label><ft:label key="contract_info_query.label.650" /></label>
													</th>
													<td rowspan="2">
														<small>${f:h(mailInfoDto.mailPass)}</small><br/>
															<ft:button styleId="mailPassWdChenge" value="contract_info_query.button.670" styleClass="btn btn-default btn-small mail-passwd"></ft:button>
															<div class="mailInfo">
																<input type="hidden" name="selectMailAddr" value="${f:h(mailInfoDto.mailAddr)}" />
																<input type="hidden" name="selectMailAccount" value="${f:h(mailInfoDto.mailAccount)}" />
															</div>
													</td>
													<%-- POP3 --%>
													<th>
														<label><ft:label key="contract_info_query.label.680" /></label>
													</th>
													<td>
														<small>${f:h(mailInfoDto.popServerName)}</small>
													</td>
												</tr>
		
												<tr>
													<%-- SMTP --%>
													<th>
														<label><ft:label key="contract_info_query.label.700" /></label>
													</th>
													<td>
														<small>${f:h(mailInfoDto.smtpServerName)}</small>
													</td>
												</tr>
											</tbody>
										</table>
									</c:forEach><%-- メール情報ループ END --%>
								</c:if><%-- メール 登録時 END --%>
								<%-- 下部マージン --%>
								<p style="height: 50px;"></p>
								<input type="hidden" name="selectMailAddr" value="${f:h(mailInfoDto.mailAddr)}" />
								<input type="hidden" name="selectMailAccount" value="${f:h(mailInfoDto.mailAccount)}" />
<!-- 								TTM_DEV 20170606 BEGIN ADD -->
								<input type="hidden"  name="radId" id="radId" value="${f:h(juchKihonInfoDto.internetConnectionId)}">
								<input type="hidden" name = "accountClass" id="accountClass" value="${f:h(juchKihonInfoDto.accountClass)}">
<!-- 								TTM_DEV 20170606 END ADD -->
							</div>
						</c:if> <!--  End if MailDispFlag equa 0 -->
						
					</c:if>
				</c:forEach>
				<%-- パラメータ --%>
				<input type="hidden" id="webAccount" name="webAccount" value="" />
				<input type="hidden" id="mailAddress" name="mailAddress" value="" />
				<input type="hidden" id="mailAccount" name="mailAccount" value="" />
<!-- 				TTM_DEV 20170606 BEGIN ADD -->
				<input type="hidden" id="kJuchNo" name="kJuchNo" value="" />
				<input type="hidden" id="kJuchMeisaiNo" name="kJuchMeisaiNo" value="" />
				<input type="hidden" id="internetConnectionId" name="internetConnectionId" value="" />
				<input type="hidden" id="accountClassification" name="accountClassification" value="" />
<!-- 				TTM_DEV 20170606 END ADD -->
<!-- 			TTM_DEV-593 20171212 BEGIN ADD -->
				<input type="hidden" id="haraiKbnChange" name="haraiKbnChange" value="" />
<!-- 			TTM_DEV-593 20171212 END ADD -->
			</s:form>
		</div>
		
		<%-- ebill --%>
		<c:if test="${ebillSsoParameterDto != null}" >
		<form name="ebillSsoForm" action="<%=FtResourcesUtil.getItemValue("system.ebill.sso.url")%>" method="post" ENCTYPE="x-www-form-encoded" target="_blank">
				<input type="hidden" name="tenant_cd" value="${f:h(ebillSsoParameterDto.tenantCd)}" />
				<input type="hidden" name="login_id" value="${f:h(ebillSsoParameterDto.loginId)}" />
				<input type="hidden" name="change_screen" value="${f:h(ebillSsoParameterDto.changeScreen)}" />
				<input type="hidden" name="ft-kakuchou-screen" id="ft-kakuchou-screen" value="${contractInfoQueryForm.screen}" />
		</form>
		</c:if>
	</tiles:put>

	<tiles:insert page="/WEB-INF/view/common/kakuchou.jsp" />
	<!-- javascript実装 -->
	<tiles:put name="myscript" type="string">
		<script type="text/javascript" src="${f:url('/ui/js/kakuchou-0.0.0.js')}"></script>
		<script type="text/javascript" src="${f:url('/ui/js/contractInfoQuery-0.0.0.js')}"></script>
	</tiles:put>

</tiles:insert>