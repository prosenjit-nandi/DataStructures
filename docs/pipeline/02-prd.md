# Product Requirements — Java Data Structures Tutorial

_Stage 2 of 5 · 2026-08-25 · Inputs: `docs/pipeline/01-discovery.md`_

## 1. Overview

### 1.1 Problem

Learning data structures in Java has two bad options. Textbooks explain the theory in pseudocode and leave the learner unable to write compiling Java. Code repositories publish implementations with no explanation, so the learner can read a `Node` class without ever understanding why it's shaped that way. Both leave out the question a practitioner actually asks — *should I use this here?* — and neither tells anyone when a structure is the **wrong** choice.

The existing repository has the right foundation (7 tested implementations, a Javadoc-to-JSON content pipeline, a deployed site) and none of the teaching layer.

### 1.2 Proposed product

A single site, generated from a Java repository, covering **129 data structures**. Every structure gets a page that answers four questions in a fixed order: what it is, when to use it, **when not to use it**, and how the implementation works line by line. Structures are organized into a difficulty-ordered learning path with explicit prerequisites, so a novice can start at "static array" and walk to "red-black tree" without ever hitting a page that assumes something they haven't seen.

The Java source stays the single source of truth. All page content is extracted from structured Javadoc tags at build time, so the site can never drift from the code it documents — and a missing tag fails the build rather than silently deleting a page.

### 1.3 Decisions on stage 1's open questions

| # | Question | Decision |
|---|---|---|
| 1 | Do exotic structures need working code? | **Tiered.** Phases 1–3 require working, tested implementations. Phases 4–5 may ship as `explanation-only` pages, which the site labels visibly. See §5.3. |
| 2 | Does the 100% coverage gate hold? | **Yes, unchanged.** It is the strongest quality signal the project has and it pressures implementations toward simplicity — which is what a teaching repository wants anyway. `explanation-only` structures have no code, so no exemption is needed. |
| 3 | Unify the interfaces? | **Yes, in phase 1.** `Stack.get(T)` is renamed `find(T)`; `Deque`, `Map`, `Set`, `Tree`, `PriorityQueue`, and `Graph` interfaces are introduced. Cheap now, expensive at 40 structures. |
| 4 | Client-side routing on Pages? | **`HashRouter`.** The `404.html` trick breaks link previews and is fragile under a project sub-path. Hash routes give real per-structure URLs today; a custom domain can move to `BrowserRouter` later. |
| 5 | Visualizations hand-authored or generated? | **Both, in that order.** A shared step-through animation component driven by a declarative op-log per structure, with hand-authored SVG allowed as an override where the generic view is inadequate. Required for phases 1–2 only. |
| 6 | How much prose per structure? | **Tiered by phase** — see §5.3. Full-tier pages target 600–1200 words; explained-tier pages target 250–500. |

## 2. Goals and success metrics

| # | Goal | Metric | Target |
|---|---|---|---|
| G1 | A novice can go from landing to running code fast | Time from any structure page to compiling code in the learner's own editor | < 60 seconds (copy button + self-contained runnable example on every full-tier page) |
| G2 | Every page answers "when *not* to use this" | Share of published pages with a populated avoid-when section | 100% |
| G3 | The catalog is genuinely comprehensive | Structures with a published page | 129 / 129 by end of phase 5 |
| G4 | Content cannot silently rot | Build failures on missing required tags | Build fails; zero silently-dropped structures |
| G5 | A learner can find a structure without knowing its name | Median keystrokes to reach a target page via search | ≤ 8 |
| G6 | A practitioner can make a decision without reading prose | Complexity table present and machine-checked on every page with an implementation | 100% |
| G7 | The learning path is followable | Every structure's prerequisites appear earlier in the path than it does | 100%, verified by a cycle/order check in CI |
| G8 | Pages are reachable and shareable | Structures with a stable, linkable URL | 100% |
| G9 | The site is usable by everyone | axe-core violations on the page template | 0 serious or critical |
| G10 | Adding structure N+1 stays cheap | Frontend files touched to publish a new structure | 0 |

