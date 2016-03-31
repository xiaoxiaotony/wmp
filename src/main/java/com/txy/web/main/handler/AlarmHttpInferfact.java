package com.txy.web.main.handler;

import org.springframework.stereotype.Controller;

import com.txy.web.common.util.AlarmUtil;
import com.txy.web.frame.proxy.AbstractHandler;


/**
 * 提供数据库调用页面推送接口处理类
 * @author AFEI
 *
 */
@Controller
public class AlarmHttpInferfact extends AbstractHandler{
	
	/**
	 * 改变网络拓扑图状态和颜色
	 */
	public void sendNetworkAlarm(){
		String location = getValue("locationKey");
		int status = getInt("status");
		AlarmUtil.sendNetworkAlarm(location, status);
	}
	
	
	/**
	 * 弹出框和播放声音接口
	 */
	public void sendAlertNetworkAlarm(){
		String context = getValue("content");
		AlarmUtil.sendAlertNetworkAlarm(context);
	}
}
