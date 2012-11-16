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
		crud.baseUri='${PATH.mailAccounts}';
		$('#back_btn').click(function(){
			history.back();
		});
		$('#edit_btn').click(function(){
			crud.editObject('${mailAccount.id }');
		});
		$('#delete_btn').click(function(){
			<sec:authorize access="hasRole('ROLE_DEMO')">
			jprompt.demo();
			</sec:authorize>
			<sec:authorize access="!hasRole('ROLE_DEMO')">
			crud.deleteObject('${mailAccount.id }');
			</sec:authorize>
		});
		$('#cancel_btn').click(function(){
			crud.goToParentPage();
		});
	});
    </script>
</head>
<body>
	<c:set var="pageTitle" value="Mail Accounts"/>
	<%@include file="/WEB-INF/views/includes/body/header.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/secondary_bar.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/sidebar.jsp" %>
	
	<section id="main" class="column">
	<article class="module width_full">
		<header>
			<h3>
				<s:message code="entity.mailAccount" text="Mail Account"/>
				(&nbsp;#${mailAccount.id }&nbsp;)
			</h3>
		</header>
		<div class="module_content">
			<fieldset>
				<label class="field">
					<s:message code="entity.mailServer" text="Mail Server"/>
				</label>
				<div class="value">
					<s:message text="${mailAccount.mailServer.serverName }"/>
				</div>
			</fieldset>
			<fieldset style="float:left; width:49%; margin-right:1%;">
				<label class="field">
					<s:message code="entity.mailAccount.displayName" text="Display Name"/>
				</label>
				<div class="value">
					<s:message text="${mailAccount.displayName }"/>
				</div>
			</fieldset>
			<fieldset style="float:left; width:49%;">
				<label class="field">
					<s:message code="entity.mailAccount.email" text="Email"/>
				</label>
				<div class="value">
					<s:message text="${mailAccount.email }"/>
				</div>
			</fieldset>
			<fieldset>
				<label class="field">
					<s:message code="entity.mailAccount.description" text="Description"/>
				</label>
				<div class="value">
					<s:message text="${mailAccount.description }"/>
				</div>
			</fieldset>
			<fieldset>
				<label class="field">
					<s:message code="entity.mailAccount.status" text="Status"/>
				</label>
				<div class="value">
					<s:message code="entity.mailAccount.status.${mailAccount.status}" 
						text="${mailAccount.status}"/>
				</div>
			</fieldset>
			
			<fieldset style="float:left; width:49%; margin-right:1%;">
				<label class="field">
					<s:message code="entity.mailAccount.smtpAuthen" text="SMTP Authentication"/>
				</label>
				<table style="width:300px;margin-left:10px;">
					<tbody>
						<tr>
							<th style="text-align: left;"><s:message code="entity.mailAccount.authen.enabled" text="Enabled"/>?</th>
							<td>
								<c:if test="${mailAccount.smtpAuthen }">
									<img src="${ctx }/resources/images/icons/tick.png"/>
								</c:if>
								<c:if test="${not mailAccount.smtpAuthen }">
									<img src="${ctx }/resources/images/icons/cross.png"/>
								</c:if>
							</td>
						</tr>
						<tr>
							<th style="width:100px;text-align: left;"><s:message code="entity.mailAccount.smtpUsername" text="Username"/></th>
							<td><s:message text="${mailAccount.smtpUsername }"/></td>
						</tr>
						<tr>
							<th style="text-align: left;"><s:message code="entity.mailAccount.smtpPassword" text="Password"/></th>
							<td>******</td>
						</tr>
					</tbody>
				</table>
			</fieldset>
			
			<fieldset style="float:left; width:49%;">
				<label class="field">
					<s:message code="entity.mailAccount.pop3Authen" text="POP3 Authentication"/>
				</label>
				<table style="width:300px;margin-left:10px;">
					<tbody>
						<tr>
							<th style="text-align: left;"><s:message code="entity.mailAccount.authen.enabled" text="Enabled"/>?</th>
							<td>
								<c:if test="${mailAccount.pop3Authen }">
									<img src="${ctx }/resources/images/icons/tick.png"/>
								</c:if>
								<c:if test="${not mailAccount.pop3Authen }">
									<img src="${ctx }/resources/images/icons/cross.png"/>
								</c:if>
							</td>
						</tr>
						<tr>
							<th style="width:100px;text-align: left;"><s:message code="entity.mailAccount.pop3Username" text="Username"/></th>
							<td><s:message text="${mailAccount.pop3Username }"/></td>
						</tr>
						<tr>
							<th style="text-align: left;"><s:message code="entity.mailAccount.pop3Password" text="Password"/></th>
							<td>******</td>
						</tr>
					</tbody>
				</table>
			</fieldset>
			<div class="clear"></div>
			
			<c:set var="command" value="${mailAccount }"/>
			<%@include file="/WEB-INF/views/includes/body/view_audit_info.jsp" %>
			<div class="clear"></div>
		</div>
		<%@include file="/WEB-INF/views/includes/body/view_footer.jsp" %>
	</article>
	<div class="spacer"></div>
	</section>
	
</body>

</html>