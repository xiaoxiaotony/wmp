var i = 0;
(function($){
	$.fn.extend({
		submitForm : function(url,msgStr,callFun,data){
			var param = "";
			if(!data){
				param = "";
			}else{
				param = data.toString();
			}
			var options = {
				url : url,
				type : 'POST',
				data : {
    				selectData : param
    			},
				success : function(data) {
					if(data.success){
						if(!Util.empty(msgStr)&&""!=msgStr){
							jAlert(msgStr+"成功");
						}
						callFun(data);
					}else{
						jAlert(data.msg);
					}
				},
				error : function(XMLHttpRequest) {
					jAlert('请求超时');
				}
			};
			$(this).ajaxSubmit(options);
		},
		loadSelect:function(param){
			var id = $(this).attr("id");
			var url = $(this).attr("url");
			var name = $(this).attr("displayText");
			var val = $(this).attr("displayValue");
			$.ajax({
				type : "POST",
				url : "../http"+url,
				success : function(data) {
					var htmlStr = "";
					if(param){
						htmlStr = "<option value=''>---请选择---</option>";
					}
					for(var i = 0; i<data.length; i++)
					{
						htmlStr+="<option value='"+data[i][val]+"'>"+data[i][name]+"</option>";
					}
					$("#"+id).append(htmlStr);
					if(param && param.callFun){
						param.callFun();
					}
				},
				error : function(x) {
					jAlert("请求数据异常");
				}
			});
		},
		"addinput":function(o){ //动态增加input
			o = $.extend({
				clicker : 	"", //开关，点击增加，可以根据class选择，默认.next()获取
				wrap : 		"li", //input的父标签
				name : 		"i-text", //input的name
				type : 		"text", //input的type
				maxlength : 20, //input的maxlength
				className : "", //input的class
				inputMax : 	0 , //新增input的上限，0表示不限制
				delClassName : "remove" //删除按钮的样式
			},o);
			//补全input的父标签
			var wrapStart = "<"+ o.wrap +">", wrapEnd = "</"+ o.wrap +">";
			//input的代码
			var input = "<input  style='width:600px; margin-bottom:5px;' type='"+ o.type +"' name='"+ o.name +i+"' maxlength='"+ o.maxlength +"'"+ (!o.className ? "" : " class='"+ o.className +"'") +" />";
			var content = wrapStart + input  + wrapEnd;
//			var remove = "<a href='#nogo' class='"+o.delClassName+"'>移除</a>";
//			var content = wrapStart + input + remove + wrapEnd;
			//定义一个变量，把容器赋值给它（因为会多次使用）
			var $container = this;
			//初始增加一个input，没有“移除”按钮
			$container.append(wrapStart + input + wrapEnd);
			
			var len;//增加input
			var $Ctrl = !o.clicker ? $container.next() : $(o.clicker);
			$Ctrl.bind("click", function(){
				var $item = $container.children(o.wrap);
				len = $item.length;//未增加前的input个数
				//设置一个开关，当开关为TRUE时才允许增加
				var isMax = o.inputMax==0 ? false : (len<o.inputMax ? false : true);
				if(!isMax){
					i++
					var input = "<input  style='width:600px; margin-bottom:5px;' type='"+ o.type +"' name='"+ o.name +i+"' maxlength='"+ o.maxlength +"'"+ (!o.className ? "" : " class='"+ o.className +"'") +" />";
					var content = wrapStart + input + wrapEnd;
					//var remove = "<a href='#nogo' class='"+o.delClassName+"'>移除</a>";
					//var content = wrapStart + input + remove + wrapEnd;
					$(content).appendTo($container).find("."+o.delClassName).click(function(){
						i--;
//						$(this).parent().remove();//移除当前input
//						len = $container.children(o.wrap).length; //统计剩下的个数
//						if(len==1) {
//							$container.find("."+o.delClassName).remove(); //只剩一个的时候，把“移除”按钮干掉
//						}
						return false;
					});
//					if(len==1) {
//						$(remove).appendTo( $item ).click(function(){
//							$(this).parent().remove();//移除当前input
//							len = $container.children(o.wrap).length; //统计剩下的个数
//							if(len==1) {
//								$container.find("."+o.delClassName).remove(); //只剩一个的时候，把“移除”按钮干掉
//							}
//							return false;
//						});//此时的len还是未增加前的input个数，增加第二个的时候的同时，为第一个input增加“移除”按钮。放在此处，可以防止inputMax为负数等情况的错误
//					}
				}
				return false;
			});
		}
	});
})(jQuery);