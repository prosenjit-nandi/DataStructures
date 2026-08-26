# Discovery — Java Data Structures Tutorial

_Stage 1 of 5 · 2026-08-25 · Repo: `prosenjit-nandi/DataStructures` @ `main`_

## 1. Executive summary

The repository is a small, clean, well-tested seed rather than a tutorial platform. It contains **7 data structure implementations** across three packages, each with a JUnit 5 test class, behind a Gradle build that enforces **100% line and branch coverage** on everything under `datastructure.*`. A React 19 + Vite application in `tutorial-app/` renders those structures as a browsable site, deployed to GitHub Pages by a working Actions workflow.

The single most valuable thing already in the repo is not any of the implementations — it is the **content pipeline**. `tutorial-app/scripts/generate-data.js` parses `@description`, `@usage`, and `@summary` Javadoc tags out of every Java source file under `datastructure/` and emits `src/data.json`, which the React app renders. Adding a structure to the site costs zero frontend work. That mechanism is the correct architectural bet and the roadmap should be built on top of it rather than around it.

Against the stated goal — every known data structure, on JDK 26, with a knowledge base covering where to use each one, where *not* to, and how the implementation works — the gap is large but well-shaped. A full domain map produces **129 structures** across 12 categories; **7 are built (5.4%)**, leaving **122 missing**. The content gap is deeper than the count suggests: the current model gives each structure three sentences and a code dump, with no complexity data, no "when not to use", no visualization, no line-by-line explanation, and no ordering that a beginner could follow. A learner arriving today gets a reference card, not a tutorial.

The recommended shape is: keep the Java-source-as-source-of-truth pipeline, expand the extracted schema substantially, rebuild the site's information architecture around a learning path, and grow the catalog in dependency order across five phases.

## 2. Current state audit

### 2.1 Repository inventory

| Area | Contents |
|---|---|
| Build | `build.gradle` (Java toolchain **26**, JUnit 5.14.4, JaCoCo 0.8.15), `settings.gradle`, Gradle wrapper **9.6.1** |
| Java main | 12 files: 7 implementations, 3 interfaces, 2 helper/demo (`Node`-style helpers inline, `thread/ThreadSample.java`) |
| Java test | 7 test classes, one per implementation |
| Frontend | `tutorial-app/` — React 19.2, Vite 8.1, Vitest 4.1, oxlint, `react-syntax-highlighter`, `lucide-react` |
| Content pipeline | `tutorial-app/scripts/generate-data.js` → `tutorial-app/src/data.json` |
| CI/CD | `.github/workflows/deploy.yml` — builds `tutorial-app` and deploys to GitHub Pages on push to `main` |
| Docs | `README.md` (note: claims JDK 26+ requirement; accurate), `tutorial-app/README.md` |

Repository history is shallow — a single squashed commit on `main` at the time of audit.

### 2.2 What is implemented

| ID | Structure | File | Interface | Tests |
|---|---|---|---|---|
| DS-004 | Singly Linked List | `src/main/java/datastructure/BasicLinkedList.java` | `interfaces.LinkedList` | `BasicLinkedListTest` |
| DS-026 | Hash Table — Linear Probing | `src/main/java/datastructure/BasicHashTable.java` | — (none) | `BasicHashTableTest` |
| DS-040 | Binary Search Tree | `src/main/java/datastructure/BasicBinaryTree.java` | — (none) | `BasicBinaryTreeTest` |
| DS-012 | Array Stack | `src/main/java/datastructure/stack/ArrayStack.java` | `interfaces.Stack` | `ArrayStackTest` |
| DS-013 | List-Backed Stack | `src/main/java/datastructure/stack/ListStack.java` | `interfaces.Stack` | `ListStackTest` |
| DS-016 | Circular Array Queue | `src/main/java/datastructure/queue/ArrayQueue.java` | `interfaces.Queue` | `ArrayQueueTest` |
| DS-017 | List-Backed Queue | `src/main/java/datastructure/queue/ListQueue.java` | `interfaces.Queue` | `ListQueueTest` |

Observations on the code itself, which are relevant because everything new has to sit beside it:

