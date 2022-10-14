package com.example.prm_final_project.Ui.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prm_final_project.Module.Deck;
import com.example.prm_final_project.Module.Quiz;
import com.example.prm_final_project.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WrittenQuizActivity extends AppCompatActivity {
    private TextView Question;
    public ArrayList<Quiz> questionSet = new ArrayList<>();
    public ArrayList<String> AnswerSet = new ArrayList<>();

    private Button SubmitQuestion, Quit;
    private EditText Answer;
    public static int marks=0,correct=0,wrong=0;
    int questnum = 0;
    private Deck deck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_written_quiz);
        deck = (Deck) getIntent().getSerializableExtra("TestDeck");
        init();
        initQuestionAnswerSet();
        int numQuest = getIntent().getIntExtra("numQues", questionSet.size());
        Collections.shuffle(questionSet);
        Question.setText(questionSet.get(questnum).getQuestion());
        SubmitQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Answer.getText().toString().trim().equals(null))
                {
                    Toast.makeText(getApplicationContext(), "Please enter answer", Toast.LENGTH_SHORT).show();
                    return;
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
                    startActivity(in);
                }

            }
        });
    }

    public void init(){
        Answer = findViewById(R.id.Answer);
        Question = findViewById(R.id.Question);
        SubmitQuestion = findViewById(R.id.NextQues);
    }

    public void initQuestionAnswerSet() {
        List<List<String>> listString = deck.getCards();
        for (List<String> card: listString) {
            Quiz quiz = new Quiz(card.get(0), card.get(1));
            questionSet.add(quiz);
            AnswerSet.add(card.get(1));
        }
    }

}