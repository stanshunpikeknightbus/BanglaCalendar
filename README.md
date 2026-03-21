# বাংলা ক্যালেন্ডার — Bangla Calendar

Android app using the official Bangladesh Revised Bengali Calendar (Bangabda 1987).
- **minSdk 26** (Android 8) · **targetSdk 35** (Android 15)
- Kotlin 2.x · Jetpack Compose · Material 3 · MVVM

---

## ⚠️ IMPORTANT — Add Font Files Before Building

The app needs 6 Bengali font files placed in `app/src/main/res/font/`.
GitHub cannot build without them.

### Step 1 — Download Noto Serif Bengali
1. Go to https://fonts.google.com/noto/specimen/Noto+Serif+Bengali
2. Click **Download family**
3. From the zip, find and rename these files exactly:

| Original filename | Rename to |
|---|---|
| NotoSerifBengali-Regular.ttf | `noto_serif_bengali_regular.ttf` |
| NotoSerifBengali-SemiBold.ttf | `noto_serif_bengali_semibold.ttf` |
| NotoSerifBengali-Bold.ttf | `noto_serif_bengali_bold.ttf` |

### Step 2 — Download Noto Sans Bengali
1. Go to https://fonts.google.com/noto/specimen/Noto+Sans+Bengali
2. Click **Download family**
3. Rename these:

| Original filename | Rename to |
|---|---|
| NotoSansBengali-Regular.ttf | `noto_sans_bengali_regular.ttf` |
| NotoSansBengali-Medium.ttf | `noto_sans_bengali_medium.ttf` |
| NotoSansBengali-SemiBold.ttf | `noto_sans_bengali_semibold.ttf` |

### Step 3 — Upload to GitHub
1. In your repo go to `app/src/main/res/font/`
2. Click **Add file → Upload files**
3. Drop all 6 renamed `.ttf` files
4. Click **Commit changes**

---

## Building with GitHub Actions

After uploading fonts, go to **Actions** tab → **Build Bangla Calendar APK** → **Run workflow**.
Download the APK from the **Artifacts** section when the build turns green ✅.

---

## Project Structure

```
app/src/main/java/com/banglacalendar/
├── MainActivity.kt
├── domain/
│   ├── model/BanglaDateConverter.kt   ← All calendar logic
│   └── usecase/CalendarUseCase.kt
└── ui/
    ├── screens/
    │   ├── CalendarScreen.kt          ← Compose UI
    │   └── CalendarViewModel.kt
    ├── theme/Theme.kt
    └── widget/BanglaCalendarWidget.kt ← Home screen widget
```

## Calendar Rules (Bangladesh 1987)

| Month | Gregorian Start | Days |
|---|---|---|
| বৈশাখ | April 14 | 31 |
| জ্যৈষ্ঠ | May 15 | 31 |
| আষাঢ় | June 15 | 31 |
| শ্রাবণ | July 16 | 31 |
| ভাদ্র | August 16 | 31 |
| আশ্বিন | September 16 | 30 |
| কার্তিক | October 16 | 30 |
| অগ্রহায়ণ | November 15 | 30 |
| পৌষ | December 15 | 30 |
| মাঘ | January 13 | 30 |
| ফাল্গুন | February 13 | 30* |
| চৈত্র | March 14 | 30* |

*31 days in leap years
