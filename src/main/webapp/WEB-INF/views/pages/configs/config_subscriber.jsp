<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<form:form id="config_subscriber_form" action="${PATH.configs }/subscriber" method="post" modelAttribute="subscriber">
<h2>File Import</h2>
<table class="fieldset tablesorter" >
	<thead>
		<tr>
			<th class="field">Name</th>
			<th>Value</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td class="field">Source Directory</td>
			<td class="value">
				<form:input path="fileImport.sourceDir"/>
				<span ref="fileImport.sourceDir" class="error"></span>
			</td>
		</tr>
		<tr>
			<td class="field">Error Directory</td>
			<td class="value">
				<form:input path="fileImport.errorDir"/>
				<span ref="fileImport.errorDir" class="error"></span>
			</td>
		</tr>
	</tbody>
</table>

<h2>Opt-Out (Unsubscribe)</h2>
<table class="fieldset tablesorter" >
	<thead>
		<tr>
			<th class="field">Name</th>
			<th>Value</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td class="field">Success Page</td>
			<td class="value">
				<form:input path="optOut.pageSuccess"/>
				<span ref="optOut.pageSuccess" class="error"></span>
			</td>
		</tr>
		<tr>
			<td class="field">Configuration Page</td>
			<td class="value">
				<form:input path="optOut.pageConfig"/>
				<span ref="optOut.pageConfig" class="error"></span>
			</td>
		</tr>
		<tr>
			<td class="field">Link Name</td>
			<td class="value">
				<form:input path="optOut.linkName"/>
				<span ref="optOut.linkName" class="error"></span>
			</td>
		</tr>
	</tbody>
</table>

<h2>RSVP</h2>
<table class="fieldset tablesorter" >
	<thead>
		<tr>
			<th class="field">Name</th>
			<th>Value</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td class="field">Success Page</td>
			<td class="value">
				<form:input path="rsvp.pageSuccess"/>
				<span ref="rsvp.pageSuccess" class="error"></span>
			</td>
		</tr>
		<tr>
			<td class="field">Configuration Page</td>
			<td class="value">
				<form:input path="rsvp.pageConfig"/>
				<span ref="rsvp.pageConfig" class="error"></span>
			</td>
		</tr>
		<tr>
			<td class="field">Attending Link Name</td>
			<td class="value">
				<form:input path="rsvp.linkNameAtt"/>
				<span ref="rsvp.linkNameAtt" class="error"></span>
			</td>
		</tr>
		<tr>
			<td class="field">Maybe Link Name</td>
			<td class="value">
				<form:input path="rsvp.linkNameMay"/>
				<span ref="rsvp.linkNameMay" class="error"></span>
			</td>
		</tr>
		<tr>
			<td class="field">Declined Link Name</td>
			<td class="value">
				<form:input path="rsvp.linkNameDec"/>
				<span ref="rsvp.linkNameDec" class="error"></span>
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
		<input type="submit" id="save_subscriber_btn" value="Save" />
		</sec:authorize>
	</div>
</footer>
</form:form>