<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html lang="en">
<head>
	<%@include file="/WEB-INF/views/includes/head/res_standard.jsp" %>
	<script type="text/javascript" src="${ctx }/resources/comps/jscolor/jscolor.js"></script>
	<style type="text/css">
		table.fieldset caption {font-size: 1.6em;text-align: left;padding-left: 5px;}
		table.fieldset {width:100%; margin: 10px 0px 15px 0px;}
		table.fieldset th.field{width:300px;}
		table.fieldset td.field{font-weight: bold;color:#888888;}
		form input[type=text]{width:80%;}
		span.info {display: block; font-size: 0.8em; color: #aaaaaa;}
		table.colors {width: 300px;}
		table.colors th{width: 100px;}
		
		.ui-tabs-vertical { width: 40em; }
		.ui-tabs-vertical .ui-tabs-nav { padding: .2em .1em .2em .2em; float: left; width: 15em; }
		.ui-tabs-vertical .ui-tabs-nav li { clear: left; width: 100%; border-bottom-width: 1px !important; border-right-width: 0 !important; margin: 0 -1px .2em 0; }
		.ui-tabs-vertical .ui-tabs-nav li a { display:block; }
		.ui-tabs-vertical .ui-tabs-nav li.ui-tabs-selected { padding-bottom: 0; padding-right: .1em; border-right-width: 1px; border-right-width: 1px; }
		.ui-tabs-vertical .ui-tabs-panel { padding: 1em; float: left; width: 20em;}
		
	</style>
	<script type="text/javascript">
	function createFormHandler(formId){
		$('#'+formId).submit(function() {  
			$.post($(this).attr("action"), $(this).serialize(), function(data) {
				$('#'+formId+' span.error').text('');
				if(data.error){
					$.each(data.fieldErrors, function(key, value){
						key = key.replace(/\./g,'\\.');
						$('#'+formId+' span[ref='+key+']').text(value);
					});
				}else{
					jprompt.alert('Your configuration has been successfully saved!');
				}
			});
			return false;
		});
	}
	$(document).ready(function() {
		$('#main_tabs').tabs();
		
		$("#calendar_tabs").tabs().addClass('ui-tabs-vertical ui-helper-clearfix');
		$("#calendar_tabs li").removeClass('ui-corner-top').addClass('ui-corner-left');
		
		createFormHandler('config_base_form');
		createFormHandler('config_ui_form');
		createFormHandler('config_subscriber_form');
		createFormHandler('config_format_form');
		createFormHandler('config_email_form');
		
	});
    </script>
</head>
<body>
	<c:set var="pageTitle" value="Configurations"/>
	<%@include file="/WEB-INF/views/includes/body/header.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/secondary_bar.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/sidebar.jsp" %>
	<section id="main" class="column">
	
	<article class="module width_full">
		<header>
			<h3>
			<img src="${ctx }/resources/images/icons/chart_bar.png" />
			Configurations
			</h3>
		</header>
		<div class="module_content" >
			<div id="main_tabs">
				<ul>
					<li><a href="#base_tab">Base</a></li>
					<li><a href="#ui_tab">Web Interface</a></li>
					<li><a href="#subscriber_tab">Subscriber</a></li>
					<li><a href="#format_tab">Data Format</a></li>
					<li><a href="#email_tab">Email</a></li>
				</ul>
				<div id="base_tab">
					<%@include file="/WEB-INF/views/pages/configs/config_base.jsp" %>	
				</div>
				<div id="ui_tab">
					<%@include file="/WEB-INF/views/pages/configs/config_ui.jsp" %>	
				</div>
				<div id="subscriber_tab">
					<%@include file="/WEB-INF/views/pages/configs/config_subscriber.jsp" %>		
				</div>
				<div id="format_tab">
					<%@include file="/WEB-INF/views/pages/configs/config_format.jsp" %>
				</div>
				<div id="email_tab">
					<%@include file="/WEB-INF/views/pages/configs/config_email.jsp" %>
				</div>
			</div>
			<div class="clear"></div>
		</div>
		
	</article>
	<div class="spacer"></div>
	</section>
	
</body>

</html>