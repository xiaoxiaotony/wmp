package com.txy.web.main.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.txy.common.bean.PageList;
import com.txy.common.bean.TreeInfoBean;
import com.txy.common.orm.SqlBeanClient;
import com.txy.common.orm.query.impl.DynamicBean;
import com.txy.common.util.DateFormatUtil;
import com.txy.common.util.StringUtils;
import com.txy.web.common.util.CimissInterfaceUtil;

/**
 * @ClassName: GroundRealTimeDataService
 * @Description: 地面实时传输资料
 * @author lqy
 * @date 2015年12月8日 上午9:34:19
 * 
 */
@Service
public class GroundRealTimeDataService {

	@Autowired
	private SqlBeanClient queryClient;

	public PageList<DynamicBean> getGroundRealTimeData(String time,
			String stationCode,  int start, int pageSize,
			String stationTypes,String userId) {
	        SimpleDateFormat formt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startTime = null;
		Date endTime = null;

		StringBuffer sql = new StringBuffer();
		sql.append("select wgs.MUNCPL as area,wgs.sname  as zm ,s.V01300 as zh,s.D_DATETIME as time ,s.V10004 as qy,s.V12001 as wd,s.V13003 as sd,s.V13019 as sl,s.V11201 as fx,"
				+ " s.V11202 as fs from surf_wea_mul_hor_tab s left join WMP_STATION wgs "
				+ "on s.V01300 = wgs.iiiii  where 1 = 1");
		                //+ "on s.V01300=wgs.iiiii left join WMP_STATION_DIC sd  on sd.MUNCPL_ID = wgs.MUNCPL_ID where 1 = 1");

		if (StringUtils.isEmpty(time)) {
			endTime = new Date();
		} else {
		    try
                    {
                        endTime = formt.parse(time);
                    }
                    catch (ParseException e)
                    {
                        e.printStackTrace();
                    }
		}
		

		if (!StringUtils.isEmpty(stationCode)) {

		    	String[] stationCodes = stationCode.split(",");
			if (stationCodes != null && stationCodes.length > 0)
			{
				 sql.append(" and s.V01300 in (");
			}
			for (int i = 0; i < stationCodes.length; i++) {
			    if (stationCodes[i] != null)
//			    if (stationCodes[i] != null && stationCodes[i].startsWith("U"))
			    {
				sql.append("'"+stationCodes[i]+"'");
				if (stationCodes.length-1 != i)
				{
				    sql.append(",");
				}
			    }
			}
			if (stationCodes != null && stationCodes.length > 0)
			{
				sql.append(") ");
			}
		    
		}
		if (!StringUtils.isEmpty(stationTypes)) {
			StringBuffer sb = new StringBuffer();
			String[] types = stationTypes.split(",");
			sb.append("(");
			for (int i = 0; i < types.length; i++) {
				if (i == 0) {
					sb.append("'" + types[i] + "'");
				} else {
					sb.append(",'" + types[i] + "'");
				}
			}
			sb.append(")");
			sql.append(" and wgs.STYPE_ID in " + sb.toString());
		}
		
		startTime = new Date(endTime.getTime() - 1000 * 60 * 60 * 24);
		sql.append(" and s.D_DATETIME >= to_date('"+formt.format(startTime)+"','yyyy-mm-dd hh24:mi:ss') and s.D_DATETIME < to_date('"+formt.format(endTime)+"','yyyy-mm-dd hh24:mi:ss')");
		sql.append(" order by s.D_DATETIME desc");
		return queryClient.createSqlQuery(sql.toString()).page(
				start, pageSize);
	}

	public List<Map<String, Object>> getStationTypes() {
		String sql = "SELECT STYPE_ID,STYPE_NAME FROM WMP_STATION_TYPE T";
		List<Map<String, Object>> list = queryClient.createJdbcTemplate()
				.queryForList(sql);
		return list;
	}

	public List<Map<String, Object>> queryThresholdValues() {
		String sql = "SELECT id,elementname,elementcode,elementvalue,createdate FROM WMP_GROUND_DATA_THRESHOLD T";
		List<Map<String, Object>> list = queryClient.createJdbcTemplate()
				.queryForList(sql);
		return list;
	}

