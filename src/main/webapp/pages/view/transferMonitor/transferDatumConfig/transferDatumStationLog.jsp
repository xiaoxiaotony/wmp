<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% 
	String station = request.getParameter("station");
	String dtype = request.getParameter("dtype");
	String ctype = request.getParameter("ctype");
	String datetime = request.getParameter("datetime");
	String typename = java.net.URLDecoder.decode(request.getParameter("typename"), "utf-8");
	String station_name = java.net.URLDecoder.decode(request.getParameter("station_name"), "utf-8"); 
%>
<div style="position: absolute;width: 100%;height:auto;text-align: center;"><h3><%=typename %></h3><h4><%=station %>(<%=station_name %>)</h4></div>
<div style="text-align: center;padding-top: 40px;position:absolute; margin-top:8%; overflow: auto;width: 100%;height: 88%;">
				<ul id = "tfds_log">
<!-- 				   <li><img alt="" src="../images/host_min.png"><h5>新一代监控传输</h5></li> -->
<!-- 				   <li><div style="margin-right:auto;margin-left:auto;height: 100px;width: 0px;border: 1px solid black;padding-top: 4%;"><div style="width:100px;text-align: right;"><img src="../images/u283.png" width="35px;" height="35px;" /><span>fsdfs</span></div></div></li> -->
				</ul>
</div>

<div id="dialog_content_tfdc" ></div>
<script>
    $(function(){
    	$.ajax({
    		type: "POST",
    		url: contentPath+"/transferDatumMonitorHander/getStationNode",
    		data: {station:'<%=station %>'},
    		success: function(msg){
    			$.each(msg, function(i, data){
    				var html_node;
    			    var html_line;
//     			    console.log(i);
//     			    console.log(data);
    			    if(i == 0){
    			    	html_node = "<li><img style='cursor:pointer;' name='dev_name' src='../images/host_min.png' onclick='queryNodeLog(\""+data.DEVIED_ID+"\")'><h5>"+data.NODE_NAME+"</h5></li>";
    			    	$("#tfds_log").append(html_node);
    				}else if(i == 1){
    					html_node = "<li><img style='cursor:pointer;' name='dev_name' src='../images/host_min.png' onclick='queryNodeLog(\""+data.DEVIED_ID+"\")'><h5>"+data.NODE_NAME+"</h5></li>";
    					html_line = "<li><div style='margin-right:auto;margin-left:auto;height: 100px;width: 0px;border:1px solid black;padding-top:4%;'><div style='width:200px;text-align: right;'><img src='../images/u283.png' width='35px' height='35px' />&nbsp;&nbsp;<b style='color:red;font-size:15px;'><%=datetime %></b></div></div></li>";
    			        $("#tfds_log").append(html_line + html_node);
    				}else if( i > 1){
    					html_node = "<li><img style='cursor:pointer;' name='dev_name' src='../images/host_min.png' onclick='queryNodeLog(\""+data.DEVIED_ID+"\")'><h5>"+data.NODE_NAME+"</h5></li>";
    					html_line = "<li><div style='margin-right:auto;margin-left:auto;height: 100px;width: 0px;border:1px solid black;padding-top:4%;'></div></li>";
    			    	$("#tfds_log").append(html_line + html_node);
    				}
    			});
			    $("img[name='dev_name']").hover(
			    		function(){
			        		$(this).css("backgroundColor", "grey");
			    		},
			    		function(){
			        		$(this).css("backgroundColor", "white");
			    		}
			    );
			    
			    /**
			     *日志弹窗
			     *
			     **/
			 	$("#dialog_content_tfdc").dialog({
			 		title: '日志查看',
			 		width: 900,
			 		height: 650,
			 		closed: true,
			 		cache: false,
			 		modal: true,
			 		draggable:false
			 	});
			    /**
			     *日志弹窗 end
			     *
			     **/
    		}
    	});

    });
    	
/**
 * 查询日志
 */
function queryNodeLog(dev_id){
 	$("#dialog_content_tfdc").panel("move",{left:($(window).width() - 900) * 0.5,top:($(window).height() - 1950) * 0.5});
	$("#dialog_content_tfdc").dialog("refresh","view/transferMonitor/transferDatumConfig/transferDatumStationLogDev.jsp?dev_id="+dev_id);
 	$("#dialog_content_tfdc").dialog("open");
}
</script>