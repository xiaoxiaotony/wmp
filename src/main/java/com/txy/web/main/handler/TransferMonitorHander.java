package com.txy.web.main.handler;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.txy.common.bean.PageList;
import com.txy.common.bean.TreeInfoBean;
import com.txy.common.constant.Constant;
import com.txy.common.exception.ServiceException;
import com.txy.common.orm.query.impl.DynamicBean;
import com.txy.tools.HttpClientUtil;
import com.txy.web.frame.proxy.AbstractHandler;
import com.txy.web.main.services.TransferMonitorService;

/**
 * 传输设备监控处理Handler
 * 
 * @author Administrator
 *
 */
@Controller
public class TransferMonitorHander extends AbstractHandler {

	@Autowired
	private TransferMonitorService transferMonitorService;

	/**
	 * 获取传输设备的列表
	 * 
	 * @return
	 */
	public PageList<DynamicBean> getTransferMonitor() {
		String ipaddress = getString("ipaddress");
		String area = getString("area");
		int start = getInt("page");
		int pageSize = getInt("rows");
		String type = getValue("type");
		PageList<DynamicBean> pageList = transferMonitorService.getTransferMonitor(start, pageSize, type, ipaddress, area);
		return pageList;
	}

	public List<DynamicBean> getDeviceTypes() {
		String dictCode = "device_type";
		List<DynamicBean> list = transferMonitorService.getDeviceTypes(dictCode);
		return list;
	}

	public DynamicBean getTransferMonitorById() {
		int id = getInt("id");
		DynamicBean bean = transferMonitorService.getTransferMonitorById(id);
		return bean;
	}

