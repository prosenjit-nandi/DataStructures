# Design System

_Stage 4 of 5 · Companion to the canvas. Values here are authoritative — the implementer should never need to open the canvas to find a number._

## 1. Color tokens

Names are **roles**, not appearances, so a future rebrand touches this file and nothing else. Dark is the primary theme; light is specified and not yet drawn.

### Dark (default)

```css
:root {
  /* surfaces — carried unchanged from tutorial-app/src/App.css */
  --bg:         #0f111a;   /* page ground            (was --bg-dark)    */
  --surface:    #161926;   /* panels, cards                              */
  --surface-2:  #1a1d2d;   /* raised: overlays, modals (was --bg-panel) */
  --rail:       #131522;   /* top bar, side rail     (was --bg-sidebar) */
  --code-bg:    #12141f;   /* code blocks                                */

  /* lines */
  --line:       rgba(255,255,255,.08);   /* was --border-color */
  --line-2:     rgba(255,255,255,.14);   /* control borders    */

  /* ink */
  --ink-hi:     #f4f7fb;   /* headings                    16.9:1 */
  --ink:        #e2e8f0;   /* body                        14.4:1  (was --text-primary) */
  --ink-mut:    #94a3b8;   /* secondary                    6.8:1  (was --text-secondary) */
  --ink-faint:  #64748b;   /* labels ≥14px only            4.1:1 */

  /* accent */
  --accent:     #3b82f6;
  --accent-hi:  #60a5fa;   /* links, hover ink */
  --accent-bg:  rgba(59,130,246,.12);
  --accent-line:rgba(59,130,246,.35);

  /* semantic — new in this system */
  --use:        #34d399;  --use-bg:   rgba(52,211,153,.09);  --use-line:   rgba(52,211,153,.28);
  --avoid:      #f87171;  --avoid-bg: rgba(248,113,113,.09); --avoid-line: rgba(248,113,113,.30);
  --warn:       #fbbf24;  --warn-bg:  rgba(251,191,36,.09);  --warn-line:  rgba(251,191,36,.28);
}
```

### Light

```css
:root[data-theme="light"] {
  --bg:#ffffff; --surface:#f8fafc; --surface-2:#ffffff; --rail:#f1f5f9; --code-bg:#f8fafc;
  --line:rgba(15,23,42,.10); --line-2:rgba(15,23,42,.18);
  --ink-hi:#0f172a; --ink:#1e293b; --ink-mut:#475569; --ink-faint:#64748b;
  --accent:#2563eb; --accent-hi:#1d4ed8; --accent-bg:rgba(37,99,235,.08); --accent-line:rgba(37,99,235,.30);
  --use:#047857;   --use-bg:rgba(4,120,87,.07);    --use-line:rgba(4,120,87,.28);
  --avoid:#b91c1c; --avoid-bg:rgba(185,28,28,.06); --avoid-line:rgba(185,28,28,.26);
  --warn:#b45309;  --warn-bg:rgba(180,83,9,.07);   --warn-line:rgba(180,83,9,.26);
}
```

The semantic hues change substantially between themes — `#34d399` is 9.9:1 on the dark ground and only 1.9:1 on white, so the light values are the darkened equivalents rather than the same hex. **Define every token on bare `:root` and redefine only what changes under `[data-theme]`**, never the reverse.

### Difficulty and status

| Meaning | Token | Also stated as |
|---|---|---|
| Beginner | `--use` | the word "Beginner" |
| Intermediate | `--accent-hi` | the word |
| Advanced | `--warn` | the word |
| Expert | `--avoid` | the word |
| Built | `--use` | ✓ Built |
| Planned | `--ink-faint` | Planned |
| Explanation only | `--warn` | Explanation only + shield icon |

Colour is never the only channel — every chip carries its word. Difficulty and status use overlapping hues, which is safe because they never appear in the same slot.

## 2. Type

```css
--disp: 'Space Grotesk', 'IBM Plex Sans', ui-sans-serif, system-ui, sans-serif;
--sans: 'IBM Plex Sans', ui-sans-serif, system-ui, 'Segoe UI', sans-serif;
--mono: 'JetBrains Mono', ui-monospace, 'SF Mono', Consolas, monospace;
```

Loaded from Google Fonts with `display=swap`. Every stack has a real system fallback; nothing depends on the webfont arriving.

| Step | Family | Size / line-height | Weight | Tracking | Used for |
|---|---|---|---|---|---|
| Display | disp | 52 / 1.06 | 600 | −.02em | Home hero only |
| H1 | disp | 38 / 1.15 | 600 | −.02em | Structure page title |
| H1-sm | disp | 32 / 1.15 | 600 | −.02em | Catalog, compare titles |
| H2 | disp | 21 / 1.15 | 600 | −.01em | Section headings |
| H3 | disp | 16–17 / 1.2 | 600 | 0 | Card titles |
| Lead | sans | 17.5 / 1.6 | 400 | 0 | The one-sentence description |
| Body | sans | 16 / 1.65 | 400 | 0 | Prose |
| Small | sans | 14 / 1.55 | 400 | 0 | Cards, table cells |
| XS | sans | 12.5 / 1.45 | 400 | 0 | Meta, captions |
| Eyebrow | sans | 11.5 / 1 | 600 | .11em, caps | Section labels |
| Code | mono | 13 / 1.75 | 400 | 0 | Code blocks |
| Code inline | mono | 12.5 | 400 | 0 | Inline identifiers |

Mobile: H1 → 27/1.12, lead → 15.5, code → 12. Everything else holds.

## 3. Spacing, radii, motion

