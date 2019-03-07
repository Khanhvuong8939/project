<%@page pageEncoding="UTF-8"%>
<%@page import="jp.co.forvaltel.common.util.FtResourcesUtil" %>

<tiles:insert template="/WEB-INF/view/common/layout.jsp" flush="true">
	
	<!-- タイトル設定 -->
	<tiles:put name="title"><ft:label key="agree.title.00" /></tiles:put>

	<!-- 表示部分実装 -->
	<tiles:put name="content" type="string">
		<div class="container-fluid imui-form-container-wide" style="position: relative;margin-bottom: 20px;">
		
		<div class="denki-layer-loader">
			<img src="${f:url('/ui/img/loadinfo.net.gif')}" alt="<bean:message key="others.page.txt.load" />"/>
			<p><bean:message key="others.page.txt.load" /></p>
		</div>
		<input type="hidden" name="token" value="${f:h(token)}"/>
		<input type="hidden" name="bpm_id" value="${f:h(bpmId)}"/>
		<input type="hidden" name="moshikomi_no" value="" id="moshikomi_no"/>
		<div class="denki-layer-blocker"><p></p></div>
		
			<s:form>
				<div class="ft-chapter-title">
					<h2><ft:label key="agree.title.01" /></h2>
				</div>
				
				<div> <!--  -->
					<table class="table table-bordered contract-table">
						<colgroup>
							<col style="width: 300px; background-color:rgb(105, 105, 105);">
							<col>
						</colgroup>

						<tbody>
							<tr>
								<th><label><ft:label key="agree.label.10" /></label></th>
								<td><small id="kkyk_bng"></small></td>
							</tr>
							<tr>
								<th><label><ft:label key="agree.label.20" /></label></th>
								<td><small id="kkyk_mei"></small></td>
							</tr>
							<tr>
								<th><label><ft:label key="agree.label.30" /></label></th>
								<td><small id="kkyk_kana"></small></td>
							</tr>
							<tr>
								<th><label><ft:label key="agree.label.40" /></label></th>
								<td><small id="yubin_bng"></small></td>
							</tr>
							<tr>
								<th><label><ft:label key="agree.label.50" /></label></th>
								<td><small id="todofken"></small></td>
							</tr>
							<tr>
								<th><label><ft:label key="agree.label.60" /></label></th>
								<td><small id="sichoku"></small></td>
							</tr>
							<tr>
								<th><label><ft:label key="agree.label.70" /></label></th>
								<td><small id="chousonbanchi"></small></td>
							</tr>
							<tr>
								<th><label><ft:label key="agree.label.80" /></label></th>
								<td><small id="biru_tatemono"></small></td>
							</tr>
							<tr>
								<th><label><ft:label key="agree.label.90" /></label></th>
								<td><small id="tel"></small></td>
							</tr>
							<tr>
								<th><label><ft:label key="agree.label.100" /></label></th>
								<td><small id="email"></small></td>
							</tr>
						</tbody>
					</table>
				</div>
				
				<div class="ft-chapter-title">
					<h2><ft:label key="agree.title.02" /></h2>
				</div>
				
				<div> <!--  -->
					<table class="table table-bordered contract-table">
						<colgroup>
							<col style="width: 300px; background-color:rgb(105, 105, 105);">
							<col>
						</colgroup>
						<tbody>
							<%-- 拡張項目 --%>
							<tr class="ui-helper-hidden">
								<th></th>
								<td class="ft-apply-kakuchou"></td>
							</tr>
						</tbody>
					</table>
					
					<div id="agree-text-dev">
						<ft:label key="agree.text.00" />
					</div>
					
					<div id="agree-btn-dev" class="row padding-top20 padding-bottom20">
						<div class="col-xs-12 text-left">
							<ft:button value="agree.button.10" styleId="agree-button" styleClass="btn btn-default"></ft:button>
						</div>
					</div>
				</div>
				
			</s:form>
		</div>
	</tiles:put>

	<tiles:insert page="/WEB-INF/view/common/kakuchou.jsp" />
	<!-- javascript実装 -->
	<tiles:put name="myscript" type="string">
		<script type="text/javascript" src="${f:url('/ui/js/agree-0.0.0.js')}"></script>
		<script type="text/javascript" src="${f:url('/ui/js/kakuchou-0.0.0.js')}"></script>
	</tiles:put>
	<s:form action="" method="post" styleClass="form-to-error">
		<input type="hidden" name="webapiMsg" value=""/>
		<input type="hidden" name="webapiErrCd" value=""/>
	</s:form>

</tiles:insert>