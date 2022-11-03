package com.example.prm_final_project.Ui.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prm_final_project.Adapter.SliderFlashcardAdapter;
import com.example.prm_final_project.Dao.DeckDao;
import com.example.prm_final_project.Dao.UserDao;
import com.example.prm_final_project.Model.Deck;
import com.example.prm_final_project.Model.RecentDeck;
import com.example.prm_final_project.Model.User;
import com.example.prm_final_project.R;
import com.example.prm_final_project.Util.Methods;
import com.google.firebase.auth.FirebaseUser;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.ArrayList;
import java.util.Hashtable;

public class ViewCardActivity extends AppCompatActivity {
    private Deck deck;
    private ViewPager2 viewPager2;
    private ImageView imageViewLearn;
    private TextView tvTitile;
    private TextView tvView, textViewAuthor, textViewView, textViewTitle;
    private SliderFlashcardAdapter sliderFlashcardAdapter;
    private ImageView imageReload, imageTest;
    private RecyclerView recyclerViewList;
    private RelativeLayout learnRelativeLayout, reloadRelativeLayout, testRelativeLayout, viewRelativeLayout;
    private RatingBar bar;
    private TextView textViewProgress;
    private User author;
    FirebaseUser firebaseUser;
    User user;

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
        viewRelativeLayout = findViewById(R.id.viewAllFlashCardItem);
        textViewProgress = findViewById(R.id.tv_progress);

        reloadRelativeLayout.setOnClickListener(view -> onReload());


        learnRelativeLayout.setOnClickListener(view -> onLearn());
        viewRelativeLayout.setOnClickListener(view -> onViewFlashCard());


        if (getIntent().getSerializableExtra("viewDeck") != null) {
            deck = (Deck) getIntent().getSerializableExtra("viewDeck");
        }
        if (deck.getCards().size() >= 5) {
            testRelativeLayout.setOnClickListener(view -> onTest());
        } else {
            testRelativeLayout.setOnClickListener(view -> onTest2());
        }
        getSupportActionBar().setTitle(deck.getTitle());
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        textViewTitle.setText(deck.getTitle());
        textViewView.setText(deck.getView() + "");

        author = UserDao.allUserHT.get(deck.getUid());
        textViewAuthor.setText(author.getUsername());
        firebaseUser = UserDao.getUser();
        loadSlideFlash();
    }


    private void onTest2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewCardActivity.this, R.style.AlertDialogTheme);
        View layout = getLayoutInflater().from(ViewCardActivity.this).inflate(
                R.layout.activity_choose_type_test_written,
                (ConstraintLayout) findViewById(R.id.layoutDialog)
        );
        builder.setView(layout);

        EditText quesNum = layout.findViewById(R.id.numques_num);
        quesNum.setHint(" /" + deck.getCards().size());
        Button yesButton = layout.findViewById(R.id.buttonYes);
        Button noButton = layout.findViewById(R.id.buttonNo);

        final AlertDialog alertDialog = builder.create();

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quesNum.getText().toString().isEmpty()) {
                    quesNum.setError("Please fill out number of questions");
                    return;
                }
                int numQues = Integer.parseInt(quesNum.getText().toString().trim()) - 1;
                if (numQues > deck.getCards().size()) {
                    Toast.makeText(getApplicationContext(), "Please choose number test smaller", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i = new Intent(ViewCardActivity.this, WrittenQuizActivity.class);
                i.putExtra("numQues", numQues);
                i.putExtra("TestDeck", deck);
                startActivity(i);
                alertDialog.dismiss();
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


    @Override
    public void onBackPressed() {
        finish();
    }
    private void onViewFlashCard() {
        Intent i = new Intent(this, ViewAllCardsActivity.class);
        i.putExtra("currentDeck", deck);
        startActivity(i);
    }

    private void onTest() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewCardActivity.this, R.style.AlertDialogTheme);
        View layout = getLayoutInflater().from(ViewCardActivity.this).inflate(
                R.layout.activity_choose_type_test,
                (ConstraintLayout) findViewById(R.id.layoutDialog)
        );
        builder.setView(layout);
        EditText quesNum = layout.findViewById(R.id.numques_num);
        quesNum.setHint(" /" + deck.getCards().size());
        RadioGroup radio_g = layout.findViewById(R.id.QuestionType);
        Button yesButton = layout.findViewById(R.id.buttonYes);
        Button noButton = layout.findViewById(R.id.buttonNo);

        final AlertDialog alertDialog = builder.create();

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radio_g.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Please select one test type", Toast.LENGTH_SHORT).show();
                    return;
                }
                RadioButton uans = (RadioButton) layout.findViewById(radio_g.getCheckedRadioButtonId());
