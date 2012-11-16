<!doctype html>
<html lang="en">

<head>
	<meta charset="utf-8"/>
	<title>Dashboard I Admin Panel</title>
	
	<link rel="stylesheet" href="${ctx }/resources/css/layout.css" type="text/css" media="screen" />
	<!--[if lt IE 9]>
	<link rel="stylesheet" href="${ctx }/resources/css/ie.css" type="text/css" media="screen" />
	<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
	<![endif]-->
	<script type="text/javascript" src="${ctx }/resources/js/jquery.min.js"></script>
	<script type="text/javascript" src="${ctx }/resources/js/hideshow.js"></script>
	<script type="text/javascript" src="${ctx }/resources/js/jquery.tablesorter.min.js"></script>
	<script type="text/javascript" src="${ctx }/resources/js/jquery.equalHeight.js"></script>
	
	<link rel="stylesheet" href="${ctx }/resources/themes/smoothness/jquery-ui.full.css" type="text/css" media="screen" />
	<script type="text/javascript" src="${ctx }/resources/js/jquery-ui/i18n/jquery-ui-i18n.js"></script>
	<script type="text/javascript" src="${ctx }/resources/js/jquery-ui.full.js"></script>
	
	<%@include file="/WEB-INF/views/includes/head/res_fullcalendar.jsp" %>
	
	<script type="text/javascript">
	$(document).ready(function() { 
		$('.column').equalHeight();
		$('input:button, button').button();
		$(".tablesorter").tablesorter(); 

   	 });
	$(document).ready(function() {

		//When page loads...
		$(".tab_content").hide(); //Hide all content
		$("ul.tabs li:first").addClass("active").show(); //Activate first tab
		$(".tab_content:first").show(); //Show first tab content
	
		//On Click Event
		$("ul.tabs li").click(function() {
	
			$("ul.tabs li").removeClass("active"); //Remove any "active" class
			$(this).addClass("active"); //Add "active" class to selected tab
			$(".tab_content").hide(); //Hide all tab content
	
			var activeTab = $(this).find("a").attr("href"); //Find the href attribute value to identify the active tab + content
			$(activeTab).fadeIn(); //Fade in the active ID content
			return false;
		});

	});
	
	$(function(){
		var date = new Date();
		var d = date.getDate();
		var m = date.getMonth();
		var y = date.getFullYear();
		
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
					calendar.fullCalendar('renderEvent',
						{
							title: title,
							start: start,
							end: end,
							allDay: allDay
						},
						true // make the event "stick"
					);
				}
				calendar.fullCalendar('unselect');
			},
			dayClick: function(date, allDay, jsEvent, view) {
		        if (allDay) {
		            alert('Clicked on the entire day: ' + date);
		        }else{
		            alert('Clicked on the slot: ' + date);
		        }
		        //alert('Coordinates: ' + jsEvent.pageX + ',' + jsEvent.pageY);
		        //alert('Current view: ' + view.name);
		        // change the day's background color just for fun
		        //$(this).css('background-color', 'red');
		    },
			eventClick: function(calEvent, jsEvent, view) {
				alert('Event: ' + calEvent.title);
				alert('Coordinates: ' + jsEvent.pageX + ',' + jsEvent.pageY);
				alert('View: ' + view.name);
				// change the border color just for fun
				$(this).css('border-color', 'red');
			},
			editable: true,
			events: {
				url: "${PATH.emails}/get-schedules",
			},
			/*
			eventSources: [{
				url: '${PATH.emails}/get-schedules',
				type: 'GET',
				data: {
	                custom_param1: 'something'
	            },
				error: function() {
					alert('there was an error while fetching events!');
				}
			}]
			*/
			/*
			events: [
				{
					title: 'All Day Event',
					start: new Date(y, m, 1),
					backgroundColor: '#edef32'
				},
				{
					title: 'Long Event',
					start: new Date(y, m, d-5),
					end: new Date(y, m, d-2)
				},
				{
					id: 999,
					title: 'Repeating Event',
					start: new Date(y, m, d-3, 16, 0),
					allDay: false
				},
				{
					id: 999,
					title: 'Repeating Event',
					start: new Date(y, m, d+4, 16, 0),
					allDay: false
				},
				{
					title: 'Meeting',
					start: new Date(y, m, d, 10, 30),
					allDay: false
				},
				{
					title: 'Lunch',
					start: new Date(y, m, d, 12, 0),
					end: new Date(y, m, d, 14, 0),
					allDay: false
				},
				{
					title: 'Birthday Party',
					start: new Date(y, m, d+1, 19, 0),
					end: new Date(y, m, d+1, 22, 30),
					allDay: false
				},
				{
					title: 'Click for Google',
					start: new Date(y, m, 28),
					end: new Date(y, m, 29),
					url: 'http://google.com/'
				}
			]*/
		});
	});
    </script>
