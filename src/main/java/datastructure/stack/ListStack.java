package datastructure.stack;

import datastructure.interfaces.Stack;

import java.util.ArrayList;
import java.util.List;

public class ListStack<T> implements Stack<T> {
    private final List<T> data;

    public ListStack() {
        data = new ArrayList<>();
    }

    @Override
    public void push(T item) {
        data.add(item);
    }

    @Override
    public T pop() {
        if (data.isEmpty()) {
            return null;
        }
        return data.remove(data.size() - 1);
    }

    @Override
    public T peek() {
        return data.isEmpty() ? null : data.get(data.size() - 1);
    }

    @Override
    public boolean contains(T item) {
        return data.contains(item);
    }

    @Override
    public T get(T item) {
        for (int i = data.size() - 1; i >= 0; i--) {
            if (data.get(i).equals(item)) {
                return data.get(i);
            }
        }
        return null;
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
