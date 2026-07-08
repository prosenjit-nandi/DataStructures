package datastructure.stack;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ArrayStackTest {

    @Test
    void newStackIsEmpty() {
        var stack = new ArrayStack<String>(4);
        assertEquals(0, stack.size());
        assertTrue(stack.isEmpty());
        assertNull(stack.peek());
        assertNull(stack.pop());
    }

    @Test
    void pushPopLifoOrder() {
        var stack = new ArrayStack<String>(4);
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
    void pushOnFullStackThrows() {
        var stack = new ArrayStack<String>(2);
        stack.push("a");
        stack.push("b");
        assertThrows(IllegalStateException.class, () -> stack.push("c"));
    }

    @Test
    void containsOnPartiallyFilledStackDoesNotThrow() {
        // Regression: the original contains() scanned the entire
        // fixed-capacity backing array, including unset null slots, and
        // threw NullPointerException on any non-full stack.
        var stack = new ArrayStack<String>(10000);
        stack.push("a");
        stack.push("b");
        assertTrue(stack.contains("a"));
        assertTrue(stack.contains("b"));
        assertFalse(stack.contains("z"));
    }

    @Test
    void getFindsItemWithoutMutatingStack() {
        // Regression: the original get() repeatedly popped elements
        // searching for a match and never restored them, corrupting the
        // stack as a side effect of a lookup.
        var stack = new ArrayStack<String>(10000);
        stack.push("a");
        stack.push("b");
        stack.push("c");
        assertEquals("a", stack.get("a"));
        assertEquals(3, stack.size());
        assertEquals("c", stack.peek());
        assertNull(stack.get("missing"));
        assertEquals(3, stack.size());
    }

    @Test
    void defaultConstructorUsesLargeDefaultCapacity() {
        var stack = new ArrayStack<Integer>();
        stack.push(1);
        assertEquals(1, stack.size());
    }
}
