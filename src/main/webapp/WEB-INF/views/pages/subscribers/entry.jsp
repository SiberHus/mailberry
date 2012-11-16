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
	<form:form id="main_form" method="post" action="${PATH.subscribers }/save-entry" modelAttribute="subscriber">
	<%@include file="/WEB-INF/views/includes/body/form_error.jsp" %>
	<c:if test="${not empty message}">
		<h4 class="alert_${message.type}">${message.text }</h4>
	</c:if>
	<form:hidden path="list.id"/>
	<form:hidden path="id"/>
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
							<form:input path="email" style="width:95%"/>
							<form:errors path="email" cssClass="error" />
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
							<form:input path="field${loop.count }Value" style="width:95%"/>
							<form:errors path="field${loop.count }Value" cssClass="error" />
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
							<form:select path="status">
								<form:option value="ACT"><s:message code="entity.subscriber.status.ACT" text="Active"/></form:option>
								<form:option value="INA"><s:message code="entity.subscriber.status.INA" text="Inactive"/></form:option>
								<form:option value="UNS"><s:message code="entity.subscriber.status.UNS" text="Unsubscribed"/></form:option>
								<form:option value="BLO"><s:message code="entity.subscriber.status.BLO" text="Blocked"/></form:option>
								<form:option value="TES"><s:message code="entity.subscriber.status.TES" text="Test"/></form:option>
							</form:select>
							<form:errors path="status" cssClass="error" />
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<footer>
			<div class="submit_link">
				<c:if test="${empty subscriber.id}">
				<input type="submit" value="Save and Continue" />
				</c:if>
				<c:if test="${not empty subscriber.id}">
				<input type="submit" value="Save" />
				</c:if>
				&nbsp;
				<input type="button" id="cancel_btn" value="Cancel"/>
			</div>
		</footer>
	</article>
	</form:form>
	<div class="spacer"></div>
	</section>
	
</body>

</html>