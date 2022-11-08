package com.example.todolistapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class TaskDetailActivity extends AppCompatActivity
        implements TaskDetailFragment.TaskDetailFragmentListener {

    public static final String SELECTED_TASK = "selectedTask";

    private TaskDetailActivityViewModel taskDetailActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

//        taskDetailActivityViewModel = new ViewModelProvider(this).get(TaskDetailActivityViewModel.class);
//        taskDetailActivityViewModel.setSelectedTask(getIntent().getParcelableExtra(SELECTED_TASK));

        TaskDetailFragment taskDetailFragment = (TaskDetailFragment) getSupportFragmentManager().findFragmentById(R.id.fragTaskDetailContainer);
        taskDetailFragment.setSelectedTask(getIntent().getParcelableExtra(SELECTED_TASK));
    }


    @Override
    public void onSaveTaskDetail() {
        // TODO: this is for mobile version, pass param back to mainActivity, navigate there
        return;
    }
}