<div class="widget ui-widget ui-corner-all" id="widget_${widget.code}">
	<div class="action">
		<img widgetControl="true" src="${ctx }/resources/images/icons/tick.png" alt="Enable"/>
		<img widgetControl="true" src="${ctx }/resources/images/icons/cross.png" alt="Disable"/>
		<img widgetControl="false" src="${ctx }/resources/images/icons/arrow_refresh.png" alt="Refresh"
			style="cursor:pointer;" onclick="widget${widget.code}();"/>
		<%--
		<img widgetControl="true" src="${gs.resource(dir:'images/icons',file:'tick.png')}"  alt="tick" />
		<img widgetControl="true" src="${gs.resource(dir:'images/icons',file:'cross.png')}"  alt="cross" />
		<img widgetControl="false" src="${gs.resource(dir:'images/icons',file:'arrow_refresh.png')}"
			alt="refresh" style="cursor:pointer;" onclick="widget${widget.code}();"/>
		--%>
	</div>
	<div class="header ui-widget-header">
		<span>${widget.name}</span>
	</div>
	<div id="content_${widget.code}" class="body"></div>
	<div id="loading_${widget.code}" class="body loading" style="text-align:center">
		<img src="${ctx }/resources/images/ajax/circle100.gif" alt="Loading" width="100" height="100"/>
	</div>
</div>
