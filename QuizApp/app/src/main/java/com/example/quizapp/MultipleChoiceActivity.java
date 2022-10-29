package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class MultipleChoiceActivity extends AppCompatActivity {

    private static final String TAG = "PhapNguyen";

    private static int availableAttempts;
    private static int passCount = 0;
    private static int failCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_choice);

        availableAttempts = getResources().getStringArray(R.array.mc_answers).length - 2;

        // Set onClickListener to the submit answer button
        Button btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSubmitAnswer();
            }
        });
    }

    private void onClickSubmitAnswer() {

        Spinner MCAnswersSpinner = findViewById(R.id.MCAnswersSpinner);
        String choice = String.valueOf(MCAnswersSpinner.getSelectedItem());
        String solution = getString(R.string.mc_solution);

        if (choice.equals(solution)) {
            Intent i = new Intent(this, FillInTheBlankActivity.class);
            startActivity(i);
        }
        else {
            availableAttempts--;
            if (availableAttempts == 0) {
                incrementFailCount();
                // load ResultActivity that user has FAILED
                Intent i = new Intent(this, ResultActivity.class);
                i.putExtra("userPassedQuiz", false);
                startActivity(i);
            }
            else {
                Toast.makeText(getApplicationContext(), "You have "
                + availableAttempts + " attempts remaining", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void incrementPassCount() {
        passCount++;
    }

    public static void incrementFailCount() {
        failCount++;
    }

    public static int getPassCount() {
        return passCount;
    }

    public static int getFailCount() {
        return failCount;
    }
}