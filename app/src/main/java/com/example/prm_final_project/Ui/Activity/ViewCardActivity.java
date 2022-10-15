package com.example.prm_final_project.Ui.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prm_final_project.Adapter.SliderFlashcardAdapter;
import com.example.prm_final_project.Adapter.cardViewAdapter1;
import com.example.prm_final_project.Model.Deck;
import com.example.prm_final_project.R;

public class ViewCardActivity extends AppCompatActivity  {
    private Deck deck;
    private ViewPager2 viewPager2;
    private ImageView imageViewLearn;
    private TextView tvTitile;
    private TextView tvView;
    private ImageView imageReload,imageTest;
    private RecyclerView recyclerViewList;
    private RelativeLayout learnRelativeLayout, fullScreenRelativeLayout, reloadRelativeLayout, testRelativeLayout, viewRelativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Take all View in layout
        setContentView(R.layout.activity_view_card);
        tvView = findViewById(R.id.textViewView);
        imageViewLearn = findViewById(R.id.imageViewLearn);
        tvTitile = findViewById(R.id.tvTitle);
        imageReload = findViewById(R.id.vc_Reload);
        imageTest = findViewById(R.id.vc_Test);
        learnRelativeLayout = findViewById(R.id.learnItem);
        fullScreenRelativeLayout = findViewById(R.id.fullScreenItem);
        reloadRelativeLayout = findViewById(R.id.reloadItem);
        testRelativeLayout = findViewById(R.id.testItem);
        viewRelativeLayout = findViewById(R.id.viewItem);

        reloadRelativeLayout.setOnClickListener(view -> onReload());
        testRelativeLayout.setOnClickListener(view -> onTest());
        learnRelativeLayout.setOnClickListener(view -> onLearn());



        if ( getIntent().getSerializableExtra("viewDeck") != null){
            deck = (Deck) getIntent().getSerializableExtra("viewDeck");
        }
        tvTitile.setText(deck.getTitle());
        tvView.setText(deck.getView()+"");
        loadSlideFlash();
    }

    private void onTest() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Setting");

        LayoutInflater inflater =ViewCardActivity.this.getLayoutInflater();
        View layout = inflater.inflate(R.layout.activity_choose_type_test    /*my layout here*/, null);
        builder.setView(layout);
        TextView totalNum =  layout.findViewById(R.id.numques_total);
        EditText quesNum = layout.findViewById(R.id.numques_num);
        totalNum.setText("/"+deck.getCards().size());
        RadioGroup radio_g = layout.findViewById(R.id.QuestionType);
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(radio_g.getCheckedRadioButtonId()==-1)
                {
                    Toast.makeText(getApplicationContext(), "Please select one choice", Toast.LENGTH_SHORT).show();
                    return;
                }
                RadioButton uans = (RadioButton) layout.findViewById(radio_g.getCheckedRadioButtonId());
                String ansText = uans.getText().toString();
                int numQues = Integer.parseInt(quesNum.getText().toString() );
                if(numQues >  deck.getCards().size() ) {
                    Toast.makeText(getApplicationContext(), "Please choose number test smaller", Toast.LENGTH_SHORT).show();
                    return;
                };
                if(ansText.equalsIgnoreCase("Multiple Choice")){
                    Intent i = new Intent(ViewCardActivity.this,TestActivity.class);
                    i.putExtra("numQues",numQues);
                    i.putExtra("TestDeck",deck);
                    startActivity(i);
                }
                else {
                    Intent i = new Intent(ViewCardActivity.this,WrittenQuizActivity.class);
                    i.putExtra("numQues",numQues);
                    i.putExtra("TestDeck",deck);
                    startActivity(i);

                };

//                Intent i = new Intent(ViewCardActivity.this,TestActivity.class);
//                i.putExtra("TestDeck",deck);
//                startActivity(i);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();


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



   public void onLearn(){
       Bundle b = new Bundle();
       Intent i = new Intent(this, LearnCardActivity.class);
       i.putExtra("currentDeck",deck);
       startActivity(i);
   };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.viewcard_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.copySet:
                Toast.makeText(this, "CopySet", Toast.LENGTH_SHORT).show();
                break;
            case R.id.shareSet:
                Toast.makeText(this, "ShareSet", Toast.LENGTH_SHORT).show();
                break;
            case R.id.editSet:
                Intent i = new Intent(this, EditDeckActivity.class);
                i.putExtra("editDeck",deck);
                startActivity(i);
                break;
            case R.id.deleteSet:
                Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    }
