package datastructure;

import datastructure.interfaces.LinkedList;

/**
 * @description A basic singly linked list implementation.
 * @usage Used for collections where frequent dynamic additions and removals (especially at the beginning or end) are needed without the overhead of resizing an underlying array.
 * @summary Composed of Node objects where each node points to the next. Supports standard list operations like add, insert, remove, and find.
 */
public class BasicLinkedList<T> implements LinkedList<T> {

    private Node first;
    private Node last;
    private int nodeCount;

    public BasicLinkedList() {
        first = null;
        last = null;
        nodeCount = 0;
    }

    @Override
    public int size() {
        return nodeCount;
    }

    @Override
    public boolean isEmpty() {
        return nodeCount == 0;
    }

    @Override
    public void add(T item) {
        var itemNode = new Node(item);
        if (nodeCount == 0) {
            first = itemNode;
            last = first;
        } else {
            last.setNextNode(itemNode);
            last = itemNode;
        }
        nodeCount++;
    }

    @Override
    public T remove() {
        if (nodeCount == 0) {
            return null;
        }
        T item = first.getNodeItem();
        first = first.getNextNode();
        nodeCount--;
        if (nodeCount == 0) {
            last = null;
        }
        return item;
    }

    @Override
    public void insert(int position, T item) {
        if (position < 0 || position > nodeCount) {
            throw new IndexOutOfBoundsException("position: " + position + ", size: " + nodeCount);
        }
        if (position == nodeCount) {
            add(item);
            return;
        }
        if (position == 0) {
            var newNode = new Node(item);
            newNode.setNextNode(first);
            first = newNode;
            nodeCount++;
            return;
        }
        Node prevNode = nodeAt(position - 1);
        var newNode = new Node(item);
        newNode.setNextNode(prevNode.getNextNode());
        prevNode.setNextNode(newNode);
        nodeCount++;
    }

    @Override
    public T removeAt(int position) {
        if (position < 0 || position >= nodeCount) {
            throw new IndexOutOfBoundsException("position: " + position + ", size: " + nodeCount);
        }
        if (position == 0) {
            return remove();
        }
        Node prevNode = nodeAt(position - 1);
        Node currentNode = prevNode.getNextNode();
        prevNode.setNextNode(currentNode.getNextNode());
        if (currentNode == last) {
            last = prevNode;
        }
        nodeCount--;
        return currentNode.getNodeItem();
    }

    @Override
    public int find(T item) {
        Node currentNode = first;
        for (int i = 0; i < nodeCount; i++) {
            if (currentNode.getNodeItem().equals(item)) {
                return i;
            }
            currentNode = currentNode.getNextNode();
        }
        return -1;
    }

    @Override
    public T get(int position) {
        if (position < 0 || position >= nodeCount) {
            throw new IndexOutOfBoundsException("position: " + position + ", size: " + nodeCount);
        }
        return nodeAt(position).getNodeItem();
    }

    private Node nodeAt(int position) {
        Node currentNode = first;
        for (int i = 0; i < position; i++) {
            currentNode = currentNode.getNextNode();
        }
        return currentNode;
    }

    private class Node {
        private Node nextNode;
        private final T nodeItem;

        Node(T item) {
            this.nodeItem = item;
            this.nextNode = null;
        }

        Node getNextNode() {
            return this.nextNode;
        }

        void setNextNode(Node n) {
            this.nextNode = n;
        }

        T getNodeItem() {
            return this.nodeItem;
        }
    }
}
