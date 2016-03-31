package com.txy.web.frame.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.txy.web.common.bean.UserInfoBean;

public final class SystemContainer
{
	
	private static final Logger log = Logger.getLogger(SystemContainer.class);
	
	/**
	 * 获取当前用户信息
	 * 
	 * @return
	 */
	public static UserInfoBean getUserSession()
	{
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		HttpSession session = request.getSession();
		log.debug("session id :" + session.getId() + ":request id:" + request.getRemoteAddr());
		return (UserInfoBean) request.getSession().getAttribute("user");
	}
	
}
