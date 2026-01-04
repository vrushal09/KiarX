package com.example.kiarx_test;

public class TaskItem extends ListItem {
    private Task task;

    public TaskItem(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return task;
    }

    @Override
    int getType() {
        return TYPE_TASK;
    }
}
