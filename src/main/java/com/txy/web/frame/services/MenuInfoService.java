package com.txy.web.frame.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.googlecode.ehcache.annotations.TriggersRemove;
import com.txy.common.bean.MenuInfoBean;
import com.txy.common.bean.PageList;
import com.txy.common.bean.TreeInfoBean;
import com.txy.common.orm.BaseService;
import com.txy.common.orm.SqlBeanClient;
import com.txy.common.util.StringUtils;

@Service
public class MenuInfoService extends BaseService
{
	
	@Autowired
	private SqlBeanClient queryClient;
	
	private static final Logger logger = Logger.getLogger(MenuInfoService.class);
	
	/**
	 * 查询Header菜单
	 * 
	 * @param node
	 * @param userId
	 * @return
	 */
//	@Cacheable(cacheName="mobileCache")
	public List<MenuInfoBean> queryHeaderMenuInfoList(String node, String userId)
	{
		List<MenuInfoBean> list = null;
		List<Object> args = new ArrayList<Object>();
		// 查询系统左边树数据
		String sql = " SELECT R.* FROM T_SYSTEM_MENU R LEFT JOIN T_SYSTEM_GROUP_MENU M ON R.MENU_ID = M.MENU_ID LEFT JOIN T_SYSTEM_USER E ON M.Groupid = E.GROUPID WHERE E.USERID = ? AND R.ENABLE = 0 AND  M.STATUS = 0 AND R.LEAF = 0 ORDER BY SORT";
		args.add(userId);
		list = this.queryClient.createSqlQuery(MenuInfoBean.class, sql.toString(), args.toArray()).list();
		return list;
	}
	
	/**
	 * 查询左边菜单
	 * 
	 * @param node
	 * @return
	 */
//	@Cacheable(cacheName="mobileCache")
	public MenuInfoBean getMenusLeftAll(String node,String userId)
	{
		MenuInfoBean rootBean = new MenuInfoBean();
		List<Object> args = new ArrayList<Object>();
		String sql = " SELECT * FROM T_SYSTEM_GROUP_MENU M LEFT JOIN T_SYSTEM_MENU R ON R.MENU_ID = M.MENU_ID LEFT JOIN T_SYSTEM_USER E ON M.Groupid = E.GROUPID WHERE E.USERID = ? AND R.ENABLE = 0 AND M.STATUS = 0 ORDER BY SORT";
		args.add(userId);
		List<MenuInfoBean> daoList = this.queryClient.createSqlQuery(MenuInfoBean.class, sql.toString(), args.toArray()).list();
		rootBean.setId(node);
		Map<String, String> tempMap = new HashMap<String, String>();
		findChild(daoList, rootBean, tempMap);
		tempMap.clear();
		return rootBean;
	}
	
	/**
	 * 重新组装菜单数据
	 * 
	 * @param list
	 * @param parentBean
	 * @param tempMap
	 */
	private void findChild(List<MenuInfoBean> list, MenuInfoBean parentBean, Map<String, String> tempMap)
	{
		List<MenuInfoBean> tempMapList = new ArrayList<MenuInfoBean>();
		for (MenuInfoBean bean : list)
		{
			if (parentBean.getId().equals(bean.getParentId()) && bean.getLeaf() < 3)
			{
				if (tempMap.get(bean.getId()) != null)
				{
					tempMapList.add(bean);
					continue;
				}
				parentBean.getChildren().add(bean);
				tempMap.put(bean.getId(), bean.getId());
			}
		}
		for (MenuInfoBean bean : parentBean.getChildren())
		{
			list.remove(bean);
			list.removeAll(tempMapList);
			findChild(list, bean, tempMap);
		}
	}
	
