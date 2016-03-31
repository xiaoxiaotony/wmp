package com.txy.web.main.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.txy.common.bean.AreaInfoBean;
import com.txy.common.bean.PageList;
import com.txy.common.bean.TreeInfoBean;
import com.txy.common.constant.Constant;
import com.txy.common.orm.SqlBeanClient;
import com.txy.common.orm.query.impl.DynamicBean;
import com.txy.common.util.StringUtils;
import com.txy.web.common.util.ConfigUtil;

@Service
public class SystemBackInfoServie {

	@Autowired
	private SqlBeanClient queryClient;

	@SuppressWarnings("rawtypes")
	public AreaInfoBean getAreaListTree() {
		AreaInfoBean bean = new AreaInfoBean();
		bean.setId("540000");
		bean.setText("西藏自治区");
		// 所有的menuInfoBean集合
		List<AreaInfoBean> daoList = new ArrayList<AreaInfoBean>();
		StringBuffer sql = new StringBuffer();
		sql.append("select m.* from wmp_station_dic m where enable = 0 and m.parentid = 540000");
		List<AreaInfoBean> resultList = queryClient.createSqlQuery(AreaInfoBean.class, sql.toString()).list();
		for (Iterator iter = resultList.iterator(); iter.hasNext();) {
			AreaInfoBean element = (AreaInfoBean) iter.next();
			// 转换为树对象数据
			AreaInfoBean newBean = new AreaInfoBean();
			newBean.setText(element.getText());
			newBean.setId(element.getId());
			newBean.setParentId(element.getParentId());
			newBean.setState("open");
			daoList.add(newBean);
		}
		bean.setChildren(daoList);
		return bean;
	}

	/**
	 * 查询右侧行政区域
	 * @param parentId
	 * @param start
	 * @param pageSize
	 * @return
	 */
	public PageList<AreaInfoBean> queryAreaPageRight(String parentId, int start, int pageSize) {
		String sql = "SELECT * from WMP_STATION_CT t WHERE t.muncpl_id = ?";
		PageList<AreaInfoBean> pageList = null;
		if("540000".equals(parentId)){
			sql = "select * from wmp_station_dic t where t.parentid = ?";
			pageList = queryClient.createSqlQuery(AreaInfoBean.class, sql, parentId).page(start, pageSize); 
		}else{
			PageList<DynamicBean> dynamicBeanList = queryClient.createSqlQuery(sql, parentId).page(start, pageSize);
			List<DynamicBean> list = dynamicBeanList.getData();
			List<AreaInfoBean> areaList = new ArrayList<AreaInfoBean>();
			for (int i = 0; i < list.size(); i++) {
				AreaInfoBean area = new AreaInfoBean();
				area.setId(list.get(i).getValue("id"));
				area.setText(list.get(i).getValue("cname"));
				area.setCode(list.get(i).getValue("id"));
				areaList.add(area);
			}
			pageList = new PageList<AreaInfoBean>(dynamicBeanList.getTotal(), areaList);
		}
		
		if(pageList.getData().isEmpty()){
			sql = "select * from wmp_station_dic t where t.muncpl_id = ? ";
			pageList = queryClient.createSqlQuery(AreaInfoBean.class, sql, parentId).page(start, pageSize);
		}
		return pageList;
	}

	
	/**
	 * 查询地面实时观测的要素
	 * @param start
	 * @param pageSize
	 * @return
	 */
	public PageList<DynamicBean> getElementPageList(int start, int pageSize,String searchKey) {
		String sql = "select * from WMP_GROUND_DATA_THRESHOLD t";
		
		if(!StringUtils.isEmpty(searchKey)){
			sql+=" where t.ELEMENTNAME like '%"+searchKey+"%' or t.ELEMENTCODE like '%"+searchKey+"%' ";
		}
		return queryClient.createSqlQuery(sql).page(start, pageSize);
	}

	
	/**
	 * 查询资料类型维护
	 * @param start
	 * @param pageSize
	 * @return
	 */
	public PageList<DynamicBean> getFileMaintenancePageList(int start, int pageSize,String searchKey) {
		String sql = "select * from TR_DATATYPE_DIC";
		
		if(!StringUtils.isEmpty(searchKey)){
			sql += " WHERE dtype like '%"+searchKey+"%' or ctype like '%"+searchKey+"%' ";
		}
		return queryClient.createSqlQuery(sql).page(start, pageSize);
	}
	
	/**
	 * 删除要素数据
	 * @param ids
	 * @return
	 */
	public int delete(String ids){
		StringBuffer sql = new StringBuffer("DELETE FROM wmp_element_maintain t WHERE t.ID =?");
		
		return queryClient.createJdbcTemplate().update(sql.toString(), ids);
	}
	
	/**
	 * 删除数据
	 * @param ids
	 * @return
	 */
	public int deleteFile(String dtype,String ctype){
		StringBuffer sql = new StringBuffer("DELETE FROM TR_DATATYPE_DIC t WHERE t.DTYPE =? AND t.CTYPE=?");
		
		return queryClient.createJdbcTemplate().update(sql.toString(), dtype,ctype);
	}
	
