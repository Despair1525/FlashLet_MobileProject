package com.example.prm_final_project.callbackInterface;

import com.example.prm_final_project.Model.Deck;
import com.example.prm_final_project.Model.RecentDeck;

import java.util.ArrayList;

public interface RecentDeckCallback {
    void onResponse(ArrayList<RecentDeck> allDecks, RecentDeck changeDeck, int type);
}
