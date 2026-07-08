package datastructure.queue;

import datastructure.interfaces.Queue;

import java.util.ArrayList;
import java.util.List;

public class ListQueue<T> implements Queue<T> {

    private final List<T> data;

    public ListQueue() {
        data = new ArrayList<>();
    }

    @Override
    public void enQueue(T item) {
        data.add(item);
    }

    @Override
    public T deQueue() {
        if (data.isEmpty()) {
            return null;
        }
        return data.remove(0);
    }

    @Override
    public T peek() {
        return data.isEmpty() ? null : data.get(0);
    }

    @Override
    public boolean contains(T item) {
        return data.contains(item);
    }

    @Override
    public T get(int position) {
        if (position < 0 || position >= data.size()) {
            return null;
        }
        return data.get(position);
    }

    @Override
    public int size() {
        return data.size();
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }
}
