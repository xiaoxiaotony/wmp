$(function(){

	/**
	 * from 表单----------------------------------
	 */
	//设置默认时间
	var nowDate = new Date();
	var defaultDate = nowDate.format('yyyy-MM-dd hh');
	$("#transfer_datetime").val(defaultDate);
	document.getElementById("now_datetime").innerHTML = nowDate.format('yyyy-MM-dd hh:mm:ss');
	//加载区域选择
	$('#transfer_stationdic').combobox({
		url:contentPath+"/commonHander/getStationDIC",
		onLoadSuccess:function(data){//全选
			var ids = new Array();
			$.each(data, function(i, msg){
				ids.push(msg.MUNCPL_ID);
			});
			$(this).combobox('setValues', ids);

			//获取表单数据
			var param = {};
			param.station_dic = $(this).combobox('getValues')+"";
			param.datetime = $('#transfer_datetime').val();
			init_transfer(param);
			
		},
		panelHeight:'auto',
		multiple:true,
		valueField:'MUNCPL_ID',
		textField:'MUNCPL',
		onSelect:function(){
			change_param();
		},
		onUnselect:function(){
			change_param();
		}
	});
	/**
	 * from 表单 end------------------------------------------
	 * 
	 */
	
});



/**
 * 
 * 获取磁铁数据查询----------------------------------------
 */
