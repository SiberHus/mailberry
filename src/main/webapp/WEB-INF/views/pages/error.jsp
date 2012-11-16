<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html lang="en">
<head>
	<%@include file="/WEB-INF/views/includes/head/res_standard.jsp" %>
	<script type="text/javascript">
	$(document).ready(function() {
		$('#back_btn').click(function(){
			history.back();
		});
	});
    </script>
</head>
<body>
	<%@include file="/WEB-INF/views/includes/body/header.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/secondary_bar.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/sidebar.jsp" %>
	
	<section id="main" class="column">
	<%@include file="/WEB-INF/views/includes/body/message.jsp" %>
	<article class="module width_full">
		<header>
			<h3>
				Error
			</h3>
		</header>
		<div class="module_content">
			<h2>We are sorry for your inconvenience.</h2>
			<fieldset>
				<label class="field">
					StackTrace
				</label>
				<textarea rows="20" style="width:97%;" readonly="readonly">${stackTrace}</textarea>
			</fieldset>
			<div class="clear"></div>
		</div>
		<footer>
			<div class="back_link">
				<input type="button" id="back_btn" value="Back"/>
			</div>
			<div class="submit_link">
				<input type="button" id="rpt_bug_btn" value="Report Bug" />
			</div>
		</footer>
	</article>
	<div class="spacer"></div>
	</section>
	
</body>

</html>