/**
 * 
 */
function parseFtpLog(){
	var log = $("#sourceLog").val();
	var url = contentPath+"/commonHander/praseFtpLog";
	Util.getAjaxData(url, {ftpLog:log}, function(data){
		$("#cont_three_textarea_div").val(data.data);
	}, false)
}

function explorOut(){
	var result = $("#cont_three_textarea_div").val();
	window.location.href=contentPath+"/commonHander/explorOut?content="+result;
}