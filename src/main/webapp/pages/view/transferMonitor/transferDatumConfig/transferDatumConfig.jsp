<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div title="资料传输流程配置" data-options="closable:true">
	<div style="position: absolute;left:1%; width:50%;height:4%;overflow: hidden;right: 0; top: 1%;bottom: 0;">
		<span style="font-weight:bold;">站点:</span>
	</div>
	
	<div id="station_tree_div" style="position: absolute;left:1%; width:49%;height:45%;overflow-y:auto;right: 0; top: 5%;bottom: 0;border: 1px solid black;">
		<ul class="easyui-tree" id="station_tree_panel"></ul>
	</div>
	
	<div style="position: absolute;left:51%; width:50%;height:4%;overflow: hidden;right: 0; top: 1%;bottom: 0;">
		<span style="font-weight:bold;">节点:</span>
		<span style="margin-left: 85%;"><a href="javascript:void(0);" class="button button01" onclick="savaData();">保存</a></span>
	</div>
	<div style="position: absolute;left:51%; width:48%;height:45%;overflow: hidden;right: 0; top: 5%;bottom: 0;border: 1px solid black;">
		<div class="easyui-panel" id="node_tree_div" style="padding:5px">
			<ul class="easyui-tree" id="node_tree_panel"></ul>
		</div>
	</div>
	
	<div style="position: absolute;left:1%; width:98%;overflow: hidden;right: 0; top: 51%;bottom: 0;">
		<span style="font-weight:bold;">节点配置列表:</span>
	</div>
	
	<div style="position: absolute;left:1%; width:98%;height:45%;overflow: hidden;right: 0; top: 55%;bottom: 0;">
		<table id="transferDatumConfig_list_data">
			<thead>
				<tr>
					<th field="node_sort_name" data-options="align:'center'" width="80%" sortable="true">节点名称</th>
					<th field="transfer_name" data-options="hidden:'true'" sortable="true"></th>
					<th field="id" width="19%" data-options="align:'center'" formatter="oper_transferDatumConfig">操作</th>
				</tr>
			</thead>
		</table>
	</div>
	
	<!--底部分页 end-->
</div>
<script type="text/javascript" src="../pages/view/transferMonitor/transferDatumConfig/transferDatumConfig.js" />