package com.tuyrk.google;

public class BeautifulNumber {
    public static long beautiful(long num) {
        for (long radix = 2; radix < num; radix++) {
            if (isBeautiful(num, radix)) {
                return radix;
            }
        }
        throw new IllegalStateException("Should not reach here.");
    }

    private static boolean isBeautiful(long num, long radix) {
        while (num > 0) {
            if (num % radix != 1) {
                return false;
            }
            num /= radix;
        }
        return true;
    }
}
