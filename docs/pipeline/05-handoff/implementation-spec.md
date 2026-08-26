# Implementation Spec

_Stage 5 of 5 · The technical how. Inputs: PRD §6–§9, design system._

## 1. Architecture

```
Java source (datastructure.*)          ← the single source of truth
        │  Javadoc tags
        ▼
generate-data.js  ──▶ validate.js  ──▶ tutorial-app/src/content/*.json
        │                  │                       │
   catalog.json      fails the build         one file per structure
   (cross-check)     on a violation          + one index for search
                                                   │
                                                   ▼
                                          React 19 · HashRouter · Vite 8
```

Two rules hold the whole design together:

1. **Content flows one way.** Nothing in `tutorial-app/src` is hand-authored per structure. If a page needs data it doesn't have, add a field to the content model — never a special case to a component.
2. **The build fails loudly.** The current generator silently drops a class with no `@description`. At 7 structures that's visible; at 129 it's invisible. Validation is what makes the pipeline trustworthy at scale.

## 2. Java side

### Package layout

`datastructure.{category}` for all twelve categories: `foundations linear hashing trees heaps tries graphs range spatial probabilistic persistent concurrent`. `datastructure.interfaces` holds contracts and is excluded from the generator. `thread` stays put and stays excluded from coverage.

Seven classes move in phase 1 (task `T1-00`):

| From | To |
|---|---|
| `datastructure.BasicLinkedList` | `datastructure.foundations.SinglyLinkedList` |
| `datastructure.BasicHashTable` | `datastructure.hashing.LinearProbingHashTable` |
| `datastructure.BasicBinaryTree` | `datastructure.trees.BinarySearchTree` |
| `datastructure.stack.ArrayStack` | `datastructure.linear.ArrayStack` |
| `datastructure.stack.ListStack` | `datastructure.linear.ListStack` |
| `datastructure.queue.ArrayQueue` | `datastructure.linear.ArrayQueue` |
| `datastructure.queue.ListQueue` | `datastructure.linear.ListQueue` |

Names change from `Basic*` to what the structure is actually called, because the class name is the page title.

### Interfaces

Existing: `LinkedList`, `Queue`, `Stack`. Changes and additions in phase 1:

- `Stack.get(T item)` → `Stack.find(T item)` — it collided with `Queue.get(int position)`, and two identically-named methods with incompatible semantics is a bad thing to teach.
- Add `Deque<T>`, `Map<K,V>`, `Set<T>`, `Tree<T>`, `PriorityQueue<T>`, `Graph<V>`.
- Every interface documents its exception contract in Javadoc, since that contract is now uniform and is itself teaching content.

Interfaces are what make the comparison view (FR-17) possible: "structures implementing `Stack`" is the query behind it.

### Error contract

| Condition | Exception |
|---|---|
| Access or removal on an empty container | `NoSuchElementException` |
| Index outside the live window | `IndexOutOfBoundsException` |
| Insert into a full fixed-capacity container | `IllegalStateException` |
| Invalid construction argument | `IllegalArgumentException` |

`null` is never an error signal. `null` returns exist only where absence is a legitimate result (`Map.get` on a missing key).

### Build

`build.gradle` gains: a `check` dependency on the content validator, and the `examples` classes compiled as part of `src/test/java`. Toolchain stays at 26, JaCoCo stays at 100% line and branch on `datastructure.*` with `thread.*` excluded.

## 3. Content pipeline

### `generate-data.js` — rewrite

Current version: one regex per tag, first Javadoc block only, single flat `data.json`, silent skip. Replace with:

1. Walk `src/main/java/datastructure/**`, skipping `interfaces/`.
2. Extract the class-level Javadoc block (the one immediately preceding the type declaration — not simply the first block in the file, which breaks the moment a license header appears).
3. Parse tags into the model: singletons overwrite, repeatables accumulate in source order.
4. Parse `@complexity` into `{op, average, worst, space}`, normalizing notation (`O(logn)` → `O(log n)`); a malformed line is a build error with file and line number.
5. Parse `@walkthrough` into `{startLine, endLine, text}` and verify the range exists in the file.
6. Parse `@avoidWhen` for a trailing `-> slug` and emit `{text, alternative}`.
7. Resolve `@helper` types: find the named package-private types in the same package, inline their source into `helpers[]`.
8. Attach derived fields: `code`, `sourcePath`, `testPath`, `examplePath`, `loc`.
9. Join with `catalog.json` on class name to attach `id`, `slug`, `phase`, `tier`, `category`.
10. Emit `tutorial-app/src/content/{slug}.json` per structure plus `index.json` (id, slug, name, category, difficulty, phase, tier, status, keywords, one-line description) for search and catalog.

