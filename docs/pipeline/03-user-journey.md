# User Journeys — Java Data Structures Tutorial

_Stage 3 of 5 · 2026-08-25 · Inputs: `docs/pipeline/02-prd.md`_

## 1. Overview

Three journeys, one per persona from the PRD. They are mapped separately because they want opposite things from the same page — Riya wants to be walked through it, Maya wants to skim past everything Riya needs. Section 5 resolves the conflicts; sections 6 and 7 turn all of it into a screen list for stage 4.

The "thinking" column is written in first person on purpose. Abstract user needs ("wants to understand the concept") produce no design; a specific question a person actually asks produces a specific panel on a page.

---

## 2. Journey A — Riya: "First contact" (a beginner's first 25 minutes)

### Context

Tuesday, 9 pm, dorm room. Riya has a data structures midterm in nine days and a lecture slide deck she doesn't understand. She searched *"linked list java example"*, and this site was the third result. She has one semester of Java, a laptop with IntelliJ already open, and roughly 25 minutes of attention before she gives up and watches a video instead.

She arrives **deep-linked on the singly linked list page**, not on the home page. This matters more than anything else in the journey: most beginners never see the landing page, so orientation has to work from any structure page.

### Stage 1 — Arrive

| Action | Touchpoint | Thinking | Friction | Requirement |
|---|---|---|---|---|
| Lands on `#/structure/singly-linked-list` from a search result | Structure page | *"Is this a tutorial or someone's GitHub dump?"* | If the first screenful is a code block, she reads it as a dump and leaves | Above the fold: title, one-sentence plain-English description, difficulty badge, and a visible "Start here" path affordance — not code (FR-13) |
| Scans the page shape | Structure page | *"How long is this? Am I going to be here all night?"* | An unbounded wall of scroll reads as work | Sticky section nav showing the fixed section order and reading time (FR-13) |
| Notices the difficulty badge says "beginner" | Structure page | *"Okay, this one's meant for me."* | — | Difficulty on every page, always in the same spot (FR-1) |

### Stage 2 — Orient

| Action | Touchpoint | Thinking | Friction | Requirement |
|---|---|---|---|---|
| Reads "what it is" | Structure page | *"A chain of boxes where each box knows the next one. Fine, I get that."* | Jargon in the first paragraph loses her instantly | Description written for a first-semester reader; no term used before it's introduced (FR-1) |
| Sees "prerequisites: static array" | Structure page | *"Do I need to go read that first? I don't have time."* | An unqualified prerequisite reads as a gate and causes bounce | Prerequisites shown with a one-line "you need this because…" and marked optional-if-known, never as a lock (FR-4, FR-11) |
| Hits the complexity table | Structure page | *"O(n)? I've seen this in lecture. I don't actually know what it means for this thing."* | Notation without a gloss is decoration | Plain-language gloss on every row: "finding an item means walking the chain from the start" (FR-14) |

### Stage 3 — Learn

| Action | Touchpoint | Thinking | Friction | Requirement |
|---|---|---|---|---|
| Plays the visualization, steps through `add` | Structure page — visualization | *"Oh — the last box's arrow changes. That's the whole trick."* | Autoplaying animation that finishes before she looks up teaches nothing | Step controls default to paused, with per-step text captions (FR-15, NFR-2) |
| Reads the implementation | Structure page — code | *"What is `<T>`? And why is `Node` a different thing that isn't on this page?"* | **This is where beginners stop.** Unexplained generics and invisible helper classes | Inline explainer on the generic bound (FR-27); helper classes shown in a tab on the same page, never assumed |
| Reads the walkthrough | Structure page — walkthrough | *"Line 34 is where it links the old last box to the new one. I'd have forgotten that."* | Walkthrough that restates the code adds nothing | Walkthrough anchors to line ranges and explains *why*, never *what* (FR-6) |
| Reads the invariants | Structure page | *"So `last` always points at the end. That's why `add` is fast."* | — | Invariants rendered as a distinct callout (FR-25) |

### Stage 4 — Apply

