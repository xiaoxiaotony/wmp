package com.txy.web.main.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.txy.common.bean.AreaInfoBean;
import com.txy.common.bean.PageList;
import com.txy.common.bean.TreeInfoBean;
import com.txy.common.constant.Constant;
import com.txy.common.orm.SqlBeanClient;
import com.txy.common.orm.query.impl.DynamicBean;
import com.txy.common.util.StringUtils;

@Service
public class TransferMonitorService {
	@Autowired
	private SqlBeanClient queryClient;

	public PageList<DynamicBean> getTransferMonitor(int start, int pageSize, String type, String ipaddress, String area) {
		String sql = "select d.*,a.muncpl as areaname,t.dict_val_name as devicetypename from wmp_device d left join wmp_station_dic a on d.area = a.muncpl_id"
				+ " left join (select * from T_SYSTEM_DICT where dict_code = 'device_type') t on t.dict_value = d.devicetype where 1 = 1 ";
		if (!StringUtils.isEmpty(ipaddress)) {
			sql += " and d.ip =  '" + ipaddress + "'";
		}
		if (!StringUtils.isEmpty(area)) {
			sql += " and d.area =  '" + area + "'";
		}
		sql += " and d.type=2 ";
		PageList<DynamicBean> list = queryClient.createSqlQuery(sql).page(start, pageSize);
		return list;
	}

	public int updateTransferDeviceInfo(Map<String, Object> map, String id) {
		return queryClient.createExecuteQuery().update("wmp_device", map, "id", id);
	}

	public int deleteTransferInfo(String id) {
		return queryClient.createExecuteQuery().delete("WMP_DEVICE", "ID", id);
	}

	public int addTransferInfo(Map<String, Object> map) {
		return queryClient.createExecuteQuery().insert("WMP_DEVICE", map);
	}

	public DynamicBean getTransferMonitorById(int id) {
		String sql = "select d.*,a.muncpl as areaname from wmp_device d left join wmp_station_dic a on d.area = a.muncpl_id where 1 = 1 and d.id = " + id;
		return queryClient.createSqlQuery(sql).uniqueResult();
	}

	/**
	 * 查询节点管理页面列表
	 * 
	 * @param searchKey
	 * @param area
	 * @param start
	 * @param pageSize
	 * @return
	 */
	public PageList<DynamicBean> getNodeManagePageList(String searchKey, String area, int start, int pageSize) {
		StringBuffer sql = new StringBuffer(
				"select n.id,n.node_name,n.node_type,k.dict_val_name,n.devied_id,e.muncpl as areaname,n.node_area,n.node_desc from wmp_node_manage n left join wmp_device d on n.devied_id = d.id left join wmp_station_dic e on n.node_area = e.muncpl_id left join t_system_dict k on k.dict_value = n.node_type where 1 = 1 and k.dict_code ='node_type'");
		List<Object> args = new ArrayList<Object>();
		if (org.apache.commons.lang.StringUtils.isNotEmpty(searchKey)) {
			sql.append(" and n.node_name like '%" + searchKey + "%'");
		}
		if (org.apache.commons.lang.StringUtils.isNotEmpty(area)) {
			sql.append(" and n.node_area = ?");
			args.add(area);
		}
		PageList<DynamicBean> pageList = queryClient.createSqlQuery(sql.toString(), args.toArray()).page(start, pageSize);
		List<DynamicBean> list = pageList.getData();
		for (DynamicBean dynamicBean : list) {
			String device_id = dynamicBean.getValue("devied_id");
			String sqlStr = "select wm_concat(a.name) as name from wmp_device a where a.id in ("+device_id+")";
			String result = queryClient.createJdbcTemplate().queryForObject(sqlStr, String.class);
			dynamicBean.add("device_name", result);
		}
		return pageList;
	}

	/**
	 * 查询节点需要关联的传输设备
	 * 
	 * @return
	 */
	public List<Map<String, Object>> queryTransferDeviceList() {
		String sql = "select t.name,t.id from wmp_device t where t.type = 2";
		return queryClient.createJdbcTemplate().queryForList(sql);
	}

	public int addNodeManageInfof(Map<String, Object> param) {
		return queryClient.createExecuteQuery().insert("WMP_NODE_MANAGE", param);
	}

	/**
	 * 删除节点数据
	 * 
	 * @param id
	 * @return
	 */
	public int deleteNodeManagerInfo(String id) {
		return queryClient.createExecuteQuery().delete("WMP_NODE_MANAGE", "ID", id);
	}

	/**
	 * 查询单个节点管理的详细信息
	 * 
	 * @param id
	 * @return
	 */
	public DynamicBean getNodeManagerById(String id) {
		return queryClient.createSqlQuery("select * from WMP_NODE_MANAGE where id = ?", id).uniqueResult();
	}

