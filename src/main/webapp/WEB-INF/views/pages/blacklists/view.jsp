<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html lang="en">
<head>
	<%@include file="/WEB-INF/views/includes/head/res_standard.jsp" %>
	<script type="text/javascript">
	$(document).ready(function() {
		crud.baseUri='${PATH.blacklists}';
		$('#back_btn').click(function(){
			history.back();
		});
		$('#edit_btn').click(function(){
			crud.editObject('${blacklist.id }');
		});
		$('#delete_btn').click(function(){
			crud.deleteObject('${blacklist.id }');
		});
		$('#cancel_btn').click(function(){
			crud.goToParentPage();
		});
	});
    </script>
</head>
<body>
	<c:set var="pageTitle" value="Blacklist"/>
	<%@include file="/WEB-INF/views/includes/body/header.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/secondary_bar.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/sidebar.jsp" %>
	
	<section id="main" class="column">
	<article class="module width_full">
		<header>
			<h3>
				<s:message code="entity.blacklist" text="Blacklist"/>
				(&nbsp;#${blacklist.id }&nbsp;)
			</h3>
		</header>
		<div class="module_content">
			<fieldset>
				<label class="field">
					<s:message code="entity.blacklist.email" text="Email"/>
				</label>
				<div class="value">
					<s:message text="${blacklist.email }"/>
				</div>
			</fieldset>
			<fieldset>
				<label class="field">
					<s:message code="entity.blacklist.fullName" text="Full Name"/>
				</label>
				<div class="value">
					<s:message text="${blacklist.fullName }"/>
				</div>
			</fieldset>
			<fieldset>
				<label class="field">
					<s:message code="entity.blacklist.reason" text="Reason"/>
				</label>
				<div class="value">
					<s:message text="${blacklist.reason }"/>
				</div>
			</fieldset>
			<c:set var="command" value="${blacklist }"/>
			<%@include file="/WEB-INF/views/includes/body/view_audit_info.jsp" %>
			<div class="clear"></div>
		</div>
		<%@include file="/WEB-INF/views/includes/body/view_footer.jsp" %>
	</article>
	<div class="spacer"></div>
	</section>
	
</body>

</html>