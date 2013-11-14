package org.hiperion.core.web.controller.service.common;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.hiperion.core.model.data.collector.DataCollector;
import org.hiperion.core.model.data.collector.blocks.SelectorBlock;
import org.hiperion.core.service.configuration.dc.xml.DataCollectorConfiguration;
import org.hiperion.core.service.configuration.dc.xml.XmlDataCollectorConfiguration;
import org.hiperion.core.service.processing.ProcessingActionType;
import org.hiperion.core.util.DataKeyCreator;
import org.hiperion.core.web.controller.service.configuration.ActionNameRestBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * User: iobestar
 * Date: 26.05.13.
 * Time: 20:14
 */
@Controller
@RequestMapping("/service/common")
public class CommonController {

    private final static Logger LOGGER = Logger.getLogger(CommonController.class);

    @Autowired
    private DataCollectorConfiguration dataCollectorConfiguration;


    @RequestMapping(value = "/streams", method = RequestMethod.GET)
    public
    @ResponseBody
    List<String> getStreamIds() {
        List<String> streamIds = new LinkedList<String>();
        try {
            for (DataCollector dataCollector : dataCollectorConfiguration.findAll()) {
                String dataCollectorId = dataCollector.getCollectorId();
                for(SelectorBlock selectorBlock : dataCollector.getSelectorBlocks()){
                    String streamId = DataKeyCreator.createKey(dataCollectorId,selectorBlock.getSelectorName());
                    streamIds.add(streamId);
                }
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return streamIds;
    }

    @RequestMapping(value = "/logs", method = RequestMethod.GET)
    public
    @ResponseBody
    List<String> getLogFileNames() {
        List<String> logFileNames = new LinkedList<String>();
        try {
            String homePath = System.getProperty("hiperion.core.home.dir");
            File logDirectory = new File(homePath,"logs");
            if(logDirectory.exists()){
                for(String fileName : logDirectory.list()){
                    if ((new File(logDirectory,fileName).isFile())){
                        logFileNames.add(fileName);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return logFileNames;
    }

    @RequestMapping(headers = {"content-type=text/plain"},value = "/log/get", method = RequestMethod.POST)
    public
    @ResponseBody
    LogContentRestResponse getLogContent(@RequestBody String logFileName) {
        try {
            String homePath = System.getProperty("hiperion.core.home.dir");
            File logDirectory = new File(homePath,"logs");
            String logContent = FileUtils.readFileToString(new File(logDirectory,logFileName));
            return new LogContentRestResponse(logContent);
        } catch (Exception e) {
            LOGGER.error(e);
            return  new LogContentRestResponse(e.getMessage());
        }
    }
}
