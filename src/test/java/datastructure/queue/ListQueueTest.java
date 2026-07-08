package datastructure.queue;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ListQueueTest {

    @Test
    void newQueueIsEmpty() {
        var queue = new ListQueue<String>();
        assertEquals(0, queue.size());
        assertTrue(queue.isEmpty());
        assertNull(queue.peek());
        assertNull(queue.deQueue());
        assertFalse(queue.contains("x"));
    }

    @Test
    void enqueueDequeueFifoOrder() {
        // Regression: the original implementation threw
        // IndexOutOfBoundsException on the very first enQueue call.
        var queue = new ListQueue<String>();
        queue.enQueue("a");
        queue.enQueue("b");
        assertEquals(2, queue.size());
        assertFalse(queue.isEmpty());
        assertEquals("a", queue.peek());
        assertEquals("a", queue.deQueue());
        assertEquals("b", queue.deQueue());
        assertTrue(queue.isEmpty());
    }

    @Test
    void growsUnboundedAcrossManyEnqueueDequeueCycles() {
        var queue = new ListQueue<Integer>();
        for (int i = 0; i < 50; i++) {
            queue.enQueue(i);
            assertEquals(i, queue.deQueue());
        }
        assertTrue(queue.isEmpty());
    }

    @Test
    void containsAndGetSeeMostRecentlyEnqueuedElement() {
        var queue = new ListQueue<String>();
        queue.enQueue("a");
        queue.enQueue("b");
        assertTrue(queue.contains("a"));
        assertTrue(queue.contains("b"));
        assertFalse(queue.contains("z"));
        assertEquals("a", queue.get(0));
        assertEquals("b", queue.get(1));
        assertNull(queue.get(-1));
        assertNull(queue.get(2));
    }
}
