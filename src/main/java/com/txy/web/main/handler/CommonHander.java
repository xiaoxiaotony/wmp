package com.txy.web.main.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.txy.web.common.bean.UserInfoBean;
import com.txy.web.frame.proxy.AbstractHandler;
import com.txy.web.main.services.CommonService;

/**
 * 公用接口
 * 
 * @author hong
 *
 */
@Controller
public class CommonHander extends AbstractHandler {

	@Autowired
	private CommonService commonService;

	/**
	 * 查询地区
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getStationDIC() {
		UserInfoBean user = getCurrentSessionUser();
		return commonService.queryStationDICByUser(user.getAttentionArea());
	}

	/**
	 * 查询站号(站名)
	 * 
	 */
	public List<Map<String, Object>> getStationAndName() {
		String dic_id = getString("dic_id");
		return commonService.queryStationAndName(dic_id);
	}

	/**
	 * 
	 * 查询资料类型
	 */
	public List<Map<String, Object>> getDataType() {
		String station_ids = getString("station_ids");
		String ismon = getString("ismon");
		String ids[] = station_ids.split(",");
		StringBuffer sb = new StringBuffer();
		for (String string : ids) {
			sb.append(",'" + string + "'");
		}
		sb.deleteCharAt(0);
		return commonService.queryDataType(sb.toString(), ismon);
	}

	public String downLoadFtpFile() {
		String fileName = getString("fileName");
		String ipaddress = getString("ipaddress");
		response.setCharacterEncoding("UTF-8");
		// fileName = "Xorg.21.log";
		response.setContentType("multipart/form-data");
		FTPClient ftpClient = new FTPClient();
		try {
			int reply;
			ftpClient.connect(ipaddress, 21);
			ftpClient.login("wlklog", "wlklog");
			reply = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
				return null;
			}
			ftpClient.changeWorkingDirectory("/");// 转移到FTP服务器目录
			FTPFile[] fs = ftpClient.listFiles();
			for (int i = 0; i < fs.length; i++) {
				if (fs[i].getName().equals(fileName)) {
					String saveAsFileName = new String(fs[i].getName().getBytes("UTF-8"), "ISO8859-1");
					response.setHeader("Content-Disposition", "attachment;fileName=" + saveAsFileName);
					OutputStream os = response.getOutputStream();
					ftpClient.retrieveFile(fs[i].getName(), os);
					os.flush();
					os.close();
					break;
				}
			}
			ftpClient.logout();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return null;
	}

	
	/**
	 * 解析ftp日志文件
	 * @return
	 */
	public String praseFtpLog() {
		String ftpLog = getPara("ftpLog");
		System.out.println(ftpLog);
		String result = praseDetailFtpLog(ftpLog);
		return result;
	}

	
	
	/**
	 * 详细解析日志文件
	 * @param ftpLog
	 * @return
	 */
	private String praseDetailFtpLog(String ftpLog) {
        //用回车键来分隔几个元素
        String[] ss = ftpLog.split("\n");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ss.length; i++) {
            System.out.println(ss[i]);
            String lineStr = ss[i].substring(0, 25);
            System.out.println(lineStr);
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy", Locale.ENGLISH);
    		Date d2 = null;
    		try {
    			d2 = sdf.parse(lineStr);
    		} catch (ParseException e) {
    			e.printStackTrace();
    		}
    		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sb.append(sdf2.format(d2)+" ");
            sb.append(ss[i].substring(25, ss[i].length()).replaceAll("FTP command", "FTP 命令").replaceAll("FTP response", "FTP 响应").replaceAll("110", "重新启动标记应答")
            		.replaceAll("120", "服务在多久时间内ready")
            		.replaceAll("125", "数据链路端口开启，准备传送")
            		.replaceAll("150", "文件状态正常，开启数据连接端口")
            		.replaceAll("200", "命令执行成功")
            		.replaceAll("202", "命令执行失败")
            		.replaceAll("211", "系统状态或是系统求助响应")
            		.replaceAll("212", "目录的状态")
            		.replaceAll("213", "文件的状态")
            		.replaceAll("214", "求助的讯息")
            		.replaceAll("215", "名称系统类型")
            		.replaceAll("220", "新的联机服务ready")
            		.replaceAll("221", "服务的控制连接端口关闭，可以注销")
            		.replaceAll("225", "数据连结开启，但无传输动作")
            		.replaceAll("226", "关闭数据连接端口，请求的文件操作成功")
            		.replaceAll("227", "进入passive mode")
            		.replaceAll("230", "使用者登入")
            		.replaceAll("250", "请求的文件操作完成")
            		.replaceAll("257", "显示目前的路径名称")
            		.replaceAll("331", "用户名称正确，需要密码")
            		.replaceAll("332", "登入时需要账号信息")
            		.replaceAll("350", "请求的操作需要进一部的命令")
            		.replaceAll("421", "无法提供服务，关闭控制连结")
            		.replaceAll("425", "无法开启数据链路")
            		.replaceAll("426", "关闭联机，终止传输")
            		.replaceAll("450", "请求的操作未执行")
            		.replaceAll("451", "命令终止:有本地的错误")
            		.replaceAll("452", "未执行命令:磁盘空间不足")
            		.replaceAll("500", "格式错误，无法识别命令")
            		.replaceAll("501", "参数语法错误")
            		.replaceAll("502", "命令执行失败")
            		.replaceAll("503", "命令顺序错误")
            		.replaceAll("504", "命令所接的参数不正确")
            		.replaceAll("530", "未登入")
            		.replaceAll("532", "储存文件需要账户登入")
            		.replaceAll("550", "未执行请求的操作")
            		.replaceAll("551", "请求的命令终止，类型未知")
            		.replaceAll("552", "请求的文件终止，储存位溢出")
            		.replaceAll("553", "未执行请求的的命令，名称不正确"));
            sb.append("\n");
        }
        System.out.println(sb.toString());
		return sb.toString();
	}
	
	

	public static void main(String[] args) {
        //用回车键来分隔几个元素
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy", Locale.ENGLISH);
		Date d2 = null;
		try {
			d2 = sdf.parse("Tue Jul 21 12:05:02 2015");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date d3 = new Date();
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("d2 ====== " + sdf2.format(d2));
		System.out.println("d3 ====== " + sdf.format(d3));
	}
	
	public String explorOut(){
		String content = getValue("content");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("multipart/form-data");
		try {
			String saveAsFileName = new String("ftplog.txt".getBytes(), "ISO8859-1");
			response.setHeader("Content-Disposition", "attachment;fileName=" + saveAsFileName);
			OutputStream os = response.getOutputStream();
			os.write(content.getBytes());
			os.flush();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}
		return null;
	}

}
