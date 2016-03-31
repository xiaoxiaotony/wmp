package com.txy.web.frame.filter;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

public class SqlInjectFilter implements Filter {

	// SQL 注入敏感词列表
	private static List<String> sensWords = new ArrayList<String>();
	// Base64 加密参数key列表
	private static List<String> encrParams = new ArrayList<String>();
	// 错误页面
	private static String error = "/sqlInjectError.jsp";
	// 调试开关
	private static boolean debug = true;
	
	private static final Logger log = Logger.getLogger(SqlInjectFilter.class);

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain fc) throws IOException, ServletException {
		if (debug) {
			log.debug("prevent sql inject filter works");
		}
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		request.setCharacterEncoding("UTF-8");
		Set<String> keys = request.getParameterMap().keySet();
		for (String key : keys) {
			String value = request.getParameter(key);
			if (encrParams.contains(key)) {
				value = new String(Base64.decodeBase64(value.getBytes()));
			}
			if (debug) {
				log.debug(MessageFormat.format("{0}={1}", key, value));
			}
			for (String word : sensWords) {
				if (value.toUpperCase().contains(word.toUpperCase())) {
					request.getSession().setAttribute("sqlInjectError", "the request parameter \"" + value + "\" contains keyword: \"" + word + "\"");
					response.sendRedirect(request.getContextPath() + error);
					return;
				}
			}
		}
		fc.doFilter(req, res);
	}

	@Override
	public void init(FilterConfig conf) throws ServletException {
		String sSensiWord = conf.getInitParameter("sensitive-words");
		String sEncryParam = conf.getInitParameter("encrypting-parameter-names");
		String errorPage = conf.getInitParameter("error-page");
		String de = conf.getInitParameter("debug");
		if (errorPage != null) {
			error = errorPage;
		}
		if (sSensiWord != null) {
			sensWords = Arrays.asList(sSensiWord.split(" "));
		}
		if (sEncryParam != null) {
			encrParams = Arrays.asList(sEncryParam.split(" "));
		}
		if (de != null && Boolean.parseBoolean(de)) {
			debug = true;
			log.debug("PreventSQLInject Filter staring...");
			log.debug("print filter details");
			log.debug("sensitive words as fllows (split with blank):");
			for (String s : sensWords) {
				System.out.print(s + " ");
			}
			log.debug("encrypting parameter key as fllows (split with blank):");
			for (String s : encrParams) {
				System.out.print(s + " ");
			}
			log.debug("error page as fllows");
			log.debug(error);
		}
	}

}