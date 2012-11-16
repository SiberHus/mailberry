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
	});
    </script>
</head>
<body>
	<c:set var="pageTitle" value="Users"/>
	<%@include file="/WEB-INF/views/includes/body/header.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/secondary_bar.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/sidebar.jsp" %>
	<section id="main" class="column">
	<form id="main_form" method="post" action="${PATH.users }/pwd/${userId }">
	<%@include file="/WEB-INF/views/includes/body/message.jsp" %>
	<input type="hidden" name="userId" value="${userId }"/>
	<article class="module width_full">
		<header>
			<h3>
			Chagen Password
			</h3>
		</header>
		<div class="module_content">
			
			<fieldset style="float:left;width:49%;">
				<label class="field">
					Old Password
					<span class="req">*</span>
				</label>
				<input type="password" name="oldPassword" style="width:300px;"/>
			</fieldset>
			<div class="clear"></div>
			<fieldset style="float:left;width:49%;">
				<label class="field">
					<s:message code="entity.user.password" text="Password"/>
					<span class="req">*</span>
				</label>
				<input type="password" name="newPassword" style="width:300px;"/>
			</fieldset>
			<div class="clear"></div>
			<fieldset style="float:left;width:49%;">
				<label class="field">
					<s:message code="entity.user.password2" text="Confirm Password"/>
					<span class="req">*</span>
				</label>
				<input type="password" name="confirmPassword" style="width:300px;"/>
			</fieldset>
			<div class="clear"></div>
		</div>
		<footer>
			<div class="submit_link">
				<sec:authorize access="hasRole('ROLE_DEMO')">
				<span>Demo user cannot save this data</span>
				</sec:authorize>
				<sec:authorize access="!hasRole('ROLE_DEMO')">
				<input type="submit" value="Update" />
				</sec:authorize>
				&nbsp;
				<input type="button" id="cancel_btn" value="Cancel" />
			</div>
		</footer>
	</article>
	</form>
	<div class="spacer"></div>
	</section>
	
</body>

</html>