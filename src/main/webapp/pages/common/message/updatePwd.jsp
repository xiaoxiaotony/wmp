<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<% 
	String id = request.getParameter("id"); 
%>
<script>
$.get("../resources/js/common/easyUIvalidateExtends.js");
</script>
<div id="win_content">
		<form id="update_pwd_form" method="post">
			<table width="100%" class="dialog_table">
				<tr>
					<td>旧密码:</td>
					<td><input type="password" name="oldPassword" data-options="missingMessage:'不能为空',required:true,validType:['mylength[2,6,\'用户名在2-6个字符之间\']','isChar']"></input></td>
				</tr>
				<tr>
					<td>新密码:</td>
					<td><input type="password" name="newPassword" id="newPassword" data-options="invalidMessage:'请输入6-12位的字符',missingMessage:'不能为空',required:true,validType:['length[6,12]']"></input></td>
				</tr>
				<tr>
					<td>确认新密码:</td>
					<td><input type="password" name="newConfimPassword" data-options="missingMessage:'不能为空',required:true,validType:['equals[\'#newPassword\']']"></input>
					</td>
				</tr>	
			</table>
		</form>
</div>
<div class="dialog_footer" data-options="region:'south',border:false"
				style="text-align: right">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="updateUserPwd()" iconcls="icon-save" style="width: 80px">保存</a> 
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="javascript:$('#dialog_content').dialog('close')"
					iconcls="icon-cancel" style="width: 80px">取消</a>
</div>
<script>
var id = "<%=id%>";
/**
 * 更新用户
 */
 function updateUserPwd(){
		var flag = $("#update_pwd_form").form('enableValidation').form('validate');
		var data = $('#update_pwd_form').serialize();
		var url = contentPath+"/userInfoHandler/updateUserPwd";
		if(flag){
			Util.getAjaxData(url,data,function(data){
			       if(data.success){
			    	   $('#dialog_content').dialog('close');
			    	   topCenter("修改密码成功！");
			    	   $.cookie('clickUrl',"");
					   $.cookie('clickId', "");
					   setTimeout(time,2000);
			       }else{
			    	   topCenter(data.msg);
			       }
			},true);
		}
}
function time(){
	Util.getAjaxData(contentPath+"/userInfoHandler/logout", null, function(data){
		window.location.href = "../login.jsp";
	});
}
</script>