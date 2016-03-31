<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<% 
	String id = request.getParameter("id"); 
%>
<div title="气象通讯传输" data-options="closable:true">
	<div class="search_bar" id="dutyDetail_search_bar_div">
		 <div class="search_tit" style="width: 80px;">值班开始时间:</div>
		<div class="search_txt" id="time_id2"> 
		</div>
		 <div class="search_tit" style="width: 80px;">值班结束时间:</div>
		<div class="search_txt" id="etime_id2"> 
		</div>
		
		<div class="search_tit" style="width: 80px;">值班人:</div>
		<div class="search_txt" id="name_id2">
		</div> 
	</div>
	<div class="">
		<table id=""   border="1" bordercolor="#000000" style="border-collapse:collapse;width: 99%;">
			<tr height="50" id="wether_id" style="background-color: #EAEAEA;">
				<td >检查类型</td>
				<td>是否齐全</td>
				<td>缺报站点及时次</td>
				<td>故障原因</td>
				<td>详情</td>
			</tr>
			
		</table>
	</div>	
</div>
<script>
var id = "<%=id%>";
 $(function(){
	var url = contentPath+"/dutyHander/getDutyWeDetails?id="+id;
	Util.getAjaxData(url,null,function(data){
		      var jsonData = data;
             if (jsonData != null && jsonData != '')
             {
             	var items = jsonData.data['items'];
		    	if(items.length == 0)
		    	{
		    		return;
		    	}
             	var dutyPerson = jsonData.data['DUTY_PERSON'];
             	var dutyDate = jsonData.data['DUTY_DATE'];
             	var etime_id2 = jsonData.data['DUTY_END_DATE'];
             	var dutyStatus = jsonData.data['DUTY_STATUS'];
             	
             	$('#name_id2').html(dutyPerson);
             	$('#time_id2').html(dutyDate);
             	$('#etime_id2').html(etime_id2);
          		
          		var html = "";
          		var index = 0;
             	 $.each(items,function(n,item) {
             	 	var itemName = item['ITEM_NAME'] == null ? "" : item['ITEM_NAME'];
             	 	var loseStation = item['LOSE_STATION'] == null ? "" : item['LOSE_STATION'] ;
             	 	var troubleItem = item['TROUBLE_ITEM'] == null ? "" : item['TROUBLE_ITEM'] ;
             	 	var solve = item['SOLVE'] == null ? "" : item['SOLVE'] ;
             	 	var isall = item['ISALL'];
             	 	
             	 	html+="<tr>"+
						"<td>"+itemName+"</td>"+
						"<td>";
						if (isall == "0")
						{
							html+= "<input type='radio' value='0' checked='checked' name='isall"+index+"'/>全  <input value='1' type='radio' name='isall"+index+"'/>不全  ";
						}
						else
						{
						
							html+= "<input type='radio' value='0' name='isall"+index+"'/>全  <input value='1'  checked='checked'  type='radio' name='isall"+index+"'/>不全  ";
						}
							
						html+="</td><td><textarea rows='2' cols='2'>"+loseStation+"</textarea></td><td>";
							var checkHtml = "";
							checkHtml+="<input type='checkbox' value='0'/>通讯线路故障"+
							"<input type='checkbox' value='1'/>站点设备故障</br>"+
							"<input type='checkbox' value='2'/>站点电力故障"+
							"<input type='checkbox' value='3'/>其他";
							var strs= new Array();
							strs=troubleItem.split(","); 
							for (i=0;i<strs.length ;i++ ) 
							{ 
								if (strs[i] == "0")
								{
									checkHtml = checkHtml.replace(/value='0'/g, " value='0' checked='checked' ");
								}
								else if (strs[i] == "1")
								{
									checkHtml = checkHtml.replace(/value='1'/g, " value='1' checked='checked' ");
								}
								else  if (strs[i] == "2")
								{
									checkHtml = checkHtml.replace(/value='2'/g, " value='2' checked='checked' ");
								} 
								else if (strs[i] == "3")
								{
									checkHtml = checkHtml.replace(/value='3'/g, " value='3' checked='checked' ");
								}
							} 
						html+=checkHtml;
						html+="</td><td><textarea rows='2' cols='2'>"+solve+"</textarea></td></tr>";
             	 	index++;
             	 });
	             	 $('#wether_id').after(html);
             }
		},false);
}); 
</script>

