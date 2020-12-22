package datastructure;

public class BasicBinaryTree<T extends Comparable<T>> {

    private Node root;
    private int size;

    public BasicBinaryTree(){
        this.root = null;
    }

    public int size(){
        return size;
    }

    public void add(T item){
        Node node = new Node(item);
        if (root == null){
            this.root = node;
            size++;
            return;
        }
        insert(root, node);
    }

    public boolean contains(T item){
        if(root == null) return false;
        return search(root, new Node(item)) != null;
    }

    public boolean delete(T item){
        boolean deleted = false;
        if(this.root == null){
            return false;
        }

        Node currentNode = search(root, new Node(item));
        if(currentNode != null){
            if(currentNode.getRight() == null && currentNode.getLeft() == null){
                unlink(currentNode, null);
                deleted = true;
            } else if(currentNode.getRight() != null && currentNode.getLeft() == null){
                unlink(currentNode, currentNode.getRight());
                deleted = true;
            } else if(currentNode.getRight() == null && currentNode.getLeft() != null){
                unlink(currentNode, currentNode.getLeft());
                deleted = true;
            } else {
                //swap with right most leaf node of the left child
                Node child = currentNode.getLeft();
                while(child.getRight() != null && child.getLeft() != null){
                    child = child.getRight();
                }
                child.parent.setRight(null);
                child.setLeft(currentNode.getLeft());
                child.setRight(currentNode.getRight());

                unlink(currentNode, child);
                deleted = true;
            }
        }
        if(deleted)
            size--;

        return deleted;
    }

    private void unlink(Node currentNode, Node newNode){
        if(currentNode == root){
            newNode.setRight(currentNode.getRight());
            newNode.setLeft(currentNode.getLeft());
        } else if (currentNode.getParent().getRight() == currentNode){
            currentNode.getParent().setRight(newNode);
        } else {
            currentNode.getParent().setLeft(newNode);
        }
    }

    private void insert(Node parent, Node child){
        if(child.getItem().compareTo(parent.getItem()) < 0){
            if(parent.getLeft() == null){
                parent.setLeft(child);
                child.setParent(parent);
                size++;
            } else {
                insert(parent.getLeft(), child);
            }
        } else {
            if(parent.getRight() == null){
                parent.setRight(child);
                child.setParent(parent);
                size++;
            } else {
                insert(parent.getRight(), child);
            }
        }
    }

    private Node search(Node parent, Node child){
        if(child.getItem().compareTo(parent.getItem()) == 0){
            return parent;
        }
        if(child.getItem().compareTo(parent.getItem()) < 0){
            if(parent.getLeft() == null){
                return null;
            } else {
                return search(parent.getLeft(), child);
            }
        } else {
            if(parent.getRight() == null){
                return null;
            } else {
                return search(parent.getRight(), child);
            }
        }
    }

    private class Node{
        private Node left;
        private Node right;
        private Node parent;
        private T item;

        public Node(T item){
            this.item = item;
            this.left = null;
            this.right = null;
            this.parent = null;
        }

        public Node getLeft() {
            return left;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public Node getRight() {
            return right;
        }

        public void setRight(Node right) {
            this.right = right;
        }

        public Node getParent() {
            return parent;
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }

        public T getItem() {
            return item;
        }

        public void setItem(T item) {
            this.item = item;
        }
    }
}
