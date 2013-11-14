package org.hiperion.common.processors;

/**
 * Created with IntelliJ IDEA.
 * User: iobestar
 * Date: 16.03.13.
 * Time: 21:52
 */
public class PeriodicActionProcessor extends PeriodicAbstractProcessor {

    private static int PERIODIC_ACTION_PROCESSOR_INDEX = 0;

    private PeriodicAction periodicAction;

    public PeriodicActionProcessor(PeriodicAction periodicAction) {
        super("PeriodicActionProcessor_" + (++PERIODIC_ACTION_PROCESSOR_INDEX));
        this.periodicAction = periodicAction;
    }

    @Override
    protected void process() throws InterruptedException {
        periodicAction.runAction();
    }
}
