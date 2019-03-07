<%@page import="org.seasar.struts.util.MessageResourcesUtil"%>
<%@ page import="jp.co.forvaltel.common.util.FtResourcesUtil" %>
<%@ page import="jp.co.forvaltel.common.constant.CommonConst" %>
<%@ page import="jp.co.forvaltel.user.constant.DenryokuConst" %>

<tiles:insert template="/WEB-INF/view/common/layout.jsp" flush="true">
	<!-- タイトル設定 -->	
	<tiles:put name="title"><ft:label key="system.title.9000" /></tiles:put>

	<!-- 表示部分実装 -->	
	<tiles:put name="content" type="string">
    <div id="ft-container">
	    <div class="ft-box-error">
	        <div class="ft-box-error-inner inner-error" data-role="content">
	            <dl class="ui-listview ui-listview-inset ui-corner-all ui-shadow">
	                <dt class="ui-li ui-li-divider ui-bar-b ui-corner-top">
						<%
						out.println(MessageResourcesUtil.getMessage(DenryokuConst.MSG_ID_ERRORS_WEBAPI_ERROR));
						%>
	               	</dt>
	                <dd class="ui-li ui-li-static ui-body-c ui-corner-bottom">
						${f:br(f:nbsp(f:h(webapiMsg)))}
	                </dd>
	            </dl>
	        </div>
	        
	    </div>
    </div>
	</tiles:put>
	
	<!-- javascript実装 -->	
	<tiles:put name="myscript" type="string">
	 
	</tiles:put>
	
</tiles:insert>