var init_table_falg = 0; //表格加载方式
var table_ids; //缺收逾限表格id
var setl;
function init_transfer(param){
	init_table_falg = 0;
	$("#transfer_tables_all").empty(); //清理磁铁
	$.ajax({
		type: "POST",
		url: contentPath+"/transferDatumMonitorHander/getSYData",
		data: param,
		success: function(msg){
			var table_html_null="";
			table_ids = new Array();
			$.each( msg, function(i, data){
//				console.log(data);
				var flag = 0;
				//组合标签开始
				var table_html_begin = "<li><div class='realtime-main'>";
				//标题部分
				var table_title = "<div class='realtime-title' id='"+data.DTYPE+"_"+data.CTYPE+"_title'>"+data.CTYPE_DESC+"</div>";
				//摘要部分
				var table_data;
				//表格部分
				var table_realdata;
				if(data.DES_SY !== undefined){ //判断当前时间是否来报
					flag = 0;
					if(data.ISFLAG == 4) table_title = "<div class='realtime-title' id='"+data.DTYPE+"_"+data.CTYPE+"_title'>"+data.CTYPE_DESC+"<i class='icon-prompt easyui-panel' title='报文缺失过大,超过"+data.DF_P+"%！'>!</i></div>";
					/**
					 * 数据格式处理
					 * 
					 */
					var yingshou_true = data.check_data.true_check.YINGSHOU;if(yingshou_true == undefined) yingshou_true = "0";
					var yingshou_false = data.check_data.false_check.YINGSHOU;if(yingshou_false == undefined) yingshou_false = "0";
					var shishou_true = data.check_data.true_check.SHISHOU_N;if(shishou_true == undefined) shishou_true ="0";
					var shishou_false = data.check_data.false_check.SHISHOU_N;if(shishou_false == undefined) shishou_false ="0";
					var queshou_true  = data.check_data.true_check.DES_SY;if(queshou_true == undefined) queshou_true = "0";
					var queshou_false = data.check_data.false_check.DES_SY;if(queshou_false == undefined) queshou_false = "0";
					
					table_data = "<div class='realtime-data'><div class='realtime-data-div1'><div  class='realtime-data-div2'> <div  class='realtime-data-div3'><a name='station_title'  title='考核站点'   style='color:#FF9C00;cursor:pointer;' id = '"+data.DTYPE+"_"+data.CTYPE+"_yx_true' onclick='alert_st_info(\""+data.DTYPE+"\",\""+data.CTYPE+"\",\""+0+"\",\"yuxian\");'> 0 </a></div><div  class='realtime-data-div4'><a name='station_title'  title='非考核站点' style='color:#FF9C00;cursor:pointer;' id = '"+data.DTYPE+"_"+data.CTYPE+"_yx_false' onclick='alert_st_info(\""+data.DTYPE+"\",\""+data.CTYPE+"\",\""+1+"\",\"yuxian\");'> 0 </a></div></div><div style='color:#FF9C00;' class='realtime-data-div5'> 阈限 </div></div><div class='realtime-data-div1'><div  class='realtime-data-div2'> <div  class='realtime-data-div3'> <a name='station_title'  title='考核站点'    style='color:#FF0000;cursor:pointer;' id='"+data.DTYPE+"_"+data.CTYPE+"_qs_true' onclick='alert_st_info(\""+data.DTYPE+"\",\""+data.CTYPE+"\",\""+0+"\",\"queshou\");'> "+queshou_true+" </a> </div><div  class='realtime-data-div4'> <a name='station_title'  title='非考核站点'  style='color:#FF0000;cursor:pointer;' id='"+data.DTYPE+"_"+data.CTYPE+"_qs_false' onclick='alert_st_info(\""+data.DTYPE+"\",\""+data.CTYPE+"\",\""+1+"\",\"queshou\");'> "+queshou_false+" </a> </div></div><div style='color:#FF0000;' class='realtime-data-div5'> 缺收 </div></div><div class='realtime-data-div1'><div class='realtime-data-div2'> <div class='realtime-data-div3' > <a name='station_title'  title='考核站点'   style='color:#00FF54;cursor:pointer;' id='"+data.DTYPE+"_"+data.CTYPE+"_ss_true' onclick='alert_st_info(\""+data.DTYPE+"\",\""+data.CTYPE+"\",\""+0+"\",\"shishou\");'> "+shishou_true+" </a> </div><div class='realtime-data-div4' > <a name='station_title'  title='非考核站点'   style='color:#00FF54;cursor:pointer;' id='"+data.DTYPE+"_"+data.CTYPE+"_ss_false' onclick='alert_st_info(\""+data.DTYPE+"\",\""+data.CTYPE+"\",\""+1+"\",\"shishou\");'> "+shishou_false+" </a> </div></div><div style='color:#00FF54;' class='realtime-data-div5'> 实收 </div></div> <div class='realtime-data-div1'><div class='realtime-data-div2'><div class='realtime-data-div3' > <a name='station_title'  title='考核站点'  style='cursor:pointer;' id='"+data.DTYPE+"_"+data.CTYPE+"_ys_true' onclick='alert_st_info(\""+data.DTYPE+"\",\""+data.CTYPE+"\",\""+0+"\",\"yingshou\");'> "+yingshou_true+" </a> </div><div class='realtime-data-div4' > <a name='station_title'  title='非考核站点'  style='cursor:pointer;' id='"+data.DTYPE+"_"+data.CTYPE+"_ys_false' onclick='alert_st_info(\""+data.DTYPE+"\",\""+data.CTYPE+"\",\""+1+"\",\"yingshou\");'> "+yingshou_false+" </a> </div></div><div class='realtime-data-div5'> 应收 </div></div></div>";
					table_ids.push(data.DTYPE+"_"+data.CTYPE); //添加表格id
				}else{ //还没到收报时间
					flag = 1;
					table_data = "<div class='realtime-data'><div class='realtime-data-div1'><div  class='realtime-data-div2'> <div  class='realtime-data-div3'><a name='station_title'  title='考核站点'   style='color:#FF9C00;cursor:pointer;'> -- </a></div><div  class='realtime-data-div4'><a name='station_title'  title='非考核站点' style='color:#FF9C00;cursor:pointer;'> -- </a></div></div><div style='color:#FF9C00;' class='realtime-data-div5'> 阈限 </div></div><div class='realtime-data-div1'><div  class='realtime-data-div2'> <div  class='realtime-data-div3'> <a name='station_title'  title='考核站点'    style='color:#FF0000;cursor:pointer;'> -- </a> </div><div  class='realtime-data-div4'> <a name='station_title'  title='非考核站点'  style='color:#FF0000;cursor:pointer;'> -- </a> </div></div><div style='color:#FF0000;' class='realtime-data-div5'> 缺收 </div></div><div class='realtime-data-div1'><div class='realtime-data-div2'> <div class='realtime-data-div3' > <a name='station_title'  title='考核站点'   style='color:#00FF54;cursor:pointer;'> -- </a> </div><div class='realtime-data-div4' > <a name='station_title'  title='非考核站点'   style='color:#00FF54;cursor:pointer;'> -- </a> </div></div><div style='color:#00FF54;' class='realtime-data-div5'> 实收 </div></div> <div class='realtime-data-div1'><div class='realtime-data-div2'><div class='realtime-data-div3' > <a name='station_title'  title='考核站点'  style='cursor:pointer;'> -- </a> </div><div class='realtime-data-div4' > <a name='station_title'  title='非考核站点'  style='cursor:pointer;'> -- </a> </div></div><div class='realtime-data-div5'> 应收 </div></div></div>";
				}
				//组合标签结束
				var table_html_end = "</li></div>";

				//插入磁铁
				var html_all = table_html_begin+table_title+table_data+table_html_end;
				if(flag == 0){
					$("#transfer_tables_all").append(html_all);
				}else{
					table_html_null+=html_all;
				}
				

			});

			//插入还没到收报时间
			$("#transfer_tables_all").append(table_html_null);
			//加载缺失逾限表格
			if(table_ids.length != 0) init_table();
			//感叹号提示
            $('.icon-prompt').tooltip({
                position: 'bottom',
                onShow: function(){
                    $(this).tooltip('tip').css({
                        backgroundColor: '#fff000',
                        borderColor: '#ff0000',
                        height: 'auto'
                    });
                }
            });
			clearInterval(setl);
			var flag = 0
			setl = setInterval(function(){
				if(flag == 0){
					$(".icon-prompt").css("background","#EAE9EA");
					flag = 1;
				}else{
					flag = 0;
					$(".icon-prompt").css("background","#FF0000");
				}
			},800);
			//感叹号提示end
			
			//站点考核和非考核提示
	        $("[name='station_title']").tooltip({
                position: 'bottom',
                onShow: function(){
                    $(this).tooltip('tip').css({
                        borderColor: '#999999',
                        height: 'auto'
                    });
                }
            });
	        //站点考核和非考核提示end
	        
		}
	});
}