</head>


<body>

	<header id="header">
		<hgroup>
			<h1 class="site_title"><a href="index.html">Mail Berry</a></h1>
			<h2 class="section_title">Dashboard</h2><div class="btn_view_site"><a href="http://www.medialoot.com">View Site</a></div>
		</hgroup>
	</header> <!-- end of header bar -->
	
	<section id="secondary_bar">
		<div class="user">
			<p>John Doe (<a href="#">Logout</a>)</p>
			<!-- <a class="logout_user" href="#" title="Logout">Logout</a> -->
		</div>
		<div class="breadcrumbs_container">
			<article class="breadcrumbs">
				<a href="#">Step1 - Select Message Type</a>
				<div class="breadcrumb_divider"></div>
				<a href="#">Step2 - Campaign Info</a>
				<div class="breadcrumb_divider"></div>
				<a href="#">Step3 - Compose Message</a>
				<div class="breadcrumb_divider"></div>
				<a class="current">Finish - Delivery Message</a>
			</article>
		</div>
	</section><!-- end of secondary bar -->
	
	<aside id="sidebar" class="column">
		<br/>

		<h3>MESSAGES</h3>
		<ul class="toggle">
			<li class="icn_new_article"><a href="#">Campaigns</a></li>
			<li class="icn_edit_article"><a href="#">Email Schedule</a></li>
			<li class="icn_categories"><a href="#">Message Templates</a></li>
			<li class="icn_tags"><a href="#">Template Variables</a></li>
			<li class="icn_tags"><a href="#">Template Chunks</a></li>
		</ul>
		<h3>LISTS</h3>
		<ul class="toggle">
			<li class="icn_view_users"><a href="#">Subscriber Lists</a></li>
			<li class="icn_view_users"><a href="#">Global Block List</a></li>
			<li class="icn_view_users"><a href="#">Local Block List</a></li>
			<li class="icn_add_user"><a href="#">Import</a></li>
			<li class="icn_profile"><a href="#">Export</a></li>
		</ul>
		<h3>ANALYTICS</h3>
		<ul class="toggle">
			<li class="icn_jump_back"><a href="#">Clickstream</a></li>
			<li class="icn_jump_back"><a href="#">Email Tracking</a></li>
			<li class="icn_jump_back"><a href="#">Campaign Statistic</a></li>
		</ul>
		<h3>TOOLS</h3>
		<ul class="toggle">
			<li class="icn_edit_article"><a href="#">Subscriber Form Builder</a></li>
			<li class="icn_photo"><a href="#">Image Gallery</a></li>
			<li class="icn_folder"><a href="#">File Manager</a></li>
		</ul>
		<h3>SETTINGS</h3>
		<ul class="toggle">
			<li class="icn_settings"><a href="#">SMTP Profiles</a></li>
			<li class="icn_security"><a href="#">Manage Users</a></li>
			<li class="icn_settings"><a href="#">Configurations</a></li>
		</ul>
		<footer>
			<hr />
			<p><strong>Copyright &copy; 2011 Website Admin</strong></p>
			<p>Theme by <a href="http://www.medialoot.com">MediaLoot</a></p>
		</footer>
	</aside><!-- end of sidebar -->
	
	<section id="main" class="column">
		
		<h4 class="alert_info"><a href="http://arshaw.com/fullcalendar/docs/" target="_blank">Documentation</a></h4>
		
		<article class="module width_full">
			<header><h3>Post New Article</h3></header>
				<div class="module_content">
						<div id='calendar'></div>
				</div>
			<footer>
				<div class="submit_link">
					<select>
						<option>Draft</option>
						<option>Published</option>
					</select>
					<input type="submit" value="Publish" class="alt_btn">
					<input type="submit" value="Reset">
				</div>
			</footer>
		</article><!-- end of post new article -->
		
		<div class="spacer"></div>
	</section>


</body>

</html>