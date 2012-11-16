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
		$(document).ready(function(){
			$('#back_btn').click(function(){
				window.location.href='${PATH.subscriberLists}/edit/${fieldValidatorFormBean.listId}';
			});
		});
    </script>
</head>
<body>
	<c:set var="pageTitle" value="Subscriber Lists"/>
	<%@include file="/WEB-INF/views/includes/body/header.jsp" %>
	<section id="secondary_bar">
		<%@include file="/WEB-INF/views/includes/body/user.jsp" %>
		<div class="breadcrumbs_container">
			<article class="breadcrumbs">
				<a href="#">
					<s:message code="view.subscriberList.step1" text="Step1 - Add Fields"/>
				</a>
				<div class="breadcrumb_divider"></div>
				<a class="current">
					<s:message code="view.subscriberList.step2" text="Step2 - Add Validators"/>
				</a>
				<div class="breadcrumb_divider"></div>
				<a href="#">
					<s:message code="view.subscriberList.step3" text="Step3 - Add Entries"/>
				</a>
			</article>
		</div>
	</section>
	<%@include file="/WEB-INF/views/includes/body/sidebar.jsp" %>
	<section id="main" class="column">
	<form:form id="main_form" method="post" action="${PATH.subscriberLists }/save-validators" modelAttribute="fieldValidatorFormBean">
	<s:bind path="*">
		<c:if test="${status.error}">
			<h4 class="alert_error">
				Please fix the following errors:
				${status.errorMessage }
			</h4>
			
		</c:if>
	</s:bind>
	<form:hidden path="listId"/>
	<article class="module width_full">
		<header>
			<h3>
				<s:message code="view.subscriberList.step2" text="Step2 - Add Validators"/>
			</h3>
		</header>
		<div class="module_content">
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
							<input type="checkbox" checked="checked" disabled="disabled"/>
						</td>
						<td><s:message code="java.lang.String" text="Text" /></td>
						<td>
							<input type="checkbox" checked="checked" disabled="disabled"/>
							<s:message code="view.subscriberList.required" text="Required"/>
						</td>
					</tr>
					<c:forEach var="fieldName" items="${list.fieldNames}" varStatus="loop">
					<tr>
						<td style="text-align: left;">
							<s:message text="${fieldName }"/>
							<form:hidden path="fieldValidators[${loop.index }].fieldNumber" value="${loop.count }" /><%--count is one-base, index is zero-base --%>
						</td>
						<td style="text-align: center;">
							<form:checkbox path="fieldValidators[${loop.index }].enabled"/>
						</td>
						<td style="text-align: left;">
							<form:select path="fieldValidators[${loop.index }].dataType">
								<form:option value="java.lang.String"><s:message code="java.lang.String" text="Text" /></form:option>
								<form:option value="java.lang.Number"><s:message code="java.lang.Number" text="Number" /></form:option>
								<form:option value="java.lang.Integer"><s:message code="java.lang.Integer" text="Integer" /></form:option>
								<form:option value="java.util.Date"><s:message code="java.util.Date" text="Date" /></form:option>
								<form:option value="java.sql.Timestamp"><s:message code="java.sql.Timestamp" text="Timestamp" /></form:option>
								<form:option value="java.sql.Time"><s:message code="java.sql.Time" text="Time" /></form:option>
							</form:select>
						</td>
						<td style="text-align: left;">
							<form:checkbox id="required_${loop.index }" path="fieldValidators[${loop.index }].required" value="true"/>
							<label for="required_${loop.index }"><s:message code="view.subscriberList.required" text="Required"/></label>
							<br/>
							<label for="minsize_${loop.index }"><s:message code="view.subscriberList.minSize" text="Min Size"/></label>
							<form:input id="minsize_${loop.index }" path="fieldValidators[${loop.index }].minSize" cssStyle="width:35px;"/>
							<img src="${ctx }/resources/images/bullets/add.png"/>
							<label for="maxsize_${loop.index }"><s:message code="view.subscriberList.maxSize" text="Max Size"/></label>
							<form:input id="maxsize_${loop.index }" path="fieldValidators[${loop.index }].maxSize" cssStyle="width:35px;"/>
							<form:errors path="fieldValidators[${loop.index }].minSize" cssClass="error" />
							<form:errors path="fieldValidators[${loop.index }].maxSize" cssClass="error" />
							<br/>
							<label for="regexp_${loop.index }"><s:message code="view.subscriberList.regExp" text="Regular Expression"/></label>
							<form:input id="regexp_${loop.index }" path="fieldValidators[${loop.index }].regExp" cssStyle="width:150px;"/>
						</td>
					</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<footer>
			<div class="back_link">
				<input type="button" id="back_btn" value="Back" />
			</div>
			<div class="submit_link">
				<input type="submit" value="Next" />
			</div>
		</footer>
	</article>
	</form:form>
	<div class="spacer"></div>
	</section>
	
</body>

</html>