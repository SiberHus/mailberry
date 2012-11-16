<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!doctype html>
<html lang="en">
<head>
	<%@include file="/WEB-INF/views/includes/head/res_standard.jsp" %>
	<%@include file="/WEB-INF/views/includes/head/res_jqgrid.jsp" %>
	<%@include file="/WEB-INF/views/includes/head/res_fullcalendar.jsp" %>
	<link rel="stylesheet" media="screen,projection" type="text/css" href="${ctx }/resources/comps/countdown/jquery.countdown.css" />
	<script type="text/javascript" src="${ctx }/resources/comps/countdown/jquery.countdown.min.js"></script>
	<style type="text/css">
		li.color{width: 300px; padding-left: 10px; font-weight: bold;}
		#defaultCountdown { width: 240px; height: 45px; }
	</style>
	<script type="text/javascript">
	// setup control widget
    var calendar = null;
    var cPeriodEvt = { url: '${PATH.calendar}/campaigns/period'};
    var cStatusEvt = { url: '${PATH.calendar}/campaigns/status'};
    var usHolEvts = { url: 'http://www.google.com/calendar/feeds/usa__en%40holiday.calendar.google.com/public/basic'};
	$(document).ready(function() {
		
		$("#campaign_datagrid").jqGrid({
			url:'${PATH.analytics}/campaigns/list/data/all',
			pager: '#main_datagrid_pager',
			colNames:['Campaign Name', 'Subscriber List', 'Mail Subject', 'Start Date', 'End Date', 'Description', 'Emails', 'Created By', 'Created At'],
			colModel:[
				{name:'campaignName', width:100, searchoptions:searchoptions.string},
				{name:'list.listName', width:100, searchoptions:searchoptions.string},
				{name:'mailSubject', hidden: true, searchoptions:searchoptions.string},
				{name:'startDate', hidden: true, formatter:'date', searchoptions:searchoptions.date},
				{name:'endDate', hidden: true, formatter:'date', searchoptions:searchoptions.date},
				{name:'description', hidden: true, searchoptions:searchoptions.string},
				{name:'emails', width:80, fixed: true, searchoptions:searchoptions.number},
				{name:'createdBy', width:50, formatter:'date', searchoptions:searchoptions.date},
				{name:'createdAt', width:50, formatter:'date', searchoptions:searchoptions.date}
			]
		}).jqGrid('navGrid','#campaign_datagrid_pager',{del:false,add:false,edit:false},{},{},{},{multipleSearch:true, multipleGroup:false});
		$('#campaign_datagrid').setGridHeight(285);
		$('#campaign_datagrid').setGridWidth(600);
		$("#campaign_dialog").dialog({
			autoOpen: false,height: 420,width: 630,modal: true,
			buttons: {
				Select: function() {
					$(this).dialog('close');
				},
				Cancel: function() {
					$(this).dialog('close');
				}
			}
		});
		$('#edit_btn').click(function(){
			window.location.href='${PATH.campaigns}/edit/'+$('#campaign_id').val();
		});
		$('#view_btn').click(function(){
			window.location.href='${PATH.campaigns}/view/'+$('#campaign_id').val();
		});
		$('#sch_btn').click(function(){
			window.location.href='${PATH.emails}/delivery/'+$('#campaign_id').val();
		});
		$('#rpt_btn').click(function(){
			window.location.href='${PATH.analytics}/campaigns/track/'+$('#campaign_id').val();
		});
		$("#event_info_dialog").dialog({
			autoOpen: false,height: 370,width: 570,modal: false,
			buttons: {Close: function() {$(this).dialog('close');}}
		});
		var calendar = $('#calendar').fullCalendar({
			header: {
				left: 'prev,next today',
				center: 'title',
				right: 'month,agendaWeek,agendaDay'
			},
			selectable: false,
			selectHelper: true,
			select: function(start, end, allDay) {
				var title = prompt('Event Title:');
				if (title) {
					calendar.fullCalendar('renderEvent',{
						title: title,
						start: start,end: end,
						allDay: allDay
						},true // make the event "stick"
					);
				}
				calendar.fullCalendar('unselect');
			},
			dayClick: function(date, allDay, jsEvent, view) {
				<%--
		        if (allDay) {
		            //alert('Clicked on the entire day: ' + date);
		        }else{
		            //alert('Clicked on the slot: ' + date);
		        }
		        $('#campaign_dialog').dialog('open');
		        //alert('Coordinates: ' + jsEvent.pageX + ',' + jsEvent.pageY);
		        //alert('Current view: ' + view.name);
		        // change the day's background color just for fun
		        //$(this).css('background-color', 'red');
		        --%>
		    },
			eventClick: function(calEvent, jsEvent, view) {
				$('#campaign_name').text(calEvent.title);
				$('#campaign_desc').text(calEvent.description);
				var status = '';
				if(calEvent.status=='DRA') status = 'Draft';
				else if(calEvent.status=='INP') status = 'In Progress';
				else if(calEvent.status=='SCH') status = 'Scheduled';
				else if(calEvent.status=='CAN') status = 'Cancelled';
				else if(calEvent.status=='SEN') status = 'Sent';
				$('#campaign_status').text(status+' - '+calEvent.start);
				$('#campaign_id').val(calEvent.id);
				if(calEvent.status=='SCH'){
					$('#defaultCountdown').countdown({until: calEvent.start});
				}
				if(calEvent.status=='SEN'){
					$('#edit_btn').hide();
					$('#sch_btn').hide();
					$('#rpt_btn').show();
				}else{
					$('#edit_btn').show();
					$('#sch_btn').show();
					$('#rpt_btn').hide();
				}
				$('#event_info_dialog').dialog('open');
				/*
				alert('Event: ' + calEvent.title);
				alert('Event: ' + calEvent.description);
				alert('Coordinates: ' + jsEvent.pageX + ',' + jsEvent.pageY);
				alert('View: ' + view.name);
				// change the border color just for fun
				$(this).css('border-color', 'red');
				*/
			},
			editable: true,
			/*
			events: {
				url: "${PATH.calendar}/events",
			}
			*/
		});
		
		calendar.fullCalendar( 'addEventSource', cStatusEvt);
		
		$('#camp_p_e').click(function(){
			if($('#camp_p_e:checked').val() !== undefined) {
				calendar.fullCalendar( 'addEventSource', cPeriodEvt);
			}else{
				calendar.fullCalendar( 'removeEventSource', cPeriodEvt);
			}
		});
		$('#camp_s_e').click(function(){
			if($('#camp_s_e:checked').val() !== undefined) {
				calendar.fullCalendar( 'addEventSource', cStatusEvt);
			}else{
				calendar.fullCalendar( 'removeEventSource', cStatusEvt);
			}
		});
		$('#us_holidays').click(function(){
			if($('#us_holidays:checked').val() !== undefined) {
				calendar.fullCalendar( 'addEventSource', usHolEvts);
			}else{
				calendar.fullCalendar( 'removeEventSource', usHolEvts);
			}
		});
	});
    </script>