## 3. Non-goals

- **Not an algorithms course.** Sorting, searching, dynamic programming, and graph traversal algorithms are out of scope except where a structure exists to serve them (a Fenwick tree ships; binary search does not get its own page).
- **Not a competitive-programming trainer.** No problem sets, no judge, no submissions.
- **Not multi-language.** Java only. No Kotlin, no Python comparison columns.
- **Not a code playground.** No in-browser Java execution. Runnable examples are copy-and-paste into the learner's own editor — running Java in the browser is a project of its own and G1 is satisfied without it.
- **Not accounts or cloud sync.** Progress is per-browser. No login, no backend.
- **Not a JDK collections reference.** The site explains what the JDK gives you and links to the official docs; it does not re-document `java.util`.

## 4. Users

Carried from discovery unchanged. **Riya, the novice, is the primary persona** — when a design decision trades her clarity against Dev's speed or Maya's density, clarity wins, and the other two are served by making dense information *skippable* rather than absent.

| | Riya (novice) | Dev (interview prep) | Maya (practitioner) |
|---|---|---|---|
| Arrives from | Course material, search | Interview prep list | A specific problem at work |
| Wants | To understand and reproduce | Coverage and confidence | A decision and a snippet |
| Reads | The whole page, slowly | Complexity + the code | The avoid-when box, then leaves |
| Fails when | Syntax appears unexplained | Content is uneven in depth | She has to read prose to find a number |

## 5. Scope by phase

### 5.1 Phase summary

| Phase | Theme | Structures | Cumulative | Exit criteria |
|---|---|---:|---:|---|
| **P1** | Foundations & platform | 24 | 24 | One structure page fully realized end-to-end; validation gate live; routes shipped |
| **P2** | Interview core | 25 | 49 | Every interview-relevant structure has a full page with complexity + visualization |
| **P3** | Advanced & applied | 36 | 85 | Comparison views live; all applied/advanced structures implemented and tested |
| **P4** | Specialized | 29 | 114 | Specialized set documented; explanation-only tier formally supported |
| **P5** | Long tail | 15 | 129 | Catalog complete; every one of 129 pages exists at some depth |

Phase 1 is deliberately small in new structures and large in platform work. Its purpose is to prove the entire pipeline — extended schema, validation gate, routing, page template, visualization component, and one fully-realized page — before any volume is added. If the extension contract is wrong, phase 1 is where that must surface.

### 5.2 Structure assignment by phase


#### Phase 1 — Foundations & platform (24 structures)

The structures a first-year course covers, on a rebuilt site that proves the content pipeline end to end.

| Category | Structures |
|---|---|
| Foundations | DS-001 Static Array; DS-002 Dynamic Array (growable); DS-004 Singly Linked List; DS-005 Doubly Linked List |
| Linear | DS-012 Array Stack; DS-013 List-Backed Stack; DS-014 Linked Stack; DS-016 Circular Array Queue; DS-017 List-Backed Queue; DS-018 Linked Queue; DS-019 Double-Ended Queue |
| Hashing | DS-025 Hash Table — Separate Chaining; DS-026 Hash Table — Linear Probing; DS-033 Hash Set |
| Trees & Balanced Search Trees | DS-039 Binary Tree & Traversals; DS-040 Binary Search Tree |
| Heaps & Priority Queues | DS-059 Binary Heap (min/max) |
| Tries & Text Structures | DS-068 Trie (prefix tree) |
| Graphs & Disjoint Sets | DS-080 Adjacency List Graph; DS-081 Adjacency Matrix Graph; DS-082 Edge List Graph; DS-085 Union-Find (quick find / quick union); DS-086 Union-Find (rank + path compression) |
| Range Query Structures | DS-090 Prefix Sum & Difference Array |

#### Phase 2 — Interview core (25 structures)

