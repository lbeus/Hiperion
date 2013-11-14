package org.hiperion.core.config;

import org.apache.log4j.Logger;
import org.hiperion.common.exception.HiperionException;
import org.hiperion.connector.model.interfaces.NodeAction;
import org.hiperion.core.model.data.collector.DataCollector;
import org.hiperion.core.model.data.collector.DataCollectorFactory;
import org.hiperion.core.model.data.source.DataSourceFactory;
import org.hiperion.core.service.collecting.CollectingService;
import org.hiperion.core.service.collecting.executor.CachedThreadDataSourceReadExecutor;
import org.hiperion.core.service.collecting.executor.DataSourceReadExecutor;
import org.hiperion.core.service.collecting.manager.PullCollectingManager;
import org.hiperion.core.service.collecting.manager.PushCollectingManager;
import org.hiperion.core.service.collecting.manager.ScheduledPullCollectingManager;
import org.hiperion.core.service.configuration.dc.xml.DataCollectorConfiguration;
import org.hiperion.core.service.configuration.dc.xml.XmlDataCollectorConfiguration;
import org.hiperion.core.service.configuration.ds.xml.DataSourceConfiguration;
import org.hiperion.core.service.configuration.ds.xml.XmlDataSourceConfiguration;
import org.hiperion.core.service.configuration.ev.xml.EventConfiguration;
import org.hiperion.core.service.configuration.ev.xml.XmlEventConfiguration;
import org.hiperion.core.service.processing.CollectedDataProcessingService;
import org.hiperion.core.service.processing.CollectingOutputQueueProcessor;
import org.hiperion.core.service.processing.ProcessingActionContextProxy;
import org.hiperion.core.service.processing.config.StreamProcessingConfig;
import org.hiperion.core.service.processing.config.StreamProcessingConfigMongo;
import org.hiperion.core.service.repository.MongoDbConfiguration;
import org.hiperion.core.service.repository.collectingoutput.mongo.CollectingOutputDataServiceContext;
import org.hiperion.core.service.rmi.NodeRmiService;
import org.hiperion.core.service.rmi.RmiDynamicClientManager;
import org.hiperion.core.service.rmi.config.NodeDescriptor;
import org.hiperion.core.service.rmi.config.NodeDescriptorConfig;
import org.hiperion.core.service.rmi.config.NodeDescriptorConfigMongo;
import org.hiperion.core.util.Config;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 16.03.13.
 * Time: 18:13
 */
@Configuration
public class HiperionCoreSpringConfig {

    private static final Logger LOGGER = Logger.getLogger(HiperionCoreSpringConfig.class);

    @Bean
    public MongoDbConfiguration mongoDbConfiguration() {
        try {
            MongoDbConfiguration mongoDbConfiguration = new MongoDbConfiguration();
            return mongoDbConfiguration;
        } catch (Exception e) {
            LOGGER.error(e);
        }
        throw new RuntimeException();
    }

    @Bean
    public CollectingOutputDataServiceContext collectingOutputDataServiceContext() {
        CollectingOutputDataServiceContext collectingOutputDataServiceContext = new
                CollectingOutputDataServiceContext(mongoDbConfiguration());
        return collectingOutputDataServiceContext;
    }

    @Bean
    public CollectingService collectingService() {
        try {
            CollectingService collectingService = new CollectingService(pullCollectingManager(),
                    scheduledPullCollectingManager(), pushCollectingManager());
            Collection<DataCollector> dataCollectors  = dataCollectorConfiguration().findAll();
            for (DataCollector dataCollector : dataCollectors) {
                try {
                    collectingService.deployCollector(dataCollector);
                } catch (HiperionException e) {
                    LOGGER.error(e);
                    throw new RuntimeException();
                }
            }
            return collectingService;
        } catch (HiperionException e) {
            LOGGER.error(e);
        }
        throw new RuntimeException();
    }

