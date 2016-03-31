<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div title="菜单信息" data-options="closable:true">
	<div style="position: absolute;left:0%; width:18%;overflow: hidden;right: 0; top: 0;bottom: 0;">
		<div class="easyui-panel" id="menu_tree_div" style="padding:5px">
			<ul class="easyui-tree" id="menu_tree_panel"></ul>
		</div>
	</div>
	
	<div style="position: absolute;left: 18.5%;overflow: hidden;right: 0;top: 0;bottom: 0;">
		<!-- 工具栏 -->
		<div class="cont_tit" id="menu_button_bar_div"></div>
		<!--table-->
		<div class="table_cont" style="top: 40px;">
			<table id="menu_Info_list_data" cellspacing="0" cellpadding="0"
				width="100%">
				<thead>
					<tr>
						<th field="name" width="20%">菜单名称</th>
						<th field="url" width="40%" formatter="formatUrl">地址</th>
						<th field="icon" width="10%">icon</th>
						<th field="leaf" width="10%" formatter="oper_isChild">是否为子节点</th>
						<th field="enable" width="10%" formatter="oper_status">状态</th>
						<th field="id" width="10%" data-options="align:'center'" formatter="oper_menu">操作</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
	<!--底部分页 end-->
</div>
<script>

function oper_isChild(val,row){
	if(val == "0"){
		return "否";
	}
	return "是";
}

function oper_status(val,row){
	if(val == "0"){
		return "正常";
	}
	return "禁用";
}


function oper_menu(val,row){
	return "<a href='javascript:void(0);' onclick='update_menu(\""+val+"\")'>修改&nbsp;&nbsp;<a href='javascript:void(0);' onclick='delete_menu(\""+val+"\")'>删除</a>";
}



var node_id = 1;
$(function(){
	$('#menu_tree_panel').tree({    
	    url: contentPath+"/menuInfoHandler/getMenuPageLeftTree",
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
			var node = $('#menu_tree_panel').tree('find', 1001);
			$('#menu_tree_panel').tree('select', node.target);
			loadRightDataGrid(1001);
		},
		onBeforeDrop:function(target, source, point){
			if(point == 'append') return false;
			var tg = $('#menu_tree_panel').tree('getData', target);
			var tg_id = tg.id;
			var id = source.id;
			var tg_parentId = tg.parentId;
			var parentId = source.parentId;
			if(tg_parentId != parentId){
				Util.showMsg("不能跨级拖放！");
			    return false;
			}
			Util.getAjaxData(contentPath+"/menuInfoHandler/updateMenuSort", {point:point,tg_id:tg_id,id:id,parentId:parentId}, function(data){
				if(data.success){
					
				}else{
					Util.showMsg("节点排序失败");
				}
			}, false);
		}
	});
	$("#menu_tree_div").height($(window).height() - 171);
});

function formatUrl(val,row){
	if(val.length == 0){
		return "一级菜单无地址";
	}
	return val;
}

function loadRightDataGrid(id){
	$('#menu_Info_list_data').datagrid({ 
	    nowrap: false, 
	    striped: true, 
	    border: true, 
	    autoRowHeight:false,
	    collapsible:false,//是否可折叠的 
	    fit: true,//自动大小 
	    pageSize:20,
	    url:contentPath+"/menuInfoHandler/queryMenuPageRight?parentId="+id, 
	    remoteSort:false,  
	    pagination:true,//分页控件 
	    rownumbers:false
	}); 

	//设置分页控件 
	var p = $('#menu_Info_list_data').datagrid('getPager');
	$(p).pagination({ 
		pageSize: 20,//每页显示的记录条数，默认为10 
	    pageList: [10,20,30,50],//可以设置每页记录条数的列表 
	    beforePageText: '第',//页数文本框前显示的汉字 
	    afterPageText: '页    共 {pages} 页', 
	    displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
	}); 
}
/**
 * 修改
 */
function update_menu(id){
	Util.window("修改菜单","dialog_content","view/systemInfo/menu/updateMenu.jsp?id="+id,500,350);	
}

/**
 * 删除
 */
 function delete_menu(id){
	 $.messager.confirm('消息', '你确定要删除？', function(r){
		 if(r){
				Util.getAjaxData(contentPath+"/menuInfoHandler/deleteMenu",{id:id},function(data){
					if(data.success){
						Util.showMsg("删除成功！");
						$('#menu_Info_list_data').datagrid('reload');
						$('#menu_tree_panel').tree('reload');
					}else{
						Util.showMsg("删除失败！存在子节点！");
					}
				}, true);
		 }
	 });
}


/**
 * 新增
 */
var toolbarParams = {
	boolbarId : "menu_button_bar_div",
	title : "菜单信息列表",
	body :[{
		icon : "icon-plus",
		text : "新增",
		event : function(){
			Util.window("新增菜单","dialog_content","view/systemInfo/menu/addMenu.jsp?nodeId="+node_id,500,350);
		}
	}]
}
/**
 * 操作工具条
 */
DataGrid.initToolsBarPanel(toolbarParams);

</script>
