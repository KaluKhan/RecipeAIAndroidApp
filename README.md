# RecipeAI 🍽️

> **Discover. Cook. Delight.**
> A beautiful, AI-powered recipe app built with Jetpack Compose and Gemini AI.

![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=flat-square&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-7F52FF?style=flat-square&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-2024.09-4285F4?style=flat-square&logo=jetpackcompose&logoColor=white)
![Gemini AI](https://img.shields.io/badge/Gemini-1.5%20Flash-8E75B2?style=flat-square&logo=google&logoColor=white)
![Min SDK](https://img.shields.io/badge/Min%20SDK-26-orange?style=flat-square)
![License](https://img.shields.io/badge/License-MIT-blue?style=flat-square)

---

## Screenshots

| Splash | Home | AI Agent | Detail |
|--------|------|----------|--------|
| Animated splash with chef branding | Staggered grid with category chips | Gemini-powered recipe suggestions | Step-by-step cooking timeline |

---

## Features

### Core
- **Staggered Grid Home Feed** — Recipes displayed in a Pinterest-style `LazyVerticalStaggeredGrid` with animated card entry
- **Category Filtering** — Filter by Breakfast, Lunch, Dinner, Coolers, Snacks, Desserts, Vegetarian, Non-Vegetarian
- **Smart Search** — Real-time search across title, description, cuisine, and tags
- **Recipe Detail Page** — Hero image, nutrition stats, ingredient list, and step-by-step cooking timeline with tips

### AI Agent
- **Gemini 1.5 Flash** integration for context-aware recipe generation
- **GPS Location** auto-fetch to suggest regionally relevant recipes
- **Manual Location** input for custom location-based suggestions
- **Category Search** — Ask for "vegan lunch", "Italian dinner", "summer coolers", and more
- Results displayed in the same beautiful staggered grid

### UX & Design
- **Animated Splash Screen** with pulsing logo and SplashScreen API
- **Custom App Icon** — Adaptive chef hat with gold AI spark
- **Sliding Navigation Drawer** — Home, AI Chef Assistant, Contact Us
- **Dark-first theme** with brand orange/gold Material 3 color scheme
- **Smooth transitions** — Slide + fade navigation animations, spring-based press feedback
- **Contact Us page** with form validation and success state

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Kotlin 2.0.21 |
| UI | Jetpack Compose (BOM 2024.09.03) + Material 3 |
| Architecture | MVVM + Repository Pattern |
| DI | Hilt 2.51.1 |
| AI | Google Generative AI SDK (Gemini 1.5 Flash) |
| Navigation | Jetpack Navigation Compose 2.8.2 |
| Image Loading | Coil 2.7.0 |
| Location | Google Play Services Location 21.3.0 |
| Permissions | Accompanist Permissions 0.36.0 |
| Async | Kotlin Coroutines + Flow |
| State | `StateFlow` + `collectAsStateWithLifecycle` |

---

## Project Structure

```
RecipeAI/
├── app/
│   └── src/main/
│       ├── java/com/recipeai/app/
│       │   ├── MainActivity.kt
│       │   ├── RecipeAIApp.kt
│       │   ├── data/
│       │   │   ├── model/
│       │   │   │   ├── Recipe.kt           # Data models, enums
│       │   │   │   └── SampleRecipes.kt    # 8 curated sample recipes
│       │   │   ├── remote/
│       │   │   │   └── GeminiService.kt    # Gemini AI integration
│       │   │   └── repository/
│       │   │       └── RecipeRepository.kt
│       │   ├── di/
│       │   │   └── AppModule.kt            # Hilt modules
│       │   ├── viewmodel/
│       │   │   ├── MainViewModel.kt
│       │   │   └── AiViewModel.kt
│       │   └── ui/
│       │       ├── theme/                  # Color, Typography, Theme
│       │       ├── navigation/             # NavHost, Drawer, Screen routes
│       │       ├── components/             # RecipeCard, SearchBar, CategoryChips, FlowRow
│       │       └── screens/
│       │           ├── splash/
│       │           ├── main/               # Staggered grid feed
│       │           ├── detail/             # Recipe detail + steps
│       │           ├── ai/                 # AI agent screen
│       │           └── contact/            # Contact us form
│       └── res/
│           ├── drawable/                   # Vector icons, app icon layers
│           ├── mipmap-anydpi-v26/          # Adaptive icon
│           └── values/                     # Strings, themes
├── gradle/
│   ├── libs.versions.toml                  # Version catalog
│   └── wrapper/
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

---

## Getting Started

### Prerequisites

- Android Studio Ladybug (2024.2.1) or newer
- JDK 17+
- Android device or emulator running API 26+
- A **Gemini API Key** — get one free at [Google AI Studio](https://aistudio.google.com/apikey)

### Setup

**1. Clone the repository**
```bash
git clone https://github.com/YOUR_USERNAME/RecipeAI.git
cd RecipeAI
```

**2. Add your Gemini API Key**

Open `app/build.gradle.kts` and replace the placeholder:
```kotlin
buildConfigField("String", "GEMINI_API_KEY", "\"YOUR_GEMINI_API_KEY_HERE\"")
```
with your actual key:
```kotlin
buildConfigField("String", "GEMINI_API_KEY", "\"AIza...your_key_here...\"")
```

> **Security note:** Never commit your real API key. For production, store it in `local.properties` (which is gitignored) and read it in `build.gradle.kts` like this:
> ```kotlin
> val localProps = java.util.Properties()
> localProps.load(rootProject.file("local.properties").inputStream())
> buildConfigField("String", "GEMINI_API_KEY", "\"${localProps["GEMINI_API_KEY"]}\"")
> ```
> Then add to `local.properties`:
> ```properties
> GEMINI_API_KEY=AIza...your_key_here...
> ```

**3. Sync and Run**
```bash
# Open in Android Studio and click Sync Now, then Run
# Or via command line:
./gradlew assembleDebug
```

---

## How to Use

### Home Screen
- Browse the curated recipe feed in the staggered grid
- Use the **category chips** to filter by meal type
- Use the **search bar** to find recipes by name, cuisine, or tag
- Tap any card to open the full recipe detail

### AI Chef Assistant
- Tap the **✨ FAB** (bottom-right) on the home screen, or use the drawer
- Grant location permission for GPS auto-fill, or type your city manually
- Optionally type a category or request (e.g. "healthy vegan breakfast")
- Tap **Get AI Recipe Suggestions** — Gemini generates 6 recipes just for you
- Tap any AI recipe card to view the full detail

### Navigation Drawer
- Swipe from the left edge, or tap the **☰ menu** icon
- Navigate to Home, AI Chef Assistant, or Contact Us

---

## Architecture Overview

```
┌──────────────────────────────────────────────┐
│                    UI Layer                   │
│  Compose Screens → ViewModels (StateFlow)     │
└───────────────────┬──────────────────────────┘
                    │ collects state
┌───────────────────▼──────────────────────────┐
│               ViewModel Layer                 │
│   MainViewModel      AiViewModel              │
│   (filter/search)    (GPS + Gemini calls)     │
└───────────────────┬──────────────────────────┘
                    │ calls
┌───────────────────▼──────────────────────────┐
│              Repository Layer                 │
│         RecipeRepository                      │
│  (local sample data + remote AI results)      │
└──────────┬────────────────────┬──────────────┘
           │                    │
┌──────────▼────────┐  ┌────────▼──────────────┐
│   Local Data      │  │    GeminiService       │
│  SampleRecipes    │  │  (Gemini 1.5 Flash)    │
└───────────────────┘  └───────────────────────┘
```

---

## Permissions

| Permission | Purpose |
|------------|---------|
| `INTERNET` | Gemini API calls, recipe image loading |
| `ACCESS_FINE_LOCATION` | GPS-based regional recipe suggestions |
| `ACCESS_COARSE_LOCATION` | Fallback coarse location |

Location is requested only when the user taps the GPS button in the AI screen. The app works fully without location permission.

---

## Customisation

### Adding More Pages to the Drawer
1. Add a new `Screen` object in `ui/navigation/Screen.kt`
2. Add a `composable(...)` entry in `AppNavigation.kt`
3. Add a `DrawerNavItem(...)` entry in `NavigationDrawerContent`

### Replacing Sample Recipes with a Live API
The `RecipeRepository` is designed for this. Inject a Retrofit service and replace the `SampleRecipes` fallback in `RecipeRepository.getAllRecipes()`.

### Changing the Color Scheme
All brand colors are in `ui/theme/Color.kt`. Change `BrandOrange` and `BrandGold` to retheme the entire app instantly.

---

## Known Limitations

- AI recipe thumbnails are Unsplash placeholder URLs — Gemini may occasionally return invalid image URLs; Coil handles these gracefully with a fallback
- AI-generated recipes are not persisted across app restarts (in-memory only)
- No authentication or user accounts in this version

---

## Contributing

Pull requests are welcome! For major changes, please open an issue first to discuss what you'd like to change.

1. Fork the repo
2. Create your feature branch: `git checkout -b feature/AmazingFeature`
3. Commit your changes: `git commit -m 'Add AmazingFeature'`
4. Push to the branch: `git push origin feature/AmazingFeature`
5. Open a Pull Request

---

## License

```
MIT License

Copyright (c) 2026 RecipeAI

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
```

---

## Author

Built with love and Gemini AI. If you find this project useful, give it a ⭐ on GitHub!

---

*RecipeAI — Powered by Gemini 1.5 Flash · Built with Jetpack Compose · MVVM Architecture*
