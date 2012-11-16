/**
 * Cookie plugin
 *
 * Copyright (c) 2006 Klaus Hartl (stilbuero.de)
 * Dual licensed under the MIT and GPL licenses:
 * http://www.opensource.org/licenses/mit-license.php
 * http://www.gnu.org/licenses/gpl.html
 *
 */

/**
 * Create a cookie with the given name and value and other optional parameters.
 *
 * @example $.cookie('the_cookie', 'the_value');
 * @desc Set the value of a cookie.
 * @example $.cookie('the_cookie', 'the_value', { expires: 7, path: '/', domain: 'jquery.com', secure: true });
 * @desc Create a cookie with all available options.
 * @example $.cookie('the_cookie', 'the_value');
 * @desc Create a session cookie.
 * @example $.cookie('the_cookie', null);
 * @desc Delete a cookie by passing null as value. Keep in mind that you have to use the same path and domain
 *       used when the cookie was set.
 *
 * @param String name The name of the cookie.
 * @param String value The value of the cookie.
 * @param Object options An object literal containing key/value pairs to provide optional cookie attributes.
 * @option Number|Date expires Either an integer specifying the expiration date from now on in days or a Date object.
 *                             If a negative value is specified (e.g. a date in the past), the cookie will be deleted.
 *                             If set to null or omitted, the cookie will be a session cookie and will not be retained
 *                             when the the browser exits.
 * @option String path The value of the path atribute of the cookie (default: path of page that created the cookie).
 * @option String domain The value of the domain attribute of the cookie (default: domain of page that created the cookie).
 * @option Boolean secure If true, the secure attribute of the cookie will be set and the cookie transmission will
 *                        require a secure protocol (like HTTPS).
 * @type undefined
 *
 * @name $.cookie
 * @cat Plugins/Cookie
 * @author Klaus Hartl/klaus.hartl@stilbuero.de
 */

/**
 * Get the value of a cookie with the given name.
 *
 * @example $.cookie('the_cookie');
 * @desc Get the value of a cookie.
 *
 * @param String name The name of the cookie.
 * @return The value of the cookie.
 * @type String
 *
 * @name $.cookie
 * @cat Plugins/Cookie
 * @author Klaus Hartl/klaus.hartl@stilbuero.de
 */