//                String ansText = uans.getText().toString();
                if (quesNum.getText().toString().isEmpty()) {
                    quesNum.setError("Please fill out number of questions");
                    return;
                }
                int numQues = Integer.parseInt(quesNum.getText().toString().trim());
                if (numQues > deck.getCards().size()) {
                    Toast.makeText(getApplicationContext(), "Please choose number test smaller", Toast.LENGTH_SHORT).show();
                    return;
                }
                ;
                if (uans == layout.findViewById(R.id.multiChoice)) {
                    //ansText.equalsIgnoreCase("Multiple Choice")
                    Intent i = new Intent(ViewCardActivity.this, TestActivity.class);
                    i.putExtra("numQues", numQues);
                    i.putExtra("TestDeck", deck);
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(ViewCardActivity.this, WrittenQuizActivity.class);
                    i.putExtra("numQues", numQues);
                    i.putExtra("TestDeck", deck);
                    startActivity(i);
                    finish();

                }
                ;
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
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private void loadSlideFlash() {
        initSlideCard(); // Set Slide Flash card
    }

    public void initSlideCard() {

        sliderFlashcardAdapter  = new SliderFlashcardAdapter(this, deck, viewPager2);
        viewPager2 = findViewById(R.id.viewPagerImageSlider);
        viewPager2.setAdapter(sliderFlashcardAdapter);
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(30));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });
        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                int positionCard = position +1;
                textViewProgress.setText(positionCard+"/"+deck.getCards().size());
                RecyclerView rvNext = (RecyclerView) viewPager2.getChildAt(0);
                // handel index out of range
                if(position == 0){
                    SliderFlashcardAdapter.SliderViewHolder holderNext= (SliderFlashcardAdapter.SliderViewHolder) rvNext.findViewHolderForAdapterPosition(position+1);
                    if(holderNext.easyFlipView.isBackSide()){
                        holderNext.easyFlipView.flipTheView();
                    }
                }
                else if(position == deck.getCards().size()-1){
                    SliderFlashcardAdapter.SliderViewHolder holderBack= (SliderFlashcardAdapter.SliderViewHolder) rvNext.findViewHolderForAdapterPosition(position-1);
                    if(holderBack.easyFlipView.isBackSide()){
                        holderBack.easyFlipView.flipTheView();
                    }
                }
                else {
                    SliderFlashcardAdapter.SliderViewHolder holderBack = (SliderFlashcardAdapter.SliderViewHolder) rvNext.findViewHolderForAdapterPosition(position - 1);
                    if(holderBack.easyFlipView.isBackSide()){
                        holderBack.easyFlipView.flipTheView();
                    }
                    SliderFlashcardAdapter.SliderViewHolder holderNext= (SliderFlashcardAdapter.SliderViewHolder) rvNext.findViewHolderForAdapterPosition(position+1);
                    if(holderNext.easyFlipView.isBackSide()){
                        holderNext.easyFlipView.flipTheView();
                    }
                }

            }
        });
    }

    public void onLearn() {
        Bundle b = new Bundle();
        Intent i = new Intent(this, LearnCardActivity.class);
        i.putExtra("currentDeck", deck);
        startActivity(i);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (firebaseUser.getUid().equalsIgnoreCase(deck.getUid())) {
            getMenuInflater().inflate(R.menu.viewcard_menu, menu);
            if (menu instanceof MenuBuilder) {
                MenuBuilder m = (MenuBuilder) menu;
                m.setOptionalIconsVisible(true);
            }
            return super.onCreateOptionsMenu(menu);
        } else {
            getMenuInflater().inflate(R.menu.viewcard_menu_not_author, menu);
            if (menu instanceof MenuBuilder) {
                MenuBuilder m = (MenuBuilder) menu;
                m.setOptionalIconsVisible(true);
            }
            return super.onCreateOptionsMenu(menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.copySet:
//                Toast.makeText(this, "CopySet", Toast.LENGTH_SHORT).show();
                Intent iCopy = new Intent(this, EditDeckActivity.class);
                Deck editDeck = deck;
                editDeck.setUid(UserDao.getUser().getUid());
                editDeck.setDeckId(Methods.generateFlashCardId());
                editDeck.setDate(Methods.getDate());
                iCopy.putExtra("editDeck", editDeck);
                startActivity(iCopy);
                break;

            case R.id.editSet:
                if (firebaseUser.getUid().equalsIgnoreCase(deck.getUid())) {
                    Intent i = new Intent(this, EditDeckActivity.class);
                    i.putExtra("editDeck", deck);
                    startActivity(i);
                    break;
                } else {
                    Toast.makeText(this, "You must own this set to edit", Toast.LENGTH_SHORT).show();
                }
            case R.id.deleteSet:
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewCardActivity.this);
                builder.setTitle("Delete");
                builder.setMessage("Do you want to delete this set");
                builder.setCancelable(true);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        firebaseUser = UserDao.getUser();
                        user = UserDao.getCurrentUser();
                        ArrayList<RecentDeck> myDeck = user.getMyDeck();
                        for (int i = 0; i < myDeck.size(); i++) {
                            if(myDeck.get(i).getDeckId().equalsIgnoreCase(deck.getDeckId())){
                                myDeck.remove(myDeck.get(i));
                            }
                        }
                        user.setMyDeck(myDeck);

                        ArrayList<RecentDeck> favoriteDeck =  user.getFavoriteDeck();
                        for (int i = 0; i < favoriteDeck.size(); i++) {
                            if(favoriteDeck.get(i).getDeckId().equalsIgnoreCase(deck.getDeckId())){
                                favoriteDeck.remove(favoriteDeck.get(i));
                            }
                        }
                        user.setFavoriteDeck(favoriteDeck);

                        ArrayList<RecentDeck> recentDecks = user.getRecentDecks();
                        for (int i = 0; i < recentDecks.size(); i++) {
                            if(recentDecks.get(i).getDeckId().equalsIgnoreCase(deck.getDeckId())){
                                recentDecks.remove(recentDecks.get(i));
                            }
                        }
                        user.setRecentDecks(recentDecks);

                        Hashtable<String,Double> rate = user.getRate();
                        rate.remove(deck.getDeckId());
                        user.setRate(rate);

                        DeckDao.deleteDeck(deck);
                        Toast.makeText(ViewCardActivity.this, "Delete successfully", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(ViewCardActivity.this, MainActivity.class);
                        startActivity(i);
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

                break;
            case R.id.saveSet:

                UserDao.addFavoriteDeck(deck.getDeckId());
                break;

            case R.id.infoSet:
                builder = new AlertDialog.Builder(this);
                LayoutInflater inflater = ViewCardActivity.this.getLayoutInflater();
                View layout = inflater.inflate(R.layout.dialog_deck_information   /*my layout here*/, null);
                builder.setView(layout);

                TextView Author = layout.findViewById(R.id.tvAuthor);
                TextView Date = layout.findViewById(R.id.tvDate);
                String userName="";
                try {
                    userName = UserDao.allUserHT.get(deck.getUid()).getUsername();
                }catch (Exception e){
                    Log.i("viewCardActivity",e.getLocalizedMessage());
                };
                Author.setText(userName);
                TextView Description = layout.findViewById(R.id.tvDescription);
                Date.setText(deck.getDate());
                if(deck.getDescriptions()!=null){
                    Description.setText(deck.getDescriptions());
                }else{
                    Description.setText("");
                }


                builder.show();
                break;

            case R.id.rateSet:
                builder = new AlertDialog.Builder(ViewCardActivity.this, R.style.AlertDialogTheme);
                inflater = ViewCardActivity.this.getLayoutInflater();
                layout = inflater.inflate(R.layout.dialog_rating_deck   /*my layout here*/, null);
                builder.setView(layout);
                RatingBar ratingBar = layout.findViewById(R.id.ratingBar);

                builder.setPositiveButton("Get Rate", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String ratingPoint = ratingBar.getRating() + "";
                        Log.i("ratingPoint", ratingPoint);
                        double score = (ratingBar.getRating() - 3 + (int) (deck.getView() / 100));
                        User rateUser = UserDao.readUserById(UserDao.getUser().getUid());
                        if (rateUser != null) {
                            rateUser.getRate().put(deck.getDeckId(), score);
                            UserDao.addUser(rateUser);
                        } else {
                            Log.i("RateUser", "Not found User ID");
                        }
                        ;
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
