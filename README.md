# Compose Widgets Library

[![Maven Central](https://img.shields.io/maven-central/v/io.github.jyotimoykashyap/composecomponents?logo=android)](https://search.maven.org/artifact/io.github.jyotimoykashyap/composecomponents)
[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](LICENSE)
![API](https://img.shields.io/badge/API-26%2B-brightgreen.svg?style=flat)
![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-blue.svg?logo=kotlin)

A collection of interactive and fun widgets for Jetpack Compose.

This repository contains a growing collection of custom widgets that can be easily integrated into your Android projects to create engaging user experiences.

## Widgets

### Progress Button

A button that displays a progress animation from left to right, filling the button's shape.

![Progress Button GIF](assets/progressbtnprev.gif)

#### Usage

To use the `ProgressButton` composable, you need to add the dependency to your project.

There are two ways to add the dependency:

**1. Using `libs.versions.toml` (Recommended)**

In your `gradle/libs.versions.toml` file, add the following library definition:

```toml
[versions]
compose-components = "0.0.2"

[libraries]
compose-components = { group = "io.github.jyotimoykashyap", name = "composecomponents", version.ref = "compose-components" }
```

Then, in your module-level `build.gradle.kts` file (e.g., `app/build.gradle.kts`), add the dependency:

```kotlin
dependencies {
    implementation(libs.compose.components)
}
```

**2. Using `build.gradle.kts` directly**

In your module-level `build.gradle.kts` file (e.g., `app/build.gradle.kts`), add the following dependency:

```kotlin
dependencies {
    implementation("io.github.jyotimoykashyap:composecomponents:0.0.2")
}
```

Once the dependency is added, you can use the `ProgressButton` in your UI like this:

```kotlin
import com.jyotimoykashyap.composecomponents.ProgressButton

ProgressButton(
    text = "Let's Get Started",
    onClick = {
        // Handle button click
    },
    onProgressComplete = {
        // Handle progress completion
    }
)
```

You can customize the button's shape and progress duration:

```kotlin
ProgressButton(
    text = "Custom Button",
    shape = RoundedCornerShape(12.dp),
    progressDurationMillis = 3000,
    onClick = { /* ... */ },
    onProgressComplete = { /* ... */ }
)
```

## Contribution

Contributions are welcome! If you have a fun and interactive widget that you'd like to share, please follow these steps:

1.  **Fork the repository** on GitHub.
2.  **Create a new branch** for your feature or bug fix.
3.  **Implement your changes** and add any necessary tests.
4.  **Ensure the code is well-formatted** and follows the existing style.
5.  **Open a pull request** with a clear description of your changes.

## License

This project is licensed under the Apache License, Version 2.0. See the [LICENSE](LICENSE) file for details.
