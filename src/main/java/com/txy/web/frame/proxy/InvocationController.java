package com.txy.web.frame.proxy;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import com.txy.common.api.Handler;
import com.txy.common.bean.Model;
import com.txy.common.constant.Constant;
import com.txy.common.exception.ServiceException;
import com.txy.common.format.DataFormat;
import com.txy.common.format.JsonDataFormat;
import com.txy.common.util.InvokMethod;
import com.txy.common.util.StringUtils;
import com.txy.web.common.bean.UserInfoBean;
import com.txy.web.frame.services.SystemInfoService;

public abstract class InvocationController implements Handler<String>
{
	
	private static final Logger log = Logger.getLogger(InvocationController.class);
	
	@Autowired
	private SystemInfoService systemInfoService;
	
	protected DataFormat dataFormat;
	
	protected InvocationController()
	{
		this.dataFormat = new JsonDataFormat();
	}
	
	public Model model;
	
	public HttpServletRequest request;
	
	public HttpServletResponse response;
	
	@Override
	public synchronized Object execute(Model model)
	{
		String currentUser = getCurrentUser(model);
		this.model = model;
		this.request = model.getRequest();
		this.response = model.getResponse();
		Object responseData = null;
		String dataType = model.getSystemModel().getDataType();
		if (StringUtils.isEmpty(dataType))
		{
			dataType = Constant.DATA_FORMAT.json.name();
		}
		Object obj = null;
		try
		{
			obj = InvokMethod.invok(this, model.getSystemModel().getMethod(), model);
			// 传入参数信息
			if (obj instanceof ModelAndView)
			{
				return obj;
			}
			// 参数处理
			String param = processRequestParam(model);
			// 是否保存日志
			systemInfoService.saveLog(model.getSystemModel().getHandler(), model.getSystemModel().getMethod(), currentUser, model
					.getSystemModel().getRequestIp(), param, 0, model.toString());
			responseData = dataFormat.format(obj, dataType);
		}
		catch (ServiceException e)
		{
			if (null != getCurrentUser(model))
			{
				systemInfoService.saveLog(model.getSystemModel().getHandler(), model.getSystemModel().getMethod(), currentUser, model
						.getSystemModel().getRequestIp(), e.getMessage(), 1, "");
			}
			if ("login".equals(model.getSystemModel().getMethod()))
			{
				try
				{
					systemInfoService.saveLog(model.getSystemModel().getHandler(), model.getSystemModel().getMethod(),
							java.net.URLDecoder.decode(model.get("loginName").toString(), "UTF-8"), model.getSystemModel().getRequestIp(),
							e.getMessage(), 1, "");
				}
				catch (UnsupportedEncodingException exception)
				{
					exception.printStackTrace();
				}
			}
			responseData = dataFormat.errorMsg(e.getMessage(), dataType);
		}
		catch (Exception e)
		{
			systemInfoService.saveLog(model.getSystemModel().getHandler(), model.getSystemModel().getMethod(), currentUser, model
					.getSystemModel().getRequestIp(), e.getMessage(), 1, "");
			responseData = dataFormat.errorMsg(e.getMessage(), dataType);
			log.error("转换JSON异常..." + e.getMessage());
		}
		return responseData;
	}
	
	/**
	 * 处理日志参数
	 * 
	 * @param model
	 * @return
	 */
	private String processRequestParam(Model model)
	{
		Map<String, String[]> map = model.getRequest().getParameterMap();
		Iterator<String> it = map.keySet().iterator();
		String value = "";
		String param = "";
		while (it.hasNext())
		{
			String key = it.next();
			String values[] = map.get(key);
			String value_a = "";
			for (int i = 0; i < values.length; i++)
			{
				value_a = values[i];
				if (i == values.length - 1)
				{
					if (i == 0)
						value = "{" + value_a + "}";
					else
						value += "," + value_a + "}";
					break;
				}
				if (i == 0)
					value = "{" + value_a;
				else
					value += "," + value_a;
			}
			param += key + ":" + value + " ";
		}
		return param;
	}
	
	/**
	 * 获取当前登录登录用户名
	 * 
	 * @param model
	 * @return
	 */
	private String getCurrentUser(Model model)
	{
		Object obj = model.getRequest().getSession().getAttribute("user");
		UserInfoBean userInfoBean = null;
		if (null != obj)
		{
			userInfoBean = (UserInfoBean) obj;
			return userInfoBean.getName();
		}
		if ("login".equals(model.getSystemModel().getMethod()))
		{
			try
			{
				return java.net.URLDecoder.decode(String.valueOf(model.get("loginName")), "UTF-8");
			}
			catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
		}
		return "";
	}
}
