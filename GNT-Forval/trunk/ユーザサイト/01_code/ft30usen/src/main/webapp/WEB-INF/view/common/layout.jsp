<%@taglib prefix="f" uri="http://sastruts.seasar.org/functions" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1">
	<%
	String url = request.getRequestURL().toString();
	String contextPath = request.getContextPath();
	String base = url.split(contextPath)[0] + contextPath + "/";
	%>
	<base href="<%=base%>"/>
	<title><tiles:getAsString name="title" /></title>


	<link type="text/css" rel="stylesheet" href="${f:url('/ui/libs/css/jquery-ui.css')}" />
	<link type="text/css" rel="stylesheet" href="${f:url('/ui/libs/css/bootstrap.css')}" />
	<link type="text/css" rel="stylesheet" href="${f:url('/ui/libs/css/bootstrap-theme.css')}" />
	<link type="text/css" rel="stylesheet" href="${f:url('/ui/css/ft_common-0.0.0.css')}" />
	<link type="text/css" rel="stylesheet" href="${f:url('/ui/css/ft_user-0.0.0.css')}" />
</head>


<body>
	<span id="ft-contextpath"><%=request.getContextPath()%></span>	
	<tiles:insert page="/WEB-INF/view/common/header.jsp" />

	<tiles:insert attribute="content" />

	<tiles:insert page="/WEB-INF/view/common/footer.jsp" />

	<tiles:insert attribute="myscript" />
	
</body>

</html>