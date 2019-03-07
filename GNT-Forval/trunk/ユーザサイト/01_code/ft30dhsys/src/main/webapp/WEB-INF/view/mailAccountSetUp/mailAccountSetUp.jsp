<%--
   - Copyright Notice   ： NTTDATA INTRAMART , INC. All Rights Reserved.
   - ファイル名                             ：mailAccountSetUp.jsp
   - 作者                                    ：Y.Wang
   - バージョン                              ：1.00
   - 作成日                                 ：2016/06/07
   - 履歴                                     ：2016/06/07 新規作成
   - 説明                                     ：メール詳細設定
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
            <h2><ft:label key="mail_account_set_up.label.10" /></h2>
        </div>
        <s:form>
            <div class="container row col-xs-12" style="margin-top: 7px;">
                <div class="col-xs-7" style="padding-top:30px; padding-left:30px; left:20%; background-color:#F0F0F0; border-style:solid; border-width:1px; border-color:black;">
                    <div class="col-xs-8">
                        <table class="table table-bordered" >
                            <colgroup>
                                <col style="width:35%">
                                <col style="width:65%">
                            </colgroup>
                            <tbody>
                                <tr style="height:28px;">
                                    <td class="text-left" style="border-style:solid; border-width:1px; border-color:black;">
                                        <ft:label key="mail_account_set_up.label.21" />：
                                    </td>
                                    <td class="text-left" style="border-style:solid; border-width:1px; border-color:black;">
                                        ${f:h(mailAccountSetUpForm.mailAddress)}
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="col-xs-12">
                        <table class="table table-bordered" >
                            <colgroup>
                                <col style="width:15%">
                                <col style="width:85%">
                            </colgroup>
                            <tbody>
                                <tr style="height:28px;">
                                    <td class="text-center" style="border-style:solid; border-width:1px; border-color:black;">
                                        <ft:label key="mail_account_set_up.label.31" />
                                    </td>
                                    <td class="text-left" style="border-style:solid; border-width:1px; border-color:black;">
                                        <div>
                                            <html:radio styleId="radioNoTransfer" name="mailAccountSetUpActionForm" property="transferConfigration" value="0" ></html:radio>
                                            <label for="radioNoTransfer"><ft:label key="mail_account_set_up.label.32"></ft:label></label>
                                        </div>
                                        <div>
                                            <html:radio styleId="radioTransferMailAddress" name="mailAccountSetUpActionForm" property="transferConfigration" value="1"></html:radio>
                                            <label for="radioTransferMailAddress"><ft:label key="mail_account_set_up.label.33"></ft:label></label>
                                            <html:text styleId="textTransferMailAddress" name="mailAccountSetUpActionForm" property="transferMailAddress" size="36"></html:text>
                                        </div>
                                        <div style="padding-left:6%">
                                            <html:checkbox styleId="checkboxLeaveMail" name="mailAccountSetUpActionForm" property="leaveMail" value="1"></html:checkbox>
                                            <label for="checkboxLeaveMail"><ft:label key="mail_account_set_up.label.35"></ft:label></label>
                                        </div>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="col-xs-10">
                        <div>
                            <html:checkbox styleId="checkboxAutoReplyConfigration" name="mailAccountSetUpActionForm" property="autoReplyConfigration" value="1" ></html:checkbox>
                            <label for="checkboxAutoReplyConfigration"><ft:label key="mail_account_set_up.label.41"></ft:label></label>
                        </div>
                    </div>
                    <div class="col-xs-11">
                        <div>
                            <span>
                                <ft:label key="mail_account_set_up.label.42"></ft:label>
                            </span>
                            <span>
                                <html:text styleId="textSubject" name="mailAccountSetUpActionForm" property="subject" size="45"></html:text><br/>
                            </span>
                            <ft:label key="mail_account_set_up.label.44"></ft:label>
                            <html:textarea styleId="textareaText" name="mailAccountSetUpActionForm" property="text" rows="10" cols="70"></html:textarea>
                        </div>
                    </div>
                    <div class="row padding-bottom10">
                        <div class="col-xs-12 text-center" style="margin-top:20px;">
                            <span style="margin-right:6%">
                                <ft:button styleId="ft-btn-back" value="mail_account_set_up.button.111" styleClass="btn btn-default btn-w100"></ft:button>
                            </span>
                            <span style="margin-left:6%">
                                <ft:button styleId="ft-btn-update" value="mail_account_set_up.button.112" styleClass="btn btn-default btn-w100"></ft:button>
                            </span>
                        </div>
                    </div>
                </div>
            </div>
            <html:hidden name="mailAccountSetUpActionForm" property="mailAddress" value="${f:h(mailAccountSetUpForm.mailAddress)}" />
            <html:hidden styleId="transferMailAddress" name="mailAccountSetUpActionForm" property="transferMailAddress" />
            <html:hidden styleId="leaveMail" name="mailAccountSetUpActionForm" property="leaveMail" />
            <html:hidden styleId="autoReplyConfigration" name="mailAccountSetUpActionForm" property="autoReplyConfigration" />
            <html:hidden styleId="subject" name="mailAccountSetUpActionForm" property="subject" />
            <html:hidden styleId="text" name="mailAccountSetUpActionForm" property="text" />
<!--        TTM_DEV 20170606 BEGIN ADD -->
            <html:hidden name="mailAccountSetUpActionForm" property="internetConnectionId" value="${f:h(mailAccountSetUpForm.internetConnectionId)}"/>
            <html:hidden name="mailAccountSetUpActionForm" property="accountClassification" value="${f:h(mailAccountSetUpForm.accountClassification)}"/>
<!--        TTM_DEV 20170606 BEGIN ADD -->
        </s:form>
    </tiles:put>

    <!-- javascript実装 -->    
    <tiles:put name="myscript" type="string">
        <script type="text/javascript" src="${f:url('/ui/js/mailAccountSetUp-0.0.0.js')}"></script>
    </tiles:put>
</tiles:insert>