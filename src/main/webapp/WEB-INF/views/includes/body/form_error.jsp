<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<s:bind path="*">
	<c:if test="${status.error}">
		<div class="ui-widget" style="padding-top:15px;">
			<div class="ui-state-error ui-corner-all" style="margin: 0px 20px 0px 20px;"> 
				<p>
					<span class="ui-icon ui-icon-alert" style="float: left; margin-left:10px;"></span>
					<s:message text="Please fix the following errors"/>: ${status.errorMessage }
					<%-- ${status.errors.errorCount }
					<ul>
						<c:forEach var="err" items="${status.errors.globalErrors }">
							<li>${err.defaultMessage }</li>
						</c:forEach>
						<c:forEach var="err" items="${status.errors.fieldErrors }">
							<li>${err.field } - ${err.defaultMessage }</li>
						</c:forEach>
					</ul>
					 --%>
				</p>
			</div>
		</div>
	</c:if>
</s:bind>