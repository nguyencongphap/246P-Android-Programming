package com.example.todolistapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddNewTaskActivity extends AppCompatActivity {

    private EditText etNewTaskTitle;
    private EditText etNewTaskDescription;
    private Button btnCreateNewTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_task);

        etNewTaskTitle = findViewById(R.id.etNewTaskTitle);
        etNewTaskDescription = findViewById(R.id.etNewTaskDescription);
        btnCreateNewTask = findViewById(R.id.btnCreateNewTask);
        btnCreateNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitNewTaskForAdding();
            }
        });

    }

    private void submitNewTaskForAdding() {
        String title = String.valueOf(etNewTaskTitle.getText());
        String value = String.valueOf(etNewTaskDescription.getText());
        if (title != null) {
            Task newTask = new Task(title, value);

            Intent i = new Intent(this, MainActivity.class);
            // Pass relevant data back as a result
            i.putExtra(MainActivity.NEW_TASK, newTask);
            // Activity finished ok, return the data
            setResult(RESULT_OK, i); // set result code and bundle data for response
            finish(); // closes the activity, pass data to parent
        }
    }
}