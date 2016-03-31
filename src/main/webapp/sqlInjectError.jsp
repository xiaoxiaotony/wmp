<%@ page language="java" import="java.util.*"
	contentType="text/html;charset=utf-8"%>
<% 
String path = request.getContextPath(); 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>防sql注入系统</title>
</head>

<body>
	<h6>
		&nbsp;&nbsp; <font color="red">这个是防sql注入系统，自动过滤您的请求，请更换请求字符串。 </font>
		<%=session.getAttribute("sqlInjectError")%>
	</h6>
</body>
</html>