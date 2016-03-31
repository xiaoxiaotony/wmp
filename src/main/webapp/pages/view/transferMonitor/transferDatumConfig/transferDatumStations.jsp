<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% 
	String station_dic = request.getParameter("station_dic");
	String dtype = request.getParameter("dtype");
	String ctype = request.getParameter("ctype");
	String datetime = request.getParameter("datetime");
	String check_type = request.getParameter("check_type");
	String type = request.getParameter("type");
%>
<div style="width: 100%;height: 100%;">
	<!--table end-->
	<table class="table05" width="100%" id ="transferDatumStationsTable">
		<thead>
			<tr>
				<th data-options="field:'IIIII',width:'20%',align:'center'" sortable="true">站号</th>
				<th data-options="field:'DIC_NAME',width:'20%',align:'center'">站名</th>
				<th data-options="field:'SNAME',width:'20%',align:'center'">区域</th>
                <th data-options="field:'TIME',width:'20%',align:'center'">时次</th>
                <th data-options="field:'FLAG',width:'20%',align:'center'" formatter="result_formart">状态</th>
			</tr>
		</thead>
	</table>
	<!--table end-->
</div>

<script type="text/javascript">

$(function(){
	
	$.ajax({
		   type: "POST",
		   url: contentPath+"/transferDatumMonitorHander/getST",
		   data: {station_dic:'<%=station_dic%>',dtype:'<%=dtype%>',ctype:'<%=ctype%>',datetime:'<%=datetime%>',check_type:'<%=check_type%>',type:'<%=type%>'},
		   success: function(data){
				$("#transferDatumStationsTable").datagrid({
					striped: true,  //隔行换色
					fit: true, //height 100%
					rownumbers:true,
		            data:data
				});
		   }
	});
	
});

function result_formart(val , row){
	if(val == '缺收') return "<span style='color:red'>"+val+"</span>";
	return val;
}

</script>
