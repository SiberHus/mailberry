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
	</style>
	<script type="text/javascript">
	$(document).ready(function() {
		$('#total_stats_tbl').visualize({type: 'bar', width:'600px'})
			.appendTo('#total_stats_chart').trigger('visualizeRefresh');
	});
    </script>
    <c:if test="${fn:length(campaigns) gt 1}">
    <script type="text/javascript">
	$(document).ready(function() {
		$('#success_rate_tbl').visualize({type: 'pie', height: '250px', width: '600px'})
			.appendTo('#success_rate_chart').trigger('visualizeRefresh');
		$('#open_rate_tbl').visualize({type: 'pie', height: '250px', width: '600px'})
			.appendTo('#open_rate_chart').trigger('visualizeRefresh');
		$('#click_rate_tbl').visualize({type: 'pie', height: '250px', width: '600px'})
			.appendTo('#click_rate_chart').trigger('visualizeRefresh');
		$('#rsvp_rate_tbl').visualize({type: 'pie', height: '250px', width: '600px'})
			.appendTo('#rsvp_rate_chart').trigger('visualizeRefresh');
		$('#forward_rate_tbl').visualize({type: 'pie', height: '250px', width: '600px'})
			.appendTo('#forward_rate_chart').trigger('visualizeRefresh');
		$('#optout_rate_tbl').visualize({type: 'pie', height: '250px', width: '600px'})
			.appendTo('#optout_rate_chart').trigger('visualizeRefresh');
		$('#back_btn').click(function(){
			window.location.href='${PATH.campaignAnalytics}/';
		});
	});
    </script>
    </c:if>