Everything a candidate is asked about: balanced trees, heaps, tries, range queries, caches.

| Category | Structures |
|---|---|
| Foundations | DS-003 Bit Set; DS-006 Circular Linked List |
| Linear | DS-015 Min/Max Stack (O(1) extremum); DS-020 Circular / Ring Buffer; DS-021 Monotonic Stack; DS-022 Monotonic Deque (sliding-window max); DS-023 Queue From Two Stacks; DS-024 Stack From Two Queues |
| Hashing | DS-027 Hash Table — Quadratic Probing; DS-028 Hash Table — Double Hashing; DS-034 Insertion-Ordered Hash Map; DS-035 LRU Cache; DS-037 Multimap |
| Trees & Balanced Search Trees | DS-042 AVL Tree; DS-043 Red-Black Tree |
| Heaps & Priority Queues | DS-061 Indexed Priority Queue |
| Tries & Text Structures | DS-069 Radix / Patricia Trie; DS-071 Binary (XOR) Trie; DS-077 KMP Failure Table |
| Range Query Structures | DS-091 Fenwick Tree (BIT); DS-093 Segment Tree; DS-096 Sparse Table |
| Probabilistic & Sketch Structures | DS-106 Bloom Filter |
| Concurrent & Lock-Free Structures | DS-122 Bounded Blocking Queue; DS-124 Copy-On-Write List |

#### Phase 3 — Advanced & applied (36 structures)

Structures a working engineer reaches for: B-trees, spatial indexes, sketches, string indexes.

| Category | Structures |
|---|---|
| Foundations | DS-009 Self-Organizing List (move-to-front); DS-010 Skip List |
| Hashing | DS-029 Robin Hood Hashing; DS-030 Cuckoo Hashing; DS-036 LFU Cache |
| Trees & Balanced Search Trees | DS-044 Splay Tree; DS-045 Treap (randomized BST); DS-047 2-3 Tree; DS-049 B-Tree; DS-050 B+ Tree; DS-051 Order-Statistic Tree; DS-053 Interval Tree; DS-055 Merkle Tree |
| Heaps & Priority Queues | DS-060 d-ary Heap; DS-065 Leftist Heap; DS-066 Skew Heap |
| Tries & Text Structures | DS-070 Ternary Search Tree; DS-072 Suffix Trie; DS-073 Suffix Array + LCP; DS-078 Z-Array |
| Graphs & Disjoint Sets | DS-087 Weighted / Rollback Union-Find; DS-089 LCA Structure (binary lifting) |
| Range Query Structures | DS-092 2D Fenwick Tree; DS-094 Segment Tree with Lazy Propagation; DS-097 Sqrt Decomposition |
| Spatial & Multidimensional | DS-098 k-d Tree; DS-099 Quadtree; DS-104 Spatial Hash Grid |
| Probabilistic & Sketch Structures | DS-107 Counting Bloom Filter; DS-109 HyperLogLog; DS-110 Count-Min Sketch; DS-112 Reservoir Sampling Structure |
| Persistent & Functional Structures | DS-114 Persistent (immutable) List |
| Concurrent & Lock-Free Structures | DS-120 Treiber Stack (lock-free); DS-123 Striped Concurrent Hash Map; DS-127 Striped Counter |

#### Phase 4 — Specialized (29 structures)

Research-grade and domain-specific structures, plus persistent collections.