- The style is genuinely good and worth preserving as the house standard: `var` for locals, `final` fields where possible, guard clauses at the top of methods, and explanatory comments at the *non-obvious* decisions (the Hibbard-deletion comment in `BasicBinaryTree` and the shift-delete comment in `BasicHashTable` are exactly right).
- Modern JDK APIs are already in use — `List.removeLast()` / `getLast()` / `getFirst()` (SequencedCollection, JDK 21+) appear in `ListStack` and `ListQueue`.
- `BasicHashTable` uses open addressing with linear probing, resize-on-load-factor, and correct shift-delete. It is the most sophisticated thing in the repo.
- Every implementation carries the three Javadoc tags the site generator needs. That discipline is already established and should become a hard rule.

### 2.3 The extension contract

This is the seam the entire roadmap hangs off, so stating it precisely matters:

1. Add a `.java` file anywhere under `src/main/java/datastructure/` (files under `interfaces/` are deliberately excluded by the generator).
2. Give the class a leading Javadoc block containing `@description`, `@usage`, and `@summary`. **A class without `@description` is silently dropped from the site** — no error, no warning.
3. Add a matching test class; the JaCoCo rule will fail `./gradlew check` on anything below 100% line *and* branch coverage.
4. On push to `main`, the workflow regenerates `data.json` (via `prebuild`) and redeploys.

The silent-drop behaviour in step 2 is the most dangerous property of the current system. At 7 structures a missing tag is obvious; at 129 it is invisible.

### 2.4 Quality gates and constraints

- **Coverage:** 100% line and branch, enforced by `jacocoTestCoverageVerification`, `thread.*` excluded. This is unusually strict and is a real constraint on implementation style — every defensive branch written must be reachable by a test, so speculative `if` statements are expensive.
- **Toolchain:** Java 26 via foojay resolver. Confirmed as the intended baseline.
- **Frontend gates:** `vitest` and `oxlint` scripts exist; CI runs neither — only `npm run build`.
- **Hosting:** GitHub Pages, static only. No server, no database, no auth. Anything stateful must live in the browser.

### 2.5 Inconsistencies found

| # | Finding | Why it matters |
|---|---|---|
| 1 | Package layout is mixed — `stack/` and `queue/` are sub-packaged, but `BasicLinkedList`, `BasicHashTable`, `BasicBinaryTree` sit loose in `datastructure/` | At 129 structures a flat root becomes unnavigable; the convention must be fixed before the catalog grows |
| 2 | `BasicHashTable` and `BasicBinaryTree` implement no interface, while stacks/queues/lists do | Learners can't compare implementations of the same contract, which is one of the most valuable teaching moves available |
| 3 | `Stack.get(T item)` takes an item and returns an item; `Queue.get(int position)` takes an index | Two methods with the same name and incompatible semantics — confusing to a novice reading both pages |
| 4 | `ArrayStack`/`ArrayQueue` default to capacity 10 000 and throw when full; `pop()`/`deQueue()` return `null` when empty rather than throwing | Mixed error strategy; also an unexplained magic number on a page a beginner is reading to learn good practice |
| 5 | CI runs neither `vitest` nor `oxlint` | Frontend regressions ship silently |
| 6 | `thread/ThreadSample.java` sits outside the domain and is excluded from coverage | Harmless, but it should be labelled as a deliberate exception rather than an oversight |
| 7 | The generator has no validation step | See §2.3 — silent drops |

## 3. Domain map

The complete field, grouped into 12 categories. Difficulty is from the perspective of the target learner (a novice Java programmer), not from a research standpoint. Phase is the recommended build order and is carried into the PRD unchanged.

**129 structures · 7 built · 122 missing**


### Foundations — Arrays & Lists

_11 structures · 1 built · 10 missing_

| ID | Structure | Difficulty | Phase | Status | JDK analogue |
|---|---|---|---|---|---|
| DS-001 | Static Array | beginner | 1 | ⬜ missing | `T[]` |
| DS-002 | Dynamic Array (growable) | beginner | 1 | ⬜ missing | `java.util.ArrayList` |
| DS-003 | Bit Set | intermediate | 2 | ⬜ missing | `java.util.BitSet` |
| DS-004 | Singly Linked List | beginner | 1 | ✅ built | `—` |
| DS-005 | Doubly Linked List | beginner | 1 | ⬜ missing | `java.util.LinkedList` |
| DS-006 | Circular Linked List | intermediate | 2 | ⬜ missing | `—` |
| DS-007 | Unrolled Linked List | advanced | 4 | ⬜ missing | `—` |
| DS-008 | XOR Linked List (theory in Java) | expert | 5 | ⬜ missing | `—` |
| DS-009 | Self-Organizing List (move-to-front) | intermediate | 3 | ⬜ missing | `—` |
| DS-010 | Skip List | advanced | 3 | ⬜ missing | `java.util.concurrent.ConcurrentSkipListMap` |
| DS-011 | Sparse Array / Sparse Set | intermediate | 4 | ⬜ missing | `—` |

