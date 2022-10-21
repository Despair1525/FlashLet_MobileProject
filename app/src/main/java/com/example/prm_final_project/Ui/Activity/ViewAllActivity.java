package com.example.prm_final_project.Ui.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.prm_final_project.R;

public class ViewAllActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        getSupportActionBar().setTitle("View All Decks");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}