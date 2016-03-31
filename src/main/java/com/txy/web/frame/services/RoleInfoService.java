package com.txy.web.frame.services;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.googlecode.ehcache.annotations.TriggersRemove;
import com.txy.common.bean.PageList;
import com.txy.common.bean.RoleInfoBean;
import com.txy.common.orm.BaseService;
import com.txy.common.orm.SqlBeanClient;
import com.txy.common.orm.query.BeanQuery;
import com.txy.common.util.StringUtils;

/**
 * 角色处理服务
 * @author fei
 *
 */
@Service
public class RoleInfoService extends BaseService
{

	@Autowired
	private SqlBeanClient sqlBeanClient;

	public PageList<RoleInfoBean> queryRolePageList(String roleName, String status, int start, int pageSize)
	{
		BeanQuery<RoleInfoBean> bean = sqlBeanClient.createBeanQuery(RoleInfoBean.class);
		if(!StringUtils.isEmpty(roleName)){
			return bean.like("groupName", roleName).page(start, pageSize);
		}
		if(!StringUtils.isEmpty(status)){
			return bean.eq("status", status).page(start, pageSize);
		}
		if(!StringUtils.isEmpty(roleName)&&!StringUtils.isEmpty(status)){
			return bean.like("groupName", roleName).eq("status", status).page(start, pageSize);
		}
		return bean.page(start, pageSize);
	}

	/**
	 * 保存或修改角色
	 * @param roleInfo
	 * @return
	 */
	public int save(RoleInfoBean roleInfo)
	{
		return sqlBeanClient.createExecuteQuery().insert(roleInfo, false);
	}

	/**
	 * 删除角色
	 * @param roleId
	 * @return
	 */
	public int deleteRole(String roleId)
	{
		return sqlBeanClient.createExecuteQuery().delete(RoleInfoBean.class, roleId);
	}
	
	/**
	 * 根据用户查询角色
	 * @param userId
	 * @return
	 */
	public List<RoleInfoBean> getRoleListByUser(String userId)
	{
		List<RoleInfoBean> list = sqlBeanClient.createBeanQuery(RoleInfoBean.class).list();
		return list;
	}

	/**
	 * 查询角色信息
	 * @param roleId
	 * @return
	 */
	public RoleInfoBean queryRoleBeanInfo(String roleId)
	{
		return sqlBeanClient.createBeanQuery(RoleInfoBean.class).eq("roleId", roleId).uniqueResult();
	}

	/**
	 * 修改角色信息
	 * @param roleBean
	 */
	public void updateRole(RoleInfoBean roleBean)
	{
		sqlBeanClient.createExecuteQuery().update(roleBean);
	}

	public boolean isExsitsRoleMenu(String roleId)
	{
		String sql = "SELECT COUNT(*) FROM T_SYSTEM_GROUP_MENU T WHERE T.GROUPID = ?";
		return sqlBeanClient.createJdbcTemplate().queryForObject(sql, Integer.class, roleId) != 0;
	}

	@TriggersRemove(cacheName="mobileCache",removeAll=true)
	public void updateRoleMenuStatusByRoleId(String roleId)
	{
		String sql = "UPDATE T_SYSTEM_GROUP_MENU T SET T.STATUS = ? WHERE T.GROUPID = ?";
		sqlBeanClient.createJdbcTemplate().update(sql, 1, roleId);
	}

	@TriggersRemove(cacheName="mobileCache",removeAll=true)
	public void updateRoleMenuPermission(String roleId, String[] sourceId)
	{
		String queryExsitSql = "SELECT COUNT(*) FROM T_SYSTEM_GROUP_MENU T WHERE T.GROUPID =? AND T.MENU_ID = ?";
		String updateSql = "UPDATE T_SYSTEM_GROUP_MENU T SET T.STATUS = ? WHERE t.GROUPID =? and t.menu_id = ?";
		String insertSql = "INSERT INTO T_SYSTEM_GROUP_MENU(ID,GROUPID,MENU_ID,STATUS) VALUES(?,?,?,?)";
		for (int i = 0; i < sourceId.length; i++)
		{
			String menuId = sourceId[i];
			int exsitCount = sqlBeanClient.createJdbcTemplate().queryForObject(queryExsitSql, Integer.class, roleId,
					menuId);
			if (exsitCount != 0)
			{
				sqlBeanClient.createJdbcTemplate().update(updateSql, 0, roleId, menuId);
			}
			else
			{
				sqlBeanClient.createJdbcTemplate().update(insertSql, StringUtils.getPrimarykeyId(),roleId, menuId, 0);
			}
		}
	}

	@TriggersRemove(cacheName="mobileCache",removeAll=true)
	public void addRoleMenuList(String[] sourceId, String roleId)
	{
		List<Object[]> list = new LinkedList<>();
		for (int i = 0; i < sourceId.length; i++)
		{
			Object[] array = null;
			if(!roleId.startsWith("100")){
				array = new Object[] {StringUtils.getPrimarykeyId(), roleId, sourceId[i], 0, 1};
			}else{
				array = new Object[] {StringUtils.getPrimarykeyId(), roleId, sourceId[i], 0, 2};
			}
			list.add(array);
		}
		String sql = "INSERT INTO T_SYSTEM_GROUP_MENU(ID,GROUPID,MENU_ID,STATUS,TYPE) VALUES(?,?,?,?,?)";
		sqlBeanClient.createJdbcTemplate().batchUpdate(sql, list);
	}
	
}
