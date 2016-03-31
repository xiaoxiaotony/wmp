<%@page import="org.apache.poi.hssf.record.chart.BeginRecord"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% 
	String station = request.getParameter("station");
	String dtype = request.getParameter("dtype");
	String ctype = request.getParameter("ctype");
	String datetime = request.getParameter("datetime");
	String dateType = request.getParameter("dateType");
	String dtypename = java.net.URLDecoder.decode(request.getParameter("dtypename"), "utf-8");
	String ctypename = java.net.URLDecoder.decode(request.getParameter("ctypename"), "utf-8");
	String sname = java.net.URLDecoder.decode(request.getParameter("sname"), "utf-8");
%>
<table class="table05" width="100%" id="transferCountInfo_datagrid">
	<thead>
		<tr>
			<th style="width:23%;text-align: center;">时间</th>
			<th style="width:26%;text-align: center;">资料类型</th>
			<th style="width:17%;text-align: center;">应收</th>
			<th style="width:17%;text-align: center;">实收</th>
			<th style="width:20%;text-align: center;">缺收</th>
		</tr>
	</thead>
	<tbody>
		<tr style="text-align: center;">
			<td><%=datetime%></td>
			<td><%=ctypename%></td>
			<td id="YINGSHOU">-----</td>
			<td id="SHISHOU" style="color: green;">-----</td>
			<td id="QUESHOU" style="color: red;">-----</td>
		</tr>
	</tbody>
</table>
<div class="cont_tit"> 
	<b>&nbsp;&nbsp;缺失列表：</b>
</div>
<table width="100%" id = "transferCountQueInfo_datagrid">
	<thead>
		<tr>
			<th data-options="field:'station',width:'30%',align:'center',formatter:alert_st_ft">站号</th>
			<th data-options="field:'sname',width:'30%',align:'center',formatter:alert_stname_ft">站名</th>
			<th data-options="field:'datetime',width:'30%',align:'center'">缺收时次</th>
		</tr>
	</thead>
</table>


<script type="text/javascript">
	$(function(){
		$.get(contentPath+"/transferMonitorCountHandler/getStationQueInfo", {station: "<%=station%>", datetime: "<%=datetime%>",dateType:"<%=dateType %>",dtype:"<%=dtype %>",ctype:"<%=ctype %>"},function(data){
		    console.log(data);
			$("#YINGSHOU").html(data.data.YINGSHOU);
			$("#SHISHOU").html(data.data.SHISHOU);
			$("#QUESHOU").html(data.data.QUESHOU);
			
			$("#transferCountQueInfo_datagrid").datagrid({
				rownumbers:true,
				data:data.data.data_que
			});

		});
	});
	
function alert_stname_ft(val,row){
	return "<%=sname %>";
}
function alert_st_ft(val,row){
	return "<%=station %>";
}
	
</script>