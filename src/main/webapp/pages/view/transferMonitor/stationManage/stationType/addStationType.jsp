<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script>
$.get("../resources/js/common/easyUIvalidateExtends.js");
</script>

<div id="win_content">
		<form id="addStationType" method="post">
			<table width="100%" class="dialog_table">
				<tr>
					<td>站号:</td>
					<td><input class="easyui-textbox" style="height:30px;width: 180px;" type="text" name="iiiii" data-options="missingMessage:'不能为空',required:true"></input></td>
				</tr>
				<tr>
					<td>站点名称:</td>
					<td><input class="easyui-textbox" style="height:30px;width: 180px;" type="text" name="sname" data-options="missingMessage:'不能为空',required:true"></input></td>
				</tr>
				<tr>
					<td>区域:</td>
					<td><select id="muncpl" style="width:180px;height:30px;" data-options="missingMessage:'不能为空',required:true"></select></td>
				</tr>
				<tr>
					<td>站点类型:</td>
					<td><select class="easyui-combobox" name="stype_id" style="width:178px; height: 30px;" data-options="panelHeight:'auto',editable:false">
							<option value="A">A</option>
							<option value="B">B</option>
							<option value="C">C</option>
							<option value="J">J</option>
							<option value="Y">Y</option>
						</select></td>
				</tr>
				<tr>
					<td>经度:</td>
					<td><input class="easyui-textbox" style="height:30px;width: 180px;" type="text" name="lo" data-options="missingMessage:'不能为空',required:true"></input></td>
				</tr>
				<tr>
					<td>纬度:</td>
					<td><input class="easyui-textbox" style="height:30px;width: 180px;" type="text" name="la" data-options="missingMessage:'不能为空',required:true"></input></td>
				</tr>
				<tr>
					<td>是否考核站:</td>
					<td><select class="easyui-combobox" name="ischeck" style="width:178px; height: 30px;" data-options="panelHeight:'auto',editable:false">
							<option value="0">是</option>
							<option value="1">否</option>
						</select></td>
				</tr>
			</table>
		</form>
</div>

<div class="dialog_footer" data-options="region:'south',border:false"
				style="text-align: right">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="addStationSubmit()" iconcls="icon-save" style="width: 80px">保存</a> 
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="javascript:$('#dialog_content').dialog('close')"
					iconcls="icon-cancel" style="width: 80px">取消</a>
</div>
<script>
$('#muncpl').combotree( {  
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
            $('#muncpl').combotree('clear');  
        }  
    }  
}); 
/**
 * 添加站点
 */
 function addStationSubmit(){
		var flag = $("#addStationType").form('enableValidation').form('validate');
		var data = $('#addStationType').serialize();
		var muncpl =  $('#muncpl').combotree('getValue');
		var muncplText =  $('#muncpl').combotree('getText');
		var url = contentPath+"/stationTypeHander/addStationInfo?muncpl="+muncpl+"&muncplText="+muncplText;
		if(flag){
			Util.getAjaxData(url,data,function(data){
			       if(data.success){
			    	   $('#stationType_list_data').datagrid('reload');
			    	   topCenter("添加成功！");
			       }else{
			    	   topCenter("添加失败！");
			       }
			},true);
		}
}
</script>