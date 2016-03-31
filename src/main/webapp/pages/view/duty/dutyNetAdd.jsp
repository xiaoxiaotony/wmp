<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<% 
	String id = request.getParameter("id"); 
%>
<div title="网络管理录入" data-options="closable:true">

	<!-- 工具栏 -->
	<div class="cont_tit" id="duty_opt_bar_div"></div>

	<div class="search_bar" id="dutyInfo_search_bar_div">
		 <div class="search_tit" style="width: 65px;">时间：</div>
		<div class="search_txt" id="time_id"> 
		</div>
		
		<div class="search_tit" style="width: 65px;">值班人：</div>
		<div class="search_txt" id="name_id">
			<!-- <select id="dutyPersonId" name="role" class="easyui-combobox" style="width:178px; height: 32px;" data-options="
    					                                valueField:'USERID',
    					                                textField:'USERNAME',
    					                                panelHeight:'auto',
    					                                editable:false,
    					                                url:'../http/dutyHander/queryUsers',
    					                                onLoadSuccess:roleLoad
    					                                "></select> -->
		    <!--  <input id="dutyPersonId" style="width: 250px;height: 32px;" class=""  /> -->
		</div> 
		
		<div class="search_tit" style="width: 60%;margin-right: 0px;">值班状态:&nbsp;</div>
		<div class="search_txt" id="status_id" style="width: 4%;margin-right: 10px;">
		</div> 
	</div>
	<div class="">
		<table id=""   border="1" bordercolor="#000000" style="border-collapse:collapse;width: 99%;">
			<tr height="50" align="center" id="item_id"  style="background-color: #EAEAEA;">
				<td width="20%">检查类型类型</td>
				<td width="20%">检查项</td>
				<td width="20%">是否正常</td>
				<td>详情</td>
			</tr>
			<tr class="item_class first">
				<td  rowspan="4">网络运行</td>
				<td>
					全区气象宽带网
				</td>
				<td>
						<input type="radio" value='0' checked="checked" name="isnormal1"/>正常  <input type="radio" value='1' name="isnormal1"/>不正常
				</td>
				<td rowspan="4"><textarea></textarea></td>
			</tr>
			<tr class="item_class">
				<td  height="30">
					区局内网
				</td>
				<td>
						<input type="radio" value='0'  checked="checked" name="isnormal2"/>正常  <input type="radio" value='1' name="isnormal2"/>不正常
				</td>
			</tr>
			<tr class="item_class">
				<td  height="30">
					核心路由器
				</td>
				<td>
						<input type="radio" value='0'  checked="checked" name="isnormal3"/>正常  <input type="radio" value='1' name="isnormal3"/>不正常
				</td>
			</tr>
			<tr class="item_class">
				<td  height="30">
					核心交换机
				</td>
				<td>
						<input type="radio" value='0'  checked="checked" name="isnormal4"/>正常  <input type="radio" value='1' name="isnormal4"/>不正常
				</td>
			</tr>
			
			
			<tr class="item_class first">
				<td  rowspan="6">新一代</td>
				<td>
					服务器SRV1-9
				</td>
				<td>
					<input type="radio" value='0'  checked="checked" name="isnormal5"/>正常  <input type="radio" value='1' name="isnormal5"/>不正常
				</td>
				<td rowspan="6"><textarea></textarea></td>
			</tr>
			<tr  class="item_class">
				<td  height="30">
					HTD RSEA-100接收机
				</td>
				<td>
						<input type="radio" value='0'  checked="checked" name="isnormal6"/>正常  <input type="radio" value='1' name="isnormal6"/>不正常
				</td>
			</tr>
			<tr  class="item_class">
				<td  height="30">
					HPStorageWorks
				</td>
				<td>
						<input type="radio" value='0'  checked="checked" name="isnormal7"/>正常  <input type="radio" value='1' name="isnormal7"/>不正常
				</td>
			</tr>
			<tr  class="item_class">
				<td  height="30">
					EMC CX4-240盘阵
				</td>
				<td>
						<input type="radio" value='0'  checked="checked" name="isnormal8"/>正常  <input type="radio" value='1' name="isnormal8"/>不正常
				</td>
			</tr>
			<tr  class="item_class">
				<td  height="30">
					IBM Storwize V7000盘阵
				</td>
				<td>
					<input type="radio" value='0'  checked="checked" name="isnormal9"/>正常  <input type="radio" value='1' name="isnormal9"/>不正常
				</td>
			</tr>
			<tr  class="item_class">
				<td  height="30">
					业务系统工作状态
				</td>
				<td>
					<input type="radio" value='0'  checked="checked" name="isnormal10"/>正常  <input type="radio" value='1' name="isnormal10"/>不正常
				</td>
			</tr>
			
			<tr class="item_class first">
				<td  rowspan="6">自动站</td>
				<td>
					华云自动站
				</td>
				<td>
						<input type="radio" value='0'  checked="checked" name="isnormal11"/>正常  <input type="radio" value='1' name="isnormal11"/>不正常
				</td>
				<td rowspan="6"><textarea></textarea></td>
			</tr>
			
			<tr  class="item_class">
				<td  height="30">
					无锡自动站
				</td>
				<td>
						<input type="radio" value='0'  checked="checked" name="isnormal12"/>正常  <input type="radio" value='1' name="isnormal12"/>不正常
				</td>
			</tr>
			
			<tr  class="item_class">
				<td  height="30">
					北斗通讯系统
				</td>
				<td>
						<input type="radio" value='0'  checked="checked" name="isnormal13"/>正常  <input type="radio" value='1' name="isnormal13"/>不正常
				</td>
			</tr>
			
			<tr  class="item_class">
				<td  height="30">
					土壤水分自动站
				</td>
				<td>
						<input type="radio" value='0'  checked="checked" name="isnormal14"/>正常  <input type="radio" value='1' name="isnormal14"/>不正常
				</td>
			</tr>
			
			<tr  class="item_class">
				<td  height="30">
					监控系统状态
				</td>
				<td>
						<input type="radio" value='0'  checked="checked" name="isnormal15"/>正常  <input type="radio" value='1' name="isnormal15"/>不正常
				</td>
			</tr>
			<tr  class="item_class">
				<td  height="30">
					交通站
				</td>
				<td>
						<input type="radio" value='0'  checked="checked" name="isnormal16"/>正常  <input type="radio" value='1' name="isnormal16"/>不正常
				</td>
			</tr>
			
			
			<tr class="item_class first">
				<td  rowspan="4">CIMISS</td>
				<td>
					服务器
				</td>
				<td>
						<input type="radio" value='0'  checked="checked" name="isnormal17"/>正常  <input type="radio" value='1' name="isnormal17"/>不正常
				</td>
				<td rowspan="4"><textarea></textarea></td>
			</tr>
			
			<tr  class="item_class">
				<td  height="30">
					EMC盘阵(注明硬盘位置)
				</td>
				<td>
						<input type="radio" value='0'  checked="checked" name="isnormal18"/>正常  <input type="radio" value='1' name="isnormal18"/>不正常
				</td>
			</tr>
			<tr  class="item_class">
				<td  height="30">
					网络设备
				</td>
				<td>
						<input type="radio" value='0'  checked="checked" name="isnormal19"/>正常  <input type="radio" value='1' name="isnormal19"/>不正常
				</td>
			</tr>
			<tr  class="item_class">
				<td  height="30">
					业务系统工作状态
				</td>
				<td>
						<input type="radio" value='0'  checked="checked" name="isnormal20"/>正常  <input type="radio" value='1' name="isnormal20"/>不正常
				</td>
			</tr>
			
			<tr class="item_class first">
				<td  rowspan="1">行业服务</td>
				<td>
					空指服务器
				</td>
				<td>
						<input type="radio" value='0'  checked="checked" name="isnormal21"/>正常  <input type="radio" value='1' name="isnormal21"/>不正常
				</td>
				<td rowspan="1"><textarea></textarea></td>
			</tr>
			<tr class="item_class first">
				<td  rowspan="2">共享服务</td>
				<td>
					CMAGXJ（233）
				</td>
				<td>
						<input type="radio" value='0'  checked="checked" name="isnormal22"/>正常  <input type="radio" value='1' name="isnormal22"/>不正常
				</td>
				<td rowspan="2"><textarea></textarea></td>
			</tr>
			
			<tr  class="item_class">
				<td  height="30">
					gxj（211）
				</td>
				<td>
						<input type="radio" value='0'  checked="checked" name="isnormal23"/>正常  <input type="radio" value='1' name="isnormal23"/>不正常
				</td>
			</tr>
			
		</table>
	</div>	
	</br>
	<span style="font-size: 18px;">故障及解决方法：</span>
	<div id="" style="">
	<textarea id="content_id" rows="" cols="" style="width: 99%;"></textarea>
	</div>
	<input type="hidden" id="dutyid"/>
	<input type="hidden" id="statusid"/>
