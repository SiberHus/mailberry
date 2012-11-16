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
		crud.baseUri='${PATH.users}';
		$('#cancel_btn').click(function(){
			window.location.href=crud.baseUri+'/';
		});
		<c:forEach var="auth" items="${user.authorities}">
		$('input[value=${auth.authority}]').attr('checked', 'checked');
		</c:forEach>
		$('#gen_key').click(function(){
			jprompt.confirm('Are you sure you want to generate a new API Key?', function(){
				$.post(crud.baseUri+'/generateApiKey', function(data){
					$('#api_key').val(data);
					$('#api_key_span').text(data);
				});
			});
		});
	});
    </script>
</head>
<body>
	<c:set var="pageTitle" value="Users"/>
	<%@include file="/WEB-INF/views/includes/body/header.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/secondary_bar.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/sidebar.jsp" %>
	<section id="main" class="column">
	<form:form id="main_form" method="post" action="${PATH.users }/save" modelAttribute="user">
	<%@include file="/WEB-INF/views/includes/body/form_error.jsp" %>
	<form:hidden path="id"/>
	<article class="module width_full">
		<header>
			<h3>
			<s:message code="view.createNew" text="Create New"/>
			<s:message code="entity.user" text="User"/>
			</h3>
		</header>
		<div class="module_content">
			<fieldset style="float:left;width:49%;margin-right: 1%;">
				<label class="field">
					<s:message code="entity.user.username" text="Username"/>
					<span class="req">*</span>
				</label>
				<c:if test="${empty user.id }">
					<form:input path="username" style="width:300px;"/>
					<form:errors path="username" cssClass="error"/>
				</c:if>
				<c:if test="${not empty user.id }">
					<div class="value">
						<form:hidden path="username" />
						<s:message text="${user.username }"/>
					</div>
				</c:if>
			</fieldset>
			<fieldset style="float:left;width:49%;">
				<label class="field">
					<s:message code="entity.user.email" text="Email"/>
					<span class="req">*</span>
				</label>
				<form:input path="email" style="width:300px;"/>
				<form:errors path="email" cssClass="error"/>
			</fieldset>
			<div class="clear"></div>
			
			<c:if test="${empty user.id }">
				<fieldset style="float:left;width:49%;margin-right: 1%;">
					<label class="field">
						<s:message code="entity.user.password" text="Password"/>
						<span class="req">*</span>
					</label>
					<form:password path="password" style="width:300px;"/>
					<form:errors path="password" cssClass="error"/>
				</fieldset>
				<div class="clear"></div>
				<fieldset style="float:left;width:49%;">
					<label class="field">
						<s:message code="entity.user.password2" text="Confirm Password"/>
						<span class="req">*</span>
					</label>
					<form:password path="password2" style="width:300px;"/>
					<form:errors path="password2" cssClass="error"/>
				</fieldset>
				<div class="clear"></div>
			</c:if>
			<c:if test="${not empty user.id }">
				<fieldset>
					<label class="field">
						<s:message code="entity.user.password" text="Password"/>
					</label>
					<div class="value">
						<a href="${PATH.users }/pwd/${user.id}">Change password</a>
					</div>
				</fieldset>
			</c:if>
			<fieldset style="float:left;width:49%;margin-right: 1%;">
				<label class="field">
					<s:message code="entity.user.firstName" text="First Name"/>
					<span class="req">*</span>
				</label>
				<form:input path="firstName" style="width:300px;"/>
				<form:errors path="firstName" cssClass="error"/>
			</fieldset>
			<fieldset style="float:left;width:49%;">
				<label class="field">
					<s:message code="entity.user.lastName" text="Last Name"/>
					<span class="req">*</span>
				</label>
				<form:input path="lastName" style="width:300px;"/>
				<form:errors path="lastName" cssClass="error"/>
			</fieldset>
			<div class="clear"></div>
			
			<security:authorize ifAllGranted="ROLE_ADMIN">
			<fieldset>
				<label class="field">
					<s:message code="entity.user.enabled" text="Enabled"/>
				</label>
				<div class="value">
					<div style="margin-bottom: 10px;">
						<form:checkbox id="user_enabled" path="enabled"/>
						<label for="user_enabled">
							<s:message code="entity.user.enabled" text="Enabled"/>
						</label>
					</div>
					<label class="field">
						<s:message code="entity.user.authorities" text="Authorities"/>
					</label>
					<ul style="margin-top: 0px;">
						<li>
							<input type="checkbox" id="auth_user" name="authorities" value="ROLE_USER"/>&nbsp;
							<label for="auth_user">
								<s:message code="entity.authority.user" text="User"/>
							</label>
						</li>
						<li>
							<input type="checkbox" id="auth_admin" name="authorities" value="ROLE_ADMIN"/>&nbsp;
							<label for="auth_admin">
								<s:message code="entity.authority.admin" text="Admin"/>
							</label>
						</li>
					</ul>
				</div>
			</fieldset>
			</security:authorize>
			
			<fieldset style="float:left;width:49%;margin-right: 1%;">
				<label class="field">
					<s:message code="entity.user.apiKey" text="API Key"/>
				</label>
				<div class="value">
					<form:hidden id="api_key" path="apiKey"/>
					<c:set var="showApiKey" value="${(empty user.id) or (_USER.id eq user.id)}"/>
					<c:if test="${showApiKey}">
						<span id="api_key_span"><s:message text="${user.apiKey }"/></span>
						<a id="gen_key" style="margin-left:10px; font-weight: bold;cursor: pointer;">Generate New Key</a>
					</c:if>
					<c:if test="${not showApiKey}">
						********************************
					</c:if>
				</div>
			</fieldset>
			<fieldset style="float:left;width:49%;">
				<label class="field">
					<s:message code="entity.user.apiClientIp" text="API Client IP"/>
				</label>
				<div class="value">
					<form:input path="apiClientIp"/>
				</div>
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