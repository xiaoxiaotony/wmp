package com.txy.web.main.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.txy.common.bean.PageList;
import com.txy.common.orm.query.impl.DynamicBean;
import com.txy.web.frame.proxy.AbstractHandler;
import com.txy.web.main.services.BaseDataConfigService;

@Controller
public class BaseDataConfigHander extends AbstractHandler
{
	
	@Autowired
	private BaseDataConfigService baseDataConfigService;
	
	/**
	 * 查询数据类型种类
	 * @return
	 */
	public PageList<DynamicBean> getPageList(){
		String type = getValue("type");
		String roleName = getValue("dictName");
		int start = getInt("page");
		int pageSize = getInt("rows");
		PageList<DynamicBean> pageList = baseDataConfigService.getPageList(roleName,type,start,pageSize);
		return pageList;
	}
	
	/**
	 * 查询基础数据配置项值
	 * @return
	 */
	public PageList<DynamicBean> getConfigPageList(){
		String code = getValue("code");
		int start = getInt("page");
		int pageSize = getInt("rows");
		return baseDataConfigService.getConfigPageList(code,start,pageSize);
	}
	
}
