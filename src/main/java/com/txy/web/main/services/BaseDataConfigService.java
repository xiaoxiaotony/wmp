package com.txy.web.main.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.txy.common.bean.PageList;
import com.txy.common.orm.SqlBeanClient;
import com.txy.common.orm.query.impl.DynamicBean;
import com.txy.common.util.StringUtils;

@Service
public class BaseDataConfigService
{

	@Autowired
	private SqlBeanClient queryClient;
	
	/**
	 * 查询基础数据配置左边面板列表
	 * @param dictName
	 * @param type
	 * @param start
	 * @param pageSize
	 * @return
	 */
	public PageList<DynamicBean> getPageList(String dictName, String type, int start,
			int pageSize)
	{
		StringBuffer sql = new StringBuffer("select distinct(t.dict_code),t.dict_type,t.dict_name,t.add_time from t_system_dict t");
		List<Object> args = new ArrayList<>();
		if(!StringUtils.isEmpty(dictName)){
			sql.append("and t.dict_name = ?");
			args.add(dictName);
		}
		return queryClient.createSqlQuery(sql.toString(), args.toArray()).page(start, pageSize);
	}

	/**
	 * 查询配置项值
	 * @param code
	 * @param start
	 * @param pageSize
	 * @return
	 */
	public PageList<DynamicBean> getConfigPageList(String code, int start, int pageSize)
	{
		String sql = "select * from T_SYSTEM_DICT t where t.dict_code = ?";
		return queryClient.createSqlQuery(sql, code).page(start, pageSize);
	}
	
	
}
