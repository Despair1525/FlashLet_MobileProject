package com.example.prm_final_project.Ui.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.prm_final_project.R;

public class ResultWrittenChoiceActivity extends AppCompatActivity {
    private TextView Correct, Wrong;
    private Button RestartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_multiple_choice);
        init();
        StringBuffer sb = new StringBuffer();
        sb.append("Correct answers: " + WrittenQuizActivity.correct + "\n");
        StringBuffer sb2 = new StringBuffer();
        sb2.append("Wrong Answers: " + WrittenQuizActivity.wrong + "\n");
        Correct.setText(sb);
        Wrong.setText(sb2);

        WrittenQuizActivity.correct=0;
        WrittenQuizActivity.wrong=0;
        RestartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(in);
            }
        });
    }

    public void init() {
        Correct = findViewById(R.id.Correct);
        Wrong = findViewById(R.id.Wrong);
        RestartButton = findViewById(R.id.btnRestart);
    }
}