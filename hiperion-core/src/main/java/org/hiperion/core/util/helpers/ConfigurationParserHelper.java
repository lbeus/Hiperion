package org.hiperion.core.util.helpers;

import org.apache.log4j.Logger;
import org.hiperion.connector.model.ParameterContext;
import org.hiperion.core.service.configuration.ds.xml.XmlDataSourceConfigurationParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * User: iobestar
 * Date: 11.05.13.
 * Time: 12:51
 */
public class ConfigurationParserHelper {

    private static final Logger LOGGER = Logger.getLogger(ConfigurationParserHelper.class);


    private ConfigurationParserHelper(){

    }

    public static ParameterContext buildParameterContext(Node parametersNode){
        Map<String, String> simpleParameters = new HashMap<String, String>();
        Map<String, List<String>> listParameters = new HashMap<String, List<String>>();
        Map<String, Map<String, String>> mapParameters = new HashMap<String, Map<String, String>>();

        NodeList parameterChildList = parametersNode.getChildNodes();
        for (int i = 0; i < parameterChildList.getLength(); i++) {
            Node childNode = parameterChildList.item(i);
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                Element parameterChild = (Element) childNode;
                if (parameterChild.getTagName().toLowerCase().equals("simple")) {
                    String parameterName = parameterChild.getAttribute("name");
                    String parameterValue = parameterChild.getTextContent();
                    simpleParameters.put(parameterName, parameterValue);
                    continue;
                }
                if (parameterChild.getTagName().toLowerCase().equals("list")) {
                    NodeList listChildList = parameterChild.getChildNodes();
                    List<String> parameterList = new LinkedList<String>();
                    for (int j = 0; j < listChildList.getLength(); j++) {
                        Node listChildNode = listChildList.item(j);
                        if (listChildNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element listChildElement = (Element) listChildNode;
                            if (listChildElement.getTagName().toLowerCase().equals("item")) {
                                parameterList.add(listChildElement.getTextContent());
                            }
                        }
                    }
                    listParameters.put(parameterChild.getAttribute("name"), parameterList);
                    continue;
                }

                if (parameterChild.getTagName().toLowerCase().equals("map")) {
                    NodeList listChildList = parameterChild.getChildNodes();
                    Map<String, String> parameterMap = new HashMap<String, String>();
                    for (int j = 0; j < listChildList.getLength(); j++) {
                        Node mapChildNode = listChildList.item(j);
                        if (mapChildNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element mapChildElement = (Element) mapChildNode;
                            if (mapChildElement.getTagName().toLowerCase().equals("entry")) {
                                parameterMap.put(mapChildElement.getAttribute("key"),
                                        mapChildElement.getAttribute("value"));
                            }
                        }
                    }
                    mapParameters.put(parameterChild.getAttribute("name"), parameterMap);
                }

            }
        }
        return new ParameterContext(simpleParameters, listParameters, mapParameters);
    }

    public static ParameterContext buildParameterContext(String xmlParameters){
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder;
            dBuilder = dbFactory.newDocumentBuilder();
            Document xmlParametersDocument = dBuilder.parse(new InputSource(new StringReader(xmlParameters)));
            xmlParametersDocument.normalize();
            Node parametersNode = xmlParametersDocument.getDocumentElement();
            return buildParameterContext(parametersNode);
        } catch (ParserConfigurationException e) {
            LOGGER.error(e);
        } catch (SAXException e) {
            LOGGER.error(e);
        } catch (IOException e) {
            LOGGER.error(e);
        }
        return null;
    }
}
