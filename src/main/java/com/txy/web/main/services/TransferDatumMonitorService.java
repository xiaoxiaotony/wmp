package com.txy.web.main.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.txy.common.orm.SqlBeanClient;

@Service
public class TransferDatumMonitorService {

    @Autowired
    private SqlBeanClient queryClient;
    
    
    private static final Logger logger = Logger.getLogger(TransferDatumMonitorService.class);
    
    /**
     * 查询实收应收资料
     * 2015/12/8 6:00:00
     * @param station_dic 
     * @param ischeck 是否是考核站 0：是，1：不是,2 : 全部 
     * @return 
     */
    public List<Map<String, Object>> querySY(String beginTime,String endTime, String station_dic,int ischeck){
    	String sql = null;
    	if(ischeck == 2)  sql = "SELECT a.dtype,a.ctype,a.dtype_desc,a.ctype_desc,b.shishou,b.yingshou,       (CASE WHEN (b.yingshou - b.shishou) < 0 THEN b.yingshou ELSE b.shishou END) AS shishou_n,       (CASE  WHEN (B.YINGSHOU - B.SHISHOU) <= 0 THEN  0  ELSE  B.YINGSHOU - B.SHISHOU END) AS DES_SY          FROM TR_DATATYPE_DIC a LEFT JOIN( SELECT       t.dtype,t.ctype,      (SELECT dtype_desc FROM TR_DATATYPE_DIC WHERE dtype = t.dtype AND ctype = t.ctype ) AS d_name,      (SELECT ctype_desc FROM TR_DATATYPE_DIC WHERE dtype = t.dtype AND ctype = t.ctype ) AS c_name,      t.shishou,      (SELECT COUNT(1) FROM TR_STATIONRCV_DIC A,  (SELECT * FROM WMP_STATION T  WHERE T.MUNCPL_ID IN  ("+station_dic+")) B WHERE A.IIIII = B.IIIII  AND A.CTYPE = T.CTYPE AND A.DTYPE = T.DTYPE) AS yingshou          FROM( SELECT a.dtype,a.ctype,COUNT(1) AS shishou FROM TR_REPO_RCV a,(SELECT a.* FROM TR_STATIONRCV_DIC a,(SELECT * FROM wmp_station t WHERE t.muncpl_id IN ("+station_dic+")) b WHERE a.iiiii  = b.iiiii AND a.mflag = 0) b  WHERE    a.d_datetime >= to_date(?,'yyyy/mm/dd hh24:mi:ss') AND                                                 a.d_datetime <  to_date(?,'yyyy/mm/dd hh24:mi:ss') AND                                                 a.dtype = b.dtype AND a.ctype = b.ctype AND a.iiiii = b.iiiii AND                                                 (a.cccc IS NULL OR a.cccc = 'BELS') GROUP BY a.dtype,a.ctype) t) b ON a.dtype = b.dtype AND a.ctype = b.ctype WHERE a.flage = 0    UNION    SELECT a.dtype,a.ctype,a.dtype_desc,a.ctype_desc,b.shishou,b.yingshou,       (CASE WHEN (b.yingshou - b.shishou) < 0 THEN b.yingshou ELSE b.shishou END) AS shishou_n,       (CASE  WHEN (B.YINGSHOU - B.SHISHOU) <= 0 THEN  0  ELSE  B.YINGSHOU - B.SHISHOU END) AS DES_SY          FROM TR_DATATYPE_DIC a  INNER JOIN( SELECT       t.dtype,t.ctype,      (SELECT dtype_desc FROM TR_DATATYPE_DIC WHERE dtype = t.dtype AND ctype = t.ctype ) AS d_name,      (SELECT ctype_desc FROM TR_DATATYPE_DIC WHERE dtype = t.dtype AND ctype = t.ctype ) AS c_name,      t.shishou,      (SELECT COUNT(1) FROM TR_STATIONRCV_DIC A,  (SELECT * FROM WMP_STATION T  WHERE T.MUNCPL_ID IN  ("+station_dic+")) B WHERE A.IIIII = B.IIIII  AND A.CTYPE = T.CTYPE AND A.DTYPE = T.DTYPE) AS yingshou          FROM( SELECT a.dtype,a.ctype,COUNT(1) AS shishou FROM TR_REPO_RCV a,(SELECT a.* FROM TR_STATIONRCV_DIC a,(SELECT * FROM wmp_station t WHERE t.muncpl_id IN ("+station_dic+")) b WHERE a.iiiii  = b.iiiii AND a.mflag = 1) b  WHERE    a.d_datetime >= to_date(?,'yyyy/mm/dd hh24:mi:ss') AND                                                 a.d_datetime <  to_date(?,'yyyy/mm/dd hh24:mi:ss') AND                                                 a.dtype = b.dtype AND a.ctype = b.ctype AND a.iiiii = b.iiiii AND                                                 (a.cccc IS NULL OR a.cccc = 'BELS') GROUP BY a.dtype,a.ctype) t) b ON a.dtype = b.dtype AND a.ctype = b.ctype WHERE a.flage = 0 ORDER BY des_sy DESC";
    	else sql = "SELECT a.dtype,a.ctype,a.dtype_desc,a.ctype_desc,b.shishou,b.yingshou,       (CASE WHEN (b.yingshou - b.shishou) < 0 THEN b.yingshou ELSE b.shishou END) AS shishou_n,       (CASE  WHEN (B.YINGSHOU - B.SHISHOU) <= 0 THEN  0  ELSE  B.YINGSHOU - B.SHISHOU END) AS DES_SY          FROM TR_DATATYPE_DIC a LEFT JOIN( SELECT       t.dtype,t.ctype,      (SELECT dtype_desc FROM TR_DATATYPE_DIC WHERE dtype = t.dtype AND ctype = t.ctype ) AS d_name,      (SELECT ctype_desc FROM TR_DATATYPE_DIC WHERE dtype = t.dtype AND ctype = t.ctype ) AS c_name,      t.shishou,      (SELECT COUNT(1) FROM TR_STATIONRCV_DIC A,  (SELECT * FROM WMP_STATION T  WHERE T.MUNCPL_ID IN  ("+station_dic+") AND T.ISCHECK = "+ischeck+") B WHERE A.IIIII = B.IIIII  AND A.CTYPE = T.CTYPE AND A.DTYPE = T.DTYPE) AS yingshou          FROM( SELECT a.dtype,a.ctype,COUNT(1) AS shishou FROM TR_REPO_RCV a,(SELECT a.* FROM TR_STATIONRCV_DIC a,(SELECT * FROM wmp_station t WHERE t.muncpl_id IN ("+station_dic+") AND T.ISCHECK = "+ischeck+") b WHERE a.iiiii  = b.iiiii AND a.mflag = 0) b  WHERE    a.d_datetime >= to_date(?,'yyyy/mm/dd hh24:mi:ss') AND                                                 a.d_datetime <  to_date(?,'yyyy/mm/dd hh24:mi:ss') AND                                                 a.dtype = b.dtype AND a.ctype = b.ctype AND a.iiiii = b.iiiii AND                                                 (a.cccc IS NULL OR a.cccc = 'BELS') GROUP BY a.dtype,a.ctype) t) b ON a.dtype = b.dtype AND a.ctype = b.ctype WHERE a.flage = 0    UNION    SELECT a.dtype,a.ctype,a.dtype_desc,a.ctype_desc,b.shishou,b.yingshou,       (CASE WHEN (b.yingshou - b.shishou) < 0 THEN b.yingshou ELSE b.shishou END) AS shishou_n,       (CASE  WHEN (B.YINGSHOU - B.SHISHOU) <= 0 THEN  0  ELSE  B.YINGSHOU - B.SHISHOU END) AS DES_SY          FROM TR_DATATYPE_DIC a  INNER JOIN( SELECT       t.dtype,t.ctype,      (SELECT dtype_desc FROM TR_DATATYPE_DIC WHERE dtype = t.dtype AND ctype = t.ctype ) AS d_name,      (SELECT ctype_desc FROM TR_DATATYPE_DIC WHERE dtype = t.dtype AND ctype = t.ctype ) AS c_name,      t.shishou,      (SELECT COUNT(1) FROM TR_STATIONRCV_DIC A,  (SELECT * FROM WMP_STATION T  WHERE T.MUNCPL_ID IN  ("+station_dic+") AND T.ISCHECK = "+ischeck+") B WHERE A.IIIII = B.IIIII  AND A.CTYPE = T.CTYPE AND A.DTYPE = T.DTYPE) AS yingshou          FROM( SELECT a.dtype,a.ctype,COUNT(1) AS shishou FROM TR_REPO_RCV a,(SELECT a.* FROM TR_STATIONRCV_DIC a,(SELECT * FROM wmp_station t WHERE t.muncpl_id IN ("+station_dic+") AND T.ISCHECK = "+ischeck+") b WHERE a.iiiii  = b.iiiii AND a.mflag = 1) b  WHERE    a.d_datetime >= to_date(?,'yyyy/mm/dd hh24:mi:ss') AND                                                 a.d_datetime <  to_date(?,'yyyy/mm/dd hh24:mi:ss') AND                                                 a.dtype = b.dtype AND a.ctype = b.ctype AND a.iiiii = b.iiiii AND                                                 (a.cccc IS NULL OR a.cccc = 'BELS') GROUP BY a.dtype,a.ctype) t) b ON a.dtype = b.dtype AND a.ctype = b.ctype WHERE a.flage = 0 ORDER BY des_sy DESC";
    	return queryClient.createJdbcTemplate().queryForList(sql,new Object[]{beginTime,endTime,beginTime,endTime});
    }
    
    
    /**
     * 整合考核和非考核数据
     */
    public List<Map<String, Object>> mergeQS_timeOut(String beginTime,String endTime, String station_dic){
    	List<Map<String, Object>> data0 = queryQS_timeOut(beginTime,endTime, station_dic, 2); //所有的数据
    	List<Map<String, Object>> data1 = querySY(beginTime,endTime, station_dic, 0); //考核的数据
    	List<Map<String, Object>> data2 = querySY(beginTime,endTime, station_dic, 1); //非考核的数据
    	
    	
    	int data1_len = data1.size();
    	int data2_len = data2.size();
    	if(data1_len != data2_len ){logger.error("非考核和考核的查询条目不一样"); return null;}
    	
    	/**
    	 * 整合考核和非考核
    	 */
    	Map<String, Object> map_check = null;
    	List<Map<String, Object>> data_check = new ArrayList<>();
    	for(int i = 0; i < data1_len; i ++){ //循环考核的数据
    		Map<String, Object>  map1 = data1.get(i);
    		//相等主键
    		String dtype;
    		String ctype;
    		try{
	    		 dtype = map1.get("DTYPE").toString();
	    		 ctype = map1.get("CTYPE").toString();
    		}catch(Exception e){continue;}
    		for(int j = 0 ; j < data2_len; j++){ //循环非考核数据
    			Map<String, Object>  map2 = data2.get(j);
    			map_check = new HashMap<String, Object>();
    			if(dtype.equals(map2.get("DTYPE")) && ctype.equals(map2.get("CTYPE"))){
    				map_check.put("true_check", map1);
    				map_check.put("false_check", map2);
    				map_check.put("DTYPE", dtype);
    				map_check.put("CTYPE", ctype);
    				data_check.add(map_check);
    			}else{continue;}
    			data2.remove(j);break;
    		}
    	}
    	
    	/**
    	 * 整合所有数据
    	 */
    	List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> map : data0) {
    		//相等主键
    		String dtype;
    		String ctype;
    		try{
	    		 dtype = map.get("DTYPE").toString();
	    		 ctype = map.get("CTYPE").toString();
    		}catch(Exception e){continue;}
    		for (Map<String, Object> map_check_n : data_check) {
    			if(dtype.equals(map_check_n.get("DTYPE")) && ctype.equals(map_check_n.get("CTYPE"))){
    				map.put("check_data", map_check_n);
    				data.add(map);
    			}else{continue;}
    			break;
			}
    		
		}
    	
    		
    	return data;
    }
    
    
    /**
     * 缺失报文报警，根据阀值表
     * @return 
     * @param ischeck 是否是考核站 0：是，1：不是
     */
    public List<Map<String, Object>> queryQS_timeOut(String beginTime,String endTime, String station_dic,int ischeck){
    	try {
    		List<Map<String, Object>> data = querySY(beginTime,endTime,station_dic,ischeck);
    		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		Date datetime =  sf.parse(beginTime);
    		String dtype = "";
    		String ctype = "";
    		String sql = "";
    		Object des_sy = ""; //缺收
    		List<Map<String, Object>> list_map = null;
    		int flag = 0;
    		int df_p = 0; //报文缺失百分比
    		List<Map<String, Object>> data_new = new ArrayList<Map<String,Object>>();
    		for (Map<String, Object> map : data) {
    			dtype = map.get("DTYPE").toString();
    			ctype = map.get("CTYPE").toString();
    			des_sy = map.get("DES_SY");
    			
    			//报文接受正常
    			if(des_sy != null && "0".equals(des_sy.toString())){
    				map.put("ISFLAG", 3);
    				data_new.add(map);
    				continue;
    			}
    			
    			
    			sql = "SELECT DISTINCT A.DTYPE, A.CTYPE, A.MASK_STR, A.SCHDNUM, A.DFLAG, A.MRANGE, A.MFLAG, A.MTIME, B.FLAGE, B.OUTTIME, B.MISSPG FROM TR_STATIONRCV_DIC A,TR_DATATYPE_DIC B WHERE A.DTYPE = B.DTYPE AND A.CTYPE = B.CTYPE AND A.INUSE = 1 AND A.CTYPE = '"+ctype+"' AND A.DTYPE = '"+dtype+"'  AND B.FLAGE = 0";
    			list_map = queryClient.createJdbcTemplate().queryForList(sql);
    			if(list_map.size() == 0) continue;
    			df_p = Integer.parseInt(list_map.get(0).get("MISSPG").toString());
    			flag = check_time(list_map.get(0),datetime);
    			if(flag == 0){ //还没到收报时间
    				map.put("ISFLAG", 0);
    				data_new.add(map);
    			}else if(flag == 1){ //报文在阈限值以内
    				map.put("ISFLAG", 1);
    				data_new.add(map);
    			}else{ //报文标记缺失
    				if(des_sy == null){ //缺失100%
    					int yingshou_n = queryClient.createJdbcTemplate().queryForObject("SELECT COUNT(1)  FROM TR_STATIONRCV_DIC A,  (SELECT *   FROM WMP_STATION T  WHERE T.MUNCPL_ID IN ("+station_dic+")) B  WHERE A.IIIII = B.IIIII   AND A.CTYPE = '"+ctype+"' AND A.DTYPE = '"+dtype+"'", Integer.class);
    					map.put("SHISHOU_N", 0);
    					map.put("YINGSHOU", yingshou_n);
    					map.put("DES_SY", yingshou_n);
    					map.put("ISFLAG", 4);
    					map.put("DF_P", df_p);
    					data_new.add(map);
    					continue;
    				}
    				int n_p = (Integer.parseInt(des_sy.toString()) / Integer.parseInt(map.get("YINGSHOU").toString())) * 100; 
    				if(n_p > df_p){
    					map.put("ISFLAG", 4);
    					map.put("DF_P", df_p);
    					data_new.add(map);
    					continue;
    				}
    				
    				map.put("ISFLAG", 2);
    				data_new.add(map);
    			}
    		}
    		return data_new;
    	} catch (ParseException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	return null;
    }
    
    /**
     * 判断当前时间是否接收报文
     * 
     * @return  0:没到收报时间，1：在阈限值以内， 2：缺报，3：报文收发正常，4:报文缺失缺失过大
     */
	public int check_time(Map<String, Object> map,Date datetime){
    	
        String dflag [] = map.get("DFLAG").toString().split(","); //是不是没天都有
        String mflag  = map.get("MFLAG").toString(); //是不是月报
        String mrange [] = map.get("MRANGE").toString().split(","); //是不是每月接收
        Object mtime = map.get("MTIME"); //接收月的最晚日期
        String mask_str = map.get("MASK_STR").toString(); //是不是没个时次都接收
//        int timeout_min = 30; //小时报文阈限30分钟
        int timeout_min = Integer.parseInt(map.get("OUTTIME").toString());
        
        //查询时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(datetime);
//        int now_year = calendar.get(Calendar.YEAR);
        int now_mon = calendar.get(Calendar.MONTH)+1; //当前月
        int now_day = calendar.get(Calendar.DATE);  //当天
        int now_hour = calendar.get(Calendar.HOUR_OF_DAY); //当前时次
        long long_date = calendar.getTimeInMillis();  //时间转毫秒
        
        //当前系统时间
        Calendar calendar_sys = Calendar.getInstance();
        calendar_sys.setTime(new Date());
        calendar_sys.add(Calendar.HOUR_OF_DAY, -8);
//        int now_year_sys = calendar_sys.get(Calendar.YEAR);
        int now_mon_sys = calendar_sys.get(Calendar.MONTH)+1; //当前月
        int now_day_sys = calendar_sys.get(Calendar.DATE);  //当天
//        int now_hour_sys = calendar_sys.get(Calendar.HOUR_OF_DAY); //当前时次
        long long_date_sys = calendar_sys.getTimeInMillis();  //时间转毫秒
        
        
//        String str_mon = (now_mon_sys+"").length() > 1 ? ""+now_mon_sys : "0"+ now_mon_sys;
//        String str_day = (now_day_sys+"").length() > 1 ? ""+now_day_sys : "0"+ now_day_sys;
//        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHH");
//        String str_ymd = now_year_sys + str_mon + str_day;
        
        if("1".equals(mflag)){ //判断是否是月报
        	if(mtime == null) return 0;
        	int i_mtime = Integer.parseInt(mtime.toString());
        	if(mrange.length == 1){//判断是不是单月或者每月接收
        		if("0".equals(mrange[1])){ //判断是不是每月接收
        			if(now_day_sys > i_mtime){
        				return 2;
        			}else{
        			    return 1;
        			}
        		}else{
        		    if(now_mon == Integer.parseInt(mrange[1])){ //是不是在当月
            			if(now_day_sys > i_mtime){
            				return 2;
            			}else{
            			    return 1;
            			}
        		    }else{
        		    	return 0;
        		    }
        		}
        	}else { //  判断是都为多月
        	    for (String string : mrange) {
        		    if(now_mon_sys == Integer.parseInt(string)){ //是不是在当月
            			if(now_day_sys > i_mtime){
            				return 2;
            			}else{
            			    return 1;
            			}
        		    }
        	    }
        	    return 0;
        	}
        }//判断是否是月报 end
        
        //判断是不是日报
    	String hour= "";
        if(dflag.length == 1){ //判断是否是每天还是单天
        	if("0".equals(dflag[0])){ //判断是都是每天都发
        		for(int i = 0,len = mask_str.length(); i < len ; i++){
        			hour = mask_str.substring(i,i+1);
        			if("0".equals(hour)) return 0; 

        			if(i == now_hour){//判断是否是当前时次
        				if((long_date_sys - long_date) > (timeout_min * 60000l)){
        					return 2;
        				}else{
        					return 1;
        				}
        			}
        		}
        		
        	}else if(dflag[0].equals(now_day)){ //判断是不是当天
        			for(int i = 0,len = mask_str.length(); i < len ; i++){
        				hour = mask_str.substring(i,i+1);
        				if("0".equals(hour)) return 0;

        				if(i == now_hour){//判断是否是当前时次
            				if((long_date_sys - long_date) > (timeout_min * 60000l)){
            					return 2;
            				}else{
            					return 1;
            				}
        				}
        			}
        		}else{
        			return 0;
        		}
        	
        	
        	}else {   //多天
        		for(String day : dflag){
        			if(day.equals(now_day)){ //判断是不是当天
            			for(int i = 0,len = mask_str.length(); i < len ; i ++){
            				hour = mask_str.substring(i,i+1);
            				if("0".equals(hour)) return 0;

            				if(i == now_hour){//判断是否是当前时次
                				if((long_date_sys - long_date) > (timeout_min * 60000l)){
                					return 2;
                				}else{
                					return 1;
                				}
            				}
            			}
            		}
        		}
        		
        	}
    	
    	return 0;
    }
    
    /**
     * 查询缺失和逾限详情
     * @param beginTime
     * @param endTime
     * @param station_dic
     * @param station_type
     * @return 
     * @return
     */
	public List<Map<String, Object>> queryQY_table(String beginTime, String endTime, String station_dic,
			String station_type) {
		String types [] =  station_type.split("_");
		String dtype = types[0];
		String ctype = types[1];
		if(types.length > 2){
			for (int i = 2; i < types.length; i++) {
				 ctype+= "_" + types[i];
			}
		}
		
		String sql = "SELECT t.dtype,t.ctype,t.iiiii, (SELECT DISTINCT sname FROM wmp_station WHERE iiiii = t.iiiii) AS sname, (SELECT DISTINCT muncpl FROM wmp_station WHERE iiiii = t.iiiii) AS dic_name, '0' AS flag FROM TR_STATIONRCV_DIC t,(SELECT * FROM WMP_STATION T WHERE T.MUNCPL_ID IN ("+station_dic+")) b WHERE t.dtype = '"+dtype+"' AND t.ctype = '"+ctype+"' AND t.IIIII = b.IIIII AND (t.dtype,t.ctype,t.iiiii) NOT IN ( SELECT a.dtype,a.ctype,a.iiiii FROM TR_REPO_RCV a,(SELECT a.* FROM TR_STATIONRCV_DIC a,(SELECT * FROM wmp_station t WHERE t.muncpl_id IN ("+station_dic+")) b WHERE a.iiiii = b.iiiii) b WHERE a.d_datetime >= to_date('"+beginTime+"','yyyy/mm/dd hh24:mi:ss') AND a.d_datetime < to_date('"+endTime+"','yyyy/mm/dd hh24:mi:ss') AND a.dtype = b.dtype AND a.ctype = b.ctype AND a.iiiii = b.iiiii AND (a.cccc IS NULL OR a.cccc = 'BELS') AND a.dtype = '"+dtype+"' AND a.ctype = '"+ctype+"') UNION SELECT a.dtype,a.ctype,a.iiiii, (SELECT DISTINCT sname FROM wmp_station WHERE iiiii = a.iiiii) AS sname, (SELECT DISTINCT muncpl FROM wmp_station WHERE iiiii = a.iiiii) AS dic_name, '1' AS flag FROM TR_REPO_RCV a,(SELECT a.* FROM TR_STATIONRCV_DIC a,(SELECT * FROM wmp_station t WHERE t.muncpl_id IN ("+station_dic+")) b WHERE a.iiiii = b.iiiii) b WHERE a.d_datetime >= to_date('"+beginTime+"','yyyy/mm/dd hh24:mi:ss') AND a.d_datetime < to_date('"+endTime+"','yyyy/mm/dd hh24:mi:ss') AND a.dtype = b.dtype AND a.ctype = b.ctype AND a.iiiii = b.iiiii AND (a.cccc IS NULL OR a.cccc = 'BELS') AND a.dtype = '"+dtype+"' AND a.ctype = '"+ctype+"' AND a.tflag = 1 ";
		queryClient.createSqlQuery(sql).list();
		return queryClient.createJdbcTemplate().queryForList(sql);
	}
	
	/**
	 * 查询缺失、阈限、实收、应收详情
	 * @param beginTime
	 * @param endTime
	 * @param station_dic
	 * @param station_type
	 * @return
	 */
	public List<Map<String, Object>> queryST_table(String beginTime, String endTime, String station_dic, String check_type,
			String dtype,String ctype,String station_type) {
		
		String sql = "";
		
	    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
	    Calendar calendar = Calendar.getInstance();
	    try {
			calendar.setTime(sf.parse(beginTime));
			calendar.add(Calendar.HOUR_OF_DAY, 8);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	    String dateTime = sf.format(calendar.getTime());
		
		String time =  dateTime.split("\\s+")[1].split(":")[0];
		
		//缺收
		if("queshou".equals(station_type))
		sql = "SELECT  '"+time+"' AS TIME,'缺收' AS FLAG,T.DTYPE, T.CTYPE, T.IIIII, (SELECT DISTINCT SNAME FROM WMP_STATION WHERE IIIII = T.IIIII) AS SNAME, (SELECT DISTINCT MUNCPL FROM WMP_STATION WHERE IIIII = T.IIIII) AS DIC_NAME FROM TR_STATIONRCV_DIC T, (SELECT * FROM WMP_STATION T WHERE T.MUNCPL_ID IN ("+station_dic+") AND T.ISCHECK = "+check_type+") B WHERE T.DTYPE = '"+dtype+"' AND T.CTYPE = '"+ctype+"' AND T.IIIII = B.IIIII AND (T.DTYPE, T.CTYPE, T.IIIII) NOT IN (SELECT A.DTYPE, A.CTYPE, A.IIIII FROM TR_REPO_RCV A, (SELECT A.* FROM TR_STATIONRCV_DIC A, (SELECT * FROM WMP_STATION T WHERE T.MUNCPL_ID IN ("+station_dic+") AND T.ISCHECK = "+check_type+") B WHERE A.IIIII = B.IIIII) B WHERE A.D_DATETIME >= TO_DATE('"+beginTime+"', 'yyyy/mm/dd hh24:mi:ss') AND A.D_DATETIME < TO_DATE('"+endTime+"', 'yyyy/mm/dd hh24:mi:ss') AND A.DTYPE = B.DTYPE AND A.CTYPE = B.CTYPE AND A.IIIII = B.IIIII AND (A.CCCC IS NULL OR A.CCCC = 'BELS') AND A.DTYPE = '"+dtype+"' AND A.CTYPE = '"+ctype+"')";
		//实收
		if("shishou".equals(station_type))
		sql = "SELECT  '"+time+"' AS TIME,'实收' AS FLAG,T.DTYPE, T.CTYPE, T.IIIII, (SELECT DISTINCT SNAME FROM WMP_STATION WHERE IIIII = T.IIIII) AS SNAME, (SELECT DISTINCT MUNCPL FROM WMP_STATION WHERE IIIII = T.IIIII) AS DIC_NAME FROM TR_STATIONRCV_DIC T, (SELECT * FROM WMP_STATION T WHERE T.MUNCPL_ID IN ("+station_dic+") AND T.ISCHECK = "+check_type+") B WHERE T.DTYPE = '"+dtype+"' AND T.CTYPE = '"+ctype+"' AND T.IIIII = B.IIIII AND (T.DTYPE, T.CTYPE, T.IIIII) IN (SELECT A.DTYPE, A.CTYPE, A.IIIII FROM TR_REPO_RCV A, (SELECT A.* FROM TR_STATIONRCV_DIC A, (SELECT * FROM WMP_STATION T WHERE T.MUNCPL_ID IN ("+station_dic+") AND T.ISCHECK = "+check_type+") B WHERE A.IIIII = B.IIIII) B WHERE A.D_DATETIME >= TO_DATE('"+beginTime+"', 'yyyy/mm/dd hh24:mi:ss') AND A.D_DATETIME < TO_DATE('"+endTime+"', 'yyyy/mm/dd hh24:mi:ss') AND A.DTYPE = B.DTYPE AND A.CTYPE = B.CTYPE AND A.IIIII = B.IIIII AND (A.CCCC IS NULL OR A.CCCC = 'BELS') AND A.DTYPE = '"+dtype+"' AND A.CTYPE = '"+ctype+"') ";
		//应收
		if("yingshou".equals(station_type))
		sql = "SELECT  '"+time+"' AS TIME,'应收' AS FLAG,T.DTYPE, T.CTYPE, T.IIIII, (SELECT DISTINCT SNAME FROM WMP_STATION WHERE IIIII = T.IIIII) AS SNAME, (SELECT DISTINCT MUNCPL FROM WMP_STATION WHERE IIIII = T.IIIII) AS DIC_NAME FROM TR_STATIONRCV_DIC T, (SELECT * FROM WMP_STATION T WHERE T.MUNCPL_ID IN ("+station_dic+") AND T.ISCHECK = "+check_type+" ) B WHERE T.DTYPE = '"+dtype+"' AND T.CTYPE = '"+ctype+"' AND T.IIIII = B.IIIII ";
		//阈限
		if("yuxian".equals(station_type))
		sql = "SELECT  '"+time+"' AS TIME,'阈限' AS FLAG,A.DTYPE, A.CTYPE, A.IIIII, (SELECT DISTINCT SNAME FROM WMP_STATION WHERE IIIII = A.IIIII) AS SNAME, (SELECT DISTINCT MUNCPL FROM WMP_STATION WHERE IIIII = A.IIIII) AS DIC_NAME FROM TR_REPO_RCV A, (SELECT A.* FROM TR_STATIONRCV_DIC A, (SELECT * FROM WMP_STATION T WHERE T.MUNCPL_ID IN ("+station_dic+") AND T.ISCHECK = 1) B WHERE A.IIIII = B.IIIII) B WHERE A.D_DATETIME >= TO_DATE('"+beginTime+"', 'yyyy/mm/dd hh24:mi:ss') AND A.D_DATETIME < TO_DATE('"+endTime+"', 'yyyy/mm/dd hh24:mi:ss') AND A.DTYPE = B.DTYPE AND A.CTYPE = B.CTYPE AND A.IIIII = B.IIIII AND (A.CCCC IS NULL OR A.CCCC = 'BELS') AND A.DTYPE = '"+dtype+"' AND A.CTYPE = '"+ctype+"' AND A.TFLAG = 1";
		
		return queryClient.createJdbcTemplate().queryForList(sql);
	}
	
	
	/**
	 * 查询逾限条目
	 * @param beginTime
	 * @param endTime
	 * @param station_dic
	 * @return
	 */
	public Map<String, Object> queryALLCon(String beginTime, String endTime, String station_dic,String station_type){
		String types [] =  station_type.split("_");
		String dtype = types[0];
		String ctype = types[1];
		if(types.length > 2){
			for (int i = 2; i < types.length; i++) {
				 ctype+= "_" + types[i];
			}
		}
		
		String sql_false = "SELECT COUNT(1) AS con_yx FROM TR_REPO_RCV a,(SELECT a.* FROM TR_STATIONRCV_DIC a,(SELECT * FROM wmp_station t WHERE t.muncpl_id IN ("+station_dic+")  AND t.ISCHECK = 1) b WHERE a.iiiii = b.iiiii) b WHERE a.d_datetime >= to_date('"+beginTime+"','yyyy/mm/dd hh24:mi:ss') AND a.d_datetime < to_date('"+endTime+"','yyyy/mm/dd hh24:mi:ss') AND a.dtype = b.dtype AND a.ctype = b.ctype AND a.iiiii = b.iiiii AND (a.cccc IS NULL OR a.cccc = 'BELS') AND a.dtype = '"+dtype+"' AND a.ctype = '"+ctype+"' AND a.tflag = 1";
		String sql_true = "SELECT COUNT(1) AS con_yx FROM TR_REPO_RCV a,(SELECT a.* FROM TR_STATIONRCV_DIC a,(SELECT * FROM wmp_station t WHERE t.muncpl_id IN ("+station_dic+")  AND t.ISCHECK = 1) b WHERE a.iiiii = b.iiiii) b WHERE a.d_datetime >= to_date('"+beginTime+"','yyyy/mm/dd hh24:mi:ss') AND a.d_datetime < to_date('"+endTime+"','yyyy/mm/dd hh24:mi:ss') AND a.dtype = b.dtype AND a.ctype = b.ctype AND a.iiiii = b.iiiii AND (a.cccc IS NULL OR a.cccc = 'BELS') AND a.dtype = '"+dtype+"' AND a.ctype = '"+ctype+"' AND a.tflag = 1";
		
		String sql_map_false = "SELECT b.yingshou, (CASE WHEN (b.yingshou - b.shishou) < 0 THEN b.yingshou ELSE b.shishou END) AS shishou_n, (b.yingshou - b.shishou) AS des_sy FROM TR_DATATYPE_DIC a LEFT JOIN( SELECT t.dtype,t.ctype, (SELECT dtype_desc FROM TR_DATATYPE_DIC WHERE dtype = t.dtype AND ctype = t.ctype ) AS d_name, (SELECT ctype_desc FROM TR_DATATYPE_DIC WHERE dtype = t.dtype AND ctype = t.ctype ) AS c_name, t.shishou,  (SELECT COUNT(1) FROM TR_STATIONRCV_DIC A, (SELECT * FROM WMP_STATION T  WHERE T.MUNCPL_ID IN  ("+station_dic+") AND T.ISCHECK = 1) B WHERE A.IIIII = B.IIIII  AND A.CTYPE = T.CTYPE AND A.DTYPE = T.DTYPE) AS yingshou FROM( SELECT a.dtype,a.ctype,COUNT(1) AS shishou FROM TR_REPO_RCV a,(SELECT a.* FROM TR_STATIONRCV_DIC a,(SELECT * FROM wmp_station t WHERE t.muncpl_id IN ("+station_dic+") AND T.ISCHECK = 1) b WHERE a.iiiii = b.iiiii) b WHERE a.d_datetime >= to_date('"+beginTime+"','yyyy/mm/dd hh24:mi:ss') AND a.d_datetime < to_date('"+endTime+"','yyyy/mm/dd hh24:mi:ss') AND a.dtype = b.dtype AND a.ctype = b.ctype AND a.iiiii = b.iiiii AND (a.cccc IS NULL OR a.cccc = 'BELS') GROUP BY a.dtype,a.ctype) t) b ON a.dtype = b.dtype AND a.ctype = b.ctype WHERE a.flage = 0 AND a.dtype = '"+dtype+"' AND a.ctype = '"+ctype+"'";
		String sql_map_true = "SELECT b.yingshou, (CASE WHEN (b.yingshou - b.shishou) < 0 THEN b.yingshou ELSE b.shishou END) AS shishou_n, (b.yingshou - b.shishou) AS des_sy FROM TR_DATATYPE_DIC a LEFT JOIN( SELECT t.dtype,t.ctype, (SELECT dtype_desc FROM TR_DATATYPE_DIC WHERE dtype = t.dtype AND ctype = t.ctype ) AS d_name, (SELECT ctype_desc FROM TR_DATATYPE_DIC WHERE dtype = t.dtype AND ctype = t.ctype ) AS c_name, t.shishou,  (SELECT COUNT(1) FROM TR_STATIONRCV_DIC A, (SELECT * FROM WMP_STATION T  WHERE T.MUNCPL_ID IN  ("+station_dic+") AND T.ISCHECK = 0) B WHERE A.IIIII = B.IIIII  AND A.CTYPE = T.CTYPE AND A.DTYPE = T.DTYPE) AS yingshou FROM( SELECT a.dtype,a.ctype,COUNT(1) AS shishou FROM TR_REPO_RCV a,(SELECT a.* FROM TR_STATIONRCV_DIC a,(SELECT * FROM wmp_station t WHERE t.muncpl_id IN ("+station_dic+") AND T.ISCHECK = 0) b WHERE a.iiiii = b.iiiii) b WHERE a.d_datetime >= to_date('"+beginTime+"','yyyy/mm/dd hh24:mi:ss') AND a.d_datetime < to_date('"+endTime+"','yyyy/mm/dd hh24:mi:ss') AND a.dtype = b.dtype AND a.ctype = b.ctype AND a.iiiii = b.iiiii AND (a.cccc IS NULL OR a.cccc = 'BELS') GROUP BY a.dtype,a.ctype) t) b ON a.dtype = b.dtype AND a.ctype = b.ctype WHERE a.flage = 0 AND a.dtype = '"+dtype+"' AND a.ctype = '"+ctype+"'";
		
		int yx_con_true = queryClient.createJdbcTemplate().queryForObject(sql_true, Integer.class);
		int yx_con_false = queryClient.createJdbcTemplate().queryForObject(sql_false, Integer.class);
		
		Map<String, Object> map_false = queryClient.createJdbcTemplate().queryForMap(sql_map_false);
		Map<String, Object> map_true = queryClient.createJdbcTemplate().queryForMap(sql_map_true);
		
		Map<String, Object> map = new HashMap<>();
		
		map.put("yx_con_true", yx_con_true);
		map.put("yx_con_false", yx_con_false);
		
		Object yingshou_false = map_false.get("YINGSHOU");
		Object yingshou_true = map_true.get("YINGSHOU");
		
		if(yingshou_false == null){
			int yingshou_n = queryClient.createJdbcTemplate().queryForObject("SELECT COUNT(1)  FROM TR_STATIONRCV_DIC A,  (SELECT *   FROM WMP_STATION T  WHERE T.MUNCPL_ID IN ("+station_dic+") AND T.ISCHECK = 1) B  WHERE A.IIIII = B.IIIII   AND A.CTYPE = '"+ctype+"' AND A.DTYPE = '"+dtype+"'", Integer.class);
			map_false.put("SHISHOU_N", 0);
			map_false.put("YINGSHOU", yingshou_n);
			map_false.put("DES_SY", yingshou_n);
		}
		
		if(yingshou_true == null){
			int yingshou_n = queryClient.createJdbcTemplate().queryForObject("SELECT COUNT(1)  FROM TR_STATIONRCV_DIC A,  (SELECT *   FROM WMP_STATION T  WHERE T.MUNCPL_ID IN ("+station_dic+") AND T.ISCHECK = 0) B  WHERE A.IIIII = B.IIIII   AND A.CTYPE = '"+ctype+"' AND A.DTYPE = '"+dtype+"'", Integer.class);
			map_true.put("SHISHOU_N", 0);
			map_true.put("YINGSHOU", yingshou_n);
			map_true.put("DES_SY", yingshou_n);
		}

		map.put("map_false", map_false);
		map.put("map_true", map_true);
		
		return map;
	}

	/**
	 * 站点关联节点查询
	 * @param station
	 * @return
	 */
	public List<Map<String, Object>> getStationNode(String station) {
		String sql = "select a.station_id,a.node_sort,b.node_name,b.devied_id from WMP_STATION_NODE_REF_DETAIL a,WMP_NODE_MANAGE b WHERE a.node_id = b.id AND  a.station_id  = '"+station+"'  ORDER BY a.node_sort ASC";
		return queryClient.createJdbcTemplate().queryForList(sql);
	}

	/**
	 * 根据设备ID查询FTP日志路径
	 * @param dev_id
	 * @return
	 */
	public Map<String, Object> queryFTPPath(String dev_id) {
		String sql = "SELECT * FROM wmp_device t WHERE t.id = '"+dev_id+"'";
		return queryClient.createJdbcTemplate().queryForMap(sql);
	}
}
