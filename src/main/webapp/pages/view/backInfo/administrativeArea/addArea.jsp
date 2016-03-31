<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<% 
	String parent_id = request.getParameter("nodeId"); 
%>
<script>
$.get("../resources/js/common/easyUIvalidateExtends.js");
</script>
<div id="win_content">
		<form id="addAreaInfo" method="post">
			<table width="100%" class="dialog_table">
				<input type="hidden" name = "PARENT_ID" value="<%=parent_id %>" />
				<tr>
					<td>区域名称:</td>
					<td><input class="easyui-textbox" style="height: 30px;" type="text" name="NAME" data-options="missingMessage:'不能为空',required:true"></input></td>
				</tr>
				<tr>
					<td>状态:</td>
					<td><select class="easyui-combobox" name="ENABLE" style="width:178px;height: 30px;" data-options="panelHeight:'auto',editable:false">
							<option value="0">启用</option>
							<option value="1">禁用</option>
						</select>
					</td>
				</tr>
			</table>
		</form>
</div>

<div class="dialog_footer" data-options="region:'south',border:false"
				style="text-align: right">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="saveMenu()" iconcls="icon-save" style="width: 80px">保存</a> 
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="javascript:$('#dialog_content').dialog('close')"
					iconcls="icon-cancel" style="width: 80px">取消</a>
</div>
<script>

/**
 * 选择图片
 */
var flag = true;
/**
 * 添加菜单
 */
 var parentId = '<%=parent_id%>';
 function saveMenu(){
		var flag = $("#addAreaInfo").form('enableValidation').form('validate');
		var data = $('#addAreaInfo').serialize();
		if(parentId == '540000'){
			Util.showMsg("不能添加一级行政区域");
			return;
		}
		var url = contentPath+"/systemBackInfoHandler/addArea";
		if(flag){
			Util.getAjaxData(url,data,function(data){
			       if(data.success){
			    	   $('#administrativeArea_Info_list_data').datagrid('reload');
			    	   $('#dialog_content').dialog('close');
			    	   topCenter("添加成功！");
			       }else{
			    	   Util.showMsg("添加失败！",350);
			       }
			},true);
		}
}

</script>