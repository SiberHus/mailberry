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
		crud.baseUri='${PATH.messageTemplates}';
		$('#editors').tabs();
		$('#main_form').submit(function(){
			$('#templateHtmlData').val(CKEDITOR.instances.templateHtmlData_.getData());
		});
		$('#cancel_btn').click(function(){
			window.location.href='${PATH.messageTemplates}/';
		});
	});
    </script>
</head>
<body>
	<c:set var="pageTitle" value="Message Templates"/>
	<%@include file="/WEB-INF/views/includes/body/header.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/secondary_bar.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/sidebar.jsp" %>
	<section id="main" class="column">
	<form:form id="main_form" method="post" action="${PATH.messageTemplates }/save" modelAttribute="messageTemplate">
	<%@include file="/WEB-INF/views/includes/body/form_error.jsp" %>
	<form:hidden path="id"/>
	<article class="module width_full">
		<header>
			<h3>
			<s:message code="view.createNew" text="Create New"/>
			<s:message code="entity.messageTemplate" text="Message Template"/>
			</h3>
		</header>
		<div class="module_content">
			<fieldset>
				<label class="field">
					<s:message code="entity.messageTemplate.templateName" text="Template Name"/>
					<span class="req">*</span>
				</label>
				<form:input path="templateName" />
				<form:errors path="templateName" cssClass="error" />
			</fieldset>
			<fieldset>
				<label class="field">
					<s:message code="entity.messageTemplate.description" text="Description"/>
				</label>
				<form:input path="description" />
			</fieldset>
			<fieldset>
				<label class="field">
					<s:message code="view.label.templateData" text="Template Data"/>
					<span class="req">*</span>
				</label>
				<div id="editors">
					<ul>
						<li><a href="#htmlTab"><s:message code="view.text.html" text="HTML"/></a></li>
						<li><a href="#textTab"><s:message code="view.text.text" text="TEXT"/></a></li>
					</ul>
					<div id="htmlTab">
						<form:hidden path="templateHtmlData" />
						<ckeditor:resources minified="true"/>
						<%-- <ckeditor:config height="300px" width="100%" toolbar="Basic"/> --%>
						<ckeditor:config height="300px" width="100%" />	
						<ckeditor:editor id="templateHtmlData_" name="templateHtmlData_" height="400px" width="100%" fullPage="true">
						${messageTemplate.templateHtmlData}
						</ckeditor:editor>
					</div>
					<div id="textTab">
						<form:textarea path="templateTextData" style="padding-left:0px;margin-left:0px ;width:100%;height:400px"/>
					</div>
				</div>
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