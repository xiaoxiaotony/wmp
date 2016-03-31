<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div title="运维值班班次统计" data-options="closable:true">
	<!-- 查询面板 -->
	<div class="search_bar" id="dutyStatistics_search_bar_div"></div>
		<!-- 工具栏 -->
	<div class="cont_tit" id="dutyStatistics_button_bar_user_div"></div>
	
	<!--table-->
	<div class="table_cont">
		<table id="dutyStatistics_list_data">
			<thead>
				<tr>
					<th field="name" width="26%" sortable="true">单位</th>
					<th field="username" width="25%">用户名</th>
					<th field="dates" width="25%">时间</th>
					<th field="nums" width="25%">班次总数</th>
				</tr>
			</thead>
		</table>
	</div>
	<!--底部分页 end-->
</div>
<script>

var queryParams = {
	queryId : "dutyStatistics_search_bar_div",
	body : [{
		column : 'types',
		type : "tabs",
		listData:"[{\"value\":\"m\",\"label\":\"月度统计\"},{\"value\":\"q\",\"label\":\"季度统计\"},{\"value\":\"y\",\"label\":\"年度统计\"}]"
	},{
		column : ['dates'],
		name : '日期：',
		type : "date",
		dateFormat:'yyyy-MM'
	}],
	queryDataGrid : "dutyStatistics_list_data"
}

/**
 * 创建查询面板
 */
DataGrid.initQueryPanel(queryParams);

var toolbarParams = {
		boolbarId : "dutyStatistics_button_bar_user_div",
		title : "值班班次统计",
		body :[]
	}
/**
 * 添加操作工具面板
 */
DataGrid.initToolsBarPanel(toolbarParams);



/**
 * start format
 */
function formatGroup(val,row){
	if(val.groupName){
		return val.groupName;
	}
	return "";
}
function formatResult(val,row){
	if(val == 0){
		return "禁用";
	}
	return "启用";
}

/**
 * 初始化表格数据
 */
$('#dutyStatistics_list_data').datagrid({
    nowrap: false,  //一行显示数据，提高加载性能true
    striped: true,  //隔行换色
    collapsible:false,//是否可折叠的 
    fit: true,//height 100%
    url:contentPath+"/dutyHander/queryDutyStatistics", 
    remoteSort:false, //要排序的数据从服务器定义 
	pageSize: 25,//每页显示的记录条数，默认为10 
    pageList: [25,30,50,100],//可以设置每页记录条数的列表 
    pagination:true,//分页控件 
    rownumbers:false
});

//提示消息弹窗
function topCenter(messges,dialog_size){
	if(!dialog_size) dialog_size=0;
	$.messager.show({
		title:'消息',
		msg:messges,
		showType:'slide',
		style:{
				right:'',
				top:document.body.scrollTop+document.documentElement.scrollTop-dialog_size,
				bottom:''
		}
	});
}


//设置分页控件 
var p = $('#dutyStatistics_list_data').datagrid('getPager');
$(p).pagination({ 
    beforePageText: '第',//页数文本框前显示的汉字 
    afterPageText: '页    共 {pages} 页', 
    displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
});
</script>
<style>
.cont_button{
	float: left;
	margin-left: 25px;
}
</style>
