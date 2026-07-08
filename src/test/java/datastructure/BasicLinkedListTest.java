package datastructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BasicLinkedListTest {

    private BasicLinkedList<String> list;

    @BeforeEach
    void setUp() {
        list = new BasicLinkedList<>();
    }

    @Test
    void newListIsEmpty() {
        assertEquals(0, list.size());
        assertTrue(list.isEmpty());
    }

    @Test
    void removeOnEmptyListReturnsNull() {
        assertNull(list.remove());
    }

    @Test
    void addAppendsInOrder() {
        list.add("a");
        list.add("b");
        list.add("c");
        assertEquals(3, list.size());
        assertFalse(list.isEmpty());
        assertEquals("a", list.get(0));
        assertEquals("b", list.get(1));
        assertEquals("c", list.get(2));
    }

    @Test
    void removeReturnsElementsInFifoOrderAndResetsLastWhenEmptied() {
        list.add("a");
        list.add("b");
        assertEquals("a", list.remove());
        assertEquals(1, list.size());
        assertEquals("b", list.remove());
        assertEquals(0, list.size());
        assertTrue(list.isEmpty());
        // list is now empty; a fresh add must work correctly (regression for
        // stale `last` pointer after emptying via remove()).
        list.add("c");
        assertEquals("c", list.get(0));
        assertEquals(1, list.size());
    }

    @Test
    void insertRejectsOutOfRangePositions() {
        list.add("a");
        assertThrows(IndexOutOfBoundsException.class, () -> list.insert(-1, "x"));
        assertThrows(IndexOutOfBoundsException.class, () -> list.insert(5, "x"));
    }

    @Test
    void insertAtEndOfEmptyListWorks() {
        list.insert(0, "a");
        assertEquals(1, list.size());
        assertEquals("a", list.get(0));
    }

    @Test
    void insertAtLogicalEndAppends() {
        list.add("a");
        list.add("b");
        list.insert(2, "c");
        assertEquals(3, list.size());
        assertEquals("a", list.get(0));
        assertEquals("b", list.get(1));
        assertEquals("c", list.get(2));
    }

    @Test
    void insertAtFrontPrependsWithoutDuplication() {
        list.add("a");
        list.add("b");
        list.insert(0, "z");
        assertEquals(3, list.size());
        assertEquals("z", list.get(0));
        assertEquals("a", list.get(1));
        assertEquals("b", list.get(2));
    }

    @Test
    void insertInMiddleShiftsSubsequentElements() {
        list.add("a");
        list.add("b");
        list.add("c");
        list.insert(2, "x");
        assertEquals(4, list.size());
        assertEquals("a", list.get(0));
        assertEquals("b", list.get(1));
        assertEquals("x", list.get(2));
        assertEquals("c", list.get(3));
    }

    @Test
    void getRejectsOutOfRangePositions() {
        list.add("a");
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
    }

    @Test
    void removeAtRejectsOutOfRangePositions() {
        list.add("a");
        assertThrows(IndexOutOfBoundsException.class, () -> list.removeAt(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.removeAt(1));
    }

    @Test
    void removeAtFrontDelegatesToRemove() {
        list.add("a");
        list.add("b");
        assertEquals("a", list.removeAt(0));
        assertEquals(1, list.size());
        assertEquals("b", list.get(0));
    }

    @Test
    void removeAtMiddleReconnectsNeighbors() {
        list.add("a");
        list.add("b");
        list.add("c");
        assertEquals("b", list.removeAt(1));
        assertEquals(2, list.size());
        assertEquals("a", list.get(0));
        assertEquals("c", list.get(1));
    }

    @Test
    void removeAtLastElementUpdatesLastPointer() {
        list.add("a");
        list.add("b");
        list.add("c");
        assertEquals("c", list.removeAt(2));
        assertEquals(2, list.size());
        // appending after removing the tail must work correctly, proving
        // `last` was updated rather than left dangling.
        list.add("d");
        assertEquals(3, list.size());
        assertEquals("d", list.get(2));
    }

    @Test
    void findOnEmptyListReturnsMinusOne() {
        assertEquals(-1, list.find("a"));
    }

    @Test
    void findReturnsIndexOrMinusOneWhenAbsent() {
        list.add("a");
        list.add("b");
        list.add("c");
        assertEquals(1, list.find("b"));
        assertEquals(-1, list.find("z"));
    }
}
