<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div title="气象通讯传输录入" data-options="closable:true">

	<!-- 工具栏 -->
	<div class="cont_tit" id="duty_opt_bar_div1"></div>

	<div class="search_bar" id="dutyInfo_search_bar_div">
		 <div class="search_tit" style="width: 65px;">时间：</div>
		<div class="search_txt" id="time_id_"> 
		</div>
		
		<div class="search_tit" style="width: 65px;">值班人：</div>
		<div class="search_txt" id="name_id_">
		    <!--  <select id="dutyPersonId_" name="role" class="easyui-combobox" style="width:178px; height: 32px;" data-options="
    					                                valueField:'USERID',
    					                                textField:'USERNAME',
    					                                panelHeight:'auto',
    					                                editable:false,
    					                                url:'../http/dutyHander/queryUsers',
    					                                onLoadSuccess:roleLoad1
    					                                "></select> -->
		</div> 
		
		<div class="search_tit" style="width: 60%;margin-right: 0px;">值班状态:&nbsp;</div>
		<div class="search_txt" id="status_id_" style="width: 4%;margin-right: 10px;">
		</div> 
	</div>
	<div>
		<table border="1" bordercolor="#000000" style="border-collapse:collapse;width: 100%;">
			<tr height="50"  align="center" id="item_id_w" style="background-color: #EAEAEA;">
				<td width="20%">检查类型类型</td>
				<td width="20%">是否齐全</td>
				<td width="20%">缺报站点及时次</td>
				<td width="20%">故障原因</td>
				<td width="20%">解决方式</td>
			</tr>
			<tr class="item_class_w">
				<td>自动站土壤水分</td>
				<td>
					<input type="radio" value='0' checked="checked" name="isall1"/>全  <input value='1' type="radio" name="isall1"/>不全  
				</td>
				<td>
					<textarea rows="2" cols="2"></textarea>
				</td>
				<td>
					<input type="checkbox" value='0'/>通讯线路故障
					<input type="checkbox" value='1'/>站点设备故障</br>
					<input type="checkbox" value='2'/>站点电力故障
					<input type="checkbox" value='3'/>其他:
				</td>
				<td><textarea rows="2" cols="2"></textarea></td>
			</tr>
			<tr class="item_class_w">
				<td>公路交通气象观测资料</td>
				<td>
					<input type="radio" value='0' checked="checked" name="isall2"/>全  <input value='1' type="radio" name="isall2"/>不全  
				</td>
				<td>
					<textarea rows="2" cols="2"></textarea>
				</td>
				<td>
					<input type="checkbox" value='0'/>通讯线路故障
					<input type="checkbox" value='1'/>站点设备故障</br>
					<input type="checkbox" value='2'/>站点电力故障
					<input type="checkbox" value='3'/>其他:
				</td>
				<td><textarea rows="2" cols="2"></textarea></td>
			</tr>
			
			
			<tr class="item_class_w">
				<td>无人自动站观测资料</td>
				<td>
					<input type="radio" value='0' checked="checked" name="isall3"/>全  <input value='1' type="radio" name="isall3"/>不全  
				</td>
				<td>
					<textarea rows="2" cols="2"></textarea>
				</td>
				<td>
					<input type="checkbox" value='0'/>通讯线路故障
					<input type="checkbox" value='1'/>站点设备故障</br>
					<input type="checkbox" value='2'/>站点电力故障
					<input type="checkbox" value='3'/>其他:
				</td>
				<td><textarea rows="2" cols="2"></textarea></td>
			</tr>
			<tr class="item_class_w">
				<td>区域地面自动站观测资料</td>
				<td>
					<input type="radio" value='0' checked="checked" name="isall4"/>全  <input value='1' type="radio" name="isall4"/>不全  
				</td>
				<td>
					<textarea rows="2" cols="2"></textarea>
				</td>
				<td>
					<input type="checkbox" value='0'/>通讯线路故障
					<input type="checkbox" value='1'/>站点设备故障</br>
					<input type="checkbox" value='2'/>站点电力故障
					<input type="checkbox" value='3'/>其他:
				</td>
				<td><textarea rows="2" cols="2"></textarea></td>
			</tr>
			
			
			<tr class="item_class_w">
				<td>新自动站观测资料</td>
				<td>
					<input type="radio" value='0' checked="checked" name="isall5"/>全  <input value='1' type="radio" name="isall5"/>不全  
				</td>
				<td>
					<textarea rows="2" cols="2"></textarea>
				</td>
				<td>
					<input type="checkbox" value='0'/>通讯线路故障
					<input type="checkbox" value='1'/>站点设备故障</br>
					<input type="checkbox" value='2'/>站点电力故障
					<input type="checkbox" value='3'/>其他:
				</td>
				<td><textarea rows="2" cols="2"></textarea></td>
			</tr>
			
			
			<tr class="item_class_w">
				<td>新自动站状态信息资料</td>
				<td>
					<input type="radio" value='0' checked="checked" name="isall6"/>全  <input value='1' type="radio" name="isall6"/>不全  
				</td>
				<td>
					<textarea rows="2" cols="2"></textarea>
				</td>
				<td>
					<input type="checkbox" value='0'/>通讯线路故障
					<input type="checkbox" value='1'/>站点设备故障</br>
					<input type="checkbox" value='2'/>站点电力故障
					<input type="checkbox" value='3'/>其他:
				</td>
				<td><textarea rows="2" cols="2"></textarea></td>
			</tr>
			
			<tr class="item_class_w">
				<td>大气成分资料</td>
				<td>
					<input type="radio" value='0' checked="checked" name="isall7"/>全  <input value='1' type="radio" name="isall7"/>不全  
				</td>
				<td>
					<textarea rows="2" cols="2"></textarea>
				</td>
				<td>
					<input type="checkbox" value='0'/>通讯线路故障
					<input type="checkbox" value='1'/>站点设备故障</br>
					<input type="checkbox" value='2'/>站点电力故障
					<input type="checkbox" value='3'/>其他:
				</td>
				<td><textarea rows="2" cols="2"></textarea></td>
			</tr>
			<tr class="item_class_w">
				<td>精细化产品</td>
				<td>
					<input type="radio" value='0' checked="checked" name="isall8"/>全  <input value='1' type="radio" name="isall8"/>不全  
				</td>
				<td>
					<textarea rows="2" cols="2"></textarea>
				</td>
				<td>
					<input type="checkbox" value='0'/>通讯线路故障
					<input type="checkbox" value='1'/>站点设备故障</br>
					<input type="checkbox" value='2'/>站点电力故障
					<input type="checkbox" value='3'/>其他:
				</td>
				<td><textarea rows="2" cols="2"></textarea></td>
			</tr>
			
			
			<tr class="item_class_w">
				<td>常规资料</td>
				<td>
					<input type="radio" value='0' checked="checked" name="isall9"/>全  <input value='1' type="radio" name="isall9"/>不全  
				</td>
				<td>
					<textarea rows="2" cols="2"></textarea>
				</td>
				<td>
					<input type="checkbox" value='0'/>通讯线路故障
					<input type="checkbox" value='1'/>站点设备故障</br>
					<input type="checkbox" value='2'/>站点电力故障
					<input type="checkbox" value='3'/>其他:
				</td>
				<td><textarea rows="2" cols="2"></textarea></td>
			</tr>
			
			
			<tr class="item_class_w">
				<td>自动雨量站观测资料</td>
				<td>
					<input type="radio" value='0' checked="checked" name="isall10"/>全  <input value='1' type="radio" name="isall10"/>不全  
				</td>
				<td>
					<textarea rows="2" cols="2"></textarea>
				</td>
				<td>
					<input type="checkbox" value='0'/>通讯线路故障
					<input type="checkbox" value='1'/>站点设备故障</br>
					<input type="checkbox" value='2'/>站点电力故障
					<input type="checkbox" value='3'/>其他:
				</td>
				<td><textarea rows="2" cols="2"></textarea></td>
			</tr>
			
			
			<tr class="item_class_w">
				<td>自动站辐射资料</td>
				<td>
					<input type="radio" value='0' checked="checked" name="isall11"/>全  <input value='1' type="radio" name="isall11"/>不全  
				</td>
				<td>
					<textarea rows="2" cols="2"></textarea>
				</td>
				<td>
					<input type="checkbox" value='0'/>通讯线路故障
					<input type="checkbox" value='1'/>站点设备故障</br>
					<input type="checkbox" value='2'/>站点电力故障
					<input type="checkbox" value='3'/>其他:
				</td>
				<td><textarea rows="2" cols="2"></textarea></td>
			</tr>
			
			
			<tr class="item_class_w">
				<td>多普勒雷达基数据资料</td>
				<td>
					<input type="radio" value='0' checked="checked" name="isall12"/>全  <input value='1' type="radio" name="isall12"/>不全  
				</td>
				<td>
					<textarea rows="2" cols="2"></textarea>
				</td>
				<td>
					<input type="checkbox" value='0'/>通讯线路故障
					<input type="checkbox" value='1'/>站点设备故障</br>
					<input type="checkbox" value='2'/>站点电力故障
					<input type="checkbox" value='3'/>其他:
				</td>
				<td><textarea rows="2" cols="2"></textarea></td>
			</tr>
			<tr class="item_class_w">
				<td>多普勒雷达产品资料</td>
				<td>
					<input type="radio" value='0' checked="checked" name="isall13"/>全  <input value='1' type="radio" name="isall13"/>不全  
				</td>
				<td>
					<textarea rows="2" cols="2"></textarea>
				</td>
				<td>
					<input type="checkbox" value='0'/>通讯线路故障
					<input type="checkbox" value='1'/>站点设备故障</br>
					<input type="checkbox" value='2'/>站点电力故障
					<input type="checkbox" value='3'/>其他:
				</td>
				<td><textarea rows="2" cols="2"></textarea></td>
			</tr>
			<tr class="item_class_w">
				<td>多普勒雷达拚图资料</td>
				<td>
					<input type="radio" value='0' checked="checked" name="isall14"/>全  <input value='1' type="radio" name="isall14"/>不全  
				</td>
				<td>
					<textarea rows="2" cols="2"></textarea>
				</td>
				<td>
					<input type="checkbox" value='0'/>通讯线路故障
					<input type="checkbox" value='1'/>站点设备故障</br>
					<input type="checkbox" value='2'/>站点电力故障
					<input type="checkbox" value='3'/>其他:
				</td>
				<td><textarea rows="2" cols="2"></textarea></td>
			</tr>
			
			
			<tr class="item_class_w">
				<td>GPS/MET水汽探测资料</td>
				<td>
					<input type="radio" value='0' checked="checked" name="isall15"/>全  <input value='1' type="radio" name="isall15"/>不全  
				</td>
				<td>
					<textarea rows="2" cols="2"></textarea>
				</td>
				<td>
					<input type="checkbox" value='0'/>通讯线路故障
					<input type="checkbox" value='1'/>站点设备故障</br>
					<input type="checkbox" value='2'/>站点电力故障
					<input type="checkbox" value='3'/>其他:
				</td>
				<td><textarea rows="2" cols="2"></textarea></td>
			</tr>
			
			
			<tr class="item_class_w">
				<td>日照资料（时间8点）</td>
				<td>
					<input type="radio" value='0' checked="checked" name="isall16"/>全  <input value='1' type="radio" name="isall16"/>不全  
				</td>
				<td>
					<textarea rows="2" cols="2"></textarea>
				</td>
				<td>
					<input type="checkbox" value='0'/>通讯线路故障
					<input type="checkbox" value='1'/>站点设备故障</br>
					<input type="checkbox" value='2'/>站点电力故障
					<input type="checkbox" value='3'/>其他:
				</td>
				<td><textarea rows="2" cols="2"></textarea></td>
			</tr>
			
			<tr class="item_class_w">
				<td>地面气象要素日数据（时间20点）</td>
				<td>
					<input type="radio" value='0' checked="checked" name="isall17"/>全  <input value='1' type="radio" name="isall17"/>不全  
				</td>
				<td>
					<textarea rows="2" cols="2"></textarea>
				</td>
				<td>
					<input type="checkbox" value='0'/>通讯线路故障
					<input type="checkbox" value='1'/>站点设备故障</br>
					<input type="checkbox" value='2'/>站点电力故障
					<input type="checkbox" value='3'/>其他:
				</td>
				<td><textarea rows="2" cols="2"></textarea></td>
			</tr>
			<tr class="item_class_w">
				<td>紫外线指数预报（时间每日09点、15点）</td>
				<td>
					<input type="radio" value='0' checked="checked" name="isall18"/>全  <input value='1' type="radio" name="isall18"/>不全  
				</td>
				<td>
					<textarea rows="2" cols="2"></textarea>
				</td>
				<td>
					<input type="checkbox" value='0'/>通讯线路故障
					<input type="checkbox" value='1'/>站点设备故障</br>
					<input type="checkbox" value='2'/>站点电力故障
					<input type="checkbox" value='3'/>其他:
				</td>
				<td><textarea rows="2" cols="2"></textarea></td>
			</tr>
			
			
			<tr class="item_class_w">
				<td>酸雨资料（时间每日15点及每月5日）</td>
				<td>
					<input type="radio" value='0' checked="checked" name="isall19"/>全  <input value='1' type="radio" name="isall19"/>不全  
				</td>
				<td>
					<textarea rows="2" cols="2"></textarea>
				</td>
				<td>
					<input type="checkbox" value='0'/>通讯线路故障
					<input type="checkbox" value='1'/>站点设备故障</br>
					<input type="checkbox" value='2'/>站点电力故障
					<input type="checkbox" value='3'/>其他:
				</td>
				<td><textarea rows="2" cols="2"></textarea></td>
			</tr>
			<tr class="item_class_w">
				<td>月气候趋势预测资料</td>
				<td>
					<input type="radio" value='0' checked="checked" name="isall20"/>全  <input value='1' type="radio" name="isall20"/>不全  
				</td>
				<td>
					<textarea rows="2" cols="2"></textarea>
				</td>
				<td>
					<input type="checkbox" value='0'/>通讯线路故障
					<input type="checkbox" value='1'/>站点设备故障</br>
					<input type="checkbox" value='2'/>站点电力故障
					<input type="checkbox" value='3'/>其他:
				</td>
				<td><textarea rows="2" cols="2"></textarea></td>
			</tr>
			<tr class="item_class_w">
				<td>探空、地面月报资料（每月4日15点前）</td>
				<td>
					<input type="radio" value='0' checked="checked" name="isall21"/>全  <input value='1' type="radio" name="isall21"/>不全  
				</td>
				<td>
					<textarea rows="2" cols="2"></textarea>
				</td>
				<td>
					<input type="checkbox" value='0'/>通讯线路故障
					<input type="checkbox" value='1'/>站点设备故障</br>
					<input type="checkbox" value='2'/>站点电力故障
					<input type="checkbox" value='3'/>其他:
				</td>
				<td><textarea rows="2" cols="2"></textarea></td>
			</tr>
			<tr class="item_class_w">
				<td>土壤墒情资料（每月6、16、26日09点前）</td>
				<td>
					<input type="radio" value='0' checked="checked" name="isall22"/>全  <input value='1' type="radio" name="isall22"/>不全  
				</td>
				<td>
					<textarea rows="2" cols="2"></textarea>
				</td>
				<td>
					<input type="checkbox" value='0'/>通讯线路故障
					<input type="checkbox" value='1'/>站点设备故障</br>
					<input type="checkbox" value='2'/>站点电力故障
					<input type="checkbox" value='3'/>其他:
				</td>
				<td><textarea rows="2" cols="2"></textarea></td>
			</tr>
			<tr class="item_class_w">
				<td>情报灾情资料（每日1200-1400在非定时资料监控）</td>
				<td>
					<input type="radio" value='0' checked="checked" name="isall23"/>全  <input value='1' type="radio" name="isall23"/>不全  
				</td>
				<td>
					<textarea rows="2" cols="2"></textarea>
				</td>
				<td>
					<input type="checkbox" value='0'/>通讯线路故障
					<input type="checkbox" value='1'/>站点设备故障</br>
					<input type="checkbox" value='2'/>站点电力故障
					<input type="checkbox" value='3'/>其他:
				</td>
				<td><textarea rows="2" cols="2"></textarea></td>
			</tr>
			<tr class="item_class_w">
				<td>北斗卫星传输资料</td>
				<td>
					<input type="radio" value='0' checked="checked" name="isall24"/>全  <input value='1' type="radio" name="isall24"/>不全  
				</td>
				<td>
					<textarea rows="2" cols="2"></textarea>
				</td>
				<td>
					<input type="checkbox" value='0'/>通讯线路故障
					<input type="checkbox" value='1'/>站点设备故障</br>
					<input type="checkbox" value='2'/>站点电力故障
					<input type="checkbox" value='3'/>其他:
				</td>
				<td><textarea rows="2" cols="2"></textarea></td>
			</tr>
			<tr class="item_class_w">
				<td>VPN网关控制台连接状态</td>
				<td>
					<input type="radio" value='0' checked="checked" name="isall25"/>全  <input value='1' type="radio" name="isall25"/>不全  
				</td>
				<td>
					<textarea rows="2" cols="2"></textarea>
				</td>
				<td>
					<input type="checkbox" value='0'/>通讯线路故障
					<input type="checkbox" value='1'/>站点设备故障</br>
					<input type="checkbox" value='2'/>站点电力故障
					<input type="checkbox" value='3'/>其他:
				</td>
				<td><textarea rows="2" cols="2"></textarea></td>
			</tr>
			<tr class="item_class_w">
				<td>网络故障、设备故障原因说明</td>
				<td>
					<input type="radio" value='0' checked="checked" name="isall26"/>全  <input value='1' type="radio" name="isall26"/>不全  
				</td>
				<td>
					<textarea rows="2" cols="2"></textarea>
				</td>
				<td>
					<input type="checkbox" value='0'/>通讯线路故障
					<input type="checkbox" value='1'/>站点设备故障</br>
					<input type="checkbox" value='2'/>站点电力故障
					<input type="checkbox" value='3'/>其他:
				</td>
				<td><textarea rows="2" cols="2"></textarea></td>
			</tr>
			
		</table>
		<input type="hidden" id="w_duty_id"/>
		<input type="hidden" id="statusid_"/>
	</div>	
