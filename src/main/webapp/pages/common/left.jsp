<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!--左边菜单-->
<div class="left_cont" id="leftMenu">
</div>
<div class="left_menu_jt" id="left_menu_jt"><i class="icon-chevron-up"></i></div>
<!--左边菜单 end-->
<script type="text/javascript">
$(function(){
	$(".nav01 ul li").on("click",function(){
		//选中点击样式
		$(this).parent().find("li").removeClass("on");
		$(this).addClass("on");
		var id = $(this).attr("id");
		var url = contentPath+"/menuInfoHandler/initLeftList";
		var leftStr = "<ul class='nav02'>";
		Util.getAjaxData(url, "id="+id, function(data){
			//设置选择的值到Cookies
			$.cookie('clickId', id);
			for(var i = 0; i<data.length;i++){
				if(data[i].children.length>0){
					leftStr+="<li><span class='nav02_lie'><i><img src='../resources/electric_skin/icons/"+data[i].icon+"'/></i><font>"+data[i].name+"</font></span>";
					leftStr+="<ul class='nav03'>";
					var children = data[i].children;
					for(var j= 0; j<children.length;j++){
						leftStr+="<li><span class='nav03_lie'><i><img style='margin-right:7px' src='../resources/electric_skin/icons/"+children[j].icon+"'/></i><font>"+children[j].name+"</font><a href='javascript:void(0);' url='"+children[j].url+"'></a></span></li>";
					}
					leftStr+="</ul>";
				}else{
					leftStr+="<li><span class='nav02_lie'><i><img src='../resources/electric_skin/icons/"+data[i].icon+"'/></i><font>"+data[i].name+"</font><a href='javascript:void(0);' url='"+data[i].url+"'></a></span>";
				}
				leftStr+="</li>";
			}
			leftStr += "</ul>"
			//清空左边树数据
			$("#leftMenu").empty().append(leftStr);
			
			$("#leftMenu ul li").on("click",function(){
				//左边菜单伸缩
				$(this).siblings("li").removeClass("on");
		        $(this).addClass("on");
		        
				if($(this).find("ul").length == 0){
					var target = $(this).find("a").attr("url");
					//设置选择的值到Cookies
					$.cookie('clickUrl', target);
					if(Util.empty(target)){
						jAlert('此模块正在开发中...');
						return false;
					}
					addTab($(this).find("font").html(),target);
					//实现选中效果
					if(!$(this).hasClass("on")){
						$(this).removeClass("on");
					}else{
						$(this).addClass("on");
					}
				}
			});
			
			//当点击header里面的菜单时默认选中左边菜单的第一个
			var url = $.cookie('clickUrl'),flag=false;
			if(url && null!=url){
				$("#leftMenu ul li").each(function(i){
					if($(this).find("a").attr("url") == url){
						if($("#leftMenu .nav02").length != 0){
							$("#leftMenu .nav02 li").first().trigger("click").parents("li").addClass("on");
						}else{
							$(this).trigger("click").addClass("on");
						}
						flag = false;
						return false;
					}else{
						flag=true;
					}
				});
				//如果刷新后没有与cookie对应的值
				if(flag){
					if($("#leftMenu .nav02").length != 0){
						$("#leftMenu .nav02 li").first().trigger("click").parents("li").addClass("on");
					}else{
						$("#leftMenu ul li").first().trigger("click").parents("li").addClass("on");
					}
				}
			}else{
				//初始化选中第一个
				$("#leftMenu ul li").first().trigger("click").parents("li").addClass("on");
			}
		}, false)
	});
	
	if($.cookie('clickId')){
		var id = $.cookie('clickId'),flagHead=false;
		$(".nav01 ul li").each(function(i){
			//如果刷新后没有与cookie对应的值
			if($(this).attr("id") === id){
				$(this).trigger("click");
				flagHead=false;
				return false;
			}else{
				flagHead=true;
			}
		});
		//如果刷新后没有与cookie对应的值
		if(flagHead){
			$(".nav01 ul li").first().trigger("click");
		}
	}else{
		//初始化选中第一个
		$(".nav01 ul li").first().trigger("click");
	}
})
</script>
