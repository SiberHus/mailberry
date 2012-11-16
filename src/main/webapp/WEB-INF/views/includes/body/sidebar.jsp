<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
	
	<aside id="sidebar" class="column">
		<br/>
		<h3>MESSAGES</h3>
		<ul class="toggle">
			<li class="campaigns"><a href="${PATH.campaigns}/">Campaigns</a></li>
			<li class="calendar"><a href="${PATH.calendar }/">Events Calendar</a></li>
			<li class="message_templates"><a href="${PATH.messageTemplates}/">Message Templates</a></li>
			<li class="template_variables"><a href="${PATH.templateVariables}/">Template Variables</a></li>
			<li class="template_chunks"><a href="${PATH.templateChunks}/">Template Chunks</a></li>
		</ul>
		<h3>DATA LISTS</h3>
		<ul class="toggle">
			<li class="subscriber_lists"><a href="${PATH.subscriberLists }/">Subscriber Lists</a></li>
			<li class="blacklist"><a href="${PATH.blacklists }/">Blacklist</a></li>
			<li class="import"><a href="${PATH.imports }/">Import</a></li>
			<li class="export"><a href="${PATH.exports }/">Export</a></li>
		</ul>
		<h3>ANALYTICS</h3>
		<ul class="toggle">
			<li class="campaign_tracking"><a href="${PATH.analytics }/campaigns/track">Track Campaign</a></li>
			<li class="clientinfo"><a href="${PATH.analytics }/clientinfo">Client Info</a></li>
			<li class="campaign_comparison"><a href="${PATH.analytics }/campaigns/compare">Compare Campaigns</a></li>
		</ul>
		<h3>TOOLS</h3>
		<ul class="toggle">
			<!-- 
			<li class="subscriber_form_builder"><a href="#">Subscriber Form Builder</a></li>
			<li class="file_manager"><a href="#">File Manager</a></li>
			<li class="image_gallery"><a href="#">Image Gallery</a></li>
			 -->
			<li class="custom_pages"><a href="${PATH.customPages }/">Custom Pages</a></li>
			<sec:authorize access="hasRole('ROLE_ADMIN')">
			<li class="db_console"><a href="${ctx }/h2" target="_blank">Database Console</a></li>
			</sec:authorize>
		</ul>
		<h3>SETTINGS</h3>
		<ul class="toggle">
			<li class="mail_servers"><a href="${PATH.mailServers }/">Mail Servers</a></li>
			<li class="mail_accounts"><a href="${PATH.mailAccounts }/">Mail Accounts</a></li>
			<sec:authorize access="hasRole('ROLE_ADMIN')">
			<li class="dashboard_widgets"><a href="${PATH.dashboard }/widgets/">Dashboard Widgets</a></li>
			<li class="manage_users"><a href="${PATH.users }/">Manage Users</a></li>
			<li class="configurations"><a href="${PATH.configs }/">Configurations</a></li>
			</sec:authorize>
		</ul>
		<footer>
			<hr />
			<p><strong>Copyright &copy; 2011 <a href="http://www.siberhus.com">SiberHus.com</a></strong></p>
			<p>Theme by <a href="http://www.medialoot.com">MediaLoot</a></p>
		</footer>
	</aside>