<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
 	String id = request.getParameter("id");
%>
<script>
$.get("../resources/js/common/easyUIvalidateExtends.js");
</script>
<div id="win_content">
		<form id="updateStationType" method="post">
			<table width="100%" class="dialog_table">
				<tr>
					<td>站号:</td>
					<td><input class="easyui-textbox" style="height:30px;width: 180px;" type="text" name="iiiii" data-options="missingMessage:'不能为空',required:true" readonly="readonly"></input></td>
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
					<td><input class="easyui-textbox" style="height:30px;width: 180px;" type="text" name="lo" data-options="missingMessage:'不能为空',required:true,validType:['mylength[1,12,\'经度在1-12个字符之间\']','isChar']"></input></td>
				</tr>
				<tr>
					<td>纬度:</td>
					<td><input class="easyui-textbox" style="height:30px;width: 180px;" type="text" name="la" data-options="missingMessage:'不能为空',required:true,validType:['mylength[1,12,\'纬度在1-12个字符之间\']','isChar']"></input></td>
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
					onclick="updateStationSubmit()" iconcls="icon-save" style="width: 80px">修改</a> 
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="javascript:$('#dialog_content').dialog('close')"
					iconcls="icon-cancel" style="width: 80px">取消</a>
</div>
<script>
var id = "<%=id%>";
$(function(){
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
	    },
	    onLoadSuccess : function(data){
	    	var url = contentPath+"/stationTypeHander/getDeviceInfoById?id="+id;
	    	Util.getAjaxData(url, null, function(data){
	    		$('#updateStationType').form('load',data.data.map);
	    		$('#muncpl').combotree('setValue', data.data.map.muncpl_id);
	    		$('#stype_id').combobox('setValue', data.data.map.stype_id);
	    	}, false);
	    }
	}); 
})


/**
 * 添加站点
 */
 function updateStationSubmit(){
		var flag = $("#updateStationType").form('enableValidation').form('validate');
		var data = $('#updateStationType').serialize();
		var muncpl =  $('#muncpl').combotree('getValue');
		var muncplText =  $('#muncpl').combotree('getText');
		var url = contentPath+"/stationTypeHander/upateStationInfo?id="+id+"&muncpl="+muncpl+"&muncplText="+muncplText;
		if(flag){
			Util.getAjaxData(url,data,function(data){
			       if(data.success){
			    	   $('#stationType_list_data').datagrid('reload');
			    	   $('#dialog_content').dialog('close');
			    	   topCenter("修改成功！");
			       }else{
			    	   topCenter(data.msg, 500);
			       }
			},true);
		}
}
</script>