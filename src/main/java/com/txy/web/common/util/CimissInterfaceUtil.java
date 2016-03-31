package com.txy.web.common.util;

import java.util.HashMap;

import cma.cimiss.RetArray2D;
import cma.cimiss.client.DataQueryClient;

public class CimissInterfaceUtil {
	
	private static String USER_ID="BELS_nmc_dou";
	private static String PWD="123";

	public static RetArray2D getDataByRetArry2D(String interfaceId, HashMap<String,String> params){
		/* 1. 定义client对象 */
		DataQueryClient client = new DataQueryClient();
		RetArray2D retArray2D = new RetArray2D();

		try {
			// 初始化接口服务连接资源
			client.initResources();
			// 调用接口
			int rst = client.callAPI_to_array2D(USER_ID, PWD, interfaceId,
					params, retArray2D);
			// 输出结果
			if (rst == 0) { // 正常返回
				return retArray2D;
			} else { // 异常返回
				System.out.println("[error] StaElemSearchAPI_CLIB_callAPI_to_array2D.");
				System.out.printf("\treturn code: %d. \n", rst);
				System.out.printf("\terror message: %s.\n", retArray2D.request.errorMessage);				
				return null;
			}
		} catch (Exception e) {
			// 异常输出
			e.printStackTrace();
			return null;
		} finally {
			// 释放接口服务连接资源
			client.destroyResources();
		}
	}
	public static String getDataByJSON(String interfaceId, HashMap<String,String> params){
		/* 1. 定义client对象 */
		DataQueryClient client = new DataQueryClient();
		
		String dataFormat = "json" ;
        /* 2.5 返回字符串 */
        StringBuffer retStr = new StringBuffer() ;
		try {
			// 初始化接口服务连接资源
			client.initResources();
			// 调用接口
			int rst = client.callAPI_to_serializedStr(USER_ID, PWD, interfaceId,
					params, dataFormat, retStr);
			// 输出结果
			if (rst == 0) { // 正常返回
				return retStr.toString();
			} else { // 异常返回
				System.out.println("[error] FileInfoSearchAPI_CLIB_callAPI_to_serializedStr_JSON.");
				System.out.printf("\treturn code: %d. \n", rst);
				return null;
			}
		} catch (Exception e) {
			// 异常输出
			e.printStackTrace();
			return null;
		} finally {
			// 释放接口服务连接资源
			client.destroyResources();
		}
	}
	public static String getDataByJSONP(String interfaceId, HashMap<String,String> params){
		/* 1. 定义client对象 */
		DataQueryClient client = new DataQueryClient();
		
		String dataFormat = "jsonp" ;
		/* 2.5 返回字符串 */
		StringBuffer retStr = new StringBuffer() ;
		try {
			// 初始化接口服务连接资源
			client.initResources();
			// 调用接口
			int rst = client.callAPI_to_serializedStr(USER_ID, PWD, interfaceId,
					params, dataFormat, retStr);
			// 输出结果
			if (rst == 0) { // 正常返回
				return retStr.toString();
			} else { // 异常返回
				System.out.println("[error] FileInfoSearchAPI_CLIB_callAPI_to_serializedStr_JSONP.");
				System.out.printf("\treturn code: %d. \n", rst);
				return null;
			}
		} catch (Exception e) {
			// 异常输出
			e.printStackTrace();
			return null;
		} finally {
			// 释放接口服务连接资源
			client.destroyResources();
		}
	}
}
