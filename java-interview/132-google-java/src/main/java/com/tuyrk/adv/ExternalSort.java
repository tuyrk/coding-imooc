package com.tuyrk.adv;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

public class ExternalSort {
    private static class ResultEntry<T extends Comparable<T>> implements Comparable<ResultEntry<T>> {
        private final T value;
        private final Iterator<T> source;

        public ResultEntry(T value, Iterator<T> source) {
            this.value = value;
            this.source = source;
        }

        @Override
        public int compareTo(ResultEntry<T> o) {
            // compare only value with o.value
            return value.compareTo(o.value);
        }
    }

    private static class MergeResultIterator<T extends Comparable<T>> implements Iterator<T> {
        private final PriorityQueue<ResultEntry<T>> queue;

        public MergeResultIterator(List<Iterable<T>> sortedData) {
            // Collect minimum data in each sortedData.
            this.queue = new PriorityQueue<>();
            for (Iterable<T> data : sortedData) {
                Iterator<T> iterator = data.iterator();
                if (iterator.hasNext()) {
                    this.queue.add(new ResultEntry<>(iterator.next(), iterator));
                }
            }
        }

        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        /**
         * Finds minimum data in our collection.
         */
        @Override
        public T next() {
            if (queue.isEmpty()) {
                throw new NoSuchElementException();
            }

            ResultEntry<T> entry = queue.poll();
            // Replace extracted data with next minimum in its source.
            if (entry.source.hasNext()) {
                queue.add(new ResultEntry<>(entry.source.next(), entry.source));
            }
            return entry.value;
        }


    }

    public Iterable<Long> merge(List<Iterable<Long>> sortedData) {
        return new Iterable<Long>() {
            @Override
            public Iterator<Long> iterator() {
                return new MergeResultIterator<>(sortedData);
            }
        };
    }
}
