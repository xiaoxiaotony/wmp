<%@ page language="java" contentType="text/html; charset=UTF-8"
	%>
<div title="传输资料" data-options="closable:true" >
	       <div class="search_bar">
                    
                    <div class="search_tit" style="width: 65px;">选择时间：</div>
                    <div class="search_txt"> 
                        <input style="width: 140px;" type="text" id="time_id"  class="Wdate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'});" />
                    </div>
                   
                    <div class="search_tit" style="width: 65px;">查询区域：</div>
                    <div class="search_txt">
                        <input id="queryAreaId" style="width: 250px;height: 32px;" class="easyui-combobox"  />  

                    </div> 
                    
                    <div class="search_tit" style="width: 420px;text-align: center;" id="check_id">
                    </div>
                    
                    <div class="search_tit"><a href="#" class="button button01" onclick="queryData();">查询</a>
                    </div>
                    <!-- <div class="search_tit"><a href="#" class="button button01">重置</a>
                    </div> -->
           </div>
	<!--table-->
	<div class="table_cont easyui-tabs" id="tt" style="top: 55px; width: 60%;">
	  <div title="报文" id="bw"  class="table_cont_tabs_div" style="height:92%">  
          <table id="groundRealTimeDataShows_list_data_id">
          	<thead data-options="frozen:true">
				<tr>
					 <th field="area" width="10%" align="center"  sortable="true">地区</th>
	         			<th field="time" width="15%" align="center" sortable="true">时间</th>
	         			<th field="zh" width="10%" align="center"  sortable="true">站号</th>
	         			<th field="zm" width="10%" align="center"  sortable="true">站名</th>
				</tr>
			</thead>
			<thead>
				<tr>
						
	         			<th field="wd" width="10%" align="center"  sortable="true">温度(℃)</th>
	         			<th field="sl" width="20%" align="center"  sortable="true">降水(mm)</th>
	         			<th field="fs" width="10%" align="center"  sortable="true">风速(m/s)</th>
	         			<th field="fx" width="20%" align="center"  sortable="true">风向</th>
	         			<th field="sd" width="10%" align="center"  sortable="true">相对湿度(%RH)</th>
	         			<th field="qy" width="8%" align="center"  sortable="true">气压(hpa)</th> 
				</tr>
			</thead>
		</table>
		
      </div>  
       <div   title="cimiss"  id="cimiss" closable="false" class="table_cont_tabs_div" style="height:92%;width: 60%;" > 
		 <table id="groundRealTimeDataShows_list_data_id_cimiss" >
		 	<thead data-options="frozen:true">
				<tr>
					 <th field="area" width="10%" align="center"  sortable="true">地区</th>
	         			<th field="time" width="15%" align="center" sortable="true">时间</th>
	         			<th field="zh" width="10%" align="center"  sortable="true">站号</th>
	         			<th field="zm" width="10%" align="center"  sortable="true">站名</th>
				</tr>
			</thead>
			<thead>
				<tr>
	         			<th field="wd" width="10%" align="center" sortable="true">温度(℃)</th>
	         			<th field="sl" width="10%" align="center" sortable="true">降水(mm)</th>
	         			<th field="fs" width="10%" align="center"  sortable="true">风速(m/s)</th>
	         			<th field="fx" width="10%" align="center" sortable="true">风向</th>
	         			<th field="sd" width="10%" align="center"  sortable="true">相对湿度(%RH)</th>
	         			<th field="qy" width="8%" align="center"  sortable="true">气压(hpa)</th>
				</tr>
			</thead>
		</table>
	  </div>   
	</div>
	<div id="chart_div_box" style="position: absolute; width:40%;right: 0; top: 12%;bottom: 0;height:350px; overflow-y:auto;">
		<div id="rtitle" style="height:7%;padding-left:15px;right: 0; width:90%;bottom: 0;font-size:14px;font-weight:bold;">
		</div>
		<div  id="container1" style="position: absolute;width:100%;height:200px;right: 0; top: 35px;">
		</div>
		<div id="container2" style="position: absolute;width:100%;height:200px;right: 0; top: 235px;">
		</div>
		<div id="container3" style="position: absolute;width:100%;height:200px;right: 0; top: 435px;">
		</div>
	</div>
	
	
	<!--底部分页 end-->
</div>

<!-- 加载js -->

