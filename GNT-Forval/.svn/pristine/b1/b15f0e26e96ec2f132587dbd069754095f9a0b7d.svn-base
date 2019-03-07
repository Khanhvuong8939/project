<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="tiles" uri="http://jakarta.apache.org/struts/tags-tiles"%>
<%@ taglib prefix="ft" uri="/WEB-INF/tld/ft.tld"%>

<tiles:insert template="/WEB-INF/view/common/layout.jsp" flush="true">

	<!-- タイトル設定 -->
	<tiles:put name="title"><ft:label key="system.title.9000" /></tiles:put>
	
	<!-- 表示部分実装 -->
	<tiles:put name="content" type="string">
		<div class="ft-chapter-title"	>
<!-- 		TTM_DEV-593 20171212 BEGIN UPDATE -->
			<!-- <h2><ft:label key="account_transfer.label.10" /></h2> -->
			<c:if test="${accountTransferForm.haraiKbnChange == 2 || contractInfoQueryForm.haraiKbnChengeMethod[0] == 2}" >
				<h2><ft:label key="account_transfer.label.10" /></h2>
			</c:if>
			<c:if test="${accountTransferForm.haraiKbnChange == 6}" >
				<h2><ft:label key="account_transfer.label.20" /></h2>
			</c:if>
<!-- 		TTM_DEV-593 20171212 END UPDATE -->
		</div>
		<s:form>
			<div class="container">
				<div class="row padding-bottom20 text-center">
					<div class="col-xs-8 col-xs-offset-2">
<!-- 					TTM_DEV-593 20171212 BEGIN UPDATE -->
						<!-- <ft:label key="account_transfer.label.50" /> -->
						<c:if test="${accountTransferForm.haraiKbnChange == 2 || contractInfoQueryForm.haraiKbnChengeMethod[0] == 2}" >
							<ft:label key="account_transfer.label.50" />
						</c:if>
						<c:if test="${accountTransferForm.haraiKbnChange == 6}" >
							<ft:label key="account_transfer.label.60" />
						</c:if>
<!-- 					TTM_DEV-593 20171212 END UPDATE -->
					</div>
				</div>
				<div class="row text-center">
					<div class="col-xs-8 col-xs-offset-2">
						<ft:label key="account_transfer.label.51" />
					</div>
				</div>
				<div class="row text-center">
					<div class="col-xs-8 col-xs-offset-2">
						<ft:label key="account_transfer.label.52" />
					</div>
				</div>
				<%--TTM_DEV-884 20180502 BEGIN DELETE 
				<div class="row text-center">
					<div class="col-xs-8 col-xs-offset-2">
						<ft:label key="account_transfer.label.53" />
					</div>
				</div> 
					TTM_DEV-884 20180502 END DELETE --%>
				<div class="row padding-bottom10">
					<div class="col-xs-12 text-center" style="margin-top:20px;">
						<html:hidden property="paymentCreditFlg"></html:hidden>
						<c:if test="${accountTransferForm.paymentCreditFlg}" ><%-- クレジットカード 判定 START --%>
							<span style="margin-right:6%">
								<ft:button value="account_transfer.button.80" styleId="ft-btn-card" styleClass="btn btn-default btn-w200"></ft:button>
							</span>
						</c:if><%-- クレジットカード 判定 END --%>
						<%-- TTM_DEV-542 20170830 BEGIN UPDATE --%>
						<c:if test="${accountTransferForm.paymentAddingUpFlg}" >
							<span style="margin-right:6%">
								<ft:button value="account_transfer.button.90" styleId="ft-btn-tel" styleClass="btn btn-default btn-w200"></ft:button>
							</span>
						</c:if>
						<%-- TTM_DEV-542 20170830 BEGIN UPDATE --%>
						<html:hidden property="paymentMethodFlg"></html:hidden>
						<c:if test="${accountTransferForm.paymentMethodFlg}" ><%-- 支払方法確定後 判定 START --%>
						<span style="margin-right:6%">
							<ft:button value="account_transfer.button.100" styleId="ft-btn-mainmenu" styleClass="btn btn-default btn-w200"></ft:button>
						</span>
						</c:if><%-- 支払方法確定後 判定 END --%>
					</div>
				</div>
			</div>
		</s:form>
	</tiles:put>
	<tiles:put name="myscript" type="string">
		<script type="text/javascript" src="${f:url('/ui/js/accountTransfer-0.0.0.js')}"></script>
	</tiles:put>
</tiles:insert>