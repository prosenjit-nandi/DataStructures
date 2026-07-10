package datastructure.queue;

import datastructure.interfaces.Queue;

/**
 * @description A Queue implementation backed by a circular fixed-size array.
 * @usage Useful for First-In-First-Out (FIFO) processing when the maximum capacity is known in advance and high performance with O(1) enqueue/dequeue is desired.
 * @summary Implements the Queue interface using a circular array technique. Maintains 'front' and 'size' pointers to handle wrap-around indices efficiently without shifting elements.
 */
public class ArrayQueue<T> implements Queue<T> {

    private final T[] data;
    private final int capacity;
    private int front;
    private int size;

    public ArrayQueue() {
        this(10000);
    }

    @SuppressWarnings("unchecked")
    public ArrayQueue(int capacity) {
        this.capacity = capacity;
        this.data = (T[]) new Object[capacity];
        this.front = 0;
        this.size = 0;
    }

    @Override
    public void enQueue(T item) {
        if (size == capacity) {
            throw new IllegalStateException("Queue is full");
        }
        int end = (front + size) % capacity;
        data[end] = item;
        size++;
    }

    @Override
    public T deQueue() {
        if (size == 0) {
            return null;
        }
        var item = data[front];
        data[front] = null;
        front = (front + 1) % capacity;
        size--;
        return item;
    }

    @Override
    public T peek() {
        return size == 0 ? null : data[front];
    }

    @Override
    public boolean contains(T item) {
        for (int i = 0; i < size; i++) {
            if (data[(front + i) % capacity].equals(item)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public T get(int position) {
        if (position < 0 || position >= size) {
            return null;
        }
        return data[(front + position) % capacity];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }
}
