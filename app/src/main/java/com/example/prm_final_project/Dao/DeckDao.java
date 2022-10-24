package com.example.prm_final_project.Dao;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.prm_final_project.Model.Deck;
import com.example.prm_final_project.Model.FavoriteDeck;
import com.example.prm_final_project.Model.RecentDeck;
import com.example.prm_final_project.callbackInterface.AllDataCallback;
import com.example.prm_final_project.callbackInterface.FirebaseCallback;
import com.example.prm_final_project.callbackInterface.RecentDeckCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class DeckDao {

    public static FirebaseDatabase rootRef =  FirebaseDatabase.getInstance();
    public static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static DatabaseReference rr ;
    public static HashMap<String,Deck> originDeck = new HashMap<>();
    public static ArrayList<Deck> originDeckList = new ArrayList<>();
    public static HashMap<String,ArrayList<String>> Hmfavorite = new HashMap<>() ;
    public static int numberDeck = 0;

    public static Hashtable<String,Deck> HmAllDeck = new Hashtable<>();


    public static void readAllDecks(FirebaseCallback callback  ,ArrayList<Deck> allDecks){
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
                originDeck.put(thisDeck.getDeckId(),thisDeck);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //1
                Deck thisDeck = changeToDeck(snapshot);

                String deckId = snapshot.getKey() ;
                Log.i("firebaseChildChange", deckId );

                for(int i =0;i<allDecks.size();i++) {
                    if(allDecks.get(i).getDeckId().equalsIgnoreCase(snapshot.getKey()) ) {
                        allDecks.set(i,thisDeck);
                        callback.onResponse(allDecks,thisDeck,1);
                        originDeck.put(thisDeck.getDeckId(),thisDeck);
                        Log.i("DeckDao","DeckChildeChange " + thisDeck.getDeckId());
                    };
                };

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
                        originDeck.remove(thisDeck.getDeckId());

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
                    Log.i("firebase",error.getMessage());
            }
        };
        rr.addChildEventListener(childEventListener);
    }

    public static void readAllDecksOnce(AllDataCallback callback){
//        FirebaseDatabase rootRef =  FirebaseDatabase.getInstance();
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
         rootRef.getReference("Decks").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot snapshot = task.getResult();
                HmAllDeck.clear();
                for(DataSnapshot ds: snapshot.getChildren()) {
                    Deck thisDeck = changeToDeck(ds);
                    HmAllDeck.put(thisDeck.getDeckId(),thisDeck);
                };
                callback.onResponse(HmAllDeck);
            }
        });
        readAllDecksHM();

    }

    public static void readAllDecksHM(){

        rootRef.getReference("Decks").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot ds, @Nullable String previousChildName) {
                //0
                Deck thisDeck = changeToDeck(ds);
                Log.i("numberDeckIncrease",thisDeck.getDeckId());
                HmAllDeck.put(thisDeck.getDeckId(),thisDeck);
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot ds, @Nullable String previousChildName) {
                Deck thisDeck = changeToDeck(ds);
                HmAllDeck.put(thisDeck.getDeckId(),thisDeck);

            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot ds) {
                //2
                Deck thisDeck = changeToDeck(ds);
                HmAllDeck.remove(thisDeck.getDeckId());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.i("DeckDao","DeckChildeMove" + snapshot.getKey());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("firebase",error.getMessage());
            }
        });


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

    public static void readRecentDeckByUser(ArrayList<RecentDeck> recentDeck ,RecentDeckCallback callback ){
//        FirebaseDatabase rootRef =  FirebaseDatabase.getInstance();
//        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = UserDao.getUser();
//        recentDeck= new ArrayList<>();
        rr = (DatabaseReference) rootRef.getReference("RecentDeck").equalTo(user.getUid()).getRef();
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot ds, @Nullable String previousChildName) {
                //0
                RecentDeck thisRecentDeck = (RecentDeck) ds.getValue(RecentDeck.class);
                    recentDeck.add(thisRecentDeck);
                    callback.onResponse(recentDeck,thisRecentDeck,1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //1
                RecentDeck thisRecentDeck = (RecentDeck) snapshot.getValue(RecentDeck.class);
//                Log.i("changeInfor",thisRecentDeck.getTimeStamp()+"|"+recentDeck.size());
                for(int i =0;i<recentDeck.size();i++) {
                    if(recentDeck.get(i).getDeckId().equalsIgnoreCase(thisRecentDeck.getDeckId()) ) {
                        recentDeck.set(i,thisRecentDeck);
                        callback.onResponse(recentDeck,thisRecentDeck,1);

                    };
                };

//                Deck thisDeck = changeToDeck(snapshot);
//                for(int i =0;i<allDecks.size();i++) {
//                    if(allDecks.get(i).getDeckId().equalsIgnoreCase(snapshot.getKey()) ) {
//                        allDecks.set(i,thisDeck);
//                        callback.onResponse(allDecks,thisDeck,1);
//                    };
//                };
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                //2
                RecentDeck thisRecentDeck = (RecentDeck) snapshot.getValue(RecentDeck.class);
                for(int i =0;i<recentDeck.size();i++) {
                    if(recentDeck.get(i).getDeckId().equalsIgnoreCase(snapshot.getKey()) ) {
                        recentDeck.remove(i);
                        callback.onResponse(recentDeck,thisRecentDeck,2);
                    };
                };
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
// Add Method
    public static void addDeck(Deck deck, FirebaseCallback callback) {
        FirebaseDatabase.getInstance().getReference("Decks").child(deck.getDeckId()).setValue(deck);
//        FirebaseDatabase.getInstance().getReference("Decks").child(deck.getDeckId()).child("view").setValue(1);
        callback.onResponse(null,deck,1);

    };


    public static void addFavoriteDeck(FavoriteDeck FvDeck) {
        FirebaseDatabase.getInstance().getReference("FavoriteDeck").child(FvDeck.getId()).setValue(FvDeck);

    };

    public static void addView(Deck deck) {
        int currentView = deck.getView();
        FirebaseDatabase.getInstance().getReference("Decks").child(deck.getDeckId()).child("view").setValue(currentView +1);
    };

    public static ArrayList<Deck> searchDeckByTitle(ArrayList<Deck> allDecks, String keywords){
        ArrayList<Deck> decks = new ArrayList<>();
        for (int i = 0; i < allDecks.size(); i++) {
            if(allDecks.get(i).getTitle().toLowerCase().contains(keywords.toLowerCase()) & allDecks.get(i).isPublic()){
                decks.add(allDecks.get(i));
            }
        }

        return decks;
    }

    public static Deck getDeckById(ArrayList<Deck> allDecks, String deckId){
        for (int i = 0; i < allDecks.size(); i++) {
            if(allDecks.get(i).getDeckId().equals(deckId)){
                return allDecks.get(i);
            }
        }
        return null;
    }

    }
