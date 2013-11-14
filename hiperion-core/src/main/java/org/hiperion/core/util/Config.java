package org.hiperion.core.util;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 14.03.13.
 * Time: 14:27
 */
public class Config {

    public static final int DEFAULT_NUMBER_OF_PROCESSING_WORKERS =
            Integer.parseInt(System.getProperty("hiperion.core.processing.workers"));

    public static final int DEFAULT_DATA_SOURCE_READ_TIME =
            Integer.parseInt(System.getProperty("hiperion.core.source.read.time.ms"));

}
