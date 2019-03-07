<%@page import="jp.co.forvaltel.common.constant.CommonConst"%>
<%@page import="jp.co.forvaltel.common.util.FtResourcesUtil" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1">
	
	<title><ft:label key="login.item.21" /></title>

	<link type="text/css" rel="stylesheet" href="../ui/libs/css/jquery-ui.css" />
	<link type="text/css" rel="stylesheet" href="../ui/libs/css/bootstrap.css" />
	<link type="text/css" rel="stylesheet" href="../ui/libs/css/bootstrap-theme.css" />
	<link type="text/css" rel="stylesheet" href="../ui/css/ft_common-0.0.0.css" />	
	<link type="text/css" rel="stylesheet" href="../ui/css/ft_user-0.0.0.css" />	
</head>

<body>
	<div class="ft-title">
		<h1><ft:label key="login.item.21"></ft:label></h1>
	</div>
	
	<div class="container">
		<div class="row padding-bottom20" class="col-xs-10">
			<div>
				<c:set var="logoUrl">/ui/img/<%=FtResourcesUtil.getItemValue("system.logo")%></c:set>
				<img src="${f:url(logoUrl)}" alt="logo" class="pull-left" />
				<h3 class="padding-top20"><ft:label key="login.item.11" /></h3>
				<hr/>
				<hr/>
			</div>
		</div>
		
		<s:form>
			<div class="row">
				<div class="col-xs-10 col-xs-offset-1 text-cnter">
					<div class="panel panel-default">
						<div class="panel-heading" style="background: transparent;">
							<strong><ft:label key="login.item.21" /></strong>
						</div>
					</div>
				</div>
			</div>
		
			<div class="row">
				<div class="col-xs-6 col-xs-offset-3">
				
					<table class="table table-bordered">
						<colgroup>
							<col class="wd-35">
							<col>
						</colgroup>
						<tbody>
							<tr>
								<td class="padding-left20" style="vertical-align: middle;">
									<strong><ft:label key="login.item.30" /></strong>
								</td>
								<td>
									<html:text property="loginId" maxlength="20" styleClass="form-control"></html:text>
								</td>
							<tr>
							<tr>
								<td class="padding-left20" style="vertical-align: middle;">
									<strong><ft:label key="login.item.33" /></strong>
								</td>
								<td>
									<input type="password" name="password" class="form-control" maxlength="30"  autocomplete="off"/>
								</td>
							<tr>
						</tbody>
					</table>
				</div>
			</div>
			
			<div class="text-center padding-bottom10">
				<html:errors/>
			</div>
			
			<div class="row padding-bottom20">
				<div class="col-xs-12 text-center">
					<ft:button value="login.button.50" styleId="login-button" styleClass="btn btn-default"></ft:button>
				</div>
			</div>
			<% String display = FtResourcesUtil.getItemValue("flag_display_link").toString().trim(); %>
			<c:if test="<%=display.equals(CommonConst.STR_TRUE)%>">
				<div class="row">
					<div class="col-xs-12 text-center">
						<s:link href='<%=FtResourcesUtil.getItemValue("login.url.61")%>' target="_blank"><ft:label key="login.item.61" /></s:link><br/>
						<s:link href='<%=FtResourcesUtil.getItemValue("login.url.62")%>' target="_blank"><ft:label key="login.item.62" /></s:link><br/>
						<s:link href='<%=FtResourcesUtil.getItemValue("login.url.63")%>' target="_blank"><ft:label key="login.item.63" /></s:link><br/>
						<s:link href='<%=FtResourcesUtil.getItemValue("login.url.64")%>' target="_blank"><ft:label key="login.item.64" /></s:link><br/>
						<s:link href='<%=FtResourcesUtil.getItemValue("login.url.65")%>' target="_blank"><ft:label key="login.item.65" /></s:link>
					</div>
				</div>			
			</c:if>
		</s:form>
	</div>
	
	<tiles:insert page="/WEB-INF/view/common/footer.jsp" />
		
	<script type="text/javascript" src="${f:url('/ui/js/login-0.0.0.js')}"></script>
	
</body>

</html>