package com.tuyrk.oop.linkedlist;

import com.tuyrk.common.Node;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedList<T> implements Iterable<T> {
    private Node<T> head;
    private Node<T> tail;

    private LinkedList() {
        head = null;
        tail = null;
    }

    public static <T> LinkedList<T> newEmptyList() {
        return new LinkedList<T>();
    }

    public void add(T value) {
        Node<T> node = new Node<>(value);
        if (tail == null) {
            head = node;
        } else {
            tail.setNext(node);
        }
        tail = node;
    }

    private static class ListIterator<T> implements Iterator<T> {

        private Node<T> currentNode;

        private ListIterator(Node<T> head) {
            this.currentNode = head;
        }

        @Override
        public boolean hasNext() {
            return currentNode != null;
        }

        @Override
        public T next() {
            if (currentNode == null) {
                throw new NoSuchElementException();
            }
            T value = currentNode.getValue();
            currentNode = currentNode.getNext();
            return value;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new ListIterator<>(head);
    }
}
