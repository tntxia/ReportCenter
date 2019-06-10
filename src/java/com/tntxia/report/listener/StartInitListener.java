package com.tntxia.report.listener;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;

import com.tntxia.report.cache.SystemCache;
import com.tntxia.web.util.Dom4jUtil;

@WebListener
public class StartInitListener implements ServletContextListener {

	private static final Logger logger = Logger
			.getLogger(StartInitListener.class);

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		logger.info("报表中心停止...");
	}
	
	@Override
	public void contextInitialized(ServletContextEvent event) {

		ServletContext context = event.getServletContext();
		
		System.out.println("==== OA 系统 初始化开始       =======");

		System.out.println("==== 加载数据库配置       =======");
		String filecenterConfigFile = context.getRealPath("/WEB-INF/config/filecenter.xml");
		Document doc = null;
		try {
			doc = Dom4jUtil.getDoc(filecenterConfigFile);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		Map<String,String> filecenter = new HashMap<String,String>();
		String host = Dom4jUtil.getProp(doc.getRootElement(), "host");
		String port = Dom4jUtil.getProp(doc.getRootElement(), "port");
		String contextPath = Dom4jUtil.getProp(doc.getRootElement(), "context-path");
		filecenter.put("host", host);
		filecenter.put("port", port);
		filecenter.put("contextPath", contextPath);
		SystemCache.filecenter = filecenter;

	}

}
