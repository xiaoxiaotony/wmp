<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div title="要素维护" data-options="closable:true">
	<!-- 查询面板 -->
	<div class="search_bar" id="elementInfo_search_bar_div"></div>
	<!-- 工具栏 -->
	<div class="cont_tit" id="elementInfo_button_bar_user_div"></div>
	<!--table-->
	<div class="table_cont">
		<table id="elementInfo_list_data">
			<thead>
				<tr>
					<th field="name" width="20%" sortable="true">要素名称</th>
					<th field="code" width="20%">要素编码</th>
					<th field="description" width="48%">要素描述</th>
					<th field="id" width="10%" data-options="align:'center'" formatter="oper_user">操作</th>
				</tr>
			</thead>
		</table>
	</div>
	<!--底部分页 end-->
</div>
<script>

var queryParams = {
	queryId : "elementInfo_search_bar_div",
	body : [{
		column : 'searchKey',
		name : '模糊信息：',
		type : "text",
		placeholder : "关键字"
	} ],
	queryDataGrid : "elementInfo_list_data"
}

/**
 * 创建查询面板
 */
DataGrid.initQueryPanel(queryParams);

/**
 * start format
 */
function oper_user(val,row){
	if(val == 1){
		return "<a href='javascript:void(0);' onclick='update_user(\""+val+"\")'>修改</a>";
	}else{
		return "<a href='javascript:void(0);' onclick='delete_user(\""+val+"\")'>删除</a>&nbsp;&nbsp;<a href='javascript:void(0);' onclick='update_user(\""+val+"\")'>修改</a>";
	}
}

var toolbarParams = {
	boolbarId : "elementInfo_button_bar_user_div",
	title : "地面实时观测的要素维护列表",
	body :[{
		icon : "icon-plus",
		text : "新增",
		hidden : false,
		event : function(){
              add();
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
$('#elementInfo_list_data').datagrid({
    nowrap: false,  //一行显示数据，提高加载性能true
    striped: true,  //隔行换色
    collapsible:false,//是否可折叠的 
    fit: true,//height 100%
    url:contentPath+"/systemBackInfoHandler/getElementPageList", 
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


/**
 * 批量删除用户
 */
function delete_users(){
	var rows = $('#elementInfo_list_data').datagrid('getSelections');
	var ids;
	var len = rows.length;
	if(len > 0){
		if(len == 1){
			if(rows[0].id == 1){ 
				$.messager.alert('消息','超级管理员不能被删除！','warning');
			    return;
			}
		}
		for(var i=0; i<len; i++){
			if(rows[i].id != 1){
				 if(i == 0) ids = rows[i].id;
				 else ids += ","+rows[i].id;
			}
		}
		$.messager.confirm('消息', '你确定要删除？', function(r){
			if(r){
				var url = contentPath+"/userInfoHandler/deleteUsers";
				var data = {ids:ids};
				Util.getAjaxData(url,data,function(data){
					       if(data.success){
					    	   topCenter("删除用户成功！");
					    	   $('#elementInfo_list_data').datagrid('reload');
					       }else{
					    	   topCenter("删除用户失败！");
					       }
				},true);
			}
		});
	}else{
		$.messager.alert('消息','没有被选择的用户！','warning');
	}
}

/**
 * 添加用户
 */
function add(){
		$('#dialog_content').dialog({
			title: '添加用户',
			width: 500,
			height: 500,
			closed: false,
			cache: false,
			href: 'view/systemInfo/userinfo/addUser.jsp',
			modal: true
		});
}

//提示消息弹窗
function topCenter(messges,dialog_size){
	if(!dialog_size) dialog_size=0;
	$.messager.show({
		title:'消息',
		msg:messges,
		showType:'slide',
		style:{
				right:'',
				top:document.body.scrollTop+document.documentElement.scrollTop-dialog_size,
				bottom:''
		}
	});
}


//设置分页控件 
var p = $('#elementInfo_list_data').datagrid('getPager');
$(p).pagination({ 
    beforePageText: '第',//页数文本框前显示的汉字 
    afterPageText: '页    共 {pages} 页', 
    displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
});
</script>