<script type="text/javascript" >
	getChartDivBox();
 $('#queryAreaId').combotree( {  
    //获取数据URL  
    url: contentPath+"/groundRealTimeDataHander/getListTree",
    multiple: true,
    //选择树节点触发事件  
    onSelect : function(node) {  
        //返回树对象  
        var tree = $(this).tree;  
        //选中的节点是否为叶子节点,如果不是叶子节点,清除选中  
        var isLeaf = tree('isLeaf', node.target);  
        if (!isLeaf) {  
            //清除选中  
            $('#queryAreaId').combotree('clear');  
        }  
    } ,
    onLoadSuccess: function () {
    
     	var htmlCheck = "";
		Util.getAjaxData(contentPath+"/groundRealTimeDataHander/stationTypes","",function(data){
		for(var i = 0; i < data.length;i++)
		{
			var obj = data[i];
			if (obj['STYPE_ID'] == 'C')
			{
				htmlCheck+= "<label style='font-weight:normal;'><input  name='checkbox' checked='true'  type='checkbox' value='"+obj['STYPE_ID']+"' class='checkClass'/> "+obj['STYPE_NAME']+"</label>&nbsp;";
			}
			else
			{
				htmlCheck+= "<label style='font-weight:normal;'><input  name='checkbox'   type='checkbox' value='"+obj['STYPE_ID']+"' class='checkClass'/> "+obj['STYPE_NAME']+"</label>&nbsp;";
			}
		}
		$("#check_id").append(htmlCheck);
		var time2 = new Date().Format("yyyy-MM-dd HH:mm:ss");
		$("#time_id").attr("value",time2);
		},false);
		
     }
});   

function getChartDivBox(){
	var height = $(window).height()-245;
	$("#chart_div_box").css({"height":height});
}

Date.prototype.Format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "H+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

var toolbarParams = {
	boolbarId : "groundRealTimeDataShows_button_bar_user_div",
	title : "地面实时资料列表:",
	mark :"<font style='color:red;'>*备注:黄色标注为数据不同、红色为超过阈值 、-- 为无观测</font>"
}
/**
 * 添加操作工具面板
 */
DataGrid.initToolsBarPanel(toolbarParams);

/* var stationCode = $('#queryAreaId').combotree('getValues');
 */		loadGroundData("/groundRealTimeDataHander/getGroundRealTimeData?stationTypes=C");
		laodCimissData("/groundRealTimeDataHander/getCimissData?stationTypes=C&stationCode=1");
function loadGroundData(loadUrl)
{
	$('#groundRealTimeDataShows_list_data_id').datagrid({
	    nowrap: false,  //一行显示数据，提高加载性能true
	    striped: true,  //隔行换色
	    collapsible:false,//是否可折叠的 
	    fit: true,//height 100%
	    height:"100",
	     singleSelect:true,
	    url:contentPath+loadUrl, 
	    remoteSort:false, //要排序的数据从服务器定义 
		pageSize: 10,//每页显示的记录条数，默认为10 
	    pageList: [10,30,50,100],//可以设置每页记录条数的列表 
	    pagination:true,//分页控件
	    rownumbers:true,
	    onSelect: function (item) {
		    	loadChart();
	     },
	    onLoadSuccess:function(data)
	    {
	    	 $(this).datagrid('selectRow',0);
	    }
	});
}
function loadChart()
{
	var row = $("#groundRealTimeDataShows_list_data_id").datagrid('getSelected');
	var stationCode = row == null ? "" : row.zh;
	title_(row);
	initTab("ground",stationCode);
}
var isLoadCimiss = false;
function laodCimissData(loadUrl){
	$('#groundRealTimeDataShows_list_data_id_cimiss').datagrid({
    nowrap: false,  //一行显示数据，提高加载性能true
    striped: true,  //隔行换色
    collapsible:false,//是否可折叠的 
    fit: true,//height 100%
    height:"100",
     singleSelect:true,
    url:contentPath+loadUrl, 
    remoteSort:false, //要排序的数据从服务器定义 
	pageSize: 100,//每页显示的记录条数，默认为10 
    pageList: [100],//可以设置每页记录条数的列表 
    pagination:true,//分页控件
    rownumbers:true,
    onLoadSuccess:function(data)
	{
		if (isLoadCimiss)
		{
	    	 $(this).datagrid('selectRow',0);
		}
	},
    onSelect: function (item) {
	    	var row = $(this).datagrid('getSelected');
	    	var stationCode = row == null ? "" : row.zh;
	    	title_(row);
	    	 initTab("cimiss",stationCode);
     }
});  
}
 
function queryData()
{
	var currentTab = $('#tt').tabs('getSelected').attr("id"); 
	var stationTypes = "";
	var t = $("#time_id").val();
	var stationCode = $('#queryAreaId').combotree('getValues');
	$(".checkClass").each(function(){
		if($(this).prop('checked'))
		{
			if (stationTypes == "")
			{
				stationTypes+=$(this).val();
			}
			else
			{
				stationTypes+=","+$(this).val();
			}
			
		}
	});
	var  charturl = "";
	if ("cimiss" == currentTab)
	{
		laodCimissData("/groundRealTimeDataHander/getCimissData?time="+t+"&stationCode="+stationCode+"&stationTypes="+stationTypes);
		 $("#groundRealTimeDataShows_list_data_id_cimiss").datagrid('selectRow',0);
		 isLoadCimiss = true;
	}
	else
	{
		loadGroundData("/groundRealTimeDataHander/getGroundRealTimeData?time="+t+"&stationCode="+stationCode+"&stationTypes="+stationTypes);
		$("#groundRealTimeDataShows_list_data_id").datagrid('selectRow',0);
		var row = $("#groundRealTimeDataShows_list_data_id").datagrid('getSelected');
		title_(row); 
	}
}


