package com.txy.web.main.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.txy.common.orm.SqlBeanClient;
import com.txy.common.util.StringUtils;


@Service
public class TransferMonitorCountService {

	@Autowired
	private SqlBeanClient queryClient;
	
	public List<Map<String, Object>> queryDataTypeDay(String station_ids, String station_type,
			String d_datetime, String sort) {
		
		String st_types [] = station_type.split(",");
		
		StringBuffer dtypes = new StringBuffer();
		StringBuffer ctypes = new StringBuffer();
		
		for (String st_type : st_types) {
			String types [] =  st_type.split("_");
			String dtype = types[0];
			String ctype = types[1];
			if(types.length > 2){
				for (int i = 2; i < types.length; i++) {
					 ctype+= "_" + types[i];
				}
			}
			dtypes.append(",'"+dtype+"'");
			ctypes.append(",'"+ctype+"'");
		}
		dtypes.deleteCharAt(0);
		ctypes.deleteCharAt(0);
		String sql = "select * from( select a.iiiii,c.sname,b.ctype_desc,b.dtype_desc,a.dtype,a.ctype,trunc((case when ((a.shishou / a.yingshou) * 100) > 100 then 100 else (a.shishou / a.yingshou) * 100 end),0) as daobaolv, trunc((a.jishi/a.yingshou)*100,0) as jishilv, trunc((a.yuxian/a.yingshou)*100,0) as yuxianlv, trunc(((case when (a.yingshou-a.shishou) < 1 then 0 else a.yingshou-a.shishou end)/a.yingshou)*100,0) as queshoulv from wmp_tfmonitor_count_day a,tr_datatype_dic b,wmp_station c where a.dtype = b.dtype and a.ctype = b.ctype and a.iiiii = c.iiiii and a.iiiii in ("+station_ids+") and a.dtype in ("+dtypes+") and a.ctype in ("+ctypes+") and a.d_datetime = to_date('"+d_datetime+"','yyyy/mm/dd hh24:mi:ss') ) where 1=1";
		if(!StringUtils.isNullOrEmpty(sort)) sql = sql + " order by " + sort;
		return queryClient.createJdbcTemplate().queryForList(sql);
	}
	
	/**
	 * 图表个站的平均到报率
	 * @param d_datetime 
	 * @return
	 */
	public List<Map<String, Object>>  queryAvgDaoBaolv(String stations, String d_datetime,String station_type){
		String st_types [] = station_type.split(",");
		
		StringBuffer dtypes = new StringBuffer();
		StringBuffer ctypes = new StringBuffer();
		
		for (String st_type : st_types) {
			String types [] =  st_type.split("_");
			String dtype = types[0];
			String ctype = types[1];
			if(types.length > 2){
				for (int i = 2; i < types.length; i++) {
					 ctype+= "_" + types[i];
				}
			}
			dtypes.append(",'"+dtype+"'");
			ctypes.append(",'"+ctype+"'");
		}
		dtypes.deleteCharAt(0);
		ctypes.deleteCharAt(0);
		String sql = "select ((select distinct sname from wmp_station where iiiii = t.iiiii) || '(' || t.iiiii || ')') as station,trunc(avg((case when ((t.shishou / t.yingshou) * 100) > 100 then 100 else (t.shishou / t.yingshou) * 100 end)),0) as daobaolv from WMP_TFMONITOR_COUNT_DAY t where t.iiiii in ("+stations+") AND T.DTYPE IN ("+dtypes+") AND T.CTYPE IN ("+ctypes+") and t.d_datetime = to_date('"+d_datetime+"','yyyy/mm/dd hh24:mi:ss') group by t.iiiii,T.DTYPE,T.CTYPE";
		return queryClient.createJdbcTemplate().queryForList(sql);
	}
	
	
	/**
	 * 图表个站的平均到报率
	 * @param d_datetime 
	 * @return 
	 */
	public List<Map<String, Object>>  queryAvgDaoBaolv(String stations, String begin_time,String end_time,String station_type){
		String st_types [] = station_type.split(",");
		
		StringBuffer dtypes = new StringBuffer();
		StringBuffer ctypes = new StringBuffer();
		
		for (String st_type : st_types) {
			String types [] =  st_type.split("_");
			String dtype = types[0];
			String ctype = types[1];
			if(types.length > 2){
				for (int i = 2; i < types.length; i++) {
					 ctype+= "_" + types[i];
				}
			}
			dtypes.append(",'"+dtype+"'");
			ctypes.append(",'"+ctype+"'");
		}
		dtypes.deleteCharAt(0);
		ctypes.deleteCharAt(0);
		String sql = "select ((select distinct sname from wmp_station where iiiii = t.iiiii) || '(' || t.iiiii || ')') as station,trunc(avg((case when ((t.shishou / t.yingshou) * 100) > 100 then 100 else (t.shishou / t.yingshou) * 100 end)),0) as daobaolv from WMP_TFMONITOR_COUNT_DAY t where t.iiiii in ("+stations+") AND T.DTYPE IN ("+dtypes+") AND T.CTYPE IN ("+ctypes+") and t.d_datetime >= to_date('"+begin_time+"','yyyy/mm/dd hh24:mi:ss') and t.d_datetime < to_date('"+end_time+"','yyyy/mm/dd hh24:mi:ss') group by t.iiiii,T.DTYPE,T.CTYPE";
		return queryClient.createJdbcTemplate().queryForList(sql);
	}

