var currentNode = null;
var canvas,stage,scene;
$(function(){
	var fa_div = $('#content_div_id');
	var fa_w = fa_div.css("width");
	var fa_h = fa_div.css("height");
	 //设置画布的大小
	canvas = document.getElementById('transfterMonitor_canvas');        
	canvas.width = fa_w.substring(0,fa_w.length - 2);
	canvas.height = fa_h.substring(0,fa_h.length - 2);
    stage = new JTopo.Stage(canvas);
    stage.wheelZoom = 0.85; // 设置鼠标缩放比例
    stage.paint();
    //显示工具栏
    //showJTopoToobar(stage,"topoMonitor_button_bar_div");
    scene = new JTopo.Scene(stage);
	var url = contentPath+"/transferDeviceToplogyConfigHander/queryDeviceArea";
	var html = "<ul>";
	Util.getAjaxData(url, null, function(data){
		var temp = 0;
		$.each(data, function(i,val){
	       html += "<li class='areaTransferDeviceConfigDivId' id='"+val.map.muncpl_id+"' ><a href='javascript:void(0);'>"+val.map.muncpl+"</a></li>";
		});   
		html += "<li class='areaTransferDeviceConfigDivId' id='540000'><a href='javascript:void(0);'>全区</a></li>";
		html += "</ul><div class='left-menu-bottom'><i class='icon-chevron-down'></i></div>";
		$("#areaTransferDeviceConfigDivId").html(html);
	}, false); 
	
	$(".areaTransferDeviceConfigDivId").on("click",function(){
		var areaId = $(this).siblings().removeClass('active').end().addClass("active").attr("id");
		showNetWorkDevice(areaId);
	});
	$(".areaTransferDeviceConfigDivId").addClass("active").last().trigger("click");
});

function showNetWorkDevice(areaId){
     scene.clear();
     //设置背景图片
     //scene.background = '../images/bg.jpg';
     //设置背景为白色
     scene.alpha = 0.95;
     var tempArray = new Array();
     var url = contentPath+"/transferMonitorHander/getTransferDeviceConfigDetail?areaId="+areaId;
     var nodeMap = new Map();  
     Util.getAjaxData(url,"",function(data){
	       for(var i = 0; i< data.length; i++){
	    	 var map = data[i].map;
	    	   //创建节点 
	    	 var name = map.name;
    	     var location_x = parseInt(map.location_x);
    	     var location_y = parseInt(map.location_y);
    	     var fromNode = new JTopo.Node(name);
    	     fromNode.setLocation(location_x, location_y);
    	     //添加当前节点的ID属性
    	     fromNode.id = map.id;
    	     fromNode.setImage('../images/host.png', true);
    	     //设置节点禁止拖动
    	     //fromNode.dragable='false';
    	     fromNode.fontColor = "#777777";
    	     var key = map.name+"_"+location_x+"_"+location_y;
    	     nodeMap.put(key,fromNode);
    	     tempArray.push(key);
    	     var node = nodeMap.get(key); 
    	     scene.add(fromNode);
    	     fromNode.dbclick(function(event){
    	    	 currentNode = this;
    	    	 handlerDBlick(event);
    	     });
	       }
	},false);
     
    Util.getAjaxData(contentPath+"/transferMonitorHander/getTransferDeviceConfigLineDetail?areaId="+areaId,"",function(data){
    	var obj = data.data;
    	for(var p in obj){
    		var val = obj[p];
    		var fromNode = null,toNode = null;
    		if("from"==val[1].FROM_OR_TO){
    			fromNode = val[1].NAME+"_"+val[1].LOCATION_X+"_"+val[1].LOCATION_Y;
    		}
    		if("from"==val[0].FROM_OR_TO){
    			fromNode = val[0].NAME+"_"+val[0].LOCATION_X+"_"+val[0].LOCATION_Y;
    		}
    		if("to"==val[1].FROM_OR_TO){
    			toNode = val[1].NAME+"_"+val[1].LOCATION_X+"_"+val[1].LOCATION_Y;
    		}
    		if("to"==val[0].FROM_OR_TO){
    			toNode = val[0].NAME+"_"+val[0].LOCATION_X+"_"+val[0].LOCATION_Y;
    		}
    		var link = new JTopo.Link(nodeMap.get(fromNode), nodeMap.get(toNode)); 
    		scene.add(link);
		}
	},false);
}

function handlerDBlick(event){
	Util.window(currentNode.text+"详情", "dialog_content", "../pages/view/transferMonitor/deviceDevicesConfig/showTransferDeviceMonitorDetail.jsp?location_node_id="+currentNode.id, 900, 650);
}