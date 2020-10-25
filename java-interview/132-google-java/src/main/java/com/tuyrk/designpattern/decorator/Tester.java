package com.tuyrk.designpattern.decorator;

import org.junit.Test;

public class Tester {
    @Test
    public void testRun() {
        new LoggingRunable(new CodingTask(0)).run();
        System.out.println("====");
        new TransactionalRunnable(new LoggingRunable(new CodingTask(0))).run();
        System.out.println("====");
        new LoggingRunable(new TransactionalRunnable(new CodingTask(0))).run();
    }
}
