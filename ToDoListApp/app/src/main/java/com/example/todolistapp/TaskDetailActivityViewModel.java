package com.example.todolistapp;

import android.util.Log;

import androidx.lifecycle.ViewModel;

public class TaskDetailActivityViewModel extends ViewModel {
    private static final String TAG = "PhapNguyen in TaskDetailActivityViewModel";
    private Task selectedTask;

    public TaskDetailActivityViewModel() {
        Log.d(TAG, "TaskDetailActivityViewModel is created: ");
    }

    public Task getSelectedTask() {
        return selectedTask;
    }

    public void setSelectedTask(Task selectedTask) {
        this.selectedTask = selectedTask;
    }
}
