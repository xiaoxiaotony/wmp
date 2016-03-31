<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<% 
	String id = request.getParameter("id"); 
%>
<script>
$.get("../resources/js/common/easyUIvalidateExtends.js");
</script>
<div id="win_content">
		<form id="updateTrouble" method="post">
		  <table width="100%" class="dialog_table">
		    <tr>
		      <td width="10%">类型</td>
			  <td width="14%">
				  <select class="easyui-combobox" name="trouble_type" id="trouble_type" style="width:178px; height: 30px;" data-options="panelHeight:'auto',editable:false">
			          <option value="1">常规日志</option>
			          <option value="2">资料缺失</option>
			          <option value="3">设备故障</option>
			          <option value="4">其它日志</option>
			        </select>      
			   </td>
			   <td width="10%">状态</td>
			   <td width="66%">
				   <select class="easyui-combobox" id="opertion_status" name="opertion_status" style="width:178px; height: 30px;" data-options="panelHeight:'auto',editable:false">
			          <option value="0">未处理</option>
			          <option value="1">已处理</option>
			        </select>      
			    </td>
		    </tr>
		    <tr>
		      <td>上报</td>
		      <td><select id="record_userid" name="record_userid" class="easyui-combobox" style="width:178px; height: 30px;" data-options="
    					                                valueField:'id',
    					                                textField:'name',
    					                                panelHeight:'auto',
    					                                editable:false,
    					                                url:'../http/userInfoHandler/queryByCheck',
    					                                onLoadSuccess:roleLoad"></select>
 			 </td>
		      <td>时间</td>
		      <td><input class="easyui-textbox" style="height:30px;width: 180px;" id="record_time" name="record_time" ></td>
		    </tr>
		    <tr>
		      <td>处理</td>
		      <td><select id="opertion_userid" name="opertion_userid" class="easyui-combobox" style="width:178px; height: 30px;" data-options="
    					                                valueField:'id',
    					                                textField:'name',
    					                                panelHeight:'auto',
    					                                editable:false,
    					                                url:'../http/userInfoHandler/queryByCheck'"></select>
              </td>
		      <td>时间</td>
		      <td><input class="easyui-textbox" style="height:30px;width: 180px;" id="opertion_time" name="opertion_time"></td>
		    </tr>
			<tr>
		      <td colspan="4">详细内容</td>
		    </tr>
			<tr>
		      <td colspan="4">
			  	<textarea id="content" name="content" style="margin: 0px; width: 520px; height: 108px;"></textarea>
			  </td>
		    </tr>
          </table>
		</form>
</div>

<div class="dialog_footer" data-options="region:'south',border:false"
				style="text-align: right">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="updateTrouble()" iconcls="icon-save" style="width: 80px">保存</a> 
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="javascript:$('#dialog_content').dialog('close')"
					iconcls="icon-cancel" style="width: 80px">取消</a>
</div>
<script>
var id = "<%=id%>";

//加载表单数据
function roleLoad(param){
	
	Util.getAjaxData(contentPath+"/dutyHander/getDutyErrorDetail?id="+id, null, function(data){
		$('#updateTrouble').form('load',data.data.map);
	});
}

/**
 * 添加用户
 */
 function updateTrouble(){
		var trouble_type = $("#trouble_type").combobox('getValue');
	
		var opertion_status=$("#opertion_status").combobox('getValue');

		var opertion_userid=$("#opertion_userid").combobox('getValue');

		var content=$("#content").val();
		
		if(content==undefined||content.length<1){
			alert('请填写详细内容');
			return false;
		}
		$.post(contentPath+"/dutyHander/updateTrouble",{TROUBLE_TYPE:trouble_type,
			OPERTION_STATUS:opertion_status,
			OPERTION_USERID:opertion_userid,
			CONTENT:content,ID:id},function(data){
				if(data.data==1){
					alert('处理成功');
					$("#error_list_data").datagrid('load');
					$('#dialog_content').dialog('close');
				}else{
					alert('处理失败');
				}
			},'json');
}
</script>