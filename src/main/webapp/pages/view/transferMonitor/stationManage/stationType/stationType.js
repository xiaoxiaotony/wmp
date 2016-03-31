var queryParams = {
	queryId : "stationType_search_bar_div",
	body : [{
		column : 'searchKey',
		name : '站号站名：',
		type : "text",
		placeholder : "关键字"
	},{
		column : 'area',
		name : '区域查询：',
		type : "text",
		placeholder : "区域查询"
	},{
		column : 'stationType',
		name : '站类型：',
		type : "select",
		valueField:'STYPE_ID',
	    textField:'STYPE_NAME',
		url : "../http/systemInfoHandler/getStationType"
	},{
		column : 'ischeck',
		name : '是否考核站：',
		type : "select",
		listData : "[{\"value\":\"0\",\"label\":\"是\"},{\"value\":\"1\",\"label\":\"否\"}]"
	} ],
	queryDataGrid : "stationType_list_data"
}

/**
 * 创建查询面板
 */
DataGrid.initQueryPanel(queryParams);

function oper_assess(val,row){
	if(val == "0"){
		return "是";
	}else{
		return "否";
	}
}

//删除和编辑用户
function oper_user(val,row){
	return "<a href='javascript:void(0);' onclick='deletes_station(\""+row.iiiii+"\")'>删除</a>&nbsp;&nbsp;<a href='javascript:void(0);' onclick='update_statinType(\""+row.iiiii+"\")'>修改</a>";
}

//删除用户
function deletes_station(id){
	 $.messager.confirm('消息', '你确定要删除？', function(r){
		 if (r){
				$.post(contentPath+"/stationTypeHander/deleteStationInfo",{stationId:id},function (data){
					if(data.data.success){
						topCenter("删除成功！");
						$('#stationType_list_data').datagrid('reload');
					}else{
						topCenter("删除失败！");
					}
				});
		 }
	 });
}

//编辑用户
function update_statinType(id){
	$('#dialog_content').dialog({
		title: '修改站点信息',
		width: 600,
		height: 420,
		closed: false,
		cache: false,
		href: 'view/transferMonitor/stationManage/stationType/updateStationType.jsp?id='+id,
		modal: true
	});
}

var toolbarParams = {
	boolbarId : "stationType_button_bar_user_div",
	title : "站点管理列表",
	body :[{
		icon : "icon-refresh",
		text : "刷新",
		hidden : false,
		event : function(){
              reloadStationType();
		}
	},{
		icon : "icon-plus",
		text : "新增",
		hidden : false,
		event : function(){
			  addStationType();
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
$('#stationType_list_data').datagrid({
    nowrap: false,  //一行显示数据，提高加载性能true
    striped: true,  //隔行换色
    collapsible:false,//是否可折叠的 
    fit: true,//height 100%
    url:contentPath+"/stationTypeHander/getStationTypeInfo", 
    remoteSort:false, //要排序的数据从服务器定义 
	pageSize: 25,//每页显示的记录条数，默认为10 
    pageList: [25,30,50,100],//可以设置每页记录条数的列表 
    pagination:true,//分页控件
    rownumbers:true
});

/**
 * 面板操作
 */
function reloadStationType(){
	$('#stationType_list_data').datagrid('reload');
}
function addStationType(){
	$('#dialog_content').dialog({
		title: '添加站点信息',
		width: 600,
		height: 420,
		closed: false,
		cache: false,
		href: 'view/transferMonitor/stationManage/stationType/addStationType.jsp',
		modal: true
	});
}