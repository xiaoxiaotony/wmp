<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div title="资料传输统计" data-options="closable:true" style="height: 100%;overflow:hidden;">
	<div class="search-tab">
		<ul>
			<li class="active" tab="../pages/view/transferMonitor/monitorCount/transferMonitorCountDay.jsp"><a href="#">日统计</a></li>
			<li tab="../pages/view/transferMonitor/monitorCount/transferMonitorCountTen.jsp"><a href="#">旬统计</a></li>
			<li tab="../pages/view/transferMonitor/monitorCount/transferMonitorCountMon.jsp"><a href="#">月统计</a></li>
			<li tab="../pages/view/transferMonitor/monitorCount/transferMonitorCountYear.jsp"><a href="#">年统计</a></li>
		</ul>
	</div>
	<div id="transferMonitorCount_con" style="padding-top: 5px;width: 100%;height: 96%;overflow:auto;"></div>
</div>

<script type="text/javascript"
	src="../pages/view/transferMonitor/monitorCount/transferMonitorProvinceCount.js" />
