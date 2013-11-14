package org.hiperion.core.web.controller;

/**
 * User: iobestar
 * Date: 14.05.13.
 * Time: 20:48
 */
public class HiperionRestResponse {
    public static final String REST_OK_MESSAGE = "OK";
    public static final String REST_ERROR_MESSAGE = "ERROR";

    public static final String PASSIVE_PULL_COLLECTOR_NOT_FOUND = "Passive pull data collector not deployed.";
    public static final String ADD_COLLECTOR_DEPLOYED = "Can't add data collector because it is deployed.";
    public static final String REMOVE_COLLECTOR_DEPLOYED = "Can't remove data collector because it is deployed.";
    public static final String RELOAD_COLLECTOR_DEPLOYED = "Can't reload data collector because it is deployed.";
    public static final String COLLECTOR_NOT_DEPLOYED = "Data collector not deployed.";
    public static final String COLLECTOR_NOT_FOUND = "Data collector not found.";

    public static final String ADD_EVENT_REGISTERED = "Can't add event because it is registered.";
    public static final String REMOVE_EVENT_REGISTERED = "Can't remove event because it is registered.";
    public static final String RELOAD_EVENT_REGISTERED = "Can't reload event because it is registered.";
    public static final String EVENT_NOT_FOUND = "Event not found.";

    public static final String PUSH_SUCCESS = "Push success.";

    private String restResponse;

    public HiperionRestResponse(String restResponse){
        this.restResponse = restResponse;
    }

    public HiperionRestResponse() {
    }

    public String getRestResponse() {
        return restResponse;
    }

    public void setRestResponse(String restResponse) {
        this.restResponse = restResponse;
    }
}
