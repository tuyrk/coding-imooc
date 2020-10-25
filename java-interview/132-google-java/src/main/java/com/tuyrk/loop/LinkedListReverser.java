package com.tuyrk.loop;

import com.tuyrk.common.Node;
import com.tuyrk.recursion.LinkedListCreator;

import java.util.ArrayList;
import java.util.Arrays;

public class LinkedListReverser {

    public static <T> Node<T> reverseLinkedList(Node<T> head) {
        Node<T> newHead = null;
        Node<T> curHead = head;
        // Loop invariant:
        // newHead points to the linked list already reversed.
        // curHead points to the linked list not yet reversed.

        // Loop invariant holds.
        while (curHead != null) {
            // Loop invariant holds.
            Node<T> next = curHead.getNext();
            curHead.setNext(newHead);
            newHead = curHead;
            curHead = next;
            // Loop invariant holds.
        }
        // Loop invariant holds.
        return newHead;
    }

    public static void main(String[] args) {
        Node.printLinkedList(reverseLinkedList(LinkedListCreator.createLinkedList(new ArrayList<>())));
        Node.printLinkedList(reverseLinkedList(LinkedListCreator.createLinkedList(Arrays.asList(1))));
        Node.printLinkedList(reverseLinkedList(LinkedListCreator.createLinkedList(Arrays.asList(1, 2, 3, 4, 5))));
        reverseLinkedList(LinkedListCreator.createLargeLinkedList(1000000));
        System.out.println("done");
    }
}
