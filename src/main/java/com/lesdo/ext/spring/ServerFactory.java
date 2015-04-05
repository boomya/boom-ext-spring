package com.lesdo.ext.spring;

import com.lesdo.ext.spring.thread.ExecutorServices;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.util.thread.ExecutorThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Created by jiangshan on 15/3/23.
 */
public class ServerFactory {

    private static Server server;

    /**
     * 创建用于开发运行调试的Jetty Server, 以src/main/webapp为Web应用目录.
     */
    private static Server createServerInSource(int port, String contextPath,
                                               String webappRootPath, String webappPath) {

        Server server = new Server(
                new ExecutorThreadPool(ExecutorServices.createExecutorService("standalone")));
        // 设置在JVM退出时关闭Jetty的钩子。
        server.setStopAtShutdown(false);

        //这是http的连接器
        ServerConnector connector = new ServerConnector(server);
        connector.setAcceptQueueSize(1024);
        connector.setPort(port);
//        // 解决Windows下重复启动Jetty居然不报告端口冲突的问题.
//        connector.setReuseAddress(false);
        server.setConnectors(new Connector[] { connector });

        WebAppContext webContext = new WebAppContext(webappRootPath + "/WEB-INF", contextPath);
        webContext.setInitParameter("org.eclipse.jetty.servlet.Default.dirAllowed", "false");
        webContext.setDescriptor(webappPath);
        // 设置webapp的位置
        webContext.setResourceBase(webappRootPath);
        webContext.setClassLoader(Thread.currentThread().getContextClassLoader());

//        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(
//                webContext.getServletContext());
//        ServiceLocator.init(webApplicationContext);

        // Create a handler list to store our static and servlet context handlers.
        HandlerList handlers = new HandlerList();
//        handlers.setHandlers(new Handler[] { staticContextHandler, webContext });
        handlers.setHandlers(new Handler[] { webContext });

//        server.setHandler(webContext);
//        // Add the handlers to the server and start jetty.
        server.setHandler(handlers);

        return server;
    }

    /**
     * 启动jetty服务
     */
    public static void startJetty(int port, String contextPath, String webappRootPath, String webappPath) {
        server = createServerInSource(port, contextPath, webappRootPath, webappPath);

        try {
            server.stop();
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }


}