	/**
	 * 删除资料类型
	 * @param ids
	 * @return
	 */
	public int deleteFile(String ids){
		StringBuffer sql = new StringBuffer("DELETE FROM TR_DATATYPE_DIC t WHERE t.DTYPE =?");
		
		return queryClient.createJdbcTemplate().update(sql.toString(), ids);
	}
	

	/**
	 * 查看编码是否重复
	 * 
	 * @param loginName
	 * @return
	 */
	public int checkCode(String code)
	{
		String sql = "SELECT COUNT(*) FROM wmp_element_maintain WHERE CODE = ?";
		return queryClient.createJdbcTemplate().queryForObject(sql, Integer.class, code);
	}
	
	/**
	 * 验证资料类型是否重复
	 * 
	 * @param loginName
	 * @return
	 */
	public int checkDtype(String Dtype)
	{
		String sql = "SELECT COUNT(*) FROM TR_DATATYPE_DIC WHERE DTYPE = ?";
		return queryClient.createJdbcTemplate().queryForObject(sql, Integer.class, Dtype);
	}
	
	/**
	 * 验证资料子类型是否重复
	 * 
	 * @param loginName
	 * @return
	 */
	public int checkCtype(String ctype)
	{
		String sql = "SELECT COUNT(*) FROM TR_DATATYPE_DIC WHERE CTYPE = ?";
		return queryClient.createJdbcTemplate().queryForObject(sql, Integer.class, ctype);
	}
	
	
	/**
	 * 添加要素数据
	 * @param name 要素名称
	 * @param code 要素编码
	 * @param description 要素描述
	 */
	@SuppressWarnings("unchecked")
	public int addElement(String name,String code,String description){
		
		@SuppressWarnings("rawtypes")
		Map map=new HashMap<String, Object>();
		
		map.put("ID", Constant.COMMON_SEQ);
		map.put("NAME", name);
		map.put("CODE", code);
		map.put("DESCRIPTION", description);
		
		return queryClient.createExecuteQuery().insert("WMP_ELEMENT_MAINTAIN", map);
	}
	
	/**
	 * 添加要素数据
	 * @param name 要素名称
	 * @param code 要素编码
	 * @param description 要素描述
	 */
	public int addFile(Map<String,Object> map){
		
		
		return queryClient.createExecuteQuery().insert("TR_DATATYPE_DIC", map);
	}
	
	/**
	 * 根据id查询要素数据
	 * @param id
	 * @returnqueryByDtype
	 */
	public DynamicBean queryById(String id){
		String sql="SELECT * FROM WMP_GROUND_DATA_THRESHOLD WHERE ID=?";
		return queryClient.createSqlQuery(sql, Integer.parseInt(id)).uniqueResult();
		
	}
	
	/**
	 * 根据资料类型查询数据
	 * @param dtype
	 * @return
	 */
	public DynamicBean queryByDtype(String dtype,String ctype){
		
		String sql="SELECT * FROM TR_DATATYPE_DIC WHERE DTYPE=? AND CTYPE=?";
		return queryClient.createSqlQuery(sql, dtype,ctype).uniqueResult();
		
	}
	
	/**
	 * 修改要素数据
	 */
	public int updateElement(Map<String, Object> map){
		
		return queryClient.createExecuteQuery().update("WMP_GROUND_DATA_THRESHOLD", map, "ID",map.get("id"));
	}
	
