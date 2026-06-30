# ☕ Caféora

**Caféora** is a premium, modern coffee ordering application built with a focus on elegant UI, responsiveness, and a seamless user experience. Leveraging the power of Jetpack Compose and Firebase, it offers a high-end boutique café experience directly on your Android device.

---

## 🚀 Features

### 1. Seamless Authentication
*   **Google One-Tap Sign-In:** Quick and secure access using your Google account.
*   **Email & Password Auth:** Traditional login and registration for flexible user access.
*   **Session Management:** The app remembers your session, taking you directly to the menu after the first login.

### 2. Gourmet Coffee Menu
*   **Interactive Pager:** Swipe through a high-resolution, curated list of coffee beverages.
*   **Vibrant Theming:** The app's background color dynamically adapts to match the character of each coffee (e.g., deep browns for Espresso, lush greens for Matcha).
*   **Detailed Insights:** Each item features a professional description, flavor profile type, and real-time price calculation based on quantity.

### 3. Intelligent Search
*   **Real-time Filtering:** Instantly find your favorite brew by name or coffee type.
*   **Optimized UI:** A compact, responsive search bar that works perfectly across all screen sizes and prevents overlapping with the status bar.

### 4. Favorites System
*   **One-Tap Save:** Mark your favorite coffees to save them to your personal favorites list.
*   **Persistent Data:** Favorites are managed via a dedicated ViewModel, ensuring they stay synced while you browse.

### 5. Advanced Shopping Cart & Checkout
*   **Quantity Control:** Adjust quantities directly within the cart or the product page.
*   **Flexible Payments:** Choose between **Secure Card Payment** (with validation) or **Cash on Delivery (COD)**.
*   **Order Success Animation:** A beautiful, premium confirmation screen featuring a branded gold seal and order details.

### 6. Personal Profile & Loyalty
*   **Order History:** View all your past purchases with detailed ID, items, and timestamps fetched from Firebase Firestore.
*   **Achievement System:** Earn titles as you order! Level up from a **Coffee Novice** to a **Coffee Master** based on your order count.
*   **Modern Logout:** Securely exit your account with a clean, themed logout interface.

### 7. Responsive & Premium Design
*   **Master-Level Layout:** 100% responsive design that adapts to any phone size, from small devices to large flagship displays.
*   **Official Splash API:** A professional, branded launch experience that works consistently on Android 12 and above.
*   **Vector Branding:** All logos are vector-based (`ic_logo.xml`), ensuring they remain sharp and never cut off on any screen resolution.

---

## 🛠️ Tech Stack

*   **Language:** [Kotlin](https://kotlinlang.org/)
*   **UI Framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (100% Declarative UI)
*   **Backend:** [Firebase Authentication](https://firebase.google.com/docs/auth) & [Cloud Firestore](https://firebase.google.com/docs/firestore)
*   **Image Loading:** [Coil](https://coil-kt.github.io/coil/) (with GIF support)
*   **Dependency Management:** Version Catalog (`libs.versions.toml`)
*   **Architecture:** MVVM (Model-View-ViewModel)
*   **System Integration:** Android 12+ Splash Screen API & Material 3 Design

---

## 📦 Getting Started

### Prerequisites
*   Android Studio Ladybug or newer.
*   JDK 17 or higher.
*   A Firebase project with Google Sign-In and Firestore enabled.

### Setup
1.  **Clone the repo:**
    ```bash
    git clone https://github.com/your-username/cafeora.git
    ```
2.  **Firebase Configuration:**
    *   Add your `google-services.json` to the `app/` directory.
    *   Ensure your **SHA-1 fingerprint** is registered in the Firebase Console.
3.  **Build & Run:**
    *   Sync Gradle files and run the app on an emulator or real device.

---

## 🎨 Design Colors
*   **Dark Brown:** `#3E2723` (Primary Background)
*   **Cream:** `#FFFDD0` (Primary Text/Buttons)
*   **Gold:** `#D4AF37` (Branding & Accents)
*   **Chocolate:** `#4E342E` (Secondary Surfaces)

---

*Developed with ❤️ for Coffee Lovers.*
