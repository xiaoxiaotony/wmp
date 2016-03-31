package com.txy.web.main.handler;

import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.txy.common.util.StringUtils;
import com.txy.tools.ExcelUtil;
import com.txy.web.common.bean.UserInfoBean;
import com.txy.web.frame.proxy.AbstractHandler;
import com.txy.web.main.services.TransferMonitorCountService;

@Controller
public class TransferMonitorCountHandler extends AbstractHandler {

	@Autowired
	private TransferMonitorCountService transferMonitorCountService;
	
	/**
	 * 查询日统计
	 * @return 
	 */
	public List<Map<String, Object>> getDataTypeDay(){
	     String station_ids = getString("station_ids");
	     String types = getString("types");
	     String sort = getString("sort");
	     if(station_ids == null || types == null) return null;
	     String ids [] = station_ids.split(",");
	     StringBuffer sb = new StringBuffer();
	     for (String string : ids) {
	    	 sb.append(",'"+string+"'");
	     }
	     sb.deleteCharAt(0);
	     String d_datetime = getString("d_datetime") + " 00:00:00";
	     return transferMonitorCountService.queryDataTypeDay(sb.toString(),types,d_datetime,sort);
	}
	
	
    public List<Map<String, Object>> getDataTypeTimes(){
	     String station_ids = getString("station_ids");
	     String types = getString("types");
	     String sort = getString("sort");
	     if(station_ids == null || types == null) return null;
	     String ids [] = station_ids.split(",");
	     StringBuffer sb = new StringBuffer();
	     for (String string : ids) {
	    	 sb.append(",'"+string+"'");
	     }
	     sb.deleteCharAt(0);
	     String year = getString("year");
	     String mon = getString("mon");
	     String day = getString("day");
	     mon = mon.length() > 1 ? mon : "0"+mon;
	     day = day.length() > 1 ? day : "0"+day;
	     String begin_time = year + "/"+ mon + "/" + day + " 00:00:00";
	     String end_time = "";
	     if("1".equals(day)){
	    	 end_time = year + "/" + mon + "/" + "11" + " 00:00:00";
	     }else if("11".equals(day)){
	    	 end_time = year + "/" + mon + "/" + "21" + " 00:00:00";
	     }else{
	    	 SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	    	 Calendar calendar = Calendar.getInstance();
	    	 try {
				calendar.setTime(sf.parse(begin_time));
	    	 } catch (ParseException e) {
				e.printStackTrace();
	    	 }
	    	 calendar.add(Calendar.MONTH, 1);
	    	 int end_mon = calendar.get(Calendar.MONTH)+1;
	    	 int end_year = calendar.get(Calendar.YEAR);
	    	 String mon_str = (end_mon+"").length() > 1 ? end_mon+"" : "0"+end_mon;
	    	 end_time = end_year + "/" + mon_str + "/" + "01" + " 00:00:00";
	     }
	     
	     return transferMonitorCountService.queryDataTypeTimes(sb.toString(),types,begin_time,end_time,sort);
    }
    
    /**
     * 统计月监控资料
     * @return
     */
    public List<Map<String, Object>> getDataTypeTimesMon(){
	     String station_ids = getString("station_ids");
	     String types = getString("types");
	     String sort = getString("sort");
	     if(station_ids == null || types == null) return null;
	     String ids [] = station_ids.split(",");
	     StringBuffer sb = new StringBuffer();
	     for (String string : ids) {
	    	 sb.append(",'"+string+"'");
	     }
	     sb.deleteCharAt(0);
	     String year = getString("year");
	     String mon = getString("mon");
	     mon = mon.length() > 1 ? mon : "0"+mon;
	     String begin_time = year + "/"+ mon + "/" + "01" + " 00:00:00";
    	 SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    	 Calendar calendar = Calendar.getInstance();
    	 try {
			calendar.setTime(sf.parse(begin_time));
    	 } catch (ParseException e) {
			e.printStackTrace();
    	 }
    	 calendar.add(Calendar.MONTH, 1);
//    	 int end_mon = calendar.get(Calendar.MONTH);
//    	 int end_year = 0;
//    	 if(end_mon == 1) end_year = calendar.get(Calendar.YEAR)+1;
//    	 else end_year = calendar.get(Calendar.YEAR);
//    	 String mon_str = (end_mon+"").length() > 1 ? end_mon+"" : "0"+end_mon;
//    	 String end_time = end_year + "/" + mon_str + "/" + "01" + " 00:00:00";
    	 String end_time = sf.format(calendar.getTime());
	     
	     return transferMonitorCountService.queryDataTypeTimes(sb.toString(),types,begin_time,end_time,sort);
   }
    
