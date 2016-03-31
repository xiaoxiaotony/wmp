<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div title="运维值班记录检索" data-options="closable:true">
	<!-- 查询面板 -->
	<div class="search_bar" id="dutyRecord_search_bar_div"></div>
	
	<!--table-->
	<div class="table_cont">
		<table id="dutyRecord_list_data">
			<thead>
				<tr>
					<th field="name" width="20%" sortable="true">单位</th>
					<th field="username" width="10%">用户名</th>
					<th field="duty_date" width="20%">班次</th>
					<th field="startDate" width="20%">上班时间</th>
					<th field="endDate" width="20%">下班时间</th>
					<th field="id" width="10%" data-options="align:'center'" formatter="oper_log">操作</th>
				</tr>
			</thead>
		</table>
	</div>	
	<!--底部分页 end-->
</div>
<script>

var queryParams = {
	queryId : "dutyRecord_search_bar_div",
	body : [{
		column : 'userId',
		name : '姓名：',
		type : "select",
		valueField:'USERID',
        textField:'USERNAME',
		url : "../http/dutyHander/getDictListVal"
		},{
			column : ['startDate'],
			name : '时间：',
			type : "date",
			dateFormat:'yyyy-MM-dd'
		},{
			column : ['endDate'],
			name : '-',
			type : "date",
			dateFormat:'yyyy-MM-dd'
		} ],
	queryDataGrid : "dutyRecord_list_data"
}

/**
 * 创建查询面板
 */
DataGrid.initQueryPanel(queryParams);

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
$('#dutyRecord_list_data').datagrid({
    nowrap: false,  //一行显示数据，提高加载性能true
    striped: true,  //隔行换色
    collapsible:false,//是否可折叠的 
    fit: true,//height 100%
    url:contentPath+"/dutyHander/getUserDutyRecord", 
    remoteSort:false, //要排序的数据从服务器定义 
	pageSize: 25,//每页显示的记录条数，默认为10 
    pageList: [25,30,50,100],//可以设置每页记录条数的列表 
    pagination:true,//分页控件 
    rownumbers:false
});


//设置分页控件 
var p = $('#dutyRecord_list_data').datagrid('getPager');
$(p).pagination({ 
    beforePageText: '第',//页数文本框前显示的汉字 
    afterPageText: '页    共 {pages} 页', 
    displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
});

function oper_log(val,row){
	
	return "<a href='javascript:void(0);' onclick='update_log(\""+val+"\")'>审核</a>";
	
}

function update_log(id){
	alert(id);
}
</script>
