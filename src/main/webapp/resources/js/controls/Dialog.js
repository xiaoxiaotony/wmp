(function($) {
	/**
	 * 拖拽实现
	 */
	var isMouseDown = false;
	var currentElement = null;
	var dropCallbacks = {};
	var dragCallbacks = {};
	var bubblings = {};
	var lastMouseX;
	var lastMouseY;
	var lastElemTop;
	var lastElemLeft;
	var dragStatus = {};
	var holdingHandler = false;
	$.getMousePosition = function(e) {
		var posx = 0;
		var posy = 0;
		if (!e)
			var e = window.event;
		if (e.pageX || e.pageY) {
			posx = e.pageX;
			posy = e.pageY;
		} else if (e.clientX || e.clientY) {
			posx = e.clientX + document.body.scrollLeft + document.documentElement.scrollLeft;
			posy = e.clientY + document.body.scrollTop + document.documentElement.scrollTop;
		}
		return {
			'x' : posx,
			'y' : posy
		};
	};
	$.updatePosition = function(e) {
		var pos = $.getMousePosition(e);
		var spanX = (pos.x - lastMouseX);
		var spanY = (pos.y - lastMouseY);
		$(currentElement).css("top", (lastElemTop + spanY));
		$(currentElement).css("left", (lastElemLeft + spanX));
	};
	$(document).mousemove(function(e) {
		if (isMouseDown && dragStatus[currentElement.id] != 'false') {
			$.updatePosition(e);
			if (dragCallbacks[currentElement.id] != undefined) {
				dragCallbacks[currentElement.id](e, currentElement);
			}
			return false;
		}
	});
	$(document).mouseup(function(e) {
		if (isMouseDown && dragStatus[currentElement.id] != 'false') {
			isMouseDown = false;
			if (dropCallbacks[currentElement.id] != undefined) {
				dropCallbacks[currentElement.id](e, currentElement);
			}
			return false;
		}
	});
	$.fn.ondrag = function(callback) {
		return this.each(function() {
			dragCallbacks[this.id] = callback;
		});
	};
	$.fn.ondrop = function(callback) {
		return this.each(function() {
			dropCallbacks[this.id] = callback;
		});
	};
	$.fn.dragOff = function() {
		return this.each(function() {
			dragStatus[this.id] = 'off';
		});
	};
	$.fn.dragOn = function() {
		return this.each(function() {
			dragStatus[this.id] = 'on';
		});
	};
	$.fn.setHandler = function(handlerId) {
		return this.each(function() {
			var draggable = this;
			bubblings[this.id] = true;
			$(draggable).css("cursor", "");
			dragStatus[draggable.id] = "handler";
			$("#" + handlerId).css("cursor", "move");
			$("#" + handlerId).mousedown(function(e) {
				holdingHandler = true;
				$(draggable).trigger('mousedown', e);
			});
			$("#" + handlerId).mouseup(function(e) {
				holdingHandler = false;
			});
		});
	}
	$.fn.easydrag = function(allowBubbling) {
		return this.each(function() {
			if (undefined == this.id || !this.id.length)
				this.id = "easydrag" + (new Date().getTime());
			bubblings[this.id] = allowBubbling ? true : false;
			dragStatus[this.id] = "on";
			$(this).css("cursor", "move");
			$(this).mousedown(function(e) {
				if ((dragStatus[this.id] == "off") || (dragStatus[this.id] == "handler" && !holdingHandler))
					return bubblings[this.id];
				$(this).css("position", "absolute");
				$(this).css("z-index", parseInt(new Date().getTime() / 1000));
				isMouseDown = true;
				currentElement = this;
				var pos = $.getMousePosition(e);
				lastMouseX = pos.x;
				lastMouseY = pos.y;
				lastElemTop = this.offsetTop;
				lastElemLeft = this.offsetLeft;
				$.updatePosition(e);
				return bubblings[this.id];
			});
		});
	};

	$.alerts = {
		verticalOffset : 0,
		horizontalOffset : 0,
		repositionOnResize : true,
		overlayOpacity : .30,
		overlayColor : '#FFF',
		draggable : true,
		okButton : '确定',
		cancelButton : '取消',
		dialogClass : null,
		alert : function(message, title, time, callback) {
			if (title == null)
				title = '提示';
			if(message.indexOf("成功")>-1||message.indexOf("完成")>-1){
				time = 1500;
			}
			$.alerts._show(title, message, null, null, null, 'alert', function(result) {
				if (callback&&result){
					callback(result);
				}
			},time);
		},
		confirm : function(message, title, callback) {
			if (title == null)
				title = '确认';
			$.alerts._show(title, message, null, null, null, 'confirm', function(result) {
				if (callback)
					callback(result);
			});
		},
		prompt : function(message, title, value, callback) {
			if (title == null)
				title = '请输入';
			$.alerts._show(title, message, value, null, null, 'prompt', function(result) {
				if (callback)
					callback(result);
			});
		},
		iframe : function(url, title, width, height) {
			if (title == null)
				title = '提示';
			$.alerts._show(title, url, null, width, height, 'iframe', null);
		},
		choose : function(message, title, width, height, callback) {
			if (title == null)
				title = '提示';
			$.alerts._show(title, message, null, width, height, 'choose', function(result) {
				if (callback)
					callback(result);
			});
		},
		html : function(message, title, width, height) {
			if (title == null)
				title = '提示';
			$.alerts._show(title, message, null, width, height, 'html', null);
		},
		_show : function(title, msg, value, width, height, type, callback, time) {
			$.alerts._hide();
			$.alerts._overlay('show');
			$("body")
					.append(
							'<div id="popup_container" class="gSys_msg_box" style="display:none;">'
									+ '<div id="popup_title" class="ptitle"><h4></h4></span><a href="javascript:;" class="fn-bg aclose" title="关闭">×</a></div>'
									+ '<div id="popup_message_box" class="pbox"><div class="pmsg"><b title="提示:" class="ico"></b><div class="ct" id="popup_message"></div></div></div>'
									+ '<div id="popup_opt_box" class="popt"><div class="opt" id="opt"></div></div>'
									+ '</div>');
			$("#popup_container").animate({
				height : 'show',
				opacity : 'show'
			}, 'normal');
			if ($.alerts.dialogClass)
				$("#popup_container").addClass($.alerts.dialogClass);
			if (width == null) {
				width = 460;
			}
			$("#popup_container").css({
				position : 'absolute',
				zIndex : 99999,
				padding : 0,
				margin : 0,
				width : width + 'px'
			});
			if (height) {
				$("#popup_container").css("height", height + 'px');
			}
			$("#popup_title h4").text(title);
			$("#popup_message_box b").addClass(type);
			$("#popup_message").text(msg);
			$("#popup_message").html($("#popup_message").text().replace(/\n/g, '<br />'));
			$.alerts._maintainPosition(true);
			$.alerts._reposition();
			$("#popup_title a").click(function() {
				$.alerts._hide();
			});
			switch (type) {
			case 'alert':
				$("#opt").html(
						'<div id="popup_ok" class="pbtn" tabindex="0"><span>' + $.alerts.okButton + '</span></div>');
				$("#popup_ok").click(function() {
					$.alerts._hide();
					callback(true);
				});
				$("#popup_ok").mouseover(function() {
					$(this).attr("class", "pbtn btn_hover");
				});
				$("#popup_ok").mouseout(function() {
					$(this).attr("class", "pbtn");
				});
				$("#popup_ok").mousedown(function() {
					$(this).attr("class", "pbtn btn_active");
				});
				$("#popup_ok").mouseup(function() {
					$(this).attr("class", "pbtn");
				});
				if (time) {
					setTimeout(function() {
						$.alerts._hide();
					}, time);
				}
				$("#popup_ok").focus().keypress(function(e) {
					if (e.keyCode == 13 || e.keyCode == 27)
						$("#popup_ok").trigger('click');
				});
				break;
			case 'confirm':
				$("#opt").html(
						'<div id="popup_cancel" class="pbtn" tabindex="0"><span>' + $.alerts.cancelButton
								+ '</span></div><div id="popup_ok" class="pbtn" tabindex="0"><span>'
								+ $.alerts.okButton + '</span></div>');
				$("#popup_ok").click(function() {
					$.alerts._hide();
					if (callback)
						callback(true);
				});
				$("#popup_cancel").click(function() {
					$.alerts._hide();
					if (callback)
						callback(false);
				});
				$("#popup_ok, #popup_cancel").mouseover(function() {
					$(this).attr("class", "pbtn btn_hover");
				});
				$("#popup_ok, #popup_cancel").mouseout(function() {
					$(this).attr("class", "pbtn");
				});
				$("#popup_ok, #popup_cancel").mousedown(function() {
					$(this).attr("class", "pbtn btn_active");
				});
				$("#popup_ok, #popup_cancel").mouseup(function() {
					$(this).attr("class", "pbtn");
				});
				$("#popup_ok").focus();
				$("#popup_ok, #popup_cancel").keypress(function(e) {
					if (e.keyCode == 13)
						$("#popup_ok").trigger('click');
					if (e.keyCode == 27)
						$("#popup_cancel").trigger('click');
				});
				break;
			case 'iframe':
				$("#popup_message_box").remove();
				$("#popup_opt_box").remove();
				$("#popup_container")
						.append(
								"<div id='popup_iframe_box' class='piframe'><div id='loading' class='loading'><img src='./loading.gif'></div></div>");
				$("#loading").css({
					width : width + "px",
					height : height + "px",
					padding : 0,
					margin : 0
				});
				var iframe = document.createElement("iframe");
				iframe.id = "iframe";
				iframe.width = width + "px";
				iframe.height = height + "px";
				iframe.scrolling = "auto";
				iframe.setAttribute("frameborder", "0", 0);
				iframe.src = msg;
				iframe.style.display = "none";
				$("#popup_iframe_box").append(iframe);
				$(iframe).load(function() {
					iframe.style.display = "block";
					$("#loading").hide();
				});
				break;
			case 'prompt':
				$("#popup_message_box b").remove();
				$("#popup_message").attr("class", "cp");
				$("#popup_message").append('<br /><input type="text" class="pinput" size="48" id="popup_prompt" />');
				$("#opt").html(
						'<div id="popup_cancel" class="pbtn" tabindex="0"><span>' + $.alerts.cancelButton
								+ '</span></div><div id="popup_ok" class="pbtn" tabindex="0"><span>'
								+ $.alerts.okButton + '</span></div>');
				$("#popup_ok, #popup_cancel").mouseover(function() {
					$(this).attr("class", "pbtn btn_hover");
				});
				$("#popup_ok, #popup_cancel").mouseout(function() {
					$(this).attr("class", "pbtn");
				});
				$("#popup_ok, #popup_cancel").mousedown(function() {
					$(this).attr("class", "pbtn btn_active");
				});
				$("#popup_ok, #popup_cancel").mouseup(function() {
					$(this).attr("class", "pbtn");
				});
				$("#popup_ok").click(function() {
					var val = $("#popup_prompt").val();
					$.alerts._hide();
					if (callback)
						callback(val);
				});
				$("#popup_cancel").click(function() {
					$.alerts._hide();
					if (callback)
						callback(null);
				});
				$("#popup_prompt, #popup_ok, #popup_cancel").keypress(function(e) {
					if (e.keyCode == 13)
						$("#popup_ok").trigger('click');
					if (e.keyCode == 27)
						$("#popup_cancel").trigger('click');
				});
				if (value)
					$("#popup_prompt").val(value);
				$("#popup_prompt").focus().select();
				break;
			case 'choose':
				$("#popup_message_box b").remove();
				$("#popup_message").attr("class", "cp");
				$("#opt").html(
						'<div id="popup_cancel" class="pbtn" tabindex="0"><span>' + $.alerts.cancelButton
								+ '</span></div><div id="popup_ok" class="pbtn" tabindex="0"><span>'
								+ $.alerts.okButton + '</span></div>');
				$("#popup_ok, #popup_cancel").mouseover(function() {
					$(this).attr("class", "pbtn btn_hover");
				});
				$("#popup_ok, #popup_cancel").mouseout(function() {
					$(this).attr("class", "pbtn");
				});
				$("#popup_ok, #popup_cancel").mousedown(function() {
					$(this).attr("class", "pbtn btn_active");
				});
				$("#popup_ok, #popup_cancel").mouseup(function() {
					$(this).attr("class", "pbtn");
				});
				$("#popup_ok").click(function() {
					var val = $("#popup_message");
					$.alerts._hide();
					if (callback)
						callback(val);
				});
				$("#popup_cancel").click(function() {
					$.alerts._hide();
					if (callback)
						callback(null);
				});
				$("#popup_ok, #popup_cancel").keypress(function(e) {
					if (e.keyCode == 13)
						$("#popup_ok").trigger('click');
					if (e.keyCode == 27)
						$("#popup_cancel").trigger('click');
				});
				break;
			case 'html':
				$("#popup_message_box b").remove();
				$("#popup_opt_box").remove();
				break;
			}
			if ($.alerts.draggable) {
				try {
					$("#popup_container").easydrag();
					$("#popup_container").setHandler('popup_title');
					$("#popup_title").css({
						cursor : 'move'
					});
				} catch (e) {
				}
			}
		},
		_hide : function() {
			$("#popup_container").animate({
				height : 'hide',
				opacity : 'hide'
			}, 'normal', function() {
				$("#popup_container").hide("slow", function() {
					$("#popup_container").remove();
				});
			})
			$.alerts._overlay('hide');
			$.alerts._maintainPosition(false);
		},
		_overlay : function(status) {
			switch (status) {
			case 'show':
				$.alerts._overlay('hide');
				$("body").append('<div id="popup_overlay"></div>');
				//写后面的遮罩层
				$("#popup_overlay").css({
					position : 'absolute',
					zIndex : 99998,
					top : '0px',
					left : '0px',
					width : '100%',
					height : $(document).height(),
					background : $.alerts.overlayColor,
					opacity : $.alerts.overlayOpacity
				});
				break;
			case 'hide':
				$("#popup_overlay").remove();
				break;
			}
		},
		_reposition : function() {
			var top = (($(window).height() / 2) - ($("#popup_container").outerHeight() / 2)) + $.alerts.verticalOffset;
			var left = (($(window).width() / 2) - ($("#popup_container").outerWidth() / 2)) + $.alerts.horizontalOffset;
			if (top < 0)
				top = 0;
			if (left < 0)
				left = 0;
			top = top + $(window).scrollTop();
			$("#popup_container").css({
				top : '85px',
				//               top: top + 'px',
				left : left + 'px'
			});
			$("#popup_container").css("border-radius", "10px");
			$("#popup_overlay").height($(document).height());
		},
		_maintainPosition : function(status) {
			if ($.alerts.repositionOnResize) {
				switch (status) {
				case true:
					$(window).bind('resize', $.alerts._reposition);
					break;
				case false:
					$(window).unbind('resize', $.alerts._reposition);
					break;
				}
			}
		}
	}
	jAlert = function(message, title, callback, time) {
		$.alerts.alert(message, title, callback, time);
	}
	jConfirm = function(message, title, callback) {
		$.alerts.confirm(message, title, callback);
	};
	jPrompt = function(message, title, value, callback) {
		$.alerts.prompt(message, title, value, callback);
	};
	jIframe = function(url, title, width, height) {
		$.alerts.iframe(url, title, width, height);
	};
	jChoose = function(html, title, width, height, callback) {
		$.alerts.choose(html, title, width, height, callback);
	};
	jHtml = function(html, title, width, height) {
		$.alerts.html(html, title, width, height);
	};
})(jQuery);
//css样式在main.css里面