package datastructure.stack;

import datastructure.interfaces.Stack;

/**
 * @description A Stack implementation backed by a fixed-size array.
 * @usage Ideal for Last-In-First-Out (LIFO) processing when the maximum number of elements is known beforehand, ensuring low memory overhead and strict O(1) performance.
 * @summary Implements the Stack interface using an array and a pointer indicating the top. Elements are pushed by incrementing the pointer and popped by decrementing it.
 */
public class ArrayStack<T> implements Stack<T> {
    private final T[] data;
    private final int capacity;
    private int pointer;

    public ArrayStack() {
        this(10000);
    }

    @SuppressWarnings("unchecked")
    public ArrayStack(int capacity) {
        this.capacity = capacity;
        data = (T[]) new Object[capacity];
        pointer = 0;
    }

    @Override
    public void push(T item) {
        if (pointer == capacity) {
            throw new IllegalStateException("Stack is full");
        }
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
    public T peek() {
        return pointer == 0 ? null : data[pointer - 1];
    }

    @Override
    public boolean contains(T item) {
        for (int i = 0; i < pointer; i++) {
            if (data[i].equals(item)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public T get(T item) {
        for (int i = pointer - 1; i >= 0; i--) {
            if (data[i].equals(item)) {
                return data[i];
            }
        }
        return null;
    }

    @Override
    public int size() {
        return pointer;
    }

    @Override
    public boolean isEmpty() {
        return pointer == 0;
    }
}
