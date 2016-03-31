package com.txy.web.frame.filter;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.txy.common.constant.Constant;
import com.txy.common.util.StringUtils;
import com.txy.tools.PropertiseUtil;
import com.txy.web.common.bean.UserInfoBean;
import com.txy.web.frame.listener.InitDataListener;


/**
 * 添加权限和资源安全过滤器
 * @author fei
 *
 */
@WebFilter(urlPatterns={"/*"})
public class SystemFilter implements Filter {

	private static final Logger log = Logger.getLogger(SystemFilter.class);
	
	private static final String PROPERTIES_PATH = "/properties/config.properties";

	@Override
	public void init(FilterConfig args) throws ServletException {
		PropertiseUtil properties = new PropertiseUtil(PROPERTIES_PATH);
		log.info("系统正在初始化配置...");
		args.getServletContext().setAttribute("skin",properties.getValue("system_skin"));
		Constant.propertiesMap.put("isLoginBrower", properties.getValue("isLoginBrower"));
		Constant.propertiesMap.put("isSendEmail", properties.getValue("isSendEmail"));
		Constant.propertiesMap.put("checkFtpPathURL", properties.getValue("checkFtpPathURL"));
		Constant.propertiesMap.put("ServiceIp", properties.getValue("ServiceIp"));
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		HttpSession session = request.getSession();
		UserInfoBean userInfo = (UserInfoBean) session.getAttribute("user");
		String uri = request.getRequestURI();
		request.setAttribute("isLoginBrower", Constant.propertiesMap.get("isLoginBrower"));
		//系统不需要登录访问
		if(Constant.propertiesMap.get("isLoginBrower").equals("1")){
			filterChain.doFilter(req, resp);
		}else{
			if (uri.contains(".jsp") && passCheck(uri)) {
				if (userInfo == null) {
					if (request.getHeader("x-requested-with") != null && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
						response.setHeader("sessionstatus", "sessionTimeOut");
						return;
					}
					response.sendRedirect(session.getServletContext().getContextPath() + "/index.jsp");
					return;
				}
				//判断URI是否存在于数据库 存在于数据库才去验收
				if(checkThisUrlIsExsitDB(uri)){
					//检查是否有权限访问URL
					if(!uri.contains("page/home.jsp")&&!checkJspPerssion(uri, request.getSession().getAttribute("currentUserPageList"))){
						response.sendRedirect(session.getServletContext().getContextPath() + "/error.jsp");
						return; 	
					}
				}
			}
			// 只拦截后台请求
			if (uri.indexOf("userInfoHandler/login") == -1 && uri.indexOf("http") > -1 && uri.indexOf("alarmHttpInferfact") == -1) {
				// 用户为空
				if (userInfo == null && StringUtils.isEmpty(request.getParameter("sessionId"))) {
					// ajax请求数据判断用户过期
					if (request.getHeader("x-requested-with") != null && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest")) {
						response.setHeader("sessionstatus", "sessionTimeOut");
						return;
					}
					response.sendRedirect(session.getServletContext().getContextPath() + "/error.jsp");
					return;
				}
				// 判断用户请求的action url的权限
				if (checkUrlPerssion(uri, userInfo)) {
					response.sendRedirect(session.getServletContext().getContextPath() + "/error.jsp");
					return;
				}
			}
			log.debug("拦截器拦截到用户信息已经通过拦截器...............");
			filterChain.doFilter(req, resp);
		}
	}
	
	/**
	 * 验证这个URL
	 * @param uri
	 * @return
	 */
	private boolean checkThisUrlIsExsitDB(String uri)
	{	
		uri = uri.substring(uri.indexOf("page")+5, uri.length());
		if(!InitDataListener.openAddressList.isEmpty()&&InitDataListener.openAddressList.contains(uri)){
			return true;	
		}
		return false;
	}

	/**
	 * 验证JSP权限
	 * @param uri
	 * @param attribute
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private boolean checkJspPerssion(String uri, Object attribute)
	{
		List<String> list = (List<String>) attribute;
		boolean flag = false;
		for (Iterator iterator = list.iterator(); iterator.hasNext();)
		{
			String url = (String) iterator.next();
			if(!StringUtils.isEmpty(url)&&uri.indexOf(url)>-1){
				flag = true;
				break;
			}
		}
		return flag;
	}

	@Override
	public void destroy() {
	}

	/**
	 * 判断不需要过滤的jsp
	 * 
	 * @param uri
	 * @return
	 */
	private boolean passCheck(String uri) {
		boolean flag = false;
		// 不是login.jsp 和 index.jsp 则判断用户信息
		if (!uri.endsWith("login.jsp") && !uri.endsWith("index.jsp") && !uri.endsWith("error.jsp")) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 坚持用户是否含有url权限
	 * 
	 * @param uri
	 * @param userInfo
	 * @return
	 */
	private boolean checkUrlPerssion(String uri, UserInfoBean userInfo) {
		uri = uri.split("/")[3] + "/" + uri.split("/")[4];
		// 判断请求的url是否需要判断权限
		if (Constant.SYSTEM_URL_LIST.contains(uri)) {
			// 判断用户是否含有此权限
			String authUrlStr = userInfo.getAuthUrlStr();
			log.info("-----------------------"+authUrlStr+"----------------------");
			if (authUrlStr.indexOf(uri) > -1) {
				return false;
			}
			return true;
		}
		return false;
	}

}
