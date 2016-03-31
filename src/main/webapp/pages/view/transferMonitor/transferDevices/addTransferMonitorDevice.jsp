<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script>
$.get("../resources/js/common/easyUIvalidateExtends.js");
</script>
<div id="win_content">
		<form id="addTransferDeviceInfo" method="post">
			<table width="100%" class="dialog_table">
				<tr>
					<td>设备名称:</td>
					<td><input class="easyui-textbox" style="height:30px;width: 180px;" type="text" name="name" data-options="missingMessage:'不能为空',required:true,validType:['mylength[2,20,\'用户名在2-20个字符之间\']','isChar']"></input></td>
				</tr>
				<tr>
					<td>设备IP:</td>
					<td><input class="easyui-textbox" style="height:30px;width: 180px;" type="text" id="deviceIp" name="deviceIp" data-options="missingMessage:'不能为空',required:true"></input></td>
				</tr>
				<tr>
					<td>设备区域:</td>
					<td>
						<select id="areaId" style="width:180px;height: 30px;"></select>
					</td>
				</tr>
				<tr>
					<td>操作系统:</td>
					<td>
						<select id="OS" name="OS" class="easyui-combobox" style="width:180px;height: 30px;">
							<option value="windows">windows</option>
							<option value="linux">linux</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>设备类型:</td>
					<td><select name="typeId" class="easyui-combobox" id="TYPEID" style="width:178px; height: 30px;" data-options="
                         valueField:'DICT_VALUE',
                         textField:'DICT_VAL_NAME',
                         panelHeight:'auto',
                         editable:false,
                         url:'../http/systemInfoHandler/getDictListVal?dict_code=device_type&all=false'"></select></td>
				</tr>
				<tr>
					<td>FTP日志路径:</td>
					<td><input class="easyui-textbox" style="height:30px;width: 180px;" type="text" id="ftplogpath" name="ftplogpath" data-options="missingMessage:'不能为空',required:true"></input> <input style="margin-left: 20px" onclick="checkFtpPath()" type="button" value='校验'> </td>
				</tr>
				<tr>
					<td>帐号:</td>
					<td><input class="easyui-textbox" style="height:30px;width: 180px;" type="text" id="account" name="account" data-options="missingMessage:'不能为空',required:true"></input></td>
				</tr>
				<tr>
					<td>密码:</td>
					<td><input class="easyui-textbox" style="height:30px;width: 180px;" type="text" id="password" name="password" data-options="missingMessage:'不能为空',required:true"></input></td>
				</tr>
				<tr>
					<td>文件正则表达式:</td>
					<td><input class="easyui-textbox" style="height:30px;width: 180px;" type="text" id="regularExpression" name="regularExpression" data-options="missingMessage:'不能为空',required:true"></input></td>
				</tr>
			</table>
		</form>
</div>

<div class="dialog_footer" data-options="region:'south',border:false"
				style="text-align: right">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="addTransferDevice()" iconcls="icon-save" style="width: 80px">保存</a> 
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="javascript:$('#dialog_content').dialog('close')"
					iconcls="icon-cancel" style="width: 80px">取消</a>
</div>
<script>
$('#areaId').combotree( {  
    //获取数据URL  
    url: contentPath+"/systemBackInfoHandler/getAreaListTree",
    //选择树节点触发事件  
    onSelect : function(node) {  
        //返回树对象  
        var tree = $(this).tree;  
        //选中的节点是否为叶子节点,如果不是叶子节点,清除选中  
        var isLeaf = tree('isLeaf', node.target);  
        if (!isLeaf) {  
            //清除选中  
            $('#areaId').combotree('clear');  
        }  
    }  
}); 

function checkFtpPath(){
	var ftpPath = $("#ftplogpath").val();
	var serverIp = $("#deviceIp").val();
	var url = contentPath+"/transferMonitorHander/checkFtpPath"
	Util.getAjaxData(url,{ftpPath:ftpPath,serverIp:serverIp},function(data){
	       if(data.data.success){
	    	   topCenter("ftp文件路径OK！");
	       }else{
	    	   topCenter("ftp文件路径不存在,请检查！");
	       }
	},true);
}


/**
 */
 function addTransferDevice(){
 		var areaId = $('#areaId').combotree('getValue');
		var flag = $("#addTransferDeviceInfo").form('enableValidation').form('validate');
		var data = $('#addTransferDeviceInfo').serialize();
		var url = contentPath+"/transferMonitorHander/addTransferInfo";
		data += "&areaId="+areaId;
		if(flag){
			Util.getAjaxData(url,data,function(data){
			       if(data.data.success){
			    	   $('#transferMonitor_list_data').datagrid('reload');
			    	   $('#dialog_content').dialog('close');
			    	   topCenter("添加成功！");
			       }else{
			    	   topCenter("添加失败！",500);
			       }
			},true);
		}
}
</script>