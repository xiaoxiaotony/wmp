<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div title="行政区域" data-options="closable:true">
	<div style="position: absolute;left:0%; width:18%;overflow: hidden;right: 0; top: 0;bottom: 0;">
		<div class="easyui-panel" id="administrativeArea_tree_div" style="padding:5px">
			<ul class="easyui-tree" id="administrativeArea_tree_panel"></ul>
		</div>
	</div>
	
	<div style="position: absolute;left: 18.5%;overflow: hidden;right: 0;top: 0;bottom: 0;">
		<!-- 工具栏 -->
		<div class="cont_tit" id="administrativeArea_button_bar_div"></div>
		<!--table-->
		<div class="table_cont" style="top: 40px;">
			<table id="administrativeArea_Info_list_data" cellspacing="0" cellpadding="0"
				width="100%">
				<thead>
					<tr>
						<th field="text" width="45%">行政区域名称</th>
						<th field="code" width="37.8%">编码</th>
						<th field="enable" width="10%" formatter="oper_status">状态</th>
						<th field="id" width="9%" data-options="align:'center'" formatter="oper_area">操作</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
	<!--底部分页 end-->
</div>

<script type="text/javascript" src="../pages/view/backInfo/administrativeArea/administrativeArea.js" >
