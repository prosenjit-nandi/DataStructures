package datastructure;

import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BasicHashTableTest {

    /** A key with a caller-controlled hashCode, used to force collisions and negative hashes deterministically. */
    private static final class FixedHashKey {
        private final String id;
        private final int fixedHash;

        FixedHashKey(String id, int fixedHash) {
            this.id = id;
            this.fixedHash = fixedHash;
        }

        @Override
        public int hashCode() {
            return fixedHash;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof FixedHashKey other && Objects.equals(id, other.id);
        }
    }

    @Test
    void putAndGetBasic() {
        var table = new BasicHashTable<String, Integer>(16);
        table.put("a", 1);
        assertEquals(1, table.get("a"));
        assertNull(table.get("missing"));
        assertEquals(1, table.size());
        assertFalse(table.isEmpty());
    }

    @Test
    void emptyTableIsEmpty() {
        var table = new BasicHashTable<String, Integer>(16);
        assertTrue(table.isEmpty());
        assertEquals(0, table.size());
    }

    @Test
    void puttingSameKeyTwiceOverwritesValueWithoutInflatingSize() {
        var table = new BasicHashTable<String, Integer>(16);
        table.put("a", 1);
        table.put("a", 2);
        assertEquals(1, table.size());
        assertEquals(2, table.get("a"));
    }

    @Test
    void negativeHashCodeKeyDoesNotThrow() {
        var table = new BasicHashTable<FixedHashKey, String>(8);
        var key = new FixedHashKey("neg", -5);
        table.put(key, "value");
        assertEquals("value", table.get(key));
        assertTrue(table.hasKey(key));
    }

    @Test
    void collisionChainProbesLinearlyAndDeleteShiftsSubsequentEntries() {
        var table = new BasicHashTable<FixedHashKey, String>(8);
        var k1 = new FixedHashKey("k1", 2);
        var k2 = new FixedHashKey("k2", 2);
        var k3 = new FixedHashKey("k3", 2);
        table.put(k1, "v1");
        table.put(k2, "v2");
        table.put(k3, "v3");

        assertEquals("v1", table.get(k1));
        assertEquals("v2", table.get(k2));
        assertEquals("v3", table.get(k3));

        assertEquals("v1", table.delete(k1));
        assertNull(table.get(k1));
        assertFalse(table.hasKey(k1));
        assertEquals("v2", table.get(k2));
        assertEquals("v3", table.get(k3));
        assertTrue(table.hasKey(k2));
        assertTrue(table.hasKey(k3));
        assertEquals(2, table.size());
    }

    @Test
    void deleteNonExistentKeyReturnsNull() {
        var table = new BasicHashTable<String, Integer>(16);
        table.put("a", 1);
        assertNull(table.delete("missing"));
        assertEquals(1, table.size());
    }

    @Test
    void hasKeyFalseOnEmptySlot() {
        var table = new BasicHashTable<String, Integer>(16);
        assertFalse(table.hasKey("missing"));
    }

    @Test
    void hasValueFindsAndMissesCorrectly() {
        var table = new BasicHashTable<String, Integer>(16);
        table.put("a", 1);
        table.put("b", 2);
        assertTrue(table.hasValue(2));
        assertFalse(table.hasValue(999));
    }

    @Test
    void hasValueOnEmptyTableIsFalse() {
        var table = new BasicHashTable<String, Integer>(16);
        assertFalse(table.hasValue(1));
    }

    @Test
    void growingPastLoadFactorKeepsAllEntriesRetrievable() {
        var table = new BasicHashTable<Integer, Integer>(4);
        for (int i = 0; i < 50; i++) {
            table.put(i, i * i);
        }
        assertEquals(50, table.size());
        for (int i = 0; i < 50; i++) {
            assertEquals(i * i, table.get(i));
            assertTrue(table.hasKey(i));
        }
    }
}
