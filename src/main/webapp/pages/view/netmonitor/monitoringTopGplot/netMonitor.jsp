<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<div id="handlerMouseover" style="display:none;position: absolute;">
	</div>
	<ul id="contextmenu_fun" style="display:none;">    
            <!-- <li><a>放大</a></li>
            <li><a>缩小</a></li>   -->  
            <li><a>ping</a></li>    
            <li><a>telnet</a></li>  
    </ul>
<div title="" data-options="closable:true">
	<div id="fa_div" style="left: 0.5%;overflow: hidden;right: 0;top: 0;bottom: 0;border: 1px solid black;">
		<!-- 工具栏 
		<div class="cont_tit" id="topoMonitor_button_bar_div"></div>
		 -->
		<!--table-->
		<div>
		    <canvas id="topoMonitor_canvas" style="background-color:gray; cursor: default;">
		    </canvas>
		</div>
	</div>
	<!--底部分页 end-->
</div>
<!-- topo组件加载 -->
<script type="text/javascript" src="../resources/js/plugins/jtopo/jquery.snippet.min.js"></script>
<script type="text/javascript" src="../resources/js/plugins/jtopo/jtopo-0.4.8-min.js"></script>
<script type="text/javascript" src="../resources/js/plugins/jtopo/jtopo-toolbar.js"></script>
<script type="text/javascript" src="../resources/js/common/jquery.messager.js"></script>
<script type="text/javascript" src="../pages/view/netmonitor/monitoringTopGplot/netMonitor.js"></script>