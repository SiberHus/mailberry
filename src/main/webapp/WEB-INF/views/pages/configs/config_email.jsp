<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<script type="text/javascript">
	$(document).ready(function() {
		
	});
</script>
<form:form id="config_email_form" action="${PATH.configs }/email" method="post" modelAttribute="email">
<h2>Email</h2>
<table class="fieldset tablesorter" >
	<thead>
		<tr>
			<th class="field">Name</th>
			<th>Value</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td class="field">Mail User-Agent</td>
			<td class="value">
				<form:input path="mailUserAgent"/>
				<span ref="mailUserAgent" class="error"></span>
			</td>
		</tr>
		<tr>
			<td class="field">Default From Name</td>
			<td class="value">
				<form:input path="defaultFromName"/>
				<span ref="defaultFromName" class="error"></span>
			</td>
		</tr>
		<tr>
			<td class="field">Default From Email</td>
			<td class="value">
				<form:input path="defaultFromEmail"/>
				<span ref="defaultFromEmail" class="error"></span>
			</td>
		</tr>
		<tr>
			<td class="field">Default ReplyTo Email</td>
			<td class="value">
				<form:input path="defaultReplyToEmail"/>
				<span ref="defaultReplyToEmail" class="error"></span>
			</td>
		</tr>
	</tbody>
</table>

<h2>Sending</h2>
<table class="fieldset tablesorter" >
	<thead>
		<tr>
			<th class="field">Name</th>
			<th>Value</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td class="field">Pause Between Messages</td>
			<td class="value">
				<form:input path="sending.pauseBetweenMessages"/>
				<span ref="sending.pauseBetweenMessages" class="error"></span>
			</td>
		</tr>
		<tr>
			<td class="field">Number of Threads</td>
			<td class="value">
				<form:input path="sending.numberOfThreads"/>
				<span ref="sending.numberOfThreads" class="error"></span>
			</td>
		</tr>
		<tr>
			<td class="field">Messages per Connection</td>
			<td class="value">
				<form:input path="sending.messagesPerConnection"/>
				<span ref="sending.messagesPerConnection" class="error"></span>
			</td>
		</tr>
		<tr>
			<td class="field">Number of Attempts</td>
			<td class="value">
				<form:input path="sending.numberOfAttempts"/>
				<span ref="sending.numberOfAttempts" class="error"></span>
			</td>
		</tr>
		<tr>
			<td class="field">Pause Between Attempts</td>
			<td class="value">
				<form:input path="sending.pauseBetweenAttempts"/>
				<span ref="sending.pauseBetweenAttempts" class="error"></span>
			</td>
		</tr>
		<tr>
			<td class="field">Block on Fail</td>
			<td class="value">
				<form:checkbox path="sending.blockOnFail"/>
			</td>
		</tr>
		<tr>
			<td class="field">Timeout</td>
			<td class="value">
				<form:input path="sending.timeout"/>
				<span ref="sending.timeout" class="error"></span>
			</td>
		</tr>
	</tbody>
</table>

<h2>Template</h2>
<table class="fieldset tablesorter" >
	<thead>
		<tr>
			<th class="field">Name</th>
			<th>Value</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td class="field">Velocity Store Path</td>
			<td class="value">
				<form:input path="template.velocityStorePath"/>
				<span ref="template.velocityStorePath" class="error"></span>
			</td>
		</tr>
		<tr>
			<td class="field">Velocity Template Encoding</td>
			<td class="value">
				<form:input path="template.velocityEncoding"/>
				<span ref="template.velocityEncoding" class="error"></span>
			</td>
		</tr>
	</tbody>
</table>

<h2>Spam Checker</h2>
<table class="fieldset tablesorter" >
	<thead>
		<tr>
			<th class="field">Name</th>
			<th>Value</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td class="field">Host</td>
			<td class="value">
				<form:input path="spamChecker.host"/>
				<span ref="spamChecker.host" class="error"></span>
			</td>
		</tr>
		<tr>
			<td class="field">Port</td>
			<td class="value">
				<form:input path="spamChecker.port"/>
				<span ref="spamChecker.port" class="error"></span>
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
		<input type="submit" id="save_email_btn" value="Save" />
		</sec:authorize>
	</div>
</footer>
</form:form>

