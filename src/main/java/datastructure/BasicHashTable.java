package datastructure;

public class BasicHashTable<K, V> {
    private static final double LOAD_FACTOR_THRESHOLD = 0.7;

    private HashEntry<K, V>[] data;
    private int capacity;
    private int size;

    public BasicHashTable(int tableSize) {
        this.capacity = tableSize;
        this.data = newTable(tableSize);
        this.size = 0;
    }

    public V get(K key) {
        int hash = calculateHash(key);
        return data[hash] == null ? null : data[hash].getValue();
    }

    public void put(K key, V value) {
        if ((double) (size + 1) / capacity > LOAD_FACTOR_THRESHOLD) {
            grow();
        }
        int hash = calculateHash(key);
        if (data[hash] == null) {
            size++;
        }
        data[hash] = new HashEntry<>(key, value);
    }

    public V delete(K key) {
        V value = get(key);
        if (value != null) {
            int hash = calculateHash(key);
            data[hash] = null;
            size--;

            // Standard open-addressing shift-delete: pull subsequent entries
            // in the probe chain back so future lookups still find them.
            hash = (hash + 1) % capacity;
            while (data[hash] != null) {
                HashEntry<K, V> entry = data[hash];
                data[hash] = null;
                size--;
                put(entry.getKey(), entry.getValue());
                hash = (hash + 1) % capacity;
            }
        }
        return value;
    }

    public boolean hasKey(K key) {
        // calculateHash's probe only ever stops on an empty slot or the
        // matching key, so a non-null slot here is guaranteed to be a match.
        int hash = calculateHash(key);
        return data[hash] != null;
    }

    public boolean hasValue(V value) {
        for (HashEntry<K, V> entry : data) {
            if (entry != null && entry.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private int calculateHash(K key) {
        int hash = Math.floorMod(key.hashCode(), capacity);
        while (data[hash] != null && !data[hash].getKey().equals(key)) {
            hash = (hash + 1) % capacity;
        }
        return hash;
    }

    private void grow() {
        HashEntry<K, V>[] oldData = data;
        capacity *= 2;
        data = newTable(capacity);
        size = 0;
        for (HashEntry<K, V> entry : oldData) {
            if (entry != null) {
                put(entry.getKey(), entry.getValue());
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static <K, V> HashEntry<K, V>[] newTable(int capacity) {
        return (HashEntry<K, V>[]) new HashEntry[capacity];
    }

    private static final class HashEntry<K, V> {
        private final K key;
        private final V value;

        HashEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        K getKey() {
            return key;
        }

        V getValue() {
            return value;
        }
    }
}
