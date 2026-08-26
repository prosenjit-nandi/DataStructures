# Authoring Contract

_Stage 5 of 5 · The template every new structure follows. Section 6 is a complete, correct specimen — pattern-match on it rather than assembling from the rules above it._

## 1. What one structure consists of

Four files, no exceptions:

| File | Path |
|---|---|
| Implementation | `src/main/java/datastructure/{category}/{Class}.java` |
| Test | `src/test/java/datastructure/{category}/{Class}Test.java` |
| Runnable example | `src/test/java/examples/{category}/{Class}Example.java` |
| Catalog entry | `docs/pipeline/05-handoff/catalog.json` — already exists; flip `status` to `done` |

The example lives in the **test** source set so the build compiles and runs it. A code sample the CI doesn't execute will eventually stop compiling, and a snippet that doesn't compile is the single fastest way to lose a beginner (journey moment M3).

## 2. Required Javadoc tags

On the implementation class, in this order. Validation fails the build on a missing required tag.

| Tag | Required | Repeats | Notes |
|---|---|---|---|
| `@description` | ✅ | — | One or two sentences: what it *is*, in words a first-semester student knows |
| `@category` | ✅ | — | Must equal the catalog category |
| `@difficulty` | ✅ | — | `beginner` / `intermediate` / `advanced` / `expert` |
| `@summary` | ✅ | — | How *this* implementation works under the hood |
| `@useWhen` | ✅ ×2 min | ✓ | One situation per tag |
| `@avoidWhen` | ✅ ×2 min | ✓ | One per tag. **Name the alternative** and it renders as a link |
| `@complexity` | ✅ if implemented | ✓ | `operation \| average \| worst \| space` |
| `@invariant` | recommended | ✓ | What stays true across every operation. Highest teaching value per line |
| `@prerequisite` | — | ✓ | Catalog slug. Must resolve and must precede this structure |
| `@helper` | ✅ if used | ✓ | Package-private types this class needs — they render on the page |
| `@jdkAnalog` | — | — | Fully-qualified JDK class |
| `@preferJdkWhen` | ✅ if `@jdkAnalog` set | — | Say plainly when the JDK class is the better choice |
| `@relatedTo` | — | ✓ | Catalog slug |
| `@keywords` | — | — | Comma-separated; feeds search |
| `@walkthrough` | ✅ for tier `full` | ✓ | `startLine-endLine: explanation`. Anchors, never copied code |
| `@visualization` | ✅ for tier `full` | — | Op-log name (`ring-buffer`, `chain`, `tree`) or `custom:<Component>` |
| `@example` | ✅ for tier `full` | — | Path to the runnable example |

**Write `@avoidWhen` as a decision, not a dead end.** "Avoid when the size is unbounded" leaves the reader on Google. "Avoid when the size is unbounded — use a Linked Queue instead" keeps them here and teaches the trade-off.

**Write `@walkthrough` about *why*, never *what*.** "Line 37 computes the index" restates the code. "Line 37 is what makes it circular: when the sum runs past the last index, `%` folds it back to the start" is the reason someone reads a walkthrough.

## 3. Implementation rules

- Package matches category. Class name matches `targetClass` in the catalog.
- Implement the shared interface if one exists for the contract.
- Errors: `NoSuchElementException` empty · `IndexOutOfBoundsException` bad index · `IllegalStateException` full. **Never `null` to signal an error.**
- No unexplained magic numbers. A default capacity needs a constant with a name and a comment.
- Modern Java where it clarifies; plain Java where it doesn't.
- Keep it teachable. Every defensive branch you add must be reachable from a test to satisfy the coverage gate — which is a good reason to write fewer of them.

## 4. Test rules

One test class, covering: happy path · empty · single element · capacity and resize boundaries · every documented exception · **delete-then-reinsert** (the case that catches wrap-around and probe-chain bugs) · generic behavior with a non-trivial type.

100% line **and** branch coverage on `datastructure.*`, enforced by `jacocoTestCoverageVerification`. Run `./gradlew check` — not `test` — before you finish.

## 5. Definition of done

