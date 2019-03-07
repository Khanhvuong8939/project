<%@page import="jp.co.forvaltel.user.constant.UserConst"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="tiles" uri="http://jakarta.apache.org/struts/tags-tiles"%>
<%@ taglib prefix="ft" uri="/WEB-INF/tld/ft.tld"%>

<tiles:insert template="/WEB-INF/view/common/layout.jsp" flush="true">

	<!-- タイトル -->
	<tiles:put name="title"><ft:label key="system.title.9000" /></tiles:put>

	<!-- 表示部分実装 -->
	<tiles:put name="content" type="string">
		<div class="ft-chapter-title">
			<h2><ft:label key="payment_credit_register.label.12" /></h2>
		</div>
		<s:form>
			<div class="container">
				<!-- 流れ図 -->
				<div class="row padding-bottom10">
					<div class="col-xs-2 col-xs-offset-2 text-center">
						<div class="panel panel-default">
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
						<div class="panel panel-success">
							<div class="panel-heading">
								<ft:label key="payment_adding_upregiste.label.63" />
							</div>
						</div>
					</div>
				</div>
				<!-- 説明 -->
				<div class="row padding-bottom10">
					<div class="col-xs-8 col-xs-offset-2 text-center">
						<ft:label key="payment_credit_register.label.62" />
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
										<strong><ft:label key="payment_credit_register.label.77" /></strong><br/>
									</td>
									<td>
										<bean:write name="paymentCreditRegisterForm" property="cardLabel"/>
									</td>
								</tr>
								<tr>
									<td class="text-right">
										<strong><ft:label key="payment_credit_register.label.81" /></strong><br/>
									</td>
									<td>
									<bean:write name="paymentCreditRegisterForm" property="cardNo1"/>
									-
									****
									-
									****
									-
									<bean:write name="paymentCreditRegisterForm" property="cardNo4"/>
									</td>
								</tr>
								<tr>
									<td class="text-right">
										<strong><ft:label key="payment_credit_register.label.91" /></strong><br/>
									</td>
									<td>
									<bean:write name="paymentCreditRegisterForm" property="expire1"/>
									/
									<bean:write name="paymentCreditRegisterForm" property="expire2"/>
									</td>
								</tr>
								
								<!-- トークン方式対応 20170302 BEGIN ADD -->
								<tr>		
									<td class="text-right">	
										<strong><ft:label key="payment_credit_register.label.111" /></strong><br/>
									</td>	
									<td>***</td>	
								</tr>		
								<!-- トークン方式対応 20170302 END ADD -->
								
								<tr>
									<td class="text-right">
										<strong><ft:label key="payment_credit_register.label.101" /></strong><br/>
									</td>
									<td>
									<bean:write name="paymentCreditRegisterForm" property="holderName"/>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</div>
			<!-- ボタン -->
			<div class="row padding-bottom10">
				<div class="col-xs-12 text-center">
					<ft:button value="payment_credit_register.button.183" styleId="ft-btn-finish" styleClass="btn btn-default btn-w200"></ft:button>
				</div>
			</div>

		</s:form>
	</tiles:put>

	<!-- javascript実装 -->
	<tiles:put name="myscript" type="string">
		<script type="text/javascript" src="${f:url('/ui/js/paymentCreditRegister-0.0.0.js')}"></script>
	</tiles:put>


</tiles:insert>
