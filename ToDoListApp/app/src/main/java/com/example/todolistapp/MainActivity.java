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
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements TaskListFragment.TaskListFragmentListener,
        TaskDetailFragment.TaskDetailFragmentListener
{

    private static final String TAG = "PhapNguyen from MainActivity";

    private static class taskListSortSetting {
        private static final int TIME_CREATED_SORT_KEY_SELECTION = 0;
        private static final int TITLE_SORT_KEY_SELECTION = 1;
        private static final int ASCENDING_SORT_ORDER_SELECTION = 0;
        private static final int DESCENDING_SORT_ORDER_SELECTION = 1;
        public static final String TIME_CREATED_SORT_KEY = "_id";
        public static final String TITLE_SORT_KEY = "TITLE";
        private static final String ASCENDING_SORT_ORDER = "ASC";
        private static final String DESCENDING_SORT_ORDER = "DESC";
    }

    public static final int INVALID_TASK_ID = -1;
    public static final String NEW_TASK = "newTask";

    private TaskListFragmentViewModel taskListFragmentViewModel;
    private Button btnAddTask;
    private Task selectedTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate in MainActivity: ");

        // Connect ViewModel to activity so that ViewModel stays alive until activity's finish()
        // is called.
        taskListFragmentViewModel = new ViewModelProvider(MainActivity.this).get(TaskListFragmentViewModel.class);

        btnAddTask = findViewById(R.id.btnStartAddNewTaskActivity);
        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AddNewTaskActivity.class);
                addNewTaskActivityResultLauncher.launch(i);
            }
        });

        Spinner spinnerTaskListSortKeys = findViewById(R.id.spinnerTaskListSortKeys);
