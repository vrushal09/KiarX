package com.example.kiarx_test;

// Model Layer: Represents the data structure for a task
public class Task {
    private int id;
    private String title;
    private String details;
    private boolean isCompleted;
    private long addedTime;
    private long completedTime;

    public Task(int id, String title, String details, boolean isCompleted, long addedTime, long completedTime) {
        this.id = id;
        this.title = title;
        this.details = details;
        this.isCompleted = isCompleted;
        this.addedTime = addedTime;
        this.completedTime = completedTime;
    }

    public Task(String title, String details, boolean isCompleted, long addedTime) {
        this.title = title;
        this.details = details;
        this.isCompleted = isCompleted;
        this.addedTime = addedTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public long getAddedTime() {
        return addedTime;
    }

    public void setAddedTime(long addedTime) {
        this.addedTime = addedTime;
    }

    public long getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(long completedTime) {
        this.completedTime = completedTime;
    }
}
