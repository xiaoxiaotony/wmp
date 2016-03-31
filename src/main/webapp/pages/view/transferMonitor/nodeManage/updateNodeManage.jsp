<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
 	String id = request.getParameter("id");
%>	
<div id="win_content">
		<form id="updateNodeManageInfo" method="post">
			<table width="100%" class="dialog_table">
				<tr>
					<td>节点名称:</td>
					<td><input class="easyui-textbox" style="height:30px;width: 180px;" type="text" name="node_name" id="node_name" data-options="missingMessage:'不能为空'"></input></td>
				</tr>
				<tr>
					<td>关联设备:</td>
					<td><select name="deviceid" class="easyui-combobox" style="width:178px;height: 30px;"  id="deviceid" data-options="
   					                                valueField:'ID',
   					                                textField:'NAME',
   					                                panelHeight:'auto',
   					                                editable:false,
   					                                multiple:true,
   					                                url:'../http/transferMonitorHander/queryTransferDeviceList'"></select></td>
				</tr>
				<tr>
					<td>类型:</td>
					<td>
						<select id="nodeType" name="nodeType" class="easyui-combobox" style="width:178px; height: 30px;" data-options="
   					                                valueField:'DICT_VALUE',
   					                                textField:'DICT_VAL_NAME',
   					                                panelHeight:'auto',
   					                                editable:false,
   					                                url:'../http/systemInfoHandler/getDictListVal?dict_code=node_type&all=false'"></select>
					</td>
				</tr>
				<tr>
					<td>所属地域:</td>
					<td>
						<select name="node_area" class="easyui-combobox" style="width:178px; height: 30px;" id="node_area" data-options="required:true,
   					                                valueField:'MUNCPL_ID',
   					                                textField:'MUNCPL',
   					                                panelHeight:'auto',
   					                                editable:false,
   					                                url:'../http/commonHander/getStationDIC/',
   					                                onLoadSuccess:roleLoad"></select>
					</td>
				</tr>
				<tr>
					<td>备注:</td>
					<td><textarea name="node_desc" id="node_desc" style="width: 250px;"></textarea>
					</td>
				</tr>
			</table>
		</form>
</div>

<div class="dialog_footer" data-options="region:'south',border:false"
				style="text-align: right">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="updateNodeInfo()" iconcls="icon-save" style="width: 80px">保存</a> 
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="javascript:$('#dialog_content').dialog('close')"
					iconcls="icon-cancel" style="width: 80px">取消</a>
</div>
<script>
var id = "<%=id%>";
function roleLoad(){
	Util.getAjaxData(contentPath+"/transferMonitorHander/getNodeManagerById?id="+id, null, function(data){
		var area = data.data.map.node_area;
		var type =  data.data.map.node_type;
		$('#nodeType').combobox('setValue',type);
		$('#node_area').combobox('setValue',area);
		$('#node_name').textbox('setValue',data.data.map.node_name);
		$('#node_desc').val(data.data.map.node_desc);
		var deviceId = data.data.map.devied_id;
		$('#deviceid').combobox('setValues',deviceId.split(','));
	});
}

/**
 * 添加用户
 */
 function updateNodeInfo(){
		var flag = $("#updateNodeManageInfo").form('enableValidation').form('validate');
		var data = $('#updateNodeManageInfo').serialize();
		var url = contentPath+"/transferMonitorHander/updateNodeManageInfo";
		if(flag){
			Util.getAjaxData(url,data,function(data){
			       if(data.success){
			    	   $('#nodeManage_list_data').datagrid('reload');
			    	   $('#dialog_content').dialog('close');
			    	   topCenter("修改成功！");
			       }else{
			    	   topCenter("修改失败！",500);
			       }
			},true);
		}
}
</script>