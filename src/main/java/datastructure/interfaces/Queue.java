package datastructure.interfaces;

public interface Queue<T> {
    void enQueue(T item);

    T deQueue();

    T peek();

    boolean contains(T item);

    T get(int position);

    int size();

    boolean isEmpty();
}
