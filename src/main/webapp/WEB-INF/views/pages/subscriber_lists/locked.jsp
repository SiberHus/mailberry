<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html lang="en">
<head>
	<%@include file="/WEB-INF/views/includes/head/res_standard.jsp" %>
	<style type="text/css">
		#center_box {
			width:400px;
			margin:0px auto; /* Right and left margin widths set to "auto" */
			text-align:left; /* Counteract to IE5/Win Hack */
			padding:15px;
			border:1px dashed #333;
			background-color:#eee;
		}
	</style>
	<script type="text/javascript">
		$(function(){
			$('#back_btn').click(function(){
				history.back();
			});
		});
    </script>
</head>
<body>
	<c:set var="pageTitle" value="Subscriber Lists"/>
	<%@include file="/WEB-INF/views/includes/body/header.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/secondary_bar.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/sidebar.jsp" %>
	<section id="main" class="column">
	<form:form id="main_form" method="post" action="${PATH.subscriberLists }/unlock/${subscriberList.id }" modelAttribute="subscriberList">
	<%@include file="/WEB-INF/views/includes/body/form_error.jsp" %>
	<article class="module width_full">
		<header>
			<h3>
				<s:message code="entity.subscriberList" text="Subscriber List"/>
				(#${subscriberList.id })
			</h3>
		</header>
		<div class="module_content">
			<div id="center_box">
				<h2>
					<img src="${ctx }/resources/images/icons/lock.png"/>
					This list was locked.
				</h2>
				<fieldset>
					<label class="field">
						<s:message code="entity.subscriberList.listName" text="List Name"/>
					</label>
					<div class="value">
						<s:message text="${subscriberList.listName }"/>
					</div>
				</fieldset>
				<p>
				We locked this list automatically because you have already added subscribers to this list.
				Changing field name in this list may cause a problem to the other parts of software that
				refer to this list's fields.
				If you are certain that changing this list is fine, you can unlock it.
				</p>
				<div style="margin-top:30px;width:100%;text-align: center;">
					<input type="submit" value="Unlock" style="width:300px;"/>
				</div>
			</div>
			<div class="clear"></div>
		</div>
		<footer>
			<div class="back_link">
				<input type="button" id="back_btn" value="Back"/>
			</div>
		</footer>
	</article>
	</form:form>
	<div class="spacer"></div>
	</section>
	
</body>

</html>