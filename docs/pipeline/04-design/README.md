# Design — Java Data Structures Tutorial

_Stage 4 of 5 · 2026-08-25 · Inputs: `docs/pipeline/03-user-journey.md`_

## Canvas

**https://claude.ai/code/artifact/d96bfa26-9dc2-4ffd-8885-6db45ce29ca4**

Eight artboards across two pages. Artboard sources are checked in beside this file as `*.dc.html` with `canvas.json` for layout, so the canvas can be rebuilt or re-seeded without the hosted page.

### Page 1 — Screens

| Artboard | Screen | Journey steps served |
|---|---|---|
| `Main` | **Structure page, full tier** — Circular Array Queue (DS-015), the core surface, top to bottom | A2–A5, B2, C1–C3 · S1 |
| `Home` | Landing with live coverage stats, resume card, and three entry points | B1, A5-return · S3 |
| `Catalog` | Browse and filter all 129, filtered to Linear + Beginner | B1 · S4 |
| `Search` | Command-palette overlay in its **no-results** state | C1, B1 · S7 |
| `Compare` | ArrayStack vs ListStack vs LinkedStack, merged complexity + "pick this one when…" | B3 · S8 |
| `Explained` | **Explanation-only tier** — Link-Cut Tree (DS-059) with the honesty banner | B4 · S2 |
| `Mobile` | The structure page at 390 px: drawer nav, chip section-jump, scroll-in-place table and code | A1–A4 on phone · S10 |

### Page 2 — Design system

| Artboard | Contents |
|---|---|
| `System` | Surfaces and ink with measured contrast ratios, type scale, difficulty and status chips, component states, spacing/radii/motion |

Screens S5 (learning path) and S9 (not found) from the journey inventory are **not** drafted. The path rail appears in context on `Main` and the entry card on `Home`, and not-found reuses the `Search` no-results treatment — both are cheap to draft later if the layouts prove non-obvious.

## Design direction

Dark-first, dense, and quiet — a reference tool that happens to teach, rather than a course that happens to have code. The palette is the existing app's: `#0f111a` ground, `#e2e8f0` ink, `#3b82f6` accent, all lifted unchanged from `tutorial-app/src/App.css` so this reads as the same product maturing rather than a replacement.

Three things are deliberately new. A **semantic colour layer** — green for *use when*, red for *avoid when*, amber for *caution and invariants* — because "when not to use this" is a stated product goal and it should be visible from across the room, not a paragraph among paragraphs. A **real type system** built on Space Grotesk for headings, IBM Plex Sans for reading, and JetBrains Mono for code: three families with character, none of them Inter, and a code face doing the developer-signalling so the reading face can just be readable. And the removal of the glassmorphism and gradient-filled headings the current app uses — at 129 pages those cost legibility every time and buy atmosphere once.

Static mockups, not a clickable prototype. The brief named a set of screens, so these are drawn to be argued with rather than clicked through.

## Files

| File | Contents |
|---|---|
| `design-system.md` | Tokens, type scale, spacing, components, states, accessibility |
| `design-rationale.md` | Why each screen is shaped the way it is, traced to journey steps |
| `*.dc.html`, `canvas.json` | Artboard sources and canvas layout |

## Open design questions

1. **The section rail on `Main` is 186 px of right-hand column.** It serves Maya's jump-to-answer directly. At 1280 px viewports it is the first thing that should collapse — into what? A sticky horizontal bar under the header is the obvious answer but competes with the breadcrumb.
2. **The visualization is drawn as a ring for `ArrayQueue`.** A ring is right for a circular buffer and wrong for a linked list. The generic op-log renderer (FR-15) needs at least three shapes — ring, chain, tree — decided before phase 1 code starts.
3. **"Rebuild it yourself" is drafted as a static checklist.** Should ticks persist per browser like read-state, or stay ephemeral? Persisting is more useful and adds a second thing that can go stale.
4. **Light mode is specified in the system doc but not drawn.** Worth one artboard before implementation, since the semantic green/red/amber trio is the part most likely to break on a light ground.

---

_Next: stage 5 — Claude Code handoff. Run the `code-handoff` skill._
