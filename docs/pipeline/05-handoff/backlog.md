# Backlog

_Stage 5 of 5 · Dependency-ordered. Acceptance criteria come from the PRD, not from fresh invention._

## How to read this

Every structure task is the same shape — implement one structure end to end per `authoring-contract.md` — so they aren't spelled out individually. **Platform tasks are different every time and are written out in full.**

Size is a rough single-session estimate: **S** ≈ under an hour · **M** ≈ half a day · **L** ≈ a day · **XL** ≈ multiple days, and an XL that stalls should be dropped to the `explained` tier rather than pushed.

**Definition of done for every structure task** (from `authoring-contract.md` §5): implementation, test at 100% line and branch, runnable example asserted by a test, all required tags, catalog entry flipped to `done`, `./gradlew check` and `npm run build` green, zero files touched under `tutorial-app/src/`.

---

## Phase 1 platform tasks

These come first. Phase 1 exists to prove the pipeline works before volume is added — if the extension contract is wrong, this is where it must surface.

### `T1-00` — Repackage and rename existing structures · L
_Satisfies FR-22, FR-21_
Move all seven implementations into `datastructure.{category}`, rename `Basic*` to the structure's real name, move tests to mirror. Rename `Stack.get(T)` → `Stack.find(T)`.
- [ ] Seven classes in their category packages; nothing left loose in `datastructure`
- [ ] `./gradlew check` green, coverage still 100%
- [ ] `catalog.json` `currentClass` fields cleared

### `T1-01` — Unify the error contract · M
_Satisfies FR-23_
Replace every `null`-on-error return with the exception from the contract table. Add the missing exception tests.
- [ ] `NoSuchElementException` on empty access across all seven
- [ ] `IndexOutOfBoundsException` on bad index; `IllegalStateException` on full
- [ ] Every documented exception has a test; branch coverage still 100%
- [ ] Interface Javadoc states the contract

### `T1-02` — Add the six new interfaces · M
_Satisfies FR-21_
`Deque`, `Map`, `Set`, `Tree`, `PriorityQueue`, `Graph`, each with documented exception contracts. Retrofit `LinearProbingHashTable` onto `Map` and `BinarySearchTree` onto `Tree`.

### `T1-03` — Extended Javadoc schema and generator rewrite · L
_Satisfies FR-1, FR-3, FR-4, FR-6, FR-8_ · blocked by `T1-00`
Rewrite `generate-data.js` per `implementation-spec.md` §3.
- [ ] All tags in the contract parsed, repeatables in source order
- [ ] `@complexity` structured and notation-normalized
- [ ] `@walkthrough` ranges verified against the file
- [ ] `@helper` types resolved and inlined
- [ ] Per-structure content files plus `index.json` emitted

### `T1-04` — Content validation gate · M
_Satisfies FR-2, NFR-10_ · blocked by `T1-03`
`validate.js` per spec §3, wired into `npm run build` and `./gradlew check`.
- [ ] Every rule in spec §3 enforced; all violations reported, not just the first
- [ ] A negative test proves a deliberately broken tag fails the build
- [ ] Prerequisite cycle and ordering checks run in CI

### `T1-05` — Routing, shell, and tokens · L
_Satisfies FR-9, FR-19, FR-20, NFR-2_
HashRouter with the seven routes, `base: '/DataStructures/'`, top bar, side rail, mobile drawer, `tokens.css` copied from the design system verbatim, light and dark.
- [ ] `#/structure/{slug}` bookmarkable and reload-safe; unknown slug renders not-found with search
- [ ] Works at 320px; no horizontal page scroll
- [ ] Focus ring on every interactive element; axe-core clean on the shell

### `T1-06` — Structure page template · L
_Satisfies FR-13, FR-14, FR-16, FR-25, FR-27_ · blocked by `T1-03`, `T1-05`
Fixed section order as a descriptor array; use, avoid, complexity, code, walkthrough, invariants, rebuild checklist, related. Sticky section rail. Empty sections omitted.
- [ ] Section order identical on every page and defined in exactly one place
- [ ] Complexity table sortable with the plain-language column
- [ ] Copy yields plain source with no gutter or markup
- [ ] Helper types render in their own tab
- [ ] Generic bounds carry an inline collapsed explainer

### `T1-07` — Catalog, search, and coverage meter · L
_Satisfies FR-10, FR-12, FR-30_
- [ ] Filter by category, difficulty, and status with a live count
- [ ] `/` focuses; arrows and Enter work; under 100 ms at 129 entries
- [ ] No-results names the two nearest structures
- [ ] Coverage meter generated at build time

