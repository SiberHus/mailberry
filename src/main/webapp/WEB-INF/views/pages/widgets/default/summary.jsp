<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<table style="float:left; width:220px; margin-right: 15px;">
	<caption style="text-decoration: underline;font-weight: bold;">Campaign Summary</caption>
	<thead>
		<tr>
			<th style="text-align: left;">Status</th>
			<th style="text-align: right;">Total</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td>
				<span style="color:#2C6FF3; font-weight:bold;">
					<s:message code="entity.campaign.status.DRA" text="Draft"/>
				</span>
			</td>
			<td style="text-align: right;"><fmt:formatNumber value="${campaignStatus.DRA }" pattern="###,###"/></td>
		</tr>
		<tr>
			<td>
				<span style="color:#E95325; font-weight:bold;">
					<s:message code="entity.campaign.status.INP" text="In Progress"/>
				</span>
			</td>
			<td style="text-align: right;"><fmt:formatNumber value="${campaignStatus.INP }" pattern="###,###"/></td>
		</tr>
		<tr>
			<td>
				<span style="color:#0D9F17; font-weight:bold;">
					<s:message code="entity.campaign.status.SEN" text="Sent"/>
				</span>
			</td>
			<td style="text-align: right;"><fmt:formatNumber value="${campaignStatus.SEN }" pattern="###,###"/></td>
		</tr>
		<tr>
			<td>
				<span style="color:#931699; font-weight:bold;">
					<s:message code="entity.campaign.status.SCH" text="Scheduled"/>
				</span>
			</td>
			<td style="text-align: right;"><fmt:formatNumber value="${campaignStatus.SCH }" pattern="###,###"/></td>
		</tr>
		<tr>
			<td>
				<span style="color:#A59CA5; font-weight:bold;">
					<s:message code="entity.campaign.status.CAN" text="Cancelled"/>
				</span>
			</td>
			<td style="text-align: right;"><fmt:formatNumber value="${campaignStatus.CAN }" pattern="###,###"/></td>
		</tr>
		<tr >
			<td style="border-top-width: thin;border-top-style: dashed;border-top-color: gray;">
				<strong>All</strong>
			</td>
			<td style="text-align: right;"><fmt:formatNumber value="${campaignStatus.ALL }" pattern="###,###"/></td>
		</tr>
	</tbody>
</table>

<table style="float:left; width:220px; margin-right: 15px;">
	<caption style="text-decoration: underline;font-weight: bold;">Subscriber Summary</caption>
	<thead>
		<tr>
			<th style="text-align: left;">Status</th>
			<th style="text-align: right;">Total</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td>
				<span style="color:#1C9F66; font-weight:bold;">
					<s:message code="entity.subscriber.status.ACT" text="Active"/>
				</span>
			</td>
			<td style="text-align: right;"><fmt:formatNumber value="${subscriberStatus.ACT}" pattern="###,###"/></td>
		</tr>
		<tr>
			<td>
				<span style="color:#858F8B; font-weight:bold;">
					<s:message code="entity.subscriber.status.INA" text="Inactive"/>
				</span>
			</td>
			<td style="text-align: right;"><fmt:formatNumber value="${subscriberStatus.INA }" pattern="###,###"/></td>
		</tr>
		<tr>
			<td>
				<span style="color:#E79C50; font-weight:bold;">
					<s:message code="entity.subscriber.status.BLO" text="Blocked"/>
				</span>
			</td>
			<td style="text-align: right;"><fmt:formatNumber value="${subscriberStatus.BLO }" pattern="###,###"/></td>
		</tr>
		<tr>
			<td>
				<span style="color:#D73021; font-weight:bold;">
					<s:message code="entity.subscriber.status.UNS" text="Unsubscribed"/>
				</span>
			</td>
			<td style="text-align: right;"><fmt:formatNumber value="${subscriberStatus.UNS }" pattern="###,###"/></td>
		</tr>
		<tr>
			<td>
				<span style="color:#3C5DC1; font-weight:bold;">
					<s:message code="entity.subscriber.status.TES" text="Test"/>
				</span>
			</td>
			<td style="text-align: right;"><fmt:formatNumber value="${subscriberStatus.TES }" pattern="###,###"/></td>
		</tr>
		<tr >
			<td style="border-top-width: thin;border-top-style: dashed;border-top-color: gray;">
				<strong>All</strong>
			</td>
			<td style="text-align: right;">
				<fmt:formatNumber value="${subscriberStatus.ALL }" pattern="###,###"/>
			</td>
		</tr>
	</tbody>
</table>
<table style="float:left; width:200px;margin-top: 25px;margin-right:15px;
	border-style: dashed;border-width: thin; border-color: gray;background-color: #dfdfdf;"">
	<thead>
		<tr></tr>
	</thead>
	<tbody>
		<tr>
			<td><strong>Total Lists</strong></td>
			<td style="text-align: right;">
				<fmt:formatNumber value="${totalLists }" pattern="###,###"/>
			</td>
		</td>
		<tr>
			<td><strong>Unique Subscribers</strong></td>
			<td style="text-align: right;">
				<fmt:formatNumber value="${uniqueSubscribers }" pattern="###,###"/>
			</td>
		</td>
		<tr>
			<td><strong>Total Blacklists</strong></td>
			<td style="text-align: right;">
				<fmt:formatNumber value="${totalBlacklists }" pattern="###,###"/>
			</td>
		</td>
	</tbody>
</table>
<table style="float:left; width:200px;margin-top: 25px;
	border-style: dashed;border-width: thin; border-color: gray;background-color: #dfdfdf;">
	<thead>
		<tr></tr>
	</thead>
	<tbody>
		<tr>
			<td><strong>Successfully Sent</strong></td>
			<td style="text-align: right;">
				<fmt:formatNumber value="${successMails }" pattern="###,###"/>
			</td>
		</td>
		<tr>
			<td><strong>Oops</strong></td>
			<td style="text-align: right;">
				<fmt:formatNumber value="${errorMails }" pattern="###,###"/>
			</td>
		</td>
		<tr>
			<td><strong>Total</strong></td>
			<td style="text-align: right;">
				<fmt:formatNumber value="${successMails+errorMails }" pattern="###,###"/>
			</td>
		</td>
	</tbody>
</table>
<div class="clear"/>

