package com.tuyrk.designpattern.decorator;

public class LoggingRunable implements Runnable {

    private final Runnable innerRunnable;

    public LoggingRunable(Runnable innerRunnable) {
        this.innerRunnable = innerRunnable;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        System.out.println("Task started at " + startTime);

        innerRunnable.run();

        long endTime = System.currentTimeMillis();
        System.out.println("Task finished at " + endTime);
        System.out.println("Elapsed time: " + (endTime - startTime));
    }
}
