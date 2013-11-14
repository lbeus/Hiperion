package org.hiperion.node.service.action;

import org.hiperion.common.exception.HiperionException;
import org.hiperion.connector.model.ParameterContext;
import org.hiperion.connector.model.ParameterInfo;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 20.04.13.
 * Time: 09:04
 */
public interface Action {

    void init();

    boolean executeAction(ParameterContext parameterContext) throws HiperionException;

    void dispose();

    ParameterInfo[] getParameterInfo();
}
