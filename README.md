# README.md for Yumi


# 🍽️ Yumi - Your Daily Meal Planner

<p align="center">
  <img src="screenshots/app_icon.png" alt="Yumi Logo" width="120" height="120">
</p>

<p align="center">
  <b>Plan Yummy, Eat Happy!</b>
</p>

<p align="center">
  <a href="#features">Features</a> •
  <a href="#screenshots">Screenshots</a> •
  <a href="#architecture">Architecture</a> •
  <a href="#tech-stack">Tech Stack</a> •
  <a href="#project-structure">Project Structure</a> •
  <a href="#setup">Setup</a> •
  <a href="#api">API</a> •
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-green.svg" alt="Platform">
  <img src="https://img.shields.io/badge/Language-Java-orange.svg" alt="Language">
  <img src="https://img.shields.io/badge/Architecture-MVP-blue.svg" alt="Architecture">
  <img src="https://img.shields.io/badge/Min%20SDK-24-yellow.svg" alt="Min SDK">
</p>

---

## 📖 Description

**Yumi** (يومي) is a modern Android meal planning application that helps users discover, plan, and organize their weekly meals. The app provides meal inspiration, allows searching by categories, countries, and ingredients, and enables users to save their favorite recipes for offline access.

The name "Yumi" means "Daily" in Arabic (يومي) and sounds like "Yummy" in English - perfect for a daily meal planner!

---

## ✨ Features

### 🍳 Meal Discovery
- **Meal of the Day** - Get daily meal inspiration with a random featured meal
- **Random Meals** - Discover new meals with random suggestions
- **Search** - Find meals by name, category, country, or ingredient
- **Categories** - Browse meals organized by food categories (Beef, Chicken, Seafood, etc.)
- **Countries** - Explore cuisines from around the world
- **Ingredients** - Search meals by specific ingredients

### 📅 Meal Planning
- **Weekly Planner** - Plan your meals for the current week
- **Meal Slots** - Organize breakfast, lunch, dinner, and snacks
- **Calendar View** - Easy week navigation with day selection

### ❤️ Favorites & Offline
- **Save Favorites** - Bookmark your favorite meals for quick access
- **Offline Access** - View saved favorites and meal plans without internet
- **Local Storage** - Data persisted using Room database

### 🔐 Authentication
- **Email Login/Signup** - Traditional email authentication
- **Social Login** - Sign in with Google, Facebook, or Twitter
- **Guest Mode** - Browse the app without an account (limited features)
- **Data Sync** - Backup and restore your data across devices using Firebase

### 🎨 User Experience
- **Dark/Light Theme** - Automatic theme switching based on system preference
- **Multi-Language** - Support for English and Arabic (RTL)
- **Modern UI** - Material Design 3 with smooth animations
- **Splash Screen** - Beautiful animated splash with Lottie

---

## 📱 Screenshots

<p align="center">
  <img src="screenshots/splash_light.png" width="200" alt="Splash Light">
  <img src="screenshots/splash_dark.png" width="200" alt="Splash Dark">
  <img src="screenshots/onboarding.png" width="200" alt="Onboarding">
  <img src="screenshots/login.png" width="200" alt="Login">
</p>

<p align="center">
  <img src="screenshots/home_light.png" width="200" alt="Home Light">
  <img src="screenshots/home_dark.png" width="200" alt="Home Dark">
  <img src="screenshots/search.png" width="200" alt="Search">
  <img src="screenshots/meal_details.png" width="200" alt="Meal Details">
</p>

<p align="center">
  <img src="screenshots/favorites.png" width="200" alt="Favorites">
  <img src="screenshots/meal_plan.png" width="200" alt="Meal Plan">
  <img src="screenshots/profile.png" width="200" alt="Profile">
  <img src="screenshots/no_internet.png" width="200" alt="No Internet">
</p>

> 📝 **Note:** Screenshots will be added as features are implemented.

---

## 🏗️ Architecture

This project follows the **MVP (Model-View-Presenter)** architecture pattern with a **Feature-based** package structure.

### MVP Pattern

```
┌─────────────────────────────────────────────────────────────┐
│                          VIEW                                │
│            (Activity/Fragment - UI Logic)                    │
│                           │                                  │
│                           ▼                                  │
│  ┌─────────────────────────────────────────────────────┐   │
│  │                    PRESENTER                          │   │
│  │         (Business Logic - Mediator)                   │   │
│  │                        │                              │   │
│  │           ┌────────────┼────────────┐                │   │
│  │           ▼            ▼            ▼                │   │
│  │    ┌──────────┐ ┌──────────┐ ┌──────────┐          │   │
│  │    │  MODEL   │ │  MODEL   │ │  MODEL   │          │   │
│  │    │ (Remote) │ │ (Local)  │ │  (Repo)  │          │   │
│  │    └──────────┘ └──────────┘ └──────────┘          │   │
│  └─────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

### Layer Responsibilities

| Layer | Responsibility |
|-------|----------------|
| **View** | Display data, handle user interactions, update UI |
| **Presenter** | Business logic, mediate between View and Model |
| **Model** | Data operations (API calls, database, repository) |

### Data Flow

```
User Action → View → Presenter → Model (Repository)
                                      │
                         ┌────────────┴────────────┐
                         ▼                         ▼
                   Remote Source              Local Source
                   (Retrofit API)             (Room DB)
                         │                         │
                         └────────────┬────────────┘
                                      ▼
                              Presenter (RxJava)
                                      │
                                      ▼
                              View (Update UI)
