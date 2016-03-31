<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<% String device_id = request.getParameter("device_id"); %>
		<table id="showNetworkTroubleDetail_list_data">
			<thead>
				<tr>
					<th field="createdate" width="40%">开始时间</th>
					<th field="endTime" width="40%">结束时间</th>
					<th field="length" width="20%">时长</th>
				</tr>
			</thead>
		</table>
<script>
var device_id = '<%=device_id%>';
$('#showNetworkTroubleDetail_list_data').datagrid({
    nowrap: false,  //一行显示数据，提高加载性能true
    striped: true,  //隔行换色
    collapsible:false,//是否可折叠的 
    fit: true,//height 100%
    url:contentPath+"/netmonitorHander/getNetworkTroubleDetail?device_id="+device_id, 
    remoteSort:false, //要排序的数据从服务器定义 
    pagination:true,//分页控件 
    rownumbers:false,//行号 
    pageSize: 20,//每页显示的记录条数，默认为10 
    pageList: [20,50,100,200]
});
</script>