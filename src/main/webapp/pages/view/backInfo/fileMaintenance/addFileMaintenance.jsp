<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<script>
$.get("../resources/js/common/easyUIvalidateExtends.js");
</script>
<div id="win_content">
		<form id="addFileForm" method="post">
		  <table width="100%" class="dialog_table">
		    <tr>
		      <td>资料类型</td>
			  <td>
				  <input class="easyui-textbox" style="height:30px;width: 180px;" type="text" name="dtype" data-options="missingMessage:'不能为空',required:true,validType:['checkName[\'/systemBackInfoHandler/checkDtype\',\'dtype\',\'资料类型已经存在\']','isChar']"></input>
			   </td>
			   <td>资料子类型</td>
			   <td>
				   <input class="easyui-textbox" style="height:30px;width: 180px;" type="text" name="ctype" data-options="missingMessage:'不能为空',required:true,validType:['checkName[\'/systemBackInfoHandler/checkCtype\',\'ctype\',\'资料子类型已经存在\']','isChar']"></input>
			    </td>
		    </tr>
		    <tr>
		    	<td>资料类型描述</td>
			  <td>
				  <input class="easyui-textbox" style="height:30px;width: 180px;" type="text" name="dtype_desc"></input>
			   </td>
			   <td>资料子类型描述</td>
			   <td>
				   <input class="easyui-textbox" style="height:30px;width: 180px;" type="text" name="ctype_desc"></input>
			    </td>
		    </tr>
		    <tr>
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
			  
		    </tr>
		    
          </table>
		</form>
</div>

<div class="dialog_footer" data-options="region:'south',border:false"
				style="text-align: right">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="addFile()" iconcls="icon-save" style="width: 80px">保存</a> 
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="javascript:$('#dialog_content').dialog('close')"
					iconcls="icon-cancel" style="width: 80px">取消</a>
</div>
<script>

/**
 * 添加用户
 */
 function addFile(){
	 var flag = $("#addFileForm").form('enableValidation').form('validate');
		var data = $('#addFileForm').serialize();
		var url = contentPath+"/systemBackInfoHandler/addFile";
		if(flag){
			Util.getAjaxData(url,data,function(data){
			       if(data.success){
			    	   $('#fileMaintenance_list_data').datagrid('reload');
			    	   topCenter("数据添加成功！",500);
			       }else{
			    	   topCenter("数据添加失败！",500);
			       }
			},true);
		}
}
</script>