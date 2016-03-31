<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div title="用户管理" data-options="closable:true">
	<!-- 查询面板 -->
	<div class="search_bar" id="stationType_search_bar_div"></div>
	<!-- 工具栏 -->
	<div class="cont_tit" id="stationType_button_bar_user_div"></div>
	<!--table-->
	<div class="table_cont">
		<table id="stationType_list_data">
			<thead>
				<tr>
					<th field="iiiii" width="11%" align="center">站号</th>
					<th field="sname" width="15%" align="center">站点名称</th>
					<th field="muncpl" width="9%" align="center">地域</th>
					<th field="stype_name" width="13%" align="center">类型站</th>
					<th field="stype_id" width="8%">站点类型编码</th>
					<th field="ischeck" width="7%" formatter="oper_assess">是否考核站</th>
					<th field="cname" width="7%">区县</th>
					<th field="lo" width="9%">经度</th>
					<th field="la" width="9%">纬度</th>
					<th field="id" width="9%" data-options="align:'center'" formatter="oper_user">操作</th>
				</tr>
			</thead>
		</table>
	</div>
	<!--底部分页 end-->
</div>

<!-- 加载js -->
<script type="text/javascript" src="../pages/view/transferMonitor/stationManage/stationType/stationType.js" >