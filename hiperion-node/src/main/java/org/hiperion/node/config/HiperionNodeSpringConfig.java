package org.hiperion.node.config;

import com.googlecode.gmail4j.GmailClient;
import com.googlecode.gmail4j.GmailConnection;
import com.googlecode.gmail4j.auth.Credentials;
import com.googlecode.gmail4j.javamail.ImapGmailClient;
import com.googlecode.gmail4j.javamail.ImapGmailConnection;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.hiperion.node.service.action.read.datasource.PushDataService;
import org.hiperion.node.service.datasource.camera.OpenCVPictureService;
import org.hiperion.node.service.datasource.waspmote.SerialPortConnection;
import org.hiperion.node.service.datasource.waspmote.parser.WaspmoteParser;
import org.hiperion.node.service.datasource.waspmote.parser.WaspmoteParserImpl;
import org.hiperion.node.util.StringParameterConverter;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * User: iobestar
 * Date: 20.04.13.
 * Time: 08:58
 */
@Configuration
@ComponentScan(basePackages = "org.hiperion.node.service")
public class HiperionNodeSpringConfig {

    @Bean
    public PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
        PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();
        propertyPlaceholderConfigurer.setLocation(new FileSystemResource(new File("etc/hiperion-node.properties")));
        return propertyPlaceholderConfigurer;
    }

    @Bean
    public StringParameterConverter stringParameterConverter() {
        StringParameterConverter stringParameterConverter = new StringParameterConverter();
        return stringParameterConverter;
    }

    @Bean
    @Scope(value = "prototype")
    public HttpClient httpClient(){
        HttpClient httpClient = new DefaultHttpClient(clientConnectionManager());
        return httpClient;
    }

    @Bean
    @Scope(value = "prototype")
    public ClientConnectionManager clientConnectionManager(){
        PoolingClientConnectionManager poolingClientConnectionManager =
                new PoolingClientConnectionManager();
        poolingClientConnectionManager.setMaxTotal(200);
        poolingClientConnectionManager.setDefaultMaxPerRoute(20);
        poolingClientConnectionManager.closeExpiredConnections();
        poolingClientConnectionManager.closeIdleConnections(30, TimeUnit.SECONDS);
        return poolingClientConnectionManager;
    }

    @Bean
    public GmailClient gmailClient() {
        GmailClient gmailClient = new ImapGmailClient();
        gmailClient.setConnection(gmailConnection());
        return gmailClient;
    }

    @Bean
    public GmailConnection gmailConnection() {
        GmailConnection gmailConnection = new ImapGmailConnection(credentials());
        return gmailConnection;
    }

    @Bean
    public Credentials credentials() {
        final String gmailUsername = System.getProperty("gmail.username");
        final String gmailPassword = System.getProperty("gmail.password");

        Credentials credentials = new Credentials();
        credentials.setUsername(gmailUsername);
        credentials.setPassword(gmailPassword.toCharArray());
        return credentials;
    }

    @Bean(destroyMethod = "dispose")
    public OpenCVPictureService openCVPictureService() {
        OpenCVPictureService.ImageType imageType =
                OpenCVPictureService.ImageType.valueOf(System.getProperty("camera.picture.type").toUpperCase());
        OpenCVPictureService openCVPictureService = new OpenCVPictureService(imageType);
        return openCVPictureService;
    }

    @Bean
    public PushDataService hiperionPushDataService(){
        PushDataService pushDataService = new
                PushDataService(httpClient());
        return pushDataService;
    }

    @Bean
    public WaspmoteParser waspmoteParser(){
        WaspmoteParser waspmoteParser = new WaspmoteParserImpl();
        return waspmoteParser;
    }

    @Bean(destroyMethod = "closeConnection")
    public SerialPortConnection serialPortConnection(){
        SerialPortConnection serialPortConnection = new SerialPortConnection(System.getProperty("waspmote.serial.port.id"));
        boolean isWaspmoteEnabled = Boolean.valueOf(System.getProperty("waspmote.enabled"));
        if (isWaspmoteEnabled){
            serialPortConnection.openConnection();
        }
        return serialPortConnection;
    }
}

