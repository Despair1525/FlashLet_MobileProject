package com.example.prm_final_project.Ui.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.prm_final_project.Adapter.MyFlashcardsPageAdapter;
import com.example.prm_final_project.Model.Deck;
import com.example.prm_final_project.Model.User;
import com.example.prm_final_project.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MyFlashcardsActivity extends AppCompatActivity {

    private Context thisContext;
    private ArrayList<Deck> decks = new ArrayList<>();
    private ArrayList<Deck> allDecks = new ArrayList<>();
    private ArrayList<User> users;

    private ViewPager viewPager;
    private MyFlashcardsPageAdapter myFlashcardsPageAdapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_flashcards);
        initUi();

        FragmentManager fragmentManager = getSupportFragmentManager();
        MyFlashcardsPageAdapter myFlashcardsPageAdapter = new MyFlashcardsPageAdapter(fragmentManager);
        viewPager.setAdapter(myFlashcardsPageAdapter);
        //Tablayout
        tabLayout.setupWithViewPager(viewPager);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("My Flashcards");
        actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initUi(){
        tabLayout = findViewById(R.id.my_flashcards_tab);
        viewPager = findViewById(R.id.my_flashcards_page);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}