| Action | Touchpoint | Thinking | Friction | Requirement |
|---|---|---|---|---|
| Clicks copy on the runnable example | Structure page — example tab | *"Let me just run this and see."* | **A snippet that doesn't compile ends the session and the trust.** | Self-contained example with `main`, compiled and tested in CI (FR-5); copy yields plain source (FR-16) |
| Pastes into IntelliJ, runs it | Her editor | *"It ran. Okay, I'm actually doing this."* | Missing imports, missing helper class, package mismatch | Example is a complete compilable file including package and imports |
| Deletes the body and tries to rewrite `add` from memory | Her editor | *"Wait, do I update `last` before or after?"* | Nothing on the page supports recall as opposed to reading | "Rebuild it yourself" checklist at the end of the page: the invariants restated as a spec, with the code collapsed |
| Gets it wrong, comes back, re-reads lines 30–40 | Structure page | *"There. `last.setNextNode` first, then move `last`."* | Losing her scroll position when she returns | Deep-linkable line anchors; walkthrough steps individually linkable (FR-6) |

### Stage 5 — Return

| Action | Touchpoint | Thinking | Friction | Requirement |
|---|---|---|---|---|
| Marks the page done | Structure page | *"One down."* | Nothing to mark means no sense of progress | Mark-as-read control on every page (FR-18) |
| Looks for what's next | Structure page — footer | *"What should I learn after this?"* | Dead-ending at the bottom of a page wastes the highest-intent moment on the site | "Next in the path" card at the page foot, driven by the prerequisite graph (FR-11) |
| Comes back Thursday | Home page | *"Where was I?"* | A home page identical to her first visit forgets her | Home shows resume state and read count when progress exists (FR-18) |

### Emotional arc

```
curious ──▶ reassured ──▶ ENGAGED ──▶ CONFUSED ──▶ relieved ──▶ SATISFIED ──▶ motivated
 arrive     difficulty    the viz     generics      explainer    it compiled    next card
                                      + helper                   and ran
                                       class
```

**Low point: the generics moment in stage 3.** It is the most reliable place a beginner quits, and it is invisible to anyone who already knows Java. The design response is non-negotiable: `<T>` and `<T extends Comparable<T>>` get an inline, in-context explanation on the page where they first appear, not a link to a Java tutorial elsewhere.

### Where it breaks down

- She never sees the home page, so every structure page must self-orient.
- The helper class problem is systemic: a `Node` that isn't visible on the page makes the code unrunnable in her head.
- 25 minutes is the real budget. A page that needs 40 loses her, which is why reading time is shown up front rather than discovered by scrolling.

---

## 3. Journey B — Dev: "Interview sweep" (six weeks out, 45 minutes a night)

### Context

Sunday afternoon. Dev has a list of twenty structures he's told himself he'll cover before onsites, and three years of professional Java in which he has used `HashMap` daily and implemented one never. He is on a laptop with a second monitor, working in sprints, and he will abandon any resource that turns out to be uneven — three great pages and then a stub is worse to him than a consistently mediocre site, because he can't plan around it.

### Stage 1 — Arrive and assess

| Action | Touchpoint | Thinking | Friction | Requirement |
|---|---|---|---|---|
| Lands on home from a Reddit comment | Home | *"Is this complete or is it another abandoned repo?"* | Vague completeness claims read as marketing | Live coverage stat on the home page: built / total, by category (FR-30) |
| Opens the catalog | Catalog | *"Does it have red-black trees and LRU cache, or does it stop at stacks?"* | 129 unfiltered cards is a wall | Filter by category, difficulty, and status; status visible per card (FR-10) |
| Filters to "interview-relevant" | Catalog | *"I want the ones that actually get asked, not van Emde Boas."* | The catalog is organized by category, not by his goal | **New requirement (see §8):** a curated "interview core" collection cutting across categories |

### Stage 2 — Sweep

