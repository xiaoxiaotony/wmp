<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div title="角色信息" data-options="closable:true">
	<!-- 查询面板 -->
	<div class="search_bar" id="role_search_bar_div"></div>
	<!-- 工具栏 -->
	<div class="cont_tit" id="button_bar_div"></div>
	<!--table-->
	<div class="table_cont">
		<table id="roleInfo_list_data" cellspacing="0" cellpadding="0" width="100%">
			<thead>
				<tr>
					<th data-options="field:'ck',checkbox:true" >用户ID</th>
					<th field="groupName" width="15%">角色名称</th>
					<th field="description" width="40%">描述</th>
					<th field="status" width="5%" formatter="formate_role_status">状态</th>
					<th field="addTime" width="20%">创建时间</th>
					<th field="roleId" width="19%" data-options="align:'center'" formatter="oper_user">操作</th>
				</tr>
			</thead>
		</table>
	</div>
	<!--底部分页 end-->
</div>
<div id="add_role_div" class="dialog_box"></div>
<script type="text/javascript" src="../pages/view/systemInfo/role/roleList.js" >
