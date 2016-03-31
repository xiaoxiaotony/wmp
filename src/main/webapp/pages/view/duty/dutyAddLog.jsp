<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script>
$.get("../resources/js/common/easyUIvalidateExtends.js");
</script>
<div id="win_content">
		<form id="addLog" method="post">
		  <table width="100%" class="dialog_table">
		    <tr>
		      <td width="10%">类型</td>
			  <td width="14%">
				  <select class="easyui-combobox" name="type" id="type" style="width:178px; height: 30px;" data-options="panelHeight:'auto',editable:false">
			          <option value="1">常规日志</option>
			          <option value="2">资料缺失</option>
			          <option value="3">设备故障</option>
			          <option value="4">其它日志</option>
			        </select>      
			   </td>
		    </tr>
			<tr>
		      <td colspan="2">详细内容</td>
		    </tr>
			<tr>
		      <td colspan="2">
			  	<textarea style="margin: 0px; width: 440px; height: 108px;" name="content" id="content"></textarea>
			  </td>
		    </tr>
          </table>
		</form>
</div>

<div class="dialog_footer" data-options="region:'south',border:false"
				style="text-align: right">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="addLog()" iconcls="icon-save" style="width: 80px">保存</a> 
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="javascript:$('#dialog_content').dialog('close')"
					iconcls="icon-cancel" style="width: 80px">取消</a>
</div>
<script>

/**
 * 添加用户
 */
 function addLog(){
		var type = $('#type').combotree('getValue');
		var content = $("#content").val();
		
		if(type<1){
			topCenter('请选择日志类型',500);
			return false;
		}
		
		if(content==undefined||content.length<1){
			topCenter("请填写内容",500);
			return false;
		}
		var url = contentPath+"/dutyHander/addLog";
		
		$.post(url,{type:type,content:content},function(data){
			if(data.data==1){
				topCenter("值班日志添加成功",500);
				$("#working_list_data").datagrid('load');
				$('#dialog_content').dialog('close');
				
			}else{
				topCenter("值班日志添加失败",500);
			}
		},'json');
}
</script>