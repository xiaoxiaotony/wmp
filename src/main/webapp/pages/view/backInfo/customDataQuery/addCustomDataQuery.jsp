<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script>
$.get("../resources/js/common/easyUIvalidateExtends.js");
</script>
<div id="win_content">
		<form id="addCustomDataInfo" method="post">
			<table width="100%" class="dialog_table">
				<tr>
					<td>定制查询数据名称:</td>
					<td><input class="easyui-textbox" style="height:30px;width: 180px;" type="text" name="name" data-options="missingMessage:'不能为空',required:true,validType:['mylength[2,20,\'用户名在2-20个字符之间\']','isChar']"></input></td>
				</tr>
				<tr>
					<td>文件存储方式:</td>
					<td>
						<input class="easyui-combobox"  style="width:180px;height: 30px;" id="storeTypeId" 
						data-options="valueField: 'label', textField: 'value', data: [{ label: '2', value: '本地或共享盘' },{ label: '3', value: 'ftp盘' }]" />
					</td>
				</tr>
					<tr class="connectClass">
						<td>用户名称:</td>
						<td><input class="easyui-textbox" name="username" id="userNameId" style="height:30px;width: 180px;" type="text" ></input></td>
					</tr>
					<tr  class="connectClass">
						<td>密码:</td>
						<td><input class="easyui-textbox" name="pwd" id="pwdId" style="height:30px;width: 180px;" type="text"></input></td>
					</tr>
					<tr  class="connectClass">
						<td>IP地址:</td>
						<td><input class="easyui-textbox"  name="ip" id="ipId" style="height:30px;width: 180px;" type="text"></input></td>
					</tr>
				<tr>
					<td>导出区域:</td>
					<td><input class="easyui-textbox" id="exportAreaId" style="height:30px;width: 180px;" type="text"  ></input></td>
				</tr>
				<tr>
					<td>输出文件路径:</td>
					<td><input class="easyui-textbox" style="height:30px;width: 180px;" type="text" id="filepath" name="filepath" data-options="missingMessage:'不能为空',required:true"></input></td>
				</tr>
				<tr>
					<td>周期:</td>
					<td>
						<select id="period" name="period" class="easyui-combobox" style="width:180px;height: 30px;">
							<option value="1">每天</option>
							<option value="2">每周</option>
							<option value="3">每月</option>
							<option value="4">每年</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>导出类型:</td>
					<td>
						<select id="exploerModel"  name="exploerModel" class="easyui-combobox" style="width:180px;height: 30px;">
						</select>
					</td>
				</tr>
				<tr>
					<td>导出字段:</td>
					<td><input class="easyui-textbox" id="exportRowId" style="height:30px;width: 180px;" type="text"></input></td>
				</tr>
				<tr>
					<td>格式类型:</td>
					<td>
						<select id="format_file" name="format_file" class="easyui-combobox" style="width:180px;height: 30px;">
							<option value="txt">txt</option>
							<option value="xls">xls</option>
						</select>
					</td>
				</tr>
			</table>
		</form>
</div>
<div class="dialog_footer" data-options="region:'south',border:false"
				style="text-align: right">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="addCustomDataQuery()" iconcls="icon-save" style="width: 80px">保存</a> 
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="javascript:$('#dialog_content').dialog('close')"
					iconcls="icon-cancel" style="width: 80px">取消</a>
</div>
<script>

$('#exportAreaId').combotree( {  
    //获取数据URL  
    url: contentPath+"/systemBackInfoHandler/getAreaTree",
    multiple: true,
    //选择树节点触发事件  
    onSelect : function(node) {  
        //返回树对象  
        var tree = $(this).tree;  
        //选中的节点是否为叶子节点,如果不是叶子节点,清除选中  
        var isLeaf = tree('isLeaf', node.target);  
        if (!isLeaf) {  
            //清除选中  
            $('#exportAreaId').combotree('clear');  
        }  
    } ,
    onLoadSuccess: function () {
    
     }
});   



$(document).ready(function(){
	var url = "../pages/view/backInfo/customDataQuery/modelType.json";
	Util.getAjaxData(url,null,function(data){
		       		var jsonData = data;
                    if (jsonData != null && jsonData != '')
                     {
                     var htmlSelect = "";
                     	Util.getAjaxData(contentPath+"/systemBackInfoHandler/queryCustomDataModels","",function(datas){
								  $.each(jsonData, function (n, value) { 
										var modelTable = value.modelTable;
										var modelName = value.modelName;
										var isExit = false;
										for(var i = 0; i < datas.length;i++)
										{
											var obj = datas[i];
											var modelType = obj['MODEL_TYPE'];
											if (modelType != null && modelTable.toLowerCase() == modelType.toLowerCase())
											{
												isExit = true;
												break;
											}
											
										}
										if (!isExit)
										{
											htmlSelect += "<option value='"+modelTable+"'>"+modelName+"</option>";
										}
										
									});
						},false);
                     
                     $("#exploerModel").append(htmlSelect);
                       loadRowTree();
					}
		},false);
		
	$("#exploerModel").change(function(){
		loadRowTree();
	});
	
	$("#storeTypeId").combobox({
		
		onChange: function (n,o) {
			initStore();
		},
		onLoadSuccess: function () { //数据加载完毕事件
             var data = $('#storeTypeId').combobox('getData');
                if (data.length > 0) {
                    $("#storeTypeId").combobox('select', data[0].label);
               }
        }
	});
});

