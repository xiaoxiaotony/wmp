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
					if(data.DES_SY <= 0){  //报文收发正常
						table_data = "<div class='realtime-data'><span>应收："+data.YINGSHOU+"</span><span class='color-green'>实收："+data.SHISHOU_N+"</span><span class='color-red'>缺收：0</span><span class='color-yellow'>逾限：0</span></div>";
						table_realdata = "<div class='realtime-table'><p>当前时间资料收发正常</p></div>";
					}else{ //有缺失的
						if(data.ISFLAG == 4) table_title = "<div class='realtime-title' id='"+data.DTYPE+"_"+data.CTYPE+"_title'>"+data.CTYPE_DESC+"<i class='icon-prompt easyui-panel' title='报文缺失过大,超过"+data.DF_P+"%！'>!</i></div>";
						table_data = "<div class='realtime-data'><span id='"+data.DTYPE+"_"+data.CTYPE+"_ys'>应收："+data.YINGSHOU+"</span><span class='color-green' id='"+data.DTYPE+"_"+data.CTYPE+"_ss'>实收："+data.SHISHOU_N+"</span><span class='color-red' id='"+data.DTYPE+"_"+data.CTYPE+"_qs' >缺收："+data.DES_SY+"</span><span class='color-yellow' id = '"+data.DTYPE+"_"+data.CTYPE+"_yx' >逾限：0</span></div>";
						table_realdata = "<div class='realtime-table' style='overflow:auto'><table width='100%' id='"+data.DTYPE+"_"+data.CTYPE+"' ><thead><tr><th field='IIIII' width='20%' data-options='align:\"center\"' formatter='format_iiiii'>站号</th><th field='SNAME' width='30%' data-options='align:\"center\"'>站名</th><th field='DIC_NAME' width='25%' data-options='align:\"center\"'>地区</th><th field='FLAG' width='25%' data-options='align:\"center\"' formatter='format_flag'>状态</th></tr></thead></table></div>";
						table_ids.push(data.DTYPE+"_"+data.CTYPE); //添加表格id
					}
				}else{ //还没到收报时间
					flag = 1;
					table_data = "<div class='realtime-data'><span>应收：--</span><span class='color-green'>实收：--</span><span class='color-red'>缺收：--</span><span class='color-yellow'>逾限：--</span></div>";
					table_realdata = "<div class='realtime-table'><b>还没到收报时间</b></div>";
				}
				//组合标签结束
				var table_html_end = "</li></div>";

				//插入磁铁
				var html_all = table_html_begin+table_title+table_data+table_realdata+table_html_end;
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
		$.get(contentPath+"/transferDatumMonitorHander/getQYDate", {station_dic: station_dic,station_type: table_id,datetime: datetime},function(data){
			$("#"+table_id).datagrid({
				striped: true,  //隔行换色
				fit: true, //height 100%
                data:data
			});
		});
		/**
		 * 初始化表格数据end
		 */
		//逾限的条目
		$.get(contentPath+"/transferDatumMonitorHander/getALLCon", {station_dic: station_dic,station_type: table_id,datetime: datetime},function(data){
            if(init_table_falg = 1){
				$("#"+table_id+"_ys").html("应收："+data.data.YINGSHOU);
				$("#"+table_id+"_ss").html("实收："+data.data.SHISHOU_N);
				$("#"+table_id+"_qs").html("缺失："+data.data.DES_SY);
            }
			$("#"+table_id+"_yx").html("逾限："+data.data.yx_con);
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