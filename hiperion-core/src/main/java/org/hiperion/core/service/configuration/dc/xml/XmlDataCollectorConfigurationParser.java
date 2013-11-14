package org.hiperion.core.service.configuration.dc.xml;

import org.apache.log4j.Logger;
import org.hiperion.core.model.data.collector.DataCollectorType;
import org.hiperion.core.model.data.collector.blocks.SelectStatement;
import org.hiperion.core.model.data.collector.blocks.SelectorBlock;
import org.hiperion.core.service.configuration.AbstractXmlConfigurationParser;
import org.hiperion.core.service.configuration.ds.xml.XmlDataSourceConfigurationParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 14.04.13.
 * Time: 21:45
 */
public class XmlDataCollectorConfigurationParser extends AbstractXmlConfigurationParser {

    private static final Logger LOGGER = Logger.getLogger(XmlDataSourceConfigurationParser.class);

    public XmlDataCollectorConfigurationParser(File dataCollectorXmlFile) {
        super(dataCollectorXmlFile);
    }

    public XmlDataCollectorConfigurationParser(String xml) {
        super(xml);
    }

    public String getDataCollectorId() {
        Document document = getXmlDocument();
        String dataCollectorId = document.getDocumentElement().getAttribute("id");
        return dataCollectorId;
    }

    public DataCollectorType getDataCollectorType() {
        String dataCollectorTypeString = getXmlDocument().getDocumentElement().getAttribute("type");
        return DataCollectorType.getDataCollectorType(dataCollectorTypeString);
    }

    public String getCronExpression() {
        String cronExpression = getXmlDocument().getDocumentElement().getAttribute("cron_expression");
        return cronExpression;
    }

    public String getDescription() {
        Node descriptionNode = getXmlDocument().getElementsByTagName("description").item(0).getFirstChild();
        return (null == descriptionNode) ? "" : descriptionNode.getNodeValue();
    }

    public String getAcquisitionBlockName() {
        Node acquisitionBlockNode = getXmlDocument().getElementsByTagName("acquisition-block").item(0);
        String acquisitionBlockName = acquisitionBlockNode.getAttributes().getNamedItem("name").getNodeValue();
        return acquisitionBlockName;
    }

    public String[] getDataSources() {
        NodeList dataSourcesNodeList = getXmlDocument().getElementsByTagName("data-source");
        String[] dataSources = new String[dataSourcesNodeList.getLength()];
        for (int i = 0; i < dataSourcesNodeList.getLength(); i++) {
            Element dataSourceNode = (Element) dataSourcesNodeList.item(i);
            dataSources[i] = dataSourceNode.getFirstChild().getNodeValue();
        }

        return dataSources;
    }

    public List<SelectorBlock> getSelectorBlocks() {

        NodeList selectorsBlockNodeList = getXmlDocument().getElementsByTagName("selector");
        List<SelectorBlock> selectorBlocks = new LinkedList<SelectorBlock>();

        for (int i = 0; i < selectorsBlockNodeList.getLength(); i++) {
            Element selectorNode = (Element) selectorsBlockNodeList.item(i);
            NodeList selectStatementNodeList = selectorNode.getElementsByTagName("select-statement");

            List<SelectStatement> selectStatements = new LinkedList<SelectStatement>();
            for (int j = 0; j < selectStatementNodeList.getLength(); j++) {
                Element selectStatementNode = (Element) selectStatementNodeList.item(j);
                String dataSourceId = selectStatementNode.getElementsByTagName("data-source-name").item(0).getTextContent();
                String fieldName = selectStatementNode.getElementsByTagName("data-field-name").item(0).getTextContent();
                NodeList outputDataFieldNameElement = selectStatementNode.getElementsByTagName("output-data-field-name");
                String outputFieldName = null;
                if (outputDataFieldNameElement.getLength() > 0) {
                    outputFieldName = selectStatementNode.getElementsByTagName("output-data-field-name").item(0).getTextContent();
                }
                SelectStatement selectStatement = new SelectStatement(dataSourceId, fieldName, outputFieldName);
                selectStatements.add(selectStatement);
            }

            String selectorName = selectorNode.getAttribute("name");
            SelectorBlock selectorBlock = new SelectorBlock(selectorName, selectStatements);
            selectorBlocks.add(selectorBlock);
        }

        return selectorBlocks;
    }
}