| Category | Structures |
|---|---|
| Foundations | DS-007 Unrolled Linked List; DS-011 Sparse Array / Sparse Set |
| Hashing | DS-031 Hopscotch Hashing; DS-038 Consistent Hashing Ring |
| Trees & Balanced Search Trees | DS-041 Threaded Binary Tree; DS-046 Scapegoat Tree; DS-048 2-3-4 Tree; DS-052 Cartesian Tree; DS-054 Rope (string tree) |
| Heaps & Priority Queues | DS-062 Min-Max (double-ended) Heap; DS-063 Binomial Heap; DS-064 Fibonacci Heap |
| Tries & Text Structures | DS-074 Suffix Tree (Ukkonen); DS-076 Aho-Corasick Automaton |
| Graphs & Disjoint Sets | DS-083 Incidence Matrix; DS-084 Compressed Sparse Row Graph; DS-088 Flow Network (residual graph) |
| Range Query Structures | DS-095 Merge Sort Tree |
| Spatial & Multidimensional | DS-100 Octree; DS-101 R-Tree; DS-105 Range Tree |
| Probabilistic & Sketch Structures | DS-108 Cuckoo Filter; DS-111 MinHash / LSH |
| Persistent & Functional Structures | DS-115 Hash Array Mapped Trie; DS-116 Bit-Partitioned Vector Trie; DS-117 Persistent Segment Tree |
| Concurrent & Lock-Free Structures | DS-121 Michael-Scott Queue (lock-free); DS-125 Concurrent Skip List; DS-128 StampedLock-Guarded Map |

#### Phase 5 — Long tail (15 structures)

Exotic and mostly-theoretical structures, documented for completeness.

| Category | Structures |
|---|---|
| Foundations | DS-008 XOR Linked List (theory in Java) |
| Hashing | DS-032 Perfect Hashing (FKS) |
| Trees & Balanced Search Trees | DS-056 van Emde Boas Tree; DS-057 Link-Cut Tree; DS-058 Euler Tour Tree |
| Heaps & Priority Queues | DS-067 Pairing Heap |
| Tries & Text Structures | DS-075 Suffix Automaton; DS-079 Wavelet Tree |
| Spatial & Multidimensional | DS-102 Ball Tree; DS-103 BSP Tree |
| Probabilistic & Sketch Structures | DS-113 t-digest (quantile sketch) |
| Persistent & Functional Structures | DS-118 Finger Tree; DS-119 Zipper |
| Concurrent & Lock-Free Structures | DS-126 Lock-Free Ring Buffer; DS-129 Work-Stealing Deque (Chase-Lev) |

### 5.3 Content depth tiers

Not every structure earns the same investment. Depth is a function of phase:

| Phase | Content tier | What every page in the tier must contain |
|---|---|---|
| P1 | Full | Visualization, line-by-line walkthrough, complexity table, use/avoid, runnable example, tests |
| P2 | Full | Same as phase 1 |
| P3 | Standard | Complexity table, use/avoid, implementation summary, runnable example, tests — visualization optional |
| P4 | Standard or explained | Full page required; working implementation optional if marked `explanation-only` |
| P5 | Explained | Concept, complexity, use/avoid, and why it is rarely used in Java; implementation optional |

A page in the `explanation-only` tier must display a visible badge stating that no implementation is provided and why. Quietly shipping a thin page is worse than an honest one.

## 6. Functional requirements

Priority: **P0** = v1 (phase 1) blocks without it · **P1** = v1 is materially weaker without it · **P2** = later phase.

### 6.1 Content model and authoring

**FR-1 — Extended Javadoc tag schema.** _(P0)_
The system shall extract a defined set of structured tags from each implementation's class Javadoc, per the schema in §8.
- Every field in §8 marked required is parsed and present in `data.json`
- Repeatable tags (`@complexity`, `@useWhen`, `@avoidWhen`, `@prerequisite`, `@invariant`) accumulate into arrays in source order
- Unknown tags are preserved into a `custom` map rather than discarded

**FR-2 — Build-time content validation.** _(P0)_
The system shall fail the build when a structure is missing a required tag, rather than omitting it from the site.
- `npm run build` exits non-zero listing every offending file and missing tag
- A structure present in the catalog but absent from source is reported as missing
- A structure in source but absent from the catalog is reported as unregistered

**FR-3 — Complexity table extraction.** _(P0)_
The system shall parse `@complexity` tags into a structured per-operation table with average case, worst case, and space.
- Malformed complexity lines fail validation with the file and line number
- Notation is normalized (`O(log n)`, not `O(logn)` / `Olog(n)`)

