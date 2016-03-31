var queryParams = {
	queryId : "nodeManage_search_bar_div",
	body : [{
		column : 'searchKey',
		name : '关键字查询：',
		type : "text",
		placeholder : "关键字"
	},{
		column : 'area',
		name : '节点区域：',
		type : "text",
		placeholder : "节点区域"
	}],
	queryDataGrid : "nodeManage_list_data"
}

/**
 * 创建查询面板
 */
DataGrid.initQueryPanel(queryParams);

/**
 * start format
 */
function oper_user(val,row){
	return "<a href='javascript:void(0);' onclick='deleteNodeManage(\""+val+"\")'>删除</a>&nbsp;&nbsp;<a href='javascript:void(0);' onclick='update_networkDevice(\""+val+"\")'>修改</a>";
}
/**
 * end format
 */
 

function deleteNodeManage(id){
	 $.messager.confirm('消息', '你确定要删除？', function(r){
		 if (r){
			$.post(contentPath+"/transferMonitorHander/deleteNodeManagerInfo",{id:id},function (data){
				if(data.success){
					topCenter("删除成功！");
					$('#nodeManage_list_data').datagrid('reload');
				}else{
					topCenter("删除失败！");
				}
			});
		 }
	 });
}

function update_networkDevice(id){
	$('#dialog_content').dialog({
		title: '修改节点数据',
		width: 550,
		height: 400,
		closed: false,
		cache: false,
		href: 'view/transferMonitor/nodeManage/updateNodeManage.jsp?id='+id,
		modal: true
	});
}

var toolbarParams = {
	boolbarId : "nodeManage_button_bar_div",
	title : "传输设备节点管理列表",
	body :[{
		icon : "icon-plus",
		text : "添加",
		event : function(){
              addDevices();
		}
	}]
}
/**
 * 添加操作工具面板
 */
DataGrid.initToolsBarPanel(toolbarParams);


/**
 * 初始化表格数据
 */
$('#nodeManage_list_data').datagrid({
    nowrap: false,  //一行显示数据，提高加载性能true
    striped: true,  //隔行换色
    collapsible:false,//是否可折叠的 
    fit: true,//height 100%
    url:contentPath+"/transferMonitorHander/getNodeManagePageList", 
    remoteSort:false, //要排序的数据从服务器定义 
    pagination:true,//分页控件 
    rownumbers:true,//行号 
    pageSize: 25,//每页显示的记录条数，默认为10 
    pageList: [25,30,50,100]//可以设置每页记录条数的列表 
});

/**
 * 添加设备
 */
function addDevices(){
	$('#dialog_content').dialog({
		title: '添加节点数据',
		width: 550,
		height: 440,
		closed: false,
		cache: false,
		href: 'view/transferMonitor/nodeManage/addNodeManage.jsp',
		modal: true
	});
}