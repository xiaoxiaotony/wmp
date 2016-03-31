package com.txy.web.frame.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.txy.common.bean.PageList;
import com.txy.common.orm.query.impl.DynamicBean;
import com.txy.common.util.StringUtils;
import com.txy.web.common.bean.UserInfoBean;
import com.txy.web.frame.proxy.AbstractHandler;
import com.txy.web.frame.services.SystemInfoService;

@Controller
public class SystemInfoHandler extends AbstractHandler {

    @Autowired
    private SystemInfoService systemInfoService;

    private static final String ROOT_AREA_CODE = "540000";

    /**
     * 查询系统管理日志列表信息
     * 
     * @return
     */
    public PageList<DynamicBean> logInfoList() {
	String logType = getValue("logType");
	String logStatus = getValue("logStatus");
	String oper_user = getValue("oper_user");
	String startTime = getValue("startTime");
	String endTime = getValue("endTime");
	int start = getInt("page");
	int pageSize = getInt("rows");
	PageList<DynamicBean> pageLogList = systemInfoService.queryLogList(
		start, pageSize, logType, logStatus, oper_user, startTime,
		endTime);
	return pageLogList;
    }

    /**
     * 获取用户信息总数
     */
    public String getUserInfoMessageCount() {
	String userId = ((UserInfoBean) getSessionAttr("user")).getId();
	int count = systemInfoService.getUserInfoMessageCount(userId);
	return String.valueOf(count);
    }

    /**
     * 查询消息列表
     * 
     * @return
     */
    public PageList<DynamicBean> getMessageInfoList() {
	String flag = getString("readFlag");
	String status = getValue("status");
	int start = getInt("page");
	int pageSize = getInt("rows");
	PageList<DynamicBean> messagePageList = systemInfoService
		.getMessageInfoList(status, start, pageSize, flag);
	return messagePageList;
    }

    /**
     * 清空日志
     * 
     * @return
     */
    public String cleanLog() {
	this.systemInfoService.cleanLog();
	return "true";
    }

    /**
     * 查询字典表信息
     * 
     * @return
     */
    public List<Map<String, Object>> getDictListVal() {
	String dict_code = getPara("dict_code");
	List<Map<String, Object>> l_map = systemInfoService
		.getDictListVal(dict_code);
	if (!StringUtils.isEmpty(dict_code)
		&& !StringUtils.isEmpty(getPara("all"))
		&& getParaToBoolean("all")) {
	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put("selected", true);
	    map.put("dict_val_name", "所有");
	    map.put("dict_value", "");
	    l_map.add(map);
	}
	return l_map;
    }

    /**
     * 查设备类型
     * 
     * @return
     */
    public List<Map<String, Object>> getDictVal() {
	return systemInfoService.getDictVal();
    }

    /**
     * 查询某条消息的详细信息
     * 
     * @return
     */
    public DynamicBean getMessageDetailInfo() {
	String id = getString("id");
	return systemInfoService.getMessageDetailInfo(id);
    }

    /**
     * 情况已读消息
     * 
     * @return
     */
    public String clearReadMessage() {
	boolean flag = systemInfoService.clearReadMessage();
	if (flag) {
	    return "true";
	}
	return "false";
    }

    /**
     * 获取
     */
    public List<Map<String, Object>> getAllAreaList() {
	List<Map<String, Object>> l_map = systemInfoService
		.getAllAreaList(ROOT_AREA_CODE);
	if (!StringUtils.isEmpty(ROOT_AREA_CODE)
		&& !StringUtils.isEmpty(getPara("all"))
		&& getParaToBoolean("all")) {
	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put("selected", true);
	    map.put("MUNCPL", "所有");
	    map.put("MUNCPL_ID", "");
	    l_map.add(map);
	}
	return l_map;
    }

    public List<Map<String, Object>> getAreas() {

	Map<String, Object> map = new HashMap<String, Object>();
	map.put("MUNCPL", "所有");
	map.put("MUNCPL_ID", "-1");
	List<Map<String, Object>> l_map = new ArrayList<Map<String, Object>>();
	l_map.add(map);
	String userId = ((UserInfoBean) getSessionAttr("user")).getId();
	List<Map<String, Object>> list = systemInfoService.getAreas(userId);
	l_map.addAll(list);
	return l_map;
    }

    /**
     * 获取站点
     */
    public List<Map<String, Object>> getStations() {
	String area = getPara("area");
	Map<String, Object> map = new HashMap<String, Object>();
	map.put("SNAME", "所有");
	map.put("IIIII", "");
	List<Map<String, Object>> l_map = new ArrayList<Map<String, Object>>();
	l_map.add(map);
	List<Map<String, Object>> list = systemInfoService.getStations(area);
	l_map.addAll(list);
	return l_map;
    }

    /**
     * 查询站类型
     * 
     * @return
     */
    public List<Map<String, Object>> getStationType() {
	return systemInfoService.getStationType();
    }
}