**FR-4 — Prerequisite graph.** _(P0)_
The system shall build a prerequisite graph from `@prerequisite` tags and verify it is acyclic and consistent with the learning path order.
- A cycle fails the build
- A prerequisite ordered after its dependent fails the build
- The graph is emitted into `data.json` for rendering

**FR-5 — Runnable examples.** _(P1)_
Each full-tier structure shall carry a self-contained runnable example compiled as part of the test source set.
- The example compiles under the project toolchain
- It is exercised by at least one test, so a broken example fails `./gradlew check`
- It appears on the page in its own tab, separate from the implementation

**FR-6 — Line-by-line walkthrough.** _(P1)_
Full-tier structures shall carry a walkthrough that anchors explanations to specific line ranges of the implementation.
- Anchors are line ranges, not copied code, so the walkthrough cannot drift from the source
- An out-of-range anchor fails validation

**FR-7 — Use / avoid guidance.** _(P0)_
Every structure shall document at least two `@useWhen` and at least two `@avoidWhen` entries.
- Validation enforces the minimum counts
- Both render as first-class page sections, not footnotes

**FR-8 — JDK analogue.** _(P1)_
Structures with a `java.util` or `java.util.concurrent` counterpart shall name it and state when the JDK class should be preferred over a hand-rolled implementation.

### 6.2 Site experience

**FR-9 — Per-structure routes.** _(P0)_
Every structure shall have a stable URL of the form `#/structure/{slug}`.
- The URL is bookmarkable and survives reload
- Back and forward navigate between structures
- An unknown slug renders a not-found state with a search box

**FR-10 — Category browse.** _(P0)_ — a catalog view grouping all structures by the 12 categories, showing per-card difficulty, phase, and build status; filterable by category, difficulty, and status.

**FR-11 — Learning path view.** _(P1)_ — an ordered, prerequisite-aware path a beginner can follow start to finish, showing what is unlocked and what a structure depends on.

**FR-12 — Search.** _(P0)_ — client-side search over name, category, tags, and description; keyboard-accessible (`/` to focus, arrows to move, Enter to open); results in under 100 ms at full catalog size.

**FR-13 — Structure page template.** _(P0)_
A single template renders every structure in a fixed section order: title and metadata → what it is → when to use → **when not to use** → complexity table → visualization → implementation → walkthrough → runnable example → related structures.
- Sections with no data are omitted rather than rendered empty
- Section order never varies between pages, so returning readers learn where to look

**FR-14 — Complexity table rendering.** _(P0)_ — a sortable per-operation table with average, worst, and space columns, and a plain-language gloss for each row that a novice can read instead of the notation.

**FR-15 — Step-through visualization.** _(P1)_ — an animated view driven by a declarative operation log, with play/pause/step/reset, and a text description of each step for screen readers. Required for phases 1–2.

**FR-16 — Code display.** _(P0)_ — syntax-highlighted Java with line numbers, a copy button, and a link to the file on GitHub. Copy yields plain source with no line numbers or markup.

**FR-17 — Comparison view.** _(P2)_ — side-by-side comparison of structures implementing the same interface, with a merged complexity table and a "pick this one when…" row.

**FR-18 — Progress tracking.** _(P1)_ — per-browser marking of structures as read, shown on cards and in the learning path, with a visible reset. Storage failure degrades silently to an untracked experience.

**FR-19 — Responsive layout.** _(P0)_ — full functionality from 320 px up; the sidebar collapses to a drawer on mobile; code blocks scroll horizontally inside their own container without the page scrolling sideways.

**FR-20 — Theme support.** _(P1)_ — light and dark, defaulting to the OS preference, with an explicit toggle that persists.

### 6.3 Java implementation standards

