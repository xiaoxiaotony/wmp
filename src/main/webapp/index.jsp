<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
	String isLoginBrower = request.getAttribute("isLoginBrower").toString();
	if(isLoginBrower.equals("0")){
		response.sendRedirect("login.jsp");
	}else{
		//默认是系统管理员
		String longinName = "admin";
		String userId = "1";
		response.sendRedirect("pages/home.jsp");
	}
%>
