# GEMINI.md - HelpJob Android App

This document provides a comprehensive overview of the HelpJob Android application, its architecture, and development conventions.

## Project Overview

HelpJob is an Android application designed to assist users with various job-related tasks. The app is built using modern Android development technologies, including Kotlin, Jetpack Compose, and a robust set of libraries for networking, dependency injection, and navigation.

### Key Features

*   **Authentication:** Users can sign up and sign in to the application.
*   **Onboarding:** A guided onboarding process for new users.
*   **Home Screen:** A central dashboard that displays job-related steps or tasks.
*   **Calculator:** A built-in calculator for job-related calculations.
*   **Document Viewer:** A feature to view documents.
*   **User Profile:** A section for users to manage their profile and settings.
*   **Multi-language Support:** The app is designed to support multiple languages.

### Technology Stack

*   **Programming Language:** [Kotlin](https://kotlinlang.org/)
*   **UI Framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose)
*   **Architecture:** Model-View-ViewModel (MVVM)
*   **Dependency Injection:** [Hilt](https://dagger.dev/hilt/)
*   **Networking:** [Retrofit](https://square.github.io/retrofit/) and [OkHttp](https://square.github.io/okhttp/)
*   **JSON Serialization:** [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization)
*   **Navigation:** [Jetpack Navigation](https://developer.android.com/guide/navigation)
*   **Asynchronous Programming:** [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
*   **Analytics:** [Firebase Analytics](https://firebase.google.com/docs/analytics)
*   **Build Tool:** [Gradle](https://gradle.org/)

## API Specification

*   **Token Return Format**: `Map<String, String> - {"token": "JWT_TOKEN_VALUE"}`
*   **Email Endpoints**:
    *   Send verification code: `/api/email/send`
    *   Verify code: `/api/email/verify`
*   **PDF Endpoint**:
    *   Generate part-time employment verification certificate and send via email: `/api/cert`

## KMP Migration Plan

### KMP Module Structure
- **shared/**:
  - `data/`: Repository, Network (Ktor Client)
  - `presentation/`: Common ViewModel logic
- **androidApp/**: Android entry point (existing code)
- **iosApp/**: iOS entry point
- **composeApp/**: CMP UI implementation (identical to Android UI)

### Technology Stack Migration
*   **Retrofit → Ktor Client**
*   **Hilt → Koin**
*   **strings.xml → Moko Resources**

### Migration Priority
1.  **Phase 1:** Data Layer (Repository, API)
2.  **Phase 2:** Common ViewModel Logic
3.  **Phase 3:** CMP UI (identical to existing Android UI)

### Current Status
*   **Android App Refactoring:** Complete
*   **KMP Setup:** Not started
*   **Current Task:** Final pre-launch tasks (e.g., add a privacy policy page served from the server)

## Building and Running

To build and run the HelpJob application, follow these steps:

1.  **Clone the repository:**
    ```bash
    git clone <repository-url>
    ```

2.  **Create `local.properties` file:**
    Create a `local.properties` file in the root directory of the project and add the following line:
    ```properties
    API_BASE_URL="<your-api-base-url>"
    ```
    Replace `<your-api-base-url>` with the actual base URL of the backend API.

3.  **Open in Android Studio:**
    Open the project in Android Studio.

4.  **Sync Gradle:**
    Sync the project with Gradle files.

5.  **Run the app:**
    Run the app on an Android emulator or a physical device.

## Development Conventions

### Code Style
The project follows the standard Kotlin coding conventions. Use the IDE's default formatter to maintain a consistent code style.

### Logging
The project uses [Timber](https://github.com/JakeWharton/timber) for logging. Use `Timber.d()` for debug logs, `Timber.e()` for error logs, and so on.

### String Handling Guidelines
*   **Android:** Maintain the existing `strings.xml`.
*   **iOS:** Reuse `strings.xml` with Moko Resources.
*   **Error Messages in UiState:** Use `Int?` for Android (`R.string...`) and `StringResource` in the `shared` module.
*   **Dynamic Strings:** Only allow `String` for dynamic content from API responses.

### Error Handling Guidelines
**Core Principle:** "Errors that the user needs to know about must be reported; internal failures should be handled silently."

**Current Implementation:**
*   `CoroutineExceptionHandler`: Handles errors within coroutines.
*   `UncaughtExceptionHandler`: Prevents global crashes.
*   **Critical Errors:** Use a Snackbar if the error cannot be displayed directly in the UI.
*   **Silent Failures:** Perform logging only.

**BaseViewModel Structure:**
```kotlin
abstract class BaseViewModel {
    protected val crashPreventionHandler = CoroutineExceptionHandler { _, throwable ->
        handleError(throwable)
    }
}
```

### Dependency Management
Dependencies are managed using Gradle's `libs.versions.toml` file. When adding new dependencies, add them to the `libs.versions.toml` file and then reference them in the `build.gradle.kts` files.

## Project Reference Materials

*   **Android Development:** [nowinandroid](https://github.com/android/now-in-android) - Google's official sample app for modern Android architecture best practices.
*   **KMP Structure:** [KMP-App-Template](https://github.com/JetBrains/kmp-app-template) - JetBrains' official KMP template for project structure and setup.
*   **CMP UI:** [compose-multiplatform](https://github.com/JetBrains/compose-multiplatform) - Compose Multiplatform example code for common UI implementation patterns.

### Official Documentation Links
*   **Ktor Client:** https://ktor.io/docs/getting-started-ktor-client-multiplatform-mobile.html
*   **Koin KMP:** https://insert-koin.io/docs/reference/koin-mp/kmp
*   **Moko Resources:** https://github.com/icerockdev/moko-resources

## AI Assistant Guidelines (For Gemini)

### Persona
You are a world-class Android and KMP developer with over 30 years of experience. You have a perfect understanding of official documentation patterns and are an expert at applying them in practice.

### Tool Usage
*   **Sequential Thinking MCP:** Use this for complex problems that require step-by-step analysis, such as planning the KMP migration or designing the `expect/actual` structure.
*   You must use MCP tools at your own discretion and respond in Korean.

### Response Rules (Strict Compliance)

1.  **Mandatory Reference to Official Documentation:**
    *   All proposals must be based on official documentation.
    *   Absolutely no speculation or assumptions.
    *   If uncertain, explicitly state "Official documentation needs to be checked."

2.  **Code Modification Instructions:**
    *   Do not provide entire code blocks.
    *   Specify the modification location in the format: `"FileName: functionName"`.
    *   **Example:**
        ```kotlin
        // Before (MainActivity.kt: onCreate function)
        class MainActivity : AppCompatActivity()

        // After
        class MainActivity : ComponentActivity()
        ```

3.  **Prevent Over-engineering:**
    *   Do not propose features that are not immediately necessary.
    *   Do not add unnecessary layers like UseCase or Mapper.
    *   Do not exceed three levels of abstraction.

4.  **No UI Changes:**
    *   Absolutely no changes to the existing Android UI composition.
    *   Maintain the screen layout and navigation flow.
    *   Only platform-specific style/theme adjustments are allowed.

5.  **Maintain Consistency:**
    *   Adhere to the existing code style.
    *   Preserve the existing package structure.
    *   Do not change naming conventions.

6.  **Migration Priority:**
    *   **Phase 1:** Data Layer (Repository, API)
    *   **Phase 2:** Common ViewModel Logic
    *   **Phase 3:** CMP UI (identical to existing Android UI)
    *   Proceed with only one phase at a time.

7.  **Verification Required:**
    *   Check Kotlin and KMP version compatibility.
    *   Verify library support for KMP.
    *   Ensure existing Android functionality works correctly.