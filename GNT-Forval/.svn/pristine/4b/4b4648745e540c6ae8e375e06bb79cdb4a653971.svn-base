<%@page import="jp.co.forvaltel.common.util.FtResourcesUtil" %>
<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="tiles" uri="http://jakarta.apache.org/struts/tags-tiles"%>
<%@ taglib prefix="ft" uri="/WEB-INF/tld/ft.tld"%>

<tiles:insert template="/WEB-INF/view/common/layout.jsp" flush="true">
	<!-- タイトル -->
	<tiles:put name="title"><ft:label key="system.title.9000" /></tiles:put>
	<!-- 表示部分実装 -->
	<tiles:put name="content" type="string">
		<div class="ft-chapter-title">
			<h2><ft:label key="payment_credit_register.label.10" /></h2>
		</div>
		<s:form>
			<div class="container">
				<!-- 流れ図 -->
				<div class="row padding-bottom10">
					<div class="col-xs-2 col-xs-offset-2 text-center">
						<div class="panel panel-success">
							<div class="panel-heading">
								<ft:label key="payment_adding_upregiste.label.61" />
							</div>
						</div>
					</div>
					<div class="col-xs-1 text-center padding-top15">
						⇒⇒
					</div>
					<div class="col-xs-2 text-center">
						<div class="panel panel-default">
							<div class="panel-heading">
								<ft:label key="payment_adding_upregiste.label.62" />
							</div>
						</div>
					</div>
					<div class="col-xs-1 text-center padding-top15">
						⇒⇒
					</div>
					<div class="col-xs-2 text-center">
						<div class="panel panel-default">
							<div class="panel-heading">
								<ft:label key="payment_adding_upregiste.label.63" />
							</div>
						</div>
					</div>
				</div>
				<!-- 説明 -->
				<div class="row padding-bottom10">
					<div class="col-xs-8 col-xs-offset-2 text-center">
						<ft:label key="payment_credit_register.label.60" />
					</div>
				</div>
				<div class="row">
					<div class="col-xs-8 col-xs-offset-2 text-center">
						<div>
							<ft:label key="payment_credit_register.label.70" />
						</div>
					</div>
				</div>
				<!-- 入力項目 -->
				<div class="row padding-bottom10">
					<div class="col-xs-8 col-xs-offset-2">
						<table class="table table-bordered">
							<colgroup>
								<col class="wd-35">
								<col>
							</colgroup>
							<tbody>
								<tr>
									<td class="text-right">
										<span class="font-caution pull-left"><small><ft:label key="payment_credit_register.label.76" /></small></span>
										<strong><ft:label key="payment_credit_register.label.77" />:</strong><br/>
									</td>
									<td>
										<html:select  property="cardName">
											<html:options collection="cardNameList" property="value" labelProperty="label"/>
										</html:select>
										<html:hidden property="cardLabel"></html:hidden>
									</td>
								</tr>
								<tr>
									<td class="text-right">
										<span class="font-caution pull-left"><small><ft:label key="payment_credit_register.label.80" /></small></span>
										<strong><ft:label key="payment_credit_register.label.81" />:</strong><br/>
									</td>
									<td>
										<html:text property="cardNo1" maxlength="4" size="4"></html:text>
										-
										<html:text property="cardNo2" maxlength="4" size="4"></html:text>
										-
										<html:text property="cardNo3" maxlength="4" size="4"></html:text>
										-
										<html:text property="cardNo4" maxlength="4" size="4"></html:text>
										<html:hidden property="cardNo" ></html:hidden>
									</td>
								</tr>
								<tr>
									<td class="text-right">
										<span class="font-caution pull-left"><small><ft:label key="payment_credit_register.label.90" /></small></span>
										<strong><ft:label key="payment_credit_register.label.91" />:</strong><br/>
									</td>
									<td>
										<html:text property="expire1" maxlength="2" size="2"></html:text>
										/
										<html:text property="expire2" maxlength="2" size="2"></html:text>
										<html:hidden property="expire"></html:hidden>
										<ft:label key="payment_credit_register.label.92" />

									</td>
								</tr>
								
								<!-- トークン方式対応 20170302 BEGIN ADD -->
								<tr>
									<td class="text-right">
										<span class="font-caution pull-left"><small><ft:label key="payment_credit_register.label.110" /></small></span>
										<strong><ft:label key="payment_credit_register.label.111" />:</strong><br/>
									</td>
									<td>
										<html:text property="securityCd" maxlength="4" size="4"></html:text>
										<html:hidden property="securityCd"></html:hidden>
									</td>
								</tr>
								<!-- トークン方式対応 20170302 END ADD -->
								
								<tr>
									<td class="text-right">
										<span class="font-caution pull-left"><small><ft:label key="payment_credit_register.label.100" /></small></span>
										<strong><ft:label key="payment_credit_register.label.101" />:</strong><br/>
										<span class="font-caution"><small><ft:label key="payment_credit_register.label.103" /></small></span>
									</td>
									<td>
										<html:text property="holder" maxlength="30" size="60"></html:text><br/>
										<html:hidden property="holderName"></html:hidden><br/>										
									</td>
								</tr>
							</tbody>
						</table>

						<!-- トークン方式対応 20170302 BEGIN ADD -->
						<html:hidden property="token"></html:hidden>
						<!-- トークン方式対応 20170302 END ADD -->
						<!-- TTM_DEV 20170621 ADD BEGIN -->
						<html:hidden property="checkToken"></html:hidden>
						
						<!-- TTM_DEV 20170621 ADD END-->
					</div>
				</div>
			</div>
			<!-- ボタン -->
			<div class="row padding-bottom10">
				<div class="col-xs-12 text-center">
					<html:hidden property="paymentMethodFlg"></html:hidden>