	/**
	 * 得到年下拉列表
	 * @return
	 */
	public List<Map<String, Object>> queryYear() {
		String sql  = "select distinct to_char(t.d_datetime,'yyyy') as data_year from WMP_TFMONITOR_COUNT_DAY t group by t.d_datetime";
		return queryClient.createJdbcTemplate().queryForList(sql);
	}

	/**
	 * 根据时间段统计报文
	 * @param string
	 * @param types
	 * @param begin_time
	 * @param end_time
	 * @param sort 
	 * @return
	 */
	public List<Map<String, Object>> queryDataTypeTimes(String station_ids,
			String station_type, String begin_time, String end_time, String sort) {
		String st_types [] = station_type.split(",");
		
		StringBuffer dtypes = new StringBuffer();
		StringBuffer ctypes = new StringBuffer();
		
		for (String st_type : st_types) {
			String types [] =  st_type.split("_");
			String dtype = types[0];
			String ctype = types[1];
			if(types.length > 2){
				for (int i = 2; i < types.length; i++) {
					 ctype+= "_" + types[i];
				}
			}
			dtypes.append(",'"+dtype+"'");
			ctypes.append(",'"+ctype+"'");
		}
		dtypes.deleteCharAt(0);
		ctypes.deleteCharAt(0);
		
		String sql = "select * from( select t.iiiii, t.sname, t.ctype_desc,t.dtype,t.ctype,trunc((case when ((t.shishou / t.yingshou) * 100) > 100 then 100 else (t.shishou / t.yingshou) * 100 end), 0) as daobaolv, trunc((t.jishi / t.yingshou) * 100, 0) as jishilv, trunc((t.yuxian / t.yingshou) * 100, 0) as yuxianlv, trunc(((case when (t.yingshou - t.shishou) < 1 then 0 else t.yingshou - t.shishou end) / t.yingshou) * 100, 0) as queshoulv from (select a.iiiii, c.sname, b.ctype_desc,b.dtype,b.ctype,sum(a.yingshou) as yingshou, sum(a.shishou) as shishou, sum(a.jishi) as jishi, sum(a.yuxian) as yuxian from wmp_tfmonitor_count_day a, tr_datatype_dic b, wmp_station c where a.dtype = b.dtype and a.ctype = b.ctype and a.iiiii = c.iiiii and a.iiiii in ("+station_ids+") and a.dtype in ("+dtypes+") and a.ctype in ("+ctypes+") and a.d_datetime >= to_date('"+begin_time+"', 'yyyy/mm/dd hh24:mi:ss') and a.d_datetime < to_date('"+end_time+"', 'yyyy/mm/dd hh24:mi:ss') group by a.iiiii, c.sname, b.ctype_desc,b.dtype,b.ctype) t ) where 1=1 ";
		if(!StringUtils.isNullOrEmpty(sort)) sql = sql + " order by " + sort;
		return queryClient.createJdbcTemplate().queryForList(sql);
	}

    /**
     * 根据用户名关联地区
     * @param userID
     * @return
     */
	public List<Map<String, Object>> queryStationDICByUser(String areas){
		String sql = "SELECT t.muncpl_id AS \"id\" ,t.muncpl AS \"text\" FROM WMP_STATION_DIC t  where t.MUNCPL_ID in (" + areas + ")";
		List<Map<String, Object>> dic = queryClient.createJdbcTemplate().queryForList(sql);
		List<Map<String, Object>> data = new ArrayList<>();
		for (Map<String, Object> map : dic) {
			String dic_id = map.get("id").toString();
			List<Map<String, Object>> ct = queryClient.createJdbcTemplate().queryForList("SELECT t.id AS \"id\" ,t.cname AS \"text\"  FROM WMP_STATION_CT t WHERE t.muncpl_id = "+dic_id);
			map.put("children", ct);
			map.put("state", "closed");
			data.add(map);
		}
		return data;
	}

