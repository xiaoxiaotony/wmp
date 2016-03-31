<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<% 
	String id = request.getParameter("id"); 
%>
<div title="值班信息" data-options="closable:true">
	<!-- 查询面板 -->
	<div class="search_bar" id="dutyDetail_search_bar_div"></div>
	<!--table-->
	<div class="table_cont">
		<table id="dutyDetail_list_data">
			<thead>
				<tr>
					<th field="item_name" width="10%" sortable="true">项名称</th>
					<th field="item_explain" width="15%">项说明</th>
					<th field="detail_name" width="28%">详细项名称</th>
					<th field="am_time" width="15%">上午时间</th>
					<th field="pm_time" width="15%">下午时间</th>
					<th field="ne_time" width="15%">晚上时间</th>
				</tr>
			</thead>
		</table>
	</div>	
	<!--底部分页 end-->
</div>
<script>
var id = "<%=id%>";
var queryParams = {
	queryId : "dutyDetail_search_bar_div",
	body : [{
		column : 'detailName',
		name : '详细名称',
		type : "text",
		placeholder : "详细名称"
	} ],
	queryDataGrid : "dutyDetail_list_data"
}

/**
 * 创建查询面板
 */
DataGrid.initQueryPanel(queryParams);



/**
 * 初始化表格数据
 */
$('#dutyDetail_list_data').datagrid({
    nowrap: false,  //一行显示数据，提高加载性能true
    striped: true,  //隔行换色
    collapsible:false,//是否可折叠的 
    fit: true,//height 100%
    url:contentPath+"/dutyHander/getDutyDetails?id="+id, 
    remoteSort:false, //要排序的数据从服务器定义 
	pageSize: 25,//每页显示的记录条数，默认为10 
    pageList: [10,25,30,50,100],//可以设置每页记录条数的列表 
    pagination:true,//分页控件 
    rownumbers:true
});


//设置分页控件 
var p = $('#dutyInfo_list_data').datagrid('getPager');
$(p).pagination({ 
    beforePageText: '第',//页数文本框前显示的汉字 
    afterPageText: '页    共 {pages} 页', 
    displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
});

</script>