	public List<DynamicBean> getGroundChartData(String time,
			String stationCode,  String stationTypes,String userId) {
		Date startTime = null;
		Date endTime = null;

		StringBuffer sql = new StringBuffer();
		sql.append("select sd.MUNCPL as area,wgs.sname  as zm ,s.V01300 as zh,s.D_DATETIME as time ,s.V10004 as qy,s.V12001 as wd,s.V13003 as sd,s.V13019 as sl,s.V11201 as fx,"
				+ " s.V11202 as fs from surf_wea_mul_hor_tab s left join WMP_STATION wgs on s.V01300=wgs.iiiii left join WMP_STATION_DIC sd"
				+ " on sd.MUNCPL_ID = wgs.MUNCPL_ID where 1 = 1");

		if (StringUtils.isEmpty(time)) {
			endTime = new Date();
		} else {
		    try
                    {
                        endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
                    }
                    catch (ParseException e)
                    {
                        e.printStackTrace();
                    }
		}
		sql.append(" and s.D_DATETIME >= ? and s.D_DATETIME < ?");

		if (!StringUtils.isEmpty(stationCode)) {
			sql.append(" and s.V01300 = '" + stationCode + "'");
		}
		else
		{
		    return new ArrayList<DynamicBean>();
		}

		if (!StringUtils.isEmpty(stationTypes)) {
			StringBuffer sb = new StringBuffer();
			String[] types = stationTypes.split(",");
			sb.append("(");
			for (int i = 0; i < types.length; i++) {
				if (i == 0) {
					sb.append("'" + types[i] + "'");
				} else {
					sb.append(",'" + types[i] + "'");
				}
			}
			sb.append(")");
			sql.append(" and wgs.STYPE_ID in " + sb.toString());
		}
		sql.append(" order by s.D_DATETIME asc");
		startTime = new Date(endTime.getTime() - 1000 * 60 * 60 * 24);
		List<Object> args = new ArrayList<Object>();
		args.add(startTime);
		args.add(endTime);
		return queryClient.createSqlQuery(sql.toString(), args.toArray())
				.list();
	}

	public String queryStationNameByID(String station_Id_d) {
		String sql = "select w.sname from WMP_STATION w where w.MUNCPL_ID = ?";
		DynamicBean bean = queryClient.createSqlQuery(sql, station_Id_d).uniqueResult();
		return  bean == null ? "" : bean.getValue("sname");
	}

	public List<DynamicBean> queryThresholds() {
	    String sql = "select id,elementname,elementcode,elementvalue,createdate from WMP_GROUND_DATA_THRESHOLD ";
	    return queryClient.createSqlQuery(sql).list();
	}

	public List<DynamicBean> queryGroundDataByTime() {
	    	Date startTime = null;
		Date endTime = new Date();
		StringBuffer sql = new StringBuffer();
		sql.append("select wgs.MUNCPL as area,wgs.sname  as zm ,s.V01300 as zh,s.D_DATETIME as time ,s.V10004 as qy,s.V12001 as wd,s.V13003 as sd,s.V13019 as sl,s.V11201 as fx,"
				+ " s.V11202 as fs from surf_wea_mul_hor_tab s left join WMP_STATION wgs "
				+ " on s.V01300 = wgs.iiiii  where 1 = 1");
				//+ "on s.V01300=wgs.iiiii left join WMP_STATION_DIC sd  on sd.MUNCPL_ID = wgs.MUNCPL_ID where 1 = 1");
		sql.append(" and s.D_DATETIME >= ? and s.D_DATETIME < ?");

		sql.append(" order by s.D_DATETIME asc");
		startTime = new Date(endTime.getTime() - 1000 * 60 * 60);
		List<Object> args = new ArrayList<Object>();
		args.add(startTime);
		args.add(endTime);
		return queryClient.createSqlQuery(sql.toString(), args.toArray())
				.list();
	}
	
