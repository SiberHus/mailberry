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
		$('#cancel_btn').click(function(){
			window.location.href=crud.baseUri+'/';
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
	<form:form id="main_form" method="post" action="${PATH.dashboard }/widgets/save" modelAttribute="dashboardWidget">
	<%@include file="/WEB-INF/views/includes/body/form_error.jsp" %>
	<form:hidden path="id"/>
	<article class="module width_full">
		<header>
			<h3>
			<s:message code="view.createNew" text="Create New"/>
			<s:message code="entity.dashboardWidget" text="Widget"/>
			</h3>
		</header>
		<div class="module_content">
			<fieldset style="width:49%;padding-right:2%;">
				<label class="field">
					<s:message code="entity.dashboardWidget.name" text="Widget Name"/>
					<span class="req">*</span>
				</label>
				<form:input id="widget_name" path="name"/>
				<div class="value" style="margin-top:10px;">
					<form:checkbox id="widget_admin_only" path="adminOnly"/>
					<label for="widget_admin_only">
						<s:message code="entity.dashboardWidget.adminOnly" text="Admin Only"/>
					</label>
				</div>
				<form:errors path="name"/>
			</fieldset>
			<div class="clear"></div>
			<fieldset>
				<label class="field">
					<s:message code="entity.dashboardWidget.contentUri" text="Content URI"/>
					<span class="req">*</span>
				</label>
				<form:input id="widget_content_uri" path="contentUri"/>
				<form:errors path="contentUri"/>
			</fieldset>
			<fieldset>
				<label class="field">
					<s:message code="entity.dashboardWidget.description" text="Description"/>
				</label>
				<form:input id="widget_description" path="description"/>
				<form:errors path="description"/>
			</fieldset>
			<fieldset style="float:left;width:49%;margin-right:1%;">
				<label class="field">
					<s:message code="entity.dashboardWidget.status" text="Status"/>
					<span class="req">*</span>
				</label>
				<form:select id="widget_status" path="status">
					<form:option value="ACT">
						<s:message code="entity.dashboardWidget.status.ACT" text="Active"/>
					</form:option>
					<form:option value="INA">
						<s:message code="entity.dashboardWidget.status.INA" text="Inactive"/>
					</form:option>
				</form:select>
				<form:errors path="status"/>
			</fieldset>
			<fieldset style="float:left;width:49%;">
				<label class="field">
					<s:message code="entity.dashboardWidget.defaultPosition" text="Default Position"/>
				</label>
				<form:select id="widget_default_position" path="defaultPosition">
					<form:option value="Center">
						<s:message code="entity.dashboardWidget.defaultPosition.center" text="Center"/>
					</form:option>
					<form:option value="Left">
						<s:message code="entity.dashboardWidget.defaultPosition.left" text="Left"/>
					</form:option>
					<form:option value="Right">
						<s:message code="entity.dashboardWidget.defaultPosition.right" text="Right"/>
					</form:option>
				</form:select>
				<form:errors path="defaultPosition"/>
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