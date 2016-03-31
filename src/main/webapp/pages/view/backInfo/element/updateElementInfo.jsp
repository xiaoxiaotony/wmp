<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script>
<% 
	String id = request.getParameter("id"); 
%>

$.get("../resources/js/common/easyUIvalidateExtends.js");
</script>
<div id="win_content">
		<form id="updateElement" method="post">
			<input type="hidden" value="<%=id %>" name='id'>
			<table width="100%" class="dialog_table">
				<tr>
					<td>要素名称:</td>
					<td><input class="easyui-textbox" style="height:30px;width: 180px;" type="text" id="name" name="elementname" data-options="disabled:true"></input></td>
				</tr>
				<tr>
					<td>要素编码 :</td>
					<td><input class="easyui-textbox" style="height:30px;width: 180px;" type="text" id="code" name="elementcode" data-options="disabled:true"></input></td>
				</tr>
				<tr>
					<td>要素阈值:</td>
					<td><input class="easyui-textbox" style="height:30px;width: 180px;" type="text" id="elementvalue" name="elementvalue"></input></td>
				</tr>
				<tr>
					<td>要素阈值:</td>
					<td>显示<input type="radio" value="0" name="isshow"/>&nbsp;&nbsp;不显示<input type="radio" value="1" name="isshow"/></td>
				</tr>
			</table>
		</form>
</div>

<div class="dialog_footer" data-options="region:'south',border:false"
				style="text-align: right">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="update()" iconcls="icon-save" style="width: 80px">保存</a> 
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="javascript:$('#dialog_content').dialog('close')"
					iconcls="icon-cancel" style="width: 80px">取消</a>
</div>
<script>
var id = "<%=id%>";

Util.getAjaxData(contentPath+"/systemBackInfoHandler/queryById?id="+id, null, function(data){
	$('#updateElement').form('load',data.data.map);
});

/**
 * 数据添加
 */
 function update(){
	
		var flag = $("#updateElement").form('enableValidation').form('validate');
		var data = $('#updateElement').serialize();
		var url = contentPath+"/systemBackInfoHandler/updateElement";
		if(flag){
			Util.getAjaxData(url,data,function(data){
			       if(data.success){
			    	   topCenter("数据修改成功！",500);
			    	   $('#elementInfo_list_data').datagrid('reload');
			       }else{
			    	   topCenter("数据修改失败！",500);
			       }
			},true);
		}
}
</script>