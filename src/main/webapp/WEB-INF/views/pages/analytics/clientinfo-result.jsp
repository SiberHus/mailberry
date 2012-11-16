<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
		table.fieldset td.field{font-weight: bold;color:#888888; width:150px;}
		table.fieldset td.value{width:200px;}
	</style>
	<script type="text/javascript">
	$(document).ready(function() {
		$('#ua_types_tbl').visualize({type: 'pie', height: '250px', width: '600px'})
			.appendTo('#ua_types_chart').trigger('visualizeRefresh');
		$('#ua_names_tbl').visualize({type: 'pie', height: '250px', width: '600px'})
			.appendTo('#ua_names_chart').trigger('visualizeRefresh');
		$('#os_families_tbl').visualize({type: 'pie', height: '250px', width: '600px'})
			.appendTo('#os_families_chart').trigger('visualizeRefresh');
		$('#os_names_tbl').visualize({type: 'pie', height: '250px', width: '600px'})
			.appendTo('#os_names_chart').trigger('visualizeRefresh');
		
		$('#back_btn').click(function(){
			window.location.href='${PATH.analytics}/clientinfo';
		});
		$('#change_campaign_lnk').click(function(){
			window.location.href='${PATH.analytics}/clientinfo';
		});
	});
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
		<div class="module_content">
			<a name="top"></a>
			<fieldset>
				<label class="field">
				Campaign Information&nbsp;&nbsp;
				(<a id="change_campaign_lnk" href="javascript:void(0);">change</a>) 
				</label>
				<div class="value">
					<input type="hidden" id="campaign_id" value="${campaign.id }"/>
					<table class="fieldset">
						<tbody>
							<tr>
								<td class="field">Campaign Name: </td>
								<td class="value"><s:message text="${campaign.campaignName }"/></td>
								<td colspan="2">&nbsp;</td>
							</tr>
							<tr>
								<td class="field">List Name: </td>
								<td class="value"><s:message text="${campaign.list.listName }"/></td>
								<td colspan="2">&nbsp;</td>
							</tr>
							<tr>
								<td class="field">Description: </td>
								<td colspan="3"><s:message text="${campaign.description }"/></td>
							</tr>
							<tr>
								<td class="field">Start Date: </td>
								<td class="value">${campaign.startDate }</td>
								<td class="field">End Date: </td>
								<td class="value">${campaign.endDate }</td>
							</tr>
							<tr>
								<td class="field">Email Subject: </td>
								<td colspan="3"><s:message text="${campaign.mailSubject }"/></td>
							</tr>
							<tr>
								<td class="field">Created By: </td>
								<td class="value">${campaign.createdBy }</td>
								<td class="field">Created At: </td>
								<td class="value">${campaign.createdAt }</td>
							</tr>
						</tbody>
					</table>
				</div>
			</fieldset>
			<div style="margin:0px 0px 30px 0px;"></div>
			<div class="graph_wrapper">
				<div class="center" style="margin:0px auto;width:60%">
					<table id="user_agents_tbl" style="width:100%;">
						<caption>User-Agent Details</caption>
						<thead>
							<tr>
								<th style="width:30%;text-align: left;">Type</th>
								<th style="width:40%;text-align: left;">Name</th>
								<th style="width:15%;text-align: center;">Total</th>
							</tr>
						</thead>
						<tbody>
						<c:forEach var="uaInfo" items="${stats.uaInfos }">
							<tr>
								<td style="text-align: left;">
									<c:if test="${not empty uaInfo.type }">
										${uaInfo.type }
									</c:if>
									<c:if test="${empty uaInfo.type }">
										Unknown
									</c:if>
								</td>
								<td style="text-align: left;">
									<c:if test="${not empty uaInfo.name }">
									<img src="${ctx}/resources/images/ico_ua/${uaInfo.icon }"/>
									${uaInfo.name }&nbsp;${uaInfo.version }
									</c:if>
									<c:if test="${empty uaInfo.name }">
										Unknown
									</c:if>
								</td>
								<td style="text-align: center;">${uaInfo.count }</td>
							</tr>
						</c:forEach>
						</tbody>
					</table>
				</div>
				
				<div style="margin:0px 0px 20px 0px;"></div>
				
				<div id="ua_types_chart" class="chart">
					<a name="ua_types_chart"></a>
					<div class="top_link"><a href="#top">Top</a></div>
				</div>
				<table id="ua_types_tbl" style="display:none;">
					<caption>User Agent Types</caption>
					<thead><tr></tr></thead>
					<tbody>
					<c:forEach var="stat" items="${stats.uaTypes }" varStatus="loop">
						<tr>
							<th scope="row">${stat.key}</th>
							<td>${stat.value}</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
				
				<div id="ua_names_chart" class="chart">
					<a name="ua_names_chart"></a>
					<div class="top_link"><a href="#top">Top</a></div>
				</div>
				<table id="ua_names_tbl" style="display:none;">
					<caption>User Agent Names</caption>
					<thead><tr></tr></thead>
					<tbody>
					<c:forEach var="stat" items="${stats.uaNames }" varStatus="loop">
						<tr>
							<th scope="row">${stat.key}</th>
							<td>${stat.value}</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
				
				<div class="center" style="margin:0px auto;width:60%">
					<table style="width:100%;">
						<caption>Operating System Details</caption>
						<thead>
							<tr>
								<th style="width:30%;text-align: left;">Family</th>
								<th style="width:40%;text-align: left;">Name</th>
								<th style="width:15%;text-align: center;">Total</th>
							</tr>
						</thead>
						<tbody>
						<c:forEach var="osInfo" items="${stats.osInfos }">
							<tr>
								<td style="text-align: left;">
									<c:if test="${not empty osInfo.family }">
										${osInfo.family }
									</c:if>
									<c:if test="${empty osInfo.family }">
										Unknown
									</c:if>
								</td>
								<td style="text-align: left;">
									<c:if test="${not empty osInfo.name }">
									<img src="${ctx}/resources/images/ico_os/${osInfo.icon }"/>
									${osInfo.name }
									</c:if>
									<c:if test="${empty osInfo.name }">
										Unknown
									</c:if>
								</td>
								<td style="text-align: center;">${osInfo.count }</td>
							</tr>
						</c:forEach>
						</tbody>
					</table>
				</div>
				<div style="margin:0px 0px 20px 0px;"></div>
				
				<div id="os_families_chart" class="chart">
					<a name="os_families_chart"></a>
					<div class="top_link"><a href="#top">Top</a></div>
				</div>
				<table id="os_families_tbl" style="display:none;">
					<caption>Operating System Families</caption>
					<thead><tr></tr></thead>
					<tbody>
					<c:forEach var="stat" items="${stats.osFamilies }" varStatus="loop">
						<tr>
							<th scope="row">${stat.key}</th>
							<td>${stat.value}</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
				
				<div id="os_names_chart" class="chart">
					<a name="os_names_chart"></a>
					<div class="top_link"><a href="#top">Top</a></div>
				</div>
				<table id="os_names_tbl" style="display:none;">
					<caption>Operating System Names</caption>
					<thead><tr></tr></thead>
					<tbody>
					<c:forEach var="stat" items="${stats.osNames }" varStatus="loop">
						<tr>
							<th scope="row">${stat.key}</th>
							<td>${stat.value}</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
				
				<div style="margin:0px 0px 20px 0px;"></div>
			</div>
			<div class="clear"></div>
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