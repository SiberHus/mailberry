<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!doctype html>
<html lang="en">
<head>
	<%@include file="/WEB-INF/views/includes/head/res_standard.jsp" %>
	<%@include file="/WEB-INF/views/includes/head/res_jqgrid.jsp" %>
	<script type="text/javascript">
	function trackCampaign(campaignId){
		window.location.href='${PATH.analytics}/campaigns/track/'+campaignId;
	}
	$(document).ready(function() {
		crud.baseUri = '${PATH.campaigns}';
		$("#main_datagrid").jqGrid({
			url:crud.baseUri+'/list/data',
			colNames:['Actions','Campaign Name', 'Subscriber List', 'Emails', 'Status', 
			          'Status', 'Created By', 'Created At'],
			colModel:[
				{name:'act', width:80, fixed: true, sortable:false, search:false},
				{name:'campaignName', width:100, searchoptions:searchoptions.string},
				{name:'list.listName', width:100, searchoptions:searchoptions.string},
				{name:'emails', width:40, searchoptions:searchoptions.number},
				{name:'statusdisplay', index:'send_time asc, status', width:80},
				{name:'status', hidden: true},
				{name:'createdBy', width:50, searchoptions:searchoptions.date},
				{name:'createdAt', width:50, formatter:'date', searchoptions:searchoptions.date}
			],
			multiselect: true,
			gridComplete: function(){
				var ids = $(this).jqGrid('getDataIDs');
				for(var i=0;i < ids.length;i++){
					var cl = ids[i];
					var rows = $(this).getRowData(cl);
					var act1='',act2='',act3='';
					var status = rows['status'];
					if(status=='DRA' || status=='CAN'){
						act1 = "<input type='image' class='grid_action' src='${ctx}/resources/images/icons/pencil.png' onclick=\"crud.editObject('"+cl+"');\" title='Edit'>";
					}else{
						act1 = "<input type='image' class='grid_action' src='${ctx}/resources/images/icons/chart_pie.png' onclick=\"trackCampaign('"+cl+"');\" title='Report'>";
					}
					act2 = "<input type='image' class='grid_action' src='${ctx}/resources/images/icons/information.png' onclick=\"crud.viewObject('"+cl+"');\" title='View'>";
					if(status=='DRA' || status=='SEN' || status=='CAN'){
						act3 = "<input type='image' class='grid_action' src='${ctx}/resources/images/icons/delete.png' onclick=\"crud.deleteObject('"+cl+"',function(){grid.reload();});\" title='Delete'>";
					}
					$(this).jqGrid('setRowData',ids[i],{act:act1+act2+act3});
				}
			},
			editurl: crud.baseUri+'/delete'
		}).jqGrid('navGrid','#main_pager',{del:true,add:false,edit:false},{},{},{},{multipleSearch:true, multipleGroup:false, showQuery: false});
		$('#main_datagrid').setGridHeight('${gridHeight}');
		grid.resize();
		$('#create_btn').click(function(){
			window.location.href='${PATH.campaigns}/step1';
		});
	});
    </script>
</head>
<body>
	<c:set var="pageTitle" value="Campaigns"/>
	<%@include file="/WEB-INF/views/includes/body/header.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/secondary_bar.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/sidebar.jsp" %>
	
	<section id="main" class="column">
		<article class="module width_full">
			<header>
				<input type="button" id="create_btn" value="Create New" style="float:left;"/>
				<h3 class="tabs_involved">
					<img src="${ctx }/resources/images/icons/transmit_blue.png"/>
					<s:message code="entity.campaign" text="Campaign"/>
				</h3>
				<ul class="tabs">
		   			<li><a href="#listTab"><s:message code="view.text.list" text="List"/></a></li>
		    		<li><a href="#helpTab"><s:message code="view.text.help" text="Help"/></a></li>
				</ul>
			</header>
			<div class="tab_container">
				<div id="listTab" class="tab_content">
					<div id="grid_wrapper" class="datagrid">
						<table id="main_datagrid"></table>
						<div id="main_pager" ></div>
					</div>
				</div>
				<div id="helpTab" class="tab_content">
					<div class="module_content">
					
					</div>
				</div>
			</div>
		</article>
		<div class="spacer"></div>
	</section>
</body>

</html>