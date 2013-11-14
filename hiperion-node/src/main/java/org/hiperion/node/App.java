package org.hiperion.node;

import org.hiperion.common.exception.HiperionException;
import org.hiperion.common.util.StartupConfig;
import org.hiperion.connector.model.DataField;
import org.hiperion.connector.model.ParameterContext;
import org.hiperion.node.config.HiperionNodeJettyServerConfig;
import org.hiperion.node.service.datasource.waspmote.parser.WaspmoteOutput;
import org.hiperion.node.service.datasource.waspmote.parser.WaspmoteParserImpl;
import org.hiperion.node.service.datasource.yahoo.weather.YahooWeatherDataSourceService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class App {
    public static void main(String[] args) throws IOException, HiperionException, InterruptedException {
        final String appConfigFile = "etc/hiperion-node.properties";
        StartupConfig.loadProperties(new File(appConfigFile));
        AnnotationConfigApplicationContext applicationContext =
                new AnnotationConfigApplicationContext(HiperionNodeJettyServerConfig.class);
        applicationContext.registerShutdownHook();
    }
}
