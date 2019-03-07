<%--
   - Copyright Notice   ： NTTDATA INTRAMART , INC. All Rights Reserved.
   - ファイル名                             ：mailAccountRegisterChange.jsp
   - 作者                                    ：Y.Wang
   - バージョン                              ：1.00
   - 作成日                                 ：2016/06/02
   - 履歴                                     ：2016/06/02 新規作成
   - 説明                                     ：メール情報設定
--%>

<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="tiles" uri="http://jakarta.apache.org/struts/tags-tiles"%>
<%@taglib prefix="ft" uri="/WEB-INF/tld/ft.tld"%>

<tiles:insert template="/WEB-INF/view/common/layout.jsp" flush="true">

    <!-- タイトル設定 --> 
    <tiles:put name="title"><ft:label key="system.title.9000" /></tiles:put>
    
    <!-- 表示部分実装 -->    
    <tiles:put name="content" type="string">
        <div class="ft-chapter-title">
            <c:if test="${mailAccountRegisterChangeForm.judgeFlg == '1'}">
                <h2><ft:label key="mail_account_register_change.label.01" /></h2>
            </c:if>
            <c:if test="${mailAccountRegisterChangeForm.judgeFlg == '2'}">
                <h2><ft:label key="mail_account_register_change.label.02" /></h2>
            </c:if>
            <c:if test="${mailAccountRegisterChangeForm.judgeFlg == '3'}">
                <h2><ft:label key="mail_account_register_change.label.03" /></h2>
            </c:if>
            <c:if test="${mailAccountRegisterChangeForm.judgeFlg == '4'}">
                <h2><ft:label key="mail_account_register_change.label.04" /></h2>
            </c:if>
        </div>
        <s:form>
            <div class="container">
                <div class="row">
                    <div class="col-xs-12 text-center">
                        <c:if test="${mailAccountRegisterChangeForm.judgeFlg == '1' or mailAccountRegisterChangeForm.judgeFlg == '3'}">
                            <label class="imui-required"><font color="#ff0000">*</font></label><ft:label key="mail_account_register_change.label.05" />
                        </c:if>
                        <c:if test="${mailAccountRegisterChangeForm.judgeFlg == '2'}">
                            <ft:label key="mail_account_register_change.label.06" />
                        </c:if>
                        <c:if test="${mailAccountRegisterChangeForm.judgeFlg == '4'}">
                            <ft:label key="mail_account_register_change.label.06" />
                        </c:if>
                    </div>
                </div>
                <c:if test="${mailAccountRegisterChangeForm.judgeFlg == '1' or mailAccountRegisterChangeForm.judgeFlg == '3'}">
                    <div class="row col-xs-12" style="margin-top: 7px;">
                </c:if>
                <c:if test="${mailAccountRegisterChangeForm.judgeFlg == '2' or mailAccountRegisterChangeForm.judgeFlg == '4'}">
                    <div class="row col-xs-12" style="margin-top: 20px;">
                </c:if>
                    <div class="col-xs-8" style="left:20%">
                        <table class="table table-bordered">
                            <colgroup>
                                <col style="width:35%; background-color:#F0F0F0">
                                <col>
                            </colgroup>
                            <tbody>
