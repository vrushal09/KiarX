# MyToDo App

A robust Android To-Do List application engineered using Java, adhering strictly to the **MVC (Model-View-Controller)** architectural pattern. The application demonstrates clean coding practices, separation of concerns, and efficient local data persistence.

**Designed and Developed by:** Mehta Vrushal

---

## 📥 Download APK

You can download the compiled application package (APK) from the following link:
👉 [Download Link](https://drive.google.com/file/d/1RIr-j7j_CNXly3Vyk7tej9R4z2nee-QF/view?usp=sharing)

---

## ℹ️ Project Scope & Constraints

Please note that due to strict time constraints, the development focus was primarily placed on:
1.  **Architectural Integrity**: Implementing a clean MVC structure.
2.  **Core Logic**: Ensuring robust CRUD operations and data handling.
3.  **Functional Requirements**: Meeting all specified task management criteria.

As a result, the User Interface (UI) adopts a minimalist design philosophy, prioritizing usability and stability over complex animations or elaborate visual styling.

---

## 📱 Functional Features

*   **Task Creation**: Allows users to input task titles and optional details.
*   **Gesture-Based Operations**:
    *   **Edit**: Swipe **Right** on a task item to update its contents.
    *   **Delete**: Swipe **Left** on a task item to permanently remove it from the database.
*   **Status Management**: Toggle tasks as completed, applying visual indicators (strikethrough) and updating timestamps.
*   **Chronological Grouping**: Tasks are dynamically sorted and grouped by date (e.g., "Today", specific dates), providing a structured view of the timeline.
*   **Local Persistence**: Utilizes **SQLite** for reliable offline storage, ensuring data integrity across app sessions.

## 🏗️ Architecture: MVC

The project is structured around the Model-View-Controller pattern to decouple data processing from the user interface.

### 1. Model (Data Layer)
Encapsulates the application's data and business logic.
*   **`Task.java`**: POJO (Plain Old Java Object) representing the data entity.
*   **`DatabaseHelper.java`**: Extends `SQLiteOpenHelper` to manage database creation, version management, and CRUD (Create, Read, Update, Delete) execution.

### 2. View (UI Layer)
Responsible for rendering the user interface and observing user interactions.
*   **`TaskAdapter.java`**: A custom `RecyclerView.Adapter` that handles view binding, data presentation, and multiple view types (Headers vs. Task Items).
*   **XML Layouts**: Modularized layout files for activities, list items, and dialogs.

### 3. Controller (Logic Layer)
Orchestrates the flow of data between the Model and the View.
*   **`MainActivity.java`**: Acts as the central controller. It initializes dependencies, handles `ItemTouchHelper` callbacks for gestures, manages dialog interactions, and updates the data model based on user input.

## 📂 Project Structure

```
com.example.kiarx_test
├── DatabaseHelper.java      // Data Persistence Layer
├── MainActivity.java        // Primary Controller
├── SplashActivity.java      // Entry Point
├── Task.java                // Data Model
├── TaskAdapter.java         // RecyclerView Adapter
├── HeaderItem.java          // View Type: Header
├── TaskItem.java            // View Type: Task
└── ListItem.java            // Abstract Base for List Items
```

## 🛠️ Tech Stack

*   **Language**: Java
*   **Minimum SDK**: API Level 24 (Android 7.0)
*   **Architecture**: MVC
*   **Database**: SQLite
*   **Key Components**:
    *   `RecyclerView` (Complex List Handling)
    *   `CardView` (UI Container)
    *   `ItemTouchHelper` (Gesture Recognition)

---
*Submitted for KiarX Test Assessment.*
