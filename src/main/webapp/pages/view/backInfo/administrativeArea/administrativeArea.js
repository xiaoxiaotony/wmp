function oper_status(val,row){
	if(val == "0"){
		return "正常";
	}
	return "禁用";
}

function oper_area(val,row){
	return "<a href='javascript:void(0);' onclick='delete_area(\""+row.id+"\")'>删除</a>";
}

// 删除区域
function delete_area(areaId){
	if(node_id == '540000'){
		Util.showMsg("不能删除一级行政区域");
		return;
	}
	$.messager.confirm('消息', '你确定要删除？', function(r){
		 if(r){
				Util.getAjaxData(contentPath+"/systemBackInfoHandler/deleteArea",{id:areaId},function(data){
					if(data.success){
						Util.showMsg("删除成功！");
						$('#administrativeArea_Info_list_data').datagrid('reload');
					}else{
						Util.showMsg("删除失败！存在子节点！");
					}
				}, true);
		 }
   });
}

var node_id = 540000;
$(function(){
	$('#administrativeArea_tree_panel').tree({    
	    url: contentPath+"/systemBackInfoHandler/getAreaListTree",
	    method:'get',
	    animate:true,
	    lines:true,
	    dnd:true,
	    onClick:function(node){
	    	node_id = node.id;
			loadRightDataGrid(node.id);
		},
		//加载完成选中跟节点的菜单按钮
		onLoadSuccess:function(){
			var node = $('#administrativeArea_tree_panel').tree('find', 540000);
			$('#administrativeArea_tree_panel').tree('select', node.target);
			loadRightDataGrid(node.id);
		}
	});
	$("#administrativeArea_tree_div").height($(window).height() - 171);
});

function loadRightDataGrid(id){
	$('#administrativeArea_Info_list_data').datagrid({ 
	    nowrap: false, 
	    striped: true, 
	    border: true, 
	    autoRowHeight:false,
	    collapsible:false,//是否可折叠的 
	    fit: true,//自动大小 
	    pageSize:20,
	    url:contentPath+"/systemBackInfoHandler/queryAreaPageRight?parentId="+id, 
	    remoteSort:false,  
	    pagination:true,//分页控件 
	    rownumbers:false,
	    pageSize: 25,//每页显示的记录条数，默认为10 
	    pageList: [25,30,50,100]//可以设置每页记录条数的列表 
	}); 
}

/**
 * 新增
 */
var toolbarParams = {
	boolbarId : "administrativeArea_button_bar_div",
	title : "行政区域列表",
	body :[{
		icon : "icon-plus",
		text : "新增",
		event : function(){
			Util.window("新增","dialog_content","view/backInfo/administrativeArea/addArea.jsp?nodeId="+node_id,500,250);
		}
	}]
}
/**
 * 操作工具条
 */
DataGrid.initToolsBarPanel(toolbarParams);
