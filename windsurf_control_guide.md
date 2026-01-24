# 🧭 Windsurf Control Guide — README (Models, Rules, Context, and Staying Sane)

This README captures the **main idea** from the transcript and turns it into a **practical playbook** for controlling Windsurf when it behaves unpredictably.

## 🧠 Main Idea (one thing to remember)
**You control consistency through three levers:**  
1) ✅ **Model choice** (quality vs cost vs speed)  
2) ✅ **Rules** (encode preferences so you don’t repeat yourself)  
3) ✅ **Context management** (give precise references, avoid bloated chats)

> 🧑‍🏫 Professor note: Most “Windsurf randomness” is actually *input/context randomness*: unclear instructions, wrong model for the task, or too much stale context.

---

# 1) 🎛️ Model Selection (the biggest quality lever)

## Why model choice matters
Windsurf may behave differently depending on:
- the model you selected,
- whether you’re in **Code (Agent)** vs **Chat** mode,
- how clear your instructions are.

In practice: **same prompt + different model = different code quality**.

---

## Two broad model types (as explained)
### A) “Standard GPT-style” models
- Faster
- Usually cheaper
- Best when the task is **clear** and mostly execution

### B) “Reasoning / Thinking” models 🧠
- Spend more time before answering
- Often higher quality on planning + hard problems
- Best for:
  - architecture decisions
  - debugging tricky issues
  - refactoring plans
  - ambiguous requirements

> ✅ Use reasoning models to *decide*; use cheaper models to *execute* once the plan is clear.

---

## Credit cost as a rough proxy for power 💳
The transcript hints at a practical heuristic:
- Higher credit cost → often more capable
- Zero/very low cost → often weaker

⚠️ Not a law, but a **useful default assumption**.

---

## “Bring Your Own Key” (BYOK) warning ⚠️
Windsurf may let you plug an API key (BYOK).  
The instructor advises caution because:
- you get billed directly,
- you may lose cost predictability/control.

🧑‍🏫 Professor note: If you’re learning or experimenting, avoid BYOK until you have strict cost monitoring.

---

## Using leaderboards (practical but not perfect)
The transcript suggests using **coding leaderboards** as a way to shortlist good models.

Important nuance:
- leaderboards often compare model families, not always the exact variant inside Windsurf,
- names may look similar (e.g., “fast” variants may differ).

✅ Use leaderboards for *direction*, then validate by trying models on your own tasks.

---

## ✅ Example: a simple “model selection matrix” (copy/paste)
This is the “example text” you requested: something you would actually write and keep in your repo.

```md
# Model Selection Matrix (Windsurf)

## Default rules
- Planning / architecture / refactor plan → use a Reasoning/Thinking model 🧠
- Implementation of clear tasks → use a regular (cheaper/faster) model ⚙️
- Troubleshooting unclear bugs → Reasoning model + Chat mode first 🔎

## Examples
- “Design the component structure” → Reasoning
- “Implement CSV parsing function from spec” → Regular
- “Why does npm start fail?” → Reasoning + Chat mode
````

---

# 2) 🧠 Chat Mode vs Code (Agent) Mode

## Chat mode = safe thinking space

Use Chat mode for:

* brainstorming
* planning
* troubleshooting
* “explain why” questions

✅ It won’t modify your codebase.

## Code (Agent) mode = makes changes

Use Code mode when you’re ready to:

* create files
* modify code
* run commands
* implement features

🧑‍🏫 Professor rule:
**If you want an explanation, use Chat mode. If you want changes, use Code mode.**
This prevents accidental “AI starts editing my repo” moments. 😅

---

# 3) 📏 Cascade Rules (make behaviour consistent)

## What rules are for

If the AI keeps doing something you dislike and you don’t want to repeat yourself:
✅ write a rule once → reuse forever.

Rules let you encode preferences like:

* coding style
* architecture patterns
* commenting conventions
* file structure expectations
* testing discipline

---

## Global vs Workspace rules (the instructor’s preference)

### Workspace rules ✅ (recommended)

* apply only to the current project
* reduce unwanted cross-project effects
* easy to copy between projects if needed

### Global rules ⚠️ (use carefully)

The instructor dislikes global rules because:

* you may forget they exist,
* they can cause “mysterious” behaviour later,
* they may conflict with project-specific needs.

🧑‍🏫 Professor note: Global rules create “hidden state”. Hidden state = debugging pain.

---

## Rule activation modes (important detail)

Windsurf rules can be activated in different ways:

* **Manual** (trigger it when needed)
* **Always on** ✅ (most useful for project conventions)
* **Model decision** (AI decides) ⚠️ less predictable
* **Glob** (apply to matching files) (advanced)

Instructor preference:
✅ Always-on rules for core standards
✅ Manual rules for occasional workflows
⚠️ Avoid “model decides” unless you enjoy surprises.

---

## ✅ Example: a good Workspace Rules file (copy/paste)

This is another “example text” you can actually store in your repo.

```md
# .windsurf/rules/project.md  (Always On)

## Coding and change rules
1. Make the smallest change that satisfies the acceptance criteria.
2. Do not modify unrelated files.
3. If adding tests, run them and report results.
4. Never change production code to make a test pass unless explicitly requested.

