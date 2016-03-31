$(function(){
	$('#station_tree_panel').tree({    
		url: contentPath+"/transferDatumConfigHander/queryAreaStations?type=1",
	    method:'get',
	    animate:true,
	    checkbox:true,
	    onlyLeafCheck:true,
	    lines:true
	});
	$('#node_tree_panel').tree({    
		url: contentPath+"/transferDatumConfigHander/queryNodes",
	    method:'get',
	    animate:true,
	    checkbox:true,
	    onlyLeafCheck:true,
	    lines:true
	});
});
$("#station_tree_panel").height($(window).height() - 171);
$("#node_tree_panel").height($(window).height() - 171);

//获取树中选择的值
function getChecked(tree){
	var nodes = $('#' + tree).tree('getChecked');
	var id = '';
	for(var i=0; i<nodes.length; i++){
		if (id != ''){
			id += ',';
		}
		id += nodes[i].id;
	}
	return id;
}

/**
 * 保存节点与站点的关系
 */
function savaData(){
	var leftSataion = getChecked("station_tree_panel");
	var rigthNode = getChecked("node_tree_panel");
	if(leftSataion.length == 0){
		alert("请选择站点数据");
		return;
	}
	if(rigthNode.length == 0){
		alert("请选择节点数据");
		return;
	}
	var url = contentPath+"/transferDatumConfigHander/savaSatationNodeRef";
	var data = {leftSataion: leftSataion,rigthNode: rigthNode};
	Util.getAjaxData(url,data,function(data){
	       if(data.success){
	    	   $('#transferDatumConfig_list_data').datagrid("reload");
	    	   topCenter("添加成功！");
	       }else{
	    	   topCenter("添加失败！",500);
	       }
	},true);
}

/**
 * 初始化表格数据
 */
$('#transferDatumConfig_list_data').datagrid({
    nowrap: false,  //一行显示数据，提高加载性能true
    striped: true,  //隔行换色
    collapsible:false,//是否可折叠的 
    fit: true,//height 100%
    url:contentPath+"/transferDatumConfigHander/getTransferDatumConfigPage", 
    remoteSort:false, //要排序的数据从服务器定义 
    pagination:true,//分页控件 
    rownumbers:true,//行号 
    pageSize: 25,//每页显示的记录条数，默认为10 
    pageList: [25,30,50,100]//可以设置每页记录条数的列表 
});

function oper_transferDatumConfig(val,row){
	return "<a href='javascript:void(0);' onclick='update_station(\""+val+"\")'>修改站点</a>&nbsp;&nbsp;" +
			"<a href='javascript:void(0);' onclick='update_nodeInfo(\""+val+"\")'>修改节点</a>&nbsp;&nbsp;"+
			"<a href='javascript:void(0);' onclick='cancel_config_info(\""+val+"\")'>撤销</a>&nbsp;&nbsp;"+
			"<a href='javascript:void(0);' onclick='preview(\""+val+"\",\""+row.transfer_name+"\")'>预览</a>&nbsp;&nbsp;";
}

function update_station(id){
	Util.window("修改站点信息", "dialog_content", "view/transferMonitor/transferDatumConfig/updateStationManager.jsp?config_id="+id, 700, 550);
}
function update_nodeInfo(id){
	Util.window("修改节点顺序", "dialog_content", "view/transferMonitor/transferDatumConfig/updateNodeManager.jsp?config_id="+id, 700, 550);
}
function cancel_config_info(id){
	$.messager.confirm('提示', '你确认撤销这条配置信息么？', function(result){
		if (result){
			var url = contentPath+"/transferDatumConfigHander/revokeConfigInfo?config_id="+id;
			Util.getAjaxData(url, null, function(data){
				if(data){
					Util.showMsg("操作成功");
					$("#transferDatumConfig_list_data").datagrid("reload");
				}else{
					Util.showMsg("操作失败");
				}
			})
		}
	});
}
function preview(id,name){
	Util.window("预览", "dialog_content", "view/transferMonitor/transferDatumConfig/previewTransferConfig.jsp?config_id="+id+"&name="+name, 900, 650);
}
