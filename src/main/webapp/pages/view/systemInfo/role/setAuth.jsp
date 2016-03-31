<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<% 
	String roleId = request.getParameter("roleId"); 
%>
<div id="win_content">
	<div id="tree_dialog" style="padding:20px;height: 520px;overflow-y:auto;"></div>
</div>
<div class="dialog_footer" data-options="region:'south',border:false"
	style="text-align: right">
	<a href="javascript:void(0)" class="easyui-linkbutton"
		onclick="saveRoleAuth()" iconcls="icon-save" style="width: 80px">保存</a> <a
		href="javascript:void(0)" class="easyui-linkbutton"
		onclick="javascript:$('#add_role_div').dialog('close')"
		iconcls="icon-cancel" style="width: 80px">取消</a>
</div>

<script type="text/javascript">
var roleId = '<%=roleId%>';
$(function(){
	$('#tree_dialog').tree({    
	    url: contentPath+"/menuInfoHandler/getMenusCheckForTree?roleId="+roleId,
	    method:'get',
	    animate:true,
	    checkbox:true,
	    lines:true
	});  
})

function getChecked(){
	var nodes = $('#tree_dialog').tree('getChecked');
	var id = '';
	for(var i=0; i<nodes.length; i++){
		if (id != ''){
			id += ',';
		}
		id += nodes[i].id;
	}
	return id;
}

function saveRoleAuth(){
	var paramData = getChecked();
	var url = contentPath+"/roleManagerHandler/updateRoleSourceInfo";
	$.ajax({
		url : url,
		type : 'POST',
		data : {
			authIds : paramData,
			roleId : roleId
		},
		success : function(data) {
			if (data.success) {
				jAlert("分配成功");
				$("#authRole").modal("hide");
			} else {
				jAlert(data.msg);
			}
		},
		error : function(XMLHttpRequest) {
			jAlert('请求超时');
		}
	});
	
}
</script>