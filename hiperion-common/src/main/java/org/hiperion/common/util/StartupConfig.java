package org.hiperion.common.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * User: iobestar
 * Date: 06.04.13.
 * Time: 16:55
 */
public class StartupConfig {

    private StartupConfig() {
    }

    public static void loadProperties(File propertiesFile) throws IOException {
        if (null == propertiesFile) {
            throw new RuntimeException("Missing hiperion core properties file.");
        }

        if (!propertiesFile.exists()) {
            throw new RuntimeException(propertiesFile + " doesn't exists.");
        }

        System.getProperties().load(new FileReader(propertiesFile));
    }
}
