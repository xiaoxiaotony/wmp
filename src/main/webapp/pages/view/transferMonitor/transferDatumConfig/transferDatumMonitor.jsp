<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div title="资料传输实时监控" data-options="closable:true" style="width: 100%;height: 94%;">

	<!-- 查询面板开始 -->
	<form id='transfer_form_id'>
		<div class="search_bar">
			<div style='float:left; margin:2px 0'>
				<div class='search_tit'>选择时间：</div>
				<div class='search_txt'>
					<input type="text" class="Wdate" name='datetime'
						id='transfer_datetime'
						onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH',isShowClear:false,readOnly:true})"
						style='width:130px;height:30px;' onchange="change_param();" />
				</div>
			</div>
			<div style='float:left; margin:2px 0'>
				<div class='search_tit'>区域：</div>
				<div class="search_txt">
					<input name='station_dic' id='transfer_stationdic'
						style='width:115px;height:32px;' />
				</div>
			</div>
			<div style='float:left; margin:2px 25px'>
				<div class="search_tit" style='margin:5px 10px'>
					<a href="#" class="button button01" onclick="date_add(-1);">前一小时</a>
				</div>
				<div class="search_tit" style='margin:5px 10px'>
					<a href="#" class="button button01" onclick="date_add(1);">后一小时</a>
				</div>
				<div class="search_tit" style='margin:5px 10px'>
					<a href="#" class="button button01" onclick="date_add(0);">当前小时</a>
				</div>
			</div>
			<div class="search_txt"
				style='float:right;font-size : 1.5em;margin:4px 20px'>
				当前监控时间：<span class='color-green' style='font-size : 1em'
					id='now_datetime'></span>
			</div>
			<div class="clr"></div>
		</div>
	</form>
	<!-- 查询面板结束 -->


	<!---实时传输-->
	<div class="realtime-wrap">
		<ul class="clear" id="transfer_tables_all">

		</ul>
	</div>
	<!---实时传输end-->


</div>
<script type="text/javascript"
	src="../pages/view/transferMonitor/transferDatumConfig/transferDatumMonitor.js" />
