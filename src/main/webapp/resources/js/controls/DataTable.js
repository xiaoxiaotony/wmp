

var DataGrid = {};
DataGrid.initQueryPanel = function(formPanel) {
	var queryFormHtml = createQueryForm(formPanel);
	$("#"+formPanel.queryId).empty().html(queryFormHtml);
	var fromId = formPanel.queryId+"_from";
	if($(".search_bar").innerHeight() > 40){
		$(".table_cont").css({
			top:$(".search_bar").innerHeight()+$(".cont_tit").innerHeight()+15
		});
	}
	$(".queryFormBtn").on("click",function(){
		var data = $("#"+fromId).serializeArray();
		var temp = $(".on_selected_tabs_div");
		if(temp.length != 0){
			var tempObj = new Object();  
			tempObj.name = $(".on_selected_tabs_div").attr("target");  
			tempObj.value = $(".on_selected_tabs_div").attr("value");  
			data.push(tempObj);
		}
		$("#"+formPanel.queryDataGrid).datagrid('reload',{param:JSON.stringify(data)});
	});
	$(".tabs_index_div").on("click",function(){
		var value = $(this).css("background-color","yellow").addClass("on_selected_tabs_div").attr("value");
		var target = $(this).siblings().css("background-color","#00BBFF").removeClass("on_selected_tabs_div").attr("target");
	});
	$(".resetFrom").on("click",function(){
		$("#"+fromId).form("reset");
	});
}

DataGrid.initToolsBarPanel = function(toolbarPanel) {
	var toolsBarHtml = createToolsBarHtml(toolbarPanel)
	$("#" + toolbarPanel.boolbarId).empty().html(toolsBarHtml);
}

/**
 * 获取查询面板参数
 */
DataGrid.getQueryPanelParam = function(formId){
	var data = $("#"+formId+"_from").serializeArray();
	return JSON.stringify(data);
}
/**
 * 创建查询面板
 * 
 * @param formPanel
 * @returns {String}
 */
