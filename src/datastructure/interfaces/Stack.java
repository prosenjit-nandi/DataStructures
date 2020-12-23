package datastructure.interfaces;

public interface Stack<T> {
    void push(T item);

    T pop();

    boolean contains(T item);

    T get(T item);

    int size();
}
