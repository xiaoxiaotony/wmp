<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script>
$.get("../resources/js/common/easyUIvalidateExtends.js");
</script>
<div id="win_content">
		<form id="addNetWorkInfo" method="post">
			<table width="100%" class="dialog_table">
				<tr>
					<td>站点设备名称:</td>
					<td><input class="easyui-numberbox" style="height:30px;width: 180px;" type="text" name="name" data-options="missingMessage:'不能为空',required:true,validType:['mylength[2,6,\'用户名在2-6个字符之间\']','isChar']"></input></td>
				</tr>
				<tr>
					<td>站点设备地址:</td>
					<td><input class="easyui-textbox" style="height:30px;width: 180px;" type="text" name="deviceaddress" data-options="missingMessage:'不能为空',required:true,validType:['mylength[2,6,\'用户名在2-6个字符之间\']','checkName[\'/userInfoHandler/checkLoginName\',\'loginname\',\'用户名已经存在\']','isChar']"></input></td>
				</tr>
				<tr>
					<td>所属区域:</td>
					<td>
						<select id="areaId" style="width:180px;height: 30px;"></select>
					</td>
				</tr>
				<tr>
					<td>设备IP:</td>
					<td><input class="easyui-textbox" style="height:30px;width: 180px;" type="text" name="ip" data-options="missingMessage:'不能为空',required:true"></input></td>
				</tr>
			</table>
		</form>
</div>

<div class="dialog_footer" data-options="region:'south',border:false"
				style="text-align: right">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="addUser()" iconcls="icon-save" style="width: 80px">保存</a> 
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


/**
 * 添加用户
 */
 function addUser(){
		var areaId = $('#areaId').combotree('getValue');
		var flag = $("#addNetWorkInfo").form('enableValidation').form('validate');
		var data = $('#addNetWorkInfo').serialize();
		data += "&areaId="+areaId;
		var url = contentPath+"/netmonitorHander/addNetWorkInfo";
		if(flag){
			Util.getAjaxData(url,data,function(data){
			       if(data.success){
			    	   $('#networkInfo_list_data').datagrid('reload');
			    	   $('#dialog_content').dialog('close');
			    	   topCenter("添加成功！");
			       }else{
			    	   topCenter("添加失败！",500);
			       }
			},true);
		}
}
</script>