package com.example.prm_final_project.Ui.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prm_final_project.Adapter.SliderFlashcardAdapter;
import com.example.prm_final_project.Adapter.cardViewAdapter1;
import com.example.prm_final_project.Dao.DeckDao;
import com.example.prm_final_project.Dao.UserDao;
import com.example.prm_final_project.Model.Deck;
import com.example.prm_final_project.Model.FavoriteDeck;
import com.example.prm_final_project.Model.User;
import com.example.prm_final_project.R;
import com.example.prm_final_project.Util.Methods;

import java.util.ArrayList;
import java.util.List;

public class ViewCardActivity extends AppCompatActivity  {
    private Deck deck;
    private ViewPager2 viewPager2;
    private ImageView imageViewLearn;
    private TextView tvTitile;
    private TextView tvView, textViewAuthor, textViewView, textViewTitle;
    private ImageView imageReload,imageTest;
    private RecyclerView recyclerViewList;
    private RelativeLayout learnRelativeLayout, reloadRelativeLayout, testRelativeLayout, viewRelativeLayout;
    private RatingBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Take all View in layout
        setContentView(R.layout.activity_view_card);

        imageViewLearn = findViewById(R.id.imageViewLearn);

        imageReload = findViewById(R.id.vc_Reload);
        imageTest = findViewById(R.id.vc_Test);
        learnRelativeLayout = findViewById(R.id.learnItem);
        reloadRelativeLayout = findViewById(R.id.reloadItem);
        testRelativeLayout = findViewById(R.id.testItem);

        textViewTitle = findViewById(R.id.textViewTitle);
        textViewView = findViewById(R.id.textViewNumView);
        textViewAuthor = findViewById(R.id.textViewAuthor);

        reloadRelativeLayout.setOnClickListener(view -> onReload());
        testRelativeLayout.setOnClickListener(view -> onTest());
        learnRelativeLayout.setOnClickListener(view -> onLearn());



        if ( getIntent().getSerializableExtra("viewDeck") != null){
            deck = (Deck) getIntent().getSerializableExtra("viewDeck");
        }
        getSupportActionBar().setTitle(deck.getTitle());
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textViewTitle.setText(deck.getTitle());
        textViewView.setText(deck.getView()+"");
        textViewAuthor.setText(deck.getAuthor());
        loadSlideFlash();
    }

    private void onTest() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewCardActivity.this, R.style.AlertDialogTheme);
        View layout = getLayoutInflater().from(ViewCardActivity.this).inflate(
                R.layout.activity_choose_type_test,
                (ConstraintLayout)findViewById(R.id.layoutDialog)
        );
        builder.setView(layout);

//        LayoutInflater inflater =ViewCardActivity.this.getLayoutInflater();
//        View layout = inflater.inflate(R.layout.activity_choose_type_test    /*my layout here*/, null);
//        builder.setView(layout);
        TextView totalNum =  layout.findViewById(R.id.numques_total);
        EditText quesNum = layout.findViewById(R.id.numques_num);
        totalNum.setText("/"+deck.getCards().size());
        RadioGroup radio_g = layout.findViewById(R.id.QuestionType);
        Button yesButton = layout.findViewById(R.id.buttonYes);
        Button noButton = layout.findViewById(R.id.buttonNo);

        final AlertDialog alertDialog = builder.create();

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();


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

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.viewcard_menu,menu);
        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
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
            case R.id.saveSet:
                String UserId = UserDao.getUser().getUid();
                String deckId = deck.getDeckId();
                String id = UserId+"-"+deckId;
                FavoriteDeck favoriteDeck = new FavoriteDeck(id,UserId,deckId, Methods.getTimeLong());
                DeckDao.addFavoriteDeck(favoriteDeck);
                break;

            case R.id.infoSet:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Deck Informations");
                LayoutInflater inflater =ViewCardActivity.this.getLayoutInflater();
                View layout = inflater.inflate(R.layout.dialog_deck_information   /*my layout here*/, null);
                builder.setView(layout);
                TextView deckIdInfo =  layout.findViewById(R.id.tvDeckId);
                TextView Author = layout.findViewById(R.id.tvAuthor);
                TextView Uid = layout.findViewById(R.id.tvUid)   ;
                deckIdInfo.setText("DeckId"+deck.getDeckId());
                Author.setText("Author:"+deck.getAuthor());
                builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
               break;

            case R.id.rateSet:
                builder = new AlertDialog.Builder(ViewCardActivity.this, R.style.AlertDialogTheme);
                inflater =ViewCardActivity.this.getLayoutInflater();
                layout = inflater.inflate(R.layout.dialog_rating_deck   /*my layout here*/, null);
                builder.setView(layout);
                RatingBar ratingBar = layout.findViewById(R.id.ratingBar);

                builder.setPositiveButton("Get Rate", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String ratingPoint = ratingBar.getRating()+"";
                        Log.i("ratingPoint", ratingPoint);
                        double score =   (ratingBar.getRating() - 3 + (int)(deck.getView() / 100 ) ) ;
                        User rateUser = UserDao.readUserById(UserDao.getUser().getUid());
                        if(rateUser != null) {
                            rateUser.getRate().put(deck.getDeckId(),score);
                            UserDao.addUser(rateUser);
                        }else {
                            Log.i("RateUser","Not found User ID");
                        };
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();

        }
        return super.onOptionsItemSelected(item);
    }

    }