## Structure
5. Keep modules small; avoid large single-file implementations.
6. Prefer extracting helpers into /src/utils or /src/services.

## Communication
7. When unsure, ask 1–3 clarifying questions before coding.
8. Summarise what you changed and why after each task.
```

🧑‍🏫 Professor note: Don’t paste huge “rule templates” blindly. Too much text = weaker compliance.

---

# 4) 🧩 Context Management (how to get better output fast)

## Why context is everything

The transcript makes a crucial point:
**Code quality depends heavily on context.**

Even strong models fail when context is missing or vague.

---

## Ways to provide context efficiently (the useful mechanics)

### A) Reference files/folders directly

* attach a file
* drag & drop a folder
* tell the agent “change only files in /src/components”

✅ This reduces “AI edits random places” behaviour.

### B) Reference exact code lines

Select code → “send to chat” → ask:

* “Explain why we need this”
* “Is this correct?”
* “Refactor just this block”

✅ Best for precision and avoiding long copy/pastes.

### C) Reference terminal errors directly 🧯

If a command fails:

* select the error output
* send to chat
* ask “why is this happening?”

✅ This gives the AI the exact error text without manual formatting mistakes.

---

## ✅ Example: “high-precision prompts” (copy/paste)

```md
# Prompt Examples (Precise Context)

## Explain a line of code
"Explain the selected line. What does it do and what breaks if I remove it?"

## Fix a terminal error
"Here is the terminal error (selected). Provide the minimal fix and the command I should run to verify."

## Restrict scope
"Only modify files under /src/components. Do not change build scripts."
```

---

# 5) 🌐 Handling outdated knowledge (web + docs)

## The problem

LLMs can be outdated, especially for:

* newly released libraries
* new API functions
* changed CLI commands

This causes:

* wrong code
* refusal to use features the model “doesn’t know”
* broken setup instructions

---

## Two built-in ways to get up-to-date info (as described)

### A) “add web” 🌐

Ask Windsurf to use the internet for current information.

### B) “docs” 📚

Use provider docs integration for up-to-date documentation.

🧑‍🏫 Professor note: If something smells like “version mismatch”, pull docs into context early.

---

# 6) 🖥️ Preview vs Windsurf Browser (UI debugging superpower)

## IDE Preview

* quick to open
* useful for basic checks
* lets you **select UI elements** and send them to chat

Example use:

* click “send element”
* select a button
* prompt: “Rename this button to ‘Select file’”

✅ This is “UI-context prompting”.

## Windsurf Browser (more advanced)

* full devtools
* can send:

    * selected DOM elements
    * console logs
    * screenshots

✅ For serious UI issues, use the browser mode.

---

# 7) 🧠 Long chats degrade performance (start fresh)

A key point from the transcript:
Long conversations can reduce model performance because:

* context becomes bloated,
* the model forgets,
* it repeats mistakes,
* output becomes inconsistent.

✅ Solution: start a new chat when quality drops.

---

# 8) 🧠 Memories vs Rules vs Project Docs (what to rely on)

## Memories (auto-generated)

* help carry basic context across chats
* limited detail
* can be manually edited
* can be created by telling Windsurf “remember X”

Useful for:

* small preferences (e.g., “I prefer red buttons”)

⚠️ But the instructor’s advice is:
**Prefer Rules and version-controlled docs** for serious projects.

---

## Best long-term practice: keep a project summary file in your repo 📄

The transcript recommends creating a Markdown file that summarises:

* technical stack
* key features
* architecture notes
* conventions

Why this matters:

* lives in Git (portable)
* works across tools, not only Windsurf
* reduces reliance on long chat history and “memories”

---

## ✅ Example: `docs/project_context.md` (copy/paste)

This is the third “example text” you asked for — something you can actually store and reference.

```md
# Project Context (Short)

## Tech stack
- Frontend: React (Vite)
- Language: TypeScript
- Charting: (chosen library)
- Testing: Playwright (E2E) + (unit test runner)

## Core user flow
1. User uploads CSV with columns: date, rate
2. App cleans data (drop invalid/missing)
3. Chart renders with tooltip + crosshair
4. User filters by range (1Y, 5Y, 10Y, Max)

## Conventions
- Keep components small and focused
- Do not modify production code to satisfy tests
- Always run tests after implementing a TODO item

## Known issues / TODO
- (list current known gaps)
```

---

# ✅ Practical “Control Checklist” (use this when Windsurf misbehaves)

When Windsurf does something unexpected, check:

1. **Model**: am I using the right model for this task? 🧠⚙️
2. **Mode**: should I be in Chat (analysis) or Code (changes)?
3. **Context**: did I reference the exact file/line/error/UI element?
4. **Rules**: do I need a workspace rule to prevent this repeating?
5. **Conversation length**: should I start a new chat? 🔄
6. **Up-to-date docs**: do I need `add web` or `docs`? 🌐📚

---

## 🎓 Final takeaway

To control Windsurf consistently, don’t “fight” the AI in chat.
Instead:

✅ choose the right model,

✅ encode preferences as rules,

✅ provide precise context,

✅ keep chats short,

✅ keep project knowledge in version-controlled docs.

That is how you turn unpredictable behaviour into a reliable workflow. 🚀
