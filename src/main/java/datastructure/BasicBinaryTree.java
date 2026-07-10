package datastructure;

import java.util.ArrayList;
import java.util.List;

/**
 * @description A basic Binary Search Tree (BST) implementation.
 * @usage Useful for maintaining a sorted collection of elements with efficient search, insertion, and deletion operations (O(log n) on average).
 * @summary Implements a node-based binary tree structure. Supports insertion, searching, Hibbard deletion, and in-order traversal.
 */
public class BasicBinaryTree<T extends Comparable<T>> {

    private Node root;
    private int size;

    public BasicBinaryTree() {
        this.root = null;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void add(T item) {
        root = insert(root, item);
        size++;
    }

    public boolean contains(T item) {
        return search(root, item) != null;
    }

    // Standard Hibbard deletion: copy the in-order successor's value into the
    // node being removed, then delete the successor (which has at most one
    // child). This avoids manual parent-pointer surgery, which previously
    // could leave a node pointing at itself when the immediate left child had
    // no right subtree.
    public boolean delete(T item) {
        if (search(root, item) == null) {
            return false;
        }
        root = deleteRecursive(root, item);
        size--;
        return true;
    }

    public List<T> inorderTraversal() {
        var result = new ArrayList<T>();
        inorder(root, result);
        return result;
    }

    private Node insert(Node node, T item) {
        if (node == null) {
            return new Node(item);
        }
        if (item.compareTo(node.getItem()) < 0) {
            node.setLeft(insert(node.getLeft(), item));
        } else {
            node.setRight(insert(node.getRight(), item));
        }
        return node;
    }

    private Node search(Node node, T item) {
        if (node == null) {
            return null;
        }
        var comparison = item.compareTo(node.getItem());
        if (comparison == 0) {
            return node;
        }
        return comparison < 0 ? search(node.getLeft(), item) : search(node.getRight(), item);
    }

    private Node deleteRecursive(Node node, T item) {
        var comparison = item.compareTo(node.getItem());
        if (comparison < 0) {
            node.setLeft(deleteRecursive(node.getLeft(), item));
            return node;
        }
        if (comparison > 0) {
            node.setRight(deleteRecursive(node.getRight(), item));
            return node;
        }
        if (node.getLeft() == null) {
            return node.getRight();
        }
        if (node.getRight() == null) {
            return node.getLeft();
        }
        var successor = min(node.getRight());
        node.setItem(successor.getItem());
        node.setRight(deleteMin(node.getRight()));
        return node;
    }

    private Node min(Node node) {
        while (node.getLeft() != null) {
            node = node.getLeft();
        }
        return node;
    }

    private Node deleteMin(Node node) {
        if (node.getLeft() == null) {
            return node.getRight();
        }
        node.setLeft(deleteMin(node.getLeft()));
        return node;
    }

    private void inorder(Node node, List<T> result) {
        if (node == null) {
            return;
        }
        inorder(node.getLeft(), result);
        result.add(node.getItem());
        inorder(node.getRight(), result);
    }

    private class Node {
        private Node left;
        private Node right;
        private T item;

        Node(T item) {
            this.item = item;
        }

        Node getLeft() {
            return left;
        }

        void setLeft(Node left) {
            this.left = left;
        }

        Node getRight() {
            return right;
        }

        void setRight(Node right) {
            this.right = right;
        }

        T getItem() {
            return item;
        }

        void setItem(T item) {
            this.item = item;
        }
    }
}
