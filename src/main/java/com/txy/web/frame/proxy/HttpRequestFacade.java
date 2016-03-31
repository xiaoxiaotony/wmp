package com.txy.web.frame.proxy;

import java.util.Enumeration;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.txy.common.api.Handler;
import com.txy.common.bean.Model;
import com.txy.common.bean.SystemModel;
import com.txy.common.bean.ViewModel;
import com.txy.common.constant.Constant;
import com.txy.web.frame.base.HandlerContainer;

/**
 * 处理所有/main下的http请求
 * 
 * @author fei
 */
public class HttpRequestFacade implements Controller
{
	
	@Autowired
	private HandlerContainer handlerContainer;
	
	private static final String SHOW_PAGE_METHOD = "page";
	
	private static final String ROOT = "/page/";
	
	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
		throws Exception
	{
		Model model = this.encapsulation_(request);
		String contentType = "application/json;charset=utf-8";
		response.setCharacterEncoding("UTF-8");
		if (Constant.DATA_FORMAT.xml.name().equals(model.get(Constant.RETURN_DATA_TYPE)))
		{
			contentType = "text/xml";
		}
		response.setContentType(contentType);
		String responseData = "";
		String path = request.getPathInfo();
		String arrays[] = path.split("/");
		if (arrays.length < 3)
		{
			String msg = "URL地址信息异常";
			responseData = "{\"success\":false,\"msg\":" + "\"" + msg + "\"}";
			response.getWriter().write(responseData);
			return null;
		}
		model.setResponse(response);
		model.getSystemModel().setHandler(arrays[1]);
		model.getSystemModel().setMethod(arrays[2]);
		
		// 拿到容器中的handler去执行
		Handler<String> handler = this.handlerContainer.getHandler(model.getSystemModel().getHandler());
		if (handler == null)
		{
			String msg = "未找到相对于的Handler[" + model.getSystemModel().getHandler() + "]";
			responseData = "{\"success\":false,\"msg\":" + "\"" + msg + "\"}";
			response.getWriter().write(responseData);
			return null;
		}
		Object obj = handler.execute(model);
		if (obj instanceof String)
		{
			this.send(model, handler, obj.toString());
			return null;
		}
		return (ModelAndView) obj;
	}
	
	/**
	 * 向客户端发送响应数据
	 * @param model
	 * @param handler
	 * @param responseData
	 * @throws Exception
	 */
	private void send(Model model, Handler<String> handler, String responseData)
		throws Exception
	{
		HttpServletRequest request = model.getRequest();
		HttpServletResponse response = model.getResponse();
		String method = model.getSystemModel().getMethod();
		if (method.equals(SHOW_PAGE_METHOD))
		{
			ViewModel viewModel = model.getViewModel();
			if (viewModel.getModel().equals("request"))
			{
				model.clear();
				response.sendRedirect(request.getContextPath() + "/" + viewModel.getPage());
				return;
			}
			model.clear();
			model = null;
			request.setAttribute("jsMap", viewModel.getJsMap());
			String path = ROOT + viewModel.getPage();
			request.getRequestDispatcher(path).forward(request, response);
		}
		// 跳转到指定handler页面
		else
		{
			model.clear();
			model = null;
			// 如果是下载文件判断
			if (!"null".equals(responseData))
			{
				response.getWriter().write(responseData);
			}
		}
	}
	
	public HandlerContainer getHandlerContainer()
	{
		return handlerContainer;
	}
	
	public void setHandlerContainer(HandlerContainer handlerContainer)
	{
		this.handlerContainer = handlerContainer;
	}
	
	/**
	 * 封装请求数据
	 * 
	 * @param request
	 * @return
	 */
	protected Model encapsulation_(HttpServletRequest request)
	{
		SystemModel systemModel = new SystemModel();
		Model model = new Model();
		String aoData = request.getParameter("param");
		if (!StringUtils.isEmpty(aoData))
		{
			model = this.parseAoData(model, aoData);
		}
		
		for (Enumeration<String> iter = request.getParameterNames(); iter.hasMoreElements();)
		{
			String key = iter.nextElement();
			String value = request.getParameter(key);
			key = key.trim();
			// 如果aoData则不再解析参数
			if (Constant.GRID_PARAM_KEY.equals(key))
			{
				continue;
			}
			value = value.trim();
			model.put(key, value);
		}
		// 封装查询字符串
		systemModel.setQueryString(request.getQueryString());
		systemModel.setRequestMethod(request.getMethod());
		systemModel.setRequestIp(request.getRemoteAddr());
		if("0:0:0:0:0:0:0:1".equals(request.getRemoteAddr())){
			systemModel.setRequestIp("127.0.0.1");
		}
		systemModel.setContextPath(request.getContextPath());
		systemModel.setRealPath(request.getSession().getServletContext().getRealPath("/"));
		systemModel.setRequestData(request.getParameterMap().toString());
		model.setRequest(request);
		model.setSystemModel(systemModel);
		return model;
	}
	
	
	/**
	 * 将datatable的数据发到model
	 * 
	 * @param model
	 * @param aoData
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Model parseAoData(Model model, String aoData)
	{
		JSONArray jsonArr = JSONArray.fromObject(aoData);
		Iterator<JSONObject> iterator = jsonArr.iterator();
		while (iterator.hasNext())
		{
			JSONObject jsonObj = iterator.next();
			// 如果内层还是数组的话，继续解析
			model.put(jsonObj.getString("name"), jsonObj.get("value"));
		}
		return model;
	}
	
}
