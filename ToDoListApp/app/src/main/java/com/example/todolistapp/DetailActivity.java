package com.example.todolistapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class DetailActivity extends AppCompatActivity {

    public static final String CLICKED_WORKOUT_ID = "clicked_workout_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        WorkoutDetailFragment fragWorkoutDetail = (WorkoutDetailFragment) getSupportFragmentManager().findFragmentById(R.id.fragTaskDetailContainer);
        int clickedWorkoutId = (int) getIntent().getExtras().get(CLICKED_WORKOUT_ID);
        fragWorkoutDetail.setWorkoutId(clickedWorkoutId);
    }
}