<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<meta http-equiv="content-language" content="en" />
	<meta name="robots" content="noindex,nofollow" />
	<link rel="stylesheet" media="screen,projection" type="text/css" href="${ctx }/resources/comps/login/reset.css" /> <!-- RESET -->
	<link rel="stylesheet" media="screen,projection" type="text/css" href="${ctx }/resources/comps/login/main.css" /> <!-- MAIN STYLE SHEET -->
	<!--[if lte IE 6]><link rel="stylesheet" media="screen,projection" type="text/css" href="${ctx }/resources/comps/login/main-ie6.css" /><![endif]--> <!-- MSIE6 -->
	<link rel="stylesheet" media="screen,projection" type="text/css" href="${ctx }/resources/comps/login/style.css" /> <!-- GRAPHIC THEME -->
	 <%--
	<script type="text/javascript" src="${ctx }/resources/comps/login/toggle.js"></script>
	 --%>
</head>
<body id="login">
	<div id="main-02">
		<div id="login-top"></div>
		<div id="login-box">
			<!-- Logo -->
			<p class="nom t-center"><a href="#"><img src="${ctx }/resources/images/logo.png" title="Mail Berry" /></a></p>
			
			<!-- Messages -->
			<c:if test="${error}">
				<p class="msg error">The password you have entered is incorrect. 
				<%--<a href="javascript:toggle('sendpass');">Forgot password?</a> --%></p>
			</c:if>
			<c:if test="${not error }">
				<p class="msg info">Enter your username and password.</p>
			</c:if>
			
			<s:url var="authUrl" value="/static/j_spring_security_check" />
			<!-- Form -->
			<form method="post" class="signin" action="${authUrl}">
			<table class="nom nostyle">
				<tr>
					<td style="width:75px;"><label for="login-user"><strong>Username:</strong></label></td>
					<td><input type="text" size="45" name="j_username" class="input-text" id="login-user" /></td>
				</tr>
				<tr>
					<td><label for="login-pass"><strong>Password:</strong></label></td>
					<td><input type="password" size="45" name="j_password" class="input-text" id="login-pass" /></td>
				</tr>
				<%if("demo".equals(System.getProperty("mailberry.mode"))){ %>
				<tr>
					<td>Demo Users</td>
					<td>
						<table style="border-width: thin;border-color: #123456;border-style: solid;background-color: #edefec;">
							<tr>
								<th>Username</th><th>Password</th>
							</tr>
							<tr>
								<td>admin_demo</td><td>admin_demo</td>
							</tr>
							<tr>
								<td>user_demo</td><td>user_demo</td>
							</tr>
						</table>
					</td>
				</tr>
				<%} %>
				
				<tr>
					<td></td>
					<td>
						<%--<span class="f-right"><a href="javascript:toggle('sendpass');">Forgot password?</a></span> --%>
						<span class="f-left low">
							<input type="checkbox" id="login-remember" name="_spring_security_remember_me" />
							<label for="login-remember">Remember me</label>
						</span>
					</td>
				</tr>
				<%--
				<!-- Show/Hide -->
				<tr id="sendpass" style="display:none;">
					<td><label for="login-sendpass"><strong>E-mail:</strong></label></td>
					<td>
						<input type="text" size="35" name="email" class="input-text f-left" id="login-sendpass" />
						<span class="f-right"><input type="submit" value="Send" /></span>
					</td>
				</tr>
				 --%>
				<tr>
					<td colspan="2" class="t-right"><input type="submit" class="input-submit" value="Sign In &raquo;" /></td>
				</tr>
			</table>
			</form>
		</div> <!-- /login-box -->
		
		<div id="login-bottom"></div>
		<%if("demo".equals(System.getProperty("mailberry.mode"))){ %>
		<div align="center">
			<script type="text/javascript"><!--
				google_ad_client = "ca-pub-5090980166916130";
				/* app-demo-ads */
				google_ad_slot = "2402989570";
				google_ad_width = 728;
				google_ad_height = 90;
				//-->
			</script>
			<script type="text/javascript"
				src="http://pagead2.googlesyndication.com/pagead/show_ads.js">
			</script>
		</div>
		<%} %>
	</div> <!-- /main -->
</body>
</html>