package com.example.prm_final_project.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.prm_final_project.Adapter.SliderFlashcardAdapter;
import com.example.prm_final_project.Module.Deck;
import com.example.prm_final_project.R;

public class LearnCardActivity extends AppCompatActivity {
    private Deck deck;
    private ViewPager2 viewPager2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_card);
        Intent intent = getIntent();
        deck = (Deck)  intent.getSerializableExtra("currentDeck");
        Log.i("Chekc",deck.getCards().size() +"");
        initSlideCard();
    };

    public void initSlideCard(){

        viewPager2 = findViewById(R.id.viewPagerImageSliderLearn);
        viewPager2.setAdapter(new SliderFlashcardAdapter(this,deck,viewPager2));
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(30));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1- Math.abs(position);
                page.setScaleY(0.85f + r* 0.15f);
            }
        });
        viewPager2.setPageTransformer(compositePageTransformer);
    };
}