/**
 * 加载缺失逾限表格
 */
function init_table(){
	var station_dic = $('#transfer_stationdic').combobox('getValues')+"";
	var datetime = $('#transfer_datetime').val();
	$.each(table_ids,function(i,table_id){
		/**
		 * 初始化表格数据
		 */
//		$.get(contentPath+"/transferDatumMonitorHander/getQYDate", {station_dic: station_dic,station_type: table_id,datetime: datetime},function(data){
//			$("#"+table_id).datagrid({
//				striped: true,  //隔行换色
//				fit: true, //height 100%
//                data:data
//			});
//		});
		/**
		 * 初始化表格数据end
		 */
		//逾限的条目
		$.get(contentPath+"/transferDatumMonitorHander/getALLCon", {station_dic: station_dic,station_type: table_id,datetime: datetime},function(data){
            if(init_table_falg = 1){
				$("#"+table_id+"_ys_false").html(data.data.map_false.YINGSHOU);
				$("#"+table_id+"_ss_false").html(data.data.map_false.SHISHOU_N);
				$("#"+table_id+"_qs_false").html(data.data.map_false.DES_SY);
				
				$("#"+table_id+"_ys_true").html(data.data.map_true.YINGSHOU);
				$("#"+table_id+"_ss_true").html(data.data.map_true.SHISHOU_N);
				$("#"+table_id+"_qs_true").html(data.data.map_true.DES_SY);
            }
			$("#"+table_id+"_yx_false").html(data.data.yx_con_false);
			$("#"+table_id+"_yx_true").html(data.data.yx_con_true);
		});
	});
}
/**
 * 
 * 获取磁铁数据查询 end----------------------------------------
 */


/**
 * 格式化站号
 */