	/**
	 * 查询所有菜单集合
	 * @param userId
	 * @param roleId
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
//	@Cacheable(cacheName="mobileCache")
	public TreeInfoBean queryAllMenuByRoleId(String userId, String selectRoleId)
	{
		TreeInfoBean bean = new TreeInfoBean();
		bean.setId("1001");
		bean.setText("省级集约化平台");
		// 所有的menuInfoBean集合
		List<TreeInfoBean> daoList = new ArrayList<TreeInfoBean>();
		
		List<String> roleIdList = queryClient.createJdbcTemplate().queryForList("SELECT T.GROUPID FROM T_SYSTEM_USER T WHERE T.USERID = ?",
				String.class, userId);
		// 如果角色为空去查询组织机构
		if (StringUtils.isEmpty(roleIdList.get(0)))
		{
			roleIdList = queryClient.createJdbcTemplate().queryForList("SELECT T.MEMBER_ORGID FROM T_SYSTEM_USER T WHERE T.USERID = ?", String.class,
					userId);
		}
		Set set = new HashSet();
		for (String roleId : roleIdList)
		{
			// 除去重复的资源
			StringBuffer sql = new StringBuffer();
			List<Object> args = new ArrayList<Object>();
			if (!userId.equals("1"))
			{
				sql.append("SELECT M.* FROM T_SYSTEM_MENU M LEFT JOIN T_SYSTEM_GROUP_MENU R ON M.MENU_ID = R.MENU_ID WHERE R.GROUPID = ? AND ENABLE = 0");
				args.add(roleId);
			}
			else
			{
				sql.append("SELECT M.* FROM T_SYSTEM_MENU M WHERE ENABLE = 0 ");
			}
			List<MenuInfoBean> resultList = queryClient.createSqlQuery(MenuInfoBean.class, sql.toString(), args.toArray()).list();
			for (Iterator iter = resultList.iterator(); iter.hasNext();)
			{
				MenuInfoBean element = (MenuInfoBean) iter.next();
				if (set.add(element.getId()))
				{
					// 转换为树对象数据
					TreeInfoBean newBean = new TreeInfoBean();
					boolean reslut = this.findStatusRoleMenu(selectRoleId, element.getId());
					newBean.setChecked(reslut);
					newBean.setText(element.getName());
					newBean.setId(element.getId());
					newBean.setParentId(element.getParentId());
					newBean.setState("open");
					daoList.add(newBean);
				}
			}
		}
		Map<String, String> tempMap = new HashMap<String, String>();
		this.findTreeChild(daoList, bean, tempMap);
		tempMap.clear();
		return bean;
	}
	
	/**
	 * 查询所有菜单集合
	 * 
	 * @param userId
	 * @param roleId
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
//	@Cacheable(cacheName="mobileCache")
//	@TriggersRemove(cacheName="mallListCache",removeAll=true)
	public TreeInfoBean queryAllMenuByRoleId()
	{
		
		TreeInfoBean bean = new TreeInfoBean();
		bean.setId("1001");
		bean.setText("省级集约化平台");
		// 所有的menuInfoBean集合
		List<TreeInfoBean> daoList = new ArrayList<TreeInfoBean>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT M.* FROM T_SYSTEM_MENU M WHERE ENABLE = 0 ORDER BY SORT");
		List<MenuInfoBean> resultList = queryClient.createSqlQuery(MenuInfoBean.class, sql.toString()).list();
		for (Iterator iter = resultList.iterator(); iter.hasNext();)
		{
			MenuInfoBean element = (MenuInfoBean) iter.next();
			// 转换为树对象数据
			TreeInfoBean newBean = new TreeInfoBean();
			newBean.setText(element.getName());
			newBean.setId(element.getId());
			newBean.setParentId(element.getParentId());
			newBean.setState("open");
			daoList.add(newBean);
		}
		Map<String, String> tempMap = new HashMap<String, String>();
		this.findTreeChild(daoList, bean, tempMap);
		tempMap.clear();
		return bean;
	}
	
	/**
	 * 查找角色下所有菜单
	 * 
	 * @param selectRoleId
	 * @param menuId
	 * @return
	 */
	public boolean findStatusRoleMenu(String selectRoleId, String menuId)
	{
		String sql = "select t.status,r.leaf from T_SYSTEM_GROUP_MENU t left join T_SYSTEM_menu r on t.MENU_ID = r.MENU_ID where t.groupid = ? and t.menu_id = ?";
		List<Map<String, Object>> list = queryClient.createJdbcTemplate().queryForList(sql, selectRoleId, menuId);
		if (!list.isEmpty() && null != list.get(0).get("LEAF"))
		{
			if (Integer.parseInt(String.valueOf(list.get(0).get("STATUS"))) == 0 && 3 != Integer.parseInt(String.valueOf(list.get(0).get("LEAF")))
					&& selectRoleId.startsWith("2000"))
			{
				return false;
			}
			if (Integer.parseInt(String.valueOf(list.get(0).get("STATUS"))) == 0 && 0 != Integer.parseInt(String.valueOf(list.get(0).get("LEAF"))))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 查询tree
	 * 
	 * @param list
	 * @param parentBean
	 * @param tempMap
	 */
	private void findTreeChild(List<TreeInfoBean> list, TreeInfoBean parentBean, Map<String, String> tempMap)
	{
		List<TreeInfoBean> tempMapList = new ArrayList<TreeInfoBean>();
		for (TreeInfoBean bean : list)
		{
			if (parentBean.getId().equals(bean.getParentId()))
			{
				if (tempMap.get(bean.getId()) != null)
				{
					tempMapList.add(bean);
					continue;
				}
				parentBean.getChildren().add(bean);
				tempMap.put(bean.getId(), bean.getId());
			}
		}
		for (TreeInfoBean bean : parentBean.getChildren())
		{
			list.remove(bean);
			list.removeAll(tempMapList);
			findTreeChild(list, bean, tempMap);
		}
	}

	/**
	 * 根据父ID查询子菜单列表
	 * @param parentId
	 * @param start
	 * @param pageSize
	 * @return
	 */
	public PageList<MenuInfoBean> queryMenuPageRight(String parentId,int start,int pageSize)
	{
		String sql = "select * from T_SYSTEM_MENU t where t.parent_id = ?";
		PageList<MenuInfoBean> pageList = queryClient.createSqlQuery(MenuInfoBean.class, sql, parentId).page(start, pageSize); 
		if(pageList.getData().isEmpty()){
			sql = "select * from T_SYSTEM_MENU t where t.menu_id = ? ";
			pageList = queryClient.createSqlQuery(MenuInfoBean.class, sql, parentId).page(start, pageSize);
		}
		return pageList;
	}
	
	/**
	 * 添加菜单
	 */
	@TriggersRemove(cacheName="mobileCache",removeAll=true)
	public int addMenu(Map<String, Object> map){
		int sort = 0;
		try{
			sort = queryClient.createJdbcTemplate().queryForObject("select MAX(t.sort) from T_SYSTEM_MENU t WHERE t.parent_id = ?", Integer.class,new Object[]{map.get("PARENT_ID")});
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		sort++;
		map.put("SORT", sort);
		map.put("MENU_ID", StringUtils.getPrimarykeyId());
		map.put("type", 1);
		int num = queryClient.createExecuteQuery().insert("T_SYSTEM_MENU", map);
		return num;
	}

	/**
	 * 通过id查询菜单
	 * @param id
	 * @return
	 */
	public MenuInfoBean queryMenuById(String id){
		String sql = "SELECT * FROM T_SYSTEM_MENU t WHERE t.menu_id = ?";
		return queryClient.createSqlQuery(MenuInfoBean.class, sql, new Object[]{id}).uniqueResult();
	}
	
	/**
	 * 修改菜单
	 * @param map
	 * @return
	 */
	@TriggersRemove(cacheName="mobileCache",removeAll=true)
	public int updateMenu(Map<String, Object> map){
		return queryClient.createExecuteQuery().update("T_SYSTEM_MENU", map, "MENU_ID", map.get("menu_id"));
	}
	
	/**
	 * 删除菜单
	 * @param id
	 * @return
	 */
	@TriggersRemove(cacheName="mobileCache",removeAll=true)
	public int deleteMenu(String id){
	    if(queryChildCount(id) == 0){
	    	return queryClient.createExecuteQuery().delete("T_SYSTEM_MENU", "MENU_ID", id);
	    }
	    return 0;
	}
	
	/**
	 * 通过id查是否有子节点
	 * @return
	 */
	public int queryChildCount(String id){
		return queryClient.createJdbcTemplate().queryForObject("SELECT COUNT(1) FROM T_SYSTEM_MENU t WHERE t.parent_id = ?",new Object[]{id}, Integer.class);
	}
}
