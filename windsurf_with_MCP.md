# 🔌 MCP in Windsurf — README (Model Context Protocol, Store, Integrations, and Real Workflows)

This README extracts the **main idea** from the transcript and turns it into a **usable workflow** you can apply when you want Windsurf to interact with external services (GitHub, files outside the repo, Jira, etc.).

## 🧠 Main Idea (one thing to remember)
**MCP turns Windsurf from “coding assistant” into a connected agent** by letting it call **external tools and data sources** through a standard interface — eliminating manual copy/paste and tab-switching.

✅ Use MCP when you feel friction: *“I keep doing this manually; the agent should do it.”*

---

# 1) 🤔 Why MCP exists (the problem it solves)

## What an LLM can’t do alone
A language model, by default:
- has **no real-time info**
- has **no direct access** to external systems
- only knows training data + what you provide in context

Windsurf already includes built-in tools, but it still can’t reach everything you use daily:
- GitHub
- Jira
- analytics dashboards
- external folders/files
- notifications
- internal services

That gap is what MCP fills.

---

# 2) 🧩 What MCP is (in plain English)

## MCP = Model Context Protocol
An **open standard** that defines how applications can expose:
- **context** (data)
- **tools** (actions)
to LLMs in a structured, consistent way.

Think of it like:
🧱 **a plugin system for tools + data**  
so the agent can do real actions: create issues, fetch data, update tickets, etc.

### MCP Server concept
An **MCP server** exposes a set of custom tools:
- purpose-built for a workflow
- callable by the agent
Examples from the transcript:
- filesystem MCP → read external folders
- GitHub MCP → create repos, create issues, etc.

---

# 3) ✅ When you actually need MCP (decision rule)

Use MCP **whenever** you notice you’re doing repetitive manual work, such as:
- copying data into/out of Windsurf 📝
- switching tabs to create GitHub/Jira tickets 🔁
- manually referencing external logs/files 📂
- doing routine “integration chores”

> 🧑‍🏫 Professor note: If a workflow step is predictable and repetitive, MCP is worth considering.  
If it’s one-off, keep it manual — avoid unnecessary complexity.

---

# 4) 🏪 MCP Store (where integrations live)

The MCP Store is like a marketplace for ready-made integrations.

## Two ways to open it (because the UI changes)
1) Click the “package” icon → **MCP Store**
2) Settings → Advanced → Cascade MCP servers → **Open MCP Store**

Once inside:
- each MCP server lists:
  - what tools it exposes
  - setup/config requirements
  - docs and usage guidance

🧑‍🏫 Professor tip: **Read the docs**. MCP servers vary a lot, and setup steps are not always trivial.

---

# 5) 🧠 Key operational reality: MCP is not “automatic magic”
The transcript shows an important behaviour:

Sometimes Windsurf **won’t automatically choose the MCP tool**, even if it’s installed.
It might:
- try to do something locally (wrong approach)
- run a terminal command instead
- fail to “realise” an MCP tool is available

✅ Solution: be explicit at first:
- “Use MCP”
- “Use the GitHub MCP server”
- “Call the filesystem MCP tool to list directory contents”

Once you establish the pattern, it tends to behave better.

---

# 6) 📂 Example 1 — Filesystem MCP (external context beyond the repo)

## Why it’s useful
Filesystem MCP lets you read files/folders **outside your project repo** and bring that into the agent’s context.

This is useful for:
- external datasets
- downloaded logs
- reference files you don’t want committed
- artifacts generated elsewhere

## Setup (what the transcript highlights)
- Install filesystem MCP server
- You must **grant access** to specific directories
  - choose a safe “sandbox” folder (e.g., `~/Downloads/windsurf/`)
- You may hit OS permission prompts ⚠️

## The “gotcha” demonstrated
The first time, Windsurf might not call MCP and instead tries another approach.

✅ Fix by prompting explicitly:
> “Using MCP, list the contents of `<path>`.”

---

# 7) 🔧 Tool overload: MCP servers add tools to context

Every enabled MCP server contributes tools into the agent’s available toolset.

### Why that matters
- tools consume “tool budget” / capacity
- too many tools at once becomes noisy and less efficient

The transcript mentions:
- there is a limit (example given: 100 tools)
- one MCP server might add many tools (filesystem: 14 tools; GitHub: ~30 tools)

✅ Best practice:
- enable only what you need
- disable servers/tools you’re not using
- keep the agent “lean”

