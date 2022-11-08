package com.example.todolistapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

public class TaskDetailActivity extends AppCompatActivity {

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
}