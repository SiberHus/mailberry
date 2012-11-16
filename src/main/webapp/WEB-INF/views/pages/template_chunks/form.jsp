<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html lang="en">
<head>
	<%@include file="/WEB-INF/views/includes/head/res_standard.jsp" %>
	<script type="text/javascript">
	$(document).ready(function() {
		crud.baseUri='${PATH.templateChunks}';
		$('#cancel_btn').click(function(){
			window.location.href=crud.baseUri+'/';
		});
	});
    </script>
</head>
<body>
	<c:set var="pageTitle" value="Template Chunks"/>
	<%@include file="/WEB-INF/views/includes/body/header.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/secondary_bar.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/sidebar.jsp" %>
	<section id="main" class="column">
	<form:form id="main_form" method="post" action="${PATH.templateChunks }/save" modelAttribute="templateChunk">
	<%@include file="/WEB-INF/views/includes/body/form_error.jsp" %>
	<form:hidden path="id"/>
	<article class="module width_full">
		<header>
			<h3>
			<s:message code="view.createNew" text="Create New"/>
			<s:message code="entity.templateChunk" text="Template Chunk"/>
			</h3>
		</header>
		<div class="module_content">
			<fieldset>
				<label class="field">
					<s:message code="entity.templateChunk.name" text="Name"/>
					<span class="req">*</span>
				</label>
				<form:input id="template_chunk_name" path="name"/>
				<form:errors path="name"/>
			</fieldset>
			<fieldset>
				<label class="field">
					<s:message code="entity.templateChunk.value" text="Value"/>
				</label>
				<form:textarea id="template_chunk_value" path="value" rows="10"/>
			</fieldset>
			<fieldset>
				<label class="field">
					<s:message code="entity.templateChunk.status" text="Status"/>
				</label>
				<form:select id="template_chunk_status" path="status">
					<form:option value="ACT">
						<s:message code="entity.templateChunk.status.ACT" text="Active"/>
					</form:option>
					<form:option value="INA">
						<s:message code="entity.templateChunk.status.INA" text="Inactive"/>
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