</head>
<body>
	<c:set var="pageTitle" value="Events Calendar"/>
	<%@include file="/WEB-INF/views/includes/body/header.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/secondary_bar.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/sidebar.jsp" %>
	<section id="main" class="column">
	
	<article class="module width_full">
		<header>
			<h3>
			<img src="${ctx }/resources/images/icons/chart_bar.png" />
			Events Calendar
			</h3>
		</header>
		<div class="module_content" >
			<div style="margin: 10px 0px 15px 0px;">
				<strong>Event Sources</strong>:
				&nbsp;&nbsp;
				<input type="checkbox" id="camp_p_e" name="cPeriodEvts">
				<label for="camp_p_e">Campaign Period Events</label>
				&nbsp;&nbsp;
				<input type="checkbox" id="camp_s_e" name="cStatusEvts" checked="checked">
				<label for="camp_s_e">Campaign Status Events</label>
				&nbsp;&nbsp;
				<input type="checkbox" id="us_holidays" name="usHolEvts">
				<label for="us_holidays">US Holidays</label>
			</div>
			<div id='calendar'></div>
			<div class="clear"></div>
			
			<h3>Campaign Event Colors</h3>
			<ul>
				<li class="color" style="background-color:${campaignColor['default'].backgroundColor};border-color:${campaignColor['default'].borderColor};color:${campaignColor['default'].textColor}">
					Default
				</li>
				<li class="color" style="background-color:${campaignColor.inp.backgroundColor};border-color:${campaignColor.inp.borderColor};color:${campaignColor.inp.textColor}">
					In Progress
				</li>
				<li class="color" style="background-color:${campaignColor.sen.backgroundColor};border-color:${campaignColor.sen.borderColor};color:${campaignColor.sent.textColor}">
					Sent
				</li>
				<li class="color" style="background-color:${campaignColor.can.backgroundColor};border-color:${campaignColor.can.borderColor};color:${campaignColor.can.textColor}">
					Cancelled
				</li>
				<li class="color" style="background-color:${campaignColor.sch.backgroundColor};border-color:${campaignColor.sch.borderColor};color:${campaignColor.sch.textColor}">
					Scheduled
				</li>
			</ul>
		</div>
	</article>
	<div class="spacer"></div>
	</section>
	
	<div id="campaign_dialog" class="datagrid" title="Select Campaign">
		<table id="campaign_datagrid"></table>
		<div id="campaign_datagrid_pager"></div>
	</div>
	<div id="event_info_dialog" title="Campaign Info">
		<div>
			<input type="hidden" id="campaign_id" />
			<fieldset>
				<label class="field">
					<s:message code="entity.campaign.campaignName" text="Campaign Name"/>
				</label>
				<div class="value">
					<span id="campaign_name"/>
				</div>
			</fieldset>
			<fieldset>
				<label class="field">
					<s:message code="entity.campaign.description" text="Description"/>
				</label>
				<div class="value">
					<span id="campaign_desc"/>
				</div>
			</fieldset>
			<fieldset>
				<label class="field">
					<s:message code="entity.campaign.status" text="Status"/>
				</label>
				<div class="value">
					<span id="campaign_status"/>
				</div>
			</fieldset>
		</div>
		<footer>
			<div class="back_link">
				<div id="defaultCountdown"></div>
			</div>
			<div class="submit_link">
				<input type="button" id="edit_btn" value="Edit" />
				<input type="button" id="view_btn" value="View" />
				<input type="button" id="sch_btn" value="Schedule" />
				<input type="button" id="rpt_btn" value="Report" />
			</div>
		</footer>
	</div>
</body>

</html>