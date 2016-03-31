<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
 	String id = request.getParameter("id");
%>
<div id="win_content">
	<form id="showNetworkDevice" method="post">
			<table width="100%" class="dialog_table">
				<tr>
					<td>站点设备名称:</td>
					<td><input class="easyui-textbox" readonly="readonly" style="height:30px;width: 180px;" type="text" id="name" name="name"></input></td>
				</tr>
				<tr>
					<td>站点设备地址:</td>
					<td><input class="easyui-textbox" style="height:30px;width: 180px;" type="text" name="ip" id="ip"></input></td>
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
					onclick="javascript:$('#dialog_content').dialog('close')"
					iconcls="icon-cancel" style="width: 80px">取消</a>
</div>
<script>
var id = "<%=id%>";
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
    },
    onLoadSuccess : function(data){
    	var url = contentPath+"/netmonitorHander/getNetworkInfo?id="+id;
    	Util.getAjaxData(url, null, function(data){
    		$('#showNetworkDevice').form('load',data.data.map);
    		$('#areaId').combotree('setValue',data.data.map.area);
    	}, false);
    }
}); 
</script>