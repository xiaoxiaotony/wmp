<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%
String path = request.getContextPath();
%>

<div title="运维值班" data-options="closable:true">
	<div id='calendar'></div>
</div>

<script>
$(document).ready(function() {
	var selectDates = [];
	
	var format = function(fmt,date){
	  var o = {   
	    "M+" : date.getMonth()+1,                 //月份   
	    "d+" : date.getDate(),                    //日   
	    "h+" : date.getHours(),                   //小时   
	    "m+" : date.getMinutes(),                 //分   
	    "s+" : date.getSeconds(),                 //秒   
	    "q+" : Math.floor((date.getMonth()+3)/3), //季度   
	    "S"  : date.getMilliseconds()             //毫秒   
	  };   
	  if(/(y+)/.test(fmt))   
	    fmt=fmt.replace(RegExp.$1, (date.getFullYear()+"").substr(4 - RegExp.$1.length));   
	  for(var k in o)   
	    if(new RegExp("("+ k +")").test(fmt))   
	  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
	  return fmt;   
	};

	var date = new Date();
	var d = date.getDate();
	var m = date.getMonth();
	var y = date.getFullYear();
	
	var calendar = $('#calendar').fullCalendar({
		header: {
			left: 'prev,next today',
			center: 'title',
			right: 'a'
		},
		selectable: false,
		selectHelper: false,
		dayClick : function(date, allDay, jsEvent, view) {
			var params = format("yyyy-MM-dd",date);
			var checkEle = $('#calendar td[data-date='+params+']');
			if(!checkEle.hasClass('fc-other-month')){
				if(checkEle.hasClass('d_active')){
					checkEle.removeClass('d_active');
					selectDates.splice(selectDates.indexOf(params),1);
				}else{
					checkEle.addClass('d_active');
					selectDates.push(params);
				}
			}

		},
		editable: false,
		events: function(start,end,callback){
			var url = contentPath+"/dutyHander/getDutyInfoByMonth";
			var params  = {
					'startDate' : format("yyyy-MM-dd",start),
					'endDate' : format("yyyy-MM-dd",end)
			}
			
			Util.getAjaxData(url, params, function(data){
				var event = [];
				for (var i = 0, iLen = data.length; i < iLen; i++) {
					if(data[i].map.username){
						var litem = {};
						litem['title'] = data[i].map.username || '';
						litem['start'] = data[i].map.duty_date ||'';
						event.push(litem);
					}
				}
				callback(event);
			},true);
			
			
		}
		
	});
	
	$('.fc-header-right').html('<span class="fc-button fc-button-today fc-state-default fc-corner-left fc-corner-right">排班</span>');

	$('.fc-header-right .fc-button').off('click','span').on('click',function(){
		if(selectDates.length <= 0){
			if(!confirm("是否刷新排班！")){
				return;
			}
		}else{
			if(!confirm("是否对所选日期进行排班！")){
				return;
			}
		}
		
		var url = contentPath+"/dutyHander/addDuty";
		var params  = {
				'dates' : selectDates.join(",")
		}
		Util.getAjaxData(url, params, function(data){
			if(data.data){
				alert("排班信息刷新成功!!");
				$('#calendar').fullCalendar('refetchEvents');
			}
		},true);
		
	});

});
</script>

<style>
	body {
		margin-top: 40px;
		text-align: center;
		font-size: 14px;
		font-family: "Lucida Grande",Helvetica,Arial,Verdana,sans-serif;
		}

	#calendar {
		width: 900px;
		margin: 0 auto;
		}
	.d_active{
		background-color: red;
	}
</style>
