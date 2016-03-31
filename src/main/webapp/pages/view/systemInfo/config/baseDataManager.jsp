<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!--显示内容区域-->
<div title="基础数据类型" data-options="closable:true">
	<!--左侧-->
	<div class="cont_left">
		<!-- 查询面板 -->
		<div class="search_bar" id="left_search_bar_div"></div>
		<!-- 工具栏 -->
		<div class="cont_tit" id="left_button_bar_div"></div>
		<!--table-->
		<div class="table_cont">
			<table id="base_list_data" cellspacing="0" cellpadding="0"
				width="100%">
				<thead>
					<tr>
						<th field="dict_name" width="15%">基础数据类型名称</th>
						<th field="dict_code" width="30%">编码</th>
						<th field="dict_type" width="10%">状态</th>
						<th field="add_time" width="25%">创建时间</th>
						<th field="dictid" width="22%" data-options="align:'center'"
							formatter="oper_user">操作</th>
					</tr>
				</thead>
			</table>
		</div>
		<!--table end-->
	</div>
	<!--左侧 end-->

	<!--右侧-->
	<div class="cont_right">
		<!-- 查询面板 -->
		<div class="search_bar" id="right_search_bar_div"></div>
		<!-- 工具栏 -->
		<div class="cont_tit" id="rigth_button_bar_div"></div>
		<!--table-->
		<div class="table_cont">
			<table id="base_right_list_data" cellspacing="0" cellpadding="0"
				width="100%">
				<thead>
					<tr>
						<th data-options="field:'ck',checkbox:true"></th>
						<th field="dict_val_name" width="20%">配置数据名称</th>
						<th field="dict_code" width="20%">编码</th>
						<th field="dict_value" width="20%">配置值</th>
						<th field="add_time" width="20%">创建时间</th>
						<th field="dictid" width="18%" data-options="align:'center'"
							formatter="oper_user">操作</th>
					</tr>
				</thead>
			</table>
		</div>
	</div>
	<!--右侧-->
</div>
<script type="text/javascript" src="../pages/view/systemInfo/config/baseDataManager.js">