	/**
	 * 
	 * @param dic_id
	 * @return
	 */
	public List<Map<String, Object>> queryDataTypeByDic(String dic_id) {
		String sql = "SELECT c.dtype || '_' || c.ctype as datatype, c.ctype_desc, c.dtype_desc FROM wmp_station a, tr_stationrcv_dic b, tr_datatype_dic c WHERE a.iiiii = b.iiiii AND b.ctype = c.ctype AND b.dtype = c.dtype AND b.mflag = 0 AND c.flage = 0 AND a.ct_id in ("+dic_id+") GROUP BY c.dtype, c.ctype, c.ctype_desc, c.dtype_desc";
		return queryClient.createJdbcTemplate().queryForList(sql);
	}

	/**
	 * 
	 * @param station_type
	 * @return
	 */
	public List<Map<String, Object>> queryStationAndName(String station_type,String dic_id) {
		
		String st_types [] = station_type.split(",");
		
		StringBuffer dtypes = new StringBuffer();
		StringBuffer ctypes = new StringBuffer();
		
		for (String st_type : st_types) {
			String types [] =  st_type.split("_");
			String dtype = types[0];
			String ctype = types[1];
			if(types.length > 2){
				for (int i = 2; i < types.length; i++) {
					 ctype+= "_" + types[i];
				}
			}
			dtypes.append(",'"+dtype+"'");
			ctypes.append(",'"+ctype+"'");
		}
		dtypes.deleteCharAt(0);
		ctypes.deleteCharAt(0);
		
		String sql = "SELECT distinct A.IIIII, A.IIIII || '(' || A.SNAME || ')' AS STNAME FROM WMP_STATION A, TR_STATIONRCV_DIC B, TR_DATATYPE_DIC C WHERE A.IIIII = B.IIIII AND B.CTYPE = C.CTYPE AND B.DTYPE = C.DTYPE AND B.MFLAG = 0 AND C.FLAGE = 0 AND C.CTYPE in ("+ctypes+") AND C.DTYPE in ("+dtypes+")  AND A.CT_ID IN ("+dic_id+")";
		return queryClient.createJdbcTemplate().queryForList(sql);
	}
	
	/**
	 * 根据站点、时间段、时间类型、资料类型，查询站点统计类型
	 * @return 
	 * 
	 */
	public Map<String, Object> queryStationInfoCount(String station,String beginTime,String endTime,String dateType,String dtype,String ctype){
		String sql = "";
		if("1".equals(dateType)){ //日统计
			sql = "SELECT t.yingshou,t.shishou,(t.yingshou-t.shishou) AS queshou FROM WMP_TFMONITOR_COUNT_DAY T WHERE T.IIIII = '"+station+"' AND T.D_DATETIME = TO_DATE('"+beginTime+"', 'yyyy/mm/dd hh24:mi:ss') AND T.DTYPE = '"+dtype+"' AND T.CTYPE = '"+ctype+"'";
		}else{//时间段统计
			sql = "SELECT sum(t.yingshou) as yingshou,sum(t.shishou) as shishou,(sum(t.yingshou)-sum(t.shishou)) AS queshou FROM WMP_TFMONITOR_COUNT_DAY T WHERE T.IIIII = '"+station+"' AND T.D_DATETIME >= TO_DATE('"+beginTime+"', 'yyyy/mm/dd hh24:mi:ss') AND T.D_DATETIME < TO_DATE('"+endTime+"', 'yyyy/mm/dd hh24:mi:ss') AND T.DTYPE = '"+dtype+"' AND T.CTYPE = '"+ctype+"' GROUP BY T.IIIII";
		}
		List<Map<String, Object>> data = queryClient.createJdbcTemplate().queryForList(sql);
		if(data.size() != 0) return data.get(0);
		return null;
	}
	
