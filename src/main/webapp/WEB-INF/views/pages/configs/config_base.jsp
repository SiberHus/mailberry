<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<form:form id="config_base_form" action="${PATH.configs }/base" method="post" modelAttribute="base">
<h2>Base</h2>
<table class="fieldset tablesorter" >
	<thead>
		<tr>
			<th class="field">Name</th>
			<th>Value</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td class="field">Temporary Directory</td>
			<td class="value">
				<form:input path="tmpDir"/>
				<span ref="tmpDir" class="error"></span>
			</td>
		</tr>
		<tr>
			<td class="field">Server URL</td>
			<td class="value">
				<form:input path="serverUrl"/>
				<span ref="serverUrl" class="error"></span>
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
		<input type="submit" id="save_base_btn" value="Save" />
		</sec:authorize>
	</div>
</footer>
</form:form>