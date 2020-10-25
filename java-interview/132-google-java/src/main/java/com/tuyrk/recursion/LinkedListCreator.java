package com.tuyrk.recursion;

import com.tuyrk.common.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class LinkedListCreator {
    /**
     * Creates a linked list.
     *
     * @param data data the data to create the list
     * @return head of the linked list. The returned linked list ends with last node with getNext() == null.
     */
    public static <T> Node<T> createLinkedList(List<T> data) {
        if (data.isEmpty()) {
            return null;
        }

        Node<T> firstNode = new Node<>(data.get(0));
        firstNode.setNext(createLinkedList(data.subList(1, data.size())));
        return firstNode;
    }

    public static Node createLargeLinkedList(int size) {
        Node prev = null;
        Node head = null;

        for (int i = 1; i <= size; i++) {
            Node node = new Node<>(i);
            if (prev != null) {
                prev.setNext(node);
            } else {
                head = node;
            }
            prev = node;
        }
        return head;
    }

    public static void main(String[] args) {
        // Node.printLinkedList(createLinkedList(new ArrayList<>()));
        // Node.printLinkedList(createLinkedList(Arrays.asList(1)));
        // Node.printLinkedList(createLinkedList(Arrays.asList(1, 2, 3, 4, 5)));

        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 5, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1));
        for (int i = 0; i < 5; i++) {
            executor.execute(() -> {
                System.out.println(System.currentTimeMillis());
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }


}
