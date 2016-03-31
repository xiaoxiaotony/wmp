<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div title="地面专网运行状况月统计" data-options="closable:true">
	<!-- 查询面板 -->
	<div class="search_bar" id="networkTroubleStatistics_search_bar_div"></div>
	<!-- 工具栏 -->
	<div class="cont_tit" id="networkTroubleStatistics_button_bar_div"></div>
	<!--table-->
	<div class="table_cont">
		<table id="networkTroubleStatistics_list_data" cellspacing="0" cellpadding="0"
			width="100%">
			<thead>
				<tr>
					<th field="name" width="20%" data-options="align:'center'" >设备名称</th>
					<th field="monthDate" width="20%" data-options="align:'center'" >时间</th>
					<th field="areaname" width="20%" data-options="align:'center'" >设备区域</th>
					<th field="status" width="15%" formatter="oper_status" data-options="align:'center'" >状态</th>
					<th field="num_count" width="15%" data-options="align:'center'" >次数</th>
					<th field="ip" width="10%" data-options="align:'center'" formatter="oper_opertion">操作</th>
				</tr>
			</thead>
		</table>
	</div>
	<!--底部分页 end-->
</div>
<script>
var queryParams = {
	queryId : "networkTroubleStatistics_search_bar_div",
	body : [ {
		column : 'device',
		name : '设备：',
		type : "text"
	}, {
		column : ['monthDate'],
		name : '时间：',
		type : "date",
		dateFormat:'yyyy-MM'
	}, {
		column : 'area',
		name : '区域：',
		type : "select",
		valueField:'MUNCPL_ID',
        textField:'MUNCPL',
		url : contentPath+"/commonHander/getStationDIC"
	}],
	queryDataGrid : "networkTroubleStatistics_list_data"
}

/**
 * 创建查询面板
 */
DataGrid.initQueryPanel(queryParams);

var toolbarParams = {
	boolbarId : "networkTroubleStatistics_button_bar_div",
	title : "设备状态统计",
	body :[{
		icon : "icon-plus",
		text : "导出",
		event : function(){
            exportExcel();
		}
	}]
}
/**
 * 查询工具条面板
 */
DataGrid.initToolsBarPanel(toolbarParams);

function oper_opertion(val,row){
	if(row.count == 0){
		return "";
	}
	return "<a href='javascript:void(0);' onclick='show_detail(\""+val+"\",\""+row.name+"\")'>查看详情</a>";
}

function oper_status(val,row){
	if("故障"==val){
		return "<font style='color:red;'>"+val+"</font>";
	}
	return val;
}

function show_detail(id,name){
	Util.window(name+"设备异常详情", "dialog_content", "../pages/view/netmonitor/networkTroubleStatistics/showNetworkTroubleDetail.jsp?device_id="+id, 600, 600);
}

$('#networkTroubleStatistics_list_data').datagrid({ 
    nowrap: false, 
    striped: true, 
    border: true, 
    autoRowHeight:false,
    collapsible:false,//是否可折叠的 
    loadMsg:'数据装载中......',
    fit: true,//自动大小 
    pageSize: 20,//每页显示的记录条数，默认为10 
    pageList: [20,30,50,100],//可以设置每页记录条数的列表 
    url:contentPath+"/netmonitorHander/getTroubleStatisticsPageList", 
    remoteSort:false,  
    pagination:true,//分页控件 
    rownumbers:false//行号 
}); 

function exportExcel(){
	window.location.href = contentPath+"/netmonitorHander/exportExcel";
}
</script>
