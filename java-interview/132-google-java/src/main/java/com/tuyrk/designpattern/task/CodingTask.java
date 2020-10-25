package com.tuyrk.designpattern.task;

import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;

public class CodingTask extends TransactionalRunnable {
    @SneakyThrows
    @Override
    protected void doRun() {
        System.out.println("Writing code.");
        TimeUnit.SECONDS.sleep(5);
    }
}
