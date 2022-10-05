package com.example.prm_final_project.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm_final_project.Module.Deck;
import com.example.prm_final_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

// Khởi động ban đầu sẽ vào loading screen để load Database

public class MainActivity extends AppCompatActivity {
    private boolean isGuest = false;
    private ArrayList<Deck> allDecks = new ArrayList<>();
    private FirebaseAuth mAuth;
    FirebaseDatabase rootRef;// Hiện tại đại để mặc định là Guest

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        rootRef = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        isGuest = checkGuest();
        readAllDecks(rootRef.getReference("Decks"), new OnGetDataListener()  {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                Toast.makeText(MainActivity.this, "Load Database Success", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure() {
                Toast.makeText(MainActivity.this, "Load Database Fail", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void readAllDecks(DatabaseReference rr, final OnGetDataListener listener){

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String uid = ds.child("uid").getValue(String.class);
                    String author = ds.child("author").getValue(String.class);
                    String title = ds.child("title").getValue(String.class);
                    List<List<String>> cards = (List<List<String>>) ds.child("cards").getValue();
                    String did = ds.child("deckid").getValue(String.class);
                    Deck thisDeck = new Deck(did, uid, title, author, cards);
                    allDecks.add(thisDeck);
                }
                listener.onSuccess(dataSnapshot);
                Intent i;
                if(isGuest){
                    i = new Intent(MainActivity.this, LoginActivity.class);
                }
                else{
                    i = new Intent(MainActivity.this, HomePageActivity.class);

                }
                i.putExtra("allDecks", allDecks);
                startActivity(i);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("HomePageActivity:", "Can't fetch all decks");
            }
        };
        rr.addListenerForSingleValueEvent(eventListener);
    }

    public FirebaseAuth getmAuth(){
        mAuth = FirebaseAuth.getInstance();
        return mAuth;
    }

    public boolean checkGuest(){
        FirebaseUser user = mAuth.getInstance().getCurrentUser();
        if (user==null){
            return true;
        }
        return false;
    }
    public FirebaseUser getCurrUser(){
        mAuth = getmAuth();
        FirebaseUser user = mAuth.getCurrentUser();
        return user;
    }

    public interface OnGetDataListener {
        //this is for callbacks
        void onSuccess(DataSnapshot dataSnapshot);
        void onFailure();
    }







}