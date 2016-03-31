<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<title>西藏省集约化平台</title>
	<jsp:include page="common/commonHead.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="common/header.jsp"></jsp:include>
	<jsp:include page="common/left.jsp"></jsp:include>
    <!--显示内容区域-->
    <div class="b_cont">
       <div id="content_div_id" class="easyui-tabs" fit="true">
       </div>
    </div>
    
    <!-- dialog 弹窗 -->
    <div id="dialog_content" class="dialog_box"></div>
    
    <!--显示内容区域 end-->
	<jsp:include page="common/foot.jsp"></jsp:include>
</body>
</html>