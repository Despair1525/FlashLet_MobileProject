package com.example.prm_final_project.Dao;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.prm_final_project.callbackInterface.FirebaseCallback;
import com.example.prm_final_project.Module.Deck;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class DeckDao {


    public static void readAllDecks( FirebaseCallback callback  , ArrayList<Deck> allDecks){
        FirebaseDatabase rootRef =  FirebaseDatabase.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference rr = rootRef.getReference("Decks");
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot ds, @Nullable String previousChildName) {
                String uid = ds.child("uid").getValue(String.class);
                String author = ds.child("author").getValue(String.class);
                String title = ds.child("title").getValue(String.class);
                List<List<String>> cards = (List<List<String>>) ds.child("cards").getValue();
                String did = ds.child("deckid").getValue(String.class);
                String date = ds.child("date").getValue(String.class);
                boolean isPublic = ds.child("public").getValue(Boolean.class);
                Deck thisDeck = new Deck(did, uid, title, author,date,isPublic ,cards);
                allDecks.add(thisDeck);
//                homeDeckAdap.notifyDataSetChanged();
//                DeckListAdapter.notifyDataSetChanged();
                callback.onResponse(allDecks);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        rr.addChildEventListener(childEventListener);
    }
}
