<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html lang="en">
<head>
	<%@include file="/WEB-INF/views/includes/head/res_standard.jsp" %>
	<style type="text/css">
		div.sortable { list-style-type: none; margin: 0; padding: 0; width: 100%; }
		div.sortable div { margin: 0 5px 5px 5px; padding: 5px; font-size: 1.2em; height: 1.5em; }
		html>body div.sortable div { height: 1.5em; line-height: 1.2em; display: inline; }
		img.del_item {cursor: pointer;}
	</style>
	<script type="text/javascript">
	$(document).ready(function() {
		$('#back_btn').click(function(){
			window.location.href='${PATH.imports}/';
		});
		$('div.sortable').sortable({
			placeholder: 'ui-state-highlight'
		});
		$('div.sortable').disableSelection();
		$('img.del_item').click(function(){
			var target = $(this);
			jprompt.confirm('<s:message code="prompt.confirmRemoveField" text="Are you sure to remove this field?"/>',function(){
				var refId = target.attr('ref');
				$('#field_'+refId).css('display','none');
				$('#field_name_'+refId).attr('disabled','disabled');
			});
		});
		$(document).ajaxStop($.unblockUI); 
		$('#import_btn').click(function(){
			if( $('#input_text').val()==''){
				jprompt.alert('No data');
				return;
			}
			var delim = $('#delim').val();
			if(delim=='') delim = $('#custom_delim').val();
			$('#delimiter').val(delim);
			$.blockUI({ message: '<h1><img src="${ctx}/resources/images/ajax/circle16.gif" /> Just a moment...</h1>' }); 
			$.post('${PATH.imports}/text', $('#main_form').serialize(),
				function(data){
					$('#input_text').val(data.rejectedText);
					//$.unblockUI();
				}
			);
		});
		$('#delim').change(function(){
			if($(this).val()=='') $('#custom_delim').show();
			else  $('#custom_delim').hide();
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
	<article class="module width_full">
		<header>
			<h3>
			<img src="${ctx }/resources/images/icons/arrow_up.png" />
			<s:message code="view.subscriber.import" text="Import"/>
			</h3>
		</header>
		<div class="module_content">
			<form id="main_form" method="post" action="${PATH.subscribers }/import/text">
			<fieldset>
				<label class="field">
					<s:message code="view.subscriber.import.text.textData" text="Text Data"/>
				</label>
				<div class="value">
					<div class="sortable">
						<div class="ui-state-default">
							<input type="hidden" name="fieldNames" value="email" />
							<s:message code="entity.subscriber.email" text="Email"/>
							<span class="req">*</span>
						</div>
						<c:forEach var="fieldName" items="${importBean.list.fieldNames }" varStatus="loop">
						<div id="field_${loop.count }" class="ui-state-default">
							<input type="hidden" id="field_name_${loop.count }" name="fieldNames" value="${fieldName }" />
							${fieldName }
							<c:if test="${importBean.list.fieldValidators[loop.index].required }">
							<span class="req">*</span>
							</c:if>
							<c:if test="${not importBean.list.fieldValidators[loop.index].required }">
							<img class="del_item" ref="${loop.count }" src="${ctx }/resources/images/icons/delete.png" />
							</c:if>
						</div>
						</c:forEach>
						<div id="field_status" class="ui-state-default">
							<input type="hidden" id="field_name_status" name="fieldNames" value="status" />
							<s:message code="entity.subscriber.status" text="Status"/>
							<img class="del_item" ref="status" src="${ctx }/resources/images/icons/delete.png" />
						</div>
					</div>
					<textarea id="input_text" name="text" style="width:100%;height:300px;margin:0px;padding:15px 0px 15px 0px;"></textarea>
				</div>
			</fieldset>
			<fieldset>
				<label class="field">
					<s:message code="view.subscriber.import.text.delimiter" text="Field Delimiter" />
				</label>
				<div style="display: inline;">
					<input type="hidden" id="delimiter" name="delimiter"/>
					<select id="delim" style="width:150px;">
						<option value=",">Comma ,</option>
						<option value=";">Semicolon ;</option>
						<option value="|">Vertical Bar |</option>
						<option value="">Custom</option>
					</select>
					<input type="text" id="custom_delim" name="delimiter" style="width:70px;display:none;"/>
				</div>
			</fieldset>
			</form>
			<div class="clear"></div>
		</div>
		<footer>
			<div class="back_link">
				<input type="button" id="back_btn" value="Back" />
			</div>
			<div class="submit_link">
				<input type="button" id="import_btn" value="Import" />
			</div>
		</footer>
	</article>
	<div class="spacer"></div>
	</section>
	
	<div id="list_dialog" class="datagrid" title="Select campaign">
		<table id="list_datagrid"></table>
		<div id="list_datagrid_pager"></div>
	</div>
	
</body>

</html>