# Prompt Library

_Copy-paste prompts for Claude Code desktop. Placeholders are in `[BRACKETS]`._

Every prompt names file paths and cites the contract, because a fresh session has none of the context that produced this repo — anchoring it to real files is what stops it improvising.

---

## 1. Implement one structure end to end

The workhorse. Use it 122 times.

```
Implement [SLUG] from docs/pipeline/05-handoff/catalog.json.

Follow docs/pipeline/05-handoff/authoring-contract.md exactly — section 6 is a complete
worked example, so match its shape rather than inventing one. Specifically:

- Implementation at the targetClass path in the catalog entry
- Every required Javadoc tag, with at least two @useWhen and two @avoidWhen, and each
  @avoidWhen naming the alternative structure with a trailing -> slug
- @complexity for every public operation, matching what the code actually does
- @invariant for everything that stays true across operations
- A test class covering all seven cases in contract section 4, including
  delete-then-reinsert
- A runnable example in src/test/java/examples/, asserted by a test so CI catches it
  if it stops compiling

Then run ./gradlew check and confirm coverage is still 100% line and branch, run
cd tutorial-app && npm run build to confirm content validation passes, and set
"status": "done" in catalog.json.

Do not edit anything under tutorial-app/src/. If the page needs data the model doesn't
have, tell me — that's a content model gap, not a component change.
```

**Filled in:**

> Implement `avl-tree` from docs/pipeline/05-handoff/catalog.json. Follow docs/pipeline/05-handoff/authoring-contract.md exactly — section 6 is a complete worked example, so match its shape rather than inventing one. […] Do not edit anything under tutorial-app/src/.

---

## 2. Implement a batch of related structures

Better than one-at-a-time when structures share machinery — three hash tables that differ only in probe strategy should be written together so the comparison is real.

```
Implement these structures from catalog.json, in this order: [SLUG], [SLUG], [SLUG].

They're all [WHAT THEY SHARE], so factor the shared parts honestly rather than
copy-pasting — but keep each class independently readable, because a learner lands on
one page and shouldn't have to open three files to understand it. That tension is
real; when it comes up, favour the learner.

Follow authoring-contract.md for each. Their @relatedTo tags should point at each
other, and their @avoidWhen entries should point at whichever sibling is the better
choice in that situation.

Run ./gradlew check after each one, not at the end — a coverage failure is much
cheaper to find one structure in.
```

**Filled in:**

> Implement these structures from catalog.json, in this order: `hash-quadratic-probing`, `hash-double-hashing`, `robin-hood-hashing`. They're all open-addressing variants that differ only in probe strategy, so factor the shared parts honestly […]

---

## 3. Build a screen from the mockups

```
Build [SCREEN] from the design canvas.

The artboard source is docs/pipeline/04-design/[ARTBOARD].dc.html — read it for exact
layout, spacing, and content. Use the tokens in docs/pipeline/04-design/design-system.md
verbatim; every value you need is in there, so if you're about to invent a hex code or
a spacing value, stop and tell me which one is missing.

docs/pipeline/04-design/design-rationale.md explains why the screen is shaped this way.
When you hit a decision the mockup doesn't cover, make it the way the rationale would.

Requirements this satisfies: [FR-IDS]. Acceptance criteria are in docs/pipeline/02-prd.md
section 6 — check them before you say it's done.

Must hold: works at 320px with no horizontal page scroll, visible focus ring on every
interactive element, and axe-core reports zero serious or critical violations.
```

**Filled in:**

> Build the structure page template from the design canvas. The artboard source is docs/pipeline/04-design/Main.dc.html […] Requirements this satisfies: FR-13, FR-14, FR-16, FR-25, FR-27.

---

## 4. Write tests for existing code

```
Write tests for [CLASS] to bring it to 100% line and branch coverage.

Cover the seven cases in docs/pipeline/05-handoff/authoring-contract.md section 4:
happy path, empty, single element, capacity and resize boundaries, every documented
exception, delete-then-reinsert, and a non-trivial generic type.

Run ./gradlew check and show me the JaCoCo report for this class.

If a branch turns out to be genuinely unreachable, don't contort a test to reach it —
tell me, because an unreachable branch is usually a sign the implementation has a
defensive check it doesn't need, and deleting it is the better fix.
```

---

## 5. Review a phase

```
Review phase [N] against the plan before we move on.

Check, and report as a list rather than prose:
- Every catalog.json structure in phase [N] with status "done" has an implementation,
  a test, and (for tier full) a runnable example and walkthrough
- No structure is marked done without its source file, and no source file is missing
  from the catalog
- Every @complexity claim matches the implementation — read the code, don't trust
  the tag
- Every @avoidWhen names an alternative that resolves to a real slug
- The prerequisite graph is acyclic and ordered
- ./gradlew check, npm run lint, npm test, npm run build all pass
- The phase's exit criteria in docs/pipeline/02-prd.md section 5.1 are met

Then tell me what to fix, ordered by how much it would cost to fix later rather than
by severity now.
```

---

## 6. Change scope and update the pipeline

Use this when a decision changes. Updating one document and leaving the other four stale is how the plan stops being true.

```
[WHAT CHANGED].

Update the pipeline documents so they stay consistent:
- docs/pipeline/05-handoff/catalog.json — the structure entries and the totals block
- docs/pipeline/05-handoff/backlog.md — task list and the counts table at the bottom
- docs/pipeline/02-prd.md — section 5 scope, and section 11 if release criteria move
- docs/pipeline/01-discovery.md — the domain map, if the catalog itself changed
- CLAUDE.md — only if a convention or command changed

Keep IDs stable. Never renumber — the whole pipeline is joined on them. If a structure
is dropped, mark it rather than deleting the entry.

Then show me a diff summary of what changed and what you deliberately left alone.
```

**Filled in:**

> We're dropping `van-emde-boas`, `link-cut-tree`, and `euler-tour-tree` to explanation-only permanently — the coverage gate makes them uneconomic. Update the pipeline documents so they stay consistent […]

---

## 7. Add a new structure to the catalog

```
Add [STRUCTURE NAME] to the catalog — it isn't there and it should be.

Assign it the next free DS- id (don't reuse or renumber), pick its category from the
twelve in catalog.json, set difficulty and phase consistently with comparable
structures already in that phase, set tier from the phase, and wire up prerequisites
using existing slugs only.

Then add it to docs/pipeline/01-discovery.md section 3 and the phase table in
docs/pipeline/02-prd.md section 5.2, and update every total that changes — including
the 129 in CLAUDE.md and the discovery executive summary.

Tell me your reasoning for the phase and difficulty before you write anything, because
those two choices are the ones most likely to be wrong.
```

---

## 8. Start a session cold

For the first prompt of a new session when you don't remember where things stand.

```
Read CLAUDE.md and docs/pipeline/05-handoff/README.md, then tell me where this project
stands: what's built, what's next in the backlog that isn't blocked, and anything in
the repo that's drifted from the plan.

Don't start work yet.
```
