var queryParams = {
	queryId : "alarmInfoList_search_bar_div",
	body : [{
		column : 'alarmType',
		name : '告警类型：',
		type : "text",
		placeholder : "关键字"
	},{
		column : 'ipaddress',
		name : '告警级别：',
		type : "text",
		placeholder : "告警级别"
	},{
		column : 'ipaddress',
		name : '地区：',
		type : "text",
		placeholder : "地区"
	}],
	queryDataGrid : "alarmInfoList_list_data"
}

/**
 * 创建查询面板
 */
DataGrid.initQueryPanel(queryParams);


var toolbarParams = {
		boolbarId : "alarmInfoList_button_bar_div",
		title : "告警列表",
		body :[{
			icon : "icon-plus",
			text : "忽略",
			event : function(){
	              cleanAlarm();
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
$('#alarmInfoList_list_data').datagrid({
    nowrap: false,  //一行显示数据，提高加载性能true
    striped: true,  //隔行换色
    collapsible:false,//是否可折叠的 
    fit: true,//height 100%
    url:contentPath+"/devicesInfoHandler/getAlarmInfoPageList", 
    remoteSort:false, //要排序的数据从服务器定义 
    pagination:true,//分页控件 
    rownumbers:false,//行号 
    pageSize: 20,//每页显示的记录条数，默认为10 
    pageList: [20,30,50,100],//可以设置每页记录条数的列表 
    //冻结列在左
    frozenColumns:[[
       {field:'ck',checkbox:true} 
    ]]
});

function cleanAlarm(){
	 $.messager.confirm('消息', '确定要清空所有告警日志么？', function(r){
		 if (r){
			$.post(contentPath+"/devicesInfoHandler/cleanAlarm",{id:""},function (data){
				if(data.success){
					topCenter("清除成功！");
					$('#alarmInfoList_list_data').datagrid('reload');
				}else{
					topCenter("删除失败！");
				}
			});
		 }
	 });
}


function oper_alarmlevel(val,row){
	if(val == 4){
		return "<font color='red'>异常严重</font>"
	}else if(val == 3){
		return "<font color='red'>严重</font>"
	}
	return "通知";
}

function oper_type(val,row){
	if(val == 1){
		return "地面专网设备报警";
	}else if(val == 2){
		return "地面传输设备告警";
	}else{
		return "要素阀值报警";
	}
}