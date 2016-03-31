$(function(){
	tabChange("../pages/view/transferMonitor/monitorCount/transferMonitorCountDay.jsp");
	$(".search-tab li").on('click',function(){
		$(".search-tab li").removeClass("active");
		$(this).addClass("active");
		tabChange($(this).attr("tab"));
	});
});

function tabChange(url){
	$.ajax({
		  url: url,
		  dataType:"html",
		  cache: false,
		  success: function(html){
		    $("#transferMonitorCount_con").html(html);
		  }
	});
}