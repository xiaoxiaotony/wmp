package com.txy.web.main.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.txy.common.bean.PageList;
import com.txy.common.exception.ServiceException;
import com.txy.common.orm.query.impl.DynamicBean;
import com.txy.web.frame.proxy.AbstractHandler;
import com.txy.web.main.services.StationTypeService;

/**
 * 后台站点处理
 * @author AFEI
 *
 */
@Controller
public class StationTypeHander extends AbstractHandler {

	@Autowired
	private StationTypeService stationTypeService;
	
	/**
	 * 查询站类型
	 */
	public PageList<DynamicBean> getStationTypeInfo(){
		String searchKey = getValue("searchKey");
		String area = getValue("area");
		String stationType = getValue("stationType");
		String ischeck = getValue("ischeck");
		int start = getInt("page");
		int pageSize = getInt("rows");
	    return stationTypeService.queryStationType(searchKey,area,stationType,ischeck,start,pageSize);
	}
	
	/**
	 * 添加站点信息
	 * @return
	 */
	public String addStationInfo(){
		String iiiii = getValue("iiiii");
		String sname = getValue("sname");
		String muncpl = getValue("muncpl");
		String muncplText = getValue("muncplText");
		String stype_id = getValue("stype_id");
		String lo = getValue("lo");
		String la = getValue("la");
		String ischeck = getValue("ischeck");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("IIIII", iiiii);
		map.put("MUNCPL", muncplText);
		map.put("SNAME", sname);
		map.put("MUNCPL_ID", muncpl);
		map.put("STYPE_ID", stype_id);
		map.put("LO", lo);
		map.put("LA", la);
		map.put("ISCHECK", ischeck);
		int count = stationTypeService.saveStationInfo(map);
		if(count != 0){
			return "{success:true}";
		}
		return "{success:false}";
	}
	
	/**
	 * 修改用户信息
	 * 
	 * @param model
	 * @return
	 */
	public String upateStationInfo()
	{
		String stationId = getValue("id");
		DynamicBean bean = stationTypeService.queryStationTypeId(stationId);
		if (null == bean)
		{
			throw new ServiceException("参数传递异常");
		}
		String sname = getValue("sname");
		String muncpl = getValue("muncpl");
		String muncplText = getValue("muncplText");
		String stype_id = getValue("stype_id");
		String lo = getValue("lo");
		String la = getValue("la");
		String ischeck = getValue("ischeck");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("MUNCPL", muncplText);
		param.put("SNAME", sname);
		param.put("MUNCPL_ID", muncpl);
		param.put("STYPE_ID", stype_id);
		param.put("LO", lo);
		param.put("LA", la);
		param.put("ISCHECK", ischeck);
		int resultTemp = stationTypeService.updateStationInfo(param,stationId);
		if (0 == resultTemp)
		{
			return "{success : false}";
		}
		return "{success : true}";
	}
	
	/**
	 * 删除站点信息
	 * @return
	 */
	public String deleteStationInfo(){
		String statinId = getValue("stationId");
		int resutl = stationTypeService.deleteStationInfo(statinId);
		if(resutl != 0){
			return "{success : true}";
		}
		return "{success : false}";
	}
	
	/**
	 * 查询站点信息
	 * 
	 * @return
	 */
	public DynamicBean getDeviceInfoById()
	{
		String id = getString("id");
		DynamicBean bean = stationTypeService.queryStationTypeId(id); 
		return bean;
	}
	
	
	
}
