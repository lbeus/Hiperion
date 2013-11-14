package org.hiperion.node.service.action.stdoutput;

import org.hiperion.common.exception.HiperionException;
import org.hiperion.connector.model.ParameterContext;
import org.hiperion.connector.model.ParameterInfo;
import org.hiperion.connector.model.enums.ParameterType;
import org.hiperion.node.service.action.Action;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * User: iobestar
 * Date: 20.04.13.
 * Time: 11:43
 */
@Component
public class StdOutputAction implements Action {

    private enum StdOutputParameterDescription {
        OUTPUT_TEXT("output-text", "Standard output text.", ParameterType.SIMPLE);

        private final String parameterName;
        private final String parameterDescription;
        private final ParameterType parameterType;

        private StdOutputParameterDescription(String parameterName, String parameterDescription,
                                              ParameterType parameterType) {
            this.parameterName = parameterName;
            this.parameterDescription = parameterDescription;
            this.parameterType = parameterType;
        }
    }

    @PostConstruct
    @Override
    public void init() {
    }

    private StdOutputActionParameter getStdOutputActionParameter(ParameterContext parameterContext) {
        final String outputText = parameterContext.getSimpleParameters().
                get(StdOutputParameterDescription.OUTPUT_TEXT.parameterName);
        return new StdOutputActionParameter() {
            @Override
            public String getOutputText() {
                return outputText;
            }
        };
    }

    @Override
    public boolean executeAction(ParameterContext parameterContext) throws HiperionException {
        StdOutputActionParameter stdOutputActionParameter =
                getStdOutputActionParameter(parameterContext);

        System.out.println(stdOutputActionParameter.getOutputText());
        return true;
    }

    @Override
    public ParameterInfo[] getParameterInfo() {
        int numOfParameters = StdOutputParameterDescription.values().length;
        ParameterInfo[] result = new ParameterInfo[numOfParameters];
        int i = 0;
        for (StdOutputParameterDescription stdOutputParameterDescription : StdOutputParameterDescription.values()) {
            result[i++] = new ParameterInfo(stdOutputParameterDescription.parameterName,
                    stdOutputParameterDescription.parameterDescription, stdOutputParameterDescription.parameterType);
        }
        return result;
    }

    @PreDestroy
    @Override
    public void dispose() {
    }
}
