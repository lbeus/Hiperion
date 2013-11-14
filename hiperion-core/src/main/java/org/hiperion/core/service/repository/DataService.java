package org.hiperion.core.service.repository;

import org.hiperion.common.exception.EntityAlreadyExist;
import org.hiperion.common.exception.EntityDoesNotExist;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 24.03.13.
 * Time: 10:58
 */
public interface DataService<K, E> {
    void save(E source) throws EntityAlreadyExist;

    void delete(K key);

    E findById(K id) throws EntityDoesNotExist;

    List<E> findAll();
}
