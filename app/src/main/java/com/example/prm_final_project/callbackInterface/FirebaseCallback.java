package com.example.prm_final_project.callbackInterface;

import com.example.prm_final_project.Model.Deck;

import java.util.ArrayList;

public interface FirebaseCallback {
        void onResponse(ArrayList<Deck> allDecks,Deck changeDeck, int type);

}

