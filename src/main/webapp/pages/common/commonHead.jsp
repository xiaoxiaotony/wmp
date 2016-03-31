<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String skin = this.getServletContext().getAttribute("skin").toString();
%>
<script type="text/javascript">
	//定义全局变量
	var resourcesPath = '<%=path%>';
</script>
<head>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">   
<title>系统首页</title>
<link href="<%=path%>/resources/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/resources/css/font-awesome.css" rel="stylesheet" />
<link href="<%=path%>/resources/<%=skin%>/css/main.css" rel="stylesheet" type="text/css" />
<link href="<%=path%>/resources/js/myflow/lib/jquery-ui-1.11.4.custom/jquery-ui.min.css" rel="stylesheet" type="text/css"/>
<link href="<%=path%>/resources/js/myflow/css/myflow.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" type="text/css"
	href="<%=path%>/resources/js/plugins/upload/uploadify.css" />

<script src="<%=path%>/resources/js/jquery-1.11.1.min.js"></script>
<script src="<%=path%>/resources/js/jquery.cookie.js"></script>
<script src="<%=path%>/resources/js/json2.js"></script>
<script src="<%=path%>/resources/js/plugins/WdatePicker.js"></script>
<script src="<%=path%>/resources/js/form/jquery.form.min.js"></script>
<script src="<%=path%>/resources/js/form/jquery-form-custom.js"></script>
<script type="text/javascript"
	src="<%=path%>/resources/js/plugins/upload/jquery.uploadify.min.js"></script>
<!--grid-->
<link rel="stylesheet" type="text/css" href="../resources/themes/bootstrap/easyui.css">
<link rel="stylesheet" type="text/css" href="../resources/themes/ui.css">
<script src="../resources/js/jquery.easyui.min.js"></script>

	

<script type="text/javascript" src="<%=path%>/js/highcharts/highcharts.js"></script>
<script type="text/javascript" src="<%=path%>/js/highcharts/modules/exporting.js"></script>
<!--grid end-->
<script src="<%=path%>/resources/js/main.js"></script>
<script src="<%=path%>/resources/js/common/tools.js"></script>

<!-- fullcalendar -->
<script src="<%=path%>/resources/js/fullcalendar/fullcalendar.min.js"></script>
<link href="<%=path%>/resources/js/fullcalendar/fullcalendar.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src= "/wmp/dwr/util.js"></script >   
<script type="text/javascript" src= "/wmp/dwr/engine.js"></script >
<script type="text/javascript" src= "/wmp/dwr/interface/alarmUtil.js" ></script>
</head>
