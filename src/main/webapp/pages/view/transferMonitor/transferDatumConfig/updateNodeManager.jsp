<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% 
	String config_id = request.getParameter("config_id");
%>
<div id="content">
	<table id="updateNodeManager_list_data" width="100%" class="table05">
		<!-- <thead>
			<tr>
				<th field="station_id" width="50%" sortable="true">站号</th>
				<th field="sname" width="35%" data-options="align:'center'" >站名</th>
			</tr>
		</thead> -->
	</table>
</div>
<div class="dialog_footer" data-options="region:'south',border:false"
				style="text-align: right">
				<a href="javascript:void(0)" id="saveButtonId" class="easyui-linkbutton"
					onclick="update()" iconcls="icon-save" style="width: 80px">保存</a> 
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="javascript:$('#dialog_content').dialog('close')"
					iconcls="icon-cancel" style="width: 80px">取消</a>
</div>
<script>
var config_id = "<%=config_id %>";
var editRow = undefined;
var editIndex = undefined;
$(function(){
	$('#updateNodeManager_list_data').datagrid({
	    fitColumns : true,
		autoRowHeight : false,
		striped : true,
		nowrap : false,
		pagination : true,
		rownumbers : true,
	    url:contentPath+"/transferMonitorHander/getNodes?config_id="+config_id, 
	    pagination:true,//分页控件 
	    pageSize: 20,//每页显示的记录条数，默认为10 
	    pageList: [20,50],
	    columns : [ [ {
							title : '节点号',
							field : 'node_id'
						}, {
							title : '节点名称',
							field : 'node_name',
							width : 100
						}
						, {
							title : '顺序',
							field : 'node_sort',
							width : 100,
							editor: {
								    type: 'text'
								}
						}
						] ]  ,
						onClickCell: function (index, field, value) {
							if (endEditing()){
								$('#updateNodeManager_list_data').datagrid('selectRow', index).datagrid('editCell', {index:index,field:field});
								editIndex = index;
							}
    				}    
		});
})

function endEditing(){
	if (editIndex == undefined){return true}
	if ($('#updateNodeManager_list_data').datagrid('validateRow', editIndex)){
		$('#updateNodeManager_list_data').datagrid('endEdit', editIndex);
		editIndex = undefined;
		return true;
	} else {
		return false;
	}
}

$.extend($.fn.datagrid.methods, {
	editCell: function(jq,param){
		return jq.each(function(){
			var opts = $(this).datagrid('options');
			var fields = $(this).datagrid('getColumnFields',true).concat($(this).datagrid('getColumnFields'));
			for(var i=0; i<fields.length; i++){
				var col = $(this).datagrid('getColumnOption', fields[i]);
				col.editor1 = col.editor;
				if (fields[i] != param.field){
					col.editor = null;
				}
			}
			$(this).datagrid('beginEdit', param.index);
			for(var i=0; i<fields.length; i++){
				var col = $(this).datagrid('getColumnOption', fields[i]);
				col.editor = col.editor1;
			}
		});
	}
});

function update()
{
   $('#updateNodeManager_list_data').datagrid('endEdit', editIndex); //行编辑完成
	var nodesIds = new Array();
	var checkedItems = $('#updateNodeManager_list_data').datagrid('getRows');
		$.each(checkedItems, function(index, item){
			nodesIds.push(item["node_id"]+","+item["node_sort"]);
		}); 
	var nids = nodesIds.join(";");
	var url = contentPath+"/transferMonitorHander/saveNodeUpdate";
	var data = {nodeIds: nids,configId: config_id};
	Util.getAjaxData(url,data,function(data){
	       if(data.success){
	    	   $("#transferDatumConfig_list_data").datagrid("reload");
		       $('#updateNodeManager_list_data').datagrid("reload");
	    	   topCenter("添加成功！");
	       }else{
	    	   topCenter("添加失败！",500);
	       }
	},true); 
}
</script>