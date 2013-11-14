package org.hiperion.core.web.controller.service.collectors;

import org.apache.log4j.Logger;
import org.hiperion.common.exception.*;
import org.hiperion.core.model.data.collector.DataCollector;
import org.hiperion.core.model.data.collector.PullDataCollector;
import org.hiperion.core.service.collecting.CollectingService;
import org.hiperion.core.service.configuration.dc.xml.DataCollectorConfiguration;
import org.hiperion.core.service.configuration.dc.xml.XmlDataCollectorConfigurationParser;
import org.hiperion.core.web.controller.HiperionRestResponse;
import org.hiperion.core.web.controller.service.XmlRestBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 24.04.13.
 * Time: 23:12
 */
@Controller
@RequestMapping("/service/collectors")
public class CollectorsController {

    private final static Logger LOGGER = Logger.getLogger(CollectorsController.class);

    @Autowired
    private DataCollectorConfiguration dataCollectorConfiguration;

    @Autowired
    private CollectingService collectingService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public
    @ResponseBody
    List<DataCollectorRestBean> getCollectors() {
        List<DataCollectorRestBean> collectorBeans = new LinkedList<DataCollectorRestBean>();
        try {
            for (DataCollector dataCollector : dataCollectorConfiguration.findAll()) {
                String name = dataCollector.getCollectorId();
                String description = dataCollector.getDescription();
                String type = "PUSH";
                String cronExpression = "";
                if (dataCollector instanceof PullDataCollector) {
                    cronExpression = ((PullDataCollector) dataCollector).getCronExpression();
                    type = "PULL";
                }
                boolean isRegistered = collectingService.isDeployed(name);

                collectorBeans.add(new DataCollectorRestBean(name, description, type, cronExpression, isRegistered));
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }

        return collectorBeans;
    }

    @RequestMapping(headers = {"content-type=application/json"}, value = "/collector/add", method = RequestMethod.POST)
    public
    @ResponseBody
    HiperionRestResponse addCollector(@RequestBody XmlRestBean collectorRequest) {
        XmlDataCollectorConfigurationParser xmlDataCollectorConfigurationParser = new
                XmlDataCollectorConfigurationParser(collectorRequest.getXmlContent());
        try {
            String dataCollectorId = xmlDataCollectorConfigurationParser.getDataCollectorId();
            if(collectingService.isDeployed(dataCollectorId)){
                return new HiperionRestResponse(HiperionRestResponse.ADD_COLLECTOR_DEPLOYED);
            }
            dataCollectorConfiguration.addConfiguration(collectorRequest.getXmlContent());
            return new HiperionRestResponse(HiperionRestResponse.REST_OK_MESSAGE);
        } catch (DataCollectorNotFound e) {
            LOGGER.error(e);
            return new HiperionRestResponse(HiperionRestResponse.COLLECTOR_NOT_FOUND);
        } catch (HiperionException e) {
            LOGGER.error(e);
            return new HiperionRestResponse(e.getMessage());
        }
    }

    @RequestMapping(value = "/collector/{collectorId}/reload", method = RequestMethod.GET)
    public
    @ResponseBody
    HiperionRestResponse reloadCollector(@PathVariable String collectorId) {
        try {
            if(collectingService.isDeployed(collectorId)){
                return new HiperionRestResponse(HiperionRestResponse.RELOAD_COLLECTOR_DEPLOYED);
            }
            dataCollectorConfiguration.reloadConfiguration(collectorId);
            return new HiperionRestResponse(HiperionRestResponse.REST_OK_MESSAGE);
        } catch (DataCollectorNotFound e) {
            LOGGER.error(e);
            return new HiperionRestResponse(HiperionRestResponse.COLLECTOR_NOT_FOUND);
        } catch (HiperionException e) {
            LOGGER.error(e);
            return new HiperionRestResponse(e.getMessage());
        }
    }

    @RequestMapping(value = "/collector/{collectorId}/remove", method = RequestMethod.GET)
    public
    @ResponseBody
    HiperionRestResponse removeCollector(@PathVariable String collectorId) {
        try {
            if(collectingService.isDeployed(collectorId)){
                return new HiperionRestResponse(HiperionRestResponse.REMOVE_COLLECTOR_DEPLOYED);
            }
            dataCollectorConfiguration.removeConfiguration(collectorId);
            return new HiperionRestResponse(HiperionRestResponse.REST_OK_MESSAGE);
        } catch (HiperionException e) {
            LOGGER.error(e);
            return new HiperionRestResponse(e.getMessage());
        }
    }

    @RequestMapping(value = "/collector/{collectorId}/deploy", method = RequestMethod.GET)
    public
    @ResponseBody
    HiperionRestResponse deployCollector(@PathVariable String collectorId) {
        try {
            if (collectingService.isDeployed(collectorId)) {
                return new HiperionRestResponse(HiperionRestResponse.REST_OK_MESSAGE);
            }
            DataCollector dataCollector = dataCollectorConfiguration.findById(collectorId);
            collectingService.deployCollector(dataCollector);
            return new HiperionRestResponse(HiperionRestResponse.REST_OK_MESSAGE);
        } catch (HiperionException e) {
            LOGGER.error(e);
            return new HiperionRestResponse(e.getMessage());
        }
    }

    @RequestMapping(value = "/collector/{collectorId}/undeploy", method = RequestMethod.GET)
    public
    @ResponseBody
    HiperionRestResponse undeployCollector(@PathVariable String collectorId) {
        try {
            if (!collectingService.isDeployed(collectorId)) {
                return new HiperionRestResponse(HiperionRestResponse.REST_OK_MESSAGE);
            }
            collectingService.undeployCollector(collectorId);
            return new HiperionRestResponse(HiperionRestResponse.REST_OK_MESSAGE);
        } catch (HiperionException e) {
            LOGGER.error(e);
            return new HiperionRestResponse(e.getMessage());
        }

    }

    @RequestMapping(value = "/collector/{collectorId}/collect", method = RequestMethod.GET)
    public
    @ResponseBody
    HiperionRestResponse runCollect(@PathVariable String collectorId) {
        try {
            collectingService.collectData(collectorId);
            return new HiperionRestResponse(HiperionRestResponse.REST_OK_MESSAGE);
        } catch (DataCollectorNotDeployed e) {
            LOGGER.error(e);
            return new HiperionRestResponse(HiperionRestResponse.COLLECTOR_NOT_DEPLOYED);
        } catch (HiperionException e) {
            LOGGER.error(e);
            return new HiperionRestResponse(e.getMessage());
        }
    }

    @RequestMapping(value = "/collector/{collectorId}/xml", method = RequestMethod.GET)
    public
    @ResponseBody
    XmlRestBean getXmlContent(@PathVariable String collectorId) {
        try {
            String xmlContent = dataCollectorConfiguration.xmlConfigurationContent(collectorId);
            return new XmlRestBean(xmlContent);
        } catch (HiperionException e) {
            LOGGER.error(e);
            return null;
        }
    }
}
