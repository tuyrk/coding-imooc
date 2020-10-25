package com.tuyrk.adv;

import org.junit.Test;

import java.util.Arrays;

public class Tester {
    @Test
    public void testExternalSort() {
        Iterable<Long> data1 = new Range(1L, 1000000000000L, 1L);
        Iterable<Long> data2 = new Range(1L, 1000000000000L, 2L);
        Iterable<Long> data3 = new Range(1L, 1000000000000L, 3L);
        Iterable<Long> data4 = new Range(1L, 1000000000000L, 5L);
        Iterable<Long> data5 = new Range(1L, 1000000000000L, 7L);

        Iterable<Long> smallData1 = new Range(1L, 10L, 1L);
        // 1, 2, 3, 4, 5, ... 9

        Iterable<Long> smallData2 = new Range(1L, 10L, 2L);
        // 1, 3, 5, 7, 9

        ExternalSort sort = new ExternalSort();

        System.out.println("Testing small data set.");
        Iterable<Long> resultSmall = sort.merge(Arrays.asList(smallData1, smallData2));
        printInitialResults(resultSmall, 100);

        System.out.println("Testing normal data set again.");
        Iterable<Long> anotherResult = sort.merge(Arrays.asList(
                sort.merge(Arrays.asList(data1, data2)),
                sort.merge(Arrays.asList(data3, data4)),
                data5)
        );
        printInitialResults(anotherResult, 100);
    }

    private static void printInitialResults(Iterable<Long> resultSmall, int resultsToPrint) {
        int count = 0;
        for (Long value : resultSmall) {
            System.out.print(value + " ");
            count++;
            if (count >= resultsToPrint) {
                break;
            }
        }
        System.out.println();
    }
}
