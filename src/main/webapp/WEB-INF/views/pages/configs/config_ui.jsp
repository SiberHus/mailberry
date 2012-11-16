<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<form:form id="config_ui_form" action="${PATH.configs }/ui" method="post" modelAttribute="ui">
<%@include file="/WEB-INF/views/includes/body/form_error.jsp" %>
<h2>DataGrid</h2>
<table class="fieldset tablesorter" >
	<thead>
		<tr>
			<th class="field">Name</th>
			<th>Value</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td class="field">Default Number of Display Row</td>
			<td class="value">
				<form:input path="dataGrid.rowNum"/>
				<span ref="dataGrid.rowNum" class="error"></span>
			</td>
		</tr>
		<tr>
			<td class="field">
				Row List (Comma Separate Value)
				<span class="info">Ex. 10, 20, 30, 40</span>
			</td>
			<td class="value">
				<form:input path="dataGrid.rowList"/>
				<span ref="dataGrid.rowList" class="error"></span>
			</td>
		</tr>
		<tr>
			<td class="field">
				Height
				<span class="info">datagrid height in pixel</span>
			</td>
			<td class="value">
				<form:input path="dataGrid.height"/>
				<span ref="dataGrid.height" class="error"></span>
			</td>
		</tr>
	</tbody>
</table>
<h2>Calendar</h2>
<div id="calendar_tabs">
	<ul>
		<li><a href="#cal_tab_1">Campaign Default</a></li>
		<li><a href="#cal_tab_2">Campaign In Progress</a></li>
		<li><a href="#cal_tab_3">Campaign Sent</a></li>
		<li><a href="#cal_tab_4">Campaign Canceled</a></li>
		<li><a href="#cal_tab_5">Campaign Scheduled</a></li>
	</ul>
	<div id="cal_tab_1">
		<table class="tablesorter colors" style="table-layout:fixed;">
			<thead>
				<tr><th>Background</th><th>Border</th><th>Text</th></tr>
			</thead>
			<tbody>
				<tr>
					<td><form:input id="default_bg_color" path="calendar.campaignBgColor" cssClass="color_picker"/></td>
					<td><form:input id="default_bd_color" path="calendar.campaignBdColor" cssClass="color_picker"/></td>
					<td><form:input id="default_text_color" path="calendar.campaignTxtColor" cssClass="color_picker"/></td>
				</tr>
				<tr>
					<td colspan="3">
						<span ref="calendar.campaignBgColor" class="error"></span>
						<span ref="calendar.campaignBdColor" class="error"></span>
						<span ref="calendar.campaignTxtColor" class="error"></span>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div id="cal_tab_2">
		<table class="tablesorter colors">
			<thead>
				<tr><th>Background</th><th>Border</th><th>Text</th></tr>
			</thead>
			<tbody>
				<tr>
					<td><form:input id="inp_bg_color" path="calendar.campaignInpBgColor" cssClass="color_picker"/></td>
					<td><form:input id="inp_bd_color" path="calendar.campaignInpBdColor" cssClass="color_picker"/></td>
					<td><form:input id="inp_text_color" path="calendar.campaignInpTxtColor" cssClass="color_picker"/></td>
				</tr>
				<tr>
					<td colspan="3">
						<span ref="calendar.campaignInpBgColor" class="error"></span>
						<span ref="calendar.campaignInpBdColor" class="error"></span>
						<span ref="calendar.campaignInpTxtColor" class="error"></span>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div id="cal_tab_3">
		<table class="tablesorter colors">
			<thead>
				<tr><th>Background</th><th>Border</th><th>Text</th></tr>
			</thead>
			<tbody>
				<tr>
					<td><form:input id="sen_bg_color" path="calendar.campaignSenBgColor" cssClass="color_picker"/></td>
					<td><form:input id="sen_bd_color" path="calendar.campaignSenBdColor" cssClass="color_picker"/></td>
					<td><form:input id="sen_text_color" path="calendar.campaignSenTxtColor" cssClass="color_picker"/></td>
				</tr>
				<tr>
					<td colspan="3">
						<span ref="calendar.campaignSenBgColor" class="error"></span>
						<span ref="calendar.campaignSenBdColor" class="error"></span>
						<span ref="calendar.campaignSenTxtColor" class="error"></span>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div id="cal_tab_4">
		<table class="tablesorter colors">
			<thead>
				<tr><th>Background</th><th>Border</th><th>Text</th></tr>
			</thead>
			<tbody>
				<tr>
					<td><form:input id="can_bg_color" path="calendar.campaignCanBgColor" cssClass="color_picker"/></td>
					<td><form:input id="can_bd_color" path="calendar.campaignCanBdColor" cssClass="color_picker"/></td>
					<td><form:input id="can_text_color" path="calendar.campaignCanTxtColor" cssClass="color_picker"/></td>
				</tr>
				<tr>
					<td colspan="3">
						<span ref="calendar.campaignCanBgColor" class="error"></span>
						<span ref="calendar.campaignCanBdColor" class="error"></span>
						<span ref="calendar.campaignCanTxtColor" class="error"></span>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div id="cal_tab_5">
		<table class="tablesorter colors">
			<thead>
				<tr><th>Background</th><th>Border</th><th>Text</th></tr>
			</thead>
			<tbody>
				<tr>
					<td><form:input id="sch_bg_color" path="calendar.campaignSchBgColor" cssClass="color_picker"/></td>
					<td><form:input id="sch_bd_color" path="calendar.campaignSchBdColor" cssClass="color_picker"/></td>
					<td><form:input id="sch_text_color" path="calendar.campaignSchTxtColor" cssClass="color_picker"/></td>
				</tr>
				<tr>
					<td colspan="3">
						<span ref="calendar.campaignSchBgColor" class="error"></span>
						<span ref="calendar.campaignSchBdColor" class="error"></span>
						<span ref="calendar.campaignSchTxtColor" class="error"></span>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</div>
<footer>
	<div class="submit_link">
		<sec:authorize access="hasRole('ROLE_DEMO')">
		<span>Demo user cannot save this data</span>
		</sec:authorize>
		<sec:authorize access="!hasRole('ROLE_DEMO')">
		<input type="submit" id="save_ui_btn" value="Save" />
		</sec:authorize>
	</div>
</footer>
</form:form>