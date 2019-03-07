<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="tiles" uri="http://jakarta.apache.org/struts/tags-tiles"%>
<%@ taglib prefix="ft" uri="/WEB-INF/tld/ft.tld"%>

<tiles:insert template="/WEB-INF/view/common/layout.jsp" flush="true">

	<!-- タイトル設定 -->
	<tiles:put name="title"><ft:label key="system.title.9000" /></tiles:put>
	
	<!-- 表示部分実装 -->
	<tiles:put name="content" type="string">
		<div class="ft-chapter-title">
				<h2><ft:label key="kaiyaku_info.label.10" /></h2>
		</div>
		<s:form>
			<div class="container">
				<div class="row padding-bottom20 text-center">
					<div class="col-xs-8 col-xs-offset-2">
						<ft:label key="kaiyaku_info.label.50" />
					</div>
				</div>
				<div class="row text-center">
					<div class="col-xs-8 col-xs-offset-2">
						<ft:label key="kaiyaku_info.label.51" />
					</div>
				</div>
				<div class="row text-center">
					<div class="col-xs-8 col-xs-offset-2">
						<ft:label key="kaiyaku_info.label.52" />
					</div>
				</div>
				<div class="row text-center">
					<div class="col-xs-8 col-xs-offset-2">
						<ft:label key="kaiyaku_info.label.53" />
					</div>
				</div>
				<div class="row padding-bottom10">
					<div class="col-xs-12 text-center" style="margin-top:20px;">
					<%--<html:hidden property="paymentMethodFlg"></html:hidden>
						<c:if test="${kaiyakuInfoForm.paymentMethodFlg}" >	 --%>
						<%-- 支払方法確定後 判定 START --%>
						<span style="margin-right:6%">
							<ft:button value="kaiyaku_info.button.100" styleId="ft-btn-mainmenu" styleClass="btn btn-default btn-w200"></ft:button>
						</span>
						<%-- </c:if> --%>
						<%-- 支払方法確定後 判定 END --%>
					</div>
				</div>
			</div>
		</s:form>
	</tiles:put>
	<tiles:put name="myscript" type="string">
		<script type="text/javascript" src="${f:url('/ui/js/kaiyakuInfo-0.0.0.js')}"></script>
	</tiles:put>
</tiles:insert>