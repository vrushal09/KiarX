package com.example.kiarx_test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

// Model Layer: Handles all database interactions (SQLite)
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ToDoList.db";
    private static final int DATABASE_VERSION = 2; // Incremented version for schema change

    private static final String TABLE_TASKS = "tasks";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DETAILS = "details";
    private static final String COLUMN_IS_COMPLETED = "is_completed";
    private static final String COLUMN_ADDED_TIME = "added_time";
    private static final String COLUMN_COMPLETED_TIME = "completed_time";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_TASKS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DETAILS + " TEXT, " +
                COLUMN_IS_COMPLETED + " INTEGER, " +
                COLUMN_ADDED_TIME + " INTEGER, " +
                COLUMN_COMPLETED_TIME + " INTEGER)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_TASKS + " ADD COLUMN " + COLUMN_ADDED_TIME + " INTEGER DEFAULT 0");
            db.execSQL("ALTER TABLE " + TABLE_TASKS + " ADD COLUMN " + COLUMN_COMPLETED_TIME + " INTEGER DEFAULT 0");
        }
    }

    // Create a new task
    public long addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, task.getTitle());
        values.put(COLUMN_DETAILS, task.getDetails());
        values.put(COLUMN_IS_COMPLETED, task.isCompleted() ? 1 : 0);
        values.put(COLUMN_ADDED_TIME, task.getAddedTime());
        values.put(COLUMN_COMPLETED_TIME, task.getCompletedTime());
        long id = db.insert(TABLE_TASKS, null, values);
        db.close();
        return id;
    }

    // Read all tasks
    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TASKS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
                String details = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DETAILS));
                boolean isCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_COMPLETED)) == 1;
                long addedTime = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ADDED_TIME));
                long completedTime = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_COMPLETED_TIME));
                taskList.add(new Task(id, title, details, isCompleted, addedTime, completedTime));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return taskList;
    }

    // Update an existing task
    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, task.getTitle());
        values.put(COLUMN_DETAILS, task.getDetails());
        values.put(COLUMN_IS_COMPLETED, task.isCompleted() ? 1 : 0);
        values.put(COLUMN_ADDED_TIME, task.getAddedTime());
        values.put(COLUMN_COMPLETED_TIME, task.getCompletedTime());

        int rowsAffected = db.update(TABLE_TASKS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(task.getId())});
        db.close();
        return rowsAffected;
    }

    // Delete a task
    public void deleteTask(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}