### `T1-08` — Op-log visualization player · L
_Satisfies FR-15, NFR-2_ · blocked by `T1-06`
`OpLogPlayer` plus `ring-buffer`, `chain`, `tree`, `grid` renderers.
- [ ] Paused by default; play, step, reset all work
- [ ] Every step emits a text caption into the DOM
- [ ] `prefers-reduced-motion` renders the final frame with controls still live

### `T1-09` — Reference specimen: ArrayQueue at full tier · M
_Satisfies release criterion 3_ · blocked by `T1-06`, `T1-08`
Bring DS-015 to the exact state in `authoring-contract.md` §6 and confirm the page matches the `Main` artboard.

### `T1-10` — CI gates and scaffolding command · M
_Satisfies FR-29, FR-31, NFR-4_
All five gates in the workflow, a PR workflow, and `npm run scaffold -- <slug>`.
- [ ] Deploy blocked by any failing gate
- [ ] Scaffold emits implementation, test, and example stubs and never overwrites
- [ ] Clean clone builds with no manual steps

### `T1-11` — Learning path and progress · M
_Satisfies FR-11, FR-18, J-1, J-3, J-4_
Prerequisite-aware path view, collections (`interview-core`, `beginner-path`), read-state in `localStorage`, resume card on home, reading time, rebuild checklist.
- [ ] Prerequisites render as guidance, never as a lock
- [ ] Storage failure degrades silently to untracked
- [ ] Collections cut across categories

### `T1-12` — Release verification · M
_Satisfies release criteria 1–10_ · blocked by everything above
Walk the PRD §11 checklist. Criterion 10 — someone other than the author adds a structure using only the contract, without asking a question — is the one that actually matters.

---

## Structure tasks

### Phase 1 — Foundations & platform

_17 structure tasks · content tier: **full**_

The structures a first-year course covers, on a rebuilt site that proves the content pipeline end to end.

| Task | Structure | Class | Prereqs | Size |
|---|---|---|---|---|
| `T1-01` | DS-001 Static Array | `StaticArray` | — | S |
| `T1-02` | DS-002 Dynamic Array (growable) | `DynamicArray` | static-array | S |
| `T1-03` | DS-005 Doubly Linked List | `DoublyLinkedList` | singly-linked-list | S |
| `T1-04` | DS-014 Linked Stack | `LinkedStack` | singly-linked-list | S |
| `T1-05` | DS-018 Linked Queue | `LinkedQueue` | singly-linked-list | S |
| `T1-06` | DS-019 Double-Ended Queue | `Deque` | array-queue | S |
| `T1-07` | DS-025 Hash Table — Separate Chaining | `HashChaining` | singly-linked-list, dynamic-array | S |
| `T1-08` | DS-033 Hash Set | `HashSet` | hash-chaining | S |
| `T1-09` | DS-039 Binary Tree & Traversals | `BinaryTree` | — | S |
| `T1-10` | DS-059 Binary Heap (min/max) | `BinaryHeap` | dynamic-array | S |
| `T1-11` | DS-068 Trie (prefix tree) | `Trie` | hash-chaining | S |
| `T1-12` | DS-080 Adjacency List Graph | `AdjacencyList` | dynamic-array | S |
| `T1-13` | DS-081 Adjacency Matrix Graph | `AdjacencyMatrix` | static-array | S |
| `T1-14` | DS-082 Edge List Graph | `EdgeList` | dynamic-array | S |
| `T1-15` | DS-085 Union-Find (quick find / quick union) | `UnionFindNaive` | static-array | S |
| `T1-16` | DS-086 Union-Find (rank + path compression) | `UnionFindOptimized` | union-find-naive | M |
| `T1-17` | DS-090 Prefix Sum & Difference Array | `PrefixSum` | static-array | S |


### Phase 2 — Interview core

_25 structure tasks · content tier: **full**_

Everything a candidate is asked about: balanced trees, heaps, tries, range queries, caches.

