<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div title="网络设备信息" data-options="closable:true">
	<!-- 查询面板 -->
	<div class="search_bar" id="networkInfo_search_bar_div"></div>
	<!-- 工具栏 -->
	<div class="cont_tit" id="networkInfo_button_bar_div"></div>
	<!--table-->
	<div class="table_cont">
		<table id="networkInfo_list_data">
			<thead>
				<tr>
					<th field="name" width="15%" sortable="true"  data-options="align:'center'"  formatter="oper_name">站点设备名称</th>
					<th field="ip" width="20%">站点设备地址</th>
					<th field="ip" width="25%">设备IP</th>
					<th field="type" width="14%" formatter="oper_type">所属类型</th>
					<th field="muncpl" width="15%">所属区域</th>
					<th field="id" width="15%" data-options="align:'center'" formatter="oper_user">操作</th>
				</tr>
			</thead>
		</table>
	</div>
	<!--底部分页 end-->
</div>
<script>
var queryParams = {
	queryId : "networkInfo_search_bar_div",
	body : [{
		column : 'searchKey',
		name : '关键字查询：',
		type : "text",
		placeholder : "关键字"
	},{
		column : 'ipaddress',
		name : 'ip地址：',
		type : "text",
		placeholder : "ip地址"
	},{
		column : 'ipaddress',
		name : '地区：',
		type : "text",
		placeholder : "地区"
	}],
	queryDataGrid : "networkInfo_list_data"
}

/**
 * 创建查询面板
 */
DataGrid.initQueryPanel(queryParams);

/**
 * start format
 */
function oper_user(val,row){
	return "<a href='javascript:void(0);' onclick='deleteNetworkDevice(\""+val+"\")'>删除</a>&nbsp;&nbsp;<a href='javascript:void(0);' onclick='update_networkDevice(\""+val+"\")'>修改</a>&nbsp;&nbsp;<a href='javascript:void(0);' onclick='show_networkInfo(\""+val+"\")'>查看详情</a>";
}

function oper_name(val,row){
	return val;
}

function oper_type(val,row){
	if(val == 1){
		return "路由器";
	}else{
		return "交换机";
	}
}

/**
 * end format
 */
 

function deleteNetworkDevice(id){
	 $.messager.confirm('消息', '你确定要删除？', function(r){
		 if (r){
			$.post(contentPath+"/netmonitorHander/deleteNetWorkInfo",{id:id},function (data){
				if(data.success){
					topCenter("删除成功！");
					$('#networkInfo_list_data').datagrid('reload');
				}else{
					topCenter("删除失败！");
				}
			});
		 }
	 });
}

/**
 * 查看详情
 */
function show_networkInfo(id){
	$('#dialog_content').dialog({
		title: '查看设备详情',
		width: 500,
		height: 300,
		closed: false,
		cache: false,
		href: 'view/netmonitor/networkDevice/showNetworkDevice.jsp?id='+id,
		modal: true
	});
}

function update_networkDevice(id){
	$('#dialog_content').dialog({
		title: '修改地面专网',
		width: 500,
		height: 300,
		closed: false,
		cache: false,
		href: 'view/netmonitor/networkDevice/updateNetworkDevice.jsp?id='+id,
		modal: true
	});
}

var toolbarParams = {
	boolbarId : "networkInfo_button_bar_div",
	title : "地面专网设备管理列表",
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
$('#networkInfo_list_data').datagrid({
    nowrap: false,  //一行显示数据，提高加载性能true
    striped: true,  //隔行换色
    collapsible:false,//是否可折叠的 
    fit: true,//height 100%
    url:contentPath+"/devicesInfoHandler/getPageList?type=1", 
    remoteSort:false, //要排序的数据从服务器定义 
    pagination:true,//分页控件 
    rownumbers:true,//行号 
    pageSize: 25,//每页显示的记录条数，默认为10 
    pageList: [25,30,50,100]
});

/**
 * 添加设备
 */
function addDevices(){
	$('#dialog_content').dialog({
		title: '添加专网设备',
		width: 500,
		height: 300,
		closed: false,
		cache: false,
		href: 'view/netmonitor/networkDevice/addNetworkDevice.jsp',
		modal: true
	});
}
</script>
