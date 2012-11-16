<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html lang="en">
<head>
	<%@include file="/WEB-INF/views/includes/head/res_standard.jsp" %>
	<script type="text/javascript">
		$(function(){
			crud.baseUri='${PATH.subscriberLists}';
			$('#back_btn').click(function(){
				history.back();
			});
			$('#edit_btn').click(function(){
				crud.editObject('${subscriberList.id }');
			});
			$('#delete_btn').click(function(){
				crud.deleteObject('${subscriberList.id }');
			});
			$('#cancel_btn').click(function(){
				crud.goToParentPage();
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
	<form:form id="main_form" method="post" action="${PATH.subscriberLists }/save" modelAttribute="subscriberList">
	<%@include file="/WEB-INF/views/includes/body/form_error.jsp" %>
	<form:hidden path="id"/>
	<article class="module width_full">
		<header>
			<h3>
				<s:message code="entity.subscriberList" text="Subscriber List"/>
				(#${subscriberList.id })
			</h3>
		</header>
		<div class="module_content">
			<fieldset>
				<label class="field">
					<s:message code="entity.subscriberList.listName" text="List Name"/>
				</label>
				<div class="value">
					<s:message text="${subscriberList.listName }"/>
				</div>
			</fieldset>
			<fieldset>
				<label class="field">
					<s:message code="entity.subscriberList.description" text="Description"/>
				</label>
				<div class="value">
					<s:message text="${subscriberList.description }"/>
				</div>
			</fieldset>
			<fieldset>
				<label class="field">
					<s:message code="entity.subscriberList.status" text="Status"/>
					<span class="req">*</span>
				</label>
				<div class="value">
					<s:message text="${subscriberList.status }"/>
				</div>
			</fieldset>
			<div class="clear"></div>
			<div style="margin-top:15px;">
				<table style="width:90%" class="tablesorter">
				<thead>
					<tr>
						<th style="text-align: left;width:25%"><s:message code="view.subscriberList.fieldName" text="Field Name"/></th>
						<th style="text-align: center;width:10%"><s:message code="view.subscriberList.validate" text="Validate"/></th>
						<th style="text-align: left;width:15%"><s:message code="view.subscriberList.dataType" text="Data Type"/></th>
						<th style="text-align: left;width:50%"><s:message code="view.subscriberList.validationRules" text="Validation Rules"/></th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td style="text-align: left;">email</td>
						<td style="text-align: center;">
							<img src="${ctx }/resources/images/icons/tick.png"/>
						</td>
						<td><s:message code="java.lang.String" text="Text" /></td>
						<td>
							<ul>
								<li><s:message code="view.subscriberList.required" text="Required"/></li>
							</ul>
						</td>
					</tr>
					<c:forEach var="fieldName" items="${subscriberList.fieldNames}" varStatus="loop">
					<tr>
						<td style="text-align: left;">
							<s:message text="${fieldName }"/>
						</td>
						<td style="text-align: center;">
							<c:if test="${subscriberList.fieldValidators[loop.index].enabled}">
								<img src="${ctx }/resources/images/icons/tick.png"/>
							</c:if>
							<c:if test="${not subscriberList.fieldValidators[loop.index].enabled}">
								<img src="${ctx }/resources/images/icons/cross.png"/>
							</c:if>
						</td>
						<td style="text-align: left;">
							<s:message code="${subscriberList.fieldValidators[loop.index].dataType }" 
								text="${subscriberList.fieldValidators[loop.index].dataType}"/>
						</td>
						<td style="text-align: left;">
							<ul>
								<c:if test="${subscriberList.fieldValidators[loop.index].required }">
								<li><s:message code="view.subscriberList.required" text="Required"/></li>
								</c:if>
								<c:if test="${subscriberList.fieldValidators[loop.index].minSize > 0 }">
								<li>
									<s:message code="view.subscriberList.minSize" text="Min Size"/>:&nbsp;
									${subscriberList.fieldValidators[loop.index].minSize }
								</li>
								</c:if>
								<c:if test="${subscriberList.fieldValidators[loop.index].maxSize > 0 }">
								<li>
									<s:message code="view.subscriberList.maxSize" text="Max Size"/>:&nbsp;
									${subscriberList.fieldValidators[loop.index].maxSize }
								</li>
								</c:if>
								<c:if test="${not empty subscriberList.fieldValidators[loop.index].regExp}">
								<li>
									<s:message code="view.subscriberList.regExp" text="Regular Expression"/>:&nbsp;
									${subscriberList.fieldValidators[loop.index].regExp }
								</li>
								</c:if>
							</ul>
						</td>
					</tr>
					</c:forEach>
				</tbody>
			</table>
			</div>
			<div class="clear"></div>
			<c:set var="command" value="${subscriberList }"/>
			<%@include file="/WEB-INF/views/includes/body/view_audit_info.jsp" %>
		</div>
		<%@include file="/WEB-INF/views/includes/body/view_footer.jsp" %>
	</article>
	</form:form>
	<div class="spacer"></div>
	</section>
	
</body>

</html>