<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script>
$.get("../resources/js/common/easyUIvalidateExtends.js");
</script>
<div id="win_content">
		<form id="addNodeManageInfo" method="post">
			<table width="100%" class="dialog_table">
				<tr>
					<td>节点名称:</td>
					<td><input class="easyui-textbox" style="height:30px;width: 180px;" type="text" name="node_name" data-options="missingMessage:'不能为空',required:true,validType:['mylength[2,30,\'用户名在2-30个字符之间\']','isChar']"></input></td>
				</tr>
				<tr>
					<td>关联设备:</td>
					<td><select name="deviceid" class="easyui-combobox" id="deviceid" style="width:178px; height: 30px;" data-options="required:true,
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
						<select name="nodeType" class="easyui-combobox" style="width:178px; height: 30px;" data-options="required:true,
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
						<select name="node_area" class="easyui-combobox" style="width:178px; height: 30px;" data-options="required:true,
   					                                valueField:'MUNCPL_ID',
   					                                textField:'MUNCPL',
   					                                panelHeight:'auto',
   					                                editable:false,
   					                                url:'../http/commonHander/getStationDIC/'"></select>
					</td>
				</tr>
				<tr>
					<td>备注:</td>
					<td><textarea name="node_desc" style="width: 250px;"></textarea>
					</td>
				</tr>
			</table>
		</form>
</div>

<div class="dialog_footer" data-options="region:'south',border:false"
				style="text-align: right">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="addNodeInfo()" iconcls="icon-save" style="width: 80px">保存</a> 
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="javascript:$('#dialog_content').dialog('close')"
					iconcls="icon-cancel" style="width: 80px">取消</a>
</div>
<script>

/**
 * 添加用户
 */
 function addNodeInfo(){
		var deviceIds = $('#deviceid').combobox('getValues');
		var url = contentPath+"/transferMonitorHander/addNodeManageInfo?deviceids="+deviceIds;
		var flag = $("#addNodeManageInfo").form('enableValidation').form('validate');
		var data = $('#addNodeManageInfo').serialize();
		if(flag){
			Util.getAjaxData(url,data,function(data){
			       if(data.success){
			    	   $('#nodeManage_list_data').datagrid('reload');
			    	   $('#dialog_content').dialog('close');
			    	   topCenter("添加成功！");
			       }else{
			    	   topCenter("添加失败！",500);
			       }
			},true);
		}
}
</script>