<!-- 						TTM_DEV 20170630 UPDATE BEGIN -->
					<html:hidden property="disPlayFlgBtnBack"></html:hidden>
					<c:if test="${paymentCreditRegisterForm.paymentMethodFlg || paymentCreditRegisterForm.disPlayFlgBtnBack}" ><%-- 支払方法確定後 判定 START --%>
<!-- 						TTM_DEV 20170630 UPDATE END -->	
					<ft:button value="payment_credit_register.button.181" styleId="ft-btn-menu" styleClass="btn btn-default btn-w100"></ft:button>
					</c:if><%-- 支払方法確定後 判定 END --%>
					<ft:button value="payment_credit_register.button.180" styleId="ft-btn-confirm" styleClass="btn btn-default btn-w100"></ft:button>
				</div>
			</div>
			<!-- 利用可能カード -->
			<div class="ft-chapter-title">
				<h2><ft:label key="payment_credit_register.label.130" /></h2>
			</div>
			<!-- 表示方法 -->
			<div style="margin-top: -13px; text-align: center;">
				<c:set var="cardUrl">/ui/img/<%=FtResourcesUtil.getItemValue("payment_credit_register.label.140")%></c:set>
				<c:set var="cardOther">/ui/img/<%=FtResourcesUtil.getItemValue("payment_credit_register.label.141")%></c:set>
				<c:set var="cardAmerican">/ui/img/<%=FtResourcesUtil.getItemValue("payment_credit_register.label.142")%></c:set>
				<img src="${f:url(cardUrl)}" alt="利用可能カード一覧" width="617" height="133">
				<table align="center" style="margin:0 auto">
					<colgroup>
						<col class="wd-35" >
						<col>
					</colgroup>
					<tbody>
						<tr>
							<td><ft:label key="payment_credit_register.label.143" /></td>
							<td width="80px"></td>
							<td><ft:label key="payment_credit_register.label.144" /></td>
						</tr>
						<tr>
							<td><img src="${f:url(cardOther)}" alt="利用可能カード一覧" width="200" height="80"></td>
							<td width="80px"></td>
							<td><img src="${f:url(cardOther)}" alt="利用可能カード一覧" width="200" height="80"></td>
						</tr>
					</tbody>
				</table>
			</div>
			<br/>
			<!-- 支払い方法変更 -->
			<div class="ft-chapter-title">
				<h2><ft:label key="payment_credit_register.label.150" /></h2>
			</div>
			<div class="container">
				<div class="row padding-bottom10">
					<div class="col-xs-12 text-center">
						<ft:label key="payment_credit_register.label.160" />
					</div>
				</div>
				<div class="row padding-bottom10">
					<div class="col-xs-12 text-center">
						<%-- TTM_DEV-542 20170830 BEGIN UPDATE --%>
						<c:if test="${paymentCreditRegisterForm.paymentTransferFlg}" >
							<ft:button value="payment_credit_register.label.190" styleId="ft-btn-account" styleClass="btn btn-default"></ft:button>
						</c:if>
						<c:if test="${paymentCreditRegisterForm.paymentAddingUpFlg}" >
							<ft:button value="payment_credit_register.label.191" styleId="ft-btn-adding" styleClass="btn btn-default"></ft:button>
						</c:if>
						<%-- TTM_DEV-542 20170830 END UPDATE --%>
					</div>
				</div>
			</div>
			<br/>
		</s:form>
	</tiles:put>

	<!-- javascript実装 -->
	<tiles:put name="myscript" type="string">
	<!-- トークン方式対応 20170302 BEGIN ADD -->
		<!-- TTM_DEV 20180403 BEGIN EDIT -->
<!-- 	<script type="text/javascript" src="https://pt01.mul-pay.jp/ext/js/token.js"></script> -->
		<script type="text/javascript" src="<%=FtResourcesUtil.getItemValue("payment_credit_register.url")%>"></script>
		<input type="hidden" id="shopId" name="shopId" value="<%=FtResourcesUtil.getItemValue("payment_credit_register.shopid")%>" />
		<!-- TTM_DEV 20180403 END EDIT -->
	<!-- トークン方式対応 20170302 END ADD -->
		<script type="text/javascript" src="${f:url('/ui/js/paymentCreditRegister-0.0.0.js')}"></script>
	</tiles:put>

</tiles:insert>
