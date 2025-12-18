# Scentify

Scentify is a Java-only MVVM Android app focused on preference-driven perfume discovery. The experience runs completely offline with data stored locally via Room and SharedPreferences.

## Architecture
- **Pattern:** MVVM with repositories handling all data access.
- **Data Layer:** `Room` database for products and cart (`AppDatabase`, `ProductDao`, `CartDao`). Local seed data ships with 18 curated perfumes containing descriptive tags used for ranking.
- **Preferences:** Stored in `SharedPreferences` through `PreferenceStorage`. Preferences are exposed via `LiveData` to keep screens in sync.
- **Domain Utilities:** `PreferenceRanker` centralizes preference-matching and keyword-aware ranking so the same logic powers Home and Search.
- **UI Layer:** Activities host long-lived flows (`OnboardingActivity`, `MainActivity`, `ProductDetailActivity`) while fragments (`home`, `search`, `cart`, `settings`) render individual destinations. RecyclerView adapters keep the UI lean.

## Core Features
1. **Onboarding:** Material chip-based interest selection across fragrance types, occasions, brand categories, and budget range with skip + edit support.
2. **Persistent Preferences:** Saved immediately and observed across screens to refresh product order automatically.
3. **Product Catalog:** Room-backed list of 18 products enriched with tags that map to preference categories for accurate ranking.
4. **Preference Ranking:** `PreferenceRanker` scores each product by tag matches, always influencing browse and search ordering.
5. **Keyword Search:** Filters by name/brand, then reuses preference ranking to keep the experience tailored.
6. **Product Detail:** Dedicated screen summarizing notes, allowing quantity selection and cart adds.
7. **Cart + Checkout:** Local cart with quantity controls, totals, and simulated checkout confirmation.
8. **Settings:** View, edit, or clear saved preferences at any time.

## Limitations
- Images are static placeholders; there is no network fetching or remote synchronization.
- Checkout is simulated—no payment or fulfillment occurs.
- Preference ranking relies on predefined tags in the sample dataset; expanding the catalog requires tagging new entries accordingly.

## Getting Started
1. Open the project in Android Studio Giraffe or newer.
2. Sync Gradle (AGP 8.11.0, Java 11 source/target).
3. Run on any API 24+ emulator/device.
4. Go through onboarding to personalize results, or skip to browse the default order.