**FR-21 — Interface unification.** _(P0)_
Structures implementing a shared contract shall implement a shared interface; `Stack.get(T)` is renamed `find(T)` to stop colliding with `Queue.get(int)`; `Deque`, `Map`, `Set`, `Tree`, `PriorityQueue`, and `Graph` interfaces are added.

**FR-22 — Package layout.** _(P0)_
Every structure shall live in `datastructure.{category}` matching its catalog category. No implementation remains loose in `datastructure`.

**FR-23 — Consistent error strategy.** _(P0)_
Empty-container access shall throw `NoSuchElementException`; invalid index shall throw `IndexOutOfBoundsException`; capacity overflow shall throw `IllegalStateException`. `null` is never used to signal an error condition, and each interface documents the contract.
- Every documented exception is covered by a test
- The rationale appears on the site, since inconsistent error handling is itself a teaching moment

**FR-24 — JDK 26 idiom.** _(P1)_
Implementations shall use current language features where they clarify: records for immutable entries, sealed interfaces for closed hierarchies, pattern matching for `switch`, enhanced instanceof, `SequencedCollection` methods, and text blocks in examples. Feature use is for clarity, not for demonstration — a `switch` pattern that obscures the algorithm is worse than an `if`.

**FR-25 — Documented invariants.** _(P1)_ — every structure states the invariants its operations maintain via `@invariant`, and these render on the page. This is the highest-value teaching content per line written.

**FR-26 — Test standard.** _(P0)_ — one test class per structure, covering happy path, empty, single-element, capacity/resize boundaries, every documented exception, and behavior after a delete-then-reinsert cycle. 100% line and branch coverage remains enforced.

**FR-27 — Generic bounds explained.** _(P1)_ — where a structure needs a bound such as `<T extends Comparable<T>>`, the page explains what the bound means and why it's needed. This is Riya's most reliable stumbling block.

### 6.4 Tooling and CI

**FR-28 — Catalog as data.** _(P0)_ — a checked-in `catalog.json` holding all 129 structures with id, slug, name, category, difficulty, phase, tier, status, and dependencies; it is the authority for what exists and CI verifies source and catalog agree.

**FR-29 — CI runs all gates.** _(P0)_ — the workflow runs `./gradlew check`, `npm run lint`, `npm test`, and content validation before building. Any failure blocks deploy.

**FR-30 — Coverage reporting.** _(P1)_ — the site displays live catalog coverage (built / total, by category and phase) generated at build time.

**FR-31 — Scaffolding command.** _(P1)_ — a generator that, given a slug, emits the implementation stub with all required tags, the test class skeleton, the runnable example stub, and the catalog entry.

**FR-32 — Link checking.** _(P2)_ — CI verifies internal cross-references and GitHub source links resolve.

**FR-33 — Bundle budget.** _(P2)_ — CI fails if the initial JS bundle exceeds the NFR-1 budget, with `data.json` code-split per structure.

## 7. Non-functional requirements

| ID | Requirement | Acceptance |
|---|---|---|
| **NFR-1** | Performance | Initial JS ≤ 250 KB gzipped; LCP < 2.0 s on simulated 4G; per-structure content lazy-loaded so page weight does not grow with catalog size |
| **NFR-2** | Accessibility | WCAG 2.2 AA: 0 serious/critical axe-core violations, full keyboard operation, visible focus, 4.5:1 text contrast in both themes, visualizations paired with text alternatives |
| **NFR-3** | Browser support | Current and previous major of Chrome, Firefox, Safari, Edge; iOS Safari and Chrome Android; no IE |
| **NFR-4** | Build reproducibility | Clean clone → `./gradlew check && cd tutorial-app && npm ci && npm run build` succeeds with no manual steps |
| **NFR-5** | Build time | Full site build under 3 minutes at 129 structures |
| **NFR-6** | Maintainability | Publishing a new structure touches only Java source plus one catalog entry — zero frontend files (G10) |
| **NFR-7** | Correctness | 100% line and branch coverage on `datastructure.*`; every complexity claim backed by the implementation actually present |
| **NFR-8** | SEO and sharing | Per-structure title and meta description; a prerendered or static fallback so pages are indexable |
| **NFR-9** | Offline tolerance | Previously visited pages remain readable offline via a service worker |
| **NFR-10** | Content consistency | Every published page conforms to its tier's required sections, enforced by validation rather than review |

