package datastructure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BasicBinaryTreeTest {

    private BasicBinaryTree<Integer> tree;

    @BeforeEach
    void setUp() {
        tree = new BasicBinaryTree<>();
    }

    @Test
    void newTreeIsEmpty() {
        assertEquals(0, tree.size());
        assertTrue(tree.isEmpty());
        assertFalse(tree.contains(1));
        assertTrue(tree.inorderTraversal().isEmpty());
    }

    @Test
    void addAndContainsFollowBstOrdering() {
        for (int v : new int[] {50, 30, 70, 20, 40, 60, 80}) {
            tree.add(v);
        }
        assertEquals(7, tree.size());
        assertFalse(tree.isEmpty());
        for (int v : new int[] {50, 30, 70, 20, 40, 60, 80}) {
            assertTrue(tree.contains(v));
        }
        assertFalse(tree.contains(999));
        assertFalse(tree.contains(35));
        assertEquals(List.of(20, 30, 40, 50, 60, 70, 80), tree.inorderTraversal());
    }

    @Test
    void deleteNonExistentItemReturnsFalse() {
        tree.add(10);
        assertFalse(tree.delete(999));
        assertEquals(1, tree.size());
    }

    @Test
    void deleteOnlyNodeEmptiesTree() {
        tree.add(10);
        assertTrue(tree.delete(10));
        assertEquals(0, tree.size());
        assertTrue(tree.isEmpty());
        assertTrue(tree.inorderTraversal().isEmpty());
    }

    @Test
    void deleteLeafNode() {
        for (int v : new int[] {50, 30, 70}) {
            tree.add(v);
        }
        assertTrue(tree.delete(30));
        assertEquals(2, tree.size());
        assertFalse(tree.contains(30));
        assertEquals(List.of(50, 70), tree.inorderTraversal());
    }

    @Test
    void deleteNodeWithOnlyRightChild() {
        // 50 -> right child 70 -> right child 80 (70 has no left child)
        for (int v : new int[] {50, 70, 80}) {
            tree.add(v);
        }
        assertTrue(tree.delete(70));
        assertEquals(2, tree.size());
        assertEquals(List.of(50, 80), tree.inorderTraversal());
    }

    @Test
    void deleteNodeWithOnlyLeftChild() {
        // 50 -> left child 30 -> left child 20 (30 has no right child)
        for (int v : new int[] {50, 30, 20}) {
            tree.add(v);
        }
        assertTrue(tree.delete(30));
        assertEquals(2, tree.size());
        assertEquals(List.of(20, 50), tree.inorderTraversal());
    }

    @Test
    void deleteTwoChildrenNodeWhereSuccessorIsImmediateRightChild() {
        // Regression for the original self-loop bug: the node being deleted
        // has two children, and its in-order successor (min of right
        // subtree) is the right child itself, with no left-chain to walk.
        tree.add(10);
        tree.add(5);
        tree.add(15);
        assertTrue(tree.delete(10));
        assertEquals(2, tree.size());
        assertEquals(List.of(5, 15), tree.inorderTraversal());
        // Tree must remain fully usable afterward (no corrupted/self-referencing node).
        tree.add(20);
        assertEquals(List.of(5, 15, 20), tree.inorderTraversal());
        assertTrue(tree.contains(15));
    }

    @Test
    void deleteTwoChildrenNodeWhereSuccessorIsDeepInLeftChain() {
        // right subtree of the deleted node has its own left chain, forcing
        // the successor-search loop to walk multiple hops.
        tree.add(10);
        tree.add(5);
        tree.add(20);
        tree.add(15);
        tree.add(12);
        assertTrue(tree.delete(10));
        assertEquals(4, tree.size());
        assertEquals(List.of(5, 12, 15, 20), tree.inorderTraversal());
        assertFalse(tree.contains(10));
        assertTrue(tree.contains(12));
    }

    @Test
    void deleteRootRepeatedlyLeavesTreeConsistent() {
        for (int v : new int[] {50, 30, 70, 20, 40, 60, 80}) {
            tree.add(v);
        }
        while (!tree.isEmpty()) {
            int before = tree.size();
            assertTrue(tree.delete(tree.inorderTraversal().get(0)));
            assertEquals(before - 1, tree.size());
        }
        assertTrue(tree.inorderTraversal().isEmpty());
    }
}