- [ ] Implementation in the right package, implementing the right interface
- [ ] All required Javadoc tags present, `@complexity` matching what the code actually does
- [ ] `@avoidWhen` entries each name an alternative
- [ ] Every helper type listed in `@helper`
- [ ] Test class covers all seven cases in §4
- [ ] Runnable example compiles, runs, and is asserted by a test
- [ ] `./gradlew check` passes — coverage gate included
- [ ] `cd tutorial-app && npm run build` passes content validation
- [ ] `catalog.json` entry set to `"status": "done"`
- [ ] Zero files changed under `tutorial-app/src/`

---

## 6. Worked example — DS-015 Circular Array Queue

Complete and correct. Copy its shape.

### `src/main/java/datastructure/linear/ArrayQueue.java`

```java
package datastructure.linear;

import datastructure.interfaces.Queue;

import java.util.NoSuchElementException;

/**
 * @description A first-in-first-out queue that stores elements in a fixed-size array and wraps the
 *              read and write positions around the end of the array, so no element is ever shifted.
 * @category linear
 * @difficulty beginner
 * @summary Keeps a `front` index and a `size` count. The next write position is computed as
 *          `(front + size) % capacity`, so the window of live elements rotates through the array
 *          instead of moving. Nothing allocates after the constructor.
 * @useWhen You know the maximum number of queued items in advance.
 * @useWhen You need strict O(1) enqueue and dequeue with no allocation after construction.
 * @useWhen A resize pause would be unacceptable in your latency budget.
 * @avoidWhen The maximum size is unknown or unbounded — use a Linked Queue instead. -> linked-queue
 * @avoidWhen You need to insert or remove from the middle — use a Doubly Linked List. -> doubly-linked-list
 * @avoidWhen You are tempted to size the array defensively large; that memory is reserved up front.
 * @complexity enQueue  | O(1) | O(1) | O(1)
 * @complexity deQueue  | O(1) | O(1) | O(1)
 * @complexity peek     | O(1) | O(1) | O(1)
 * @complexity contains | O(n) | O(n) | O(1)
 * @invariant size is always between 0 and capacity inclusive.
 * @invariant Live elements occupy exactly the indices (front + i) % capacity for i in [0, size).
 * @invariant Every slot outside that window holds null, so the queue never pins a dead object.
 * @prerequisite static-array
 * @jdkAnalog java.util.ArrayDeque
 * @preferJdkWhen You want a queue in production. ArrayDeque grows automatically and outperforms
 *                this for almost every workload; this exists so you understand what it does.
 * @relatedTo linked-queue
 * @relatedTo circular-buffer
 * @keywords fifo, ring, wrap-around, modular arithmetic, bounded
 * @walkthrough 30-36: The constructor allocates once and never again. Everything after this point
 *              runs in constant space — that property is the whole reason to choose this.
 * @walkthrough 45: The line that makes it circular. When the sum runs past the last index, `%`
 *              folds it back to the beginning. If you remember one line from this class, this one.
 * @walkthrough 55-57: deQueue nulls the slot it vacates. Skipping that would still be correct, but
 *              the array would keep a reference to a dead object and stop the GC reclaiming it.
 * @visualization ring-buffer
 * @example src/test/java/examples/linear/ArrayQueueExample.java
 */
public class ArrayQueue<T> implements Queue<T> {

    /** Small enough to be obviously a default, large enough to be useful in the examples. */
    private static final int DEFAULT_CAPACITY = 16;

    private final T[] data;
    private final int capacity;
    private int front;
    private int size;

    public ArrayQueue() {
        this(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public ArrayQueue(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("capacity must be at least 1, was " + capacity);
        }
        this.capacity = capacity;
        this.data = (T[]) new Object[capacity];
        this.front = 0;
        this.size = 0;
    }

    @Override
    public void enQueue(T item) {
        if (size == capacity) {
            throw new IllegalStateException("Queue is full (capacity " + capacity + ")");
        }
        int end = (front + size) % capacity;
        data[end] = item;
        size++;
    }

    @Override
    public T deQueue() {
        if (size == 0) {
            throw new NoSuchElementException("Queue is empty");
        }
        var item = data[front];
        data[front] = null;
        front = (front + 1) % capacity;
        size--;
        return item;
    }

    @Override
    public T peek() {
        if (size == 0) {
            throw new NoSuchElementException("Queue is empty");
        }
        return data[front];
    }

    @Override
    public boolean contains(T item) {
        for (int i = 0; i < size; i++) {
            if (data[(front + i) % capacity].equals(item)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public T get(int position) {
        if (position < 0 || position >= size) {
            throw new IndexOutOfBoundsException("position: " + position + ", size: " + size);
        }
        return data[(front + position) % capacity];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }
}
```

