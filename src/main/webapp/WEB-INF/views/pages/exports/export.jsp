<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ckeditor" uri="http://www.siberhus.com/taglibs/ckeditor"%>
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
				{name:'listName', width:100, searchoptions:searchoptions.string},
				{name:'fieldCount', width:50, searchoptions:searchoptions.number},
				{name:'status', width:50, searchoptions:searchoptions.string},
				{name:'createdBy', width:50},
				{name:'createdAt', width:50, formatter:'date', searchoptions:searchoptions.date}
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
		$('#file_type').change(function(){
			var val = $(this).val();
			if(val=='CSV'){
				$('#config_csv').show();
				$('#config_excel').hide();
				$('#config_xml').hide();
			}else if(val=='XLS' || val=='XLSX'){
				$('#config_csv').hide();
				$('#config_excel').show();
				$('#config_xml').hide();
			}else{
				$('#config_csv').hide();
				$('#config_excel').hide();
				$('#config_xml').show();
			}
		});
		$('#main_form').submit(function(){
			if(!$('#list_id').val()){
				jprompt.alert('<s:message code="prompt.alert.pleaseSelectList" text="Please select list to be exported"/>');
				return false;
			}
		});
	});
    </script>
</head>
<body>
	<c:set var="pageTitle" value="Data Export"/>
	<%@include file="/WEB-INF/views/includes/body/header.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/secondary_bar.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/sidebar.jsp" %>
	<section id="main" class="column">
	<form id="main_form" method="post" action="${PATH.exports }/">
	<article class="module width_full">
		<header>
			<h3>
			<img src="${ctx }/resources/images/icons/arrow_down.png" />
			<s:message code="view.subscriber.export" text="Export"/>
			</h3>
		</header>
		<div class="module_content">
			<fieldset>
				<label class="field">
					<s:message code="view.subscriber.impexp.selectList" text="Select list to be exported"/>
				</label>
				<div class="value">
					<input type="hidden" id="list_id" name="list.id"/>
					<input type="text" id="list_name" style="width:335px;" readonly="readonly"/>
				  	<input id="browse_lists" type="button" value="Browse" />
				</div>
			</fieldset>
			<fieldset>
				<label class="field">
					<s:message code="view.subscriber.impexp.filter" text="Filter"/>
				</label>
				<div class="value">
					<label for="sub_status"><s:message code="entity.subscriber.status" text="Status"/></label>
					<select id="sub_status" name="status" style="width:300px;">
						<option value=""><s:message code="entity._status.ALL" text="All" /></option>
						<option value="ACT"><s:message code="entity._status.ACT" text="Active" /></option>
						<option value="INA"><s:message code="entity._status.INA" text="Inactive" /></option>
						<option value="UNS"><s:message code="entity._status.UNS" text="Unsubscribed" /></option>
						<option value="BLO"><s:message code="entity._status.BLO" text="Blocked" /></option>
						<option value="TES"><s:message code="entity._status.TES" text="Test" /></option>
					</select>
					<div id="fieldnames_div" style="margin-top: 15px; "></div>
				</div>
			</fieldset>
			<fieldset>
				<label class="field">
					<s:message code="view.subscriber.impexp.selectFileType" text="Select a desired file type"/>
				</label>
				<div class="value">
					<label for="file_type"><s:message code="view.subscriber.impexp.fileType" text="File Type"/></label>
					<select id="file_type" name="fileType" style="width:300px;">
						<option value="CSV"><s:message code="x.file.type.csv.detail" text="Comma Separated Values (*.csv)" /></option>
						<option value="XLS"><s:message code="x.file.type.xls.detail" text="Excel 97-2003 Workbook (*.xls)" /></option>
						<option value="XLSX"><s:message code="x.file.type.xlsx.detail" text="Excel Workbook (*.xlsx)" /></option>
						<option value="XML"><s:message code="x.file.type.xml.detail" text="XML Data (*.xml)" /></option>
					</select>
					<div style="margin-top: 20px;">
						<h4><s:message code="view.subscriber.impexp.fileFormatConfig" text="File format configuration"/></h4>
						<table class="tablesorter" style="width:500px;">
							<thead>
								<tr>
									<th style="width:200px;"><s:message code="view.subscriber.impexp.configName" text="Name"/></th>
									<th style="width:300px;"><s:message code="view.subscriber.impexp.configValue" text="Value"/></th>
								</tr>
							</thead>
							<tbody id="config_csv">
								<tr>
									<td><s:message code="view.subscriber.impexp.labeled" text="Labeled"/></td>
									<td><input type="checkbox" name="csv.lebeled" checked="checked" value="true"/></td>
								</tr>
								<tr>
									<td><s:message code="view.subscriber.impexp.charset" text="Charset"/></td>
									<td>
										<select name="csv.charset">
											<option value="UTF-8">UTF-8</option>
											<c:forEach var="charset" items="${Charsets }">
											<option value="${charset }">${charset} </option>
											</c:forEach>
										</select>
									</td>
								</tr>
								<tr>
									<td><s:message code="view.subscriber.impexp.separator" text="Separator Character"/></td>
									<td><input type="text" name="csv.separator" value="," maxlength="1"/></td>
								</tr>
								<tr>
									<td><s:message code="view.subscriber.impexp.quoteChar" text="Quote Character"/></td>
									<td><input type="text" name="csv.quoteChar" value="&quot;" maxlength="1"/></td>
								</tr>
								<tr>
									<td><s:message code="view.subscriber.impexp.escapeChar" text="Escape Character"/></td>
									<td><input type="text" name="csv.escapeChar" value="&quot;" maxlength="1"/></td>
								</tr>
								<tr>
									<td><s:message code="view.subscriber.impexp.fileFormat" text="File Format"/></td>
									<td>
										<select name="csv.fileFormat">
											<option value="WIN">Windows</option>
											<option value="UNIX">Unix</option>
											<option value="MAC">Mac</option>
										</select>
									</td>
								</tr>
								
							</tbody>
							<tbody id="config_excel" style="display:none;">
								<tr>
									<td><s:message code="view.subscriber.impexp.labeled" text="Labeled"/></td>
									<td><input type="checkbox" name="excel.lebeled" checked="checked" value="true"/></td>
								</tr>
								<tr>
									<td><s:message code="view.subscriber.impexp.itemsPerSheet" text="Items Per Sheet"/></td>
									<td>
										<input type="text" name="excel.itemsPerSheet" value="5000"/>
									</td>
								</tr>
							</tbody>
							<tbody id="config_xml" style="display: none;">
								<tr>
									<td><s:message code="view.subscriber.impexp.charset" text="Charset"/></td>
									<td>
										<select name="xml.charset">
											<option value="UTF-8">UTF-8</option>
											<c:forEach var="charset" items="${Charsets }">
											<option value="${charset }">${charset} </option>
											</c:forEach>
										</select>
									</td>
								</tr>
								<tr>
									<td><s:message code="view.subscriber.impexp.prettyPrint" text="Pretty Print"/></td>
									<td><input type="checkbox" name="xml.prettyPrint" checked="checked" value="true"/></td>
								</tr>
								<tr>
									<td><s:message code="view.subscriber.impexp.rootTagName" text="Root Tag Name"/></td>
									<td>
										<input type="text" name="xml.rootTagName" value="list"/>
									</td>
								</tr>
								<tr>
									<td><s:message code="view.subscriber.impexp.itemTagName" text="Item Tag Name"/></td>
									<td>
										<input type="text" name="xml.itemTagName" value="subscriber" />
									</td>
								</tr>
								<tr>
									<td><s:message code="view.subscriber.impexp.fileFormat" text="File Format"/></td>
									<td>
										<select name="xml.fileFormat">
											<option value="WIN">Windows</option>
											<option value="UNIX">Unix</option>
											<option value="MAC">Mac</option>
										</select>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</fieldset>
			<div class="clear"></div>
		</div>
		<footer>
			<div class="submit_link">
				<input type="submit" id="export_btn" value="Export" />
			</div>
		</footer>
	</article>
	</form>
	<div class="spacer"></div>
	</section>
	
	<div id="list_dialog" class="datagrid" title="Select Subscriber List">
		<table id="list_datagrid"></table>
		<div id="list_datagrid_pager"></div>
	</div>
	
</body>

</html>