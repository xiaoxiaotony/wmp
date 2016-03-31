package com.txy.web.main.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.txy.common.bean.PageList;
import com.txy.common.orm.SqlBeanClient;
import com.txy.common.orm.query.impl.DynamicBean;

@Service
public class DevicesInfoService {

	@Autowired
	private SqlBeanClient queryClient;

	public PageList<DynamicBean> getDeviceInfoPageList(String searchKey, String ipaddress, String type, int start, int pageSize) {
		StringBuffer sql = new StringBuffer("SELECT t.*,a.muncpl FROM WMP_DEVICE t left join WMP_STATION_DIC a on a.MUNCPL_ID = t.area where 1 = 1");
		List<Object> args = new ArrayList<Object>();
		if (StringUtils.isNotEmpty(searchKey)) {
			sql.append(" and name like '%" + searchKey + "%'");
		}
		sql.append(" and (type = 1 or type = 3)");
		if (StringUtils.isNotEmpty(ipaddress)) {
			sql.append(" and ip = ?");
			args.add(ipaddress);
		}
		sql.append(" order by ip asc");
		return queryClient.createSqlQuery(sql.toString(), args.toArray()).page(start, pageSize);
	}

	/**
	 * 查询所有告警列表
	 * 
	 * @param type
	 * @param start
	 * @param pageSize
	 * @return
	 */
	public PageList<DynamicBean> getAlarmInfoPageList(String type, int start, int pageSize) {
		String sql = "select * from wmp_alarm_info t order by t.alarmtime desc,t.alarmlevel desc";
		return queryClient.createSqlQuery(sql).page(start, pageSize);
	}

	
	/**
	 * 清楚所有告警信息
	 * @return
	 */
	public int deleteAllAlarmInfo() {
		String sql = "delete from wmp_alarm_info";
		return queryClient.createJdbcTemplate().update(sql);
	}
}
