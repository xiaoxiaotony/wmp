<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<% String location_node_id = request.getParameter("location_node_id"); %>
<div id="win_content">
	<!--设备详情-->
	<div class="sbxq-main">
		<h3>文件系统</h3>
		<table width="100%" class="table05">
			<thead>
				<tr>
					<th align="center">FileSystem</th>
					<th align="center">容量</th>
					<th align="center">已用</th>
					<th align="center">可用</th>
					<th align="center">已用%</th>
					<th align="center">挂在点</th>
				</tr>
			</thead>
			<tbody id="diskInfo_context" >
			</tbody>
		</table>
		<div class="sbxq-table01">
			<table width="100%">
				<tr>
					<th>硬件状态</td>
					<td id="alarm_div"></td>
				</tr>
				<tr>
					<th>服务器名 </td>
					<td id="server_name"></td>
				</tr>
				<tr>
					<th>ip地址：</td>
					<td id="server_ip"></td>
				</tr>
				<tr>
					<th>系统类型 </td>
					<td id="server_os"></td>
				</tr>
			</table>
		</div>
		<h3>FTP日志列表</h3>
		<table id="transfter_device_ftplog_list_data" width="99%" class="table05">
			<thead>
				<tr>
					<th field="ftp_log_name" width="50%" sortable="true">文件名</th>
					<th field="path" width="37.5%" data-options="align:'center'" >文件路径</th>
					<th field="id" width="15%" data-options="align:'center'" formatter="oper_log">操作</th>
				</tr>
			</thead>
		</table>
	</div>
</div>
<!--查看日志-->
<div class="log-wrap" id="loginWrap">
    <div class="log-main panel window">
        <div class="panel-header panel-header-noborder window-header">
            <div class="panel-title panel-with-icon" id="log-content-title"></div>
            <div class="panel-icon icon-plus"></div>
            <div class="panel-tool">
                <a class="panel-tool-close" href="javascript:void(0)"></a>
            </div>
        </div>
        <div class="log-content" id="log-content">
        </div>
        <div style="padding:5px;" class="panel-footer panel-footer-noborder">
            <div style="text-align:right;padding:5px 0 0;">
                <a class="easyui-linkbutton l-btn l-btn-small" href="javascript:void(0)" style="width:80px"><span class="l-btn-left l-btn-icon-left" style="margin-top: 0px;"><span class="l-btn-text">确定</span><span class="l-btn-icon icon-ok">&nbsp;</span></span></a>
            </div>
        </div>
    </div>
    <div class="log-bg"></div>
</div>
<script>
var location_node_id = '<%=location_node_id%>';
var deviceId;
//查询服务器信息
Util.getAjaxData(contentPath+"/transferMonitorHander/getDeviceDetailInfo?location_node_id="+location_node_id,"",function(data){
	var data = data.data.map;
	$("#server_name").html(data.name);
	//报警 <a class="icon-wonder" style="margin-left: 10px;">!</a>
	$("#alarm_div").html("正常");
	$("#server_ip").html(data.ip);
	$("#server_os").html(data.os);
	deviceId = data.id;
},false);
//查询磁盘信息
Util.getAjaxData(contentPath+"/transferMonitorHander/getDeviceDiskInfo?deviceId="+deviceId,"",function(data){
	var tempStr = "";
	for (var i = 0; i < data.length; i++) {
		var map = data[i].map;
		tempStr += "<tr><td align='center'>"+map.path+"</td><td align='center'>"+map.totalsize+"</td><td align='center'>"+map.usedsize+"</td><td align='center'>"+map.freeSize+"</td><td align='center'>"+map.usageRate+"%</td><td align='center'>"+map.root+"</td></tr>"
	}
	$("#diskInfo_context").html(tempStr);
},true);

$('#transfter_device_ftplog_list_data').datagrid({
    nowrap: true,  //一行显示数据，提高加载性能true
    striped: true,  //隔行换色
    url:contentPath+"/transferMonitorHander/getDeviceFtpLog?deviceId="+deviceId, 
    pagination:true,//分页控件 
    rownumbers:false,//行号 
    pageSize: 5,//每页显示的记录条数，默认为10 
    pageList: [5,10,20,50]//可以设置每页记录条数的列表 
});

function oper_log(val,row){
	var logPath = row.path+row.ftp_log_name;
	return "<a href='javascript:showFtpLog(\""+logPath+"\");' class='easyui-linkbutton'>下载</a>";
}

//查询ftp日志记录
/**
 * 初始化表格数据
 */
function showFtpLog(path){
	//$('#loginWrap').show();
	// "G:\\tomcat-8\\logs\\error.log"
	//var url = path;
	//$("#log-content-title").html(path);
	//$('#log-content').load('../pages/view/transferMonitor/deviceDevicesConfig/readFtpLogShowFile.jsp?logUrl='+url);
	var ip = $("#server_ip").html();
	window.location.href=contentPath+"/commonHander/downLoadFtpFile?fileName="+path+"&ipaddress="+ip;
}


$(function(){
	//拖拽日志
    $('.log-wrap').draggable({
        handle: $(this).find(".panel-title")
    });
    //隐藏日志层
    $("#loginWrap .l-btn-small,#loginWrap .panel-tool-close").on("click", function() {
        $("#loginWrap").hide();
    });
})
</script>