| Task | Structure | Class | Prereqs | Size |
|---|---|---|---|---|
| `T2-01` | DS-003 Bit Set | `Bitset` | static-array | M |
| `T2-02` | DS-006 Circular Linked List | `CircularLinkedList` | singly-linked-list | M |
| `T2-03` | DS-015 Min/Max Stack (O(1) extremum) | `MinStack` | array-stack | M |
| `T2-04` | DS-020 Circular / Ring Buffer | `CircularBuffer` | array-queue | M |
| `T2-05` | DS-021 Monotonic Stack | `MonotonicStack` | array-stack | M |
| `T2-06` | DS-022 Monotonic Deque (sliding-window max) | `MonotonicDeque` | deque | M |
| `T2-07` | DS-023 Queue From Two Stacks | `QueueFromStacks` | array-stack | S |
| `T2-08` | DS-024 Stack From Two Queues | `StackFromQueues` | array-queue | S |
| `T2-09` | DS-027 Hash Table — Quadratic Probing | `HashQuadraticProbing` | hash-linear-probing | M |
| `T2-10` | DS-028 Hash Table — Double Hashing | `HashDoubleHashing` | hash-linear-probing | M |
| `T2-11` | DS-034 Insertion-Ordered Hash Map | `LinkedHashMap` | hash-chaining, doubly-linked-list | M |
| `T2-12` | DS-035 LRU Cache | `LruCache` | linked-hash-map | M |
| `T2-13` | DS-037 Multimap | `Multimap` | hash-chaining | S |
| `T2-14` | DS-042 AVL Tree | `AvlTree` | bst | M |
| `T2-15` | DS-043 Red-Black Tree | `RedBlackTree` | bst | L |
| `T2-16` | DS-061 Indexed Priority Queue | `IndexedPq` | binary-heap, hash-chaining | M |
| `T2-17` | DS-069 Radix / Patricia Trie | `RadixTrie` | trie | L |
| `T2-18` | DS-071 Binary (XOR) Trie | `BinaryXorTrie` | trie | M |
| `T2-19` | DS-077 KMP Failure Table | `KmpFailureTable` | — | M |
| `T2-20` | DS-091 Fenwick Tree (BIT) | `FenwickTree` | prefix-sum | M |
| `T2-21` | DS-093 Segment Tree | `SegmentTree` | binary-tree, static-array | M |
| `T2-22` | DS-096 Sparse Table | `SparseTable` | static-array | M |
| `T2-23` | DS-106 Bloom Filter | `BloomFilter` | bitset | M |
| `T2-24` | DS-122 Bounded Blocking Queue | `BoundedBlockingQueue` | array-queue | M |
| `T2-25` | DS-124 Copy-On-Write List | `CopyOnWriteList` | dynamic-array | M |


### Phase 3 — Advanced & applied

_36 structure tasks · content tier: **standard**_

Structures a working engineer reaches for: B-trees, spatial indexes, sketches, string indexes.

| Task | Structure | Class | Prereqs | Size |
|---|---|---|---|---|
| `T3-01` | DS-009 Self-Organizing List (move-to-front) | `SelfOrganizingList` | singly-linked-list | M |
| `T3-02` | DS-010 Skip List | `SkipList` | singly-linked-list | L |
| `T3-03` | DS-029 Robin Hood Hashing | `RobinHoodHashing` | hash-linear-probing | L |
| `T3-04` | DS-030 Cuckoo Hashing | `CuckooHashing` | hash-linear-probing | L |
| `T3-05` | DS-036 LFU Cache | `LfuCache` | lru-cache | L |
| `T3-06` | DS-044 Splay Tree | `SplayTree` | bst | L |
| `T3-07` | DS-045 Treap (randomized BST) | `Treap` | bst, binary-heap | L |
| `T3-08` | DS-047 2-3 Tree | `TwoThreeTree` | bst | L |
| `T3-09` | DS-049 B-Tree | `BTree` | two-three-tree | L |
| `T3-10` | DS-050 B+ Tree | `BPlusTree` | b-tree | XL |
| `T3-11` | DS-051 Order-Statistic Tree | `OrderStatisticTree` | avl-tree | L |
| `T3-12` | DS-053 Interval Tree | `IntervalTree` | red-black-tree | L |
| `T3-13` | DS-055 Merkle Tree | `MerkleTree` | binary-tree | M |
| `T3-14` | DS-060 d-ary Heap | `DAryHeap` | binary-heap | M |
| `T3-15` | DS-065 Leftist Heap | `LeftistHeap` | binary-heap | L |
| `T3-16` | DS-066 Skew Heap | `SkewHeap` | leftist-heap | L |
| `T3-17` | DS-070 Ternary Search Tree | `TernarySearchTree` | trie, bst | L |
| `T3-18` | DS-072 Suffix Trie | `SuffixTrie` | trie | M |
| `T3-19` | DS-073 Suffix Array + LCP | `SuffixArray` | suffix-trie | L |
| `T3-20` | DS-078 Z-Array | `ZArray` | — | M |
| `T3-21` | DS-087 Weighted / Rollback Union-Find | `WeightedUnionFind` | union-find-optimized | L |
| `T3-22` | DS-089 LCA Structure (binary lifting) | `LcaStructure` | adjacency-list, sparse-table | L |
| `T3-23` | DS-092 2D Fenwick Tree | `Fenwick2d` | fenwick-tree | L |
| `T3-24` | DS-094 Segment Tree with Lazy Propagation | `SegmentTreeLazy` | segment-tree | L |
| `T3-25` | DS-097 Sqrt Decomposition | `SqrtDecomposition` | static-array | M |
| `T3-26` | DS-098 k-d Tree | `KdTree` | bst | L |
| `T3-27` | DS-099 Quadtree | `Quadtree` | binary-tree | M |
| `T3-28` | DS-104 Spatial Hash Grid | `SpatialHash` | hash-chaining | M |
| `T3-29` | DS-107 Counting Bloom Filter | `CountingBloom` | bloom-filter | L |
| `T3-30` | DS-109 HyperLogLog | `Hyperloglog` | bitset | L |
| `T3-31` | DS-110 Count-Min Sketch | `CountMinSketch` | static-array | L |
| `T3-32` | DS-112 Reservoir Sampling Structure | `ReservoirSample` | static-array | M |
| `T3-33` | DS-114 Persistent (immutable) List | `PersistentList` | singly-linked-list | M |
| `T3-34` | DS-120 Treiber Stack (lock-free) | `TreiberStack` | linked-stack | L |
| `T3-35` | DS-123 Striped Concurrent Hash Map | `StripedHashMap` | hash-chaining | L |
| `T3-36` | DS-127 Striped Counter | `StripedCounter` | static-array | M |


