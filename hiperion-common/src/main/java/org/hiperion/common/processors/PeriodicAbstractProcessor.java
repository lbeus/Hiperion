package org.hiperion.common.processors;

import org.apache.log4j.Logger;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 16.03.13.
 * Time: 18:55
 */
public abstract class PeriodicAbstractProcessor implements Runnable {

    private final static Logger LOGGER = Logger.getLogger(PeriodicAbstractProcessor.class);

    private volatile Thread thread;
    private AtomicBoolean active;
    private AtomicBoolean processing;
    private AtomicBoolean dead;

    private final String name;

    protected PeriodicAbstractProcessor(String name) {
        this.name = name;
        this.thread = new Thread(this);
        this.thread.setName(name);
        this.active = new AtomicBoolean(false);
        this.processing = new AtomicBoolean(false);
        this.dead = new AtomicBoolean(false);
    }

    public void start() {
        if (isActive()) {
            return;
        }
        if(null == this.thread){
            this.thread = new Thread(this);
            this.thread.setName(name);
        }
        active.set(true);
        this.thread.start();
    }

    @Override
    public void run() {

        while (isActive()) {
            try {
                processing.set(true);
                process();
                processing.set(false);
            } catch (InterruptedException e) {
                LOGGER.debug(e);
            }
        }
    }

    public Thread stop(final long waitMillis) {
        if (!isActive()) {
            return null;
        }
        Thread stoppingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    active.set(false);
                    final int sleepingPart = 50;
                    long sleepingTime = 0;
                    while (true) {
                        if (!isProcessing()) {
                            break;
                        }
                        Thread.sleep(sleepingPart);
                        sleepingTime += sleepingPart;
                        if (sleepingTime > waitMillis) {
                            break;
                        }
                    }
                    if (isProcessing()) {
                        thread.interrupt();
                    }
                } catch (InterruptedException e) {
                    LOGGER.debug(e);
                }
                thread = null;
            }
        });
        stoppingThread.start();
        return stoppingThread;
    }

    public Thread stop() {
        return stop(1000);
    }

    public boolean isActive() {
        return active.get();
    }

    public boolean isProcessing() {
        return processing.get();
    }

    protected abstract void process() throws InterruptedException;
}
