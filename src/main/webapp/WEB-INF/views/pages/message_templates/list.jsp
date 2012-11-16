<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html lang="en">
<head>
	<%@include file="/WEB-INF/views/includes/head/res_standard.jsp" %>
	<%@include file="/WEB-INF/views/includes/head/res_jqgrid.jsp" %>
	<script type="text/javascript">
	$(document).ready(function() {
		crud.baseUri = '${PATH.messageTemplates}';
		$("#main_datagrid").jqGrid({
			url:crud.baseUri+'/list/data',
			colNames:['Actions','Template Name', 'Description', 'Created By', 'Created At'],
			colModel:[
				{name:'act', width:80, fixed: true, sortable:false, search:false},
				{name:'templateName', width:100, searchoptions:searchoptions.string},
				{name:'description', width:100, searchoptions:searchoptions.string},
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
			window.location.href='${PATH.messageTemplates}/create';
		});
	});
    </script>
</head>
<body>
	<c:set var="pageTitle" value="Message Templates"/>
	<%@include file="/WEB-INF/views/includes/body/header.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/secondary_bar.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/sidebar.jsp" %>
	
	<section id="main" class="column">
		<article class="module width_full">
			<header>
				<input type="button" id="create_btn" value="Create New" style="float:left;"/>
				<h3 class="tabs_involved">
					<img src="${ctx }/resources/images/icons/html.png"/>
					<s:message code="entity.messageTemplate" text="Message Template"/>
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
					<jsp:include page="/WEB-INF/views/help/en/message_templates.jsp"/>
					</div>
				</div>
			</div>
		</article>
		<div class="spacer"></div>
	</section>
</body>

</html>