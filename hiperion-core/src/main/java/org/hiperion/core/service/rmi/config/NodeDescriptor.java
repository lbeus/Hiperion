package org.hiperion.core.service.rmi.config;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 01.05.13.
 * Time: 00:12
 */
public class NodeDescriptor implements Serializable {

    private final static long serialVersionUID = 1L;

    private String nodeId;
    private String hostname;
    private int port;
    private String servicePath;

    public NodeDescriptor() {
    }

    public NodeDescriptor(String nodeId, String hostname, int port, String servicePath) {
        this.nodeId = nodeId;
        this.hostname = hostname;
        this.port = port;
        this.servicePath = servicePath;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getServicePath() {
        return servicePath;
    }

    public void setServicePath(String servicePath) {
        this.servicePath = servicePath;
    }
}
