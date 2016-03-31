<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div title="资料类型维护" data-options="closable:true">
	<!-- 查询面板 -->
	<div class="search_bar" id="fileMaintenance_search_bar_div"></div>
	<!-- 工具栏 -->
	<div class="cont_tit" id="fileMaintenance_button_bar_user_div"></div>
	<!--table-->
	<div class="table_cont">
		<table id="fileMaintenance_list_data">
			<thead>
				<tr>
					<th field="dtype" width="10%">资料类型</th>
					<th field="ctype" width="18%">资料子类型</th>
					<th field="dtype_desc" width="15%">资料类型描述</th>
					<th field="ctype_desc" width="30%">资料子类型描述</th>
					<th field="flage" width="5%" formatter="oper_flage">是否启用</th>
					<th field="outtime" width="5%">逾限时间</th>
					<th field="misspg" width="5%">缺报百分率</th>
					<!-- <th field="bflag" width="5%">基本文件标志</th>
					<th field="tflag" width="5%">时效统计标志</th>
					<th field="fsjs_time" width="8%">发送及时时间</th>
					<th field="fsyx_time" width="8%">发送逾限时间</th>
					<th field="gflag" width="5%">公报统计标志</th>
					<th field="file_flag" width="5%">文件类型标志</th>
					<th field="bull_flag" width="8%">公报类型标志</th>
					<th field="repo_flag" width="8%">报告类型标志</th>
					<th field="intervaltimes" width="8%">通知间隔</th>
					<th field="start_time" width="8%">起始时间</th>
					<th field="jg_time" width="8%">间隔时间</th>
					<th field="file_threshold_time" width="8%">资料阈限时间</th> -->
					<th field="id" width="10%" data-options="align:'center'" formatter="oper_file">操作</th>
				</tr>
			</thead>
		</table>
	</div>
	<!--底部分页 end-->
</div>
<script>

var queryParams = {
	queryId : "fileMaintenance_search_bar_div",
	body : [{
		column : 'searchKey',
		name : '模糊信息：',
		type : "text",
		placeholder : "关键字"
	} ],
	queryDataGrid : "fileMaintenance_list_data"
}
/**
 * 添加数据类型
 */
function add() {
	$('#dialog_content').dialog({
		title : '值班记录',
		width : 800,
		height : 560,
		closed : false,
		cache : false,
		href : 'view/backInfo/fileMaintenance/addFileMaintenance.jsp',
		modal : true
	});
}

/**
 * 创建查询面板
 */
DataGrid.initQueryPanel(queryParams);

/**
 * start format
 */
 function oper_file(val,row){
	 	return "<a href='javascript:void(0);' onclick='update_file(\""+row.dtype+"\",\""+row.ctype+"\")'>修改</a>";
	}
	
function oper_flage(val,row){
	if(val == 0){
		return "启用";
	}else if(val == 0){
		return "禁用";
	}else{
		return "";
	}
}

var toolbarParams = {
	boolbarId : "fileMaintenance_button_bar_user_div",
	title : "传输监控资料配置维护列表",
	body :[]
}
/**
 * 添加操作工具面板
 */
DataGrid.initToolsBarPanel(toolbarParams);


/**
 * 初始化表格数据
 */
$('#fileMaintenance_list_data').datagrid({
    nowrap: false,  //一行显示数据，提高加载性能true
    striped: true,  //隔行换色
    collapsible:false,//是否可折叠的 
    fit: true,//height 100%
    url:contentPath+"/systemBackInfoHandler/getFileMaintenancePageList", 
    remoteSort:false, //要排序的数据从服务器定义 
	pageSize: 25,//每页显示的记录条数，默认为10 
    pageList: [25,30,50,100],//可以设置每页记录条数的列表 
    pagination:true,//分页控件 
    rownumbers:false,//行号 
    //冻结列在左
    frozenColumns:[[
       {field:'ck',checkbox:true} 
    ]]
});

function delete_file(dtype,ctype){
	$.messager.confirm('消息', '你确定要删除？', function(r){
		if(r){
			var url = contentPath+"/systemBackInfoHandler/deleteFile";
			var data = {dtype:dtype,ctype:ctype};
			Util.getAjaxData(url,data,function(data){
				       if(data.success){
				    	   topCenter("数据删除成功！");
				    	   $('#fileMaintenance_list_data').datagrid('reload');
				       }else{
				    	   topCenter("数据删除失败！");
				       }
			},true);
		}
	});
	
}

function update_file(dtype,ctype){
	$('#dialog_content').dialog({
		title : '修改要素数据',
		width : 700,
		height : 420,
		closed : false,
		cache : false,
		href : 'view/backInfo/fileMaintenance/updateFileMaintenance.jsp?dtype=' +dtype+'&ctype='+ctype,
		modal : true
	});
}

</script>
