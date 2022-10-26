package com.example.prm_final_project.Ui.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.prm_final_project.Adapter.cardViewAdapter1;
import com.example.prm_final_project.Model.Deck;
import com.example.prm_final_project.R;

import java.util.List;

public class ViewAllCardsActivity extends AppCompatActivity {

    private Deck deck;
    private List<List<String>> studyingCards;
    private RecyclerView recyclerViewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_all_cards);
        Intent intent = getIntent();
        deck = (Deck)  intent.getSerializableExtra("currentDeck");
        studyingCards = deck.getCards();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(deck.getTitle());
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        loadSlideFlash();
    }

    private void loadSlideFlash(){
        recyclerViewList = findViewById(R.id.RecyclerViewFlashCard);
        cardViewAdapter1 cardViewAdapter = new cardViewAdapter1(this,deck);
        recyclerViewList.setAdapter(cardViewAdapter);
        recyclerViewList.setLayoutManager(new LinearLayoutManager((this)));

    };

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}