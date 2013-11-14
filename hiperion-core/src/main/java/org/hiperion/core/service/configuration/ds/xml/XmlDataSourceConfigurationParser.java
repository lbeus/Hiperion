package org.hiperion.core.service.configuration.ds.xml;

import org.apache.log4j.Logger;
import org.hiperion.connector.model.ParameterContext;
import org.hiperion.core.service.configuration.AbstractXmlConfigurationParser;
import org.hiperion.core.util.helpers.ConfigurationParserHelper;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * User: iobestar
 * Date: 14.04.13.
 * Time: 11:40
 */
public class XmlDataSourceConfigurationParser extends AbstractXmlConfigurationParser {

    private static final Logger LOGGER = Logger.getLogger(XmlDataSourceConfigurationParser.class);

    public XmlDataSourceConfigurationParser(File dataSourceXmlFile) {
        super(dataSourceXmlFile);
    }

    public XmlDataSourceConfigurationParser(String xml) {
        super(xml);
    }

    public String getDataSourceId() {
        String dataSourceId = getXmlDocument().getDocumentElement().getAttribute("id");
        return dataSourceId;
    }

    public String getDescription() {
        Node descriptionNode = getXmlDocument().getElementsByTagName("description").item(0).getFirstChild();
        return (null == descriptionNode) ? "" : descriptionNode.getTextContent();
    }

    public String getLatitude() {
        Node latitudeNode = getXmlDocument().getElementsByTagName("latitude").item(0).getFirstChild();
        return (null == latitudeNode) ? "" : latitudeNode.getTextContent();
    }

    public String getLongitude() {
        Node longitudeNode = getXmlDocument().getElementsByTagName("longitude").item(0).getFirstChild();
        return (null == longitudeNode) ? "" : longitudeNode.getTextContent();
    }

    public String getDataSourceServiceId() {
        Element serviceElement = (Element) getXmlDocument().getElementsByTagName("service").item(0);
        return serviceElement.getAttribute("id");
    }

    public String getNodeId() {
        Element serviceElement = (Element) getXmlDocument().getElementsByTagName("service").item(0);
        return serviceElement.getAttribute("node_id");
    }

    public ParameterContext getParameterContext() {

        Node parametersNode = getXmlDocument().getElementsByTagName("parameters").item(0);
        if (null != parametersNode){
            return ConfigurationParserHelper.buildParameterContext(parametersNode);
        }

        return new ParameterContext(new HashMap<String, String>(),
                new HashMap<String, List<String>>(), new HashMap<String, Map<String, String>>());
    }
}
