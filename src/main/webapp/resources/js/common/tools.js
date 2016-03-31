//ajax请求数据判断会话是否过期
$(document).ajaxComplete(function(event, xhr, settings) {
	var sessionstatus = xhr.getResponseHeader("sessionstatus");
	if (sessionstatus == "sessionTimeOut") {
		jAlert("用户会话已经过期....");
		window.location.href = '../index.jsp';
	}
});

var contentPath = "../http";
var apiPath = "../api";

/**
 * 格式化日期
 */
Date.prototype.format = function(format) {
	var o = {
		"M+" : this.getMonth() + 1, // month
		"d+" : this.getDate(), // day
		"h+" : this.getHours(), // hour
		"m+" : this.getMinutes(), // minute
		"s+" : this.getSeconds(), // second
		"q+" : Math.floor((this.getMonth() + 3) / 3),
		"S" : this.getMilliseconds()
	}
	if (/(y+)/.test(format))
		format = format.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(format))
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
					: ("00" + o[k]).substr(("" + o[k]).length));
	return format;
}

/**
 * 重写array的属性方法
 * 
 * @param dx
 * @returns {Boolean}
 */
Array.prototype.remove = function(dx) {
	if (!dx) {
		return false;
	}
	for (var i = 0, n = 0; i < this.length; i++) {
		if (this[i] != dx) {
			this[n++] = this[i];
		}
	}
	this.length -= 1;
};

var Util = {};
/**
 * 判断是否为空
 * 
 * @param v
 * @returns {Boolean}
 */
Util.empty = function(v) {
	switch (typeof v) {
	case 'undefined':
		return true;
	case 'string':
		if (v.length == 0)
			return true;
		break;
	case 'boolean':
		if (!v)
			return true;
		break;
	case 'number':
		if (0 === v)
			return true;
		break;
	case 'object':
		if (null === v)
			return true;
		if (undefined !== v.length && v.length == 0)
			return true;
		for ( var k in v) {
			return false;
		}
		return true;
		break;
	}
	return false;
};

/**
 * 格式化日期
 */
Util.formateDate = function(date) {
	var result = "", hour = 0, min = date;
	if (date > 60) {
		hour = (date / 60).toString().substr(0,
				(date / 60).toString().indexOf("."));
	}
	if (hour != 0) {
		result += hour + "小时";
		min = date - hour * 60;
	}
	result += min + "分前";
	return result;
}

/**
 * 通过AJAX获取json数据
 */
Util.getAjaxData = function(url, param, callFun, async) {
	$.ajax({
		type : "POST",
		url : url,
		data : param,
		cache : false,
		async : async,
		success : function(data, textStatus) {
			callFun(data);
		},
		error : function(x) {
			jAlert("请求数据异常");
		}
	});
};

var jsArray = {};
/**
 * body中添加javascript 脚本
 */
Util.loadScript = function(scriptName, callback) {
	if (!jsArray[scriptName]) {
		jsArray[scriptName] = true;
		var body = document.getElementsByTagName('body')[0];
		var script = document.createElement('script');
		script.type = 'text/javascript';
		script.src = scriptName;
		script.onload = callback;
		body.appendChild(script);
	} else if (callback) {
		callback();
	}
}

/**
 * 切换和加载页面用
 * 
 * @param url
 * @param container
 */

Util.loadURL = function(url, container, fun) {
	$.ajax({
				type : "GET",
				url : url,
				dataType : 'html',
				cache : true,
				beforeSend : function() {
					container
							.html('<h1><i class="fa fa-cog fa-spin"></i> Loading...</h1>');
					if (container[0] == $("#content")[0]) {
						drawBreadCrumb();
						$("html").animate({
							scrollTop : 0
						}, "fast");
					}
				},
				success : function(data) {
					container.css({
						opacity : '0.0'
					}).html(data).delay(50).animate({
						opacity : '1.0'
					}, 300);

					fun();
				},
				error : function(xhr, ajaxOptions, thrownError) {
					container
							.html('<h4 style="margin-top:10px; display:block; text-align:left"><i class="fa fa-warning txt-color-orangeDark"></i> Error 404! Page not found.</h4>');
				},
				async : false
			});
}
/**
 * 弹出框信息
 */
Util.window = function(title, id, contentUrl, width, height) {
	$('#' + id).dialog({
		title : title,
		href : contentUrl,
		width : width,
		height : height,
		closed : false,
		cache : false,
		modal : true
	});
}

/**
 * 关闭窗口
 */
Util.closeWindow = function(id) {
	$('#' + id).dialog('close');
};

//提示消息弹窗
Util.showMsg = function(messges, dialog_size) {
	if (!dialog_size)
		dialog_size = 0;
	$.messager.show({
		title : '消息',
		msg : messges,
		showType : 'slide',
		style : {
			right : '',
			top : document.body.scrollTop + document.documentElement.scrollTop
					- dialog_size,
			bottom : '确定'
		}
	});
}

//提示消息弹窗
topCenter = function(messges, dialog_size) {
	if (!dialog_size)
		dialog_size = 0;
	$.messager.show({
		title : '消息',
		msg : messges,
		showType : 'slide',
		style : {
			right : '',
			top : document.body.scrollTop + document.documentElement.scrollTop
					- dialog_size,
			bottom : ''
		}
	});
}

function Map() {
	var struct = function(key, value) {
		this.key = key;
		this.value = value;
	}

	var put = function(key, value) {
		for (var i = 0; i < this.arr.length; i++) {
			if (this.arr[i].key === key) {
				this.arr[i].value = value;
				return;
			}
		}
		this.arr[this.arr.length] = new struct(key, value);
	}

	var get = function(key) {
		for (var i = 0; i < this.arr.length; i++) {
			if (this.arr[i].key === key) {
				return this.arr[i].value;
			}
		}
		return null;
	}

	var remove = function(key) {
		var v;
		for (var i = 0; i < this.arr.length; i++) {
			v = this.arr.pop();
			if (v.key === key) {
				continue;
			}
			this.arr.unshift(v);
		}
	}

	var size = function() {
		return this.arr.length;
	}

	var isEmpty = function() {
		return this.arr.length <= 0;
	}
	this.arr = new Array();
	this.get = get;
	this.put = put;
	this.remove = remove;
	this.size = size;
	this.isEmpty = isEmpty;
}
