package org.hiperion.node.config;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlet.ServletMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 06.04.13.
 * Time: 15:55
 */
@Configuration
public class HiperionNodeJettyServerConfig {

    @Autowired
    ApplicationContext applicationContext;

    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server jettyServer() {
        Server server = new Server();
        server.addConnector(serverConnector());
        server.setHandler(context());
        return server;
    }

    @Bean
    public SelectChannelConnector serverConnector() {
        SelectChannelConnector selectChannelConnector =
                new SelectChannelConnector();
        selectChannelConnector.setHost(System.getProperty("jetty.server.host"));
        selectChannelConnector.setPort(Integer.parseInt(System.getProperty("jetty.server.port")));
        selectChannelConnector.setMaxIdleTime(Integer.parseInt(System.getProperty("jetty.server.idle.timeout")));
        selectChannelConnector.setAcceptQueueSize(Integer.parseInt(System.getProperty("jetty.server.accept.queue.size")));
        return selectChannelConnector;
    }

    @Bean
    public ServletContextHandler context() {
        ServletContextHandler servletContextHandler = new ServletContextHandler();
        servletContextHandler.setContextPath(System.getProperty("jetty.server.servlet.context.path"));
        servletContextHandler.setSessionHandler(new SessionHandler());
        servletContextHandler.setResourceBase(".");
        servletContextHandler.setServletHandler(servletHandler());
        return servletContextHandler;
    }

    @Bean
    public ServletHandler servletHandler() {
        ServletHandler servletHandler = new ServletHandler();
        servletHandler.addServlet(servletHolder());
        servletHandler.addServletMapping(servletMapping());
        return servletHandler;

    }

    @Bean
    public ServletHolder servletHolder() {
        ServletHolder servletHolder = new ServletHolder();
        servletHolder.setName("DefaultServlet");
        servletHolder.setInitParameter("contextClass", "org.springframework.web.context.support.AnnotationConfigWebApplicationContext");
        servletHolder.setInitParameter("contextConfigLocation",
                "org.hiperion.node.config.HiperionNodeSpringConfig,org.hiperion.node.config.HiperionNodeSpringServerConfig");
        servletHolder.setServlet(dispatcherServlet());
        servletHolder.setInitOrder(1);
        return servletHolder;
    }

    @Bean
    public DispatcherServlet dispatcherServlet() {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        return dispatcherServlet;
    }

    @Bean
    public ServletMapping servletMapping() {
        ServletMapping servletMapping = new ServletMapping();
        servletMapping.setPathSpec(System.getProperty("jetty.server.dispatcher.path"));
        servletMapping.setServletName("DefaultServlet");
        return servletMapping;
    }
}
