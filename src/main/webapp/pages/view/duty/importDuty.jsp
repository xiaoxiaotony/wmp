<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script>
$.get("../resources/js/common/easyUIvalidateExtends.js");
</script>
<div id="win_content">
		<form id="importDudtyInfo"   action="../http/dutyHander/importDutyInfo" enctype="multipart/form-data" method="post">
			<table width="100%" class="dialog_table">
				<tr>
					<td>值班记录:</td>
					<td><input id="dutyFileId" style="height:30px;width: 180px;" type="file" name="dutyFile" ></input></td>
				</tr>
			</table>
		</form>
</div>

<div class="dialog_footer" data-options="region:'south',border:false"
				style="text-align: right">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="importDuty()" iconcls="icon-save" style="width: 80px">保存</a> 
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="javascript:$('#dialog_content').dialog('close')"
					iconcls="icon-cancel" style="width: 80px">取消</a>
</div>
<script>
 var options = {
	    beforeSubmit:  showRequest,  //提交前处理
	    success:       showResponse,  //处理完成
	    dataType: 'json',
	    resetForm: false 
	};
	
	//提交前校验数据
	function showRequest(formData, jqForm, options) {
		var file = $("#dutyFileId").val();
		if (file == null || file == "")
		{
			 topCenter("上传值班文件");
			return false;
		}
	    return true;
	}
	//提交后的回调函数   
	function showResponse(data, status)  {
		if (status == 'success')
		{
			if(data.data.success)
			{
			     topCenter("导入成功！");
			     $('#dialog_content').dialog('close');
			    $('#dutyInfo_list_data').datagrid('reload');
			 }else{
			    topCenter("导入失败，检查excel格式");
			}
		}
		else
		{
			 topCenter("导入失败，检查excel格式");
		}
		
	}
/**
 * 导入数据
 */
 function importDuty(){
 		load();
		$("#importDudtyInfo").ajaxSubmit(options);
} 

function load()
{
	 $("<div class=\"datagrid-mask\"></div>").css({display:"block",width:"100%",height:$(window).height()}).appendTo("#win_content");
    $("<div class=\"datagrid-mask-msg\"></div>").html("正在导入...").appendTo("#win_content").css({display:"block",left:170,top:80});  
}

	
</script>