<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div title="SQL监控信息" data-options="closable:true">
	<!-- 工具栏 -->
	<div class="cont_tit" id="msg_button_bar_div"></div>
	<!--table-->
	<div class="table_cont" style="top: 40px;">
		<table id="excuteSql_Info_list_data">
			<thead>
				<tr>
					<th field="ID" width="3%">ID</th>
					<th field="SQL" width="60%">SQL</th>
					<th field="ExecuteCount" width="5%">执行次数</th>
					<th field="MaxTimespan" width="7%">执行时间(毫秒)</th>
					<th field="EffectedRowCount" width="5%">更新行数</th>
					<th field="FetchRowCount" width="5%">读取行数</th>
					<th field="FetchRowCountHistogram" width="5%">读取行分布</th>
					<th field="MaxTimespanOccurTime" width="10%">执行时间</th>
				</tr>
			</thead>
		</table>
	</div>
	<!--底部分页 end-->
</div>
<script>
/**
 * 查询工具条面板
 */
var toolbarParams = {
	boolbarId : "msg_button_bar_div",
	title : "sql执行监控列表",
	body :[{
		icon : "icon-trash",
		text : "清空",
		event : function(){
			clean_excute_sql();
		}
	}]
}
DataGrid.initToolsBarPanel(toolbarParams);

function clean_excute_sql(){
	$.messager.confirm('消息', '你确定要清空？', function(result){
		 if (result){
			 Util.getAjaxData(resourcesPath+"/druid/reset-all.json", null, function(data){
				 var jsonData = JSON.parse(data);
				 if (jsonData.ResultCode == 1) {
						alert("操作成功");
						$('#excuteSql_Info_list_data').datagrid("reload");
					}
			 })
		 }
	 });
}

$('#excuteSql_Info_list_data').datagrid({ 
    nowrap: false, 
    striped: true, 
    border: true, 
    autoRowHeight:false,
    collapsible:false,//是否可折叠的 
    fit: true,//自动大小 
    pageSize: 25,//每页显示的记录条数，默认为10 
    pageList: [25,30,50,100],//可以设置每页记录条数的列表 
    url:resourcesPath+"/druid/sql.json?orderBy=MaxTimespanOccurTime&orderType=asc&page=1&perPageCount=100000", 
    remoteSort:false,
    pagination:false,//分页控件 
    rownumbers:false,//行号 
    loadFilter: function(data){
    	if(data.Content == null){
    		data.Content = [];
    	}
    	return {rows: data.Content};
	}
}); 
</script>