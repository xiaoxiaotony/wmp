//这个方法用来启动该页面的ReverseAjax功能
dwr.engine.setActiveReverseAjax(true);
//设置在页面关闭时，通知服务端销毁会话
dwr.engine.setNotifyServerOnPageUnload(true);
var nodeMap;
var currentNode = null;
var fa_div = $('#content_div_id');
var fa_w = fa_div.css("width");
var fa_h = fa_div.css("height");

//设置画布的大小
var canvas = document.getElementById('topoMonitor_canvas');        
canvas.width = fa_w.substring(0,fa_w.length - 2);
canvas.height = fa_h.substring(0,fa_h.length - 2);

var stage = new JTopo.Stage(canvas);
//显示工具栏
//showJTopoToobar(stage,"topoMonitor_button_bar_div");
stage.wheelZoom = 0.85; // 设置鼠标缩放比例
function init_topo(){
     stage.addEventListener("dbclick", function(event){
    	 runPrefixMethod(stage.canvas, "RequestFullScreen")
     });
     var scene = new JTopo.Scene(stage);
     scene.background = '../images/bg.jpg';
     var url = contentPath+"/netmonitorHander/getNetworkNodeConfigDetail";
     nodeMap = new Map();
     var tempArray = new Array();
     Util.getAjaxData(url,"",function(data){
	       for(var i = 0; i< data.length; i++){
	    	 var map = data[i].map;
	    	   //创建节点
	    	 var name = map.name;
    	     var location_x = parseInt(map.location_x);
    	     var location_y = parseInt(map.location_y);
    	     var fromNode = new JTopo.Node(name);
    	     fromNode.setLocation(location_x, location_y);
    	     if(map.to_status == 1){
    	    	 fromNode.setImage('../images/green.png', true);
    	     }else{
    	    	 fromNode.setImage('../images/red.png', true);
    	     }
    	     fromNode.id = map.id;
    	     fromNode.ip = map.ip;
    	     //设置节点禁止拖动
    	     fromNode.dragable='false';
    	     var key = map.name+"_"+location_x+"_"+location_y;
    	     nodeMap.put(key,fromNode);
    	     tempArray.push(key);
    	     var node = nodeMap.get(key); 
    	     scene.add(fromNode);
    	     fromNode.mouseover(function(event){
    	    	 currentNode = this;
    	    	 handlerMouseover(event);
    	     });
    	     
    	     fromNode.addEventListener('mouseup', function(event){
                 currentNode = this;
                 handler(event);
             });
    	     fromNode.addEventListener('mouseup', function(event){
                 currentNode = this;
                 handler(event);
             });
    	     
    	     stage.click(function(event){
                 if(event.button == 0){// 右键
                     // 关闭弹出菜单（div）
                     $("#contextmenu_fun").hide();
                 }
             });
    	     
	       }
	},false);
     
    Util.getAjaxData(contentPath+"/netmonitorHander/getNetWorkNodeConfigLineDetail","",function(data){
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

function showNetworkAlarm(key,status){
	   var node = nodeMap.get(key); 
	   if(status == 0){
		   node.setImage('../images/red.png', true);
	   }else{
		   node.setImage('../images/green.png', true);
	   }
}

$(function(){
	//加载topo
	init_topo();
});

function handler(event){
    if(event.button == 2){// 右键
        // 当前位置弹出菜单（div）
        $("#contextmenu_fun").css({
            top: event.pageY-150,
            left: event.pageX-150
        }).show();    
    }
}

$("#contextmenu_fun a").click(function(){
   var text = $(this).text();
   var toIp = currentNode.ip;
   if(text == 'ping'){
    	$('#dialog_content').dialog({
    		title: 'ping设备 '+toIp,
    		width: 450,
    		height: 280,
    		closed: false,
    		cache: false,
    		href: 'view/netmonitor/monitoringTopGplot/showPingDetail.jsp?ip='+toIp,
    		modal: true
    	});
    }else if(text == 'telnet'){
    	$.messager.prompt('提示:','请输入telnet端口:',function(port){   
    		if(port){   
    			$('#dialog_content').dialog({
    				title: 'telnet设备 '+toIp,
    				width: 450,
    				height: 280,
    				closed: false,
    				cache: false,
    				href: 'view/netmonitor/monitoringTopGplot/telnetPingDetail.jsp?ip='+toIp+'&port='+port,
    				modal: true
    			});
    		}   
		})  
    }
    $("#contextmenu_fun").hide();
});


function handlerMouseover(event){
	var ip = currentNode.ip;
	$("#handlerMouseover").html("").html(ip).css({
        top: event.pageY-150,
        left: event.pageX-150
    }).show(); 
}


function alertNetworkAlarm(context){
	$.messageBox.lays(300, 200);
	$.messageBox.anim('fade', 2000);
	$.messageBox.show("网络告警",context,0);
	if(navigator.userAgent.indexOf("MSIE")>0){// IE         
		var player = document.createElement('bgsound');     
		player.id = 'div_wav';    
		player.src = '../resources/mp3/alarm.wav';     
		player.setAttribute('autostart', 'true');     
		document.body.appendChild(player);  
	}else{ // Other FF Chome Safari Opera         
		var player = document.createElement('audio');     
		player.id = 'div_wav';    
		player.setAttribute('autoplay','autoplay');     
		document.body.appendChild(player);         
		var mp3 = document.createElement('source');    
		mp3.src = '../resources/mp3/alarm.wav';   
		mp3.type= 'audio/ogg'; 
		player.appendChild(mp3);          
	} 
}
