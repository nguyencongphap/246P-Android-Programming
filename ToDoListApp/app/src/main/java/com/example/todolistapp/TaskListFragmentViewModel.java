package com.example.todolistapp;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class TaskListFragmentViewModel extends ViewModel {
    private static final String TAG = "PhapNguyen in TaskListFragmentViewModel";
    private ArrayList<Task> taskList= new ArrayList<>();

    public TaskListFragmentViewModel() {
        Log.d(TAG, "TaskListFragmentViewModel is created: ");
    }

    public ArrayList<Task> getTaskList() {
        return taskList;
    }

    public void addTask(Task newTask) {
        taskList.add(newTask);
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        Log.d(TAG, "onCleared: ViewModel is CLEARED!!");
    }
}
