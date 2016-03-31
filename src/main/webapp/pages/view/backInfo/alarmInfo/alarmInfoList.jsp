<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div title="告警数据列表" data-options="closable:true">
	<!-- 查询面板 -->
	<div class="search_bar" id="alarmInfoList_search_bar_div"></div>
	<!-- 工具栏 -->
	<div class="cont_tit" id="alarmInfoList_button_bar_div"></div>
	<!--table-->
	<div class="table_cont">
		<table id="alarmInfoList_list_data">
			<thead>
				<tr>
					<th field="ip" width="15%">站点设备地址</th>
					<th field="type" width="10%" sortable="true" formatter="oper_type">告警类型</th>
					<th field="alarmlevel" width="10%" sortable="true" formatter="oper_alarmlevel">告警等级</th>
					<th field="isread" width="10%" sortable="true">是否忽略</th>
					<th field="alarmtime" width="15%">告警时间</th>
					<th field="descption" width="39%">告警描述</th>
				</tr>
			</thead>
		</table>
	</div>
	<!--底部分页 end-->
</div>
<script type="text/javascript" src="../pages/view/backInfo/alarmInfo/alarmInfoList.js"></script>