/**
 * 配置数据管理 加载列表
 * 左边面板  
 */
function oper_user(val,row){
	return "<a href='javascript:void(0);' onclick='delete_data(\""+val+"\")'>删除</a>&nbsp;&nbsp;<a href='javascript:void(0);' onclick='update(\""+val+"\")'>修改</a>";
}

function delete_data(id){
}

var toolbarParams = {
	boolbarId : "left_button_bar_div",
	title : "基础数据类型",
	body :[{
		icon : "icon-plus",
		text : "新增",
		event : function(){
			Util.window("添加角色", "add_role_div", "../pages/view/systemInfo/role/addRole.jsp", 600, 400);
		}
	}]
}
/**
 * 创建工具栏面板
 */
DataGrid.initToolsBarPanel(toolbarParams);

var queryParams = {
	queryId : "left_search_bar_div",
	body : [ {
		column : 'roleName',
		name : '基础数据名称：',
		type : "text"
	}],
	queryDataGrid : "base_list_data"
}
/**
 * 创建查询面板
 */
DataGrid.initQueryPanel(queryParams);


$('#base_list_data').datagrid({ 
    nowrap: false, 
    striped: true, 
    fit: true,//自动大小 
    border: true, 
    autoRowHeight:false,
    collapsible:false,//是否可折叠的 
    pageSize: 25,//每页显示的记录条数，默认为10 
    pageList: [25,30,50,100],//可以设置每页记录条数的列表 
    url:contentPath+"/baseDataConfigHander/getPageList?type=0", 
    remoteSort:false,
    width:500,
    idField:'id', 
    pagination:true,//分页控件 
    rownumbers:false,
    singleSelect:true,
    onClickRow:function(index,row){
    	$('#base_right_list_data').datagrid({
    		url:contentPath+"/baseDataConfigHander/getConfigPageList?code="+row.dict_code
    	});
    	$('#base_right_list_data').datagrid('load');
    }
});
//设置分页控件 
var p = $('#base_list_data').datagrid('getPager');
$(p).pagination({ 
    beforePageText: '第',//页数文本框前显示的汉字 
    afterPageText: '页    共 {pages} 页', 
    displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
});




/**
 * 配置数据管理 加载列表
 * 
 * 右边面板  
 */
var toolbarParams = {
	boolbarId : "rigth_button_bar_div",
	title : "配置信息列表",
	body :[{
		icon : "icon-plus",
		text : "新增",
		event : function(){
			Util.window("添加角色", "add_role_div", "../pages/view/systemInfo/role/addRole.jsp", 600, 400);
		}
	},{
		icon : "icon-pencil",
		text : "批量删除",
		event : function(){
			alert("删除");
		}
	}]
}
/**
 * 创建工具栏面板
 */
DataGrid.initToolsBarPanel(toolbarParams);


var queryParams = {
	queryId : "right_search_bar_div",
	body : [ {
		column : 'roleName',
		name : '名称：',
		type : "text"
	}],
	queryDataGrid : "base_right_list_data"
}
/**
 * 创建查询面板
 */
DataGrid.initQueryPanel(queryParams);

$('#base_right_list_data').datagrid({ 
    nowrap: false, 
    striped: true, 
    fit: true,//自动大小 
    border: true, 
    autoRowHeight:false,
    collapsible:false,//是否可折叠的 
    pageSize: 25,//每页显示的记录条数，默认为10 
    pageList: [25,30,50,100],//可以设置每页记录条数的列表 
    url:contentPath+"/baseDataConfigHander/getConfigPageList?code=device_type", 
    remoteSort:false,
    width:500,
    idField:'id', 
    pagination:true,//分页控件 
    rownumbers:false
});
//设置分页控件 
var p = $('#base_right_list_data').datagrid('getPager');
$(p).pagination({ 
    beforePageText: '第',//页数文本框前显示的汉字 
    afterPageText: '页    共 {pages} 页', 
    displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
});