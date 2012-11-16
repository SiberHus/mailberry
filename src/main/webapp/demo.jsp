<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<!doctype html>
<html lang="en">

<head>
	<meta charset="utf-8"/>
	<title>Dashboard I Admin Panel</title>
	
	<link rel="stylesheet" href="${ctx }/resources/css/layout.css" type="text/css" media="screen" />
	<!--[if lt IE 9]>
	<link rel="stylesheet" href="${ctx }/resources/css/ie.css" type="text/css" media="screen" />
	<script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
	<![endif]-->
	<script type="text/javascript" src="${ctx }/resources/js/jquery.min.js"></script>
	<script type="text/javascript" src="${ctx }/resources/js/hideshow.js"></script>
	<script type="text/javascript" src="${ctx }/resources/js/jquery.tablesorter.min.js"></script>
	<script type="text/javascript" src="${ctx }/resources/js/jquery.equalHeight.js"></script>
	
	<link rel="stylesheet" href="${ctx }/resources/themes/smoothness/jquery-ui.full.css" type="text/css" media="screen" />
	<script type="text/javascript" src="${ctx }/resources/js/jquery-ui/i18n/jquery-ui-i18n.js"></script>
	<script type="text/javascript" src="${ctx }/resources/js/jquery-ui.full.js"></script>
	
	<link rel="stylesheet" media="screen,projection" type="text/css" href="${ctx }/resources/css/ui.jqgrid.css" />
	<script type="text/javascript" src="${ctx }/resources/jqgrid/i18n/grid.locale-en.js"></script>
	<script type="text/javascript" src="${ctx }/resources/js/jqgrid/jquery.jqGrid.min.js"></script>
	<script type="text/javascript" src="${ctx }/resources/js/jqgrid/jquery.jqGrid.fluid.js"></script>
	
	<link rel="stylesheet" href="${ctx }/resources/comps/codemirror/codemirror.css" type="text/css" media="screen" />
	<link rel="stylesheet" href="${ctx }/resources/comps/codemirror/theme/night.css" type="text/css" media="screen" />
	<script type="text/javascript" src="${ctx }/resources/comps/codemirror/codemirror.js"></script>
	<script type="text/javascript" src="${ctx }/resources/comps/codemirror/mode/velocity/velocity.js"></script>
	
	<link rel="stylesheet" media="screen,projection" type="text/css" href="${ctx }/resources/css/jquery.jgrowl.css" />
	<script type="text/javascript" src="${ctx }/resources/js/jquery.jgrow.min.js"></script>
	
	<script type="text/javascript">
	$(document).ready(function() { 
		$('.column').equalHeight();
		$('input:button, button').button();
		$('#editors').tabs();
		var editor = CodeMirror.fromTextArea(document.getElementById("code"), {
			tabMode: "indent", matchBrackets: true, theme: "night", lineNumbers: true,
			indentUnit: 4, mode: "text/velocity"
		});
		$(".tablesorter").tablesorter(); 

   	 });
	$(document).ready(function() {

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

	$('ul.sortable').sortable();
});
    </script>
</head>


