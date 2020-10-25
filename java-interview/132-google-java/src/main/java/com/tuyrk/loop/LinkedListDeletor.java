package com.tuyrk.loop;


import com.tuyrk.common.Node;
import com.tuyrk.recursion.LinkedListCreator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class LinkedListDeletor {
    public static <T> Node<T> deleteIfEquals(Node<T> head, T value) {
        while (head != null && Objects.equals(head.getValue(), value)) {
            head = head.getNext();
        }
        if (head == null) {
            return null;
        }

        Node<T> prev = head;
        // Loop invariant: list nodes from head up to prev has bean processed. (Nodes with values equal to value are deleted.)
        while (prev.getNext() != null) {
            if (Objects.equals(prev.getNext().getValue(), value)) {
                // delete it.
                prev.setNext(prev.getNext().getNext());
            } else {
                prev = prev.getNext();
            }
        }

        return head;
    }

    public static void main(String[] args) {
        Node.printLinkedList(deleteIfEquals(LinkedListCreator.createLinkedList(Arrays.asList(1, 2, 3, 2, 5)), 2));
        Node.printLinkedList(deleteIfEquals(LinkedListCreator.createLinkedList(Arrays.asList(1, 2, 3, 2, 2)), 2));
        Node.printLinkedList(deleteIfEquals(LinkedListCreator.createLinkedList(Arrays.asList(1, 2, 3, 2, 2)), 1));
        Node.printLinkedList(deleteIfEquals(LinkedListCreator.createLinkedList(Arrays.asList(2, 2, 3, 2, 2)), 2));
        Node.printLinkedList(deleteIfEquals(LinkedListCreator.createLinkedList(Arrays.asList(2, 2, 2, 2, 2)), 2));
        Node.printLinkedList(deleteIfEquals(LinkedListCreator.createLinkedList(Arrays.asList(2)), 2));
        Node.printLinkedList(deleteIfEquals(LinkedListCreator.createLinkedList(Arrays.asList(2)), 1));
        Node.printLinkedList(deleteIfEquals(LinkedListCreator.createLinkedList(new ArrayList<Integer>()), 2));
    }
}
