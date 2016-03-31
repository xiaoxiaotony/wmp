<%@page import="com.txy.web.common.bean.UserInfoBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
String longinName="";
if (request.getSession().getAttribute("user") != null) {
	UserInfoBean userBean = (UserInfoBean) session.getAttribute("user");
	longinName = userBean.getName();
	
}
%>
<div class="easyui-layout" style="height:100%;overflow: hidden;">
		<div data-options="region:'north'" style="height:auto;">
			<div class="search_bar" id="user_search_bar_div">
				<div style="float: left; margin: 2px 0">
					<div class="search_tit">班次：</div>
					<div class="search_txt">
						<input type="text" id="nowDay" readonly="readonly"
							style="width: 70px;" />
					</div>
					<div class="search_tit">时间：</div>
					<div class="search_txt">
						<input type="text" id="nowTime" readonly="readonly" />
					</div>
					<div class="search_tit">姓名：</div>
					<div class="search_txt">
						<input type="text"  readonly="readonly" value="<%=longinName %>" />
						<!--<select id="user" name="userName" class="easyui-combobox"
							style="width: 178px; height: 30px;"
							data-options="
	    					                                valueField:'id',
	    					                                textField:'name',
	    					                                panelHeight:'auto',
	    					                                editable:false,
	    					                                url:'../http/userInfoHandler/queryByCheck?is_now=asd',
	    					                                "></select>-->
					</div>
				</div>
				<div style="float: left; margin: 2px 0">
					<div class="search_button">
						<a class="button button01 updateBtn" href="javascript:void(0);">上班登记</a>
					</div>
				</div>
				<div class="search_txt" style="float: right">
					<div>
						状态：<span style="color: green; font-weight: bold;" id="status">登录系统</span>
					</div>
				</div>
			</div>
		</div>
		<div data-options="region:'center'" style="height:40%;top:0px;">
			<!--值班记录 工具栏-->
			<div class="cont_tit" id="work_button_bar_div"></div>
	
			<!--table-->
			<div class="table_cont" style="top:40px;">
				<table id="working_list_data">
					<thead>
						<tr>
							<th field="time" width="20%" sortable="false">记录时间</th>
							<th field="type" width="20%" formatter="formattype">日志类型</th>
							<th field="content" width="59%">日志内容</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
		<div data-options="region:'south',split:true" style="height:40%;">
			<!--日常故障 工具栏 -->
			<div class="cont_tit" id="error_button_bar_div"></div>
			<!--日常故障 数据表-->
			<div class="table_cont" style="top:40px;">
				<table id="error_list_data">
					<thead>
						<tr>
							<th field="record_time" width="15%" sortable="false">记录时间</th>
							<th field="trouble_type" width="20%" formatter="formattype">故障类型</th>
							<th field="recordname" width="15%">登记人</th>
							<th field="opertion_time" width="15%">处理时间</th>
							<th field="opertion_status" width="20%" formatter="formatStatus">处理状态</th>
							<th field="reportedname" width="14%">处理人</th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
