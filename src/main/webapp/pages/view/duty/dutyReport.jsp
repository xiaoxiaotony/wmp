<%@ page language="java" contentType="text/html; charset=UTF-8"
	%>
<div title="值班报表" data-options="closable:true" >
	       <div class="search_bar">
                    <div class="search_tit" style="width: 65px;">值班月份：</div>
                    <div class="search_txt"> 
                        <input style="width: 140px;" type="text" id="start_time"  class="Wdate" onclick="WdatePicker({dateFmt:'yyyy-MM'});" />
                    </div>
                    <!-- <div class="search_tit" style="width: 65px;">结束时间：</div>
                    <div class="search_txt"> 
                        <input style="width: 140px;" type="text" id="end_time"  class="Wdate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'});" />
                    </div> -->
                   
                    <div class="search_tit"><a href="#" class="button button01" onclick="queryDutyData();">查询</a>
                    </div>
                </div>
     <div id="count_id" style="top:15%; width: 48%;left:0%;position: absolute;height:71%;">
     	<table id="dutyCount_list_data1">
			<thead>
				<tr>
					<th field="duty_person" width="20%" sortable="true">值班人</th>
					<th field="count" width="20%" sortable="true">值班次数</th>
					<th field="type" formatter="format_type" width="20%">值班类型</th>
					<th field="duty_date" width="20%">值班月份</th>
					<th field="user_id" width="20%" data-options="align:'center'" formatter="queryDetails">操作</th>
				</tr>
			</thead>
		</table>
     </div>
	<div  id="container_duty" style="position: absolute;left:50%; width:50%;height:71%;right: 0; top: 15%;bottom: 0;">
	</div>
	<!--底部分页 end-->
</div>

<!-- 加载js -->

<script type="text/javascript" >
$(document).ready(function(){
	queryDutyData();
});
 
function queryDutyData()
{
	var beginDate = $("#start_time").val();
	var url = contentPath+"/dutyHander/getDutyReportData?dutyDate="+beginDate;
	initDutyChart(url);
	$('#dutyCount_list_data1').datagrid({
    nowrap: false,  //一行显示数据，提高加载性能true
    striped: true,  //隔行换色
    collapsible:false,//是否可折叠的 
    fit: true,//height 100%
    url:contentPath+"/dutyHander/queryDutyCount?dutyDate="+beginDate, 
    remoteSort:false, //要排序的数据从服务器定义 
	pageSize: 20,//每页显示的记录条数，默认为10 
    pageList: [10,20,30,50,100],//可以设置每页记录条数的列表 
    pagination:true,//分页控件 
    rownumbers:false
});
}



//设置分页控件 
var p = $('#dutyInfo_list_data1').datagrid('getPager');
$(p).pagination({ 
    beforePageText: '第',//页数文本框前显示的汉字 
    afterPageText: '页    共 {pages} 页', 
    displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
});

function format_type(val,row){
	return val == "0" ? "网络管理" : "气象通讯传输";
}
function format_status(val,row){
	return val == "0" ? "值班中" : "值班结束";
}



function queryDetails(val,row){
	return "<a href='javascript:void(0);' onclick='queryRecordDetails(\""+val+"\",\""+row.type+"\",\""+row.duty_date+"\")'>详情</a>";
}

function queryRecordDetails(id,type,dutyDate){
	var url = "view/duty/dutyRecordDetail.jsp?userId="+id+"&type="+type+"&dutyDate="+dutyDate;
	$('#dialog_content').dialog({
		title: '值班详情',
		width: 800,
		height: 400,
		closed: false,
		cache: false,
		href: url,
		modal: true
	});
}

function initDutyChart(url)
{
	Util.getAjaxData(url,"",function(data){
		var result = data.data['result'];
		var title = data.data['title'];
		 dutyChart(title,result);
	},false);
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

function dutyChart(title,datas)
{
	$('#container_duty').highcharts({
        chart: {
            type: 'column'
        },
        title: {
            text: '值班报表'
        },
        subtitle: {
            text: ''
        },
        xAxis: {categories:eval(title)},
        yAxis: {
            min: 0,
            title: {
                text: '次数'
            }
        },
        tooltip: {
            headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                '<td style="padding:0"><b>{point.y} 次</b></td></tr>',
            footerFormat: '</table>',
            shared: true,
            useHTML: true
        },
        plotOptions: {
            column: {
                pointPadding: 0.2,
                borderWidth: 0
            }
        },
         series: [{ name: '值班', data: eval(datas) }]
    });
}

</script>

