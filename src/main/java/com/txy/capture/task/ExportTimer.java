package com.txy.capture.task;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import org.springframework.beans.factory.annotation.Autowired;

import com.txy.common.orm.query.impl.DynamicBean;
import com.txy.web.main.services.SystemBackInfoServie;

public class ExportTimer {
    public static Map<String, Object>  map = new HashMap<String, Object>();
    public static Map<String, Long>  timerCycle = new HashMap<String, Long>();

    @Autowired
    public static  SystemBackInfoServie systemBackInfoServie;
    
    static
    {
	/*<option value="1">每天</option>
	<option value="2">每周</option>
	<option value="3">每月</option>
	<option value="4">每年</option>*/
//	timerCycle.put("1", 1000*60L);
	timerCycle.put("1", 1000*60*60*24L);
	timerCycle.put("2", 1000*60*60*24*7L);
	timerCycle.put("3", 1000*60*60*24*30L);
	timerCycle.put("4", 1000*60*60*24*365L);
    }
    
    public static void changeTimer(String modelType,String oldModelType)
    {
	Object obj = map.get(oldModelType);
	if (obj == null)
	{
	    addTimer(modelType);
	    return;
	}
	 Timer timer = (Timer)obj;
	 timer.cancel();
	 map.remove(oldModelType);
	 DynamicBean dynamicBean =  systemBackInfoServie.queryWmpCustomByModelType(modelType);
	 long cycleTime = timerCycle.get(dynamicBean.getValue("period"));
	 Timer t = new Timer();
	    t.schedule(new DataExportTask(dynamicBean,systemBackInfoServie)
	    , 0,cycleTime);
	 map.put(modelType, t);
    }
    
    
    public static void addTimer(String modelType)
    {
	DynamicBean dynamicBean =  systemBackInfoServie.queryWmpCustomByModelType(modelType);
	Timer t = new Timer();
	 long cycleTime = timerCycle.get(dynamicBean.getValue("period"));
	t.schedule(new DataExportTask(dynamicBean,systemBackInfoServie)
	, 0,cycleTime);
	
	map.put(modelType, t);
    }
}