### Linear — Stacks, Queues, Deques

_13 structures · 4 built · 9 missing_

| ID | Structure | Difficulty | Phase | Status | JDK analogue |
|---|---|---|---|---|---|
| DS-012 | Array Stack | beginner | 1 | ✅ built | `java.util.ArrayDeque` |
| DS-013 | List-Backed Stack | beginner | 1 | ✅ built | `java.util.ArrayDeque` |
| DS-014 | Linked Stack | beginner | 1 | ⬜ missing | `—` |
| DS-015 | Min/Max Stack (O(1) extremum) | intermediate | 2 | ⬜ missing | `—` |
| DS-016 | Circular Array Queue | beginner | 1 | ✅ built | `java.util.ArrayDeque` |
| DS-017 | List-Backed Queue | beginner | 1 | ✅ built | `java.util.ArrayDeque` |
| DS-018 | Linked Queue | beginner | 1 | ⬜ missing | `—` |
| DS-019 | Double-Ended Queue | beginner | 1 | ⬜ missing | `java.util.ArrayDeque` |
| DS-020 | Circular / Ring Buffer | intermediate | 2 | ⬜ missing | `—` |
| DS-021 | Monotonic Stack | intermediate | 2 | ⬜ missing | `—` |
| DS-022 | Monotonic Deque (sliding-window max) | intermediate | 2 | ⬜ missing | `—` |
| DS-023 | Queue From Two Stacks | beginner | 2 | ⬜ missing | `—` |
| DS-024 | Stack From Two Queues | beginner | 2 | ⬜ missing | `—` |

### Hashing — Tables, Sets, Caches

_14 structures · 1 built · 13 missing_

| ID | Structure | Difficulty | Phase | Status | JDK analogue |
|---|---|---|---|---|---|
| DS-025 | Hash Table — Separate Chaining | beginner | 1 | ⬜ missing | `java.util.HashMap` |
| DS-026 | Hash Table — Linear Probing | intermediate | 1 | ✅ built | `—` |
| DS-027 | Hash Table — Quadratic Probing | intermediate | 2 | ⬜ missing | `—` |
| DS-028 | Hash Table — Double Hashing | intermediate | 2 | ⬜ missing | `—` |
| DS-029 | Robin Hood Hashing | advanced | 3 | ⬜ missing | `—` |
| DS-030 | Cuckoo Hashing | advanced | 3 | ⬜ missing | `—` |
| DS-031 | Hopscotch Hashing | expert | 4 | ⬜ missing | `—` |
| DS-032 | Perfect Hashing (FKS) | expert | 5 | ⬜ missing | `—` |
| DS-033 | Hash Set | beginner | 1 | ⬜ missing | `java.util.HashSet` |
| DS-034 | Insertion-Ordered Hash Map | intermediate | 2 | ⬜ missing | `java.util.LinkedHashMap` |
| DS-035 | LRU Cache | intermediate | 2 | ⬜ missing | `LinkedHashMap(accessOrder)` |
| DS-036 | LFU Cache | advanced | 3 | ⬜ missing | `—` |
| DS-037 | Multimap | beginner | 2 | ⬜ missing | `Map<K,List<V>>` |
| DS-038 | Consistent Hashing Ring | advanced | 4 | ⬜ missing | `—` |

### Trees & Balanced Search Trees

_20 structures · 1 built · 19 missing_

