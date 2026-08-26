# DataStructures — project instructions

A teaching repository: every known data structure, implemented in modern Java, with a generated
tutorial site explaining what each one is, when to use it, **when not to**, and how the code works.
The Java source is the single source of truth — all site content is extracted from Javadoc tags at
build time, so the site can never drift from the code.

Target: 129 structures across 12 categories. 7 are built today.

## Layout

```
src/main/java/datastructure/{category}/   Implementations. One class per structure.
src/main/java/datastructure/interfaces/   Shared contracts. Excluded from the site generator.
src/test/java/datastructure/{category}/   One test class per implementation.
src/test/java/examples/{category}/        Runnable examples — compiled and executed by the suite.
tutorial-app/                             React 19 + Vite 8 site.
tutorial-app/scripts/generate-data.js     Javadoc → src/data.json. The content pipeline.
docs/pipeline/                            Discovery, PRD, journeys, design, this handoff.
docs/pipeline/05-handoff/catalog.json     Authoritative list of all 129 structures.
```

Categories (and therefore packages): `foundations linear hashing trees heaps tries graphs range
spatial probabilistic persistent concurrent`.

## Commands

```bash
./gradlew check                  # tests + 100% line/branch coverage gate. Must pass before every commit.
./gradlew test                   # tests only
cd tutorial-app && npm ci        # first time
cd tutorial-app && npm run dev    # localhost:5173 — regenerates data.json first
cd tutorial-app && npm run build  # production build — regenerates data.json first
cd tutorial-app && npm test       # vitest
cd tutorial-app && npm run lint   # oxlint
```

Coverage report: `build/reports/jacoco/test/html/index.html`.

## Conventions

**Java 26 toolchain.** Use current language features where they make the teaching clearer — records
for immutable entries, sealed interfaces for closed hierarchies, pattern matching for `switch`,
`SequencedCollection` methods, text blocks in examples. Use them for clarity, never for display: a
pattern-matching `switch` that obscures the algorithm is worse than an `if`.

**Style, matching what's already here.** `var` for locals, `final` fields where possible, guard
clauses at the top of methods. Comment the *non-obvious decision*, not the obvious line — the
Hibbard-deletion note in `BinarySearchTree` is the model.

**Every implementation class needs the full Javadoc tag block.** See
`docs/pipeline/05-handoff/authoring-contract.md`. This is not documentation hygiene — the site is
generated from those tags, and validation fails the build when required tags are missing. A class
with no `@description` used to disappear from the site silently; that is now an error.

**Error strategy, uniform across all structures.** Empty-container access throws
`NoSuchElementException`. Invalid index throws `IndexOutOfBoundsException`. Capacity overflow throws
`IllegalStateException`. **Never return `null` to signal an error** — the existing `pop()`/`deQueue()`
`null` returns are being removed. Every documented exception needs a test, because the coverage gate
counts branches.

**100% line and branch coverage on `datastructure.*` is enforced and is not negotiable.** Practical
consequence: don't write defensive branches you can't reach from a test. Fewer branches is also
better teaching code, so this pressure points the right way. `thread.*` is the one exclusion.

**Interfaces.** If two structures share a contract, they implement a shared interface — that's what
makes the site's comparison view possible. `Stack.get(T)` is renamed `find(T)` (it collided with
`Queue.get(int)`).

**Prerequisites must be acyclic and must precede their dependents** in the learning path. CI checks
this from `catalog.json`.

## Adding a structure

1. Find it in `docs/pipeline/05-handoff/catalog.json` — it's already there with its ID, slug,
   category, phase, tier, target class name, and prerequisites. Don't invent new entries.
2. Follow `authoring-contract.md` exactly. It has one complete worked example; pattern-match on it.
3. Implementation → `datastructure.{category}.{Class}`; test → mirrored path; runnable example →
   `src/test/java/examples/{category}/{Class}Example.java`.
4. Set `"status": "done"` in `catalog.json`.
5. `./gradlew check && cd tutorial-app && npm run build` before you call it finished.

Zero frontend files change. If you find yourself editing `tutorial-app/src/`, the content model is
missing a field — fix the model, not the page.

## Don't

- **Don't edit `tutorial-app/src/data.json`.** It's generated. Edit the Javadoc.
- **Don't add a structure that isn't in `catalog.json`,** and don't renumber IDs — the whole pipeline
  is keyed on them.
- **Don't weaken the coverage gate** to land a hard structure. Drop it to the `explained` tier
  instead (no implementation, honest badge on the page) — that's a supported outcome.
- **Don't return `null` for errors** or add new magic-number capacities like the existing `10000`.
- **Don't ship a code sample that isn't compiled by the build.** Examples live in the test source set
  so a broken sample fails CI. A snippet that doesn't compile is the fastest way to lose a learner.
- **Don't reference a package-private helper type (`Node`, `HashEntry`) without including it** in the
  structure's `@helper` list — otherwise the site renders code the reader cannot run.

## Where the reasoning lives

`docs/pipeline/01-discovery.md` (audit + full domain map) · `02-prd.md` (numbered requirements and
acceptance criteria) · `03-user-journey.md` (who this is for and where they get stuck) ·
`04-design/design-system.md` (tokens and component specs) · `05-handoff/` (spec, contract, backlog,
prompts). When a decision here looks arbitrary, it's explained in one of those.
