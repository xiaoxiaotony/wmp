package com.txy.web.frame.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.txy.capture.task.DataExportTask;
import com.txy.capture.task.ExportTimer;
import com.txy.common.orm.query.impl.DynamicBean;
import com.txy.web.main.services.GroundRealTimeDataService;
import com.txy.web.main.services.SystemBackInfoServie;

public class InitDataListener implements ServletContextListener {

	private static final Logger logger = Logger.getLogger(InitDataListener.class);

	public static List<String> openAddressList = new ArrayList<>();

	public static final String port = "10000";

	private GroundRealTimeDataService groundRealTimeDataService;
	
	private   SystemBackInfoServie systemBackInfoServie;
	
	public Object getBean(ServletContext servletContext, String beanName) {
		return WebApplicationContextUtils.getWebApplicationContext(servletContext).getBean(beanName);
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		logger.info("----------------------------------------------------");
		ServletContext servletContext = sce.getServletContext();
		groundRealTimeDataService = (GroundRealTimeDataService) getBean(servletContext, "groundRealTimeDataService");
		systemBackInfoServie = (SystemBackInfoServie) getBean(servletContext, "systemBackInfoServie");
		// 设置根目录别名
		servletContext.setAttribute("listTree", groundRealTimeDataService.getListTree());
		servletContext.setAttribute("stationTypes", groundRealTimeDataService.getStationTypes());
		
//		initTimer();
		ExportTimer.systemBackInfoServie = systemBackInfoServie;
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}
	
	@SuppressWarnings("unused")
	private void initTimer()
	{
	    List<DynamicBean> datas = systemBackInfoServie.queryWmpCustom();
		for (DynamicBean dynamicBean : datas) {
		    long cycleTime =  ExportTimer.timerCycle.get(dynamicBean.getValue("period"));
		    Timer timer = new Timer();
		    timer.schedule(new DataExportTask(dynamicBean,systemBackInfoServie)
		    , 0 , cycleTime);
		    ExportTimer.map.put(dynamicBean.getValue("model_type"), timer);
		}
		 
	}
}
