<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script>
$.get("../resources/js/common/easyUIvalidateExtends.js");
</script>
<div id="win_content">
		<form id="addElement" method="post">
			<table width="100%" class="dialog_table">
				<tr>
					<td>要素名称:</td>
					<td><input class="easyui-textbox" style="height:30px;width: 180px;" type="text" name="name" data-options="missingMessage:'不能为空',required:true"></input></td>
				</tr>
				<tr>
					<td>编码:</td>
					<td><input class="easyui-textbox" style="height:30px;width: 180px;" type="text" name="code" data-options="missingMessage:'不能为空',required:true,validType:['checkName[\'/systemBackInfoHandler/checkCode\',\'code\',\'编码已经存在\']','isChar']"></input></td>
				</tr>
				<tr>
					<td>要素描述:</td>
					<td><input class="easyui-textbox" style="height:30px;width: 180px;" type="text" name="description"></input></td>
				</tr>
							
				<!-- <tr>
					<td>电话号码:</td>
					<td><input class="easyui-textbox" style="height:30px;width: 180px;" type="text" name="phone" data-options="validType:'phone'"></input></td>
				</tr>
				<tr>
					<td>联系地址:</td>
					<td><input class="easyui-textbox" style="height:30px;width: 180px;" type="text" name="address" data-options="validType:['mylength[5,50,\'联系地址在5-50个字符之间\']','isChar']"></input></td>
				</tr> -->
			</table>
		</form>
</div>

<div class="dialog_footer" data-options="region:'south',border:false"
				style="text-align: right">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="add()" iconcls="icon-save" style="width: 80px">保存</a> 
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="javascript:$('#dialog_content').dialog('close')"
					iconcls="icon-cancel" style="width: 80px">取消</a>
</div>
<script>

/**
 * 数据添加
 */
 function add(){
	
		var flag = $("#addElement").form('enableValidation').form('validate');
		var data = $('#addElement').serialize();
		var url = contentPath+"/systemBackInfoHandler/addElement";
		if(flag){
			Util.getAjaxData(url,data,function(data){
			       if(data.success){
			    	   $('#elementInfo_list_data').datagrid('reload');
			    	   topCenter("数据添加成功！",500);
			       }else{
			    	   topCenter("数据添加失败！",500);
			       }
			},true);
		}
}
</script>