| ID | Structure | Difficulty | Phase | Status | JDK analogue |
|---|---|---|---|---|---|
| DS-039 | Binary Tree & Traversals | beginner | 1 | ⬜ missing | `—` |
| DS-040 | Binary Search Tree | beginner | 1 | ✅ built | `java.util.TreeMap` |
| DS-041 | Threaded Binary Tree | advanced | 4 | ⬜ missing | `—` |
| DS-042 | AVL Tree | intermediate | 2 | ⬜ missing | `—` |
| DS-043 | Red-Black Tree | advanced | 2 | ⬜ missing | `java.util.TreeMap` |
| DS-044 | Splay Tree | advanced | 3 | ⬜ missing | `—` |
| DS-045 | Treap (randomized BST) | advanced | 3 | ⬜ missing | `—` |
| DS-046 | Scapegoat Tree | advanced | 4 | ⬜ missing | `—` |
| DS-047 | 2-3 Tree | advanced | 3 | ⬜ missing | `—` |
| DS-048 | 2-3-4 Tree | advanced | 4 | ⬜ missing | `—` |
| DS-049 | B-Tree | advanced | 3 | ⬜ missing | `—` |
| DS-050 | B+ Tree | expert | 3 | ⬜ missing | `—` |
| DS-051 | Order-Statistic Tree | advanced | 3 | ⬜ missing | `—` |
| DS-052 | Cartesian Tree | advanced | 4 | ⬜ missing | `—` |
| DS-053 | Interval Tree | advanced | 3 | ⬜ missing | `—` |
| DS-054 | Rope (string tree) | advanced | 4 | ⬜ missing | `—` |
| DS-055 | Merkle Tree | intermediate | 3 | ⬜ missing | `—` |
| DS-056 | van Emde Boas Tree | expert | 5 | ⬜ missing | `—` |
| DS-057 | Link-Cut Tree | expert | 5 | ⬜ missing | `—` |
| DS-058 | Euler Tour Tree | expert | 5 | ⬜ missing | `—` |

### Heaps & Priority Queues

_9 structures · 0 built · 9 missing_

| ID | Structure | Difficulty | Phase | Status | JDK analogue |
|---|---|---|---|---|---|
| DS-059 | Binary Heap (min/max) | beginner | 1 | ⬜ missing | `java.util.PriorityQueue` |
| DS-060 | d-ary Heap | intermediate | 3 | ⬜ missing | `—` |
| DS-061 | Indexed Priority Queue | intermediate | 2 | ⬜ missing | `—` |
| DS-062 | Min-Max (double-ended) Heap | advanced | 4 | ⬜ missing | `—` |
| DS-063 | Binomial Heap | advanced | 4 | ⬜ missing | `—` |
| DS-064 | Fibonacci Heap | expert | 4 | ⬜ missing | `—` |
| DS-065 | Leftist Heap | advanced | 3 | ⬜ missing | `—` |
| DS-066 | Skew Heap | advanced | 3 | ⬜ missing | `—` |
| DS-067 | Pairing Heap | expert | 5 | ⬜ missing | `—` |

### Tries & Text Structures

_12 structures · 0 built · 12 missing_

| ID | Structure | Difficulty | Phase | Status | JDK analogue |
|---|---|---|---|---|---|
| DS-068 | Trie (prefix tree) | beginner | 1 | ⬜ missing | `—` |
| DS-069 | Radix / Patricia Trie | advanced | 2 | ⬜ missing | `—` |
| DS-070 | Ternary Search Tree | advanced | 3 | ⬜ missing | `—` |
| DS-071 | Binary (XOR) Trie | intermediate | 2 | ⬜ missing | `—` |
| DS-072 | Suffix Trie | intermediate | 3 | ⬜ missing | `—` |
| DS-073 | Suffix Array + LCP | advanced | 3 | ⬜ missing | `—` |
| DS-074 | Suffix Tree (Ukkonen) | expert | 4 | ⬜ missing | `—` |
| DS-075 | Suffix Automaton | expert | 5 | ⬜ missing | `—` |
| DS-076 | Aho-Corasick Automaton | advanced | 4 | ⬜ missing | `—` |
| DS-077 | KMP Failure Table | intermediate | 2 | ⬜ missing | `—` |
| DS-078 | Z-Array | intermediate | 3 | ⬜ missing | `—` |
| DS-079 | Wavelet Tree | expert | 5 | ⬜ missing | `—` |

### Graphs & Disjoint Sets

_10 structures · 0 built · 10 missing_

| ID | Structure | Difficulty | Phase | Status | JDK analogue |
|---|---|---|---|---|---|
| DS-080 | Adjacency List Graph | beginner | 1 | ⬜ missing | `—` |
| DS-081 | Adjacency Matrix Graph | beginner | 1 | ⬜ missing | `—` |
| DS-082 | Edge List Graph | beginner | 1 | ⬜ missing | `—` |
| DS-083 | Incidence Matrix | intermediate | 4 | ⬜ missing | `—` |
| DS-084 | Compressed Sparse Row Graph | advanced | 4 | ⬜ missing | `—` |
| DS-085 | Union-Find (quick find / quick union) | beginner | 1 | ⬜ missing | `—` |
| DS-086 | Union-Find (rank + path compression) | intermediate | 1 | ⬜ missing | `—` |
| DS-087 | Weighted / Rollback Union-Find | advanced | 3 | ⬜ missing | `—` |
| DS-088 | Flow Network (residual graph) | advanced | 4 | ⬜ missing | `—` |
| DS-089 | LCA Structure (binary lifting) | advanced | 3 | ⬜ missing | `—` |

