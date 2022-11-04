package com.example.prm_final_project.Ui.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prm_final_project.Model.Deck;
import com.example.prm_final_project.Model.Quiz;
import com.example.prm_final_project.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class TestActivity extends AppCompatActivity {
    private Deck deck;

    private TextView Question;

    public ArrayList<Quiz> quizArrayList;
    public ArrayList<Quiz> questionSet = new ArrayList<>();
    public ArrayList<String> AnswerSet = new ArrayList<>();
    public Random rd;
    public String show = "";

    private Button SubmitQuestion, Quit;
    private RadioGroup radio_g;
    private RadioButton rb1,rb2,rb3,rb4;

    public static int marks=0,correct=0,wrong=0, numQuest = 0;
    public static String title = "";
    int questnum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
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
        Log.i("numQues", questionSet.size() +"");

        randAnswer(questionSet.get(questnum).getAnswer());
        SubmitQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(radio_g.getCheckedRadioButtonId()==-1) {
                    Toast.makeText(getApplicationContext(), "Please select one choice", Toast.LENGTH_SHORT).show();
                    return;
                }
                RadioButton uans = (RadioButton) findViewById(radio_g.getCheckedRadioButtonId());
                String ansText = uans.getText().toString();
//                Toast.makeText(getApplicationContext(), ansText, Toast.LENGTH_SHORT).
//                show();
                if(ansText.equalsIgnoreCase(questionSet.get(questnum).getAnswer())) {
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
                    randAnswer(questionSet.get(questnum).getAnswer());
                }
                else{
                    marks=correct;
                    Intent in = new Intent(getApplicationContext(),ResultMultipleChoiceActivity.class);
                    in.putExtra("viewDeck",deck);
                    startActivity(in);
                    finish();
                }
                radio_g.clearCheck();
            }
        });
    }

    public void init(){
        Question = findViewById(R.id.Question);
        radio_g = findViewById(R.id.Options);
        rb1 = findViewById(R.id.radioButtonA);
        rb2 = findViewById(R.id.radioButtonB);
        rb3 = findViewById(R.id.radioButtonC);
        rb4 = findViewById(R.id.radioButtonD);
        SubmitQuestion = findViewById(R.id.NextQues);
    }

    public void randAnswer(String Answer){
        List<List<String>> listString = deck.getCards();
        ArrayList<String> AnswerSetRand = new ArrayList<>();
        for (List<String> card: listString) {
            if(card.get(1).equalsIgnoreCase(Answer)){
                continue;
            }else{
                AnswerSetRand.add(card.get(1));
            }

        }
        for (int i = 0; i < AnswerSetRand.size(); i++) {
            if(Answer.equalsIgnoreCase(AnswerSetRand.get(i))){
                AnswerSetRand.remove(i);
            }
        }
        Collections.shuffle(AnswerSetRand);
        Log.i("AnswerSetSize",AnswerSetRand.size()+"");
        ArrayList<String> arrayListSetAswer = new ArrayList<>();
        arrayListSetAswer.add(AnswerSetRand.get(0));
        arrayListSetAswer.add(AnswerSetRand.get(1));
        arrayListSetAswer.add(AnswerSetRand.get(2));
        arrayListSetAswer.add(Answer);
        Collections.shuffle(arrayListSetAswer);
        Log.i("AnswerSize",arrayListSetAswer.size()+"");

        rb1.setText(arrayListSetAswer.get(0));
        rb2.setText(arrayListSetAswer.get(1));
        rb3.setText(arrayListSetAswer.get(2));
        rb4.setText(arrayListSetAswer.get(3));
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
        Intent i = new Intent(this, ViewCardActivity.class);
        i.putExtra("viewDeck", deck);
        startActivity(i);
        return true;
    }
    @Override
    public void onBackPressed() {
        finish();
        Intent i = new Intent(this, ViewCardActivity.class);
        i.putExtra("viewDeck", deck);
        startActivity(i);
    }



}