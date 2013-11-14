package org.hiperion.core.service.configuration.ev.xml;

import org.hiperion.connector.model.ParameterContext;
import org.hiperion.core.service.configuration.AbstractXmlConfigurationParser;
import org.hiperion.core.service.processing.actions.event.EventPostAction;
import org.hiperion.core.service.processing.actions.event.condition.EventCondition;
import org.hiperion.core.service.processing.actions.event.condition.impl.AbstractComplexEventCondition;
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
 * Date: 06.05.13.
 * Time: 20:10
 */
public class XmlEventConfigurationParser extends AbstractXmlConfigurationParser {

    public XmlEventConfigurationParser(File dataSourceXmlFile) {
        super(dataSourceXmlFile);
    }

    public XmlEventConfigurationParser(String xml) {
        super(xml);
    }

    public String getEventId(){
        String eventId = getXmlDocument().getDocumentElement().getAttribute("id");
        return eventId;
    }

    public boolean getPublishable(){
        boolean publishable = Boolean.valueOf(getXmlDocument().getDocumentElement().getAttribute("publishable"));
        return publishable;
    }

    public boolean getStateful(){
        boolean stateful = Boolean.valueOf(getXmlDocument().getDocumentElement().getAttribute("stateful"));
        return stateful;
    }

    public String getStreamId() {
        String streamId = getXmlDocument().getDocumentElement().getAttribute("stream_id");
        return streamId;
    }

    public String getDescription() {
        Element descriptionElement = (Element) getXmlDocument().getElementsByTagName("description").item(0);
        return (null == descriptionElement) ? "" : descriptionElement.getTextContent();
    }

    public EventCondition getRiseCondition(){
        Element riseConditionsElement = (Element) getXmlDocument().getElementsByTagName("rise-conditions").item(0);
        NodeList riseConditionNodeList = riseConditionsElement.getChildNodes();
        for(int i = 0; i < riseConditionNodeList.getLength(); i++){
            Node riseConditionNode = riseConditionNodeList.item(i);
            if(riseConditionNode.getNodeType() == Node.ELEMENT_NODE){
                if(isComplexCondition(riseConditionNode)){
                    return buildEventCondition(riseConditionNode);
                }
            }
        }
        return null;
    }

    private EventCondition buildEventCondition(Node complexConditionNode){
        AbstractComplexEventCondition eventCondition = ComplexConditionMapping.
                getComplexConditionMapping(complexConditionNode.getNodeName()).getEventCondition();

        NodeList conditionNodeList = complexConditionNode.getChildNodes();
        for(int i = 0; i < conditionNodeList.getLength(); i++) {
            Node conditionNode = conditionNodeList.item(i);
            if(conditionNode.getNodeType() == Node.ELEMENT_NODE){
                if(isComplexCondition(conditionNode)){
                    EventCondition simpleEventCondition = buildEventCondition(conditionNode);
                    eventCondition.addConditionOperator(simpleEventCondition);
                    continue;
                }
                Element conditionNodeElement = (Element) conditionNode;
                String fieldName = conditionNodeElement.getAttribute("field_name");
                String conditionValue = conditionNodeElement.getTextContent();
                SimpleConditionMapping simpleConditionMapping = SimpleConditionMapping.getSimpleConditionMapping(conditionNode.getNodeName());
                eventCondition.addConditionOperator(simpleConditionMapping.getEventCondition(fieldName,conditionValue));
            }
        }

        return eventCondition;
    }

    private boolean isComplexCondition(Node conditionNode){
        if(conditionNode.getNodeName().equals("or-condition") ||
                conditionNode.getNodeName().equals("and-condition")){
            return true;
        }
        return false;
    }

    public List<EventPostAction> getEventPostActions(){
        Element postActionsElement = (Element) getXmlDocument().getElementsByTagName("post-actions").item(0);
        List<EventPostAction> eventPostActions = new LinkedList<EventPostAction>();

        NodeList postActionNodeList = postActionsElement.getChildNodes();
        for(int i = 0;i < postActionNodeList.getLength(); i++){
            Node postActionNode = postActionNodeList.item(i);
            if(postActionNode.getNodeType() == Node.ELEMENT_NODE){
                Element postActionNodeElement = (Element) postActionNode;
                String actionId = postActionNodeElement.getAttribute("id");
                String nodeId = postActionNodeElement.getAttribute("node_id");
                Node parametersNode = postActionNodeElement.getElementsByTagName("parameters").item(0);
                ParameterContext parameterContext = new ParameterContext(new HashMap<String, String>(),
                        new HashMap<String, List<String>>(), new HashMap<String, Map<String, String>>());
                if(null != parametersNode){
                    parameterContext = ConfigurationParserHelper.buildParameterContext(parametersNode);
                }
                eventPostActions.add(new EventPostAction(nodeId,actionId,parameterContext));
            }
        }
        return eventPostActions;
    }
}
