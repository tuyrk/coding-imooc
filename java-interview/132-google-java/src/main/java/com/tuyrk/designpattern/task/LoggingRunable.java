package com.tuyrk.designpattern.task;

public abstract class LoggingRunable implements Runnable {
    protected abstract void doRun();

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        System.out.println("Task started at " + startTime);

        doRun();

        long endTime = System.currentTimeMillis();
        System.out.println("Task finished at " + endTime);
        System.out.println("Elapsed time: " + (endTime - startTime));
    }
}