Per-structure files matter for NFR-1: the catalog index is small and always loaded, and a structure's body is fetched when its route opens, so page weight doesn't grow with the catalog.

### `validate.js` — new, runs before generate

Fails the build on any of:

- a required tag missing for the structure's tier
- fewer than two `@useWhen` or two `@avoidWhen`
- a malformed `@complexity` line
- a `@walkthrough` range outside the file
- a `@prerequisite` or `@relatedTo` slug that isn't in the catalog
- a prerequisite cycle, or a prerequisite ordered after its dependent
- a class in the catalog marked `done` with no source file, or a source file with no catalog entry
- a referenced helper type that isn't inlined
- an `@example` path that doesn't exist

Output is a list, not the first failure — fixing ten tags one build at a time is how a contributor gives up.

## 4. Frontend

### Stack and routing

React 19.2, Vite 8.1, `react-router` with **`HashRouter`**. GitHub Pages serves from `/DataStructures/`, and the `404.html` redirect trick breaks link previews under a project sub-path. Set `base: '/DataStructures/'` in `vite.config.js`.

```
#/                          Home
#/learn                     Learning path
#/catalog                   All 129, filterable
#/category/:category        Category landing
#/structure/:slug           Structure page
#/compare?a=&b=&c=          Comparison
#/about                     Contributing + tier policy
```

### Components

```
src/
  routes/       Home Learn Catalog Category Structure Compare About NotFound
  components/
    layout/     TopBar SideRail SectionRail MobileDrawer
    structure/  TitleBlock UsePanel AvoidPanel ComplexityTable
                Visualization CodeBlock Walkthrough InvariantList
                RebuildChecklist RelatedRail
    catalog/    StructureCard FilterBar CoverageMeter
    search/     SearchOverlay SearchResult
    ui/         Chip Button Panel Table Badge
  viz/          OpLogPlayer + renderers: RingBuffer Chain Tree Grid
  lib/          content.js search.js progress.js theme.js
  styles/       tokens.css base.css
```

`StructurePage` renders sections in the fixed order from FR-13 and omits any section with no data. The order is defined once as an array of section descriptors — that's what guarantees it never varies between pages.

### Visualization

`@visualization` names an op-log renderer. `OpLogPlayer` owns transport (play/pause/step/reset, current index, captions); the renderer owns drawing. Four renderers cover phases 1–2: `ring-buffer`, `chain` (linked structures), `tree`, `grid` (arrays and tables). `custom:<Component>` escapes to a hand-authored component when the generic view can't do the job.

Every step emits a text caption into the DOM. Nothing autoplays. Under `prefers-reduced-motion` the player renders the final frame and keeps the step controls live.

### Search

Client-side over `index.json`. Prefix and substring matching on name, plus keyword and category matching, plus a small hand-maintained synonym map for problem-first queries ("range sum" → fenwick-tree, segment-tree; "dedup at scale" → bloom-filter, hyperloglog) — that map is Maya's front door. `/` focuses, arrows move, Enter opens, Escape closes. No-results names the two nearest structures by edit distance rather than shrugging.

### Progress and theme

`localStorage`, wrapped in try/catch — a private window or blocked storage degrades to an untracked experience with no error. Keys: `ds:read` (array of slugs), `ds:theme`, `ds:viz-collapsed`. Theme follows `prefers-color-scheme` until the toggle is used.

### Tokens

`src/styles/tokens.css` holds the values from `docs/pipeline/04-design/design-system.md` §1–§3 verbatim. Define the full palette on bare `:root`, redefine only what changes under `:root[data-theme="light"]`. No component defines a raw color.

## 5. CI

`.github/workflows/deploy.yml` runs, in order, and blocks deploy on any failure:

```
./gradlew check                     # tests + coverage gate
cd tutorial-app && npm ci
npm run lint                        # oxlint
npm test                            # vitest
npm run validate                    # content validation
npm run build                       # generate + vite build
```

Add a second workflow for pull requests running everything except the deploy step. Bundle budget check (FR-33) lands in phase 3.

## 6. Scaffolding

`npm run scaffold -- <slug>` reads `catalog.json` and writes: the implementation stub with every required tag present and empty, the test class skeleton with the seven §4 cases stubbed, and the example stub. It never overwrites. This is what keeps a 40-line Javadoc block from being the reason someone doesn't add a structure.
