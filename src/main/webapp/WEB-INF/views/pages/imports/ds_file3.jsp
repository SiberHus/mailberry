<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="stringutils" uri="http://commons.apache.org/lang/StringUtils" %>
<!doctype html>
<html lang="en">
<head>
	<%@include file="/WEB-INF/views/includes/head/res_standard.jsp" %>
	<style type="text/css">
		
	</style>
	<script type="text/javascript">
	var updateInterval = 800;
	function checkProgress(){
		$.get('${PATH.imports}/file/progress/${trackingId}', function(data) {
			$('#created_count').text(data.created);
			$('#updated_count').text(data.updated);
			$('#success_count').text(data.success);
			$('#error_count').text(data.error);
			if(!data.finish){
				setTimeout(checkProgress, updateInterval);
			}else{
				window.location.href='${PATH.imports}/file/track/${trackingId}';
			}
		});
	}
	$(document).ready(function() {
		$('#done_btn').click(function(){
			window.location.href = '${PATH.imports}/';
		});
		$('#err_download_btn').click(function(){
			<c:if test="${not fileImport.hasErrorFile}">
			jprompt.alert('no error file');
			</c:if>
			<c:if test="${fileImport.hasErrorFile}">
			window.location.href='${PATH.imports}/file/download/${trackingId}/error';
			</c:if>
		});
		$('#src_download_btn').click(function(){
			<c:if test="${not fileImport.hasSourceFile}">
			jprompt.alert('source file was deleted');
			</c:if>
			<c:if test="${fileImport.hasSourceFile}">
			window.location.href='${PATH.imports}/file/download/${trackingId}/source';
			</c:if>
		});
		<c:if test="${fileImport.status ne 'DONE'}">
		setTimeout(checkProgress, updateInterval);
		</c:if>
		<c:if test="${fileImport.status eq 'DONE'}">
		$('#proc_display').hide();
		$('#done_display').show();
		</c:if>
	});
    </script>
</head>
<body>
	<c:set var="pageTitle" value="Data Import"/>
	<%@include file="/WEB-INF/views/includes/body/header.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/secondary_bar.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/sidebar.jsp" %>
	<section id="main" class="column">
	<article class="module width_full" style="1500px;">
		<header>
			<h3>
			<img src="${ctx }/resources/images/icons/arrow_up.png" />
			<s:message code="view.subscriber.import" text="Import"/>
			</h3>
		</header>
		<div class="module_content">
			<article class="stats_graph">
				<fieldset>
					<label class="field">
						<s:message code="lbl.import.import2list" text="Import to List"/>
					</label>
					<div class="value">
						<s:message text="${fileImport.list }"/>
					</div>
				</fieldset>
				<fieldset style="width:48%; float:left; margin-right: 3%;">
					<label class="field">
						<s:message code="lbl.import.originalFileName" text="Original File Name"/>
					</label>
					<div class="value">
						<s:message text="${fileImport.originalName }"/>
					</div>
				</fieldset>
				<fieldset style="width:48%; float:left;">
					<label class="field">
						<s:message code="lbl.import.fileType" text="File Type"/>
					</label>
					<div class="value">
						<s:message text="${fileImport.fileType }"/>
					</div>
				</fieldset>
				<div class="clear"></div>
				<c:if test="${not empty fileImport.fatalError}">
				<fieldset>
					<label class="field">
						<s:message code="lbl.import.errorMsg" text="Error Message"/>
					</label>
					<div class="value">
						<s:message text="${fileImport.fatalError }"/>
					</div>
				</fieldset>
				</c:if>
				<c:set var="command" value="${fileImport }"/>
				<%@include file="/WEB-INF/views/includes/body/view_audit_info.jsp" %>
				<div style="text-align: center;margin: 25px 0px 10px 0px;">
					<h3>
						<img src="${ctx }/resources/images/icons/disk.png"/>&nbsp;
						<s:message code="lbl.import.download" text="Download"/>
					</h3>
					<input type="button" id="src_download_btn" value="Source File" style="width:85%"/>
					<br/>
					<input type="button" id="err_download_btn" value="Error File" style="width:85%"/>
				</div>
			</article>
			<article class="stats_overview">
				<div id="proc_display" style="text-align: center;">
					<img src="${ctx }/resources/images/ajax/community128.gif"/>
					<h3><s:message code="lbl.import.processing" text="Processing..."/></h3>
				</div>
				<div id="done_display" style="text-align: center;display: none;">
					<img src="${ctx }/resources/images/ajax/community128_still.gif"/>
					<h3><s:message code="lbl.import.done" text="Done"/></h3>
				</div>
				<div class="overview_today">
					<p class="overview_day">Subscriber</p>
					<p id="created_count" class="overview_count">${fileImport.created }</p>
					<p class="overview_type">Created</p>
					<p id="updated_count" class="overview_count">${fileImport.updated}</p>
					<p class="overview_type">Updated</p>
				</div>
				<div class="overview_previous">
					<p class="overview_day">Result</p>
					<p id="success_count" class="overview_count">${fileImport.success }</p>
					<p class="overview_type">Success</p>
					<p id="error_count" class="overview_count">${fileImport.error }</p>
					<p class="overview_type">Error</p>
				</div>
			</article>
			<div class="clear"></div>
			
		</div>
		<footer>
			<div class="submit_link">
				<input type="submit" id="done_btn" value="Done" />
			</div>
		</footer>
	</article>
	<div class="spacer"></div>
	</section>
	
	
	
</body>

</html>