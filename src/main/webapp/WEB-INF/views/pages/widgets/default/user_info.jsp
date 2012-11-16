<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<table style="float:left; width:49%;margin-right:1%;margin-bottom: 15px;
	border-style: dashed;border-width: thin; border-color: gray;background-color: #dfdfdf;">
	<tbody>
		<tr>
			<td><strong>My Campaigns</strong></td>
			<td style="text-align: right;">
				<fmt:formatNumber value="${myCampaigns }" pattern="###,###"/>
			</td>
		</td>
		<tr>
			<td><strong>My Lists</strong></td>
			<td style="text-align: right;">
				<fmt:formatNumber value="${myLists }" pattern="###,###"/>
			</td>
		</td>
		<tr>
			<td><strong>My Subscribers</strong></td>
			<td style="text-align: right;">
				<fmt:formatNumber value="${mySubscribers }" pattern="###,###"/>
			</td>
		</td>
		<tr>
			<td><strong>My Mail Accounts</strong></td>
			<td style="text-align: right;">
				${myMailAccounts }
			</td>
		</td>
	</tbody>
</table>

<table style="float:left; width:50%;
	border-style: dashed;border-width: thin; border-color: gray;background-color: #dfdfdf;">
	<tbody>
		<tr>
			<td><strong>Username</strong></td>
			<td>
				<s:message text="${user.username }"/>
			</td>
		</td>
		<tr>
			<td><strong>Email</strong></td>
			<td>
				<s:message text="${user.email }"/>
			</td>
		</td>
		<tr>
			<td><strong>Full Name</strong></td>
			<td>
				<s:message text="${user.firstName }"/>&nbsp;
				<s:message text="${user.lastName }"/>
				<br/>
			</td>
		</td>
		<tr>
			<td style="text-align: center;border-top-style: dashed; border-top-color: black;border-top-width: thin;" colspan="2">
				<br/>
				<a href="${PATH.users }/pwd/${user.id}">Change password</a>
			</td>
		</td>
	</tbody>
</table>

<%--
<div style="float:left; width:220px; marign-right:1%">
	
</div>
<div style="float:left; width:69%;">
	<fieldset style="float:left; width:49%; margin-right: 1%;">
		<label class="field">
			<s:message code="entity.user.username" text="Username"/> 
		</label>
		<div class="value">
			<s:message text="${user.username }"/>
		</div>
	</fieldset>
	<fieldset style="float:left; width:49%;" >
		<label class="field">
			<s:message code="entity.user.email" text="Email"/> 
		</label>
		<div class="value">
			<s:message text="${user.email }"/>
		</div>
	</fieldset>
	<fieldset style="float:left; width:49%; margin-right: 1%;">
		<label class="field">
			<s:message code="entity.user.fullName" text="Full Name"/> 
		</label>
		<div class="value">
			<s:message text="${user.firstName }"/>&nbsp;
			<s:message text="${user.lastName }"/>
		</div>
	</fieldset>
	<fieldset style="float:left; width:49%;" >
		<label class="field">
			<s:message code="entity.user.lastName" text="Last Name"/> 
		</label>
		<div class="value">
			
		</div>
	</fieldset>
</div>
 --%>
<div class="clear"/>