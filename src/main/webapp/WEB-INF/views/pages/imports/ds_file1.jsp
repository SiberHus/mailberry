<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html lang="en">
<head>
	<%@include file="/WEB-INF/views/includes/head/res_standard.jsp" %>
	<style type="text/css">
	#filetype_dialog {
		padding:15px;
		background-color:#eee;
	}
	</style>
	<script type="text/javascript">
	function showConfig(filetype, autodetect){
		if(filetype){
			if(autodetect){
				$('#filetype').val(filetype);
				var filetypeName = $.trim($('#filetype_'+filetype.toLowerCase()+' + label').text());
				if(!filetypeName) filetypeName = '????';
				$('#sel_filetype_btn').val(filetypeName);
			}
			$('#filetype_'+filetype.toLowerCase()).attr('checked', 'checked');
			$('#file_config_fieldset').show();
			$('#config_csv').hide();
			$('#config_excel').hide();
			$('#config_xml').hide();
			if(filetype=='CSV'){
				$('#config_csv').show();
			}else if(filetype=='XLS' || filetype=='XLSX'){
				$('#config_excel').show();
			}else if(filetype=='XML'){
				$('#config_xml').show();
			}else if(filetype=='VCF'){
				
			}else{
				$('#filetype').val('');
				$('#file_config_fieldset').hide();
				$('#filetype_auto').attr('checked', 'checked');
				jprompt.confirm('<s:message code="view.subscriber.impexp.unkowFormat" text="Unkown format! You have to choose file format explicitly."/>',
						function(){$('#filetype_dialog').dialog('open');});
			}
		}
	}
	
	$(document).ready(function() {
		$('#back_btn').click(function(){
			window.location.href='${PATH.imports}/';
		});
		$('#filetype_dialog').dialog({
			autoOpen: false, modal: true,
			width: 580
		});
		$('#sel_filetype_btn').click(function(){
			$('#filetype_dialog').dialog('open');
		});
		$('#sel_filetype_btn').val($.trim($('#filetype_auto + label').text()));
		$('input.filetype_option').click(function(){
			var filetype = $(this).val();
			if(filetype==''){
				$('#file_config_fieldset').hide();
				$('#config_csv').hide();
				$('#config_excel').hide();
				$('#config_xml').hide();
				$('#sel_filetype_btn').val($.trim($('#filetype_auto + label').text()));
				$('#upload_file').val('');
			}
			showConfig(filetype, true);
			$('#filetype_dialog').dialog('close');
		});
		$('#upload_file').change(function(){
			var filename = $(this).val();
			var fpos = filename.lastIndexOf('.');
			var ext = filename.substring(fpos+1, filename.length);
			var filetype = ext.toUpperCase();
			if($('#filetype_auto').attr('checked')=='checked'){
				showConfig(filetype, true);
			}else{
				showConfig(filetype, false);
			}
		});
		$('#main_form').submit(function(){
			//alert(document.forms[0].elements['fileType'].value);
			//return false;
		});
		showConfig('${fileDataSource.fileType}', true);
	});
    </script>