> 🧑‍🏫 Professor note: Tool overload is like a cluttered toolbox — finding the right tool becomes slower and more error-prone.

---

# 8) 🐙 Example 2 — GitHub MCP server (real external actions)

## Why GitHub MCP is valuable
Your repo is local; GitHub gives:
- backup
- collaboration
- issues + PR workflow
- visibility and sharing

### Setup steps (as described)
1) Install GitHub MCP server
2) Create a GitHub **Personal Access Token (classic)**
   - GitHub → Settings → Developer Settings → Personal access tokens → Tokens (classic)
   - choose short expiration (good hygiene)
   - minimal scope: `repo` (enough for typical tasks)
3) Paste token into Windsurf configuration

### Common obstacle: Docker requirement 🐳
The transcript shows:
- some MCP servers require Docker locally
- if you see an error about Docker, install/run Docker Desktop
- then refresh and re-enable

✅ Best practice:
- keep Docker off when not needed (reduce background overhead)
- treat MCP servers like “capabilities you enable temporarily”

---

# 9) 🧠 How to validate MCP is actually being used
A key technique from the transcript:

✅ When you request an MCP action, you should see an **MCP tool call** in the chat.

If you don’t see it:
- the agent probably isn’t using MCP
- you may need to prompt more explicitly

---

# 10) ✅ A *useful* real workflow: TODO → GitHub Issues (automation win)

This is the strongest example in the transcript.

## Goal
Take incomplete tasks from `todo.md` and convert them into GitHub Issues using GitHub MCP.

## What makes it powerful
- saves manual issue creation
- preserves acceptance criteria
- creates structured work items per phase/task
- enables text-driven workflow:
  - “Work on issue #3”
  - “Close issue when done”
  - “Create PR for issue #X”

## What the transcript shows done right
Before mass-creating issues, the agent asks clarifying questions:
- owner
- repo name
- defaults confirmation

✅ That’s good behaviour — it prevents creating lots of wrong data.

> 🧑‍🏫 Professor note: In automation tasks, “asking first” is a feature, not a delay.  
Wrong automation is worse than manual work.

---

# 11) The MCP golden prompting pattern 🥇

When doing MCP workflows, include one of these in your prompt:

✅ “Use MCP for this.”  
✅ “If anything is unclear, ask follow-up questions before taking actions.”  
✅ “Show me the plan first, then execute.”

This helps avoid:
- wrong repo
- wrong permissions
- mass-creating issues incorrectly
- acting on the wrong scope

---

# 12) 🧾 Example: an MCP workflow prompt pack (copy/paste)
You asked for examples of the kind of text to write — here are practical prompts you can keep in your README.

```md
# MCP Prompt Pack (Copy/Paste)

## 1) Verify MCP is active
"Using MCP, list the available tools from the GitHub MCP server."

## 2) Filesystem MCP: bring external context
"Using MCP filesystem tools, list the contents of: /Users/<me>/Downloads/windsurf.
Then show the first 20 lines of <file>."

## 3) GitHub MCP: create issues from todo.md
"Using GitHub MCP: convert all unchecked items in todo.md into GitHub issues.
Ask me to confirm owner/repo before creating anything.
Include acceptance criteria in each issue body."

## 4) Guardrails for automation
"Show the plan first. Do not execute destructive actions without asking.
If unsure, ask follow-up questions."
````

---

# ✅ Practical checklist: MCP setup & usage

## Before installing an MCP server

* ✅ Do I really need this, or is manual faster for a one-off?
* ✅ Do I understand what permissions I’m granting?
* ✅ Do I know what tools it will expose?

## After installing

* ✅ Confirm it shows under MCP servers
* ✅ Confirm required dependencies (e.g., Docker) are running if needed
* ✅ Ask a small “safe” query first (e.g., list tools, list directory)

## During usage

* ✅ Check for MCP tool call in chat
* ✅ If it doesn’t call MCP, explicitly say “use MCP”
* ✅ Avoid enabling too many MCP servers at once

---

## 🎓 Final takeaway

MCP is the bridge from:
🧑‍💻 “AI writes code”
to
🤖 “AI performs real workflow actions”

Use it to remove friction (copy/paste, tab switching, ticket creation), but keep control:

* be explicit early
* keep tools enabled only when needed
* confirm before bulk actions
* treat permissions and tokens as real security assets 🔐

```
```
