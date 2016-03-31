<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<div class="search_bar">

	<div class="search_txt" style="margin-left: 10px;">
		年：
		<input  id='transferMCYear_year'
			style='width:80px;height:32px;' />
	</div>

		
	<div class="search_tit">区域：</div>
	<div class="search_txt">
		<input name='station_dic' id='transferMCYear_stationdic'
			style='width:115px;height:32px;' />
	</div>
	
	
	<div class="search_tit">资料选择：</div>
	<div class="search_txt">
		<input name='station' id='transferMCYear_datatype'
			style='width:160px;height:32px;' />
	</div>

	<div class="search_tit">站点：</div>
	<div class="search_txt">
		<input name='station' id='transferMCYear_station'
			style='width:115px;height:32px;' />
	</div>


	<div class="search_tit">
		<a href="#" class="button button01" onclick="transferMCYear_count();">统计</a>
	</div>
	<div class="clr"></div>
</div>
<div class="cont_tit">
	<b>资料统计列表：</b>
	<div class="cont_button">
		<a href="#" class="button" onclick="to_exel();"> <i class="icon-pencil"></i> 下载
		</a>
	</div>
	<div class="clr"></div>
</div>

<!--table-->
<div style="width: 100%;height: 40%;">
	<table class="table05" width="100%" id = "transferMCYear_datagrid">
		<thead>
			<tr>
				<th data-options="field:'IIIII',width:'10%'" sortable="true">站号</th>
				<th data-options="field:'SNAME',width:'10%'">站名</th>
				<th data-options="field:'CTYPE_DESC',width:'20%'">资料子类型</th>
				<th data-options="field:'DAOBAOLV',width:'15%',formatter:daobaolv,sortable:true">到报</th>
				<th data-options="field:'JISHILV',width:'20%',formatter:jishilv,sortable:true">及时</th>
				<th data-options="field:'QUESHOULV',width:'20%',formatter:queshilv,sortable:true">缺失</th>
				<th data-options="field:'id',width:'5%',formatter:que_info">操作</th>
			</tr>
		</thead>
	</table>
	<!--table end-->
</div>

<div class="mar-t20" id="transferMCYear_charts" style="height:50%;border:1px solid #ccc;padding:10px;"></div>

<script type="text/javascript" src="../resources/js/plugins/echarts/echarts.js"></script>
<script>
var dic_name;
   $(function(){
		/**
		 * from 表单----------------------------------
		 */
		//设置默认时间
		var nowDate = new Date();
		var year = nowDate.getFullYear()-1;
		var mon = nowDate.getMonth();
		if(mon == 0) mon += 1;
		
		//加载年
		$("#transferMCYear_year").combobox({
			url:contentPath+"/transferMonitorCountHandler/getYear",
			onLoadSuccess:function(data){//全选
				$(this).combobox('setValue', year);
			},
			panelHeight:'auto',
			multiple:false,
			valueField:'DATA_YEAR',
			textField:'DATA_YEAR'
		});
		
		//选择区域
		transferMCYear_stationDic();
   });
   
 //查看详情
   function que_info(val,row){
      	return "<a herf='#' style='cursor:pointer;' onclick='alert_info(\""+row.IIIII+"\",\""+row.DTYPE+"\",\""+row.CTYPE+"\",\""+row.CTYPE_DESC+"\",\""+row.DTYPE_DESC+"\",\""+row.SNAME+"\");'>详情</a>";
   }
   //查看详情
   function alert_info(station,dtype,ctype,ctypename,dtypename,sname){
	   var year = $("#transferMCYear_year").combobox('getValue')+"";
       var datetime = year+"/01/01 00:00:00";
   	$("#dialog_content").dialog({
   		title: '详情',
   		width: 700,
   		height: 650,
   		closed: false,
   		cache: false,
   		href: "view/transferMonitor/monitorCount/transferMonitorCountAlert.jsp?datetime="+datetime+"&station="+station+"&dtype="+dtype+"&ctype="+ctype+"&ctypename="+ctypename+"&dtypename="+dtypename+"&sname="+sname+"&dateType="+4,
   		modal: true
   	});
   }
   
