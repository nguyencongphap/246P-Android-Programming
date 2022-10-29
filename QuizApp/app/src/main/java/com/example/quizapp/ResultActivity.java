package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    private static final String TAG = "PhapNguyen";
    private boolean userPassedQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        userPassedQuiz = getIntent().getBooleanExtra("userPassedQuiz", false);
        Log.d(TAG, "userPassedQuiz: " + userPassedQuiz);

        Button btnRetakeQuiz = findViewById(R.id.btnRetakeQuiz);
        btnRetakeQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retakeQuiz();
            }
        });

        Button btnShowStats = findViewById(R.id.btnShowStats);
        btnShowStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStats();
            }
        });

        displayQuizResultMessage();
    }

    private void showStats() {
        String statsText = "You passed "
                + MultipleChoiceActivity.getPassCount()
                + " times\n"
                + "You failed "
                + MultipleChoiceActivity.getFailCount()
                + " times\n";
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT, statsText);
        startActivity(Intent.createChooser(i, "Show statistics using"));
    }

    private void retakeQuiz() {
        Intent i = new Intent(this, MultipleChoiceActivity.class);
        startActivity(i);
    }

    private void displayQuizResultMessage() {
        TextView tvQuizResultMessage = findViewById(R.id.tvQuizResultMessage);
        if (userPassedQuiz) {
            tvQuizResultMessage.setText("You have PASSED this quiz");
        }
        else {
            tvQuizResultMessage.setText("You have FAILED this quiz");
        }
//        tvQuizResultMessage.append("\nYou passed "
//                + MultipleChoiceActivity.getPassCount()
//                + " times\n"
//                + "You failed "
//                + MultipleChoiceActivity.getFailCount()
//                + " times\n"
//        );
    }


}