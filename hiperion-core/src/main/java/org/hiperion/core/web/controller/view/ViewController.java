package org.hiperion.core.web.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * User: iobestar
 * Date: 12.05.13.
 * Time: 12:05
 */

@Controller
@RequestMapping("/view")
public class ViewController {

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/collectors", method = RequestMethod.GET)
    public String collectors() {
        return "collectors";
    }

    @RequestMapping(value = "/sources", method = RequestMethod.GET)
    public String sources() {
        return "sources";
    }

    @RequestMapping(value = "/events", method = RequestMethod.GET)
    public String events() {
        return "events";
    }

    @RequestMapping(value = "/configuration/nodes", method = RequestMethod.GET)
    public String nodes() {
        return "conf_nodes";
    }

    @RequestMapping(value = "/configuration/processing", method = RequestMethod.GET)
    public String processing() {
        return "conf_processing";
    }

    @RequestMapping(value = "/action", method = RequestMethod.GET)
    public String action() {
        return "action";
    }

    @RequestMapping(value = "/output/live-data", method = RequestMethod.GET)
    public String liveData() {
        return "live-data";
    }

    @RequestMapping(value = "/output/live-events", method = RequestMethod.GET)
    public String liveEvents() {
        return "live-events";
    }

    @RequestMapping(value = "/log", method = RequestMethod.GET)
    public String log() {
        return "log";
    }
}
