package org.hiperion.core.config;

import org.cometd.annotation.ServerAnnotationProcessor;
import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.server.BayeuxServerImpl;
import org.hiperion.core.service.collecting.CollectingOutput;
import org.hiperion.core.service.processing.actions.event.publisher.EventPublisher;
import org.hiperion.core.service.processing.actions.event.publisher.PublishEvent;
import org.hiperion.core.service.processing.actions.publish.OutputPublisher;
import org.hiperion.core.service.processing.actions.publish.comet.CometOutputPublisher;
import org.hiperion.core.service.processing.util.CometConfigurator;
import org.hiperion.core.service.processing.actions.event.publisher.comet.CometEventPublisher;
import org.hiperion.core.service.processing.actions.store.MongoStoreService;
import org.hiperion.core.service.processing.actions.store.StoreService;
import org.hiperion.core.service.processing.util.CometPublisher;
import org.hiperion.core.service.repository.collectingoutput.mongo.CollectingOutputDataServiceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * User: iobestar
 * Date: 25.05.13.
 * Time: 12:54
 */
@Configuration
@ComponentScan(basePackages = "org.hiperion.core.service.processing.actions")
public class ProcessingActionContextSpringConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public StoreService storeService(){
        MongoStoreService mongoStoreService = new
                MongoStoreService(applicationContext.getBean(CollectingOutputDataServiceContext.class));
        return mongoStoreService;
    }

    @Bean
    public ServerAnnotationProcessor serverAnnotationProcessor(){
        ServerAnnotationProcessor serverAnnotationProcessor =
                new ServerAnnotationProcessor(bayeuxServer());
        return serverAnnotationProcessor;
    }

    @Bean(name ="bayeux",initMethod = "start", destroyMethod = "stop")
    public BayeuxServer bayeuxServer() {
        BayeuxServerImpl bayeuxServer = new BayeuxServerImpl();
        return bayeuxServer;
    }

    @Bean
    public CometConfigurator cometConfigurator(){
        CometConfigurator cometConfigurator = new CometConfigurator(bayeuxServer(),
                serverAnnotationProcessor());
        return cometConfigurator;
    }

    @Bean
    public CometPublisher<CollectingOutput> outputCometPublisher(){
        CometPublisher<CollectingOutput> outputCometPublisher =
                new CometPublisher<CollectingOutput>(bayeuxServer());
        return outputCometPublisher;
    }

    @Bean
    @DependsOn(value = "cometConfigurator")
    public EventPublisher eventPublisher(){
        CometEventPublisher cometEventPublisher =
                new CometEventPublisher(eventCometPublisher());
        return cometEventPublisher;
    }

    @Bean
    public CometPublisher<PublishEvent> eventCometPublisher(){
        CometPublisher<PublishEvent> eventCometPublisher =
                new CometPublisher<PublishEvent>(bayeuxServer());
        return eventCometPublisher;
    }

    @Bean
    public OutputPublisher outputPublisher(){
        CometOutputPublisher cometOutputPublisher =
                new CometOutputPublisher(outputCometPublisher());
        return cometOutputPublisher;
    }


}
