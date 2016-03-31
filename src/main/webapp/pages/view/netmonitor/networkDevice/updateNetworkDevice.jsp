<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
 	String id = request.getParameter("id");
%>
<div id="win_content">
	<form id="upateNetWorkInfo" method="post">
			<table width="100%" class="dialog_table">
				<tr>
					<td>站点设备名称:</td>
					<td><input class="easyui-textbox" readonly="readonly" style="height:30px;width: 180px;" type="text" id="name" name="name" data-options="missingMessage:'不能为空',required:true"></input></td>
				</tr>
				<tr>
					<td>站点设备地址:</td>
					<td><input class="easyui-textbox" style="height:30px;width: 180px;" type="text" name="ip" data-options="missingMessage:'不能为空',required:true"></input></td>
				</tr>
				<tr>
					<td>所属区域:</td>
					<td>
						<select id="areaId" style="width:180px;height: 30px;"></select>
					</td>
				</tr>
				<tr>
					<td>设备IP:</td>
					<td><input class="easyui-textbox" style="height:30px;width: 180px;" type="text" name="ip" id="ip" data-options="missingMessage:'不能为空',required:true"></input></td>
				</tr>
			</table>
		</form>
</div>
<div class="dialog_footer" data-options="region:'south',border:false"
				style="text-align: right">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="update_deviceInfo()" iconcls="icon-save" style="width: 80px">保存</a> 
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="javascript:$('#dialog_content').dialog('close')"
					iconcls="icon-cancel" style="width: 80px">取消</a>
</div>
<script>
var id = "<%=id%>";
var url = contentPath+"/netmonitorHander/getNetworkInfo?id="+id;
$('#areaId').combotree( {  
    url: contentPath+"/systemBackInfoHandler/getAreaListTree",
    onSelect : function(node) {  
        var tree = $(this).tree;  
        var isLeaf = tree('isLeaf', node.target);  
        if (!isLeaf) {  
            $('#areaId').combotree('clear');  
        }  
    },
    onLoadSuccess : function(data){
    	var url = contentPath+"/netmonitorHander/getNetworkInfo?id="+id;
    	Util.getAjaxData(url, null, function(data){
    		$('#upateNetWorkInfo').form('load',data.data.map);
    		$('#areaId').combotree('setValue',data.data.map.area);
    	}, false);
    }
});

function update_deviceInfo(){
	var areaId = $('#areaId').combotree('getValue');
	var flag = $("#upateNetWorkInfo").form('enableValidation').form('validate');
	var data = $('#upateNetWorkInfo').serialize();
	var url = contentPath+"/netmonitorHander/updateNetworkDeviceInfo?areaId="+areaId+"&id="+id;
	if(flag){
		Util.getAjaxData(url,data,function(data){
		       if(data.success){
		    	   $('#networkInfo_list_data').datagrid('reload');
		    	   $('#dialog_content').dialog('close');
		    	   topCenter("修改成功！");
		       }else{
		    	   topCenter("修改失败！",500);
		       }
		},true);
	}
}
</script>