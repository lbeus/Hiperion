package org.hiperion.core.web.controller.service.configuration;

import org.apache.log4j.Logger;
import org.hiperion.core.service.processing.ProcessingActionType;
import org.hiperion.core.service.processing.config.StreamProcessingConfig;
import org.hiperion.core.service.rmi.NodeRmiService;
import org.hiperion.core.service.rmi.config.NodeDescriptor;
import org.hiperion.core.service.rmi.config.NodeDescriptorConfig;
import org.hiperion.core.web.controller.HiperionRestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

/**
 * User: iobestar
 * Date: 19.05.13.
 * Time: 20:00
 */
@Controller
@RequestMapping("/service/configuration")
public class ConfigurationController {

    private static final Logger LOGGER = Logger.getLogger(ConfigurationController.class);

    @Autowired
    private StreamProcessingConfig streamProcessingConfig;

    @Autowired
    private NodeDescriptorConfig nodeDescriptorConfig;

    @Autowired
    private NodeRmiService nodeRmiService;

    @RequestMapping(value = "/processing/actions", method = RequestMethod.GET)
    public
    @ResponseBody
    List<ActionNameRestBean> getActionIds() {
        List<ActionNameRestBean> actionIds = new LinkedList<ActionNameRestBean>();
        try {
            for (ProcessingActionType processingActionType : ProcessingActionType.values()) {
                actionIds.add(new ActionNameRestBean(processingActionType.getValue(),
                        processingActionType.getDescription()));
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return actionIds;
    }

    @RequestMapping(headers = {"content-type=application/json"}, value = "/processing/enable", method = RequestMethod.POST)
    public
    @ResponseBody
    HiperionRestResponse enableProcessing(@RequestBody ProcessingRestBean processingRestBean) {
        try {
            streamProcessingConfig.enableProcessingFor(processingRestBean.getStreamId(),
                    ProcessingActionType.getProcessingActionType(processingRestBean.getActionId()));
            return new HiperionRestResponse(HiperionRestResponse.REST_OK_MESSAGE);
        } catch (Exception e) {
            LOGGER.error(e);
            return new HiperionRestResponse(e.getMessage());
        }
    }

    @RequestMapping(headers = {"content-type=application/json"}, value = "/processing/disable", method = RequestMethod.POST)
    public
    @ResponseBody
    HiperionRestResponse disableProcessing(@RequestBody ProcessingRestBean processingRestBean) {
        try {
            streamProcessingConfig.disableProcessingFor(processingRestBean.getStreamId(),
                    ProcessingActionType.getProcessingActionType(processingRestBean.getActionId()));
            return new HiperionRestResponse(HiperionRestResponse.REST_OK_MESSAGE);
        } catch (Exception e) {
            LOGGER.error(e);
            return new HiperionRestResponse(e.getMessage());
        }
    }

    @RequestMapping(value = "/nodes", method = RequestMethod.GET)
    public
    @ResponseBody
    List<NodeDescriptor> getNodes() {
        List<NodeDescriptor> nodes = new LinkedList<NodeDescriptor>();
        try {
            nodes = nodeDescriptorConfig.findAll();
        } catch (Exception e) {
            LOGGER.error(e);
        }

        return nodes;
    }

    @RequestMapping(value = "/nodes/node/save", method = RequestMethod.POST)
    public
    @ResponseBody
    HiperionRestResponse saveNode(@RequestBody NodeDescriptor nodeDescriptor) {
        try {
            nodeDescriptorConfig.addNodeDescriptor(nodeDescriptor);
            nodeRmiService.registerNode(nodeDescriptor);
            return new HiperionRestResponse(HiperionRestResponse.REST_OK_MESSAGE);
        } catch (Exception e) {
            LOGGER.error(e);
            return new HiperionRestResponse(e.getMessage());
        }
    }

    @RequestMapping(value = "/node/{nodeId}/remove", method = RequestMethod.GET)
    public
    @ResponseBody
    HiperionRestResponse removeNode(@PathVariable String nodeId) {
        try {
            nodeDescriptorConfig.removeNodeDescriptor(nodeId);
            nodeRmiService.unregisterNode(nodeId);
            return new HiperionRestResponse(HiperionRestResponse.REST_OK_MESSAGE);
        } catch (Exception e) {
            LOGGER.error(e);
            return new HiperionRestResponse(e.getMessage());
        }
    }

    @RequestMapping(value = "/processing/streams", method = RequestMethod.GET)
    public
    @ResponseBody
    List<ActionNameRestBean> getStreamIds() {
        List<ActionNameRestBean> actionIds = new LinkedList<ActionNameRestBean>();
        try {
            for (ProcessingActionType processingActionType : ProcessingActionType.values()) {
                actionIds.add(new ActionNameRestBean(processingActionType.getValue(),
                        processingActionType.getDescription()));
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return actionIds;
    }

}
