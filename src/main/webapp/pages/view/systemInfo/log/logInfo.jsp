<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div title="日志信息" data-options="closable:true">
	<!-- 查询面板 -->
	<div class="search_bar" id="log_search_bar_div"></div>
	<!-- 工具栏 -->
	<div class="cont_tit" id="log_button_bar_div"></div>
	<!--table-->
	<div class="table_cont">
		<table id="logInfo_list_data" cellspacing="0" cellpadding="0"
			width="100%">
			<thead>
				<tr>
					<th field="opreate" width="5%">操作</th>
					<th field="log_type" width="9%" formatter="formatLogType">日志类型</th>
					<th field="record_time" width="15%" sortable="true" >记录时间</th>
					<th field="oper_user" width="8%">操作者</th>
					<th field="result" width="5%" formatter="formatResult">结果</th>
					<th field="param" width="47%">参数</th>
					<th field="request_ip" width="10%">请求IP</th>
				</tr>
			</thead>
		</table>
	</div>
	<!--底部分页 end-->
</div>
<script>
var queryParams = {
	queryId : "log_search_bar_div",
	body : [ {
		column : 'logType',
		name : '日志类型：',
		type : "select",
		listData : "[{\"value\":\"0\",\"label\":\"业务日志\"},{\"value\":\"1\",\"label\":\"操作日志\"}]"
	}, {
		column : 'logStatus',
		name : '结果：',
		type : "select",
		listData : "[{\"value\":\"0\",\"label\":\"成功\"},{\"value\":\"1\",\"label\":\"失败\"}]"
	}, {
		column : 'oper_user',
		name : '操作者：',
		type : "text"
	}, {
		column : ['startTime','endTime'],
		name : '记录时间：',
		type : "date"
	} ],
	queryDataGrid : "logInfo_list_data"
}

/**
 * 创建查询面板
 */
DataGrid.initQueryPanel(queryParams);

var toolbarParams = {
	boolbarId : "log_button_bar_div",
	title : "日志信息列表",
	body :[{
		icon : "icon-trash",
		text : "清空",
		event : function(){
			clean_log();
		}
	}]
}
/**
 * 查询工具条面板
 */
DataGrid.initToolsBarPanel(toolbarParams);

function formatResult(val,row){
	if(val == 0){
		return "成功";
	}
	return "失败";
}

function formatLogType(val,row){
	if(val == 0){
		return "业务日志";
	}
	return "操作日志";
}

$('#logInfo_list_data').datagrid({ 
    nowrap: false, 
    striped: true, 
    border: true, 
    autoRowHeight:false,
    collapsible:false,//是否可折叠的 
    loadMsg:'数据装载中......',
    fit: true,//自动大小 
    pageSize: 25,//每页显示的记录条数，默认为10 
    pageList: [25,30,50,100],//可以设置每页记录条数的列表 
    url:contentPath+"/systemInfoHandler/logInfoList", 
    remoteSort:false,  
    pagination:true,//分页控件 
    rownumbers:true//行号 
    //frozenColumns:[[ 
      // {field:'ck',checkbox:true} 
    //]]
}); 


function clean_log(){
	$.messager.confirm('提示', '你确认清空所有日志？', function(result){
		if (result){
			var url = contentPath+"/systemInfoHandler/cleanLog";
			Util.getAjaxData(url, null, function(data){
				if(data){
					Util.showMsg("操作成功");
					$("#logInfo_list_data").datagrid("reload");
				}else{
					Util.showMsg("操作失败");
				}
			})
		}
	});
}

//设置分页控件 
var p = $('#logInfo_list_data').datagrid('getPager');
$(p).pagination({ 
    beforePageText: '第',//页数文本框前显示的汉字 
    afterPageText: '页    共 {pages} 页',
    displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
}); 
</script>