//        // Create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.task_list_sorting_keys, android.R.layout.simple_spinner_dropdown_item);
//        // Apply the adapter to the spinner
//        spinnerTaskListSortKeys.setAdapter(adapter);
        taskListFragmentViewModel.setTaskListSortKey(taskListSortSetting.TIME_CREATED_SORT_KEY);
        spinnerTaskListSortKeys.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Log.d(TAG, " spinnerTaskListSortKeys onItemSelected: ");

                if (i == taskListSortSetting.TIME_CREATED_SORT_KEY_SELECTION) {
                    taskListFragmentViewModel.setTaskListSortKey(taskListSortSetting.TIME_CREATED_SORT_KEY);

                }
                else if (i == taskListSortSetting.TITLE_SORT_KEY_SELECTION) {
                    taskListFragmentViewModel.setTaskListSortKey(taskListSortSetting.TITLE_SORT_KEY);
                }
                populateTaskListUsingDatabase();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                taskListFragmentViewModel.setTaskListSortKey(taskListSortSetting.TIME_CREATED_SORT_KEY);
                populateTaskListUsingDatabase();
            }
        });

        Spinner spinnerTaskListSortOrder = findViewById(R.id.spinnerTaskListSortOrder);
        taskListFragmentViewModel.setTaskListSortOrder(taskListSortSetting.ASCENDING_SORT_ORDER);
        spinnerTaskListSortOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, " spinnerTaskListSortOrder onItemSelected: ");

                if (i == taskListSortSetting.ASCENDING_SORT_ORDER_SELECTION) {
                    taskListFragmentViewModel.setTaskListSortOrder(taskListSortSetting.ASCENDING_SORT_ORDER);
                }
                else if (i == taskListSortSetting.DESCENDING_SORT_ORDER_SELECTION) {
                    taskListFragmentViewModel.setTaskListSortOrder(taskListSortSetting.DESCENDING_SORT_ORDER);
                }
                populateTaskListUsingDatabase();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                taskListFragmentViewModel.setTaskListSortOrder(taskListSortSetting.ASCENDING_SORT_ORDER);
                populateTaskListUsingDatabase();
            }
        });

        populateTaskListUsingDatabase();
    }

    private void populateTaskListUsingDatabase() {
        // Construct a list of tasks using data from database
        // We will send this list to the adapter so the views will be updated accordingly
        ArrayList<Task> taskList = new ArrayList<>();
        // Create a cursor
        SQLiteOpenHelper todoDatabaseHelper = new ToDoAppDatabaseHelper(this);
        try {
            SQLiteDatabase db = todoDatabaseHelper.getWritableDatabase();   // we want both read and write access
            String orderByPreference = String.join(" ",
                    taskListFragmentViewModel.getTaskListSortKey(),
                    taskListFragmentViewModel.getTaskListSortOrder()
                    );

            Log.d(TAG, "orderByPreference: " + orderByPreference);

            Cursor cursor = db.query("TASK",
                    new String[] {"_id", "TITLE", "DESCRIPTION", "STATUS"},
                    null,
                    null,
                    null,
                    null,
                    orderByPreference
            );

            // Move to the first record in the Cursor
            if (cursor.moveToFirst()) {

                // Get the drink details from the cursor
                int idNumber = cursor.getInt(0);
                String titleText = cursor.getString(1);
                String descriptionText = cursor.getString(2);
                String statusText = cursor.getString(3);

                // create the Task
                Task newTask = new Task(idNumber, titleText, descriptionText, statusText);
                taskList.add(newTask);

                while (cursor.moveToNext()) {
                    // Get the drink details from the cursor
                    idNumber = cursor.getInt(0);
                    titleText = cursor.getString(1);
                    descriptionText = cursor.getString(2);
                    statusText = cursor.getString(3);

                    // create the Task
                    newTask = new Task(idNumber, titleText, descriptionText, statusText);
                    taskList.add(newTask);
                }
            }
            cursor.close();
            db.close();

        } catch (SQLiteException e) {
            Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT).show();
        }

        taskListFragmentViewModel.setTaskList(taskList);
        TaskListFragment taskListFragment = (TaskListFragment) getSupportFragmentManager().findFragmentById(R.id.fragTaskListContainer);
        // Created a new array list instead of editing the one being bound to list view
        // So, we tell the list view to bind to this newly created array list
        taskListFragment.createBindNewTaskAdapter();
    }

    private void notifyDataSetChangedTaskAdapter() {
        TaskListFragment taskListFragment = (TaskListFragment) getSupportFragmentManager().findFragmentById(R.id.fragTaskListContainer);
        if (taskListFragment != null) {
            TaskAdapter taskAdapter = taskListFragment.getTaskAdapter();
            if (taskAdapter != null) {
                taskAdapter.notifyDataSetChanged();
            }
        }
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
        int selectedTaskId = (int) id;
        selectedTask = taskListFragmentViewModel.getTaskList().get(selectedTaskId);

        View fragTaskDetailContainer = findViewById(R.id.fragTaskDetailContainer);
        if (fragTaskDetailContainer != null) { // Tablet version flow
            taskListFragmentViewModel.setUsingTabletLayout(true);

            // Create a new fragment everytime the user clicks and add it to the FrameLayout
            TaskDetailFragment taskDetailFragment = TaskDetailFragment
                    .newInstance(selectedTask, taskListFragmentViewModel.getTaskList(), selectedTaskId);

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
            taskListFragmentViewModel.setUsingTabletLayout(false);

            Intent i = new Intent(MainActivity.this, TaskDetailActivity.class);
            i.putExtra(TaskDetailActivity.SELECTED_TASK, selectedTask);
            i.putExtra(TaskDetailActivity.SELECTED_TASK_ID, selectedTaskId);
            taskDetailActivityLauncher.launch(i);
        }
    }

    ActivityResultLauncher<Intent> taskDetailActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    handleActivityResultFromTaskDetailActivity(result);
                }
            }
    );

    private void handleActivityResultFromTaskDetailActivity(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            int selectedTaskPosition = data.getIntExtra(TaskDetailActivity.SELECTED_TASK_ID, INVALID_TASK_ID);

            if (data.getBooleanExtra(TaskDetailActivity.TASK_IS_UPDATED, false)) {
                Task updatedTask = data.getParcelableExtra(TaskDetailActivity.UPDATED_TASK);
                if (updatedTask != null && selectedTaskPosition != INVALID_TASK_ID) {
                    Log.d(TAG, "Call onUpdateTaskDetail from handling DetailActivity: ");

                    onUpdateTaskDetail(updatedTask, selectedTaskPosition);
                }
            }
            else if (data.getBooleanExtra(TaskDetailActivity.TASK_IS_REMOVED, false)) {
                if (selectedTaskPosition != INVALID_TASK_ID) {
                    Log.d(TAG, "Call onRemoveTask from handling DetailActivity: ");

                    onRemoveTask(selectedTaskPosition);
                }
            }

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

    private ContentValues generateRowDataFromTask(Task task) {
        ContentValues taskValues = new ContentValues();
        taskValues.put("TITLE", task.getTitle());
        taskValues.put("DESCRIPTION", task.getDescription());
        taskValues.put("STATUS", task.getStatus());
        return taskValues;
    }

    private void addNewTaskToTaskList(Task newTask) {
        taskListFragmentViewModel.addTask(newTask);
        TaskListFragment taskListFragment = (TaskListFragment) getSupportFragmentManager().findFragmentById(R.id.fragTaskListContainer);
        taskListFragment.getTaskAdapter().notifyDataSetChanged();

        Log.d(TAG, "Finished addNewTaskToTaskList in MainActivity: ");

        // Insert the data of newTask to database
        ContentValues taskValues = generateRowDataFromTask(newTask);

        SQLiteOpenHelper todoDatabaseHelper = new ToDoAppDatabaseHelper(this);
        try {
            SQLiteDatabase db = todoDatabaseHelper.getWritableDatabase();
            db.insert("TASK", null, taskValues);
            db.close();
        } catch(SQLiteException e) {
            Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpdateTaskDetail(Task newTask, int selectedTaskPosition) {
        Task selectedTask =  taskListFragmentViewModel.getTaskList().get(selectedTaskPosition);
        taskListFragmentViewModel.getTaskList().set(selectedTaskPosition, newTask);
        TaskListFragment taskListFragment = (TaskListFragment) getSupportFragmentManager().findFragmentById(R.id.fragTaskListContainer);
        taskListFragment.getTaskAdapter().notifyDataSetChanged();

        // Update the corresponding row in database
        int rowIdToUpdate = selectedTask.getId();
        ContentValues taskValues = generateRowDataFromTask(newTask);

        SQLiteOpenHelper todoDatabaseHelper = new ToDoAppDatabaseHelper(this);
        try {
            SQLiteDatabase db = todoDatabaseHelper.getWritableDatabase();
            db.update("TASK", taskValues,
                    "_id=?",
                    new String[] {String.valueOf(rowIdToUpdate)});
            db.close();
        } catch(SQLiteException e) {
            Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRemoveTask(int selectedTaskPosition) {
        Task selectedTask =  taskListFragmentViewModel.getTaskList().get(selectedTaskPosition);
        taskListFragmentViewModel.getTaskList().remove(selectedTaskPosition);
        TaskListFragment taskListFragment = (TaskListFragment) getSupportFragmentManager().findFragmentById(R.id.fragTaskListContainer);
        taskListFragment.getTaskAdapter().notifyDataSetChanged();

        if (taskListFragmentViewModel.isUsingTabletLayout()) {
            getSupportFragmentManager().popBackStack(getSupportFragmentManager().getBackStackEntryAt(0).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        // Delete the corresponding row in database
        int rowIdToUpdate = selectedTask.getId();
        SQLiteOpenHelper todoDatabaseHelper = new ToDoAppDatabaseHelper(this);
        try {
            Log.d(TAG, "entered try block: ");

            SQLiteDatabase db = todoDatabaseHelper.getWritableDatabase();
            int deleteProcessResult = db.delete("TASK",
                    "_id=?",
                    new String[] {String.valueOf(rowIdToUpdate)});

            Log.d(TAG, "deleteProcessResult: " + deleteProcessResult);

            db.close();
        } catch(SQLiteException e) {
            Log.d(TAG, "entered catch block: ");

            Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy in MainActivity: ");
    }


}