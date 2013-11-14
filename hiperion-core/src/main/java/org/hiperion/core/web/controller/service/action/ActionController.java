package org.hiperion.core.web.controller.service.action;

import org.apache.log4j.Logger;
import org.hiperion.common.exception.HiperionException;
import org.hiperion.connector.model.ActionDescription;
import org.hiperion.connector.model.ParameterContext;
import org.hiperion.core.service.rmi.NodeRmiService;
import org.hiperion.core.service.rmi.config.NodeDescriptor;
import org.hiperion.core.service.rmi.config.NodeDescriptorConfig;
import org.hiperion.core.util.helpers.ConfigurationParserHelper;
import org.hiperion.core.web.controller.HiperionRestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

/**
 * User: iobestar
 * Date: 22.05.13.
 * Time: 18:43
 */
@Controller
@RequestMapping("/service/action")
public class ActionController {

    private static final Logger LOGGER = Logger.getLogger(ActionController.class);

    @Autowired
    private NodeRmiService nodeRmiService;

    @Autowired
    private NodeDescriptorConfig nodeDescriptorConfig;

    @RequestMapping(headers = {"content-type=application/json"}, value = "/run", method = RequestMethod.POST)
    public
    @ResponseBody
    HiperionRestResponse runAction(@RequestBody RunActionRestRequest runActionRestRequest) {
        try {
            ParameterContext parameterContext = ConfigurationParserHelper.buildParameterContext(
                    runActionRestRequest.getParametersXml());
            nodeRmiService.runAction(runActionRestRequest.getNodeId(),runActionRestRequest.getActionId(),parameterContext);
            return new HiperionRestResponse(HiperionRestResponse.REST_OK_MESSAGE);
        } catch (Exception e) {
            LOGGER.error(e);
            return new HiperionRestResponse(e.getMessage());
        }
    }

    @RequestMapping(value = "/nodes", method = RequestMethod.GET)
    public
    @ResponseBody
    List<String> getNodes() {
        List<NodeDescriptor> nodeDescriptors = nodeDescriptorConfig.findAll();
        List<String> nodeIds = new LinkedList<String>();
        for (NodeDescriptor nodeDescriptor : nodeDescriptors) {
            nodeIds.add(nodeDescriptor.getNodeId());
        }
        return nodeIds;
    }

    @RequestMapping(value = "/node/{nodeId}/actions", method = RequestMethod.GET)
    public
    @ResponseBody
    List<String> getNodeActions(@PathVariable String nodeId) {
        List<String> actions = new LinkedList<String>();
        try {
            ActionDescription[] actionDescriptions = nodeRmiService.getActionDescriptions(nodeId);
            for (ActionDescription actionDescription : actionDescriptions) {
                actions.add(actionDescription.getActionId());
            }
        } catch (HiperionException e) {
            LOGGER.error(e);
        }
        return actions;
    }

    @RequestMapping(value = "/node/{nodeId}/available", method = RequestMethod.GET)
    public
    @ResponseBody
    Boolean isNodeAvailable(@PathVariable String nodeId) {
        if (nodeRmiService.isNodeAvailable(nodeId)) {
            return Boolean.valueOf(true);
        }
        return Boolean.valueOf(false);
    }


}
