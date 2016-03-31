function oper_user(val,row){
	if(val == 1){
		return "<a href='javascript:void(0);' onclick='set_auth(\""+val+"\")'>权限设置</a>";
	}
	return "<a href='javascript:void(0);' onclick='delete_role(\""+val+"\")'>删除</a>&nbsp;&nbsp;<a href='javascript:void(0);' onclick='update_role(\""+val+"\")'>修改</a>&nbsp;&nbsp;<a href='javascript:void(0);' onclick='set_auth(\""+val+"\")'>权限设置</a>";
}

var toolbarParams = {
	boolbarId : "button_bar_div",
	title : "角色列表",
	body :[{
		icon : "icon-plus",
		text : "新增",
		event : function(){
			Util.window("添加角色", "add_role_div", "../pages/view/systemInfo/role/addRole.jsp", 600, 400);
		}
	}]
}
/**
 * 查询工具条面板
 */
DataGrid.initToolsBarPanel(toolbarParams);

$('#roleInfo_list_data').datagrid({ 
    nowrap: false, 
    striped: true, 
    fit: true,//自动大小 
    border: true, 
    autoRowHeight:false,
    collapsible:false,//是否可折叠的 
    pageSize: 25,//每页显示的记录条数，默认为10 
    pageList: [25,30,50,100],//可以设置每页记录条数的列表 
    url:contentPath+"/roleManagerHandler/getRoleListInfo", 
    remoteSort:false,  
    idField:'id', 
    pagination:true,//分页控件 
    rownumbers:false
});
/**
 * 创建查询面板
 */
var queryParams = {
	queryId : "role_search_bar_div",
	body : [ {
		column : 'roleName',
		name : '角色名称：',
		type : "text"
	}, {
		column : 'roleStatus',
		name : '状态：',
		type : "select",
		placeholder : "角色状态",
		listData : "[{\"value\":\"0\",\"label\":\"正常\"},{\"value\":\"1\",\"label\":\"禁用\"}]"
	}],
	queryDataGrid : "roleInfo_list_data"
}
DataGrid.initQueryPanel(queryParams);

function formate_role_status(val,row){
	if(val == 0){
		return "启用";
	}
	return "禁用";
}


/**
 * 保存角色
 */
function saveRole(){
	$.messager.progress();	// 显示进度条
	$('#add_role_div_page').form('submit', {
		url: contentPath+"/roleManagerHandler/addRole",
		onSubmit: function(){
			var isValid = $(this).form('validate');
			if (!isValid){
				$.messager.progress('close');	// 如果表单是无效的则隐藏进度条
			}
			return isValid;	// 返回false终止表单提交
		},
		success: function(data){
			$.messager.progress('close');	// 如果提交成功则隐藏进度条
			$('#roleInfo_list_data').datagrid("reload");
			Util.closeWindow("add_role_div");
		}
	});
}

function delete_role(roleId){
	jConfirm("你确定要删除这个角色么?","提示",function(data){
		if(data){
			Util.getAjaxData(contentPath+"/roleManagerHandler/deleteRole?roleId="+roleId, null, function(data){
				if(data.data){
					Util.showMsg("删除成功");
					$('#roleInfo_list_data').datagrid("reload");
				}else{
					Util.showMsg("删除失败");
				}
			});
		}
	});
}

function update_role(roleId){
	$('#dialog_content').dialog({
		title: '编辑角色',
		width: 500,
		height: 320,
		closed: false,
		cache: false,
		href: 'view/systemInfo/role/updateRole.jsp?id='+roleId,
		modal: true
	});
}

function set_auth(roleId){
	Util.window("设置权限", "add_role_div", "../pages/view/systemInfo/role/setAuth.jsp?roleId="+roleId, 600, 600);
}
