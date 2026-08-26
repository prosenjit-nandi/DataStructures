# Design Rationale

_Why each screen is shaped the way it is, traced back to stage 3. Stage 5 will make dozens of decisions these mockups don't cover; this is what lets those be made the same way._

## The one decision everything else follows from

Three people want opposite things from the same page. Riya wants to be walked through it slowly. Maya wants the answer in the first screenful and then to leave. Dev wants to skip the teaching and land on the code, twenty times in a row.

The resolution is **a fixed section order plus a sticky section rail**. Order never varies between pages, so after two pages every reader knows where things are; the rail makes jumping a first-class action rather than a scroll. Neither reading mode is the default, and neither costs the other anything.

Ordering **avoid-when above the implementation** is the one place a real trade was made, and it went Maya's way. She reads it and leaves — that's her whole journey. Riya reads linearly regardless, so the position costs her nothing. Dev jumps past it either way. A section that only one persona needs, placed where it costs the others nothing, is free.

## `Main` — the structure page

**No code above the fold, ever** (M1). Riya arrives here from a search result, not the home page, so the first screenful decides whether the site is a tutorial or a GitHub dump. Title, one plain-English sentence, difficulty, and reading time — then a prerequisite line phrased as an offer ("already comfortable with that? Skip ahead") rather than a gate, because an unqualified prerequisite reads as a lock and causes bounce (A2).

**Reading time in the badge row** is J-3 from the journey. Both Riya and Dev budget attention before committing. It costs one chip and removes the "how long is this?" scroll.

**Use-when green, avoid-when red, and both loud.** "When not to use this" is a stated product goal, so it gets a full panel with its own colour, not a footnote. Each avoid-when entry names its alternative and links to it (J-5) — "avoid when the size is unknown" is a dead end; "use a Linked Queue instead" is a decision. The fourth entry names `ArrayDeque` and says outright that the JDK class is better, because a tutorial that pretends its teaching implementations are production code is lying to the reader in a way they'll discover later.

**The complexity table has a fifth column in plain English** (A2). "O(n)" without a gloss is decoration to someone who has seen the notation in a lecture and never internalized it. "There's no index — every element gets checked one by one" is the same fact, teachable.

**The visualization is paused by default with step controls** (A3, NFR-2). An animation that finishes before the reader looks up teaches nothing. Each step is captioned in text, which serves screen readers and also serves the reader who'd rather read than watch.

**The code block has three tabs, and the second one is `Queue.java`.** This is M4, the invisible-helper problem — the single most concrete thing the journey exposed that the PRD missed. `BasicLinkedList` references a `Node` class that appears nowhere on the site today, which makes the code unreadable in a beginner's head. Helper types render on the page that uses them, or the page is broken.

**The walkthrough explains why, never what.** Line 37's entry doesn't say "computes the index" — it says this is the line that makes it circular and if you remember one line from this page, remember this. Restating code in prose is the most common way a walkthrough section becomes worthless.

**"Now rebuild it yourself" with the code hidden** is J-4, and it's the section that does the actual teaching. M6 is the moment where reading either converts to understanding or evaporates. The invariants are already stated above; restating them as a build spec with the implementation collapsed turns the page from something read into something done.

**"Next in the path" is the largest thing at the foot of the page.** The bottom of a page the reader finished is the highest-intent moment on the site, and today it's a dead end (A5).

## `Home`

Built for two visitors who need different things in the first three seconds.

Dev's question is "is this complete or another abandoned repo?", so the **live coverage panel** is the first thing on the right — 7 of 129, broken down by category, generated at build time. Honest and unflattering. Claiming completeness the catalog doesn't have would be found out on his second click.

Riya's question is "where do I start?", so the primary action is **"Start at the beginning"** and the resume strip sits directly under it when progress exists (A5-return).

The **three entry cards** are the three journeys made literal: a path, a curated interview collection (J-1), and a problem-first door for Maya. J-1 is worth the extra data model: Dev's goal — "the twenty that get asked" — cuts straight across a category taxonomy, and without collections the IA simply has no answer for him.

## `Catalog`

129 items is a wall, so **filters are above the grid and always visible**, with active filters as removable chips and a live result count. The category section carries a paragraph of orientation prose — the taxonomy is only useful if the reader knows what the category is *for*, and "the cheapest way to learn how a contract differs from an implementation" is a better reason to read the Linear section than the word "Linear".

Cards carry **status and complexity at a glance** so Dev can triage without opening anything, and the next section header is rendered dimmed rather than hidden, so a filter visibly narrows the catalog instead of appearing to delete it.

## `Search`

Drawn in its **no-results state on purpose**. Empty states are trust moments (S7), and this is the one Riya hits when she searches for a term her course uses that the catalog doesn't. The overlay doesn't shrug — it names what it couldn't find, then offers the two nearest structures with enough description to tell whether either is what she meant. The footer teaches problem-first search ("range sum", "dedup at scale"), which is Maya's door.

## `Compare`

Dev's "if they ask which one I'd pick, I want the actual trade-off, not vibes" (B3). Three implementations of one interface, a merged complexity table, and then the rows that actually decide it — grows on demand, allocates while running, cache behaviour — ending in a highlighted **"pick this one when…"** row.

The three avoid-when cards below are per-structure rather than merged, because "avoid ArrayStack when the maximum depth is a guess" is only meaningful next to the specific structure. The amber footnote says none of these is what you'd ship. Again: the site's credibility depends on saying that out loud.

## `Explained`

The whole artboard exists for one component: the **amber banner** (M5). A thin page that announces itself keeps Dev's trust; one that doesn't destroys his confidence in every page he already read. So the banner leads with "there is no implementation on this page — on purpose", gives the real reason (days of work for an audience of nearly nobody, against a 100% branch-coverage gate), states the phase, and offers a way to ask for it.

The rest of the page is a full-tier page minus code — and the avoid-when section is *better* here than on most pages, because for a link-cut tree the honest answer really is "almost always, use something simpler", with both simpler things linked.

## `Mobile`

A drawer for the rail, and the section rail becomes a **horizontal chip scroller** — the jump affordance is too valuable to drop, and chips survive the width where a vertical rail can't.

The table and the code block each **scroll inside their own container**; the page body never scrolls sideways (FR-19). This is the specific failure the current app would have at 129 structures with wide complexity tables.

The **sticky bottom bar** carries mark-as-read and next-in-path at 46px, above the 44px minimum, because on a phone the end of the page is where the session either continues or ends and neither action should require scrolling back up.

No fake status bar and no fake keyboard — the real ones render on top on a real device.

## What was deliberately left out

- **The learning path screen (S5).** The rail on `Main` and the entry card on `Home` carry the concept; a dedicated screen is a layout problem, not a design-direction problem, and can be drafted when it's built.
- **Not-found (S9).** Reuses the `Search` no-results treatment.
- **Light mode.** Specified in tokens, undrawn. The semantic green/red/amber trio is the part most likely to break on a light ground and deserves one artboard before implementation.
- **Progress tracking beyond the counter.** Passive by design — nothing prompts, nothing gates, no account. It should be invisible until used, which means there is very little to draw.
- **Any hover state on the mockups.** They're in `design-system.md` as specs rather than drawn, because drawing them costs an artboard each and buys nothing a table doesn't.
