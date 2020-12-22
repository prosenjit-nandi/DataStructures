package datastructure;

public class BasicHashTable<K, V> {
    private HashEntry[] data;
    private int capacity;
    private int size;

    public BasicHashTable(int tableSize){
        this.capacity = tableSize;
        data = new HashEntry[this.capacity];
        this.size = 0;
    }

    public V get(K key){
        int hash = calculateHash(key);
        if(data[hash] == null){
            return null;
        }
        V value = (V) data[hash].getValue();
        return value;
    }

    public void put(K key, V value){
        int hash = calculateHash(key);
        data[hash] = new HashEntry<K, V>(key, value);
        size++;
    }

    public V delete(K key){
        V value = get(key);

        if(value != null) {
            int hash = calculateHash(key);
            data[hash] = null;
            size--;

            hash = (hash + 1) % capacity;
            while (data[hash] != null){
                HashEntry entry = data[hash];
                data[hash] = null;
                put((K)entry.getkey(), (V)entry.getValue());
                size--;
                hash = (hash + 1) % capacity;
            }
        }

        return value;
    }

    public boolean hasKey(K key){
        int hash = calculateHash(key);
        return data[hash] != null && data[hash].getkey().equals(key);
    }

    public boolean hasValue(V value){
        for (HashEntry<K, V> dataItem: data){
            if(dataItem != null && dataItem.getValue().equals(value)){
                return true;
            }
        }
        return false;
    }

    private int calculateHash(K key){
        int hash = key.hashCode() % this.capacity;
        while(data[hash] != null && !data[hash].getkey().equals(key)){
            hash = (hash + 1) % this.capacity;
        }
        return hash;
    }

    public int size(){
        return this.size;
    }

    private class HashEntry<K, V> {
        private K key;
        private V value;

        public HashEntry(K key, V value){
            this.key = key;
            this.value = value;
        }

        public K getkey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }
    }
}
