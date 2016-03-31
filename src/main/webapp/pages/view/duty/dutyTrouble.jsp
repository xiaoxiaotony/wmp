<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<div class="easyui-layout" style="height:100%;overflow: hidden;">
		<div data-options="region:'north'" style="height:60px;">
			<div class="search_bar" id="dutyTrouble_search_bar_div"></div>
		</div>
		<div data-options="region:'center'" style="height:40%;">
			<!--table-->
			<div class="table_cont" style="top:0px;">
				<table id="dutyTrouble_list_data">
					<thead>
						<tr>
							<th field="id" width="20%" sortable="true">id</th>
							<th field="name" width="20%" sortable="true">单位</th>
							<th field="username" width="20%">值班人</th>
							<th field="trouble_type" width="20%">故障类型</th>
							<th field="duty_id" width="20%">班次（序号）</th>
						</tr>
					</thead>
				</table>			
			</div>
		</div>
		<div data-options="region:'south',split:true" style="height:40%;top:0px;">
			<!--日常故障 数据表-->
			<div class="table_cont" style="top:40px;">
				<table id="troubleDetail_list_data">
					<thead>
						<tr>
							<th field="content" width="33%">故障描述</th>
							<th field="opertion_status" width="33%">处理结果</th>
							<th field="record_time" width="34%">记录时间</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
<script>

var queryParams = {
	queryId : "dutyTrouble_search_bar_div",
	body : [{
		column : ['type'],
		name : '故障类型：',
		type:'select',
		valueField:'id',
        textField:'text',
		url : '../pages/view/duty/troubleType.json'
		},{
			column : ['beginDate'],
			name : '开始时间：',
			type : "date",
			dateFormat:'yyyy-MM-dd'
		},{
			column : ['endDate'],
			name : '-结束时间',
			type : "date",
			dateFormat:'yyyy-MM-dd'
		} ],
	queryDataGrid : "dutyTrouble_list_data"
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
$('#dutyTrouble_list_data').datagrid({
    nowrap: false,  //一行显示数据，提高加载性能true
    striped: true,  //隔行换色
    collapsible:false,//是否可折叠的 
    fit: true,//height 100%
    url:contentPath+"/dutyHander/getDutyErrorByType", 
    remoteSort:false, //要排序的数据从服务器定义 
	pageSize: 25,//每页显示的记录条数，默认为10 
    pageList: [25,30,50,100],//可以设置每页记录条数的列表 
    pagination:true,//分页控件 
    rownumbers:false,
    onClickRow:showDetail,
    onLoadSuccess:function(){
    	$(".layout-panel").css("top","0px");
    	$(".table_cont").css("top","0px");
    }
    
});

/**
 * 初始化表格数据
 */
$('#troubleDetail_list_data').datagrid({
    nowrap: false,  //一行显示数据，提高加载性能true
    striped: true,  //隔行换色
    collapsible:false,//是否可折叠的 
    fit: true,//height 100%
    pagination:false,//分页控件 
    rownumbers:false,
});

function showDetail(rowIndex, rowData) {
	$.ajax({
        async: false,
        url: contentPath+"/dutyHander/getDutyErrorDetail?id="+rowData.id,
        type: 'POST',
        dataType: 'json',
        success: function (data) {
        	
        	if($('#troubleDetail_list_data').datagrid('getRows').length>0){
        		$('#troubleDetail_list_data').datagrid('deleteRow',0);
        	}
        	
            $('#troubleDetail_list_data').datagrid('appendRow',data.data.map);
            
        }
    });

	
}

//设置分页控件 
var p = $('#dutyTrouble_list_data').datagrid('getPager');
$(p).pagination({ 
    beforePageText: '第',//页数文本框前显示的汉字 
    afterPageText: '页    共 {pages} 页', 
    displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
});
</script>
