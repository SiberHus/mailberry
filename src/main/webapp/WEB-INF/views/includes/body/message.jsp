<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>

<c:if test="${not empty message }">
	<c:set var="messageClass" value="ui-state-highlight" />
	<c:if test="${message.type eq 'error'}">
		<c:set var="messageClass" value="ui-state-error" />
	</c:if>
	<div class="ui-widget" style="padding-top:15px;">
		<div class="${messageClass } ui-corner-all" style="margin: 0px 20px 0px 20px;"> 
			<p>
				<span class="ui-icon ${message.icon }" style="float: left; margin-left:10px;"></span>
				<span style="margin-left:5px;">${message.text }</span>
			</p>
		</div>
	</div>
</c:if>