<%@page import="jp.co.forvaltel.common.constant.CommonConst"%>
<%@page import="jp.co.forvaltel.common.util.FtResourcesUtil" %>
<div class="ft-title">
	<h1><ft:label key="system.title.9000"></ft:label></h1>
</div>

<div class="container-fluid">
	<div class="row">
		<div class="col-md-4">
			<img src="${f:url('/ui/img/')}<%=FtResourcesUtil.getItemValue("system.logo")%>" alt="logo" class="img-responsive pull-left margin-left20"
				 style="width: auto; max-height: 40px;" />
		</div>
		<div class="col-md-7">
			<c:if test='${userSessionDto.kkykBng != null}' >
				<table class="pull-right margin-right30 margin-top8">
					<tr>
						<th style="min-width:80px;"><ft:label key="system.item.9010" /></th>
						<td>：</td>
						<td>${userSessionDto.kkykBng}</td>
					</tr>
					<tr>
						<th><ft:label key="system.item.9030" /></th>
						<td>：</td>
						<td>${userSessionDto.seiqMei}</td>
					</tr>
				</table>
			</c:if>
		</div>
		<div class="col-md-1">
			<button id="ft-logout" type="button" class="btn btn-default navbar-btn pull-right margin-right20"><ft:label key="system.button.9050" /></button>
		</div>
	</div>
</div>

<%-- banner --%>
<% String display = FtResourcesUtil.getItemValue("flag_display_banner").toString().trim(); %>
<c:if test="<%=display.equals(CommonConst.STR_TRUE)%>">
<c:if test='${contractInfoQueryForm.bannerUrlLink != null}'>
	<c:if test='${contractInfoQueryForm.bannerUrlLink != ""}'>
		<div id="banner-info" class="container-fluid">
			<a href="${contractInfoQueryForm.bannerUrlLink }" target="_blank">
				<img src="${f:url(contractInfoQueryForm.bannerImgLink)}" alt="banner"/>
			</a>
		</div>
	</c:if>
</c:if>
</c:if>

<html:errors/>
<ft:infos />
<ft:warns/>

<div id="ft-dialog-confirm" title="">
</div>