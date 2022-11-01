package com.example.prm_final_project.Ui.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prm_final_project.Model.Deck;
import com.example.prm_final_project.Model.Quiz;
import com.example.prm_final_project.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WrittenQuizActivity extends AppCompatActivity {
    private TextView Question;
    public ArrayList<Quiz> questionSet = new ArrayList<>();
    public ArrayList<String> AnswerSet = new ArrayList<>();
    private EditText Answer;
    public static int marks=0,correct=0,wrong=0,numQuest = 0 ;
    public static String title  ="";
    int questnum = 0;
    private Deck deck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_written_quiz);
        deck = (Deck) getIntent().getSerializableExtra("TestDeck");
        init();
        initQuestionAnswerSet();
        numQuest = getIntent().getIntExtra("numQues", questionSet.size());
        title = deck.getTitle();

        Collections.shuffle(questionSet);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(deck.getTitle());
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);

        Question.setText(questionSet.get(questnum).getQuestion());
        Answer.setOnEditorActionListener(actionListener);
    }

    private TextView.OnEditorActionListener actionListener  = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            switch (i) {
                case EditorInfo.IME_ACTION_DONE:
                    if(Answer.getText().toString().trim().equals(null))
                    {
                        Toast.makeText(getApplicationContext(), "Please enter answer", Toast.LENGTH_SHORT).show();
                    }

                    String ansText = Answer.getText().toString();
//                Toast.makeText(getApplicationContext(), ansText, Toast.LENGTH_SHORT).
//                show();
                    if(ansText.trim().equalsIgnoreCase(questionSet.get(questnum).getAnswer())) {
                        correct++;
                        Toast.makeText(getApplicationContext(), "Correct", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        wrong++;
                        Toast.makeText(getApplicationContext(), "Wrong", Toast.LENGTH_SHORT).show();
                    }

                    questnum++;
                    if(questnum < numQuest)
                    {
                        Question.setText(questionSet.get(questnum).getQuestion());

                    }
                    else{
                        marks=correct;
                        Intent in = new Intent(getApplicationContext(),ResultWrittenChoiceActivity.class);
                        in.putExtra("viewDeck",deck);
                        startActivity(in);
                    }
                    Answer.setText("");
                    break;
            }
            return true;
        }
    };

    public void init(){
        Answer = findViewById(R.id.Answer);
        Question = findViewById(R.id.Question);
    }

    public void initQuestionAnswerSet() {
        List<List<String>> listString = deck.getCards();
        for (List<String> card: listString) {
            Quiz quiz = new Quiz(card.get(0), card.get(1));
            questionSet.add(quiz);
            AnswerSet.add(card.get(1));
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}