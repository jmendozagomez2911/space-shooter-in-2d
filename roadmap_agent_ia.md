# 📈 AI Coding Workflow (Windsurf Example) — README

Welcome! 👋  
This README captures the **main idea** from the video transcripts and turns it into a **clear, teachable workflow** you can reuse for AI-assisted development.

## 🧠 Main Idea (the one thing to remember)
**AI coding tools accelerate implementation, but you still own the engineering process**:  
✅ define requirements → ✅ break into steps → ✅ verify continuously → ✅ version everything → ✅ refactor safely → ✅ deploy responsibly.

> 🧑‍🏫 Professor note: The AI is not “wrong” — it’s **underspecified**. Most failures come from *you not defining the target precisely enough*.

---

## 🎯 What you’re building (teaching project)
A small web app that:

- 📂 lets the user upload a **CSV** with **USD/EUR exchange rate**
- 🧹 parses the file, **drops missing values**
- 📉 plots a **line chart**
  - X-axis: **date**
  - Y-axis: **exchange rate**
- 🧩 includes UX features (time range selector, tooltip + crosshair, legend, labels)

This is deliberately “simple but real” so you learn a workflow that scales beyond the demo. 💡

---

## 🚫 Why a “one-sentence prompt” rarely works
When you say “build me an app” in one line, you’re implicitly asking the model to guess:

- file format details,
- UI behaviours,
- default states (what shows on first load?),
- edge cases (missing values, invalid dates, empty files),
- library choices,
- architecture decisions.

⚠️ The model fills gaps with assumptions. Sometimes lucky. Often wrong.  
So the course pushes **front-loading clarity**.

✅ **Rule:** If the app has UI + data + interactions, your prompt needs structure, not vibes.

---

# ✅ The Workflow (the way to get reliable results)

## 🧾 Step 1 — Write a Specification (PRD-style)
Think of this as your **single source of truth** 🧭  
Not long for the sake of length — detailed for the sake of **zero ambiguity**.

### ✅ What to include (practical checklist)
**A) Product behaviour**
- What problem it solves (1 paragraph)
- Who uses it and why
- What the app must do (bullets)

**B) Data contract**
- Expected CSV columns (names, types, examples)
- Date format rules
- Missing value policy
- What happens if parsing fails

**C) UX requirements**
- What appears on initial load
- Interactions (hover tooltip, crosshair, selector)
- Error messages (bad file, missing column, etc.)

**D) Non-functional requirements**
- Performance target (e.g., “CSV up to 50k rows”)
- Basic accessibility (keyboard nav if relevant)
- Browser support (Chrome-only vs broad)

### 🧠 The best trick used in the transcripts
Add this line to your spec prompt:

> **“Ask follow-up questions before generating anything.”** ❓

Why it matters:
- It surfaces missing decisions early
- It prevents “silent assumptions”

💬 **Professor note:** Voice dictation is fine, but always **review** the generated text—transcription errors are common.

---

## 🧾 Example: what a good spec *actually* looks like (copy/paste)
Below is the kind of spec text you want the AI to produce. This is the “example of the text” (PRD-style) you asked for ✅

