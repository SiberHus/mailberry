<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="res" uri="http://www.siberhus.com/web/tags/resources"%>
<!doctype html>
<html lang="en">
<head>
	
	<%@include file="/WEB-INF/views/includes/head/res_standard.jsp" %>
	<res:style src="/resources/comps/dashboard/dashboard.css" minify="true"/>
	<res:style src="/resources/css/horizontal_bar_chart.css" minify="true"/>
	<res:script src="/resources/js/_gen.dashboard.js" merge="true" minify="true">
		<res:script src="/resources/js/jquery.cookie.js"/>
		<res:script src="/resources/comps/dashboard/dashboard.js"/>
	</res:script>
	<script type="text/javascript">
	<c:forEach var="widget" items="${userWidgets }">
		function widget${widget.code}(){
			$('#content_${widget.code}').html('');
			$('#loading_${widget.code}').show();
			$.get('${ctx}${widget.contentUri}', function(data) {
				$('#loading_${widget.code}').hide();
				$('#content_${widget.code}').html(data);
			});
		}
		widget${widget.code}();
	</c:forEach>
    </script>
</head>
<body>
	<c:set var="pageTitle" value="Dashboard"/>
	<%@include file="/WEB-INF/views/includes/body/header.jsp" %>
	<section id="secondary_bar">
		<%@include file="/WEB-INF/views/includes/body/user.jsp" %>
		<div class="breadcrumbs_container">
			<h3 style="margin-left: 20px;">Welcome back <i>${_USER.firstName }&nbsp;${_USER.lastName }</i></h3> 
		</div>
	</section>
	<%@include file="/WEB-INF/views/includes/body/sidebar.jsp" %>
	
	<section id="main" class="column">
	<article class="module width_full">
		<header>
			<h3>
				<s:message code="lbl.dashboard" text="Dashboard"/>
			</h3>
		</header>
		<div class="module_content" style="padding-bottom: 5px;">
			<div id="dashboard">
				<div id="topsection" class="sortable">
					<c:forEach var="widget" items="${userWidgets}">
						<c:if test="${widget.defaultPosition eq 'Center'}">
							<%@include file="_widget.jsp" %> 
						</c:if>
					</c:forEach>
				</div>
				<div id="leftsection" class="sortable">
					<c:forEach var="widget" items="${userWidgets}">
						<c:if test="${widget.defaultPosition eq 'Left'}">
							<%@include file="_widget.jsp" %> 
						</c:if>
					</c:forEach>
				</div>
				<div id="rightsection" class="sortable">
					<c:forEach var="widget" items="${userWidgets}">
						<c:if test="${widget.defaultPosition eq 'Right'}">
							<%@include file="_widget.jsp" %> 
						</c:if>
					</c:forEach>
				</div>
			</div>
			
			<br style="clear: both" />
		</div>
		<footer>
			<div class="submit_link">
				<g:if test="${not empty userWidgets}">
				<a href="javascript:void(0);" id="edit_dashboard">
					<img src="${ctx }/resources/images/icons/pencil.png"/>
					<span>Edit</span>
				</a>
				<a href="javascript:void(0);" id="save_dashboard">
					<img src="${ctx }/resources/images/icons/disk.png"/>
					<span>Save</span>
				</a>
				</g:if>
			</div>
		</footer>
	</article>
	<div class="spacer"></div>
	<%if("demo".equals(System.getProperty("mailberry.mode"))){ %>
		<strong style="margin-left:20px;">The below ads appear in demo version only</strong>
		<div align="center">
			<script type="text/javascript"><!--
				google_ad_client = "ca-pub-5090980166916130";
				/* app-demo-ads */
				google_ad_slot = "2402989570";
				google_ad_width = 728;
				google_ad_height = 90;
				//-->
			</script>
			<script type="text/javascript"
				src="http://pagead2.googlesyndication.com/pagead/show_ads.js">
			</script>
		</div>
	<%} %>
	</section>
	
</body>

</html>