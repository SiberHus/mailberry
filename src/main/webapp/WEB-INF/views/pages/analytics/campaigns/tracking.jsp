<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="esc" uri="http://commons.apache.org/lang/StringEscapeUtils" %>
<!doctype html>
<html lang="en">
<head>
	<%@include file="/WEB-INF/views/includes/head/res_standard.jsp" %>
	<%@include file="/WEB-INF/views/includes/head/res_jqgrid.jsp" %>
	<%@include file="/WEB-INF/views/includes/head/res_visualize.jsp" %>
	<script type="text/javascript" src="${ctx }/resources/js/siberhus.js"></script>
	<style type="text/css">
		div.chart{display:block; margin:0px auto;width:750px;margin-bottom: 30px;}
		div.top_link{float:right;font-size: 1.5em;font-weight: bold;}
		table.stat td{ text-align: center; }
		table.fieldset td.field{font-weight: bold;color:#888888; width:150px;}
		table.fieldset td.value{width:200px;}
	</style>
	<script type="text/javascript">
	// setup control widget
	var timer = null;
	var updateInterval = 5;
	var minInterval = 5, maxInterval = 30;
	function updateTracking() {
		$.post('${PATH.analytics}/campaigns/track/query',{campaignId:$('#campaign_id').val()},function(data){
			$('#stat_emails').text(data.emails);
			$('#stat_successes').text(data.successes);
			$('#stat_hardBounces').text(data.hardBounces);
			$('#stat_softBounces').text(data.softBounces);
			$('#stat_opens').text(data.opens);
			$('#stat_clicks').text(data.clicks);
			$('#stat_forwards').text(data.forwards);
			$('#stat_optOuts').text(data.optOuts);
			$('#stat_rsvps').text(data.rsvps);
			$('#stat_successRate').text(data.successRate);
			$('#stat_hardBounceRate').text(data.hardBounceRate);
			$('#stat_softBounceRate').text(data.softBounceRate);
			$('#stat_openRate').text(data.openRate);
			$('#stat_forwardRate').text(data.forwardRate);
			$('#stat_optOutRate').text(data.optOutRate);
			$('#stat_rsvpRate').text(data.rsvpRate);
			
			$.each(data.clickstreams, function(idx, elem){
				if(document.getElementById('click_progress_'+idx)){
					$('#click_url_'+idx).text(elem.url);
					$('#click_count_'+idx).text(elem.count);
					$('#click_progress_'+idx).progressbar({ value: elem.rate });
					
				}else{
					TableUI.addRow('clickstream_tbl', [
						"<span id='click_url_"+idx+"'>"+elem.url+"</span>"+
						"<div id='click_progress_"+idx+"'></div>",
						"<span id='click_count_"+idx+"'>"+elem.count+"</span>"
					],function(){
						$('#click_progress_'+idx).progressbar({ value: elem.rate });
					});
				}
			});
			
			$('.visualize').trigger('visualizeRefresh');
			timer = setTimeout(updateTracking, updateInterval*1000);
		});
    }
    
	$(document).ready(function() {
		$('#total_stats_tbl').visualize({type: 'bar', width:'600px'})
			.appendTo('#total_stats_chart').trigger('visualizeRefresh');
		$("#update_interval").val(updateInterval).change(function () {
			var v = $(this).val();
			if (v && !isNaN(+v)) {
				v = +v;
				if (v < minInterval)
					updateInterval = minInterval;
				else if (v > maxInterval)
					updateInterval = maxInterval;
				else
					updateInterval = v;
				$(this).val("" + updateInterval);
			}
		});
		$('#update_interval').val(updateInterval);
		$("#campaign_datagrid").jqGrid({
			url:'${PATH.analytics}/campaigns/list/data/all',
			pager: '#campaign_datagrid_pager',
			colNames:['Campaign Name', 'Subscriber List', 'Mail Subject', 'Start Date', 'End Date', 'Description', 'Emails', 'Created By', 'Created At'],
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
					var selId = grid.getSelectedId('#campaign_datagrid');
					if(selId==null) return;
					var rows = $('#campaign_datagrid').getRowData(selId);
					$('#campaign_name_txt').text(rows['campaignName']);
					$('#list_name_txt').text(rows['list.listName']);
					$('#description_txt').text(rows['description']);
					$('#start_date_txt').text(rows['startDate']);
					$('#end_date_txt').text(rows['endDate']);
					$('#mail_subject_txt').text(rows['mailSubject']);
					$('#created_by_txt').text(rows['createdBy']);
					$('#created_at_txt').text(rows['createdAt']);
					$('#campaign_id').val(selId);
					
					if(timer){
						clearTimeout(timer);
					}
					updateTracking();
					
					$(this).dialog('close');
				}
			}
		});
		$('#change_campaign_lnk').click(function(){
			$('#campaign_dialog').dialog('open');
		});
		
		<c:if test="${empty campaign}">
		$('#campaign_dialog').dialog('open');
		</c:if>
		<c:if test="${not empty campaign}">
		$('#campaign_name_txt').text('${esc:escapeJavaScript(campaign.campaignName)}');
		$('#list_name_txt').text('${esc:escapeJavaScript(campaign.list.listName)}');
		$('#description_txt').text('${esc:escapeJavaScript(campaign.description)}');
		$('#start_date_txt').text('${campaign.startDate}');
		$('#end_date_txt').text('${campaign.endDate}');
		$('#mail_subject_txt').text('${esc:escapeJavaScript(campaign.mailSubject)}');
		$('#created_by_txt').text('${campaign.createdBy}');
		$('#created_at_txt').text('${campaign.createdAt}');
		$('#campaign_id').val('${campaign.id}');
		updateTracking();
		</c:if>
		
		
	});
    </script>