### `src/test/java/examples/linear/ArrayQueueExample.java`

Self-contained: package, imports, `main`, and output a reader can predict. This is what the copy button hands them.

```java
package examples.linear;

import datastructure.linear.ArrayQueue;

/** Runnable example for DS-015. Compiled and executed by ArrayQueueExampleTest. */
public final class ArrayQueueExample {

    public static void main(String[] args) {
        var queue = new ArrayQueue<String>(3);

        queue.enQueue("first");
        queue.enQueue("second");
        queue.enQueue("third");
        System.out.println("front of a full queue: " + queue.peek());   // first

        System.out.println("served: " + queue.deQueue());               // first
        System.out.println("served: " + queue.deQueue());               // second

        // Slot 0 and 1 are free again. The next write wraps around to index 0 —
        // this is the behaviour a growable queue would not need and a fixed array would not have.
        queue.enQueue("fourth");
        queue.enQueue("fifth");

        while (!queue.isEmpty()) {
            System.out.println("served: " + queue.deQueue());           // third, fourth, fifth
        }
    }
}
```

### `src/test/java/datastructure/linear/ArrayQueueTest.java`

```java
package datastructure.linear;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class ArrayQueueTest {

    @Test
    void servesInFirstInFirstOutOrder() {
        var q = new ArrayQueue<Integer>(4);
        q.enQueue(1); q.enQueue(2); q.enQueue(3);
        assertEquals(1, q.deQueue());
        assertEquals(2, q.deQueue());
        assertEquals(3, q.deQueue());
    }

    @Test
    void wrapsAroundTheEndOfTheArray() {          // the case that matters most
        var q = new ArrayQueue<String>(3);
        q.enQueue("a"); q.enQueue("b"); q.enQueue("c");
        q.deQueue(); q.deQueue();                  // frees slots 0 and 1
        q.enQueue("d"); q.enQueue("e");            // must wrap
        assertEquals("c", q.deQueue());
        assertEquals("d", q.deQueue());
        assertEquals("e", q.deQueue());
        assertTrue(q.isEmpty());
    }

    @Test
    void reportsSizeAndEmptiness() {
        var q = new ArrayQueue<Integer>(2);
        assertTrue(q.isEmpty());
        assertEquals(0, q.size());
        q.enQueue(7);
        assertFalse(q.isEmpty());
        assertEquals(1, q.size());
    }

    @Test
    void findsAndIndexesLiveElementsAcrossTheWrap() {
        var q = new ArrayQueue<String>(3);
        q.enQueue("a"); q.enQueue("b"); q.enQueue("c");
        q.deQueue();
        q.enQueue("d");
        assertTrue(q.contains("d"));
        assertFalse(q.contains("a"));
        assertEquals("b", q.get(0));
        assertEquals("d", q.get(2));
    }

    @Test
    void throwsWhenFull() {
        var q = new ArrayQueue<Integer>(1);
        q.enQueue(1);
        assertThrows(IllegalStateException.class, () -> q.enQueue(2));
    }

    @Test
    void throwsWhenEmpty() {
        var q = new ArrayQueue<Integer>(2);
        assertThrows(NoSuchElementException.class, q::deQueue);
        assertThrows(NoSuchElementException.class, q::peek);
    }

    @Test
    void throwsOnAnIndexOutsideTheLiveWindow() {
        var q = new ArrayQueue<Integer>(3);
        q.enQueue(1);
        assertThrows(IndexOutOfBoundsException.class, () -> q.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> q.get(1));
    }

    @Test
    void rejectsANonPositiveCapacity() {
        assertThrows(IllegalArgumentException.class, () -> new ArrayQueue<Integer>(0));
    }

    @Test
    void defaultConstructorIsUsable() {           // covers the no-arg branch
        var q = new ArrayQueue<Integer>();
        q.enQueue(1);
        assertEquals(1, q.deQueue());
    }

    @Test
    void theRunnableExampleRuns() {               // keeps the published snippet honest
        assertDoesNotThrow(() -> examples.linear.ArrayQueueExample.main(new String[0]));
    }
}
```

Ten tests, and every one of them exists to cover a branch — that's what 100% branch coverage looks like in practice. Note the last one: it is what stops the published code sample from silently rotting.
