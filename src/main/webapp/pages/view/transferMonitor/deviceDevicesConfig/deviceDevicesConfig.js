$(function(){
	var height = $(window).height();
	$("#myflow").css({ "overflow-y": "auto", "height": height-140 });
	var url = contentPath+"/transferDeviceToplogyConfigHander/queryDeviceArea";
	var html = "<ul>";
	Util.getAjaxData(url, null, function(data){
		var temp = 0;
		$.each(data, function(i,val){
	       html += "<li class='areaIndexCodeDiv' id='"+val.map.muncpl_id+"' ><a href='javascript:void(0);'>"+val.map.muncpl+"</a></li>";
		});   
		html += "<li class='areaIndexCodeDiv' id='540000'><a href='javascript:void(0);'>全区</a></li>";
		html += "</ul><div class='left-menu-bottom'><i class='icon-chevron-down'></i></div>";
		$("#areaConfigDivId").html(html);
	}, false); 
	
	$(".areaIndexCodeDiv").on("click",function(){
		var areaId = $(this).siblings().removeClass('active').end().addClass("active").attr("id");
		loadingCenterNodeInfo(areaId);
	});
	$(".areaIndexCodeDiv").addClass("active").last().trigger("click");
});

function loadingCenterNodeInfo(areaId){
	Util.getAjaxData(contentPath+"/transferMonitorHander/getCenterNodeInfo","areaCode="+areaId,function(data){
		var result = data[0].children;
		var htmlStr = "";
		for(var i = 0; i<result.length; i++){
			htmlStr += "<div id='deviceflow_tools_handle' style='text-align: center;' class='ui-widget-header'>"+result[i].text+"</div>";
			var resultChildren = result[i].children;
			for(var j = 0; j<resultChildren.length;j++){
				htmlStr += "<div class='node state stateClass' id='"+resultChildren[j].id+"' type='state'><img src='../resources/js/myflow/img/16/start_event_empty.png' />"+resultChildren[j].text+"</div>";
			}
		}
		$('#centerContext').html(htmlStr);
	},false);
	
	Util.getAjaxData(contentPath+"/transferMonitorHander/getTransferDeviceConfigByAreaId","areaCode="+areaId,function(result){
		var resultStr = JSON.stringify(result.data).replace(/\"/g,"'");
		$('#myflow').myflow({ 
			basePath : "",
			restore : eval("("+resultStr+")"),
			tools : {
				save : {
					onclick : function(data) {
						Util.getAjaxData(contentPath+"/transferMonitorHander/saveTransferDeviceConfigByAreaId?areaId="+areaId,"data="+data,function(data){
							if(data.success){
								alert("配置信息保存成功");
							}else{
								alert(data.msg);
							}
						},false);
					}
				}
			}
		});
	},false);
}
