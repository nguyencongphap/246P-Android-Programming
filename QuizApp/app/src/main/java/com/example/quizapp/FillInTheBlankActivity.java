package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FillInTheBlankActivity extends AppCompatActivity {

    private static final String TAG = "PhapNguyen";
    private static int availableAttempts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_in_the_blank);

        availableAttempts = 2;

        // Set onClickListener to the submit answer button
        Button btnSubmit = findViewById(R.id.btnSubmitFillTheBlank);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSubmitAnswer();
            }
        });
    }

    private void onClickSubmitAnswer() {
        EditText etFITBUserAnswer = (EditText) findViewById(R.id.etFITBUserAnswer);
        String userAnswer = etFITBUserAnswer.getText().toString().toLowerCase();
        String solution = getString(R.string.fitb_solution).toLowerCase();

        if (userAnswer.equals(solution)) {
            MultipleChoiceActivity.incrementPassCount();

//            load ResultActivity that user has PASSED
            Intent i = new Intent(this, ResultActivity.class);
            i.putExtra("userPassedQuiz", true);
            startActivity(i);
        }
        else {
            availableAttempts--;
            if (availableAttempts == 0) {
                MultipleChoiceActivity.incrementFailCount();

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
}