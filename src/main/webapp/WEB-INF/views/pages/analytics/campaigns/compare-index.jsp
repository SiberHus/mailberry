<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html lang="en">
<head>
	<%@include file="/WEB-INF/views/includes/head/res_standard.jsp" %>
	<%@include file="/WEB-INF/views/includes/head/res_jqgrid.jsp" %>
	<script type="text/javascript" src="${ctx }/resources/js/siberhus.js"></script>
	<style type="text/css">
		table.stat td{ text-align: center; }
	</style>
	<script type="text/javascript">
	var campaignIds = [];
	function getRowCount(tableId){
		var oRows = document.getElementById(tableId).getElementsByTagName('tr');
		return oRows.length;
	}
	function removeCampaign(id, row){
		TableUI.deleteRow(row);
		campaignIds.splice(campaignIds.indexOf(id), 1);
	}
	function addCampaign(data){
		var rowCount = getRowCount('campaigns_tbl');
		if(rowCount>5){
			jprompt.alert('Cannot compare more than 5 campaigns at a time.');
			return false;//break;
		}
		TableUI.addRow('campaigns_tbl', [
			"<input type='hidden' id='campaign_id_"+data.id+"' name='campaignIds' value='"+data.id+"'/>"+data.campaignName,
			data.listName,data.description, data.emails, data.startDate, data.endDate,
			"<a style='cursor:pointer;' href='javascript:void(0);' onclick='removeCampaign("+data.id+",this);'>"+'<input type="image" src="${ctx}/resources/images/icons/delete.png" title="Delete">'+"</a>"
		],function(){
			campaignIds.push(data.id);
		});
		return true;//continue;
	}
	$(document).ready(function() {
		$("#campaign_datagrid").jqGrid({
			url:'${PATH.analytics}/campaigns/list/data/sent',
			pager: '#campaign_datagrid_pager',
			colNames:['Campaign Name', 'Subscriber List', 'Mail Subject', 'Start Date', 'End Date', 'Description', 'Emails', 'Created By', 'Created At'],
			multiselect:true,
			colModel:[
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
		}).jqGrid('navGrid','#campaign_datagrid_pager',{del:false,add:false,edit:false},{},{},{},{multipleSearch:true, multipleGroup:false});
		$('#campaign_datagrid').setGridHeight(260);
		$('#campaign_datagrid').setGridWidth(700);
		$("#campaign_dialog").dialog({
			autoOpen: false,height: 420,width: 730,modal: true,
			buttons: {
				Select: function() {
					var selIds = grid.getSelectedIds('#campaign_datagrid');
					if(selIds==null) return;
					for(var i in selIds){
						var id = selIds[i];
						var rows = $('#campaign_datagrid').getRowData(id);
						var data = [];
						data.id = id;
						if(campaignIds.indexOf(data.id)!=-1){
							continue;
						}
						data.campaignName = rows['campaignName'];
						data.listName = rows['list.listName'];
						data.description = rows['description'];
						data.emails = rows['emails'];
						data.startDate = rows['startDate'];
						data.endDate = rows['endDate'];
						if(!addCampaign(data)){
							break;
						}
					}
					$(this).dialog('close');
				},
				Cancel: function() {
					$(this).dialog('close');
				}
			}
		});
		$('#browse_campaigns').click(function(){
			$('#campaign_dialog').dialog('open');
		});
		$('#campaign_name').click(function(){
			$('#campaign_dialog').dialog('open');
		});
		<c:forEach var="campaign" items="${campaigns}">
		addCampaign({
			id:'${campaign.id}',
			campaignName:'${campaign.campaignName}',
			listName:'${campaign.list.listName}',
			description:'${campaign.description}',
			emails:'${campaign.emails}',
			startDate:'${campaign.startDate}',
			endDate:'${campaign.endDate}'
		});
		</c:forEach>
	});
    </script>
</head>
<body>
	<c:set var="pageTitle" value="Campaign Comparison"/>
	<%@include file="/WEB-INF/views/includes/body/header.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/secondary_bar.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/sidebar.jsp" %>
	<section id="main" class="column">
	<form id="main_form" action="${PATH.analytics }/campaigns/compare/query" method="post">
	<article class="module width_full">
		<header>
			<h3>
			<img src="${ctx }/resources/images/icons/chart_bar.png" />
			Campaign Comparison
			</h3>
		</header>
		<div class="module_content">
			<fieldset>
				<label class="field">
					&nbsp;<input id="browse_campaigns" type="button" value="+ Add" />
					<s:message code="lbl.analytics.campaigns.addCampaign" text="Please add at least one campaign and click compare button"/>
				</label>
				<div class="value">
					<table id="campaigns_tbl" class="tablesorter">
						<thead>
						<tr id="campaigns_th">
							<th style="text-align:left;width:20%">
								<s:message code="entity.campaign.campaignName" text="Campaign Name"/>
							</th>
							<th style="text-align:left;width:20%">
								<s:message code="entity.subscriberList.listName" text="List Name"/>
							</th>
							<th style="text-align:left;width:25%">
								<s:message code="entity.campaign.description" text="Description"/>
							</th>
							<th style="text-align:left;width:10%">
								<s:message code="entity.campaign.emails" text="Emails"/>
							</th>
							<th style="text-align:left;width:10%">
								<s:message code="entity.campaign.startDate" text="Start Date"/>
							</th>
							<th style="text-align:left;width:10%">
								<s:message code="entity.campaign.endDate" text="End Date"/>
							</th>
							<th style="text-align:left;width:5%">&nbsp;</th>
						</tr>
						</thead>
						<tbody></tbody>
					</table>
				</div>
			</fieldset>
			<div class="clear"></div>
		</div>
		<footer>
			<div class="submit_link">
				<input type="submit" id="compare_btn" value="Compare" />
			</div>
		</footer>
	</article>
	</form>
	<div class="spacer"></div>
	</section>
	
	<div id="campaign_dialog" class="datagrid" title="Select campaign">
		<table id="campaign_datagrid"></table>
		<div id="campaign_datagrid_pager"></div>
	</div>
	
</body>

</html>