| Action | Touchpoint | Thinking | Friction | Requirement |
|---|---|---|---|---|
| Opens LRU cache, jumps straight to code | Structure page | *"I know what an LRU is. Show me the implementation."* | Being made to scroll past beginner prose every time is friction that compounds over twenty pages | Sticky section nav allows a one-click jump to Implementation; section order is fixed so the jump is muscle memory (FR-13) |
| Reads the complexity table | Structure page | *"Get O(1), put O(1), and eviction is O(1) because of the doubly linked list. Good — that's the answer they want."* | — | Complexity table above the code (FR-14) |
| Notices `@preferJdkWhen` | Structure page | *"`LinkedHashMap` with accessOrder does this in one line. That's a good thing to say in an interview."* | — | JDK analogue + when to prefer it (FR-8) |
| Moves to the next structure | Path / related rail | *"Next."* | Returning to the catalog and re-filtering every time is dead time | "Related structures" rail and next-in-collection navigation on every page (FR-1, FR-11) |

### Stage 3 — Verify

| Action | Touchpoint | Thinking | Friction | Requirement |
|---|---|---|---|---|
| Opens compare on ArrayStack vs ListStack vs LinkedStack | Compare view | *"If they ask which one I'd pick, I want the actual trade-off, not vibes."* | Three tabs and manual diffing | Compare view: merged complexity table plus a "pick this one when…" row per implementation (FR-17) |
| Copies a snippet into a scratch file to type it from memory | His editor | *"Can I write this cold?"* | — | Copy button (FR-16) |
| Marks four structures read | Structure pages | *"Sixteen to go."* | — | Progress tracking with a visible count (FR-18) |

### Stage 4 — Return nightly

| Action | Touchpoint | Thinking | Friction | Requirement |
|---|---|---|---|---|
| Returns Monday night | Home | *"Where was I in the interview list?"* | Generic home page | Resume card showing the last-read structure and remaining count in the active collection (FR-18) |
| Hits a phase-4 explanation-only page | Structure page | *"There's no code here. Is this whole site half-finished?"* | **An unlabelled thin page destroys trust in every page he already read.** | Explicit `explanation-only` badge with the reason and the phase it's slated for (PRD §5.3) |

### Emotional arc

```
skeptical ──▶ reassured ──▶ EFFICIENT ──▶ efficient ──▶ jarred ──▶ reassured
  arrive      coverage      the sweep      compare      thin page   honest badge
              stat
```

**Low point: hitting a thin page unannounced.** The fix is honesty made visible, not hiding the page. Dev tolerates incompleteness he can predict and abandons incompleteness that surprises him.

### Where it breaks down

- Depth inconsistency is his only real failure mode, and it is a content-governance problem rather than a UI one — which is why NFR-10 enforces tier conformance in the build rather than in review.
- His goal cuts across the category taxonomy, and nothing in the PRD served that. See §8.

---

## 4. Journey C — Maya: "The decision" (eleven minutes, mid-task)

### Context

Thursday, 2 pm, in the middle of a work task. She has a 50-million-row array and needs range sums with point updates. She half-remembers "Fenwick tree" from years ago and needs to know whether it fits before she commits an afternoon. She has a terminal open, a Jira ticket open, and no patience.

### Stage 1 — Arrive by search

| Action | Touchpoint | Thinking | Friction | Requirement |
|---|---|---|---|---|
| Searches *"java fenwick tree range sum"*, lands on the structure page | Structure page | *"Complexity table. Where."* | Anything above the complexity table is an obstacle | Complexity table within the first two screenfuls; sticky nav for direct jump (FR-13, FR-14) |
| Reads the table | Structure page | *"Update O(log n), prefix query O(log n), space O(n). That works."* | — | (FR-14) |

### Stage 2 — Qualify

