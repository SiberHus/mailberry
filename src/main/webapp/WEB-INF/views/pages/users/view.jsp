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
		$('#back_btn').click(function(){
			history.back();
		});
		$('#edit_btn').click(function(){
			crud.editObject('${user.id }');
		});
		$('#delete_btn').click(function(){
			<sec:authorize access="hasRole('ROLE_DEMO')">
			jprompt.demo();
			</sec:authorize>
			<sec:authorize access="!hasRole('ROLE_DEMO')">
			crud.deleteObject('${user.id }');
			</sec:authorize>
		});
		$('#cancel_btn').click(function(){
			crud.goToParentPage();
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
	<article class="module width_full">
		<header>
			<h3>
				<s:message code="entity.user" text="User"/>
				(&nbsp;#${user.id }&nbsp;)
			</h3>
		</header>
		<div class="module_content">
			<fieldset style="float:left;width:49%;margin-right: 1%;">
				<label class="field">
					<s:message code="entity.user.username" text="Username"/>
				</label>
				<div class="value">
					<s:message text="${user.username }"/>
				</div>
			</fieldset>
			<fieldset style="float:left;width:49%;">
				<label class="field">
					<s:message code="entity.user.email" text="Email"/>
				</label>
				<div class="value">
					<s:message text="${user.email }"/>
				</div>
			</fieldset>
			<div class="clear"></div>
			<fieldset>
				<label class="field">
					<s:message code="entity.user.password" text="Password"/>
				</label>
				<div class="value">
					<a href="${PATH.users }/pwd/${user.id}">Change password</a>
				</div>
			</fieldset>
			<fieldset style="float:left;width:49%;margin-right: 1%;">
				<label class="field">
					<s:message code="entity.user.firstName" text="First Name"/>
				</label>
				<div class="value">
					<s:message text="${user.firstName }"/>
				</div>
			</fieldset>
			<fieldset style="float:left;width:49%;">
				<label class="field">
					<s:message code="entity.user.lastName" text="Last Name"/>
				</label>
				<div class="value">
					<s:message text="${user.lastName }"/>
				</div>
			</fieldset>
			<div class="clear"></div>
			
			<fieldset>
				<label class="field">
					<s:message code="entity.user.enabled" text="Enabled"/>
				</label>
				<div class="value">
					<label class="field">
						<c:if test="${user.enabled }">
							<img src="${ctx }/resources/images/icons/tick.png"/>
						</c:if>
						<c:if test="${not user.enabled }">
							<img src="${ctx }/resources/images/icons/cross.png"/>
						</c:if>
						<s:message code="entity.user.authorities" text="Authorities"/>
					</label>
					<ul>
					<c:forEach var="auth" items="${user.authorities }">
						<li>${auth.authority }</li>
					</c:forEach>
					</ul>
				</div>
			</fieldset>
			
			<fieldset style="float:left;width:49%;margin-right: 1%;">
				<label class="field">
					<s:message code="entity.user.apiKey" text="API Key"/>
				</label>
				<div class="value">
					<c:if test="${_USER.id eq user.id}">
						<s:message text="${user.apiKey }"/>
					</c:if>
					<c:if test="${_USER.id ne user.id}">
						********************************
					</c:if>
				</div>
			</fieldset>
			<fieldset style="float:left;width:49%;">
				<label class="field">
					<s:message code="entity.user.apiClientIp" text="API Client IP"/>
				</label>
				<div class="value">
					<s:message text="${user.apiClientIp }"/>
				</div>
			</fieldset>
			<div class="clear"></div>
			
			<c:set var="command" value="${user }"/>
			<%@include file="/WEB-INF/views/includes/body/view_audit_info.jsp" %>
			<div class="clear"></div>
		</div>
		<footer>
			<security:authorize ifAllGranted="ROLE_ADMIN">
			<div class="back_link">
				<input type="button" id="back_btn" value="Back"/>
			</div>
			</security:authorize>
			<div class="submit_link">
				<input type="button" id="edit_btn" value="Edit" />
				&nbsp;
				<security:authorize ifAllGranted="ROLE_ADMIN">
				<input type="button" id="delete_btn" value="Delete" />
				&nbsp;
				<input type="button" id="cancel_btn" value="Cancel" />
				</security:authorize>
			</div>
		</footer>
	</article>
	<div class="spacer"></div>
	</section>
	
</body>

</html>