package com.txy.web.frame.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.txy.common.bean.PageList;
import com.txy.common.orm.BaseService;
import com.txy.common.orm.SqlBeanClient;
import com.txy.common.util.RSAUtils;
import com.txy.common.util.StringUtils;
import com.txy.tools.PropertiseUtil;
import com.txy.web.common.bean.UserInfoBean;
import com.txy.web.frame.base.SystemContainer;

@Service
public class UserInfoService extends BaseService
{
	
	@Autowired
	private SqlBeanClient queryClient;
	
	private PropertiseUtil properties = new PropertiseUtil("/properties/config.properties");
	
	/**
	 * 登陆
	 * 
	 * @param userName
	 * @param password
	 * @return
	 */
	public UserInfoBean login(String userName, String password)
	{
		UserInfoBean userInfo = queryClient.createBeanQuery(UserInfoBean.class).eq("loginName", userName)
				.eq("loginPassword", StringUtils.SHA256(new String(RSAUtils.decode(password)))).uniqueResult();
		return userInfo;
	}
	
	/**
	 * 获取用户被锁次数
	 * 
	 * @param userName
	 * @return
	 */
	public long getUserErrorCount(String userName)
	{
		String sql = "select errorcount from T_SYSTEM_USER where ACCOUNT = '" + userName + "'";
		List<Map<String, Object>> result = queryClient.createJdbcTemplate().queryForList(sql);
		if (result.isEmpty())
		{
			return 0l;
		}
		else
		{
			return Long.valueOf(result.get(0).get("errorcount").toString());
		}
	}
	
	/**
	 * 获取用户被锁时间
	 * 
	 * @param loginName
	 * @return
	 */
	public Date getLockDate(String loginName)
	{
		String sql = "SELECT LOCK_DATE FROM T_SYSTEM_USER WHERE ACCOUNT='" + loginName + "'";
		List<Map<String, Object>> list = queryClient.createJdbcTemplate().queryForList(sql);
		if (list.isEmpty())
		{
			return null;
		}
		return (Date) list.get(0).get("LOCK_DATE");
	}
	
	/**
	 * 更新用户被锁次数
	 * 
	 * @param userName
	 * @param num
	 */
	public void updateUserErrorCount(String userName, int num)
	{
		if (num == 0)
		{
			this.queryClient.createJdbcTemplate().update(
					"update T_SYSTEM_USER t set t.errorcount = 0 where ACCOUNT = '" + userName + "' and t.lock_date is null");
		}
		else
		{
			this.queryClient.createJdbcTemplate().update(
					"update T_SYSTEM_USER t set t.errorcount = t.errorcount+ 1 where ACCOUNT = '" + userName + "' and t.lock_date is null");
		}
	}
	
	/**
	 * 更新用户被锁时间和次数
	 * 
	 * @param userName
	 * @param date
	 */
	public void updateUserCountAndLockDate(String userName, Date date)
	{
		String sql = "update T_SYSTEM_USER  set lock_date = ? where ACCOUNT = ?";
		this.queryClient.createJdbcTemplate().update(sql, date, userName);
	}
	
	/**
	 * 查询用户信息
	 * 
	 * @param start
	 * @param pageSize
	 * @param type
	 * @param suberFlag
	 * @return
	 */
	public PageList<UserInfoBean> queryList(int start, int pageSize, String type, String suberFlag)
	{
		if (Boolean.parseBoolean(suberFlag))
		{
			
			String userId = "1";
			if(null != SystemContainer.getUserSession()){
				userId = SystemContainer.getUserSession().getId();
			}
			if (!"1".equals(userId))
			{
				return queryClient.createBeanQuery(UserInfoBean.class).setJoinBean("RoleInfoBean").eq("userType", type).eq("id", userId)
						.page(start, pageSize);
			}
		}
		return queryClient.createBeanQuery(UserInfoBean.class).setJoinBean("RoleInfoBean").eq("userType", type).page(start, pageSize);
	}
	
	/**
	 * 查询用户信息
	 * 
	 * @param start
	 * @param pageSize
	 * @return
	 */
	public PageList<UserInfoBean> queryList(int start, int pageSize, String linkName, String type, String suberFlag,String company)
	{
		String conlums[] = new String[] { "USERNAME", "ACCOUNT", "GROUP_NAME", "PHONE" };
		String values[] = new String[] { linkName, linkName, linkName, linkName, linkName };
		if (Boolean.parseBoolean(suberFlag))
		{
			String userId = "1";
			if(null != SystemContainer.getUserSession()){
				userId = SystemContainer.getUserSession().getId();
			}
			if (!"1".equals(userId))
			{
				return queryClient.createBeanQuery(UserInfoBean.class).setJoinBean("RoleInfoBean").eq("userType", type).orLike(conlums, values).orLike(new String[]{"company"}, new String[]{company})
						.eq("id", userId).page(start, pageSize);
			}
		}
		return queryClient.createBeanQuery(UserInfoBean.class).setJoinBean("RoleInfoBean").eq("userType", type).orLike(conlums, values).orLike(new String[]{"company"}, new String[]{company})
				.page(start, pageSize);
	}
	
