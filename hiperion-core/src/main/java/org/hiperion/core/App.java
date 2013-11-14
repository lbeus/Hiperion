package org.hiperion.core;

import org.apache.log4j.Logger;
import org.hiperion.common.exception.HiperionException;
import org.hiperion.common.processors.ItemProcessor;
import org.hiperion.common.processors.QueueItemProcessor;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 23.04.13.
 * Time: 21:26
 */
public class App {

    private static Logger LOGGER = Logger.getLogger(App.class);

    public static void main(String[] args) throws IOException, HiperionException, InterruptedException {
//        StartupConfig.loadProperties();
//        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(
//                HiperionCoreSpringClientConfig.class,
//                HiperionCoreSpringConfig.class,
//                ProcessingActionContextSpringConfig.class);
        String homePath = "E:/documents/education/FERSEM10/DR/source/hiperion/hiperion-core.log";
        System.getProperties().put("hiperion.core.home.dir",homePath);
        while(true){
            Thread.sleep(1);
            LOGGER.info("JUPTANCkJMVD00DQpDU0E9MA0KQkJMPTANClJCTD0wDQpES0w9TE9XDQpFWEY9MDJUPTANCkJMVD00DQpDU0E9MA0KQkJMPTANClJCTD0wDQpES0w9TE9XDQpFWEY9MDJUPTANCkJMVD00DQpDU0E9MA0KQkJMPTANClJCTD0wDQpES0w9TE9XDQpFWEY9MDJUPTANCkJMVD00DQpDU0E9MA0KQkJMPTANClJCTD0wDQpES0w9TE9XDQpFWEY9MDJUPTANCkJMVD00DQpDU0E9MA0KQkJMPTANClJCTD0wDQpES0w9TE9XDQpFWEY9MDJUPTANCkJMVD00DQpDU0E9MA0KQkJMPTANClJCTD0wDQpES0w9TE9XDQpFWEY9MDJUPTANCkJMVD00DQpDU0E9MA0KQkJMPTANClJCTD0wDQpES0w9TE9XDQpFWEY9MDJUPTANCkJMVD00DQpDU0E9MA0KQkJMPTANClJCTD0wDQpES0w9TE9XDQpFWEY9MDJUPTANCkJMVD00DQpDU0E9MA0KQkJMPTANClJCTD0wDQpES0w9TE9XDQpFWEY9MDJUPTANCkJMVD00DQpDU0E9MA0KQkJMPTANClJCTD0wDQpES0w9TE9XDQpFWEY9MDJUPTANCkJMVD00DQpDU0E9MA0KQkJMPTANClJCTD0wDQpES0w9TE9XDQpFWEY9MDJUPTANCkJMVD00DQpDU0E9MA0KQkJMPTANClJCTD0wDQpES0w9TE9XDQpFWEY9MDJUPTANCkJMVD00DQpDU0E9MA0KQkJMPTANClJCTD0wDQpES0w9TE9XDQpFWEY9MDJUPTANCkJMVD00DQpDU0E9MA0KQkJMPTANClJCTD0wDQpES0w9TE9XDQpFWEY9MDJUPTANCkJMVD00DQpDU0E9MA0KQkJMPTANClJCTD0wDQpES0w9TE9XDQpFWEY9MDJUPTANCkJMVD00DQpDU0E9MA0KQkJMPTANClJCTD0wDQpES0w9TE9XDQpFWEY9MD");
        }
    }
}