```

---

## 🛠️ Tech Stack

| Technology | Purpose |
|------------|---------|
| **Java** | Programming Language |
| **MVP** | Architecture Pattern |
| **RxJava 3** | Reactive Programming & Async Operations |
| **Retrofit 2** | REST API Client |
| **Room** | Local Database (SQLite) |
| **Firebase Auth** | Authentication (Email + Social) |
| **Firebase Firestore** | Cloud Data Sync & Backup |
| **Glide** | Image Loading & Caching |
| **Lottie** | Splash & UI Animations |
| **Material Design 3** | UI Components & Theming |
| **Navigation Component** | Fragment Navigation |
| **SharedPreferences** | User Settings Storage |

---

## 📁 Project Structure

```
app/
├── src/
│   └── main/
│       ├── java/com/yumi/app/
│       │   │
│       │   ├── base/                      # Base classes
│       │   │   ├── BaseActivity.java
│       │   │   ├── BaseFragment.java
│       │   │   ├── BasePresenter.java
│       │   │   └── BaseView.java
│       │   │
│       │   ├── data/                      # Data Layer
│       │   │   ├── local/                 # Local Data Source
│       │   │   │   ├── db/
│       │   │   │   │   ├── AppDatabase.java
│       │   │   │   │   ├── dao/
│       │   │   │   │   │   ├── MealDao.java
│       │   │   │   │   │   ├── FavoriteDao.java
│       │   │   │   │   │   └── MealPlanDao.java
│       │   │   │   │   └── entity/
│       │   │   │   │       ├── MealEntity.java
│       │   │   │   │       ├── FavoriteEntity.java
│       │   │   │   │       └── MealPlanEntity.java
│       │   │   │   └── prefs/
│       │   │   │       └── PreferencesManager.java
│       │   │   │
│       │   │   ├── remote/                # Remote Data Source
│       │   │   │   ├── api/
│       │   │   │   │   ├── ApiClient.java
│       │   │   │   │   └── MealApiService.java
│       │   │   │   └── model/
│       │   │   │       ├── MealResponse.java
│       │   │   │       ├── CategoryResponse.java
│       │   │   │       ├── CountryResponse.java
│       │   │   │       └── IngredientResponse.java
│       │   │   │
│       │   │   └── repository/            # Repository Pattern
│       │   │       ├── MealRepository.java
│       │   │       ├── MealRepositoryImpl.java
│       │   │       ├── AuthRepository.java
│       │   │       └── AuthRepositoryImpl.java
│       │   │
│       │   ├── features/                  # Feature-based Modules
│       │   │   │
│       │   │   ├── splash/                # Splash Feature ✅
│       │   │   │   ├── view/
│       │   │   │   │   └── SplashActivity.java
│       │   │   │   ├── presenter/
│       │   │   │   │   ├── SplashPresenter.java
│       │   │   │   │   └── SplashPresenterImpl.java
│       │   │   │   └── contract/
│       │   │   │       └── SplashContract.java
│       │   │   │
│       │   │   ├── onboarding/            # Onboarding Feature
│       │   │   │   ├── view/
│       │   │   │   ├── presenter/
│       │   │   │   ├── contract/
│       │   │   │   └── adapter/
│       │   │   │
│       │   │   ├── auth/                  # Authentication Feature
│       │   │   │   ├── login/
│       │   │   │   │   ├── view/
│       │   │   │   │   ├── presenter/
│       │   │   │   │   └── contract/
│       │   │   │   └── signup/
│       │   │   │       ├── view/
│       │   │   │       ├── presenter/
│       │   │   │       └── contract/
│       │   │   │
│       │   │   ├── home/                  # Home Feature
│       │   │   │   ├── view/
│       │   │   │   ├── presenter/
│       │   │   │   ├── contract/
│       │   │   │   └── adapter/
│       │   │   │
│       │   │   ├── search/                # Search Feature
│       │   │   │   ├── view/
│       │   │   │   ├── presenter/
│       │   │   │   ├── contract/
│       │   │   │   └── adapter/
│       │   │   │
│       │   │   ├── categories/            # Categories Feature
│       │   │   │   ├── view/
│       │   │   │   ├── presenter/
│       │   │   │   ├── contract/
│       │   │   │   └── adapter/
│       │   │   │
│       │   │   ├── countries/             # Countries Feature
│       │   │   │   ├── view/
│       │   │   │   ├── presenter/
│       │   │   │   ├── contract/
│       │   │   │   └── adapter/
│       │   │   │
│       │   │   ├── ingredients/           # Ingredients Feature
│       │   │   │   ├── view/
│       │   │   │   ├── presenter/
│       │   │   │   ├── contract/
│       │   │   │   └── adapter/
│       │   │   │
│       │   │   ├── mealdetails/           # Meal Details Feature
│       │   │   │   ├── view/
│       │   │   │   ├── presenter/
│       │   │   │   └── contract/
│       │   │   │
│       │   │   ├── favorites/             # Favorites Feature
│       │   │   │   ├── view/
│       │   │   │   ├── presenter/
│       │   │   │   ├── contract/
│       │   │   │   └── adapter/
│       │   │   │
│       │   │   ├── mealplan/              # Meal Plan Feature
│       │   │   │   ├── view/
│       │   │   │   ├── presenter/
│       │   │   │   ├── contract/
│       │   │   │   └── adapter/
│       │   │   │
│       │   │   └── profile/               # Profile/Settings Feature
│       │   │       ├── view/
│       │   │       ├── presenter/
│       │   │       └── contract/
│       │   │
│       │   ├── utils/                     # Utility Classes
│       │   │   ├── Constants.java
│       │   │   ├── NetworkUtils.java
│       │   │   ├── DateUtils.java
│       │   │   └── RxSchedulers.java
│       │   │
│       │   └── YumiApplication.java       # Application Class
│       │
│       └── res/
│           ├── drawable/
│           ├── drawable-night/
│           ├── font/
│           ├── layout/
│           ├── mipmap-*/
│           ├── values/
│           ├── values-night/
│           ├── values-ar/
│           └── xml/
│
├── build.gradle
└── proguard-rules.pro
```

---

## ⚙️ Setup

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK 34
- Min SDK 24

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/YOUR_USERNAME/Yumi.git
   cd Yumi
   ```

