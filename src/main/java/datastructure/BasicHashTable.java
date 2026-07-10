package datastructure;

/**
 * @description A basic implementation of a Hash Table, providing key-value mapping functionality.
 * @usage Best used when fast O(1) lookups, insertions, and deletions are required, and the exact order of elements is not important.
 * @summary Uses an array of HashEntry objects and implements open-addressing with linear probing to handle collisions. It automatically resizes when a load factor threshold is reached.
 */
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
        var hash = calculateHash(key);
        return data[hash] == null ? null : data[hash].value();
    }

    public void put(K key, V value) {
        if ((double) (size + 1) / capacity > LOAD_FACTOR_THRESHOLD) {
            grow();
        }
        var hash = calculateHash(key);
        if (data[hash] == null) {
            size++;
        }
        data[hash] = new HashEntry<>(key, value);
    }

    public V delete(K key) {
        var value = get(key);
        if (value != null) {
            var hash = calculateHash(key);
            data[hash] = null;
            size--;

            // Standard open-addressing shift-delete: pull subsequent entries
            // in the probe chain back so future lookups still find them.
            hash = (hash + 1) % capacity;
            while (data[hash] != null) {
                var entry = data[hash];
                data[hash] = null;
                size--;
                put(entry.key(), entry.value());
                hash = (hash + 1) % capacity;
            }
        }
        return value;
    }

    public boolean hasKey(K key) {
        // calculateHash's probe only ever stops on an empty slot or the
        // matching key, so a non-null slot here is guaranteed to be a match.
        var hash = calculateHash(key);
        return data[hash] != null;
    }

    public boolean hasValue(V value) {
        for (var entry : data) {
            if (entry != null && entry.value().equals(value)) {
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
        var hash = Math.floorMod(key.hashCode(), capacity);
        while (data[hash] != null && !data[hash].key().equals(key)) {
            hash = (hash + 1) % capacity;
        }
        return hash;
    }

    private void grow() {
        var oldData = data;
        capacity *= 2;
        data = newTable(capacity);
        size = 0;
        for (var entry : oldData) {
            if (entry != null) {
                put(entry.key(), entry.value());
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static <K, V> HashEntry<K, V>[] newTable(int capacity) {
        return (HashEntry<K, V>[]) new HashEntry[capacity];
    }

    private record HashEntry<K, V>(K key, V value) {
    }
}
