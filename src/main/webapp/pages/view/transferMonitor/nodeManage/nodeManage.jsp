<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div title="传输设备管理" data-options="closable:true">
	<!-- 查询面板 -->
	<div class="search_bar" id="nodeManage_search_bar_div"></div>
	<!-- 工具栏 -->
	<div class="cont_tit" id="nodeManage_button_bar_div"></div>
	<!--table-->
	<div class="table_cont">
		<table id="nodeManage_list_data">
			<thead>
				<tr>
					<th field="node_name" width="15%" sortable="true">节点名称</th>
					<th field="device_name" width="34%">关联设备</th>
					<th field="dict_val_name" width="10%">节点类型</th>
					<th field="areaname" width="8%">节点区域</th>
					<th field="node_desc" width="20%">节点描述</th>
					<th field="id" width="12%" data-options="align:'center'" formatter="oper_user">操作</th>
				</tr>
			</thead>
		</table>
	</div>
	<!--底部分页 end-->
</div>
<script type="text/javascript" src="../pages/view/transferMonitor/nodeManage/nodeManage.js"></script>