| Action | Touchpoint | Thinking | Friction | Requirement |
|---|---|---|---|---|
| Jumps to "when not to use" | Structure page | *"I need to know what bites me before I know what it does."* | This section buried below the code is the single most common tutorial failure | **Avoid-when is a first-class section, positioned before the implementation, never a footnote** (FR-7, FR-13) |
| Reads: *"avoid when you need range assignment rather than point update — use a segment tree with lazy propagation"* | Structure page | *"Good, I only need point updates. And now I know the escape hatch if that changes."* | — | Avoid-when entries name the alternative structure and link to it (FR-7) |
| Checks the JDK analogue | Structure page | *"Nothing in java.util does this. Fine, I'm writing it."* | — | (FR-8) |

### Stage 3 — Take

| Action | Touchpoint | Thinking | Friction | Requirement |
|---|---|---|---|---|
| Copies the implementation | Structure page | *"I'll adapt this to longs."* | Copying markup, line numbers, or a fragment that needs a missing helper | Copy yields plain, complete, compilable source (FR-16) |
| Skims the invariants | Structure page | *"One-indexed internally. That's the bit I'd have gotten wrong."* | — | Invariants callout (FR-25) |
| Glances at "related: segment tree, 2D Fenwick" | Structure page | *"Bookmarking the 2D one — that's next quarter's problem."* | — | Related rail (FR-1) |
| Leaves | — | — | — | — |

### Emotional arc

```
impatient ──▶ satisfied ──▶ CONFIDENT ──▶ gone
  arrive      the table    avoid-when     (this is success)
                           answered it
```

Maya's journey succeeding means she leaves quickly. Designing to keep her on the page would make the product worse.

### Where it breaks down

- Any layout that puts teaching content between her and the complexity table costs her the visit.
- A generic "when not to use" that doesn't name the alternative sends her to Google, and she doesn't come back.

---

## 5. Cross-journey conflicts

| # | Conflict | Resolution |
|---|---|---|
| 1 | Riya needs the page to unfold slowly; Maya needs the answer in the first screenful | Fixed section order plus a **sticky section nav**. Maya jumps, Riya scrolls. Neither mode is the default — the nav makes both first-class. Ordering avoid-when *before* the implementation serves Maya without costing Riya anything, since she reads linearly regardless |
| 2 | Riya needs generics explained inline; Dev finds inline explanation patronizing | Explainers are **inline but collapsed**, marked with a subtle affordance on the token itself. Expanding is one click; ignoring is free |
| 3 | Riya wants a guided path; Dev and Maya arrive deep and never see it | The path is a **rail, not a gate**. Every structure page carries its position in the path and a next card; nothing is locked |
| 4 | Dev wants coverage-by-goal; the IA is organized by category | Collections cut across categories (see §8) and coexist with the taxonomy rather than replacing it |
| 5 | Riya benefits from visualization; it is noise to Maya | Visualization sits below avoid-when and complexity, and remembers its collapsed state per browser |
| 6 | Progress tracking matters to Riya and Dev, is irrelevant to Maya | Fully passive — nothing prompts, nothing gates, no account. Invisible until used |

---

## 6. Screen inventory

Stage 4 designs exactly this list.

| # | Screen | Purpose | Required states | Journey steps served | PRD refs |
|---|---|---|---|---|---|
| S1 | **Structure page — full tier** | The core surface. Fixed section order: title/meta → what it is → use when → **avoid when** → complexity → visualization → implementation → walkthrough → runnable example → related + next | populated · loading · code-copied · viz playing · explainer expanded | A2–A5, B2, C1–C3 | FR-13/14/15/16/6/5/7 |
| S2 | **Structure page — explanation-only tier** | Honest thin page for phase 4–5 structures | populated with badge | B4 | PRD §5.3 |
| S3 | **Home / landing** | Orient a first-time visitor in ten seconds; resume for a returning one | first visit · returning with progress | B1, A5-return | FR-30, FR-18 |
| S4 | **Catalog browse** | Scan and filter all 129 | populated · filtered · no results | B1 | FR-10 |
| S5 | **Learning path** | Ordered, prerequisite-aware sequence | not started · in progress · complete | A5, conflict #3 | FR-11 |
| S6 | **Category landing** | Orientation prose plus the members of one of 12 categories | populated | browse | FR-10 |
| S7 | **Search overlay** | Reach any structure from anywhere by keyboard | idle · typing · results · **no results** | C1, B1 | FR-12 |
| S8 | **Compare view** | Side-by-side trade-offs for structures sharing an interface | two selected · three selected · picker empty | B3 | FR-17 |
| S9 | **Not found** | Recover from a dead or renamed slug | — | — | FR-9 |
| S10 | **Structure page — mobile** | S1 at 320–480 px: drawer nav, horizontally scrollable code and tables | populated · drawer open | A1–A4 on phone | FR-19 |

