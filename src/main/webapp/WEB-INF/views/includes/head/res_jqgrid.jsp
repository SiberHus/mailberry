<link rel="stylesheet" media="screen,projection" type="text/css" href="${ctx }/resources/comps/jqgrid/ui.jqgrid.css" />
<script type="text/javascript" src="${ctx }/resources/comps/jqgrid/i18n/grid.locale-en.js"></script>
<script type="text/javascript" src="${ctx }/resources/comps/jqgrid/jquery.jqGrid.min.js"></script>
<script type="text/javascript" src="${ctx }/resources/comps/jqgrid/jquery.jqGrid.fluid.js"></script>
<script type="text/javascript">
	function alertNoRowSelected(){
		alert('<s:message code="alert.noRowSelected" text="Please select row"/>');
	}
	var searchoptions = [];
	searchoptions.string = {searchhidden:true, sopt:['eq','ne','bw','bn','ew','en','cn','nc'] };
	searchoptions.number = {searchhidden:true, sopt:['eq','ne','lt','le','gt','ge','in','ni'] };
	searchoptions.date = {searchhidden:true, sopt:['eq','ne','lt','le','gt','ge'],dataInit : function(elem){$(elem).datepicker();}};
	searchoptions.bool = {searchhidden:true, sopt:['eq','ne']};
	
	var grid = [];//grid functions
	grid.resize = function(gridExp){
		if(gridExp)
			$(gridExp).fluidGrid({base:'div.grid_wrapper', offset:0});
		else
			$('#main_datagrid').fluidGrid({base:'div.grid_wrapper', offset:0});
	};
	grid.reload = function(gridExp){
		if(gridExp) $(gridExp).trigger('reloadGrid');
		else $('#main_datagrid').trigger('reloadGrid');
	};
	grid.getSelectedIds = function(gridExp){
		var selIds = $(gridExp).jqGrid('getGridParam','selarrrow');
		/*for(var i in selIds){
			alert(selIds[i]);
		}*/
		if (selIds)	{
			return selIds;
		} else { alertNoRowSelected(); return null; }
	};
	grid.getSelectedId = function(gridExp){
		var selId = $(gridExp).jqGrid('getGridParam','selrow');
		if (selId)	{
			return selId;
		} else { alertNoRowSelected(); return null; }
	};
	grid.getSelectedValues = function(gridExp){
		var selId = $(gridExp).jqGrid('getGridParam','selrow');
		if (selId)	{
			return $(gridExp).getRowData(selId);
		} else { alertNoRowSelected(); return null; }
	};
	$(function(){
		//exists fn is defined in res_standard.jsp
		if($('#main_datagrid').exists()){
			$(window).resize(function() {
				grid.resize($('#main_datagrid'));
			});
		}
		var rowNum = ${gridRowNum};
		var rowList = '${gridRowList}'.split(',');
		$.extend($.jgrid.defaults, {
			datatype: 'json',
			rowNum:rowNum, rowList:rowList,
			pager: '#main_pager', pagerpos: 'center',
			sortname: 'id',sortorder: 'desc',
			viewrecords: true, recordpos: 'right',
			width: 'auto',height: 'auto'
		});
	});
</script>