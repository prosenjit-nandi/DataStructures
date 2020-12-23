package datastructure.stack;

import datastructure.interfaces.Stack;

public class ArrayStack<T> implements Stack<T> {
    private final T[] data;
    private int pointer;

    public ArrayStack() {
        this(10000);
    }

    public ArrayStack(int size) {
        data = (T[]) new Object[size];
        pointer = 0;
    }

    @Override
    public void push(T item) {
        data[pointer++] = item;
    }

    @Override
    public T pop() {
        if (pointer == 0) {
            return null;
        }
        return data[--pointer];
    }

    @Override
    public boolean contains(T item) {
        for (T dataItem : data) if (dataItem.equals(item)) return true;
        return false;
    }

    @Override
    public T get(T item) {
        while (pointer > 0) {
            T dataItem = pop();
            if (dataItem.equals(item)) {
                return dataItem;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return pointer;
    }

}
