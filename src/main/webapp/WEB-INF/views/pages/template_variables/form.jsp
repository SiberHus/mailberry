<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html lang="en">
<head>
	<%@include file="/WEB-INF/views/includes/head/res_standard.jsp" %>
	<script type="text/javascript">
	$(document).ready(function() {
		crud.baseUri='${PATH.templateVariables}';
		$('#cancel_btn').click(function(){
			window.location.href=crud.baseUri+'/';
		});
	});
    </script>
</head>
<body>
	<c:set var="pageTitle" value="Template Variables"/>
	<%@include file="/WEB-INF/views/includes/body/header.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/secondary_bar.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/sidebar.jsp" %>
	<section id="main" class="column">
	<form:form id="main_form" method="post" action="${PATH.templateVariables }/save" modelAttribute="templateVariable">
	<%@include file="/WEB-INF/views/includes/body/form_error.jsp" %>
	<form:hidden path="id"/>
	<article class="module width_full">
		<header>
			<h3>
			<s:message code="view.createNew" text="Create New"/>
			<s:message code="entity.templateVariable" text="Template Variable"/>
			</h3>
		</header>
		<div class="module_content">
			<fieldset>
				<label class="field">
					<s:message code="entity.templateVariable.name" text="Name"/>
					<span class="req">*</span>
				</label>
				<form:input id="template_variable_name" path="name"/>
				<form:errors path="name"/>
			</fieldset>
			<fieldset>
				<label class="field">
					<s:message code="entity.templateVariable.value" text="Value"/>
				</label>
				<form:input id="template_variable_value" path="value"/>
			</fieldset>
			<fieldset style="width:49%;">
				<label class="field">
					<s:message code="entity.templateVariable.status" text="Status"/>
				</label>
				<form:select id="template_variable_status" path="status">
					<form:option value="ACT">
						<s:message code="entity.templateVariable.status.ACT" text="Active"/>
					</form:option>
					<form:option value="INA">
						<s:message code="entity.templateVariable.status.INA" text="Inactive"/>
					</form:option>
				</form:select>
			</fieldset>
			<div class="clear"></div>
		</div>
		<footer>
			<div class="submit_link">
				<input type="submit" value="Save" />
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