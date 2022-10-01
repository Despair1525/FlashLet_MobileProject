package com.example.prm_final_project.Controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.prm_final_project.Adapter.SliderFlashcardAdapter;
import com.example.prm_final_project.Adapter.cardViewAdapter1;
import com.example.prm_final_project.Module.Deck;
import com.example.prm_final_project.R;

import java.util.ArrayList;
import java.util.List;

public class ViewCard extends AppCompatActivity  {
    private Deck deck;
    private ViewPager2 viewPager2;
    private ImageView imageViewLearn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Take all View in layout
        imageViewLearn = findViewById(R.id.imageViewLearn);
        setContentView(R.layout.activity_view_card);
        RecyclerView recyclerViewList = findViewById(R.id.RcCardList);
        initDeck();
        initSlideCard(); // Set Slide Flash card


        cardViewAdapter1 cardViewAdapter = new cardViewAdapter1(this,deck);
        recyclerViewList.setAdapter(cardViewAdapter);
        recyclerViewList.setLayoutManager(new LinearLayoutManager((this)));
    }

    public void initSlideCard(){
        viewPager2 = findViewById(R.id.viewPagerImageSlider);
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

    private void initDeck() {
        List<List<String>> cards = new ArrayList<>();
        List<String> card1 = new ArrayList<>();
        card1.add("Manh");
        card1.add("Dep trai");
        cards.add(card1);

        List<String> card2 = new ArrayList<>();
        card2.add("Minh");
        card2.add("Gay");
        cards.add(card2);

        List<String> card3 = new ArrayList<>();
        card3.add("Mai");
        card3.add("Luoi");
        cards.add(card3);

        List<String> card4 = new ArrayList<>();
        card4.add("Khoa");
        card4.add("Sang tao");
        cards.add(card4);

        List<String> card5 = new ArrayList<>();
        card5.add("Thanh");
        card5.add("cham chi");
        cards.add(card5);
        deck = new Deck("01","admin","First Flashcard","manh dep trai",cards);
        }

   public void onLearn(View view){
        Bundle b = new Bundle();
       Intent i = new Intent(this,activity_learn_card.class);
       i.putExtra("currentDeck",deck);
       startActivity(i);
   };

    ;
    }
