package com.txy.web.frame.handler;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.sun.star.uno.Exception;
import com.txy.common.api.Handler;
import com.txy.common.bean.MenuInfoBean;
import com.txy.common.bean.PageList;
import com.txy.common.bean.TreeInfoBean;
import com.txy.common.constant.Constant;
import com.txy.common.exception.ServiceException;
import com.txy.tools.FileUtils;
import com.txy.web.common.bean.UserInfoBean;
import com.txy.web.frame.proxy.AbstractHandler;
import com.txy.web.frame.services.MenuInfoService;

/**
 * 菜单操作处理类
 * 
 * @author fei
 */
@Controller
public class MenuInfoHandler extends AbstractHandler
{
	
	@Autowired
	private MenuInfoService menuInfoService;
	
	@Autowired
	private Map<String, Handler<String>> handlerMap;
	
	/**
	 * 查询主面板上的header菜单
	 * 
	 * @param model
	 * @return
	 */
	public List<MenuInfoBean> initHeaderMenuInfoList()
	{
		HttpSession session = model.getRequest().getSession();
		String userId = "1";
		UserInfoBean bean = (UserInfoBean) session.getAttribute("user");
		//必须登录0  1非必须登录
		if (bean == null && Constant.propertiesMap.get("isLoginBrower").equals("0"))
		{
			throw new ServiceException("登录超时");
		}
		if(Constant.propertiesMap.get("isLoginBrower").equals("0")){
			userId = bean.getId();
		}
		String node = getValue("node");
		List<MenuInfoBean> list = this.menuInfoService.queryHeaderMenuInfoList(node, userId);
		return list;
	}
	
	/**
	 * 查询主面板左边树菜单
	 * 
	 * @param model
	 * @return
	 */
	public List<MenuInfoBean> initLeftList()
	{
		HttpSession session = model.getRequest().getSession();
		UserInfoBean bean = (UserInfoBean) session.getAttribute("user");
		//必须登录0  1非必须登录
		String userId = "1";
		if (bean == null && Constant.propertiesMap.get("isLoginBrower").equals("0"))
		{
			throw new ServiceException("登录超时");
		}
		if(Constant.propertiesMap.get("isLoginBrower").equals("0")){
			userId = bean.getId();
		}
		String node = getValue("id");
		MenuInfoBean menusBean = this.menuInfoService.getMenusLeftAll(node,userId);
		return menusBean.getChildren();
	}
	
	/**
	 * 获取角色管理下权限设置 菜单资源树
	 * @param model
	 * @return
	 */
	public TreeInfoBean getMenusCheckForTree()
	{
		String roleId = getValue("roleId");
		String userId = "1";
		if(null != getCurrentSessionUser()){
			userId = getCurrentSessionUser().getId(); 
		}
		TreeInfoBean treeBean = this.menuInfoService.queryAllMenuByRoleId(userId, roleId);
		return treeBean;
	}
	
	/**
	 * 查询菜单管理页面左边面板的树
	 * @param model
	 * @return
	 */
	public TreeInfoBean getMenuPageLeftTree()
	{
		TreeInfoBean treeBean = this.menuInfoService.queryAllMenuByRoleId();
		return treeBean;
	}
	
	/**
	 * 查询菜单管理页面右侧数据
	 * @return
	 */
	public PageList<MenuInfoBean> queryMenuPageRight(){
		String parentId = getValue("parentId");
		int start = getInt("page");
		int pageSize = getInt("rows");
		PageList<MenuInfoBean> pageList = menuInfoService.queryMenuPageRight(parentId,start,pageSize);
		return pageList;
	}
	
	/**
	 * 添加菜单
	 */
	public boolean addMenu(){
		Map<String, Object> map = getParamMap();
 		int flag = menuInfoService.addMenu(map);
 		if(flag > 0) return true;
 		return false;
	}
	
	/**
	 * 通过id查询菜单
	 */
	public MenuInfoBean queryMenuById(){
		String id = getValue("id");
		return menuInfoService.queryMenuById(id);
	}
	
	/**
	 * 修改菜单
	 */
	public boolean updateMenu(){
		Map<String, Object> map = getParamMap();
		map.put("open_address", map.get("url"));
		map.remove("url");
	    int flag = menuInfoService.updateMenu(map);
	    if(flag > 0) return true;
	    return false;
	}
	
	/**
	 * 删除菜单
	 */
	public boolean deleteMenu(){
		String id = getValue("id");
		int falg = menuInfoService.deleteMenu(id);
		if(falg > 0) return true;
		return false;
	}
	
	/**
	 * 得到菜单icon
	 * @return
	 */
	public List<String> getMenuIcons(){
		String path = getSession().getServletContext().getRealPath("/")+"\\resources\\electric_skin\\icons\\";
		List<String> list = null;
		try {
			list = FileUtils.getImageNames(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
