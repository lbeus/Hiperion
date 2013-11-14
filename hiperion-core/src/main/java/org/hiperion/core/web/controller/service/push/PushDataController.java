package org.hiperion.core.web.controller.service.push;

import org.apache.log4j.Logger;
import org.hiperion.common.exception.DataCollectorNotDeployed;
import org.hiperion.common.exception.DataCollectorNotFound;
import org.hiperion.connector.model.DataField;
import org.hiperion.connector.model.DataSourceResult;
import org.hiperion.core.service.collecting.CollectingService;
import org.hiperion.core.web.controller.HiperionRestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * User: iobestar
 * Date: 21.05.13.
 * Time: 21:26
 */
@Controller
@RequestMapping("/service/push")
public class PushDataController {

    private static final Logger LOGGER = Logger.getLogger(PushDataController.class);

    @Autowired
    private CollectingService collectingService;

    @RequestMapping(headers = {"content-type=application/json"}, value = "/", method = RequestMethod.POST)
    public
    @ResponseBody
    HiperionRestResponse pushData(@RequestBody List<PushDataRestRequest> pushDataRestRequestList) {
        try {
            for(PushDataRestRequest pushDataRestRequest : pushDataRestRequestList){
                String dataCollectorId = pushDataRestRequest.getDataCollectorId();
                String dataSourceId = pushDataRestRequest.getDataSourceId();
                Map<String,DataField> dataFields = pushDataRestRequest.getDataFields();
                collectingService.collectData(dataCollectorId,dataSourceId,new DataSourceResult(dataFields));
            }
            return new HiperionRestResponse(HiperionRestResponse.PUSH_SUCCESS);
        }catch (DataCollectorNotDeployed e){
            LOGGER.error(e);
            return new HiperionRestResponse(HiperionRestResponse.COLLECTOR_NOT_DEPLOYED);
        } catch (Exception e) {
            LOGGER.error(e);
            return new HiperionRestResponse(e.getMessage());
        }
    }
}
