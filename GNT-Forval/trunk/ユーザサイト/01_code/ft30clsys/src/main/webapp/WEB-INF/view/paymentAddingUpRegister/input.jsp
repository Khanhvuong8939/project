<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="tiles" uri="http://jakarta.apache.org/struts/tags-tiles"%>
<%@ taglib prefix="ft" uri="/WEB-INF/tld/ft.tld"%>

<tiles:insert template="/WEB-INF/view/common/layout.jsp" flush="true">

	<!-- タイトル設定 -->
	<tiles:put name="title"><ft:label key="system.title.9000" /></tiles:put>
	
	<!-- 表示部分実装 -->
	<tiles:put name="content" type="string">
		<div class="ft-chapter-title">
			<h2><ft:label key="payment_adding_upregiste.label.50" /></h2>
		</div>
		<s:form>
			<div class="container">
				<div class="row padding-bottom10">
					<div class="col-xs-2 col-xs-offset-2 text-center">
						<div class="panel panel-success">
							<div class="panel-heading">
								<ft:label key="payment_adding_upregiste.label.61" />
							</div>
						</div>
					</div>
					<div class="col-xs-1 text-center padding-top15">
						<ft:label key="payment_adding_upregiste.label.60" />
					</div>
					<div class="col-xs-2 text-center">
						<div class="panel panel-default">
							<div class="panel-heading">
								<ft:label key="payment_adding_upregiste.label.62" />
							</div>
						</div>
					</div>
					<div class="col-xs-1 text-center padding-top15">
						<ft:label key="payment_adding_upregiste.label.60" />
					</div>
					<div class="col-xs-2 text-center">
						<div class="panel panel-default">
							<div class="panel-heading">
								<ft:label key="payment_adding_upregiste.label.63" />
							</div>
						</div>
					</div>
				</div>
				<div class="row padding-bottom10">
					<div class="col-xs-8 col-xs-offset-2">
						<ft:label key="payment_adding_upregiste.label.70" />
					</div>
				</div>
				<div class="row padding-bottom20">
					<div class="col-xs-8 col-xs-offset-2">
						<ft:label key="payment_adding_upregiste.label.71" />
					</div>
				</div>
				<div class="row">
					<div class="col-xs-8 col-xs-offset-2 text-center">
						<div>
							<span class="font-caution"><small>*</small></span>
							<ft:label key="payment_adding_upregiste.label.90" />
						</div>
					</div>
				</div>
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
										<span class="font-caution pull-left"><small>*</small></span>
										<strong><ft:label key="payment_adding_upregiste.label.100" />:</strong><br/>
										<span class="font-caution"><small><ft:label key="payment_adding_upregiste.label.120" /></small><br/></span>
										<span class="font-caution"><small><ft:label key="payment_adding_upregiste.label.130" /></small></span>
									</td>
									<td>
										<html:text property="agencyCd" maxlength="11" size="24"></html:text>
										<div id="ft-link-reference-url" class="ui-helper-hidden"><ft:label key="payment_adding_upregiste.label.151" /></div>
										<s:link href="javascript:void(0);" styleId="ft-link-reference" styleClass="padding-left20"><ft:label key="payment_adding_upregiste.label.150" /></s:link><br/>
										<span class="font-caution"><small><ft:label key="payment_adding_upregiste.label.140" /></small></span>
									</td>
								<tr>
								<tr>
									<td class="text-right">
										<span class="font-caution pull-left"><small>*</small></span>
										<strong><ft:label key="payment_adding_upregiste.label.170" />:</strong><br/>
										<span class="font-caution"><small><ft:label key="payment_adding_upregiste.label.190" /></small><br/></span>
										<span class="font-caution"><small><ft:label key="payment_adding_upregiste.label.200" /></small></span>
									</td>
									<td>
										<html:text property="holderName" maxlength="40" size="60"></html:text><br/>
										<span class="font-caution"><small><ft:label key="payment_adding_upregiste.label.210" /></small></span>
									</td>
								<tr>
								<tr>
									<td class="text-right">
										<span class="font-caution pull-left"><small>*</small></span>
										<strong><ft:label key="payment_adding_upregiste.label.220" />:</strong><br/>
										<span class="font-caution"><small><ft:label key="payment_adding_upregiste.label.240" /></small><br/></span>
										<span class="font-caution"><small><ft:label key="payment_adding_upregiste.label.250" /></small></span>
									</td>
									<td>
										<html:text property="holderNameKana" maxlength="80" size="60"></html:text><br/>
										<span class="font-caution"><small><ft:label key="payment_adding_upregiste.label.260" /></small></span>
									</td>
								<tr>
							</tbody>
						</table>
					</div>
				</div>

				<div class="row padding-bottom10">
					<div class="col-xs-7 col-xs-offset-5">
						<table>
							<tbody>
								<tr>
									<td rowspan="2"><html:checkbox styleId="ft-done" styleClass="margin-right-3" property="doneFlg"></html:checkbox></td>
									<td><label for="ft-done"><ft:label key="payment_adding_upregiste.label.600" /></label></td>
								</tr>
								<tr>
									<td>
										<ft:label key="payment_adding_upregiste.label.601" />
										<div id="ft-link-terms-url" class="ui-helper-hidden"><ft:label key="payment_adding_upregiste.label.603" /></div>
										<s:link href="javascript:void(0);" styleId="ft-link-terms"><ft:label key="payment_adding_upregiste.label.602" /></s:link>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>

				<div class="row padding-bottom10">
					<div class="col-xs-12 text-center">
						<html:hidden property="paymentMethodFlg"></html:hidden>
