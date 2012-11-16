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
		$('#cancel_btn').click(function(){
			window.location.href=crud.baseUri+'/';
		});
		$('#smtp_authen').click(function(){
			var checked = $('input[name=smtpAuthen]:checked').val();
			if(checked){
				$('#smtp_account_div').show();
			}else{
				$('#smtp_account_div').hide();
			}
		});
		<c:if test="${mailAccount.smtpAuthen}">
		$('#smtp_account_div').show();
		</c:if>
		
		$('#pop3_authen').click(function(){
			var checked = $('input[name=pop3Authen]:checked').val();
			if(checked){
				$('#pop3_account_div').show();
			}else{
				$('#pop3_account_div').hide();
			}
		});
		<c:if test="${mailAccount.pop3Authen}">
		$('#pop3_account_div').show();
		</c:if>
	});
    </script>
</head>
<body>
	<c:set var="pageTitle" value="Mail Accounts"/>
	<%@include file="/WEB-INF/views/includes/body/header.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/secondary_bar.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/sidebar.jsp" %>
	<section id="main" class="column">
	<form:form id="main_form" method="post" action="${PATH.mailAccounts }/save" modelAttribute="mailAccount">
	<%@include file="/WEB-INF/views/includes/body/form_error.jsp" %>
	<form:hidden path="id"/>
	<article class="module width_full">
		<header>
			<h3>
			<s:message code="view.createNew" text="Create New"/>
			<s:message code="entity.mailAccount" text="Mail Account"/>
			</h3>
		</header>
		<div class="module_content">
			<fieldset style="width:49%;">
				<label class="field">
					<s:message code="entity.mailServer" text="Mail Server"/>
					<span class="req">*</span>
				</label>
				<form:select path="mailServer.id">
					<form:options items="${mailServers }" itemLabel="serverName" itemValue="id"/>
				</form:select>
				<form:errors path="mailServer.id" cssClass="error"/>
			</fieldset>
			<fieldset style="float:left; width:49%; margin-right:1%;">
				<label class="field">
					<s:message code="entity.mailAccount.displayName" text="Display Name"/>
					<span class="req">*</span>
				</label>
				<form:input path="displayName" style="width:300px;"/>
				<form:errors path="displayName" cssClass="error"/>
			</fieldset>
			<fieldset style="float:left; width:49%;">
				<label class="field">
					<s:message code="entity.mailAccount.email" text="Email"/>
					<span class="req">*</span>
				</label>
				<form:input path="email" style="width:300px;"/>
				<form:errors path="email" cssClass="error"/>
			</fieldset>
			<div class="clear"></div>
			
			<fieldset>
				<label class="field">
					<s:message code="entity.mailAccount.description" text="Description"/>
				</label>
				<form:input path="description" rows="10"/>
			</fieldset>
			<fieldset>
				<label class="field">
					<s:message code="entity.mailAccount.status" text="Status"/>
					<span class="req">*</span>
				</label>
				<form:select id="profile_status" path="status" style="width:310px;">
					<form:option value="ACT">
						<s:message code="entity.mailAccount.status.ACT" text="Active"/>
					</form:option>
					<form:option value="INA">
						<s:message code="entity.mailAccount.status.INA" text="Inactive"/>
					</form:option>
				</form:select>
			</fieldset>
			
			<fieldset>
				<label class="field">
					<s:message code="entity.mailAccount.smtpAuthen" text="SMTP Authentication"/>
				</label>
				<div class="value">
					<form:checkbox id="smtp_authen" path="smtpAuthen"/>
					<label for="smtp_authen">
						<s:message code="entity.mailAccount.authen.enabled" text="Enabled"/>
					</label>
					<div id="smtp_account_div" style="display:none; margin:10px 0px 0px 5px;">
						<label for="smtp_username">
							<s:message code="entity.mailAccount.smtpUsername" text="Username"/>
						</label>
						<form:input id="smtp_username" path="smtpUsername" style="width:150px;"/>
						
						<label for="smtp_password">
							<s:message code="entity.mailAccount.smtpPassword" text="Password"/>
						</label>
						<form:password id="smtp_password" path="smtpPassword" style="width:150px;"/>
					</div>
				</div>
				<form:errors path="smtpUsername"/>
				<form:errors path="smtpPassword"/>
			</fieldset>
			
			
			<fieldset>
				<label class="field">
					<s:message code="entity.mailAccount.pop3Authen" text="POP3 Authentication"/>
				</label>
				<div class="value">
					<form:checkbox id="pop3_authen" path="pop3Authen"/>
					<label for="pop3_authen">
						<s:message code="entity.mailAccount.authen.enabled" text="Enabled"/>
					</label>
					<div id="pop3_account_div" style="display:none; margin:10px 0px 0px 5px;">
						<label for="pop3_username">
							<s:message code="entity.mailAccount.pop3Username" text="Username"/>
						</label>
						<form:input id="pop3_username" path="pop3Username" style="width:150px;"/>
						
						<label for="pop3_password">
							<s:message code="entity.mailAccount.pop3Password" text="Password"/>
						</label>
						<form:password id="pop3_password" path="pop3Password" style="width:150px;"/>
					</div>
				</div>
				<form:errors path="pop3Username"/>
				<form:errors path="pop3Password"/>
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