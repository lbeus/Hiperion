package org.hiperion.core.web.controller.service;

import java.io.Serializable;

/**
 * User: iobestar
 * Date: 14.05.13.
 * Time: 20:54
 */
public class XmlRestBean implements Serializable {

    private final static long serialVersionUID = 1L;

    private String xmlContent;

    public XmlRestBean(String xmlContent) {
        this.xmlContent = xmlContent;
    }

    public XmlRestBean() {
    }

    public String getXmlContent() {
        return xmlContent;
    }

    public void setXmlContent(String xmlContent) {
        this.xmlContent = xmlContent;
    }
}
