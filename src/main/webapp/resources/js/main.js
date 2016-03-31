//左边菜单打开右边页面判断
function addTab(title, href) {
	if ($("#content_div_id").tabs('exists', title)) {
		$("#content_div_id").tabs('select', title);
	} else {
		/**添加一个tab标签**/
		$("#content_div_id").tabs('add', {
			title : title,
			selected : true,
			closable : true,
			cache : false,
			href : href
		});
	}
}
var commonTree = {
	loadHeader : function(treePanel) {
		$.ajax({
			type : "POST",
			url : treePanel.dataUrl,
			data : treePanel.param,
			async : false,
			success : function(data) {
				var resultStr = completeHeader(data);
				$(".nav01").append(resultStr);
			}
		});
	}
};
/**
 * 组装header菜单
 * @param data
 * @returns {String}
 */
function completeHeader(data) {
	var headerStr = '<ul>';
	for (var i = 0; i < data.length; i++) {
		headerStr += '<li id="' + data[i].id + '" class="nav01_lie' + i
				+ '"><i><img src="../resources/electric_skin/icons/'
				+ data[i].icon + '"/></i><font>' + data[i].name
				+ '</font><a href="javascript:void(0)"></a></li>';
	}
	headerStr += "</ul>";
	return headerStr;
}

$(function() {
	//左边菜单显示隐藏
	$("#left_menu_jt").click(function() {
		var jt = $(this)
		var left_c = $(".left_cont")
		if (left_c.is(":hidden")) {
			$(".b_cont").css("left", left_c.width());
			jt.find("i").attr("class", "icon-chevron-up");
			$(".nav01,.logo,.user").fadeIn();
			jt.animate({
				top : 76
			}, 200);
			$(".header_bg").animate({
				top : 0
			}, 200, function() {
				left_c.slideDown(400, function() {
					$(".b_cont").css({
						left : left_c.width(),
						top : 76
					});
				});
			});
		} else {
			$(".b_cont").css({
				left : 0,
				top : 0
			});
			jt.find("i").attr("class", "icon-chevron-down");
			$(".nav01,.logo,.user").fadeOut();
			left_c.slideUp(400, function() {
				jt.animate({
					top : 0
				}, 200);
				$(".header_bg").animate({
					top : -76
				}, 200);
			})
		}
		$("#content_div_id").tabs();
//		var tab = $('#content_div_id').tabs('getSelected');
//		var index = $('#content_div_id').tabs('getTabIndex',tab);
//		$("#content_div_id").tabs('select', index);
	});
	//左边菜单伸缩
	$(".nav02 li").click(function() {
		$(this).siblings("li").removeClass("on")
		$(this).addClass("on")
	});

	$(".datagrid-btable").each(function() {
		$(this).find(".datagrid-row:odd td").css("background", "#DCDCDC")
	})

	//感叹号提示
	$(".icon-prompt").on("click", function() {
		$(this).toggleClass("active");
	});
});
document.write('<script type="text/javascript" src="../resources/js/controls/Dialog.js"><\/script>');
document.write('<script type="text/javascript" src="../resources/js/controls/DataTable.js"><\/script>');
(function(jQuery) {

	if (jQuery.browser)
		return;

	jQuery.browser = {};
	jQuery.browser.mozilla = false;
	jQuery.browser.webkit = false;
	jQuery.browser.opera = false;
	jQuery.browser.msie = false;

	var nAgt = navigator.userAgent;
	jQuery.browser.name = navigator.appName;
	jQuery.browser.fullVersion = '' + parseFloat(navigator.appVersion);
	jQuery.browser.majorVersion = parseInt(navigator.appVersion, 10);
	var nameOffset, verOffset, ix;

	// In Opera, the true version is after "Opera" or after "Version" 
	if ((verOffset = nAgt.indexOf("Opera")) != -1) {
		jQuery.browser.opera = true;
		jQuery.browser.name = "Opera";
		jQuery.browser.fullVersion = nAgt.substring(verOffset + 6);
		if ((verOffset = nAgt.indexOf("Version")) != -1)
			jQuery.browser.fullVersion = nAgt.substring(verOffset + 8);
	}
	// In MSIE, the true version is after "MSIE" in userAgent 
	else if ((verOffset = nAgt.indexOf("MSIE")) != -1) {
		jQuery.browser.msie = true;
		jQuery.browser.name = "Microsoft Internet Explorer";
		jQuery.browser.fullVersion = nAgt.substring(verOffset + 5);
	}
	// In Chrome, the true version is after "Chrome" 
	else if ((verOffset = nAgt.indexOf("Chrome")) != -1) {
		jQuery.browser.webkit = true;
		jQuery.browser.name = "Chrome";
		jQuery.browser.fullVersion = nAgt.substring(verOffset + 7);
	}
	// In Safari, the true version is after "Safari" or after "Version" 
	else if ((verOffset = nAgt.indexOf("Safari")) != -1) {
		jQuery.browser.webkit = true;
		jQuery.browser.name = "Safari";
		jQuery.browser.fullVersion = nAgt.substring(verOffset + 7);
		if ((verOffset = nAgt.indexOf("Version")) != -1)
			jQuery.browser.fullVersion = nAgt.substring(verOffset + 8);
	}
	// In Firefox, the true version is after "Firefox" 
	else if ((verOffset = nAgt.indexOf("Firefox")) != -1) {
		jQuery.browser.mozilla = true;
		jQuery.browser.name = "Firefox";
		jQuery.browser.fullVersion = nAgt.substring(verOffset + 8);
	}
	// In most other browsers, "name/version" is at the end of userAgent 
	else if ((nameOffset = nAgt.lastIndexOf(' ') + 1) < (verOffset = nAgt
			.lastIndexOf('/'))) {
		jQuery.browser.name = nAgt.substring(nameOffset, verOffset);
		jQuery.browser.fullVersion = nAgt.substring(verOffset + 1);
		if (jQuery.browser.name.toLowerCase() == jQuery.browser.name
				.toUpperCase()) {
			jQuery.browser.name = navigator.appName;
		}
	}
	// trim the fullVersion string at semicolon/space if present 
	if ((ix = jQuery.browser.fullVersion.indexOf(";")) != -1)
		jQuery.browser.fullVersion = jQuery.browser.fullVersion
				.substring(0, ix);
	if ((ix = jQuery.browser.fullVersion.indexOf(" ")) != -1)
		jQuery.browser.fullVersion = jQuery.browser.fullVersion
				.substring(0, ix);

	jQuery.browser.majorVersion = parseInt('' + jQuery.browser.fullVersion, 10);
	if (isNaN(jQuery.browser.majorVersion)) {
		jQuery.browser.fullVersion = '' + parseFloat(navigator.appVersion);
		jQuery.browser.majorVersion = parseInt(navigator.appVersion, 10);
	}
	jQuery.browser.version = jQuery.browser.majorVersion;
})(jQuery);