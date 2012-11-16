<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ckeditor" uri="http://www.siberhus.com/taglibs/ckeditor"%>
<!doctype html>
<html lang="en">
<head>
	<%@include file="/WEB-INF/views/includes/head/res_standard.jsp" %>
	<script type="text/javascript">
	$(document).ready(function() {
		crud.baseUri='${PATH.customPages}';
		$('#editors').tabs();
		$('#main_form').submit(function(){
			$('#content').val(CKEDITOR.instances.content_.getData());
		});
		$('#cancel_btn').click(function(){
			window.location.href='${PATH.customPages}/';
		});
	});
    </script>
</head>
<body>
	<c:set var="pageTitle" value="Custom Pages"/>
	<%@include file="/WEB-INF/views/includes/body/header.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/secondary_bar.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/sidebar.jsp" %>
	<section id="main" class="column">
	<form:form id="main_form" method="post" action="${PATH.customPages }/save" modelAttribute="customPage">
	<%@include file="/WEB-INF/views/includes/body/form_error.jsp" %>
	<form:hidden path="id"/>
	<article class="module width_full">
		<header>
			<h3>
			<s:message code="view.createNew" text="Create New"/>
			<s:message code="entity.customPage" text="Custom Page"/>
			</h3>
		</header>
		<div class="module_content">
			<fieldset>
				<label class="field">
					<s:message code="entity.customPage.name" text="Page Name"/>
					<span class="req">*</span>
				</label>
				<form:input path="name" style="width:60%;"/>
				<form:errors path="name" cssClass="error" />
			</fieldset>
			<fieldset>
				<label class="field">
					<s:message code="entity.customPage.content" text="Page Content"/>
					<span class="req">*</span>
				</label>
				<div id="editors">
					<form:hidden path="content" />
					<ckeditor:resources minified="true"/>
					<%-- <ckeditor:config height="300px" width="100%" toolbar="Basic"/> --%>
					<ckeditor:config height="300px" width="100%" />	
					<ckeditor:editor id="content_" name="content_" height="400px" width="100%" >
					${customPage.content}
					</ckeditor:editor>
				</div>
				<form:errors path="content" cssClass="error" />
			</fieldset>
			<fieldset>
				<label class="field">
					<s:message code="entity.customPage.visibility" text="Visibility"/>
					<span class="req">*</span>
				</label>
				<form:select path="visibility" style="width:200px;">
					<form:option value="Hidden">Hidden</form:option>
					<form:option value="Public">Public</form:option>
					<form:option value="Secured">Secured</form:option>
				</form:select>
				<form:errors path="visibility" cssClass="error" />
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