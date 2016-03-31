package com.txy.web.main.services;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.txy.capture.task.Packet;
import com.txy.common.bean.AreaInfoBean;
import com.txy.common.bean.PageList;
import com.txy.common.bean.TreeInfoBean;
import com.txy.common.constant.Constant;
import com.txy.common.orm.SqlBeanClient;
import com.txy.common.orm.query.impl.DynamicBean;

/**
 * 拓扑图网络连接网状态
 * 
 * @author hong
 *
 */
@Service
public class NetmonitorService {

	@Autowired
	private SqlBeanClient queryClient;

	@Autowired
	private DefaultLobHandler lobHandler;

	public List<DynamicBean> queryDeviceTree() {

		String sql = "SELECT t.name AS text,t.ip as id FROM WMP_DEVICE t";
		return queryClient.createSqlQuery(sql).list();

	}

	public TreeInfoBean queryDeviceLeftConfigTree(String type) {
		// 查询所有行政区域
		String sql = "select * from wmp_station_dic where parentid = ?";
		List<AreaInfoBean> areaList = queryClient.createSqlQuery(AreaInfoBean.class, sql, "540000").list();
		TreeInfoBean treeBean = new TreeInfoBean();
		treeBean.setId("540000");
		treeBean.setText("地面专用网络列表");
		List<TreeInfoBean> rootTreeList = new ArrayList<TreeInfoBean>();
		for (AreaInfoBean areaInfoBean : areaList) {
			TreeInfoBean tempTreeBean = new TreeInfoBean();
			String id = areaInfoBean.getId();
			tempTreeBean.setId(id);
			tempTreeBean.setText(areaInfoBean.getText());
			List<DynamicBean> deviceBeanList = queryClient.createSqlQuery("select * from wmp_device a where a.area = ? and type = ?", id, type)
					.list();
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
		treeBean.setChildren(rootTreeList);
		return treeBean;
	}

	/**
	 * 查询网络运行情况统计
	 * 
	 * @param device
	 * @param monthDate
	 * @param area
	 * @param status
	 * @param start
	 * @param pageSize
	 * @return
	 */
	public PageList<DynamicBean> getTroubleStatisticsPageList(String device, String monthDate, String area, String status, int start, int pageSize) {
		StringBuffer sb = new StringBuffer(
				"select t.name, a.muncpl as areaname, t.ip, t.id, count(k.status) as num_count from wmp_device t left join wmp_station_dic a on t.area = a.muncpl_id left join (select to_status as status, toip from wmp_network_node_data where to_char(createdate, 'yyyy-mm') = ? ) k on t.ip = k.toip where 1 = 1");
		List<Object> tempArgs = new ArrayList<Object>();
		if (StringUtils.isEmpty(monthDate)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			monthDate = sdf.format(new Date());
		}
		tempArgs.add(monthDate);
		if (StringUtils.isNotEmpty(area)) {
			sb.append(" and t.area = ? ");
			tempArgs.add(area);
		}
		if (StringUtils.isNotEmpty(device)) {
			sb.append(" and t.name = ? ");
			tempArgs.add(device);
		}
		sb.append(" and t.type = 1 and t.name in (select c.name from wmp_transfer_config_detail c where c.type = 10000)  group by t.name, a.muncpl, t.ip, t.id order by num_count desc");
		PageList<DynamicBean> pageList = queryClient.createSqlQuery(sb.toString(), tempArgs.toArray()).page(start, pageSize);
		List<DynamicBean> list = pageList.getData();
		for (@SuppressWarnings("rawtypes")
		Iterator iterator = list.iterator(); iterator.hasNext();) {
			DynamicBean dynamicBean = (DynamicBean) iterator.next();
			int count = Integer.valueOf(dynamicBean.getValue("num_count"));
			if (count == 0) {
				dynamicBean.add("status", "正常");
				dynamicBean.add("count", 0);
			} else {
				dynamicBean.add("count", count);
				dynamicBean.add("status", "故障");
			}
			dynamicBean.add("monthDate", monthDate);
		}
		return pageList;
	}

	/**
	 * 添加网络设备信息
	 * 
	 * @param map
	 * @return
	 */
	public int addNetWorkInfo(Map<String, Object> map) {
		return queryClient.createExecuteQuery().insert("WMP_DEVICE", map);
	}

	/**
	 * 删除网络设备信息
	 * 
	 * @param id
	 * @return
	 */
	public int deleteNetWorkInfo(String id) {
		return queryClient.createExecuteQuery().delete("WMP_DEVICE", "ID", id);
	}

	/**
	 * 查询网络设备信息
	 * 
	 * @param id
	 * @return
	 */
	public DynamicBean queryNetworkInfoById(String id) {
		String sql = "select * from wmp_device a where id = ?";
		return queryClient.createSqlQuery(sql, id).uniqueResult();
	}

	/**
	 * 修改网络设备
	 * 
	 * @param map
	 * @param id
	 * @return
	 */
	public int updateNetworkDeviceInfo(Map<String, Object> map, String id) {
		return queryClient.createExecuteQuery().update("wmp_device", map, "id", id);
	}

	public int saveNetworkConfig(final String dataJsonStr) {
		JSONObject jsonObj = JSON.parseObject(dataJsonStr);
		queryClient.createExecuteQuery().delete("WMP_TRANSFER_CONFIG_DETAIL", "type", "10000");
		JSONObject pathsObj = jsonObj.getJSONObject("paths");
		Set<String> pathsSet = pathsObj.keySet();

		JSONObject statesObj = jsonObj.getJSONObject("states");
		Set<String> set = statesObj.keySet();
		for (String pathSet : pathsSet) {
			Map<String, Object> detailMap = new HashMap<String, Object>();
			detailMap.put("type", 10000);
			JSONObject tempPathObj = JSON.parseObject(pathsObj.get(pathSet).toString());
			for (String key : set) {
				JSONObject tempObj = JSON.parseObject(statesObj.get(key).toString());
				String fromNode = JSON.parseObject(tempObj.get("text").toString()).getString("text");
				System.out.println(fromNode + "-------------");
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
		queryClient.createExecuteQuery().delete("WMP_NETWORK_NODE_CONFIG", "id", "10000");
		int result = this.queryClient.createJdbcTemplate().execute("insert into WMP_NETWORK_NODE_CONFIG (id,value) values (?, ?)",
				new AbstractLobCreatingPreparedStatementCallback(this.lobHandler) {
					protected void setValues(PreparedStatement ps, LobCreator lobCreator) throws SQLException {
						ps.setString(1, "10000");
						lobCreator.setClobAsString(ps, 2, dataJsonStr);
					}
				});
		return result;
	}

	public String getNetworkConfig() {
		String sql = "select value from wmp_network_node_config where id = 10000";
		DynamicBean bean = queryClient.createSqlQuery(sql).uniqueResult();
		if (null != bean) {
			return bean.getValue("value");
		}
		return "";
	}

	public List<DynamicBean> getNetworkNodeConfigDetail(String areas) {
		String sql = " select m.id,m.name,m.location_x,m.location_y,m.to_status,c.ip from (select min(ID)id,a.name,a.location_x,a.location_y,d.to_status  from WMP_TRANSFER_CONFIG_DETAIL a left join (select b.name,c.to_status from wmp_device b left join WMP_NETWORK_NODE_DATA c on b.ip = c.toip where c.createdate between sysdate-(5/24/60) and sysdate) d on a.name = d.name where a.type = 10000 and d.to_status is not null group by a.name,a.location_x,a.location_y,d.to_status ) m left join wmp_device c on m.name = c.name where c.type = 1 AND c.area IN (select MUNCPL_ID from wmp_station_dic where parentid = '540000' and MUNCPL_ID in ("
				+ areas + "))";
		List<DynamicBean> list = queryClient.createSqlQuery(sql).list();
		if (list.isEmpty()) {
			list = queryClient
					.createSqlQuery(
							"select m.id,m.name,m.location_x,m.location_y,m.to_status,c.ip from (select min(id) id, a.name, a.location_x, a.location_y, '1' as to_status from wmp_transfer_config_detail a left join (select b.name, b.ip from wmp_device b left join wmp_network_node_data c on b.ip = c.toip) d on a.name = d.name where a.type = 10000 group by a.name, a.location_x, a.location_y) m left join wmp_device c on m.name = c.name where c.type = 1 AND c.area IN (select MUNCPL_ID from wmp_station_dic where parentid = '540000' and MUNCPL_ID in ("
									+ areas + "))").list();
		}
		return list;
	}

	/**
	 * 查询所有网络设备的信息
	 * 
	 * @return
	 */
	public List<DynamicBean> getAllNetworkNodeListInfo() {
		String sql = "select b.location_x, b.location_y,a.ip,a.name from wmp_device a left join WMP_TRANSFER_CONFIG_DETAIL b on b.name = a.name  where a.type = 1";
		return queryClient.createSqlQuery(sql).list();
	}

	public Map<String, Object> getNetWorkNodeConfigLineDetail() {
		String lineSql = "select distinct line from WMP_TRANSFER_CONFIG_DETAIL where type = 10000";
		List<DynamicBean> lineList = queryClient.createSqlQuery(lineSql).list();
		String sql = "select id,from_or_to,name,location_x,location_y from WMP_TRANSFER_CONFIG_DETAIL where line = ? and type = 10000";
		Map<String, Object> map = new HashMap<String, Object>();
		for (DynamicBean bean : lineList) {
			List<Map<String, Object>> idList = queryClient.createJdbcTemplate().queryForList(sql, bean.getValue("line"));
			map.put(bean.getValue("line"), idList);
		}
		return map;
	}

	public void saveCaptureNetworkInfo(Map<String, Object> map) {
		queryClient.createExecuteQuery().insert("WMP_NETWORK_NODE_DATA", map);
	}

	public List<DynamicBean> getNetworkConfigLocationKey(String ip) {
		String sql = "select  distinct b.name,b.location_x,b.location_y from wmp_device a left join wmp_transfer_config_detail b on a.name = b.name where a.ip = ?";
		List<DynamicBean> list = queryClient.createSqlQuery(sql, ip).list();
		return list;
	}

	public PageList<DynamicBean> getNetworkTroubleDetail(String device_id, int start, int pageSize) {
		String sql = "select to_char(t.createdate,'yyyy-mm-dd hh24:mi:ss') AS createdate,t.id from wmp_network_node_data t where t.toip = ?  AND t.to_status = 0 ORDER BY t.createdate DESC";
		PageList<DynamicBean> pageList = queryClient.createSqlQuery(sql, device_id).page(start, pageSize);
		List<DynamicBean> list = pageList.getData();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			for (int i = 0; i < list.size(); i++) {
				DynamicBean startBean = list.get(i);
				if (i == 0) {
					startBean.add("length", "");
					startBean.add("endTime", sdf.format(new Date()));
				} else {
					DynamicBean endBean = list.get(i - 1);
					Long length = sdf.parse(endBean.getValue("createdate")).getTime() - sdf.parse(startBean.getValue("createdate")).getTime();
					startBean.add("length", length / 1000 / 60 + "分钟");
					startBean.add("endTime", endBean.getValue("createdate"));
				}

			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return pageList;
	}

	/**
	 * 查询统计数据
	 * 
	 * @param device
	 * @param monthDate
	 * @param area
	 * @param status
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map<String, Object>> queryStatisticsList(String device, String monthDate, String area, String status) {
		StringBuffer sb = new StringBuffer(
				"select t.name, a.muncpl as areaname, t.ip, count(k.status) as num_count from wmp_device t left join wmp_station_dic a on t.area = a.muncpl_id left join (select to_status as status, toip from wmp_network_node_data where to_char(createdate, 'yyyy-mm') = ? ) k on t.ip = k.toip where 1 = 1");
		List<Object> tempArgs = new ArrayList<Object>();
		if (StringUtils.isEmpty(monthDate)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
			monthDate = sdf.format(new Date());
			tempArgs.add(monthDate);
		}
		if (StringUtils.isNotEmpty(area)) {
			sb.append(" and t.area = ? ");
			tempArgs.add(area);
		}
		if (StringUtils.isNotEmpty(device)) {
			sb.append(" and t.name = ? ");
			tempArgs.add(device);
		}
		sb.append(" and t.type = 1 and t.name in (select c.name from wmp_transfer_config_detail c where c.type = 10000)  group by t.name, a.muncpl, t.ip order by num_count desc");
		List<Map<String, Object>> list = queryClient.createJdbcTemplate().queryForList(sb.toString(), tempArgs.toArray());
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Map<String, Object> map = (Map<String, Object>) iterator.next();
			int count = Integer.valueOf(map.get("num_count").toString());
			if (count == 0) {
				map.put("status", "正常");
			} else {
				map.put("status", "故障");
			}
			map.put("monthDate", monthDate);
		}
		return list;
	}

	/**
	 * ping网络设备
	 * 
	 * @param serverIp
	 * @param ip
	 * @return
	 */
	public String pingNetworkInfo(String serverIp, String toIp) {
		InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 8000);
		// 链接超时
		String msg_id = "102";
		String resultStr = "";
		try {
			// 采集网络设备信息
			SocketChannel socketChannel = SocketChannel.open();
			socketChannel.socket().connect(inetSocketAddress, 8000);
			// 获取from开始的ip
			System.out.println("请求的from_ip" + serverIp);
			Map<String, Object> map = new HashMap<String, Object>();
			System.out.println("目标ip:" + toIp);
			map.put("ip", toIp);
			map.put("type", "ping");
			String data = JSONObject.toJSONString(map);
			resultStr = sendNetworkMessageToServer(socketChannel, msg_id, serverIp, data);
			socketChannel.close();
		} catch (IOException e) {
			return resultStr;
		}
		return resultStr;
	}

	/**
	 * 发送信息....并解析....
	 * 
	 * @param msg_id
	 * @param serverIp
	 * @param body
	 */
	public String sendNetworkMessageToServer(SocketChannel socketChannel, String msg_id, String serverIp, String body) {
		try {
			// 接受超时时间
			socketChannel.socket().setSoTimeout(3000);
			Packet pkt_obj = new Packet(Short.parseShort(msg_id), body);
			socketChannel.write(pkt_obj.getBuf());
			ByteBuffer byteBuffer = ByteBuffer.allocate(12);
			int readBytes = socketChannel.read(byteBuffer);
			int ret = -1;
			if (readBytes == 12) {
				ret = pkt_obj.ParseHead(byteBuffer);
			}
			if (ret > 0) {
				ByteBuffer body_buf = ByteBuffer.allocate(ret);
				readBytes = socketChannel.read(body_buf);
				if (readBytes == ret) {
					String recv = new String(body_buf.array(), "GB2312").trim();
					System.out.println(recv);
					// {"ping":[{"fromIp":"192.168.2.157","status":1,"toIp":"192.168.2.252"}]}
					if ("102".equals(msg_id)) {
						return recv;
					}
				}
			}
		} catch (UnsupportedOperationException e2) {
			System.out.println(e2.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String telentNetworkInfo(String serverIp, String ip, String port) {
		InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 8000);
		// 链接超时
		String msg_id = "104";
		String resultStr = "";
		try {
			// 采集网络设备信息
			SocketChannel socketChannel = SocketChannel.open();
			socketChannel.socket().connect(inetSocketAddress, 8000);
			// 获取from开始的ip
			System.out.println("请求的from_ip" + serverIp);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ip", ip);
			map.put("port", port);
			map.put("type", "telnet");
			String data = JSONObject.toJSONString(map);
			resultStr = sendTelenetNetworkMessageToServer(socketChannel, msg_id, serverIp, data);
			socketChannel.close();
		} catch (IOException e) {
			return resultStr;
		}
		return resultStr;
	}
	
	
	/**
	 * 发送TELENET....并解析....
	 * 
	 * @param msg_id
	 * @param serverIp
	 * @param body
	 */
	public String sendTelenetNetworkMessageToServer(SocketChannel socketChannel, String msg_id, String serverIp, String body) {
		try {
			// 接受超时时间
			socketChannel.socket().setSoTimeout(3000);
			Packet pkt_obj = new Packet(Short.parseShort(msg_id), body);
			socketChannel.write(pkt_obj.getBuf());
			ByteBuffer byteBuffer = ByteBuffer.allocate(12);
			int readBytes = socketChannel.read(byteBuffer);
			int ret = -1;
			if (readBytes == 12) {
				ret = pkt_obj.ParseHead(byteBuffer);
			}
			if (ret > 0) {
				ByteBuffer body_buf = ByteBuffer.allocate(ret);
				readBytes = socketChannel.read(body_buf);
				if (readBytes == ret) {
					String recv = new String(body_buf.array(), "GB2312").trim();
					System.out.println(recv);
					// {"ping":[{"fromIp":"192.168.2.157","status":1,"toIp":"192.168.2.252"}]}
					if ("104".equals(msg_id)) {
						return recv;
					}
				}
			}
		} catch (UnsupportedOperationException e2) {
			System.out.println(e2.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

}
