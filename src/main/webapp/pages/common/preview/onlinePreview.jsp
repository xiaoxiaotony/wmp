<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<% 
String pathname = request.getParameter("pathname");
String filename = request.getParameter("filename");
String contextPath = request.getContextPath();
%>

<div class="modal-header">
	<button type="button" class="close" onclick="close_view(this);">
		&times;
	</button>
	<h4 class="modal-title" id="myModalLabel"></h4>
</div>

<div class="modal-body" style="padding: 1px 1px 1px 1px;">
	<div style="position: relative;">
		<a id="viewerPlaceHolder"
			style="width: 800px; height: 680px; display: block"></a>
		<script type="text/javascript">
			$.get("../resources/js/plugin/flexPaper/flexpaper_flash_debug.js",init());
			function init() {
				$.get("../resources/js/plugin/flexPaper/flexpaper_flash.js",function() {
					var url = contentPath+"/knowledgeLibHandler/getOnlineFileName";
					var pathname = "<%=pathname%>";
					var filename = "<%=filename%>";
					var path = "<%=contextPath %>";
					$("#myModalLabel").html(filename);
					Util.getData(url,{pathName:pathname,fileName:filename},function (data){
						var fp = new FlexPaperViewer(
								'../resources/js/plugin/flexPaper/FlexPaperViewer',
								'viewerPlaceHolder',
								{
									config : {
										SwfFile : escape(path + '/uploads/' + data.fileName),
										Scale : 0.6,
										ZoomTransition : 'easeOut',
										ZoomTime : 0.5,
										ZoomInterval : 0.2,
										FitPageOnLoad : true,
										FitWidthOnLoad : true,
										FullScreenAsMaxWindow : false,
										ProgressiveLoading : false,
										MinZoomSize : 0.2,
										MaxZoomSize : 5,
										SearchMatchAll : false,
										InitViewMode : 'Portrait',
										PrintPaperAsBitmap : false,

										ViewModeToolsVisible : true,
										ZoomToolsVisible : true,
										NavToolsVisible : true,
										CursorToolsVisible : true,
										SearchToolsVisible : true,

										localeChain : 'zh_CN'
									}
								});
					});
			   });
		}
		</script>
	</div>
</div>
<div class="modal-footer">
	<button type="button" class="btn btn-default" onclick="close_view(this);">
		取消</button>
</div>
<script>
     function close_view(e){
    	if($(e).attr("class") == "close"){
    	   $(e).parent(".modal-header").parent().parent().parent().modal("hide");
    	}else{
    	   $(e).parent(".modal-footer").parent().parent().parent().modal("hide");
    	}
		
     }
</script>