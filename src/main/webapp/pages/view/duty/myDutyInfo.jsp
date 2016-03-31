<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div title="值班信息" data-options="closable:true">
	<!-- 查询面板 -->
	<div class="search_bar" id="mydutyInfo_search_bar_div"></div>
	<!--table-->
	<div class="table_cont">
		<table id="mydutyInfo_list_data">
			<thead>
				<tr>
					<th field="duty_date" width="15%" sortable="true" formatter="format_time">值班时间</th>
					<th field="duty_person" width="15%">值班人员</th>
					<th field="duty_fault_reason" width="30%">故障原因及解决方法</th>
					<th field="duty_remark" width="30%">备注及交代事宜</th>
					<th field="id" width="10%" data-options="align:'center'" formatter="querymyDetails">操作</th>
				</tr>
			</thead>
		</table>
	</div>	
	<!--底部分页 end-->
</div>
<script>

var queryParams = {
	queryId : "mydutyInfo_search_bar_div",
	body : [{
			column : ['startDate'],
			name : '开始时间:',
			type : "date",
			dateFormat:'yyyy-MM-dd'
		},{
			column : ['endDate'],
			name : '结束时间:',
			type : "date",
			dateFormat:'yyyy-MM-dd'
		} ],
	queryDataGrid : "mydutyInfo_list_data"
}

/**
 * 创建查询面板
 */
DataGrid.initQueryPanel(queryParams);



/**
 * 初始化表格数据
 */
$('#mydutyInfo_list_data').datagrid({
    nowrap: false,  //一行显示数据，提高加载性能true
    striped: true,  //隔行换色
    collapsible:false,//是否可折叠的 
    fit: true,//height 100%
    url:contentPath+"/dutyHander/getDutyInfos?isMyDuty=true", 
    remoteSort:false, //要排序的数据从服务器定义 
	pageSize: 25,//每页显示的记录条数，默认为10 
    pageList: [10,25,30,50,100],//可以设置每页记录条数的列表 
    pagination:true,//分页控件 
    rownumbers:false
});


//设置分页控件 
var p = $('#mydutyInfo_list_data').datagrid('getPager');
$(p).pagination({ 
    beforePageText: '第',//页数文本框前显示的汉字 
    afterPageText: '页    共 {pages} 页', 
    displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
});

function format_time(val,row){
	return val.split(" ")[0];
}

function querymyDetails(val,row){
	return "<a href='javascript:void(0);' onclick='query_details(\""+val+"\")'>详情</a>";
}

function query_details(id){
	$('#dialog_content').dialog({
		title: '值班详情',
		width: 1000,
		height: 600,
		closed: false,
		cache: false,
		href: 'view/duty/dutyDetail.jsp?id='+id,
		modal: true
	});
}
</script>
