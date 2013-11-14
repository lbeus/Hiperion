package org.hiperion.core.service.processing.actions.stdout;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.hiperion.core.service.collecting.CollectingOutput;
import org.hiperion.core.service.processing.ProcessingAction;
import org.hiperion.core.service.processing.ProcessingActionType;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 10.04.13.
 * Time: 20:39
 */
@Component
public final class StdOutProcessingAction implements ProcessingAction {

    private static final Logger LOGGER = Logger.getLogger(StdOutProcessingAction.class);

    private final ObjectMapper objectMapper;

    public StdOutProcessingAction() {
        this.objectMapper = new ObjectMapper();
    }

    @PostConstruct
    @Override
    public void init() {
    }

    @Override
    public void processCollectingOutput(CollectingOutput collectingOutput) {
        try {
            System.out.println(objectMapper.writeValueAsString(collectingOutput));
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    @PreDestroy
    @Override
    public void dispose() {
    }
}
