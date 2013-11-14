package org.hiperion.core.service.processing.util;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.cometd.annotation.Session;
import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.bayeux.server.ConfigurableServerChannel;
import org.cometd.bayeux.server.ServerChannel;
import org.cometd.bayeux.server.ServerSession;

import java.io.IOException;

/**
 * User: iobestar
 * Date: 25.05.13.
 * Time: 22:41
 */
public class CometPublisher<T> {

    private final static Logger LOGGER = Logger.getLogger(CometPublisher.class);

    private BayeuxServer bayeuxServer;

    private final ObjectMapper objectMapper;

    @Session
    private ServerSession serverSession;

    public CometPublisher(BayeuxServer bayeuxServer) {
        this.bayeuxServer = bayeuxServer;
        this.objectMapper = new ObjectMapper();
    }

    public void publishMessage(T message, String channelName) {

        bayeuxServer.createIfAbsent(channelName, new ConfigurableServerChannel.Initializer() {
            public void configureChannel(ConfigurableServerChannel channel) {
                channel.setPersistent(true);
                channel.setLazy(true);
            }
        });

        try {
            String jsonEvent = objectMapper.writeValueAsString(message);
            ServerChannel channel = bayeuxServer.getChannel(channelName);
            channel.publish(serverSession, jsonEvent, null);
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }


}