//到报率
function daobaolv(val,row){
    if(val){
       return  "<span class='rate-main color-darkgreen'><div><div style='width:"+val+"%'></div></div><i>"+val+"%</i></span>";
    }
    return  "<span class='rate-main color-darkgreen'><div><div style='width:"+0+"%'></div></div><i>"+0+"%</i></span>";
}
//及时率
function jishilv(val,row){
    if(val){
       return  "<span class='rate-main color-green'><div><div style='width:"+val+"%'></div></div><i>"+val+"%</i></span>";
    }
    return  "<span class='rate-main color-green'><div><div style='width:"+0+"%'></div></div><i>"+0+"%</i></span>";
}
//缺失
function queshilv(val,row){
	if(val){
       return  "<span class='rate-main color-red'><div><div style='width:"+val+"%'></div></div><i>"+val+"%</i></span>";
    }
    return  "<span class='rate-main color-red'><div><div style='width:"+0+"%'></div></div><i>"+0+"%</i></span>";
}
//缺失
function yuxianlv(val,row){
	if(val){
       return  "<span class='rate-main color-yellow'><div><div style='width:"+val+"%'></div></div><i>"+val+"%</i></span>";
    }
    return  "<span class='rate-main color-yellow'><div><div style='width:"+0+"%'></div></div><i>"+0+"%</i></span>";
}


//区域选择
function transferMCYear_stationDic(){
	//加载区域选择
	$("#transferMCYear_stationdic").combotree({
		    //获取数据URL  
		    url: contentPath+"/transferMonitorCountHandler/getArae",
		    multiple: true,
		    //选择树节点触发事件  
		    onCheck : function() {
		    	dic_name = $("#transferMCYear_stationdic").combotree('getValues');
		    	transferMCYear_datatype(dic_name);
		    },
		    onLoadSuccess:function(a,data){
		    	$("#transferMCYear_stationdic").combotree('setValue',data[0].id);
		    	dic_name = $("#transferMCYear_stationdic").combotree('getValues');
		    	transferMCYear_datatype(dic_name);
		    }
	 });
}

//站点选择
function transferMCYear_stationName(station_type){
//	$("#transferMCYear_station").combobox("clear");
	$("#transferMCYear_station").combobox({
		url:contentPath+"/transferMonitorCountHandler/getStationAndName?station_type="+station_type+"&dic_id="+dic_name,
		onLoadSuccess:function(data){//全选
//				if(data.length > 3){
//					$(this).combobox('setValues', [data[0].IIIII,data[1].IIIII,data[2].IIIII,data[3].IIIII]);
//				}else{
				var ids = new Array();
				$.each(data, function(i, msg){
					ids.push(msg.IIIII);
				});
				$(this).combobox('setValues', ids);
//				}
		    transferMCYear_count();
		},
		multiple:true,
		valueField:'IIIII',
		textField:'STNAME'
	});
 }
 
 //资料信息
 function transferMCYear_datatype(dic_id){
//		$("#transferMCYear_datatype").combobox("clear");
		$("#transferMCYear_datatype").combobox({
			url:contentPath+"/transferMonitorCountHandler/getDataType?dic_id="+dic_id,
			onLoadSuccess:function(data){//全选
//					var ids = new Array();
//					$.each(data, function(i, msg){
//						ids.push(msg.DATATYPE);
//					});
//					$(this).combobox('setValues', ids);
					$(this).combobox('setValue', data[0].DATATYPE);
					var station_type = $('#transferMCYear_datatype').combobox('getValues');
					transferMCYear_stationName(station_type);
			},
			onSelect: function(rec){
				var station_type = $('#transferMCYear_datatype').combobox('getValues');
				transferMCYear_stationName(station_type);
	        },
	        onUnselect:function(rec){
				var station_type = $('#transferMCYear_datatype').combobox('getValues');
				transferMCYear_stationName(station_type);
	        },
//			panelHeight:'auto',
			multiple:true,
			valueField:'DATATYPE',
			textField:'CTYPE_DESC'
		});
}

/**
*
*统计
**/
function transferMCYear_count(){
    var year = $("#transferMCYear_year").combobox('getValue')+"";
    var station_ids = $("#transferMCYear_station").combobox('getValues')+"";
    var types = $("#transferMCYear_datatype").combobox('getValues')+"";
    //加载表格
    initMDay_table(station_ids,types,year);
    //加载图表
    $.get(contentPath+"/transferMonitorCountHandler/getAvgDaoBaolvTimesYear", {station_ids: station_ids,year:year,types:types},function(data){
		 var data_st = new Array();
		 var data_val = new Array();
		 $.each(data, function(i, msg){
  				data_st.push(msg.STATION);
  				data_val.push(msg.DAOBAOLV);
		 });
		 initDaycharts(data_st,data_val);
	});
}
 
