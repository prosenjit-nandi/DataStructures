package datastructure.interfaces;

public interface LinkedList<T> {
    void add(T item);

    T remove();

    void insert(int position, T item);

    T removeAt(int position);

    int find(T item);

    T get(int position);
}
