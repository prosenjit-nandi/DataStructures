# Claude Code Handoff — Java Data Structures Tutorial

_Stage 5 of 5 · 2026-08-26 · Inputs: stages 1–4_

## Start here

```bash
git clone https://github.com/prosenjit-nandi/DataStructures.git
cd DataStructures
./gradlew check                              # confirm the baseline is green
cd tutorial-app && npm ci && npm run dev     # site at localhost:5173
```

Then open the repo in Claude Code and paste this:

> Read CLAUDE.md and docs/pipeline/05-handoff/backlog.md. Start with `T1-00` — repackage and rename the seven existing structures into `datastructure.{category}` per the mapping in `implementation-spec.md` §2, move their tests to mirror, and rename `Stack.get(T)` to `find(T)`. Run `./gradlew check` and confirm coverage is still 100% line and branch before you finish. Don't touch anything under `tutorial-app/src/`.

`T1-00` first is deliberate. It is the cheapest task that touches every existing file, so it surfaces anything the audit got wrong before real work depends on it.

## What's in this pack

| File | Use it when |
|---|---|
| `../../../CLAUDE.md` | Always loaded by Claude Code. Layout, commands, conventions, and the things not to do |
| `implementation-spec.md` | You need to know *how* — architecture, the content pipeline, routing, components, CI |
| `catalog.json` | You need the authoritative list: all 129 structures with id, slug, category, difficulty, phase, tier, status, target class, and prerequisites. Machine-readable; query it rather than reading prose |
| `authoring-contract.md` | You are adding a structure. §6 is a complete worked specimen — pattern-match on it |
| `backlog.md` | You need to know what to do next. Dependency-ordered, 143 tasks across five phases |
| `prompts.md` | You want a prompt that already knows the file paths and the contract |

Upstream reasoning, when a decision here looks arbitrary: `01-discovery.md` (audit and the full domain map) · `02-prd.md` (numbered requirements with acceptance criteria) · `03-user-journey.md` (who this is for and where they give up) · `04-design/` (canvas, tokens, rationale).

## Phase plan at a glance

| Phase | Theme | Structures | New tasks | Tier | Exit criteria |
|---|---|---:|---:|---|---|
| **1** | Foundations & platform | 24 | 30 | full | The pipeline is proven end to end: extended schema, validation gate, routes, page template, visualization, and DS-015 fully realized |
| **2** | Interview core | 25 | 27 | full | Every interview-relevant structure has a complete page with complexity and visualization |
| **3** | Advanced & applied | 36 | 39 | standard | Comparison views live; all applied structures implemented and tested |
| **4** | Specialized | 29 | 31 | standard / explained | The specialized set is documented; the explanation-only tier is formally supported |
| **5** | Long tail | 15 | 16 | explained | Catalog complete — all 129 have a page at their declared tier |

Only phase 1 is a release. Phases 2–5 ship continuously.

## Traceability

Everything is joined on the `DS-` ids assigned in discovery. They never change.

```
discovery §3        DS-015 Circular Array Queue, linear, beginner, phase 1
      │
PRD §5.2            → phase 1, tier full
PRD §6              → FR-1 tags · FR-7 use/avoid · FR-13 section order · FR-14 complexity
      │
journey §2, §4      → A2 complexity gloss · A3 visualization · C2 avoid-when placement
                    → M2 generics explainer · M3 runnable example · M4 helper types
      │
design              → Main.dc.html artboard · design-system.md tokens
      │
handoff             → catalog.json DS-015 · authoring-contract.md §6 · backlog T1-09
```

Given any task, you can walk back to the requirement that justifies it and the journey step that produced the requirement. If you can't, something was invented that shouldn't have been — say so rather than building it.

## The two things most likely to go wrong

**Content volume.** 129 pages of good prose is a book, and this is the risk that actually kills projects like this one. The mitigations are all in place: tiered depth so the long tail is allowed to be short, phases that ship independently, a scaffolding command, and an explanation-only tier that is an honest finish line rather than an admission of failure. Use them. A phase-4 structure that has fought you for two days should be dropped to `explained`, not pushed through.

**Coverage on hard structures.** 100% branch coverage on a red-black tree or a B+ tree is real work. Do not weaken the gate to land one — it is the strongest quality signal the project has, and it pressures implementations toward the simplicity a teaching repository wants anyway. Drop the structure a tier instead.