</head>
<body>
	<c:set var="pageTitle" value="Campaign Tracking"/>
	<%@include file="/WEB-INF/views/includes/body/header.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/secondary_bar.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/sidebar.jsp" %>
	<section id="main" class="column">
	
	<article class="module width_full">
		<header>
			<h3>
			<img src="${ctx }/resources/images/icons/chart_bar.png" />
			Campaign Tracking
			</h3>
		</header>
		<div class="module_content" >
			<fieldset>
				<div style="float:right;">
					Time between updates (seconds):
					<input type="text" id="update_interval" style="width:50px;"/>
				</div>
				<label class="field">
				Campaign Information&nbsp;&nbsp;
				(<a id="change_campaign_lnk" href="javascript:void(0);">change</a>) 
				</label>
				<div class="value">
					<input type="hidden" id="campaign_id" />
					<table class="fieldset">
						<tbody>
							<tr>
								<td class="field">Campaign Name: </td>
								<td class="value"><span id="campaign_name_txt"/></td>
								<td colspan="2">&nbsp;</td>
							</tr>
							<tr>
								<td class="field">List Name: </td>
								<td class="value"><span id="list_name_txt"/></td>
								<td colspan="2">&nbsp;</td>
							</tr>
							<tr>
								<td class="field">Description: </td>
								<td colspan="3"><span id="description_txt"/></td>
							</tr>
							<tr>
								<td class="field">Start Date: </td>
								<td class="value"><span id="start_date_txt"/></td>
								<td class="field">End Date: </td>
								<td class="value"><span id="end_date_txt"/></td>
							</tr>
							<tr>
								<td class="field">Email Subject: </td>
								<td colspan="3"><span id="mail_subject_txt"/></td>
							</tr>
							<tr>
								<td class="field">Created By: </td>
								<td class="value"><span id="created_by_txt"/></td>
								<td class="field">Created At: </td>
								<td class="value"><span id="created_at_txt"/></td>
							</tr>
						</tbody>
					</table>
				</div>
			</fieldset>
			<div style="margin:0px 0px 30px 0px;"></div>
			<div class="graph_wrapper">
				<div id="total_stats_chart" class="chart"></div>
				<div class="center" style="margin:0px auto;width:80%">
				<table id="total_stats_tbl" class="stat" style="width:100%;">
					<caption>Campaign Tracking</caption>
					<thead>
						<tr>
							<th scope="col" style="width:11%;">Emails</th>
							<th scope="col" style="width:11%;">Successes</th>
							<th scope="col" style="width:11%;">Hard Bounces</th>
							<th scope="col" style="width:11%;">Soft Bounce</th>
							<th scope="col" style="width:11%;">Opens</th>
							<th scope="col" style="width:11%;">Clicks</th>
							<th scope="col" style="width:11%;">RSVPs</th>
							<th scope="col" style="width:11%;">Forwards</th>
							<th scope="col" style="width:11%;">Unsubscribes</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td><span id="stat_emails">0</span></td>
							<td><span id="stat_successes">0</span></td>
							<td><span id="stat_hardBounces">0</span></td>
							<td><span id="stat_softBounces">0</span></td>
							<td><span id="stat_opens">0</span></td>
							<td><span id="stat_clicks">0</span></td>
							<td><span id="stat_rsvps">0</span></td>
							<td><span id="stat_forwards">0</span></td>
							<td><span id="stat_optOuts">0</span></td>
						</tr>
					</tbody>
				</table>
				<table class="stat" style="width:100%;">
					<thead>
						<tr></tr>
					</thead>
					<tbody>
						<tr>
							<td style="width:11%;">N/A</td>
							<td style="width:11%;">
								<span id="stat_successRate">0</span>%
							</td>
							<td style="width:11%;">
								<span id="stat_hardBounceRate">0</span>%
							</td>
							<td style="width:11%;">
								<span id="stat_softBounceRate">0</span>%
							</td>
							<td style="width:11%;">
								<span id="stat_openRate">0</span>%
							</td>
							<td style="width:11%;">N/A</td>
							<td style="width:11%;">
								<span id="stat_rsvpRate">0</span>%
							</td>
							<td style="width:11%;">
								<span id="stat_forwardRate">0</span>%
							</td>
							<td style="width:11%;">
								<span id="stat_optOutRate">0</span>%
							</td>
						</tr>
					</tbody>
				</table>
				
				<div style="margin:0px 0px 30px 0px;"></div>
				<table id="clickstream_tbl" class="stat" style="width:100%;">
					<caption>Clickstream</caption>
					<thead>
						<tr>
							<th scope="col" style="width:78%;">URL</th>
							<th scope="col" style="width:22%;">Clicks</th>
						</tr>
					</thead>
					<tbody></tbody>
				</table>
				<div style="margin:0px 0px 30px 0px;"></div>
				
				</div>
				
			</div>
			<div class="clear"></div>
		</div>
	</article>
	<div class="spacer"></div>
	</section>
	
	
	<div id="campaign_dialog" class="datagrid" title="Select campaign">
		<div class="grid_wrapper">
			<table id="campaign_datagrid"></table>
		</div>
		<div id="campaign_datagrid_pager"></div>
	</div>
	
</body>

</html>