	public List<DynamicBean> getDeviceTypes(String dictCode) {
		return queryClient.createSqlQuery("select * from T_SYSTEM_DICT where dict_code = ?", dictCode).list();
	}

	/**
	 * 根据行政区域查询传输设备配置信息
	 * 
	 * @param areaId
	 * @return
	 */
	public String getTransferDeviceConfigByAreaId(String areaId) {
		String sql = "select value from WMP_NETWORK_NODE_CONFIG where id = ?";
		DynamicBean resultBean = queryClient.createSqlQuery(sql, areaId).uniqueResult();
		if (null != resultBean) {
			return resultBean.getValue("value");
		}
		return "";
	}

	/**
	 * 查询根据区域配置传输设备节点的数据
	 * 
	 * @param areaCode
	 * @return
	 */
	public TreeInfoBean getCenterNodeInfo(String areaCode) {
		// 查询所有行政区域
		TreeInfoBean treeBean = new TreeInfoBean();
		List<TreeInfoBean> rootTreeList = new ArrayList<TreeInfoBean>();
		// 如果为540000 则查询全区
		if ("540000".equals(areaCode)) {
			String sql = "select * from wmp_station_dic where parentid = ?";
			List<AreaInfoBean> areaList = queryClient.createSqlQuery(AreaInfoBean.class, sql, areaCode).list();
			for (AreaInfoBean areaInfoBean : areaList) {
				TreeInfoBean tempTreeBean = new TreeInfoBean();
				String areaId = areaInfoBean.getId();
				tempTreeBean.setId(areaId);
				tempTreeBean.setText(areaInfoBean.getText());
				List<DynamicBean> deviceBeanList = queryClient.createSqlQuery("select * from wmp_device a where a.area = ? and type = 2", areaId).list();
				// 如果行政区域下面没有网络设备
				if (deviceBeanList.isEmpty()) {
					continue;
				}
				List<TreeInfoBean> listTree = new ArrayList<TreeInfoBean>();
				for (DynamicBean dynamicBean : deviceBeanList) {
					TreeInfoBean tempBean = new TreeInfoBean();
					tempBean.setId(dynamicBean.getValue("id"));
					tempBean.setText(dynamicBean.getValue("name"));
					listTree.add(tempBean);
				}
				tempTreeBean.setChildren(listTree);
				rootTreeList.add(tempTreeBean);
			}
		} else {
			TreeInfoBean tempTreeBean = new TreeInfoBean();
			List<DynamicBean> deviceBeanList = queryClient.createSqlQuery("select * from wmp_device a where a.area = ?  and type = 2", areaCode).list();
			// 如果行政区域下面没有网络设备
			String sql = "select * from wmp_station_dic where muncpl_id = ?";
			AreaInfoBean areanBean = queryClient.createSqlQuery(AreaInfoBean.class, sql, areaCode).uniqueResult();
			tempTreeBean.setId(areanBean.getId());
			tempTreeBean.setText(areanBean.getText());
			List<TreeInfoBean> listTree = new ArrayList<TreeInfoBean>();
			for (DynamicBean dynamicBean : deviceBeanList) {
				TreeInfoBean tempBean = new TreeInfoBean();
				tempBean.setId(dynamicBean.getValue("id"));
				tempBean.setText(dynamicBean.getValue("name"));
				listTree.add(tempBean);
			}
			tempTreeBean.setChildren(listTree);
			rootTreeList.add(tempTreeBean);
		}
		treeBean.setChildren(rootTreeList);
		return treeBean;
	}

