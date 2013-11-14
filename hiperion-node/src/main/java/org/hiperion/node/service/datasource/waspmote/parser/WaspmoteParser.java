package org.hiperion.node.service.datasource.waspmote.parser;

/**
 * User: iobestar
 * Date: 22.06.13.
 * Time: 17:38
 */
public interface WaspmoteParser {
    WaspmoteOutput parseInputPacket(String inputString);
}
