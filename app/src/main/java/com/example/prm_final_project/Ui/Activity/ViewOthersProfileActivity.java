package com.example.prm_final_project.Ui.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prm_final_project.Adapter.HomeDeckListAdapter;
import com.example.prm_final_project.Adapter.UserListAdapter;
import com.example.prm_final_project.Dao.DeckDao;
import com.example.prm_final_project.Dao.UserDao;
import com.example.prm_final_project.Model.Deck;
import com.example.prm_final_project.Model.RecentDeck;
import com.example.prm_final_project.Model.User;
import com.example.prm_final_project.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class ViewOthersProfileActivity extends AppCompatActivity {
    private ImageView avatarImg;
    private TextView userName, notificationText;
    private RecyclerView deckRecyclerView;
    private TabLayout profileTabLayout;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_others_profile);
        init();

        Intent intent = getIntent();
        String userId = intent.getStringExtra("currentUser");
        currentUser = UserDao.getUserById(userId);

        // set avatar
        Uri imgUri;
        if(currentUser.getAvatar() != null && currentUser.getAvatar().startsWith("content")) {
            imgUri = Uri.parse(currentUser.getAvatar());
            try {
                avatarImg.setImageURI(imgUri);
            } catch (Exception e){
                avatarImg.setImageResource(R.drawable.default_avatar);
            }
        }

        userName.setText(currentUser.getUsername());

        // tab layout
        profileTabLayout.addTab(profileTabLayout.newTab().setText("All Public Decks"));

        // recycler view
        ArrayList<String> deckIds = getListDeckIds(currentUser);
        ArrayList<Deck> decks = DeckDao.getDecksByListIds(DeckDao.HmAllDeck, deckIds, true);

        if(decks.isEmpty()){
            notificationText.setText("There is no public decks!");
            notificationText.setVisibility(View.VISIBLE);
        } else {
            HomeDeckListAdapter homeDeckListAdapter = new HomeDeckListAdapter(this, decks);
            deckRecyclerView.setAdapter(homeDeckListAdapter);
            deckRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void init(){
        avatarImg = findViewById(R.id.profile_avt);
        userName = findViewById(R.id.profile_user_name);
        notificationText = findViewById(R.id.profile_text_notification);
        deckRecyclerView = findViewById(R.id.profile_recycler_deck_view);
        profileTabLayout = findViewById(R.id.profile_tab_layout);
    }

    private ArrayList<String> getListDeckIds(User user){
        ArrayList<String> deckIds = new ArrayList<>();
        for (int i = 0; i < user.getMyDeck().size(); i++) {
            RecentDeck recentDeck = user.getMyDeck().get(i);
            deckIds.add(recentDeck.getDeckId());
        }

        return deckIds;
    }
}