</head>
<body>
	<c:set var="pageTitle" value="Data Import"/>
	<%@include file="/WEB-INF/views/includes/body/header.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/secondary_bar.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/sidebar.jsp" %>
	<section id="main" class="column">
	<form:form id="main_form"  action="${PATH.imports }/file/step1" modelAttribute="fileDataSource" enctype="multipart/form-data">
	<%@include file="/WEB-INF/views/includes/body/form_error.jsp" %>
	<form:hidden path="list.id"/>
	<form:hidden id="filetype" path="fileType"/>
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
					<s:message code="view.subscriber.impexp.fileType" text="File Type"/>
				</label>
				<div class="value">
					<input type="button" id="sel_filetype_btn"/>
					<form:errors path="fileType" cssClass="error"/>
				</div>
			</fieldset>
			<fieldset>
				<label class="field">
					<s:message code="view.subscriber.impexp.chooseFile" text="Choose file to be imported"/>
				</label>
				<div class="value">
					<input type="file" id="upload_file" name="uploadFile"/>
				</div>
			</fieldset>
			<fieldset>
				<label class="field">
					<s:message code="view.subscriber.impexp.config" text="Configurations"/>
				</label>
				<div class="value">
					<form:checkbox id="update_subscriber" path="update" value="true"/>
					<label for="update_subscriber">
						<s:message code="view.subscriber.impexp.update" text="Update existing subscriber"/>
					</label><br/>
					<form:checkbox id="del_src_file" path="deleteSourceFile" value="true"/>
					<label for="del_src_file">
						<s:message code="view.subscriber.impexp.deleteSrcFile" text="Delete source file after finish importing"/></label>
					<br/>
					<form:checkbox id="create_err_file" path="createErrorFile" value="true"/>
					<label for="create_err_file">
						<s:message code="view.subscriber.impexp.createErrFile" text="Create error file as"/>
					</label>
					<br/>
				</div>
			</fieldset>
			<fieldset id="file_config_fieldset" style="display:none;">
				<label class="field">
					<s:message code="view.subscriber.impexp.fileFormatConfig" text="File format configuration"/>
				</label>
				<div class="value">
				<table class="tablesorter" style="width:500px;">
					<thead>
						<tr>
							<th style="width:200px;"><s:message code="view.subscriber.impexp.configName" text="Name"/></th>
							<th style="width:300px;"><s:message code="view.subscriber.impexp.configValue" text="Value"/></th>
						</tr>
					</thead>
					<tbody id="config_csv" style="display:none;">
						<tr>
							<td><s:message code="view.subscriber.impexp.labeled" text="Labeled"/></td>
							<td><form:checkbox path="csv.labeled" value="true"/></td>
						</tr>
						<tr>
							<td><s:message code="view.subscriber.impexp.charset" text="Charset"/></td>
							<td>
								<form:select path="csv.charset" items="${Charsets }" />
							</td>
						</tr>
						<tr>
							<td><s:message code="view.subscriber.impexp.separator" text="Separator Character"/></td>
							<td><form:input path="csv.separator" maxlength="1"/></td>
						</tr>
						<tr>
							<td><s:message code="view.subscriber.impexp.quoteChar" text="Quote Character"/></td>
							<td><form:input path="csv.quoteChar" maxlength="1"/></td>
						</tr>
						<tr>
							<td><s:message code="view.subscriber.impexp.escapeChar" text="Escape Character"/></td>
							<td><form:input path="csv.escapeChar" maxlength="1"/></td>
						</tr>
						<tr>
							<td><s:message code="view.subscriber.impexp.fileFormat" text="File Format"/></td>
							<td>
								<form:select path="csv.fileFormat">
									<form:option value="WIN">Windows</form:option>
									<form:option value="UNIX">Unix</form:option>
									<form:option value="MAC">Mac</form:option>
								</form:select>
							</td>
						</tr>
						
					</tbody>
					<tbody id="config_excel" style="display:none;">
						<tr>
							<td><s:message code="view.subscriber.impexp.labeled" text="Labeled"/></td>
							<td><form:checkbox path="excel.labeled" value="true"/></td>
						</tr>
						<tr>
							<td><s:message code="view.subscriber.impexp.multipleSheets" text="Multiple Sheets"/></td>
							<td>
								<form:checkbox path="excel.multipleSheets" value="true"/>
							</td>
						</tr>
					</tbody>
					<tbody id="config_xml" style="display: none;">
						<tr>
							<td><s:message code="view.subscriber.impexp.charset" text="Charset"/></td>
							<td>
								<form:select path="xml.charset" items="${Charsets }" />
							</td>
						</tr>
						<tr>
							<td><s:message code="view.subscriber.impexp.rootTagName" text="Root Tag Name"/></td>
							<td>
								<form:input path="xml.rootTagName" />
							</td>
						</tr>
						<tr>
							<td><s:message code="view.subscriber.impexp.itemTagName" text="Item Tag Name"/></td>
							<td>
								<form:input path="xml.itemTagName" />
							</td>
						</tr>
						<tr>
							<td><s:message code="view.subscriber.impexp.fileFormat" text="File Format"/></td>
							<td>
								<form:select path="xml.fileFormat">
									<form:option value="WIN">Windows</form:option>
									<form:option value="UNIX">Unix</form:option>
									<form:option value="MAC">Mac</form:option>
								</form:select>
							</td>
						</tr>
					</tbody>
				</table>
				</div>
			</fieldset>
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
	
	<div id="filetype_dialog" title="Select file type">
		<table style="width:550px;background: white;padding: 10px;">
			<tr>
				<td style="text-align: center;width:50px;">
					<img src="${ctx }/resources/images/icons_64/zoom.png"/>
				</td>
				<td>
					<input type="radio" id="filetype_auto" name="_fileType" class="filetype_option" value="" checked="checked" />
					<label for="filetype_auto">
						<strong><s:message code="view.subscriber.impexp.autoDetect" text="Auto Detect"/></strong>
					</label>
					<p style="margin: 5px 0px 0px 25px;">
						This option will let the system choose the right file 
						type for you base on file extension.
					</p>
				</td>
			</tr>
			<tr>
				<td>
					<img src="${ctx }/resources/images/icons_64/csv.png"/>
				</td>
				<td>
					<input type="radio" id="filetype_csv" name="_fileType" class="filetype_option" value="CSV" />
					<label for="filetype_csv">
						<strong><s:message code="view.subscriber.impexp.csv" text="CSV (Comma Separated Values)"/></strong>
					</label>
					<p style="margin: 5px 0px 0px 25px;">
						It's a plain text file that is used to store tabular data.
						Actually you can replace comma by other characters such as vertical bar (|), 
						semicolon (;), colon (:).
					</p>
				</td>
			</tr>
			<tr>
				<td><img src="${ctx }/resources/images/icons_64/excel.png"/></td>
				<td>
					<input type="radio" id="filetype_xlsx" name="_fileType" class="filetype_option" value="XLSX" />
					<label for="filetype_xlsx">
						<strong><s:message code="view.subscriber.impexp.xlsx" text="XLSX (Microsoft Office Excel 2007)"/></strong>
					</label>
					<p style="margin: 5px 0px 0px 25px;">
						XLSX file is a new Microsoft Excel file format. It's a zipped, XML-based file format
						known as Office Open XML or OOXML
					</p>
				</td>
			</tr>
			<tr>
				<td><img src="${ctx }/resources/images/icons_64/excel.png"/></td>
				<td>
					<input type="radio" id="filetype_xls" name="_fileType" class="filetype_option" value="XLS" />
					<label for="filetype_xls">
						<strong><s:message code="view.subscriber.impexp.xls" text="XLS (Microsoft Office Excel 97-2003)"/></strong>
					</label>
					<p style="margin: 5px 0px 0px 25px;">
						The old Microsoft Excel file format. 
					</p>
				</td>
			</tr>
			<tr>
				<td><img src="${ctx }/resources/images/icons_64/xml.png"/></td>
				<td>
					<input type="radio" id="filetype_xml" name="_fileType" class="filetype_option" value="XML" />
					<label for="filetype_xml">
						<strong><s:message code="view.subscriber.impexp.xml" text="XML (Extensible Markup Language)"/></strong>
					</label>
					<p style="margin: 5px 0px 0px 25px;">
						The platform independent and self-describing file format. It's a simple plain text
						file that contains tags and attributes.<br/>
						<strong>Cuation:</strong> Not all xml files can be imported.
					</p>
				</td>
			</tr>
			<tr>
				<td><img src="${ctx }/resources/images/icons_64/vcard.png"/></td>
				<td>
					<input type="radio" id="filetype_vcf" name="_fileType" class="filetype_option" value="VCF" />
					<label for="filetype_vcf">
						<strong><s:message code="view.subscriber.impexp.vcf" text="VCF (Virtual Card File)"/></strong>
					</label>
					<p style="margin: 5px 0px 0px 25px;">
						vCard is a file format standard for electronic business cards.
					</p>
				</td>
			</tr>
		</table>
	</div>
	
	
	<div class="spacer"></div>
	</section>
	
	
	
</body>

</html>