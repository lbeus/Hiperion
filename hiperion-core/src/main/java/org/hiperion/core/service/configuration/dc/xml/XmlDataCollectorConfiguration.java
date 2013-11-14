package org.hiperion.core.service.configuration.dc.xml;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.hiperion.common.exception.DataCollectorDeployed;
import org.hiperion.common.exception.DataCollectorExist;
import org.hiperion.common.exception.DataCollectorNotFound;
import org.hiperion.common.exception.HiperionException;
import org.hiperion.core.model.data.collector.*;
import org.hiperion.core.model.data.collector.blocks.SelectorBlock;
import org.hiperion.core.service.collecting.CollectingService;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 13.04.13.
 * Time: 15:16
 */
public class XmlDataCollectorConfiguration implements DataCollectorConfiguration {

    private static final Logger LOGGER = Logger.getLogger(XmlDataCollectorConfiguration.class);

    private final ConcurrentHashMap<String, DataCollector> dataCollectors;
    private final DataCollectorFactory dataCollectorFactory;
    private final File storeDirectory;


    public XmlDataCollectorConfiguration(DataCollectorFactory dataCollectorFactory,
                                         File storeDirectory) {
        this.dataCollectors = new ConcurrentHashMap<String, DataCollector>();
        this.storeDirectory = storeDirectory;
        this.dataCollectorFactory = dataCollectorFactory;
    }

    @Override
    public void init() {

        if (!storeDirectory.isDirectory()) {
            return;
        }
        for (File xmlFile : storeDirectory.listFiles()) {
            if (xmlFile.isFile()) {
                XmlDataCollectorConfigurationParser xmlDataCollectorConfigurationParser = new
                        XmlDataCollectorConfigurationParser(xmlFile);
                String dataCollectorId = xmlDataCollectorConfigurationParser.getDataCollectorId();
                if (xmlDataCollectorConfigurationParser.getDataCollectorType() == DataCollectorType.PULL) {
                    dataCollectors.putIfAbsent(dataCollectorId, createPullDataCollector(xmlDataCollectorConfigurationParser));
                    continue;
                }
                dataCollectors.putIfAbsent(dataCollectorId, createPushDataCollector(xmlDataCollectorConfigurationParser));
            }
        }
    }

    @Override
    public void addConfiguration(String dataCollectorXml) throws HiperionException {
        try {
            XmlDataCollectorConfigurationParser xmlDataCollectorConfigurationParser = new
                    XmlDataCollectorConfigurationParser(dataCollectorXml);
            String dataCollectorId = xmlDataCollectorConfigurationParser.getDataCollectorId();

            DataCollector dataCollector = dataCollectors.get(dataCollectorId);
            if (null != dataCollector) {
                removeConfiguration(dataCollectorId);
            }

            FileUtils.writeStringToFile(new File(storeDirectory, dataCollectorId + ".xml"), dataCollectorXml);
            reloadConfiguration(dataCollectorId);
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    @Override
    public void removeConfiguration(String dataCollectorId) throws HiperionException{
        DataCollector dataCollector = dataCollectors.get(dataCollectorId);
        if (null == dataCollector) {
            return;
        }

        File dataCollectorXmlFile = new File(storeDirectory, dataCollectorId + ".xml");
        if (dataCollectorXmlFile.exists() && dataCollectorXmlFile.isFile()) {
            dataCollectorXmlFile.delete();
        }
        dataCollectors.remove(dataCollectorId);
    }

    @Override
    public void reloadConfiguration(String key) throws HiperionException {
        File dataCollectorXmlFile = new File(storeDirectory, key + ".xml");
        if (!dataCollectorXmlFile.exists() || !dataCollectorXmlFile.isFile()) {
            throw new DataCollectorNotFound();
        }

        XmlDataCollectorConfigurationParser xmlDataCollectorConfigurationParser = new
                XmlDataCollectorConfigurationParser(dataCollectorXmlFile);

        String dataCollectorId = xmlDataCollectorConfigurationParser.getDataCollectorId();

        if (xmlDataCollectorConfigurationParser.getDataCollectorType() == DataCollectorType.PULL) {
            dataCollectors.put(dataCollectorId, createPullDataCollector(xmlDataCollectorConfigurationParser));
            return;
        }

        dataCollectors.put(dataCollectorId, createPushDataCollector(xmlDataCollectorConfigurationParser));
    }

    @Override
    public DataCollector findById(String id) throws DataCollectorNotFound {
        DataCollector dataCollector = dataCollectors.get(id);
        if (null == dataCollector) {
            throw new DataCollectorNotFound();
        }
        return dataCollector;
    }

    @Override
    public Collection<DataCollector> findAll() {
        return dataCollectors.values();
    }

    private PullDataCollector createPullDataCollector(XmlDataCollectorConfigurationParser xmlDataCollectorParser) {
        String dataCollectorId = xmlDataCollectorParser.getDataCollectorId();
        String description = xmlDataCollectorParser.getDescription();
        String cronExpression = xmlDataCollectorParser.getCronExpression();
        String acquisitionName = xmlDataCollectorParser.getAcquisitionBlockName();
        String[] dataSources = xmlDataCollectorParser.getDataSources();
        List<SelectorBlock> selectorBlocks = xmlDataCollectorParser.getSelectorBlocks();

        return dataCollectorFactory.createPullDataCollector(dataCollectorId, description, cronExpression, acquisitionName, dataSources, selectorBlocks);
    }

    @Override
    public String xmlConfigurationContent(String configurationId) throws DataCollectorNotFound{
        DataCollector dataCollector = dataCollectors.get(configurationId);
        if(null == dataCollector){
            throw new DataCollectorNotFound();
        }

        File dataCollectorXmlFile = new File(storeDirectory, configurationId + ".xml");
        try {
            return FileUtils.readFileToString(dataCollectorXmlFile);
        } catch (IOException e) {
            LOGGER.error(e);
        }
        return null;
    }

    private PushDataCollector createPushDataCollector(XmlDataCollectorConfigurationParser xmlDataCollectorParser) {
        String dataCollectorId = xmlDataCollectorParser.getDataCollectorId();
        String description = xmlDataCollectorParser.getDescription();
        List<SelectorBlock> selectorBlocks = xmlDataCollectorParser.getSelectorBlocks();
        return dataCollectorFactory.createPushDataCollector(dataCollectorId, description, selectorBlocks);
    }
}
