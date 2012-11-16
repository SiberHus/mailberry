/**
 * siberhus.js
 * version 1.0
 * @author Hussachai Puripunpinyo
 * Copyright 2010 SiberHus.com All Rights Reserved
 */
UI = {};
ChkBoxUI = {};
SelectUI= {};
TableUI = {};
Form = {};
Utils = {};
I18N = {};
I18N._lang = new Array();
I18N.lang = function(key){
	var val = I18N._lang[key];
	if(val)return val;
	else return key;
};
I18N._lang["_default.isRequired"] = "is required attribute.";
I18N._lang["_ui.hasAlreadyExisted"] = "has already existed";
I18N._lang["_ui.opNotSupport"] = "Error: Operation does not support!";

/*
 * This method will return null, if the child of frame page is refreshed.
 * It doesn't have any problems if the parent page is not a frame.
 */
UI.getParentElementById = function(elementId){
	if(window.opener){
		return window.opener.document.getElementById(elementId);
	}else{
		var xWin=window.dialogArguments;
		return xWin.document.getElementById(elementId);
	}
};

UI.setParentElementValue = function(parentElemId, modelValue, modelLabel) {
	var elem = UI.getParentElementById(parentElemId);
	if (elem.type == 'hidden') {
		//alert('hidden');
		var elemLabel = UI.getParentElementById(parentElemId + 'Label');
		if (elemLabel) {
			elemLabel.value = modelLabel;
		}
		elem.value = modelValue;
		// }else if(elem.type=='text'){
		// alert('text');
	} else if (elem.type == 'select-one' || elem.type == 'select-multiple') {
		//alert('select');
		elem.value = modelValue;
	}
	window.close();
};

UI.addParentElementValue = function(parentElemId, modelValue, modelLabel) {
	var elem = UI.getParentElementById(parentElemId);
	if (elem.type == 'select-one' || elem.type == 'select-multiple') {
		//alert('select');
		if (SelectUI.hasOption(elem, modelValue)) {
			alert(modelLabel + ' ' + I18N.lang('_ui.hasAlreadyExisted'));
			return;
		}
		SelectUI.addOption(elem, modelValue, modelLabel);
	} else {
		alert(I18N.lang('_ui.opNotSupport'));
	}
};

UI.createElement = function(elemeType, attribs){
	var elem = document.createElement(elemeType);
	for(var key in attribs){
		elem.setAttribute(key, attribs[key]);
	}
	return elem;
};

UI.findElement = function(elementOrId){
	var element;
	if (typeof elementOrId == "string") {
		element = document.getElementById(elementOrId);
		return element;
	}else{
		element = elementOrId;
		if(element!=null){
			return element;
		}
		return UI.getParentElementById(elementOrId);
	}
};

UI.getElementsByClass = function(searchClass,node,tag) {
	
	var classElements = new Array();
	if ( node == null )
		node = document;
	if ( tag == null )
		tag = '*';
	var els = node.getElementsByTagName(tag);
	var elsLen = els.length;
	var pattern = new RegExp("(^|\\s)"+searchClass+"(\\s|$)");
	for (i = 0, j = 0; i < elsLen; i++) {
		if ( pattern.test(els[i].className) ) {
			classElements[j] = els[i];
			j++;
		}
	}
	return classElements;
};

UI.findPos = function(obj) {
	var curleft = curtop = 0;
	if (obj.offsetParent) {
		curleft = obj.offsetLeft;
		curtop = obj.offsetTop;
		while (obj = obj.offsetParent) {
			curleft += obj.offsetLeft;
			curtop += obj.offsetTop;
		}
	}
	return [curleft,curtop];
};

UI.getScreenSize = function(){
	var screenW = 640, screenH = 480;
	if (parseInt(navigator.appVersion)>3) {
		screenW = screen.width;
		screenH = screen.height;
	}
	else if (navigator.appName == "Netscape" 
		&& parseInt(navigator.appVersion)==3
		&& navigator.javaEnabled()) {
		var jToolkit = java.awt.Toolkit.getDefaultToolkit();
		var jScreenSize = jToolkit.getScreenSize();
		screenW = jScreenSize.width;
		screenH = jScreenSize.height;
	}
	return [screenW,screenH];
};

/**
 * Example:
 * <img class="lookupItem" onclick="UI.showDialog(this,{target:'${ctx}/action/merchant?layout=minimum&elemId=merchant&modelName=name&multiSel=true'});"/>
 */