</div>
<script>
var issave = false;
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
	boolbarId : "duty_opt_bar_div1",
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
             weather_opt();
		}
	}
	]
}
DataGrid.initToolsBarPanel(toolbarParams);


function change_work()
{
		var id = $('#w_duty_id').val(); 
		if (id == null || id == '')
		{
			topCenter("还未保存");
			return;
		}
		
		
		var statusVal = $('#statusid_').val();
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
					$('#statusid_').val("1");
					$('#status_id_').html("结束");
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

/* var wename = "";
//为下拉列表选择一个默认的
function roleLoad1(param){
	$('#dutyPersonId_').combobox('setValue', wename);
}
 */
function weather_opt()
{
	var count = 0;
	$('.item_class_w').each(function(i){
		count++;
	});

	var data = "[";
	$('.item_class_w').each(function(i){
		$(this).children().each(function(j){
				if (j == 0)
				{
					var itemName = $(this).html();
					data+="{\"itemName\":\""+itemName+"\",";
				} 
				else if (j == 1)
				{
					var isall = "";
					$(this).children().each(function(n){
						if ($(this).is(":checked"))
						{
							isall = $(this).val();
						}
					});
					data+="\"isall\":\""+isall+"\",";
				}
				 else if (j == 2)
				{
						var loseStation = $(this).find("textarea").val();
						data+="\"loseStation\":\""+loseStation+"\",";
				}
				else if (j == 3)
				{
					var troubleItem = "";
					$(this).children().each(function(n){
						if ($(this).is(":checked"))
						{
							troubleItem += $(this).val();
							troubleItem+=",";
						}
					});
					troubleItem = troubleItem.substring(0,troubleItem.length-1);
					data+="\"troubleItem\":\""+troubleItem+"\",";
				}  
				else if  (j == 4)
				{
					var solve = $(this).find("textarea").val();
					data+="\"solve\":\""+solve+"\"";
				}
		}); 
		if (Number(count) -1 != i)
		{
			data+="},";
		}
		else
		{
			data+="}";
		}
	});
	data+="]";
	//var name = $('#dutyPersonId_').combobox('getText');
	var time = $('#time_id_').html();
	var dutyid = $('#w_duty_id').val(); 
	
	 if (dutyid == null || dutyid == '')
	{
		if (issave)
		{
			topCenter("已经保存");
			return;
		}
		var url = contentPath+"/dutyHander/saveWetherNetDutyLog?itemData="+data+"&time="+time;
		Util.getAjaxData(url,"",function(data){
		var res = data.data.success;
			if (res)
			{
				$('#w_duty_id').val(data.data.id);
				$('#status_id_').html("值班中");
				issave = true;
				topCenter("保存成功");
			}
			else
			{
				topCenter("保存失败");
			}
			},false);	
	}
	else
	{
		var id = $('#w_duty_id').val();
		if (id == null || id == '')
		{
			return;
		}
		
		var statusVal = $('#statusid_').val();
		if (statusVal == "1")
		{
			topCenter("值班结束不能修改");
			return;
		}
		
		var url = contentPath+"/dutyHander/updateWehther?itemData="+data+"&time="+time+"&name="+name+"&id="+id;
		Util.getAjaxData(url,"",function(data){
			if (data.data.success)
			{
				$('#w_duty_id').val(data.data.id);
				topCenter("修改成功");
			}
			else
			{
				topCenter("修改失败");
			}
			},true);
	}
		

}

$(function(){
	var time1 = new Date().Format("yyyy-MM-dd HH:mm:ss");
	$("#time_id_").html(time1);
});

 $(function(){
	var url = contentPath+"/dutyHander/getDutyWeInfo";
	Util.getAjaxData(url,null,function(data){
		     var jsonData = data;
             if (jsonData != null && jsonData != '')
             {
             	var items = jsonData.data['items'];
             	var username = jsonData.data['username'];
             	if (dutyPerson != null && dutyPerson != "")
	            {
		            $('#name_id_').html(dutyPerson);
	            }
	            else
	            {
	            	  $('#name_id_').html(username);
	            }
		    	if(items.length == 0)
		    	{
		    		$('#status_id_').html("未值班");
		    		return;
		    	}
             	var dutyPerson = jsonData.data['DUTY_PERSON'];
             	var dutyDate = jsonData.data['DUTY_DATE'];
             	var dutyStatus = jsonData.data['DUTY_STATUS']+"";
             	
             	var id = jsonData.data['ID'];
             	
             	//wename = dutyPerson;
             	$('#time_id_').html(dutyDate);
             	$('#w_duty_id').val(id);
             	$('#statusid_').val(dutyStatus);
             	$('#status_id_').html(dutyStatus == "" ? "未值班" : (dutyStatus == "0" ? "值班中" : "结束"));
          		
          		var html = "";
          		var index = 0;
             	 $.each(items,function(n,item) {
             	 	var itemName = item['ITEM_NAME'] == null ? "" : item['ITEM_NAME'];
             	 	var loseStation = item['LOSE_STATION'] == null ? "" : item['LOSE_STATION'] ;
             	 	var troubleItem = item['TROUBLE_ITEM'] == null ? "" : item['TROUBLE_ITEM'] ;
             	 	var solve = item['SOLVE'] == null ? "" : item['SOLVE'] ;
             	 	var isall = item['ISALL'];
             	 	
             	 	html+="<tr class='item_class_w'>"+
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
             	 if (html != "")
             	 {
             	 	 issave = true;
             	 	 $('.item_class_w').remove();
	             	 $('#item_id_w').after(html);
             	 } 
             }
		},false);
}); 
</script>