    @Bean
    public PullCollectingManager pullCollectingManager() {
        PullCollectingManager pullCollectingManager = new PullCollectingManager();
        return pullCollectingManager;
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public ScheduledPullCollectingManager scheduledPullCollectingManager() {
        StdSchedulerFactory schedulerFactory = new StdSchedulerFactory();
        try {
            Scheduler scheduler = schedulerFactory.getScheduler();
            ScheduledPullCollectingManager scheduledPullCollectingManager = new
                    ScheduledPullCollectingManager(scheduler);
            return scheduledPullCollectingManager;
        } catch (SchedulerException e) {
            LOGGER.error(e);
        }
        throw new RuntimeException();
    }

    @Bean
    public PushCollectingManager pushCollectingManager() {
        PushCollectingManager pushCollectingManager = new PushCollectingManager();
        return pushCollectingManager;
    }

    @Bean(initMethod = "startService", destroyMethod = "stopService")
    public CollectedDataProcessingService collectedDataProcessingService() {
        final int NUMBER_OF_WORKERS = Config.DEFAULT_NUMBER_OF_PROCESSING_WORKERS;
        CollectingOutputQueueProcessor[] queueProcessors = new CollectingOutputQueueProcessor[NUMBER_OF_WORKERS];
        for (int i = 0; i < NUMBER_OF_WORKERS; i++) {
            queueProcessors[i] = new CollectingOutputQueueProcessor(streamProcessingConfig(),
                    processingActionContextProxy());
        }
        CollectedDataProcessingService collectedDataProcessingService = new
                CollectedDataProcessingService(queueProcessors);
        return collectedDataProcessingService;
    }

    @Bean
    public ProcessingActionContextProxy processingActionContextProxy() {
        ProcessingActionContextProxy processingActionContextProxy = new
                ProcessingActionContextProxy();
        return processingActionContextProxy;
    }

    @Bean
    public StreamProcessingConfig streamProcessingConfig() {
        try {
            StreamProcessingConfig streamProcessingConfig =
                    new StreamProcessingConfigMongo(mongoDbConfiguration());
            return streamProcessingConfig;
        } catch (HiperionException e) {
            LOGGER.error(e);
        }
        throw new RuntimeException();
    }

    @Bean
    public DataSourceReadExecutor dataSourceReadExecutor() {
        DataSourceReadExecutor dataSourceReadExecutor = new CachedThreadDataSourceReadExecutor();
        return dataSourceReadExecutor;
    }

    @Bean
    public NodeDescriptorConfig nodeDescriptorConfig() {
        try {
            NodeDescriptorConfigMongo nodeDescriptorConfigMongo =
                    new NodeDescriptorConfigMongo(mongoDbConfiguration());
            return nodeDescriptorConfigMongo;
        } catch (HiperionException e) {
            LOGGER.error(e);
        }
        throw new RuntimeException();
    }

    @Bean(initMethod = "init", destroyMethod = "dispose")
    public NodeRmiService nodeRmiService() {
        NodeRmiService nodeRmiService = new NodeRmiService(nodeServiceHiperionClientManager());
        List<NodeDescriptor> nodeDescriptorList = nodeDescriptorConfig().findAll();
        for (NodeDescriptor nodeDescriptor : nodeDescriptorList) {
            try {
                nodeRmiService.registerNode(nodeDescriptor);
            } catch (HiperionException e) {
                LOGGER.error(e);
                throw new RuntimeException();
            }
        }
        return nodeRmiService;
    }

    @Bean
    public RmiDynamicClientManager<NodeAction> nodeServiceHiperionClientManager() {
        RmiDynamicClientManager<NodeAction> nodeServiceHiperionClientManager = new
                RmiDynamicClientManager<NodeAction>();
        return nodeServiceHiperionClientManager;
    }

    @Bean
    public DataSourceFactory dataSourceFactory() {
        DataSourceFactory dataSourceFactory = new
                DataSourceFactory(nodeRmiService());
        return dataSourceFactory;
    }

    @Bean(initMethod = "init")
    public DataSourceConfiguration dataSourceConfiguration() {
        XmlDataSourceConfiguration xmlDataSourceConfiguration = new
                XmlDataSourceConfiguration(new File(System.getProperty("hiperion.core.ds.store.dir")), dataSourceFactory());
        return xmlDataSourceConfiguration;
    }

    @Bean
    public DataCollectorFactory dataCollectorFactory() {
        DataCollectorFactory dataCollectorFactory = new DataCollectorFactory(collectedDataProcessingService(),
                dataSourceReadExecutor(), dataSourceConfiguration());
        return dataCollectorFactory;
    }

    @Bean(initMethod = "init")
    public DataCollectorConfiguration dataCollectorConfiguration() {
        XmlDataCollectorConfiguration xmlDataCollectorConfiguration = new
                XmlDataCollectorConfiguration(dataCollectorFactory(),
                new File(System.getProperty("hiperion.core.dc.store.dir")));
        return xmlDataCollectorConfiguration;
    }

    @Bean(initMethod = "init")
    public EventConfiguration eventConfiguration() {
        XmlEventConfiguration xmlEventConfiguration = new
                XmlEventConfiguration(new File(System.getProperty("hiperion.core.ev.store.dir")));
        return xmlEventConfiguration;
    }
}