The empty and error states in this table are not padding. Riya meets the no-results state when she searches for a term her course uses and the site doesn't; Dev meets the badge state on his first thin page. Both are trust moments.

---

## 7. Make-or-break moments

| # | Moment | Failure mode | Design response |
|---|---|---|---|
| **M1** | The first screenful of a deep-linked structure page | Reads as a code dump; she leaves in eight seconds | Title, plain-English description, difficulty badge, reading time, and section nav above the fold. **No code above the fold, ever** |
| **M2** | First contact with `<T extends Comparable<T>>` | Beginner concludes the material is beyond her and stops | Inline collapsed explainer on the token itself, at the point of first appearance on that page (FR-27) |
| **M3** | Copied code doesn't compile | Trust is gone for the whole site, not just the page | Runnable examples are compiled and executed in CI (FR-5); copy yields complete plain source including package and imports (FR-16) |
| **M4** | The invisible helper class | The code is unreadable and un-runnable in her head | Helper types (`Node`, `HashEntry`) render in a tab on the same page; validation fails if a referenced helper isn't included |
| **M5** | Landing on a thin page unannounced | Uneven depth destroys confidence in pages already read | Visible `explanation-only` badge stating why and when it's scheduled |
| **M6** | End of page — "did I actually learn this?" | Reading is mistaken for understanding; nothing converts | "Rebuild it yourself" checklist: the invariants restated as a spec with the implementation collapsed |
| **M7** | Return visit after a week | Cold restart; the habit dies | Resume card on home; per-structure read state; next-in-path card at every page foot |

---

## 8. Requirements this surfaced that the PRD missed

Five, and the first two are substantial.

**J-1 — Curated collections that cut across categories.** _(P1)_
Dev's entire goal — "the twenty that get asked" — has no representation in a category-based IA. The site needs named, ordered collections (`interview-core`, `beginner-path`, `systems-engineering`, `competitive-programming`) that a structure can belong to independently of its category, with progress and next-item navigation scoped to the active collection. This is a small data addition (`@collection` tag plus a collections file) and it is the difference between Dev using the site and bookmarking a different one.

**J-2 — Helper types must render on the page that uses them.** _(P0)_
`BasicLinkedList` references `Node`; `BasicHashTable` references `HashEntry`. Neither appears on the site today because the generator emits one record per class and the page renders one class. For a beginner this makes the code literally unreadable. The content model needs a `helpers[]` field, the generator needs to resolve and inline referenced package-private types, and validation should fail when a structure's source references a type that is neither in the JDK nor included. This was invisible from the requirements and obvious within thirty seconds of watching someone read.

**J-3 — Reading time on every page.** _(P1)_
Both Riya and Dev budget attention before committing to a page. Displaying estimated reading time above the fold costs nothing and directly reduces bounce at M1.

**J-4 — "Rebuild it yourself" section.** _(P1)_
The PRD gets the learner as far as *reading* an implementation and stops. M6 is the moment learning either converts or evaporates. Restating the invariants as a build spec with the code collapsed is a small section that does the actual teaching work.

**J-5 — Avoid-when entries must name their alternative.** _(P0, tightens FR-7)_
"Avoid when you need range assignment" is a dead end. "Avoid when you need range assignment — use a segment tree with lazy propagation" is a decision. The `@avoidWhen` tag should support an optional trailing slug reference that renders as a link, and validation should warn when an avoid-when entry names no alternative.

---

_Next: stage 4 — design mockups. Run the `design-mockups` skill against this document._
