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
		$('#back_btn').click(function(){
			history.back();
		});
		$('#edit_btn').click(function(){
			crud.editObject('${mailServer.id }');
		});
		$('#delete_btn').click(function(){
			<sec:authorize access="hasRole('ROLE_DEMO')">
			jprompt.demo();
			</sec:authorize>
			<sec:authorize access="!hasRole('ROLE_DEMO')">
			crud.deleteObject('${mailServer.id }');
			</sec:authorize>
		});
		$('#cancel_btn').click(function(){
			crud.goToParentPage();
		});
	});
    </script>
</head>
<body>
	<c:set var="pageTitle" value="SMTP Profiles"/>
	<%@include file="/WEB-INF/views/includes/body/header.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/secondary_bar.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/sidebar.jsp" %>
	
	<section id="main" class="column">
	<article class="module width_full">
		<header>
			<h3>
				<s:message code="entity.mailServer" text="SMTP Profile"/>
				(&nbsp;#${mailServer.id }&nbsp;)
			</h3>
		</header>
		<div class="module_content">
			<fieldset>
				<label class="field">
					<s:message code="entity.mailServer.serverName" text="Server Name"/>
				</label>
				<div class="value">
					<s:message text="${mailServer.serverName }"/>
					<strong>
						&nbsp;&nbsp;
						[<c:if test="${mailServer.publicServer }">
							<s:message code="entity.mailServer.publicServer" text="Public Server"/>
						</c:if>
						<c:if test="${not mailServer.publicServer }">
							<s:message code="entity.mailServer.privateServer" text="Private Server"/>
						</c:if>]
					</strong>
				</div>
			</fieldset>
			<fieldset>
				<label class="field">
					<s:message code="entity.mailServer.description" text="Description"/>
				</label>
				<div class="value">
					<s:message text="${mailServer.description }"/>
				</div>
			</fieldset>
			<fieldset>
				<label class="field">
					<s:message code="entity.mailServer.status" text="Status"/>
				</label>
				<div class="value">
					<s:message code="entity.mailServer.status.${mailServer.status}" 
						text="${mailServer.status}"/>
				</div>
			</fieldset>
			
			<fieldset style="float:left; width:49%; margin-right:1%">
				<label class="field">
					<s:message code="entity.mailServer.smtpServer" text="SMTP Server Host"/>
				</label>
				<table style="width:300px;margin-left:10px;">
					<tbody>
						<tr>
							<th style="width:150px;text-align: left;">Host name/IP</th>
							<td><s:message text="${mailServer.smtpServer }"/></td>
						</tr>
						<tr>
							<th style="text-align: left;"><s:message code="entity.mailServer.smtpPort" text="Port"/></th>
							<td>${mailServer.smtpPort }</td>
						</tr>
						<tr>
							<th style="text-align: left;"><s:message code="entity.mailServer.smtpConnectionSecurity" text="Connection Security"/></th>
							<td>${mailServer.smtpConnectionSecurity }</td>
						</tr>
					</tbody>
				</table>
			</fieldset>
			<fieldset>
				<label class="field">
					<s:message code="entity.mailServer.smtpProperties" text="SMTP Connection Properties"/>
				</label>
				<div class="value" style="margin-bottom: 10px;">
					<label for="smtp_timeout">
						<strong><s:message code="entity.mailServer.smtpTimeout" text="Timeout"/></strong>
					</label>
					<c:if test="${empty mailServer.smtpTimeout }">
						infinite
					</c:if>
					<c:if test="${not empty mailServer.smtpTimeout }">
						${mailServer.smtpTimeout }
					</c:if>
					seconds
				</div>
				<textarea rows="8" style="width:550px;" readonly="true">
${mailServer.smtpProperties }
				</textarea>
			</fieldset>
			
			<fieldset style="float:left; width:49%; margin-right:1%">
				<label class="field">
					<s:message code="entity.mailServer.pop3Server" text="POP3 Server Host"/>
				</label>
				<table style="width:300px;margin-left:10px;">
					<tbody>
						<tr>
							<th style="width:150px;text-align: left;">Host name/IP</th>
							<td><s:message text="${mailServer.pop3Server }"/></td>
						</tr>
						<tr>
							<th style="text-align: left;"><s:message code="entity.mailServer.pop3Port" text="Port"/></th>
							<td>${mailServer.pop3Port }</td>
						</tr>
						<tr>
							<th style="text-align: left;"><s:message code="entity.mailServer.pop3ConnectionSecurity" text="Connection Security"/></th>
							<td>${mailServer.pop3ConnectionSecurity }</td>
						</tr>
					</tbody>
				</table>
			</fieldset>
			<fieldset>
				<label class="field">
					<s:message code="entity.mailServer.pop3Properties" text="POP3 Connection Properties"/>
				</label>
				<div class="value" style="margin-bottom: 10px;">
					<label for="pop3_timeout">
						<strong><s:message code="entity.mailServer.pop3Timeout" text="Timeout"/></strong>
					</label>
					<c:if test="${empty mailServer.pop3Timeout }">
						infinite
					</c:if>
					<c:if test="${not empty mailServer.pop3Timeout }">
						${mailServer.pop3Timeout }
					</c:if>
					seconds
				</div>
				<textarea rows="8" style="width:550px;" readonly="true">
${mailServer.pop3Properties }
				</textarea>
			</fieldset>
			
			<c:set var="command" value="${mailServer }"/>
			<%@include file="/WEB-INF/views/includes/body/view_audit_info.jsp" %>
			<div class="clear"></div>
		</div>
		<%@include file="/WEB-INF/views/includes/body/view_footer.jsp" %>
	</article>
	<div class="spacer"></div>
	</section>
	
</body>

</html>