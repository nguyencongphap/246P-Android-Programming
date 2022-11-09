package com.example.todolistapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.ListFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

// TODO: Implement Task Adapter
// TODO: Adapter has the built-in addAll() method that takes in an ArrayList<Task> and populate it into Views
// TODO: Put this adapter into ViewModel so it survives throughout lifecycle states

// TODO: Put the data source ArrayList into ViewModel


public class TaskListFragment extends ListFragment {

    // Define an interface here so that any activity that implements this interface
    // can communicate with this fragment and vice versa.
    interface TaskListFragmentListener {
        void itemClicked(long id);
        ArrayList<Task> getTaskList();
    }

    private static final String TAG = "PhapNguyen in TaskListFragment";
    // Save a reference to the activity to which WorkoutFragment gets attached
    private TaskListFragmentListener listener;
    private TaskAdapter taskAdapter;

    // This is the context (in this case, the activity) the fragment is attached to
    // this allows the fragment to communicate with the activity it attaches to
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        listener = (TaskListFragmentListener) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment. Don't need to specify layout file because this
        // fragment extends ListFragment
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Create the adapter to convert the array to views
        ArrayList<Task>  taskList;
        if (listener != null) {
            taskList = listener.getTaskList();

            Log.d(TAG, "taskList.size(): " + taskList.size());
        }
        else {
            taskList = new ArrayList<>();
        }

        taskAdapter = new TaskAdapter(requireActivity(), R.layout.task_list_item, taskList);
        // Bind the array adapter to the list view
        setListAdapter(taskAdapter);
    }

    // four parameters: the list view, the item in the list that was clicked, its position, and the row ID of the underlying data
    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        // Call the listener's implementation of itemClicked, passing it the row ID of the
        // item in the list that was clicked.
        if (listener != null) {
            listener.itemClicked(id);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        Log.d(TAG, "onDetach in TaskListFragment: ");
    }

    public TaskAdapter getTaskAdapter() {
        return taskAdapter;
    }
}