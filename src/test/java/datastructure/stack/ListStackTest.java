package datastructure.stack;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ListStackTest {

    @Test
    void newStackIsEmpty() {
        var stack = new ListStack<String>();
        assertEquals(0, stack.size());
        assertTrue(stack.isEmpty());
        assertNull(stack.peek());
        assertNull(stack.pop());
    }

    @Test
    void pushPopLifoOrder() {
        var stack = new ListStack<String>();
        stack.push("a");
        stack.push("b");
        assertEquals(2, stack.size());
        assertFalse(stack.isEmpty());
        assertEquals("b", stack.peek());
        assertEquals("b", stack.pop());
        assertEquals("a", stack.pop());
        assertTrue(stack.isEmpty());
    }

    @Test
    void pushAfterPopReusesSpaceCorrectly() {
        // Regression: the original push() used data.add(pointer++, item),
        // which after a pop() would insert into the middle of the backing
        // list rather than appending, leaving stale trailing elements.
        var stack = new ListStack<String>();
        stack.push("a");
        stack.push("b");
        stack.pop();
        stack.push("c");
        assertEquals(2, stack.size());
        assertEquals("c", stack.pop());
        assertEquals("a", stack.pop());
        assertTrue(stack.isEmpty());
    }

    @Test
    void containsOnPartiallyFilledStackDoesNotThrow() {
        var stack = new ListStack<String>();
        stack.push("a");
        stack.push("b");
        assertTrue(stack.contains("a"));
        assertTrue(stack.contains("b"));
        assertFalse(stack.contains("z"));
    }

    @Test
    void getFindsItemWithoutMutatingStack() {
        var stack = new ListStack<String>();
        stack.push("a");
        stack.push("b");
        stack.push("c");
        assertEquals("a", stack.get("a"));
        assertEquals(3, stack.size());
        assertEquals("c", stack.peek());
        assertNull(stack.get("missing"));
        assertEquals(3, stack.size());
    }
}
