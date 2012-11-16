<%@taglib prefix="ckeditor" uri="http://www.siberhus.com/taglibs/ckeditor"%>

<html>
	<body>
		<ckeditor:resources minified="true"/>
		<%
			request.setAttribute("name", "TUEY");
		%>
		<ckeditor:config height="300px" width="50%">
			Hello ${name}
		</ckeditor:config>
		
		<form action="${pageContext.request.contextPath}/test/foo1" method="post">
			<ckeditor:editor name="myeditor" height="400px" width="80%" >
			${name}
			</ckeditor:editor>
			<input type="submit" name="submit" value="submit"/>
		</form>
		
	</body>
</html>