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
		crud.baseUri='${PATH.dashboard}/widgets';
		$('#back_btn').click(function(){
			history.back();
		});
		$('#edit_btn').click(function(){
			crud.editObject('${dashboardWidget.id }');
		});
		$('#delete_btn').click(function(){
			<sec:authorize access="hasRole('ROLE_DEMO')">
			jprompt.demo();
			</sec:authorize>
			<sec:authorize access="!hasRole('ROLE_DEMO')">
			crud.deleteObject('${dashboardWidget.id }');
			</sec:authorize>
		});
		$('#cancel_btn').click(function(){
			crud.goToParentPage();
		});
	});
    </script>
</head>
<body>
	<c:set var="pageTitle" value="Dashboard Widgets"/>
	<%@include file="/WEB-INF/views/includes/body/header.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/secondary_bar.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/sidebar.jsp" %>
	
	<section id="main" class="column">
	<article class="module width_full">
		<header>
			<h3>
				<s:message code="entity.dashboardWidget" text="Widget"/>
				(&nbsp;#${dashboardWidget.id }&nbsp;)
			</h3>
		</header>
		<div class="module_content">
			<fieldset>
				<label class="field">
					<s:message code="entity.dashboardWidget.name" text="Widget Name"/>
				</label>
				<div class="value">
					<s:message text="${dashboardWidget.name }"/>
				</div>
			</fieldset>
			<fieldset>
				<label class="field">
					<s:message code="entity.dashboardWidget.contentUri" text="Content URI"/>
				</label>
				<div class="value">
					<s:message text="${dashboardWidget.contentUri }"/>
				</div>
			</fieldset>
			<fieldset>
				<label class="field">
					<s:message code="entity.dashboardWidget.description" text="Description"/>
				</label>
				<div class="value">
					<s:message text="${dashboardWidget.description }"/>
				</div>
			</fieldset>
			
			<c:set var="command" value="${templateVariable }"/>
			<%@include file="/WEB-INF/views/includes/body/view_audit_info.jsp" %>
			<div class="clear"></div>
		</div>
		<%@include file="/WEB-INF/views/includes/body/view_footer.jsp" %>
	</article>
	<div class="spacer"></div>
	</section>
	
</body>

</html>