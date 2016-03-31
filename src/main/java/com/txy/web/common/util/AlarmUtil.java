package com.txy.web.common.util;

import java.util.Collection;

import org.directwebremoting.Browser;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;

public class AlarmUtil {

	public static void sendNetworkAlarm(final String str, final int status) {
		Runnable run = new Runnable() {
			private ScriptBuffer script = new ScriptBuffer();
			@Override
			public void run() {
				// 设置要调用的 js及参数
				script.appendCall("showNetworkAlarm", str, status);
				// 得到所有ScriptSession
				Collection<ScriptSession> sessions = Browser.getTargetSessions();
				// 遍历每一个ScriptSession
				for (ScriptSession scriptSession : sessions) {
					scriptSession.addScript(script);
				}
			}
		};
		// 执行推送
		Browser.withAllSessions(run);
	}

	
	/**
	 * 右下角弹出警告
	 * @param context
	 */
	public static void sendAlertNetworkAlarm(final String context) {
		Runnable run = new Runnable() {
			private ScriptBuffer script = new ScriptBuffer();
			@Override
			public void run() {
				// 设置要调用的 js及参数
				script.appendCall("alertNetworkAlarm", context);
				// 得到所有ScriptSession
				Collection<ScriptSession> sessions = Browser.getTargetSessions();
				// 遍历每一个ScriptSession
				for (ScriptSession scriptSession : sessions) {
					scriptSession.addScript(script);
				}
			}
		};
		// 执行推送
		Browser.withAllSessions(run);
	}
}