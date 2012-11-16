<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!doctype html>
<html lang="en">
<head>
	<%@include file="/WEB-INF/views/includes/head/res_standard.jsp" %>
	<c:set var="PATH.campaign">
		<%=com.siberhus.mailberry.controller.CampaignsController.PATH%>
	</c:set>
	<script type="text/javascript">
	$(document).ready(function() {
		$('#editors').tabs();
		crud.baseUri='${PATH.campaigns}';
		$('#back_btn').click(function(){
			history.back();
		});
		$('#edit_btn').click(function(){
			crud.editObject('${campaign.id }');
		});
		$('#delete_btn').click(function(){
			crud.deleteObject('${campaign.id }');
		});
		$('#cancel_btn').click(function(){
			crud.goToParentPage();
		});
	});
    </script>
</head>
<body>
	<c:set var="pageTitle" value="Campaigns"/>
	<%@include file="/WEB-INF/views/includes/body/header.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/secondary_bar.jsp" %>
	<%@include file="/WEB-INF/views/includes/body/sidebar.jsp" %>
	
	<section id="main" class="column">
	<article class="module width_full">
		<header>
			<h3 class="tabs_involved">
				<img src="${ctx }/resources/images/icons/transmit_blue.png"/>
				<s:message code="entity.campaign" text="Campaign"/>
				(&nbsp;#${campaign.id }&nbsp;)
			</h3>
		</header>
		<div id="tab1" class="module_content">
			<fieldset>
				<label class="field">
					<s:message code="entity.campaign.campaignName" text="Campaign Name"/>
				</label>
				<div class="value">
					<s:message text="${campaign.campaignName }"/>
				</div>
			</fieldset>
			<fieldset style="width:48%; float:left; margin-right: 3%;">
				<label class="field">
					<s:message code="entity.subscriberList" text="Subscriber List"/>
				</label>
				<div class="value">
					<s:message text="${campaign.list }"/>
				</div>
			</fieldset>
			<fieldset style="width:48%; float:left;">
				<label class="field">
					<s:message code="entity.mailAccount" text="Mail Account"/>
				</label>
				<div class="value">
					<s:message text="${campaign.mailAccount }"/>
				</div>
			</fieldset>
			<div class="clear"></div>
			<fieldset>
				<label class="field">
					<s:message code="entity.campaign.description" text="Description"/>
				</label>
				<div class="value">
					<s:message text="${campaign.description }"/>
				</div>
			</fieldset>
			
			<fieldset style="width:48%; float:left; margin-right: 3%;">
				<label class="field">
					<s:message code="entity.campaign.status" text="Status"/>
				</label>
				<div class="value">
					<c:if test="${campaign.status eq 'DRA'}">
						<s:message code="entity.campaign.status.draft" text="Draft" />
					</c:if>
					<c:if test="${campaign.status eq 'INP'}">
						<s:message code="entity.campaign.status.inProgress" text="In Progress" />
					</c:if>
					<c:if test="${campaign.status eq 'SEN'}">
						<s:message code="entity.campaign.status.sent" text="Sent" />&nbsp;-
						<fmt:formatDate value="${campaign.sendTime }" pattern="${FMT.displayTimestamp }"/>
					</c:if>
					<c:if test="${campaign.status eq 'CAN'}">
						<s:message code="entity.campaign.status.cancelled" text="Cancelled" />
					</c:if>
					<c:if test="${campaign.status eq 'SCH'}">
						<s:message code="entity.campaign.status.scheduled" text="Scheduled" />&nbsp;-
						<fmt:formatDate value="${campaign.scheduledTime }" pattern="${FMT.displayTimestamp }"/>
					</c:if>
				</div>
			</fieldset>
			<fieldset style="width:23%; float:left; margin-right: 2%;">
				<label class="field">
					<s:message code="entity.campaign.startDate" text="Start Date"/>
				</label>
				<div class="value">
					${campaign.startDate }
				</div>
			</fieldset>
			<fieldset style="width:23%; float:left;">
				<label class="field">
					<s:message code="entity.campaign.endDate" text="End Date"/>
				</label>
				<div class="value">
					${campaign.endDate }
				</div>
			</fieldset>
			<div class="clear"></div>
			<fieldset style="width:48%; float:left; margin-right: 3%;">
				<label class="field">
					<s:message code="view.campaign.step2.blacklist" text="Blacklist"/>
				</label>
				<div class="value">
					<c:if test="${campaign.blacklistEnabled }">
						<img src="${ctx }/resources/images/icons/tick.png"/>
					</c:if>
					<c:if test="${not campaign.blacklistEnabled }">
						<img src="${ctx }/resources/images/icons/cross.png"/>
					</c:if>
					<s:message code="entity.campaign.blacklist" text="Blacklist Enabled"/>
				</div>
			</fieldset>
			<fieldset style="width:48%; float:left;">
				<label class="field">
					<s:message code="view.campaign.step2.emailTracking" text="Email Tracking"/>
				</label>
				<div class="value">
					<c:if test="${campaign.trackable }">
						<img src="${ctx }/resources/images/icons/tick.png"/>
					</c:if>
					<c:if test="${not campaign.trackable }">
						<img src="${ctx }/resources/images/icons/cross.png"/>
					</c:if>
					<s:message code="entity.campaign.trackable" text="Trackable"/>
					<br/>
					<c:if test="${campaign.clickstream }">
						<img src="${ctx }/resources/images/icons/tick.png"/>
					</c:if>
					<c:if test="${not campaign.clickstream }">
						<img src="${ctx }/resources/images/icons/cross.png"/>
					</c:if>
					<s:message code="entity.campaign.clickstream" text="Clickstream"/>
				</div>
			</fieldset>
			
			<div class="clear"></div>
			
			<fieldset style="width:48%; float:left; margin-right: 3%;">
				<label class="field">
					<s:message code="entity.campaign.fromEmail" text="From Email"/>
				</label>
				<div class="value">
					<s:message text="${campaign.fromEmail }" />
				</div>
			</fieldset>
			<fieldset style="width:48%; float:left;">
				<label class="field">
					<s:message code="entity.campaign.replyToEmail" text="Reply-To Email"/>
				</label>
				<div class="value">
					<s:message text="${campaign.replyToEmail }" />
				</div>
			</fieldset>
			<div class="clear"></div>
			
			<fieldset style="width:48%; float:left; margin-right: 3%;">
				<label class="field">
					<s:message code="entity.campaign.mailPriority" text="Priority"/>
				</label>
				<div class="value">
					<s:message text="${campaign.mailPriority }" />
				</div>
			</fieldset>
			<fieldset style="width:48%; float:left;">
				<label class="field">
					<s:message code="entity.campaign.messageCharset" text="Charset"/>
				</label>
				<div class="value">
					<s:message text="${campaign.messageCharset }" />
				</div>
			</fieldset>
			<div class="clear"></div>
			<fieldset>
				<label class="field">
					<s:message code="entity.campaign.mailSubject" text="Subject"/>
				</label>
				<div class="value">
					<s:message text="${campaign.mailSubject }" />
				</div>
			</fieldset>
			<fieldset>
				<label class="field">
					<s:message code="view.campaign.messageBody" text="Message Body"/>
				</label>
				<div id="editors">
					<ul>
						<c:if test="${campaign.messageType ne 'TEXT'}">
							<li><a href="#htmlTab"><s:message code="view.text.html" text="HTML"/></a></li>
						</c:if>
						<c:if test="${campaign.messageType ne 'HTML'}">
							<li><a href="#textTab"><s:message code="view.text.text" text="TEXT"/></a></li>
						</c:if>
					</ul>
					<c:if test="${campaign.messageType ne 'TEXT'}">
					<div id="htmlTab">
						<%--
						<s:message text="${campaign.messageBodyHtml }"/>
						 --%>
						<iframe src="${PATH.campaigns }/html/${campaign.id}" width="100%" height="300" >
  							<p>Your browser does not support iframes.</p>
						</iframe>
					</div>
					</c:if>
					<c:if test="${campaign.messageType ne 'HTML'}">
					<div id="textTab">
						<%--
						<s:message text="${campaign.messageBodyText }"/>
						 --%>
						 ${messageBodyText }
					</div>
					</c:if>
				</div>
			</fieldset>
			<fieldset>
				<div class="value">
					<c:if test="${campaign.inlineResource }">
						<img src="${ctx }/resources/images/icons/tick.png"/>
					</c:if>
					<c:if test="${not campaign.inlineResource }">
						<img src="${ctx }/resources/images/icons/cross.png"/>
					</c:if>
					<s:message code="entity.campaign.inlineResource" text="Embed Images"/>
				</div>
			</fieldset>
			<fieldset>
				<label class="field">
					<s:message code="entity.campaign.attachments" text="Attachments"/>
				</label>
				<div class="value">
					<table id="attachmentTbl" style="width: 100%">
						<tr id="attachmentTh">
							<th style="text-align:left;width:30%">
								<s:message code="entity.attachment.fileName" text="File Name"/>
							</th>
							<th style="text-align:left;width:40%">
								<s:message code="entity.attachment.filePath" text="File Path"/>
							</th>
							<th style="text-align:left;width:10%">
								<s:message code="entity.attachment.compressed" text="Compressed"/>
							</th>
							<th style="text-align:left;width:20%">
								<s:message code="entity.attachment.archivePasswd" text="Archive Password"/>
							</th>
								<th>&nbsp;</th>
							</tr>
						<c:forEach var="attachment" items="${campaign.attachments }">
						<tr>
							<td>${attachment.fileName }</td>
							<td>${attachment.filePath }</td>
							<td>${attachment.compressed }</td>
							<td>${attachment.archivePasswd }</td>
						</tr>
						</c:forEach>
					</table>
				</div>
			</fieldset>
			<div class="clear"></div>
			<c:set var="command" value="${campaign }"/>
			<%@include file="/WEB-INF/views/includes/body/view_audit_info.jsp" %>
		</div>
		<footer>
			<div class="back_link">
				<input type="button" id="back_btn" value="Back" />
			</div>
			<div class="submit_link">
				<c:if test="${campaign.status eq 'DRA'}">
					<input type="button" id="edit_btn" value="Edit" />&nbsp;
				</c:if>
				<input type="button" id="delete_btn" value="Delete" />
				&nbsp;
				<input type="button" id="cancel_btn" value="Cancel" />
			</div>
		</footer>
	</article>
	
	<div class="spacer"></div>
	</section>
	
</body>

</html>