<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!doctype html>
<s:message var="textConfrimSend" code="view.email.confirmSend" text="You are about to send this campaign to {0} subscribers of the {1}. \nAre you sure you want to continue?"
	arguments="${subscriberCount }|*| ${campaign.list }" argumentSeparator="|*|"/>

<html lang="en">
<head>
	<%@include file="/WEB-INF/views/includes/head/res_standard.jsp" %>
	<%@include file="/WEB-INF/views/includes/head/res_jqgrid.jsp" %>
	<link rel="stylesheet" media="screen,projection" type="text/css" href="${ctx }/resources/css/color_buttons.css" />
	<style type="text/css">
    .callout_down {
        height: 0;
        width: 0;
        border-top: 12px solid #ffffff;
        border-left: 12px dotted transparent;
        border-right: 12px dotted transparent;
        left: 0px;
        top: 0px;
        margin-left: 20px;
        z-index: 11;
    }
    .callout_down2 {
        position: relative;
        left: -10px;
        top: -12px;
        height: 0;
        width: 0;
        border-top: 10px solid #eaeaef;
        border-left: 10px dotted transparent;
        border-right: 10px dotted transparent;
        z-index: 10;
    }
    .callout_body {
        background-color: #eaeaef;
        border: solid 1px #ffffff;
        position: relative;
        top: 1px;
        z-index: 3;
        width: 98%;
        padding: 4px;
    }
    .callout_container {
        background-color: #cdcdd3;
        padding: 8px;
    }
    #center_box {
		width:400px;
		margin:0px auto; /* Right and left margin widths set to "auto" */
		text-align:left; /* Counteract to IE5/Win Hack */
		padding:15px;
		border:1px dashed #333;
		background-color:#eee;
	}
	</style>
	<script type="text/javascript">
	function checkProgress(){
		$.get('${PATH.emails}/check-progress/${campaign.id}', function(data) {
			var value = parseInt(data);
			if(!isNaN(value)){
				$('#progressbar_value').text(value);
				$('#progressbar').progressbar('value', value );
			}else{
				$('#progressbar_value').text(data);
				$('#progressbar').progressbar('value', -1 );
			}
		});
	}
	function sendTest(email){
		$('#send_test_btn').attr('disabled', 'disabled');
		$.post('${PATH.emails}/send-test/${campaign.id}',{email:email},
			function(data){
				$('#send_test_btn').removeAttr('disabled');
				if(data.error){
					$('#test_success_div').hide();
					$('#test_error_div').show();
					$('#test_error_detail').text(data.errorDetail);
					$('#notification').jGrowl('Cannot send email due to '+data.errorDetail);
				}else{
					$('#test_error_div').hide();
					$('#test_success_div').show();
					$('#test_success_detail').text(new Date().toDateString());
					$('#notification').jGrowl('A test email has been sent successfully.');
				}
		});
	}
	$(document).ready(function() {
		<c:if test="${campaign.status eq 'SCH' or campaign.status eq 'INP'}">
		$('#send_btn').hide();
		$('#cancel_delivery_div').show();
		</c:if>
		<c:if test="${campaign.status eq 'SEN'}">
		$('#send_btn').hide();
		$('#cancel_delivery_div').hide();
		</c:if>
		$('#progressbar').progressbar({
			value: 0
		});
		
		setInterval('checkProgress()', 7000);//poll every 7 secs
		
		$('#send_btn').click(function(){
			
			var option = $('input[name=sendReal]:checked').val();
			var confirmMsg = '${textConfrimSend}';
			if(option==1){
				$.prompt(confirmMsg,{
					callback: function(v,m,f){
						if(v=='1'){
							$.post('${PATH.emails}/send-now/${campaign.id}',
									{date:date, hour:hour, minute:minute},
								function(data){
									$('#send_btn').hide();
									$('#cancel_delivery_div').show();
							});
						}
					},
					buttons: { Continue: '1', Cancel: '0' }
				});
			}else if(option==2){
				var scheduledDate = $.datepicker.parseDate('${FMT.jsDate}', $('#scheduled_date').val());
				if(scheduledDate==null){
					jprompt.alert('<s:message code="view.alert.dateMissing" text="You have to enter date"/>');
					return;
				}
				scheduledDate.setHours($('#scheduled_hour').val());
				scheduledDate.setMinutes($('#scheduled_minute').val());
				var nextFireTime = scheduledDate - new Date();
				var minutes = Math.ceil(nextFireTime/1000/60);//minutes
				var hours= 0, days = 0;
				if(minutes>60){ 
					hours = Math.floor(minutes/60);
					minutes = minutes - (hours*60); 
				}
				if(hours>24){
					days = Math.floor(hours/24);
					hours = hours - (days*24);
				}
				if(minutes<0){ 
					jprompt.alert('<s:message code="view.alert.pastDate" text="You have to enter a future date"/>');
					return;
				}
				var daysTxt = '<s:message code="view.text.days" text="days"/>';
				var hoursTxt = '<s:message code="view.text.hours" text="hours"/>';
				var minutesTxt = '<s:message code="view.text.minutes" text="minutes"/>';
				var nextFireTxt = '<s:message code="view.email.alertNextFire" text="Campaign will be sent in: "/>';
				if(days>0){ nextFireTxt += days+' '+daysTxt+', '+hours+' '+hoursTxt+', '+minutes+' '+minutesTxt;}
				else if(hours>0){ nextFireTxt += hours+' '+hoursTxt+', '+minutes+' '+minutesTxt;}
				else if(minutes>0){ nextFireTxt += minutes+' '+minutesTxt;}
				var date = $('#scheduled_date').val();
				var hour = $('#scheduled_hour').val();
				var minute = $('#scheduled_minute').val();
				
				$.prompt(confirmMsg,{
					callback: function(v,m,f){
						if(v=='1'){
							$('#send_btn').attr('disabled', 'disabled');
							$.post('${PATH.emails}/schedule-delivery/${campaign.id}',
									{date:date, hour:hour, minute:minute},
								function(data){
									$('#send_btn').hide();
									$('#cancel_delivery_div').show();
							});
							$.prompt(nextFireTxt,{ buttons: { OK: 'OK'}});
						}
					},
					buttons: { Continue: '1', Cancel: '0' }
				});
			}
		});
		
		$('#cancel_delivery_btn').click(function(){
			$.post('${PATH.emails}/cancel-delivery/${campaign.id}',
				function(data){
					window.location.href='${PATH.emails}/delivery/${campaign.id}';
			});
		});
		
		$('#send_test_btn').click(function(){
			var option = $('input[name=sendTest]:checked').val();
			if(option==1){
				var email = $('#test_email').val();
				if(!email){
					$('#test_success_div').hide();
					$('#test_error_div').show();
					$('#test_error_detail').text('email required');
					return;
				}
				sendTest(email);
			}else if(option==2){
				sendTest('');
			}
		});
	});
    </script>