### Range Query Structures

_8 structures · 0 built · 8 missing_

| ID | Structure | Difficulty | Phase | Status | JDK analogue |
|---|---|---|---|---|---|
| DS-090 | Prefix Sum & Difference Array | beginner | 1 | ⬜ missing | `—` |
| DS-091 | Fenwick Tree (BIT) | intermediate | 2 | ⬜ missing | `—` |
| DS-092 | 2D Fenwick Tree | advanced | 3 | ⬜ missing | `—` |
| DS-093 | Segment Tree | intermediate | 2 | ⬜ missing | `—` |
| DS-094 | Segment Tree with Lazy Propagation | advanced | 3 | ⬜ missing | `—` |
| DS-095 | Merge Sort Tree | advanced | 4 | ⬜ missing | `—` |
| DS-096 | Sparse Table | intermediate | 2 | ⬜ missing | `—` |
| DS-097 | Sqrt Decomposition | intermediate | 3 | ⬜ missing | `—` |

### Spatial & Multidimensional

_8 structures · 0 built · 8 missing_

| ID | Structure | Difficulty | Phase | Status | JDK analogue |
|---|---|---|---|---|---|
| DS-098 | k-d Tree | advanced | 3 | ⬜ missing | `—` |
| DS-099 | Quadtree | intermediate | 3 | ⬜ missing | `—` |
| DS-100 | Octree | advanced | 4 | ⬜ missing | `—` |
| DS-101 | R-Tree | expert | 4 | ⬜ missing | `—` |
| DS-102 | Ball Tree | expert | 5 | ⬜ missing | `—` |
| DS-103 | BSP Tree | expert | 5 | ⬜ missing | `—` |
| DS-104 | Spatial Hash Grid | intermediate | 3 | ⬜ missing | `—` |
| DS-105 | Range Tree | expert | 4 | ⬜ missing | `—` |

### Probabilistic & Sketch Structures

_8 structures · 0 built · 8 missing_

| ID | Structure | Difficulty | Phase | Status | JDK analogue |
|---|---|---|---|---|---|
| DS-106 | Bloom Filter | intermediate | 2 | ⬜ missing | `—` |
| DS-107 | Counting Bloom Filter | advanced | 3 | ⬜ missing | `—` |
| DS-108 | Cuckoo Filter | advanced | 4 | ⬜ missing | `—` |
| DS-109 | HyperLogLog | advanced | 3 | ⬜ missing | `—` |
| DS-110 | Count-Min Sketch | advanced | 3 | ⬜ missing | `—` |
| DS-111 | MinHash / LSH | expert | 4 | ⬜ missing | `—` |
| DS-112 | Reservoir Sampling Structure | intermediate | 3 | ⬜ missing | `—` |
| DS-113 | t-digest (quantile sketch) | expert | 5 | ⬜ missing | `—` |

### Persistent & Functional Structures

_6 structures · 0 built · 6 missing_

| ID | Structure | Difficulty | Phase | Status | JDK analogue |
|---|---|---|---|---|---|
| DS-114 | Persistent (immutable) List | intermediate | 3 | ⬜ missing | `java.util.List.of` |
| DS-115 | Hash Array Mapped Trie | expert | 4 | ⬜ missing | `—` |
| DS-116 | Bit-Partitioned Vector Trie | expert | 4 | ⬜ missing | `—` |
| DS-117 | Persistent Segment Tree | expert | 4 | ⬜ missing | `—` |
| DS-118 | Finger Tree | expert | 5 | ⬜ missing | `—` |
| DS-119 | Zipper | advanced | 5 | ⬜ missing | `—` |

### Concurrent & Lock-Free Structures

_10 structures · 0 built · 10 missing_