### `docs/spec.md`
```md
# USD/EUR Exchange Rate — Web App Specification

## 1. Goal
Build a small web app that lets a user upload a CSV file containing USD/EUR exchange rates and displays an interactive line chart.

## 2. Users & Use Case
- User uploads a CSV of historical rates.
- The app cleans the data  (drops missing values).
- The app renders a line chart and allows quick time-range filtering.

## 3. Inputs (CSV Contract)
### Required columns
- `date` (string): ISO format preferred (YYYY-MM-DD)
- `rate` (number): USD per 1 EUR (e.g., 1.0842)

### Cleaning rules
- Drop rows where `date` or `rate` is missing.
- Parse dates; if date parsing fails for any row, drop that row and report how many were dropped.
- Sort by date ascending after cleaning.

### Example
date,rate
2023-01-01,1.0701
2023-01-02,1.0652

## 4. UI & UX Requirements
### Default state
- On first load, show:
  - App title: “USD/EUR Exchange Rate”
  - A short instruction: “Upload a CSV to view the chart”
  - A placeholder chart area (empty state)

### Chart requirements
- Line chart
- X-axis label: Date
- Y-axis label: USD per EUR
- Legend visible (series name: “USD/EUR”)

### Interaction
- Time range selector with buttons:
  - 1Y, 5Y, 10Y, Max
- Hover tooltip:
  - shows date + exact rate
  - includes a vertical crosshair aligned with cursor

### Errors
- If CSV missing required columns: show a clear error and do not render chart.
- If CSV is empty after cleaning: show “No valid data to display.”

## 5. Technical Constraints
- Node.js project
- Choose a charting library suitable for interactive tooltips and time-series charts.

## 6. Acceptance Criteria (MVP)
- Uploading the provided sample CSV renders a chart.
- Hover shows date + rate with a vertical crosshair.
- Range selector filters data correctly.
- Missing/invalid data results in clear user-facing errors.
````

---

## ✅ Step 2 — Turn the spec into a TODO list (with acceptance criteria)

The TODO list is your execution plan 📌
It stops the AI from trying to “build everything” in one chaotic jump.

### What a good TODO item looks like

Each task should be:

* small (1 feature, not 5)
* verifiable
* paired with acceptance criteria ✅

Example:

* [ ] Implement CSV upload UI
  **Acceptance:** user can select a `.csv`; invalid types show an error.

---

## 🧾 Example: what a good TODO file looks like (copy/paste)

This is the second “example of the text” you requested ✅

### `todo.md`

```md
# ✅ TODO — USD/EUR Exchange Rate App

## Phase 1 — Project foundation
- [ ] Initialise Node.js project
  - ✅ Acceptance: `npm install` works; `npm run dev` starts server.

- [ ] Create basic UI layout (title + upload area + chart placeholder)
  - ✅ Acceptance: App loads with title and upload control visible.

## Phase 2 — Data ingestion
- [ ] Parse CSV into rows with {date, rate}
  - ✅ Acceptance: Valid sample CSV parses into non-empty dataset.

- [ ] Clean data (drop missing values, parse dates, sort)
  - ✅ Acceptance: Rows with missing/invalid date or rate are removed and a count is available.

## Phase 3 — Chart
- [ ] Render line chart (date on X, rate on Y)
  - ✅ Acceptance: Chart renders after upload with correct axes.

- [ ] Tooltip + vertical crosshair
  - ✅ Acceptance: Hover shows correct date + rate and crosshair.

- [ ] Time range selector (1Y, 5Y, 10Y, Max)
  - ✅ Acceptance: Selecting a range filters visible datapoints correctly.

## Phase 4 — Quality + polish
- [ ] Error handling for missing columns / empty cleaned dataset
  - ✅ Acceptance: Clear error messages; app does not crash.

- [ ] Add README basics
  - ✅ Acceptance: New dev can run the app following README.

## Phase 5 — Deployment (preview)
- [ ] Deploy to Netlify preview
  - ✅ Acceptance: URL loads and basic flow works (upload → chart).