UI.showDialog = function(callerElem, attribs){
	//var position = UI.findPos(callerElem);
	var screenSize = UI.getScreenSize();
	var width = screenSize[0]/2+'px';
	var height = screenSize[1]/2+'px';
	var top,left;
	var scrollable = 1;
	var resizable = 1;
	var statusbar = 1;
	var target = attribs.target;
	var modal = attribs.modal;
	var position = 'center';//center,relative,absolute
	if(modal==null){
		modal = false;
		if(BrowserDetect){
			var browser = BrowserDetect.browser;
			//Chrome,Firefox,Safari,Explorer,Opera
			if(browser=='Firefox'
				|| browser=='Safari'
				|| browser=='Chrome'){
				modal = true;
			}
		}
	}
	if(!target){
		alert('target '+ I18N.lang('_default.isRequired'));
		return;
	}
	if(attribs.height!=null){
		height = attribs.height;
	}
	if(attribs.width!=null){
		width = attribs.width;
	}
	if(attribs.position!=null){
		position = attribs.position;
	}
	if(position=='center'){
		left = screenSize[0]/4+'px';
		top = screenSize[1]/4+'px';
	}else if(position=='relative'){
		left = position[0];
		top = position[1];
	}else{
		//absolute
		left = attribs.left;
		top = attribs.top;
	}
	
	if(attribs.scrollable!=null){
		scrollable = attribs.scrollable;
	}
	if(attribs.resizable!=null){
		resizable = attribs.resizable;
	}
	if(attribs.statusbar!=null){
		statusbar = attribs.statusbar;
	}
	if(modal){
		var params = 'dialogLeft:'+left+';'
			+ 'dialogTop:'+top+';'
			+ 'dialogHeight:'+height+';'
			+ 'dialogWidth:'+width+';'
			+ 'resizable:'+resizable+';'
			+ 'scroll:'+scrollable+';'
			+ 'status:'+statusbar;
		//alert(params);
		window.showModalDialog(target, window,params);
	}else{
		var fullscreen = 0;
		var addressbar = 0;
		var menubar = 0;
		var titlebar = 0;
		var toolbar = 0;
		var params = 'left='+left+','
			+ 'top='+top+','
			+ 'height='+height+','
			+ 'width='+width+','
			+ 'resizable='+resizable+','
			+ 'scroll='+scrollable+','
			+ 'status='+statusbar+','
			+ 'fullscreen='+fullscreen+','
			+ 'location='+addressbar+','
			+ 'menubar='+menubar+','
			+ 'titlebar='+titlebar+','
			+ 'toolbar='+toolbar;

		//alert(params);
		window.open(target,'pop',params);
	}
};

ChkBoxUI.toggleCheckedItems = function (chkElems, toggleElem) {
	var status = false;
	if (toggleElem.checked) {
		status = true;
	}
	if(chkElems.length>1){
		for (var i = 0; i < chkElems.length; i++) {
			chkElems[i].checked = status;
		}
	}else{
		if(chkElems.checked){
			chkElems.checked = status;
		}else{
			chkElems.attr('checked', status);//!!! JQuery method
		}
	}
};

ChkBoxUI.equalsCheckedItemsCount = function (chkElems, expectedNumber){
	var counter = 0;
	var i;
	if(chkElems.length>1){
		for (i = 0; i < chkElems.length; i++) {
			if(chkElems[i].checked){
				counter = counter+1;
			}
		}
	}else{
		if(chkElems.checked){
			counter = counter+1;
		}
	}
	if(counter==expectedNumber){
		return true;
	}
	return false;
};

SelectUI.hasOption = function(selectElemOrId, optionValue){
	var selectElem = UI.findElement(selectElemOrId);
	var i;
	for (i = selectElem.length - 1; i >= 0; i--) {
		if (selectElem.options[i].value==optionValue) {
			return true;
		}
	}
	return false;
};

SelectUI.addOption = function(selectElemOrId, optionValue, optionLabel){
	var selectElem = UI.findElement(selectElemOrId);
	//selectElem.options[selectElem.options.length] = new Option(optionLabel, optionValue); 
	var option ;
	if(window.opener){
		option = window.opener.document.createElement('option');
	}else if(window.dialogArguments){
		option = window.dialogArguments.document.createElement("option");
	}else{
		option = window.document.createElement('option');
	}
	option.text =  optionLabel;
	option.value =  optionValue;
	try {
		selectElem.add(option, null); // standards compliant; doesn't work in IE
	} catch (ex) {
		selectElem.add(option); // IE only
	}
};

