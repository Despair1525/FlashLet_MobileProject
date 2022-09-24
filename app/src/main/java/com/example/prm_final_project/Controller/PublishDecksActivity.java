package com.example.prm_final_project.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.prm_final_project.Module.Deck;
import com.example.prm_final_project.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PublishDecksActivity extends AppCompatActivity implements View.OnClickListener {

    ArrayList<Deck> allDecks = new ArrayList<>();
    ArrayList<String> myDeckKeys = new ArrayList<>();
    Map<String,Deck> keyedDecks = new HashMap<>();
    ArrayList<Deck> personalDecks = new ArrayList<>();
    private ListView lvDecks;
    private boolean isGuest = true;
    private boolean inPublic = true;

//    FirebaseDatabase rootRef;
//    FirebaseAuth mAuth;

    private ImageView addDeck;
    private TextView myDecks, publicDecks, logout;
    private SearchView svDecks;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_decks);


/////// Connect with Database
//      Lấy dữ liệu ra ở đây
//      1. Check là Guest
//      2. Lấy dữ liệu
/////////////////////////////
        svDecks = (SearchView) findViewById(R.id.svSearchPublic);
        myDecks = (TextView) findViewById(R.id.tvMyDecks);
        publicDecks = (TextView) findViewById(R.id.tvPublicDecks);
        logout = (TextView) findViewById(R.id.tvLogout);
        addDeck = (ImageView)findViewById(R.id.abPlusPublic);
        lvDecks = (ListView) findViewById(R.id.lvDecksPublic);

        logout.setOnClickListener(this);

///////////////////////

    }

    @Override
    public void onClick(View view) {
        if(view == logout) {
            Intent i = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(i);

        };
    }
}