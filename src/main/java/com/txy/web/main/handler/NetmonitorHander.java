package com.txy.web.main.handler;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.txy.common.bean.PageList;
import com.txy.common.bean.TreeInfoBean;
import com.txy.common.constant.Constant;
import com.txy.common.orm.query.impl.DynamicBean;
import com.txy.tools.ExcelUtil;
import com.txy.web.common.bean.UserInfoBean;
import com.txy.web.frame.proxy.AbstractHandler;
import com.txy.web.main.services.NetmonitorService;

@Controller
public class NetmonitorHander extends AbstractHandler {

	@Autowired
	private NetmonitorService netmonitorService;
	
	private static final String type = "1";

	public List<DynamicBean> getDeviceTree() {
		return netmonitorService.queryDeviceTree();
	}

	/**
	 * 查询左边区域设备信息
	 * 
	 * @return
	 */
	public TreeInfoBean getNetworkTopArchitectureConfigleft() {
		return netmonitorService.queryDeviceLeftConfigTree(type);
	}

	/**
	 * 查询地面专网设备的运行统计情况
	 */
	public PageList<DynamicBean> getTroubleStatisticsPageList() {
		String device = getValue("device");
		String monthDate = getValue("monthDate");
		String area = getValue("area");
		String status = getValue("status");
		int start = getInt("page");
		int pageSize = getInt("rows");
		PageList<DynamicBean> pageList = netmonitorService.getTroubleStatisticsPageList(device, monthDate, area, status, start, pageSize);
		return pageList;
	}

	/**
	 * 添加网络设备信息
	 * 
	 * @return
	 */
	public String addNetWorkInfo() {
		String name = getValue("name");
		String deviceaddress = getValue("deviceaddress");
		String ip = getValue("ip");
		String areaId = getValue("areaId");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ID", Constant.COMMON_SEQ);
		map.put("NAME", name);
		map.put("TYPE", 1);
		map.put("CREATETIME", new Date());
		map.put("DEVICEADDRESS", deviceaddress);
		map.put("AREA", areaId);
		map.put("IP", ip);
		int num = netmonitorService.addNetWorkInfo(map);
		if (num == 1) {
			return "{success:true}";
		}
		return "{success:false}";
	}

	/**
	 * 删除网络设备信息
	 * 
	 * @return
	 */
	public String deleteNetWorkInfo() {
		String id = getValue("id");
		if (StringUtils.isNotEmpty(id)) {
			int result = netmonitorService.deleteNetWorkInfo(id);
			if (result != 0) {
				return "{success:true}";
			}
		}
		return "{success:false}";
	}
	
	
	/**
	 * 查询单个网络设备的信息
	 * @return
	 */
	public DynamicBean getNetworkInfo(){
		String id = getValue("id");
		if (StringUtils.isNotEmpty(id)) {
			return netmonitorService.queryNetworkInfoById(id);
		}
		return null;
	}
	
	/**
	 * 修改单个网络设备信息
	 * @return
	 */
	public String updateNetworkDeviceInfo(){
		String id = getValue("id");
		String deviceaddress = getValue("deviceaddresss");
		String areaId = getValue("areaId");
		String ip = getValue("ip");
		if (StringUtils.isNotEmpty(id)) {
			DynamicBean dynamicBean = netmonitorService.queryNetworkInfoById(id);
			Map<String, Object> map = dynamicBean.getMap();
			map.put("area", areaId);
			map.put("ip", ip);
			map.put("deviceaddresss", deviceaddress);
			int result = netmonitorService.updateNetworkDeviceInfo(map,id);
			if(1 == result){
				return "{success:true}"; 
			}
		}
		return "{success:false}";
	}
	
	
	/**
	 * 保存网络设备拓扑图配置信息
	 * @return
	 */
	public String saveNetworkConfig(){
		String dataJsonStr = getValue("data");
		System.out.println(dataJsonStr);
		//先保存json数据
		int num = netmonitorService.saveNetworkConfig(dataJsonStr);
		if(num != 0){
			return "{success:true}"; 
		}
		return "{success:false}";
	}
	
	/**
	 * 查询网络设备配置图拖拽数据
	 * @return
	 */
	public String getNetworkConfig(){
		String result = netmonitorService.getNetworkConfig();
		return result;
	}
	
	
	/**
	 * 查询拓扑图展示详情
	 * @return
	 */
	public List<DynamicBean> getNetworkNodeConfigDetail(){
		UserInfoBean user = getCurrentSessionUser();
		String areas = user.getAttentionArea();
		return netmonitorService.getNetworkNodeConfigDetail(areas);
	}
	
	/**
	 * 查询拓扑图连线信息
	 * @return
	 */
	public Map<String, Object> getNetWorkNodeConfigLineDetail(){
		Map<String, Object> map = netmonitorService.getNetWorkNodeConfigLineDetail();
		return map;
	}

	/**
	 * 查询异常设备详情
	 * @return
	 */
	public PageList<DynamicBean> getNetworkTroubleDetail(){
		String device_id = getValue("device_id");
		int start = getInt("page");
		int pageSize = getInt("rows");
		PageList<DynamicBean> pageList = netmonitorService.getNetworkTroubleDetail(device_id, start, pageSize);
		return pageList;
	}
	
	
	/**
	 * 设备状态统计数据导出
	 */
	public void exportExcel(){
		String device = getValue("device");
		String monthDate = getValue("monthDate");
		String area = getValue("area");
		String status = getValue("status");
		List<Map<String, Object>> data = netmonitorService.queryStatisticsList(device, monthDate, area, status);
		Map<String, String> columName = new HashMap<String, String>();
		columName.put("NAME", "设备名称 ");
		columName.put("monthDate", "时间 ");
		columName.put("AREANAME", "设备区域 ");
		columName.put("IP", "IP地址 ");
		columName.put("status", "状态");
		columName.put("NUM_COUNT", "次数");
		response.setContentType("application/binary;charset=ISO8859_1");  
        try  
        {  
            ServletOutputStream outputStream = response.getOutputStream();  
            String fileName = new String(("设备状态统计数据").getBytes(), "ISO8859_1");  
            response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xls");// 组装附件名称和格式  
            ExcelUtil.toExcel(outputStream, data, columName);
        }  
        catch (IOException e)  
        {  
            e.printStackTrace();  
        }  
	}
	
	/**
	 * ping数据接口
	 * @return
	 */
	public String pingNetworkInfo(){
		String ip = getValue("ip");
		String serverIp = Constant.propertiesMap.get("ServiceIp").toString();
		String resultStr = netmonitorService.pingNetworkInfo(serverIp,ip);
		return resultStr;
	}
	
	/**
	 * telentNetworkInfo 网络设备信息
	 * @return
	 */
	public String telentNetworkInfo(){
		String ip = getValue("ip");
		String port = getValue("port");
		String serverIp = Constant.propertiesMap.get("ServiceIp").toString();
		String resultStr = netmonitorService.telentNetworkInfo(serverIp,ip,port);
		return resultStr;
	}

}
