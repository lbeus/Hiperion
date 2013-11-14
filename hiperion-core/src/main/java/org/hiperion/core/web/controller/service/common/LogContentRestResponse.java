package org.hiperion.core.web.controller.service.common;

import java.io.Serializable;

/**
 * User: iobestar
 * Date: 30.05.13.
 * Time: 19:08
 */
public class LogContentRestResponse implements Serializable {

    private final static long serialVersionUID = 1L;

    private String logContent;

    public LogContentRestResponse(String logContent) {
        this.logContent = logContent;
    }

    public LogContentRestResponse() {
    }

    public String getLogContent() {
        return logContent;
    }

    public void setLogContent(String logContent) {
        this.logContent = logContent;
    }
}
