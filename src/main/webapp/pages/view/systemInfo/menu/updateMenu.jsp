<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<% 
	String id = request.getParameter("id"); 
%>
<script>
$.get("../resources/js/common/easyUIvalidateExtends.js");
</script>
<div id="win_content">
		<form id="updateMenuInfo" method="post">
			<table width="100%" class="dialog_table">
				<tr>
					<td>菜单名称:</td>
					<td><input class="easyui-textbox" style="height: 30px;" type="text" name="name" data-options="missingMessage:'不能为空',required:true"></input></td>
				</tr>
				<tr>
					<td>地址:</td>
					<td><input  type="text" id="open_address"  name="url" readonly="readonly"></input></td>
				</tr>
				<tr>
					<td>icon:</td>
					<td><input  type="text" id="icon" name="icon" onclick="img_select(this);" oninput="img_display();" readonly="readonly" onblur="img_display();"></input></td>
				</tr>
				<tr>
					<td>是否为子节点:</td>
					<td><select class="easyui-combobox" name="leaf" style="width:178px;height: 30px;" data-options="panelHeight:'auto',editable:false,onChange:combChange">
							<option value="1">是</option>
							<option value="0">否</option>
						</select>
					</td>
				</tr>				
				<tr>
					<td>状态:</td>
					<td><select class="easyui-combobox" name="enable" style="width:178px;height: 30px;" data-options="panelHeight:'auto',editable:false">
							<option value="0">启用</option>
							<option value="1">禁用</option>
						</select>
					</td>
				</tr>
			</table>
		</form>
</div>

<div class="dialog_footer" data-options="region:'south',border:false"
				style="text-align: right">
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="saveMenu()" iconcls="icon-save" style="width: 80px">保存</a> 
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="javascript:$('#dialog_content').dialog('close')"
					iconcls="icon-cancel" style="width: 80px">取消</a>
</div>


<!-- 图片选择 -->
<div id="imgs" style="display: none;
						position: absolute;
						border: 2px solid lightblue;
						background-color:white;
						height:100px;
						width:200px;
						z-index:1000;
						overflow: auto;">
</div>

<script>
/**
 * 选择图片
 */
 var flag = true;
 function img_select(e){
// 	var top = $(e).offset().top;
// 	var left = $(e).offset().left;
// 	top -= 298;
// 	left -= 710;
//     var left = getX(e);
//     var top = getY(e);
    
    var top = $(e).position().top+33;

    var left = $(e).position().left;
	
	
 	$("#imgs").css({"display":"block","top":top,"left":left});
 	var url = contentPath+"/menuInfoHandler/getMenuIcons";
 	if(flag){
 		Util.getAjaxData(url, null, function(data){
 			flag = false;
 			var imgs = "";
 			for(var i = 0,len = data.length; i < len ; i++){
 			   if(data[i] == "xtgl_ico.png") continue;
 	           imgs += "<img alt='' src='../resources/electric_skin/icons/"+data[i]+"' onclick='select_img(\""+data[i]+"\");' style='cursor:pointer;' onmouseover='change_img_over(this);' onmouseout='change_img_out(this);'/>";
 			}
 		 	$("#imgs").append(imgs);
 		}, true);
 	}
}
function img_display(){
	setTimeout(function(){
			flag = true;
			$("#imgs").css({"display":"none"});
			$("#imgs").html("");
	},200);
}
function select_img(img_name){
	$("#ICON").val(img_name);
}
function change_img_over(e){
    $(e).css("border","1px solid black");
}
function change_img_out(e){
 	$(e).css("border","");
}
function getX(e) {
	e = e || window.event;
	return e.pageX || e.clientX + document.body.scroolLeft;
}
function getY(e) {
	e = e|| window.event;
	return e.pageY || e.clientY + document.boyd.scrollTop;
}
/**
 * end 图片选择
 */



var id = "<%=id%>";
var parent_id;
$(function(){
	Util.getAjaxData(contentPath+"/menuInfoHandler/queryMenuById?id="+id, null, function(data){
		$('#updateMenuInfo').form('load',data.data);
		parent_id = data.data.parentId;
		if(data.data.leaf == 0){
	    	$("#open_address").attr({
	    		  disabled:true,
	    	});
	    	$("#open_address").css({"backgroundColor":"#CDCDCD"});
		}
	});
});



function combChange(newValue, oldValue){
      if(newValue  >  0){
    	  $("#open_address").attr({
    		  disabled:false,
    	  });
      }else{
    	  $("#open_address").attr({
    		  disabled:true,
    	  });
    	  $("#open_address").css({"backgroundColor":"#CDCDCD"});
      }
}
/**
 * 添加菜单
 */
 function saveMenu(){
		var flag = $("#updateMenuInfo").form('enableValidation').form('validate');
		var data = $('#updateMenuInfo').serialize();
		data += "&parent_id="+parent_id+"&menu_id="+id+"&type="+1;
		var url = contentPath+"/menuInfoHandler/updateMenu";
		if(flag){
			Util.getAjaxData(url,data,function(data){
			       if(data.success){
			    	   $('#menu_Info_list_data').datagrid('reload');
			    	   $('#menu_tree_panel').tree('reload');
			    	   Util.showMsg("修改菜单成功！",350);
			       }else{
			    	   Util.showMsg("修改菜单失败！",350);
			       }
			},true);
		}
}

</script>