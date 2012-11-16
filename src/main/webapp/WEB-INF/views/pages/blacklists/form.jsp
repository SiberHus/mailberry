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
		$('#cancel_btn').click(function(){
			window.location.href=crud.baseUri+'/';
		});
		$('#emails').change(function(){
			var data = $(this).val();
			//data = data.replace('[,:;\|\s]','\n');
			var re = new RegExp('[,:;\\|\\s]+', 'g');
			data = data.replace(re, '\n');
			$(this).val(data);
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
	<form:form id="main_form" method="post" action="${PATH.blacklists }/save" modelAttribute="blacklist">
	<%@include file="/WEB-INF/views/includes/body/form_error.jsp" %>
	<form:hidden path="id"/>
	<article class="module width_full">
		<header>
			<h3>
			<s:message code="view.createNew" text="Create New"/>
			<s:message code="entity.blacklist" text="Blacklist"/>
			</h3>
		</header>
		<div class="module_content">
			<fieldset>
				<label class="field">
					<s:message code="entity.blacklist.email" text="Email"/>
					<span class="req">*</span>
				</label>
				<c:if test="${not empty blacklist.email }">
					<form:input path="email" />
					<form:errors path="email" cssClass="error" />
				</c:if>
				<c:if test="${empty blacklist.email }">
					<form:textarea path="emails" rows="7"/>
					<form:errors path="emails" cssClass="error" />
				</c:if>
				<div class="clear"></div>
				<div style="margin:15px 0px 0px 25px;">
					Separate each email by one of these characters
					<ul>
						<li>comma (,)</li>
						<li>pipe or virtical bar (|)</li>
						<li>space ( )</li>
						<li>tab (\t)</li>
						<li>new line (\n)</li>
					</ul>
				</div>
			</fieldset>
			<fieldset>
				<label class="field">
					<s:message code="entity.blacklist.fullName" text="Full Name"/>
				</label>
				<form:input path="fullName" />
			</fieldset>
			<fieldset>
				<label class="field">
					<s:message code="entity.blacklist.reason" text="Reason"/>
				</label>
				<form:input path="reason" />
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