<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% 
	String config_id = request.getParameter("config_id");
	String name = request.getParameter("name");
%>
<div style="position: absolute;width: 100%;text-align: center;"><h3><%=name %>区域传输节点</h3></div>
<div id="fa_div" style="position: absolute; margin-top:6%; overflow: hidden;">
	<div title="" data-options="closable:true">
		<div>
		    <canvas id="previewTransferConfig_canvas" style="background-color:gray; cursor: default;">
		    </canvas>
		</div>
		<!--底部分页 end-->
	</div>
</div>
<script type="text/javascript" src="../resources/js/plugins/jtopo/jquery.snippet.min.js"></script>
<script type="text/javascript" src="../resources/js/plugins/jtopo/jtopo-0.4.8-min.js"></script>
<script>
var canvas,stage,scene;
$(function(){
	var config_id = "<%=config_id%>";
	canvas = document.getElementById('previewTransferConfig_canvas');        
	canvas.width = 880;
	canvas.height = 530;
    stage = new JTopo.Stage(canvas);
    stage.paint();
    scene = new JTopo.Scene(stage);
    scene.clear();
    //设置背景图片
    //scene.background = '../images/bg.jpg';
    scene.alpha = 0.95;//设置背景为白色
    var tempArray = new Array();
    var url = contentPath+"/transferDatumConfigHander/getPreviewTransferConfig?config_id="+config_id;
    Util.getAjaxData(url,"",function(data){
	   for(var i = 0; i< data.length; i++){
		 var map = data[i].map;
    	   //创建节点 
    	 var name = map.node_name;
	     var fromNode = new JTopo.Node(name);
	     fromNode.setLocation(75+(i*200), 200);
	     //添加当前节点的ID属性
	     fromNode.id = map.node_id;
	     fromNode.setImage('../images/host_min.png', true);
	     //设置节点禁止拖动
	     //fromNode.dragable='false';
	     fromNode.fontColor = "#777777";
	     tempArray.push(fromNode);
	     scene.add(fromNode);
       }
	},false);
    for(var k = 0; k<tempArray.length; k++){
		var fromNode = tempArray[k];
		if(k+1 == tempArray.length){
			return;
		}
		var toNode = tempArray[k+1]
		var link = new JTopo.Link(fromNode, toNode, 'Arrow'); 
		link.dashedPattern = 3; // 虚线
        link.bundleGap = 20; // 线条之间的间隔
        link.strokeColor = '0,200,255';
        arrowsRadius = 10;
		scene.add(link);
	}
})
</script>

