<h3>Application Information</h3>
<table class="tablesorter">
	<tbody>
		<tr>
			<td><strong>Server Info</strong></td>
			<td><%=application.getServerInfo() %></td>
		</tr>
		<tr>
			<td><strong>Java</strong></td>
			<td><%=System.getProperty("java.version") %></td>
		</tr>
		<tr>
			<td><strong>Servlet</strong></td>
			<td><%=application.getMajorVersion() %>.<%= application.getMinorVersion() %></td>
		</tr>
		<tr>
			<td><strong>JSP</strong></td>
			<td><%=JspFactory.getDefaultFactory().getEngineInfo().getSpecificationVersion() %></td>
		</tr>
	</tbody>
</table>
<div class="clear"/>