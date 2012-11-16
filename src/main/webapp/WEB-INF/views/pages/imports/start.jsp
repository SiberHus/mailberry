<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html lang="en">
<head>
	<%@include file="/WEB-INF/views/includes/head/res_standard.jsp" %>
	<%@include file="/WEB-INF/views/includes/head/res_jqgrid.jsp" %>
	<style type="text/css">
		div.filter_item{ margin-top: 5px; }
	</style>
	<script type="text/javascript">
	$(document).ready(function() {
		$("#list_datagrid").jqGrid({
			url:'${PATH.subscriberLists}/list/data',
			pager: '#list_datagrid_pager',
			colNames:['','List Name', 'Field Count', 'Status', 'Created By', 'Created At'],
			colModel:[
			    {name:'hidden', hidden:true},
				{name:'listName', width:100},
				{name:'fieldCount', width:50},
				{name:'status', width:50},
				{name:'createdBy', width:50},
				{name:'createdAt', width:50, formatter:'date'}
			]
		});
		$('#list_datagrid').setGridHeight(230);
		$('#list_datagrid').setGridWidth(600);
		$("#list_dialog").dialog({
			autoOpen: false,height: 420,width: 630,modal: true,
			buttons: {
				Select: function() {
					var rows = grid.getSelectedValues('#list_datagrid');
					if(rows==null) return;
					var listId = grid.getSelectedId('#list_datagrid');
					$('#list_id').val(listId);
					$('#list_name').val(rows['listName']);
					$.getJSON('${PATH.subscriberLists}/fieldnames/'+listId,
						function(data){
						var filters = '';
						$.each(data, function(key, val) {
							filters+='<div class="filter_item"><input type="checkbox" id="fieldnumber_' 
							+ key + '" name="fieldNumbers" value="'+(key+1)+'" checked="checked"/><label for="fieldnumber_'
							+key+'">'+val+'</label></div>';
						});
						$('#fieldnames_div').html(filters);
					});
					$(this).dialog('close');
				},
				Cancel: function() {
					$(this).dialog('close');
				}
			}
		});
		$('#browse_lists').click(function(){
			$("#list_dialog").dialog('open');
		});
		$('#list_name').click(function(){
			$("#list_dialog").dialog('open');
		});
		$('#main_form').submit(function(){
			if(!$('#list_id').val()){
				jprompt.alert('<s:message code="prompt.alert.pleaseSelectList" text="Please select list to be imported"/>');
				return false;
			}
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
	<form:form id="main_form" method="post" action="${PATH.imports }/start" modelAttribute="importBean">
	<%@include file="/WEB-INF/views/includes/body/form_error.jsp" %>
	<article class="module width_full">
		<header>
			<h3>
			<img src="${ctx }/resources/images/icons/arrow_up.png" />
			<s:message code="view.subscriber.import" text="Import"/>
			</h3>
		</header>
		<div class="module_content">
			<fieldset>
				<label class="field">
					<s:message code="view.subscriber.import.selectList" text="Select list for adding the new contacts"/>
				</label>
				<div class="value">
					<form:hidden id="list_id" path="list.id"/>
					<form:input id="list_name" path="list.listName" cssStyle="width:335px;" readonly="true"/>
				  	<input id="browse_lists" type="button" value="Browse" />
				</div>
				<form:errors path="list.id" cssClass="error"/>
			</fieldset>
			<fieldset>
				<label class="field">
					<s:message code="view.subscriber.import.selectDatasource" text="Select data source to be imported"/>
				</label>
				<div class="value">
					<ul style="list-style: none;">
						<li>
							<form:radiobutton id="ds_file" path="dataSource" value="file"/>
							<label for="ds_file">
								<s:message code="view.subscriber.import.file" text="External File"/>
							</label>
						</li>
						<%--
						<li>
							<form:radiobutton id="ds_database" path="dataSource" value="database"/>
							<label for="ds_database">
								<s:message code="view.subscriber.import.database" text="Relational Database"/>
							</label>
						</li>
						<li>
							<form:radiobutton id="ds_saas" path="dataSource" value="saas"/>
							<label for="ds_saas">
								<s:message code="view.subscriber.import.saas" text="3rd Party SaaS"/>
							</label>
						</li>
						 --%>
						<li>
							<form:radiobutton id="ds_text" path="dataSource" value="text"/>
							<label for="ds_text">
								<s:message code="view.subscriber.import.text" text="Copy N Paste (Textarea)"/>
							</label>
						</li>
						<li>
							<form:radiobutton id="ds_manual" path="dataSource" value="manual"/>
							<label for="ds_manual">
								<s:message code="view.subscriber.import.manual" text="Add Entry One-by-One"/>
							</label>
						</li>
					</ul>
				</div>
				<form:errors path="dataSource" cssClass="error"/>
			</fieldset>
			<div class="clear"></div>
		</div>
		<footer>
			<div class="submit_link">
				<input type="submit" id="import_btn" value="Next" />
			</div>
		</footer>
	</article>
	</form:form>
	<div class="spacer"></div>
	</section>
	
	<div id="list_dialog" class="datagrid" title="Select campaign">
		<table id="list_datagrid"></table>
		<div id="list_datagrid_pager"></div>
	</div>
	
</body>

</html>