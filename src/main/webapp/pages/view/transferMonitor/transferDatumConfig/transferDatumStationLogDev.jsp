<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% 
	String dev_id = request.getParameter("dev_id");
%>
<div style="width: 100%;height: 100%;">
	<div style="float: right;height: 100%;width: 80%;">
       <textarea  id = "ftplog_info" style="height: 97%;width: 99%;"></textarea>
	</div>
	<div style="border: 1px solid black;height: 100%;width: 20%;text-align: center; ">
		<table id = "transferDatumStationLogDev_table" width="100%" class="table05">
			<thead hidden="hidden">
				<tr>
					<th field='fileName' width='112%' data-options="align:'center'">日志文件</th>
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
	</div>
</div>


<script>
$(function(){
//     alert('<%=dev_id%>');
// 	$.ajax({
// 		type: "POST",
// 		url: contentPath+"/transferDatumMonitorHander/getFTPName",
// 		data: {dev_id:'<%=dev_id%>'},
// 			success : function(data) {
// 				$("#transferDatumStationLogDev_table").datagrid({
// 					singleSelect : true,
// 					data : data,
// 					onSelect : function(index, row) {
// 						$.post(contentPath+"/transferDatumMonitorHander/getFTPInfo",{filePath:row.filePath},function(data){
// 							$("#ftplog_info").html(data.data);
// 						});
// 					}
// 				});
// 				$("#transferDatumStationLogDev_table").datagrid('selectRow', 0);
// 			}
// 		});
});
</script>