	/**
	 * 查询缺失详情
	 * @param station
	 * @param beginTime
	 * @param endTime
	 * @param dateType
	 * @param dtype
	 * @param ctype
	 * @return 
	 */
	public Map<String, Object> queryStationQueInfo(String station,String beginTime,String endTime,String dateType,String dtype,String ctype){
try { 
		Map<String, Object> map_con = queryStationInfoCount(station, beginTime, endTime, dateType, dtype, ctype);
		Object queshou = map_con.get("QUESHOU");
		if(queshou == null || "0".equals(queshou.toString())){
			map_con.put("list_que", null);
			return map_con;
		}
		
		//查询导报时间标志
	    String sql = "SELECT DISTINCT A.DTYPE, A.CTYPE, A.MASK_STR, A.SCHDNUM, A.DFLAG, A.MRANGE, A.MFLAG, A.MTIME, B.FLAGE, B.OUTTIME, B.MISSPG FROM TR_STATIONRCV_DIC A,TR_DATATYPE_DIC B WHERE A.DTYPE = B.DTYPE AND A.CTYPE = B.CTYPE AND A.INUSE = 1 AND A.CTYPE = '"+ctype+"' AND A.DTYPE = '"+dtype+"'  AND B.FLAGE = 0 AND A.IIIII = '"+station+"'";
	    Map<String, Object> map = queryClient.createJdbcTemplate().queryForMap(sql);
	    
	    //查询时间段
	    List<Map<String, Object>> list_date = null;
	    if(!"1".equals(dateType)){
	    	list_date = queryClient.createJdbcTemplate().queryForList("SELECT t.d_datetime FROM WMP_TFMONITOR_COUNT_DAY T WHERE T.IIIII = '"+station+"' AND T.D_DATETIME >= TO_DATE('"+beginTime+"', 'yyyy/mm/dd hh24:mi:ss') AND T.D_DATETIME < TO_DATE('"+endTime+"', 'yyyy/mm/dd hh24:mi:ss') AND T.DTYPE = '"+dtype+"' AND T.CTYPE = '"+ctype+"' ORDER BY t.d_datetime ASC");
	    }
        
	    //查询实际到报begin
	    String sql_dao = "";
	    SimpleDateFormat sft = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	    Calendar cl = Calendar.getInstance();
	    cl.setTime(sft.parse(beginTime));
	    String beginTime_temp = "";
	    String endTime_temp = "";
	    List<Map<String, Object>> list_dao = new ArrayList<>();
	    List<Map<String, Object>> list_dao_temp = null;
	    if("1".equals(dateType)){ //日查询
	    	sql_dao = "SELECT * FROM TR_REPO_RCV T WHERE T.IIIII = '"+station+"' AND T.CTYPE = '"+ctype+"' AND T.DTYPE = '"+dtype+"' AND T.D_DATETIME >= TO_DATE('"+beginTime+"', 'yyyy/mm/dd hh24:mi:ss') AND T.D_DATETIME < TO_DATE('"+endTime+"', 'yyyy/mm/dd hh24:mi:ss') ORDER BY T.D_DATETIME ASC";
	    	list_dao = queryClient.createJdbcTemplate().queryForList(sql_dao);
	    }else if(!"1".equals(dateType)){
	    	for (Map<String, Object> map_date : list_date) {
	    	    beginTime_temp = sft.format(map_date.get("D_DATETIME"));
	    	    cl.setTime(sft.parse(beginTime_temp));
	    	    cl.add(Calendar.DAY_OF_MONTH, 1);
	    	    endTime_temp = sft.format(cl.getTime());
	    	    sql_dao = "SELECT * FROM TR_REPO_RCV T WHERE T.IIIII = '"+station+"' AND T.CTYPE = '"+ctype+"' AND T.DTYPE = '"+dtype+"' AND T.D_DATETIME >= TO_DATE('"+beginTime_temp+"', 'yyyy/mm/dd hh24:mi:ss') AND T.D_DATETIME < TO_DATE('"+endTime_temp+"', 'yyyy/mm/dd hh24:mi:ss') ORDER BY T.D_DATETIME ASC";
	    	    list_dao_temp = queryClient.createJdbcTemplate().queryForList(sql_dao);
	    	    for (Map<String, Object> map2 : list_dao_temp) {
	    	    	list_dao.add(map2);
			    }
			}
	    }
	    //查询实际到报end
	    
	    
	    
        String dflag [] = map.get("DFLAG").toString().split(","); //是不是没天都有
        String mflag  = map.get("MFLAG").toString(); //是不是月报
        String mrange [] = map.get("MRANGE").toString().split(","); //是不是每月接收
        Object mtime = map.get("MTIME"); //接收月的最晚日期
        String mask_str = map.get("MASK_STR").toString(); //是不是没个时次都接收
        
        beginTime = beginTime.replace("/", "-");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sf.parse(beginTime));

        int year = calendar.get(Calendar.YEAR);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int mon = calendar.get(Calendar.MONTH)+1;
        String day_str = (day+"").length() > 1 ? day+"" : "0"+day;
        String mon_str = (mon+"").length() > 1 ? mon + "" : "0"+mon;
        String date = year + "-" + mon_str + "-" + day_str;
        
        List<String> mask_list = new ArrayList<>();
        
        if("1".equals(mflag)){ //判断是否是月报
        	
        	String mon_d = dflag[0].equals("0") ? mtime.toString() : dflag[0];
        	String datetime = year + "-" + mon_str + "-" + (mon_d.length() > 1 ? mon_d : "0"+mon_d);
        	
        	if(mrange.length == 1){//判断是不是单月或者每月接收
        		if("0".equals(mrange[1])){ //判断是不是每月接收
        			for(int i = 0,len = mask_str.length(); i < len ; i++){
        				String hour = mask_str.substring(i,i+1);
        				if("1".equals(hour)) mask_list.add(datetime + " " + ((i+"").length() > 1 ? i : "0"+i) +":00:00");
        			}
        		}else{
        		    if(mon == Integer.parseInt(mrange[1])){ //是不是在当月
            			for(int i = 0,len = mask_str.length(); i < len ; i++){
            				String hour = mask_str.substring(i,i+1);
            				if("1".equals(hour)) mask_list.add(datetime + " " + ((i+"").length() > 1 ? i : "0"+i) +":00:00");
            			}
        		    }
        		}
        	}else { //判断是为多月
        	    for (String string : mrange) {
        		    if(mon == Integer.parseInt(string)){ //是不是在当月
            			for(int i = 0,len = mask_str.length(); i < len ; i++){
            				String hour = mask_str.substring(i,i+1);
            				if("1".equals(hour)) mask_list.add(datetime + " " + ((i+"").length() > 1 ? i : "0"+i) +":00:00");
            			}
        		    }
        	    }
        	}
        }//判断是否是月报 end
        
        //判断是不是日报
        String hour= "";
        if(dflag.length == 1){ //判断是否是每天还是单天
        	
        	if("0".equals(dflag[0])){ //判断是都是每天都发
        		for(int i = 0,len = mask_str.length(); i < len ; i++){
        			hour = mask_str.substring(i,i+1);
        			if("1".equals(hour)) mask_list.add(date + " " + ((i+"").length() > 1 ? i : "0"+i) +":00:00");
        		}

        	}else if(dflag[0].equals(day)){ //判断是不是当天
        		for(int i = 0,len = mask_str.length(); i < len ; i++){
        			hour = mask_str.substring(i,i+1);
        			if("1".equals(hour)) mask_list.add(date + " " + ((i+"").length() > 1 ? i : "0"+i) +":00:00");
        		}
        	}


        }else {   //多天
        	for(String day_d : dflag){
        		if(Integer.parseInt(day_d) == day){ //判断是不是当天
        			for(int i = 0,len = mask_str.length(); i < len ; i ++){
        				hour = mask_str.substring(i,i+1);
        				if("1".equals(hour)) mask_list.add(date + " " + ((i+"").length() > 1 ? i : "0"+i) +":00:00");
        			}
        		}
        	}

        }//判断是否是日报 end
        
        int mask_len = mask_list.size();
        int dao_len = list_dao.size();
        
        List<Map<String,Object>> data = new ArrayList<>();
        String datetime_d = "";
        
        if("1".equals(dateType)){ //日统计
	        for(int i = 0 ; i < mask_len; i ++){
	        	Map<String,Object> map_data = new HashMap<>();
	        	String datetime = mask_list.get(i);
	        	boolean bln = true;
	            	 for(int j = 0 ; j < dao_len; j++){
	            		 datetime_d = list_dao.get(j).get("D_DATETIME").toString();
	            		 datetime_d = datetime_d.substring(0,datetime_d.length()-2);
	            		 if(datetime.equals(datetime_d)){ bln = false;break;}
	            	 }
	            if(bln){
	            	map_data.put("datetime", datetime);
	            	data.add(map_data);
	            }
	        }
        }else if(!"1".equals(dateType)){ //旬统计
        	for(int g = 0; g < list_date.size(); g ++){
	        	Map<String,Object> map_data = new HashMap<>();
	        	String datetime = mask_list.get(g);
	        	boolean bln = true;
	            	 for(int k = 0 ; k < dao_len; k++){
	            		 datetime_d = list_dao.get(k).get("D_DATETIME").toString();
	            		 datetime_d = datetime_d.substring(0,datetime_d.length()-2);
	            		 if(datetime.equals(datetime_d)){ bln = false;break;}
	            	 }
	            if(bln){
	            	map_data.put("datetime", datetime);
	            	data.add(map_data);
	            }
        	}
        }
        map_con.put("data_que", data);
        return map_con;
        
} catch (ParseException e) {
	e.printStackTrace();
	return null;
}
	}

}
