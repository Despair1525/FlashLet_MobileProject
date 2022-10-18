package com.example.prm_final_project.Ui.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm_final_project.Model.Deck;
import com.example.prm_final_project.R;

import java.util.ArrayList;

public class MyFlashcardsActivity extends AppCompatActivity {
    private ArrayList<Deck> allDecks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_flashcards);

    }
}