function createQueryForm(formPanel) {
	var num = formPanel.body.length;
	var htmlStr = "<form id='"+formPanel.queryId+"_from'>";
	for (var i = 0; i < num; i++) {
		if (formPanel.body[i].type == 'select') {
			if(formPanel.body[i].listData){
				htmlStr += "<div style='float:left; margin:2px 0'><div class='search_tit'>"+formPanel.body[i].name+"</div><div class='search_txt'><input name='"+formPanel.body[i].column+"' style='width:115px;height:32px;' class='easyui-combobox' data-options='panelHeight:\"auto\",editable:false,valueField: \"value\",textField: \"label\",data: "+formPanel.body[i].listData+"'/></div></div>";
			}else if (formPanel.body[i].url){
				htmlStr += "<div style='float:left; margin:2px 0'><div class='search_tit'>"+formPanel.body[i].name+"</div><div class='search_txt'><input name='"+formPanel.body[i].column+"' style='width:115px;height:32px;' class='easyui-combobox' data-options='textField:\""+formPanel.body[i].textField+"\",valueField:\""+formPanel.body[i].valueField+"\",panelHeight:\"auto\",editable:false,url: \""+formPanel.body[i].url+"\"' /></div></div>";
			}
		}
		if (formPanel.body[i].type == 'date') {
			var datefmt = "";
			var defaultDate = "";
			var defaultDate_1 = "";
			if(formPanel.body[i].dateFormat) datefmt = "{dateFmt:\""+formPanel.body[i].dateFormat+"\"}";
			if(formPanel.body[i].dateFormat && formPanel.body[i].defaultDate){
				var nowDate = new Date(); 
				defaultDate = nowDate.format(formPanel.body[i].dateFormat);
				var nowDate_1 = new Date(nowDate.setDate(nowDate.getDate()+1));
				defaultDate_1 = nowDate_1.format(formPanel.body[i].dateFormat);
			}
			if(formPanel.body[i].column.length==2){
				htmlStr +="<div class='search_tit' style='float:left; margin:6px 0'>"+formPanel.body[i].name+"</div><div class='search_txt_max' style='margin:8px 0'><input class='Wdate' value='"+defaultDate+"' name='"+formPanel.body[i].column[0]+"' onClick='WdatePicker("+datefmt+")' style='width:130px;height:32px;'>&nbsp;&nbsp;至&nbsp;&nbsp;<input class='Wdate' value='"+defaultDate_1+"' name='"+formPanel.body[i].column[1]+"' onClick='WdatePicker("+datefmt+")' style='width:130px;height:30px;'></div>";
			}else{
				htmlStr +="<div class='search_tit' style='float:left; margin:6px 0'>"+formPanel.body[i].name+"</div><div class='search_txt' style='margin:7px 0'><input class='Wdate' value='"+defaultDate+"'  onClick='WdatePicker("+datefmt+")' name='"+formPanel.body[i].column[0]+"' style='width:130px;height:32px;'></div>";
			}
		}
		
		if (formPanel.body[i].type == 'text') {
			if (!formPanel.body[i].placeholder) {
				formPanel.body[i].placeholder = "";
			}
			htmlStr += "<div style='float:left; margin:2px 0'><div class='search_tit'>"
					+ formPanel.body[i].name	
					+ "</div> <div class='search_txt'> <input type='text' style='width:110px;' placeholder='" 
					+ formPanel.body[i].placeholder + "' name='"
					+ formPanel.body[i].column + "' id='"
					+ formPanel.body[i].column + "'></div></div>";
		}
		if(formPanel.body[i].type == 'checkBox'){
			if(formPanel.body[i].listData){
				var myobj = eval(formPanel.body[i].listData);
				htmlStr +="<div style='padding-top:5px;float:left;padding-left: 8px;margin-right:5px;'>";
				for(var k = 0; k<myobj.length; k++){
					htmlStr += "<label style='padding-left: 4px;'><input style='padding-top:5px;' name='"+formPanel.body[i].column+"' type='checkbox' value='"+myobj[k].value+"' />&nbsp;"+myobj[k].label+"</label>";
				}
				htmlStr+="</div>";
			}
		}
		if(formPanel.body[i].type == 'tabs'){
			if(formPanel.body[i].listData){
				var myobj = eval(formPanel.body[i].listData);
				htmlStr +="<div style='padding-top:5px;float:left;padding-left: 8px;margin-right:5px;'>";
				for(var k = 0; k<myobj.length; k++){
					htmlStr += "<label class='tabs_index_div' target='"+formPanel.body[i].column+"' style='background-color:#00BBFF;height:28px;line-height:23px;padding-left: 4px;border:1px solid #CCCCCC;font-size:14px;margin:0 10px;padding:0 8px;' value='"+myobj[k].value+"'>&nbsp;"+myobj[k].label+"</label>";
				}
				htmlStr+="</div>";
			}
		}
	}
	htmlStr += "</form><div style='float:left; margin:2px 0'><div class='search_button'><a class='button button01 queryFormBtn' href='javascript:void(0);'><i class='icon-search'></i>查询</a></div> <div class='search_button'><a class='button button01 resetFrom' href='javascript:void(0);' ><i class='icon-search'></i>重置</a></div></div>";
	return htmlStr;
}

/**
 * 查询工具条面板
 */
function createToolsBarHtml(toolbarPanel) {
	var htmlStr = "<b>" + toolbarPanel.title + "</b><div class='cont_button'>";
	if(toolbarPanel.body){
		var num = toolbarPanel.body.length;
		for (var i = 0; i < num; i++) {
			if(toolbarPanel.body[i].hidden){
				htmlStr += "<span style='display:none;'><a href='javascript:void(0)' onclick='("+toolbarPanel.body[i].event+")()' class='button'><i class='"
				+ toolbarPanel.body[i].icon + "'></i>&nbsp;&nbsp;&nbsp;"
				+ toolbarPanel.body[i].text + "</a></span>";
			}else{
				htmlStr += "<span><a href='javascript:void(0)' onclick='("+toolbarPanel.body[i].event+")()' class='button'><i class='"
				+ toolbarPanel.body[i].icon + "'></i>&nbsp;&nbsp;&nbsp;"
				+ toolbarPanel.body[i].text + "</a></span>";
			}
		}
	}
	if(toolbarPanel.mark){
		htmlStr +=toolbarPanel.mark;
	}
	htmlStr += "</div>";
	return htmlStr;
}