package org.hiperion.connector.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 28.04.13.
 * Time: 00:33
 */
public class ParameterContext implements Serializable {

    private final static long serialVersionUID = 1L;

    private Map<String, String> simpleParameters;
    private Map<String, List<String>> listParameters;
    private Map<String, Map<String, String>> mapParameters;

    public ParameterContext() {
    }

    public ParameterContext(Map<String, String> simpleParameters, Map<String, List<String>> listParameters,
                            Map<String, Map<String, String>> mapParameters) {
        this.simpleParameters = simpleParameters;
        this.listParameters = listParameters;
        this.mapParameters = mapParameters;
    }

    public Map<String, String> getSimpleParameters() {
        return simpleParameters;
    }

    public void setSimpleParameters(Map<String, String> simpleParameters) {
        this.simpleParameters = simpleParameters;
    }

    public Map<String, List<String>> getListParameters() {
        return listParameters;
    }

    public void setListParameters(Map<String, List<String>> listParameters) {
        this.listParameters = listParameters;
    }

    public Map<String, Map<String, String>> getMapParameters() {
        return mapParameters;
    }

    public void setMapParameters(Map<String, Map<String, String>> mapParameters) {
        this.mapParameters = mapParameters;
    }

    // TODO: eyuals 6 hash
}
