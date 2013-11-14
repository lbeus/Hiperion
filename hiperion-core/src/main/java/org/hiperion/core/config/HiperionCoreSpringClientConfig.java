package org.hiperion.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 07.04.13.
 * Time: 12:36
 * To change this template use File | Settings | File Templates.
 */
@Configuration
public class HiperionCoreSpringClientConfig {

    @Autowired
    ApplicationContext applicationContext;
}
