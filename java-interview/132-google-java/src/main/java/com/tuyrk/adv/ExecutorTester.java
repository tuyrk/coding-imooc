package com.tuyrk.adv;

import com.tuyrk.designpattern.decorator.CodingTask;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

@Slf4j
public class ExecutorTester {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        List<Future<?>> taskResults = new ArrayList<>();
        IntStream.rangeClosed(1, 10).forEach(i -> {
            taskResults.add(executorService.submit(new CodingTask(i)));
        });
        log.info("10 tasks dispatched successfully.");

        /// shutdown()方法会等待已有的线程执行完成，如果添加新的任务则会抛出异常
        /*for (Future<?> taskResult : taskResults) {
            taskResult.get();
        }
        log.info("All tasks finished.");*/

        executorService.shutdown();
    }
}
