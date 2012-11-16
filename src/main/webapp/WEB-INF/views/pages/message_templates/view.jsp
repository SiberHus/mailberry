<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="stringUtils" uri="http://commons.apache.org/lang/StringUtils" %>
<!doctype html>
<html lang="en">
<head>
	<%@include file="/WEB-INF/views/includes/head/res_standard.jsp" %>
	<script type="text/javascript">
	$(document).ready(function() {
		crud.baseUri='${PATH.messageTemplates}';
		$('#back_btn').click(function(){
			history.back();
		});
		$('#edit_btn').click(function(){
			crud.editObject('${messageTemplate.id }');
		});
		$('#delete_btn').click(function(){
			crud.deleteObject('${messageTemplate.id }');
		});
		$('#cancel_btn').click(function(){
			crud.goToParentPage();
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
	<article class="module width_full">
		<header>
			<h3>
				<s:message code="entity.messageTemplate" text="Message Template"/>
				(&nbsp;#${messageTemplate.id }&nbsp;)
			</h3>
		</header>
		<div class="module_content">
			<fieldset>
				<label class="field">
					<s:message code="entity.messageTemplate.templateName" text="Template Name"/>
				</label>
				<div class="value">
					<s:message text="${messageTemplate.templateName }"/>
				</div>
			</fieldset>
			<fieldset>
				<label class="field">
					<s:message code="entity.messageTemplate.description" text="Description"/>
				</label>
				<div class="value">
					<s:message text="${messageTemplate.description }"/>
				</div>
			</fieldset>
			<c:if test="${stringUtils:isNotBlank(messageTemplate.templateHtmlData)}">
			<fieldset>
				<label class="field">
					<s:message code="entity.messageTemplate.templateHtmlData" text="Template Data (HTML/Rich Text)"/>
				</label>
				<div class="value">
					<%--
					<s:message text="${messageTemplate.templateHtmlData}"/>
					 --%>
					<iframe src="${PATH.messageTemplates }/html/${messageTemplate.id}" width="100%" height="300" >
  						<p>Your browser does not support iframes.</p>
					</iframe>
				</div>
			</fieldset>
			</c:if>
			<c:if test="${stringUtils:isNotBlank(messageTemplate.templateTextData)}">
			<fieldset>
				<label class="field">
					<s:message code="entity.messageTemplate.templateHtmlData" text="Template Data (Plain Text)"/>
				</label>
				<div class="value">
					<pre>
${templateTextData}
					</pre>
				</div>
			</fieldset>
			</c:if>
			<c:set var="command" value="${messageTemplate }"/>
			<%@include file="/WEB-INF/views/includes/body/view_audit_info.jsp" %>
			<div class="clear"></div>
		</div>
		<%@include file="/WEB-INF/views/includes/body/view_footer.jsp" %>
	</article>
	<div class="spacer"></div>
	</section>
	
</body>

</html>