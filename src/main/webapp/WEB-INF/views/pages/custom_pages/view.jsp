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
		crud.baseUri='${PATH.customPages}';
		$('#back_btn').click(function(){
			history.back();
		});
		$('#edit_btn').click(function(){
			crud.editObject('${customPage.id }');
		});
		$('#delete_btn').click(function(){
			crud.deleteObject('${customPage.id }');
		});
		$('#cancel_btn').click(function(){
			crud.goToParentPage();
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
	<article class="module width_full">
		<header>
			<h3>
				<s:message code="entity.customPage" text="Custom Page"/>
				(&nbsp;#${customPage.id }&nbsp;)
			</h3>
		</header>
		<div class="module_content">
			<fieldset>
				<label class="field">
					<s:message code="entity.customPage.name" text="Page Name"/>
				</label>
				<div class="value">
					<s:message text="${customPage.name }"/>
				</div>
			</fieldset>
			<fieldset>
				<label class="field">
					<s:message code="entity.customPage.content" text="Page Content"/>
				</label>
				<div class="value">
					<s:message var="pageName" text="${customPage.name}"/>
					<a href="${ctx }/pages/${pageName}" target="_blank">
						View page content
					</a>
				</div>
			</fieldset>
			<fieldset style="float:left; width: 49%; margin-left: 1%;">
				<label class="field">
					<s:message code="entity.customPage.visibility" text="Visibility"/>
				</label>
				<div class="value">
					<s:message text="${customPage.visibility }"/>
				</div>
			</fieldset>
			<fieldset style="float:left; width: 49%;">
				<label class="field">
					<s:message code="entity.customPage.visitCount" text="Visits"/>
				</label>
				<div class="value">
					<s:message text="${customPage.visitCount }"/>
				</div>
			</fieldset>
			<div class="clear"></div>
			<c:set var="command" value="${customPage }"/>
			<%@include file="/WEB-INF/views/includes/body/view_audit_info.jsp" %>
			<div class="clear"></div>
		</div>
		<%@include file="/WEB-INF/views/includes/body/view_footer.jsp" %>
	</article>
	<div class="spacer"></div>
	</section>
	
</body>

</html>