</div>
<script>
var id = "<%=id%>";
var isSave = false; 
Date.prototype.Format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "H+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

var toolbarParams = {
	boolbarId : "duty_opt_bar_div",
	title : "信息网络中心网络维护值班日志",
	body :[{
		icon : "icon-plus",
		text : "换班",
		hidden : false,
		event : function(){
			change_work();
		}
	},{
		icon : "icon-plus",
		text : "保存",
		hidden : false,
		event : function(){
              net_opt();
		}
	}
	]
}
DataGrid.initToolsBarPanel(toolbarParams);

function change_work()
{
		var id = $('#dutyid').val(); 
		if (id == null || id == '')
		{
			topCenter("还未保存");
			return;
		}
	
		var statusVal = $('#statusid').val();
		if (statusVal == "1")
		{
			topCenter("换班已完成");
			return;
		}
		var url = contentPath+"/dutyHander/updateDutyStatus?id="+id;
		 $.messager.confirm('消息', '你确定要换班嘛？', function(r){
		 if (r){
				Util.getAjaxData(url,"",function(data){
				if (data.data.success)
				{
					$('#statusid').val("1");
					$('#status_id').html("结束");
					topCenter("换班成功");
				}
				else
				{
					topCenter("换班失败");
				}
				},false);
		}
	});
		
}

