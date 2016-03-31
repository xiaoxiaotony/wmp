package com.txy.web.main.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.txy.common.bean.PageList;
import com.txy.common.orm.query.impl.DynamicBean;
import com.txy.web.frame.proxy.AbstractHandler;
import com.txy.web.main.services.DevicesInfoService;

/**
 * 所有监控设备维护
 * 
 * @author tony
 *
 */
@Controller
public class DevicesInfoHandler extends AbstractHandler {

	@Autowired
	private DevicesInfoService devicesInfoService;

	public PageList<DynamicBean> getPageList() {
		String searchKey = getValue("searchKey");
		String ipaddress = getValue("ipaddress");
		String type = getValue("type");
		int start = getInt("page");
		int pageSize = getInt("rows");
		return devicesInfoService.getDeviceInfoPageList(searchKey, ipaddress, type, start, pageSize);
	}
	
	
	/**
	 * 查询所有告警信息
	 * @return
	 */
	public PageList<DynamicBean> getAlarmInfoPageList(){
		String type = getValue("type");
		int start = getInt("page");
		int pageSize = getInt("rows");
		return devicesInfoService.getAlarmInfoPageList(type,start,pageSize);
	}
	
	
	/**
	 * 清空所有报警信息
	 */
	public String cleanAlarm(){
		int result = devicesInfoService.deleteAllAlarmInfo();
		if(0 != result){
			return "{success : true}";
		}
		return "{success : false}";
	}
}
