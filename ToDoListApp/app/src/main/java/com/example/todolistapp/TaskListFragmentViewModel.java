package com.example.todolistapp;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class TaskListFragmentViewModel extends ViewModel {
    private static final String TAG = "PhapNguyen in TaskListFragmentViewModel";
    private ArrayList<Task> taskList= new ArrayList<>();
    private String taskListSortKey;
    private String taskListSortOrder;
    private boolean usingTabletLayout = false;
    private String titleSearchQuery = "";

    public TaskListFragmentViewModel() {
        Log.d(TAG, "TaskListFragmentViewModel is created: ");
    }

    public ArrayList<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(ArrayList<Task> taskList) {
        this.taskList = taskList;
    }

    public String getTaskListSortKey() {
        return taskListSortKey;
    }

    public void setTaskListSortKey(String taskListSortKey) {
        this.taskListSortKey = taskListSortKey;
    }

    public String getTaskListSortOrder() {
        return taskListSortOrder;
    }

    public void setTaskListSortOrder(String taskListSortOrder) {
        this.taskListSortOrder = taskListSortOrder;
    }

    public boolean isUsingTabletLayout() {
        return usingTabletLayout;
    }

    public void setUsingTabletLayout(boolean usingTabletLayout) {
        this.usingTabletLayout = usingTabletLayout;
    }

    public String getTitleSearchQuery() {
        return titleSearchQuery;
    }

    public void setTitleSearchQuery(String titleSearchQuery) {
        this.titleSearchQuery = titleSearchQuery;
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