## 8. Content model

The unit is a **Structure**. Fields are extracted from class Javadoc tags unless marked derived.

| Field | Source | Type | Required | Notes |
|---|---|---|---|---|
| `id` | catalog | string | ✅ | `DS-001`; stable forever |
| `slug` | catalog | string | ✅ | URL segment; kebab-case |
| `name` | derived | string | ✅ | Class name, humanized |
| `category` | `@category` | enum(12) | ✅ | Must match catalog |
| `difficulty` | `@difficulty` | enum | ✅ | beginner / intermediate / advanced / expert |
| `phase` | catalog | int 1–5 | ✅ | |
| `tier` | catalog | enum | ✅ | full / standard / explained |
| `description` | `@description` | string | ✅ | One or two sentences: what it is |
| `summary` | `@summary` | string | ✅ | How this implementation works |
| `useWhen[]` | `@useWhen` | string[] | ✅ min 2 | |
| `avoidWhen[]` | `@avoidWhen` | string[] | ✅ min 2 | |
| `complexity[]` | `@complexity` | object[] | ✅ if implemented | `op \| average \| worst \| space` |
| `invariants[]` | `@invariant` | string[] | — | |
| `prerequisites[]` | `@prerequisite` | slug[] | — | Must resolve; must precede in path order |
| `jdkAnalog` | `@jdkAnalog` | string | — | Fully-qualified class, or `none` |
| `preferJdkWhen` | `@preferJdkWhen` | string | — | Required when `jdkAnalog` is set |
| `relatedTo[]` | `@relatedTo` | slug[] | — | Renders the "related structures" rail |
| `keywords[]` | `@keywords` | string[] | — | Feeds search |
| `walkthrough[]` | `@walkthrough` | object[] | ✅ for tier=full | `startLine-endLine: explanation` |
| `visualization` | `@visualization` | string | ✅ for tier=full | Op-log name or `custom:<component>` |
| `example` | `@example` | path | ✅ for tier=full | Path to the runnable example class |
| `code` | derived | string | ✅ if implemented | Full source |
| `sourcePath` | derived | string | ✅ if implemented | Repo-relative |
| `testPath` | derived | string | ✅ if implemented | |
| `loc` | derived | int | — | |

### Worked example — `DS-015` Circular Array Queue

```java
/**
 * @description A first-in-first-out queue that stores elements in a fixed-size array and
 *              wraps the read and write positions around the end of the array.
 * @category linear
 * @difficulty beginner
 * @summary Keeps a `front` index and a `size` count. The next write position is computed as
 *          `(front + size) % capacity`, so elements never need to be shifted — the window of
 *          live elements simply rotates through the array.
 * @useWhen You know the maximum number of queued items in advance.
 * @useWhen You need strict O(1) enqueue and dequeue with no allocation after construction.
 * @useWhen You are in a latency-sensitive path where resizing pauses are unacceptable.
 * @avoidWhen The maximum size is unknown or unbounded — use a linked or growable queue.
 * @avoidWhen You need to insert or remove from the middle.
 * @avoidWhen You are tempted to size the array defensively large; the memory is reserved up front.
 * @complexity enqueue | O(1) | O(1) | O(1)
 * @complexity dequeue | O(1) | O(1) | O(1)
 * @complexity peek    | O(1) | O(1) | O(1)
 * @complexity contains| O(n) | O(n) | O(1)
 * @invariant size is always between 0 and capacity inclusive.
 * @invariant Live elements occupy indices (front + i) % capacity for i in [0, size).
 * @prerequisite static-array
 * @jdkAnalog java.util.ArrayDeque
 * @preferJdkWhen You want a queue in production — ArrayDeque grows automatically and is
 *                faster than this for almost every workload.
 * @relatedTo linked-queue
 * @relatedTo circular-buffer
 * @keywords fifo, ring, wrap-around, modular arithmetic
 * @walkthrough 24-31: The constructor allocates once. Nothing after this point allocates.
 * @walkthrough 38-44: enQueue computes the write slot by wrapping past the end of the array.
 * @walkthrough 47-56: deQueue nulls the slot it vacates so the queue does not pin dead objects.
 * @visualization ring-buffer
 * @example src/test/java/examples/linear/ArrayQueueExample.java
 */
```