2. **Open in Android Studio**
   - File → Open → Select the project folder

3. **Configure Firebase**
   - Create a new Firebase project at [Firebase Console](https://console.firebase.google.com)
   - Add an Android app with package name: `com.yumi.app`
   - Download `google-services.json` and place it in `app/` folder
   - Enable Authentication (Email, Google, Facebook, Twitter)
   - Enable Firestore Database

4. **Add API Keys** (if needed)
   
   Create `local.properties` or add to `gradle.properties`:
   ```properties
   # Facebook
   FACEBOOK_APP_ID=your_facebook_app_id
   FACEBOOK_CLIENT_TOKEN=your_facebook_client_token
   
   # Twitter
   TWITTER_API_KEY=your_twitter_api_key
   TWITTER_API_SECRET=your_twitter_api_secret
   ```

5. **Build & Run**
   ```bash
   ./gradlew assembleDebug
   ```
   Or click ▶️ Run in Android Studio

---

## 🌐 API

This app uses [TheMealDB API](https://www.themealdb.com/api.php) - a free recipe API.

### Endpoints Used

| Endpoint | Description |
|----------|-------------|
| `random.php` | Get random meal |
| `search.php?s=` | Search meals by name |
| `filter.php?c=` | Filter by category |
| `filter.php?a=` | Filter by area/country |
| `filter.php?i=` | Filter by ingredient |
| `lookup.php?i=` | Get meal details by ID |
| `categories.php` | List all categories |
| `list.php?a=list` | List all countries |
| `list.php?i=list` | List all ingredients |

### Base URL
```
https://www.themealdb.com/api/json/v1/1/
```

---

## 📋 Implementation Progress

| Feature | Status |
|---------|--------|
| Splash Screen | ✅ Completed |
| Onboarding | 🔲 Pending |
| Login/Signup | 🔲 Pending |
| Social Auth | 🔲 Pending |
| Guest Mode | 🔲 Pending |
| Home Screen | 🔲 Pending |
| Meal of the Day | 🔲 Pending |
| Random Meals | 🔲 Pending |
| Categories List | 🔲 Pending |
| Countries List | 🔲 Pending |
| Ingredients List | 🔲 Pending |
| Search | 🔲 Pending |
| Meal Details | 🔲 Pending |
| Video Player | 🔲 Pending |
| Favorites | 🔲 Pending |
| Meal Planner | 🔲 Pending |
| Offline Mode | 🔲 Pending |
| Data Sync | 🔲 Pending |
| Profile/Settings | 🔲 Pending |
| Dark/Light Theme | ✅ Completed |
| Arabic Localization | 🔲 Pending |

---

## 🎬 Demo

📹 [Watch Demo Video](https://drive.google.com/your-demo-link)

---

## 📄 License

```
MIT License

Copyright (c) 2024 Yumi Team

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
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

<p align="center">
  Made with ❤️ for ITI Android Development Course
</p>

<p align="center">
  <b>Yumi - Plan Yummy, Eat Happy! 🍽️</b>
</p>

---

## 📝 GitHub Repository Description (Short)

```
🍽️ Yumi - A modern Android meal planner app built with MVP architecture. Discover meals, plan your week, and save favorites offline. Features TheMealDB API, Firebase Auth, Room DB, RxJava, and Material Design 3.
```
