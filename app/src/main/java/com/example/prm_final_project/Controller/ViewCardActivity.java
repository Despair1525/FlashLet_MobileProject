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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prm_final_project.Adapter.SliderFlashcardAdapter;
import com.example.prm_final_project.Adapter.cardViewAdapter1;
import com.example.prm_final_project.Module.Deck;
import com.example.prm_final_project.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewCardActivity extends AppCompatActivity  {
    private Deck deck;
    private ViewPager2 viewPager2;
    private ImageView imageViewLearn;
    private TextView tvTitile;
    private ImageView imageReload;
    private RecyclerView recyclerViewList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Take all View in layout
        setContentView(R.layout.activity_view_card);
        imageViewLearn = findViewById(R.id.imageViewLearn);
        tvTitile = findViewById(R.id.tvTitle);
        imageReload = findViewById(R.id.vc_Reload);
        imageReload.setOnClickListener(view -> onReload());


        if ( getIntent().getSerializableExtra("viewDeck") != null){
            deck = (Deck) getIntent().getSerializableExtra("viewDeck");
        }
        tvTitile.setText(deck.getTitle());
        loadSlideFlash();
    }

    private void onReload() {
        loadSlideFlash();
    }
    private void loadSlideFlash(){
        recyclerViewList = findViewById(R.id.RcCardList);
        initSlideCard(); // Set Slide Flash card
        cardViewAdapter1 cardViewAdapter = new cardViewAdapter1(this,deck);
        recyclerViewList.setAdapter(cardViewAdapter);
        recyclerViewList.setLayoutManager(new LinearLayoutManager((this)));

    };

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



   public void onLearn(View view){
        Bundle b = new Bundle();
       Intent i = new Intent(this, LearnCardActivity.class);
       i.putExtra("currentDeck",deck);
       startActivity(i);
   };

    }
