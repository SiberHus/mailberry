<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<!doctype html>
<html lang="en">
<head>
	<%@include file="/WEB-INF/views/includes/head/res_standard.jsp" %>
	<script type="text/javascript">
	$(document).ready(function() {
		crud.baseUri='${PATH.mailServers}';
		$('#cancel_btn').click(function(){
			window.location.href=crud.baseUri+'/';
		});
	});
    </script>
</head>
<body>
	<c:set var="pageTitle" value="Mail Servers"/>
	<%@include file="/WEB-INF/views/includes/body/header.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/secondary_bar.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/sidebar.jsp" %>
	<section id="main" class="column">
	<form:form id="main_form" method="post" action="${PATH.mailServers }/save" modelAttribute="mailServer">
	<%@include file="/WEB-INF/views/includes/body/form_error.jsp" %>
	<form:hidden path="id"/>
	<article class="module width_full">
		<header>
			<h3>
			<s:message code="view.createNew" text="Create New"/>
			<s:message code="entity.mailServer" text="Mail Server"/>
			</h3>
		</header>
		<div class="module_content">
			<fieldset>
				<label class="field">
					<s:message code="entity.mailServer.serverName" text="Server Name"/>
					<span class="req">*</span>
				</label>
				<form:input path="serverName" style="width:300px;"/>
				<form:checkbox id="public_server" path="publicServer"/>
				<label for="public_server">
					<s:message code="entity.mailServer.publicServer" text="Public Server"/>
				</label>
				<form:errors path="serverName" cssClass="error"/>
			</fieldset>
			<fieldset>
				<label class="field">
					<s:message code="entity.mailServer.description" text="Description"/>
				</label>
				<form:input path="description" rows="10"/>
			</fieldset>
			<fieldset>
				<label class="field">
					<s:message code="entity.mailServer.status" text="Status"/>
					<span class="req">*</span>
				</label>
				<form:select id="profile_status" path="status" style="width:310px;">
					<form:option value="ACT">
						<s:message code="entity.mailServer.status.ACT" text="Active"/>
					</form:option>
					<form:option value="INA">
						<s:message code="entity.mailServer.status.INA" text="Inactive"/>
					</form:option>
				</form:select>
			</fieldset>
			
			<fieldset>
				<label class="field">
					<s:message code="entity.mailServer.smtpServer" text="SMTP Server Host"/>
					<span class="req">*</span>
				</label>
				<form:input path="smtpServer" style="width:300px;"/>
				
				<label for="smtp_port">
					<s:message code="entity.mailServer.smtpPort" text="Port"/>
					<span class="req">*</span>
				</label>
				<form:input id="smtp_port" path="smtpPort" style="width:80px;"/>
				<div class="value" style="margin-top:10px;">
					<s:message text="Connection Security"/>: &nbsp;
					<form:radiobutton id="smtp_consec_none" path="smtpConnectionSecurity" value="NONE"/>
					<label for="smtp_consec_none">
						<s:message code="entity.mailServer.smtpConnectionSecurity.NONE" text="NONE"/>
					</label>
					&nbsp;&nbsp;
					<form:radiobutton id="smtp_consec_ssl" path="smtpConnectionSecurity" value="SSL"/>
					<label for="smtp_consec_ssl">
						<s:message code="entity.mailServer.smtpConnectionSecurity.SSL" text="SSL"/>
					</label>
					&nbsp;&nbsp;
					<form:radiobutton id="consec_starttls" path="smtpConnectionSecurity" value="STARTTLS"/>
					<label for="smtp_consec_starttls">
						<s:message code="entity.mailServer.smtpConnectionSecurity.STARTTLS" text="STARTTLS"/>
					</label>
				</div>
				<form:errors path="smtpServer" cssClass="error"/>
				<form:errors path="smtpPort" cssClass="error"/>
			</fieldset>
			<fieldset>
				<label class="field">
					<s:message code="entity.mailServer.smtpProperties" text="SMTP Connection Properties"/>
				</label>
				<div class="value" style="margin-bottom: 10px;">
					<label for="smtp_timeout">
						<strong><s:message code="entity.mailServer.smtpTimeout" text="Timeout"/></strong>
					</label>
					<form:input id="smtp_timeout" path="smtpTimeout" style="width:200px;"/>
					<span>seconds</span>
				</div>
				<form:textarea path="smtpProperties" rows="8" style="width:550px;"/>
			</fieldset>
			
			<fieldset>
				<label class="field">
					<s:message code="entity.mailServer.pop3Server" text="POP3 Server Host"/>
					<span class="req">*</span>
				</label>
				<form:input path="pop3Server" style="width:300px;"/>
				
				<label for="pop3_port">
					<s:message code="entity.mailServer.pop3Port" text="Port"/>
					<span class="req">*</span>
				</label>
				<form:input id="pop3_port" path="pop3Port" style="width:80px;"/>
				<div class="value" style="margin-top:10px;">
					<s:message text="Connection Security"/>: &nbsp;
					<form:radiobutton id="pop3_consec_none" path="pop3ConnectionSecurity" value="NONE"/>
					<label for="pop3_consec_none">
						<s:message code="entity.mailServer.pop3ConnectionSecurity.NONE" text="NONE"/>
					</label>
					&nbsp;&nbsp;
					<form:radiobutton id="pop3_consec_ssl" path="pop3ConnectionSecurity" value="SSL"/>
					<label for="pop3_consec_ssl">
						<s:message code="entity.mailServer.pop3ConnectionSecurity.SSL" text="SSL"/>
					</label>
					&nbsp;&nbsp;
					<form:radiobutton id="consec_starttls" path="pop3ConnectionSecurity" value="STARTTLS"/>
					<label for="pop3_consec_starttls">
						<s:message code="entity.mailServer.pop3ConnectionSecurity.STARTTLS" text="STARTTLS"/>
					</label>
				</div>
				<form:errors path="pop3Server" cssClass="error"/>
				<form:errors path="pop3Port" cssClass="error"/>
			</fieldset>
			<fieldset>
				<label class="field">
					<s:message code="entity.mailServer.pop3Properties" text="POP3 Connection Properties"/>
				</label>
				<div class="value" style="margin-bottom: 10px;">
					<label for="pop3_timeout">
						<strong><s:message code="entity.mailServer.pop3Timeout" text="Timeout"/></strong>
					</label>
					<form:input id="pop3_timeout" path="pop3Timeout" style="width:200px;"/>
					<span>seconds</span>
				</div>
				<form:textarea path="pop3Properties" rows="8" style="width:550px;"/>
			</fieldset>
			<div class="clear"></div>
		</div>
		
		<footer>
			<div class="submit_link">
				<sec:authorize access="hasRole('ROLE_DEMO')">
				<span>Demo user cannot save this data</span>
				</sec:authorize>
				<sec:authorize access="!hasRole('ROLE_DEMO')">
				<input type="submit" value="Save" />
				</sec:authorize>
				&nbsp;
				<input type="button" id="cancel_btn" value="Cancel" />
			</div>
		</footer>
	</article>
	</form:form>
	<div class="spacer"></div>
	</section>
	
</body>

</html>