    /**
     * 统计年监控资料
     * @return
     */
    public List<Map<String, Object>> getDataTypeTimesYear(){
	     String station_ids = getString("station_ids");
	     String types = getString("types");
	     String sort = getString("sort");
	     if(station_ids == null || types == null) return null;
	     String ids [] = station_ids.split(",");
	     StringBuffer sb = new StringBuffer();
	     for (String string : ids) {
	    	 sb.append(",'"+string+"'");
	     }
	     sb.deleteCharAt(0);
	     String year = getString("year");
	     String begin_time = year + "/"+ "01" + "/" + "01" + " 00:00:00";
    	 SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    	 Calendar calendar = Calendar.getInstance();
    	 try {
			calendar.setTime(sf.parse(begin_time));
    	 } catch (ParseException e) {
			e.printStackTrace();
    	 }
    	 calendar.add(Calendar.YEAR, 1);
    	 int end_year = calendar.get(Calendar.YEAR);
    	 String end_time = end_year + "/" + "01" + "/" + "01" + " 00:00:00";
	     
	     return transferMonitorCountService.queryDataTypeTimes(sb.toString(),types,begin_time,end_time,sort);
   }
	
	/**
	 * 查询图表平均到报率
	 * 
	 */
	public List<Map<String, Object>> getAvgDaoBaolv(){
		 String station_ids = getString("station_ids");
		 String types = getString("types");
	     String ids [] = station_ids.split(",");
	     StringBuffer sb = new StringBuffer();
	     for (String string : ids) {
	    	 sb.append(",'"+string+"'");
	     }
	     sb.deleteCharAt(0);
	     String d_datetime = getString("d_datetime") + " 00:00:00";
		 return transferMonitorCountService.queryAvgDaoBaolv(sb.toString(),d_datetime,types);
	}
	
	
	/**
	 * 查询图表平均到报率时间段
	 * 
	 */
	public List<Map<String, Object>> getAvgDaoBaolvTimes(){
		 String station_ids = getString("station_ids");
		 String types = getString("types");
	     String ids [] = station_ids.split(",");
	     StringBuffer sb = new StringBuffer();
	     for (String string : ids) {
	    	 sb.append(",'"+string+"'");
	     }
	     sb.deleteCharAt(0);
	     String year = getString("year");
	     String mon = getString("mon");
	     String day = getString("day");
	     mon = mon.length() > 1 ? mon : "0"+mon;
	     day = day.length() > 1 ? day : "0"+day;
	     String begin_time = year + "/"+ mon + "/" + day + " 00:00:00";
	     String end_time = "";
	     if("1".equals(day)){
	    	 end_time = year + "/" + mon + "/" + "11" + " 00:00:00";
	     }else if("11".equals(day)){
	    	 end_time = year + "/" + mon + "/" + "21" + " 00:00:00";
	     }else{
	    	 SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	    	 Calendar calendar = Calendar.getInstance();
	    	 try {
				calendar.setTime(sf.parse(begin_time));
	    	 } catch (ParseException e) {
				e.printStackTrace();
	    	 }
	    	 calendar.add(Calendar.MONTH, 1);
	    	 int end_mon = calendar.get(Calendar.MONTH)+1;
	    	 int end_year = calendar.get(Calendar.YEAR);
	    	 String mon_str = (end_mon+"").length() > 1 ? end_mon+"" : "0"+end_mon;
	    	 end_time = end_year + "/" + mon_str + "/" + "01" + " 00:00:00";
	     }
		return transferMonitorCountService.queryAvgDaoBaolv(sb.toString(),begin_time,end_time,types);
	}
	
	/**
	 * 查询图表平均到报率时间段月
	 * 
	 */
	public List<Map<String, Object>> getAvgDaoBaolvTimesMon(){
		 String station_ids = getString("station_ids");
		 String types = getString("types");
	     String ids [] = station_ids.split(",");
	     StringBuffer sb = new StringBuffer();
	     for (String string : ids) {
	    	 sb.append(",'"+string+"'");
	     }
	     sb.deleteCharAt(0);
	     String year = getString("year");
	     String mon = getString("mon");
	     mon = mon.length() > 1 ? mon : "0"+mon;
	     String begin_time = year + "/"+ mon + "/" + "01" + " 00:00:00";
    	 SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    	 Calendar calendar = Calendar.getInstance();
    	 try {
			calendar.setTime(sf.parse(begin_time));
    	 } catch (ParseException e) {
			e.printStackTrace();
    	 }
    	 calendar.add(Calendar.MONTH, 1);

    	 String end_time = sf.format(calendar.getTime());
		
    	 return transferMonitorCountService.queryAvgDaoBaolv(sb.toString(),begin_time,end_time,types);
	}
	
	
	/**
	 * 查询图表平均到报率时间段年
	 * 
	 */
	public List<Map<String, Object>> getAvgDaoBaolvTimesYear(){
		 String station_ids = getString("station_ids");
		 String types = getString("types");
	     String ids [] = station_ids.split(",");
	     StringBuffer sb = new StringBuffer();
	     for (String string : ids) {
	    	 sb.append(",'"+string+"'");
	     }
	     sb.deleteCharAt(0);
	     String year = getString("year");
	     String begin_time = year + "/"+ "01" + "/" + "01" + " 00:00:00";
    	 SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    	 Calendar calendar = Calendar.getInstance();
    	 try {
			calendar.setTime(sf.parse(begin_time));
    	 } catch (ParseException e) {
			e.printStackTrace();
    	 }
    	 calendar.add(Calendar.YEAR, 1);
    	 int end_year = calendar.get(Calendar.YEAR);
    	 String end_time = end_year + "/" + "01" + "/" + "01" + " 00:00:00";
		
    	 return transferMonitorCountService.queryAvgDaoBaolv(sb.toString(),begin_time,end_time,types);
	}
	
	
	/**
	 * 得到年的下拉列表
	 */
	public List<Map<String, Object>> getYear(){
		return transferMonitorCountService.queryYear();
	}
	
	