<!--                             TTM_DEV ORDER_MODULE 20170530 BEGIN ADD -->
<!--                             	<tr> -->
<!--                                     <td class="text-right"> -->
<%--                                             <strong><ft:label key="mail_account_register_change.label.09" /></strong><br/> --%>
<!--                                     </td> -->
<!--                                     <td> -->
<%--                                             ${f:h(mailAccountRegisterChangeForm.internetConnectionId)} --%>
<!--                                     </td> -->
<!--                                 </tr> -->
<!--                                 <tr> -->
<!-- 								TTM_DEV ORDER_MODULE 20170530 END ADD -->
									<tr>
                                    <td class="text-right">
                                        <c:if test="${mailAccountRegisterChangeForm.judgeFlg == '1'}">
                                            <strong><label class="imui-required"><font color="#ff0000">*</font></label><ft:label key="mail_account_register_change.label.07" /></strong><br/>
                                            <strong><font color="#ff0000"><ft:label key="mail_account_register_change.label.08" /></font></strong>
                                        </c:if>
                                        <c:if test="${mailAccountRegisterChangeForm.judgeFlg == '2' or mailAccountRegisterChangeForm.judgeFlg == '3' or mailAccountRegisterChangeForm.judgeFlg == '4'}">
                                            <strong><ft:label key="mail_account_register_change.label.07" /></strong>
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:if test="${mailAccountRegisterChangeForm.judgeFlg == '1'}">
                                            <html:text size="32" styleId="textMailAddress" name="mailAccountRegisterChangeForm" property="mailAddress"></html:text>
											<ft:label key="mail_account_register_change.label.10"/>
										</c:if>
                                        <c:if test="${mailAccountRegisterChangeForm.judgeFlg == '2' or mailAccountRegisterChangeForm.judgeFlg == '3' or mailAccountRegisterChangeForm.judgeFlg == '4'}">
                                            ${f:h(mailAccountRegisterChangeForm.mailAddress)}
                                        </c:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="text-right">
                                        <strong><ft:label key="mail_account_register_change.label.11" /></strong><br/>
                                    </td>
                                    <td>
                                        ${f:h(mailAccountRegisterChangeForm.pop3ServerName)}
                                    </td>
                                </tr>
                                <tr>
                                    <td class="text-right">
                                        <strong><ft:label key="mail_account_register_change.label.13" /></strong><br/>
                                    </td>
                                    <td>
                                        ${f:h(mailAccountRegisterChangeForm.smtpServerName)}
                                    </td>
                                </tr>
                                <tr>
                                    <td class="text-right">
                                        <c:if test="${mailAccountRegisterChangeForm.judgeFlg == '1' or mailAccountRegisterChangeForm.judgeFlg == '3'}">
                                            <strong><label class="imui-required"><font color="#ff0000">*</font></label><ft:label key="mail_account_register_change.label.15" /></strong><br/>
                                            <strong><font color="#ff0000"><ft:label key="mail_account_register_change.label.16" /></font></strong>
                                        </c:if>
                                        <c:if test="${mailAccountRegisterChangeForm.judgeFlg == '2' or mailAccountRegisterChangeForm.judgeFlg == '4'}">
                                            <strong><ft:label key="mail_account_register_change.label.15" /></strong><br/>
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:if test="${mailAccountRegisterChangeForm.judgeFlg == '1' or mailAccountRegisterChangeForm.judgeFlg == '3'}">
                                            <html:text size="32" name="mailAccountRegisterChangeForm" property="mailAddressPassword"></html:text><br/>
                                            <font color="#ff0000"><ft:label key="mail_account_register_change.label.18" /></font>
                                        </c:if>
                                        <c:if test="${mailAccountRegisterChangeForm.judgeFlg == '2' or mailAccountRegisterChangeForm.judgeFlg == '4'}">
                                            ${f:h(mailAccountRegisterChangeForm.mailAddressPassword)}
                                        </c:if>
                                    </td>
                                </tr>
                                <c:if test="${mailAccountRegisterChangeForm.judgeFlg == '1' or mailAccountRegisterChangeForm.judgeFlg == '3'}">
                                    <tr>
                                        <td class="text-right">
                                            <strong><label class="imui-required"><font color="#ff0000">*</font></label><ft:label key="mail_account_register_change.label.19" /></strong><br/>
                                        </td>
                                        <td>
                                            <html:text size="32" name="mailAccountRegisterChangeForm" property="mailAddressPasswordConfirm"></html:text>
                                        </td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>

                <div class="row padding-bottom10">
                    <div class="col-xs-12 text-center">
                        <c:if test="${mailAccountRegisterChangeForm.judgeFlg == '1' or mailAccountRegisterChangeForm.judgeFlg == '3'}">
                            <span style="margin-right:2%">
                                <ft:button styleId="ft-btn-menu" value="mail_account_register_change.button.21" styleClass="btn btn-default btn-w150"></ft:button>
                            </span>
                        </c:if>
                        <c:if test="${mailAccountRegisterChangeForm.judgeFlg == '2' or mailAccountRegisterChangeForm.judgeFlg == '4'}">
                            <ft:button styleId="ft-btn-menu" value="mail_account_register_change.button.24" styleClass="btn btn-default btn-w150"></ft:button>
                        </c:if>
                        <c:if test="${mailAccountRegisterChangeForm.judgeFlg == '1'}">
                            <span style="margin-right:2%">
                                <ft:button styleId="ft-btn-regist" value="mail_account_register_change.button.22" styleClass="btn btn-default btn-w150"></ft:button>
                            </span>
                        </c:if>
                        <c:if test="${mailAccountRegisterChangeForm.judgeFlg == '3'}">
                            <span style="margin-right:2%">
                                <ft:button styleId="ft-btn-change" value="mail_account_register_change.button.23" styleClass="btn btn-default btn-w150"></ft:button>
                            </span>
                        </c:if>
                    </div>
                </div>
            </div>
<!--             TTM_DEV 20170606 BEGIN ADD -->
             <html:hidden name="mailAccountRegisterChangeForm" property="internetConnectionId" value="${f:h(mailAccountRegisterChangeForm.internetConnectionId)}"/>
              <html:hidden name="mailAccountRegisterChangeForm" property="accountClassification" value="${f:h(mailAccountRegisterChangeForm.accountClassification)}"/>              
<!--            TTM_DEV 20170606 BEGIN ADD -->
            <html:hidden name="mailAccountRegisterChangeForm" property="judgeFlg" value="${f:h(mailAccountRegisterChangeForm.judgeFlg)}"/>
            <c:if test="${mailAccountRegisterChangeForm.judgeFlg == '3'}">
                <html:hidden name="mailAccountRegisterChangeForm" property="mailAddress" value="${f:h(mailAccountRegisterChangeForm.mailAddress)}"/>
            </c:if>
            <html:hidden name="mailAccountRegisterChangeForm" property="pop3ServerName" value="${f:h(mailAccountRegisterChangeForm.pop3ServerName)}"/>
            <html:hidden name="mailAccountRegisterChangeForm" property="smtpServerName" value="${f:h(mailAccountRegisterChangeForm.smtpServerName)}"/>
        </s:form>
    </tiles:put>

    <!-- javascript実装 -->
    <tiles:put name="myscript" type="string">
        <script type="text/javascript" src="${f:url('/ui/js/mailAccountRegisterChange-0.0.0.js')}"></script>
    </tiles:put>
</tiles:insert>