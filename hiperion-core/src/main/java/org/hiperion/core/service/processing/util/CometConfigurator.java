package org.hiperion.core.service.processing.util;

import org.cometd.annotation.ServerAnnotationProcessor;
import org.cometd.bayeux.server.BayeuxServer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

/**
 * User: iobestar
 * Date: 25.05.13.
 * Time: 13:16
 */
public class CometConfigurator implements DestructionAwareBeanPostProcessor, ServletContextAware {

    private BayeuxServer bayeuxServer;
    private ServerAnnotationProcessor serverAnnotationProcessor;

    public CometConfigurator(BayeuxServer bayeuxServer, ServerAnnotationProcessor serverAnnotationProcessor) {
        this.bayeuxServer = bayeuxServer;
        this.serverAnnotationProcessor = serverAnnotationProcessor;
    }

    @Override
    public void postProcessBeforeDestruction(Object bean, String name) throws BeansException {
        serverAnnotationProcessor.deprocessCallbacks(bean);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String name) throws BeansException {
        System.out.println("Configuring service " + name);
        serverAnnotationProcessor.processDependencies(bean);
        serverAnnotationProcessor.processConfigurations(bean);
        serverAnnotationProcessor.processCallbacks(bean);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String name) throws BeansException {
        return bean;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        servletContext.setAttribute(BayeuxServer.ATTRIBUTE, bayeuxServer);
    }
}
