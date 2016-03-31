<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% 
	String config_id = request.getParameter("config_id");
%>
<div id="win_content">
	<table id="updateStationManager_list_data" width="100%" class="table05">
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
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="update()" iconcls="icon-save" style="width: 80px">保存</a> 
				<a href="javascript:void(0)" class="easyui-linkbutton"
					onclick="javascript:$('#dialog_content').dialog('close')"
					iconcls="icon-cancel" style="width: 80px">取消</a>
</div>
<script>
 //url:contentPath+"/transferMonitorHander/getStationListByConfigId?config_id="+config_id, 
	var config_id = "<%=config_id %>";
$(function(){
	$('#updateStationManager_list_data').datagrid({
	    fitColumns : true,
		autoRowHeight : false,
		striped : true,
		nowrap : true,
		selectOnCheck : true,
		checkOnSelect : true,
		pagination : true,
		rownumbers : true,
	    url:contentPath+"/transferMonitorHander/getAllStations?config_id="+config_id, 
	    pagination:true,//分页控件 
	    pageSize: 10,//每页显示的记录条数，默认为10 
	    pageList: [5,10,20,50],
	    columns : [ [ {
							title : 'sid',
							field : 'sid',
							checkbox : true
						}, {
							title : '站号',
							field : 'station_id',
							width : 100
						}
						, {
							title : '站名',
							field : 'sname',
							width : 100
						}
						] ],
						onLoadSuccess:function(data){        
							if(data){
								 $.each(data.rows, function(index, item){
											if(item.isChecked){
												$('#updateStationManager_list_data').datagrid('checkRow', index);
											}
										});
							
							}
						}       
		});
})

function update()
{
	var ids = new Array();
	var checkedItems = $('#updateStationManager_list_data').datagrid('getChecked');
		$.each(checkedItems, function(index, item){
			ids.push(item["sid"]);
		}); 
	var sids = ids.join(";");
	var url = contentPath+"/transferMonitorHander/saveUpdate";
	var data = {stationIds: sids,configId: config_id};
	Util.getAjaxData(url,data,function(data){
	       if(data.success){
	    	   topCenter("修改成功！");
	       }else{
	    	   topCenter("修改失败！",500);
	       }
	},true);
}
</script>