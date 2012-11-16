<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!doctype html>
<html lang="en">
<head>
	<%@include file="/WEB-INF/views/includes/head/res_standard.jsp" %>
	<%@include file="/WEB-INF/views/includes/head/res_jqgrid.jsp" %>
	<style type="text/css">
		div.chart{display:block; margin:0px auto;width:750px;margin-bottom: 30px;}
		div.top_link{float:right;font-size: 1.5em;font-weight: bold;}
		table.stat td{ text-align: center; }
		table.fieldset td.field{font-weight: bold;color:#888888; width:150px;}
		table.fieldset td.value{width:200px;}
	</style>
	<script type="text/javascript">
	// setup control widget
    
	$(document).ready(function() {
		$("#main_datagrid").jqGrid({
			url:'${PATH.analytics}/campaigns/list/data/all',
			pager: '#main_datagrid_pager',
			colNames:['Campaign Name', 'Subscriber List', 'Mail Subject', 'Start Date', 'End Date', 'Description', 'Emails', 'Created By', 'Created At'],
			colModel:[
				/*{name:'act', width:35, fixed: true, sortable:false, search:false},*/
				{name:'campaignName', width:100, searchoptions:searchoptions.string},
				{name:'list.listName', width:100, searchoptions:searchoptions.string},
				{name:'mailSubject', hidden: true, searchoptions:searchoptions.string},
				{name:'startDate', hidden: true, formatter:'date', searchoptions:searchoptions.date},
				{name:'endDate', hidden: true, formatter:'date', searchoptions:searchoptions.date},
				{name:'description', hidden: true, searchoptions:searchoptions.string},
				{name:'emails', width:80, fixed: true, searchoptions:searchoptions.number},
				{name:'createdBy', width:50, searchoptions:searchoptions.string},
				{name:'createdAt', width:50, formatter:'date', searchoptions:searchoptions.date}
			]
			/*
			,gridComplete: function(){
				var ids = $(this).jqGrid('getDataIDs');
				for(var i=0;i < ids.length;i++){
					var cl = ids[i];
					var rows = $(this).getRowData(cl);
					var act='';
					act = "<input type='image' class='grid_action' src='${ctx}/resources/images/icons/information.png' onclick=\"goToResultPage('"+cl+"');\" title='View'>";
					$(this).jqGrid('setRowData',ids[i],{act:act});
				}
			}
			*/
		}).jqGrid('navGrid','#main_datagrid_pager',{del:false,add:false,edit:false},{},{},{},{multipleSearch:true, multipleGroup:false});
		$('#main_datagrid').setGridHeight('${gridHeight}');
		grid.resize();
		
		$('#analyze_btn').click(function(){
			var id = grid.getSelectedId('#main_datagrid');
			if(id==null)return;
			window.location.href='${PATH.analytics}/clientinfo/'+id;
		});
	});
	function goToResultPage(id){
		window.location.href='${PATH.analytics}/clientinfo/'+id;
	}
    </script>
</head>
<body>
	<c:set var="pageTitle" value="Client Information"/>
	<%@include file="/WEB-INF/views/includes/body/header.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/secondary_bar.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/sidebar.jsp" %>
	<section id="main" class="column">
	
	<article class="module width_full">
		<header>
			<h3>
			<img src="${ctx }/resources/images/icons/chart_bar.png" />
			Client Info Analytics
			</h3>
		</header>
		<div class="module_content" >
			<div class="grid_wrapper">
				<table id="main_datagrid"></table>
				<div id="main_datagrid_pager"></div>
			</div>
			<div class="clear"></div>
		</div>
		<footer>
			<div class="submit_link">
				<input type="submit" id="analyze_btn" value="Analyze" />
			</div>
		</footer>
	</article>
	<div class="spacer"></div>
	</section>
	
</body>

</html>