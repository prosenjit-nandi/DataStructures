package datastructure;

public class ArrayQueue<T> implements Queue<T> {

    private T[] data;
    private int front;
    private int end;

    public ArrayQueue(){
        this(10000);
    }

    public ArrayQueue(int size){
        data = (T[]) new Object[size];
        front = -1;
        end = -1;
    }

    @Override
    public void enQueue(T item) {
        if(this.end + 1 % data.length == this.front){
            return;
        }
        if(this.size() == 0){
            this.front++;
        }
        data[++this.end] = item;
    }

    @Override
    public T deQueue(){
        if(this.size() == 0){
            return null;
        }
        T item = data[this.front];
        data[this.front] = null;

        if(front == end){
            front = -1;
            end = -1;
        } else {
            this.front++;
        }
        return item;
    }

    @Override
    public boolean contains(T item) {
        if(this.size() == 0){
            return false;
        }
        for (int i = front; i < end; i++) {
            if(data[i].equals(item)) return true;
        }
        return false;
    }

    @Override
    public T get(int position) {
        if(this.size() == 0 || position > size()){
            return null;
        }
        int truePosition = 0;
        for (int i = front; i < end; i++) {
            if(truePosition == position) return data[i];
            truePosition++;
        }
        return null;
    }

    @Override
    public int size() {
        if(front == -1 && end == -1) return 0;
        else return end - front + 1;
    }


}
