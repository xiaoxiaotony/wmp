<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<% 
	String dtype = request.getParameter("dtype");
	String ctype = request.getParameter("ctype");
%>
<script>
$.get("../resources/js/common/easyUIvalidateExtends.js");
</script>
<div id="win_content">
		<form id="updateFileForm" method="post">
		  <table width="100%" class="dialog_table">
		    <tr>
		      <td>资料类型</td>
			  <td>
				  <input class="easyui-textbox" style="height:30px;width: 180px;" type="text" name="dtype" readonly="readonly"></input>
			   </td>
		    </tr>
		    <tr>
			   <td>资料子类型</td>
			   <td>
				   <input class="easyui-textbox" style="height:30px;width: 180px;" type="text" name="ctype" readonly="readonly"></input>
			    </td>
		    </tr>
		    <tr>
		    	<td>资料类型描述</td>
			  <td>
				  <input class="easyui-textbox" style="height:30px;width: 180px;" type="text" name="dtype_desc" readonly="readonly"></input>
			   </td>
		    </tr>
		    <tr>
			   <td>资料子类型描述</td>
			   <td>
				   <input class="easyui-textbox" style="height:30px;width: 250px;" type="text" name="ctype_desc" readonly="readonly"></input>
			    </td>
		    </tr>
		    <tr>
		    	<td>是否启用</td>
			  <td>
				  <select id="flage" class="easyui-combobox"
					name="flage"
					data-options="panelHeight:'auto',editable:false"
					style="width: 200px; height: 30px;">
						<option value="0">启用</option>
						<option value="1">禁用</option>
				</select></input>
			   </td>
		    </tr>
		    <tr>
			   <td>逾限时间</td>
			   <td>
				   <input class="easyui-textbox" style="height:30px;width: 180px;" type="text" name="outtime" data-options="required:true, validType: ['integer','length[0,5]']"></input>
			    </td>
		    </tr>
		    <tr>
		    	<td>缺报百分率</td>
			  <td>
				  <input class="easyui-textbox" style="height:30px;width: 180px;" type="text" name="misspg" data-options="required:true, validType: ['integer','length[0,5]']"></input>
			   </td>
		    </tr>
		    <!-- <tr>
		    	<td>接收及时时间</td>
			  <td>
				  <input class="easyui-numberspinner" style="height:30px;width: 180px;" type="text" name="jsh_time"></input>
			   </td>
			   <td>接收逾限时间</td>
			   <td>
				   <input class="easyui-numberspinner" style="height:30px;width: 180px;" type="text" name="yx_time"></input>
			    </td>
		    </tr>
		    <tr>
		    	<td>质量统计标志</td>
			  <td>
				  <input class="easyui-numberspinner" data-options="min:0,max:1" value="0" style="height:30px;width: 180px;" type="text" name="qflag"></input>
			   </td>
			   <td>基本文件标志</td>
			   <td>
				   <input class="easyui-numberspinner" data-options="min:0,max:1" value="0" style="height:30px;width: 180px;" type="text" name="bflag"></input>
			    </td>
		    </tr>
		    <tr>
		    	<td>时效统计标志</td>
			  <td>
				  <input class="easyui-numberspinner" data-options="min:0,max:1" value="0" style="height:30px;width: 180px;" type="text" name="tflag"></input>
			   </td>
			   <td>发送及时时间</td>
			   <td>
				   <input class="easyui-numberspinner" style="height:30px;width: 180px;" type="text" name="fsjs_time"></input>
			    </td>
		    </tr>
		    <tr>
		    	<td>发送逾限时间</td>
			  <td>
				  <input class="easyui-numberspinner" style="height:30px;width: 180px;" type="text" name="fsyx_time"></input>
			   </td>
			   <td>公报统计标志</td>
			   <td>
				   <input class="easyui-numberspinner" data-options="min:0,max:1" value="0" style="height:30px;width: 180px;" type="text" name="gflag" ></input>
			    </td>
		    </tr>
		    <tr>
		    	<td>文件类型标志</td>
			  <td>
				  <input class="easyui-numberspinner" data-options="min:0,max:1" value="0" style="height:30px;width: 180px;" type="text" name="file_flag"></input>
			   </td>
			   <td>公报类型标志</td>
			   <td>
				   <input class="easyui-numberspinner" data-options="min:0,max:1" value="0" style="height:30px;width: 180px;" type="text" name="bull_flag" ></input>
			    </td>
		    </tr>
		    <tr>
		    	<td>报告类型标志</td>
			  <td>
				  <input class="easyui-numberspinner" data-options="min:0,max:1" value="0" style="height:30px;width: 180px;" type="text" name="repo_flag" ></input>
			   </td>
			   <td>通知间隔</td>
			   <td>
				   <input class="easyui-numberspinner" style="height:30px;width: 180px;" type="text" name="intervaltimes"></input>
			    </td>
		    </tr>
		    <tr>
		    	<td>起始时间</td>
			  <td>
				  <input class="easyui-numberspinner" style="height:30px;width: 180px;" type="text" name="start_time" ></input>
			   </td>
			   <td>间隔时间</td>
			   <td>
				   <input class="easyui-numberspinner" style="height:30px;width: 180px;" type="text" name="jg_time"></input>
			    </td>
		    </tr>
		    <tr>
		    	<td>资料阈限时间</td>
			  <td colspan="3">
				  <input class="easyui-numberspinner" style="height:30px;width: 180px;" type="text" name="file_threshold_time" ></input>
			   </td>
		    </tr> -->
		    
          </table>
		</form>
</div>

<div class="dialog_footer" data-options="region:'south',border:false"
				style="text-align: right">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="updateFile()" iconcls="icon-save" style="width: 80px">保存</a> 
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="javascript:$('#dialog_content').dialog('close')"
					iconcls="icon-cancel" style="width: 80px">取消</a>
</div>
<script>

var dtype = "<%=dtype%>";
var ctype = "<%=ctype%>";
Util.getAjaxData(contentPath+"/systemBackInfoHandler/queryByDtype?dtype="+dtype+'&ctype='+ctype, null, function(data){
	$('#updateFileForm').form('load',data.data.map);
});


/**
 * 修改
 */
 function updateFile(){
	 var flag = $("#updateFileForm").form('enableValidation').form('validate');
		var data = $('#updateFileForm').serialize();
		var url = contentPath+"/systemBackInfoHandler/updateFile";
		if(flag){
			Util.getAjaxData(url,data,function(data){
			       if(data.success){
			    	   $('#fileMaintenance_list_data').datagrid('reload');
			    	   topCenter("数据修改成功！");
			    	   Util.closeWindow("dialog_content");
			       }else{
			    	   topCenter("数据修改失败！",500);
			       }
			},true);
		}
}
</script>