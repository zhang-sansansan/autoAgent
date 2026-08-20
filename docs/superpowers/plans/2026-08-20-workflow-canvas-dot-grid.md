# Workflow Canvas Dot Grid Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Reduce the Agent workflow canvas dot-grid density and contrast while preserving spatial orientation.

**Architecture:** Keep the existing two-layer CSS background and change only its numeric visual tokens. Extend the existing stylesheet contract test so the approved spacing and opacity cannot regress.

**Tech Stack:** CSS, TypeScript, Vitest, Rsbuild, in-app browser

---

### Task 1: Tune and verify the workflow canvas background

**Files:**
- Modify: `frontend/src/styles/theme-contract.test.ts`
- Modify: `frontend/src/styles/index.css:119-123`

- [ ] **Step 1: Write the failing contract assertions**

Add these assertions to the existing dark theme contract test:

```ts
expect(stylesheet).toContain('rgba(139, 92, 246, .05)');
expect(stylesheet).toContain('rgba(135, 145, 180, .14) .65px');
expect(stylesheet).toContain('background-size: auto, 42px 42px');
```

- [ ] **Step 2: Run the focused test and verify it fails**

Run:

```bash
npm test -- --run src/styles/theme-contract.test.ts
```

Expected: FAIL because the stylesheet still contains the 24px, 28%, 0.8px background.

- [ ] **Step 3: Apply the approved background values**

Change the workflow background rule to:

```css
.ann-workflow-theme .gedit-playground {
  background-image:
    radial-gradient(circle at 50% 0%, rgba(139, 92, 246, .05), transparent 38%),
    radial-gradient(rgba(135, 145, 180, .14) .65px, transparent .65px) !important;
  background-size: auto, 42px 42px !important;
}
```

- [ ] **Step 4: Run automated verification**

Run:

```bash
npm test -- --run src/styles/theme-contract.test.ts
npx tsc --noEmit
npm run build
```

Expected: contract test passes, TypeScript exits 0, and Rsbuild reports a successful production build.

- [ ] **Step 5: Verify in the browser**

Open `/agent-config`, verify the computed background size is `auto, 42px 42px`, confirm the dot grid is visibly calmer, and capture a screenshot for visual review.

- [ ] **Step 6: Commit the implementation**

```bash
git add frontend/src/styles/theme-contract.test.ts frontend/src/styles/index.css
git commit -m "fix(frontend): soften workflow canvas dot grid"
```
