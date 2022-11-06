package com.example.prm_final_project.Dao;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.prm_final_project.Model.Deck;
import com.example.prm_final_project.Model.RecentDeck;
import com.example.prm_final_project.Util.Methods;
import com.example.prm_final_project.callbackInterface.AllDataCallback;
import com.example.prm_final_project.callbackInterface.FirebaseCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class DeckDao {

    public static FirebaseDatabase rootRef =  FirebaseDatabase.getInstance();
    public static Hashtable<String,Deck> HmAllDeck = new Hashtable<>();

    public static void readAllDecksOnce(AllDataCallback callback){
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
                Deck thisDeck = ds.getValue(Deck.class);
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
        String title = ds.child("title").getValue(String.class);
        List<List<String>> cards = (List<List<String>>) ds.child("cards").getValue();
        String did = ds.child("deckId").getValue(String.class);
        String date = ds.child("date").getValue(String.class);
        String descriptions = ds.child("descriptions").getValue(String.class);
        int view = ds.child("view").getValue(Integer.class);
        boolean isPublic = ds.child("public").getValue(Boolean.class);
        Deck thisDeck = new Deck(did, uid, title,descriptions, date,isPublic ,view,cards);
        return thisDeck;
    };

// Add Method
    public static void addDeck(Deck deck, FirebaseCallback callback) {
        FirebaseDatabase.getInstance().getReference("Decks").child(deck.getDeckId()).setValue(deck);
//        FirebaseDatabase.getInstance().getReference("Decks").child(deck.getDeckId()).child("view").setValue(1);
        callback.onResponse(null,deck,1);

    };

    public static void addView(Deck deck) {
        int currentView = deck.getView();
        FirebaseDatabase.getInstance().getReference("Decks").child(deck.getDeckId()).child("view").setValue(currentView +1);
    };

    public static void deleteDeck(Deck deck){
        FirebaseDatabase.getInstance().getReference("Decks").child(deck.getDeckId()).removeValue();
    }


    public static ArrayList<Deck> searchDeckByTitle(ArrayList<Deck> allDecks, String keywords){
        ArrayList<Deck> decks = new ArrayList<>();
        for (int i = 0; i < allDecks.size(); i++) {
            if(allDecks.get(i).getTitle().toLowerCase().contains(keywords.toLowerCase()) & allDecks.get(i).isPublic()){
                decks.add(allDecks.get(i));
            }
        }
        return decks;
    }

    public static Deck getDeckById(Hashtable<String,Deck> allDecks, String deckId, Boolean isPublic){
        Deck d = allDecks.get(deckId);
        if(d != null && isPublic){
            if(d.isPublic()){
                return d;
            } else {
                return null;
            }
        }
        return d;
    }

    public static ArrayList<Deck> getDecksByListIds(Hashtable<String, Deck> allDecks, ArrayList<String> deckIds, Boolean isPublic){
        ArrayList<Deck> results = new ArrayList<>();
        for (int i = 0; i < deckIds.size(); i++) {
            Deck d = getDeckById(allDecks, deckIds.get(i), isPublic);
            if(d != null){
                results.add(d);
            }
        }
        return results;
    }

public static ArrayList<RecentDeck> createRecentDeck(String deckId,ArrayList<RecentDeck> userRecent){
    RecentDeck newRecent = new RecentDeck(deckId, Methods.getTimeLong());
    int lastRecent = 0;
    for(int position = 0; position < userRecent.size() ; position++) {
        if(userRecent.get(position).getTimeStamp()  < userRecent.get(lastRecent).getTimeStamp() ) {
            lastRecent = position;
        };
        if(newRecent.getDeckId().equals(userRecent.get(position).getDeckId())) {
            userRecent.set(position,newRecent );
        };
    };
    userRecent.add(newRecent);
    if(userRecent.size() > 10) userRecent.remove(lastRecent);
        return userRecent;
};
}
