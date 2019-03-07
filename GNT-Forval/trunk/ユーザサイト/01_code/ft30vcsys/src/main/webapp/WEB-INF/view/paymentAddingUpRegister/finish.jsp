<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="tiles" uri="http://jakarta.apache.org/struts/tags-tiles"%>
<%@ taglib prefix="ft" uri="/WEB-INF/tld/ft.tld"%>

<tiles:insert template="/WEB-INF/view/common/layout.jsp" flush="true">

	<!-- タイトル設定 -->
	<tiles:put name="title"><ft:label key="system.title.9000" /></tiles:put>	
	
	<!-- 表示部分実装 -->	
	<tiles:put name="content" type="string">
		<div class="ft-chapter-title">
			<h2><ft:label key="payment_adding_upregiste.label.52" /></h2>
		</div>
		<s:form>
			<div class="container">
				<div class="row padding-bottom10">
					<div class="col-xs-2 col-xs-offset-2 text-center">
						<div class="panel panel-default">
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
						<div class="panel panel-success">
							<div class="panel-heading">
								<ft:label key="payment_adding_upregiste.label.63" />
							</div>
						</div>
					</div>
				</div>

				<div class="row padding-bottom20">
					<div class="col-xs-8 col-xs-offset-2 text-center">
						<div>
							<ft:label key="payment_adding_upregiste.label.73" />
						</div>
					</div>
				</div>

				<div class="row padding-bottom10">
					<div class="col-xs-12 text-center">
						<ft:button value="payment_adding_upregiste.button.650" styleId="ft-btn-finish" styleClass="btn btn-default"></ft:button>
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