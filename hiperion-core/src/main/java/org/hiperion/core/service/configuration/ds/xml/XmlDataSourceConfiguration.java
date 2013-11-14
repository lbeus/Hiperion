package org.hiperion.core.service.configuration.ds.xml;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.hiperion.common.exception.DataSourceExist;
import org.hiperion.common.exception.DataSourceNotFound;
import org.hiperion.common.exception.EntityDoesNotExist;
import org.hiperion.connector.model.ParameterContext;
import org.hiperion.core.model.data.source.DataSource;
import org.hiperion.core.model.data.source.DataSourceFactory;
import org.hiperion.core.service.configuration.dc.xml.XmlDataCollectorConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: iobestar
 * Date: 13.04.13.
 * Time: 15:21
 */
public class XmlDataSourceConfiguration implements DataSourceConfiguration {

    private final static Logger LOGGER = Logger.getLogger(XmlDataCollectorConfiguration.class);

    private final ConcurrentHashMap<String, DataSource> dataSources;
    private final File storeDirectory;
    private final DataSourceFactory dataSourceFactory;

    public XmlDataSourceConfiguration(File storeDirectory, DataSourceFactory dataSourceFactory) {
        this.dataSources = new ConcurrentHashMap<String, DataSource>();
        this.storeDirectory = storeDirectory;
        this.dataSourceFactory = dataSourceFactory;
    }

    @Override
    public void init() {
        if (!storeDirectory.isDirectory()) {
            return;
        }

        for (File xmlFile : storeDirectory.listFiles()) {
            if (xmlFile.isFile()) {
                XmlDataSourceConfigurationParser xmlDataSourceParser = new
                        XmlDataSourceConfigurationParser(xmlFile);
                String dataSourceId = xmlDataSourceParser.getDataSourceId();
                dataSources.putIfAbsent(dataSourceId, createDataSource(xmlDataSourceParser));
            }
        }
    }

    @Override
    public void addConfiguration(String dataSourceXml) throws DataSourceExist {
        try {
            XmlDataSourceConfigurationParser xmlDataSourceParser = new
                    XmlDataSourceConfigurationParser(dataSourceXml);

            String dataSourceId = xmlDataSourceParser.getDataSourceId();
            DataSource dataSource = dataSources.get(dataSourceId);
            if (null != dataSource) {
                removeConfiguration(dataSourceId);
            }

            FileUtils.writeStringToFile(new File(storeDirectory, dataSourceId + ".xml"), dataSourceXml);
            reloadConfiguration(dataSourceId);
        } catch (IOException e) {
            LOGGER.error(e);
        } catch (EntityDoesNotExist e) {
            LOGGER.error(e);
        }
    }

    @Override
    public void removeConfiguration(String dataSourceId) {
        DataSource dataSource = dataSources.get(dataSourceId);

        if (null == dataSource) {
            return;
        }
        File dataSourceXmlFile = new File(storeDirectory, dataSourceId + ".xml");
        if (dataSourceXmlFile.exists() && dataSourceXmlFile.isFile()) {
            dataSourceXmlFile.delete();
        }
        dataSources.remove(dataSourceId);
    }

    @Override
    public void reloadConfiguration(String key) throws DataSourceNotFound {
        File dataSourceXmlFile = new File(storeDirectory, key + ".xml");
        if (!dataSourceXmlFile.exists() || !dataSourceXmlFile.isFile()) {
            throw new DataSourceNotFound();
        }

        XmlDataSourceConfigurationParser xmlDataSourceParser = new
                XmlDataSourceConfigurationParser(dataSourceXmlFile);
        String dataSourceId = xmlDataSourceParser.getDataSourceId();
        dataSources.put(dataSourceId, createDataSource(xmlDataSourceParser));
    }

    @Override
    public DataSource findById(String id) throws DataSourceNotFound {
        DataSource dataSource = dataSources.get(id);
        if (null == dataSource) {
            throw new DataSourceNotFound();
        }

        return dataSource;
    }

    @Override
    public Collection<DataSource> findAll() {
        return dataSources.values();
    }

    private DataSource createDataSource(XmlDataSourceConfigurationParser xmlDataSourceParser) {
        String dataSourceId = xmlDataSourceParser.getDataSourceId();
        String nodeId = xmlDataSourceParser.getNodeId();
        String dataSourceServiceId = xmlDataSourceParser.getDataSourceServiceId();
        ParameterContext parameterContext = xmlDataSourceParser.getParameterContext();
        String description = xmlDataSourceParser.getDescription();
        String latitude = xmlDataSourceParser.getLatitude();
        String longitude = xmlDataSourceParser.getLongitude();
        return dataSourceFactory.create(dataSourceId, nodeId, dataSourceServiceId, parameterContext, description, latitude, longitude);
    }

    @Override
    public String xmlConfigurationContent(String configurationId) throws DataSourceNotFound {
        DataSource dataSource = dataSources.get(configurationId);
        if(null == dataSource){
            throw new DataSourceNotFound();
        }

        File dataSourceXmlFile = new File(storeDirectory, configurationId + ".xml");
        try {
            return FileUtils.readFileToString(dataSourceXmlFile);
        } catch (IOException e) {
            LOGGER.error(e);
        }
        return null;
    }
}
