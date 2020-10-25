package com.tuyrk.loop;

public class BinarySearch {
    /**
     * Searches element k in a sorted array
     *
     * @param arr a sorted array
     * @param k   the element to search
     * @return index in arr where k is. -1 if not found.
     */
    public static int binarySearch(int[] arr, int k) {
        int a = 0;
        int b = arr.length; // 此处b为数组最后一个元素的位置+1，形成区间[a, b)
        // Loop invariant: [a, b) is a valid range. (a <= b)
        // k may only be within range [a, b)
        while (a < b) {
            // int m = (a + b) / 2; // may overflow!
            // int m = a / 2 + b / 2; // 如果a和b都是奇数则计算结果将会比正确结果少1
            int m = a + (b - a) / 2;
            // b == a+1 : m = a
            // b == a+2 : m = a + 1
            if (k < arr[m]) {
                b = m; // 此处的b也保持是数组最后一个元素的位置+1
            } else if (k > arr[m]) {
                a = m + 1;
            } else {
                return m;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        System.out.println(binarySearch(new int[]{1, 2, 10, 15, 100}, 15));
        System.out.println(binarySearch(new int[]{1, 2, 10, 15, 100}, -2));
        System.out.println(binarySearch(new int[]{1, 2, 10, 15, 100}, 101));
        System.out.println(binarySearch(new int[]{1, 2, 10, 15, 100}, 13));
        System.out.println("====================");
        System.out.println(binarySearch(new int[]{}, 13));
        System.out.println(binarySearch(new int[]{12}, 13));
        System.out.println(binarySearch(new int[]{13}, 13));
        System.out.println("====================");
        System.out.println(binarySearch(new int[]{12, 13}, 12));
        System.out.println(binarySearch(new int[]{12, 13}, 13));
    }
}