</head>
<body>
	<c:set var="pageTitle" value="Campaign Comparison"/>
	<%@include file="/WEB-INF/views/includes/body/header.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/secondary_bar.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/sidebar.jsp" %>
	<section id="main" class="column">
	
	<article class="module width_full">
		<header>
			<h3>
			<img src="${ctx }/resources/images/icons/chart_bar.png" />
			Campaign Comparison
			</h3>
		</header>
		<div class="module_content graph_wrapper" style="margin-left: 0px;">
			<a name="top"></a>
			<div id="total_stats_chart" class="chart"></div>
			<div class="center" style="margin:0px auto;width:80%">
			<table id="total_stats_tbl" style="width:100%;">
				<caption>Campaign Analytics</caption>
				<thead>
					<tr>
						<td class="title" style="width:30%;">Campaign Name</td>
						<th scope="col" style="width:10%;">Emails</th>
						<th scope="col" style="width:10%;">
							<a href="#success_rate_chart">Successes</a>
						</th>
						<th scope="col" style="width:10%;">
							<a href="#open_rate_chart">Opens</a>
						</th>
						<th scope="col" style="width:10%;">
							<a href="#click_rate_chart">Clicks</a>
						</th>
						<th scope="col" style="width:10%;">
							<a href="#rsvp_rate_chart">RSVPs</a>
						</th>
						<th scope="col" style="width:10%;">
							<a href="#forward_rate_chart">Forwards</a>
						</th>
						<th scope="col" style="width:10%;">
							<a href="#optout_rate_chart">Unsubscribes</a>
						</th>
					</tr>
				</thead>
				<tbody>
				<c:forEach var="stat" items="${stats }" varStatus="loop">
					<tr>
						<th scope="row">
							<a href="${PATH.campaigns }/view/${campaigns[loop.index].id}" target="_blank">
								${campaigns[loop.index].campaignName }
							</a>
						</th>
						<td style="text-align: center;">
							<a href="${PATH.subscribers }/${campaigns[loop.index].list.id}" target="_blank">
								${stat.emails }
							</a>
						</td>
						<td style="text-align: center;">${stat.successes }</td>
						<td style="text-align: center;">${stat.opens }</td>
						<td style="text-align: center;">${stat.clicks }</td>
						<td style="text-align: center;">${stat.rsvps }</td>
						<td style="text-align: center;">${stat.forwards }</td>
						<td style="text-align: center;">${stat.optOuts }</td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
			<c:if test="${fn:length(campaigns) eq 1}">
			<table style="width:100%;">
				<tbody>
				<c:forEach var="stat" items="${stats }" varStatus="loop">
				<tr>
					<td style="width:30%;" scope="row">&nbsp;</td>
					<td style="width:10%;text-align: center;">N/A</td>
					<td style="width:10%;text-align: center;">${stat.successRate }%</td>
					<td style="width:10%;text-align: center;">${stat.openRate }%</td>
					<td style="width:10%;text-align: center;">N/A</td>
					<td style="width:10%;text-align: center;">${stat.rsvpRate }%</td>
					<td style="width:10%;text-align: center;">${stat.forwardRate }%</td>
					<td style="width:10%;text-align: center;">${stat.optOutRate }%</td>
				</tr>
				</c:forEach>
				</tbody>
			</table>
			</c:if>
			</div>
			<div style="margin:0px 0px 20px 0px;"></div>
			<c:if test="${fn:length(campaigns) gt 1}">
			<div id="success_rate_chart" class="chart">
				<a name="success_rate_chart"></a>
				<div class="top_link"><a href="#top">Top</a></div>
			</div>
			<table id="success_rate_tbl" style="display:none;">
				<caption>Sending Success Rate</caption>
				<thead><tr></tr></thead>
				<tbody>
				<c:forEach var="stat" items="${stats }" varStatus="loop">
					<tr>
						<th scope="row">${campaigns[loop.index].campaignName }</th>
						<td>${stat.successRate }</td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
			<div id="open_rate_chart" class="chart">
				<a name="open_rate_chart"></a>
				<div class="top_link"><a href="#top">Top</a></div>
			</div>
			<table id="open_rate_tbl" style="display:none;">
				<caption>Email Open Rate</caption>
				<thead><tr></tr></thead>
				<tbody>
				<c:forEach var="stat" items="${stats }" varStatus="loop">
					<tr>
						<th scope="row">${campaigns[loop.index].campaignName }</th>
						<td>${stat.openRate }</td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
			<div id="click_rate_chart" class="chart">
				<a name="click_rate_chart"></a>
				<div class="top_link"><a href="#top">Top</a></div>
			</div>
			<table id="click_rate_tbl" style="display:none;">
				<caption>Click Rate</caption>
				<thead><tr></tr></thead>
				<tbody>
				<c:forEach var="stat" items="${stats }" varStatus="loop">
					<tr>
						<th scope="row">${campaigns[loop.index].campaignName }</th>
						<td>${stat.clicks }</td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
			<div id="rsvp_rate_chart" class="chart">
				<a name="rsvp_rate_chart"></a>
				<div class="top_link"><a href="#top">Top</a></div>
			</div>
			<table id="rsvp_rate_tbl" style="display:none;">
				<caption>RSVP Response Rate</caption>
				<thead><tr></tr></thead>
				<tbody>
				<c:forEach var="stat" items="${stats }" varStatus="loop">
					<tr>
						<th scope="row">${campaigns[loop.index].campaignName }</th>
						<td>${stat.rsvpRate }</td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
			<div id="forward_rate_chart" class="chart">
				<a name="forward_rate_chart"></a>
				<div class="top_link"><a href="#top">Top</a></div>
			</div>
			<table id="forward_rate_tbl" style="display:none;">
				<caption>Email Forward Rate</caption>
				<thead><tr></tr></thead>
				<tbody>
				<c:forEach var="stat" items="${stats }" varStatus="loop">
					<tr>
						<th scope="row">${campaigns[loop.index].campaignName }</th>
						<td>${stat.forwardRate }</td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
			<div id="optout_rate_chart" class="chart">
				<a name="optout_rate_chart"></a>
				<div class="top_link"><a href="#top">Top</a></div>
			</div>
			<table id="optout_rate_tbl" style="display:none;">
				<caption>Unsubscribe Rate</caption>
				<thead><tr></tr></thead>
				<tbody>
				<c:forEach var="stat" items="${stats }" varStatus="loop">
					<tr>
						<th scope="row">${campaigns[loop.index].campaignName }</th>
						<td>${stat.optOutRate }</td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
			</c:if>
			<div class="clear"></div>
		</div>
		<footer>
			<form action="${PATH.analytics }/campaigns/compare" method="post">
			<c:forEach var="stat" items="${stats }" varStatus="loop">
				<input type="hidden" name="campaignIds" value="${campaigns[loop.index].id}"/>
			</c:forEach>
			<div class="back_link">
				<input type="submit" id="back_btn" value="Back" />
			</div>
			</form>
		</footer>
	</article>
	<div class="spacer"></div>
	</section>
	
	
</body>

</html>