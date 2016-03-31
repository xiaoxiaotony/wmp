package com.txy.web.frame.proxy;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;

import com.txy.common.exception.ServiceException;
import com.txy.common.util.StringUtils;
import com.txy.tools.JsonToolUtil;
import com.txy.web.common.bean.UserInfoBean;

/**
 * 通过反射去处理真正的请求的实现类
 * 
 * @author fei
 * 
 */
public class AbstractHandler extends InvocationController
{
	
	// 获取请求参数值，如果请求参数不存在则返回空字符
	public String getValue(String parameterName)
	{
		return StringUtils.objToString(model.get(parameterName));
	}
	
	// 获取字符串参数
	public String getString(String parameterName)
	{
		return StringUtils.objToString(model.get(parameterName));
	}
	
	// 获取非空的字符串参数
	public String getStringNotEmpty(String parameterName)
	{
		if (StringUtils.isEmptyObj(model.get(parameterName)))
		{
			throw new ServiceException("参数[" + parameterName + "]不能为空");
		}
		return this.getString(parameterName);
	}
	
	public String getValueNotEmpty(String parameterName)
	{
		if (StringUtils.isEmptyObj(model.get(parameterName)))
		{
			throw new ServiceException("参数[" + parameterName + "]不能为空");
		}
		return this.getValue(parameterName);
	}
	
	public int getInt(String parameterName)
	{
		try
		{
			return StringUtils.toNum(this.getValue(parameterName));
		}
		catch (Exception e)
		{
			throw new ServiceException("参数[" + parameterName + "]不能转换成Int类型");
		}
	}
	
	public long getLong(String parameterName)
	{
		try
		{
			Double d = StringUtils.toDouble(this.getValueNotEmpty(parameterName));
			return d.longValue();
			
		}
		catch (Exception e)
		{
			throw new ServiceException("参数[" + parameterName + "]不能转换成Long类型");
		}
	}
	
	// 获取指定类型的对象值
	public <T> T getValue(Class<T> cla, String parameterName)
	{
		return cla.cast(model.get(parameterName));
	}
	
	public <T> T getValueNotEmpty(Class<T> cla, String parameterName)
	{
		if (model.get(parameterName) == null)
		{
			throw new ServiceException("参数[" + parameterName + "]不能为空");
		}
		return this.getValue(cla, parameterName);
	}
	
	/**
	 * 获取日期默认日期格式：yyyy-MM-dd hh:mm:ss
	 *
	 * @param parameterName
	 * @return
	 */
	public Date getDate(String parameterName)
	{
		SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try
		{
			return formatDate.parse(this.getValueNotEmpty(parameterName));
		}
		catch (ParseException e)
		{
			throw new ServiceException("日期格式不正确");
		}
	}
	
	/**
	 *
	 * @param parameterName
	 * @param format:日期格式
	 * @return
	 */
	public Date getDate(String parameterName, String format)
	{
		SimpleDateFormat formatDate = new SimpleDateFormat(format);
		try
		{
			
			return formatDate.parse(this.getValueNotEmpty(parameterName));
		}
		catch (ParseException e)
		{
			throw new ServiceException("日期格式不正确");
		}
	}
	
	public void setAttr(String name, Object value)
	{
		model.getRequest().setAttribute(name, value);
	}
	
	public void removeAttr(String name)
	{
		model.getRequest().removeAttribute(name);
	}
	
	public void setAttrs(Map<String, Object> attrMap)
	{
		for (Map.Entry<String, Object> entry : attrMap.entrySet())
			model.getRequest().setAttribute(entry.getKey(), entry.getValue());
	}
	
	public String getPara(String name)
	{
		return model.getRequest().getParameter(name);
	}
	
	public String getPara(String name, String defaultValue)
	{
		String result = model.getRequest().getParameter(name);
		return result != null && !"".equals(result) ? result : defaultValue;
	}
	
	public Map<String, String[]> getParaMap()
	{
		return model.getRequest().getParameterMap();
	}
	