### Phase 4 — Specialized

_29 structure tasks · content tier: **standard**_

Research-grade and domain-specific structures, plus persistent collections.

| Task | Structure | Class | Prereqs | Size |
|---|---|---|---|---|
| `T4-01` | DS-007 Unrolled Linked List | `UnrolledLinkedList` | doubly-linked-list | L |
| `T4-02` | DS-011 Sparse Array / Sparse Set | `SparseArray` | static-array | M |
| `T4-03` | DS-031 Hopscotch Hashing | `HopscotchHashing` | hash-linear-probing | XL |
| `T4-04` | DS-038 Consistent Hashing Ring | `ConsistentHashing` | hash-chaining | L |
| `T4-05` | DS-041 Threaded Binary Tree | `ThreadedBinaryTree` | binary-tree | L |
| `T4-06` | DS-046 Scapegoat Tree | `ScapegoatTree` | bst | L |
| `T4-07` | DS-048 2-3-4 Tree | `TwoThreeFourTree` | two-three-tree | L |
| `T4-08` | DS-052 Cartesian Tree | `CartesianTree` | treap | L |
| `T4-09` | DS-054 Rope (string tree) | `Rope` | binary-tree | L |
| `T4-10` | DS-062 Min-Max (double-ended) Heap | `MinMaxHeap` | binary-heap | L |
| `T4-11` | DS-063 Binomial Heap | `BinomialHeap` | binary-heap | L |
| `T4-12` | DS-064 Fibonacci Heap | `FibonacciHeap` | binomial-heap | XL |
| `T4-13` | DS-074 Suffix Tree (Ukkonen) | `SuffixTree` | suffix-trie | XL |
| `T4-14` | DS-076 Aho-Corasick Automaton | `AhoCorasick` | trie | L |
| `T4-15` | DS-083 Incidence Matrix | `IncidenceMatrix` | adjacency-matrix | M |
| `T4-16` | DS-084 Compressed Sparse Row Graph | `CsrGraph` | adjacency-list | L |
| `T4-17` | DS-088 Flow Network (residual graph) | `ResidualGraph` | adjacency-list | L |
| `T4-18` | DS-095 Merge Sort Tree | `MergeSortTree` | segment-tree | L |
| `T4-19` | DS-100 Octree | `Octree` | quadtree | L |
| `T4-20` | DS-101 R-Tree | `RTree` | b-tree | XL |
| `T4-21` | DS-105 Range Tree | `RangeTree` | bst | XL |
| `T4-22` | DS-108 Cuckoo Filter | `CuckooFilter` | cuckoo-hashing | L |
| `T4-23` | DS-111 MinHash / LSH | `Minhash` | hash-chaining | XL |
| `T4-24` | DS-115 Hash Array Mapped Trie | `Hamt` | trie, hash-chaining | XL |
| `T4-25` | DS-116 Bit-Partitioned Vector Trie | `PersistentVector` | hamt | XL |
| `T4-26` | DS-117 Persistent Segment Tree | `PersistentSegmentTree` | segment-tree | XL |
| `T4-27` | DS-121 Michael-Scott Queue (lock-free) | `MsQueue` | linked-queue | XL |
| `T4-28` | DS-125 Concurrent Skip List | `ConcurrentSkipList` | skip-list | XL |
| `T4-29` | DS-128 StampedLock-Guarded Map | `StampedLockMap` | hash-chaining | L |