| ID | Structure | Difficulty | Phase | Status | JDK analogue |
|---|---|---|---|---|---|
| DS-120 | Treiber Stack (lock-free) | advanced | 3 | ⬜ missing | `—` |
| DS-121 | Michael-Scott Queue (lock-free) | expert | 4 | ⬜ missing | `ConcurrentLinkedQueue` |
| DS-122 | Bounded Blocking Queue | intermediate | 2 | ⬜ missing | `ArrayBlockingQueue` |
| DS-123 | Striped Concurrent Hash Map | advanced | 3 | ⬜ missing | `ConcurrentHashMap` |
| DS-124 | Copy-On-Write List | intermediate | 2 | ⬜ missing | `CopyOnWriteArrayList` |
| DS-125 | Concurrent Skip List | expert | 4 | ⬜ missing | `ConcurrentSkipListMap` |
| DS-126 | Lock-Free Ring Buffer | expert | 5 | ⬜ missing | `—` |
| DS-127 | Striped Counter | intermediate | 3 | ⬜ missing | `java.util.concurrent.atomic.LongAdder` |
| DS-128 | StampedLock-Guarded Map | advanced | 4 | ⬜ missing | `—` |
| DS-129 | Work-Stealing Deque (Chase-Lev) | expert | 5 | ⬜ missing | `ForkJoinPool internals` |

## 4. Gap analysis

### 4.1 Coverage

| Category | Total | Built | Missing | Coverage |
|---|---:|---:|---:|---:|
| Foundations — Arrays & Lists | 11 | 1 | 10 | 9% |
| Linear — Stacks, Queues, Deques | 13 | 4 | 9 | 31% |
| Hashing — Tables, Sets, Caches | 14 | 1 | 13 | 7% |
| Trees & Balanced Search Trees | 20 | 1 | 19 | 5% |
| Heaps & Priority Queues | 9 | 0 | 9 | 0% |
| Tries & Text Structures | 12 | 0 | 12 | 0% |
| Graphs & Disjoint Sets | 10 | 0 | 10 | 0% |
| Range Query Structures | 8 | 0 | 8 | 0% |
| Spatial & Multidimensional | 8 | 0 | 8 | 0% |
| Probabilistic & Sketch Structures | 8 | 0 | 8 | 0% |
| Persistent & Functional Structures | 6 | 0 | 6 | 0% |
| Concurrent & Lock-Free Structures | 10 | 0 | 10 | 0% |
| **Total** | **129** | **7** | **122** | **5.4%** |

By difficulty, the catalog is weighted toward material the current site could not teach at all:

| Difficulty | Count | Share |
|---|---:|---:|
| Beginner | 25 | 19% |
| Intermediate | 35 | 27% |
| Advanced | 43 | 33% |
| Expert | 26 | 20% |

### 4.2 Quality gaps in what exists