	public List<DynamicBean> getStationByStationType(String stationtype,String staIds)
	{
	    String sql = "select t.IIIII from WMP_STATION t where t.stype_id in "+stationtype+" and t.MUNCPL_ID in ("+staIds+")";
	    return queryClient.createSqlQuery(sql.toString()) .list();
	}
	
	public List<DynamicBean> getCimissData(Date startTime,Date endTime,String stationCode,String stationTypes)
	{
		String data = null;
		HashMap<String, String> params = new HashMap<String, String>();
		StringBuffer sb =  null;
		if (!StringUtils.isEmpty(stationTypes)) {
                     sb = new StringBuffer();
                    String[] types = stationTypes.split(",");
                    sb.append("(");
                    for (int i = 0; i < types.length; i++) {
                            if (i == 0) {
                                    sb.append("'" + types[i] + "'");
                            } else {
                                    sb.append(",'" + types[i] + "'");
                            }
                    }
                    sb.append(")");
                    
                    
		}
		
		// cimiss查询方法
                String queryMethd = "getSurfEleInRegionByTimeRange";
		
		if (!StringUtils.isEmpty(stationCode))
		{
		    StringBuffer staIds = new StringBuffer();
		    String[] stationCodes = stationCode.split(",");
                    for (int i = 0; i < stationCodes.length; i++) {
                        if (stationCodes[i] != null && !stationCodes[i].startsWith("U"))
                        {
                                    staIds.append(stationCodes[i]);
                                    if (stationCodes.length-1 != i)
                                    {
                                        staIds.append(",");
                                    }
                        }
                    }
                    
                    char c = staIds.charAt(staIds.length() - 1);
                    if (c == ',')
                    {
                        int index = staIds.toString().lastIndexOf(',');
                        staIds.deleteCharAt(index);
                    }
                    List<DynamicBean> list =  new ArrayList<>();
                    if (sb != null)
                    {
                         list =  getStationByStationType(sb.toString(),staIds.toString());
                    }
                    staIds = new StringBuffer();
                    for (int j = 0; j < list.size(); j++)
                    {
                        if (list.get(j).getValue("iiiii") != null)
                        {
                            staIds.append(list.get(j).getValue("iiiii"));
                            if (list.size()-1 != j)
                            {
                                staIds.append(",");
                            }
                        }
                    }
                    if (staIds != null && !"".equals(staIds))
                    {
                        char m = staIds.charAt(staIds.length() - 1);
                        if (m == ',')
                        {
                            int index = staIds.toString().lastIndexOf(',');
                            staIds.deleteCharAt(index);
                        }
                        queryMethd = "getSurfEleByTimeRangeAndStaID";
                        params.put("staIds", staIds.toString());
                    }
                    else
                    {
                        params.put("adminCodes", "54000");
                    }
		}
		else
		{
		    params.put("adminCodes", "54000");
		}
		// 资料代码
		params.put("dataCode", "SURF_CHN_MUL_HOR");
		// 要素字段代码
		params.put("elements", "Station_name,city,Datetime,Station_Id_d,PRS,TEM,RHU,PRE,WIN_S_INST,WIN_D_INST");
		// 时间段
		params.put("timeRange", "("+DateFormatUtil.formateDatetime(startTime,"yyyyMMddHHmmss")+","+DateFormatUtil.formateDatetime(endTime,"yyyyMMddHHmmss")+"]");
		// 排序：按照站号,资料时间从小到大排序
		params.put("orderBy", "Station_Id_C:asc,Datetime:asc");

		params.put("limitCnt", "1000");
		// params.put("staIds", stations);
		// // 站台级别
		// params.put("staLevels", s_type);

		//List<Map<String, Object>> thresholds = this.queryThresholdValues();
		
		data = CimissInterfaceUtil.getDataByJSON(queryMethd, params);
		//data = "{\"returnCode\":\"0\",\"returnMessage\":\"Query Succeed\",\"rowCount\":\"3\",\"colCount\":\"9\",\"requestParams\":\"dataCode=SURF_CHN_MUL_HOR&timeRange=(20151221000000,20151222000000]&orderBy=Station_Id_C:asc,Datetime:asc&limitCnt=100&adminCodes=54000&elements=city,Datetime,Station_Id_d,PRS,TEM,RHU,PRE,WIN_S_INST,WIN_D_INST\",\"requestTime\":\"2016-03-07 13:10:14\",\"responseTime\":\"2016-03-07 13:10:16\",\"takeTime\":\"1.303\",\"fieldNames\":\"地市 资料时间 区站号(数字) 气压 温度/气温 相对湿度 降水量 瞬时风速 瞬时风向\",\"fieldUnits\":\"- - - 百帕 摄氏度(℃) 百分率 毫米 米/秒 度\",\"DS\":[{\"city\":\"\",\"Datetime\":\"2015-12-22 00:00:00\",\"Station_Id_d\":\"658786\",\"PRS\":\"999999\",\"TEM\":\"2.7\",\"RHU\":\"999999\",\"PRE\":\"999998\",\"WIN_S_INST\":\"999999\",\"WIN_D_INST\":\"999999\"},{\"city\":\"\",\"Datetime\":\"2015-12-21 21:00:00\",\"Station_Id_d\":\"837157\",\"PRS\":\"999999\",\"TEM\":\"999999\",\"RHU\":\"999999\",\"PRE\":\"999999\",\"WIN_S_INST\":\"999999\",\"WIN_D_INST\":\"999999\"},{\"city\":\"\",\"Datetime\":\"2015-12-22 00:00:00\",\"Station_Id_d\":\"837157\",\"PRS\":\"999999\",\"TEM\":\"999999\",\"RHU\":\"999999\",\"PRE\":\"999999\",\"WIN_S_INST\":\"999999\",\"WIN_D_INST\":\"999999\"}]}";

		List<DynamicBean> l = new ArrayList<DynamicBean>();
		if (!StringUtils.isEmpty(data))
		{
			
			JSONObject jsonObj = JSON.parseObject(data);
			//int rowCount = jsonObj.getInteger("rowCount");
			//int colCount = jsonObj.getInteger("colCount");
			JSONArray arrs = jsonObj.getJSONArray("DS");
			for (Object object : arrs) {
			    DynamicBean bean = new DynamicBean();
				JSONObject node = (JSONObject)object;
				String city =  node.getString("city");
				String DataTime =  node.getString("Datetime");
				String Station_Id_d =  node.getString("Station_Id_d");
				String qy =  node.getString("PRS");
				String wd =  node.getString("TEM");
				String sd =  node.getString("RHU");
				String sl =  node.getString("PRE");
				String fs =  node.getString("WIN_S_INST");
				String fx =  node.getString("WIN_D_INST");
				String stationName =  node.getString("Station_name");
				bean.add("area", city);
				bean.add("time", DataTime);
				bean.add("zh", Station_Id_d);
				bean.add("zm", stationName);
				/*bean.add("wd", wd == null ? wd : compare(Double.parseDouble(wd)/10,"wd",thresholds));
				bean.add("sl", sl == null ? sl : compare(Double.parseDouble(sl)/10,"sl",thresholds));
				bean.add("fs", fs == null ? fs : compare(Double.parseDouble(fs)/10,"fs",thresholds));
				bean.add("fx", fx);
				bean.add("sd", sd == null ? sd : compare(Double.parseDouble(sd)/10,"sd",thresholds));
				bean.add("qy", qy == null ? qy : compare(Double.parseDouble(qy)/10,"qy",thresholds));*/
				 bean.add("wd", wd);
		                    bean.add("sl", sl);
		                    bean.add("fs", fs);
		                    bean.add("fx", fx);
		                    bean.add("sd", sd);
		                    bean.add("qy", qy);
				l.add(bean);
			}
			
		}
		return l;
	}
	public List<DynamicBean> getCimissDataReport(Date startTime,Date endTime,String stationCode)
	{
	    String data = null;
	    HashMap<String, String> params = new HashMap<String, String>();
	    // cimiss查询方法
	    String queryMethd = "getSurfEleByTimeRangeAndStaID";
	    
            
            if (StringUtils.isEmpty(stationCode))
            {
                queryMethd = "getSurfEleInRegionByTimeRange";
                params.put("adminCodes", "54000");
            }
            else
            {
                params.put("staIds", stationCode);
            }
	    
	    // 资料代码
	    params.put("dataCode", "SURF_CHN_MUL_HOR");
	    // 要素字段代码
	    params.put("elements", "Station_name,city,Datetime,Station_Id_d,PRS,TEM,RHU,PRE,WIN_S_INST,WIN_D_INST");
	    // 时间段
	    params.put("timeRange", "("+DateFormatUtil.formateDatetime(startTime,"yyyyMMddHHmmss")+","+DateFormatUtil.formateDatetime(endTime,"yyyyMMddHHmmss")+"]");
	    // 排序：按照站号,资料时间从小到大排序
	    params.put("orderBy", "Station_Id_C:asc,Datetime:asc");
	    
	    params.put("limitCnt", "1000");
	    // // 站台级别
	    // params.put("staLevels", s_type);
	    
	    
	    data = CimissInterfaceUtil.getDataByJSON(queryMethd, params);
	    //data = "{\"returnCode\":\"0\",\"returnMessage\":\"Query Succeed\",\"rowCount\":\"3\",\"colCount\":\"9\",\"requestParams\":\"dataCode=SURF_CHN_MUL_HOR&timeRange=(20151221000000,20151222000000]&orderBy=Station_Id_C:asc,Datetime:asc&limitCnt=100&adminCodes=54000&elements=city,Datetime,Station_Id_d,PRS,TEM,RHU,PRE,WIN_S_INST,WIN_D_INST\",\"requestTime\":\"2016-03-07 13:10:14\",\"responseTime\":\"2016-03-07 13:10:16\",\"takeTime\":\"1.303\",\"fieldNames\":\"地市 资料时间 区站号(数字) 气压 温度/气温 相对湿度 降水量 瞬时风速 瞬时风向\",\"fieldUnits\":\"- - - 百帕 摄氏度(℃) 百分率 毫米 米/秒 度\",\"DS\":[{\"city\":\"\",\"Datetime\":\"2015-12-22 00:00:00\",\"Station_Id_d\":\"658786\",\"PRS\":\"999999\",\"TEM\":\"2.7\",\"RHU\":\"999999\",\"PRE\":\"999998\",\"WIN_S_INST\":\"999999\",\"WIN_D_INST\":\"999999\"},{\"city\":\"\",\"Datetime\":\"2015-12-21 21:00:00\",\"Station_Id_d\":\"837157\",\"PRS\":\"999999\",\"TEM\":\"999999\",\"RHU\":\"999999\",\"PRE\":\"999999\",\"WIN_S_INST\":\"999999\",\"WIN_D_INST\":\"999999\"},{\"city\":\"\",\"Datetime\":\"2015-12-22 00:00:00\",\"Station_Id_d\":\"837157\",\"PRS\":\"999999\",\"TEM\":\"999999\",\"RHU\":\"999999\",\"PRE\":\"999999\",\"WIN_S_INST\":\"999999\",\"WIN_D_INST\":\"999999\"}]}";
	    
	    List<DynamicBean> l = new ArrayList<DynamicBean>();
	    if (!StringUtils.isEmpty(data))
	    {
	        
	        JSONObject jsonObj = JSON.parseObject(data);
	        //int rowCount = jsonObj.getInteger("rowCount");
	        //int colCount = jsonObj.getInteger("colCount");
	        JSONArray arrs = jsonObj.getJSONArray("DS");
	        for (Object object : arrs) {
	            DynamicBean bean = new DynamicBean();
	            JSONObject node = (JSONObject)object;
	            String city =  node.getString("city");
	            String DataTime =  node.getString("Datetime");
	            String Station_Id_d =  node.getString("Station_Id_d");
	            String qy =  node.getString("PRS");
	            String wd =  node.getString("TEM");
	            String sd =  node.getString("RHU");
	            String sl =  node.getString("PRE");
	            String fs =  node.getString("WIN_S_INST");
	            String fx =  node.getString("WIN_D_INST");
	            
	            String stationName =  node.getString("Station_name");
	            bean.add("area", city);
	            bean.add("time", DataTime);
	            bean.add("zh", Station_Id_d);
	            bean.add("zm", stationName);
	            bean.add("wd", wd);
	            bean.add("sl", sl);
	            bean.add("fs", fs);
	            bean.add("fx", fx);
	            bean.add("sd", sd);
	            bean.add("qy", qy);
	            l.add(bean);
	        }
	        
	    }
	    return l;
	}
	