### Phase 5 — Long tail

_15 structure tasks · content tier: **explained**_

Exotic and mostly-theoretical structures, documented for completeness.

| Task | Structure | Class | Prereqs | Size |
|---|---|---|---|---|
| `T5-01` | DS-008 XOR Linked List (theory in Java) | `XorLinkedList` | doubly-linked-list | XL |
| `T5-02` | DS-032 Perfect Hashing (FKS) | `PerfectHashing` | hash-chaining | XL |
| `T5-03` | DS-056 van Emde Boas Tree | `VanEmdeBoas` | bst | XL |
| `T5-04` | DS-057 Link-Cut Tree | `LinkCutTree` | splay-tree | XL |
| `T5-05` | DS-058 Euler Tour Tree | `EulerTourTree` | treap | XL |
| `T5-06` | DS-067 Pairing Heap | `PairingHeap` | binomial-heap | XL |
| `T5-07` | DS-075 Suffix Automaton | `SuffixAutomaton` | suffix-tree | XL |
| `T5-08` | DS-079 Wavelet Tree | `WaveletTree` | binary-tree | XL |
| `T5-09` | DS-102 Ball Tree | `BallTree` | kd-tree | XL |
| `T5-10` | DS-103 BSP Tree | `BspTree` | binary-tree | XL |
| `T5-11` | DS-113 t-digest (quantile sketch) | `TDigest` | binary-heap | XL |
| `T5-12` | DS-118 Finger Tree | `FingerTree` | two-three-tree | XL |
| `T5-13` | DS-119 Zipper | `Zipper` | persistent-list | L |
| `T5-14` | DS-126 Lock-Free Ring Buffer | `LockFreeRingBuffer` | circular-buffer | XL |
| `T5-15` | DS-129 Work-Stealing Deque (Chase-Lev) | `ChaseLevDeque` | deque | XL |

---

## Later-phase platform tasks

| Task | Phase | Description | Satisfies |
|---|---|---|---|
| `T2-P1` | 2 | Comparison view — merged complexity table and "pick this one when" rows for structures sharing an interface | FR-17 |
| `T2-P2` | 2 | Problem-first search synonym map ("range sum", "dedup at scale") | FR-12, journey C |
| `T3-P1` | 3 | Bundle budget check in CI; per-structure content code-splitting verified | FR-33, NFR-1 |
| `T3-P2` | 3 | Internal link checker across cross-references and GitHub source links | FR-32 |
| `T3-P3` | 3 | Prerendered or static fallback for indexability | NFR-8 |
| `T4-P1` | 4 | Explanation-only tier: page variant, badge, and validation rules for tier `explained` | PRD §5.3 |
| `T4-P2` | 4 | Service worker for offline reading of visited pages | NFR-9 |
| `T5-P1` | 5 | Final catalog audit — every one of the 129 has a page at its declared tier | G3, NFR-10 |

## Counts

| Phase | Structures in scope | Already built | New structure tasks | Platform tasks | Tier |
|---|---:|---:|---:|---:|---|
| 1 | 24 | 7 | 17 | 13 | full |
| 2 | 25 | 0 | 25 | 2 | full |
| 3 | 36 | 0 | 36 | 3 | standard |
| 4 | 29 | 0 | 29 | 2 | standard / explanation-optional |
| 5 | 15 | 0 | 15 | 1 | explained |
| **Total** | **129** | **7** | **122** | **21** | — |

All seven existing implementations are phase 1, which is why phase 1 carries only 17 new structure tasks against 24 structures in scope. Those seven are not re-implemented — they are repackaged in `T1-00`, brought onto the uniform error contract in `T1-01`, and re-tagged to the extended schema as part of `T1-03`. DS-015 goes further and becomes the full-tier reference specimen in `T1-09`.

**143 tasks in total.** Phase 1 is 30 of them, and it is the only phase whose completion is a release.
