package com.tuyrk.recursion;

import com.tuyrk.common.Node;

import java.util.ArrayList;
import java.util.Arrays;

public class LinkedListReverser {
    /**
     * Reverses a linked list.
     *
     * @param head head the linked list to reverse
     * @return head of the reversed linked list
     */
    public static <T> Node<T> reverseLinkedList(Node<T> head) {
        // size == 0 or size == 1
        if (head == null || head.getNext() == null) {
            return head;
        }

        Node<T> newHead = reverseLinkedList(head.getNext());
        head.getNext().setNext(head);
        head.setNext(null);
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
