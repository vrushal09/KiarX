# MyToDo App

A simple, clean, and efficient To-Do List Android application developed in Java using the **MVC (Model-View-Controller)** architecture. The app focuses on a user-friendly experience with modern gestures like swiping to manage tasks.

**Developed by:** MehtaVrushal

## 📱 Features

*   **Task Management**:
    *   **Add Task**: Create new tasks with a title and optional details.
    *   **Edit Task**: Swipe **Right** on any task to edit its content.
    *   **Delete Task**: Swipe **Left** on any task to delete it permanently.
    *   **Mark as Completed**: Toggle the checkbox to mark a task as done (applies strikethrough styling).
*   **Organization**:
    *   **Date Grouping**: Tasks are automatically grouped by date (e.g., "Today", "Wednesday, Oct 25, 2023").
    *   **Timestamps**: Displays the exact time a task was added or completed.
*   **Data Persistence**: All tasks are stored locally using **SQLite**, ensuring data is saved even when the app is closed.
*   **Modern UI/UX**:
    *   Splash Screen on startup.
    *   Clean CardView layout for tasks.
    *   Intuitive Swipe Gestures (Green/Teal for Edit, Red for Delete).
    *   Edge-to-edge design support.

## 🏗️ Architecture: MVC

The application strictly follows the Model-View-Controller pattern to ensure separation of concerns and maintainability.

### 1. Model (Data Layer)
Responsible for handling the data logic and database interactions.
*   **`Task.java`**: POJO class representing a task entity (ID, Title, Details, Completed Status, Timestamps).
*   **`DatabaseHelper.java`**: Manages SQLite database creation, upgrades, and CRUD operations (`addTask`, `updateTask`, `deleteTask`, `getAllTasks`).

### 2. View (UI Layer)
Responsible for displaying data to the user and capturing interactions.
*   **Layouts**:
    *   `activity_main.xml`: Main screen with RecyclerView and Floating Action Button.
    *   `item_task.xml`: Layout for individual task items (CardView).
    *   `item_header.xml`: Layout for date headers.
    *   `dialog_add_edit_task.xml`: Dialog for inputting task details.
*   **`TaskAdapter.java`**: A custom RecyclerView adapter that:
    *   Binds data to views.
    *   Handles view types (Headers vs. Tasks).
    *   Manages UI updates like strikethrough text for completed tasks.

### 3. Controller (Logic Layer)
Acts as the bridge between the Model and the View.
*   **`MainActivity.java`**:
    *   Initializes the `DatabaseHelper` and `TaskAdapter`.
    *   Listens for user actions (swipes, clicks).
    *   Updates the Model (database) based on user input.
    *   Refreshes the View (adapter) when data changes.
    *   Handles Swipe Gestures logic using `ItemTouchHelper`.

## 📂 Project Structure

```
com.example.kiarx_test
├── DatabaseHelper.java      // SQLite Database Handler (Model)
├── HeaderItem.java          // Helper for Header rows in RecyclerView
├── ListItem.java            // Abstract base class for RecyclerView items
├── MainActivity.java        // Main Controller Activity
├── SplashActivity.java      // Entry point / Splash Screen
├── Task.java                // Task Data Model
├── TaskAdapter.java         // RecyclerView Adapter (View Logic)
└── TaskItem.java            // Helper for Task rows in RecyclerView
```

## 🛠️ Tech Stack

*   **Language**: Java
*   **Minimum SDK**: 24 (Android 7.0)
*   **Components**:
    *   `RecyclerView`: For efficient list display.
    *   `SQLite`: For local data storage.
    *   `CardView`: For UI elements.
    *   `ItemTouchHelper`: For implementing swipe gestures.

## 🚀 How to Run

1.  Open the project in **Android Studio**.
2.  Sync Gradle files to ensure dependencies are downloaded.
3.  Connect an Android device or start an Emulator.
4.  Run the application.

---
*Created for KiarX Test Assessment.*
