package com.example.todolistapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class WorkoutListFragment extends ListFragment {
    // Define an interface here so that any activity that implements this interface
    // can communicate with this fragment and vice versa.
    static interface Listener {
        void itemClicked(long id);
    };

    // Save a reference to the activity to which WorkoutFragment gets attached
    private Listener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Create a String array of the workout names using the static data from Workout class
        String[] names = new String[Workout.workouts.length];
        for (int i=0; i< names.length; i++) {
            names[i] = Workout.workouts[i].getName();
        }

        // Create adapter to bind data source to ListFragment
        // the Fragment class isn’t a subclass of the Context class.
        // use the inflater to get the context
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                inflater.getContext(), android.R.layout.simple_list_item_1, names);
        setListAdapter(adapter);    // Bind the array adapter to the list view

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    // This is the context (in this case, the activity) the fragment is attached to
    // this allows the fragment to communicate with the activity it attaches to
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.listener = (Listener) context;
    }

    // four parameters: the list view, the item in the list that was clicked, its position, and the row ID of the underlying data
    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (listener != null) {
            // Call the listener's implementation of itemClicked, passing it the row ID of the
            // item in the list that was clicked.
            listener.itemClicked(id);
        }
    }


}