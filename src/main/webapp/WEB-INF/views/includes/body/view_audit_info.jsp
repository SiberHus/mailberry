<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<fieldset style="width:49%; float:left; margin-right: 1%;"> <!-- to make two field float next to one another, adjust values accordingly -->
	<label class="field">
		<s:message code="entity.audit.createdBy" text="Created By"/>
	</label>
	<div class="value">
		<s:message text="${command.createdBy }"/>
	</div>
	<label class="field">
		<s:message code="entity.audit.createdAt" text="Created At"/>
	</label>
	<div class="value">
		<s:message text="${command.createdAt }"/>
	</div>
</fieldset>
<fieldset style="width:49%; float:left;"> <!-- to make two field float next to one another, adjust values accordingly -->
	<label class="field">
		<s:message code="entity.audit.lastModifiedBy" text="Last Modified By"/>
	</label>
	<div class="value">
		<s:message text="${command.lastModifiedBy }"/>
	</div>
	<label class="field">
		<s:message code="entity.audit.lastModifiedAt" text="Last Modified At"/>
	</label>
	<div class="value">
		<s:message text="${command.lastModifiedAt }"/>
	</div>
</fieldset>
<div class="clear"></div>