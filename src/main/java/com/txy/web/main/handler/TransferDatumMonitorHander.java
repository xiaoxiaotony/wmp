package com.txy.web.main.handler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.txy.common.util.StringUtils;
import com.txy.web.common.util.SmbUtil;
import com.txy.web.frame.proxy.AbstractHandler;
import com.txy.web.main.services.TransferDatumMonitorService;

/**
 * 资料监控
 * @author hong
 *
 */
@Controller
public class TransferDatumMonitorHander extends AbstractHandler {
	@Autowired
	private TransferDatumMonitorService service;
	
	/**
	 * 得到资料类型和摘要数据
	 * @return
	 */
	public List<Map<String, Object>> getSYData(){
		try {
			String datetime = getString("datetime");
			String station_dic = getString("station_dic");
			if(StringUtils.isEmpty(datetime)) return null;
			datetime +=":00:00";
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sf.parse(datetime));
			calendar.add(Calendar.HOUR_OF_DAY, -7);
			String endTime = sf.format(calendar.getTime());
			calendar.add(Calendar.HOUR_OF_DAY, -1);
			String beginTime = sf.format(calendar.getTime());
//			String beginTime = "2016-1-18 6:00:00";
//			String endTime = "2016-1-18 7:00:00";
//			return service.querySY(beginTime, endTime,station_dic);
			List<Map<String, Object>> data  = service.mergeQS_timeOut(beginTime, endTime,station_dic);
			Collections.sort(data, new Comparator<Map<String, Object>>() {
				@Override
				public int compare(Map<String, Object> o1, Map<String, Object> o2) {
					if(o1.get("DES_SY") == null || o2.get("DES_SY") == null) return 0;
					Integer d1 =Integer.parseInt(o1.get("DES_SY").toString());
					Integer d2 = Integer.parseInt(o2.get("DES_SY").toString());
					if (d1 < d2 ) {
						return 1;
					} else if (d1 > d2) {
						return -1;
					} else {
						return 0;
					}
				}
			});
			return data;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 查询逾限和缺报详情
	 * @return
	 */
	public List<Map<String, Object>> getQYDate(){
		try {
			String datetime = getString("datetime");
			String station_dic = getString("station_dic");
			String station_type = getString("station_type");
			if(StringUtils.isEmpty(datetime)) return null;
			datetime +=":00:00";
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sf.parse(datetime));
			calendar.add(Calendar.HOUR_OF_DAY, -7);
			String endTime = sf.format(calendar.getTime());
			calendar.add(Calendar.HOUR_OF_DAY, -1);
			String beginTime = sf.format(calendar.getTime());
//			String beginTime = "2015/12/22 6:00:00";
//			String endTime = "2015/12/22 7:00:00";
			List<Map<String, Object>> data = service.queryQY_table(beginTime,endTime,station_dic,station_type);
			return data;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 */
	public List<Map<String, Object>> getST(){
		try {
			String datetime = getString("datetime");
			String station_dic = getString("station_dic");
			String check_type = getString("check_type");
			String dtype = getString("dtype");
			String ctype = getString("ctype");
			String station_type = getString("type");
			if(StringUtils.isEmpty(datetime)) return null;
			datetime +=":00:00";
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sf.parse(datetime));
			calendar.add(Calendar.HOUR_OF_DAY, -7);
			String endTime = sf.format(calendar.getTime());
			calendar.add(Calendar.HOUR_OF_DAY, -1);
			String beginTime = sf.format(calendar.getTime());
//			String beginTime = "2016-1-18 6:00:00";
//			String endTime = "2016-1-18 7:00:00";
			
			return service.queryST_table(beginTime, endTime, station_dic, check_type, dtype,ctype,station_type);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 得到所有条目
	 * @return
	 */
	public Map<String, Object> getALLCon(){
		try {
			String datetime = getString("datetime");
			String station_dic = getString("station_dic");
			String station_type = getString("station_type");
			if(StringUtils.isEmpty(datetime)) return null;
			datetime +=":00:00";
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sf.parse(datetime));
			calendar.add(Calendar.HOUR_OF_DAY, -7);
			String endTime = sf.format(calendar.getTime());
			calendar.add(Calendar.HOUR_OF_DAY, -1);
			String beginTime = sf.format(calendar.getTime());
//			String beginTime = "2016-1-18 6:00:00";
//			String endTime = "2016-1-18 7:00:00";
			return service.queryALLCon(beginTime, endTime, station_dic,station_type);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 
	 * 站点关联节点查询
	 * @return 
	 */
	public List<Map<String, Object>> getStationNode(){
//		String station = getString("station");
		String station = "U1802";
		return service.getStationNode(station);
	}
	
	/**
	 * 查询FTP日志
	 * @return 
	 */
	public List<Map<String, String>> getFTPName(){
//		String dev_id = getString("dev_id");
		String dev_id = "41";
		Map<String,Object> map = service.queryFTPPath(dev_id); //得到FTP路径
//		String ip = map.get("IP").toString();
		String ip = "192.168.2.99";
		Object ftp_log_path = map.get("FTPLOGPATH");
		if(ftp_log_path == null) return null;
		String pwd = "hong";
		String user = "administrator";
		String url = "smb://"+user+":"+pwd+"@"+ip+ftp_log_path+"/";
		return SmbUtil.getFileNames(url);
//		smb://administrator:hong@192.168.2.99/ftplog/log_db_20150722.txt		
	}
	
	/**
	 * 查询FTP日志内容
	 * @return 
	 */
	public String getFTPInfo(){
		String filePath = getString("filePath");
		return SmbUtil.getFileInfo(filePath);
//		smb://administrator:hong@192.168.2.99/ftplog/log_db_20150722.txt		
	}
	
}
