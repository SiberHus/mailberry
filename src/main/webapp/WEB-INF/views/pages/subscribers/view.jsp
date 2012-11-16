<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ckeditor" uri="http://www.siberhus.com/taglibs/ckeditor"%>
<!doctype html>
<html lang="en">
<head>
	<%@include file="/WEB-INF/views/includes/head/res_standard.jsp" %>
	<%@include file="/WEB-INF/views/includes/head/res_jqgrid.jsp" %>
	<script type="text/javascript" src="${ctx }/resources/js/siberhus.js"></script>
	<script type="text/javascript">
		$(function(){
			crud.baseUri='${PATH.subscribers }';
			$('#back_btn').click(function(){
				history.back();
			});
			$('#edit_btn').click(function(){
				crud.editObject('${subscriber.id}');
			});
			$('#delete_btn').click(function(){
				crud.deleteObject('${subscriber.id}');
			});
			$('#cancel_btn').click(function(){
				window.location.href='${PATH.subscribers }/${subscriber.list.id}';
			});
		});
    </script>
</head>
<body>
	<c:set var="pageTitle" value="Subscribers"/>
	<%@include file="/WEB-INF/views/includes/body/header.jsp" %>
	<section id="secondary_bar">
		<%@include file="/WEB-INF/views/includes/body/user.jsp" %>
		<div class="breadcrumbs_container">
			<article class="breadcrumbs">
				<a href="${PATH.subscriberLists }/">
					<s:message code="entity.subscriberList" text="Subscriber Lists"/>
				</a>
				<div class="breadcrumb_divider"></div>
				<a href="${PATH.subscribers }/${subscriber.list.id}">
					<s:message code="entity.subscriber" text="Subscribers"/>
				</a>
				<div class="breadcrumb_divider"></div>
				<a class="current">
					<s:message code="view.subscriber.addEntry" text="Add Entry"/>
				</a>
			</article>
		</div>
	</section>
	<%@include file="/WEB-INF/views/includes/body/sidebar.jsp" %>
	<section id="main" class="column">
	<article class="module width_full">
		<header>
			<h3>
				<s:message code="view.subscriber.addEntry" text="Add Subscriber Entry"/>
			</h3>
		</header>
		<div class="module_content">
			<table style="width:90%" class="tablesorter">
				<thead>
					<tr>
						<th style="text-align: left;width:27%"><s:message code="view.subscriber.fieldName" text="Field Name"/></th>
						<th style="text-align: left;width:23%"><s:message code="view.subscriber.fieldType" text="Field Type"/></th>
						<th style="text-align: left;width:50%"><s:message code="view.subscriber.fieldValue" text="Value"/></th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>
							<strong><s:message code="entity.subscriber.email" text="Email"/></strong>
							<span class="req">*</span>
						</td>
						<td><s:message code="java.lang.String" text="Text" /></td>
						<td>
							<s:message text="${subscriber.email }"/>
						</td>
					</tr>
					<c:forEach var="fieldName" items="${subscriber.list.fieldNames}" varStatus="loop">
					<c:set var="validator" value="${subscriber.list.fieldValidators[loop.index]}"/>
					<tr>
						<td style="text-align: left;">
							<strong><s:message text="${fieldName }"/></strong>
							<c:if test="${validator.required}">
							<span class="req">*</span>
							</c:if>
						</td>
						<td style="text-align: left;">
							<c:if test="${validator.dataType eq 'java.lang.String'}">
							<s:message code="java.lang.String" text="Text" />
							</c:if>
							<c:if test="${validator.dataType eq 'java.lang.Number'}">
							<s:message code="java.lang.Number" text="Number" />
							</c:if>
							<c:if test="${validator.dataType eq 'java.lang.Integer'}">
							<s:message code="java.lang.Integer" text="Integer" />
							</c:if>
							<c:if test="${validator.dataType eq 'java.util.Date'}">
							<s:message code="java.util.Date" text="Date" />
							: ${FMT.inputDate }
							</c:if>
							<c:if test="${validator.dataType eq 'java.sql.Timestamp'}">
							<s:message code="java.sql.Timestamp" text="Timestamp" />
							: ${FMT.inputTimestamp }
							</c:if>
							<c:if test="${validator.dataType eq 'java.sql.Time'}">
							<s:message code="java.sql.Time" text="Time" />
							: ${FMT.inputTime }
							</c:if>
						</td>
						<td style="text-align: left;">
							<c:set var="fieldName" value="field${loop.count }Value"/>
							<s:message text="${subscriber[fieldName] }" />
						</td>
					</tr>
					</c:forEach>
					<tr>
						<td>
							<strong><s:message code="entity.subscriber.status" text="Status"/></strong>
							<span class="req">*</span>
						</td>
						<td><s:message code="java.lang.String" text="Text" /></td>
						<td>
							<s:message code="entity.subscriber.status.${subscriber.status }" text="${subscriber.status }"/>
						</td>
					</tr>
				</tbody>
			</table>
			<c:set var="command" value="${subscriber }"/>
			<%@include file="/WEB-INF/views/includes/body/view_audit_info.jsp" %>
		</div>
		<%@include file="/WEB-INF/views/includes/body/view_footer.jsp" %>
	</article>
	<div class="spacer"></div>
	</section>
	
</body>

</html>