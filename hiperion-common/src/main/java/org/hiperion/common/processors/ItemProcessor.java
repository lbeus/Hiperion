package org.hiperion.common.processors;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 16.03.13.
 * Time: 21:25
 */
public interface ItemProcessor<T> {
    void process(T item);
}
