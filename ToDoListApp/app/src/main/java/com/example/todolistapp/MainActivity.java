package com.example.todolistapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements TaskListFragment.TaskListFragmentListener,
        TaskDetailFragment.TaskDetailFragmentListener
{

    private static final String TAG = "PhapNguyen from MainActivity";
    public static final String NEW_TASK = "newTask";

    private TaskListFragmentViewModel taskListFragmentViewModel;
    private Button btnAddTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate in MainActivity: ");

        // Connect ViewModel to activity so that ViewModel stays alive until activity's finish()
        // is called. Without this, when the fragment is detached during activity's onDestroy()
        // the ViewModel will be destroyed and a new ViewModel will be created.
        if (taskListFragmentViewModel == null) {
            taskListFragmentViewModel = new ViewModelProvider(MainActivity.this).get(TaskListFragmentViewModel.class);
        }
        else {
            Log.d(TAG, "ViewModel is ALREADY Initialized");
        }

        btnAddTask = findViewById(R.id.btnStartAddNewTaskActivity);
        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AddNewTaskActivity.class);
                addNewTaskActivityResultLauncher.launch(i);
            }
        });

    }

    ActivityResultLauncher<Intent> addNewTaskActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // If the user comes back to this activity from AddNewTaskActivity
                    // with no error or cancellation
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        // Get the data passed from AddNewTaskActivity
                        Task newTask = data.getParcelableExtra(NEW_TASK);
                        addNewTaskToTaskList(newTask);
                    }
                }
            }
    );

    @Override
    public void itemClicked(long id) {
        Log.d(TAG, "itemClicked in MainActivity: ");

        // param id is the row ID of the item clicked received from the fragment
        Task selectedTask = taskListFragmentViewModel.getTaskList().get((int) id);

        View fragTaskDetailContainer = findViewById(R.id.fragTaskDetailContainer);
        if (fragTaskDetailContainer != null) { // Tablet version flow
            // Create a new fragment everytime the user clicks and add it to the FrameLayout
            TaskDetailFragment taskDetailFragment = TaskDetailFragment
                    .newInstance(selectedTask, taskListFragmentViewModel.getTaskList(), (int) id);

//            // Populate data of the selected task into the views of taskDetailFragment
//            taskDetailFragment.setSelectedTask(selectedTask);

            // Add the fragment to the FrameLayout fragmentContainer
            // add, replace, or remove fragments at runtime using a fragment transaction.
            // Begin transaction
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Say what changes the transaction should include.
            transaction.replace(R.id.fragTaskDetailContainer, taskDetailFragment);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

            // you can use the addToBackStack() method to add the transaction to the back stack.
            // This method takes a String name to label the transaction to programmatically retrieve
            // the transaction. Most of the time you won’t need to do this.
            transaction.addToBackStack(null);

            // Finally, you need to commit the transaction. This finishes the transaction,
            // and applies the changes you specified.
            transaction.commit();
        }
        else { // Mobile version flow
            Intent i = new Intent(MainActivity.this, TaskDetailActivity.class);
            i.putExtra(TaskDetailActivity.SELECTED_TASK, selectedTask);
            startActivity(i);
        }
    }

    @Override
    public ArrayList<Task> getTaskList() {
        Log.d(TAG, "taskListFragmentViewModel == null: " + (taskListFragmentViewModel == null));
        if (taskListFragmentViewModel == null) {
            taskListFragmentViewModel = new ViewModelProvider(MainActivity.this).get(TaskListFragmentViewModel.class);
        }
        Log.d(TAG, "Finished getTaskList in MainActivity: ");
        return taskListFragmentViewModel.getTaskList();
    }

    private void addNewTaskToTaskList(Task newTask) {
        taskListFragmentViewModel.addTask(newTask);

        Log.d(TAG, "Finished addNewTaskToTaskList in MainActivity: ");
    }

    @Override
    public void onSaveTaskDetail() {
        TaskListFragment taskListFragment = (TaskListFragment) getSupportFragmentManager().findFragmentById(R.id.fragTaskListContainer);
        taskListFragment.getTaskAdapter().notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy in MainActivity: ");
    }


}