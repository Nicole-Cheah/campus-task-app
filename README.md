# Campus Courier App

A peer-to-peer campus task marketplace Android app that enables real-time task matching and fulfillment between students using Firebase-backed services.

---

## Features

**For Requestors**
- Post tasks with item name, description, category, urgency, location, deadline, and an optional photo
- Edit or delete open requests
- Track request status and view history

**For Suppliers**
- Browse all open requests with live search and filters (location, category)
- Accept or cancel tasks
- View task completion history

**For Admins**
- Review user-submitted reports
- Accept or decline reports to manage demerit points

**Shared**
- Firebase Authentication (email & password)
- Demerit points system — track your standing via a progress bar
- Report a user with selectable reasons and a custom description
- FAQ & Rules page with direct Telegram support link
- Update Telegram handle from your profile

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java |
| UI | XML layouts, Material 3, ViewBinding |
| Auth | Firebase Authentication |
| Database | Firebase Firestore |
| Storage | Firebase Storage |
| Architecture | Activity-based, Observer pattern |
| Build | Gradle (Kotlin DSL) |

---

## Project Structure

```
app/src/main/java/com/example/campuscourier/
├── shared/          # Auth, profile, reporting, shared helpers
├── requestor/       # Post, view, edit requests
├── supplier/        # Browse, accept, complete requests
└── admin/           # Report review dashboard
```

---

## Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- Android device / emulator (API 24+)
- A Firebase project with **Authentication**, **Firestore**, and **Storage** enabled

### Setup

1. Clone the repository
   ```bash
   git clone https://github.com/Nicole-Cheah/campus-task-app.git
   cd campus-task-app
   ```

2. Connect Firebase
   - Create a project at [console.firebase.google.com](https://console.firebase.google.com)
   - Register an Android app with package name `com.example.campuscourier`
   - Download `google-services.json` and place it in `app/`

3. Enable Firebase services
   - **Authentication** → Email/Password
   - **Firestore** → create a database in production mode
   - **Storage** → default bucket

4. Open the project in Android Studio, sync Gradle, and run on a device or emulator

---

## Requirements

- Min SDK: 24 (Android 7.0)
- Target SDK: 34 (Android 14)
