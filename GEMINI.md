# GEMINI.md — Project Context for CCA-F Practice App

## Project Overview
Android app: "CCA-F Practice Exams: Claude Certified Architect"
Offline-first practice exam / flashcard app for Anthropic's CCA-F certification.
Package: com.arslan.ccafprep

## Hard Constraints (do not violate)
- 100% OFFLINE except Google Play Billing. No Firebase, no backend, no network
  calls, no accounts, no analytics SDKs that phone home.
- ALL question content must be ORIGINAL — written from publicly known domain/
  skill descriptions only. Never reproduce or closely paraphrase third-party
  prep site content or real/leaked exam questions.
- No Anthropic/Claude logos or trademarked visual assets anywhere in the app.
- Always label content as independent/unofficial study material with the
  disclaimer shown in `app/src/main/res/values/strings.xml` → `disclaimer_text`.

## Architecture
- Kotlin, Jetpack Compose (Material 3)
- MVVM + Clean Architecture: domain / data / presentation layers
- Room for local persistence (questions, progress, spaced-repetition state)
- DataStore for settings
- Hilt for DI
- Coroutines + Flow
- Google Play Billing Library v7 — ONE-TIME non-consumable purchase only,
  no subscriptions

## Package Structure
com.arslan.ccafprep/
├── data/
│   ├── local/        (Room entities, DAOs, database)
│   ├── repository/   (repository implementations)
│   └── billing/      (Play Billing wrapper)
├── domain/
│   ├── model/        (Question, Domain, UserAttempt, SpacedRepetitionState)
│   └── usecase/
├── presentation/
│   ├── quiz/
│   ├── flashcard/
│   ├── mockexam/
│   ├── progress/
│   └── paywall/
└── di/               (Hilt modules)

## Exam Domain Taxonomy (use for tagging all questions)
1. Agentic Architecture & Orchestration (~27%)
2. Tool Design & MCP Integration (~18%)
3. Claude Code Configuration & Workflows (~20%)
4. Prompt Engineering & Structured Output (~20%)
5. Context Management & Reliability (~15%)

## Conventions
- ViewModels expose StateFlow, never LiveData.
- One feature = one Compose screen + one ViewModel + one UseCase, no
  god-objects.
- Free tier = Domain 1 fully unlocked. Paid IAP unlocks Domains 2–5 + mock exam.
- Question bank lives in app/src/main/assets/questions_v1.json, seeded into
  Room on first launch only (check a "seeded" flag in DataStore).

## Build Status / Current Phase
- [x] Phase 1 — Project setup, Gradle, Hilt, Navigation, Theme
- [x] Phase 2 — Domain models + Room data layer
- [x] Phase 3 — Study modes (Randomized, Section, Timed, Review, Flashcard)
- [x] Phase 4 — Spaced repetition engine
- [x] Phase 5 — Content seeding (150–250 original questions)
- [x] Phase 6 — Progress dashboard
- [x] Phase 7 — Play Billing one-time purchase
- [x] Phase 8 — Final Polish & Compliance Verification (Mocks & Debug Toggles Removed)
- [x] Phase 9 — Deployment Ready

## What NOT to do
- Don't regenerate whole files when a small edit will do — ask for diffs.
- Don't re-scan the full project tree if structure hasn't changed since last
  session — assume the structure above is current unless told otherwise.
- Don't add network/cloud dependencies "for convenience" — this app is
  intentionally offline-only.