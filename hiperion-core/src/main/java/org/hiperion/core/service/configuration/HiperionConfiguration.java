package org.hiperion.core.service.configuration;

import org.hiperion.common.exception.DataCollectorNotFound;
import org.hiperion.common.exception.EntityAlreadyExist;
import org.hiperion.common.exception.EntityDoesNotExist;
import org.hiperion.common.exception.HiperionException;

import java.util.Collection;

/**
 * User: iobestar
 * Date: 05.05.13.
 * Time: 11:08
 */
public interface HiperionConfiguration<S, K, E> {

    void init();

    void addConfiguration(S configurationSource) throws HiperionException;

    void removeConfiguration(K configurationId) throws HiperionException;

    void reloadConfiguration(K key) throws HiperionException;

    E findById(K configurationId) throws HiperionException;

    Collection<E> findAll() throws HiperionException;

    String xmlConfigurationContent(String configurationId) throws HiperionException;
}
