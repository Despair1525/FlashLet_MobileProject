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

import com.example.prm_final_project.Module.Deck;
import com.example.prm_final_project.Module.Quiz;
import com.example.prm_final_project.R;

import java.util.ArrayList;
import java.util.Collections;
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

    public static int marks=0,correct=0,wrong=0;
    int questnum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        deck = (Deck) getIntent().getSerializableExtra("TestDeck");
        init();
        initQuestionAnswerSet();
        int numQuest = getIntent().getIntExtra("numQues", questionSet.size());


        Collections.shuffle(questionSet);
        Question.setText(questionSet.get(questnum).getQuestion());
        Log.i("numQues", questionSet.size() +"");

        randAnswer(AnswerSet, questionSet.get(questnum).getAnswer(), AnswerSet.size());
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
                    randAnswer(AnswerSet, questionSet.get(questnum).getAnswer(), AnswerSet.size());
                }
                else{
                    marks=correct;
                    Intent in = new Intent(getApplicationContext(),ResultMultipleChoiceActivity.class);
                    startActivity(in);
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

    public void randAnswer(ArrayList<String> AnswerSet, String Answer, int AnswerSize){
        rd = new Random();
        Set<Integer> setSup = new LinkedHashSet<Integer>();
        while (setSup.size() < 3) {
            setSup.add(rd.nextInt(AnswerSize));
        }
        ArrayList<String> answers = new ArrayList<>();
        Iterator itr = setSup.iterator();
        while (itr.hasNext()){
            answers.add(AnswerSet.get((Integer) itr.next()).toString());
        }
        answers.add(Answer);


        Set<Integer> setSup2 = new LinkedHashSet<Integer>();
        while (setSup2.size() < 4) {
            setSup2.add(rd.nextInt(4)+1);
        }
        Iterator itr2 = setSup2.iterator();
        ArrayList<Integer> index = new ArrayList<>();
        while (itr2.hasNext()){
            index.add((Integer) itr2.next());
        }

        rb1.setText(answers.get(index.get(0) -1));
        rb2.setText(answers.get(index.get(1) -1));
        rb3.setText(answers.get(index.get(2)-1 ));
        rb4.setText(answers.get(index.get(3)-1));
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