    // private String compare(double value,String code,List<Map<String, Object>>
    // thresholds)
    // {
    // for (Map<String, Object> threshold : thresholds) {
    // if (code.equals(threshold.get("ELEMENTCODE")+""))
    // {
    // if (value > Integer.parseInt(threshold.get("ELEMENTVALUE")+""))
    // {
    // return "<font style='color:red;'>"+value+"</font>";
    // }
    // }
    // }
    // return value+"";
    // }
	
	
	/**
	 * 查询所有的站点信息
	 * @param type
	 * @return
	 */
	public List<TreeInfoBean> getListTree() {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from wmp_station_dic sd ") ;
		List<DynamicBean> areaList = queryClient.createSqlQuery(sql.toString()).list();
		List<TreeInfoBean> rootTreeList = new ArrayList<TreeInfoBean>();
		for (DynamicBean bean : areaList) {
		    	String id = bean.getValue("muncpl_id");
		    
			TreeInfoBean tempTreeBean = new TreeInfoBean();
			tempTreeBean.setId(id);
			tempTreeBean.setText(bean.getValue("muncpl"));
			tempTreeBean.setState("closed");
			if ("1".equals(id))
			{
			    tempTreeBean.setChecked(true);
			}
			
			List<DynamicBean> county = queryClient.createSqlQuery("select * from wmp_station_ct a where a.MUNCPL_ID = ?", id).list();
			// 如果行政区域下面没有网络设备
			if (county.isEmpty()) {
				continue;
			}
			List<TreeInfoBean> listTree = new ArrayList<TreeInfoBean>();
			for (DynamicBean dynamicBean : county) {
			    
			    	String cid = dynamicBean.getValue("id");
				TreeInfoBean tempBean = new TreeInfoBean();
				tempBean.setId(cid);
				tempBean.setText(dynamicBean.getValue("cname"));
				
				List<DynamicBean> deviceBeanList = queryClient.createSqlQuery("select * from wmp_station a where a.ct_id = ?", cid).list();
				//站点
				if (deviceBeanList.isEmpty()) {
					continue;
				}
				List<TreeInfoBean> stationTree = new ArrayList<TreeInfoBean>();
				for (DynamicBean b : deviceBeanList) {
					TreeInfoBean tb = new TreeInfoBean();
					tb.setId(b.getValue("iiiii"));
					tb.setText(b.getValue("sname")+b.getValue("iiiii"));
					stationTree.add(tb);
				}
				tempBean.setChildren(stationTree);
				
				listTree.add(tempBean);
			}
			tempTreeBean.setChildren(listTree);
			rootTreeList.add(tempTreeBean);
		}
		return rootTreeList;
	}

	/** 
	* @Title: getAttenAres 
	* @Description: 关注的区域
	* @param userId
	* @return   
	* Set<String>
	* @author lqy
	* @throws 
	*/
	public Set<String> getAttenAres(String userId) {
	    
	    Set<String> set = new HashSet<String>();
	    String sqlUserAtten = "select ATTENTION_AREA from T_SYSTEM_USER where USERID = ?";
		DynamicBean userAttenObj = queryClient.createSqlQuery(sqlUserAtten, userId).uniqueResult();
		String userAtten = userAttenObj.getValue("attention_area");
		if (userAtten != null && userAtten != "")
		{
			String[] attens = userAtten.split(",");
			if (attens != null && attens.length > 0)
			{
			    for (int i = 0; i < attens.length; i++) {
				set.add(attens[i]);
			    }
			}
		}
	    
	    return set;
	}


}
