package org.hiperion.core.service.collecting;

import org.apache.log4j.Logger;
import org.hiperion.common.exception.HiperionException;
import org.hiperion.core.model.data.collector.PullDataCollector;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 22.03.13.
 * Time: 14:42
 */
@DisallowConcurrentExecution
public class CollectingJob implements Job {

    private final static Logger LOGGER = Logger.getLogger(CollectingJob.class);

    public final static String COLLECTOR_MAP_KEY = "dc";

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            PullDataCollector pullDataCollector = (PullDataCollector)
                    jobExecutionContext.getJobDetail().getJobDataMap().get(COLLECTOR_MAP_KEY);
            LOGGER.info("Collecting data for data collector: " + pullDataCollector.getCollectorId());
            pullDataCollector.collectData();
        } catch (HiperionException e) {
            throw new JobExecutionException(e);
        }
    }
}
