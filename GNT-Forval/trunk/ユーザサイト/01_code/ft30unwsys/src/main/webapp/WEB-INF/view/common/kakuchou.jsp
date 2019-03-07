<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ft" uri="/WEB-INF/tld/ft.tld"%>

<table class="wd-100 ft-table ui-helper-hidden">
	<tbody>
		<tr class="ft-template-label" data-parent="">
			<th><label class="ft-template-item-title"></label></th>
			<td>
				<span class="ft-template-item-label"></span>
				<br/><span class="ft-template-item-commnet"></span>
			</td>
		</tr>
<!-- 		TTM_DEV 20170705 ADD BEGIN -->
		<tr class="ft-template-hidden ui-helper-hidden" data-parent="">
			<th><label class="ft-template-item-title"></label></th>
			<td>
				<span class="ft-template-item-label"></span>
				<br/><span class="ft-template-item-commnet"></span>
			</td>
		</tr>
<!-- 		TTM_DEV 20170705 ADD END -->
		<tr class="ft-template-text">
			<th><label class="ft-template-item-title"></label></th>
			<td>
				<input type="text" class="ft-template-item-text"/>
				<br/><span class="ft-template-item-commnet"></span>
			</td>
		</tr>
		<tr class="ft-template-button">
			<th><label class="ft-template-item-title"></label></th>
			<td>
				<input type="button" class="btn btn-default ft-template-item-button" tabindex="-1" />
				<br/><span class="ft-template-item-commnet"></span>
			</td>
		</tr>
		<tr class="ft-template-url">
			<th><label class="ft-template-item-title"></label></th>
			<td>
				<a href="" target="_blank" class="ft-template-item-link"></a>
			</td>
		</tr>
		<tr class="ft-template-select">
			<th><label class="ft-template-item-title"></label></th>
			<td>
				<select class="ft-template-item-select"></select>
				<br/><span class="ft-template-item-commnet"></span>
			</td>
		</tr>
		<tr class="ft-template-date">
			<th><label class="ft-template-item-title"></label></th>
			<td>
				<input type="text" class="ft-template-item-date" maxlength="10" size="10" />
				<br/><span class="ft-template-item-commnet"></span>
			</td>
		</tr>
		<tr class="ft-template-check">
			<th><label class="ft-template-item-title"></label></th>
			<td>
				<input type="hidden" class="ft-template-item-check-hidden ft-kakuchou-check-hidden" />
				<input id="ft-template-item-check" type="checkbox" class="ft-template-item-check ft-kakuchou-check" />
				<label for="ft-template-item-check" class="ft-template-item-check-label"></label>
				<br/><span class="ft-template-item-commnet"></span>
			</td>
		</tr>
	</tbody>
</table>