```

---

## 🧼 Step 3 — Split planning vs building (use a fresh chat)

The transcripts show a very practical habit:

* **Chat #1:** planning (spec + TODO)
* **Chat #2:** implementation (feature work)

🎯 Why it works:

* planning creates lots of context noise
* implementation works better with **only the relevant files referenced**

✅ **Rule:** when building, attach/reference `spec.md` + `todo.md` in the new chat.

---

## 🔁 Step 4 — Work in small iterations (and verify after each one)

This is the core discipline.

### A good iteration loop

1. implement one task
2. run the app (or tests)
3. check acceptance criteria
4. commit the change
5. move to next task

✅ **You don’t trust completion messages** — you trust verified behaviour.

👀 Common reality from the transcript:

* the AI reports “phase complete”
* but the UI is broken or missing features

So you become the “eyes” of the process 👁️:

* describe what you see
* point to what’s missing
* be concrete

🚫 Bad feedback: “It doesn’t work.”
✅ Good feedback:

> “On initial load I see no chart. After uploading sample CSV, I see a ‘processed’ message but the chart remains blank. The 1Y/5Y/10Y/Max buttons do nothing.”

This reduces back-and-forth dramatically ⚡

---

## 🧰 Step 5 — Use Git (real versioning, not just accept/reject)

The tool’s accept/reject UI is helpful… but not a substitute for Git.

### Why Git is essential

* revert quickly when AI breaks something 🧯
* compare working vs broken versions 🔍
* isolate changes into meaningful commits 🧱

### ✅ Minimal workflow

* `main` stays stable
* use `feature/<name>` branches
* commit per verified iteration

### `.gitignore` matters more than usual

AI tools generate:

* test artifacts,
* screenshots,
* downloaded browsers,
* build outputs.

⚠️ Without `.gitignore`, your repo becomes noise.

🧑‍🏫 Professor tip: Keep the ignore file **small and specific** to *your project*, not a generic mega-template.

---

## 🧪 Step 6 — Testing (what the transcript *really* teaches)

The transcript demonstrates a key lesson via failure.

### What NOT to do 🚫

“Generate all E2E tests with Playwright” in one go.

Why it goes wrong:

* tests don’t match real behaviour
* tests aren’t executed
* AI “fixes” tests by modifying production code (bad)
* you end up debugging 50+ failures

### Better approach ✅

1. Write **one** E2E test
2. Run it
3. Fix until stable
4. Add the next test

Include this constraint in your prompt:

> **“Do not modify production code to make tests pass.”** 🛑

Because true E2E tests validate **user-visible behaviour**, not internal structure.

💡 Why E2E was attractive here:
The app started as one large JS file, so unit testing was awkward at first. E2E provides fast coverage until the architecture improves.

---

## 🧱 Step 7 — Refactor when structure becomes a bottleneck

As features grow, a single large file becomes:

* hard to test,
* easy to break,
* painful to maintain.

Refactoring into React/components is sensible 🧩

### What makes refactoring risky

It touches many files, so break risk rises.

✅ Best practice implied:

* refactor with a strong reasoning model
* verify behaviour after refactor
* keep changes reviewable

💬 Professor note: “Refactor this” is too vague.
Better:

> “Refactor into components: Chart, Controls, FileUpload, DataParser. Keep UI behaviour identical.”

---

## 🛑 Troubleshooting tip (stuck commands)

Sometimes a command runs forever (dev server) or appears stuck.

Two recoveries:

* stop the command directly 🛑
* or hit the square stop button and type **“continue”** to resume correctly

🧯 If nothing changes for ~1 minute, intervene.

---

## 🚀 Deployment (fast preview, not production)

Deployment via Netlify integration can be:

* one prompt
* one click
* instant URL 🌐

⚠️ It’s positioned as **preview deployment**, not production.

### Important correction (when it matters)

If you handle sensitive data:

* deploy under your own account/domain
* manage secrets properly
* apply access controls + security review

Preview deployments are great for demos, not regulated environments.

---

## 🧑‍🏫 Prompt templates you can reuse (compact + effective)

### 1) Spec prompt

> “Write a PRD-style spec for this app. Ask follow-up questions first. Output Markdown as `docs/spec.md`.”

### 2) TODO prompt

> “Convert `docs/spec.md` into a checkbox TODO list with acceptance criteria per item. Output `todo.md`.”

### 3) Build prompt

> “Implement Phase 1 only. Mark TODO items done **only after** acceptance criteria are met.”

### 4) Debug prompt

> “Here’s what I observe: (bullets). Propose the smallest fix. Don’t introduce unrelated changes.”

### 5) Testing prompt

> “Write ONE Playwright E2E test for the current behaviour. Run it. Fix until it passes. Don’t modify production code.”

---

## ✅ The mindset you’re meant to learn

If you want AI coding tools to feel “magical”, the secret is discipline ✨:

* **clarity beats conversation**
* **small steps beat big rewrites**
* **verification beats optimism**
* **Git beats regret**
* **tests beat manual re-checking**
* **refactoring beats accumulated mess**

🎓 That’s the real lesson of the section.

```
```
