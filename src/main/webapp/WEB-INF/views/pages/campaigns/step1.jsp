<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ckeditor" uri="http://www.siberhus.com/taglibs/ckeditor"%>
<!doctype html>
<html lang="en">
<head>
	<%@include file="/WEB-INF/views/includes/head/res_standard.jsp" %>
	<%@include file="/WEB-INF/views/includes/head/res_jqgrid.jsp" %>
	<script type="text/javascript">
	$(document).ready(function() {
		$("#campaign_datagrid").jqGrid({
			url:'${PATH.campaigns}/list/data',
			pager: '#campaign_datagrid_pager',
			colNames:['ID','Campaign Name', 'Subscriber List', 'Status'],
			colModel:[
				{name:'id', width:10},
				{name:'campaignName', width:100},
				{name:'list.listName', width:100},
				{name:'status', width:30}
			]
		});
		$('#campaign_datagrid').setGridHeight(230);
		$('#campaign_datagrid').setGridWidth(600);
		$("#campaign_dialog").dialog({
			autoOpen: false,height: 420,width: 630,modal: true,
			buttons: {
				Select: function() {
					var rows = grid.getSelectedValues('#campaign_datagrid');
					$('#campaign_id').val(grid.getSelectedId('#campaign_datagrid'));
					$('#campaign_name').val(rows['campaignName']);
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
		$("#template_datagrid").jqGrid({
			url:'${PATH.messageTemplate}/list/data',
			pager: '#template_datagrid_pager',
			colNames:['ID','Template Name', 'Description'],
			colModel:[
				{name:'id', width:10},
				{name:'templateName', width:100},
				{name:'description', width:100}
			]
		});
		$('#template_datagrid').setGridHeight(230);
		$('#template_datagrid').setGridWidth(600);
		$("#template_dialog").dialog({
			autoOpen: false,height: 420,width: 630,modal: true,
			buttons: {
				Select: function() {
					var rows = grid.getSelectedValues('#template_datagrid');
					$('#template_id').val(grid.getSelectedId('#template_datagrid'));
					$('#template_name').val(rows['templateName']);
					$(this).dialog('close');
				},
				Cancel: function() {
					$(this).dialog('close');
				}
			}
		});
		$('#browse_templates').click(function(){
			$("#template_dialog").dialog('open');
		});
		$('#template_name').click(function(){
			$("#template_dialog").dialog('open');
		});
		$('#createFrom_blank').click(function(){
			$('#template_panel').fadeOut();
			$('#campaign_panel').fadeOut();
		});
		$('#createFrom_template').click(function(){
			$('#template_panel').fadeIn();
			$('#campaign_panel').fadeOut();
		});
		$('#createFrom_replicate').click(function(){
			$('#template_panel').fadeOut();
			$('#campaign_panel').fadeIn();
		});
		<c:if test="${campaignStep1FormBean.createFrom eq 'template'}">
		$('#template_panel').fadeIn();
		</c:if>
		<c:if test="${campaignStep1FormBean.createFrom eq 'replicate'}">
		$('#campaign_panel').fadeIn();
		</c:if>
		
		$('#back_btn').click(function(){
			window.location.href='${PATH.campaigns}/';
		});
	});
    </script>
</head>
<body>
	<c:set var="pageTitle" value="Campaigns"/>
	<%@include file="/WEB-INF/views/includes/body/header.jsp" %>
	<section id="secondary_bar">
		<%@include file="/WEB-INF/views/includes/body/user.jsp" %>
		<div class="breadcrumbs_container">
			<article class="breadcrumbs">
				<a class="current">
					<s:message code="view.campaign.step1" text="Step1 - Select Message Type"/>
				</a>
				<div class="breadcrumb_divider"></div>
				<a href="${PATH.campaigns }/step2">
					<s:message code="view.campaign.step2" text="Step2 - Create Campaign"/>
				</a>
				<div class="breadcrumb_divider"></div>
				<a href="${PATH.campaigns }/step3">
					<s:message code="view.campaign.step3" text="Step3 - Compose Message"/>
				</a>
				<div class="breadcrumb_divider"></div>
				<a href="#">
					<s:message code="view.campaign.finish" text="Finish - Deliver Message"/>
				</a>
			</article>
		</div>
	</section>
	<%@include file="/WEB-INF/views/includes/body/sidebar.jsp" %>
	<section id="main" class="column">
	<form:form id="main_form" method="post" action="${PATH.campaigns }/step1" modelAttribute="campaignStep1FormBean">
	<%@include file="/WEB-INF/views/includes/body/form_error.jsp" %>
	<article class="module width_full">
		<header>
			<h3>
			<s:message code="view.campaign.step1" text="Step1 - Select Message Type"/>
			</h3>
		</header>
		<div class="module_content">
			<fieldset style="width:48%; float:left; margin-right: 3%;"> <!-- to make two field float next to one another, adjust values accordingly -->
				<label class="field">
					<s:message code="view.campaign.new" text="New campaign"/>
				</label>
				<div class="value">
					<div>
						<form:radiobutton id="createFrom_blank" path="createFrom" value="blank"/>
						<label for="createFrom_blank">
							<s:message code="view.campaign.createBlank" text="Create from scratch"/>
						</label>
					</div>
					<br/>
					<div>
						<form:radiobutton id="createFrom_template" path="createFrom" value="template"/>
						<label for="createFrom_template">
							<s:message code="view.campaign.loadFromTemplate" text="Load from template"/>
						</label>
					</div>
					<form:hidden id="template_id" path="templateId"/><br/>
				  	<div id="template_panel" style="display: none">
				  		<form:input id="template_name" path="templateName" style="width:60%;" readonly="true"/>
				  		<input id="browse_templates" type="button" value="Browse" />
				  		<form:errors path="createFrom" cssClass="error" />
				  	</div>
				  	<div>
				  		<form:radiobutton id="createFrom_replicate" path="createFrom" value="replicate"/>
				  		<label for="createFrom_replicate">
							<s:message code="view.campaign.replicateCampaign" text="Replicate campaign"/>
						</label>
					</div>
					<form:hidden id="campaign_id" path="campaignId"/><br/>
				  	<div id="campaign_panel" style="display: none">
				  		<form:input id="campaign_name" path="campaignName" style="width:60%;" readonly="true"/>
				  		<input id="browse_campaigns" type="button" value="Browse" />
				  		<form:errors path="createFrom" cssClass="error" />
				  	</div>
				</div>
			</fieldset>
			<fieldset style="width:48%; float:left;"> <!-- to make two field float next to one another, adjust values accordingly -->
				<label class="field">
					<s:message code="entity.campaign.messageType" text="Message Type"/>
				</label>
				<div class="value">
					<div>
						<form:radiobutton id="mtype_text" path="messageType" value="TEXT" />
						<label for="mtype_text">
							<s:message code="view.campaign.step1.plainText" text="Plain/Text"/>
						</label>
					</div><br/>
					<div>
						<form:radiobutton id="mtype_html" path="messageType" value="HTML" />
						<label for="mtype_html">
							<s:message code="view.campaign.step1.html" text="HTML (Rich Text)"/>
						</label>
					</div><br/>
					<div>
						<form:radiobutton id="mtype_mix" path="messageType" value="MIX" />
						<label for="mtype_mix">
							<s:message code="view.campaign.step1.mix" text="Plain/Text + HTML"/>
						</label>
					</div><br/>
				</div>
			</fieldset>
			<div class="clear"></div>
			<fieldset>
				<label class="field">
					<s:message code="view.campaign.step1.advance" text="Advance"/>
				</label>
				<div class="value">
					<form:checkbox id="velocity" path="velocity" value="true"/>
					<label for="velocity">
						<s:message code="view.campaign.step1.velocity" text="Advanced Template (Apache Velocity)"/>
					</label>
				</div>
			</fieldset>
			<div class="clear"></div>
		</div>
		<footer>
			<div class="back_link">
				<input type="button" id="back_btn" value="Back" />
			</div>
			<div class="submit_link">
				<input type="submit" value="Next" />
			</div>
		</footer>
	</article>
	</form:form>
	<div class="spacer"></div>
	</section>
	
	<div id="campaign_dialog" class="datagrid" title="Select campaign">
		<table id="campaign_datagrid"></table>
		<div id="campaign_datagrid_pager"></div>
	</div>
	<div id="template_dialog" class="datagrid" title="Select template">
		<table id="template_datagrid"></table>
		<div id="template_datagrid_pager"></div>
	</div>
</body>

</html>