SelectUI.clear = function(selectElemOrId){
	var selectElem = UI.findElement(selectElemOrId);
	selectElem.options.length = 0;
};

SelectUI.removeSelected = function(selectElemOrId){
	var selectElem = UI.findElement(selectElemOrId);
	var i;
	for (i = selectElem.length - 1; i >= 0; i--) {
		if (selectElem.options[i].selected) {
			selectElem.remove(i);
		}
	}
};
SelectUI.removeAll = function(selectElemOrId){
	var selectElem = UI.findElement(selectElemOrId);
	var i;
	for (i = selectElem.length - 1; i >= 0; i--) {
		selectElem.remove(i);
	}
};
SelectUI.selectAll = function(selectElemOrId){
	var selectElem = UI.findElement(selectElemOrId);
	if (selectElem.type == 'select-multiple') {
		for (var i = 0; i < selectElem.options.length; i++) {
			selectElem.options[i].selected = true;
		}
	}
};

var rowNum = 0;
TableUI.addRow = function (tableElemOrId, cellElems, callbackFn){
	var tableElem =  UI.findElement(tableElemOrId);
	var tbody = tableElem.getElementsByTagName('tbody');
	if(tbody){
		tableElem = tbody[0];
	}
	var row = tableElem.insertRow(tableElem.rows.length);
	var cellIdx = 0;
	for(var i in cellElems){
		var cell = row.insertCell(cellIdx);
		var elem = cellElems[i];
		if(typeof elem=='string' || elem instanceof String){
			cell.innerHTML = elem;
		}else{
			cell.appendChild(elem);
		}
		cellIdx++;
	}
	if(callbackFn)callbackFn();
	rowNum++;
};
TableUI.deleteRow = function (elemInCell){
	var cellElem = elemInCell.parentNode;
	var rowElem = cellElem.parentNode;
	var tableElem = rowElem.parentNode;
	if(!(tableElem instanceof HTMLTableElement)){
		tableElem = tableElem.parentNode;
	}
	tableElem.deleteRow(rowElem.rowIndex);
};

Form.setReadonly = function(targetForm) {
	for (var i = 0; i < targetForm.length; i++) {
		var element = targetForm.elements[i];
		element.setAttribute("className", "readonly");
		element.setAttribute("class", "readonly");
		element.setAttribute("style", "border: none");
		element.setAttribute("readonly", "readonly");
		// element.setAttribute("disabled","disabled");
	}
};

/*
Form.createField(form, 'input',{
	'type':'hidden','name':'fieldName','value':$('#dirNode').val()
});
 */
Form.createField = function(targetForm, fieldType, attribs){
	var field = document.createElement(fieldType);
	for(var key in attribs){
		field.setAttribute(key, attribs[key]);
	}
	targetForm.appendChild(field);
};

Form.setFocus = function setFocus(id) {
    var field = document.getElementById(id);
    if (field && field.focus && field.type != "hidden" && field.disabled != true) {
    	try {
			field.focus();
		} catch (err) {
		}
    }
};

Form.clear = function(targetForm) {
	var object = new Array();
	object[0] = targetForm.getElementsByTagName('input');
	object[1] = targetForm.getElementsByTagName('textarea');
	object[2] = targetForm.getElementsByTagName('select');
	var type = null;
	for ( var x = 0; x < object.length; x++) {
		for ( var y = 0; y < object[x].length; y++) {
			type = object[x][y].type;
			switch (type) {
			case "hidden":
			case "text":
			case "textarea":
			case "password":
				object[x][y].value = "";
				break;
			case "radio":
			case "checkbox":
				object[x][y].checked = "";
				break;
			case "select-one":
				object[x][y].options[0].selected = true;
				break;
			case "select-multiple":
				for (var z = 0; z < object[x][y].options.length; z++) {
					object[x][y].options[z].selected = false;
				}
				break;
			}
		}
	}
};

Utils.trim = function trim(str) {  
    while (str.charAt(0) == (" ")) {  
        str = str.substring(1);
      }
      while (str.charAt(str.length - 1) == " ") {  
          str = str.substring(0,str.length-1);
      }
      return str;
};