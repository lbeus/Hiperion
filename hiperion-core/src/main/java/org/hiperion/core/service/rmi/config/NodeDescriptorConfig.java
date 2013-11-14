package org.hiperion.core.service.rmi.config;

import org.hiperion.core.service.rmi.config.NodeDescriptor;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 01.05.13.
 * Time: 00:27
 */
public interface NodeDescriptorConfig {

    void addNodeDescriptor(NodeDescriptor nodeDescriptor);

    void removeNodeDescriptor(String nodeId);

    NodeDescriptor findById(String nodeId);

    List<NodeDescriptor> findAll();

}
