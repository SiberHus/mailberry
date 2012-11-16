<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html lang="en">
<head>
	<%@include file="/WEB-INF/views/includes/head/res_standard.jsp" %>
	<script type="text/javascript" src="${ctx }/resources/js/siberhus.js"></script>
	<script type="text/javascript">
	function getRowCount(tableId){
		var oRows = document.getElementById(tableId).getElementsByTagName('tr');
		return oRows.length;
	}
	function addFieldName(fieldName){
		var idx = getRowCount('fields_tbl')-1;
		if(idx+1>20+1){ //+1 is email is email field
			alert('exceed limit');
			return;
		}
		TableUI.addRow('fields_tbl', [
			"<input type='text' style='width:95%;' name='fieldNames' value='"+fieldName+"' />",
			"<a style='cursor:pointer;' onclick='TableUI.deleteRow(this);'>"+'<input type="image" src="${ctx}/resources/images/icons/delete.png" title="Delete">'+"</a>"
		],function(){});
	}
	$(document).ready(function() {
		$('#fields_th').hide();
		<c:if test="${not subscriberList.locked }">
		<c:forEach var="fieldName" items="${subscriberList.fieldNames }">
		addFieldName('${fieldName }');
		</c:forEach>
		</c:if>
		$('#add_field_btn').click(function(){
			addFieldName('');
		});
		$('#back_btn').click(function(){
			window.location.href='${PATH.subscriberLists}/';
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
				<a class="current">
					<s:message code="view.subscriberList.step1" text="Step1 - Add Fields"/>
				</a>
				<div class="breadcrumb_divider"></div>
				<a href="#">
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
	<form:form id="main_form" method="post" action="${PATH.subscriberLists }/save" modelAttribute="subscriberList">
	<%@include file="/WEB-INF/views/includes/body/form_error.jsp" %>
	<form:hidden path="id"/>
	<form:hidden path="subscriberCount"/>
	<article class="module width_full">
		<header>
			<h3>
				<s:message code="view.subscriberList.step1" text="Step1 - Add Fields"/>
			</h3>
		</header>
		<div class="module_content">
			<fieldset>
				<label class="field">
					<s:message code="entity.subscriberList.listName" text="List Name"/>
					<span class="req">*</span>
				</label>
				<form:input path="listName" />
				<form:errors path="listName" cssClass="error" />
			</fieldset>
			<fieldset>
				<label class="field">
					<s:message code="entity.subscriberList.description" text="Description"/>
				</label>
				<form:input path="description" />
			</fieldset>
			<fieldset style="width:33%; float:left;">
				<label class="field">
					<s:message code="entity.subscriberList.status" text="Status"/>
					<span class="req">*</span>
				</label>
				<form:select path="status">
					<form:option value="ACT">
						<s:message code="entity.subscriberList.status.ACT" text="Active"/>
					</form:option>
					<form:option value="INA">
						<s:message code="entity.subscriberList.status.INA" text="Inactive"/>
					</form:option>
					<form:option value="LOC">
						<s:message code="entity.subscriberList.status.LOC" text="Locked"/>
					</form:option>
					<form:option value="UNL">
						<s:message code="entity.subscriberList.status.UNL" text="Never Locked"/>
					</form:option>
				</form:select>
			</fieldset>
			<div class="clear"></div>
			<fieldset>
				<label class="field">
					<s:message code="view.subscriberList.fieldNames" text="Field Names"/>
				</label>
				<div class="value">
					<c:if test="${not subscriberList.locked }">
					<input id="add_field_btn" type="button" value="Add Field"/>
					</c:if>
					<form:errors path="fieldNames" cssClass="error" />
					<hr/>
					<table id="fields_tbl" style="width: 100%">
						<tr>
							<th style="text-align:left; padding-left: 10px;">
								<s:message code="view.subscriberList.fieldName" text="File Name"/>
							</th>
							<th>&nbsp;</th>
						</tr>
						<tr>
							<td>
								<input type="text" style="width:95%;" value="email" readonly="true"/>
							</td>
							<th>&nbsp;</th>
						</tr>
						<c:if test="${subscriberList.locked }">
						<c:forEach var="fieldName" items="${subscriberList.fieldNames }">
						<tr>
							<td>
								<input type="text" style="width:95%;" name="fieldNames" value="${fieldName}"/>
							</td>
							<th>&nbsp;</th>
						</tr>
						</c:forEach>
						</c:if>
					</table>
				</div>
			</fieldset>
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