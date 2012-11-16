<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
	<%--
	<script type="text/javascript" src="${ctx }/resources/js/jquery.min.js"></script>
	<link rel="stylesheet" href="${ctx }/resources/themes/smoothness/jquery-ui.full.css" type="text/css" media="screen" />
	<script type="text/javascript" src="${ctx }/resources/js/jquery-ui/i18n/jquery-ui-i18n.js"></script>
	<script type="text/javascript" src="${ctx }/resources/js/jquery-ui.full.js"></script>
	
	<%@include file="/WEB-INF/views/includes/head/res_fullcalendar.jsp" %>
	--%>
	<link rel='stylesheet' type='text/css' href='http://localhost/fullcalendar/fullcalendar/fullcalendar.css' />
	<link rel='stylesheet' type='text/css' href='http://localhost/fullcalendar/fullcalendar/fullcalendar.print.css' media='print' />
	<script type='text/javascript' src='http://localhost/fullcalendar/jquery/jquery-1.5.2.min.js'></script>
	<script type='text/javascript' src='http://localhost/fullcalendar/jquery/jquery-ui-1.8.11.custom.min.js'></script>
	<script type='text/javascript' src='http://localhost/fullcalendar/fullcalendar/fullcalendar.min.js'></script>
	<script type='text/javascript'>

	$(document).ready(function() {
	
		$('#calendar').fullCalendar({
			
			editable: true,
			
			events: {
				url: "${PATH.emails}/get-schedules",
			},
			eventDrop: function(event, delta) {
				alert(event.title + ' was moved ' + delta + ' days\n' +
					'(should probably update your database)');
			},
			
			loading: function(bool) {
				if (bool) $('#loading').show();
				else $('#loading').hide();
			}
			
		});
		
	});

</script>
<style type='text/css'>

	body {
		margin-top: 40px;
		text-align: center;
		font-size: 14px;
		font-family: "Lucida Grande",Helvetica,Arial,Verdana,sans-serif;
		}
		
	#loading {
		position: absolute;
		top: 5px;
		right: 5px;
		}

	#calendar {
		width: 900px;
		margin: 0 auto;
		}

</style>
</head>
<body>
<div id='loading' style='display:none'>loading...</div>
<div id='calendar'></div>
<p>json-events.php needs to be running in the same directory.</p>
</body>
</html>
