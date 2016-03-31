package com.txy.web.frame.base;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.ApplicationContext;

import com.txy.common.api.Handler;
public class HandlerContainer
{
	@Resource
	private Map<String,Handler<String>> handlerContainerMap;
	private Map<String,Map<String,String>> appMapContainer=new HashMap<String,Map<String,String>>();
	
	private static final Logger log = Logger.getLogger(HandlerContainer.class);
	
	public Map<String, Map<String, String>> getAppMapContainer()
	{
		return appMapContainer;
	}
	public void setAppMapContainer(Map<String, Map<String, String>> appMapContainer)
	{
		this.appMapContainer = appMapContainer;
	}
	public Map<String,Handler<String>> getHandlerContainerMap()
	{
		return this.handlerContainerMap;
	}
	public Handler<String> getHandler(String key)
	{
		return this.handlerContainerMap.get(key);
	}
	
	/**
	 * 刷新Handler容器
	 */
	@SuppressWarnings("unchecked")
	public void refresh(ApplicationContext wac,Map<String,BeanDefinitionBuilder> argsMap,String appCode)
	{
		log.info("-------------------------------------------------刷新Handler容器---------------------------------------");
		Map<String,String> map=new HashMap<String,String>();
		for(Entry<String,BeanDefinitionBuilder> entry:argsMap.entrySet())
		{
			map.put(entry.getKey(),entry.getKey());
			Object obj=wac.getBean(entry.getKey());
			if(obj instanceof Handler)
			{
				Handler<String> handler=(Handler<String>)obj;
				handlerContainerMap.put(entry.getKey(),handler);
			}	
		}
		argsMap.clear();
		this.getAppMapContainer().put(appCode,map);
	}
	
	public void clear(String appCode)
	{
		Map<String,String> map=this.appMapContainer.get(appCode);
		for(Entry<String,String> entry:map.entrySet())
		{
			this.handlerContainerMap.remove(entry.getKey());
		}
	}
	
}
