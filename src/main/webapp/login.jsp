<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String isLoginBrower = request.getAttribute("isLoginBrower")
			.toString();
%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<title>西藏省集约化平台</title>
<style type="text/css">
html {
	height: 100%
}

.login_bg {
    position: absolute;
    left: 0;
    top: 0;
    bottom: 0;
    right: 0;
    z-index: 1
}
.login_bg img{
    position: absolute;
    left: 0;
    top: 0;
    right: 0;
    bottom: 0;
    width: 100%
}

body {
    overflow: hidden
}

.login_cont {
    width: 619px;
    height: 310px;
    background: url(images/cont_bg.png) no-repeat;
    z-index: 2;
    margin:0 auto;
    font-size: 12px;
    color: #496561;
    position: absolute
}
.login_cont table{
    margin-top: 100px;
    margin-left: 260px
}
.login_cont td{
    padding-top: 6px
}
.login_cont input[type="text"],.login_cont input[type="password"]{
    width: 200px;
    height: 32px;
    border: 1px solid #94C5DD;
    padding: 0 5px;
    line-height: 32px
}
.login_cont input[type="checkbox"]{
    width: 16px;
    height: 16px;
    vertical-align: -5px
}
.login_cont input[type="image"]{
    margin-right: 10px
}
.login_footer{
    position: fixed;
    width: 100%;
    z-index: 3;
    bottom: 0;
    text-align: center;
    line-height: 30px;
    font-size: 12px;
    color: #005F59;
    display: none
}
</style>

<script type="text/javascript" src="resources/js/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="resources/js/common/security.js"></script>
<script src="resources/js/jquery.cookie.js"></script>

<!-- 非登录状态 -->
<script type="text/javascript">
     var isLoginBrower = '<%=isLoginBrower%>';
	 if (isLoginBrower != 0) {
		$.ajax({
			url : 'http/userInfoHandler/login?number=' + Math.random(),
			type : 'POST',
			data : {
				loginName : encodeURI('admin'),
				loginPassword : encode64('admin')
			},
			dataType : 'json',
			timeout : 30000,
			error : function() {
				alert('请求超时');
			},
			success : function(result) {
				if (result.success) {
					document.location.href = "pages/home.jsp";
				}
			}
		});
	}
</script>

</head>
<body>
	<div class="login_bg">
		<img src="images/login_bg.jpg" />
	</div>
	<div class="login_cont" id="login_cont">
		<table>
			<tr>
				<td>账号：</td>
				<td><input type="text" id="loginUserName"
					onchange="remberUser();" /></td>
			</tr>
			<tr>
				<td>密码：</td>
				<td><input type="password" id="loginPassword" /></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td style="font-size:12px"><div id="errorMsg"
						style="float:right;font-size:12px;color:red"></div>
					<input type="checkbox" id="rememberPwd" />记住密码</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td><input type="image" src="images/login_button.gif"
					onclick="login(this);" /><input type="image"
					src="images/reset_button.gif" /></td>
			</tr>
		</table>
	</div>
	<div class="login_footer">©国家电网公司 版权所有国家电网公司。保留所有权利</div>
	<script>
	
		$(function() {

			$(".login_bg img").css({
				"width" : $(window).width(),
				"height" : $(window).height()
			});

			$("#login_cont").css(
					{
						"left" : $("body").width() / 2
								- $("#login_cont").width() / 2,
						"top" : $(window).height() / 2
								- $("#login_cont").height() / 2 - 30
					});

			//记住密码功能
			$("#rememberPwd").on("click", function() {
				if ($("#rememberPwd").is(':checked') == true) {
					var userName = $("#loginUserName").val();
					var passWord = $("#loginPassword").val();
					$.cookie("rememberPwd", "true", {
						expires : 7
					}); // 存储一个带7天期限的 cookie 
					$.cookie("loginUserName", userName, {
						expires : 7
					}); // 存储一个带7天期限的 cookie 
					$.cookie("loginPassword", passWord, {
						expires : 7
					}); // 存储一个带7天期限的 cookie
					return;
				} else {
					$.cookie("rememberPwd", "false", {
						expires : -1
					});
					$.cookie("loginUserName", '', {
						expires : -1
					});
					$.cookie("loginPassword", '', {
						expires : -1
					});
				}
			})

			if ($.cookie("rememberPwd") == "true") {
				$("#rememberPwd").attr("checked", true);
				$("#loginUserName").val($.cookie("loginUserName"));
				$("#loginPassword").val($.cookie("loginPassword"));
			}
		})

		function remberUser() {
			$("#rememberPwd").attr("checked", false);
		}

		function login(obj) {
			var loginName = document.getElementById("loginUserName").value;
			var loginPassword = document.getElementById("loginPassword").value;
			if (loginName == "" || loginPassword == "") {
				var msg = "用户名或密码不能为空";
				document.getElementById("loginUserName").focus(true, 100);
				return false;
			}
			if (isLoginBrower == 0) {

			}
			$.ajax({
				url : 'http/userInfoHandler/login?number=' + Math.random(),
				type : 'POST',
				data : {
					loginName : encodeURI(loginName),
					loginPassword : encode64(loginPassword)
				},
				dataType : 'json',
				timeout : 30000,
				error : function() {
					alert('请求超时');
				},
				success : function(result) {
					if (result.success) {
						document.location.href = "pages/home.jsp";
					} else {
						$("#errorMsg").html(result.msg);
						document.getElementById("loginUserName").value = "";
						document.getElementById("loginPassword").value = "";
					}
				}
			});
		}

		document.onkeydown = function(event) {
			e = event ? event : (window.event ? window.event : null);
			if (e.keyCode == 13) {
				login(this);
			}
		}
	</script>
</body>
</html>