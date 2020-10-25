package com.tuyrk.recursion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Combinations {
    /**
     * Generates all combinations and output them, selecting n elements from data.
     *
     * @param selected 已选择的数组
     * @param data     数组
     * @param n        选择个数
     */
    public static void combinations(List<Integer> selected, List<Integer> data, int n) {
        // initial value for recursion
        // how to select elements
        // how to output

        if (n < 0) {
            return;
        }
        if (n == 0) {
            // output all selected elements + empty list
            selected.forEach(e -> System.out.print(e + " "));
            System.out.println();
            return;
        }

        if (data.isEmpty()) {
            return;
        }
        if (data.size() < n) {
            return;
        }

        selected.add(data.get(0)); // select element 0
        combinations(selected, data.subList(1, data.size()), n - 1);

        selected.remove(selected.size() - 1); // un-select element 0
        combinations(selected, data.subList(1, data.size()), n);
    }

    public static void main(String[] args) {
        combinations(new ArrayList<>(), new ArrayList<>(), 2);
        System.out.println("==============================");
        combinations(new ArrayList<>(), new ArrayList<>(), 0);
        System.out.println("==============================");
        combinations(new ArrayList<>(), Arrays.asList(1, 2, 3, 4, 5), 0);
        System.out.println("==============================");
        combinations(new ArrayList<>(), Arrays.asList(1, 2, 3, 4, 5), 1);
        System.out.println("==============================");
        combinations(new ArrayList<>(), Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), 4);
    }
}