var isInit = false;
$('#tt').tabs({ 
		border:false, 
		onSelect:function(currentTab){ 
			if (isInit)
			{
				var row;
				if ("cimiss" == currentTab)
				{
					 $("#groundRealTimeDataShows_list_data_id_cimiss").datagrid('selectRow',0);
					 row = $("#groundRealTimeDataShows_list_data_id_cimiss").datagrid('getSelected');
					title_(row);
				}
				else
				{
			    	 row = $("#groundRealTimeDataShows_list_data_id").datagrid('getSelected');
			    	 title_(row);
					
				}
		    	var stationCode = row == null ? "" : row.zh;
		    	 initTab(currentTab,stationCode);
			}
			isInit = true;
		} 
}); 

function title_(row)
{
		var area = row == null ? "" : row.area;
		var stationName = row == null ? "" : row.zm;
		if (area != "" || stationName != "")
		{
			$('#rtitle').html(area+" > "+stationName);
		}
		else
		{
			$('#rtitle').html("");
		}
}

function initTab(currentTab,stationCode)
{
	 		 var stationTypes = "";
			var t = $("#time_id").val();
			$(".checkClass").each(function(){
				if($(this).prop('checked'))
				{
					if (stationTypes == "")
					{
						stationTypes+=$(this).val();
					}
					else
					{
						stationTypes+=","+$(this).val();
					}
					
				}
			});
			var  charturl = "";
			if ("cimiss" == currentTab)
			{
				charturl = contentPath+"/groundRealTimeDataHander/getGroundCimissChartData?time="+t+"&stationCode="+stationCode+"&stationTypes="+stationTypes;
			}
			else
			{
				charturl = contentPath+"/groundRealTimeDataHander/getGroundChartData?time="+t+"&stationCode="+stationCode+"&stationTypes="+stationTypes;
			}
			initChart(charturl); 
}

function initChart(url)
{
	Util.getAjaxData(url,"",function(data){
		var wd = data.data['wd'];
		var sl = data.data['sl'];
		var qy = data.data['qy'];
		var wd_time = data.data['wd_time'];
		var sl_time = data.data['sl_time'];
		var qy_time = data.data['qy_time'];
		 wdchart(wd,wd_time);
		slchart(sl,sl_time);
		qychart(qy,qy_time); 
	});
}


  Highcharts.setOptions({
            lang: {
               　			  printChart:"打印图表",
                  downloadJPEG: "下载JPEG 图片" , 
                  downloadPDF: "下载PDF文档"  ,
                  downloadPNG: "下载PNG 图片"  ,
                  downloadSVG: "下载SVG 矢量图" , 
                  exportButtonTitle: "导出图片" 
            }
        });

function wdchart(datas,time)
{
	$('#container1').highcharts({
        chart: {
            type: 'line'
        },
        title: {
            text: '二十四小时温度变化情况'
        },
       xAxis: {
            categories:eval(time),
            tickInterval:3 
        },
        yAxis: {
            title: {
                text: '温度(℃)'
            }
        },
        tooltip: {
            formatter: function() {
                    return '<b>'+ this.series.name +'</b>'+
                    ': '+ this.y +' ℃,时间:'+this.x;
            }
        },
        series: [{ name: '温度', data: eval(datas) }]
    });

}


function slchart(datas,time)
{
	$('#container2').highcharts({
        chart: {
            type: 'line'
        },
        title: {
            text: '二十四小时降水变化情况'
        },
       xAxis: {
            categories:eval(time),
            tickInterval:3 
        },
        yAxis: {
            title: {
                text: '降水量(mm)'
            }
        },
        tooltip: {
            formatter: function() {
                    return '<b>'+ this.series.name +'</b>'+
                    ' : '+ this.y +' mm,时间:'+this.x;
            }
        },
        
        series: [{
            name: '降水量',
            data: eval(datas)
        }]
    });
}

function qychart(datas,time)
{
	 $('#container3').highcharts({
        chart: {
            type: 'line'
        },
        title: {
            text: '二十四小时气压变化情况'
        },
        xAxis: {
            categories:eval(time),
            tickInterval:3 
        },
        yAxis: {
            title: {
                text: '气压(hpa)'
            }
        },
        tooltip: {
            formatter: function() {
                    return '<b>'+ this.series.name +'</b>'+
                   ': '+ this.y +' hpa,时间:'+this.x;
            }
        },
        
        series: [{
            name: '气压',
            data: eval(datas)
        }]
    });
}
	         			
function strUtil(s)
{
	 var lastIndex = s.lastIndexOf(',');
     if (lastIndex > -1) {
         s = s.substring(0, lastIndex) + s.substring(lastIndex + 1, s.length);
        return s;
     }	
}
</script>

