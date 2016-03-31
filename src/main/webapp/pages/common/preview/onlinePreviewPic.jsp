<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<% 
String pathname = request.getParameter("pathname");
String filename = request.getParameter("filename");
String contextPath = request.getContextPath();
%>

<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal"
		aria-hidden="true">&times;</button>
	<h4 class="modal-title" id="myModalLabel"><%=filename %></h4>
</div>

<div class="modal-body" style="padding: 1px 1px 1px 1px;">
	<div style="position: relative;">
		<img id="viewerPlaceHolder" src="<%=pathname %>"
			style="display: block"></img>
	</div>
</div>