<body>

	<header id="header">
		<hgroup>
			<h1 class="site_title"><a href="index.html">Mail Berry</a></h1>
			<h2 class="section_title">Dashboard</h2><div class="btn_view_site"><a href="http://www.medialoot.com">View Site</a></div>
		</hgroup>
	</header> <!-- end of header bar -->
	
	<section id="secondary_bar">
		<div class="user">
			<p>John Doe (<a href="#">Logout</a>)</p>
			<!-- <a class="logout_user" href="#" title="Logout">Logout</a> -->
		</div>
		<div class="breadcrumbs_container">
			<article class="breadcrumbs">
				<a href="#">Step1 - Select Message Type</a>
				<div class="breadcrumb_divider"></div>
				<a href="#">Step2 - Campaign Info</a>
				<div class="breadcrumb_divider"></div>
				<a href="#">Step3 - Compose Message</a>
				<div class="breadcrumb_divider"></div>
				<a class="current">Finish - Delivery Message</a>
			</article>
		</div>
	</section><!-- end of secondary bar -->
	
	<aside id="sidebar" class="column">
		<br/>

		<h3>MESSAGES</h3>
		<ul class="toggle">
			<li class="icn_new_article"><a href="#">Campaigns</a></li>
			<li class="icn_edit_article"><a href="#">Email Schedule</a></li>
			<li class="icn_categories"><a href="#">Message Templates</a></li>
			<li class="icn_tags"><a href="#">Template Variables</a></li>
			<li class="icn_tags"><a href="#">Template Chunks</a></li>
		</ul>
		<h3>LISTS</h3>
		<ul class="toggle">
			<li class="icn_view_users"><a href="#">Subscriber Lists</a></li>
			<li class="icn_view_users"><a href="#">Global Block List</a></li>
			<li class="icn_view_users"><a href="#">Local Block List</a></li>
			<li class="icn_add_user"><a href="#">Import</a></li>
			<li class="icn_profile"><a href="#">Export</a></li>
		</ul>
		<h3>ANALYTICS</h3>
		<ul class="toggle">
			<li class="icn_jump_back"><a href="#">Clickstream</a></li>
			<li class="icn_jump_back"><a href="#">Email Tracking</a></li>
			<li class="icn_jump_back"><a href="#">Campaign Statistic</a></li>
		</ul>
		<h3>TOOLS</h3>
		<ul class="toggle">
			<li class="icn_edit_article"><a href="#">Subscriber Form Builder</a></li>
			<li class="icn_photo"><a href="#">Image Gallery</a></li>
			<li class="icn_folder"><a href="#">File Manager</a></li>
		</ul>
		<h3>SETTINGS</h3>
		<ul class="toggle">
			<li class="icn_settings"><a href="#">SMTP Profiles</a></li>
			<li class="icn_security"><a href="#">Manage Users</a></li>
			<li class="icn_settings"><a href="#">Configurations</a></li>
		</ul>
		<footer>
			<hr />
			<p><strong>Copyright &copy; 2011 Website Admin</strong></p>
			<p>Theme by <a href="http://www.medialoot.com">MediaLoot</a></p>
		</footer>
	</aside><!-- end of sidebar -->
	
	<section id="main" class="column">
		
		<h4 class="alert_info">Welcome to the free MediaLoot admin panel template, this could be an informative message.</h4>
		
		<article class="module width_full">
			<header><h3>Stats</h3></header>
			<div class="module_content">
				<article class="stats_graph">
					<img src="http://chart.apis.google.com/chart?chxr=0,0,3000&chxt=y&chs=520x140&cht=lc&chco=76A4FB,80C65A&chd=s:Tdjpsvyvttmiihgmnrst,OTbdcfhhggcTUTTUadfk&chls=2|2&chma=40,20,20,30" width="520" height="140" alt="" />
				</article>
				
				<article class="stats_overview">
					<div class="overview_today">
						<p class="overview_day">Today</p>
						<p class="overview_count">1,876</p>
						<p class="overview_type">Hits</p>
						<p class="overview_count">2,103</p>
						<p class="overview_type">Views</p>
					</div>
					<div class="overview_previous">
						<p class="overview_day">Yesterday</p>
						<p class="overview_count">1,646</p>
						<p class="overview_type">Hits</p>
						<p class="overview_count">2,054</p>
						<p class="overview_type">Views</p>
					</div>
				</article>
				<div class="clear"></div>
			</div>
		</article><!-- end of stats article -->
		
		<article class="module width_3_quarter">
		<header><h3 class="tabs_involved">Content Manager</h3>
		<ul class="tabs">
   			<li><a href="#tab1">Posts</a></li>
    		<li><a href="#tab2">Comments</a></li>
		</ul>
		</header>

		<div class="tab_container">
			<div id="tab1" class="tab_content">
			<table class="tablesorter" cellspacing="0"> 
			<thead> 
				<tr> 
   					<th></th> 
    				<th>Entry Name</th> 
    				<th>Category</th> 
    				<th>Created On</th> 
    				<th>Actions</th> 
				</tr> 
			</thead> 
			<tbody> 
				<tr> 
   					<td><input type="checkbox"></td> 
    				<td>Lorem Ipsum Dolor Sit Amet</td> 
    				<td>Articles</td> 
    				<td>5th April 2011</td> 
    				<td><input type="image" src="resources/images/icn_edit.png" title="Edit"><input type="image" src="resources/images/icn_trash.png" title="Trash"></td> 
				</tr> 
				<tr> 
   					<td><input type="checkbox"></td> 
    				<td>Ipsum Lorem Dolor Sit Amet</td> 
    				<td>Freebies</td> 
    				<td>6th April 2011</td> 
   				 	<td><input type="image" src="resources/images/icn_edit.png" title="Edit"><input type="image" src="resources/images/icn_trash.png" title="Trash"></td> 
				</tr>
				<tr> 
   					<td><input type="checkbox"></td> 
    				<td>Sit Amet Dolor Ipsum</td> 
    				<td>Tutorials</td> 
    				<td>10th April 2011</td> 
    				<td><input type="image" src="resources/images/icn_edit.png" title="Edit"><input type="image" src="resources/images/icn_trash.png" title="Trash"></td> 
				</tr> 
				<tr> 
   					<td><input type="checkbox"></td> 
    				<td>Dolor Lorem Amet</td> 
    				<td>Articles</td> 
    				<td>16th April 2011</td> 
   				 	<td><input type="image" src="resources/images/icn_edit.png" title="Edit"><input type="image" src="resources/images/icn_trash.png" title="Trash"></td> 
				</tr>
				<tr> 
   					<td><input type="checkbox"></td> 
    				<td>Dolor Lorem Amet</td> 
    				<td>Articles</td> 
    				<td>16th April 2011</td> 
   				 	<td><input type="image" src="resources/images/icn_edit.png" title="Edit"><input type="image" src="resources/images/icn_trash.png" title="Trash"></td> 
				</tr>  
			</tbody> 
			</table>
			</div><!-- end of #tab1 -->
			
			<div id="tab2" class="tab_content">
			<table class="tablesorter" cellspacing="0"> 
			<thead> 
				<tr> 
   					<th></th> 
    				<th>Comment</th> 
    				<th>Posted by</th> 
    				<th>Posted On</th> 
    				<th>Actions</th> 
				</tr> 
			</thead> 
			<tbody> 
				<tr> 
   					<td><input type="checkbox"></td> 
    				<td>Lorem Ipsum Dolor Sit Amet</td> 
    				<td>Mark Corrigan</td> 
    				<td>5th April 2011</td> 
    				<td><input type="image" src="resources/images/icn_edit.png" title="Edit"><input type="image" src="resources/images/icn_trash.png" title="Trash"></td> 
				</tr> 
				<tr> 
   					<td><input type="checkbox"></td> 
    				<td>Ipsum Lorem Dolor Sit Amet</td> 
    				<td>Jeremy Usbourne</td> 
    				<td>6th April 2011</td> 
   				 	<td><input type="image" src="resources/images/icn_edit.png" title="Edit"><input type="image" src="resources/images/icn_trash.png" title="Trash"></td> 
				</tr>
				<tr> 
   					<td><input type="checkbox"></td> 
    				<td>Sit Amet Dolor Ipsum</td> 
    				<td>Super Hans</td> 
    				<td>10th April 2011</td> 
    				<td><input type="image" src="resources/images/icn_edit.png" title="Edit"><input type="image" src="resources/images/icn_trash.png" title="Trash"></td> 
				</tr> 
				<tr> 
   					<td><input type="checkbox"></td> 
    				<td>Dolor Lorem Amet</td> 
    				<td>Alan Johnson</td> 
    				<td>16th April 2011</td> 
   				 	<td><input type="image" src="resources/images/icn_edit.png" title="Edit"><input type="image" src="resources/images/icn_trash.png" title="Trash"></td> 
				</tr> 
				<tr> 
   					<td><input type="checkbox"></td> 
    				<td>Dolor Lorem Amet</td> 
    				<td>Dobby</td> 
    				<td>16th April 2011</td> 
   				 	<td><input type="image" src="resources/images/icn_edit.png" title="Edit"><input type="image" src="resources/images/icn_trash.png" title="Trash"></td> 
				</tr> 
			</tbody> 
			</table>

			</div><!-- end of #tab2 -->
			
		</div><!-- end of .tab_container -->
		
		</article><!-- end of content manager article -->
		
		<article class="module width_quarter">
			<header><h3>Messages</h3></header>
			<div class="message_list">
				<div class="module_content">
					<div class="message"><p>Vivamus sagittis lacus vel augue laoreet rutrum faucibus dolor.</p>
					<p><strong>John Doe</strong></p></div>
					<div class="message"><p>Vivamus sagittis lacus vel augue laoreet rutrum faucibus dolor.</p>
					<p><strong>John Doe</strong></p></div>
					<div class="message"><p>Vivamus sagittis lacus vel augue laoreet rutrum faucibus dolor.</p>
					<p><strong>John Doe</strong></p></div>
					<div class="message"><p>Vivamus sagittis lacus vel augue laoreet rutrum faucibus dolor.</p>
					<p><strong>John Doe</strong></p></div>
					<div class="message"><p>Vivamus sagittis lacus vel augue laoreet rutrum faucibus dolor.</p>
					<p><strong>John Doe</strong></p></div>
				</div>
			</div>
			<footer>
				<form class="post_message">
					<input type="text" value="Message" onfocus="if(!this._haschanged){this.value=''};this._haschanged=true;">
					<input type="submit" class="btn_post_message" value=""/>
				</form>
			</footer>
		</article><!-- end of messages article -->
		
		<div class="clear"></div>
		
		<article class="module width_full">
			<header><h3>Post New Article</h3></header>
				<div class="module_content">
						<fieldset>
							<label class="field">Post Title</label>
							<input type="text">
						</fieldset>
						<fieldset>
							<label class="field">Content</label>
							<div id="editors">
								<ul>
									<li><a href="#htmlTab">HTML</a></li>
									<li><a href="#textTab">Text</a></li>
								</ul>
								<div id="htmlTab">
									<textarea rows="12" style="padding-left:0px;margin-left:0px ;width:100%"></textarea>
								</div>
								<div id="textTab">
									<textarea id="code" rows="12"></textarea>
								</div>
							</div>
						</fieldset>
						<fieldset style="width:48%; float:left; margin-right: 3%;"> <!-- to make two field float next to one another, adjust values accordingly -->
							<label class="field">Category</label>
							<select style="width:92%;">
								<option>Articles</option>
								<option>Tutorials</option>
								<option>Freebies</option>
							</select>
						</fieldset>
						<fieldset style="width:48%; float:left;"> <!-- to make two field float next to one another, adjust values accordingly -->
							<label class="field">Tags</label>
							<input type="text" style="width:92%;">
						</fieldset><div class="clear"></div>
				</div>
			<footer>
				<div class="submit_link">
					<select>
						<option>Draft</option>
						<option>Published</option>
					</select>
					<input type="submit" value="Publish" class="alt_btn">
					<input type="submit" value="Reset">
				</div>
			</footer>
		</article><!-- end of post new article -->
		
		<h4 class="alert_warning">A Warning Alert</h4>
		
		<h4 class="alert_error">An Error Message</h4>
		
		<h4 class="alert_success">A Success Message</h4>
		
		<article class="module width_full">
			<header><h3>Basic Styles</h3></header>
				<div class="module_content">
					<h1>Header 1</h1>
					<h2>Header 2</h2>
					<h3>Header 3</h3>
					<h4>Header 4</h4>
					<p>Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Cras mattis consectetur purus sit amet fermentum. Maecenas faucibus mollis interdum. Maecenas faucibus mollis interdum. Cras justo odio, dapibus ac facilisis in, egestas eget quam.</p>

<p>Donec id elit non mi porta <a href="#">link text</a> gravida at eget metus. Donec ullamcorper nulla non metus auctor fringilla. Cras mattis consectetur purus sit amet fermentum. Aenean eu leo quam. Pellentesque ornare sem lacinia quam venenatis vestibulum.</p>

					<ul>
						<li>Donec ullamcorper nulla non metus auctor fringilla. </li>
						<li>Cras mattis consectetur purus sit amet fermentum.</li>
						<li>Donec ullamcorper nulla non metus auctor fringilla. </li>
						<li>Cras mattis consectetur purus sit amet fermentum.</li>
					</ul>
				</div>
				<ul class="sortable" style="list-style: none;">
					<li class="ui-state-default">Name1</li>
					<li class="ui-state-default">Name2</li>
					<li class="ui-state-default">Name3</li>
					<li class="ui-state-default">Name4</li>
				</ul>
		</article><!-- end of styles article -->
		<div class="spacer"></div>
	</section>


</body>

</html>