<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div id="win_content">
	<form action="" method="post" id="add_role_div_page">
		<table width="100%" class="dialog_table">
			<tr>
				<td>角色名称:</td>
				<td><input type="text" name="roleName"
					data-options="required:true,validType:{length:[0,30]}" /></td>
			</tr>
			<tr>
				<td>状态:</td>
				<td><select id="role_status" class="easyui-combobox"
					name="role_status"
					data-options="panelHeight:'auto',editable:false"
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
		onclick="saveRole()" iconcls="icon-save" style="width: 80px">保存</a> <a
		href="javascript:void(0)" class="easyui-linkbutton"
		onclick="javascript:$('#add_role_div').dialog('close')"
		iconcls="icon-cancel" style="width: 80px">取消</a>
</div>
<script type="text/javascript">
</script>

