package com.example.todolistapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class TaskDetailActivity extends AppCompatActivity
        implements TaskDetailFragment.TaskDetailFragmentListener {

    public static final String SELECTED_TASK = "selectedTask";
    public static final String SELECTED_TASK_ID = "selectedTaskId";
    public static final String UPDATED_TASK = "updatedTask";
    public static final String TASK_LIST_CHANGED = "taskListChanged";
    public static final String TASK_IS_UPDATED = "taskIsUpdated";
    public static final String TASK_IS_REMOVED = "taskIsRemoved";

    private TaskDetailActivityViewModel taskDetailActivityViewModel;
    private Task selectedTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        TaskDetailFragment taskDetailFragment = (TaskDetailFragment) getSupportFragmentManager().findFragmentById(R.id.fragTaskDetailContainer);
        selectedTask = getIntent().getParcelableExtra(SELECTED_TASK);
        taskDetailFragment.setSelectedTask(selectedTask);
        int selectedTaskId = getIntent().getIntExtra(SELECTED_TASK_ID, -1);
        taskDetailFragment.setSelectedTaskId(selectedTaskId);
    }

    @Override
    public void onUpdateTaskDetail(Task newTask, int selectedTaskId) {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra(TASK_IS_UPDATED, true);
        i.putExtra(UPDATED_TASK, newTask);
        i.putExtra(SELECTED_TASK_ID, selectedTaskId);
        setResult(Activity.RESULT_OK, i);
        finish();
    }

    @Override
    public void onRemoveTask(int selectedTaskId) {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra(TASK_IS_REMOVED, true);
        i.putExtra(SELECTED_TASK_ID, selectedTaskId);
        setResult(Activity.RESULT_OK, i);
        finish();
    }
}