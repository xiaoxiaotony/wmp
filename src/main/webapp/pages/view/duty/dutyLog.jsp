<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div title="值班列表" data-options="closable:true">
	<!-- 查询面板 -->
	<div class="search_bar" id="dutyLog_search_bar_div"></div>
	<!--table-->
	<div class="table_cont">
		<table id="dutyLog_list_data">
			<thead>
				<tr>
					<th field="duty_date" width="15%" sortable="true">值班开始时间</th>
					<th field="duty_end_date" width="15%" sortable="true">值班结束时间</th>
					<th field="duty_person" width="15%">值班人员</th>
					 <th field="type" formatter="format_type" width="15%">值班类型</th>
					<th field="duty_status" formatter="format_status" width="15%">状态</th>
					<th field="id" width="10%" data-options="align:'center'" formatter="queryDetails">操作</th>
				</tr>
			</thead>
		</table>
	</div>	
	<!--底部分页 end-->
</div>
<script>

var queryParams = {
	queryId : "dutyLog_search_bar_div",
	body : [{
		column : 'searchKey',
		name : '关键字:',
		type : "text",
		placeholder : "关键字"
		}],
	queryDataGrid : "dutyLog_list_data"
}

/**
 * 创建查询面板
 */
DataGrid.initQueryPanel(queryParams);



/**
 * 初始化表格数据
 */
$('#dutyLog_list_data').datagrid({
    nowrap: false,  //一行显示数据，提高加载性能true
    striped: true,  //隔行换色
    collapsible:false,//是否可折叠的 
    fit: true,//height 100%
    url:contentPath+"/dutyHander/getDutyList", 
    remoteSort:false, //要排序的数据从服务器定义 
	pageSize: 25,//每页显示的记录条数，默认为10 
    pageList: [10,25,30,50,100],//可以设置每页记录条数的列表 
    pagination:true,//分页控件 
    rownumbers:false
});


//设置分页控件 
var p = $('#dutyLog_list_data').datagrid('getPager');
$(p).pagination({ 
    beforePageText: '第',//页数文本框前显示的汉字 
    afterPageText: '页    共 {pages} 页', 
    displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
});
function format_type(val,row){
	return val == "0" ? "网络管理" : "气象通讯传输";
}
function format_status(val,row){
	return val == "0" ? "值班中" : "值班结束";
}

function queryDetails(val,row){
	return "<a href='javascript:void(0);' onclick='query_details(\""+val+"\",\""+row.type+"\")'>详情</a>";
}

function query_details(id,type){
	var url = "";
	if (type == "0")
	{
		url = "view/duty/dutyNetLogDetail.jsp?id="+id
	}
	else
	{
		url = "view/duty/dutyWetherLogDetail.jsp?id="+id
	}
	$('#dialog_content').dialog({
		title: '值班详情',
		width: 900,
		height: 600,
		closed: false,
		cache: false,
		href: url,
		modal: true
	});
}

</script>
