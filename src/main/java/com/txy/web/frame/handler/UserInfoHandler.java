package com.txy.web.frame.handler;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.txy.common.bean.BaseBean;
import com.txy.common.bean.PageList;
import com.txy.common.bean.RoleInfoBean;
import com.txy.common.exception.ServiceException;
import com.txy.common.exception.SystemException;
import com.txy.common.util.StringUtils;
import com.txy.web.common.bean.UserInfoBean;
import com.txy.web.frame.proxy.AbstractHandler;
import com.txy.web.frame.services.SystemInfoService;
import com.txy.web.frame.services.UserInfoService;

/**
 * 用户信息操作类
 * 
 * @author fei
 */
@Controller
public class UserInfoHandler extends AbstractHandler
{
	
	private static final Logger log = Logger.getLogger(UserInfoHandler.class);
	
	@Autowired
	private UserInfoService userInfoService;
	
	@Autowired
	private SystemInfoService systemInfoService;

	
	/**
	 * 用户登录
	 * 
	 * @param model
	 * @return
	 */
	public BaseBean login()
	{
		String userName = getValue("loginName");
		try
		{
			//对传输的url账号密码加密
			userName = java.net.URLDecoder.decode(userName,"UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		String password = getValue("loginPassword");
		log.info("用户正在登陆"+userName+"........");
		UserInfoBean userInfo = userInfoService.login(userName, password);
		long count = userInfoService.getUserErrorCount(userName);
		Date lockDate = userInfoService.getLockDate(userName);
		BaseBean baseBean = new BaseBean();
		baseBean.setData(userInfo);
		if("admin".equals(userName)){
			if(null == userInfo){
				throw new ServiceException("账号或者密码错误");
			}
		}else{
			if(null == userInfo){
				//记录账号错误次数
				if(count == 5){
					//修改最后锁定时间并且修改用户状态
					userInfoService.updateUserCountAndLockDate(userName,new Date());
					throw new ServiceException("失败超过5次，请10分钟后再登陆!");
				}else{
					userInfoService.updateUserErrorCount(userName,1);
				}
				throw new ServiceException("账号或者密码错误");
			}
			if(null != lockDate){
				long errorTime = new Date().getTime() - lockDate.getTime();
				if(errorTime < 10*60*1000){
					throw new ServiceException("失败超过5次，请10分钟后再登陆!");
				}else{
					//正确后更新用户状态和时间
					userInfoService.updateUserCountAndLockDate(userName,null);
				}
			}
			if(0 != count){
				userInfoService.updateUserErrorCount(userName,0);
			}
		}
		if(userInfo.getStatus() == 1){
			throw new ServiceException("账号已经禁用，请联系管理员");
		}
		setSessionAttr("user", userInfo);
		baseBean.setSuccess(true);
		log.info(userName + "登录成功");
		return baseBean;
	}
	
	/**
	 * 用户退出
	 * 
	 * @param model
	 * @return
	 */
	public String logout()
	{
		model.getRequest().getSession().invalidate();
		return "{success:true}";
	}
	
	/**
	 * 查询用户管理列表
	 * @param model
	 * @return
	 */
	public PageList<UserInfoBean> getUserInfoList()
		throws SystemException
	{
		String linkName = getValue("searchKey");
		String suberFlag = getString("suber");
		String company = getString("company");
		int start = getInt("page");
		int pageSize = getInt("rows");
		String type = getPara("type");
		//模糊查询
		PageList<UserInfoBean> pageList = null;
		if(!StringUtils.isEmpty(linkName)||!StringUtils.isEmpty(company)){
			//查询系统用户列表
			pageList = userInfoService.queryList(start, pageSize,linkName,type,suberFlag,company);
		}else{
			pageList = userInfoService.queryList(start, pageSize,type,suberFlag);
		}
		//查询系统用户列表
		pageList.setData(getListUserInfoBeanNew(pageList));
		return pageList;
	}
	
	/**
	 * 设备类型翻译
	 */
	private List<UserInfoBean> getListUserInfoBeanNew(PageList<UserInfoBean> pageList){
		List<UserInfoBean> bean = pageList.getData();
		List<UserInfoBean> bean_new = new ArrayList<UserInfoBean>();
		for (UserInfoBean userInfoBean : bean) {
			if(null != userInfoBean.getAttentionArea()){
				String areaName = systemInfoService.getAreaNameById(userInfoBean.getAttentionArea());
				userInfoBean.setOrgId(areaName);
			}
			bean_new.add(userInfoBean);
		}
		return bean_new;
	}
	
	/**
	 * 添加用户
	 * @return
	 */
	public String addUser(){
		String name = getValue("username");
		String account = getValue("account");
		String password = getValue("password");
		int status = getInt("status");
		String roleID = getValue("role");
		String phone = getValue("phone");
		String address = getValue("address");
		String areaId = getValue("areaId");
		String attentionArea = getValue("attentionArea");
		UserInfoBean user = new UserInfoBean();
		user.setId(StringUtils.getPrimarykeyId());
		user.setOrgId(areaId);
		user.setName(name);
		user.setLoginName(account);
		user.setLoginPassword(StringUtils.SHA256(password));
		user.setStatus(status);
		user.setAddress(address);
		user.setAddTime(new Date());
		
		String attenionAreaId = "";
		if (!StringUtils.isEmpty(attentionArea))
		{
			String[] arrs = attentionArea.split(",");
			for (int i = 1; i < arrs.length; i++) {
				attenionAreaId+=arrs[i];
				if (arrs.length != i+1)
				{
					attenionAreaId+=",";
				}
			}
		}
		user.setAttentionArea(attenionAreaId);
		//设置用户类型为系统用户
		user.setUserType(0);
		if(null != getCurrentSessionUser()){
			user.setAddUser(getCurrentSessionUser().getId());
		}else{
			user.setAddUser("1");
		}
		user.setPhone(phone);
		RoleInfoBean role = new RoleInfoBean();
		role.setRoleId(roleID);
        user.setReInfoBean(role);
		userInfoService.addUser(user);
		// 如果角色不为空
		return "{success:true}";
	}
	
	/**
	 * 删除用户
	 * @return
	 */
	public String deleteUser(){
		String id = getValue("id");
		int count = userInfoService.deleteUser(id);
		if (count > 0)
		{
			return "{success : true}";
		}
		return "{success : false}";
	}
	
	/**
	 * 获取用户信息
	 */
	public UserInfoBean getUserInfoById()
	{
		String id = getValue("id");
		if (StringUtils.isEmpty(id))
		{
			throw new ServiceException("参数错误");
		}
		return userInfoService.getUserInfoBeanById(id);
	}
	
	/**
	 * 验证注册时登录名是否重复
	 */
	public String checkLoginName()
	{
		String loginName = getValue("loginname");
		int count = userInfoService.checkLoginName(loginName);
		if (count > 0)
		{
			return "{success : false}";
		}
		return "{success : true}";
	}
	
	/**
	 * 修改用户信息
	 * 
	 * @param model
	 * @return
	 */
	public String updateUserInfoBean()
	{
		String userId = getValue("id");
		UserInfoBean userInfo = userInfoService.getUserInfoBeanById(userId);
		String name = getValue("name");
		int status = getInt("status");
		String phone = getValue("phone");
		String groupId = getValue("role");
		String address = getValue("address");
		String attentionArea = getValue("attentionAreaId");
		String attenionAreaId = "";
		if (!StringUtils.isEmpty(attentionArea))
		{
			String[] arrs = attentionArea.split(",");
			for (int i = 1; i < arrs.length; i++) {
				attenionAreaId+=arrs[i];
				if (arrs.length-1 != i)
				{
					attenionAreaId+=",";
				}
			}
		}
		if(!"1".equals(userId)){
			userInfo.setAttentionArea(attenionAreaId); 
		}
		if (null == userInfo)
		{
			throw new ServiceException("参数传递异常");
		}
		userInfo.setName(name);
		userInfo.setStatus(status);
		userInfo.setPhone(phone);
		RoleInfoBean roleBean = new RoleInfoBean();
		roleBean.setRoleId(groupId);
		userInfo.setReInfoBean(roleBean);
		userInfo.setAddress(address);
		int resultTemp = userInfoService.updateUserInfoBean(userInfo);
		if (0 == resultTemp)
		{
			return "{success : false}";
		}
		return "{success : true}";
	}
	
	/**
	 * 删除用户
	 * @return
	 */
	public String deleteUsers(){
		String ids = getValue("ids");
		int count = userInfoService.deleteUsers(ids);
		if (count > 0)
		{
			return "{success : true}";
		}
		return "{success : false}";
	}

	/**
	 * 修改密码
	 * @return
	 */
	public String updateUserPwd(){
		String userId = getCurrentSessionUser().getId();
		if(org.apache.commons.lang.StringUtils.isEmpty(userId)){
			throw new ServiceException("用户未登陆，修改密码异常！");
		}
		String oldPasword = getString("oldPassword");
		String newPassword = getString("newPassword");
		oldPasword = StringUtils.SHA256(oldPasword);
		if(getCurrentSessionUser().getLoginPassword().equals(oldPasword)){
			if(this.userInfoService.updatePwd(userId,StringUtils.SHA256(newPassword))){
				return "true";
			};
		}else{
			throw new ServiceException("原密码密码错误！");
		}
		return "false";
	}
	
    /**
     * 重置密码
     * @return
     */
	public String resetPWD(){
		String id = getValue("id");
		int count = userInfoService.resetPwd(id);
		if (count > 0)
		{
			return "{success : true}";
		}
		return "{success : false}";
	}
	
	
	
	public List<UserInfoBean> queryByCheck(){
		String is_now=getValue("is_now");
		return userInfoService.queryByCheck(is_now);
	}
	
}
