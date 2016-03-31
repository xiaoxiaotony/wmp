<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div title="定制数据库查询列表" data-options="closable:true">
	<!-- 查询面板 -->
	<div class="search_bar" id="customDataQuery_search_bar_div"></div>
	<!-- 工具栏 -->
	<div class="cont_tit" id="customDataQuery_button_bar_user_div"></div>
	<!--table-->
	<div class="table_cont">
		<table id="customDataQuery_list_data">
			<thead>
				<tr>
					<th field="name" width="20%" sortable="true">查询名称</th>
					<th field="createtime" width="20%">创建时间</th>
					<th field="filepath" width="20%">文件输入路径</th>
					<th field="period" width="15%" formatter="oper_period">周期</th>
					<th field="format_file" width="15%">格式类型</th>
					<th field="id" width="10%" data-options="align:'center'" formatter="oper_user">操作</th>
				</tr>
			</thead>
		</table>
	</div>
	<!--底部分页 end-->
</div>
<script>

var queryParams = {
	queryId : "customDataQuery_search_bar_div",
	body : [{
		column : 'searchKey',
		name : '模糊信息：',
		type : "text",
		placeholder : "关键字"
	} ],
	queryDataGrid : "customDataQuery_list_data"
}

/**
 * 创建查询面板
 */
DataGrid.initQueryPanel(queryParams);

/**
 * start format
 */
function oper_user(val,row){
	return "<a href='javascript:void(0);' onclick='delete_customDataQuery(\""+val+"\")'>删除</a>&nbsp;&nbsp;<a href='javascript:void(0);' onclick='update_customDataQuery(\""+val+"\")'>修改</a>";
}

function oper_period(val,row){
	if(val == 1){
		return "每天";
	}else if(val == 2){
		return "每周";
	}else if(val == 3){
		return "每月";
	}else{
		return "每年";
	}
}

var toolbarParams = {
	boolbarId : "customDataQuery_button_bar_user_div",
	title : "定制数据查询功能列表",
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
$('#customDataQuery_list_data').datagrid({
    nowrap: false,  //一行显示数据，提高加载性能true
    striped: true,  //隔行换色
    collapsible:false,//是否可折叠的 
    fit: true,//height 100%
    url:contentPath+"/systemBackInfoHandler/getCustomDataQueryListData", 
    remoteSort:false, //要排序的数据从服务器定义 
    pagination:true,//分页控件 
    rownumbers:false,//行号 
    pageSize: 25,//每页显示的记录条数，默认为10 
    pageList: [25,30,50,100]
});


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

function add(){
	$('#dialog_content').dialog({
		title: '添加定制数据查询信息',
		width: 500,
		height: 580,
		closed: false,
		cache: false,
		href: 'view/backInfo/customDataQuery/addCustomDataQuery.jsp',
		modal: true
	});
}

function delete_customDataQuery(id){
	$.messager.confirm('消息', '你确定要删除？', function(r){
		 if (r){
			$.post(contentPath+"/systemBackInfoHandler/deleteCustomDataQuery",{id:id},function (data){
				if(data.success){
					topCenter("删除成功！");
					$('#customDataQuery_list_data').datagrid('reload');
				}else{
					topCenter("删除失败！");
				}
			});
		 }
	 });
}

function update_customDataQuery(id){
	$('#dialog_content').dialog({
		title: '修改查询数据',
		width: 500,
		height: 580,
		closed: false,
		cache: false,
		href: 'view/backInfo/customDataQuery/updateCustomDataQuery.jsp?id='+id,
		modal: true
	});
}
</script>
