package com.example.prm_final_project.Dao;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.prm_final_project.callbackInterface.FirebaseCallback;
import com.example.prm_final_project.Model.Deck;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class DeckDao {

    public static FirebaseDatabase rootRef =  FirebaseDatabase.getInstance();
    public static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static DatabaseReference rr ;

    public static void readAllDecks( FirebaseCallback callback  , ArrayList<Deck> allDecks){
//        FirebaseDatabase rootRef =  FirebaseDatabase.getInstance();
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        rr = rootRef.getReference("Decks");
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot ds, @Nullable String previousChildName) {
              //0
                Deck thisDeck = changeToDeck(ds);
                allDecks.add(thisDeck);
//                homeDeckAdap.notifyDataSetChanged();
//                DeckListAdapter.notifyDataSetChanged();
                callback.onResponse(allDecks,thisDeck,0);
                Log.i("DeckDao","DeckChildeAddd" + ds.getKey());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //1
                Deck thisDeck = changeToDeck(snapshot);
                for(int i =0;i<allDecks.size();i++) {
                    if(allDecks.get(i).getDeckId().equalsIgnoreCase(snapshot.getKey()) ) {
                        allDecks.set(i,thisDeck);
                        callback.onResponse(allDecks,thisDeck,1);
                    };
                };
                Log.i("DeckDao","DeckChildeChange" + snapshot.getKey());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                //2
                String id = snapshot.getKey();
                for(int i =0;i<allDecks.size();i++) {
                    if(allDecks.get(i).getDeckId().equalsIgnoreCase(id) ) {
                        Deck thisDeck = allDecks.get(i);
                        allDecks.remove(i);
                        callback.onResponse(allDecks,thisDeck,2);
                    };
                };
                Log.i("DeckDao","DeckChildeRemove" + snapshot.getKey());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.i("DeckDao","DeckChildeMove" + snapshot.getKey());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        rr.addChildEventListener(childEventListener);
    }
    public static Deck changeToDeck(@NonNull DataSnapshot ds){
        String uid = ds.child("uid").getValue(String.class);
        String author = ds.child("author").getValue(String.class);
        String title = ds.child("title").getValue(String.class);
        List<List<String>> cards = (List<List<String>>) ds.child("cards").getValue();
        String did = ds.child("deckId").getValue(String.class);
        String date = ds.child("date").getValue(String.class);
        String descriptions = ds.child("descriptions").getValue(String.class);
        int view = ds.child("view").getValue(Integer.class);
        boolean isPublic = ds.child("public").getValue(Boolean.class);
        Deck thisDeck = new Deck(did, uid, title,descriptions, author,date,isPublic ,view,cards);
        return thisDeck;
    };

    public static void addDeck(Deck deck, FirebaseCallback callback) {
        FirebaseDatabase.getInstance().getReference("Decks").child(deck.getDeckId()).setValue(deck);
//        FirebaseDatabase.getInstance().getReference("Decks").child(deck.getDeckId()).child("view").setValue(1);

        callback.onResponse(null,deck,1);
    };
    public static void addView(Deck deck) {
        int currentView = deck.getView();
        FirebaseDatabase.getInstance().getReference("Decks").child(deck.getDeckId()).child("view").setValue(currentView +1);
    };

    }
