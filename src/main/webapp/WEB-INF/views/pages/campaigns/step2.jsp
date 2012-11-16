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
		$("#list_datagrid").jqGrid({
			pager: 'list_datagrid_pager',
			url:'${PATH.subscriberLists}/list/data',
			colNames:['ID','List Name', 'Total Fields', 'Status'],
			colModel:[
				{name:'id', width:10},
				{name:'listName', width:100},
				{name:'totalFields', width:100},
				{name:'status', width:30}
			]
		});
		$('#list_datagrid').setGridHeight(230);
		$('#list_datagrid').setGridWidth(600);
		$("#list_dialog").dialog({
			autoOpen: false,height: 420,width: 630,modal: true,
			buttons: {
				Select: function() {
					var rows = grid.getSelectedValues('#list_datagrid');
					$('#list_id').val(grid.getSelectedId('#list_datagrid'));
					$('#list_name').val(rows['listName']);
					$(this).dialog('close');
				},
				Cancel: function() {
					$(this).dialog('close');
				}
			}
		});
		$('#browse_lists').click(function(){
			$("#list_dialog").dialog('open');
		});
		$('#list_name').click(function(){
			$("#list_dialog").dialog('open');
		});
		$('#back_btn').click(function(){
			window.location.href='${PATH.campaigns}/step1';
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
				<a href="${PATH.campaigns}/step1">
					<s:message code="view.campaign.step1" text="Step1 - Select Message Type"/>
				</a>
				<div class="breadcrumb_divider"></div>
				<a class="current">
					<s:message code="view.campaign.step2" text="Step2 - Create Campaign"/>
				</a>
				<div class="breadcrumb_divider"></div>
				<a href="${PATH.campaigns}/step3">
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
	<form:form id="main_form" method="post" action="${PATH.campaigns}/step2" modelAttribute="campaignStep2FormBean">
	<%@include file="/WEB-INF/views/includes/body/form_error.jsp" %>
	<article class="module width_full">
		<header>
			<h3>
			<s:message code="view.campaign.step2" text="Step2 - Create Campaign"/>
			</h3>
		</header>
		<div class="module_content">
			<fieldset>
				<label class="field">
					<s:message code="entity.campaign.campaignName" text="Campaign Name"/>
					<span class="req">*</span>
				</label>
				<form:input path="campaignName" />
				<form:errors path="campaignName" cssClass="error" />
			</fieldset>
			<fieldset style="width:48%; float:left; margin-right: 3%;">
				<label class="field">
					<s:message code="entity.subscriberList" text="Subscriber List"/>
					<span class="req">*</span>
				</label>
				<form:hidden id="list_id" path="listId"/>
				<form:input id="list_name" path="listName" style="width:60%;" readonly="true"/>
				<input id="browse_lists" type="button" value="Browse" />
				<form:errors path="listId" cssClass="error"/>
			</fieldset>
			<fieldset style="width:48%; float:left;">
				<label class="field">
					<s:message code="entity.mailAccount" text="Mail Account"/>
					<span class="req">*</span>
				</label>
				<form:select path="mailAccountId">
				 	<form:options items="${mailAccounts}" itemLabel="displayName" itemValue="id"/>
				</form:select>
				<form:errors path="mailAccountId" cssClass="error"/>
				<br/>
			</fieldset>
			<div class="clear"></div>
			
			<fieldset>
				<label class="field">
					<s:message code="entity.campaign.description" text="Description"/>
				</label>
				<form:input path="description" />
			</fieldset>
			<fieldset style="width:48%; float:left; margin-right: 3%;">
				<label class="field">
					<s:message code="entity.campaign.startDate" text="Start Date"/>
				</label>
				<form:input path="startDate" cssClass="date" cssStyle="width:100px;"/>
			</fieldset>
			<fieldset style="width:48%; float:left;">
				<label class="field">
					<s:message code="entity.campaign.endDate" text="End Date"/>
				</label>
				<form:input path="endDate" cssClass="date" cssStyle="width:100px;"/>
			</fieldset>
			<div class="clear"></div>
			
			<fieldset style="width:48%; float:left; margin-right: 3%;">
				<label class="field">
					<s:message code="view.campaign.step2.blacklist" text="Blacklist"/>
				</label>
				<div class="value">
					<form:checkbox id="blacklistEnabled" path="blacklistEnabled"/>
					<label for="blacklistEnabled">
						<s:message code="entity.campaign.blacklist" text="Blacklist Enabled"/>
					</label>
					<br/>
				</div>
			</fieldset>
			<fieldset style="width:48%; float:left;">
				<label class="field">
					<s:message code="view.campaign.step2.emailTracking" text="Email Tracking"/>
				</label>
				<div class="value">
					<form:checkbox id="trackable" path="trackable"/>
					<label for="trackable">
						<s:message code="entity.campaign.trackable" text="Trackable"/>
					</label>
					<br/>
					<form:checkbox id="clickstream" path="clickstream"/>
					<label for="clickstream">
						<s:message code="entity.campaign.clickstream" text="Clickstream"/>
					</label>
				</div>
			</fieldset>
			<div class="clear"></div>
		</div>
		<footer>
			<div class="back_link">
				<input type="button" id="back_btn" value="Back"/>
			</div>
			<div class="submit_link">
				<input type="submit" value="Next" />
			</div>
		</footer>
	</article>
	</form:form>
	<div class="spacer"></div>
	</section>
	
	<div id="list_dialog" class="datagrid" title="Select list">
		<table id="list_datagrid"></table>
		<div id="list_datagrid_pager" style="height:40px"></div>
	</div>
</body>

</html>