package com.txy.web.main.services;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.txy.common.bean.PageList;
import com.txy.common.orm.SqlBeanClient;
import com.txy.common.orm.query.impl.DynamicBean;

@Service
public class StationTypeService {
	
	@Autowired
	private SqlBeanClient queryClient;

	/**
	 * 查询站点信息
	 * @param start
	 * @param pageSize
	 * @return
	 */
	public PageList<DynamicBean> queryStationType(String searchKey,String area,String stationType,String ischeck,int start,int pageSize) {
	    StringBuffer sql = new StringBuffer("select t.iiiii,t.lo,t.la,t.stype_id,t.muncpl_id,t.sname,t.lo_n,t.la_n,t.muncpl,b.stype_name,t.ischeck,t.cname from WMP_STATION t left join wmp_station_dic a on a.muncpl_id = t.muncpl_id left join wmp_station_type b on b.stype_id = t.stype_id where 1 = 1");
	    if(StringUtils.isNotEmpty(searchKey)){
	    	sql.append(" and t.iiiii like'%" + searchKey + "%'");
	    }
	    if(StringUtils.isNotEmpty(area)){
	    	sql.append(" and (t.muncpl like '%" + area + "%' or t.cname like '%" + area + "%')");
	    }
	    if(StringUtils.isNotEmpty(stationType)){
	    	sql.append(" and t.stype_id = '" + stationType + "'");
	    }
	    if(StringUtils.isNotEmpty(ischeck)){
	    	sql.append(" and t.ISCHECK = " + ischeck);
	    }
		return queryClient.createSqlQuery(sql.toString()).page(start, pageSize);
	}

	/**
	 * 保存站点信息
	 * @param map
	 * @return
	 */
	public int saveStationInfo(Map<String, Object> map) {
		return queryClient.createExecuteQuery().insert("wmp_station", map);
	}

	
	/**
	 * 查询单站点的基础信息
	 * @param stationId
	 * @return
	 */
	public DynamicBean queryStationTypeId(String stationId) {
		String sql = "select * from wmp_station where IIIII = ?";
		return queryClient.createSqlQuery(sql, stationId).uniqueResult();
	}

	/**
	 * 修改站点信息
	 * @param map
	 * @param id
	 * @return
	 */
	public int updateStationInfo(Map<String, Object> map, String id) {
		return queryClient.createExecuteQuery().update("wmp_station", map, "IIIII", id);
	}
	
	/**
	 * 删除站点信息
	 * @param id
	 * @return
	 */
	public int deleteStationInfo(String id){
		return queryClient.createExecuteQuery().delete("wmp_station", "IIIII", id);
	}

	
}
