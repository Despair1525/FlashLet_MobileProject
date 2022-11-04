package com.example.prm_final_project.Ui.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.prm_final_project.Model.Deck;
import com.example.prm_final_project.R;

public class ResultWrittenChoiceActivity extends AppCompatActivity {
    private TextView Correct, Wrong;
    private Button RestartButton;
    private Deck deck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_multiple_choice);
        init();

        if ( getIntent().getSerializableExtra("viewDeck") != null){
            deck = (Deck) getIntent().getSerializableExtra("viewDeck");
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(WrittenQuizActivity.title);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        StringBuffer sb = new StringBuffer();
        sb.append("Your score is " + WrittenQuizActivity.correct + " out of " + WrittenQuizActivity.numQuest);



        Correct.setText(sb);

        WrittenQuizActivity.correct=0;
        WrittenQuizActivity.wrong=0;
        RestartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(),ViewCardActivity.class);
                in.putExtra("viewDeck",deck);
                startActivity(in);
                finish();
            }
        });
    }

    public void init() {
        Correct = findViewById(R.id.Correct);
        RestartButton = findViewById(R.id.btnRestart);
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        Intent in = new Intent(getApplicationContext(),ViewCardActivity.class);
        in.putExtra("viewDeck",deck);
        startActivity(in);
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent in = new Intent(getApplicationContext(),ViewCardActivity.class);
        in.putExtra("viewDeck",deck);
        startActivity(in);
    }

}