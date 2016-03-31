<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div title="传输设备拓扑配置" data-options="closable:true">
	<div class="left-menu" id="areaConfigDivId">
    </div>
	<div class="networkConfig_div_box"  style="position: absolute; left: 9.5%; overflow: hidden; right: 0; top: 0; bottom: 0;">
		<div id="deviceflow_tools"
			style="position: absolute; top: 10; left: 10; background-color: #fff; width: 120px; cursor: default; padding: 3px;"
			class="ui-widget-content">
			<div id="deviceflow_tools_handle" style="text-align: center;" class="ui-widget-header">工具集</div>
			<div class="node" id="myflow_save">
				<img src="../resources/js/myflow/img/save.gif" />&nbsp;&nbsp;保存
			</div>
			<div class="node selectable" id="path">
				<img src="../resources/js/myflow/img/16/flow_sequence.png" />&nbsp;&nbsp;转换
			</div>
			<div id="centerContext" >
			</div>
		</div>
		<div id="myflow"></div>
		<!--底部分页 end-->
	</div>
</div>

<script type="text/javascript" src="../resources/js/myflow/lib/raphael.js"></script>
<script type="text/javascript" src="../resources/js/myflow/lib/jquery-ui-1.11.4.custom/jquery-ui.min.js"></script>
<script type="text/javascript" src="../resources/js/myflow/deviceflow.js"></script>
<script type="text/javascript" src="../resources/js/myflow/myflow.jpdl3.js"></script>
<script type="text/javascript" src="../resources/js/myflow/myflow.editors.js"></script>
<script type="text/javascript" src="../pages/view/transferMonitor/deviceDevicesConfig/deviceDevicesConfig.js"></script>