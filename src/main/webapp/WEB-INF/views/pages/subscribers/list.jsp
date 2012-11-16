<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html lang="en">
<head>
	<%@include file="/WEB-INF/views/includes/head/res_standard.jsp" %>
	<%@include file="/WEB-INF/views/includes/head/res_jqgrid.jsp" %>
	<script type="text/javascript">
	$(document).ready(function() {
		crud.baseUri = '${PATH.subscribers}';
		var colNames = new Array();
		colNames.push('Actions', 'Email');
		<c:forEach var="fieldName" items="${subscriberList.fieldNames}" varStatus="loop">
		colNames.push('${fieldName}');
		</c:forEach>
		colNames.push('Status', 'Created By', 'Created At');
		$("#main_datagrid").jqGrid({
			url:crud.baseUri+'/${listId}/list/data',
			colNames:colNames,
			colModel:[
				{name:'act', width:80, fixed: true, sortable:false, search:false},
				{name:'email', width:100, searchoptions:searchoptions.string},
				<c:forEach var="fieldName" items="${subscriberList.fieldNames}" varStatus="loop">
				{name:'<s:message text="field${loop.count }Value" />',width:50, hidden:true, searchoptions:searchoptions.string},
				</c:forEach>
				{name:'status', width:50, searchoptions:searchoptions.string},
				{name:'createdBy', width:50, searchoptions:searchoptions.string},
				{name:'createdAt', width:50, formatter:'date', searchoptions:searchoptions.date}
			],
			multiselect: true,
			gridComplete: function(){
				<%@include file="/WEB-INF/views/includes/body/grid_actions.jsp" %>
			},
			editurl: crud.baseUri+'/delete'
		}).jqGrid('navGrid','#main_pager',{del:true,add:false,edit:false},{},{},{},{multipleSearch:true, multipleGroup:false, showQuery: false});
		$('#main_datagrid').setGridHeight('${gridHeight}');
		grid.resize();
		
		$('#create_btn').click(function(){
			window.location.href=crud.baseUri+'/${listId}/add-entry';
		});
		$('#back_btn').click(function(){
			window.location.href='${PATH.subscriberLists}/';
		});
	});
    </script>
</head>
<body>
	<c:set var="pageTitle" value="Subscribers"/>
	<%@include file="/WEB-INF/views/includes/body/header.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/secondary_bar.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/sidebar.jsp" %>
	
	<section id="main" class="column">
		<article class="module width_full">
			<header>
				<input type="button" id="create_btn" value="Create New" style="float:left;"/>
				<h3 class="tabs_involved">
					<img src="${ctx }/resources/images/icons/book_open.png"/>
					<s:message code="entity.subscriber" text="Subscriber"/>
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
			<footer>
			<div class="back_link">
				<input type="submit" id="back_btn" value="Back" />
			</div>
		</footer>
		</article>
		<div class="spacer"></div>
	</section>
</body>

</html>