<script>
	var date = new Date();
	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	var day = date.getDate() > 9 ? date.getDate() : '0' + date.getDate();
	var hour = date.getHours();
	var min = date.getMinutes();
	var se = date.getSeconds();
	var seq = '' + year + month + day;
	$("#nowTime").val(
			'' + year + '年' + month + '月' + day + '日 ' + hour + ':' + min + ':'
					+ se);
	$("#nowDay").val(seq);
	setTimeout("resh_time()", 1000);
	
	function resh_time() {
		date = new Date();

		year = date.getFullYear();
		month = date.getMonth() + 1;
		day = date.getDate() > 9 ? date.getDate() : '0' + date.getDate();
		hour = date.getHours();
		min = date.getMinutes();
		se = date.getSeconds();
		$("#nowTime").val(
				'' + year + '年' + month + '月' + day + '日 ' + hour + ':' + min
						+ ':' + se);
		$(".layout-panel").css('top','0px');
		setTimeout("resh_time()", 1000);
	}
	/**
	 * 初始化值班记录表格
	 */
	$('#working_list_data').datagrid({
		nowrap : false, //一行显示数据，提高加载性能true
		striped : true, //隔行换色
		collapsible : false,//是否可折叠的 
		fit : true,//height 100%
		url : contentPath + "/dutyHander/queryLogByUser",
		remoteSort : false, //要排序的数据从服务器定义 
		pageSize : 25,//每页显示的记录条数，默认为10 
		pageList : [ 25, 30, 50, 100 ],//可以设置每页记录条数的列表 
		pagination : false,//分页控件 
		rownumbers : false,//行号 
		//冻结列在左
		frozenColumns : [ [ {
			field : 'ck',
			checkbox : true
		} ] ]
	});

	var toolbarParams = {
		boolbarId : "work_button_bar_div",
		title : "值班记录列表",
		body : [ {
			icon : "icon-plus",
			text : "上报",
			hidden : false,
			event : function() {
				upload();
			}
		}, {
			icon : "icon-plus",
			text : "新增",
			hidden : false,
			event : function() {
				add();
			}
		} ]
	}
	/**
	 * 添加操作工具面板
	 */
	DataGrid.initToolsBarPanel(toolbarParams);

	function upload() {
		var rowData = $("#working_list_data").datagrid('getSelected');

		if (rowData == undefined || rowData.id == '') {
			alert('请选择您要上报的记录');
			return false;
		}

		$.post(contentPath + "/dutyHander/addTrouble", {
			id : rowData.id
		}, function(data) {
			if (data.data == 1) {
				alert('上报成功');
				$("#error_list_data").datagrid('load');
			} else {
				alert('上报失败');
			}
		}, 'json');
	}

	function formatStatus(val, row) {

		if (val == 1) {
			return "已处理";
		} else if (val == 0) {
			return "未处理";
		} else {
			return "数据异常";
		}
	}

	//格式化日志类型
	function formattype(val, row) {
		var result = '';
		switch (val) {
		case 1:
			result = '常规日志';
			break;
		case 2:
			result = '资料缺失';
			break;
		case 3:
			result = '设备故障';
			break;
		case 4:
			result = '其它日志';
			break;
		default:
			result = '数据异常';
			break;
		}

		return result;
	}
	/**
	 * 初始化日常故障记录表格
	 */
	$('#error_list_data').datagrid({
		nowrap : false, //一行显示数据，提高加载性能true
		striped : true, //隔行换色
		collapsible : false,//是否可折叠的 
		fit : true,//height 100%
		url : contentPath + "/dutyHander/queryRecordByUser",
		remoteSort : false, //要排序的数据从服务器定义 
		pageSize : 25,//每页显示的记录条数，默认为10 
		pageList : [ 25, 30, 50, 100 ],//可以设置每页记录条数的列表 
		pagination : false,//分页控件 
		rownumbers : false,//行号 
		//冻结列在左
		frozenColumns : [ [ {
			field : 'ck',
			checkbox : true
		} ] ]
	});

	var errorToolbarParams = {
		boolbarId : "error_button_bar_div",
		title : "日常故障列表",
		body : [ {
			icon : "icon-plus",
			text : "处理",
			hidden : false,
			event : function() {
				edit();
			}
		} ]
	}
	/**
	 * 添加操作工具面板
	 */
	DataGrid.initToolsBarPanel(errorToolbarParams);

	/**
	 * 添加值班记录
	 */
	function add() {
		$('#dialog_content').dialog({
			title : '值班记录',
			width : 500,
			height : 500,
			closed : false,
			cache : false,
			href : 'view/duty/dutyAddLog.jsp',
			modal : true
		});
	}

	function edit() {
		var rowData = $("#error_list_data").datagrid('getSelected');

		if (rowData == undefined || rowData.id < 0) {
			alert('请选择您要处理的故障记录');
			return false;
		}
		$('#dialog_content').dialog({
			title : '故障处理',
			width : 800,
			height : 500,
			closed : false,
			cache : false,
			href : 'view/duty/dutyFaulthandlinginput.jsp?id=' + rowData.id,
			modal : true
		});
	}

	$(".updateBtn").click(function() {

		var btn_str = $(".updateBtn").text();

		if (confirm('是否进行' + btn_str)) {
			var type = '上班登记' == btn_str ? 'start' : 'end';

			$.post('../http/dutyHander/workDuty', {
				dutyType : type
			}, function(data) {
				if (data.data > 0) {
					alert('登记成功');
				} else {
					alert('登记失败');
				}
			}, 'json');

		}

	});
</script>
