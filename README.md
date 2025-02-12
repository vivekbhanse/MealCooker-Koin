![image](https://github.com/user-attachments/assets/d779b3dd-637f-40bd-8af2-4a1ad28b2d23)![image](https://github.com/user-attachments/assets/9d86a3cc-f61d-4446-8df0-a2dcaf661df8)MyKoinApp

Overview

MyKoinApp is a Jetpack Compose-based Android application that integrates various modern Android libraries, including Koin for dependency injection, Room for local storage, Retrofit for network operations, and biometric authentication for secure login.

![image](https://github.com/user-attachments/assets/2573c4e0-de6f-46c6-9768-ce038de44319)


🔧 Dependencies Used

🏗 Core Libraries

AndroidX Core KTX – Kotlin extensions for Android.

Lifecycle Runtime KTX – Enables lifecycle-aware components.

Activity Compose – Required for Jetpack Compose apps.

🎨 Jetpack Compose UI

Compose BOM (Bill of Materials) – Manages Compose versioning.

UI, Foundation & Material3 – Essential for UI components.

Tooling Preview & Debug Tools – Enables UI previews and testing.

🏗 Dependency Injection

Koin for Android & Compose – Dependency injection framework.

🔄 Coroutines & Concurrency

Kotlinx Coroutines (Core & Android) – Asynchronous programming support.

🖼 Image Loading

Coil for Jetpack Compose – Image loading and caching.

🔑 Security & Authentication

Biometric API – Enables biometric authentication.

AndroidX Security Crypto – Secure data encryption.

🗄 Local Storage

Room Database (Runtime, KTX, Compiler) – Local database management.

📡 Networking

Retrofit – REST API calls.

Gson Converter – JSON parsing with Gson.

OkHttp Logging Interceptor – Logs network requests for debugging.

🔄 Navigation & UI Enhancements

Compose Navigation – Handles screen transitions.

Accompanist System UI Controller – Customizes status & navigation bars.

Accompanist SwipeRefresh – Enables swipe-to-refresh.

📊 Logging

Timber – Lightweight logging library for debugging.

🧪 Testing Dependencies

JUnit – Unit testing framework.

Espresso – UI testing.

Compose UI Testing – Testing Jetpack Compose UI elements.

🚀 Getting Started

1️⃣ Clone the repository

git clone https://github.com/vivekbhanse/MealCooker-Koin
cd MyKoinApp

2️⃣ Open in Android Studio

Ensure you have the latest Android Studio Flamingo or later installed.

3️⃣ Sync Gradle

In Android Studio, sync the project by clicking on "Sync Now".

4️⃣ Run the App

Select a device and click "Run" ▶️ to launch the app.

📌 Notes

The project uses Kotlin DSL for Gradle with dependency version management.

Uses Koin instead of Dagger/Hilt for dependency injection.

Jetpack Compose UI simplifies UI creation with declarative components.

Biometric authentication ensures secure access to the application.

📌 Notes

The project uses Kotlin DSL for Gradle with dependency version management.

Uses Koin instead of Dagger/Hilt for dependency injection.

Jetpack Compose UI simplifies UI creation with declarative components.

Biometric authentication ensures secure access to the application.

📜 License

This project is open-source and available under the MIT License.
