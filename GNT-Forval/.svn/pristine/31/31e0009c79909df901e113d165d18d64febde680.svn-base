<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="tiles" uri="http://jakarta.apache.org/struts/tags-tiles"%>
<%@ taglib prefix="ft" uri="/WEB-INF/tld/ft.tld"%>

<tiles:insert template="/WEB-INF/view/common/layout.jsp" flush="true">

	<!-- タイトル設定 -->
	<tiles:put name="title"><ft:label key="system.title.9000" /></tiles:put>	
	
	<!-- 表示制御 -->
	<c:set var="disabledId" value="" />
	<c:if test="${addOptionForm.displayFlg == 'finish'}">
		<c:set var="disabledId" value="ft-kakuchou-disabled" />
	</c:if>

	<!-- 表示部分実装 -->

	<tiles:put name="content" type="string">
		<div class="ft-chapter-title">
			<c:if test="${(addOptionForm.displayFlg == 'input') }">
				<h2><ft:label key="add_option.label.30" /></h2>
			</c:if>
			<c:if test="${(addOptionForm.displayFlg == 'finish') }">
				<h2><ft:label key="add_option.label.31" /></h2>
			</c:if>
		</div>
		<s:form>
			<div class="col-xs-12 padding-bottom10" style="padding:30px">
				<div class="row">
					<table id="ft-option-list" class="table table-bordered">
						<tbody>
							<tr>
								<td class="wd-20" style="background-color:rgba(242, 242, 242, 1)">
									<strong><ft:label key="add_option.label.40" /></strong>
								</td>
								<td>
<!-- 								TTM_DEV 20170606 BEGIN UPDATE -->
<%-- 								<span id="ft-kakuchou-juchno" class="ui-helper-hidden">${f:h(addOptionForm.pJuchNo)}</span>	 --%>
									<span id="ft-kakuchou-juchno" class="ui-helper-hidden">${f:h(addOptionForm.kJuchNo)}</span>
									<input  id="ft-kakuchou-screen"  class="ui-helper-hidden" value="${f:h(addOptionForm.screen)}">
<!-- 								TTM_DEV 20170606 END UPDATE -->
									<span id="ft-kakuchou-hyoujikbn" class="ui-helper-hidden">${f:h(addOptionForm.hyoujiKbn)}</span>
									<c:forEach var="kakuchouDtoList" varStatus="status" items="${addOptionForm.kakuchouDtoList}">
										<span class="ui-helper-hidden ft-inputed-kakucho" id="${f:h(kakuchouDtoList.srvShohinKbn)}-${f:h(kakuchouDtoList.srvShohinCd)}-${f:h(kakuchouDtoList.koumokuCd)}">${f:h(kakuchouDtoList.inputValue)}</span>
									</c:forEach>
									<select id="oya-shohinList" class="ft-kakuchou-shohinlist" name="shohinCd" <c:if test="${(addOptionForm.displayFlg =='finish')}">disabled</c:if>>
										<option value=""></option>
										<c:set var="select" value="" />
										<c:forEach var="itemList" items="${shohinList}">
											<option value="${f:h(itemList.itemCd)}" label="${f:h(itemList.itemDisplay)}" <c:if test="${itemList.itemCd == addOptionForm.shohinCd}">selected</c:if> >${f:h(itemList.itemDisplay)}</option>
										</c:forEach>
									</select>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<div id="${disabledId}">
										<table class="table table-bordered">
											<tr>
												<td class="ft-kakuchou-shohin ui-helper-hidden" id="oya-shohinList">
<!-- 												TTM_DEV 20170606 BEGIN UPDATE -->
<%-- 												<span class="ui-helper-hidden ft-kakuchou-juchmeisaino">${f:h(addOptionForm.pJuchMeisaiNo)}</span> --%>
													<span class="ui-helper-hidden ft-kakuchou-juchmeisaino">${f:h(addOptionForm.kJuchMeisaiNo)}</span>
<!-- 												TTM_DEV 20170606 BEGIN UPDATE	 -->
													<span class="ui-helper-hidden ft-kakuchou-shohincd"></span>
												</td>
											</tr>
										</table>
									</div>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<!-- オプション追加画面時のみ表示 -->
			<c:if test="${(addOptionForm.displayFlg == 'input') }">
				<div class="col-xs-10 col-xs-offset-1 padding-bottom10">
					<div>
						<table>
							<tbody>
								<tr>
									<td id="concentCheck" style="visibility:hidden">
										<html:checkbox styleId="ft-done" property="confirm" ></html:checkbox>
										<label for="ft-done"><ft:label key="add_option.label.100" /></label>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</c:if>
			<!-- オプション追加画面時のみ表示 -->
			<c:if test="${addOptionForm.displayFlg == 'input'}">
				<div class="row padding-bottom10">
					<div class="col-xs-12 text-center">
						<ft:button value="add_option.label.110" styleId="ft-btn-register" styleClass="btn btn-default btn-w150"  disabled="true"></ft:button>
						<ft:button value="add_option.label.111" styleId="ft-btn-back" styleClass="btn btn-default btn-w150"></ft:button>
					</div>
				</div>
			</c:if>
			<c:if test="${addOptionForm.displayFlg == 'finish'}">
				<div class="row padding-bottom10">
					<div class="col-xs-12 text-center">
						<ft:button value="add_option.label.112" styleId="ft-btn-mein-menu" styleClass="btn btn-default btn-w150"></ft:button>
					</div>
				</div>
			</c:if>
<!-- 			TTM_DEV 20170606 BEGIN UPDATE -->
<%-- 			<html:hidden name ="kJuchNo" property="kJuchNo" value="${f:h(addOptionForm.pJuchNo)}"/> --%>
<%-- 			<html:hidden name ="kJuchMeisaiNo" property="kJuchMeisaiNo" value="${f:h(addOptionForm.pJuchMeisaiNo)}"/> --%>
			<html:hidden name ="kJuchNo" property="kJuchNo" value="${f:h(addOptionForm.kJuchNo)}"/>
			<html:hidden name ="kJuchMeisaiNo" property="kJuchMeisaiNo" value="${f:h(addOptionForm.kJuchMeisaiNo)}"/>
			<html:hidden name ="screen" property="screen" value="${f:h(addOptionForm.screen)}"/>
<!-- 			TTM_DEV 20170606 END UPDATE -->
			<html:hidden name ="hyoujiKbn" property="hyoujiKbn" value="${f:h(addOptionForm.hyoujiKbn)}"/>
			<html:hidden name ="displayFlg" property="displayFlg" value="${f:h(addOptionForm.displayFlg)}"/>
			<div class="col-xs-12" id="test"></div>
		</s:form>
	</tiles:put>
	<tiles:insert page="/WEB-INF/view/common/kakuchou.jsp" />
	<!-- javascript実装 -->	
	<tiles:put name="myscript" type="string">
		<script type="text/javascript" src="${f:url('/ui/js/addOption-0.0.0.js')}"></script>
		<script type="text/javascript" src="${f:url('/ui/js/kakuchou-0.0.0.js')}"></script>
	</tiles:put>
</tiles:insert>