package com.example.prm_final_project.Ui.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.prm_final_project.Model.Deck;
import com.example.prm_final_project.R;

import java.util.List;

public class LearnCardActivity extends AppCompatActivity implements View.OnClickListener {
    private Deck deck;
    private Button buttonContinue;
    private Button buttonStudying;
    private TextView textViewCardBack;
    private TextView textViewCardFront;
    private TextView textViewProgress;
    private List<List<String>> studyingCards;
    private int currentProgress = 1;
    private ProgressBar progressBar;

    private ViewPager2 viewPager2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_card);
        Intent intent = getIntent();
        deck = (Deck)  intent.getSerializableExtra("currentDeck");
        studyingCards = deck.getCards();
        Log.i("Chekc",deck.getCards().size() +"");
        initSlideCard();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(deck.getTitle());
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        buttonStudying.setOnClickListener(this);
        buttonContinue.setOnClickListener(this);
    };

    public void initSlideCard(){
        buttonContinue = findViewById(R.id.buttonContinue);
        buttonStudying = findViewById(R.id.buttonStudying);
        textViewCardBack = findViewById(R.id.SliderCardBack);
        textViewCardFront = findViewById(R.id.SliderCardFront);
        progressBar = findViewById(R.id.progressBar);
        textViewProgress = findViewById(R.id.tv_progress);
        progressBar.setMax(deck.getCards().size());
        progressBar.setProgress(1);
        textViewProgress.setText("0/"+progressBar.getMax());
        addaptChange();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onClick(View view) {
        if(view == buttonContinue){
            studyingCards.remove(0);
            if(deck.getCards().size() == 0){
                addaptChangeEmpty();
                buttonContinue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LearnCardActivity.this);
                        builder.setTitle("Congrats! You have just finished this set");
                        builder.setMessage("Do you want to reset this set");
                        builder.setCancelable(true);
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        finish();
                                    }
                                });

                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        finish();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });
            }else {
                addaptChange();
                currentProgress++;
                textViewProgress.setText(currentProgress+"/"+progressBar.getMax());
                progressBar.setProgress(currentProgress);
            }
        }
        if(view == buttonStudying){
            List<String> currentCard  = studyingCards.get(0);
            int addPosition = studyingCards.size() / 2;
            studyingCards.remove(0);
            studyingCards.add(addPosition, currentCard);
            addaptChange();
        }


    }

    public void addaptChange(){
        textViewCardFront.setText(studyingCards.get(0).get(0));
        textViewCardBack.setText(studyingCards.get(0).get(1));
    }
    public void addaptChangeEmpty(){
        textViewCardFront.setText("Congrats!!");
        textViewCardBack.setText("Congrats!!");
    }
}