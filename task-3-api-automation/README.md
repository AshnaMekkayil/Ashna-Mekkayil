# Task 3 — REST API Test Automation (Kotlin + REST Assured)

## Setup
### Prerequisites
- Java 17+
- Internet access (public Swagger Petstore API)
- No global Gradle required (Gradle Wrapper included)

### Dependencies
All dependencies are managed using Gradle and will be downloaded automatically during the first test run.

Base URL used:
- https://petstore.swagger.io/v2

---

## How to run tests (Windows)
Open PowerShell in the `task-3-api-automation/` folder and run:

```powershell
.\gradlew.bat clean test
```

Test Execution Report

After execution, the HTML report is generated here:

build/reports/tests/test/index.html

A copy of the last execution report is included in this repository:

reports/test/index.html

Approach
What is tested

CRUD operations for the PET endpoint:

POST /pet → create a new pet

GET /pet/{id} → verify the created pet exists

PUT /pet → update pet details

DELETE /pet/{id} → delete the pet

GET after delete → verify pet is not found (expected 404 or 400)

Design choices

API calls are wrapped inside PetApi to keep test code clean and reusable.

A unique pet ID is generated for every run to reduce collision issues in the shared public environment.

Assertions are implemented with clear error messages to support easier debugging when failures happen.

Why this tech stack

Kotlin: matches the team’s preferred language and is commonly used in modern automation frameworks.

REST Assured: simple and readable DSL for writing API tests in a Given/When/Then style.

JUnit 5: widely used test framework with good IDE and CI integration.

Jackson: required for JSON serialization of Kotlin data classes used in API request bodies.

Gradle Wrapper: ensures consistent execution across different machines without installing Gradle globally.