jQuery.cookie = function(name, value, options) {
    if (typeof value != 'undefined') { // name and value given, set cookie
        options = options || {};
        if (value === null) {
            value = '';
            options = $.extend({}, options); // clone object since it's unexpected behavior if the expired property were changed
            options.expires = -1;
        }
        var expires = '';
        if (options.expires && (typeof options.expires == 'number' || options.expires.toUTCString)) {
            var date;
            if (typeof options.expires == 'number') {
                date = new Date();
                date.setTime(date.getTime() + (options.expires * 24 * 60 * 60 * 1000));
            } else {
                date = options.expires;
            }
            expires = '; expires=' + date.toUTCString(); // use expires attribute, max-age is not supported by IE
        }
        // NOTE Needed to parenthesize options.path and options.domain
        // in the following expressions, otherwise they evaluate to undefined
        // in the packed version for some reason...
        var path = options.path ? '; path=' + (options.path) : '';
        var domain = options.domain ? '; domain=' + (options.domain) : '';
        var secure = options.secure ? '; secure' : '';
        document.cookie = [name, '=', encodeURIComponent(value), expires, path, domain, secure].join('');
    } else { // only name given, get cookie
        var cookieValue = null;
        if (document.cookie && document.cookie != '') {
            var cookies = document.cookie.split(';');
            for (var i = 0; i < cookies.length; i++) {
                var cookie = jQuery.trim(cookies[i]);
                // Does this cookie string begin with the name we want?
                if (cookie.substring(0, name.length + 1) == (name + '=')) {
                    cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
                    break;
                }
            }
        }
        return cookieValue;
    }
};
$(document).ready(function(){
	// Declare well used objects
	var editBnt = $('#edit_dashboard');
	var saveBnt = $('#save_dashboard');
	var regions = $("#dashboard .sortable");
	var widgets = $('.widget',regions);

	/* CREATE INITIAL DASHBOARD STATE */
	// Hide Save button
	saveBnt.hide();

	// Get the current dashboard settings from the cookie
	// and position the widgets accordingly
	//
	// When the widgets are saved there are saved in a string with the following
	// format
	//
	// {region}[]={widget_id}&...&{widget_id}[]=visible/hidden
	//
	// Where the following variables exist:
	//	{region} 	= The dashboard region, either topsection, leftsection or rightsection
	//	{widget_id} = The widget md5 name hash
	//
	// The actual widgets have ID of something like widget_98321j3ky98123jk213 but only ever
	// the bit after widget_ is stored
	var arr = $.cookie('gsk_dashboard');
	if( arr != null)
	{
		// Only position the widgets if we have a cookie
		arr = arr.split('&');
	  	for(i=0;i<arr.length;i++)
	  	{
	  		var cmd = arr[i].split('[]=');
	  		if(cmd[0] == "topsection" || cmd[0] == "leftsection" || cmd[0] == "rightsection")
	  		{
	  			// We have a position cmd
	  			$('#widget_'+cmd[1],regions).appendTo('#'+cmd[0]);
	  		}
	  		else
	  		{
	  			// We have a visibility cmd
	  			if(cmd[1] == 'hidden')
	  				$('#widget_'+cmd[0],regions).hide();
	  		}
	  	}
	 }

	// Make regions sortable
	regions.sortable({
		cursor: 'move',
		connectWith: [$('#topsection'),$('#leftsection'),$('#rightsection')],
		opacity: 0.8,
		scroll: false
	});

	// Disable regions
	regions.sortable("disable");

	// Assign a class to the actions so we can identify them in the future
	$('div.action',widgets).each(function(){
		var tick = $('img:eq(0)',this);
		var cross= $('img:eq(1)',this);

		// Setup onclick actions
		tick.addClass('db-visible').hide().click(function(){
			$(this).hide();
			cross.show();
		});
		cross.addClass('db-hidden').hide().click(function(){
			$(this).hide();
			tick.show();
		});
	});

	/* WHEN USER REQUESTS TO EDIT DASHBOARD */
	editBnt.click(function(){
		// Hide the button and display the save button
		editBnt.hide();
		saveBnt.show();

		// Make regions sortable
		regions.sortable("enable");

		// Get rid of everything apart from the header in the widgets
		$('div.body',widgets).hide();

		// Loop over all widgets
		widgets.each(function(){
			// Decide whether to show a tick or a cross for this widget
			if($(this).css('display') == 'none')
				$('.action img[widgetControl=true].db-hidden',this).show();
			else
				$('.action img[widgetControl=true].db-visible',this).show();

			// Show the widget
			$(this).show();
		});

		$('.action img[widgetControl=false]',widgets).hide();
		
	});

	/* WHEN THE USER REQUESTS TO SAVE THE DASHBOARD */
	saveBnt.click(function(){
		// Variable to store cookie settings in
		var cookie = "";

		// Save the order which the widgets are in
		regions.each(function(){
			var widget = $(this).sortable("serialize").replace(/widget/g, $(this).attr('id'));

			// If no widgets in region, move to next region
			if(widget == "") return true;

			if(cookie == "") cookie = widget;
			else cookie = cookie + "&" + widget;
		});

		// Loop over all widgets and save their values into cookies
		// If it isn't meant to be shown hide the widget
		widgets.each(function(){
			newID = $(this).attr('id').replace(/widget_/g, '');
			if($('div.action img[widgetControl=true]:visible',this).hasClass('db-hidden')){
				// Cross is showing
				cookie = cookie + "&" + newID + "[]=hidden";
				$(this).hide();
			}
			else{
				// Tick is showing
				cookie = cookie + "&" + newID + "[]=visible";
			}
		});

		// Save cookie
		$.cookie('gsk_dashboard',cookie,{expires: 31});

		// Hide the button and display the edit button
		saveBnt.hide();
		editBnt.show();

		// Make regions sortable
		regions.sortable("disable");

		// Show bodies of widgets again
		$('div.body',widgets).show();

		// Hide the actions
		$('.action img[widgetControl=true]',widgets).hide();
		$('.action img[widgetControl=false]',widgets).show();
		$('div.loading').hide();
	});
  });