- **Two of seven** structures implement no interface (§2.5 #2).
- **Zero** structures document time or space complexity anywhere — not in the code, not on the site. For a data structures tutorial this is the single most conspicuous omission.
- **Zero** structures document when *not* to use them, which is half of the stated goal.
- **Zero** runnable examples. Every code sample is a class definition; none can be pasted into a `main` and run.
- Error-handling strategy is inconsistent and undocumented (§2.5 #4).
- The `@usage` tag conflates "where to use" with "why this variant" — it will need splitting.

### 4.3 Structural gaps in the system

These are gaps in the product, not the catalog, and they matter more:

1. **No learning path.** The sidebar is alphabetical, so a beginner's first click is `ArrayQueue` — a circular buffer with modular arithmetic. There is no notion of prerequisites, ordering, or difficulty.
2. **No complexity data.** No Big-O table per operation, no comparison across implementations of the same contract.
3. **No "when not to use".** Explicitly requested; entirely absent.
4. **No explanation of the code.** The site shows the source and a one-sentence summary. Nothing walks a learner through *why* `front = (front + 1) % capacity` works.
5. **No visualization.** Pointer-based structures are close to unlearnable from source text alone.
6. **No search or filtering.** Tolerable at 7 items; unusable at 129.
7. **No category or comparison views.** A learner cannot ask "show me all the queues" or "ArrayStack vs ListStack".
8. **No progress tracking.** Nothing marks what's been read, so a returning learner restarts cold.
9. **No content validation.** A malformed or missing tag disappears silently.
10. **Single-page, non-linkable.** The whole app is client state — no per-structure URL, so nothing can be bookmarked, shared, or found by a search engine.

Gap 10 is worth calling out separately: without routes, none of the tutorial content is indexable, which caps the project's reach regardless of how good the writing gets.

## 5. Audience

**Persona A — Riya, the novice (primary).**
Second-year CS student, one semester of Java. Can read a `for` loop and write a class; has never seen `<T extends Comparable<T>>` and reads it as noise. Goal: "understand linked lists well enough to write one from scratch in an exam." Gets stuck when a code sample assumes a helper class she can't see, when generics syntax appears without explanation, and when she can't tell whether she's understood a page or merely finished scrolling it. Done means: she closed the laptop, opened an empty file, and wrote a working singly linked list from memory.

**Persona B — Dev, the interview candidate.**
Three years of Java at work, six weeks from onsite loops. Uses `HashMap` daily and has never implemented one. Goal: "be able to explain and implement the twenty structures that actually get asked." Gets stuck on the gap between *using* the JDK collection and *implementing* it, and wastes time because nothing tells him which structures are actually interview-relevant. Done means: he can whiteboard an LRU cache and state the complexity of every operation without hedging.

**Persona C — Maya, the practitioner.**
Senior backend engineer with a specific problem — range queries over a large array, or dedup at a scale where a `HashSet` won't fit in memory. Not learning; deciding. Goal: "is a Fenwick tree the right tool here, and what does it cost me?" Gets stuck because tutorials optimize for teaching rather than for decision-making, so she has to read three pages to find one complexity table. Done means: she picked a structure, knows its trade-offs, and has code she can adapt in under ten minutes.

All three want different things from the same page. That tension is the core design problem and is handed to stage 3 deliberately.

## 6. Constraints and assumptions

**Constraints**
- Java toolchain 26 (confirmed); JUnit 5 + JaCoCo at 100% line/branch on `datastructure.*`.
- React 19 + Vite 8 retained; static hosting on GitHub Pages under a project sub-path (`/DataStructures/`), which affects router basename and asset paths.
- No backend, no database, no authentication. Browser storage only for anything stateful.
- Java source remains the single source of truth for content; the site is generated from it.

**Assumptions**
- Solo maintainer working with an AI coding agent, so the authoring contract has to be mechanically followable rather than tribal knowledge.
- Completeness of the catalog is a stated goal, but the long tail can ship as documented-and-explained without production-grade implementations.
- English only for v1.

## 7. Open questions for stage 2

1. **Does the exotic tail need working code, or is a documented explanation enough?** Implementing a link-cut tree to 100% branch coverage is days of work for an audience of approximately nobody. Recommendation: phases 1–3 get full implementations; phases 4–5 may ship explanation-only pages, clearly labelled.
2. **Does the 100% coverage gate hold at 129 structures?** It is excellent discipline and it is also the main cost driver per structure. Recommendation: hold it — it is a differentiator and it forces simpler code.
3. **Do the interfaces get unified?** Fixing `Stack.get`/`Queue.get` and adding interfaces for maps and trees is a small breaking change now and a large one later.
4. **Client-side routing on GitHub Pages** requires either a hash router or the `404.html` redirect trick. Which?
5. **Visualizations: hand-authored or generated?** Hand-authored SVG per structure is high quality and doesn't scale to 129; a generic step-through animator scales but is a significant build in itself.
6. **How much per-structure prose is realistic?** A good page is 600–1200 words. At 129 structures that is a book. Recommendation: tier it — depth scales with phase.

## 8. Recommended direction

- **Keep the generator; widen the schema.** Java source stays the source of truth. Extend the extracted tags from three to roughly a dozen (complexity, category, difficulty, prerequisites, when-not-to-use, JDK analogue, runnable example) and add a validation step that fails the build on missing required tags instead of dropping content silently.
- **Rebuild the IA around a learning path, not an alphabetical list.** Categories, difficulty, prerequisites, real routes per structure, search, and comparison views.
- **Fix the foundations before scaling the catalog.** Unify package layout and interfaces at 7 structures, not at 40.
- **Grow in dependency order across five phases**, with phase 1 deliberately small so it proves the whole pipeline — schema, validation, routing, page template, and one fully-realized structure page — before volume is added.
- **Tier the content depth.** Phase 1–2 structures get the full treatment: visualization, line-by-line walkthrough, complexity table, when-not-to-use, runnable example. Phase 4–5 structures get a solid explanation and may skip the implementation.

---

_Next: stage 2 — PRD. Run the `product-prd` skill against this document._