<!-- 						TTM_DEV 20170630 UPDATE BEGIN -->
						<html:hidden property="disPlayFlgBtnBack"></html:hidden>
						<c:if test="${paymentAddingUpRegisterForm.paymentMethodFlg || paymentAddingUpRegisterForm.disPlayFlgBtnBack}" ><%-- 支払方法確定後 判定 START --%>
<!-- 						TTM_DEV 20170630 UPDATE END -->
						<ft:button value="payment_adding_upregiste.button.620" styleId="ft-btn-back-input" styleClass="btn btn-default btn-w100"></ft:button>
						</c:if><%-- 支払方法確定後 判定 END --%>
						<ft:button value="payment_adding_upregiste.button.630" styleId="ft-btn-confirm" styleClass="btn btn-default btn-w100" disabled="true"></ft:button>
					</div>
				</div>
			</div>

			<div class="ft-chapter-title">
				<h2><ft:label key="payment_adding_upregiste.label.700" /></h2>
			</div>
			<div class="container">
				<div class="row padding-bottom10">
					<div class="col-xs-12 text-center">
						<ft:label key="payment_adding_upregiste.label.710" />
					</div>
				</div>
				<div class="row padding-bottom10">
					<div class="col-xs-12 text-center">
						<html:hidden property="paymentCreditFlg"></html:hidden>
						<c:if test="${paymentAddingUpRegisterForm.paymentCreditFlg}" ><%-- クレジットカード 判定 START --%>
							<ft:button value="payment_adding_upregiste.button.720" styleId="ft-btn-credit" styleClass="btn btn-default"></ft:button>
						</c:if><%-- クレジットカード 判定 END --%>
						<%-- TTM_DEV-542 20170830 BEGIN UPDATE --%>
						<c:if test="${paymentAddingUpRegisterForm.paymentTransferFlg}" >
							<ft:button value="payment_adding_upregiste.button.730" styleId="ft-btn-account" styleClass="btn btn-default"></ft:button>
						</c:if>
						<%-- TTM_DEV-542 20170830 END UPDATE --%>
					</div>
				</div>
			</div>

		</s:form>
	</tiles:put>

	<!-- javascript実装 -->
	<tiles:put name="myscript" type="string">
		<script type="text/javascript" src="${f:url('/ui/js/paymentAddingUpRegister-0.0.0.js')}"></script>
	</tiles:put>
</tiles:insert>