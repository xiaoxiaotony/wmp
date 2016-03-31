<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
 	String ip = request.getParameter("ip");
 	String port = request.getParameter("port");
%>
<div id="win_content">
	<textarea id="telnet_result_box" style="height: 224px;width: 434px;"></textarea>
</div>
<div class="dialog_footer" data-options="region:'south',border:false"
				style="text-align: right">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="javascript:$('#dialog_content').dialog('close')"
					iconcls="icon-cancel" style="width: 80px">关闭</a>
</div>
<script>
function ajaxLoading(){ 
    $("<div class=\"datagrid-mask\"></div>").css({display:"block",width:"100%",height:$(window).height()}).appendTo("body"); 
    $("<div class=\"datagrid-mask-msg\"></div>").html("正在处理，请稍候。。。").appendTo("body").css({display:"block","padding":"10px 5px 25px 30px","z-index": "999999",left:($(document.body).outerWidth(true) - 190) / 2,top:($(window).height() - 45) / 2}); 
} 
function ajaxLoadEnd(){ 
     $(".datagrid-mask").remove(); 
     $(".datagrid-mask-msg").remove();             
} 
var toIp = "<%=ip%>";
var port = "<%=port%>";
ajaxLoading();
var url = contentPath+"/netmonitorHander/telentNetworkInfo?ip="+toIp+'&port='+port;
Util.getAjaxData(url,"",function(data){
	ajaxLoadEnd();
	//1 是成功，0是失败
	if(0 == data.data){
		 $("#telnet_result_box").html("连接"+toIp+"...无法打开到主机的连接。 在端口 "+port+": 连接失败");
	}else{
		 $("#telnet_result_box").html("telnet设备"+toIp+"端口"+port+"成功");
	}
});
</script>


