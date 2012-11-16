<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<div class="user">
	<p>
		<security:authentication property="name" />&nbsp;&nbsp;
		(<a href="${ctx }/static/j_spring_security_logout">Logout</a>)
	</p>
</div>