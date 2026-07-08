package datastructure.queue;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ArrayQueueTest {

    @Test
    void newQueueIsEmpty() {
        var queue = new ArrayQueue<String>(4);
        assertEquals(0, queue.size());
        assertTrue(queue.isEmpty());
        assertNull(queue.peek());
        assertNull(queue.deQueue());
        assertFalse(queue.contains("x"));
    }

    @Test
    void enqueueDequeueFifoOrder() {
        var queue = new ArrayQueue<String>(4);
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
    void wrapsAroundCapacityUnderRepeatedCycles() {
        // Regression: the original ArrayQueue never wrapped its internal
        // index, so this exact enqueue/dequeue/enqueue pattern used to throw
        // ArrayIndexOutOfBoundsException once the running index exceeded capacity.
        var queue = new ArrayQueue<Integer>(3);
        for (int i = 0; i < 10; i++) {
            queue.enQueue(i);
            assertEquals(i, queue.deQueue());
        }
        assertTrue(queue.isEmpty());

        queue.enQueue(1);
        queue.enQueue(2);
        assertEquals(1, queue.deQueue());
        queue.enQueue(3);
        queue.enQueue(4);
        assertEquals(2, queue.deQueue());
        assertEquals(3, queue.deQueue());
        assertEquals(4, queue.deQueue());
        assertTrue(queue.isEmpty());
    }

    @Test
    void enqueueOnFullQueueThrows() {
        var queue = new ArrayQueue<String>(2);
        queue.enQueue("a");
        queue.enQueue("b");
        assertThrows(IllegalStateException.class, () -> queue.enQueue("c"));
    }

    @Test
    void containsAndGetSeeMostRecentlyEnqueuedElement() {
        // Regression: the original off-by-one loop (`i < end`) never
        // inspected the last enqueued slot.
        var queue = new ArrayQueue<String>(4);
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

    @Test
    void defaultConstructorUsesLargeDefaultCapacity() {
        var queue = new ArrayQueue<Integer>();
        queue.enQueue(1);
        assertEquals(1, queue.size());
    }
}
