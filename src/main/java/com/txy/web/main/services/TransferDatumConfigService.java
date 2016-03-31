package com.txy.web.main.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.txy.common.bean.PageList;
import com.txy.common.bean.TreeInfoBean;
import com.txy.common.constant.Constant;
import com.txy.common.orm.SqlBeanClient;
import com.txy.common.orm.query.impl.DynamicBean;
import com.txy.common.util.StringUtils;

/**
 * 资料传输流程配置
 * @author AFEI
 *
 */
@Service
public class TransferDatumConfigService {
	@Autowired
	private SqlBeanClient queryClient;

	/**
	 * 查询所有的站点信息
	 * @param type
	 * @return
	 */
	public List<TreeInfoBean> queryAreaStations(int type) {
		String sql = "select * from wmp_station_dic";
		List<DynamicBean> areaList = queryClient.createSqlQuery(sql).list();
		List<TreeInfoBean> rootTreeList = new ArrayList<TreeInfoBean>();
		for (DynamicBean bean : areaList) {
			TreeInfoBean tempTreeBean = new TreeInfoBean();
			String id = bean.getValue("muncpl_id");
			tempTreeBean.setId(id);
			tempTreeBean.setText(bean.getValue("muncpl"));
			tempTreeBean.setState("closed");
			List<DynamicBean> deviceBeanList = queryClient.createSqlQuery("select * from wmp_station a where a.MUNCPL_ID = ?", id).list();
			// 如果行政区域下面没有网络设备
			if (deviceBeanList.isEmpty()) {
				continue;
			}
			List<TreeInfoBean> listTree = new ArrayList<TreeInfoBean>();
			for (DynamicBean dynamicBean : deviceBeanList) {
				TreeInfoBean tempBean = new TreeInfoBean();
				tempBean.setId(dynamicBean.getValue("iiiii"));
				tempBean.setText(dynamicBean.getValue("sname"));
				listTree.add(tempBean);
			}
			tempTreeBean.setChildren(listTree);
			rootTreeList.add(tempTreeBean);
		}
		return rootTreeList;
	}

	
	/**
	 * 查询所有节点信息
	 * @return
	 */
	public List<TreeInfoBean> queryNodes() {
		String sql = "select * from wmp_station_dic";
		List<DynamicBean> areaList = queryClient.createSqlQuery(sql).list();
		List<TreeInfoBean> rootTreeList = new ArrayList<TreeInfoBean>();
		for (DynamicBean bean : areaList) {
			TreeInfoBean tempTreeBean = new TreeInfoBean();
			String id = bean.getValue("muncpl_id");
			tempTreeBean.setId(id);
			tempTreeBean.setText(bean.getValue("muncpl"));
			List<DynamicBean> nodeList = queryClient.createSqlQuery("select * from wmp_node_manage t where t.node_area = ?",id).list();
			// 如果行政区域下面没有网络设备
			if (nodeList.isEmpty()) {
				continue;
			}
			List<TreeInfoBean> listTree = new ArrayList<TreeInfoBean>();
			for (DynamicBean dynamicBean : nodeList) {
				TreeInfoBean tempBean = new TreeInfoBean();
				tempBean.setId(dynamicBean.getValue("id"));
				tempBean.setText(dynamicBean.getValue("node_name"));
				listTree.add(tempBean);
			}
			tempTreeBean.setChildren(listTree);
			rootTreeList.add(tempTreeBean);
		}
		return rootTreeList;
	}


	/**
	 * 保存站点和节点的关系信息
	 * @param leftSataion
	 * @param rigthNode
	 * @return
	 */
	public boolean savaSatationNodeRef(String leftSataion, String rigthNode) {
		String[] stationArray = leftSataion.split(",");
		String[] nodeArray = rigthNode.split(",");
		String sql = "select replace(wm_concat(distinct a.muncpl),',','-') from wmp_node_manage t left join wmp_station_dic a on t.node_area = a.muncpl_id where t.id in ("+rigthNode+")";
		String name = queryClient.createJdbcTemplate().queryForObject(sql, String.class);
		//保存节点与站点的关系信息
		String primkey = StringUtils.getPrimarykeyId();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("ID", primkey);
		param.put("ENABLE", 0);
		param.put("TRANSFER_NAME", name);
		param.put("CREATE_TIME", new Date());
		int result = queryClient.createExecuteQuery().insert("WMP_STATION_NODE_REF", param);
		for (int i = 0; i < nodeArray.length; i++) {
			String nodeId = nodeArray[i];
			for (int j = 0; j < stationArray.length; j++) {
				String station = stationArray[j];
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", Constant.COMMON_SEQ);
				map.put("STATION_ID", station);
				map.put("NODE_ID", nodeId);
				map.put("NODE_SORT", i);
				map.put("REF_ID", primkey);
				queryClient.createExecuteQuery().insert("WMP_STATION_NODE_REF_DETAIL", map);
			}
		}
		if(result != 0){
			return true;
		}
		return false;
	}


	/**
	 * 查询配置列表详情
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public PageList<DynamicBean> getTransferDatumConfigPage(int start,int pageSize) {
		String sql = "select * from wmp_station_node_ref t";
		PageList<DynamicBean> pageList = queryClient.createSqlQuery(sql).page(start, pageSize);
		List<DynamicBean> list = pageList.getData();
		String temp_sql = "select b.node_id,a.node_name, b.node_sort from (select node_id, node_sort from WMP_STATION_NODE_REF_DETAIL t where t.ref_id = ? group by node_sort,node_id order by node_sort ) b left join wmp_node_manage a on b.node_id =a.id ORDER BY b.node_sort asc";
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			DynamicBean dynamicBean = (DynamicBean) iterator.next();
			String id = dynamicBean.getValue("id");
			List<DynamicBean> nodeNameList = queryClient.createSqlQuery(temp_sql, id).list();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < nodeNameList.size(); i++) {
				DynamicBean tempBean = nodeNameList.get(i);
				if(i == nodeNameList.size()||i == 0){
					sb.append(tempBean.getValue("node_name"));
				}else{
					sb.append("---->"+tempBean.getValue("node_name"));
				}
			}
			dynamicBean.add("node_sort_name", sb.toString());
		}
		return pageList;
	}


	/**
	 * 获取预览的配置信息
	 * @param config_id
	 * @return
	 */
	public List<DynamicBean> getPreviewTransferConfig(String config_id) {
		String temp_sql = "select b.node_id,a.node_name, b.node_sort from (select node_id, node_sort from WMP_STATION_NODE_REF_DETAIL t where t.ref_id = ? group by node_sort,node_id order by node_sort ) b left join wmp_node_manage a on b.node_id =a.id ORDER BY b.node_sort asc";
		List<DynamicBean> nodeNameList = queryClient.createSqlQuery(temp_sql, config_id).list();
		return nodeNameList;
	}


	/**
	 * 撤销配置信息
	 * @param config_id
	 * @return
	 */
	public int revokeConfigInfo(String config_id) {
		int result = queryClient.createExecuteQuery().delete("WMP_STATION_NODE_REF", "id", config_id);
		if(result != 0){
			return queryClient.createExecuteQuery().delete("WMP_STATION_NODE_REF_DETAIL", "ref_id", config_id);
		}
		return 0;
	}
}