	public void toExcel() {
		HttpServletResponse response = getResponse();
		String excleData = getString("excleData");
		String headerIndex = getString("headerIndex");
		String headText = getString("headText");
		String excleName = getString("excleName");
		
		JSONArray excle = JSONArray.fromObject(excleData);
		JSONArray args = JSONArray.fromObject(headerIndex);
		JSONArray titles = JSONArray.fromObject(headText);
		HSSFWorkbook workBook = ExcelUtil.toExcel(excle, args.toArray(), titles.toArray());	
		
		try {
			response.addHeader(
					"Content-Disposition",
					"attachment;filename=\""
							+ new String((excleName + ".xls")
									.getBytes("GBK"), "ISO8859_1") + "\"");
			OutputStream out = response.getOutputStream();
			workBook.write(out);
			out.flush();
			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * 查询区域和县两级菜单
	 * 
	 */
	public List<Map<String, Object>> getArae(){
		UserInfoBean user = getCurrentSessionUser();
		return transferMonitorCountService.queryStationDICByUser(user.getAttentionArea());
	}
	
	/**
	 * 根据县级区域ID查询类型
	 * 
	 */
	public List<Map<String, Object>> getDataType(){
		String dic_id = getString("dic_id");
		if(StringUtils.isNullOrEmpty(dic_id)) return null;
		return transferMonitorCountService.queryDataTypeByDic(dic_id);
	}
	
	/**
	 * 根据资料类型选择站点
	 * 
	 */
	public List<Map<String, Object>> getStationAndName(){
		String station_type = getString("station_type");
		String dic_id = getString("dic_id");
		if(StringUtils.isNullOrEmpty(station_type)) return null;
		return transferMonitorCountService.queryStationAndName(station_type,dic_id);
	}
	
	/**
	 * 根据站点时间资料类型查询缺失详情
	 * @return 
	 * dateType:1 日，2旬，3月，4年
	 */
	public Map<String, Object> getStationQueInfo(){
		 String station = getString("station");
		 String datetime = getString("datetime");
		 datetime = datetime.replace("-", "/");
		 if(datetime.length() < 12) datetime+=" 00:00:00";
		 String beginTime = "";
		 String endTime = "";
		 String dateType = getString("dateType");
		 SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		 Calendar calendar = null;
		 try {
		    calendar = Calendar.getInstance();
			calendar.setTime(sf.parse(datetime));
		 } catch (ParseException e) {
			e.printStackTrace();
		 }
		 
		 if("1".equals(dateType)){
			 calendar.add(Calendar.DAY_OF_MONTH, 1);
			 endTime = sf.format(calendar.getTime());
			 beginTime = datetime;
		 }else if("2".equals(dateType)){
			calendar.add(Calendar.DAY_OF_MONTH, 10);
			endTime = sf.format(calendar.getTime());
			beginTime = datetime;
		 }else if("3".equals(dateType)){
				calendar.add(Calendar.MONTH, 1);
				endTime = sf.format(calendar.getTime());
				beginTime = datetime;
		 }else if("4".equals(dateType)){
				calendar.add(Calendar.YEAR, 1);
				endTime = sf.format(calendar.getTime());
				beginTime = datetime;
		 }
		 String dtype = getString("dtype");
		 String ctype = getString("ctype");
//		 return transferMonitorCountService.queryStationQueInfo("56312", "2016/1/17 00:00:00", "2016/1/18 00:00:00", "1", "UPAR", "UPAR");
		 return transferMonitorCountService.queryStationQueInfo(station, beginTime, endTime, dateType, dtype, ctype);
	}
	
}
