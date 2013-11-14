package org.hiperion.core.service.rmi;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.remoting.RemoteConnectFailureException;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 21.04.13.
 * Time: 12:39
 */
public class RmiDynamicClientManager<T> {

    private ConfigurableApplicationContext configurableApplicationContext;
    private Set<String> registeredInterfaces;

    public RmiDynamicClientManager() {
        this.configurableApplicationContext = new
                AnnotationConfigApplicationContext(RmiDynamicClientContext.class);
        this.registeredInterfaces = Sets.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
    }

    public T getClientInterfaceById(String clientInterfaceId) throws NoSuchBeanDefinitionException, ClassCastException {
        T clientServiceInterface = (T) configurableApplicationContext.getBean(clientInterfaceId);
        return clientServiceInterface;
    }

    public boolean isClientInterfaceRegistered(String clientInterfaceId) {
        return registeredInterfaces.contains(clientInterfaceId);
    }

    public void addRmiInterface(String interfaceId, String rmiUri, Class<T> type) throws RemoteConnectFailureException {
        if (registeredInterfaces.contains(interfaceId)) {
            return;
        }
        DefaultListableBeanFactory factory = (DefaultListableBeanFactory)
                configurableApplicationContext.getBeanFactory();
        ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
        MutablePropertyValues mutablePropertyValues = new MutablePropertyValues();
        mutablePropertyValues.addPropertyValue("serviceUrl", rmiUri);
        mutablePropertyValues.addPropertyValue("serviceInterface", type);
        BeanDefinition beanDefinition = new RootBeanDefinition(HttpInvokerProxyFactoryBean.class,
                constructorArgumentValues, mutablePropertyValues);

        factory.registerBeanDefinition(interfaceId, beanDefinition);
        registeredInterfaces.add(interfaceId);
    }

    public void removeRmiInterface(String interfaceId) {
        DefaultListableBeanFactory factory = (DefaultListableBeanFactory)
                configurableApplicationContext.getBeanFactory();
        if (factory.containsBean(interfaceId)) {
            factory.removeBeanDefinition(interfaceId);
            registeredInterfaces.remove(interfaceId);
        }
    }

    public Set<String> getRegisteredInterfacesId() {
        ImmutableSet<String> result = ImmutableSet.copyOf(registeredInterfaces);
        return result;
    }

    public void dispose() {
        configurableApplicationContext.close();
    }
}
