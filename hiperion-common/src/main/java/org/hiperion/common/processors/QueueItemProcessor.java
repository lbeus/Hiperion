package org.hiperion.common.processors;

import org.apache.log4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * User: iobestar
 * Date: 16.03.13.
 * Time: 21:25
 */
public class QueueItemProcessor<T> extends PeriodicAbstractProcessor {

    private final static Logger LOGGER = Logger.getLogger(QueueItemProcessor.class);

    private static int QUEUE_ITEM_PROCESSOR_INDEX = 0;

    private final ItemProcessor<T> itemProcessor;
    private final BlockingQueue<T> queue;
    private AtomicBoolean blocked;

    public QueueItemProcessor(ItemProcessor<T> itemProcessor, BlockingQueue<T> queue) {
        super("QueueItemProcessor_" + (++QUEUE_ITEM_PROCESSOR_INDEX));
        this.itemProcessor = itemProcessor;
        this.queue = queue;
        this.blocked = new AtomicBoolean();
    }

    @Override
    protected void process() throws InterruptedException {
        T item;
        try {
            blocked.set(true);
            item = queue.take();
            blocked.set(false);
        } catch (InterruptedException e) {
            LOGGER.debug(e);
            throw e;
        }

        if (null == item) {
            return;
        }
        itemProcessor.process(item);
    }

    public boolean isBlocked() {
        return blocked.get();
    }

    public void queue(T item) {
        queue.add(item);
    }

    public int getQueueSize() {
        return queue.size();
    }
}