	/**
	 * 查看登陆名是否重复
	 * 
	 * @param loginName
	 * @return
	 */
	public int checkLoginName(String loginName)
	{
		String sql = "SELECT COUNT(*) FROM T_SYSTEM_USER WHERE ACCOUNT = ?";
		return queryClient.createJdbcTemplate().queryForObject(sql, Integer.class, loginName);
	}
	
	/**
	 * 添加用户
	 * 
	 * @param userBean
	 * @return
	 */
	public int addUser(UserInfoBean userBean)
	{
		return queryClient.createExecuteQuery().insert(userBean, true);
	}
	
	/**
	 * 删除用户
	 * 
	 * @param id
	 * @return
	 */
	public int deleteUser(String id)
	{
		String sql = "DELETE from T_SYSTEM_USER t WHERE t.userid = ?";
		return queryClient.createJdbcTemplate().update(sql, id);
	}
	
	/**
	 * 根据id查用户
	 * 
	 * @param id
	 * @return
	 */
	public UserInfoBean getUserInfoBeanById(String id)
	{
		UserInfoBean bean = queryClient.createBeanQuery(UserInfoBean.class).eq("id", id).uniqueResult();
		if (bean.getUserType() == 0)
		{
			bean = queryClient.createBeanQuery(UserInfoBean.class).setJoinBean("RoleInfoBean").eq("id", id).uniqueResult();
		}
		return bean;
	}
	
	/**
	 * 更新用户信息
	 * 
	 * @param userInfo
	 * @return
	 */
	public int updateUserInfoBean(UserInfoBean userInfo)
	{
		return queryClient.createExecuteQuery().update(userInfo);
	}
	
	/**
	 * 批量删除
	 * 
	 * @param ids
	 * @return
	 */
	public int deleteUsers(String ids)
	{
		StringBuffer sql = new StringBuffer("DELETE FROM t_system_user t WHERE t.userid IN (");
		String id_arr[] = ids.split(",");
		List<String> agrs = new ArrayList<String>();
		for (int i = 0; i < id_arr.length; i++)
		{
			if (i == 0)
			{
				sql.append("?");
			}
			else
			{
				sql.append(",?");
			}
			agrs.add(id_arr[i]);
		}
		sql.append(")");
		return queryClient.createJdbcTemplate().update(sql.toString(), agrs.toArray());
	}

	/**
	 * 修改用户密码
	 * @param userId
	 * @param newPassword
	 * @return
	 */
	public boolean updatePwd(String userId, String newPassword)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("PASSWORD", newPassword);
		if(0 != queryClient.createExecuteQuery().update("T_SYSTEM_USER", map, "USERID", userId)){
			return true;
		}
		return false;
	}

	/**
	 * 重置密码
	 * @param id
	 * @return
	 */
	public int resetPwd(String id) {
		
		String sql = "update T_SYSTEM_USER t set t.PASSWORD = '"+StringUtils.SHA256(properties.getValue("reset_pwd"))+"' WHERE t.userid = ?";
		return queryClient.createJdbcTemplate().update(sql, id);
	}
	
	/**
	 * 下拉框数据填充
	 * @param is_now 是否加入班次（今天）
	 * @return 数据列表
	 */
	public List<UserInfoBean> queryByCheck(String is_now){
		
		List<UserInfoBean> userList=null;

		String sql="SELECT USERID,USERNAME FROM T_SYSTEM_USER";
		if(StringUtils.isEmpty(is_now)){
			userList=queryClient.createSqlQuery(UserInfoBean.class,sql).list();
		}else{
			sql=sql+" WHERE USERID IN (SELECT USERID FROM WMP_DUTY_USER WHERE DUTY_DATE=?)";
			
			String now_time=StringUtils.formartDate(new Date(),"yyyy-MM-dd")+" 00:00:00";
			
			userList=queryClient.createSqlQuery(UserInfoBean.class, sql,StringUtils.toDate(now_time)).list();
		}
		
		return userList;
	}
}
