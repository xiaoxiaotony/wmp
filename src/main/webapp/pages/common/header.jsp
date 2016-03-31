<%@page import="com.txy.common.util.StringUtils"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.txy.web.common.bean.UserInfoBean"%>
<%
	String contextPath = request.getContextPath();
	String isLoginBrower = request.getAttribute("isLoginBrower").toString();
	//默认是系统管理员
	String longinName = "admin";
	String userId = "1";
	if (request.getSession().getAttribute("user") != null) {
		UserInfoBean userBean = (UserInfoBean) session.getAttribute("user");
		longinName = userBean.getName();
		userId = userBean.getId();
	}
%>
<!--头部背景-->
<div class="header_bg">
</div>
<!--头部背景 end-->

<!--项目标识-->
<div class="logo">
</div>

<!--项目标识-->

<!--登陆信息-->
<div class="user">
    <i id="icon-user" class="icon-user"></i>欢迎您：<%=longinName %>
    <!-- <a href="javascript:void(0)" onclick="lookMessage()" class="user_comm"> --></span>
    <a href="javascript:void(0)" id="update_pwd" class="user_exit" style="color:white">修改密码</a>
    <a href="javascript:void(0)" id="logout" class="user_exit" style="display:inline-block;height:22px;padding:0 3px;  background: #F9E183; color: #FF5700; border-radius: 3px; line-height: 22px;">注销系统</a>
</div>
<!--登陆信息 end-->

<!--主导航-->
<div class="nav01">
</div>
<!--主导航end-->

<div id="common_plugin_div" style="display: none;"></div>
<script type="text/javascript">
var userId = '<%=userId%>';
var isLoginBrower='<%=isLoginBrower%>';
$(function(){
	//查询用户消息数
	if(0==isLoginBrower){
		//var url = contentPath+"/systemInfoHandler/getUserInfoMessageCount";
		var url = contentPath+"/subMessageHander/getNotReadMessage";
		Util.getAjaxData(url, null, function(data){
			if(data.data != '0'){
				$(".user .user_comm").html(data.data);
			}else{
				$(".user .user_comm").css({"display":"none"});
			}
		}, false);
	}else{ //非登录的情况
		$(".user .user_comm").css({"display":"none"});
	}
	
	//加载第一排URL
	var treePanel = {
		dataUrl : contentPath+"/menuInfoHandler/initHeaderMenuInfoList",
		param : "node=1"
	};
	commonTree.loadHeader(treePanel);
	
	$("#logout").on("click",function(){
		jConfirm("您确定要退出系统么？","提示",function(res){
			if(res){
				//清空cookies
				$.cookie('clickUrl',"");
				$.cookie('clickId', "");
				Util.getAjaxData(contentPath+"/userInfoHandler/logout", null, function(data){
					window.location.href = "../login.jsp";
				})
			}
		});
	});
	
	$("#update_pwd").on("click",function(){
		Util.window("修改密码", "dialog_content", "../pages/common/message/updatePwd.jsp", 450, 300);
	});
	
	//判断用户是否在线
	if("<%=userId%>" !== "1"){
		check_user();
		setInterval("check_user()",60*1000);
	}
});


//判断用户在不在线
var check_bln = "";
function check_user(){
	var url = contentPath+"/monitorHander/check_user";
     $.get(url,function(data){
    	  if(check_bln === data.data) return;
    	  check_bln = data.data;
    	  if(check_bln){
    		    $("#icon-user").css("color","#00ff00");
    			$('#icon-user').tooltip({
    				position: 'bottom',
    				content: '订阅方在线',
    				onShow: function(){
    					$(this).tooltip('tip').css({
    						backgroundColor: '#fff000',
    						borderColor: '#008000',
    						boxShadow: '1px 1px 3px #292929'
    					});
    				}
    			});
    	  }else{
  		    $("#icon-user").css("color","red");
			$('#icon-user').tooltip({
				position: 'bottom',
				content: '订阅方不在线',
				onShow: function(){
					$(this).tooltip('tip').css({
						backgroundColor: '#fff000',
						borderColor: '#ff0000',
						boxShadow: '1px 1px 3px #292929'
					});
				}
			});
    	  }
     });
}

function lookMessage(){
	$(".nav01 ul li").each(function(i){
		if($(this).attr("id") == 10){
			$(this).trigger("click");
			$(".user .user_comm").css({"display":"none"});
		}
	});
}
</script>