The Javadoc block above is the *entire* authoring surface for that page. No frontend file is edited to publish it (G10, NFR-6).

## 9. Information architecture

```
#/                             Home — what this is, learning path entry, coverage, search
#/learn                        Ordered learning path, prerequisite-aware
#/catalog                      All 129, filterable by category / difficulty / phase / status
#/category/{category}          One of 12 category landing pages with orientation prose
#/structure/{slug}             The structure page (FR-13 section order)
#/compare?a={slug}&b={slug}    Side-by-side comparison (FR-17)
#/about                        Contributing, the authoring contract, project status
```

## 10. Dependencies and risks

| Risk | Likelihood | Impact | Mitigation |
|---|---|---|---|
| Content volume stalls the project — 129 pages of good prose is a book | **High** | **High** | Tiered depth (§5.3); phases are independently shippable; scaffolding command (FR-31); explanation-only tier is a legitimate finish line, not a failure |
| 100% branch coverage becomes prohibitive on complex structures (red-black, B+ tree) | Medium | High | Keep implementations teaching-simple; prefer fewer defensive branches; if a phase-4 structure genuinely can't be covered, drop it to explanation-only rather than weakening the gate |
| Visualization component balloons into its own product | Medium | Medium | Declarative op-log with one generic renderer; required only for phases 1–2; custom SVG allowed but never required |
| Javadoc tags become unwieldy — the example above is 40 lines of comment | Medium | Medium | Accepted deliberately: the alternative is a parallel content tree that drifts. Validation plus scaffolding keeps it mechanical |
| GitHub Pages sub-path breaks routing or assets | Low | Medium | HashRouter (§1.3 #4); Vite `base` set to `/DataStructures/`; smoke-test the deployed URL in CI |
| Complexity claims are wrong somewhere across 129 pages | Medium | High | Complexity is authored next to the code it describes; walkthrough anchors force the author to re-read the implementation; peer-checkable table format |
| Scope creep into algorithms | Medium | Medium | §3 non-goals are explicit; a structure page may mention an algorithm but never teaches one |

## 11. Release criteria

**v1 (end of phase 1) ships when all of the following are true:**

1. All P0 requirements are implemented and their acceptance criteria verified.
2. All 24 phase-1 structures are implemented, tested at 100% line and branch, and published.
3. At least one structure page is complete at full tier — visualization, walkthrough, complexity table, use/avoid, runnable example — and serves as the reference specimen for the authoring contract.
4. Content validation fails the build on a deliberately broken tag (verified by a negative test).
5. `#/structure/{slug}` resolves, is bookmarkable, and survives reload for every published structure.
6. Search returns the correct page for all 12 category names and all phase-1 structure names.
7. axe-core reports 0 serious or critical violations on home, catalog, and structure pages in both themes.
8. Lighthouse performance ≥ 90 on the structure page at simulated 4G.
9. A clean clone builds and deploys with no manual steps (NFR-4).
10. `docs/pipeline/05-handoff/authoring-contract.md` has been followed end-to-end by someone other than its author to add one structure without asking a question.

Criterion 10 is the real test. Everything else can be satisfied by the person who designed the system; only that one proves the system works without them.

---

_Next: stage 3 — user journeys. Run the `user-journey-map` skill against this document._