	/**
	 * 按照区域保存传输设备的配置图信息
	 * 
	 * @param areaId
	 * @param dataJsonStr
	 * @return
	 */
	public int saveTransferDeviceConfigByAreaId(String areaId, String dataJsonStr) {
		JSONObject jsonObj = JSON.parseObject(dataJsonStr);
		queryClient.createExecuteQuery().delete("WMP_TRANSFER_CONFIG_DETAIL", "type", areaId);
		JSONObject pathsObj = jsonObj.getJSONObject("paths");
		Set<String> pathsSet = pathsObj.keySet();
		JSONObject statesObj = jsonObj.getJSONObject("states");
		Set<String> set = statesObj.keySet();
		for (String pathSet : pathsSet) {
			Map<String, Object> detailMap = new HashMap<String, Object>();
			detailMap.put("type", areaId);
			JSONObject tempPathObj = JSON.parseObject(pathsObj.get(pathSet).toString());
			for (String key : set) {
				JSONObject tempObj = JSON.parseObject(statesObj.get(key).toString());
				// 获取attr
				String fromNode = JSON.parseObject(tempObj.get("text").toString()).getString("text");
				if (key.equals(tempPathObj.getString("from"))) {
					detailMap.put("id", Constant.COMMON_SEQ);
					detailMap.put("FROM_OR_TO", "from");
					detailMap.put("NAME", fromNode);
					tempObj = JSON.parseObject(statesObj.get(tempPathObj.getString("from")).toString());
					String locationX = JSON.parseObject(tempObj.get("attr").toString()).getString("x");
					String locationY = JSON.parseObject(tempObj.get("attr").toString()).getString("y");
					detailMap.put("LOCATION_X", locationX);
					detailMap.put("LOCATION_Y", locationY);
					detailMap.put("LINE", pathSet);
					queryClient.createExecuteQuery().insert("WMP_TRANSFER_CONFIG_DETAIL", detailMap);
				}
				if (key.equals(tempPathObj.getString("to"))) {
					detailMap.put("id", Constant.COMMON_SEQ);
					detailMap.put("FROM_OR_TO", "to");
					detailMap.put("NAME", fromNode);
					tempObj = JSON.parseObject(statesObj.get(tempPathObj.getString("to")).toString());
					String locationX = JSON.parseObject(tempObj.get("attr").toString()).getString("x");
					String locationY = JSON.parseObject(tempObj.get("attr").toString()).getString("y");
					detailMap.put("LOCATION_X", locationX);
					detailMap.put("LOCATION_Y", locationY);
					detailMap.put("LINE", pathSet);
					queryClient.createExecuteQuery().insert("WMP_TRANSFER_CONFIG_DETAIL", detailMap);
				}
			}
		}
		queryClient.createExecuteQuery().delete("WMP_NETWORK_NODE_CONFIG", "id", areaId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", areaId);
		map.put("value", dataJsonStr);
		return queryClient.createExecuteQuery().insert("WMP_NETWORK_NODE_CONFIG", map);
	}

	/**
	 * 更具区域ID查询设备信息
	 * 
	 * @param areaId
	 * @return
	 */
	public List<DynamicBean> getTransferDeviceConfigDetail(String areaId) {
		String sql = "select min(ID)id,name,location_x,location_y  from WMP_TRANSFER_CONFIG_DETAIL where type=? group by name,location_x,location_y";
		return queryClient.createSqlQuery(sql, areaId).list();
	}

	/**
	 * 更具区域ID查询设备信息
	 * 
	 * @param areaId
	 * @return
	 */
	public Map<String, Object> getTransferDeviceConfigLineDetail(String areaId) {
		String lineSql = "select distinct line from WMP_TRANSFER_CONFIG_DETAIL where type=?";
		List<DynamicBean> lineList = queryClient.createSqlQuery(lineSql, areaId).list();
		String sql = "select id,from_or_to,name,location_x,location_y from WMP_TRANSFER_CONFIG_DETAIL where line = ? and type = ?";
		Map<String, Object> map = new HashMap<String, Object>();
		for (DynamicBean bean : lineList) {
			List<Map<String, Object>> idList = queryClient.createJdbcTemplate().queryForList(sql, bean.getValue("line"), areaId);
			map.put(bean.getValue("line"), idList);
		}
		return map;
	}

	/**
	 * 查询传输设备区域
	 * 
	 * @param deviceid
	 * @return
	 */
	public String getDeviceAreaByDeviceId(String deviceid) {
		String sql = "select area from WMP_DEVICE where id = ?";
		return queryClient.createJdbcTemplate().queryForObject(sql, String.class, deviceid);
	}

	/**
	 * 查询传输设备的基础详情
	 * 
	 * @param location_node_id
	 * @return
	 */
	public DynamicBean getDeviceDetailInfoByLocationNodeId(String location_node_id) {
		String sql = "select * from wmp_device b where b.name = (select t.name from wmp_transfer_config_detail t where t.id = ?)";
		return queryClient.createSqlQuery(sql, location_node_id).uniqueResult();
	}

	/**
	 * 查询设备磁盘信息
	 * 
	 * @param nodeName
	 * @return
	 */
	public List<DynamicBean> getDeviceDiskInfo(String deviceId) {
		String sql = "select *　from wmp_disk_info where serverid = ?";
		// 处理可用和使用率
		List<DynamicBean> list = queryClient.createSqlQuery(sql, deviceId).list();
		for (DynamicBean dynamicBean : list) {
			double freeSize = Double.valueOf(dynamicBean.getValue("totalsize")) - Double.valueOf(dynamicBean.getValue("usedsize"));
			dynamicBean.add("freeSize", freeSize);
			double usageRate = Math.round(Double.valueOf(dynamicBean.getValue("usedsize")) / Double.valueOf(dynamicBean.getValue("totalsize")) * 100);
			dynamicBean.add("usageRate", usageRate);
			if(StringUtils.isEmpty(dynamicBean.getValue("path"))){
				dynamicBean.add("path", dynamicBean.getValue("root"));
				dynamicBean.add("root", "");
			}
		}
		return list;
	}

	/**
	 * 查询传输设备的ftp日志
	 * 
	 * @param deviceId
	 * @return
	 */
	public PageList<DynamicBean> getDeviceFtpLog(String deviceId,int start, int pageSize) {
		String sql = "select * from wmp_ftp_log t where t.device_id = ?";
		return queryClient.createSqlQuery(sql,deviceId).page(start, pageSize);
	}

	public List<DynamicBean> getAllServerInfo() {
		String sql = "select t.ip,t.name,t.id,t.ftplogpath from wmp_device t where t.type =  2";
		return queryClient.createSqlQuery(sql).list();
	}

	/**
	 * 采集服务器磁盘信息
	 * @param map
	 */
	public void saveCaptureServerDiskInfo(int id,String path, Map<String, Object> map) {
		int result = queryClient.createSqlQuery("select * from WMP_DISK_INFO where SERVERID = ? and ROOT = ?", id, path).count();
		if(result != 0){
			Map<String, Object> conditMap = new HashMap<String, Object>();
			conditMap.put("ROOT", path);
			conditMap.put("SERVERID", id);
			queryClient.createExecuteQuery().update("WMP_DISK_INFO", map, conditMap);
		}else{
			map.put("ID", Constant.COMMON_SEQ);
			queryClient.createExecuteQuery().insert("WMP_DISK_INFO", map);
		}
		
	}

	public void saveFtpLogInfo(Map<String, Object> map) {
		map.put("ID", Constant.COMMON_SEQ);
		queryClient.createExecuteQuery().insert("WMP_FTP_LOG", map);
	}

	public List<DynamicBean> getStationListByConfigId(String configId) {
		String sql = "select a.station_id,s.sname from (select distinct(station_id) from wmp_station_node_ref_detail where ref_id = ?) a left join WMP_STATION s on a.station_id = s.iiiii";
		return queryClient.createSqlQuery(sql, configId).list();
	}

	public PageList<DynamicBean> getAllStations(String configId,int start, int pageSize) {
	    String sql = "select * from (select s.iiiii as station_id, s.iiiii as sid,s.sname from (select distinct(station_id) from wmp_station_node_ref_detail where ref_id = ?) a left join WMP_STATION s on a.station_id = s.iiiii union all select t.IIIII as station_id ,t.iiiii as sid,t.SNAME from WMP_STATION t where t.IIIII not in (select distinct(station_id)  as IIIII from wmp_station_node_ref_detail where ref_id = ?))";
	    return queryClient.createSqlQuery(sql,configId,configId).page(start, pageSize);
	}

	public boolean saveStationUpdate(String stationIds, String configId) {
	    String[] sids = stationIds.split(";");
	    String sql = "select s.node_id,s.node_sort from WMP_STATION_NODE_REF_DETAIL s where s.ref_id = ?   group by s.node_sort,node_id";
	    List<DynamicBean> nodes = queryClient.createSqlQuery(sql,configId).list();
	    
	    queryClient.createExecuteQuery().delete("WMP_STATION_NODE_REF_DETAIL", "ref_id", configId);
	    for (DynamicBean node : nodes) {
		for (String sid : sids) {
		    Map<String, Object> map = new HashMap<String, Object>();
		    map.put("id", Constant.COMMON_SEQ);
		    map.put("STATION_ID", sid);
		    map.put("NODE_ID", node.getMap().get("node_id"));
		    map.put("NODE_SORT", node.getMap().get("node_sort"));
		    map.put("REF_ID", configId);
		    queryClient.createExecuteQuery().insert("WMP_STATION_NODE_REF_DETAIL", map);
		}
	    }
	    return true;
	}

	public PageList<DynamicBean> getNodes(String configId,int start, int pageSize) {
		 String sql = "select t.node_name,n.node_id,n.node_sort from (select s.node_id,s.node_sort from WMP_STATION_NODE_REF_DETAIL s where s.ref_id = ?   group by s.node_sort,node_id) n left join  wmp_node_manage t on n.node_id=t.id order by n.node_sort asc";
		 PageList<DynamicBean> nodes = queryClient.createSqlQuery(sql,configId).page(start, pageSize);
		return nodes;
	}

	public boolean saveNodeUpdate(String nodeIds, String configId) {
	    String[] nids = nodeIds.split(";");
	    for (String node : nids) {
		String[] arrs = node.split(",");
		String nodeId = arrs[0];
		String nodeSort = arrs[1];
		queryClient.createJdbcTemplate().update("update WMP_STATION_NODE_REF_DETAIL set node_sort = ? where node_id = ? and ref_id = ?", nodeSort,nodeId,configId);
	    }
	    return true;
	}
}
