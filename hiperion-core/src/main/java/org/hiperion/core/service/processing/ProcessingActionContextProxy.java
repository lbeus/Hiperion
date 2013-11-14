package org.hiperion.core.service.processing;

import org.hiperion.common.exception.ProcessingActionNotFound;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * User: iobestar
 * Date: 08.05.13.
 * Time: 20:29
 */
public class ProcessingActionContextProxy {

    @Autowired
    private ApplicationContext applicationContext;

    public <T extends ProcessingAction> ProcessingAction getProcessingAction(Class<T> actionType) throws ProcessingActionNotFound {
        try{
            ProcessingAction processingAction = applicationContext.getBean(actionType);
            return processingAction;
        } catch (BeansException e){
            throw new ProcessingActionNotFound();
        }
    }
}
