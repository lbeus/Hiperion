package org.hiperion.node.service.action.read.datasource;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.hiperion.connector.model.PushDataRequest;

import java.io.IOException;
import java.net.URL;

/**
 * User: iobestar
 * Date: 21.06.13.
 * Time: 14:48
 */
public class PushDataService {

    private static final Logger LOGGER = Logger.getLogger(PushDataService.class);

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public PushDataService(HttpClient httpClient) {
        this.httpClient = httpClient;
        this.objectMapper = new ObjectMapper();
    }

    public void pushData(String uri, PushDataRequest[] pushDataRequest) {
        try {

            URL url = new URL(uri);
            String hostname = url.getHost();
            int port = url.getPort();
            String scheme = url.getProtocol();
            HttpHost httpHost = new HttpHost(hostname, port,scheme);

            String postJson = objectMapper.writeValueAsString(pushDataRequest);
            HttpPost httpPost = new HttpPost(uri);

            StringEntity requestEntity = new StringEntity(postJson);
            requestEntity.setContentType("application/json");
            requestEntity.setContentEncoding("utf-8");
            httpPost.setEntity(requestEntity);

            HttpResponse httpResponse = httpClient.execute(httpHost, httpPost);
            EntityUtils.consume(httpResponse.getEntity());
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    public void dispose() {
        httpClient.getConnectionManager().shutdown();
    }
}