function net_opt()
{
	 var data = "[";
	var length = 0;
	var detailData = "";
	$('.item_class').each(function(i){
	var res = $(this).is(".first");
	if (res)
	{
		$(this).children().each(function(j){
				if (j == 0)
				{
					length = Number(length)+  Number($(this).attr("rowspan"));
					 detailData = "\"details\":[";
					var itemName = $(this).html();
					data +="{\"itemName\":\""+itemName+"\",";
				} 
				else if (j == 1)
				{
					var dname = $(this).html();
					detailData +="{\"detail\":\""+dname+"\"";
				}
				 else if (j == 2)
				{
					var redVal = "";
					$(this).children().each(function(n){
						if ($(this).is(":checked"))
						{
							redVal = $(this).val();
						}
					});
					detailData +=",\"isnomal\":\""+redVal+"\"}";
				}
				else if (j == 3)
				{
						var detailExplain = $(this).find("textarea").val();
						data +="\"itemExplain\":\""+detailExplain+"\",";
				}  
		}); 
	}
	else
	{
		  $(this).children().each(function(j){
				if (j == 0)
				{
					var dname = $(this).html();
					detailData +=",{\"detail\":\""+dname+"\"";
				} 
				else if (j == 1)
				{
					var redVal = "";
					$(this).children().each(function(n){
						if ($(this).is(":checked"))
						{
							redVal = $(this).val();
						}
					});
					
					detailData +=",\"isnomal\":\""+redVal+"\"}";
				}
		});   
		
		}
		
		 if (length -1 == i)
		{
			var lastIndex = Number($('.item_class').length) - 1;
			if (i == lastIndex)
			{
				detailData+="]}"
			}
			else
			{
				detailData+="]},"
			}
		} 
		 
		data+=detailData;
		detailData = "";
	});
		data += "]";
	 //var name = $('#dutyPersonId').combobox('getText');
	var time = $('#time_id').html();
	var reason = $('#content_id').val(); 
	var dutyid = $('#dutyid').val(); 
	
	 if (dutyid == null || dutyid == '')
	{
		if (isSave)
		{
			topCenter("已经保存");
			return;
		}
		var url = contentPath+"/dutyHander/saveNetDutyLog?itemData="+data+"&time="+time+"&reason="+reason;
		Util.getAjaxData(url,"",function(data){
		if (data.data.success)
		{
			isSave =  true;
			 $('#status_id').html("值班中");
			 $('#dutyid').val(data.data.id);
			topCenter("保存成功");
		}
		else
		{
			topCenter("保存失败");
		}
		},true);
	}
	else
	{
		var id = $('#dutyid').val(); 
		if (id == null || id == '')
		{
			return;
		}
		
		var statusVal = $('#statusid').val();
		if (statusVal == "1")
		{
			topCenter("值班结束不能修改");
			return;
		}
		
		var url = contentPath+"/dutyHander/updateNetDutyLog?itemData="+data+"&time="+time+"&reason="+reason+"&name="+name+"&id="+id;
		Util.getAjaxData(url,"",function(data){
		if (data.data.success)
		{
			$('#dutyid').val(data.data.id);
			topCenter("修改成功");
		}
		else
		{
			topCenter("修改失败");
		}
		},true);
	}
	 
		
		
}
/* var sename = "";
//为下拉列表选择一个默认的
function roleLoad(param){
	$('#dutyPersonId').combobox('setValue', sename);
} */
$(function(){
	var time1 = new Date().Format("yyyy-MM-dd HH:mm:ss");
	$("#time_id").html(time1);
	var url = contentPath+"/dutyHander/queryNetInfo";
	Util.getAjaxData(url,"",function(data){
			var jsonData = data;
			var items = jsonData.data['items'];
			var dutyPerson = jsonData.data['DUTY_PERSON'];
			var username = jsonData.data['username'];
			if (dutyPerson != null && dutyPerson != "")
            {
	            $('#name_id').html(dutyPerson);
            }
            else
            {
            	  $('#name_id').html(username);
            }
			
			if(items.length == 0)
		    {
		    		$('#status_id').html("未值班");
		    		return;
		    }
			
			var dutyFaultReason = jsonData.data['DUTY_FAULT_REASON'];
			var id = jsonData.data['ID'];
			var dutyStatus = jsonData.data['DUTY_STATUS']+"";
			//sename = dutyPerson;
            $('#content_id').val(dutyFaultReason);
            
            $('#statusid').val(dutyStatus);
            $('#dutyid').val(id);
            $('#status_id').html(dutyStatus == "" ? "未值班" : (dutyStatus == "0" ? "值班中" : "结束"));
			var indexDe = 0;
          		var html = "";
          		var isFirst = false;
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
             	 	 		html+= "<tr class='item_class first'>"+
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
								"<td class='textareaclass' rowspan='"+detailLength+"'><textarea>"+itemExplain+"</textarea></td>"+
							"</tr>";
             	 	 	}
             	 	 	else
             	 	 	{
             	 	 		html+= "<tr  class='item_class'>"+
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
             	 if (html != "")
             	 {
             	 	 isSave  = true;
             	 	 $('.item_class').remove();
	             	 $('#item_id').after(html);
             	 }
		},false);	
	
}); 
</script>

