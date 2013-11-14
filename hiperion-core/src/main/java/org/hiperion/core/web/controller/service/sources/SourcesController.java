package org.hiperion.core.web.controller.service.sources;

import org.apache.log4j.Logger;
import org.hiperion.common.exception.EntityDoesNotExist;
import org.hiperion.common.exception.HiperionException;
import org.hiperion.core.model.data.source.DataSource;
import org.hiperion.core.service.configuration.ds.xml.DataSourceConfiguration;
import org.hiperion.core.web.controller.HiperionRestResponse;
import org.hiperion.core.web.controller.service.XmlRestBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

/**
 * User: iobestar
 * Date: 17.05.13.
 * Time: 11:59
 */
@Controller
@RequestMapping("/service/sources")
public class SourcesController {

    private final static Logger LOGGER = Logger.getLogger(SourcesController.class);

    @Autowired
    private ApplicationContext applicationContext;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public
    @ResponseBody
    List<DataSourceRestBean> getCollectors() {
        List<DataSourceRestBean> dataSourceBeans = new LinkedList<DataSourceRestBean>();
        try {
            DataSourceConfiguration dataSourceConfiguration = applicationContext.getBean(DataSourceConfiguration.class);
            for (DataSource dataSource : dataSourceConfiguration.findAll()) {
                String name = dataSource.getDataSourceId();
                String description = dataSource.getDescription();
                String latitude = dataSource.getLatitude();
                String longitude = dataSource.getLongitude();

                dataSourceBeans.add(new DataSourceRestBean(name, description, latitude, longitude));
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return dataSourceBeans;
    }

    @RequestMapping(headers ={"content-type=application/json"},value = "/source/add", method = RequestMethod.POST)
    public
    @ResponseBody
    HiperionRestResponse addSource(@RequestBody XmlRestBean sourceRequst) {
        try {
            DataSourceConfiguration dataSourceConfiguration = applicationContext.getBean(DataSourceConfiguration.class);
            dataSourceConfiguration.addConfiguration(sourceRequst.getXmlContent());
            return new HiperionRestResponse(HiperionRestResponse.REST_OK_MESSAGE);
        } catch (Exception e) {
            LOGGER.error(e);
            return new HiperionRestResponse(e.getMessage());
        }
    }

    @RequestMapping(value = "/source/{sourceId}/reload", method = RequestMethod.GET)
    public
    @ResponseBody
    HiperionRestResponse reloadSource(@PathVariable String sourceId) {
        try {
            DataSourceConfiguration dataSourceConfiguration =
                    applicationContext.getBean(DataSourceConfiguration.class);
            dataSourceConfiguration.reloadConfiguration(sourceId);
            return new HiperionRestResponse(HiperionRestResponse.REST_OK_MESSAGE);
        } catch (HiperionException e) {
            LOGGER.error(e);
            return new HiperionRestResponse(e.getMessage());
        }
    }

    @RequestMapping(value = "/source/{sourceId}/remove", method = RequestMethod.GET)
    public
    @ResponseBody
    HiperionRestResponse removeSource(@PathVariable String sourceId) {
        try {
            DataSourceConfiguration dataSourceConfiguration =
                    applicationContext.getBean(DataSourceConfiguration.class);
            dataSourceConfiguration.removeConfiguration(sourceId);
            return new HiperionRestResponse(HiperionRestResponse.REST_OK_MESSAGE);
        } catch (Exception e) {
            LOGGER.error(e);
            return new HiperionRestResponse(e.getMessage());
        }
    }

    @RequestMapping(value = "/source/{sourceId}/xml", method = RequestMethod.GET)
    public
    @ResponseBody
    XmlRestBean getXmlContent(@PathVariable String sourceId) {
        try {
            DataSourceConfiguration dataSourceConfiguration =
                    applicationContext.getBean(DataSourceConfiguration.class);
            String xmlContent = dataSourceConfiguration.xmlConfigurationContent(sourceId);
            return new XmlRestBean(xmlContent);
        } catch (HiperionException e) {
            LOGGER.error(e);
            return null;
        }
    }
}
