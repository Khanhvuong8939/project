<%@page import="org.seasar.struts.util.MessageResourcesUtil"%>
<%@ page import="jp.co.forvaltel.common.util.FtResourcesUtil" %>
<%@ page import="jp.co.forvaltel.common.constant.CommonConst" %>

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
						out.println(MessageResourcesUtil.getMessage(CommonConst.MSG_ID_ERRORS_SYSTEM_TITLE));
						%>
	               	</dt>
	                <dd class="ui-li ui-li-static ui-body-c ui-corner-bottom">
						<%
//	 					TTM_DEV BEGIN EDIT 20180312
//							out.println(MessageResourcesUtil.getMessage(CommonConst.MSG_ID_ERRORS_SYSTEM, FtResourcesUtil.getItemValue(CommonConst.ITEM_KEY_SYETEM_ERROR_TEL), FtResourcesUtil.getItemValue(CommonConst.ITEM_KEY_SYETEM_ERROR_MAIL)));
							out.println(MessageResourcesUtil.getMessage(CommonConst.MSG_ID_ERRORS_SYSTEM));
//		 				TTM_DEV END EDIT 20180312
						%>
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