function initStore()
{	
         var storeType = $("#storeTypeId").combobox("getValue");
		if (storeType == 2 || storeType == 3)
		{
			$(".connectClass").css({display:""});
		}
		else
		{
			$(".connectClass").css({display:"none"});
		} 
}

function loadRowTree()
{
		var modelType = $("#exploerModel").val();
		if (modelType != null && modelType != '')
		{
			$('#exportRowId').combotree( {  
		    //获取数据URL  
		    url: contentPath+"/systemBackInfoHandler/getRowTree?modelType="+modelType,
		    multiple: true,
		    //选择树节点触发事件  
		    onSelect : function(node) {  
		        //返回树对象  
		        var tree = $(this).tree;  
		        //选中的节点是否为叶子节点,如果不是叶子节点,清除选中  
		        var isLeaf = tree('isLeaf', node.target);  
		        if (!isLeaf) {  
		            //清除选中  
		            $('#exportRowId').combotree('clear');  
		        }  
		    } ,
		    onLoadSuccess: function () {
	   			  }
			});  
		}
}

function addCustomDataQuery(){

	var area = $('#exportAreaId').combotree('getValues');
	var rowname = $('#exportRowId').combotree('getValues');

	var flag = $("#addCustomDataInfo").form('enableValidation').form('validate');
	if(!flag)
	{
		return;
	}
	var storeType = $("#storeTypeId").combobox("getValues");
	if (storeType != "1")
	{
		var res =  validateConfig();
		if(!res)
		{
			return;
		}
		
	}
	else
	{
		var storePath = $("#filepath").val();
		var pathRes = testPath(storePath);
		if(!pathRes)
		{
			return;
		}
	}

	var vr =  validateParam(area,rowname);
	if(!vr)
	{
		return;
	}

	
	var username = $("#userNameId").val();
	var pwd = $("#pwdId").val();
	var ip = $("#ipId").val();
	var filepath = $("#filepath").val();
	var checkUrl = contentPath+"/systemBackInfoHandler/checkStoreSpace?storeType="+storeType
					+"&username="+username+"&pwd="+pwd+"&ip="+ip+"&filepath="+filepath;
	Util.getAjaxData(checkUrl,null,function(data){
       if(data.data.success){
			var data = $('#addCustomDataInfo').serialize();
			data+="&storeType="+storeType+"&area="+area+"&rowname="+rowname;
			var url = contentPath+"/systemBackInfoHandler/addCustomDataInfo";
				Util.getAjaxData(url,data,function(data){
				       if(data.data.success){
				    	   $('#customDataQuery_list_data').datagrid('reload');
				    	   $('#dialog_content').dialog('close');
				    	   topCenter("添加成功！");
				       }else{
				    	   topCenter("添加失败！");
				       }
				},true);
       }else{
       		if (storeType == 1)
       		{
       			  topCenter("本地文件保存路径不正确");
       		}
       		else if (storeType == 2)
       		{
       			 topCenter("共享盘配置不正确");
       		}
       		else
       		{
       			topCenter("ftp配置不正确");
       		}
       }
	},true);
}

function validateConfig()
{
	var username = $("#userNameId").val();
	if (username == null || "" == username)
	{
		topCenter("请配置用户名");
		return false;
	}
	var pwd = $("#pwdId").val();
	if (pwd == null || "" == pwd)
	{
		topCenter("请配置密码");
		return false;
	}
	var ip = $("#ipId").val();
	
	if (ip == null || "" == ip)
	{
		topCenter("请配置ip");
		return false;
	}
	return true;
}


function validateParam(area,rowName)
{
	if (area == null || "" == area)
	{
		topCenter("请选择导出区域");
		return false;
	}
	if (rowName == null || "" == rowName)
	{
		topCenter("请选择导出字段");
		return false;
	}
	return true;
}

function testPath(storePath) {
    var path =  /^[a-zA-Z]:[\\]((?! )(?![^\\/]*\s+[\\/])[\w -]+[\\/])*(?! )(?![^.]*\s+\.)[\w -]+$/;
    if (!path.test(storePath))
     {
    	topCenter("文件路径不正确");
        return false;
    }
    return true;
}

//提示消息弹窗
function topCenter(messges,dialog_size){
	if(!dialog_size) dialog_size=0;
	$.messager.show({
		title:'消息',
		msg:messges,
		showType:'slide',
		style:{
				right:'',
				top:document.body.scrollTop+document.documentElement.scrollTop-dialog_size,
				bottom:''
		}
	});
}


</script>