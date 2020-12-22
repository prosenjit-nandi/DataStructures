package datastructure;

public class BasicLinkedList<T> implements LinkedList<T> {

    private Node first;
    private Node last;
    private int nodeCount;

    public BasicLinkedList(){
        first = null;
        last = null;
        nodeCount = 0;
    }

    public int size(){
        return nodeCount;
    }

    @Override
    public void add(T item) {
        Node itemNode = new Node(item);
        if(nodeCount == 0){
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
        if(nodeCount == 0){
            return null;
        }
        T item = first.getNodeItem();
        first = first.getNextNode();
        nodeCount--;
        return item;
    }

    @Override
    public void insert(int position, T item) {
        if(nodeCount == 0){
            add(item);
        }
        if(position > nodeCount){
            return;
        }
        Node currentNode = first;
        for (int i = 0; i < position && currentNode != null; i++) {
            currentNode = currentNode.getNextNode();
        }
        Node newNode = new Node(item);
        Node nextNode = currentNode.getNextNode();
        currentNode.setNextNode(newNode);
        newNode.setNextNode(nextNode);
        nodeCount++;
    }

    @Override
    public T removeAt(int position) {
        if(nodeCount == 0){
            return null;
        }
        if(position == 0){
            remove();
        }
        Node currentNode = first;
        Node prevNode = first;
        for (int i = 0; i < position && currentNode != null; i++) {
            prevNode = currentNode;
            currentNode = currentNode.getNextNode();
        }

        T item = currentNode.getNodeItem();
        prevNode.setNextNode(currentNode.getNextNode());
        nodeCount--;

        return item;
    }

    @Override
    public int find(T item) {
        Node currentNode = first;
        if(size() == 0){
            return -1;
        }
        for (int i = 0; i < size() && currentNode != null; i++) {
            if(currentNode.getNodeItem().equals(item)){
                return i;
            }
            currentNode = currentNode.getNextNode();
        }
        return -1;
    }

    @Override
    public T get(int position) {
        Node currentNode = first;
        if(nodeCount == 0){
            return first.getNodeItem();
        }
        for (int i = 0; i < position && currentNode != null; i++) {
            currentNode = currentNode.getNextNode();
        }

        return currentNode.getNodeItem();
    }



    private class Node {
        private Node nextNode;
        private T nodeItem;

        public Node(T item){
            this.nodeItem = item;
            this.nextNode = null;
        }

        public void setNextNode(Node n){
            this.nextNode = n;
        }

        public Node getNextNode(){
            return this.nextNode;
        }

        public T getNodeItem(){
            return this.nodeItem;
        }

    }
}
