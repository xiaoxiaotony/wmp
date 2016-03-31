<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<% 
	String id = request.getParameter("id"); 
%>
<script>
$.get("../resources/js/common/easyUIvalidateExtends.js");
</script>

<div id="win_content">
		<form id="updateUserInfo" method="post">
			<table width="100%" class="dialog_table">
			    <input type="hidden" value="<%=id %>" name='id'>
				<tr>
					<td>用户名:</td>
					<td><input class="easyui-textbox"  style="height:30px;width: 180px;" type="text" name="name" data-options="missingMessage:'不能为空',required:true,validType:['mylength[2,6,\'用户名在2-6个字符之间\']','isChar']"></input></td>
				</tr>
				<tr>
					<td>登陆名:</td>
					<td><input class="easyui-textbox"  style="height:30px;width: 180px;" type="text" name="loginName" data-options="disabled:true,missingMessage:'不能为空',required:true,validType:['mylength[2,6,\'用户名在2-6个字符之间\']','checkName[\'/userInfoHandler/checkLoginName\',\'loginname\',\'用户名已经存在\']','isChar']"></input></td>
				</tr>
				<tr>
					<td>状态:</td>
					<td><select class="easyui-combobox" style="width:178px; height: 30px;" name="status" style="width:178px;" data-options="panelHeight:'auto',editable:false">
							<option value="0">启用</option>
							<option value="1">禁用</option>
						</select>
					</td>
				</tr>				
				<tr>
					<td>关注区域:</td>
					<td>
						<select id="attentionAreaId" style="width:180px;height: 30px;"></select>
					</td>
				</tr>
				<tr>
					<td>所属角色:</td>
					<td>
    					<select id="role" name="role" class="easyui-combobox" style="width:178px; height: 30px;" data-options="
    					                                valueField:'roleId',
    					                                textField:'description',
    					                                panelHeight:'auto',
    					                                editable:false,
    					                                url:'../http/roleManagerHandler/getRoleListByUser',
    					                                onLoadSuccess:roleLoad"></select>
					</td>
				</tr>
				<tr>
					<td>电话号码:</td>
					<td><input class="easyui-textbox" style="height:30px;width: 180px;" type="text" name="phone" data-options="validType:'phone'"></input></td>
				</tr>
				<tr>
					<td>联系地址:</td>
					<td><input class="easyui-textbox" style="height:30px;width: 180px;" type="text" name="address" data-options="validType:['mylength[5,50,\'联系地址在5-50个字符之间\']','isChar']"></input></td>
				</tr>
			</table>
		</form>
</div>
<div class="dialog_footer" data-options="region:'south',border:false"
				style="text-align: right">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="updateUser()" iconcls="icon-save" style="width: 80px">保存</a> 
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="javascript:$('#dialog_content').dialog('close')"
					iconcls="icon-cancel" style="width: 80px">取消</a>
</div>
<script>
var id = "<%=id%>";

$('#attentionAreaId').combotree( {  
    //获取数据URL  
    url: contentPath+"/systemBackInfoHandler/getAreaListTree",
    multiple: true,
    //选择树节点触发事件  
    onSelect : function(node) {  
        //返回树对象  
        var tree = $(this).tree;  
        //选中的节点是否为叶子节点,如果不是叶子节点,清除选中  
        var isLeaf = tree('isLeaf', node.target);  
        if (!isLeaf) {  
            //清除选中  
            $('#attentionAreaId').combotree('clear');  
        }  
    }  
}); 

//加载表单数据
function roleLoad(param){
	Util.getAjaxData(contentPath+"/userInfoHandler/getUserInfoById?id="+id, null, function(data){
		$('#updateUserInfo').form('load',data.data);
		var roleId = data.data.reInfoBean.roleId;
		$('#role').combobox('setValue', roleId);
		 $('#attentionAreaId').combotree('setValues',data.data.attentionArea.split(',')); 
	});
}
/**
 * 更新用户
 */
 function updateUser(){
		var flag = $("#updateUserInfo").form('enableValidation').form('validate');
		var data = $('#updateUserInfo').serialize();
		var attentionAreaId = $('#attentionAreaId').combotree('getValues');
		var url = contentPath+"/userInfoHandler/updateUserInfoBean?attentionAreaId="+attentionAreaId;
		if(flag){
			Util.getAjaxData(url,data,function(data){
			       if(data.success){
			    	   $('#userInfo_list_data').datagrid('reload');
			    	   $('#dialog_content').dialog('close');
			    	   topCenter("修改用户成功！");
			       }else{
			    	   topCenter("修改用户失败！");
			       }
			},true);
		}
}
</script>