	public Enumeration<String> getParaNames()
	{
		return model.getRequest().getParameterNames();
	}
	
	public String[] getParaValues(String name)
	{
		return model.getRequest().getParameterValues(name);
	}
	
	public Integer[] getParaValuesToInt(String name)
	{
		String[] values = model.getRequest().getParameterValues(name);
		if (values == null)
			return null;
		Integer[] result = new Integer[values.length];
		for (int i = 0; i < result.length; i++)
			result[i] = Integer.parseInt(values[i]);
		return result;
	}
	
	public Enumeration<String> getAttrNames()
	{
		return model.getRequest().getAttributeNames();
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getAttr(String name)
	{
		return (T) model.getRequest().getAttribute(name);
	}
	
	public String getAttrForStr(String name)
	{
		return (String) model.getRequest().getAttribute(name);
	}
	
	public Integer getAttrForInt(String name)
	{
		return (Integer) model.getRequest().getAttribute(name);
	}
	
	private Integer toInt(String value, Integer defaultValue)
	{
		if (value == null || "".equals(value.trim()))
			return defaultValue;
		if (value.startsWith("N") || value.startsWith("n"))
			return -Integer.parseInt(value.substring(1));
		return Integer.parseInt(value);
	}
	
	public Integer getParaToInt(String name)
	{
		return toInt(model.getRequest().getParameter(name), null);
	}
	
	public Integer getParaToInt(String name, Integer defaultValue)
	{
		return toInt(model.getRequest().getParameter(name), defaultValue);
	}
	
	private Long toLong(String value, Long defaultValue)
	{
		if (value == null || "".equals(value.trim()))
			return defaultValue;
		if (value.startsWith("N") || value.startsWith("n"))
			return -Long.parseLong(value.substring(1));
		return Long.parseLong(value);
	}
	
	public Long getParaToLong(String name)
	{
		return toLong(model.getRequest().getParameter(name), null);
	}
	
	public Long getParaToLong(String name, Long defaultValue)
	{
		return toLong(model.getRequest().getParameter(name), defaultValue);
	}
	
	private Boolean toBoolean(String value, Boolean defaultValue)
	{
		if (value == null || "".equals(value.trim()))
			return defaultValue;
		value = value.trim().toLowerCase();
		if ("1".equals(value) || "true".equals(value))
			return Boolean.TRUE;
		else if ("0".equals(value) || "false".equals(value))
			return Boolean.FALSE;
		throw new RuntimeException("Can not parse the parameter \"" + value + "\" to boolean value.");
	}
	
	public Boolean getParaToBoolean(String name)
	{
		return toBoolean(model.getRequest().getParameter(name), null);
	}
	
	public Boolean getParaToBoolean(String name, Boolean defaultValue)
	{
		return toBoolean(model.getRequest().getParameter(name), defaultValue);
	}
	
	private Date toDate(String value, Date defaultValue)
	{
		if (value == null || "".equals(value.trim()))
			return defaultValue;
		try
		{
			return new java.text.SimpleDateFormat("yyyy-MM-dd").parse(value);
		}
		catch (ParseException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public Date getParaToDate(String name)
	{
		return toDate(model.getRequest().getParameter(name), null);
	}
	
	public Date getParaToDate(String name, Date defaultValue)
	{
		return toDate(model.getRequest().getParameter(name), defaultValue);
	}
	
	public HttpServletRequest getRequest()
	{
		return model.getRequest();
	}
	
	public HttpServletResponse getResponse()
	{
		return response;
	}
	
	public HttpSession getSession()
	{
		return model.getRequest().getSession();
	}
	
	public HttpSession getSession(boolean create)
	{
		return model.getRequest().getSession(create);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getSessionAttr(String key)
	{
		HttpSession session = model.getRequest().getSession(false);
		return session != null ? (T) session.getAttribute(key) : null;
	}
	
	public void setSessionAttr(String key, Object value)
	{
		model.getRequest().getSession().setAttribute(key, value);
	}
	
	public void removeSessionAttr(String key)
	{
		HttpSession session = model.getRequest().getSession(false);
		if (session != null)
			session.removeAttribute(key);
	}
	
	public String getCookie(String name, String defaultValue)
	{
		Cookie cookie = getCookieObject(name);
		return cookie != null ? cookie.getValue() : defaultValue;
	}
	
	public String getCookie(String name)
	{
		return getCookie(name, null);
	}
	
	public Integer getCookieToInt(String name)
	{
		String result = getCookie(name);
		return result != null ? Integer.parseInt(result) : null;
	}
	
	public Integer getCookieToInt(String name, Integer defaultValue)
	{
		String result = getCookie(name);
		return result != null ? Integer.parseInt(result) : defaultValue;
	}
	
	public Long getCookieToLong(String name)
	{
		String result = getCookie(name);
		return result != null ? Long.parseLong(result) : null;
	}
	
	public Long getCookieToLong(String name, Long defaultValue)
	{
		String result = getCookie(name);
		return result != null ? Long.parseLong(result) : defaultValue;
	}
	
	public Cookie getCookieObject(String name)
	{
		Cookie[] cookies = model.getRequest().getCookies();
		if (cookies != null)
			for (Cookie cookie : cookies)
				if (cookie.getName().equals(name))
					return cookie;
		return null;
	}
	
	public Cookie[] getCookieObjects()
	{
		Cookie[] result = model.getRequest().getCookies();
		return result != null ? result : new Cookie[0];
	}
	
	public void setCookie(Cookie cookie)
	{
		response.addCookie(cookie);
	}
	
	public void setCookie(String name, String value, int maxAgeInSeconds, String path)
	{
		setCookie(name, value, maxAgeInSeconds, path, null);
	}
	
	public void setCookie(String name, String value, int maxAgeInSeconds, String path, String domain)
	{
		Cookie cookie = new Cookie(name, value);
		if (domain != null)
			cookie.setDomain(domain);
		cookie.setMaxAge(maxAgeInSeconds);
		cookie.setPath(path);
		response.addCookie(cookie);
	}
	
	public void setCookie(String name, String value, int maxAgeInSeconds)
	{
		setCookie(name, value, maxAgeInSeconds, "/", null);
	}
	
	public void removeCookie(String name)
	{
		setCookie(name, null, 0, "/", null);
	}
	
	public void removeCookie(String name, String path)
	{
		setCookie(name, null, 0, path, null);
	}
	
	public void removeCookie(String name, String path, String domain)
	{
		setCookie(name, null, 0, path, domain);
	}
	
	/**
	 * 获取当前登录的用户信息
	 * 
	 * @return
	 */
	public UserInfoBean getCurrentSessionUser()
	{
		return getSessionAttr("user");
	}
	
	/**
	 * 获取from页面提供的map
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map<String, Object> getParamMap()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String[]> pararmMap = this.getRequest().getParameterMap();
		Set<String> keySet = pararmMap.keySet();
		for (Iterator it = keySet.iterator(); it.hasNext();)
		{
			String key = (String) it.next();
			map.put(key, pararmMap.get(key)[0]);
		}
		return map;
	}
	
	/**
	 * 获取查询面板参数
	 * 
	 * @return
	 */
	public Map<String, Object> getQueryParamMap()
	{
		return JsonToolUtil.parseJSONToMap(getPara("param"));
	}
	
	/**
	 * 用于添加，修改，获取参数转为对象
	 * @param cla
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public <T> T getParamToObject(Class<T> cla)
	{
		Map<String, Object> map = getParamMap();
		T t = null;
		Field[] fields = cla.getDeclaredFields();
		String beanField = "";
		try
		{
			t = cla.newInstance();
			Set<String> key = map.keySet();
			for (Iterator it = key.iterator(); it.hasNext();)
			{
				String keyStr = (String) it.next();
				for (int i = 0; i < fields.length; i++)
				{
					beanField = fields[i].getName();
					if (beanField.equals(keyStr))
					{
						BeanUtils.setProperty(t, beanField, map.get(keyStr));
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return t;
	}
	
}
