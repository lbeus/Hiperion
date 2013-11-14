package org.hiperion.core.service.collecting.manager;

import org.apache.log4j.Logger;
import org.hiperion.common.exception.HiperionException;
import org.hiperion.core.model.data.collector.PullDataCollector;
import org.hiperion.core.service.collecting.CollectingJob;
import org.quartz.*;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 30.03.13.
 * Time: 18:13
 */
public class ScheduledPullCollectingManager {

    private final static Logger LOGGER = Logger.getLogger(ScheduledPullCollectingManager.class);

    private final Scheduler pullCollectingScheduler;

    public ScheduledPullCollectingManager(Scheduler pullCollectingScheduler) {
        this.pullCollectingScheduler = pullCollectingScheduler;
    }

    public void schedulePullCollecting(PullDataCollector pullDataCollector)
            throws HiperionException {
        String cronExpression = pullDataCollector.getCronExpression();
        if (!isValidCronExpression(cronExpression)) {
            throw new HiperionException("Cron expression not valid.");
        }

        JobDetail collectingJobDetail = newJob(CollectingJob.class).withIdentity(pullDataCollector.getCollectorId()).build();
        collectingJobDetail.getJobDataMap().put(CollectingJob.COLLECTOR_MAP_KEY, pullDataCollector);
        CronTrigger cronTrigger = newTrigger().withIdentity(pullDataCollector.getCollectorId())
                .withSchedule(cronSchedule(cronExpression)).build();
        try {
            pullCollectingScheduler.scheduleJob(collectingJobDetail, cronTrigger);
            LOGGER.info("Data collector " + pullDataCollector.getCollectorId() + " scheduled.");
        } catch (SchedulerException e) {
            throw new HiperionException(e);
        }
    }

    public void unschedulePullCollecting(String dataCollectorId)
            throws HiperionException {
        try {
            pullCollectingScheduler.unscheduleJob(new TriggerKey(dataCollectorId));
            LOGGER.info("Data collector " + dataCollectorId + " unscheduled.");
        } catch (SchedulerException e) {
            throw new HiperionException(e);
        }
    }

    private boolean isValidCronExpression(String cronExpression) {
        if (null == cronExpression) {
            return true;
        }
        return CronExpression.isValidExpression(cronExpression);
    }

    public boolean containsDataCollector(String dataCollectorId) {
        try {
            return pullCollectingScheduler.checkExists(new TriggerKey(dataCollectorId));
        } catch (SchedulerException e) {
            LOGGER.error(e);
        }
        return false;
    }

    public void start() {
        try {
            pullCollectingScheduler.start();
        } catch (SchedulerException e) {
            LOGGER.error(e);
        }
    }

    public void stop() {
        try {
            pullCollectingScheduler.shutdown();
        } catch (SchedulerException e) {
            LOGGER.error(e);
        }
    }
}
