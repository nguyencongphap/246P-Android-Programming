package com.example.todolistapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class WorkoutDetailFragment extends Fragment {
    public WorkoutDetailFragment() {
        // Required empty public constructor
        // Android uses it to reinstantiate the fragment when needed, and if it’s not there,
        // you’ll get a runtime exception.
    }

    private long workoutId;

    // This is the onCreateView() method. It's called when Android needs the fragment's layout
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // The onCreateView() method returns a View object that represents the fragment's user
        // interface.

        // The first parameter is a LayoutInflator that you can use to inflate the fragment’s layout.
        // Inflating the layout turns XML views into Java objects
        // Inflate the layout for this fragment
        // This is the fragment equivalent of calling an activity’s setContentView() method.
        // You use it to say what layout the fragment should use, in this case R.layout.fragment_workout_detail.
        return inflater.inflate(R.layout.fragment_workout_detail, container, false);

        // The second parameter is a ViewGroup. This is the ViewGroup in the activity’s layout that
        // will contain the fragment.

        // The final parameter is a Bundle. This is used if you’ve previously saved the fragment’s
        // state, and want to reinstate it.
    }

    public void setWorkoutId(long workoutId) {
        this.workoutId = workoutId;
    }

    @Override
    public void onStart() {
        super.onStart();

        // The getView() method gets the fragment's root View.
        // We use this to get references to the workout title and description TextViews.
        View view = getView();
        if (view != null) {
            TextView title = view.findViewById(R.id.tvWorkoutTitle);
            Workout workout = Workout.workouts[(int) workoutId];
            title.setText(workout.getName());
            TextView description = (TextView) view.findViewById(R.id.tvDescription);
            description.setText(workout.getDescription());
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putLong("workoutId", workoutId);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            workoutId = savedInstanceState.getLong("workoutId");
        }
    }
}