/**
* 初始化表格数据
*/
function initMDay_table(station_ids,types,year){
	$.get(contentPath+"/transferMonitorCountHandler/getDataTypeTimesYear", {station_ids: station_ids,types: types,year:year},function(data){
		$("#transferMCYear_datagrid").datagrid({
			striped: true,  //隔行换色
			fit: true, //height 100%
            remoteSort:true,
            multiSort:false,
            onSortColumn:function(sort,order){
                initMDay_table_sort(station_ids,types,year,sort+" "+order);
            },
            data:data
		});
	});
}
/**
* 表格数据排序
*/
function initMDay_table_sort(tation_ids,types,year,mon,day,sort){
	$.get(contentPath+"/transferMonitorCountHandler/getDataTypeTimesYear", {station_ids: station_ids,types: types,year:year,mon:mon,day:day,sort:sort},function(data){
		$("#transferMCYear_datagrid").datagrid('loadData',data);
	});
}

/**
*
*加载图表
*/
require.config({
    paths: {
        echarts: '../resources/js/plugins/echarts'
    }
});
function initDaycharts(data_st,data_val){
	require(
	        [
	            'echarts',
	            'echarts/chart/bar',
	            'echarts/chart/line'
	        ],
	        function (ec) {
	            //--- 折柱 ---
	            var myChart = ec.init(document.getElementById("transferMCYear_charts"));
	            myChart.setOption({
	                title : {
        					text: '各站综合平均到报率',
    				},
	                tooltip : {
	                    trigger: 'axis'
	                },
	                legend: {
	                    data:['平均到报率']
	                },
	                toolbox: {
	                    show : true,
	                    feature : {
	                        mark : {show: true},
	                        dataView : {show: true, readOnly: false},
	                        magicType : {show: true, type: ['line', 'bar']},
	                        restore : {show: true},
	                        saveAsImage : {show: true}
	                    }
	                },
	                calculable : true,
	                xAxis : [
	                    {
	                        type : 'category',
	                        data : data_st
	                    }
	                ],
	                yAxis : [
	                    {
	                        type : 'value',
	                        splitArea : {show : true}
	                    }
	                ],
	                series : [
	                    {
	                        name:'平均到报率',
	                        type:'bar',
	                        itemStyle:{normal:{color:function(){
	                            return "#87CEFA";
	                        }}},
	                        data:data_val
	                    }
	                ]
	            });          
	});
}

//到处表格
function to_exel(){
		var data = $("#transferMCYear_datagrid").datagrid("getData");
		var json_data = data.rows; //得到表格数据
		var col_fields = $("#transferMCYear_datagrid").datagrid('getColumnFields'); //得到表头字段
		var col_name = ["站号","站名","资料类型","到报率","及时率","缺失率","逾限率"]; //得到表头名
	    var date = new Date();
	    //创建隐藏的表单，存储表格数据
	    var form = null;
		if (form == null) {
			form=$("<form>");//定义一个form表单
			form.attr("style","display:none");
			form.attr("target","");
			form.attr("id","countYear_from");
			form.attr("method","post");
			form.attr("action",contentPath+"/transferMonitorCountHandler/toExcel");
			$("body").append(form);//将表单放置在web中
	
            var input1 = $("<input>"); 

            input1.attr("type","hidden"); 

            input1.attr("name","excleData");

            input1.attr("value",JSON.stringify(json_data)); 
            
            var input2 = $("<input>");

            input2.attr("type","hidden"); 

            input2.attr("name","headerIndex");

            input2.attr("value",JSON.stringify(col_fields));
            
            var input3 = $("<input>"); 

            input3.attr("type","hidden"); 

            input3.attr("name","headText");

            input3.attr("value",JSON.stringify(col_name));
            
            var input4 = $("<input>");

            input4.attr("type","hidden");

            input4.attr("name","excleName");

            input4.attr("value",dic_name+"-资料到报年统计"+date.getFullYear()+"_"+(date.getMonth()+1)+ "_"+date.getDate());
             
		    form.append(input1);   //将查询参数控件提交到表单上
		    form.append(input2);
		    form.append(input3);
		    form.append(input4);

		}else{
			("#countYear_from").empty();
			
		    var input1 = $("<input>");

            input1.attr("type","hidden");

            input1.attr("name","excleData");

            input1.attr("value",JSON.stringify(json_data)); 
            
            var input2 = $("<input>");

            input2.attr("type","hidden"); 

            input2.attr("name","headerIndex");

            input2.attr("value",JSON.stringify(col_fields));
            
            var input3 = $("<input>"); 

            input3.attr("type","hidden"); 

            input3.attr("name","headText");

            input3.attr("value",JSON.stringify(col_name));
            
            var input4 = $("<input>");

            input4.attr("type","hidden"); 

            input4.attr("name","excleName");

            input4.attr("value",dic_name+"-资料到报年统计"+date.getFullYear()+"_"+(date.getMonth()+1)+ "_"+date.getDate());
            
		    form.append(input1);   //将查询参数控件提交到表单上
		    form.append(input2);
		    form.append(input3);
		    form.append(input4);
		}
		
       form.submit();   //表单提交
 
	}
</script>