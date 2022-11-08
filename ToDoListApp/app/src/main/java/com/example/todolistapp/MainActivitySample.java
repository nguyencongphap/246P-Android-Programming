package com.example.todolistapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivitySample extends AppCompatActivity
        implements WorkoutListFragment.Listener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_sample);

    }

    // Provide the implementation for the method of the WorkoutListFragment.Listener interface
    @Override
    public void itemClicked(long id) {
        View fragmentContainer = findViewById(R.id.fragTaskDetailContainer);
        if (fragmentContainer != null) {    // Using tablet version layout
            // Create a new fragment everytime the user clicks
            WorkoutDetailFragment workoutDetailFragment = new WorkoutDetailFragment();
            // WorkoutDetailFragment’s views are updated with details
            // of the workout that was selected using the id passed into itemClicked
            workoutDetailFragment.setWorkoutId(id);

            // Add the fragment to the FrameLayout fragmentContainer
            // add, replace, or remove fragments at runtime using a fragment transaction.
            // Begin transaction
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Say what chadnges the transaction should include.
            transaction.replace(R.id.fragTaskDetailContainer, workoutDetailFragment);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

            // you can use the addToBackStack() method to add the transaction to the back stack.
            // This method takes a String name to label the transaction to programmatically retrieve
            // the transaction. Most of the time you won’t need to do this.
            transaction.addToBackStack(null);

            // Finally, you need to commit the transaction. This finishes the transaction,
            // and applies the changes you specified.
            transaction.commit();
        }
        else {  // Using mobile version layout
            // Pass the row ID of the item clicked received from the fragment to WorkoutDetailActivity
            Intent i = new Intent(this, DetailActivity.class);
            i.putExtra(DetailActivity.CLICKED_WORKOUT_ID, (int) id);
            startActivity(i);
        }

    }
}