	/**
	 * 修改传输设备
	 * @return
	 */
	public String updateTransferDeviceInfo() {
		String id = getValue("id");
		String deviceaddress = getValue("deviceaddress");
		String areaId = getValue("areaId");
		String name = getValue("name");
		String ftplogpath = getValue("ftplogpath");
		String ip = getValue("ip");
		String OS = getValue("OS");
		String account = getValue("account");
		String password = getValue("password");
		String regularExpression = getValue("regularexpression");
		if (StringUtils.isNotEmpty(id)) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("area", areaId);
			map.put("ip", ip);
			map.put("os", OS);
			map.put("deviceaddress", deviceaddress);
			map.put("ftplogpath", ftplogpath);
			map.put("name", name);
			map.put("account", account);
			map.put("password", password);
			map.put("regularexpression", regularExpression);
			int result = transferMonitorService.updateTransferDeviceInfo(map, id);
			if (1 == result) {
				return "{success:true}";
			}
		}
		return "{success:false}";
	}

	/**
	 * 获取节点管理页面列表
	 * 
	 * @return
	 */
	public PageList<DynamicBean> getNodeManagePageList() {
		String searchKey = getString("searchKey");
		String area = getString("area");
		int start = getInt("page");
		int pageSize = getInt("rows");
		PageList<DynamicBean> pageList = transferMonitorService.getNodeManagePageList(searchKey, area, start, pageSize);
		return pageList;
	}

	/**
	 * 节点管理添加方法
	 * 
	 * @return
	 */
	public String addNodeManageInfo() {
		String node_name = getValue("node_name");
		String deviceids = getValue("deviceids");
		String nodeType = getValue("nodeType");
		String node_desc = getValue("node_desc");
		String node_area = getValue("node_area");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("ID", Constant.COMMON_SEQ);
		param.put("NODE_NAME", node_name);
		param.put("DEVIED_ID", deviceids);
		param.put("NODE_TYPE", nodeType);
		param.put("NODE_AREA", node_area);
		param.put("NODE_DESC", node_desc);
		param.put("CREATETIME", new Date());
		int result = transferMonitorService.addNodeManageInfof(param);
		if (result != 0) {
			return "true";
		}
		return "false";
	}

	/**
	 * 查询传输设备下拉选择
	 * 
	 * @return
	 */
	public List<Map<String, Object>> queryTransferDeviceList() {
		return transferMonitorService.queryTransferDeviceList();
	}

	/**
	 * 删除节点数据信息
	 * 
	 * @return
	 */
	public String deleteNodeManagerInfo() {
		String id = getValue("id");
		int result = transferMonitorService.deleteNodeManagerInfo(id);
		if (result != 0) {
			return "{success:true}";
		}
		return "{success:false}";
	}

	/**
	 * 查询单个节点数据
	 * 
	 * @return
	 */
	public DynamicBean getNodeManagerById() {
		String id = getValue("id");
		return transferMonitorService.getNodeManagerById(id);
	}

	/**
	 * 修改节点管理数据
	 * 
	 * @return
	 */
	public String updateNodeManageInfo() {
		String id = getValue("id");
		int result = transferMonitorService.deleteNodeManagerInfo(id);
		if (result != 0) {
			return "{success:true}";
		}
		return "{success:false}";
	}

	public String deleteTransferInfo() {
		String id = getValue("id");
		if (StringUtils.isNotEmpty(id)) {
			int result = transferMonitorService.deleteTransferInfo(id);
			if (result != 0) {
				return "{success:true}";
			}
		}
		return "{success:false}";
	}

	/**
	 * 添加传输设备信息
	 * 
	 * @return
	 */
	public String addTransferInfo() {
		String name = getValue("name");
		String deviceIp = getValue("deviceIp");
		String areaId = getValue("areaId");
		String typeId = getValue("typeId");
		String OS = getValue("OS");
		String ftplogpath = getValue("ftplogpath");
		String account = getValue("account");
		String password = getValue("password");
		String regularExpression = getValue("regularExpression");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ID", Constant.COMMON_SEQ);
		map.put("ftplogpath", ftplogpath);
		map.put("NAME", name);
		map.put("TYPE", 2);
		map.put("OS", OS);
		map.put("DEVICETYPE", typeId);
		map.put("CREATETIME", new Date());
		map.put("DEVICEADDRESS", deviceIp);
		map.put("AREA", areaId);
		map.put("IP", deviceIp);
		map.put("ACCOUNT", account);
		map.put("PASSWORD", password);
		map.put("REGULAREXPRESSION", regularExpression);
		int num = transferMonitorService.addTransferInfo(map);
		if (num == 1) {
			return "{success:true}";
		}
		return "{success:false}";
	}

	/**
	 * 根据行政区域的编码查询各个区域下面的节点信息
	 * 
	 * @return
	 */
	public TreeInfoBean getCenterNodeInfo() {
		String areaCode = getValue("areaCode");
		if (StringUtils.isEmpty(areaCode)) {
			areaCode = "540000";
		}
		return transferMonitorService.getCenterNodeInfo(areaCode);
	}

	/**
	 * 查询传输设备配置信息的json数据
	 * 
	 * @return
	 */
	public String getTransferDeviceConfigByAreaId() {
		String areaId = getPara("areaCode");
		if (StringUtils.isEmpty(areaId)) {
			areaId = "541000";
		}
		String result = transferMonitorService.getTransferDeviceConfigByAreaId(areaId);
		return result;
	}

	/**
	 * 按照区域保存传输设备的配置图信息
	 * 
	 * @return
	 */
	public String saveTransferDeviceConfigByAreaId() {
		String areaId = getPara("areaId");
		String dataJsonStr = getValue("data");
		if (StringUtils.isEmpty(areaId)) {
			throw new ServiceException("行政区域数据异常");
		}
		int temp = transferMonitorService.saveTransferDeviceConfigByAreaId(areaId, dataJsonStr);
		if (temp != 0) {
			return "{success:true}";
		}
		return "{success:false}";
	}

	/**
	 * 根据区域查询传输设备展示信息
	 * 
	 * @return
	 */
	public List<DynamicBean> getTransferDeviceConfigDetail() {
		String areaId = getValue("areaId");
		List<DynamicBean> list = transferMonitorService.getTransferDeviceConfigDetail(areaId);
		return list;
	}

	/**
	 * 根据区域查询传输设备展示信息
	 * 
	 * @return
	 */
	public Map<String, Object> getTransferDeviceConfigLineDetail() {
		String areaId = getValue("areaId");
		Map<String, Object> map = transferMonitorService.getTransferDeviceConfigLineDetail(areaId);
		return map;
	}
	
	/**
	 * 查询传输设备详情
	 * @return
	 */
	public DynamicBean getDeviceDetailInfo(){
		String location_node_id = getValue("location_node_id");
		return transferMonitorService.getDeviceDetailInfoByLocationNodeId(location_node_id);
	}
	
	
	/**
	 * 查询设备磁盘信息列表
	 * @return
	 */
	public List<DynamicBean> getDeviceDiskInfo(){
		String deviceId = getValue("deviceId");
		List<DynamicBean> list = transferMonitorService.getDeviceDiskInfo(deviceId);
		return list;
	}
	
	
	public PageList<DynamicBean> getDeviceFtpLog(){
		String deviceId = getValue("deviceId");
		int start = getInt("page");
		int pageSize = getInt("rows");
		PageList<DynamicBean> pageList = transferMonitorService.getDeviceFtpLog(deviceId,start,pageSize);
		return pageList;
	}
	
	/**
	 * 添加严重ftp日志文件是否存在
	 * @return
	 */
	public String checkFtpPath(){
		String checkFtpPath = getValue("ftpPath");
		String serverIp = getValue("serverIp");
		try {
			String url = Constant.propertiesMap.get("checkFtpPathURL").toString()+"?checkFtpPath="+checkFtpPath+"&serverIp="+serverIp;
			String result = HttpClientUtil.callHttpForGet(url);
			if(result.indexOf("true")>-1){
				return "{success:true}";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "{success:false}";
	}
	
	
	
	/**
	 * 查询
	 * @return
	 */
	public List<DynamicBean> getStationListByConfigId(){
		String configId = getValue("config_id");
		return transferMonitorService.getStationListByConfigId(configId);
	}
	public PageList<DynamicBean> getNodes(){
		String configId = getValue("config_id");
		int start = getInt("page");
		int pageSize = getInt("rows");
		return transferMonitorService.getNodes(configId,start,pageSize);
	}
	
	public PageList<DynamicBean> getAllStations(){
	    	int start = getInt("page");
		int pageSize = getInt("rows");
	    	String configId = getValue("config_id");
	    	PageList<DynamicBean> nochecked = transferMonitorService.getAllStations(configId,start,pageSize);
	    	 List<DynamicBean> list = getStationListByConfigId();
	    	 for (DynamicBean bean : nochecked.getData()) {
	    	     	for (DynamicBean select : list) {
			    if (bean.getMap().get("station_id") != null && bean.getMap().get("station_id").equals(select.getMap().get("station_id")))
			    {
				bean.add("isChecked", true);
				break;
			    }
			    else
			    {
				bean.add("isChecked", false);
			    }
			}
		}
		return nochecked;
	}
	
	public String saveUpdate()
	{
	    String stationIds = getValue("stationIds");
		String configId = getValue("configId");
		boolean flag = transferMonitorService.saveStationUpdate(stationIds,configId);
		if(flag){
			return "{success:true}";
		}
		return "{success:false}";
	}
	public String saveNodeUpdate()
	{
	    String nodeIds = getValue("nodeIds");
	    String configId = getValue("configId");
	    boolean flag = transferMonitorService.saveNodeUpdate(nodeIds,configId);
	    if(flag){
		return "{success:true}";
	    }
	    return "{success:false}";
	}
	
}
