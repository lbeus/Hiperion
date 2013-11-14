package org.hiperion.core.app;

import org.hiperion.common.util.StartupConfig;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.io.IOException;

/**
 * User: iobestar
 * Date: 23.04.13.
 * Time: 19:55
 */
public class ApplicationInitializer implements ServletContextListener {

    private static final String PROPERTIES_FILE_NAME = "etc/hiperion-core.properties";

    private static final String DC_STORE_DIR_KEY = "hiperion.core.dc.store.dir";
    private static final String DS_STORE_DIR_KEY = "hiperion.core.ds.store.dir";
    private static final String EV_STORE_DIR_KEY = "hiperion.core.ev.store.dir";
    private static final String HOME_DIR_KEY = "hiperion.core.home.dir";

    private static final String DC_STORE_PATH = "etc/store/dc/";
    private static final String DS_STORE_PATH = "etc/store/ds/";
    private static final String EV_STORE_PATH = "etc/store/ev/";

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            String homePath = getHomePath();

            System.getProperties().put(HOME_DIR_KEY, homePath);

            File homeDirectory = new File(homePath);
            File dcStoreDirectory = new File(homeDirectory, DC_STORE_PATH);
            File dsStoreDirectory = new File(homeDirectory, DS_STORE_PATH);
            File evStoreDirectory = new File(homeDirectory, EV_STORE_PATH);
            System.getProperties().put(DC_STORE_DIR_KEY, dcStoreDirectory.getAbsolutePath());
            System.getProperties().put(DS_STORE_DIR_KEY, dsStoreDirectory.getAbsolutePath());
            System.getProperties().put(EV_STORE_DIR_KEY, evStoreDirectory.getAbsolutePath());

            File propertiesFile = new File(homeDirectory, PROPERTIES_FILE_NAME);
            StartupConfig.loadProperties(propertiesFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }

    private String getHomePath() {
        String homePath = System.getProperty("hiperion.core.root.path");
        if(null == homePath){
            File userDirectory = new File(System.getProperty("user.dir"));
            homePath = new File(userDirectory.getParent()).getParent();
        }
        return homePath;
    }
}
