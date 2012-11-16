<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="stringutils" uri="http://commons.apache.org/lang/StringUtils" %>
<!doctype html>
<html lang="en">
<head>
	<%@include file="/WEB-INF/views/includes/head/res_standard.jsp" %>
	<style type="text/css">
		div#sortable { margin: 0; padding: 0; }
		div.field_block { margin: 0 5px 5px 5px; padding: 5px; font-size: 1.2em; height: 1.5em; width: 90%; }
		html>body div#sortable div { height: 1.5em; line-height: 1.2em; }
		img.del_item {float:right;}
		th.title {width:25%;text-align:center;height: 25px;}
		#sample_data_tbl {width:100%;background-color: #eeeeee;border-style: solid;border-width: 0.1em;}
	</style>
	<script type="text/javascript">
	$(document).ready(function() {
		$('div#sortable').sortable({
			placeholder: 'ui-state-highlight'
		});
		$('div#sortable').disableSelection();
		$('img.del_item').click(function(){
			var delBtn = $(this);
			jprompt.confirm('<s:message code="prompt.confirmRemoveField" text="Are you sure to remove this field?"/>',function(){
				var refId = delBtn.attr('ref');
				$('#field_lbl_'+refId).text('');
				delBtn.css('display','none');
				$('#field_name_'+refId).val('');
			});
		});
		$('#back_btn').click(function(){
			window.location.href='${PATH.imports}/file/step1';
		});
	});
    </script>
</head>
<body>
	<c:set var="pageTitle" value="Data Import"/>
	<%@include file="/WEB-INF/views/includes/body/header.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/secondary_bar.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/sidebar.jsp" %>
	<section id="main" class="column">
	<form:form id="main_form"  action="${PATH.imports }/file/step2" modelAttribute="fileDataSource" enctype="multipart/form-data">
	<%@include file="/WEB-INF/views/includes/body/form_error.jsp" %>
	<article class="module width_full" style="1500px;">
		<header>
			<h3>
			<img src="${ctx }/resources/images/icons/arrow_up.png" />
			<s:message code="view.subscriber.import" text="Import"/>
			</h3>
		</header>
		<div class="module_content">
			<table id="sample_data_tbl">
				<thead>
					<tr>
						<th class="title">Fields</th>
						<th class="title">Row 1</th>
						<th class="title">Row 2</th>
						<th class="title">Row 3</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>
							<div id="sortable" style="top: 0px;">
								<div class="field_block ui-state-hover" style="cursor: pointer;">
									<input type="hidden" name="fieldNames" value="email" />
									<s:message code="entity.subscriber.email" text="Email"/>
									<span class="req">*</span>
								</div>
								<c:forEach var="fieldName" items="${fieldNames }" varStatus="loop">
								<div id="field_${loop.count }" class="field_block ui-state-hover" style="cursor: pointer;">
									<input type="hidden" id="field_name_${loop.count }" name="fieldNames" value="${fieldName }" />
									<span id="field_lbl_${loop.count }" title="${fieldName }">
										${fieldName }
									</span>
									<c:if test="${fileDataSource.list.fieldValidators[loop.index].required }">
										<span class="req">*</span>
									</c:if>
									<c:if test="${not importBean.list.fieldValidators[loop.index].required and not empty fieldName}">
										<img class="del_item" ref="${loop.count }" src="${ctx }/resources/images/icons/delete.png" />
									</c:if>
								</div>
								</c:forEach>
								<div id="field_status" class="field_block ui-state-hover" style="cursor: pointer;">
									<input type="hidden" id="field_name_status" name="fieldNames" value="status" />
									<span id="field_lbl_status"><s:message code="entity.subscriber.status" text="Status"/></span>
									<img class="del_item" ref="status" src="${ctx }/resources/images/icons/delete.png" />
								</div>
							</div>
						</td>
						<c:forEach var="dataRow" items="${dataList }">
						<td>
							<c:forEach var="data" items="${dataRow}">
							<div class="field_block ui-state-default" title='<s:message text="${data}"/>'>
								<s:message text="${stringutils:abbreviate(data, 30) }"/>
							</div>
							</c:forEach>
						</td>
						</c:forEach>
					</tr>
				</tbody>
			</table>
			
			<div class="clear"></div>
		</div>
		<footer>
			<div class="back_link">
				<input type="button" id="back_btn" value="Back" />
			</div>
			<div class="submit_link">
				<input type="submit" id="import_btn" value="Next" />
			</div>
		</footer>
	</article>
	</form:form>
	
	<div class="spacer"></div>
	</section>
	
	
	
</body>

</html>