<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="tiles" uri="http://jakarta.apache.org/struts/tags-tiles"%>
<%@ taglib prefix="ft" uri="/WEB-INF/tld/ft.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="jp.co.forvaltel.common.util.FtResourcesUtil" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1">
	<link type="text/css" rel="stylesheet" href="../ui/libs/css/jquery-ui.css" />
	<link type="text/css" rel="stylesheet" href="../ui/libs/css/bootstrap.css" />
	<link type="text/css" rel="stylesheet" href="../ui/libs/css/bootstrap-theme.css" />
	<link type="text/css" rel="stylesheet" href="../ui/css/ft_common-0.0.0.css" />	
	<link type="text/css" rel="stylesheet" href="../ui/css/ft_user-0.0.0.css" />	
</head>

<body>
	<div class="ft-title">
		<h1><ft:label key="system.title.9000"></ft:label></h1>
	</div>
	
	<div class="container-fluid">
		<div class="row">
			<div class=".col-md-12">
				<img src="${f:url('/ui/img/')}<%=FtResourcesUtil.getItemValue("system.logo")%>" alt="logo" class="img-responsive pull-left margin-left20" style="width:auto;max-height:40px;"/>
				<button type="button" class="btn btn-default navbar-btn pull-right margin-right20"><ft:label key="system.button.9050" /></button>
			</div>
		</div>
	</div>

	<html:errors/>
	<ft:infos />

	<div id="ft-dialog-confirm" title="">
	</div>

	<!-- 表示部分実装 -->	
	<div class="container-fluid">
		<s:form>

			<%-- ホームページ情報削除完了 --%>
			<c:if test="${contractInfoQueryForm.completeOfHPFlag}" >
				<div class="ft-chapter-title">
					<h2><ft:label key="contract_info_query.label.3000" /></h2>
				</div>
				<%-- メッセージ --%>
				<p style="text-align: center; margin-bottom: 30px;">
					<ft:label key="contract_info_query.label.3100" />
				</p>
				<%-- メインメニューへ --%>
				<div class="row padding-bottom10">
					<div class="col-xs-12 text-center">
						<ft:button value="contract_info_query.button.3200" styleId="ft-btn-finish" styleClass="btn btn-default"></ft:button>
					</div>
				</div>
			</c:if>

			<%-- メール情報削除完了 --%>
			<c:if test="${!contractInfoQueryForm.completeOfHPFlag}" >
				<div class="ft-chapter-title">
					<h2><ft:label key="contract_info_query.label.4000" /></h2>
				</div>
				<%-- メッセージ --%>
				<p style="text-align: center; margin-bottom: 30px;">
					<ft:label key="contract_info_query.label.4100" />
				</p>
				<%-- メインメニューへ --%>
				<div class="row padding-bottom10">
					<div class="col-xs-12 text-center">
						<ft:button value="contract_info_query.button.4200" styleId="ft-btn-finish" styleClass="btn btn-default"></ft:button>
					</div>
				</div>
			</c:if>

		</s:form>
	</div>

	<tiles:insert page="/WEB-INF/view/common/footer.jsp" />

	<script type="text/javascript" src="${f:url('/ui/js/contractInfoQuery-0.0.0.js')}"></script>

</body>

</html>