package com.tuyrk.designpattern.decorator;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class CodingTask implements Runnable {
    private final int employeeId;

    public CodingTask(int employeeId) {
        this.employeeId = employeeId;
    }

    @SneakyThrows
    @Override
    public void run() {
        log.info("Employee {} started writing code.", employeeId);
        TimeUnit.SECONDS.sleep(5);
        log.info("Employee {} finished writing code.", employeeId);
    }
}
