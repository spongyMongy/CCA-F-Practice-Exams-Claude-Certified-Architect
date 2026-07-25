# Add 2 More Mock Exams

The goal is to provide more structured practice for the CCA-F certification by adding two additional mock exam options. This requires expanding the question bank to ensure variety and implementing a weighted selection algorithm that mimics the real exam's domain distribution.

## Proposed Changes

### Data Layer

#### [questions_v2.json](file:///C:/Users/makti/StudioProjects/CCA-F-Practice-Exams-Claude-Certified-Architect/app/src/main/assets/questions_v2.json)
- Expand the question bank from 75 to 200+ original questions.
- Distribute questions across the 5 domains according to official weights (27%, 18%, 20%, 20%, 15%).

#### [DatabaseSeeder.kt](file:///C:/Users/makti/StudioProjects/CCA-F-Practice-Exams-Claude-Certified-Architect/app/src/main/java/com/arslan/ccafprep/data/local/seed/DatabaseSeeder.kt)
- Update the threshold for re-seeding to ensure the new questions are loaded: `questionsCount < 200`.

### Presentation Layer

#### [HomeScreen.kt](file:///C:/Users/makti/StudioProjects/CCA-F-Practice-Exams-Claude-Certified-Architect/app/src/main/java/com/arslan/ccafprep/presentation/home/HomeScreen.kt)
- Replace the single "Full Mock Exam" card with a vertically stacked list of three Mock Exam cards:
    - **Mock Exam 1**: Standard weighted practice.
    - **Mock Exam 2**: Different question set (via seed).
    - **Mock Exam 3**: Different question set (via seed).
- Ensure all three are locked behind the Pro paywall.

#### [QuizViewModel.kt](file:///C:/Users/makti/StudioProjects/CCA-F-Practice-Exams-Claude-Certified-Architect/app/src/main/java/com/arslan/ccafprep/presentation/quiz/QuizViewModel.kt)
- Update `loadQuestions()` for `mode == "mock"` to implement weighted selection:
    - Select 16 questions from Domain 1.
    - Select 11 questions from Domain 2.
    - Select 12 questions from Domain 3.
    - Select 12 questions from Domain 4.
    - Select 9 questions from Domain 5.
- Use the `domainId` passed in the route as a `Random(seed)` to ensure Mock Exam 1, 2, and 3 provide distinct but repeatable question sets.

## Verification Plan

### Automated Tests
- I will run `gradle_build` to ensure the project still compiles after UI and logic changes.

### Manual Verification
- **Seeding**: Verify in logs or by checking the "Analytics" screen that total questions reach ~200.
- **UI Layout**: Verify the Home Screen displays three distinct Mock Exam cards.
- **Access Control**: Verify that clicking any Mock Exam redirects to the Paywall if Pro is not active.
- **Exam Content**:
    - Start Mock Exam 1: Verify 60 questions total.
    - Start Mock Exam 2: Verify it contains different questions than Exam 1.
    - Check Domain Distribution: Ensure questions from all 5 domains are present.
