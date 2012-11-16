<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>

<h3>Last ${count } campaigns</h3>
<table class="tablesorter">
	<thead>
		<tr>
			<th style="width:35%;">Campaign</th>
			<th style="width:25%;">List</th>
			<th style="text-align: center;width:100px;">Emails</th>
			<th>Status</th>
			<th style="width:120px;">Created By</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="campaign" items="${campaigns }">
		<tr>
			<td>
				<a href="${PATH.campaigns }/view/${campaign.id}">
					<s:message text="${campaign.campaignName }"/>
				</a>
			</td>
			<td>
				<a href="${PATH.subscriberLists }/view/${campaign.list.id}">
					<s:message text="${campaign.list.listName }"/>
				</a>
			</td>
			<td style="text-align: center;">
				<fmt:formatNumber value="${campaign.emails }" pattern="###,###"/>
			</td>
			<td>
				<c:if test="${campaign.status eq 'DRA'}">
					<s:message code="entity.campaign.status.DRA" text="Draft"/>
				</c:if>
				<c:if test="${campaign.status eq 'INP'}">
					<s:message code="entity.campaign.status.INP" text="In Progress"/>
				</c:if>
				<c:if test="${campaign.status eq 'SEN'}">
					<span title="${campaign.sendTime}">
						<s:message code="entity.campaign.status.SEN" text="Sent"/>&nbsp;-
						<fmt:formatDate value="${campaign.sendTime }" pattern="${FMT.displayDate }"/>
					</span>
				</c:if>
				<c:if test="${campaign.status eq 'CAN'}">
					<s:message code="entity.campaign.status.CAN" text="Cancelled"/>
				</c:if>
				<c:if test="${campaign.status eq 'SCH'}">
					<span title="${campaign.scheduledTime}">
						<s:message code="entity.campaign.status.SCH" text="Scheduled"/>&nbsp;-
						<fmt:formatDate value="${campaign.scheduledTime }" pattern="${FMT.displayDate }"/>
					</span>
				</c:if>
			</td>
			<td><s:message text="${campaign.createdBy }"/></td>
		</tr>
		</c:forEach>
	</tbody>
</table>