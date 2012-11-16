<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="ckeditor" uri="http://www.siberhus.com/taglibs/ckeditor"%>
<%@ taglib prefix="esc" uri="http://commons.apache.org/lang/StringEscapeUtils"%>

<!doctype html>
<html lang="en">
<head>
	<%@include file="/WEB-INF/views/includes/head/res_standard.jsp" %>
	<%@include file="/WEB-INF/views/includes/head/res_codemirror.jsp" %>
	<script type="text/javascript" src="${ctx }/resources/js/siberhus.js"></script>
	<script type="text/javascript">
	function getRowCount(tableId){
		var oRows = document.getElementById(tableId).getElementsByTagName('tr');
		return oRows.length;
	}
	function deleteAttachment(row){
		TableUI.deleteRow(row);
		var iRowCount = getRowCount('attachmentTbl');
		if(iRowCount==1){
			$('#attachmentTh').hide();
		}
	}
	var currentTextElem;
	function addAttachment(fileName, filePath, compressed, archivePasswd){
		var compressedTrue = "";
		var idx = getRowCount('attachmentTbl')-1;
		if(compressed=='true') compressedTrue = "checked='checked'"; 
		TableUI.addRow('attachmentTbl', [
			"<input type='text' style='width:85%;' name='attachments["+idx+"].fileName' value='"+fileName+"' onclick='currentTextElem=this;'/>",
			"<input type='text' style='width:85%;' name='attachments["+idx+"].filePath' value='"+filePath+"' onclick='currentTextElem=this;'/>",
			"<input type='checkbox' name='attachments["+idx+"].compressed' value='true' "+compressedTrue+"/>",
			"<input type='text' style='width:85%;' name='attachments["+idx+"].archivePasswd' value='"+archivePasswd+"' onclick='currentTextElem=this;'/>",
			"<a style='cursor:pointer;' onclick='deleteAttachment(this);'>"+'<input type="image" src="${ctx}/resources/images/icons/delete.png" title="Delete">'+"</a>"
		],function(){$('#attachmentTh').show();});
	}
	function insertAtCursor(myValue, type) {
		var myField = currentTextElem;
		if(myField==null) return null;
		if(myValue=='_unsubscribe_'){
			var linkName = window.prompt('Please enter link name','${optOutLink}');
			if(linkName!=null && linkName!=''){
				myValue = '<a href="\${_serverUrl_}/t/unsubscribe/\${_campaignId_}/\${_subscriberId_}/\${_securityToken_}" target="_blank">'+linkName+'</a>';
			}else{ return; }
			if(myField instanceof CKEDITOR.editor){
				myField.insertHtml(myValue);
				return;
			}
		}else if(myValue=='_rsvp_attending_'){
			var linkName = window.prompt('Please enter link name','${rsvpAttLink}');
			if(linkName!=null && linkName!=''){
				myValue = '<a href="\${_serverUrl_}/t/rsvp/\${_campaignId_}/\${_subscriberId_}/\${_securityToken_}/ATT" target="_blank">'+linkName+'</a>';
			}else{ return; }
			if(myField instanceof CKEDITOR.editor){
				myField.insertHtml(myValue);
				return;
			}
		}else if(myValue=='_rsvp_maybe_'){
			var linkName = window.prompt('Please enter link name','${rsvpMayLink}');
			if(linkName!=null && linkName!=''){
				myValue = '<a href="\${_serverUrl_}/t/rsvp/\${_campaignId_}/\${_subscriberId_}/\${_securityToken_}/MAY" target="_blank">'+linkName+'</a>';
			}else{ return; }
			if(myField instanceof CKEDITOR.editor){
				myField.insertHtml(myValue);
				return;
			}
		}else if(myValue=='_rsvp_declined_'){
			var linkName = window.prompt('Please enter link name','${rsvpDecLink}');
			if(linkName!=null && linkName!=''){
				myValue = '<a href="\${_serverUrl_}/t/rsvp/\${_campaignId_}/\${_subscriberId_}/\${_securityToken_}/DEC" target="_blank">'+linkName+'</a>';
			}else{ return; }
			if(myField instanceof CKEDITOR.editor){
				myField.insertHtml(myValue);
				return;
			}
		}
		
		if(type==0) { myValue = '\${'+myValue+'}'; }
		else if(type==1) { myValue = '$('+myValue+')'; }
		else if(type==2) { myValue = '$['+myValue+']'; }
		if(myField instanceof CKEDITOR.editor){
			myField.insertText(myValue);
			return;
		}
		//IE support
		if (document.selection) {
			myField.focus();
			sel = document.selection.createRange();
			sel.text = myValue;
		}
		//MOZILLA/NETSCAPE support
		else if (myField.selectionStart || myField.selectionStart == '0') {
			var startPos = myField.selectionStart;
			var endPos = myField.selectionEnd;
			myField.value = myField.value.substring(0, startPos)+ myValue
				+ myField.value.substring(endPos, myField.value.length);
		} else {
			myField.value += myValue;
		}
	}
	$(function(){
		$('#attachmentTh').hide();
		$('#addAttachment').click(function(){
			addAttachment('','',false,'');
		});
		<c:forEach var="att" items="${campaignStep3FormBean.attachments}">
		addAttachment('${att.fileName}','${att.filePath}','${att.compressed}','${att.archivePasswd}');
		</c:forEach>
		$('#editors').tabs();
		
		$('#msgVarTab').hide();
		$('#msgChunkTab').hide();
		$('#variableType').change(function(){
			switch($(this).val()){
			case '0':
				$('#dataVarTab').show(); 
				$('#msgVarTab').hide();
				$('#msgChunkTab').hide();
				break;
			case '1':
				$('#dataVarTab').hide(); 
				$('#msgVarTab').show();
				$('#msgChunkTab').hide();
				break;
			case '2':
				$('#dataVarTab').hide(); 
				$('#msgVarTab').hide();
				$('#msgChunkTab').show();
				break;
			}
		});
		
		$('#fromEmail').click(function(){currentTextElem=document.getElementById('fromEmail');});
		$('#fromName').click(function(){currentTextElem=document.getElementById('fromName');});
		$('#replyToEmail').click(function(){currentTextElem=document.getElementById('replyToEmail');});
		$('#mailSubject').click(function(){currentTextElem=document.getElementById('mailSubject');});
		$('#messageBodyText').click(function(){currentTextElem=document.getElementById('messageBodyText');});
		
		<c:if test="${not sessionScope['campaign.step1'].velocity}">
		//var element = CKEDITOR.document.getById( 'messageBodyHtml_' );
		var element = CKEDITOR.instances.messageBodyHtml_;
		element.on( 'focus', function(event) {
			currentTextElem = CKEDITOR.instances.messageBodyHtml_;
		});
		
		$('#step3_form').submit(function(){
			$('#messageBodyHtml').val(CKEDITOR.instances.messageBodyHtml_.getData());
		});
		</c:if>
		<c:if test="${sessionScope['campaign.step1'].velocity}">
		
		var velocityHtml = CodeMirror.fromTextArea(document.getElementById('messageBodyHtml_'), {
			tabMode: 'indent', matchBrackets: true, theme: 'night', lineNumbers: true,
			indentUnit: 4, mode: "text/velocity"
		});
		var velocityText = CodeMirror.fromTextArea(document.getElementById('messageBodyText_'), {
			tabMode: 'indent', matchBrackets: true, theme: 'night', lineNumbers: true,
			indentUnit: 4, mode: "text/velocity"
		});
		$('#step3_form').submit(function(){
			$('#messageBodyHtml').val(velocityHtml.getValue());
			$('#messageBodyText').val(velocityText.getValue());
		});
		</c:if>
		$('#step3_form').submit(function(){
			var text = $('#messageBodyText').val();
			if(text){
				text = text.replace(/^\s\s*/, '').replace(/\s\s*$/, '');
				if(text!==''){
					return true;
				}
			}
			if(!confirm('Text message is empty, Would you like the program to extract text from your HTML?')){
				return false;
			}
		});
		$('#spam_chk_btn').click(function(){
			var html, text;
			var subject = $('#mailSubject').val();
			<c:if test="${not sessionScope['campaign.step1'].velocity}">
			html = CKEDITOR.instances.messageBodyHtml_.getData();
			text = $('#messageBodyText').val();
			</c:if>
			<c:if test="${sessionScope['campaign.step1'].velocity}">
			html = velocityHtml.getValue();
			text = velocityText.getValue();
			</c:if>
			$('#spam_wait').show();
			$('#spam_score').text('');
			$.post('${PATH.emails}/check-spam', {subject:subject, html:html, text:text}, function(data){
				$('#spam_score').text(data.score);
				$('#spam_threshold').text('/'+data.requiredScore);
				if(data.spam){
					$('#spam_score').css('color','red');
					$('#spam_detail_div').show();
					$('#spam_detail').val(data.analysisDetails);
				}else{
					$('#spam_score').css('color','black');
					$('#spam_detail_div').hide();
				}
				$('#spam_wait').hide();
				if(data.error){
					jprompt.alert(data.errorDetail);
				}
			});
		});
		$('#back_btn').click(function(){
			window.location.href='${PATH.campaigns}/step2';
		});
		$('#hide_varbox').click(function(){
			$('#varbox').hide();
			$('#side_menu').css('visibility', 'visible');
		});
		$('#show_varbox').click(function(){
			$('#varbox').fadeIn();
			$('#side_menu').css('visibility', 'hidden');
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
				<a href="${PATH.campaigns }/step1">
					<s:message code="view.campaign.step1" text="Step1 - Select Message Type"/>
				</a>
				<div class="breadcrumb_divider"></div>
				<a href="${PATH.campaigns }/step2">
					<s:message code="view.campaign.step2" text="Step2 - Create Campaign"/>
				</a>
				<div class="breadcrumb_divider"></div>
				<a class="current">
					<s:message code="view.campaign.step3" text="Step3 - Compose Message"/>
				</a>
				<div class="breadcrumb_divider"></div>
				<a href="#">
					<s:message code="view.campaign.finish" text="Finish - Deliver Message"/>
				</a>
			</article>
		</div>
	</section>
	<div id="side_menu" style="visibility: hidden;">
	<%@include file="/WEB-INF/views/includes/body/sidebar.jsp" %>
	</div>
	<section id="main" class="column">
	<form:form id="step3_form" method="post" action="${PATH.campaigns }/step3" modelAttribute="campaignStep3FormBean">
	<%@include file="/WEB-INF/views/includes/body/form_error.jsp" %>
	
	<article class="module width_full">
		<header>
			<h3>
			<s:message code="view.campaign.step3" text="Step3 - Compose Message"/>
			&nbsp;
			<a href="javascript:void(0);" id="show_varbox" style="width:300px;" title="Show Variables Box">
				<img src="${ctx }/resources/images/icons/page_white_code.png" style="vertical-align: middle;"/>
			</a>
			</h3>
		</header>
		<div class="module_content">
			<fieldset style="width:48%; float:left; margin-right: 3%;">
				<label class="field">
					<s:message code="entity.campaign.fromName" text="From Name"/>
					<form:errors path="fromName" cssClass="error" />
				</label>
				<form:input path="fromName" style="width:90%"/>
				<label class="field" style="margin-top:5px;">
					<s:message code="entity.campaign.fromEmail" text="From Email"/>
					<span class="req">*</span><form:errors path="fromEmail" cssClass="error" />
				</label>
				<form:input path="fromEmail" style="width:90%"/>
			</fieldset>
			<fieldset style="width:48%; float:left;">
				<label class="field">
					<s:message code="entity.campaign.replyToEmail" text="Reply-To Email"/>
					<form:errors path="replyToEmail" cssClass="error" />
				</label>
				<form:input path="replyToEmail" style="width:90%"/>
			</fieldset>
			<div class="clear"></div>
			<fieldset style="width:48%; float:left; margin-right: 3%;">
				<label class="field">
					<s:message code="entity.campaign.mailPriority" text="Priority"/>
				</label>
				<form:select path="mailPriority">
			 		<form:option value="">
			 			<s:message code="view.text.none" text="None"/>
			 		</form:option>
			 		<form:option value="1">
			 			<s:message code="view.text.highest" text="Highest"/>
			 		</form:option>
			 		<form:option value="2">
			 			<s:message code="view.text.high" text="High"/>
			 		</form:option>
			 		<form:option value="3">
			 			<s:message code="view.text.normal" text="Normal"/>
			 		</form:option>
			 		<form:option value="4">
			 			<s:message code="view.text.low" text="Low"/>
			 		</form:option>
			 		<form:option value="5">
			 			<s:message code="view.text.lowest" text="Lowest"/>
			 		</form:option>
			 	</form:select>
			</fieldset>
			<fieldset style="width:48%; float:left;">
				<label class="field">
					<s:message code="entity.campaign.messageCharset" text="Charset"/>
				</label>
				<form:select path="messageCharset">
			 		<form:options items="${Charsets }"/>
			 	</form:select>
			</fieldset>
			<div class="clear"></div>
			<fieldset>
				<label class="field">
					<s:message code="entity.campaign.mailSubject" text="Subject"/>
					<span class="req">*</span>
				</label>
				<form:input path="mailSubject" />
				<form:errors path="mailSubject" cssClass="error" />
			</fieldset>
			<fieldset>
				<label class="field">
					<s:message code="view.campaign.step3.messageBody" text="Message Body"/>
					<span class="req">*</span>
				</label>
				<div id="editors" class="value">
					<ul>
						<c:if test="${messageType ne 'TEXT'}">
							<li><a href="#htmlTab"><s:message code="view.text.html" text="HTML"/></a></li>
						</c:if>
						<c:if test="${messageType ne 'HTML'}">
							<li><a href="#textTab"><s:message code="view.text.text" text="TEXT"/></a></li>
						</c:if>
						<li><a href="#spamChkTab"><s:message code="view.campaign.spamCheck" text="Spam Check"/></a></li>
					</ul>
					<c:if test="${messageType ne 'TEXT'}">
					<div id="htmlTab">
						<form:hidden path="messageBodyHtml" />
						<c:if test="${not sessionScope['campaign.step1'].velocity}">
							<ckeditor:resources minified="true"/>
							<ckeditor:config height="300px" width="100%" />	
							<ckeditor:editor id="messageBodyHtml_" name="messageBodyHtml_" height="400px" width="100%" fullPage="true">
								${campaignStep3FormBean.messageBodyHtml }
							</ckeditor:editor>
						</c:if>
						<c:if test="${sessionScope['campaign.step1'].velocity}">
							<textarea id="messageBodyHtml_" cssStyle="width:100%;height:400px">${esc:escapeHtml(campaignStep3FormBean.messageBodyHtml) }</textarea>
						</c:if>
						<form:errors path="messageBodyHtml" cssClass="error"/>
					</div>
					</c:if>
					<c:if test="${messageType ne 'HTML'}">
					<div id="textTab">	
						<c:if test="${not sessionScope['campaign.step1'].velocity}">
							<form:textarea path="messageBodyText" cssStyle="width:100%;height:400px"/>
						</c:if>
						<c:if test="${sessionScope['campaign.step1'].velocity}">
							<form:hidden path="messageBodyText" />
							<textarea id="messageBodyText_" cssStyle="width:100%;height:400px">${esc:escapeHtml(campaignStep3FormBean.messageBodyText) }</textarea>
						</c:if>
						<form:errors path="messageBodyText" cssClass="error"/>
					</div>
					</c:if>
					<div id="spamChkTab">
						<h2>Spam Check</h2>
						<p>
						You can quickly determine your message's quality and deliverability with 
						Spamassassin which is the powerful number one open-source spam filter. 
						The result score should be positive value and not over 6.3 (Threshold); 
						otherwise your message will be moved to the recipient's spam box.
						<strong>Remember that lower spam score is better. </strong>
						</p>
						<div style="width:400px;margin:0px auto;">
							<table style="width:400px;">
								<tr>
									<td style="text-align: center;">
										<img src="${ctx }/resources/images/spamassassin.png"/>
										<br/>
										<span>Powered by <a href="http://spamassassin.apache.org/" target="_blank">Spamassassin</a></span>
									</td>
								</tr>
								<tr>
									<td style="text-align: center;padding: 10px;">
										<div style="font-size: 2em">
											<s:message code="view.campaign.spamScore" text="Score"/> &nbsp;
											<span id="spam_score">???</span><span id="spam_threshold"></span>
											<img id="spam_wait" style="display:none;"src="${ctx }/resources/images/ajax/circle16.gif"/>
										</div>
									</td>
								</tr>
								<tr>
									<td style="text-align: center;padding: 10px;">
										<div id="spam_detail_div" style="display:none;">
										<img src="${ctx }/resources/images/icons/error.png"/>
										<s:message code="view.campaign.spamDetected" text="Spam Detected"/>
										<textarea id="spam_detail" style="width:100%;height:80px;" readonly="readonly"></textarea>
										</div>
									</td>
								</tr>
								<tr>
									<td style="text-align: center;"><input type="button" id="spam_chk_btn" value="Check Now" style="width:200px"/></td>
								</tr>
							</table>
						</div>
					</div>
				</div>
			</fieldset>
			<%--
			<fieldset>
				<div class="value">
					<form:checkbox id="inlineResource" path="inlineResource"/>
					<label for="inlineResource">
						<s:message code="entity.campaign.inlineResource" text="Embed Images"/>
					</label>
				</div>
			</fieldset>
			 --%>
			<fieldset>
				<label class="field">
					<s:message code="entity.campaign.attachments" text="Attachments"/>
				</label>
				<div class="value">
					<input id="addAttachment" type="button" value="Add attachment"/> 
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
					</table>
				</div>
			</fieldset>
		</div>
		<footer>
			<div class="back_link">
				<input type="button" id="back_btn" value="Back"/>
			</div>
			<div class="submit_link">
				<form:select path="status">
				 	<form:option value="">
				 		<s:message code="entity.campaign.status.published" text="Published"/>
				 	</form:option>
				 	<form:option value="DRAFT">
				 		<s:message code="entity.campaign.status.draft" text="Draft"/>
				 	</form:option>
				</form:select>
				<input type="submit" value="Save" />
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
	
	<div id="varbox" style="position:absolute;width:18%;min-height: 450px;
    	left:0px;top:110px;border-width:1px;border-style: solid;border-color: gray;">
    <div style="float:right;margin-top:2px;margin-right:5px;">
    	<a href="javascript:void(0);" id="hide_varbox" title="Hide">
    		<img src="${ctx }/resources/images/icons/cross.png"/>
    	</a>
    </div>
    <div style="padding:16px;">
	    <select id="variableType">
	    	<option value="0" selected="selected">Data Variables</option>
	    	<option value="1">Template Variables</option>
	    	<option value="2">Template Chunks</option>
	    </select>
	    <p style="margin:0px;">
	    	Click at desired variable name to insert it into editing area.
	    </p>
		<div id="dataVarTab">
			<ul style="height:320px; overflow:auto;">
				<c:forEach var="var" items="${dataVars }">
				<li><a href="javascript:void(0);" onclick="insertAtCursor('${var}',0);">${var}</a></li>
				</c:forEach>
			</ul>
		</div>
		<div id="msgVarTab">
			<ul style="height:320px; overflow:auto;">
				<c:forEach var="var" items="${msgVars }">
				<li><a href="javascript:void(0);" onclick="insertAtCursor('${var}',1);">${var}</a></li>
				</c:forEach>
			</ul>
		</div>
		<div id="msgChunkTab">
			<ul style="height:320px; overflow:auto;">
				<c:forEach var="var" items="${msgChunks }">
				<li><a href="javascript:void(0);" onclick="insertAtCursor('${var}',2);">${var}</a></li>
				</c:forEach>
			</ul>
		</div>
	</div>
	
</div>

<script type="text/javascript">
/* Script by: www.jtricks.com
 * Version: 20071127
 * Latest version:
 * www.jtricks.com/javascript/navigation/fixed_menu.html
 */
fixedMenuId = 'varbox';

var fixedMenu = {
    hasInner: typeof(window.innerWidth) == 'number',
    hasElement: document.documentElement != null
       && document.documentElement.clientWidth,

    menu: document.getElementById
        ? document.getElementById(fixedMenuId)
        : document.all
          ? document.all[fixedMenuId]
          : document.layers[fixedMenuId]
};

fixedMenu.computeShifts = function(){
    fixedMenu.shiftX = fixedMenu.hasInner
        ? pageXOffset
        : fixedMenu.hasElement
          ? document.documentElement.scrollLeft
          : document.body.scrollLeft;
    if (fixedMenu.targetLeft > 0)
        fixedMenu.shiftX += fixedMenu.targetLeft;
    else{
        fixedMenu.shiftX += 
            (fixedMenu.hasElement
              ? document.documentElement.clientWidth
              : fixedMenu.hasInner
                ? window.innerWidth - 20
                : document.body.clientWidth)
            - fixedMenu.targetRight
            - fixedMenu.menu.offsetWidth;
    }

    fixedMenu.shiftY = fixedMenu.hasInner
        ? pageYOffset
        : fixedMenu.hasElement
          ? document.documentElement.scrollTop
          : document.body.scrollTop;
    if (fixedMenu.targetTop > 0)
        fixedMenu.shiftY += fixedMenu.targetTop;
    else{
        fixedMenu.shiftY += 
            (fixedMenu.hasElement
            ? document.documentElement.clientHeight
            : fixedMenu.hasInner
              ? window.innerHeight - 20
              : document.body.clientHeight)
            - fixedMenu.targetBottom
            - fixedMenu.menu.offsetHeight;
    }
};

fixedMenu.moveMenu = function(){
    fixedMenu.computeShifts();

    if (fixedMenu.currentX != fixedMenu.shiftX
        || fixedMenu.currentY != fixedMenu.shiftY){
        fixedMenu.currentX = fixedMenu.shiftX;
        fixedMenu.currentY = fixedMenu.shiftY;

        if (document.layers){
            fixedMenu.menu.left = fixedMenu.currentX;
            fixedMenu.menu.top = fixedMenu.currentY;
        }
        else{
            fixedMenu.menu.style.left = fixedMenu.currentX + 'px';
            fixedMenu.menu.style.top = fixedMenu.currentY + 'px';
        }
    }

    fixedMenu.menu.style.right = '';
    fixedMenu.menu.style.bottom = '';
};

fixedMenu.floatMenu = function(){
    fixedMenu.moveMenu();
    setTimeout('fixedMenu.floatMenu()', 20);
};

// addEvent designed by Aaron Moore
fixedMenu.addEvent = function(element, listener, handler){
    if(typeof element[listener] != 'function' || 
       typeof element[listener + '_num'] == 'undefined'){
        element[listener + '_num'] = 0;
        if (typeof element[listener] == 'function'){
            element[listener + 0] = element[listener];
            element[listener + '_num']++;
        }
        element[listener] = function(e){
            var r = true;
            e = (e) ? e : window.event;
            for(var i = 0; i < element[listener + '_num']; i++)
                if(element[listener + i](e) === false)
                    r = false;
            return r;
        };
    }

    //if handler is not already stored, assign it
    for(var i = 0; i < element[listener + '_num']; i++)
        if(element[listener + i] == handler)
            return;
    element[listener + element[listener + '_num']] = handler;
    element[listener + '_num']++;
};

fixedMenu.supportsFixed = function(){
    var testDiv = document.createElement("div");
    testDiv.id = "testingPositionFixed";
    testDiv.style.position = "fixed";
    testDiv.style.top = "0px";
    testDiv.style.right = "0px";
    document.body.appendChild(testDiv);
    var offset = 1;
    if (typeof testDiv.offsetTop == "number"
        && testDiv.offsetTop != null 
        && testDiv.offsetTop != "undefined"){
        offset = parseInt(testDiv.offsetTop);
    }
    if (offset == 0){
        return true;
    }
    return false;
};

fixedMenu.init = function(){
    if (fixedMenu.supportsFixed())
        fixedMenu.menu.style.position = "fixed";
    else{
        var ob = document.layers ? fixedMenu.menu : fixedMenu.menu.style;
        fixedMenu.targetLeft = parseInt(ob.left);
        fixedMenu.targetTop = parseInt(ob.top);
        fixedMenu.targetRight = parseInt(ob.right);
        fixedMenu.targetBottom = parseInt(ob.bottom);

        if (document.layers){
            menu.left = 0;
            menu.top = 0;
        }
        fixedMenu.addEvent(window, 'onscroll', fixedMenu.moveMenu);
        fixedMenu.floatMenu();
    }
};

fixedMenu.addEvent(window, 'onload', fixedMenu.init);
</script>
</body>
</html>