package org.hiperion.node.config;

import org.apache.log4j.Logger;
import org.hiperion.connector.model.interfaces.NodeAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 20.04.13.
 * Time: 13:17
 */
@Configuration
public class HiperionNodeSpringServerConfig {

    @Autowired
    ApplicationContext applicationContext;

    @Bean
    public HttpInvokerServiceExporter httpInvokerServiceExporter() {
        HttpInvokerServiceExporter httpInvokerServiceExporter = new
                HttpInvokerServiceExporter();
        httpInvokerServiceExporter.setService(applicationContext.getBean(NodeAction.class));
        httpInvokerServiceExporter.setServiceInterface(NodeAction.class);
        return httpInvokerServiceExporter;
    }

    @Bean
    public SimpleUrlHandlerMapping simpleUrlHandlerMapping() {
        SimpleUrlHandlerMapping simpleUrlHandlerMapping = new
                SimpleUrlHandlerMapping();
        Properties mappingProperties = new Properties();
        String path = System.getProperty("hiperion.node.action.service.path");
        mappingProperties.put(path, httpInvokerServiceExporter());
        simpleUrlHandlerMapping.setMappings(mappingProperties);
        return simpleUrlHandlerMapping;
    }
}
