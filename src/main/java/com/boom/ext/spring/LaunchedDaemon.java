package com.boom.ext.spring;

import com.boom.ext.spring.thread.ExecutorServices;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.util.thread.ExecutorThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by jiangshan on 15/4/2.
 */
public class LaunchedDaemon implements ApplicationContextAware {

    private Server             server;
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;

        ServiceLocator.init(applicationContext);
    }

    public void start() {
        String webappRootPath = LaunchedDaemon.class.getClassLoader().getResource(".").getPath();
        String webappPath = webappRootPath + "WEB-INF/web.xml";

        server = new Server();

        Server server = new Server(new ExecutorThreadPool(ExecutorServices.createExecutorService("standalone")));
        // 设置在JVM退出时关闭Jetty的钩子。
        server.setStopAtShutdown(false);

        //这是http的连接器
        ServerConnector connector = new ServerConnector(server);
        connector.setAcceptQueueSize(1024);
        connector.setPort(8080);
        server.setConnectors(new Connector[] { connector });

//        WebAppContext webContext = new WebAppContext(webappRootPath + "/WEB-INF", contextPath);
        WebAppContext webContext = new WebAppContext();
        webContext.setContextPath("/standalone");

        webContext.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false");
        webContext.setDescriptor(webappPath);
        // 设置webapp的位置
        webContext.setResourceBase(webappRootPath);
//        webContext.setClassLoader(Thread.currentThread().getContextClassLoader());
        webContext.setClassLoader(applicationContext.getClassLoader());

        webContext.setConfigurationDiscovered(true);
        webContext.setParentLoaderPriority(true);

        // Create a handler list to store our static and servlet context handlers.
        HandlerList handlers = new HandlerList();
//        handlers.setHandlers(new Handler[] { staticContextHandler, webContext });
        handlers.setHandlers(new Handler[] { webContext });

//        server.setHandler(webContext);
//        // Add the handlers to the server and start jetty.
        server.setHandler(handlers);

//        // 以下代码是关键
//        XmlWebApplicationContext xmlWebAppContext = new XmlWebApplicationContext();
//        xmlWebAppContext.setParent(applicationContext);
//        xmlWebAppContext.setConfigLocation("");
//        xmlWebAppContext.setServletContext(webContext.getServletContext());
//        xmlWebAppContext.refresh();

        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destory() {
        server.setStopAtShutdown(true);
    }

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
    }
}
