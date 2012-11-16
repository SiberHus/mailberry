<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<form:form id="config_format_form" action="${PATH.configs }/format" method="post" modelAttribute="format">
<h2>Data Format</h2>
<table class="fieldset tablesorter" >
	<thead>
		<tr>
			<th class="field">Name</th>
			<th>Value</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td class="field">Display Timestamp</td>
			<td class="value">
				<form:input path="displayTimestamp"/>
				<span ref="displayTimestamp" class="error"></span>
			</td>
		</tr>
		<tr>
			<td class="field">Display Date</td>
			<td class="value">
				<form:input path="displayDate"/>
				<span ref="displayDate" class="error"></span>
			</td>
		</tr>
		<tr>
			<td class="field">Display Time</td>
			<td class="value">
				<form:input path="displayTime"/>
				<span ref="displayTime" class="error"></span>
			</td>
		</tr>
		<tr>
			<td class="field">Input Timestamp</td>
			<td class="value">
				<form:input path="inputTimestamp"/>
				<span ref="inputTimestamp" class="error"></span>
			</td>
		</tr>
		<tr>
			<td class="field">Input Date</td>
			<td class="value">
				<form:input path="inputDate"/>
				<span ref="inputDate" class="error"></span>
			</td>
		</tr>
		<tr>
			<td class="field">Input Time</td>
			<td class="value">
				<form:input path="inputTime"/>
				<span ref="inputTime" class="error"></span>
			</td>
		</tr>
	</tbody>
</table>
<footer>
	<div class="submit_link">
		<sec:authorize access="hasRole('ROLE_DEMO')">
		<span>Demo user cannot save this data</span>
		</sec:authorize>
		<sec:authorize access="!hasRole('ROLE_DEMO')">
		<input type="submit" id="save_format_btn" value="Save" />
		</sec:authorize>
	</div>
</footer>
</form:form>