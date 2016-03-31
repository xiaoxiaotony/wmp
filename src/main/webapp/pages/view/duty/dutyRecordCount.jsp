<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div title="值班记录统计" data-options="closable:true">
	<!-- 查询面板 -->
	<div class="search_bar" id="dutyCount_search_bar_div"></div>
	<!--table-->
	<div class="table_cont">
		<table id="dutyCount_list_data">
			<thead>
				<tr>
					<th field="duty_person" width="20%" sortable="true">值班人</th>
					<th field="count" width="20%" sortable="true">值班次数</th>
					<th field="type" formatter="format_type" width="15%">值班类型</th>
					<th field="duty_date" width="15%">值班月份</th>
					<th field="user_id" width="10%" data-options="align:'center'" formatter="queryDetails">操作</th>
				</tr>
			</thead>
		</table>
	</div>	
	<!--底部分页 end-->
</div>
<script>

var queryParams1 = {
	queryId : "dutyCount_search_bar_div",
	body : [
	{		
			column : ['dutyDate'],
			name : '值班月份:',
			type : "date",
			dateFormat:'yyyy-MM'
	}],
	queryDataGrid : "dutyCount_list_data"
}

/**
 * 创建查询面板
 */
DataGrid.initQueryPanel(queryParams1);



/**
 * 初始化表格数据
 */
$('#dutyCount_list_data').datagrid({
    nowrap: false,  //一行显示数据，提高加载性能true
    striped: true,  //隔行换色
    collapsible:false,//是否可折叠的 
    fit: true,//height 100%
    url:contentPath+"/dutyHander/queryDutyCount", 
    remoteSort:false, //要排序的数据从服务器定义 
	pageSize: 20,//每页显示的记录条数，默认为10 
    pageList: [10,20,30,50,100],//可以设置每页记录条数的列表 
    pagination:true,//分页控件 
    rownumbers:false
});


//设置分页控件 
var p = $('#dutyInfo_list_data').datagrid('getPager');
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
	return "<a href='javascript:void(0);' onclick='queryRecordDetails(\""+val+"\",\""+row.type+"\",\""+row.duty_date+"\")'>详情</a>";
}

function queryRecordDetails(id,type,dutyDate){
	var url = "view/duty/dutyRecordDetail.jsp?userId="+id+"&type="+type+"&dutyDate="+dutyDate;
	$('#dialog_content').dialog({
		title: '值班详情',
		width: 800,
		height: 400,
		closed: false,
		cache: false,
		href: url,
		modal: true
	});
}

</script>
