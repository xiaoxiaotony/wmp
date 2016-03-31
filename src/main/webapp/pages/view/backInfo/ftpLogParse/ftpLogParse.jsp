<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!--显示内容区域-->
<div title="基础数据类型" data-options="closable:true">
	<!--左侧-->
	<div class="cont_three_left">
		需要解析日志文本：<textarea style="height: 96%;width: 98%;" id="sourceLog"></textarea>
	</div>
	<!--左侧 end-->
	<div class="cont_three_center">
		<div style="margin-top: 5px;height: 30px;"><a class='button button01' href='javascript:void(0);' onclick="parseFtpLog()">解析-->></a></div> 
		<div style="margin-top: 5px;height: 30px;"><a class='button button01' href='javascript:void(0);' onclick="explorOut()">导出结果</a></div> 
	</div>
	<!--右侧-->
	<div class="cont_three_right">
		解析的结果：<textarea style="height: 96%;width: 98%;" id="cont_three_textarea_div"></textarea>
	</div>
</div>
<script type="text/javascript" src="../pages/view/backInfo/ftpLogParse/ftpLogParse.js"></script>
