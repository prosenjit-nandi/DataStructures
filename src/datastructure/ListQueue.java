package datastructure;

import java.util.ArrayList;
import java.util.List;

public class ListQueue<T> implements Queue<T> {

    private List<T> data;
    private int front;
    private int end;

    public ListQueue(){
        data = new ArrayList<T>();
        front = -1;
        end = -1;
    }

    @Override
    public void enQueue(T item) {
        if(this.end + 1 % data.size() == this.front){
            return;
        }
        if(this.size() == 0){
            this.front++;
        }
        data.set(++this.end, item);
    }

    @Override
    public T deQueue(){
        if(this.size() == 0){
            return null;
        }
        T item = data.get(this.front);
        data.set(this.front, null);

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
            if(data.get(i).equals(item)) return true;
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
            if(truePosition == position) return data.get(i);
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
