package com.tuyrk.google;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Tester {
    public static void main(String[] args) {
        Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        int cases = in.nextInt();
        for (int i = 1; i <= cases; i++) {
            long num = in.nextLong();
            System.out.printf("Case #%d: %d %d\n", i, BeautifulNumber.beautiful(num), BeautifulNumberLarge.beautiful(num));
        }
    }
}
