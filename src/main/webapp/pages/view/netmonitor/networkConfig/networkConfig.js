$(function(){
	var height = $(window).height();
	var width = $(window).width();
	$("#myflow").css({"overflow":"auto", "height": height-160, "width":width-188 });
	Util.getAjaxData(contentPath+"/netmonitorHander/getNetworkTopArchitectureConfigleft","",function(data){
		var result = data[0].children;
		var htmlStr = "";
		for(var i = 0; i<result.length; i++){
			htmlStr += "<div id='myflow_tools_handle' style='text-align: center;' class='ui-widget-header'>"+result[i].text+"</div>";
			var resultChildren = result[i].children;
			for(var j = 0; j<resultChildren.length;j++){
				htmlStr += "<div class='node state' id='"+resultChildren[j].id+"' type='state'><img src='../resources/js/myflow/img/16/start_event_empty.png' />"+resultChildren[j].text+"</div>";
			}
		}
		$('#path').after(htmlStr);
	},false);
	
	Util.getAjaxData(contentPath+"/netmonitorHander/getNetworkConfig",null,function(result){
		var resultStr = JSON.stringify(result.data).replace(/\"/g,"'");
		$('#myflow').myflow({ 
			basePath : "",
			restore : eval("("+resultStr+")"),
			tools : {
				save : {
					onclick : function(data) {
						Util.getAjaxData(contentPath+"/netmonitorHander/saveNetworkConfig","data="+data,function(data){
							if(data.success){
								alert("配置信息保存成功");
							}else{
								alert("网络配置信息保存失败");
							}
						},false);
					}
				}
			}
		});
	},false);
	
});
