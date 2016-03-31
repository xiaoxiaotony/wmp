<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<% 
	String id = request.getParameter("id"); 
%>
<div title="值班日志详情" data-options="closable:true">
	<div class="search_bar" id="dutyDetail_search_bar_div">
		 <div class="search_tit" style="width: 80px;">值班开始时间:</div>
		<div class="search_txt" id="time_id3"> 
		</div>
		 <div class="search_tit" style="width: 80px;">值班结束时间:</div>
		<div class="search_txt" id="etime_id3"> 
		</div>
		
		<div class="search_tit" style="width: 80px;">值班人:</div>
		<div class="search_txt" id="name_id3">
		</div> 
	</div>
	<div class="">
		<table id=""  border="1" bordercolor="#000000" style="border-collapse:collapse;width: 99%;">
			<tr height="50"  align="center"  id="net_id" style="background-color: #EAEAEA;">
				<td width="20%">检查类型类型</td>
				<td width="30%">检查项</td>
				<td width="20%">是否正常</td>
				<td>详情</td>
			</tr>
			
		</table>
	</div>	
	</br>
	故障及解决方法：
	<div id="" style="">
	<textarea id="content_id1" rows="" cols="" style="width: 99%;"></textarea>
	</div>
</div>
<script>
var id = "<%=id%>";
$(function(){
	var url = contentPath+"/dutyHander/getDutyLogDetails?id="+id;
	Util.getAjaxData(url,null,function(data){
		     var jsonData = data;
             if (jsonData != null && jsonData != '')
             {
             	var dutyPerson = jsonData.data['DUTY_PERSON'];
             	var dutyDate = jsonData.data['DUTY_DATE'];
             	var dutyEndDate = jsonData.data['DUTY_END_DATE'];
             	var dutyFaultReason = jsonData.data['DUTY_FAULT_REASON'];
             	$('#name_id3').html(dutyPerson);
             	$('#time_id3').html(dutyDate);
             	$('#etime_id3').html(dutyEndDate);
             	 $('#content_id1').val(dutyFaultReason);
          		var items = jsonData.data['items'];
          		var html = "";
          		var isFirst = false;
          		var indexDe = 0;
             	 $.each(items,function(n,item) {
             	 	isFirst = true;
             	 	var itemName = item['ITEM_NAME'] == null ? "" : item['ITEM_NAME'];
             	 	var itemExplain = item['ITEM_EXPLAIN'] == null ? "" : item['ITEM_EXPLAIN'] ;
             	 	var details = item['details'];
             	 	var detailLength = details.length;
             	 	 $.each(details,function(b,detail) {
             	 	 	var detailName = detail['DETAIL_NAME'];
             	 	 	var isnomal = detail['ISNOMAL'];
             	 	 	if (isFirst)
             	 	 	{
             	 	 		html+= "<tr>"+
								"<td  rowspan='"+detailLength+"'>"+itemName+"</td>"+
								"<td>"+detailName+"</td>"+
								"<td>";
							if (isnomal == "0")
							{
								html+="<input type='radio' value='0' checked='checked'  name='isnormal"+indexDe+"'/>正常  <input type='radio' value='1'  name='isnormal"+indexDe+"'/>不正常";
							}
							else
							{
								html+="<input type='radio' value='0' name='isnormal"+indexDe+"'/>正常  <input type='radio'  checked='checked' value='1'  name='isnormal"+indexDe+"'/>不正常";
							}

							html+="</td>"+
								"<td class='textareaclass' rowspan='"+detailLength+"'>"+itemExplain+"</td>"+
							"</tr>";
             	 	 	}
             	 	 	else
             	 	 	{
             	 	 		html+= "<tr>"+
								"<td>"+detailName+"</td>"+
								"<td>";
									if (isnomal == "0")
									{
										html+="<input type='radio' value='0' checked='checked'  name='isnormal"+indexDe+"'/>正常  <input type='radio' value='1'  name='isnormal"+indexDe+"'/>不正常";
									}
									else
									{
										html+="<input type='radio' value='0' name='isnormal"+indexDe+"'/>正常  <input type='radio'  checked='checked' value='1'  name='isnormal"+indexDe+"'/>不正常";
									}
							html+="</td>"+
							"</tr>";
             	 	 	}
             	 		isFirst = false;
             	 		indexDe++;
             	 	 });
             	 });
             	 $('#net_id').after(html);
             }
		},false);
});
</script>

