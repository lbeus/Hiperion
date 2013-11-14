package org.hiperion.core.service.repository.collectingoutput;

import org.hiperion.core.service.collecting.CollectingOutput;
import org.hiperion.core.service.repository.DataService;

import java.util.Collection;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 24.04.13.
 * Time: 18:42
 */
public interface CollectingOutputDataService extends DataService<String, CollectingOutput> {
    void delete(Set<String> ids);

    Collection<CollectingOutput> findAll(long fromTimestamp, long toTimestamp);
}
