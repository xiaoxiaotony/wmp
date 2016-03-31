<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<% 
	String id = request.getParameter("id"); 
%>
<script>
$.get("../resources/js/common/easyUIvalidateExtends.js");
</script>
<div id="update_role_div">
	<form id="updateRoleInfo" method="post">
		<table width="100%" class="dialog_table">
			<input type="hidden" name="roleId"/>
			<tr>
				<td>角色名称:</td>
				<td><input class="easyui-textbox" type="text" name="groupName"
					data-options="missingMessage:'不能为空',required:true,validType:['isChar']"></input></td>
			</tr>
			<tr>
				<td>状态:</td>
				<td><select class="easyui-combobox"
					name="status" data-options="panelHeight:'auto',editable:false"
					style="width: 200px; height: 30px;">
						<option value="0">正常</option>
						<option value="1">禁用</option>
				</select></td>
			</tr>
			<tr>
				<td>描述:</td>
				<td><textarea name="description" style="width: 250px;"></textarea>
				</td>
			</tr>
		</table>
	</form>
</div>

<div class="dialog_footer" data-options="region:'south',border:false"
		style="text-align: right">
		<a href="javascript:void(0)" class="easyui-linkbutton"
			onclick="updateRole()" iconcls="icon-save" style="width: 80px">保存</a> <a
			href="javascript:void(0)" class="easyui-linkbutton"
			onclick="javascript:$('#dialog_content').dialog('close')"
			iconcls="icon-cancel" style="width: 80px">取消</a>
</div>

<script>
var id = "<%=id%>";
//加载表单数据
$(function(){
	Util.getAjaxData(contentPath+"/roleManagerHandler/getRoleInfoById?roleId="+id, null, function(data){
		$('#updateRoleInfo').form('load',data.data);
	})
})
/**
 * 更新角色
 */
 function updateRole(){
	$('#updateRoleInfo').form('submit', {
		url:contentPath+"/roleManagerHandler/updateRoleInfo",
		onSubmit: function(){
			return $(this).form('enableValidation').form('validate');
		},
		success:function(data){
			   var json = $.parseJSON(data);
		       if(json.success){
		    	   Util.showMsg("修改角色成功！");
		    	   $('#roleInfo_list_data').datagrid('reload');
		    	   $('#dialog_content').dialog('close');
		       }else{
		    	   Util.showMsg("修改角色失败！");
		       }
		}
	});
}
</script>