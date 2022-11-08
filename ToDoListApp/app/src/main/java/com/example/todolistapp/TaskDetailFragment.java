package com.example.todolistapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

public class TaskDetailFragment extends Fragment {
    private static final String TAG = "PhapNguyen in TaskDetailFragment";
    private static final String SELECTED_TASK = "selectedTask";

    public TaskDetailFragment() {
        // Required empty public constructor
    }

    private Task selectedTask;
    private TaskListFragmentViewModel taskListFragmentViewModel;

    @NonNull
    public static TaskDetailFragment newInstance(Task selectedTask) {
        TaskDetailFragment newTaskDetailFragment = new TaskDetailFragment();

        Bundle args = new Bundle();
        args.putParcelable(SELECTED_TASK, selectedTask);
        newTaskDetailFragment.setArguments(args);

        return newTaskDetailFragment;
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
            tvTaskTitle.setText(selectedTask.getTitle());
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

    public void setSelectedTask(Task selectedTask) {
        this.selectedTask = selectedTask;
    }
}