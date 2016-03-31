package com.txy.web.frame.handler;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.txy.common.bean.PageList;
import com.txy.common.bean.RoleInfoBean;
import com.txy.common.exception.ServiceException;
import com.txy.common.util.StringUtils;
import com.txy.web.common.bean.UserInfoBean;
import com.txy.web.frame.proxy.AbstractHandler;
import com.txy.web.frame.services.RoleInfoService;

/**
 * 用户角色管理
 * @author fei
 *
 */
@Controller
public class RoleManagerHandler extends AbstractHandler
{
	private static final Logger logger = Logger.getLogger(RoleManagerHandler.class);

	@Autowired
	private RoleInfoService roleInfoService;
	
	/**
	 * 查询列表
	 * @return
	 */
	public PageList<RoleInfoBean> getRoleListInfo(){
		String roleName = getValue("roleName");
		String roleStatus = getValue("roleStatus");
		int start = getInt("page");
		int pageSize = getInt("rows");
		PageList<RoleInfoBean> pageList = roleInfoService.queryRolePageList(roleName,roleStatus,start,pageSize);
		return pageList;
	}
	
	
	/**
	 * 查询用户列表的角色
	 * @param model
	 * @return
	 */
	public List<RoleInfoBean> getRoleListByUser(){
		UserInfoBean userInfo = ((UserInfoBean)getSessionAttr("user"));
		String userId = "1";
		if(null != userInfo){
			userId = userInfo.getId();
		}
		List<RoleInfoBean> list = roleInfoService.getRoleListByUser(userId);
		return list;
	}
	
	/**
	 * 添加角色
	 * @return
	 */
	public String addRole(){
		String roleName = getValue("roleName");
		String description = getValue("description");
		String role_status = getValue("role_status");
		logger.info(roleName+description+role_status);
		RoleInfoBean roleInfo = new RoleInfoBean();
		roleInfo.setRoleId(StringUtils.getPrimarykeyId());
		roleInfo.setGroupName(roleName);
		roleInfo.setStatus(Integer.valueOf(role_status));
		roleInfo.setDescription(description);
		roleInfo.setAddUser(getCurrentSessionUser().getId());
		roleInfo.setAddTime(new Date());
		if(roleInfoService.save(roleInfo)>0){
			return "true";
		}
		return "false";
	}
	
	/**
	 * 删除角色
	 * @return
	 */
	public String deleteRole(){
		String roleId = getValue("roleId");
		if(StringUtils.isEmpty(roleId)){
			throw new ServiceException("传入参数异常");
		}
		if(roleInfoService.deleteRole(roleId)>0){
			return "true";
		} 
		return "false";
	}
	
	/**
	 * 编辑角色
	 * @return
	 */
	public String updateRoleInfo(){
		String roleId = getValue("roleId");
		RoleInfoBean roleBean = roleInfoService.queryRoleBeanInfo(roleId);
		if(null != roleBean){
			roleBean.setGroupName(getValue("groupName"));
			roleBean.setStatus(getInt("status"));
			roleBean.setDescription(getValue("description"));
			roleBean.setUpdateTime(new Date());
			roleBean.setUpdateUser(getCurrentSessionUser().getId());
			roleInfoService.updateRole(roleBean);
			return "true";
		}
		return "false";
	}
	
	/**
	 * 查询角色信息
	 * @return
	 */
	public RoleInfoBean getRoleInfoById(){
		String roleId = getValue("roleId");
		return roleInfoService.queryRoleBeanInfo(roleId);
	}
	
	
	/**
	 * 更新角色资源
	 * @param model
	 * @return
	 */
	public String updateRoleSourceInfo(){
		String sourceId = getValue("authIds");
		String roleId = getValue("roleId");
		String[] source = sourceId.split(",");
		boolean flag = this.roleInfoService.isExsitsRoleMenu(roleId);
		if(flag){
			//资源存在则修改role_menu_status为1
			this.roleInfoService.updateRoleMenuStatusByRoleId(roleId);
			//然后根据提交的menu修改状态0
			roleInfoService.updateRoleMenuPermission(roleId,source);
		}else{
			//不存在，则第一次添加
			roleInfoService.addRoleMenuList(source,roleId);
		}
		return "true";
	}
}
