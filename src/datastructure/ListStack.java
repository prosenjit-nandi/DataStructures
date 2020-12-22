package datastructure;

import java.util.ArrayList;
import java.util.List;

public class ListStack<T> implements Stack<T>{
    private List<T> data;
    private int pointer;

    public ListStack(){
        data = new ArrayList<T>();
        pointer = 0;
    }

    public void push(T item){
        data.add(pointer++, item);
    }

    public T pop() {
        if(pointer == 0){
            return null;
        }
        return data.get(--pointer);
    }

    public boolean contains(T item){
        for (T dataItem: data) if (dataItem.equals(item)) return true;
        return false;
    }

    public T get(T item){
        while(pointer > 0){
            T dataItem = pop();
            if(dataItem.equals(item)){
                return dataItem;
            }
        }
        return null;
    }

    public int size(){
        return pointer;
    }

}