</head>
<body>
	<c:set var="pageTitle" value="Email Delivery"/>
	<%@include file="/WEB-INF/views/includes/body/header.jsp" %>
	<section id="secondary_bar">
		<%@include file="/WEB-INF/views/includes/body/user.jsp" %>
		<div class="breadcrumbs_container">
			<article class="breadcrumbs">
				<a href="${PATH.campaigns}/">
					<s:message code="view.campaign.listCampaign" text="List Campaigns"/>
				</a>
				<div class="breadcrumb_divider"></div>
				<a href="${PATH.campaigns}/edit/${campaign.id}">
					<s:message code="view.campaign.editCampaign" text="Edit Campaign"/>
				</a>
				<div class="breadcrumb_divider"></div>
				<a class="current">
					<s:message code="view.campaign.finish" text="Finish - Deliver Message"/>
				</a>
			</article>
		</div>
	</section>
	<%@include file="/WEB-INF/views/includes/body/sidebar.jsp" %>
	<section id="main" class="column">
	
	<article class="module width_full">
		<header>
			<h3>
			<s:message code="view.email.messageDelivery" text="Message Delivery" />
			</h3>
		</header>
		<div class="module_content">
			<div style="width:60%; float:left; margin-right: 1%;">
				<div class="callout_container">
					<div class="callout_body">
						<p>${campaignSummary }</p>
					</div>
					<div class="callout_down">
						<div class="callout_down2"></div>
					</div>
					<div><a href="#">MailBerry</a> Said</div></div>
				</div>
			</div>
			<div style="width:36%; float:left;background-color: #cdcdd3; padding: 10px;">
				<div id="test_success_div" class="ui-state-highlight ui-corner-all" style="margin-top: 3px; padding: 0 .7em; display: none;"> 
					<p>
					<span class="ui-icon ui-icon-info" style="float: left; margin-right: .3em;"></span>
						<s:message code="view.email.sentTestSuccess" text="A test email has been sent successfully."/>&nbsp;<b><span id="test_success_detail"></span></b>
					</p>
				</div>
				<div id="test_error_div" class="ui-state-error ui-corner-all" style="margin-top: 3px; padding: 0 .7em; display: none;"> 
					<p>
					<span class="ui-icon ui-icon-alert" style="float: left; margin-right: .3em;"></span>
						<s:message code="view.email.sentTestError" text="Cannot send email due to "/><span id="test_error_detail"></span>
					</p>
				</div>
				<div style="width:100%;margin-top:10px;">
					<b>Send Test</b>
					<div class="value">
						<div style="padding:5px;">
							<input type="radio" id="send_test1" name="sendTest" value="1" checked="checked">
							<label for="send_test1">
								<s:message code="view.email.sendToTestEmail" text="Send to test email"/>
							</label>
							<input type="text" id="test_email" style="width:150px"/>
						</div>
						<div style="padding:5px;">
							<input type="radio" id="send_test2" name="sendTest" value="2">
							<label for="send_test2">
								<s:message code="view.email.sendToTestSubscribers" text="Send to test subscribers"/>
							</label>
						</div>
						<input type="button" id="send_test_btn" value="Send Test" style="margin-top:10px;width:80%"/>
					</div>
				</div>
			</div>
	    	<div class="clear"></div>
	    	<div style="width:60%; float:left; margin-right: 1%;">
	    		<c:if test="${campaign.status eq 'DRA'}">
	    			<h4 class="alert_info">
	    				<s:message code="view.email.sendingNotification" text="You are going to send campaign to {0} subscriber(s)"
	    					arguments="${subscriberCount }"/> 
	    			</h4>
	    		</c:if>
	    		<div style="margin:20px;">
		    		<div style="margin-bottom: 5px;">
		    			<b><s:message code="view.email.progress" text="Progress"/>: </b>
		    			<span id="progressbar_value">0%</span>
			    	</div>
			    	<div id="progressbar"></div>
		    		<fieldset>
		    			<label class="field">
		    				<s:message code="entity.campaign.status" text="Campaign Status"/>
		    			</label>
		    			<div class="value">
		    				<c:if test="${campaign.status eq 'DRA'}">
		    					<s:message code="entity.campaign.status.draft" text="Draft"/>
		    					&nbsp;-&nbsp;
		    					<s:message code="view.email.readyToSend" text="Ready To Send"/>
		    				</c:if>
		    				<c:if test="${campaign.status eq 'SEN'}">
		    					<s:message code="entity.campaign.status.sent" text="Sent"/>
		    					&nbsp;-&nbsp;
		    					<fmt:formatDate value="${campaign.finishTime }" pattern="${FMT.displayTimestamp }"/> 
		    				</c:if>
		    				<c:if test="${campaign.status eq 'INP'}">
		    					<s:message code="entity.campaign.status.inprogress" text="In Progress"/>
		    					&nbsp;-&nbsp;
		    					<fmt:formatDate value="${campaign.sendTime }" pattern="${FMT.displayTimestamp }"/>
		    				</c:if>
		    				<c:if test="${campaign.status eq 'CAN'}">
		    					<s:message code="entity.campaign.status.cancelled" text="Cancelled"/>
		    				</c:if>
		    				<c:if test="${campaign.status eq 'SCH'}">
		    					<s:message code="entity.campaign.status.scheduled" text="Scheduled"/>
		    					&nbsp;-&nbsp;
		    					<fmt:formatDate value="${campaign.scheduledTime }" pattern="${FMT.displayTimestamp }"/> 
		    				</c:if>
		    			</div>
		    		</fieldset>
		    		<div>
		    			<div style="padding:5px;">
							<input type="radio" id="send_real1" name="sendReal" value="1" checked="checked">
							<label for="send_real1"><s:message code="view.email.sendNow" text="Send now"/></label>
						</div>
						<div style="padding:5px;">
							<input type="radio" id="send_real2" name="sendReal" value="2">
							<label for="send_real2"><s:message code="view.email.scheduleDelivery" text="Schedule delivery"/></label>&nbsp;
							<input type="text" id="scheduled_date" class="date" style="width:120px"/>
							&nbsp;
							<select id="scheduled_hour">
								<option value="0">Hour</option>
								<c:forTokens var="hour" items="0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23" delims=",">
									<option var="${hour }">${hour }</option>
								</c:forTokens>
							</select>
							<select id="scheduled_minute">
								<option value="0">Minute</option>
								<option value="0">0</option><option value="15">15</option>
								<option value="30">30</option><option value="45">45</option>
							</select>
							${FMT.inputDate } ${FMT.inputTime }
						</div>
						<br/>
		    			<input type="button" id="send_btn" value="Send Campaign" style="width:300px;"/>
		    			<div id="cancel_delivery_div" style="display: none;">
		    				<input type="button" id="cancel_delivery_btn" value="Cancel Delivery" style="width:300px;"/>
		    			</div>
		    		</div>
		    	</div>
	    	</div>
	    	<div style="width:95%;margin: 20px;">
	    </div>
	    <div class="clear"></div>
	</article>
	<div class="spacer"></div>
	</section>
	
	<div id="notification" class="jGrowl top-right"></div>
</body>

</html>