function format_iiiii(val,data){
     return "<a href='javascript:void(0);' onclick='queryJD(\""+val+"\",\""+data.DTYPE+"\",\""+data.CTYPE+"\",\""+data.SNAME+"\")'>"+val+"</a>";
}
/**
 * 格式化状态
 */
function format_flag(val,data){
	if(val == 0) return "<span style='color:red;'>缺失</span>";
	if(val == 1) return "<span style='color:orange;'>逾限</span>";
	return val;
}


/**
 * 查看站点详情
 * 
 */
function alert_st_info(dtype,ctype,check_type,type){
	var station_dic = $('#transfer_stationdic').combobox('getValues')+"";
	var datetime = $('#transfer_datetime').val();
	$('#dialog_content').dialog({
		title: '详情',
		width: 700,
		height: 650,
		closed: false,
		cache: false,
		href: "view/transferMonitor/transferDatumConfig/transferDatumStations.jsp?station_dic="+station_dic+"&datetime="+datetime+"&dtype="+dtype+"&ctype="+ctype+"&check_type="+check_type+"&type="+type,
		modal: true
	});
	
}


/**
 * 查看站点关联节点
 */
function queryJD(station,dtype,ctype,station_name){
	var datetime = $("#transfer_datetime").val() +":00:00";
	var typename =  $("#"+dtype+"_"+ctype+"_title").html().split("<")[0];
//	alert(datatime + "  " +station_name + "  "+ typename);
	$.get(contentPath+"/transferDatumMonitorHander/getStationNode",{station:station},
			function(data){
                if(data.length > 0){
                	$('#dialog_content').dialog({
                		title: '节点流程',
                		width: 900,
                		height: 650,
                		closed: false,
                		cache: false,
                		href: "view/transferMonitor/transferDatumConfig/transferDatumStationLog.jsp?station="+station+"&dtype="+dtype+"&ctype="+ctype+"&datetime="+datetime+"&typename="+typename+"&station_name="+station_name,
                		modal: true
                	});
                }else{
                	$.messager.alert('消息','该站点没有关联节点！','warning');
                }
			}
	);
}

//重新加载磁铁页面
function reload_tables(flag){
	//重新调整时间选择
	if(flag == 1) {
		var now_date = new Date().format('yyyy-MM-dd hh');
		if(now_date != $("#transfer_datetime").val()) $("#transfer_datetime").val(now_date);
	}
	
	var param = {};
	param.station_dic = $('#transfer_stationdic').combobox('getValues')+"";
	param.datetime = $('#transfer_datetime').val();
	init_transfer(param);
}

/**
 * 时间变化、地区选择变化
 */
function change_param(){
	reload_tables();
}

/**
 * 时间加减
 */
function date_add(i){
	var date_time_now = $("#transfer_datetime").val()+":00:00";
	var now_date = new Date().format('yyyy-MM-dd hh');
	if(i == 1) {
		if(now_date == $("#transfer_datetime").val()) return;
	}
	if(i == 0){
		$("#transfer_datetime").val(now_date);
		reload_tables();
		return;
	}
	var dt = new Date(date_time_now.replace(/-/g, " / "));//要计算的时间
	var StateTimeofHour = dt.getHours();//获取分钟属性
	dt.setHours(StateTimeofHour+i); //设置分钟属性
	var date_time = dt.format('yyyy-MM-dd hh');
	$("#transfer_datetime").val(date_time);
	reload_tables();
}

/**
 * 页面动态时间
 */
var t = setTimeout(time,1000);//开始执行
var m_l = 1;
function time()
{
   var dt = new Date();
   var m=dt.getMinutes();
   var s = dt.getSeconds();
   if((s%10) == 0){init_table_falg = 1;init_table();} //十秒执行一次
   if((m%5) == 0 && m_l != m ){  //5分钟重新刷新页面
	   reload_tables(1);
	   m_l = m;
   } //每5分钟执行一次
   
   clearTimeout(t);//清除定时器
   document.getElementById("now_datetime").innerHTML =  dt.format('yyyy-MM-dd hh:mm:ss');
   t = setTimeout(time,1000); //设定定时器，循环执行            
}