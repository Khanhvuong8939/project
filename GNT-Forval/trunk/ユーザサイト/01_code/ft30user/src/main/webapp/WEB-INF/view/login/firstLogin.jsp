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
				<img src="${f:url('/ui/img/')}<%=FtResourcesUtil.getItemValue("system.logo")%>" alt="logo" class="pull-left" />
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
							<span class="text-success"><strong><ft:label key="login.first.item.21" /></strong></span>
						</div>
					</div>
				</div>
			</div>
			
			<div class="row">
				<div class="col-xs-8 col-xs-offset-2">
					<html:hidden property="version"/>
					<!-- TTM_DEV USER_SITE_FIRST_MODULE BEGIN DELETE -->
					<!-- <table class="table table-bordered margin-bottom0">
						<colgroup>
							<col class="wd-25">
							<col>
						</colgroup>
						<tbody>
							<tr>
								<th>
									<strong><ft:label key="login.first.item.31" /></strong>
								</th>
								<td>
									${f:h(firstLoginViewInfoDto.srvName)}
								</td>
							</tr>
							<c:if test='${firstLoginViewInfoDto.authAccountLabel != null}' >
								<tr>
									<th>
										<strong>${f:h(firstLoginViewInfoDto.authAccountLabel)}</strong>
									</th>
									<td>
										${f:h(firstLoginViewInfoDto.authAccountValue)}
									</td>
								</tr>
							</c:if>
							<c:if test='${firstLoginViewInfoDto.authPwLabel != null}' >
								<tr>
									<th>
										<strong>${f:h(firstLoginViewInfoDto.authPwLabel)}</strong>
									</th>
									<td>
										${f:h(firstLoginViewInfoDto.authPwValue)}
									</td>
								</tr>
							</c:if>
							<c:if test='${firstLoginViewInfoDto.initialClaimValueDateLabel != null}' >
								<tr>
									<th>
										<strong>${f:h(firstLoginViewInfoDto.initialClaimValueDateLabel)}</strong>
									</th>
									<td>
										${f:h(firstLoginViewInfoDto.initialClaimValueDateValue)}
									</td>
								</tr>
							</c:if>
							<c:if test='${firstLoginViewInfoDto.usageFeeLabel != null}' >
								<tr>
									<th>
										<strong>${f:h(firstLoginViewInfoDto.usageFeeLabel)}</strong>
									</th>
									<td>
										${f:h(firstLoginViewInfoDto.usageFeeValue)}
									</td>
								</tr>
							</c:if>
						</tbody>
					</table> -->
					<!-- TTM_DEV USER_SITE_FIRST_MODULE END DELETE -->
					<!-- TTM_DEV USER_SITE_FIRST_MODULE BEGIN ADD -->
					<c:forEach var="firstLoginDtoList" varStatus="status" items="${firstLoginViewInfoDto.firstLoginDtoList}">
						<div style="margin-bottom:10px">
							<table class="table table-bordered margin-bottom0">
								<colgroup>
									<col class="wd-25">
									<col>
								</colgroup>
								<tbody>
									<tr>
										<th>
											<strong><ft:label key="login.first.item.31" /></strong>
										</th>
										<td>
											${f:h(firstLoginDtoList.srvName)}
										</td>
									</tr>
									<c:if test='${firstLoginDtoList.authAccountLabel != null}' >
										<tr>
											<th>
												<strong>${f:h(firstLoginDtoList.authAccountLabel)}</strong>
											</th>
											<td>
												${f:h(firstLoginDtoList.authAccountValue)}
											</td>
										</tr>
									</c:if>
									<c:if test='${firstLoginDtoList.authPwLabel != null}' >
										<tr>
											<th>
												<strong>${f:h(firstLoginDtoList.authPwLabel)}</strong>
											</th>
											<td>
												${f:h(firstLoginDtoList.authPwValue)}
											</td>
										</tr>
									</c:if>
									<c:if test='${firstLoginDtoList.initialClaimValueDateLabel != null}' >
										<tr>
											<th>
												<strong>${f:h(firstLoginDtoList.initialClaimValueDateLabel)}</strong>
											</th>
											<td>
												${f:h(firstLoginDtoList.initialClaimValueDateValue)}
											</td>
										</tr>
									</c:if>
									<c:if test='${firstLoginDtoList.usageFeeLabel != null}' >
										<tr>
											<th>
												<strong>${f:h(firstLoginDtoList.usageFeeLabel)}</strong>
											</th>
											<td>
												${f:h(firstLoginDtoList.usageFeeValue)}
											</td>
										</tr>
									</c:if>
								</tbody>
							</table>
						</div>
					</c:forEach>
					<!-- TTM_DEV USER_SITE_FIRST_MODULE END ADD -->
					<table class="table table-bordered">
						<colgroup>
							<col class="wd-25">
							<col class="wd-25">
							<col class="wd-25">
							<col class="wd-25">
						</colgroup>
						<tbody>
							<tr>
								<th>
									<strong><ft:label key="login.first.item.41" /></strong>
								</th>
								<td>
									${f:h(loginForm.loginId)}
								</td>
								<th>
									<strong><ft:label key="login.first.item.43" /></strong>
								</th>
								<td>
									${f:h(loginForm.password)}
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			
			<div class="text-center padding-bottom10">
				<html:errors/>
			</div>
			<% String display = FtResourcesUtil.getItemValue("flag_display_link").toString().trim(); %>
			<c:if test="<%=display.equals(CommonConst.STR_TRUE)%>">
				<div class="row">
					<div class="col-xs-12 text-center">
						<c:if test='${FtResourcesUtil.getItemValue("login.first.item.60") != ""}' >
							<s:link href='<%=FtResourcesUtil.getItemValue("login.first.url.60")%>' target="_blank"><ft:label key="login.first.item.60" /></s:link><br/>
						</c:if>
						<c:if test='${FtResourcesUtil.getItemValue("login.first.item.61") != ""}' >
							<s:link href='<%=FtResourcesUtil.getItemValue("login.first.url.61")%>' target="_blank"><ft:label key="login.first.item.61" /></s:link><br/>
						</c:if>
						<c:if test='${FtResourcesUtil.getItemValue("login.first.item.62") != ""}' >
							<s:link href='<%=FtResourcesUtil.getItemValue("login.first.url.62")%>' target="_blank"><ft:label key="login.first.item.62" /></s:link><br/>
						</c:if>
						<c:if test='${FtResourcesUtil.getItemValue("login.first.item.63") != ""}' >
							<s:link href='<%=FtResourcesUtil.getItemValue("login.first.url.63")%>' target="_blank"><ft:label key="login.first.item.63" /></s:link><br/>
						</c:if>
						<c:if test='${FtResourcesUtil.getItemValue("login.first.item.64") != ""}' >
							<s:link href='<%=FtResourcesUtil.getItemValue("login.first.url.64")%>' target="_blank"><ft:label key="login.first.item.64" /></s:link>
						</c:if>
					</div>
				</div>		
			</c:if>
			<div class="row padding-bottom20">
				<div class="col-xs-12 text-center">
					<ft:button value="login.first.button.65" styleId="agree-button" styleClass="btn btn-default"></ft:button>
				</div>
			</div>

		</s:form>
	</div>
	
	<tiles:insert page="/WEB-INF/view/common/footer.jsp" />
		
	<script type="text/javascript" src="${f:url('/ui/js/login-0.0.0.js')}"></script>

</body>

</html>