- **Spacing**: 4px base — 4, 8, 12, 16, 24, 32, 48, 64. Page gutters 44px desktop / 16px mobile. Section gap 26px. Panel padding 22px 24px.
- **Radii**: 4 chips · 6 buttons and inputs · 10 cards and code blocks · 14 panels and overlays.
- **Shadow**: one only, on overlays — `0 24px 60px rgba(0,0,0,.55)`. Cards and panels use borders, not shadows.
- **Motion**: `cubic-bezier(.4,0,.2,1)` at 180ms, carried from the existing app. Nothing autoplays. Everything inside `@media (prefers-reduced-motion: reduce)` drops to no transition, and the visualization renders its final frame with step controls still operable.

## 4. Components

### Button

36px tall (46px on mobile), radius 6, 13.5px/500 label, 15px horizontal padding, 7px icon gap.

| State | Primary | Secondary |
|---|---|---|
| Default | bg `--accent`, ink `#fff` | transparent, border `--line-2`, ink `--ink` |
| Hover | bg `#2563eb` | bg `rgba(255,255,255,.05)` |
| Focus | 2px `#93c5fd` ring, 2px offset | same |
| Active | translateY(1px) | same |
| Disabled | opacity .42, no pointer events | same |

Small variant: 29px, 12.5px label, 11px padding.

### Chip

22px tall, radius 4, 11.5px/500, 9px padding, 1px border. Variants: neutral (`--line-2` / `--ink-mut`), `.id` (mono, `--ink-faint`), and the four semantic tints, each `color` + `border-color` + `background` from its token trio.

### Panel

`--surface` on `--line`, radius 14, padding 22/24. **Semantic panels** — use, avoid, caution — take their `--*-line` border plus a `linear-gradient(180deg, var(--*-bg), transparent 60%)` wash over the surface, so the colour reads at a glance without tinting the text area.

Header: 17px icon in the semantic colour, 9px gap, H2 heading in the light tint (`#6ee7b7` / `#fca5a5` / `#fcd34d`), 14px bottom margin.

### Rail item

32px tall, radius 6, 9px gap, 13.5px label, `--ink-mut`. Hover: `rgba(255,255,255,.05)` + `--ink`. Active: `--accent-bg` + `--ink-hi` + `inset 2px 0 0 var(--accent)`. A 6px status dot leads: green built, blue current, `#2e3444` not yet.

### Table

Header cells: eyebrow type, `--ink-faint`, 9px bottom padding, 1px `--line` rule. Body cells: 11px vertical padding, 14px, `--line` rule, top-aligned. Operation column mono `--ink-hi` 13px; complexity columns mono `--accent-hi` 13px; the plain-language column `--ink-mut`. Last row drops its rule.

**Every table sits inside `overflow-x: auto`.** The page body never scrolls horizontally.

### Code block

`--code-bg` on `--line`, radius 10, clipped. Bar: 8/10 padding, tabs at 12.5px mono (active = `rgba(255,255,255,.06)` + `--ink-hi`), copy and GitHub buttons right-aligned. Gutter: 40px, right-padded 14px, `#3d465c`, `user-select: none` so copy excludes it. Highlighted lines: `rgba(59,130,246,.10)` + `inset 2px 0 0 var(--accent)`.

Syntax: comment `#5a6478` italic · keyword `#c4a2f5` · type `#6fc3df` · method `#82aaff` · number `#f78c6c` · string `#c3e88d` · punctuation `#7d8799` · plain `#cdd6e4`.

### Search overlay

660px wide, 112px from top, `--surface-2` on `--line-2`, radius 14, the one shadow. Scrim `rgba(9,10,15,.62)` over a 2px blur. Result rows: 11/12 padding, radius 6; selected row gets `--accent-bg` + `inset 2px 0 0 var(--accent)`. Footer strip carries the keyboard legend.

### Section rail (right column)

186px, sticky at top 0. Items 12px left-padded with `inset 1px 0 0 var(--line)`; the active item swaps to its section's semantic colour and a 2px bar — which is why "When not to use it" reads red in the rail.

## 5. Breakpoints

| Width | Behaviour |
|---|---|
| ≥1440 | Left rail 252 + article + 186 section rail |
| 1120–1439 | Section rail collapses (see open question 1) |
| 768–1119 | Left rail becomes a drawer; article full width |
| 320–767 | Mobile artboard: 54px sticky header, 16px gutters, chip section-jump, sticky bottom action bar |

## 6. Accessibility

- **Contrast, measured against `--bg #0f111a`**: `--ink-hi` 16.9:1 · `--ink` 14.4:1 · `--ink-mut` 6.8:1 · `--use` 9.9:1 · `--warn` 11.4:1 · `--avoid` 7.3:1 · `--accent-hi` 7.0:1. `--ink-faint` is 4.1:1 and is therefore restricted to non-essential text at 14px or larger, where the AA large-text threshold of 3:1 applies. `--accent #3b82f6` on `--bg` is 4.3:1 — it is used as a **background** behind white and as a decorative bar, never as text on the page ground; link text uses `--accent-hi`.
- **Focus** is a 2px `#93c5fd` ring at 2px offset on every interactive element. `outline: none` without a replacement is a build error, not a style choice.
- **Targets**: 44px minimum on mobile; 36px is desktop-pointer only. The chip section-jump row is 44px tall on mobile including padding even though the chips are 22px.
- **The visualization** pairs every step with a text caption in the DOM, the step control is a real button, and the whole component is operable by keyboard. It is never the only way a fact is stated.
- **Colour is never load-bearing alone** — status, difficulty, and use/avoid all carry a word and an icon.
- **Reflow**: everything works at 320px and at 400% zoom; wide content scrolls inside its own container.

---

_Values in this file are the contract for stage 5. If an implementation needs a number that isn't here, that's a gap in this document — add it here rather than inventing it in CSS._
