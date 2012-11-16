<%@taglib prefix="res" uri="http://www.siberhus.com/web/tags/resources"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>MailBerry - The powerful email campaign manager on the web</title>
<link rel="shortcut icon" href="${ctx }/resources/favicon.ico" type="image/x-icon" />
<res:style src="/resources/css/layout.css" minify="true"/>
<!--[if lt IE 9]>
<link rel="stylesheet" href="${ctx }/resources/css/ie.css" type="text/css" media="screen" />
<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->
<!--[if IE]>
<style>
	fieldset select{margin-left:10px;}
	td input[type=text]{margin-left:10px;}
</style>
<![endif]-->
<res:style src="/resources/themes/smoothness/jquery-ui.full.css" minify="true"/>
<res:style src="/resources/css/jquery.jgrowl.css" minify="true"/>
<res:style src="/resources/css/tipsy.css" minify="true"/>
<res:style src="/resources/css/jquery.impromptu.css" minify="true"/>
<!-- 
<script type="text/javascript" src="${ctx }/resources/js/jquery-ui/i18n/jquery-ui-i18n.js"></script>
 -->
<res:script baseDir="/resources/js" src="_gen.standard.js" merge="true" minify="true">
	<res:script src="jquery.min.js"/>
	<res:script src="hideshow.js"/>
	<res:script src="jquery.equalHeight.js"/>
	<res:script src="jquery-ui.full.js"/>
	<res:script src="jquery.jgrowl.min.js"/>
	<res:script src="jquery.tipsy.js"/>
	<res:script src="jquery.impromptu.js"/>
	<res:script src="jquery.blockUI.js"/>
</res:script>
<script type="text/javascript">
var jprompt = [];
jprompt.alert = function(message){
	$.prompt(message,{ buttons: { OK: 'OK'}});
};
jprompt.confirm = function(message, fnc){
	$.prompt(message,{
		callback: function(v,m,f){
			if(v=='1') fnc();
		},
		buttons: {OK:'1', Cancel:'0'}
	});
};
jprompt.demo = function(){
	jprompt.alert('<s:message code="alert.demo" text="Demo user cannot do this operation"/>');
};
var crud = [];//grid functions
crud.baseUri = '';
crud.goToParentPage = function(){
	window.location.href=crud.baseUri+'/';
};
crud.createObject = function(){
	window.location.href=crud.baseUri+'/create';
};
crud.editObject = function(id){
	window.location.href=crud.baseUri+'/edit/'+id;
};
crud.viewObject = function(id){
	window.location.href=crud.baseUri+'/view/'+id;
};
crud.deleteObject = function(id, fn){
	jprompt.confirm('<s:message code="alert.delete.confirm" text="Are you sure to delete this item?"/>',
		function(){
			$.post(crud.baseUri+'/delete/'+id, function(html) {
				if(fn){
					fn();
				}else{
					window.location.href=crud.baseUri+'/';
				}
			});
		});
};

$(document).ready(function() {
	jQuery.fn.exists = function(){
		return jQuery(this).length>0;
	};
	//$('.column').equalHeight();
	$('input:submit, input:button, button').button();
	$('input.date').datepicker({
		dateFormat: '${FMT.jsDate}',
		changeMonth: true, changeYear: true,
		showOn: "button",
		buttonImage: "${ctx}/resources/images/icons/date.png",
		buttonImageOnly: true
	});
	//When page loads...
	$(".tab_content").hide(); //Hide all content
	$("ul.tabs li:first").addClass("active").show(); //Activate first tab
	$(".tab_content:first").show(); //Show first tab content
	
	//On Click Event
	$("ul.tabs li").click(function() {
		$("ul.tabs li").removeClass("active"); //Remove any "active" class
		$(this).addClass("active"); //Add "active" class to selected tab
		$(".tab_content").hide(); //Hide all tab content

		var activeTab = $(this).find("a").attr("href"); //Find the href attribute value to identify the active tab + content
		$(activeTab).fadeIn(); //Fade in the active ID content
		return false;
	});
});
</script>
<%if("demo".equals(System.getProperty("mailberry.mode"))){ %>
<script type="text/javascript">
  var _gaq = _gaq || [];
  _gaq.push(['_setAccount', 'UA-23130985-1']);
  _gaq.push(['_trackPageview']);
  (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  })();
</script>
<%} %>