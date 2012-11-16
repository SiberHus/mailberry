<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html lang="en">
<head>
	<%@include file="/WEB-INF/views/includes/head/res_standard.jsp" %>
	<%@include file="/WEB-INF/views/includes/head/res_jqgrid.jsp" %>
	<script type="text/javascript">
	$(document).ready(function() {
		crud.baseUri = '${PATH.templateChunks}';
		$("#main_datagrid").jqGrid({
			url:crud.baseUri+'/list/data',
			colNames:['Actions','Chunk Name', 'Chunk Value', 'Status', 'Created By', 'Created At'],
			colModel:[
				{name:'act', width:80, fixed: true, sortable:false, search:false},
				{name:'name', width:80, searchoptions:searchoptions.string},
				{name:'value', width:100, search:false},
				{name:'status', width:100, searchoptions:searchoptions.string},
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
			window.location.href='${PATH.templateChunks}/create';
		});
	});
    </script>
</head>
<body>
	<c:set var="pageTitle" value="Template Chunks"/>
	<%@include file="/WEB-INF/views/includes/body/header.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/secondary_bar.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/sidebar.jsp" %>
	
	<section id="main" class="column">
		<article class="module width_full">
			<header>
				<input type="button" id="create_btn" value="Create New" style="float:left;"/>
				<h3 class="tabs_involved">
					<img src="${ctx }/resources/images/icons/script_code.png"/>
					<s:message code="entity.templateChunk" text="Template Chunk"/>
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
					<h1>Header 1</h1>
					<h2>Header 2</h2>
					<h3>Header 3</h3>
					<h4>Header 4</h4>
					<p>Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Cras mattis consectetur purus sit amet fermentum. Maecenas faucibus mollis interdum. Maecenas faucibus mollis interdum. Cras justo odio, dapibus ac facilisis in, egestas eget quam.</p>
					<p>Donec id elit non mi porta <a href="#">link text</a> gravida at eget metus. Donec ullamcorper nulla non metus auctor fringilla. Cras mattis consectetur purus sit amet fermentum. Aenean eu leo quam. Pellentesque ornare sem lacinia quam venenatis vestibulum.</p>
					<ul>
						<li>Donec ullamcorper nulla non metus auctor fringilla. </li>
						<li>Cras mattis consectetur purus sit amet fermentum.</li>
						<li>Donec ullamcorper nulla non metus auctor fringilla. </li>
						<li>Cras mattis consectetur purus sit amet fermentum.</li>
					</ul>
				</div>
				</div>
			</div>
		</article>
		<div class="spacer"></div>
	</section>
</body>

</html>