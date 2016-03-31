<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div title="传输设备管理" data-options="closable:true">
	<!-- 查询面板 -->
	<div class="search_bar" id="transferMonitor_search_bar_div"></div>
	<!-- 工具栏 -->
	<div class="cont_tit" id="transferMonitor_button_bar_div"></div>
	<!--table-->
	<div class="table_cont">
		<table id="transferMonitor_list_data">
			<thead>
				<tr>
					<th field="name" width="15%" sortable="true" formatter="oper_name">站点设备名称</th>
					<th field="ip" width="15%">设备IP</th>
					<th field="devicetypename" width="10%">设备类型</th>
					<th field="areaname" width="10%">所属区域</th>
					<th field="os" width="5%">操作系统</th>
					<th field="ftplogpath" width="30%">ftp路径日志</th>
					<th field="id" width="14%" data-options="align:'center'" formatter="oper_user">操作</th>
				</tr>
			</thead>
		</table>
	</div>
	<!--底部分页 end-->
</div>
<script>
var queryParams = {
	queryId : "transferMonitor_search_bar_div",
	body : [{
		column : 'ipaddress',
		name : 'ip地址：',
		type : "text",
		placeholder : "ip地址"
	},{
		column : 'area',
		name : '地区：',
		type : "text",
		placeholder : "地区"
	}],
	queryDataGrid : "transferMonitor_list_data"
}

/**
 * 创建查询面板
 */
DataGrid.initQueryPanel(queryParams);

/**
 * start format
 */
function oper_user(val,row){
	return "<a href='javascript:void(0);' onclick='deleteTransferDevice(\""+val+"\")'>删除</a>&nbsp;&nbsp;<a href='javascript:void(0);' onclick='updateTransferDevice(\""+val+"\")'>修改</a>";
}

function oper_name(val,row){
	return val+"&nbsp;&nbsp;("+row.deviceaddress+")";
}

/**
 * end format
 */
 

function deleteTransferDevice(id){
	 $.messager.confirm('消息', '你确定要删除？', function(r){
		 if (r){
			$.post(contentPath+"/transferMonitorHander/deleteTransferInfo",{id:id},function (data){
				if(data.success){
					topCenter("删除成功！");
					$('#transferMonitor_list_data').datagrid('reload');
				}else{
					topCenter("删除失败！");
				}
			});
		 }
	 });
}

function updateTransferDevice(id){
	$('#dialog_content').dialog({
		title: '修改传输设备',
		width: 500,
		height: 480,
		closed: false,
		cache: false,
		href: 'view/transferMonitor/transferDevices/updateTransferMonitorDevice.jsp?id='+id,
		modal: true
	});
}

var toolbarParams = {
	boolbarId : "transferMonitor_button_bar_div",
	title : "传输设备信息列表",
	body :[{
		icon : "icon-plus",
		text : "添加",
		event : function(){
			addTransferDeviceInfo();
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
$('#transferMonitor_list_data').datagrid({
    nowrap: false,  //一行显示数据，提高加载性能true
    striped: true,  //隔行换色
    collapsible:false,//是否可折叠的 
    fit: true,//height 100%
    url:contentPath+"/transferMonitorHander/getTransferMonitor?type=2", 
    remoteSort:false, //要排序的数据从服务器定义 
    pagination:true,//分页控件 
    rownumbers:true,//行号 
    pageSize: 25,//每页显示的记录条数，默认为10 
    pageList: [25,30,50,100]
});

/**
 * 添加设备
 */
function addTransferDeviceInfo(){
	$('#dialog_content').dialog({
		title: '添加传输设备',
		width: 500,
		height: 480,
		closed: false,
		cache: false,
		href: 'view/transferMonitor/transferDevices/addTransferMonitorDevice.jsp',
		modal: true
	});
}
</script>