	/**
	 * 修改要素数据
	 */
	public int updateFile(Map<String, Object> map){
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("DTYPE", map.get("dtype"));
		conditionMap.put("CTYPE", map.get("ctype"));
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("FLAGE", map.get("flage"));
		paramMap.put("OUTTIME", map.get("outtime"));
		paramMap.put("MISSPG", map.get("misspg"));
		return queryClient.createExecuteQuery().update("TR_DATATYPE_DIC", paramMap, conditionMap);
	}

	
	public int addArea(Map<String, Object> map) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("ID", Constant.COMMON_SEQ);
		param.put("MUNCPL_ID", map.get("PARENT_ID"));
		param.put("CNAME", map.get("NAME"));
		return queryClient.createExecuteQuery().insert("WMP_STATION_CT", param);
	}

	public int deleteArea(String id) {
		return queryClient.createExecuteQuery().delete("WMP_STATION_CT", "ID", id);
	}

	/**
	 * 保存数据
	 * @param param
	 * @return
	 */
	public int addCustomDataInfo(Map<String, Object> param) {
		return queryClient.createExecuteQuery().insert("WMP_CUSTOM_QUERY", param);
	}

	/**
	 * 查询数据
	 * @param start
	 * @param pageSize
	 * @param searchKey
	 * @return
	 */
	public PageList<DynamicBean> getCustomDataQueryListData(int start,
			int pageSize, String searchKey, String userId) {
		StringBuffer sb = new StringBuffer();
		sb.append("select * from WMP_CUSTOM_QUERY where 1 = 1 and user_id = '" + userId + "'");
		List<Object> args = new ArrayList<Object>();
		if(!StringUtils.isEmpty(searchKey)){
			sb.append(" and NAME = ?");
			args.add(searchKey);
		}
		return queryClient.createSqlQuery(sb.toString(), args.toArray()).page(start, pageSize);
	}

	
	public int deleteCustomDataQuery(String id) {
		return queryClient.createExecuteQuery().delete("WMP_CUSTOM_QUERY", "id", id);
	}

	/**
	 * 查询单挑数据
	 * @param id
	 * @return
	 */
	public DynamicBean getTransferMonitorById(int id) {
		String sql = "select * from WMP_CUSTOM_QUERY where id = ?";
		return queryClient.createSqlQuery(sql, id).uniqueResult();
	}

	/**
	 * 修改数据
	 * @param param
	 * @param id
	 * @return
	 */
	public int updateCustomDataInfo(Map<String, Object> param, String id) {
		return queryClient.createExecuteQuery().update("WMP_CUSTOM_QUERY", param, "id", id);
	}

	/** 
	* @Title: queryCustomDataModels 
	* @Description: 查询已经配置的模块
	* @return   
	* List<DynamicBean>
	* @author lqy
	* @throws 
	*/
	public List<Map<String, Object>> queryCustomDataModels() {
	    String sql = "select MODEL_TYPE from WMP_CUSTOM_QUERY ";
	    List<Map<String, Object>> list = queryClient.createJdbcTemplate()
			.queryForList(sql);
	    return list;
	}

	public List<DynamicBean> queryWmpCustom() {
	    String sql = "select * from WMP_CUSTOM_QUERY ";
	    return queryClient.createSqlQuery(sql).list();
	}

	public DynamicBean queryWmpCustomByModelType(String modelType) {
	    String sql = "select * from WMP_CUSTOM_QUERY where model_type = ?";
	    return queryClient.createSqlQuery(sql, modelType).uniqueResult();
	}

	public DynamicBean queryCustomDataModelById(String id) {
	    String sql = "select * from WMP_CUSTOM_QUERY where id = ?";
	    return queryClient.createSqlQuery(sql, id).uniqueResult();
	}

	public PageList<DynamicBean> querySaveDatas(String tableName,
		int start, int pageSize) {
	    String sql = "select * from "+ tableName;
	    return queryClient.createSqlQuery(sql).page(start, pageSize);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map<String, Object>> querySaveDatas(String modelName,DynamicBean bean) {
	    String modelSql = ConfigUtil.getModelSql(modelName); 
	    if (modelSql == null || "".equals(modelSql))
	    {
		return null;
	    }
	    
	    if (bean != null && bean.getValue("AREA") != null && !"".equals(bean.getValue("AREA")))
	    {
	        String areas = bean.getValue("AREA");
	        if (areas != null && !"".equals(areas))
	        {
	            modelSql = "select * from ("+modelSql+") temp where temp.muncpl_id in ("+areas+")";
	        }
	    }
	    
	    List<Map<String, Object>> list = queryClient.createJdbcTemplate()
			.queryForList(modelSql);
	    
	    if ("wmp_device".equals(modelName))
	    {
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
		    Map<String, Object> map = (Map<String, Object>) iterator.next();
		    int count = Integer.valueOf(map.get("num_count").toString());
		    if (count == 0) {
			map.put("status", "正常");
		    } else {
			map.put("status", "故障");
		    }
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		    String monthDate = sdf.format(new Date());
		    map.put("monthDate", monthDate);
		}
	    }
	    
	    return list;
	}
	
	
	/**
         * 查询所有区域
         * @param type
         * @return
         */
        public List<TreeInfoBean> getAreaTree() {
                StringBuffer sql = new StringBuffer();
                sql.append("select * from wmp_station_dic sd ") ;
                List<DynamicBean> areaList = queryClient.createSqlQuery(sql.toString()).list();
                List<TreeInfoBean> rootTreeList = new ArrayList<TreeInfoBean>();
                for (DynamicBean bean : areaList) {
                        String id = bean.getValue("muncpl_id");
                        TreeInfoBean tempTreeBean = new TreeInfoBean();
                        tempTreeBean.setId(id);
                        tempTreeBean.setText(bean.getValue("muncpl"));
                        tempTreeBean.setChildren(null);
                        rootTreeList.add(tempTreeBean);
                }
                return rootTreeList;
        }

        
    public List<TreeInfoBean> getRowTree(String modelType)
    {
        Map<String, String> maps = ConfigUtil.getColumName(modelType);
        List<TreeInfoBean> rootTreeList = new ArrayList<TreeInfoBean>();
        for (String key : maps.keySet()) {
                TreeInfoBean tempTreeBean = new TreeInfoBean();
                tempTreeBean.setId(key);
                tempTreeBean.setText(maps.get(key));
                tempTreeBean.setChildren(null);
                rootTreeList.add(tempTreeBean);
        }
        return rootTreeList;
    }

}
