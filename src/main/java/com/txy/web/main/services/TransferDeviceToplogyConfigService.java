package com.txy.web.main.services;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.txy.common.orm.SqlBeanClient;
import com.txy.common.orm.query.impl.DynamicBean;

@Service
public class TransferDeviceToplogyConfigService {
	@Autowired
	private SqlBeanClient queryClient;


	public List<DynamicBean> queryDeviceArea(String areas) {
		if(StringUtils.isEmpty(areas)){
			return queryClient.createSqlQuery("select * from wmp_station_dic where parentid = '540000'").list();
		}
	    return queryClient.createSqlQuery("select * from wmp_station_dic where parentid = '540000' and MUNCPL_ID in ("+areas+")").list();
	}


	public List<DynamicBean> queryAreaNodes(String areaId) {
	    return queryClient.createSqlQuery("select w.*,t.dict_val_name as nodetypename from WMP_NODE_MANAGE w left join (select * from t_system_dict where dict_code = 'node_type') t on w.node_type = t.dict_value where node_area = ?",areaId).list();
	}
}
