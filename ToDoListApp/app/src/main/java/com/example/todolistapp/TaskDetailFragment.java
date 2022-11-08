package com.example.todolistapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

public class TaskDetailFragment extends Fragment {
    private static final String TAG = "PhapNguyen in TaskDetailFragment";
    private static final String SELECTED_TASK = "selectedTask";
    private static final String TASK_LIST = "taskList";
    private static final String SELECTED_TASK_ID = "selectedTaskId";

    public TaskDetailFragment() {
        // Required empty public constructor
    }

    private TaskDetailFragmentListener listener;
    private Task selectedTask;
    private ArrayList<Task> taskList;
    private int selectedTaskId;

    static interface TaskDetailFragmentListener {
        public void onSaveTaskDetail();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        listener = (TaskDetailFragmentListener) context;
    }

    @NonNull
    public static TaskDetailFragment newInstance(Task selectedTask, ArrayList<Task> taskList, int selectedTaskId) {
        TaskDetailFragment newTaskDetailFragment = new TaskDetailFragment();

        Bundle args = new Bundle();
        args.putParcelable(SELECTED_TASK, selectedTask);
        args.putParcelableArrayList(TASK_LIST, taskList);
        args.putInt(SELECTED_TASK_ID, selectedTaskId);
        newTaskDetailFragment.setArguments(args);

        return newTaskDetailFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        Log.d(TAG, "savedInstanceState != null: " + (savedInstanceState != null));
        Log.d(TAG, "args != null: " + (args != null));

        if (args != null) {
            selectedTask = getArguments().getParcelable(SELECTED_TASK);
            taskList = getArguments().getParcelableArrayList(TASK_LIST);
            selectedTaskId = getArguments().getInt(SELECTED_TASK_ID);
        }
    }

    // This is the onCreateView() method. It's called when Android needs the fragment's layout
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView in TaskDetailFragment is CALLED: ");

        // The onCreateView() method returns a View object that represents the fragment's user
        // interface.

        // The first parameter is a LayoutInflator that you can use to inflate the fragment’s layout.
        // Inflating the layout turns XML views into Java objects
        // Inflate the layout for this fragment
        // This is the fragment equivalent of calling an activity’s setContentView() method.
        // You use it to say what layout the fragment should use, in this case R.layout.fragment_workout_detail.

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task_detail, container, false);

        // The second parameter is a ViewGroup. This is the ViewGroup in the activity’s layout that
        // will contain the fragment.

        // The final parameter is a Bundle. This is used if you’ve previously saved the fragment’s
        // state, and want to reinstate it.
    }

    // We need to update the views of this fragment with details of the task
    // when the activity becomes visible,
    // so we’ll use the fragment’s onStart() method.
    @Override
    public void onStart() {
        super.onStart();

        // The getView() method gets the fragment's root View.
        // We use this to get references to the workout title and description TextViews.
        // Can't call findViewById directly because fragments are not views
        View view = getView();
        if (view != null) {
            TextView tvTaskTitle = view.findViewById(R.id.etTaskTitle);
            tvTaskTitle.setText(selectedTask.getTitle());   // TODO: Debug NullPtrException right here
            TextView tvDescription = view.findViewById(R.id.etTaskDescription);
            tvDescription.setText(selectedTask.getDescription());
            String taskStatus = selectedTask.getStatus();
            if (taskStatus.equals(Task.TODO)) {
                ((RadioButton) view.findViewById(R.id.radBtnTODO)).setChecked(true);
            }
            else if (taskStatus.equals(Task.DOING)) {
                ((RadioButton) view.findViewById(R.id.radBtnDOING)).setChecked(true);
            }
            else if (taskStatus.equals(Task.DONE)) {
                ((RadioButton) view.findViewById(R.id.radBtnDONE)).setChecked(true);
            }

            Button btnSaveTask = getView().findViewById(R.id.btnSaveTask);
            btnSaveTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveTask();
                }
            });


        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putParcelable(SELECTED_TASK, selectedTask);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            selectedTask = savedInstanceState.getParcelable(SELECTED_TASK);
        }
    }

    private void saveTask() {
        View view = getView();
        if (view != null) {
            EditText etTaskTitle = view.findViewById(R.id.etTaskTitle);
            selectedTask.setTitle(String.valueOf(etTaskTitle.getText()));
            EditText etTaskDescription = view.findViewById(R.id.etTaskDescription);
            selectedTask.setDescription(String.valueOf(etTaskDescription.getText()));
            if (((RadioButton) view.findViewById(R.id.radBtnTODO)).isChecked()) {
                selectedTask.setStatus(Task.TODO);
            }
            else if (((RadioButton) view.findViewById(R.id.radBtnDOING)).isChecked()) {
                selectedTask.setStatus(Task.DOING);
            }
            else if (((RadioButton) view.findViewById(R.id.radBtnDONE)).isChecked()) {
                selectedTask.setStatus(Task.DONE);
            }

            if (listener != null) {
                listener.onSaveTaskDetail();
            }
        }
    }

    public void setSelectedTask(Task selectedTask) {
        this.selectedTask = selectedTask;
    }
}