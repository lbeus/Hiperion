package org.hiperion.core.service.configuration;

import org.apache.log4j.Logger;
import org.hiperion.core.service.configuration.ds.xml.XmlDataSourceConfigurationParser;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;

/**
 * User: iobestar
 * Date: 05.05.13.
 * Time: 17:56
 */
public abstract class AbstractXmlConfigurationParser {

    private static final Logger LOGGER = Logger.getLogger(XmlDataSourceConfigurationParser.class);

    private Document xmlDocument;

    public AbstractXmlConfigurationParser(File dataSourceXmlFile) {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;

        try {
            dBuilder = dbFactory.newDocumentBuilder();
            this.xmlDocument = dBuilder.parse(dataSourceXmlFile);
            this.xmlDocument.normalize();
        } catch (ParserConfigurationException e) {
            LOGGER.error(e);
        } catch (SAXException e) {
            LOGGER.error(e);
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    public AbstractXmlConfigurationParser(String xml) {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;

        try {
            dBuilder = dbFactory.newDocumentBuilder();
            this.xmlDocument = dBuilder.parse(new InputSource(new StringReader(xml)));
            this.xmlDocument.normalize();
        } catch (ParserConfigurationException e) {
            LOGGER.error(e);
        } catch (SAXException e) {
            LOGGER.error(e);
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    public Document getXmlDocument() {
        return xmlDocument;
    }
}
