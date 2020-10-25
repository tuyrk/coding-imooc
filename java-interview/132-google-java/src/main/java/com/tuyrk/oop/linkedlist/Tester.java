package com.tuyrk.oop.linkedlist;

import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Tester {
    @Test
    public void testInt() {
        LinkedList<Integer> intList = LinkedList.newEmptyList();
        IntStream.rangeClosed(1, 100).forEach(intList::add);
        for (Integer value : intList) {
            System.out.println(value);
        }
    }

    @Test
    public void testString() {
        LinkedList<String> stringList = LinkedList.newEmptyList();
        StringBuilder sb = new StringBuilder();
        IntStream.rangeClosed(1, 100).forEach(i -> {
            sb.append("a");
            stringList.add(sb.toString());
        });
        for (String value : stringList) {
            System.out.println(value);
        }
    }

    @Test
    public void testList() {
        List<Integer> list1 = new ArrayList<>(); // Java1.7
        List<Integer> list2 = Stream.of(1, 2, 3).collect(Collectors.toList());
        List<Integer> list3 = Collections.emptyList();

        // Objects.equals(emptyIntList, LinkedList.<Integer> newEmptyList());
    }
}

class TypeArrayList<T> {
    public static <T> void printList(List<T> list) {
    }
}
