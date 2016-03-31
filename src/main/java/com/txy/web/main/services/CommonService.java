package com.txy.web.main.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.txy.common.orm.SqlBeanClient;

@Service
public class CommonService {
	@Autowired
	private SqlBeanClient queryClient;
	
    /**
     * 根据用户名关联地区
     * @param userID
     * @return
     */
	public List<Map<String, Object>> queryStationDICByUser(String areas){
		String sql = "SELECT * FROM WMP_STATION_DIC where MUNCPL_ID in (" + areas + ")";
		return queryClient.createJdbcTemplate().queryForList(sql);
	}
	
	
	/**
	 * 保存告警信息
	 * @param map
	 */
	public void saveAlarmInfo(Map<String, Object> map){
		queryClient.createExecuteQuery().insert("WMP_ALARM_INFO", map);
	}

	/**
	 * 查询站号站名
	 * @param dic_id
	 * @return 
	 */
	public List<Map<String, Object>> queryStationAndName(String dic_id) {
		String sql = "SELECT  a.iiiii, a.iiiii || '(' || a.sname || ')'  AS stname FROM wmp_station a,wmp_station_dic b WHERE a.muncpl_id  = b.muncpl_id  AND a.muncpl_id = "+dic_id;
		return queryClient.createJdbcTemplate().queryForList(sql);
	}

	/**
	 * 根据站号查资料
	 * @return 
	 */
	public List<Map<String, Object>> queryDataType(String stations,String ismon) {
		String sql = "SELECT c.dtype || '_' || c.ctype as datatype, c.ctype_desc, c.dtype_desc FROM wmp_station a, tr_stationrcv_dic b, tr_datatype_dic c WHERE a.iiiii = b.iiiii AND b.ctype = c.ctype AND b.dtype = c.dtype AND b.mflag = 0 AND c.flage = 0 AND a.iiiii in ("+stations+") GROUP BY c.dtype, c.ctype, c.ctype_desc, c.dtype_desc";
		return queryClient.createJdbcTemplate().queryForList(sql);
	}
}
